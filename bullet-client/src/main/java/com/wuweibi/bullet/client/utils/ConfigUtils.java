package com.wuweibi.bullet.client.utils;

/**
 * Created by marker on 2017/12/6.
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.wuweibi.bullet.utils.FileTools;
import com.wuweibi.bullet.utils.String2Utils;
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
    private static final String CONFIG_FILE = "/config.json";

    /**
     * 项目运行的根目录
     */
    public static String HomeDir;
    public static String ConfDir;
    public static String LogsDir;

    private static JSONObject configMap = null;


    static {
        HomeDir = System.getProperty("java.bullet.home.dir");
        ConfDir = HomeDir + File.separator + "conf";
        LogsDir = HomeDir + File.separator + "logs";
        logger.debug("准备加载config.json配置资源...");
        reload();
    }


    /**
     * 获取客户端路径
     * @return
     */
    public static String getClientProjectPath(){
        if(String2Utils.isEmpty(HomeDir)){
            return "bullet-client";
        }
        return HomeDir;
    }

    /**
     * 通道地址
     * @return
     */
    public static String getTunnel(){
        return configMap.getString("tunnel");
    }


    /**
     * 获取设备编号
     * @return
     */
    public static String getDeviceNo() {
        // 优先读取环境变量的设备编码
        String deviceNo = System.getProperty("BULLET_DEVICE_NO");
        if(StringUtils.isNotBlank(deviceNo)){
            logger.debug("读取到环境变量，不采用配置 env deviceNo={}", deviceNo);
            return deviceNo;
        }
        // 读取Bullet配置文件设备信息
        return configMap.getString("deviceNo");
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
        configMap.put("deviceNo", deviceNo);
    }

    /**
     * 获取日志服务开启状态
     * @return
     */
    public static Boolean getLogService() {
        Boolean logService = configMap.getBoolean("logService");
        if(logService == null){
            return false;
        }
        return logService;
    }


    /**
     * 获取配置对象
     * @return
     */
    public static Properties getProperties() {
        Properties pro = new Properties();
        for(Map.Entry<String, Object> entry : configMap.entrySet()){
            pro.setProperty(entry.getKey(), (String) entry.getValue());
        }
        return pro;
    }


    /**
     * 保存配置
     */
    public static void store(){
        String fileName = ConfDir + CONFIG_FILE;
        File file = new File(fileName);
        String jsonStr = JSON.toJSONString(configMap, SerializerFeature.PrettyFormat,
                SerializerFeature.WriteMapNullValue, SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteNullListAsEmpty);
        try {
            FileTools.setFileContet(fileName, jsonStr , FileTools.FILE_CHARACTER_UTF8);
        } catch (IOException e) {
            logger.error("", e);
        }
    }


    /**
     * 重新加载配置资源
     */
    public static void reload(){
        String result = "{}";
        InputStream inputStream = null;
        try {
            File file = new File(ConfDir + CONFIG_FILE);
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
