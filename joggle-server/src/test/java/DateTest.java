/**
 * Created by marker on 2017/11/23.
 */

import cn.hutool.core.date.DateUtil;
import com.wuweibi.bullet.utils.DateTimeUtil;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author marker
 * @create 2017-11-23 下午9:11
 **/
public class DateTest {


    public static void main(String[] args) {



        // Setup HTTP service and add Handlers
//        NettyHttpService service = NettyHttpService.builder()
//                .setPort(7777).set
//                .addHttpHandlers(new ApplicationHandler())
//                .build();

// Start the HTTP service
//        service.start();
        Date date = DateUtil.parse("2024071700", "yyyyMMddHH");
        LocalDateTime a = DateUtil.parseLocalDateTime("2024071719", "yyyyMMddHH");
        LocalDateTime b = DateUtil.parseLocalDateTime("2024071720", "yyyyMMddHH");
        int hours = DateTimeUtil.getHourDiff(a,b);
        for (int i =0 ;i <= (hours); i++){
            System.out.print( i);


            System.out.println(" "+            a.plusHours(i).toString());
        }




    }
}
