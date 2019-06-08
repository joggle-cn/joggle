package com.wuweibi.bullet.jwt.utils;
/**
 * Created by marker on 2018/5/7.
 */

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.wuweibi.bullet.jwt.domain.JwtSession;

import java.io.UnsupportedEncodingException;
import java.util.Date;

/**
 * JWT实现工具类
 *
 * @author marker
 * @create 2018-05-07 16:24
 **/
public class JWTUtil {




    /**
     * Session 手机号
     */
    private static final String UID    = "uid";




    /**
     * 校验token是否正确
     * @param token 密钥
     * @param secret 用户的密码
     * @return 是否正确
     */
    public static boolean verify(String token, String secret, JwtSession session) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm)
                    .withClaim(UID, session.userId())
                    .build();
            verifier.verify(token);

            return true;
        } catch (Exception exception) {
            return false;
        }
    }



    public static String getUserId(String token) {
        try {
            DecodedJWT jwt = JWT.decode(token);
            return jwt.getClaim(UID).asString();
        } catch (JWTDecodeException e) {
            return null;
        }
    }


    /**
     * 生成签名,5min后过期
     * @param secret 用户的密码
     * @return 加密的token
     */
    public static String sign(String secret, JwtSession session) {
        try {
//            ParamsSettings paramsSettings = SpringUtil.getBean(ConfigService.class).getParamsSettings();

            Date date = new Date(System.currentTimeMillis() + 100000000);
            Algorithm algorithm = Algorithm.HMAC256(secret);
            // 附带username信息
            return JWT.create()
                    .withClaim(UID, session.userId())
                    .withExpiresAt(date)
                    .sign(algorithm);
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }


    /**
     * 获取Session
     * @return
     */
    public static JwtSession getSession(String token) {
//        Subject subject = SecurityUtils.getSubject();
//        String token = subject.getPrincipal().toString();

        try {
            DecodedJWT jwt = JWT.decode(token);

            return JwtSession.builder()
                    .userId(jwt.getClaim(UID).asLong());
        } catch (JWTDecodeException e) {
            return null;
        }
    }


}
