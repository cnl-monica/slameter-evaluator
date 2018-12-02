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

package module.calculation;

import com.mongodb.AggregationOutput;
import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import module.Flow;

/**
 *
 * @author esperian
 */
public class CalculateTransferedDataPacket {
    private Long startInterval;
    private Long endInterval;
    
    private Double resutValue;
    
    public void processData(Iterator<DBObject> result){
        
//        Hashtable<String,Flow> zoznam = new Hashtable<>();
//                    while(result.hasNext()){
//                        
//                        DBObject row=result.next();
//                        
//                        long startRecord= (long)row.get("flowStartMilliseconds");
//                        Object flowID =row.get("flowId");
//                        
//                        
//                        if(zoznam.containsKey(startRecord+":"+flowID)==false){
//                            Flow f= new Flow();
//                            f.addFlowDataOctet(row);
//                            
//                            zoznam.put(startRecord+":"+flowID, f);
//                            //f.printAll();
//                        }else{
//                            
//                            Flow tok= (Flow) zoznam.get(startRecord+":"+flowID);
//                            tok.addFlowDataOctet(row);
//                            zoznam.put(startRecord+":"+flowID, tok);
//                        }
//                    }
                    
                    double am=0.0;
                    while(result.hasNext()){
                        DBObject row=result.next();
                        
                        long startRecord= (long)row.get("flowStartMilliseconds");
                        long endRecord= (long)row.get("flowEndMilliseconds");
                        long octets= (long)row.get("packetDeltaCount");
                        
                            if(startRecord>= getStartInterval() && endRecord<= getEndInterval()){
                                am+= octets;
                            }else if(startRecord< getStartInterval() && endRecord> getEndInterval()){
                                    am+= (double)((getEndInterval()-getStartInterval())*octets)/(endRecord-startRecord);
                            }else if(startRecord< getStartInterval() && endRecord< getEndInterval()){
                                    am+= (double) ((endRecord-getStartInterval())*(octets))/(endRecord-startRecord);
                            }else if(startRecord>= getStartInterval() && endRecord>= getEndInterval()){
                                   am+= (double) ((getEndInterval()-startRecord)*(octets))/(endRecord-startRecord);
                            }
                    }
                   // System.out.println("packets "+am);
                    setResutValue(am);
    }
    
        public void processData(AggregationOutput aggOut){
         
                    double am=0.0;
                    Iterator<DBObject> result2= aggOut.results().iterator();
            
            while(result2.hasNext()){
                    
                        DBObject row=result2.next();
                        long startRecord= (long)row.get("flowStartMilliseconds");
                        long endRecord= (long)row.get("flowEndMilliseconds");
                        long octets= (long)row.get("packetDeltaCount");
                        
                          
                          if((startRecord<getStartInterval() && endRecord<getStartInterval())||(startRecord>getEndInterval() && endRecord>getEndInterval())){
                              
                          }else{
                            if(startRecord>= getStartInterval() && endRecord<= getEndInterval()){
                                
                                am+= octets;
                                
                            }else if(startRecord< getStartInterval() && endRecord> getEndInterval()){
                                
                                    am+= (double)((getEndInterval()-getStartInterval())*octets)/(endRecord-startRecord);
                                   
                            }else if(startRecord< getStartInterval() && endRecord< getEndInterval()){
                                
                                    am+= (double) ((endRecord-getStartInterval())*(octets))/(endRecord-startRecord);
                                    
                            }else if(startRecord>= getStartInterval() && endRecord>= getEndInterval()){
                                
                                   am+= (double) ((getEndInterval()-startRecord)*(octets))/(endRecord-startRecord);
                                   
                            }
                        }
                        //}
                    }
                    //System.out.println("pocet iteracii "+s);
                    setResutValue(am);
    }
    
        /**
     * @return the startInterval
     */
    public Long getStartInterval() {
        return startInterval;
    }

    /**
     * @param startInterval the startInterval to set
     */
    public void setStartInterval(Long startInterval) {
        this.startInterval = startInterval;
    }

    /**
     * @return the endInterval
     */
    public Long getEndInterval() {
        return endInterval;
    }

    /**
     * @param endInterval the endInterval to set
     */
    public void setEndInterval(Long endInterval) {
        this.endInterval = endInterval;
    }

    /**
     * @return the resutValue
     */
    public Double getResutValue() {
        return resutValue;
    }
    
    public Long getTimeIntervalInSec(){
        return ((getEndInterval()-getStartInterval())/1000);
    }
            
    /**
     * @param resutValue the resutValue to set
     */
    public void setResutValue(Double resutValue) {
        this.resutValue = resutValue;
    }
    
}
