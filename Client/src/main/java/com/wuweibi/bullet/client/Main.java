package com.wuweibi.bullet.client;

/**
 * Created by marker on 2017/12/11.
 */

import com.wuweibi.bullet.ConfigUtils;

import javax.websocket.ContainerProvider;
import javax.websocket.DeploymentException;
import javax.websocket.Session;
import javax.websocket.WebSocketContainer;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Timer;

/**
 * 启动主要运行类
 *
 * (目前采用多个长链接，且有重试机制)
 *
 *
 * @author marker
 * @create 2017-12-11 下午9:08
 **/
public class Main {


    /**
     * 程序入口
     * @param args
     */
    public static void main(String[] args) throws URISyntaxException, IOException, DeploymentException, InterruptedException {

        ConnectionPool pool = new ConnectionPool();
        pool.setConfig(ConfigUtils.getProperties());


        // 启动线程池
        pool.startup();

        // 保持不退出
        while (true){
            Thread.sleep(3000000L);
        }

    }


}
