package com.wuweibi.bullet.utils;/**
 * Created by marker on 2017/12/7.
 */

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author marker
 * @create 2017-12-07 下午1:01
 **/
public class StringHttpUtils {


    /**
     *
     * @param httpRequestStr
     * @return
     */
    public static String getHost(String httpRequestStr) {
        ByteArrayInputStream bis = new ByteArrayInputStream(httpRequestStr.getBytes());
        InputStreamReader isr = new InputStreamReader(bis);
        BufferedReader br = new BufferedReader(isr);//此时获取到的bre就是整个文件的缓存流


        try {
            br.readLine();
            String host = br.readLine();
            host = host.split(": ")[1];
            return host.split(":")[0];
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return "";
    }


    /**
     * 获取二级域名
     * @param host 域名
     * @return
     */
    public static String getSecondLevelDomain(String host) {
        Pattern pattern = Pattern.compile("([0-9a-z\\.]+)\\.[0-9a-z]+\\.[0-9a-z]+");
        Matcher matcher = pattern.matcher(host);
        matcher.find();
        return matcher.group(1);
    }
}
