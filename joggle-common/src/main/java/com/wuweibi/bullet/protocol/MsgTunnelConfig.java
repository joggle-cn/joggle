package com.wuweibi.bullet.protocol;
/**
 * Created by marker on 2017/12/7.
 */

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * 通道配置消息
 *
 * @author marker
 * @create 2021-07-31 下午11:13
 **/
@Slf4j
public class MsgTunnelConfig extends Message {


    private JSONObject config;


    /**
     * 构造
     */
    public MsgTunnelConfig(JSONObject config) {
        super(Message.TUNNEL_CONFIG);
        this.config = config;
    }

    public MsgTunnelConfig(MsgHead head, JSONObject config) {
        super(Message.TUNNEL_CONFIG, head);
        this.config = config;
    }


    @Override
    public void write(OutputStream out) throws IOException {
        String json = config.toJSONString();
        byte[] data = json.getBytes(StandardCharsets.UTF_8);
        this.getHead().setLength(24 + data.length);
        getHead().write(out);
        out.write(data);
        log.debug("send {}", this.toString());
        out.flush();
    }

    @Override
    public void read(InputStream in) throws IOException {
        log.debug("reciver {}", this.toString());
    }


}
