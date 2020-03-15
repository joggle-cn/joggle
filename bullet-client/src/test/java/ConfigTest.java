/**
 * Created by marker on 2017/12/6.
 */

import com.wuweibi.bullet.ConfigUtils;
import com.wuweibi.bullet.client.domain.NgrokConf;
import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author marker
 * @create 2017-12-06 下午9:49
 **/
public class ConfigTest {


    @Test
    public void test(){
        System.out.println(ConfigUtils.getTunnel());
    }



    @Test
    public void test1() throws IOException {

        Yaml yaml = new Yaml();
        InputStream inputStream = this.getClass().getResourceAsStream("/conf/ngrok.yml");

        NgrokConf testEntity = yaml.loadAs(inputStream, NgrokConf.class);//如果读入Map,这里可以是Mapj接口,默认实现为LinkedHashMap
        System.out.println(testEntity);

        yaml.dump(testEntity, new FileWriter("./a.yml"));
    }
}
