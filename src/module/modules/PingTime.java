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
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.QueryBuilder;
import connctions.DatabaseConnection;
import connctions.JedisConnection;
import evaluatorik.Config;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import module.AbstractModule;
import module.ModuleResponse;
import module.PoolRequest;
import module.PrepareStatement;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author esperian
 */
public class PingTime extends AbstractModule{
    private static PingTime PingTimeInstance;
    
    public static PingTime getInstance(){
        
        
        if(PingTimeInstance==null){
            PingTimeInstance = new PingTime();
            return PingTimeInstance;
        }else{
            
            return PingTimeInstance;
        }
    }
    
    private PingTime(){ 
        setPoolRequest(new PoolRequest());
        setJedis(JedisConnection.getConnection());      
        setPrepareStatement(new PrepareStatement());
        setRequiredAttribute("time","exporter_id");
        setOptionalAttribute("name");

    }
    
    @Override
    public void run() {
        
        while(true){
                try {    
                    if(getPoolRequest().isEmpty()){
                        log.log(Level.INFO, "zastavujem module {0}", getModuleName());
                        synchronized(this){
                            this.wait();
                        } 
                    }
                } catch (InterruptedException ex) {
                    Logger.getLogger(PingTime.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                //System.out.println("Prijimam data pre module "+getModuleName());
                
                JSONObject obj;
            try {
                Long startT;
                Long endT;

                startT=new Date().getTime();
                //System.out.println("PingTime "+startT);
                obj= new JSONObject(getPoolRequest().get().request);
                
                
                QueryBuilder query =getPrepareStatement().getStatement(obj,this);
                if(query==null){
                    getJedis().publish("ResponsePingTime",ModuleResponse.sentData("PingTime", "error", "Some of required attribute is missing").toString());
                }else{
                    DatabaseConnection db = new DatabaseConnection();
                    db.connect(Config.dbHost,Config.dbPort,Config.dbName);
                    
                    //podla query vyberieme data
                    DBObject condition = new  BasicDBObject("$match",query.get());
                    //z kolekcie nam staci vybrat len rid
                    DBObject select= new  BasicDBObject("$project",new BasicDBObject("rid",1)); 
                    AggregationOutput aggOut=db.getDatabase().getCollection("records_main").aggregate(condition,select);
                    
                    //vytvorime array z vybranych rid
                    ArrayList<Double> list = new ArrayList<>();
                    Iterator<DBObject> objjj= aggOut.results().iterator();
                    while(objjj.hasNext()){
                        Double rid=((Double)objjj.next().get("rid"));
                        list.add(rid);
                    }
                    //System.out.println("Ping Time ----"+list.size());
                    
                if(!list.isEmpty()){
                    DBCollection cnl =db.getDatabase().getCollection("records_CNLinformationElements");
                    DBObject match= new  BasicDBObject("$match",new QueryBuilder().put("rid").in(list).get());
                    select= new  BasicDBObject("$project",new BasicDBObject("roundTripTimeNanoseconds", 1));
                                        
                    aggOut =cnl.aggregate(match,select);
                    db.disconnect();
                    
                    objjj= aggOut.results().iterator();
                    double vysledok = 0;
                    int pocet =0;
                    while(objjj.hasNext()){
                        vysledok+= ((long) objjj.next().get("roundTripTimeNanoseconds"));
                        pocet++;
                    }
                    //System.out.println("|ResponsePingTime   | Dáta"+(vysledok/1000000/pocet));
                    getJedis().publish("ResponsePingTime",ModuleResponse.sentData("PingTime", "ok", ""+(vysledok/1000000/pocet)).toString());
                }else{
                    getJedis().publish("ResponsePingTime",ModuleResponse.sentData("PingTime", "unavailable", "null").toString());
                }
             }
                endT=new Date().getTime();
               /// System.out.println("PingTime "+(endT-startT));
            } catch (UnknownHostException | JSONException ex) {
                Logger.getLogger(PingTime.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
    }
    
    @Override
    public String getModuleName() {
        return "PingTime";
    }
    
}
