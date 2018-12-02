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

import evaluatorik.Config;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import redis.clients.jedis.Response;

/**
 *
 * @author esperian
 */
public class ModuleResponse {
    public static final Logger log = Logger.getLogger(Config.class.getName());
    static SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss,SSS");
    public static JSONObject sentData(String name, String status, String res){
        JSONObject response = new JSONObject();
        try {
            response.put("response", res);
            response.put("status", status);
            response.put("name", name);
        
             Date now = new Date();
        String strDate = sdf.format(now);
        System.out.println("["+strDate+"] Module "+name+" sending result of evaluation. Data: "+res+" Status: "+status);
                 
        } catch (JSONException ex) {
            Logger.getLogger(ModuleResponse.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }
    
        public static JSONObject sentDataDouble(String name, String status, Double res){
        JSONObject response = new JSONObject();
        try {
            response.put("response", res);
            response.put("status", status);
            response.put("name", name);
            
        Date now = new Date();
        String strDate = sdf.format(now);
        System.out.println("["+strDate+"] Module "+name+" sending result of evaluation. Data: "+res+" Status: "+status);
                 
        } catch (JSONException ex) {
            Logger.getLogger(ModuleResponse.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }
    
        public static JSONObject sentDataArray(String name, String status, long[] res){
        JSONObject response = new JSONObject();
        try {
            JSONObject array= new JSONObject();
            array.put("amountUpload", res[0]);
            array.put("amountDownload", res[1]);
            
            response.put("response", array);
            response.put("status", status);
            response.put("name", name);
            
               Date now = new Date();
        String strDate = sdf.format(now);  
        System.out.println("["+strDate+"] Module "+name+" sending result of evaluation. Data: "+res+" Status: "+status);
                 
        } catch (JSONException ex) {
            Logger.getLogger(ModuleResponse.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }
    
       public static JSONObject sentDataJsonArray(String name, String status, JSONArray res){
        JSONObject response = new JSONObject();
        try {
            
            response.put("response", res);
            response.put("status", status);
            response.put("name", name);
            
                   Date now = new Date();
        String strDate = sdf.format(now);
        System.out.println("["+strDate+"] Module "+name+" sending result of evaluation. Data: "+res+" Status: "+status);
                 
        } catch (JSONException ex) {
            Logger.getLogger(ModuleResponse.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }

    public static Object sentDataJsonObject(String res, String status, JSONObject name) {
        JSONObject response = new JSONObject();
        try {
            response.put("response", name);
            response.put("status", status);
            response.put("name", res);
        } catch (JSONException ex) {
            Logger.getLogger(ModuleResponse.class.getName()).log(Level.SEVERE, null, ex);
        }
        return response;
    }

    
    
}
