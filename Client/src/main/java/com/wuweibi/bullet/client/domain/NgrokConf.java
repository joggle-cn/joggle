package com.wuweibi.bullet.client.domain;
/**
 * Created by marker on 2019/4/10.
 */

import lombok.Data;

import java.util.Map;

/**
 * @author marker
 * @create 2019-04-10 16:13
 **/
@Data
public class NgrokConf {


    private String server_addr;
    private String trust_host_root_certs;

    private Map<String, Tunnels> tunnels;

}
