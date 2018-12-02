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

package connctions;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import java.net.UnknownHostException;

/**
 *
 * @author esperian
 */
public class DatabaseConnection {
    
    private MongoClient mongo;
    private DB database;
    private boolean isConnected= false;

    /**
    * Connection to database.
    * @param host String host
    * @param port String  port
    * @param name String database name
    * @param username String username
    * @param password String password
    * @return valueIfIsConnected
    */
    public boolean connect(String host, String port, String name) {

        try {
            mongo = new MongoClient(host, Integer.parseInt(port));
            database = mongo.getDB(name);
            isConnected = true;
            return true;
            
            
        } catch (UnknownHostException | MongoException ex) {
            java.util.logging.Logger.getLogger(DatabaseConnection.class.getName()).log(java.util.logging.Level.SEVERE, null, ex  + " Probably caused by failed database connection. Check database settings and connection.");
            isConnected=false;
            return false;
        }        
    }
    
    public void disconnect(){
        mongo.close();
        isConnected = false;
    }
    
    public boolean isConnected(){
        return isConnected;
    }
    
    /**
     * @return the database
     */
    public DB getDatabase() {
        return database;
    }
}
