package com.wuweibi.bullet.conn;

import com.wuweibi.bullet.protocol.Message;
import com.wuweibi.bullet.utils.Utils;
import com.wuweibi.bullet.websocket.Bullet3Annotation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

import static com.wuweibi.bullet.protocol.Message.CONTROL_SERVER_WRAPPER;

/**
 *
 *
 * @author marker
 *    2018-01-10 下午9:12
 **/
@Slf4j
public final class WebsocketPool {

    /** 根据客户端缓存链接 */
    public final Map<Integer, List<Bullet3Annotation>> clientConnections = new ConcurrentHashMap<>();


    /**
     * 新增一个链接
     * @param conn 链接对象
     */
    public void addConnection(Bullet3Annotation conn){
        Integer tunnelId = conn.getTunnelId();
        List<Bullet3Annotation> bulletAnnotations = clientConnections.get(tunnelId);
        if (CollectionUtils.isEmpty(bulletAnnotations) ) {
            bulletAnnotations = new ArrayList<>();
            clientConnections.put(tunnelId, bulletAnnotations);
        }
        conn.setIndex(bulletAnnotations.size() + 1);
        bulletAnnotations.add(conn); // 由于开发集群模式，只管注册
    }

    /**
     * 移除一个链接
     * @param conn 链接对象
     * @param message
     */
    public void removeConnection(Bullet3Annotation conn, String message) {
        if (conn == null) {
            return;
        }
        Integer tunnelId = conn.getTunnelId();
        List<Bullet3Annotation> annotations = clientConnections.get(tunnelId); // 直接全部移除
        if (CollectionUtils.isEmpty(annotations)) {
            return;
        }
        annotations.remove(conn);

        conn.stop(message);
    }







    /**
     * 根据通道id获取设备
     * @param tunnelId 通道id
     * @return
     */
    public List<Bullet3Annotation> getByTunnelId(Integer tunnelId) {
        List<Bullet3Annotation> bulletAnnotation = clientConnections.get(tunnelId);
        return bulletAnnotation;
    }


    /**
     * websoket链接数量
     * @return
     */
    public Integer count() {
        return clientConnections.size();
    }


    /**
     * 全局
     */
    public void stop() {
        Set<Integer> sets = clientConnections.keySet();
        for(Integer key : sets){
            List<Bullet3Annotation> bulletAnnotation = clientConnections.get(key);
            bulletAnnotation.forEach((annotation)->{ // 直接全部移除
                annotation.stop("批量下线");
            });
        }
    }


    /**
     * 判断通道链接是否存在
     * @param tunnelId 通道id
     * @return
     */
    public boolean exists(Integer tunnelId) {
        return !CollectionUtils.isEmpty(clientConnections.get(tunnelId));
    }


    /**
     * 广播所有客户端
     * @param deviceNo
     * @param msg
     */
    public void boradcast(Integer tunnelId, String deviceNo, Message msg) {
        List<Bullet3Annotation> bulletAnnotations = this.getByTunnelId(tunnelId);
        if (CollectionUtils.isEmpty(bulletAnnotations)) {
            return;
        }
        bulletAnnotations.forEach((annotation)->{
            annotation.sendMessage(deviceNo, msg);
        });
    }


    /**
     * 打印连接池信息
     */
    public void printDetailInfo(){
        log.info("============= clientConnections size {} ============= ", this.clientConnections.size());
        Set<Integer> sets = this.clientConnections.keySet();
        for(Integer key:sets){
            List<Bullet3Annotation> ba = this.clientConnections.get(key);
            ba.forEach((item-> {
                log.info("tunnelId={}, session[{}]", item.getTunnelId(), item.getSession().isOpen());
            }));
        }
        log.info("============= clientConnections size {} ============= ", this.clientConnections.size());
    }

    public Object getInfo() {
        Map<Integer, String> data =  new HashMap<>();
        for(Map.Entry<Integer, List<Bullet3Annotation>> item:clientConnections.entrySet()){
            data.put(item.getKey(), item.getValue().toString());
        };



        return data;
    }


    /**
     * 获取所有节点链接
     * @return
     */
    public Stream<Bullet3Annotation> listStream() {
        return clientConnections.values().stream().flatMap(List::stream);
    }


    /**
     * 发送设备消息
     *
     * @param serverTunnelId 通道id
     * @param deviceNo       设备编号
     * @param msg 消息
     */
    public void sendMessage(Integer serverTunnelId, String deviceNo, Message msg) {
        List<Bullet3Annotation> annotations = this.getByTunnelId(serverTunnelId);
        if (CollectionUtils.isEmpty(annotations)) {// 设备不在线
            return;
        }
        // 发送消息到joggle客户端
        annotations.forEach((annotation)->{
            annotation.sendMessage(deviceNo, msg);
        });
    }

    public void sendMessageBytes(int type, Integer serverTunnelId, String deviceNo, byte[] data) {
        List<Bullet3Annotation> annotations = this.getByTunnelId(serverTunnelId);
        if (CollectionUtils.isEmpty(annotations)) {// 设备不在线
            return;
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.write(Utils.IntToBytes4(type));
            outputStream.write(Utils.IntToBytes4(deviceNo.length()));
            outputStream.write(deviceNo.getBytes(StandardCharsets.UTF_8));
            outputStream.write(data);
            // 包装了Bullet协议的
            byte[] resultBytes = outputStream.toByteArray();
            ByteBuffer buf = ByteBuffer.wrap(resultBytes);

            annotations.forEach((annotation)->{
                try {
                    annotation.getSession().getBasicRemote().sendBinary(buf, true);
                } catch (IOException e) {
                    log.error("", e);
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            log.error("", e);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
    }

    public void sendMessageToServer(Integer serverTunnelId, Message msg) {
        log.info("Control -> Server: {} {}", msg.getCommand(),msg.getSequence());

        List<Bullet3Annotation> annotations = this.getByTunnelId(serverTunnelId);
        if (CollectionUtils.isEmpty(annotations)) {// 设备不在线
            return;
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try {
            outputStream.write(Utils.IntToBytes4(CONTROL_SERVER_WRAPPER));
            msg.write(outputStream);
            // 包装了Bullet协议的
            byte[] resultBytes = outputStream.toByteArray();
            ByteBuffer buf = ByteBuffer.wrap(resultBytes);
            annotations.forEach((annotation)->{
                try {
                    annotation.getSession().getBasicRemote().sendBinary(buf, true);
                } catch (IOException e) {
                    log.error("", e);
                    throw new RuntimeException(e);
                }
            });
        } catch (Exception e) {
            log.error("", e);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }

    }
}
