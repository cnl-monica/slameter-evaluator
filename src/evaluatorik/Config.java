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

import java.io.IOException;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author esperian
 */
public class Config {
    
    private static final Logger log = Logger.getLogger(Config.class.getName());
    
    public static String dbHost="localhost";
    public static String dbPort="27017";
    public static String dbName="monica2";
    public static String dbLogin="bm";
    public static String dbPassword="bm";
    
    public static String redisHost="localhost";
    public static String redisPort="6379";
    public static String redisPoolSize="20";
    
    public static boolean AmountOfTransferredData=false;
    public static boolean BandwidthHistoryTrend=false;
    public static boolean AverageOfTransferredDataPacket=false;
    public static boolean MaximumDownloadUpload=false;
    public static boolean AmountOfTransferredDataPacket=false;
    public static boolean BandwidthHistoryTrendPacket=false;
    public static boolean AverageMiniTable=false;
    public static boolean HistoryTrendFlows=false;
    public static boolean AverageOfTransferredData=false;
    public static boolean HistoryTable=false;
    public static boolean AmountMiniTable=false;
    public static boolean NumberOfFlows=false;
    public static boolean PingTime=false;
    public static boolean TopUploader=false;
    public static boolean TopDownloader=false;
    
    	public void parseXmlFile(){
        Document doc;    
        try {
            //get the factory
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            
            //Using factory get an instance of document builder
            DocumentBuilder db = dbf.newDocumentBuilder();
            
            //parse using builder to get DOM representation of the XML file
            doc = db.parse("evaluator.xml");
            
            Element docEle = doc.getDocumentElement();
            NodeList nl = docEle.getElementsByTagName("database");
            
            Element database = (Element)nl.item(0);
            
            Node node = (Node) database.getElementsByTagName("dbHost").item(0).getChildNodes().item(0);
            
            if(node.getNodeValue()!=null){
                dbHost= node.getNodeValue();
            }
            
            node = (Node) database.getElementsByTagName("dbPort").item(0).getChildNodes().item(0);
            
            if(node.getNodeValue()!=null){
                dbPort= node.getNodeValue();
            }
            
            node = (Node) database.getElementsByTagName("dbName").item(0).getChildNodes().item(0);
            
            if(node.getNodeValue()!=null){
                dbName= node.getNodeValue();
            }
            
            node = (Node) database.getElementsByTagName("dbLogin").item(0).getChildNodes().item(0);
            
            if(node.getNodeValue()!=null){
                dbLogin= node.getNodeValue();
            }
            
            node = (Node) database.getElementsByTagName("dbPassword").item(0).getChildNodes().item(0);
            
            if(node.getNodeValue()!=null){
                dbPassword= node.getNodeValue();
            }
                      
            
            nl = docEle.getElementsByTagName("redis");
            
            database = (Element)nl.item(0);
            
            node = (Node) database.getElementsByTagName("redisHost").item(0).getChildNodes().item(0);
            
            if(node.getNodeValue()!=null){
                redisHost= node.getNodeValue();
            }
            
            node = (Node) database.getElementsByTagName("redisPort").item(0).getChildNodes().item(0);
            
            if(node.getNodeValue()!=null){
                redisPort= node.getNodeValue();
            }
            
            node = (Node) database.getElementsByTagName("redisPoolSize").item(0).getChildNodes().item(0);
            
            if(node.getNodeValue()!=null){
                redisPoolSize= node.getNodeValue();
            }
            
            nl = docEle.getElementsByTagName("modules");
            Element modules = (Element)nl.item(0);
            
            node = (Node) modules.getElementsByTagName("AmountOfTransferredData").item(0).getChildNodes().item(0);
            if("true".equals(node.getNodeValue())){
                AmountOfTransferredData=true;
            }

            node = (Node) modules.getElementsByTagName("BandwidthHistoryTrend").item(0).getChildNodes().item(0);
            if("true".equals(node.getNodeValue())){
                BandwidthHistoryTrend=true;
            }

            node = (Node) modules.getElementsByTagName("AverageOfTransferredDataPacket").item(0).getChildNodes().item(0);
            if("true".equals(node.getNodeValue())){
                AverageOfTransferredDataPacket=true;
            }

            node = (Node) modules.getElementsByTagName("MaximumDownloadUpload").item(0).getChildNodes().item(0);
            if("true".equals(node.getNodeValue())){
                MaximumDownloadUpload=true;
            }

            node = (Node) modules.getElementsByTagName("AmountOfTransferredDataPacket").item(0).getChildNodes().item(0);
            if("true".equals(node.getNodeValue())){
                AmountOfTransferredDataPacket=true;
            }

            node = (Node) modules.getElementsByTagName("BandwidthHistoryTrendPacket").item(0).getChildNodes().item(0);
            if("true".equals(node.getNodeValue())){
                BandwidthHistoryTrendPacket=true;
            }

            node = (Node) modules.getElementsByTagName("AverageMiniTable").item(0).getChildNodes().item(0);
            if("true".equals(node.getNodeValue())){
                AverageMiniTable=true;
            }

            node = (Node) modules.getElementsByTagName("HistoryTrendFlows").item(0).getChildNodes().item(0);
            if("true".equals(node.getNodeValue())){
                HistoryTrendFlows=true;
            }

            node = (Node) modules.getElementsByTagName("AverageOfTransferredData").item(0).getChildNodes().item(0);
            if("true".equals(node.getNodeValue())){
                AverageOfTransferredData=true;
            }

            node = (Node) modules.getElementsByTagName("HistoryTable").item(0).getChildNodes().item(0);
            if("true".equals(node.getNodeValue())){
                HistoryTable=true;
            }

            node = (Node) modules.getElementsByTagName("AmountMiniTable").item(0).getChildNodes().item(0);
            if("true".equals(node.getNodeValue())){
                AmountMiniTable=true;
            }
            node = (Node) modules.getElementsByTagName("NumberOfFlows").item(0).getChildNodes().item(0);
            if("true".equals(node.getNodeValue())){
                NumberOfFlows=true;
            }

            node = (Node) modules.getElementsByTagName("PingTime").item(0).getChildNodes().item(0);
            if("true".equals(node.getNodeValue())){
                PingTime=true;
            }
            
            node = (Node) modules.getElementsByTagName("TopDownloader").item(0).getChildNodes().item(0);
            if("true".equals(node.getNodeValue())){
                TopDownloader=true;
            }
            
            node = (Node) modules.getElementsByTagName("TopUploader").item(0).getChildNodes().item(0);
            if("true".equals(node.getNodeValue())){
                TopUploader=true;
            }

            
            
        } catch (ParserConfigurationException | SAXException | IOException ex) {
            log.info("No evaluator.xml file. Default values will be used") ;
        }

        
		
	}
        
        public void ConfigToString(){
            log.info("Config");
            log.info("DB host "+dbHost);
            log.info("DB port "+dbPort);
            log.info("DB name "+dbName);
            log.info("DB login "+dbLogin);
            log.info("DB pass "+dbPassword);

            log.info("Redis host "+redisHost);
            log.info("Redis port "+redisPort);
            log.info("Redis pool size "+redisPoolSize);
            
        }
}
