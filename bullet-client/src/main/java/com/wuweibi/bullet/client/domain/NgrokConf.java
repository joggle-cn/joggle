package com.wuweibi.bullet.client.domain;
/**
 * Created by marker on 2019/4/10.
 */

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * Ngrok 配置
 * @author marker
 * @create 2019-04-10 16:13
 **/
@Data
public class NgrokConf {


    private String server_addr;

    /**
     * 信任根证书（不用自己的）
     */
    private boolean trust_host_root_certs;
    /**
     * 跳过证书验证
     */
    private boolean use_insecure_skip_verify;
    /**
     * 使用客户端证书路径
     */
    private String use_client_crt_path;
    private boolean web_addr;
    private boolean console_ui;

    /**
     * 客户端秘钥 (绑定时服务器生成)
     */
    private String auth_token;

    /**
     * 设备编号（client_id)
     */
    private String client_no;

    private Map<String, Tunnels> tunnels = new HashMap<>(10);

}
