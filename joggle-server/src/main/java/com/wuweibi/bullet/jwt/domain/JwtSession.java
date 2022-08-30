package com.wuweibi.bullet.jwt.domain;
/**
 * Created by marker on 2018/5/28.
 */

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * JWT SESSION
 *
 * @author marker
 * @create 2018-05-28 13:40
 **/
@Data
@Deprecated
@Accessors(chain = true, fluent = true)
public class JwtSession implements Serializable {


    /**
     * userId
     */
    private Long userId;


    /**
     * 创建实例
     */
    public static JwtSession builder(){
        return new JwtSession();
    }


    /**
     * 是否登录
     * @return
     */
    public boolean isLogin() {
        if (userId != null){
            return true;
        }
        return false;
    }
}
