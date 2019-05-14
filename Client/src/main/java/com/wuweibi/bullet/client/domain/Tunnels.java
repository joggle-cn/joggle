package com.wuweibi.bullet.client.domain;/**
 * Created by marker on 2019/4/10.
 */

import lombok.Data;

/**
 *
 *
 * @author marker
 * @create 2019-04-10 16:14
 **/
@Data
public class Tunnels {

    private String subdomain; // "haha"
    private Proto proto; //
    private Integer remote_port;
    private String hostname; // hostname



    /**
     * http 访问时账号密码认证
     *
     *   auth: "user:secretpassword"
     */
    private String auth;

    private String host_header;

    private boolean bind_tls = false;


}
