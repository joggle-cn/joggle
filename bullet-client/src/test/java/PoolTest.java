/**
 * Created by marker on 2018/1/8.
 */

import com.wuweibi.bullet.client.ConnectionPool;
import org.junit.Test;

import javax.websocket.DeploymentException;
import java.io.IOException;
import java.net.URISyntaxException;

/**
 * @author marker
 * @create 2018-01-08 下午8:12
 **/
public class PoolTest {

    @Test
    public void test() throws URISyntaxException, InterruptedException, IOException, DeploymentException {
        // 设置配置文件地址
        System.setProperty("java.bullet.conf.dir", "/WORK/git/Bullet/Client/conf");


        ConnectionPool pool = ConnectionPool.getInstance();


        // 启动线程池
        pool.startup();

        Thread.sleep(1000000L);





    }



}
