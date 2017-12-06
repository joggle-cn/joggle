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

import com.wuweibi.bullet.ByteUtils;
import com.wuweibi.bullet.server.HandlerBytes;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;


/**
 * WebSocket服务
 *
 *
 * @author marker
 * @version 1.0
 */
@ServerEndpoint(value = "/tunnel/{user}")
public class BulletAnnotation {
    private Logger logger = LoggerFactory.getLogger(BulletAnnotation.class);

    /**  */
    public static final Set<BulletAnnotation> connections =
            new CopyOnWriteArraySet<>();
    /**  */
    private Session session;

    /**
     * 设备ID
     * */
    private String deviceId;


    public BulletAnnotation() {
    }


    /**
     *
     * @param session session
     */
    @OnOpen
    public void start(Session session, @PathParam("user")String deviceId) {
        this.session = session;
        this.deviceId = deviceId;// 设备ID
        session.setMaxBinaryMessageBufferSize(101024000);
        session.setMaxIdleTimeout(60000);

        // 注册相关信息


        connections.add(this);

    }



    @OnClose
    public void end() {
        connections.remove(this);
    }


    @OnMessage
    public void incoming(byte[] bytes) {

        byte[] timeBytes =  new byte[8];
        System.arraycopy(bytes, 0, timeBytes, 0, 8);

        logger.debug("读取请求信息...");
        long time = ByteUtils.bytes2Long(timeBytes);
        logger.debug("time = {}", ByteUtils.bytes2Long(timeBytes));
        int size = bytes.length - 8;
        logger.debug("size = {}", size);
        byte[] responseData = new byte[size];
        System.arraycopy(bytes, 8, responseData, 0, size);


        synchronized (BulletAnnotation.class){


            ChannelHandlerContext ctx = HandlerBytes.cache.get(time);


            // 在当前场景下，发送的数据必须转换成ByteBuf数组
            ByteBuf encoded = ctx.alloc().buffer(responseData.length);
            encoded.writeBytes(responseData);
            ctx.write(encoded);
            ctx.flush();
            ctx.close();
            HandlerBytes.cache.remove(time);
            logger.debug("count={}", HandlerBytes.cache.size());
            System.out.println(HandlerBytes.cache.size());

        }
    }




    @OnError
    public void onError(Throwable t) throws Throwable {
        t.printStackTrace();
        System.out.println("Chat Error: " + t.toString() );
    }



    /**
     * 获取会话信息
     * @return
     */
    public Session getSession() {
        return this.session;
    }


    /**
     * 获取设备ID
     * @return
     */
    public String getDeviceId(){
        return this.deviceId;
    }


}
