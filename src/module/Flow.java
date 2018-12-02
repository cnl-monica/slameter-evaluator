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

import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author esperian
 */

    
public class Flow{
        
        private ArrayList<Record> flowData;
        
        public Flow(){
            flowData=new ArrayList<>();
        }
        
        public void addFlowDataOctet(DBObject obj){
            flowData.add(new Record((long)obj.get("flowStartMilliseconds"),(long)obj.get("flowEndMilliseconds"),(long)obj.get("octetDeltaCount")));
        }
        
        public void addFlowDataPacket(DBObject obj){
            flowData.add(new Record((long)obj.get("flowStartMilliseconds"),(long)obj.get("flowEndMilliseconds"),(long)obj.get("packetDeltaCount"),""));
        }
        
        public void printAll(){
            for(Record obj : flowData){
                System.out.print(obj.getEndTime()); 
            }
            //System.out.println("\n");
        }
        
        public ArrayList<Record> sortIt(){
            Collections.sort(flowData, new RecordsComparator());
            return flowData;
        }

    
    public static class Record implements Comparable<Record>{
        public long startTime;
        public long endTime;
        public long octets;
        public long packets;
        
        public Record(long st, long et, long oc){
            this.startTime=st;
            this.endTime=et;
            this.octets=oc;
        }
        
        public Record(long st, long et, long packets,String s){
            this.startTime=st;
            this.endTime=et;
            this.packets=packets;
        }
        /**
         * @return the startTime
         */
        public long getStartTime() {
            return startTime;
        }

        /**
         * @param startTime the startTime to set
         */
        public void setStartTime(long startTime) {
            this.startTime = startTime;
        }

        /**
         * @return the endTime
         */
        public long getEndTime() {
            return endTime;
        }

        /**
         * @param endTime the endTime to set
         */
        public void setEndTime(long endTime) {
            this.endTime = endTime;
        }

        /**
         * @return the octets
         */
        public long getOctets() {
            return octets;
        }

        /**
         * @param octets the octets to set
         */
        public void setOctets(long octets) {
            this.octets = octets;
        }

        @Override
        public int compareTo(Record o) {
            long compareTime = o.getEndTime();
            return Long.compare(this.endTime,compareTime);
        }

        /**
         * @return the packets
         */
        public long getPackets() {
            return packets;
        }

        /**
         * @param packets the packets to set
         */
        public void setPackets(long packets) {
            this.packets = packets;
        }
    }
    public static class RecordsComparator implements Comparator<Record> {

        @Override
        public int compare(Record o1, Record o2) {
            return o1.compareTo(o2);
        }
    }

    }