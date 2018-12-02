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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
public class HistoryTable extends AbstractModule{
    private static HistoryTable HistoryTableInstance;
    private static CalculateTransferedData calculate= new CalculateTransferedData();
    public static HistoryTable getInstance(){
        
        
        if(HistoryTableInstance==null){
            HistoryTableInstance = new HistoryTable();
            return HistoryTableInstance;
        }else{
            return HistoryTableInstance;
        }
    }
    
    private HistoryTable(){ 
        setPoolRequest(new PoolRequest());
        setJedis(JedisConnection.getConnection());      
        setPrepareStatement(new PrepareStatement());
        setRequiredAttribute("time","exporter_id");
        setOptionalAttribute("name","destination_ip","source_ip","client_ip");
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
                    Logger.getLogger(HistoryTable.class.getName()).log(Level.SEVERE, null, ex);
                }
                               
                        
                JSONArray upMaxObject = new JSONArray();
                JSONArray downMaxObject = new JSONArray();
                JSONArray upAverageObject = new JSONArray();
                JSONArray downAverageObject = new JSONArray();
                
                JSONObject obje = new JSONObject();
            try {
                obje.put("maximumDownload", downMaxObject);
                obje.put("averageDownload", downAverageObject);
                obje.put("maximumUpload", upMaxObject);
                obje.put("averageUpload", upAverageObject);
            } catch (JSONException ex) {
                Logger.getLogger(HistoryTable.class.getName()).log(Level.SEVERE, null, ex);
            }
                
                //System.out.println("Prijimam data pre module "+getModuleName());
            
                JSONObject obj;
            try {
                Long startT;
                Long endT;

                startT=new Date().getTime();
                DatabaseConnection db = new DatabaseConnection(); 
                db.connect(Config.dbHost,Config.dbPort,Config.dbName);

                obj= new JSONObject(getPoolRequest().get().request);

                QueryBuilder q =getPrepareStatement().getStatement(obj,this);
                
                
                if(q==null || obj.has("client_ip")==false){
                    getJedis().publish("ResponseHistoryTable",ModuleResponse.sentData("HistoryTable", "error", "Some of required attribute is missing").toString());
                }else{                
                getPrepareStatement().getStatement(obj,this);
                JSONArray time =obj.getJSONArray("time");
                DateFormat dfDateOnly = new SimpleDateFormat("d.M.yyyy");
                
                String startDateString = dfDateOnly.format(new Long(time.getLong(0)));
                String endDateString = dfDateOnly.format(new Long(time.getLong(1)));
                
                long numberOfDays = 0;
                JSONObject jsonQueryUP;
                JSONObject jsonQueryDOWN;
                try {
                     numberOfDays = 1 + ((dfDateOnly.parse(endDateString).getTime() - dfDateOnly.parse(startDateString).getTime()) / (1000 * 60 * 60 * 24));
                } catch (ParseException ex) {
                    Logger.getLogger(HistoryTable.class.getName()).log(Level.SEVERE, null, ex);
                }
                    //System.out.println("pocet dni "+numberOfDays);
                    for (int i = 0; i < numberOfDays; i++) {

                    long startMillis = dfDateOnly.parse(startDateString).getTime() + i * (1000 * 60 * 60 * 24);
                    long endMillis = startMillis + 86399999l;
                    
                    
                    calculate.setStartInterval(startMillis);
                    calculate.setEndInterval(endMillis);
                    jsonQueryUP =new JSONObject();
                    jsonQueryDOWN = new JSONObject();
                    
                    JSONArray timeNew = new JSONArray();
                    timeNew.put(startMillis);
                    timeNew.put(endMillis);
                    
                    jsonQueryUP.put("time", timeNew);
                    jsonQueryUP.put("exporter_id", obj.get("exporter_id"));
                    JSONObject o=new JSONObject();
                    o.put("type", "array");
                    o.put("ips", new JSONArray().put(obj.get("client_ip"))); 
                    jsonQueryUP.put("source_ip", o);
                    //-------------------------------
                    jsonQueryDOWN.put("time", timeNew);
                    jsonQueryDOWN.put("exporter_id", obj.get("exporter_id"));
                    JSONObject o1=new JSONObject();
                    o1.put("type", "array");
                    o1.put("ips", new JSONArray().put(obj.get("client_ip"))); 
                    jsonQueryDOWN.put("destination_ip", o1);
                   
                    QueryBuilder query =getPrepareStatement().getStatement(jsonQueryUP,this);
                    DBObject condition = new  BasicDBObject("$match",query.get());
                    DBObject select= new  BasicDBObject("$project",new BasicDBObject("flowId",1).append("flowStartMilliseconds", 1).append("flowEndMilliseconds", 1).append("octetDeltaCount", 1));
                    
                    AggregationOutput aggOut=db.getDatabase().getCollection("records_main").aggregate(condition,select);
                    Iterator<DBObject> result= aggOut.results().iterator();
                    
                    calculate.processData(result);
                    upAverageObject.put(new JSONArray().put(calculate.getStartInterval()).put(calculate.getResutValue()/((double) calculate.getTimeIntervalInSec())));
                    calculate.processDataPerSec2(aggOut);
                    upMaxObject.put(new JSONArray().put(calculate.getStartInterval()).put(calculate.getResutValue()));
                    
                    QueryBuilder query2 =getPrepareStatement().getStatement(jsonQueryDOWN,this);
                    DBObject condition2 = new  BasicDBObject("$match",query2.get());
                    DBObject select2= new  BasicDBObject("$project",new BasicDBObject("flowId",1).append("flowStartMilliseconds", 1).append("flowEndMilliseconds", 1).append("octetDeltaCount", 1));
                    
                    AggregationOutput aggOut2=db.getDatabase().getCollection("records_main").aggregate(condition2,select2);
                    Iterator<DBObject> result2= aggOut2.results().iterator();
                    
                    calculate.processData(result2);
                    downAverageObject.put(new JSONArray().put(calculate.getStartInterval()).put((int)(calculate.getResutValue()/((double) calculate.getTimeIntervalInSec()))));
                    calculate.processDataPerSec2(aggOut2);
                    downMaxObject.put(new JSONArray().put(calculate.getStartInterval()).put(calculate.getResutValue()));
                    }
                    
                    //System.out.println("|ResponseHistoryTable   | Dáta"+obje.toString());
                    //String s = "{name: HistoryTable, status:ok,response: {maximumDownload:[[1396994400000, 789],[1397080800000, 987]],averageDownload:[[1396994400000, 123],[1397080800000, 456]],maximumUpload:[[1396994400000, 888],[1397080800000, 777]],averageUpload:[[1396994400000, 321 ],[1397080800000, 654]]}}";
                    //getJedis().publish("ResponseHistoryTable",s);
                    //System.out.println("- * "+ModuleResponse.sentData("HistoryTable", "ok", obje.toString()).toString());
                    //getJedis().publish("ResponseAmountMiniTable",new JSONObject(s).toString());
                    getJedis().publish("ResponseHistoryTable",ModuleResponse.sentDataJsonObject("HistoryTable", "ok", obje).toString());
                    } 
                endT=new Date().getTime();
                } catch (JSONException | UnknownHostException | ParseException ex) {
                Logger.getLogger(HistoryTable.class.getName()).log(Level.SEVERE, null, ex);
            }   
        }        
    }
    
    @Override
    public String getModuleName() {
        
        
        return "HistoryTable";
    }
    
}
