package com.wuweibi.bullet.client.threads;
/**
 * Created by marker on 2019/4/10.
 */

import com.wuweibi.bullet.client.Connection;
import com.wuweibi.bullet.client.ConnectionPool;
import com.wuweibi.bullet.client.domain.MappingInfo;
import com.wuweibi.bullet.client.service.CommandThreadPool;
import com.wuweibi.bullet.client.service.SpringUtil;
import com.wuweibi.bullet.protocol.MsgCommandLog;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 *
 * Ngrok命令日志线程
 *
 * @author marker
 * @create 2019-04-10 12:50
 **/
@Slf4j
public class NgrokLogThread extends Thread  {

    private String command;


    private MappingInfo config;

    /**
     * 唯一ID
     */
    private Long mappingId;


    private Process process = null;


    /**
     * 构造
     * @param config
     */
    public NgrokLogThread(MappingInfo config, Process process){
        this.config = config;
        this.process = process;
        this.mappingId = this.config.getId();
    }


    @Override
    public void run() {
        log.debug("run: {}", command);
        try {
            //获取执行命令后的输入流
            InputStream inputStream = process.getInputStream();
            InputStreamReader buInputStreamReader = new InputStreamReader(inputStream , Charset.forName("UTF-8"));//装饰器模式
            BufferedReader bufferedReader = new BufferedReader(buInputStreamReader);//直接读字符串

            InputStream errorStream = process.getInputStream();
            InputStreamReader errorInputStreamReader = new InputStreamReader(errorStream,  Charset.forName("UTF-8"));//装饰器模式
            BufferedReader errorBufferedReader = new BufferedReader(errorInputStreamReader);//直接读字符串
            String str = null;

            CommandThreadPool commandThreadPool = SpringUtil.getBean(CommandThreadPool.class);

            ConnectionPool connectionPool = ConnectionPool.getInstance();

            Connection connection = connectionPool.getConn();

            while(true){
                str = bufferedReader.readLine();
                if(str != null ){
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                    MsgCommandLog msg = new MsgCommandLog();
                    msg.setMappingId(this.mappingId);
                    msg.setLine(str);
                    msg.write(outputStream);

                    // 包装了Bullet协议的
                    byte[] resultBytes = outputStream.toByteArray();
                    ByteBuffer buf = ByteBuffer.wrap(resultBytes);

                    connection.getSession().getBasicRemote().sendBinary(buf);
                }

                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    log.error("", e);
                }
            }

        } catch (IOException e) {
            log.error("", e);
        }

    }



}
