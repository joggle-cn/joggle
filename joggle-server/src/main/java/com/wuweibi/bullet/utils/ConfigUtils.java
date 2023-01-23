package com.wuweibi.bullet.utils;

/**
 * Created by marker on 2017/12/6.
 */

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;


/**
 * 配置文件工具
 *
 * @author marker
 * @create 2017-12-06 下午9:43
 **/
public class ConfigUtils {
    /**
     * 日志
     */
    private static Logger logger = LoggerFactory.getLogger(ConfigUtils.class);

    /**
     * 配置文件名称
     */
    public static final String configMapIG_FILE = "/config.json";

    public static String ConfDir;

    static JSONObject configMap;

    static {
        String HomeDir = System.getProperty("java.bullet.home.dir");
        if(HomeDir == null){
            HomeDir = "bullet-client";
        }
        ConfDir = HomeDir + File.separator + "conf"; 
        logger.debug("准备加载config.json配置资源...");
        reload();
    }


    /**
     * 获取客服务器端路径
     *
     * @return
     */
    public static String getServerProjectPath() {
        String confDir = System.getProperty("java.bullet.home.dir");
        if (String2Utils.isEmpty(confDir)) {
            confDir = "bullet-server";
        }
        return confDir;
    }

    /**
     * 通道端口
     *
     * @return
     */
    public static int getTunnelPort() {
        return configMap.getIntValue("tunnelPort");
    }

    /**
     * Http端口
     *
     * @return
     */
    public static int getHttpPort() {
        return configMap.getIntValue("httpPort");
    }

    /**
     * Https端口
     *
     * @return
     */
    public static int getHttpsPort() {
        return configMap.getIntValue("httpsPort");
    }


    /**
     * 获取BulletDomain 一级域名配置
     *
     * @return
     */
    public static String getBulletDomain() {
        String domain = configMap.getString("domain");

        if (StringUtils.isNotBlank(domain)) {
            logger.debug("读取的config.json BULLET_DOMAIN={}", domain);
            return domain;
        }

        String bulletDomain = System.getProperty("JOGGLE_DOMAIN");
        if (StringUtils.isNotBlank(bulletDomain)) {
            logger.debug("读取env domain={}", bulletDomain);
            return bulletDomain;
        }

        return "joggle.cn";
    }


    /**
     * 设置设备编号
     *
     * @return
     */
    public static void setDeviceNo(String deviceNo) {
        configMap.put("deviceNo", deviceNo);
    }


    /**
     * 获取配置对象
     *
     * @return
     */
    public static Properties getProperties() {
        Properties pro = new Properties();
        for (Map.Entry<String, Object> entry : configMap.entrySet()) {
            pro.setProperty(entry.getKey(), (String) entry.getValue());
        }
        return pro;
    }



    /**
     * 重新加载配置资源
     */
    public static void reload(){
        String result = "{}";
        InputStream inputStream = null;
        try {
            File file = new File(ConfDir + configMapIG_FILE);
            logger.info("正在加载配置文件 ",file.getAbsoluteFile());
            inputStream = new FileInputStream(file);
            result = IOUtils.toString(inputStream, "UTF-8");
            logger.debug("{}", result);
        } catch (IOException e) {
            logger.error("", e);
        } finally {
            IOUtils.closeQuietly(inputStream);
        }
        configMap = JSONObject.parseObject(result);
    }


    /**
     * 获取操作系统名称
     *
     * @return
     */
    public static String getOSName() {
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
