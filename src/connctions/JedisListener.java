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

package connctions;

import evaluatorik.Config;
import java.util.logging.Level;
import java.util.logging.Logger;
import module.PoolRequest;
import module.modules.*;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPubSub;

public class JedisListener extends JedisPubSub{
    private static final Logger log = Logger.getLogger(Config.class.getName());
    Jedis jedis = JedisConnection.getConnection();
    @Override
    public void onMessage(String channel, String message) {
        
        switch(channel){
            case "AmountOfTransferredDataTable": {
                
                log.log(Level.INFO, "New message for module AmountOfTransferredData received", AmountOfTransferredData.getInstance().getModuleName());
                if(Config.AmountOfTransferredData==true){
                jedis.publish("AmountOfTransferredDataTableSIG", "OK");
                AmountOfTransferredData.getInstance().getPoolRequest().add(new PoolRequest.Request(channel, message));
                System.out.println("Prijata sprava z webu: "+message);
                
                
                if(AmountOfTransferredData.getInstance().getState()==Thread.State.WAITING){
                synchronized(AmountOfTransferredData.getInstance()){
                    AmountOfTransferredData.getInstance().notify();
                }                
                }
                }
            }break;
            case "AmountOfTransferredData": {
                log.log(Level.INFO, "New message for module AmountOfTransferredData received", AmountOfTransferredData.getInstance().getModuleName());
                
                if(Config.AmountOfTransferredData==true){
                jedis.publish("AmountOfTransferredDataSIG", "OK");
                AmountOfTransferredData.getInstance().getPoolRequest().add(new PoolRequest.Request(channel, message));
                System.out.println("Prijata sprava z webu: "+message);
                
                
                if(AmountOfTransferredData.getInstance().getState()==Thread.State.WAITING){
                synchronized(AmountOfTransferredData.getInstance()){
                    AmountOfTransferredData.getInstance().notify();
                }
                }
                }
            }break;
            case "BandwidthHistoryTrend": {
                log.log(Level.INFO, "New message for module BandwidthHistoryTrend received", BandwidthHistoryTrend.getInstance().getModuleName());
                if(Config.BandwidthHistoryTrend==true){
                jedis.publish("BandwidthHistoryTrendSIG", "OK");
                BandwidthHistoryTrend.getInstance().getPoolRequest().add(new PoolRequest.Request(channel, message));
                System.out.println("Prijata sprava z webu: "+message);
                
                if(BandwidthHistoryTrend.getInstance().getState()==Thread.State.WAITING){
                synchronized(BandwidthHistoryTrend.getInstance()){
                    BandwidthHistoryTrend.getInstance().notify();
                }
                }
                }
            }break;
            case "AverageDownloadUploadPacket": {
                log.log(Level.INFO, "New message for module AverageOfTransferredDataPacket received", AverageDownloadUploadPacket.getInstance().getModuleName());
                if(Config.AverageOfTransferredDataPacket==true){
                jedis.publish("AverageDownloadUploadPacketSIG", "OK");
                AverageDownloadUploadPacket.getInstance().getPoolRequest().add(new PoolRequest.Request(channel, message));
                System.out.println("Prijata sprava z webu: "+message);
                
                if(AverageDownloadUploadPacket.getInstance().getState()==Thread.State.WAITING){
                synchronized(AverageDownloadUploadPacket.getInstance()){
                    AverageDownloadUploadPacket.getInstance().notify();
                }
                }
                }
            }break;
            case "MaximumDownloadUpload": {    
                log.log(Level.INFO, "New message for module MaximumDownloadUpload received", MaximumDownloadUpload.getInstance().getModuleName());
                if(Config.MaximumDownloadUpload==true){
                jedis.publish("MaximumDownloadUploadSIG", "OK");
                MaximumDownloadUpload.getInstance().getPoolRequest().add(new PoolRequest.Request(channel, message));
                System.out.println("Prijata sprava z webu: "+message);
                
                if(MaximumDownloadUpload.getInstance().getState()==Thread.State.WAITING){
                synchronized(MaximumDownloadUpload.getInstance()){
                    MaximumDownloadUpload.getInstance().notify();
                }
                }
                }
            }break;
            case "AmountOfTransferredDataPacket": {    
                log.log(Level.INFO, "New message for module AmountOfTransferredDataPacket received", AmountOfTransferredDataPacket.getInstance().getModuleName());
                if(Config.AmountOfTransferredDataPacket==true){
                jedis.publish("AmountOfTransferredDataPacketSIG", "OK");
                AmountOfTransferredDataPacket.getInstance().getPoolRequest().add(new PoolRequest.Request(channel, message));
                System.out.println("Prijata sprava z webu: "+message);
                
                if(AmountOfTransferredDataPacket.getInstance().getState()==Thread.State.WAITING){
                synchronized(AmountOfTransferredDataPacket.getInstance()){
                    AmountOfTransferredDataPacket.getInstance().notify();
                }
                }
                }
            }break;
            case "BandwidthHistoryTrendPacket": {    
                log.log(Level.INFO, "New message for module BandwidthHistoryTrendPacket received", BandwidthHistoryTrendPacket.getInstance().getModuleName());
                if(Config.BandwidthHistoryTrendPacket==true){
                jedis.publish("BandwidthHistoryTrendPacketSIG", "OK");
                BandwidthHistoryTrendPacket.getInstance().getPoolRequest().add(new PoolRequest.Request(channel, message));
                System.out.println("Prijata sprava z webu: "+message);
                
                if(BandwidthHistoryTrendPacket.getInstance().getState()==Thread.State.WAITING){
                synchronized(BandwidthHistoryTrendPacket.getInstance()){
                    BandwidthHistoryTrendPacket.getInstance().notify();
                }
                }
                }
            }break;
            case "AverageMiniTable": {    
                log.log(Level.INFO, "New message for module AverageMiniTable received", AverageMiniTable.getInstance().getModuleName());
                if(Config.AverageMiniTable==true){
                jedis.publish("AverageMiniTableSIG", "OK");
                AverageMiniTable.getInstance().getPoolRequest().add(new PoolRequest.Request(channel, message));
                System.out.println("Prijata sprava z webu: "+message);
                
                if(AverageMiniTable.getInstance().getState()==Thread.State.WAITING){
                synchronized(AverageMiniTable.getInstance()){
                    AverageMiniTable.getInstance().notify();
                }
                }
                }
            }break;
            case "HistoryTrendFlows": {    
                log.log(Level.INFO, "New message for module HistoryTrendFlows received", HistoryTrendFlows.getInstance().getModuleName());
                if(Config.HistoryTrendFlows==true){
                jedis.publish("HistoryTrendFlowsSIG", "OK");
                HistoryTrendFlows.getInstance().getPoolRequest().add(new PoolRequest.Request(channel, message));
                System.out.println("Prijata sprava z webu: "+message);
                
                
                if(HistoryTrendFlows.getInstance().getState()==Thread.State.WAITING){
                synchronized(HistoryTrendFlows.getInstance()){
                    HistoryTrendFlows.getInstance().notify();
                }
                }
                }
            }break;
            case "AverageDownloadUpload": {    
                log.log(Level.INFO, "New message for module AverageOfTransferredData received", AverageDownloadUpload.getInstance().getModuleName());
                if(Config.AverageOfTransferredData==true){
                jedis.publish("AverageDownloadUploadSIG", "OK");
                AverageDownloadUpload.getInstance().getPoolRequest().add(new PoolRequest.Request(channel, message));
                System.out.println("Prijata sprava z webu: "+message);
                
                if(AverageDownloadUpload.getInstance().getState()==Thread.State.WAITING){
                synchronized(AverageDownloadUpload.getInstance()){
                    AverageDownloadUpload.getInstance().notify();
                }
                }
                }
            }break;
            case "HistoryTable": {    
                log.log(Level.INFO, "New message for module HistoryTable received", HistoryTable.getInstance().getModuleName());
                if(Config.HistoryTable==true){
                jedis.publish("HistoryTableSIG", "OK");
                HistoryTable.getInstance().getPoolRequest().add(new PoolRequest.Request(channel, message));
                System.out.println("Prijata sprava z webu: "+message);
                
                
                if(HistoryTable.getInstance().getState()==Thread.State.WAITING){
                synchronized(HistoryTable.getInstance()){
                    HistoryTable.getInstance().notify();
                }
                }
                }
            }break;
            case "AmountMiniTable": {    
                log.log(Level.INFO, "New message for module AmountMiniTable received", AmountMiniTable.getInstance().getModuleName());
                if(Config.AmountMiniTable==true){
                jedis.publish("AmountMiniTableSIG", "OK");
                AmountMiniTable.getInstance().getPoolRequest().add(new PoolRequest.Request(channel, message));
                System.out.println("Prijata sprava z webu: "+message);
                
                if(AmountMiniTable.getInstance().getState()==Thread.State.WAITING){
                synchronized(AmountMiniTable.getInstance()){
                    AmountMiniTable.getInstance().notify();
                }
                }
                }
            }break;
            case "NumberOfFlows": {    
                log.log(Level.INFO, "New message for module NumberOfFlows received", NumberOfFlows.getInstance().getModuleName());
                if(Config.NumberOfFlows==true){
                jedis.publish("NumberOfFlowsSIG", "OK");
                NumberOfFlows.getInstance().getPoolRequest().add(new PoolRequest.Request(channel, message));
                System.out.println("Prijata sprava z webu: "+message);
                
                if(NumberOfFlows.getInstance().getState()==Thread.State.WAITING){
                synchronized(NumberOfFlows.getInstance()){
                    NumberOfFlows.getInstance().notify();
                }
                }
                }
            }break;
            case "PingTime": {    
                log.log(Level.INFO, "New message for module PingTime received", PingTime.getInstance().getModuleName());
                if(Config.PingTime==true){
                jedis.publish("PingTimeSIG", "OK");
                PingTime.getInstance().getPoolRequest().add(new PoolRequest.Request(channel, message));
                System.out.println("Prijata sprava z webu: "+message);
                
                
                if(PingTime.getInstance().getState()==Thread.State.WAITING){
                synchronized(PingTime.getInstance()){
                    PingTime.getInstance().notify();
                }
                }
                }
            }break;
            case "TopDownloader": {
                log.log(Level.INFO, "New message for module TopDownloader received", TopDownloader.getInstance().getModuleName());
                if(Config.TopDownloader==true){
                jedis.publish("TopDownloaderSIG", "OK");
                TopDownloader.getInstance().getPoolRequest().add(new PoolRequest.Request(channel, message));
                System.out.println("Prijata sprava z webu: "+message);
                
                
                if(TopDownloader.getInstance().getState()==Thread.State.WAITING){
                synchronized(TopDownloader.getInstance()){
                    TopDownloader.getInstance().notify();
                }                
                }
                }
            }break;
            case "TopUploader": {
                log.log(Level.INFO, "New message for module TopUploader received", TopUploader.getInstance().getModuleName());
                if(Config.TopUploader==true){
                jedis.publish("TopUploaderSIG", "OK");
                TopUploader.getInstance().getPoolRequest().add(new PoolRequest.Request(channel, message));
                System.out.println("Prijata sprava z webu: "+message);
                
                
                if(TopUploader.getInstance().getState()==Thread.State.WAITING){
                synchronized(TopUploader.getInstance()){
                    TopUploader.getInstance().notify();
                }                
                }
                }
            }break;
            default: log.log(Level.WARNING, "Modul {0} is not exist", channel); break;
        }
    }

    @Override
    public void onSubscribe(String channel, int subscribedChannels) {
        
        switch(channel){
            case "AmountOfTransferredData": 
                AmountOfTransferredData.getInstance().start();
                break;
            case "BandwidthHistoryTrend":
                BandwidthHistoryTrend.getInstance().start();
                break;
            case "AverageDownloadUploadPacket": 
                AverageDownloadUploadPacket.getInstance().start();
                break;
            case "MaximumDownloadUpload":
                MaximumDownloadUpload.getInstance().start();
                break;
            case "AmountOfTransferredDataPacket": 
                AmountOfTransferredDataPacket.getInstance().start();
                break;
            case "BandwidthHistoryTrendPacket": 
                BandwidthHistoryTrendPacket.getInstance().start();
                break;
            case "AverageMiniTable": 
                AverageMiniTable.getInstance().start();
                break;
            case "HistoryTrendFlows": 
                HistoryTrendFlows.getInstance().start();
                break;
            case "AverageDownloadUpload": 
                AverageDownloadUpload.getInstance().start();
                break;
            case "HistoryTable": 
                HistoryTable.getInstance().start();
                break;
            case "AmountMiniTable": 
                AmountMiniTable.getInstance().start();
                break;
            case "NumberOfFlows": 
                NumberOfFlows.getInstance().start();
                break;
            case "PingTime": 
                PingTime.getInstance().start();
                break;
            case "TopDownloader": 
                TopDownloader.getInstance().start();
                break;
            case "TopUploader":
                TopUploader.getInstance().start();
                break;
            default:
        }
    }

    @Override
    public void onUnsubscribe(String channel, int subscribedChannels) {
        System.out.println(channel);
        switch(channel){
            case "AmountOfTransferredData": 
                AmountOfTransferredData.getInstance().stopModule();
                break;
            case "BandwidthHistoryTrend":
                BandwidthHistoryTrend.getInstance().stopModule();
                break;
            case "AverageDownloadUploadPacket": 
                AverageDownloadUploadPacket.getInstance().stopModule();
                break;
            case "MaximumDownloadUpload":
                MaximumDownloadUpload.getInstance().stopModule();
                break;
            case "AmountOfTransferredDataPacket": 
                AmountOfTransferredDataPacket.getInstance().stopModule();
                break;
            case "BandwidthHistoryTrendPacket": 
                BandwidthHistoryTrendPacket.getInstance().stopModule();
                break;
            case "AverageMiniTable": 
                AverageMiniTable.getInstance().stopModule();
                break;
            case "HistoryTrendFlows": 
                HistoryTrendFlows.getInstance().stopModule();
                break;
            case "AverageDownloadUpload": 
                AverageDownloadUpload.getInstance().stopModule();
                break;
            case "HistoryTable": 
                HistoryTable.getInstance().stopModule();
                break;
            case "AmountMiniTable": 
                AmountMiniTable.getInstance().stopModule();
                break;
            case "NumberOfFlows": 
                NumberOfFlows.getInstance().stopModule();
                break;
            case "PingTime": 
                PingTime.getInstance().stopModule();
                break;
            case "TopDownloader":
                TopDownloader.getInstance().stopModule();
                break;
            case "TopUploader":
                TopUploader.getInstance().stopModule();
                break;
            default:
        }
    }

    @Override
    public void onPUnsubscribe(String pattern, int subscribedChannels) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void onPSubscribe(String pattern, int subscribedChannels) {

    }
    
    @Override
    public void onPMessage(String pattern, String channel, String message) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}