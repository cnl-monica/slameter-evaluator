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
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import module.AbstractModule;
import module.MaxValues;
import module.MaxValues.User;
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
public class TopDownloader extends AbstractModule{
    private static CalculateTransferedData calculate= new CalculateTransferedData();
    private static TopDownloader topDownloaderInstance;
    private MaxValues array;
    
    public static TopDownloader getInstance(){
        if(topDownloaderInstance==null){
            topDownloaderInstance = new TopDownloader();
            return topDownloaderInstance;
        }else{
            return topDownloaderInstance;
        }
    }
    
    private TopDownloader(){ 
        setPoolRequest(new PoolRequest());
        setJedis(JedisConnection.getConnection());      
        setPrepareStatement(new PrepareStatement());
        setRequiredAttribute("time","exporter_id");
        setOptionalAttribute("name","destination_ip");
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
                    Logger.getLogger(TopDownloader.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                //System.out.println("Prijimam data pre module "+getModuleName());
            
                JSONObject obj;
            try {
                Long startT;
                Long endT;

                startT=new Date().getTime();
                //System.out.println("TopDownloader "+startT);
                array = new MaxValues(5);
                DatabaseConnection db = new DatabaseConnection(); 
                db.connect(Config.dbHost,Config.dbPort,Config.dbName);
                PoolRequest.Request req =getPoolRequest().get();
                obj= new JSONObject(req.request);
                QueryBuilder query =getPrepareStatement().getStatement(obj,this);
                
                //v pripade ze nejaky atribuch chýba
                if(query==null){
                    
                    getJedis().publish("ResponseTopDownloader",ModuleResponse.sentData("TopDownloader", "error", "Some of required attribute is missing").toString());
                }else{
                JSONArray time =obj.getJSONArray("time");
                calculate.setStartInterval(new Long(time.getLong(0)));
                calculate.setEndInterval(new Long(time.getLong(1)));
                    
                    //inak namapujeme query a vyberieme potrebne polozky
                    DBObject condition = new  BasicDBObject("$match",query.get());
                    DBObject select= new  BasicDBObject("$project",new BasicDBObject("flowId",1).append("flowStartMilliseconds", 1).append("flowEndMilliseconds", 1).append("octetDeltaCount", 1).append("destinationIPv4Address", 1));
                    
                    AggregationOutput aggOut=db.getDatabase().getCollection("records_main").aggregate(condition,select);
                     db.disconnect();
                    
                    ArrayList<DBObject> outList = (ArrayList<DBObject>) aggOut.results();
                    //System.out.println("TopUploader zaznam "+outList.size());
                    ArrayList<String> sourceIP = new ArrayList<>();   
                    Hashtable<String,ArrayList<DBObject>> data = new Hashtable<String,ArrayList<DBObject>>();
                    for (DBObject object : outList) {
                        String ipcka = InetAddress.getByAddress((byte[])object.get("destinationIPv4Address")).toString().substring(1, InetAddress.getByAddress((byte[])object.get("destinationIPv4Address")).toString().length());
                        //System.out.println(ipcka);
                       
                        if(data.containsKey(ipcka)){
                            ArrayList<DBObject> oldData =data.get(ipcka);
                            oldData.add(object);
                            data.put(ipcka, oldData);
                        }else{
                            ArrayList<DBObject> dataIpcky = new ArrayList<DBObject>();
                            dataIpcky.add(object);
                            data.put(ipcka, dataIpcky);
                            sourceIP.add(ipcka);
                        }
                    }
                    
                    for(String ipcka:sourceIP){
                        calculate.processData(data.get(ipcka));
                       // System.out.println(ipcka +" "+calculate.getResutValue());
                        array.push(new MaxValues.User(calculate.getResutValue(),ipcka));
                    }
                    
                    //System.out.println("|ResponseTopDownloader   | Dáta"+array.toArray().toString());
                    getJedis().publish("ResponseTopDownloader",ModuleResponse.sentData("TopDownloader", "ok", array.toArray().toString()).toString());
                }
                } catch (JSONException | UnknownHostException ex) {
                Logger.getLogger(TopDownloader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
    }
    
    @Override
    public String getModuleName() {
        return "TopDownloader";
    }
    
}
