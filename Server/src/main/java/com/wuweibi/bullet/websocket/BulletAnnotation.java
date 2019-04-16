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
import com.wuweibi.bullet.conn.CoonPool;
import com.wuweibi.bullet.entity.DeviceMapping;
import com.wuweibi.bullet.protocol.Message;
import com.wuweibi.bullet.protocol.MsgHead;
import com.wuweibi.bullet.protocol.MsgMapping;
import com.wuweibi.bullet.protocol.MsgProxyHttp;
import com.wuweibi.bullet.server.HandlerBytes;
import com.wuweibi.bullet.service.DeviceMappingService;
import com.wuweibi.bullet.service.DeviceOnlineService;
import com.wuweibi.bullet.utils.SpringUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;


/**
 * WebSocket服务
 *
 *
 * @author marker
 * @version 1.0
 */
@ServerEndpoint(value = "/tunnel/{deviceNo}/{connId}")
public class BulletAnnotation {
    /** 日志 */
    private Logger logger = LoggerFactory.getLogger(BulletAnnotation.class);

    /** session */
    private Session session;

    /** ID */
    private int id;

    /**  设备ID  */
    private String deviceNo;


    public BulletAnnotation() {
    }


    /**
     *
     * @param session session
     */
    @OnOpen
    public void start(Session session,
              // 设备ID
              @PathParam("deviceNo")String deviceNo ,
              // 链接ID
              @PathParam("connId")int connId
    ) {
        this.session  = session;
        this.deviceNo = deviceNo;// 设备ID
        this.id = connId;

        session.setMaxBinaryMessageBufferSize(101024000);
        session.setMaxIdleTimeout(60000);


        // 更新设备状态
        DeviceOnlineService deviceOnlineService = SpringUtils.getBean(DeviceOnlineService.class);
        deviceOnlineService.saveOrUpdateOnline(deviceNo);

        // 将链接添加到连接池
        CoonPool pool = SpringUtils.getBean(CoonPool.class);
        pool.addConnection(this);

        // 获取设备的配置数据,并将映射配置发送到客户端
        DeviceMappingService deviceMappingService = SpringUtils.getBean(DeviceMappingService.class);

        List<DeviceMapping> list = deviceMappingService.getDeviceAll(deviceNo);


        for(DeviceMapping entity :list){
            if(!org.apache.commons.lang3.StringUtils.isBlank(deviceNo)){
                JSONObject data = (JSONObject) JSON.toJSON(entity);
                MsgMapping msg = new MsgMapping(data.toJSONString());
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                try {
                    msg.write(outputStream);
                    // 包装了Bullet协议的
                    byte[] resultBytes = outputStream.toByteArray();
                    ByteBuffer buf = ByteBuffer.wrap(resultBytes);

                    this.getSession().getBasicRemote().sendBinary(buf);

                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    IOUtils.closeQuietly(outputStream);
                }
            }

        }

    }



    @OnClose
    public void end() {

        CoonPool pool = SpringUtils.getBean(CoonPool.class);
        pool.removeConnection(this);

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
                case Message.Heart:// 心跳消息
                    return;
            }
        } catch (IOException e) {
           logger.error("", e);
        }



        String sequence = msg.getSequence();
        logger.debug("sequence={}",sequence);

        byte[] responseData =  msg.getContent();

        logger.debug("接收到内容数据准备响应，长度为:{}", responseData.length);




        ChannelHandlerContext ctx = HandlerBytes.cache.get(sequence);

        if(ctx != null){
            try {
                // 在当前场景下，发送的数据必须转换成ByteBuf数组
                ByteBuf encoded = ctx.alloc().buffer(responseData.length);
                encoded.writeBytes(responseData);
                ctx.writeAndFlush(encoded).addListener(new ChannelFutureListener() {
                    public void operationComplete(ChannelFuture future) throws Exception {
                        if (!future.isSuccess()){
                            logger.debug("Failed to send a 413 Request Entity Too Large. | {}", future.cause());
                            ctx.close();
                        }
                    }
                });


            } finally {
                HandlerBytes.cache.remove(sequence);
            }
            logger.debug("count={}", HandlerBytes.cache.size());
        }

    }




    @OnError
    public void onError(Throwable t) throws Throwable {
        logger.error("Chat Error: " + t.toString() );

        updateOutLine();
    }


    /**
     * 更新为离线状态
     */
    private void updateOutLine(){
        // 更新设备状态
        DeviceOnlineService deviceOnlineService = SpringUtils.getBean(DeviceOnlineService.class);

        deviceOnlineService.updateOutLine(this.deviceNo);
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
    public String getDeviceNo() {
        return this.deviceNo;
    }

    /**
     * 服务器端主动关闭连接
     */
    public void stop() {
        try {
            this.session.close(
                    new CloseReason(CloseReason.CloseCodes.SERVICE_RESTART,
                            "重启设备！"));
        } catch (IOException e) {
            logger.error("", e);
        }
    }
}
