package com.wuweibi.bullet;

/**
 * Created by marker on 2017/12/6.
 */

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;


/**
 * @author marker
 * @create 2017-12-06 下午9:43
 **/
public class ConfigUtils {
    /** 日志 */
    private static Logger logger = LoggerFactory.getLogger(ConfigUtils.class);

    static JSONObject CONF ;

    static {
        String result = "{}";
        try {
            File file = new File("./conf/config.json");
            System.out.println(file.getAbsoluteFile());
            InputStream inputStream = new FileInputStream(file);

//            InputStream inputStream = ConfigUtils.class.getResourceAsStream("/config.json");



            result = IOUtils.toString(inputStream, "UTF-8");
            logger.debug("{}", result);
        } catch (IOException e) {
            logger.error("", e);
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
