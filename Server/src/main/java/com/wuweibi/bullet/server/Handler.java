package com.wuweibi.bullet.server;/**
 * Created by marker on 2017/11/19.
 */

import com.wuweibi.bullet.ByteUtils;
import com.wuweibi.bullet.Sequence;
import com.wuweibi.bullet.websocket.BulletAnnotation;

import java.io.InputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 * 服务器接收到请求，准备发送代理请求
 *
 * @author marker
 * @create 2017-11-19 下午5:47
 **/
@Deprecated
public class Handler implements Runnable{


    public  static  Map<Long, Socket> cache = new HashMap<>();
    private Socket socket;
    public Handler(Socket socket){
        this.socket = socket;
    }

    private Sequence sequence = new Sequence();



    /**
     * 线程
     */
    public void run(){
            long time = sequence.next();

            Handler.cache.put(time, socket);
            try{
                InputStream is  = socket.getInputStream();

                byte[] bytes = ByteUtils.input2byte(is);
                // 根据域名判断应该请求哪个客户端

                byte[] timebytes = ByteUtils.long2Bytes(time);
                byte[] results = ByteUtils.byteMerger(timebytes, bytes);


                for (BulletAnnotation client : BulletAnnotation.connections) {

                    ByteBuffer buf = ByteBuffer.wrap(results);

                    client.getSession().getAsyncRemote().sendBinary(buf);

                    System.out.println("发送消息个内网机器");

                }
            } catch (Exception e){
                e.printStackTrace();
            }
    }



    public Socket getSocket() {
        return this.socket;
    }


}