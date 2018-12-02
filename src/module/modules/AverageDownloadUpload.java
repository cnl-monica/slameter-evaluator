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
public class AverageDownloadUpload extends AbstractModule{
    private static AverageDownloadUpload AverageOfTransferredDataInstance;
    private static CalculateTransferedData calculate= new CalculateTransferedData();
    public static AverageDownloadUpload getInstance(){
        
        
        if(AverageOfTransferredDataInstance==null){
            AverageOfTransferredDataInstance = new AverageDownloadUpload();
            return AverageOfTransferredDataInstance;
        }else{
            return AverageOfTransferredDataInstance;
        }
    }
    
    private AverageDownloadUpload(){ 
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
                    Logger.getLogger(AverageDownloadUpload.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                //System.out.println("Prijimam data pre module "+getModuleName());
            
                JSONObject obj;
            try {
                DatabaseConnection db = new DatabaseConnection(); 
                db.connect(Config.dbHost,Config.dbPort,Config.dbName);
                PoolRequest.Request req =getPoolRequest().get();
                obj= new JSONObject(req.request);
                
                QueryBuilder query =getPrepareStatement().getStatement(obj,this);
                
                //v pripade ze nejaky atribuch chýba
                if(query==null){
                    getJedis().publish("ResponseAverageDownloadUpload",ModuleResponse.sentData("AverageDownloadUpload", "error", "Some of required attribute is missing").toString());
                    //throw new EvaluateException("Some of required attribute is missing");
                }else{                    
                JSONArray time =obj.getJSONArray("time");
                calculate.setStartInterval(time.getLong(0));
                calculate.setEndInterval(time.getLong(1));
                    
                    //inak namapujeme query a vyberieme potrebne polozky
                    DBObject condition = new  BasicDBObject("$match",query.get());
                    DBObject select= new  BasicDBObject("$project",new BasicDBObject("flowId",1).append("flowStartMilliseconds", 1).append("flowEndMilliseconds", 1).append("octetDeltaCount", 1).append("rid", 1));         
                    
                    AggregationOutput aggOut=db.getDatabase().getCollection("records_main").aggregate(condition,select);
                    Iterator<DBObject> result= aggOut.results().iterator();
                    db.disconnect();

                    calculate.processData(result);
                    
                    Double finalResult;
                    if(calculate.getTimeIntervalInSec()<=1){
                        finalResult=calculate.getResutValue();
                    }else{
                        //System.out.println(calculate.getResutValue() +"----"+calculate.getTimeIntervalInSec());
                        finalResult=calculate.getResutValue() /((double) calculate.getTimeIntervalInSec());
                    }
                    getJedis().publish("ResponseAverageDownloadUpload",ModuleResponse.sentDataDouble("AverageDownloadUpload", "ok", finalResult).toString());
                    //System.out.println("|ResponseAverageDownloadUpload   | Dáta"+finalResult);
                }
                } catch (JSONException | UnknownHostException ex) {
                Logger.getLogger(AverageDownloadUpload.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
    }
    
    @Override
    public String getModuleName() {
           return "AverageDownloadUpload";
    }
    
}
