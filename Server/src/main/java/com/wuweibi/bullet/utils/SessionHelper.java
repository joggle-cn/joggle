package com.wuweibi.bullet.utils;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Session 帮助对象
 *
 * Created by marker on 16/8/3.
 */
public class SessionHelper {

    public static String USER_ID = "s_userId";



    /**
     * 获取Session中的用户Id
     * 没有登录返回-1;
     *
     * @return
     */
    public static long getUserId(){
        HttpServletRequest req = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();

        HttpSession session = req.getSession();
        try{
            return (Long)session.getAttribute(USER_ID);

        }catch (Exception e){
            return -1;
        }
    }
}
