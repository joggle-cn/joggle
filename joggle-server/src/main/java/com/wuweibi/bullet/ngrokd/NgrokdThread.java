package com.wuweibi.bullet.ngrokd;
/**
 * Created by marker on 2019/4/10.
 */

import com.wuweibi.bullet.utils.ConfigUtils;
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

        // 服务端443的证书
        String privkeyPath   = projectPath + "/conf/server/privkey.pem";
        String fullchainPath = projectPath + "/conf/server/fullchain.pem";

        // ngrok通道的证书
        String tunnelPrivkeyPath   = projectPath + "/conf/tunnel/privkey.pem";
        String tunnelFullchainPath = projectPath + "/conf/tunnel/fullchain.pem";

        String ngrokdLogPath = projectPath + "/logs/ngrokd.log";

        this.command = projectPath + "/bin/ngrokd" +
            " -tlsKey=" + privkeyPath +
            " -tlsCrt=" + fullchainPath +
            " -tlsTunnelKey=" + tunnelPrivkeyPath +
            " -tlsTunnelCrt=" + tunnelFullchainPath +
            " -domain=" + domain +
            " -httpAddr=:" + ConfigUtils.getHttpPort() +
            " -httpsAddr=:" + ConfigUtils.getHttpsPort() +
            " -log=" + ngrokdLogPath +
            " -serverUrl=http://localhost:8081"  +
            " -tunnelAddr=:" + ConfigUtils.getTunnelPort();

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
