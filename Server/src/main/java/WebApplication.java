/**
 * Created by marker on 2019/4/10.
 */

import com.wuweibi.bullet.listener.CloseServerListener;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 *
 *
 * @author marker
 * @create 2019-04-10 09:50
 **/


@SpringBootConfiguration
@Configuration
@ComponentScan({
        "com.wuweibi.bullet",
        "com.wuweibi.bullet.controller",
})
@EnableAutoConfiguration
public class WebApplication  {



    /**
     * 入口
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        SpringApplication app = new SpringApplication(WebApplication.class);
        app.addListeners(new CloseServerListener());
        app.run(args);
    }

}
