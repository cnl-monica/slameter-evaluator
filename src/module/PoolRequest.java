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

import java.util.LinkedList;
import java.util.Queue;

/**
 *
 * @author esperian
 */
public class PoolRequest {
    Queue<Request> queue;
    
    public PoolRequest(){
        queue =  new LinkedList<>();
    }
    
    public void add(Request r){
        queue.add(r);
    }
   
    
    public Request get(){
            return queue.poll();
    }
    
    public boolean isEmpty(){
        if(queue.isEmpty()){
                return true;
        }
        return false;
    }
    
    public int size(){
        return queue.size();
    }
    
    public static class Request{
        public String id;
        public String request;
    
        public Request(String id,String request){
            this.id=id;
            this.request=request;
        }
        
        public Request(){
        
        }
    }
    
}
