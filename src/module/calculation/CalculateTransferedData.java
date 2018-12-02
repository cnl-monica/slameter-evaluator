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
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import module.Flow;

/**
 *
 * @author esperian
 */
public class CalculateTransferedData {
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
                        long octets= (long)row.get("octetDeltaCount");
                        
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
                    setResutValue(am);
    }
    
        public void processData(ArrayList<DBObject> result){
        
                    long s=0;
                    double am=0.0;
                    for(DBObject obj : result){
                        s++;
                        DBObject row=obj;
                        long startRecord= (long)row.get("flowStartMilliseconds");
                        long endRecord= (long)row.get("flowEndMilliseconds");
                        long octets= (long)row.get("octetDeltaCount");
                        
                          
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
        
            public void processData(AggregationOutput aggOut){
        
                    double am=0.0;
                    Iterator<DBObject> result2= aggOut.results().iterator();
            
            while(result2.hasNext()){
                    
                        DBObject row=result2.next();
                        long startRecord= (long)row.get("flowStartMilliseconds");
                        long endRecord= (long)row.get("flowEndMilliseconds");
                        long octets= (long)row.get("octetDeltaCount");
                        
                          
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
    
    public void processDataPerSec(AggregationOutput aggOut){
        long l = new Date().getTime();
        //sekunda,pocetOktetov
        Long[] times= getMaxAndMin(aggOut);
        Hashtable<Long[],Double> data = devideOnSecInterval(times[0],times[1]);
            
            Iterator<DBObject> result2= aggOut.results().iterator();
            
            while(result2.hasNext()){
                //System.out.println("sad");
                DBObject row=result2.next();
                long startRecord = (long)row.get("flowStartMilliseconds");
                long endRecord = (long)row.get("flowEndMilliseconds");
                Long octets= (long)row.get("octetDeltaCount");
                
                Enumeration<Long[]> timeStamp=data.keys();
                
                while(timeStamp.hasMoreElements()){
                    
                     Long[] timeInterval = timeStamp.nextElement();
                     if(isInInterval(timeInterval,startRecord) || isInInterval(timeInterval,endRecord)){
                     if(timeInterval[0]<startRecord && timeInterval[1]>endRecord){
                         Double pripocet= octets.doubleValue();
                         Double akt =data.get(timeInterval);
                         //System.out.println(pripocet +" "+timeInterval[0] +":"+timeInterval[1]);
                         data.put(timeInterval, (pripocet+akt));
                         //System.out.println("Zaciatok aj koniec v intervale");
                     }else if(timeInterval[0]>startRecord && timeInterval[1]<endRecord){
                         Double pripocet= (double)((timeInterval[1]-timeInterval[0])*octets.doubleValue())/(endRecord-startRecord);
                         Double akt =data.get(timeInterval);
                         //System.out.println(pripocet +" "+timeInterval[0] +":"+timeInterval[1]);
                         data.put(timeInterval, (pripocet+akt));
                         
                         
                     }else if(timeInterval[0]<startRecord && timeInterval[1]<endRecord){
                         Double pripocet= (double) ((timeInterval[1]-startRecord)*(octets.doubleValue()))/(endRecord-startRecord);
                         Double akt =data.get(timeInterval);
                         
                          data.put(timeInterval, (pripocet+akt));
                         
                         //System.out.println("Zaciatok v intervale koniec mimo");
                     }else if(timeInterval[0]>startRecord && timeInterval[1]>endRecord){
                         Double pripocet= (double) ((endRecord-timeInterval[0])*(octets.doubleValue()))/(endRecord-startRecord);
                         Double akt =data.get(timeInterval);
                         data.put(timeInterval, (pripocet+akt));
                     }
                      
                     }
                     
                     
                }
            }
            
            //System.out.println(data.size());
            //System.out.println(data.toString());
            Set<Double> daticka = new TreeSet<>();
                daticka.addAll(data.values());
            
            
                //System.out.println("-------------------");
           //System.out.println(daticka.toArray()[0]);
           if(daticka.toArray().length==0){
               setResutValue(new Double(-1));
           }else{
           setResutValue((Double)daticka.toArray()[daticka.toArray().length-1]);
           }
           
    }
           
    public Hashtable<Long[],Double> devideOnSecInterval(long zaciatok,long koniec){
        Hashtable<Long[],Double> intervals =new Hashtable<Long[],Double>();
        long akt=zaciatok;
        if((zaciatok % 1000)!=0){
            if((zaciatok+(1000-(zaciatok % 1000)))>koniec){
            Long[] as= {zaciatok,koniec};
            intervals.put(as,0.0);
            akt=koniec;
            }else{
            Long[] as= {zaciatok,(zaciatok+(1000-(zaciatok % 1000)))};
            intervals.put(as,0.0);
            akt=(zaciatok+(1000-(zaciatok % 1000)));
            }
        }
        
        while((akt+1000)<koniec){
            Long[] as= {(akt+1),(akt+1000)};
            intervals.put(as,0.0);
            akt=akt+1000;
        }
        
        if(akt==koniec){
            
        }else if (akt<koniec){
            Long[] as= {(akt+1),koniec};
            intervals.put(as,0.0);
        }
        
        return intervals;
    }
    
            public Long[] getMaxAndMin(AggregationOutput aggOut){
       Long[] minmax = new Long[2];
       minmax[0]=0L;
       minmax[1]=0L;
       int i=0;
       Iterator<DBObject> it= aggOut.results().iterator();
           while(it.hasNext()){
               DBObject obj =it.next();
               Long objStart = (Long)obj.get("flowStartMilliseconds");
               Long objEnd = (Long)obj.get("flowStartMilliseconds");
               
               if(i==0){
                   minmax[0] = objStart;
                   minmax[1] = objEnd;
                   i++;
               }
               
               if(minmax[0]>objStart){
                   minmax[0]=objStart;
               }
               
               if(minmax[1]<objEnd){
                   minmax[1]=objEnd;
               }
           }
           
       return minmax;
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
    
    public boolean isInInterval(Long[] interval, Long number){
        return (number>=interval[0] && number<=interval[1]);
    }
    
        public void processDataPerSec2(AggregationOutput aggOut){
        long l = new Date().getTime();
        //sekunda,pocetOktetov
        Long[] times= getMaxAndMin(aggOut);
        Hashtable<Long[],Double> data = devideOnSecInterval(getStartInterval(),getEndInterval());
            
            Iterator<DBObject> result2= aggOut.results().iterator();
            
            while(result2.hasNext()){
                //System.out.println("sad");
                DBObject row=result2.next();
                long startRecord = (long)row.get("flowStartMilliseconds");
                long endRecord = (long)row.get("flowEndMilliseconds");
                Long octets= (long)row.get("octetDeltaCount");
                
                Enumeration<Long[]> timeStamp=data.keys();
                
                while(timeStamp.hasMoreElements()){
                    
                     Long[] timeInterval = timeStamp.nextElement();
                     if(isInInterval(timeInterval,startRecord) || isInInterval(timeInterval,endRecord)){
                     if(timeInterval[0]<startRecord && timeInterval[1]>endRecord){
                         Double pripocet= octets.doubleValue();
                         Double akt =data.get(timeInterval);
                         //System.out.println(pripocet +" "+timeInterval[0] +":"+timeInterval[1]);
                         data.put(timeInterval, (pripocet+akt));
                         //System.out.println("Zaciatok aj koniec v intervale");
                     }else if(timeInterval[0]>startRecord && timeInterval[1]<endRecord){
                         Double pripocet= (double)((timeInterval[1]-timeInterval[0])*octets.doubleValue())/(endRecord-startRecord);
                         Double akt =data.get(timeInterval);
                         //System.out.println(pripocet +" "+timeInterval[0] +":"+timeInterval[1]);
                         data.put(timeInterval, (pripocet+akt));
                         
                         
                     }else if(timeInterval[0]<startRecord && timeInterval[1]<endRecord){
                         Double pripocet= (double) ((timeInterval[1]-startRecord)*(octets.doubleValue()))/(endRecord-startRecord);
                         Double akt =data.get(timeInterval);
                         
                          data.put(timeInterval, (pripocet+akt));
                         
                         //System.out.println("Zaciatok v intervale koniec mimo");
                     }else if(timeInterval[0]>startRecord && timeInterval[1]>endRecord){
                         Double pripocet= (double) ((endRecord-timeInterval[0])*(octets.doubleValue()))/(endRecord-startRecord);
                         Double akt =data.get(timeInterval);
                         data.put(timeInterval, (pripocet+akt));
                     }
                      
                     }
                     
                     
                }
            }
            
            //System.out.println(data.size());
            //System.out.println(data.toString());
            Set<Double> daticka = new TreeSet<>();
                daticka.addAll(data.values());
            
            
                //System.out.println("-------------------");
           setResutValue((Double)daticka.toArray()[daticka.toArray().length-1]);
           long l2 = new Date().getTime();
           ///System.out.println((l2-l));
           
    }
}
