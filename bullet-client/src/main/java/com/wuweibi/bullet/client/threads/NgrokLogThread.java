package com.wuweibi.bullet.client.threads;
/**
 * Created by marker on 2019/4/10.
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wuweibi.bullet.client.domain.MappingInfo;
import com.wuweibi.bullet.client.domain.NgrokConf;
import com.wuweibi.bullet.client.domain.Proto;
import com.wuweibi.bullet.client.domain.Tunnels;
import com.wuweibi.bullet.client.utils.ConfigUtils;
import com.wuweibi.bullet.utils.FileTools;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.charset.Charset;

import static com.wuweibi.bullet.client.utils.ConfigUtils.getClientProjectPath;

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

            while(true){
                str = bufferedReader.readLine();
                if(str != null ){
                    System.out.println(str);

                }

                str = errorBufferedReader.readLine();
                if(str != null ){
                    System.out.println(str);
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
