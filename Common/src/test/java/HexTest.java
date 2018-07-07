/**
 * Created by marker on 2018/7/6.
 */

import org.junit.Test;

/**
 * @author marker
 * @create 2018-07-06 21:15
 **/
public class HexTest {


    @Test
    public void test(){


        String str = "d9e";
        Integer in = Integer.valueOf(str,16);

        System.out.println(in);
        String st = Integer.toHexString(in).toUpperCase();
        st = String.format("%5s",st);
        st= st.replaceAll(" ","0");
        System.out.println(st);

    }


}
