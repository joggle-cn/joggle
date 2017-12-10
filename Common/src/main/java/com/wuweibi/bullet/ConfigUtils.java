package com.wuweibi.bullet;

/**
 * Created by marker on 2017/12/6.
 */

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;





/**
 * @author marker
 * @create 2017-12-06 下午9:43
 **/
public class ConfigUtils {

    static JSONObject CONF ;

    static {
        InputStream inputStream = ConfigUtils.class.getResourceAsStream("/config.json");

        String result = "{}";
        try {
            result = IOUtils.toString(inputStream, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }


        CONF = JSONObject.parseObject(result);
    }


    /**
     * 通道地址
     * @return
     */
    public static String getTunnel(){
        return CONF.getString("tunnel");
    }


    /**
     * 获取设备ID
     * @return
     */
    public static String getDeviceId() {
        return CONF.getString("deviceId");
    }
}
