/**
 * Created by marker on 2017/11/19.
 */

import org.junit.Test;

import java.net.URISyntaxException;
import java.nio.ByteBuffer;

/**
 *
 *
 * @author marker
 * @create 2017-11-19 下午4:19
 **/
public class ByteBufferTest {


    @Test
    public void test() throws URISyntaxException, InterruptedException {

        ByteBuffer byteBuffer = ByteBuffer.allocate(10);


        byte[] bytes = new byte[]{
                1,3
        };

        byteBuffer.putInt(1);
        byteBuffer.put(bytes);



        byteBuffer.flip();


        System.out.println(byteBuffer.getInt());

        byte[] bytes2 = new byte[2];

        System.out.println(byteBuffer.get(bytes2));


        System.out.println(bytes2[0]+ " " +bytes2[1]);

    }



}
