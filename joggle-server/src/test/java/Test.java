/**
 * Created by marker on 2017/11/23.
 */

import com.alibaba.fastjson.JSONObject;
import com.wuweibi.bullet.utils.StringUtil;

/**
 * @author marker
 * @create 2017-11-23 下午9:11
 **/
public class Test {


    public static void main(String[] args) {



        // Setup HTTP service and add Handlers
//        NettyHttpService service = NettyHttpService.builder()
//                .setPort(7777).set
//                .addHttpHandlers(new ApplicationHandler())
//                .build();

// Start the HTTP service
//        service.start();

        String ip = "cff55eb03fd34667adf157ce62e8bb89";
        JSONObject a = new JSONObject();
        a.put("a",1);
        a.put("b",2);
        System.out.println( a.toJSONString());




        System.out.println( StringUtil.getBaseDomain("www.joggle.cn"));
        System.out.println( StringUtil.getBaseDomain("www.faceinner.cn"));
        System.out.println( StringUtil.getBaseDomain("www.yl-blog.cn"));
        System.out.println( StringUtil.getBaseDomain("22.www.yl-blog.cn"));

    }
}
