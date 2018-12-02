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

import evaluatorik.Config;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 *
 * @author esperian
 */
public class JedisConnection {
    private static JedisConnection connection=null;
    private static JedisPool pool;
        
    private JedisConnection(){
       JedisPoolConfig c= new JedisPoolConfig();
       c.setMaxActive(Integer.parseInt(Config.redisPoolSize));
       pool =new JedisPool(c,Config.redisHost,Integer.parseInt(Config.redisPort));
    }
        
    public static Jedis getConnection(){
        if(connection==null){
            connection =new JedisConnection();
            return pool.getResource();
        }else{
            return pool.getResource();
        }
    }
    
    public static void returnConnection(Jedis jedis){
        pool.returnResource(jedis);
    }
}