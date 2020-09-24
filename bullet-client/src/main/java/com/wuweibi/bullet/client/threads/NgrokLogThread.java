package com.wuweibi.bullet.client.threads;
/**
 * Created by marker on 2019/4/10.
 */

import com.wuweibi.bullet.client.Connection;
import com.wuweibi.bullet.client.ConnectionPool;
import com.wuweibi.bullet.client.domain.MappingInfo;
import com.wuweibi.bullet.protocol.MsgCommandLog;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Ngrok命令日志线程
 *
 * @author marker
 * @create 2019-04-10 12:50
 **/
@Slf4j
public class NgrokLogThread extends Thread {

    private String command;


    private MappingInfo config;

    /**
     * 唯一ID
     */
    private Long mappingId;

    private boolean isOpen;


    // 读可重复读，读写互斥 参考 ReadAndWriteLockTest
    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);


    private BufferedReader bufferedReader = null;


    private InputStream inputStream;

    /**
     * 构造
     *
     * @param config
     */
    public NgrokLogThread(MappingInfo config, InputStream inputStream) {
        this.config = config;
        this.mappingId = this.config.getId();
        this.inputStream = inputStream;
        this.isOpen = true;
    }


    @Override
    public void run() {
        try {
            // 获取执行命令后的输入流
            InputStreamReader buInputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));//装饰器模式
            bufferedReader = new BufferedReader(buInputStreamReader);//直接读字符串

            String str = null;

            ConnectionPool connectionPool = ConnectionPool.getInstance();

            Connection connection = connectionPool.getConn();

            while (this.isOpen) {
                Lock readLock = readWriteLock.readLock();
                readLock.lock();
                try {
                    str = bufferedReader.readLine();
                    if (str != null) {
                        if (connection.isActive() && connection.getSession().isOpen()) {
                            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                            MsgCommandLog msg = new MsgCommandLog();
                            msg.setMappingId(this.mappingId);
                            msg.setLine(str);
                            msg.write(outputStream);
//                            System.out.println(str);
                            // 包装了Bullet协议的
                            byte[] resultBytes = outputStream.toByteArray();
                            outputStream.close();
                            ByteBuffer buf = ByteBuffer.wrap(resultBytes);

                            connection.getSession().getBasicRemote().sendBinary(buf);
                        }

                    }
                } catch (Exception e) {
                    this.isOpen = false;
                } finally {
                    readLock.unlock();
                }
            }

        } catch (Exception e) {
            log.error("", e);
            this.stopThread();
        }

    }


    /**
     * 停止线程
     */
    public void stopThread() {
        this.isOpen = false;


        readWriteLock.writeLock().lock();
        try {
            this.interrupt();
        } finally {
            readWriteLock.writeLock().unlock();
        }
    }


}
