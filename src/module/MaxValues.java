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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;

/**
 *
 * @author esperian
 */
public class MaxValues {
    private ArrayList<User>  values;
    private int maxRecords;
    
    public MaxValues(){
        this.maxRecords =5;
        values =new ArrayList<>();
    }
    
    public MaxValues(int max){
        this.maxRecords =max;
        values =new ArrayList<>();
    }
        
    public void push(User user){
        values.add(user);
        Collections.sort(values);
        if(values.size()>getMaxRecords()){
            values.remove(getMaxRecords());
        }
    }
    
    public JSONArray toArray(){
        JSONArray obj=new JSONArray();
        
        for(User u :values){
            try {
                obj.put(new JSONArray().put(u.ipcka).put(u.octetCount));
            } catch (JSONException ex) {
                Logger.getLogger(MaxValues.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return obj;
    }


    public void printTree(){
        Iterator<User> iterate = values.iterator();
        int i=0;
            while(iterate.hasNext()){
                //System.out.println(iterate.next().octetCount);
            }
    }

    /**
     * @return the maxRecords
     */
    public int getMaxRecords() {
        return maxRecords;
    }

    /**
     * @param maxRecords the maxRecords to set
     */
    public void setMaxRecords(int maxRecords) {
        this.maxRecords = maxRecords;
    }
   
    
    public static class User implements Comparable<User>{
        public double octetCount;
        public String ipcka;
        
        public User(double octetCount,String ipcka){
            this.octetCount=octetCount;
            this.ipcka=ipcka;
        }

        @Override
        public int compareTo(User o) {
            if(this.octetCount==o.octetCount){
                return 0;
            }else if(this.octetCount>o.octetCount){
                return -1;
            }else{
                return 1;
            }
        }
    }

}
