package com.wuweibi.bullet;

/**
 * Created by marker on 2017/12/6.
 */

import com.alibaba.fastjson.JSONObject;
import com.wuweibi.bullet.utils.StringUtils;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Map;
import java.util.Properties;


/**
 * 配置文件工具
 *
 *
 * @author marker
 * @create 2017-12-06 下午9:43
 **/
public class ConfigUtils {
    /** 日志 */
    private static Logger logger = LoggerFactory.getLogger(ConfigUtils.class);

    /**
     * 配置文件名称
     */
    public static final String CONFIG_FILE = "/config.json";

    static JSONObject CONF ;

    static {
        // 获取配置文件路径
        String confDir = System.getProperty("java.bullet.conf.dir");
        System.out.println(confDir);
        if(StringUtils.isEmpty(confDir)){
            confDir = "Client/conf";
        }

        String result = "{}";
        try {
            File file = new File(confDir + CONFIG_FILE);
            logger.info("正在加载配置文件 ",file.getAbsoluteFile());
            InputStream inputStream = new FileInputStream(file);

            result = IOUtils.toString(inputStream, "UTF-8");
            logger.debug("{}", result);
        } catch (IOException e) {
            logger.error("", e);
        }
        CONF = JSONObject.parseObject(result);
    }


    public static String getClientProjectPath(){
        String confDir = System.getProperty("java.bullet.home.dir");
        if(StringUtils.isEmpty(confDir)){
            confDir = "Client";
        }

        return confDir;
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


    /**
     * 获取配置对象
     * @return
     */
    public static Properties getProperties() {
        Properties pro = new Properties();
        for(Map.Entry<String, Object> entry : CONF.entrySet()){
            pro.setProperty(entry.getKey(), (String) entry.getValue());
        }
        return pro;
    }


    /**
     * 获取操作系统名称
     * @return
     */
    public static String getOSName()  {
        String osName = System.getProperty("os.name");
        if (osName.startsWith("Windows"))
            return "windows";
        if ((osName.contains("SunOS")) || (osName.contains("Solaris")))
            return "linux";
        if (osName.contains("Mac"))
            return "darwin";
        if (osName.contains("FreeBSD"))
             return "linux";
        if (osName.startsWith("Linux")) {
        return "linux";
        }
        return "linux";
    }
}
