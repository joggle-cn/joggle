package com.wuweibi.bullet.domain.domain.session;
/**
 * Created by marker on 2019/8/21.
 */

import lombok.Data;

import java.io.Serializable;

/**
 * 用户会话相关信息
 * 通过JWT字符串，解码得到的JSON串，再转换为实体对象SessionUser
 *
 * {
 *    "user_name": "zhoutaoo",
 *    "userMobile": "15619841000",
 *    "scope": ["read"],
 *    "organization": "zhoutaoo",
 *    "exp": 1566358556,
 *    "userId": 102,
 *    "authorities": ["ADMIN", "IT"],
 *    "jti": "6d47096c-beff-46f0-bddf-7395e884c76c",
 *    "client_id": "test_client"
 * }
 * @author marker
 * @create 2019-08-21 09:29
 **/
@Data
public class Session implements Serializable {


    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 手机号
     */
    @Deprecated
    private String userMobile;

    /**
     * 客户端ID
     */
    private String client_id;

    /**
     *  liuzc 10/12  add
     * 商家ID
     */
    private Long supplierId;


    /**
     * 判断用户是否登录
     *
     * @return
     */
    public boolean isLogin(){
        if(userId != null){
            return true;
        }
        return false;
    }

    /**
     * 判断用户未登录
     * @return
     */
    public boolean isNotLogin() {
        return !isLogin();
    }
}
