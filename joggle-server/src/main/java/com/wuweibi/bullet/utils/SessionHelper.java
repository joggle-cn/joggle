package com.wuweibi.bullet.utils;

import com.wuweibi.bullet.jwt.domain.JwtSession;
import com.wuweibi.bullet.jwt.utils.JWTUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Session 帮助对象
 *
 * Created by marker on 16/8/3.
 */
@Deprecated
public class SessionHelper {

    private static Logger logger = LoggerFactory.getLogger(SessionHelper.class);

    public static String USER_ID = "s_userId";



    public static Long getUserId(){
        return getUserId(null);
    }

    /**
     * 获取Session中的用户Id
     * 没有登录返回-1;
     *
     * @return
     * @param request
     */
    public static Long getUserId(HttpServletRequest request){
        HttpServletRequest req = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = req.getSession();
        try {
            Long userId = (Long) session.getAttribute(USER_ID);
            if(userId != null){
                return userId;
            }

            // JWT验证
            String jwtToken = req.getHeader("Authorization");

            JwtSession jwtSession = JWTUtil.getSession(jwtToken);

            if(null != jwtSession){
                return jwtSession.isLogin()?jwtSession.userId():null;
            }


        } catch (Exception e) {
            logger.error("", e);
        }
        return null;
    }
}
