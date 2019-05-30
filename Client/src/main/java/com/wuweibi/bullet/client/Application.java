package com.wuweibi.bullet.client;

/**
 * Created by marker on 2019/4/10.
 */

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 *
 *
 * @author marker
 * @create 2019-04-10 09:50
 **/


@SpringBootApplication
public class Application implements CommandLineRunner {



    /**
     * 入口
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... strings) throws Exception {
        ConnectionPool pool = new ConnectionPool();

        // 启动线程池
        pool.startup();
        // 保持不退出
        while (true){
            Thread.sleep(1000L);
        }
    }
}
