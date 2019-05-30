package com.wuweibi.bullet.conn;/**
 * Created by marker on 2018/1/10.
 */

import com.wuweibi.bullet.websocket.BulletAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 *
 *
 * @author marker
 * @create 2018-01-10 下午9:12
 **/
public final class CoonPool {
    /** 日志 */
    private Logger logger = LoggerFactory.getLogger(CoonPool.class);


    /** 缓存全部链接 */
    public static final Set<BulletAnnotation> connections = new CopyOnWriteArraySet<>();

    /** 根据客户端缓存链接 */
    public static final Map<String, List<BulletAnnotation>> clientConnections = new ConcurrentHashMap<>();


    /**
     * 新增一个链接
     * @param conn 链接对象
     */
    public void addConnection(BulletAnnotation conn){
        String deviceNo = conn.getDeviceNo();

        List<BulletAnnotation> list = clientConnections.get(deviceNo);
        if(list == null){
            synchronized (CoonPool.class){
                if(list == null){
                    list = new ArrayList<>();
                    clientConnections.put(deviceNo, list);
                }
            }
        }

        // 添加链接
        list.add(conn);
        connections.add(conn);

    }


    /**
     * 移除一个链接
     * @param conn 链接对象
     */
    public void removeConnection(BulletAnnotation conn) {
        String deviceNo = conn.getDeviceNo();
        connections.remove(conn);

//        List<BulletAnnotation> list = clientConnections.get(deviceNo);
//        list.remove(conn);
        clientConnections.remove(deviceNo); // 直接全部移除
    }


    // 每个客户端的链接树
    public static final Map<String, Integer> dataCount = new ConcurrentHashMap<>();


    /**
     * 根据客户端ID获取一个可用的链接
     * @param deviceNo
     * @return
     */
    public BulletAnnotation getByDeviceNo(String deviceNo) {

        List<BulletAnnotation> list = clientConnections.get(deviceNo);
        if(list ==null) return null;
        int len = list.size(); // 总链接个数
        if (len == 0) {
            return null;
        }

        return  list.get(0);

        // 计算命中的链接
//        Integer count = dataCount.get(deviceNo);
//        if(count == null){
//            count = 0;
//        }
//        int index = (count%len);
//        dataCount.put(deviceNo, index);
//        logger.debug("deviceNo[{}] 正在使用链接 Connection[{}]", deviceNo, index);
//        return list.get(index);

    }


    /**
     * 获取设备状态
     * @param deviceNo 设备编号
     * @return
     */
    public int getDeviceStatus(String deviceNo) {
        List<BulletAnnotation> list = this.clientConnections.get(deviceNo);
        if(list == null){
            return -1;
        }
        if(list.size() > 0){
            return 1;
        }
        return -1;
    }
}
