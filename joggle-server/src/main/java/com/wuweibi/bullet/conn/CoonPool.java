package com.wuweibi.bullet.conn;
import com.wuweibi.bullet.protocol.Message;
import com.wuweibi.bullet.websocket.BulletAnnotation;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
public final class CoonPool {
    /** 日志 */
    private Logger logger = LoggerFactory.getLogger(CoonPool.class);

    /** 根据客户端缓存链接 */
    public final Map<String, BulletAnnotation> clientConnections = new ConcurrentHashMap<>();


    /**
     * 新增一个链接
     * @param conn 链接对象
     */
    public void addConnection(BulletAnnotation conn){
        String deviceNo = conn.getDeviceNo();
        BulletAnnotation bulletAnnotation = clientConnections.get(deviceNo);
        if(bulletAnnotation != null){
            conn.stop();
            return;
        }
        clientConnections.put(deviceNo, conn);
    }


    /**
     * 移除一个链接
     * @param conn 链接对象
     */
    public void removeConnection(BulletAnnotation conn) {
        if(conn == null){
            return;
        }
        String deviceNo = conn.getDeviceNo();
        clientConnections.remove(deviceNo); // 直接全部移除
        conn.stop();
    }


    /**
     * 根据设备编号移除链接
     * @param deviceNo 设备编号
     */
    public void removeByDeviceNo(String deviceNo) {
        logger.info("连接池.removeByDeviceNo 设备[{}] ", deviceNo);
        BulletAnnotation bulletAnnotation = clientConnections.get(deviceNo);
        if(bulletAnnotation  == null){
            return;
        }
        clientConnections.remove(deviceNo); // 直接全部移除
        bulletAnnotation.stop();
    }


    /**
     * 根据客户端ID获取一个可用的链接
     * @param deviceNo 设备编号
     * @return
     */
    public BulletAnnotation getByDeviceNo(String deviceNo) {
        BulletAnnotation bulletAnnotation = clientConnections.get(deviceNo);
        return bulletAnnotation;
    }


    /**
     * 获取设备状态
     * @param deviceNo 设备编号
     * @return
     */
    public int getDeviceStatus(String deviceNo) {
        BulletAnnotation bulletAnnotation = this.clientConnections.get(deviceNo);
        if(bulletAnnotation == null){
            return -1;
        }
        Session session = bulletAnnotation.getSession();
        return session.isOpen()? 1:-1;
    }


    public DeviceStatus getDeviceStatusEnum(String deviceNo) {
        BulletAnnotation bulletAnnotation = this.clientConnections.get(deviceNo);
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

    public void stop() {
        Set<String> sets = clientConnections.keySet();
        for(String key : sets){
            BulletAnnotation bulletAnnotation = clientConnections.get(key);
            bulletAnnotation.stop();
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
     * 广播所有客户端
     * @param deviceNo
     * @param msg
     */
    public void boradcast(String deviceNo, Message msg) {

        BulletAnnotation bulletAnnotation = this.getByDeviceNo(deviceNo);
        if(bulletAnnotation == null){
            return;
        }
        if(!bulletAnnotation.getSession().isOpen()){
            return;
        }

        bulletAnnotation.sendMessage(msg);

    }


    /**
     * 打印连接池信息
     */
    public void printDetailInfo(){
        log.info("============= clientConnections size {} ============= ", this.clientConnections.size());
        Set<String> sets = this.clientConnections.keySet();
        for(String key:sets){
            BulletAnnotation ba = this.clientConnections.get(key);
            log.info("deviceNo={}, session[{}], status={}", ba.getDeviceNo(), ba.getSession().isOpen(),ba.getStatus());
        }
        log.info("============= clientConnections size {} ============= ", this.clientConnections.size());
    }

}
