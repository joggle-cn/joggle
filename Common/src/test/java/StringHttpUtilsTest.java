/**
 * Created by marker on 2017/12/7.
 */

import com.wuweibi.bullet.utils.StringHttpUtils;
import org.junit.Test;

/**
 * @author marker
 * @create 2017-12-07 下午1:07
 **/
public class StringHttpUtilsTest {


    @Test
    public void test(){
        String str = "GET /1121 HTTP/1.1\n" +
                "Host: localhost:8081";



        String host = StringHttpUtils.getHost(str);
        System.out.println(host);

    }


}
