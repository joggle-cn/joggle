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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wuweibi.bullet.domain.domain.CandidateMsg;
import com.wuweibi.bullet.domain.domain.Message;
import com.wuweibi.bullet.domain.domain.OfferMsg;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;


/**
 * 
 * @author marker
 * @version 1.0
 */
@ServerEndpoint(value = "/ws/video/{user}")
public class VideoAnnotation {
   
    private static final Set<VideoAnnotation> connections =
            new CopyOnWriteArraySet<VideoAnnotation>();

    private String nickname;
    private Session session;

    public VideoAnnotation() { 
    }


    @OnOpen
    public void start(Session session, @PathParam("user")String user) {
        this.session = session;
        this.nickname = user; 
        connections.add(this);
        
        
        // 通知在线用户列表
        
        
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
    	JSONObject msg = JSONObject.parseObject(message);// (message, Message.class);
    	System.out.println(message);
    	int command = msg.getIntValue("command");
    	switch(command){
    	case Message.MSG_candidate:
    		CandidateMsg imsg = JSON.parseObject(message, CandidateMsg.class);
        	sendMsg(imsg.getToUser(), message); 
    		break;
    	case Message.MSG_OFFER:
    		OfferMsg imsg2 = JSON.parseObject(message, OfferMsg.class);
        	sendMsg(imsg2.getToUser(), message); 
    		;break; 
	    case Message.MSG_OFFER_RESP:
			OfferMsg imsg3 = JSON.parseObject(message, OfferMsg.class);
	    	sendMsg(imsg3.getToUser(), message); 
			;break;
		} 
    }




    @OnError
    public void onError(Throwable t) throws Throwable {
        System.out.println("Chat Error: " + t.toString() );
    }

    
    private static void sendMsg(String user,String msg) {
        for (VideoAnnotation client : connections) {
        	String result = "user not online!";
        	if(client.nickname.equals(user)){
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
                    broadcast(message);
                }
        	} 
        }
    }
    
    

    private static void broadcast(String msg) {
        for (VideoAnnotation client : connections) {
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
                broadcast(message);
            }
        }
    }
}
