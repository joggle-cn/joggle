/*
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.wuweibi.bullet.websocket;

import com.wuweibi.bullet.conn.CoonPool;
import com.wuweibi.bullet.protocol.MsgLogOpen;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.utils.SpringUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;


/**
 * 日志
 * @author marker
 * @version 1.0
 */
@ServerEndpoint(value = "/_ws/log/{id}")
public class LogAnnotation {
   
    public static final Set<LogAnnotation> connections =
            new CopyOnWriteArraySet<LogAnnotation>();

    private String nickname;
    private Session session;
    private Long mappingId;

    public LogAnnotation() { 
    }


    @OnOpen
    public void start(Session session, @PathParam("id") Long mappingId) {
        this.session = session;
        this.mappingId = mappingId;
        connections.add(this);


        // 通知对应的mapping
        // 找设备ID


        CoonPool pool = SpringUtils.getBean(CoonPool.class);
        DeviceMappingService deviceMappingService = SpringUtils.getBean(DeviceMappingService.class);


        String deviceNo = deviceMappingService.getDeviceNoByMappingId(this.mappingId);

        BulletAnnotation annotation = pool.getByDeviceNo(deviceNo);

        // 开启日志
        MsgLogOpen msgLogOpen = new MsgLogOpen();
        msgLogOpen.setMappingId(this.mappingId);
        msgLogOpen.setOpen(1);

        try {
            annotation.sendObject(msgLogOpen);
        } catch (IOException e) {
            e.printStackTrace();
        }


//        String message = String.format("* %s %s", nickname, "has joined.");
//        broadcast(message);
    }


    @OnClose
    public void end() {
        connections.remove(this);
//        String message = String.format("* %s %s",
//                nickname, "has disconnected.");
//        broadcast(message);
    }


    @OnMessage(maxMessageSize=2000999)
    public void incoming(String message) { 

    }




    @OnError
    public void onError(Throwable t) throws Throwable {
        System.out.println("Chat Error: " + t.toString() );
    }

    
    public static void sendMsg(String user,String msg) {
        for (LogAnnotation client : connections) {
        	String result = "user not online!";
            result = msg;
            try {
                client.session.getBasicRemote().sendText(result);
            } catch (IOException e) {
                e.printStackTrace();
                connections.remove(client);
                try {
                    client.session.close();
                } catch (IOException e1) {
                    // Ignore
                }
                String message = String.format("* %s %s",
                        client.nickname, "has been disconnected.");
//                broadcast(user, message);
            }
        }
    }
    
    

    public static void broadcast(Long mappingId, String msg) {
        for (LogAnnotation client : connections) {
            if(mappingId.compareTo(client.getMappingId()) == 0){
                try {
                    synchronized (client) {
                        client.session.getBasicRemote().sendText(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    connections.remove(client);
                    try {
                        client.session.close();
                    } catch (IOException e1) {
                        // Ignore
                    }
                    String message = String.format("* %s %s",
                            client.nickname, "has been disconnected.");
                    broadcast(mappingId, message);
                }
            }
        }
    }

    private Long getMappingId() {
        return this.mappingId;
    }
}
