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

import com.mongodb.BasicDBObject;
import com.mongodb.QueryBuilder;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author esperian
 */
public class PrepareStatement {
   
    QueryBuilder query;
        
        
        public QueryBuilder getStatement(JSONObject object, AbstractModule m) throws UnknownHostException{
           
            query=null;
            query=new QueryBuilder(); 
            
            object =removeNullValue(object, m);
        
            if(!containRequiredAttribute(object, m)){
                return null;
            }
        
            JSONObject request = object;
         Iterator<String> keys = request.keys();
         
         while(keys.hasNext()){
             String id=keys.next();
             switch(id){
                       case "name": break;
        /*ok*/         case "time":
                     try {
                         
                         JSONArray time =request.getJSONArray("time");
                         long pociatocna =time.getLong(0);
                         long konecna = time.getLong(1);
                         query.put("flowStartMilliseconds").lessThanEquals(konecna);
                         query.put("flowEndMilliseconds").greaterThanEquals(pociatocna);
                         
                     } catch (JSONException ex) {
                         Logger.getLogger(PrepareStatement.class.getName()).log(Level.SEVERE, null, ex);
                     }
                 break;                 
        /*ok*/         case "exporter_id": 
                     try {
                         int i = (int) request.get("exporter_id");
                         
                         query.put("observationPointId").is(request.getLong("exporter_id"));
                     } catch (JSONException ex) {
                         Logger.getLogger(PrepareStatement.class.getName()).log(Level.SEVERE, null, ex);
                     }
                 break;
                                  
        /*ok*/  case "host_port": 
                     try {
                         JSONObject o=request.getJSONObject(id);
                         JSONArray port= o.getJSONArray("ports");
                         
                         switch(o.getString("type")){
                             case "array": 
                                 
                                 query.and(new QueryBuilder().or(new QueryBuilder().put("sourceTransportPort").in(JArraytoArrayI(port)).get(),new QueryBuilder().put("destinationTransportPort").in(JArraytoArrayI(port)).get()).get());
                                 break;
                             case "range": 
                                 
                                 query.and(new QueryBuilder().or(new QueryBuilder().put("sourceTransportPort").greaterThanEquals(JArraytoArrayI(port)[0]).lessThanEquals(JArraytoArrayI(port)[1]).get(), new QueryBuilder().put("destinationTransportPort").greaterThanEquals(JArraytoArrayI(port)[0]).lessThanEquals(JArraytoArrayI(port)[1]).get()).get());
                                 break;
                         }                  
                         
                     } catch (JSONException ex) {
                         Logger.getLogger(PrepareStatement.class.getName()).log(Level.SEVERE, null, ex);
                     }

                 break;
        /*ok*/         case "destination_port": 
                     
        /*ok*/         case "source_port": 
                     try {
                         JSONObject o=request.getJSONObject(id);
                         JSONArray port= o.getJSONArray("ports");
                         
                         switch(o.getString("type")){
                             case "array": 
                                 
                                 //System.out.println("*******"+id.substring(0,id.indexOf('_')));
                                 query.put(id.substring(0,id.indexOf('_'))+"TransportPort").in(JArraytoArrayI(port));
                                 break;
                             case "range": 
                                 
                                 query.put(id.substring(0,id.indexOf('_'))+"TransportPort").greaterThanEquals(port.getInt(0)).lessThanEquals(port.getInt(1));
                                 break;
                         }                  
                         
                     } catch (JSONException ex) {
                         Logger.getLogger(PrepareStatement.class.getName()).log(Level.SEVERE, null, ex);
                     }

                 break;
                 
                 case "client_ip": 
                     String ip = null;
                     try {
                         ip = request.getString("client_ip");
                     } catch (JSONException ex) {
                         Logger.getLogger(PrepareStatement.class.getName()).log(Level.SEVERE, null, ex);
                     }
                        query.and(new QueryBuilder().or(new BasicDBObject("sourceIPv4Address", InetAddress.getByName(ip).getAddress()),new BasicDBObject("destinationIPv4Address", InetAddress.getByName(ip).getAddress())).get());
                     
                 break;
                 case "host_ip": 
                     try {
                         JSONObject o=request.getJSONObject("host_ip");
                         JSONArray ips =o.getJSONArray("ips");
                         
                         switch(o.getString("type")){
                             case "array": 
                                 query.and(new QueryBuilder().or(new QueryBuilder().put("sourceIPv4Address").in(JArrayToArrayB(ips)).get(), new QueryBuilder().put("destinationIPv4Address").in(JArrayToArrayB(ips)).get()).get());
                                 break;
                             case "range": 
                                 query.and(new QueryBuilder().or(new QueryBuilder().put("sourceIPv4Address").greaterThanEquals(JArrayToArrayB(ips).get(0)).lessThanEquals(JArrayToArrayB(ips).get(1)).get(), new QueryBuilder().put("destinationIPv4Address").greaterThanEquals(JArrayToArrayB(ips).get(0)).lessThanEquals(JArrayToArrayB(ips).get(1)).get()).get());
                                 break;
                         }
                     } catch (JSONException ex) {
                         Logger.getLogger(PrepareStatement.class.getName()).log(Level.SEVERE, null, ex);
                     }
                break;
                case "source_ip": 
                     try {
                         JSONObject o=request.getJSONObject("source_ip");
                         JSONArray ips =o.getJSONArray("ips");
                         
                         switch(o.getString("type")){
                             case "array": 
                                 
                                 query.put("sourceIPv4Address").in(JArrayToArrayB(ips));
                                 
                                 break;
                             case "range": 
                                 
                                 query.put("sourceIPv4Address").greaterThanEquals(JArrayToArrayB(ips).get(0)).lessThanEquals(JArrayToArrayB(ips).get(1));
                             break;
                         }
                     } catch (JSONException ex) {
                         Logger.getLogger(PrepareStatement.class.getName()).log(Level.SEVERE, null, ex);
                     }
                break;
       /*ok*/     case "destination_ip":
                     try {
                         JSONObject o=request.getJSONObject("destination_ip");
                         JSONArray ips =o.getJSONArray("ips");
                         
                         
                         switch(o.getString("type")){
                             case "array": 
                                 query.put("destinationIPv4Address").in(JArrayToArrayB(ips).toArray());
                             break;
                             case "range":
                                 query.put("destinationIPv4Address").greaterThanEquals(JArrayToArrayB(ips).get(0)).lessThanEquals(JArrayToArrayB(ips).get(1));
                             break;
                             }
                         } catch (JSONException ex) {
                             Logger.getLogger(PrepareStatement.class.getName()).log(Level.SEVERE, null, ex);
                         }
                 break;
                 default : // System.out.println("Undefined parameter "+id); break;
                        
             }
         }
         return query;
        }
        
        public JSONObject removeNullValue(JSONObject jo, AbstractModule m){
           
            Iterator<String> keysFromObj =jo.keys();
            JSONObject correct= new JSONObject();
            while(keysFromObj.hasNext()){
                
                    String id=keysFromObj.next();
                    if((!jo.isNull(id))==true){
                        if(m.getOptionalAttribute().contains(id)==true || m.getRequiredAttribute().contains(id)==true){
                            try {
                                correct.put(id, jo.get(id));
                            } catch (JSONException ex) {
                                Logger.getLogger(PrepareStatement.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }else{
                            System.out.println(id+" key is not required and optional too.");
                        }
                    }else{
                        //System.out.println("Value of "+id+" key is null.");
                    }
            }
            
            return correct;
        }
        
        public boolean containRequiredAttribute(JSONObject jo,AbstractModule m){
        boolean contain=true;
        for (String requiredAttribute : m.getRequiredAttribute()) {
                if(jo.has(requiredAttribute)==false){
                    System.out.println("sprava neobsahuje atribut+:"+ requiredAttribute);
                    contain=false;
                }
        }
        return contain;
        }
        
        public boolean checkPort(int i){
            return (i<=65535 && i>=0);
        }
        
        public Integer[] JArraytoArrayI(JSONArray array){
            ArrayList<Integer> list = new ArrayList<>();
            
            for(int i=0; i<array.length();i++){
                try {
                    if(checkPort(i)==true){
                    list.add(array.getInt(i));
                    }
                } catch (JSONException ex) {
                    Logger.getLogger(PrepareStatement.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            return list.toArray(new Integer[list.size()]);
        }
        
        public boolean chcekIPaddress(String ip){
            String ipv4Pattern = "(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])";
            String ipv6Pattern = "([0-9a-f]{1,4}:){7}([0-9a-f]){1,4}";
            
            Pattern VALID_IPV4_PATTERN = Pattern.compile(ipv4Pattern, Pattern.CASE_INSENSITIVE);
            Pattern VALID_IPV6_PATTERN = Pattern.compile(ipv6Pattern, Pattern.CASE_INSENSITIVE);
            
            Matcher m1 = VALID_IPV4_PATTERN.matcher(ip);
            Matcher m2 = VALID_IPV6_PATTERN.matcher(ip);
            
        return (m1.matches()==true || m2.matches()==true);
        }
        
        public ArrayList<byte[]> JArrayToArrayB(JSONArray array){
         ArrayList<byte[]> list = new ArrayList<>();
        
            for(int i=0;i<array.length();i++){
             try {
                 if(chcekIPaddress(array.get(i).toString())){
                     //System.out.println("adsfad");
                 list.add(InetAddress.getByName(array.get(i).toString()).getAddress());
                 }
                 //System.out.println("*********"+Arrays.toString(InetAddress.getByName(array.get(i).toString()).getAddress()));
              } catch (JSONException | UnknownHostException ex) {
                //System.out.println("************************************");
                Logger.getLogger(PrepareStatement.class.getName()).log(Level.SEVERE, null, ex);
              }
            }
            return list;
        }
}