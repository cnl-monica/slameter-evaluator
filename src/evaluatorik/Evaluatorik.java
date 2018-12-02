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

package evaluatorik;

import connctions.DatabaseConnection;
import connctions.JedisConnection;
import java.util.Set;
import module.modules.AmountMiniTable;
import redis.clients.jedis.Jedis;

/**
 *
 * @author esperian 
 */
public class Evaluatorik{
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        
          Config c = new Config();
          c.parseXmlFile();
          c.ConfigToString();
        
        final DatabaseConnection d = new DatabaseConnection();
        boolean isConected =d.connect(Config.dbHost,Config.dbPort,Config.dbName);

        
       new RunModules().getAllModulesViaConfig();
       Thread shutdownHook = new Thread("ShutDown Hook") { // by Tomas Verescak

            @Override
            public void run() {
                System.out.println("Exit evaluatorik");
                Jedis ss =JedisConnection.getConnection();
                Set<String> kluceCo= ss.keys("*");
                System.out.println("Remove all messages from redis");
                for(String mm:kluceCo){
                    ss.del(mm);
                }
                System.out.println("Disconnect from database");
                d.disconnect();
            }
        };
        Runtime.getRuntime().addShutdownHook(shutdownHook);
    }
}