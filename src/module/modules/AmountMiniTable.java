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
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import module.AbstractModule;
import module.ModuleResponse;
import module.PoolRequest;
import module.PrepareStatement;
import module.calculation.CalculateTransferedData;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


/**
 *
 * @author esperian
 */
public class AmountMiniTable extends AbstractModule{
    private static CalculateTransferedData calculate;
    private static AmountMiniTable AmountMiniTableInstance;
    
    
    public static AmountMiniTable getInstance(){
        
        if(AmountMiniTableInstance==null){
            AmountMiniTableInstance = new AmountMiniTable();
            calculate= new CalculateTransferedData();
            return AmountMiniTableInstance;
        }else{
            
            return AmountMiniTableInstance;
        }
    }
    
    private AmountMiniTable(){ 
        setPoolRequest(new PoolRequest());
        setJedis(JedisConnection.getConnection());      
        setPrepareStatement(new PrepareStatement());
        setRequiredAttribute("time","exporter_id");
        setOptionalAttribute("name","destination_ip","source_ip");
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
                    Logger.getLogger(AmountMiniTable.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                //System.out.println("Prijimam data pre module "+getModuleName());
            
    try {
        Long startT;
        Long endT;
        
        startT=new Date().getTime();
        //System.out.println("AmountMiniTable "+startT);
        DatabaseConnection db = new DatabaseConnection(); 
        db.connect(Config.dbHost,Config.dbPort,Config.dbName);

        JSONObject jsonQueryUP;
        JSONObject jsonQueryDOWN;
        JSONObject jsonInpuut = new JSONObject(getPoolRequest().get().request);

        if(jsonInpuut.has("time") && jsonInpuut.has("exporter_id") && jsonInpuut.has("client_ip")){
            String ip  = jsonInpuut.getString("client_ip");

            JSONObject o=new JSONObject();
            o.put("type", "array");
            o.put("ips", new JSONArray().put(ip));    

            JSONObject responseData = new JSONObject();
            JSONArray time =jsonInpuut.getJSONArray("time");
            calculate.setStartInterval(new Long(time.getLong(0)));
            calculate.setEndInterval(new Long(time.getLong(1)));

            jsonQueryUP =new JSONObject();
            jsonQueryUP.put("exporter_id", jsonInpuut.get("exporter_id"));
            jsonQueryUP.put("time",time);
            jsonQueryUP.put("source_ip", o);
                  
            QueryBuilder query =getPrepareStatement().getStatement(jsonQueryUP,this);

            db.connect(Config.dbHost,Config.dbPort,Config.dbName);
            DBObject condition = new  BasicDBObject("$match",query.get());
            DBObject select= new  BasicDBObject("$project",new BasicDBObject("flowId",1).append("flowStartMilliseconds", 1).append("flowEndMilliseconds", 1).append("octetDeltaCount", 1).append("rid", 1));
            
            
            
            AggregationOutput aggOut=db.getDatabase().getCollection("records_main").aggregate(condition,select);
            Iterator<DBObject> result= aggOut.results().iterator();
            //System.out.println("1---"+(((Collection<DBObject>) aggOut.results()).size()));
            calculate.processData(result);
            responseData.put("amountUpload", calculate.getResutValue());
            
            //====================================
            jsonQueryDOWN=new JSONObject();
            jsonQueryDOWN.put("exporter_id", jsonInpuut.get("exporter_id"));
            jsonQueryDOWN.put("time",time);
            jsonQueryDOWN.put("destination_ip", o);

            query =getPrepareStatement().getStatement(jsonQueryDOWN,this);
                    
            condition = new  BasicDBObject("$match",query.get());
            select= new  BasicDBObject("$project",new BasicDBObject("flowId",1).append("flowStartMilliseconds", 1).append("flowEndMilliseconds", 1).append("octetDeltaCount", 1).append("rid", 1));
            
            aggOut=db.getDatabase().getCollection("records_main").aggregate(condition,select);
            result= aggOut.results().iterator();
            //System.out.println("2---"+(((Collection<DBObject>) aggOut.results()).size()));
            
            db.disconnect();
            
            calculate.processData(result);
            responseData.put("amountDownload", calculate.getResutValue());
            getJedis().publish("ResponseAmountMiniTable",ModuleResponse.sentData("AmountMiniTable", "ok", ""+responseData).toString());
            endT=new Date().getTime();
            //System.out.println("AmountMiniTable "+(endT-startT));
            
            //System.out.println("|ResponseAmountMiniTable   | Dáta"+responseData.toString());
            
        }else{
            getJedis().publish("ResponseAmountMiniTable",ModuleResponse.sentData("AmountMiniTable", "error", "Some of required attribute is missing").toString());
        }

    } catch (JSONException | UnknownHostException ex) {
        Logger.getLogger(AmountMiniTable.class.getName()).log(Level.SEVERE, null, ex);
    }
                
                
        }        
    }
    
    @Override
    public String getModuleName() {
            return "AmountMiniTable";
    }
    
}
