package com.wuweibi.bullet.client.threads;
/**
 * Created by marker on 2019/4/10.
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wuweibi.bullet.client.Connection;
import com.wuweibi.bullet.client.ConnectionPool;
import com.wuweibi.bullet.client.domain.MappingInfo;
import com.wuweibi.bullet.client.domain.NgrokConf;
import com.wuweibi.bullet.client.domain.Proto;
import com.wuweibi.bullet.client.domain.Tunnels;
import com.wuweibi.bullet.client.utils.ConfigUtils;
import com.wuweibi.bullet.protocol.MsgCommandLog;
import com.wuweibi.bullet.utils.FileTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import static com.wuweibi.bullet.client.utils.ConfigUtils.getClientProjectPath;

/**
 *
 * Ngrok命令线程
 *
 * @author marker
 * @create 2019-04-10 12:50
 **/
@Slf4j
public class CommandThread extends Thread  {

    private String command;


    private MappingInfo config;

    /**
     * 唯一ID
     */
    private Long mappingId;


    /**
     * 日志输出开关
     */
    private boolean isLogOpen = false;


    private Process process = null;


    // 读可重复读，读写互斥 参考 ReadAndWriteLockTest
    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock(true);




    /**
     * 构造
     * @param config
     */
    public CommandThread(MappingInfo config){
        this.config = config;

        this.mappingId = config.getId();
        // 解析为命令
        String projectPath = getClientProjectPath();
        // 生成文件目录
        String configPath = projectPath + "/conf/domain/";
        String logsPath   = projectPath + "/logs/domain/";
        new File(configPath).mkdirs();
        new File(logsPath).mkdirs();


        String projectName = config.getHostname() + config.getProtocol() + config.getDomain();



        // 去除系统判定Ngrok命令太大了
//        String osName = ConfigUtils.getOSName();
//        log.debug("os name = {}", osName);


        StringBuilder command = new StringBuilder(projectPath + "/bin/ngrok -config=");



        Yaml yaml = new Yaml();

        String configYmlFile = projectPath + "/conf/ngrok.yml";

        String ymlText = "";
        try {
            ymlText = FileTools.getFileContet(new File(configYmlFile), FileTools.FILE_CHARACTER_UTF8);
        } catch (IOException e) {
            log.error("", e);
        }

        NgrokConf testEntity = yaml.loadAs(ymlText, NgrokConf.class);//如果读入Map,这里可以是Mapj接口,默认实现为LinkedHashMap

        // 动态获取客户端Id
        testEntity.setClient_no(ConfigUtils.getDeviceNo());

        Tunnels tunnels = new Tunnels();
        tunnels.setSubdomain(config.getDomain());
        tunnels.setHostname(config.getHostname());
        Proto proto = new Proto();
        if(config.getProtocol() == 1){// http
            proto.setHttp(config.getHost()+':'+config.getPort());
            tunnels.setBind_tls(config.getBindTls());
        } else if(config.getProtocol() == 2){ // tcp
            proto.setTcp(config.getHost()+':'+config.getPort());
            tunnels.setRemote_port(this.config.getRemotePort());
        } else if(config.getProtocol() == 3) { // https
            proto.setHttps(config.getHost()+':'+config.getPort());
        }


        // 配置简单认证
        if(config.getProtocol() == 3 || config.getProtocol() == 1){
            if(StringUtils.isNotBlank(config.getAuth())){
                tunnels.setAuth(config.getAuth());
            }
        }

        tunnels.setProto(proto);



        String mappingName = "mp_" + config.getId();

        testEntity.getTunnels().put(mappingName, tunnels);


        command.append(configPath).append(projectName).append(".yml ");

        if(ConfigUtils.getLogService()){// 开启日志
//            command.append("-log="+ logsPath + projectName + ".log");
            command.append("-log=stdout");
        }
        command.append(" start ").append(mappingName);


        try {

            File file= new File(configPath + projectName + ".yml");
            String datajson =  JSON.toJSONString(testEntity);

            JSONObject data = JSON.parseObject(datajson);

            yaml.dump(data, new FileWriter(file));


        } catch (IOException e) {
            log.error("", e);
        }


        this.command = command.toString();


        // 睡眠1秒保证文件被写入
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    private InputStream inputStream;




    @Override
    public void run() {
        log.debug("run: {}", command);
        try {
            this.process = Runtime.getRuntime().exec(command);
            this.inputStream = process.getInputStream();

            // 获取执行命令后的输入流
            InputStreamReader buInputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));//装饰器模式
            BufferedReader bufferedReader = new BufferedReader(buInputStreamReader);//直接读字符串

            String str = null;

            ConnectionPool connectionPool = ConnectionPool.getInstance();

            Connection connection = connectionPool.getConn();

            while (true) {
                Lock readLock = readWriteLock.readLock();
                readLock.lock();
                try {
                    str = bufferedReader.readLine();
                    // 不管日志有没有打开都需要消费
                    if(str == null || !this.isLogOpen){
                        log.debug("ngrok: {}", str);
                        continue;
                    }
                    if (connection.isActive() && connection.getSession().isOpen()) {
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

                        MsgCommandLog msg = new MsgCommandLog();
                        msg.setMappingId(this.mappingId);
                        msg.setLine(str);
                        msg.write(outputStream);
                        // 包装了Bullet协议的
                        byte[] resultBytes = outputStream.toByteArray();
                        outputStream.close();
                        ByteBuffer buf = ByteBuffer.wrap(resultBytes);

                        connection.getSession().getBasicRemote().sendBinary(buf);
                    }

                } catch (Exception e) {
                    log.error("", e);
                } finally {
                    readLock.unlock();
                }
            }
        } catch (IOException e) {
            log.error("", e);
        }

    }

    public Long getMappingId(){
        return this.mappingId;
    }


    /**
     * 停止线程
     */
    public void stopThread(){
        if(this.process != null) {
            this.process.destroy();
        }
        this.isLogOpen = false;

        log.debug("准备停止[{}]Ngrok线程", this.config.getId());
        this.interrupt();
    }

    public void openLog() {
        log.debug("准备开启[{}]日志数据流", this.config.getId());
        this.isLogOpen = true;
    }

    public void closeLog(){
        log.debug("准备停止[{}]日志线程", this.config.getId());
        this.isLogOpen = false;
    }
}
