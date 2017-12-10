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
import com.wuweibi.bullet.entity.DeviceMapping;
import com.wuweibi.bullet.entity.DeviceOnline;
import com.wuweibi.bullet.protocol.Message;
import com.wuweibi.bullet.protocol.MsgHead;
import com.wuweibi.bullet.protocol.MsgProxyHttp;
import com.wuweibi.bullet.server.HandlerBytes;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.service.DeviceOnlineService;
import com.wuweibi.bullet.utils.SpringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.ByteArrayInputStream;
import java.io.IOException;
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
        this.session  = session;
        this.deviceId = deviceId;// 设备ID
        session.setMaxBinaryMessageBufferSize(101024000);
        session.setMaxIdleTimeout(60000);

        // 注册相关信息
        connections.add(this);

        // 更新设备状态
        DeviceOnlineService deviceOnlineService = SpringUtils.getBean(DeviceOnlineService.class);

        deviceOnlineService.saveOrUpdateOnline(deviceId);

    }



    @OnClose
    public void end() {
        connections.remove(this);

        updateOutLine();

    }


    @OnMessage
    public void incoming(byte[] bytes) {

        ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
        MsgHead head = new MsgHead();
        MsgProxyHttp msg = null;
        try {
            head.read(bis);//读取消息头
            switch (head.getCommand()) {
                case Message.Proxy_Http:// Bind响应命令
                    msg = new MsgProxyHttp(head);
                    msg.read(bis);
                    ;break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }



        String sequence =  msg.getSequence();
        byte[] responseData =  msg.getContent();



        synchronized (BulletAnnotation.class){

            ChannelHandlerContext ctx = HandlerBytes.cache.get(sequence);

            if(ctx != null){
                // 在当前场景下，发送的数据必须转换成ByteBuf数组
                ByteBuf encoded = ctx.alloc().buffer(responseData.length);
                encoded.writeBytes(responseData);
                ctx.write(encoded);
                ctx.flush();
                ctx.close();
                HandlerBytes.cache.remove(sequence);
                logger.debug("count={}", HandlerBytes.cache.size());
                System.out.println(HandlerBytes.cache.size());
            }

        }
    }




    @OnError
    public void onError(Throwable t) throws Throwable {
        t.printStackTrace();
        System.out.println("Chat Error: " + t.toString() );


        updateOutLine();
    }


    private void updateOutLine(){

        // 更新设备状态
        DeviceOnlineService deviceOnlineService = SpringUtils.getBean(DeviceOnlineService.class);

        deviceOnlineService.updateOutLine(this.deviceId);
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
