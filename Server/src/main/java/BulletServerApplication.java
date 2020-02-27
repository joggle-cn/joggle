/**
 * Created by marker on 2019/4/10.
 */

import com.wuweibi.bullet.listener.CloseServerListener;
import com.wuweibi.bullet.listener.StartedServerListener;
import com.wuweibi.bullet.ngrokd.NgrokdThread;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * 服务器端
 *
 * @author marker
 * @create 2019-04-10 09:50
 **/
@SpringBootConfiguration
@Configuration
@ComponentScan({
        "com.wuweibi.bullet",
})
@EnableAutoConfiguration
public class BulletServerApplication  {



    /**
     * 入口
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(BulletServerApplication.class);
        app.addListeners(
                new StartedServerListener(),
                new CloseServerListener());
        app.run(args);
        System.out.println("======================================");
        System.out.println("========  Bullet Server ==============");
        System.out.println("======================================");

        // 启动ngrokd线程
        new NgrokdThread().start();
    }

}
