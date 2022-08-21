package com.wuweibi.bullet.protocol;
/**
 * Created by marker on 2017/12/7.
 */

import com.alibaba.fastjson.JSON;
import com.wuweibi.bullet.protocol.domain.ProxyConfig;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 *
 * 代理Http请求信息
 *
 * @author marker
 * @create 2017-12-07 下午1:13
 **/
public class MsgProxy extends Message {

    // 请求内容(不计入协议长度)
    private ProxyConfig config;


    private String jsonStr;


    /**
     * 构造
     */
    public MsgProxy(ProxyConfig config) {
        super(Message.PROXY);
        this.config = config;
        this.jsonStr = JSON.toJSONString(this.config);
        getHead().setLength(super.getLength() + jsonStr.length());
    }

    public MsgProxy() {
        super(Message.PROXY);
    }

    public MsgProxy(MsgHead head) {
        super(Message.PROXY, head);
    }


    @Override
    public void write(OutputStream out) throws IOException {
        getHead().write(out);
        out.write(this.jsonStr.getBytes(StandardCharsets.UTF_8));
        out.flush();
    }

    @Override
    public void read(InputStream in) throws IOException {
        int len = getLength() - 24;
        byte bs[] = new byte[len];
        in.read(bs);

        // 读取承载协议数据
        this.jsonStr = new String(bs, StandardCharsets.UTF_8);
        this.config = JSON.parseObject(this.jsonStr, ProxyConfig.class);
    }

    public ProxyConfig getConfig() {
        return config;
    }

}
