package com.wuweibi.bullet.ngrokd;
/**
 * Created by marker on 2019/4/10.
 */

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import static com.wuweibi.bullet.utils.ConfigUtils.getBulletDomain;
import static com.wuweibi.bullet.utils.ConfigUtils.getServerProjectPath;


/**
 *
 * Ngrok命令线程
 *
 * @author marker
 * @create 2020-02-27 12:50
 **/
@Slf4j
public class NgrokdThread extends Thread  {

    private String command;


    private Process process = null;


    /**
     * 构造
     */
    public NgrokdThread( ){


        // 解析为命令
        String projectPath = getServerProjectPath();
        String domain = getBulletDomain();

        log.debug("BulletServer项目路径：{}", projectPath);


        String privkeyPath   = projectPath + "/conf/privkey.pem";
        String fullchainPath = projectPath + "/conf/fullchain.pem";
        String ngrokdLogPath = projectPath + "/logs/ngrokd.log";

        this.command = projectPath + "/bin/ngrokd" +
            " -tlsKey=" + privkeyPath +
            " -tlsCrt=" + fullchainPath +
            " -domain=" + domain +
            " -httpAddr=:80" +
            " -httpsAddr=:443" +
            " -log=" + ngrokdLogPath +
            " -tunnelAddr=:8083";

//        StringBuilder command = new StringBuilder(projectPath + "/bin/ngrokd -config=");
//
//        this.command = command.toString();
    }




    @Override
    public void run() {
        log.debug("run: {}", command);
        try {
            this.process = Runtime.getRuntime().exec(command);

            //取得命令结果的输出流
            new OutPrintlnThread(process.getInputStream()).start();
            new OutPrintlnThread(process.getErrorStream()).start();

        } catch (IOException e) {
            log.error("", e);
        }

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
