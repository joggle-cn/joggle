package com.wuweibi.bullet.conn;

import com.wuweibi.bullet.protocol.Message;
import com.wuweibi.bullet.websocket.Bullet3Annotation;
import lombok.extern.slf4j.Slf4j;

import javax.websocket.Session;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 *
 * @author marker
 *    2018-01-10 下午9:12
 **/
@Slf4j
public final class WebsocketPool {

    /** 根据客户端缓存链接 */
    public final Map<String, Bullet3Annotation> clientConnections = new ConcurrentHashMap<>();


    /**
     * 新增一个链接
     * @param conn 链接对象
     */
    public void addConnection(Bullet3Annotation conn){
        String tunnelId = conn.getTunnelId().toString();
        Bullet3Annotation bulletAnnotation = clientConnections.get(tunnelId);
//        if (bulletAnnotation != null) {
//            conn.stop(String.format("通道服务%s已注册，请修改配置", tunnelId));
//            return;
//        }
        clientConnections.put(tunnelId, conn);
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
        String tunnelId = conn.getTunnelId().toString();
        clientConnections.remove(tunnelId); // 直接全部移除
        conn.stop(message);
    }





    /**
     * 根据客户端ID获取一个可用的链接
     * @param deviceNo 设备编号
     * @return
     */
    @Deprecated
    public Bullet3Annotation getByDeviceNo(String deviceNo) {
        Bullet3Annotation bulletAnnotation = clientConnections.get(deviceNo);
        return bulletAnnotation;
    }

    /**
     * 根据通道id获取设备
     * @param tunnelId 通道id
     * @return
     */
    public Bullet3Annotation getByTunnelId(Integer tunnelId) {
        Bullet3Annotation bulletAnnotation = clientConnections.get(tunnelId.toString());
        return bulletAnnotation;
    }





    public DeviceStatus getDeviceStatusEnum(String deviceNo) {
        Bullet3Annotation bulletAnnotation = this.clientConnections.get(deviceNo);
        if(bulletAnnotation == null){
            return DeviceStatus.OUTLINE;
        }
        Session session = bulletAnnotation.getSession();
        return session.isOpen()? DeviceStatus.ONLINE:DeviceStatus.OUTLINE;
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
        Set<String> sets = clientConnections.keySet();
        for(String key : sets){
            Bullet3Annotation bulletAnnotation = clientConnections.get(key);
            bulletAnnotation.stop("批量下线");
        }
    }


    /**
     * 判断是否存在
     * @param deviceNo
     * @return
     */
    public boolean exists(String deviceNo) {
        return clientConnections.containsKey(deviceNo);
    }


    /**
     * 广播所有客户端 TODO
     * @param deviceNo
     * @param msg
     */
    public void boradcast(String deviceNo, Message msg) {
        Bullet3Annotation bulletAnnotation = this.getByTunnelId( 1);
        if (bulletAnnotation == null) {
            return;
        }
        bulletAnnotation.sendMessage(deviceNo, msg);
    }


    /**
     * 打印连接池信息
     */
    public void printDetailInfo(){
        log.info("============= clientConnections size {} ============= ", this.clientConnections.size());
        Set<String> sets = this.clientConnections.keySet();
        for(String key:sets){
            Bullet3Annotation ba = this.clientConnections.get(key);
            log.info("deviceNo={}, session[{}]", 1, ba.getSession().isOpen());
        }
        log.info("============= clientConnections size {} ============= ", this.clientConnections.size());
    }

}
