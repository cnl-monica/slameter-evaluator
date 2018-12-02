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

import connctions.JedisConnection;
import java.io.File;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import module.AbstractModule;
import connctions.JedisListener;
import redis.clients.jedis.Jedis;

/**
 *
 * @author esperian
 */
public class RunModules {
        private static final Logger log = Logger.getLogger(Config.class.getName());

        public static ArrayList<String> listenedModules = new ArrayList<>();
        public static JedisListener sss = new JedisListener();;
        
        public void getAllModulesViaConfig(){
        listenedModules.clear();
        
        if(Config.AmountOfTransferredData==true){
            listenedModules.add("AmountOfTransferredData");
            listenedModules.add("aa");
            log.info("AmountOfTransferredData is ready to use");

        }
        if(Config.BandwidthHistoryTrend==true){
            listenedModules.add("BandwidthHistoryTrend");
            log.info("BandwidthHistoryTrend is ready to use");

        }
        if(Config.AverageOfTransferredDataPacket==true){
            listenedModules.add("AverageDownloadUploadPacket");
            log.info("AverageOfTransferredDataPacket is ready to use");

        }
        if(Config.MaximumDownloadUpload==true){
            listenedModules.add("MaximumDownloadUpload");
            log.info("MaximumDownloadUpload is ready to use");

        }
        if(Config.AmountOfTransferredDataPacket==true){
            listenedModules.add("AmountOfTransferredDataPacket");
            log.info("AmountOfTransferredDataPacket is ready to use");

        }
        if(Config.BandwidthHistoryTrendPacket==true){
            listenedModules.add("BandwidthHistoryTrendPacket");
            log.info("BandwidthHistoryTrendPacket is ready to use");

        }
        if(Config.AverageMiniTable==true){
            listenedModules.add("AverageMiniTable");
            log.info("AverageMiniTable is ready to use");

        }
        if(Config.HistoryTrendFlows==true){
            listenedModules.add("HistoryTrendFlows");
            log.info("HistoryTrendFlows is ready to use");

        }
        if(Config.AverageOfTransferredData==true){
            listenedModules.add("AverageDownloadUpload");
            log.info("AverageOfTransferredData is ready to use");

        }
        if(Config.HistoryTable==true){
            listenedModules.add("HistoryTable");
            log.info("HistoryTable is ready to use");

        }
        if(Config.AmountMiniTable==true){
            listenedModules.add("AmountMiniTable");
            log.info("AmountMiniTable is ready to use");
        }
        if(Config.NumberOfFlows==true){
            listenedModules.add("NumberOfFlows");
            log.info("NumberOfFlows is ready to use");

        }
        if(Config.PingTime==true){
            listenedModules.add("PingTime");
            log.info("PingTime is ready to use");

        }   
        if(Config.TopDownloader==true){
            listenedModules.add("TopDownloader");
            log.info("TopDownloader is ready to use");

        }   
        if(Config.TopUploader==true){
            listenedModules.add("TopUploader");
            log.info("TopUploader is ready to use");

        }   
        sub(listenedModules);
        
    }
    
    public void getAllModulesViaReflection(){
        listenedModules.clear();
        //File directory=new File(File file = new File(new File(Thread.currentThread().getContextClassLoader().getResource("").toURI()), "com/configuration/settings.properties"););   
        File directory = null;
            try {
                directory = new File(new File(Thread.currentThread().getContextClassLoader().getResource("").toURI()), "module/modules");
            } catch (URISyntaxException ex) {
                Logger.getLogger(RunModules.class.getName()).log(Level.SEVERE, null, ex);
            }

                if(directory.exists()) {
            String[] files=directory.list();  
            
            for (String file : files) {
                if (file.endsWith(".class")) {   
                    try {
                        Class c = Class.forName("module.modules." + file.replaceFirst(".class", ""));
                        if(c.getSuperclass()==AbstractModule.class){
                            listenedModules.add(c.getSimpleName());
                            log.log(Level.INFO, "{0} is ready to use", c.getSimpleName());
                            
                        }
                    }catch (ClassNotFoundException | SecurityException ex) {
                        log.warning("Problem with obtaining class name");
                        
                    }
                }   
            }
        }else{
            System.out.println("neexistuje");
        }
        listenedModules.add("AmountOfTransferredDataTable");
        sub(listenedModules);
    }
        
   
    public void sub(ArrayList list){
       
        new Thread(new Runnable() {
            Jedis jj = JedisConnection.getConnection(); 
            
            @Override
            public void run() {
                String[] stockArr = null;
                try {
                    stockArr = new String[listenedModules.size()];
                    stockArr = listenedModules.toArray(stockArr);
                   
                    jj.subscribe(sss, stockArr);
                } catch (Exception e) {
                   System.out.println(e);
                   log.warning("Problem occured with starting module");
                }
            }
        }
        ).start();  
    }
        
}
