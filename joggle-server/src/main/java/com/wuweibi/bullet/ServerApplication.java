package com.wuweibi.bullet; /**
 * Created by marker on 2019/4/10.
 */

import com.wuweibi.bullet.listener.CloseServerListener;
import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Bullet服务器端
 *
 * @author marker
 * @create 2019-04-10 09:50
 **/


@EnableTransactionManagement
@SpringBootApplication
@EnableAdminServer
public class ServerApplication {

    static {
        // 强制使用IPv4（解决双栈环境问题）
        System.setProperty("java.net.preferIPv4Stack", "true");
    }

    /**
     * 程序入口
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) {

// 启动参数
        System.setProperty("com.sun.xml.bind.v2.bytecode.ClassTailor.noOptimize", "true");
        SpringApplication app = new SpringApplication(ServerApplication.class);
        app.addListeners(new CloseServerListener());
        app.run(args);
        System.out.println("======================================");
        System.out.println("========  Bullet Server Started ==============");
        System.out.println("======================================");
    }

}
