package com.wuweibi.bullet.utils;

/**
 * Created by marker on 2017/12/6.
 */

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public static String ConfDir;

    static JSONObject CONF ;



    /**
     * 获取客服务器端路径
     * @return
     */
    public static String getServerProjectPath(){
        String confDir = System.getProperty("java.bullet.home.dir");
        if(String2Utils.isEmpty(confDir)){
            confDir = "bullet-server";
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
     * 获取BulletDomain 一级域名配置
     *
     * @return
     */
    public static String getBulletDomain(){
        String bulletDomain = System.getProperty("BULLET_DOMAIN");
        if(StringUtils.isNotBlank(bulletDomain)){
            logger.debug("读取env BULLET_DOMAIN={}", bulletDomain);
            return bulletDomain;
        }
        return "joggle.cn";
    }


    /**
     * 设置设备编号
     * @return
     */
    public static void setDeviceNo(String deviceNo) {
        CONF.put("deviceNo", deviceNo);
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
