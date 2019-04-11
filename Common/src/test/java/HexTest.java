/**
 * Created by marker on 2018/7/6.
 */

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author marker
 * @create 2018-07-06 21:15
 **/
public class HexTest {


    @Test
    public void test() throws IOException, InterruptedException {

        String  command = "/WORK/git/Bullet/Client/bin/ngrok  http 80";

        Process p = Runtime.getRuntime().exec(command);

        InputStream is = p.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        p.waitFor();
        if (p.exitValue() != 0) {
            //说明命令执行失败
            //可以进入到错误处理步骤中
        }

        String s = null;
        while ((s = reader.readLine()) != null) {
            System.out.println(s);
        }
    }


}
