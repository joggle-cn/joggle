package com.wuweibi.bullet.client.domain;/**
 * Created by marker on 2019/4/10.
 */

import lombok.Data;

/**
 *
 * 协议
 * @author marker
 * @create 2019-04-10 16:20
 **/
@Data
public class Proto {

    // 1
    private String http;
    // 2
    private String tcp;
    // 3
    private String https;

}
