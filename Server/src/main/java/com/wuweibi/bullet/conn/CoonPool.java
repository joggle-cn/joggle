package com.wuweibi.bullet.conn;/**
 * Created by marker on 2018/1/10.
 */

import com.wuweibi.bullet.websocket.BulletAnnotation;
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
 * @create 2018-01-10 下午9:12
 **/
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
            bulletAnnotation.stop();
        }
        clientConnections.put(deviceNo, conn);
    }


    /**
     * 移除一个链接
     * @param conn 链接对象
     */
    public void removeConnection(BulletAnnotation conn) {
        if(conn != null){
            String deviceNo = conn.getDeviceNo();
            clientConnections.remove(deviceNo); // 直接全部移除

            conn.stop();
        }
    }


    /**
     * 根据客户端ID获取一个可用的链接
     * @param deviceNo
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
}
