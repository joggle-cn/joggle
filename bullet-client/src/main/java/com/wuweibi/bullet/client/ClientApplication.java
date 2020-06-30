package com.wuweibi.bullet.client;

/**
 * Created by marker on 2019/4/10.
 */

import com.wuweibi.bullet.client.listener.CloseClientListener;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * 客户端Application
 *
 * @author marker
 * @create 2019-04-10 09:50
 **/
@SpringBootApplication
public class ClientApplication implements CommandLineRunner {

    /**
     * 入口
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {


        SpringApplication app = new SpringApplication(ClientApplication.class);
        app.addListeners(new CloseClientListener(ConnectionPool.getInstance()));
        app.run(args);
    }

    @Override
    public void run(String... strings) throws Exception {
        ConnectionPool pool = ConnectionPool.getInstance();

        // 启动线程池
        pool.startup();
        // 保持不退出
        while (true){
            Thread.sleep(10000L);
        }
    }
}
