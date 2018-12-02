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

package module;

import connctions.JedisConnection;
import evaluatorik.Config;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Logger;
import redis.clients.jedis.Jedis;
/**
 *
 * @author esperian
 */
public abstract class AbstractModule extends Thread{
    
    private PoolRequest requests;
    private Jedis jedis;
    private PrepareStatement prepareStatement; 
    private ArrayList<String> requiredAttribute;
    private ArrayList<String> optionalAttribute;
    public final Logger log = Logger.getLogger(Config.class.getName());
 
    public ArrayList<String> getRequiredAttribute() {
        return requiredAttribute;
    }

    public void setRequiredAttribute(String... s) {
        if(requiredAttribute==null){
            requiredAttribute=new ArrayList<>();
        }
        requiredAttribute.addAll(Arrays.asList(s));
    }
    
    public ArrayList<String> getOptionalAttribute() {
        return optionalAttribute;
    }

    public void setOptionalAttribute(String... s) {
        if(optionalAttribute==null){
            optionalAttribute=new ArrayList<>();
        }
        optionalAttribute.addAll(Arrays.asList(s));
    }
    
    public void returnJedisResources(Jedis jedis){
        JedisConnection.returnConnection(jedis);
        System.out.println("Vraciam Jedisa");
    }
    
    public PoolRequest getPoolRequest(){
        return requests;
    }
    
    public void setPoolRequest(PoolRequest requests){
        this.requests=requests;
    }
    
    public Jedis getJedis(){
        return jedis;
    }
    
    public void setJedis(Jedis jedis){
        this.jedis=jedis;
    }
    
    public void setPrepareStatement(PrepareStatement ps){
        this.prepareStatement=ps;
    }
    
    public PrepareStatement getPrepareStatement(){
        return prepareStatement;
    }
    
    public abstract String getModuleName();
    @Override
    public abstract void run();
        
    public void stopModule() {
        System.out.println("Module "+getModuleName()+" was stopped");
        this.interrupt();
    }
}
