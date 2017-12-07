package com.wuweibi.bullet.utils;/**
 * Created by marker on 2017/12/7.
 */

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

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

}
