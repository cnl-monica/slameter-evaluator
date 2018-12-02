/*  Copyright (C) 2015 MONICA Research Group / TUKE 
*  2015 Pavol Benko
*
* This file is part of Evaulatorik.
*
* Evaulatorik is free software; you can redistribute it and/or modify
* it under the terms of the GNU General Public License as published by
* the Free Software Foundation; either version 3 of the License, or
* (at your option) any later version.

* Evaulatorik is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU General Public License for more details.

* You should have received a copy of the GNU General Public License
* along with Evaulatorik; If not, see <http://www.gnu.org/licenses/>.
*/

package module.modules;

import com.mongodb.AggregationOutput;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import connctions.DatabaseConnection;
import connctions.JedisConnection;
import evaluatorik.Config;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import module.AbstractModule;
import module.ModuleResponse;
import module.PoolRequest;
import module.PrepareStatement;
import module.calculation.CalculateTransferedDataPacket;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author esperian
 */
public class BandwidthHistoryTrendPacket extends AbstractModule{
    private static BandwidthHistoryTrendPacket BandwidthHistoryTrendPacketInstance;
    private static CalculateTransferedDataPacket calculate= new CalculateTransferedDataPacket();
    public static BandwidthHistoryTrendPacket getInstance(){
        
        
        if(BandwidthHistoryTrendPacketInstance==null){
            BandwidthHistoryTrendPacketInstance = new BandwidthHistoryTrendPacket();
            return BandwidthHistoryTrendPacketInstance;
        }else{
            
            return BandwidthHistoryTrendPacketInstance;
        }
    }
    
    private BandwidthHistoryTrendPacket(){ 
        setPoolRequest(new PoolRequest());
        setJedis(JedisConnection.getConnection());      
        setPrepareStatement(new PrepareStatement());
        setRequiredAttribute("time","exporter_id");
        setOptionalAttribute("name","client_ip","host_ip","source_ip","destination_ip","host_port","source_port","destination_port" );

    }
    
    @Override
    public void run() {

        while(true){
                try {    
                    if(this.getPoolRequest().isEmpty()){
                        log.log(Level.INFO, "zastavujem module {0}", getModuleName());
                        synchronized(this){
                            this.wait();
                        } 
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(BandwidthHistoryTrend.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                //System.out.println("Prijimam data pre module "+getModuleName());
                JSONObject obj;
                try {
                Long startT;
                Long endT;

                startT=new Date().getTime();
                DatabaseConnection db = new DatabaseConnection();   
                obj= new JSONObject(getPoolRequest().get().request);
                
                QueryBuilder query =getPrepareStatement().getStatement(obj,this);
                if(query==null){
                    getJedis().publish("ResponseBandwidthHistoryTrendPacket",ModuleResponse.sentData("BandwidthHistoryTrendPacket", "error", "Some of required attribute is missing").toString());
                }else{
                
                db.connect(Config.dbHost,Config.dbPort,Config.dbName);
                List<Long> intervals = getIntervals(query,db);
                Collections.sort(intervals);
                //Enumeration<Long[]> keys = intervals.keys();
                JSONArray finalResult = new JSONArray();
                
                query =getPrepareStatement().getStatement(obj,this);
                DBObject condition = new  BasicDBObject("$match",query.get());
                DBObject select= new  BasicDBObject("$project",new BasicDBObject("flowId",1).append("flowStartMilliseconds", 1).append("flowEndMilliseconds", 1).append("packetDeltaCount", 1));
                    
                DBObject sortFields = new BasicDBObject("packetDeltaCount", -1);
                DBObject sort = new BasicDBObject("$sort", sortFields );
                AggregationOutput aggOut=db.getDatabase().getCollection("records_main").aggregate(condition,select,sort);
                
                for(int i=0; i<intervals.size(); i++){
                  
                    calculate.setStartInterval(intervals.get(i));
                    calculate.setEndInterval(intervals.get(i)+999);
                    
                    
                    calculate.processData(aggOut);
                    finalResult.put(new JSONArray().put(intervals.get(i)).put(calculate.getResutValue()));
                }
                getJedis().publish("ResponseBandwidthHistoryTrendPacket",ModuleResponse.sentDataJsonArray("BandwidthHistoryTrendPacket", "ok", finalResult).toString());
                //System.out.println("|ResponseBandwidthHistoryTrendPacket   | Dáta"+finalResult);
                }
                endT=new Date().getTime();
                } catch (JSONException | UnknownHostException ex) {
                Logger.getLogger(BandwidthHistoryTrend.class.getName()).log(Level.SEVERE, null, ex);
            }
        }      
    }
    
        public List<Long> getIntervals(QueryBuilder guery,DatabaseConnection db){
        Hashtable<Long,Double> table = new Hashtable<Long,Double>();
        List<Long> table2 = new ArrayList<Long>();
        DBObject condition = new  BasicDBObject("$match",guery.get());
        DBObject select= new  BasicDBObject("$project",new BasicDBObject("flowId",1).append("flowStartMilliseconds", 1).append("flowEndMilliseconds", 1).append("packetDeltaCount", 1));
        
        DBObject sortFields = new BasicDBObject("flowStartMilliseconds", 1);
        DBObject sort = new BasicDBObject("$sort", sortFields );
        
        AggregationOutput aggOut=db.getDatabase().getCollection("records_main").aggregate(condition,select,sort);
        Iterator<DBObject> result= aggOut.results().iterator();
        int i=0;
        while(result.hasNext()){
           DBObject oo= result.next();
           long s = (long) oo.get("flowStartMilliseconds");
           long e = (long) oo.get("flowEndMilliseconds");
           
           if(!table.containsKey(s)){
               table.put(s, 0.0);
               table2.add(s);
               i++;
           }
           if(!table.containsKey(e)){
               table.put(e, 0.0);
               table2.add(e);
               i++;
           }
        }
        
        return table2;
    }
    
    @Override
    public String getModuleName() {
        return "BandwidthHistoryTrendPacket";
    }
    
}
