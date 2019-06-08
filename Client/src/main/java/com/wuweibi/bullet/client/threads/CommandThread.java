package com.wuweibi.bullet.client.threads;
/**
 * Created by marker on 2019/4/10.
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wuweibi.bullet.ConfigUtils;
import com.wuweibi.bullet.client.domain.MappingInfo;
import com.wuweibi.bullet.client.domain.NgrokConf;
import com.wuweibi.bullet.client.domain.Proto;
import com.wuweibi.bullet.client.domain.Tunnels;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

/**
 *
 *
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


    private Process process = null;


    /**
     * 构造
     * @param config
     */
    public CommandThread(MappingInfo config){
        this.config = config;

        this.mappingId = config.getId();
        // 解析为命令
        String projectPath = ConfigUtils.getClientProjectPath();
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
        InputStream inputStream = this.getClass().getResourceAsStream("/conf/ngrok.yml");

        NgrokConf testEntity = yaml.loadAs(inputStream, NgrokConf.class);//如果读入Map,这里可以是Mapj接口,默认实现为LinkedHashMap


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
        tunnels.setProto(proto);


        String mappingName = "mp_" + config.getId();

        testEntity.getTunnels().put(mappingName, tunnels);




        command.append(configPath).append(projectName).append(".yml ");

        if(ConfigUtils.getLogService()){// 开启日志
            command.append("-log="+ logsPath + projectName + ".log");
        }
        command.append("start ").append(mappingName);


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


    @Override
    public void run() {
        log.debug("run: {}", command);
        try {
            this.process = Runtime.getRuntime().exec(command);


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
        this.interrupt();
    }
}
