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
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
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
public class HistoryTrendFlows extends AbstractModule{
    private static HistoryTrendFlows HistoryTrendFlowsInstance;
    
    public static HistoryTrendFlows getInstance(){
        
        if(HistoryTrendFlowsInstance==null){
            HistoryTrendFlowsInstance = new HistoryTrendFlows();
            return HistoryTrendFlowsInstance;
        }else{
            return HistoryTrendFlowsInstance;
        }
    }
    
    private HistoryTrendFlows(){ 
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
                    Logger.getLogger(HistoryTrendFlows.class.getName()).log(Level.SEVERE, null, ex);
                }
                //System.out.println("Prijimam data pre module "+getModuleName());
            
            JSONObject obj;
            try {
                Long startT;
                Long endT;

                startT=new Date().getTime();
                //System.out.println("HistoryTrendFlows "+startT);
                obj= new JSONObject(getPoolRequest().get().request);

                QueryBuilder query =getPrepareStatement().getStatement(obj,this);
                
                if(query==null){
                    getJedis().publish("ResponseHistoryTrendFlows",ModuleResponse.sentData("HistoryTrendFlows", "error", "Some of required attribute is missing").toString());
                }else{
                    DatabaseConnection db = new DatabaseConnection();                   
                    
                    db.connect(Config.dbHost,Config.dbPort,Config.dbName);
                    
                    DBObject condition = new  BasicDBObject("$match",query.get());
                    DBObject select= new  BasicDBObject("$project",new BasicDBObject("flowId",1).append("flowStartMilliseconds", 1).append("flowEndMilliseconds", 1));
                    DBObject group = new BasicDBObject("$group", new BasicDBObject("_id", new BasicDBObject().append("flowid", "$flowId").append("flowStartMilliseconds","$flowStartMilliseconds")).append("flowStartMilliseconds", new BasicDBObject("$min", "$flowStartMilliseconds")).append("flowEndMilliseconds", new BasicDBObject("$max", "$flowEndMilliseconds")));                 
                    
                    
                    
                    List l=db.getDatabase().getCollection("records_main").distinct("flowStartMilliseconds");
                    
                    Collections.sort(l);
                    
                    AggregationOutput aggOut=db.getDatabase().getCollection("records_main").aggregate(condition,select,group);
                   
                        
                    int i =(((Collection<DBObject>) aggOut.results()).size());
                    
                    db.disconnect();
                    
                    //v pripade ze vysledok agregacie je 0 vratime prazdne pole
                    if(i==0){
                        getJedis().publish("ResponseHistoryTrendFlows",ModuleResponse.sentData("HistoryTrendFlows", "unavailable", "null").toString());
                    //inak pristupime k spracuvaniu
                    }else{
                        //ziskame iterator pre potreby ziskania casov.
                        Iterator<DBObject> iterator =aggOut.results().iterator();
                        //System.out.println("--- "+(((Collection<DBObject>) aggOut.results()).size()));
                        Set<Long> time = new TreeSet<>();
                        
                        //pridame pociatocnu a konecnu hodnotu zvoleneho intervalu
                        JSONArray startEnd =obj.getJSONArray("time");
                        long pociatocna =startEnd.getLong(0);
                        long konecna = startEnd.getLong(1);
                        
                        addTimeToList(time,pociatocna,pociatocna,konecna);
                        addTimeToList(time,konecna,pociatocna,konecna);

                        //pridanie casov v kt. nastala nejaka zmena
                        while(iterator.hasNext()){
                            DBObject object=iterator.next();
                            long start =((Long)object.get("flowStartMilliseconds"));
                            long end =((Long)object.get("flowEndMilliseconds"));

                            addTimeToList(time,start,pociatocna,konecna);
                            if(start==end){
                                //toto len preto aby sme zaznamenali situaciu ze nejaky flow ukoncil
                                addTimeToList(time,start+1l,pociatocna,konecna);                            
                            }else{
                                addTimeToList(time,end,pociatocna,konecna);
                                addTimeToList(time,start,pociatocna,konecna);
                            }
                        }
                        //pre kazdy cas zistime pocet flowov a inicializujeme tak pole
                        JSONArray result = new JSONArray();
                        for(Long value: time){
                            result.put(new JSONArray().put(value).put(getNumberOfFlows(aggOut, value)));
                        }
                        //System.out.println("|ResponseHistoryTrendFlows   | Dáta"+result.toString());
                        if(result.length()==0){
                            getJedis().publish("ResponseHistoryTrendFlows",ModuleResponse.sentData("HistoryTrendFlows", "unavailable", "null").toString());
                        }else{
                            getJedis().publish("ResponseHistoryTrendFlows",ModuleResponse.sentDataJsonArray("HistoryTrendFlows", "ok", result).toString());
                        }
                    }
                }
                endT=new Date().getTime();
                //System.out.println("HistoryTrendFlows "+(endT-startT));
            } catch (JSONException | UnknownHostException ex) {
                Logger.getLogger(HistoryTrendFlows.class.getName()).log(Level.SEVERE, null, ex);
            }
        }        
    }
    
    public long getNumberOfFlows(AggregationOutput output, long time){
        long number = 0;
        
        Iterator<DBObject> iterator =output.results().iterator();
        
        while(iterator.hasNext()){
            DBObject aa = iterator.next();
            //vyberieme vhodne hodnoty z daneho intervalu ( )
            if(time>((Long)aa.get("flowStartMilliseconds")) && time<((Long)aa.get("flowEndMilliseconds"))){
                number++;
            }
            //pridame hranicnu hodnotu pre start < )
            if(time==((Long)aa.get("flowStartMilliseconds"))){
                number++;
            }
        }
        
        return number;
    }
    
    public void addTimeToList(Set<Long> time,long value,long start,long end){
        if(time.contains(value)==false){  //pokial tuto hodnotu nemame v zozname
            if(value>=start && value<=end){ //a sucastne spada dointervalu
                time.add(value);
            }
        }
    }
    
    @Override
    public String getModuleName() {
        return "HistoryTrendFlows";
    }
    
}
