/**
 * Created by marker on 2017/12/6.
 */

import com.wuweibi.bullet.utils.Utils;
import org.junit.Test;

/**
 * @author marker
 * @create 2017-12-06 下午9:49
 **/
public class LongTest {


    @Test
    public void test(){
        Long a = 1l;
        System.out.println("a="+a);

        byte[] bs = Utils.LongToBytes8(a);
        System.out.println("size="+bs.length);

        long b  = Utils.Bytes8ToLong(bs);

        System.out.println(b );
    }
}
