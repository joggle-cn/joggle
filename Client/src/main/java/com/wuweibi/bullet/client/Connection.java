package com.wuweibi.bullet.client;
/**
 * Created by marker on 2018/1/8.
 */

import com.wuweibi.bullet.ConfigUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;

/**
 * @author marker
 * @create 2018-01-08 下午8:15
 **/
public class Connection {

    private Logger logger = LoggerFactory.getLogger(Connection.class);

    private WebSocketContainer container = ContainerProvider.getWebSocketContainer();


    // WebSocket session
    private Session session;

    private BulletClient client = new BulletClient();



    /** 重试链接次数 */
    private int count = 0;

    public Connection() {  }




    /**
     * 是否活跃
     * @return
     */
    public boolean isActive(){
        return session.isOpen();
    }



    /**
     * 打开WebSocket链接
     */
    public void open() throws Exception {
        client.setConnection(this);
        boolean status = true;

        // while得作用是链接成功才会断开
        while (status){

            String tunnel   = ConfigUtils.getTunnel();// 通道服务器
            String deviceNo =  ConfigUtils.getDeviceNo(); // 设备ID

            String url = tunnel + "/" + deviceNo;

            // 获取WebSocket连接器，其中具体实现可以参照websocket-api.jar的源码,Class.forName("org.apache.tomcat.websocket.WsWebSocketContainer");

            logger.info("websocket to {}", url);


            try {
                this.session = container.connectToServer(client, new URI(url)); // 连接会话
                count = 0; // 初始化链接次数。
                status = false;
            } catch (DeploymentException e){
                logger.error("websocket connection faild! wait 10s try angain! reason: {}", e.getMessage());
                Thread.sleep(10000L);
                // 如果已经链接过了的情况与Clouse冲突的，直接关闭返回
                if(this.session != null) {
                    if(!this.session.isOpen()){ // 关闭了
                        this.session = null;
                        logger.error("websocket connection closed!!!");
                    }
                }

                count++;
                logger.debug("Connection 第{}次尝试连接",  count);


            } catch (Exception e){
                logger.error("", e);
                Thread.sleep(10000L);

            } finally {

            }
        }

    }


    /**
     * 再次打开连接
     * （等待30秒，重试次数20次）
     */
    public void opeAngain() {
        try {
            count++;
            logger.debug("Connection 第{}次尝试连接", count);
            // 无限重试
            open();
        } catch (Exception e) {
            logger.error(e.getMessage());
        } finally {

        }
    }


    /**
     * 停止运行
     */
    public void stop() {
        try {
            this.session.close(new CloseReason(CloseReason.CloseCodes.NORMAL_CLOSURE, "客户端停止运行了！"));
        } catch (IOException e) {
            logger.error("", e);
        }
    }
}
