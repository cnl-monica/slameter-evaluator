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
import java.util.Date;
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
public class NumberOfFlows extends AbstractModule {

    private static NumberOfFlows NumberOfFlowsInstance;

    public static NumberOfFlows getInstance() {

        if (NumberOfFlowsInstance == null) {
            NumberOfFlowsInstance = new NumberOfFlows();
            return NumberOfFlowsInstance;
        } else {
            return NumberOfFlowsInstance;
        }
    }

    private NumberOfFlows() {
        setPoolRequest(new PoolRequest());
        setJedis(JedisConnection.getConnection());
        setPrepareStatement(new PrepareStatement());
        setRequiredAttribute("time", "exporter_id");
        setOptionalAttribute("name", "client_ip", "host_ip", "source_ip", "destination_ip", "host_port", "source_port", "destination_port");
    }

    @Override
    public void run() {

        while (true) {
            try {
                if (this.getPoolRequest().isEmpty()) {
                    log.log(Level.INFO, "zastavujem module {0}", getModuleName());
                    synchronized (this) {
                        this.wait();
                    }
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(NumberOfFlows.class.getName()).log(Level.SEVERE, null, ex);
            }
           // System.out.println("Prijimam data pre module " + getModuleName());
            JSONObject obj;
            try {
                Long startT;
                Long endT;
                
                startT=new Date().getTime();
                //System.out.println("NumberOfFlows "+startT);
                obj = new JSONObject(getPoolRequest().get().request);
                //System.out.println(obj.toString());
                QueryBuilder query = getPrepareStatement().getStatement(obj, this);
                //System.out.println(query);
                if (query == null) {
                    getJedis().publish("ResponseNumberOfFlows", ModuleResponse.sentData("NumberOfFlows", "error", "Some of required attribute is missing").toString());
                } else {
                    DatabaseConnection db = new DatabaseConnection();

                    db.connect(Config.dbHost, Config.dbPort, Config.dbName);

                    DBObject condition = new BasicDBObject("$match", query.get());
                    DBObject select = new BasicDBObject("$project", new BasicDBObject("flowId", 1));
                    DBObject group = new BasicDBObject("$group", new BasicDBObject("_id", "$flowId"));

                    AggregationOutput aggOut = db.getDatabase().getCollection("records_main").aggregate(condition, select, group);
                        
                    int i = (((Collection<DBObject>) aggOut.results()).size());

                    db.disconnect();
                    getJedis().publish("ResponseNumberOfFlows", ModuleResponse.sentData("NumberOfFlows", "ok", "" + i).toString());
                    //System.out.println("|ResponseNumberOfFlows   | Dáta: "+i);
                }
                 endT=new Date().getTime();
                //System.out.println("NumberOfFlows "+(endT-startT));
            } catch (JSONException | UnknownHostException ex) {
                Logger.getLogger(NumberOfFlows.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public String getModuleName() {
        return "NumberOfFlows";
    }

}
