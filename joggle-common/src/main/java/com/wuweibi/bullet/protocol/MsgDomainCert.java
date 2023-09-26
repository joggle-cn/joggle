package com.wuweibi.bullet.protocol;
/**
 * Created by marker on 2017/12/7.
 */

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * 域名证书消息
 *
 * @author marker
 * @create 2023-09-26 下午1:13
 **/
@Slf4j
public class MsgDomainCert extends Message {


    /**
     * domain
     */
    private String domain;

    private String certKey;

    private String certPem;

    private Map<String,String> data = new HashMap<>(3);

    private String json;
    /**
     * 构造
     */
    public MsgDomainCert(String domain, String certKey, String certPem) {
        super(Message.DOMAIN_CERT);
        this.domain = domain;
        this.certKey = certKey;
        this.certPem = certPem;
        data.put("domain", domain);
        data.put("certKey", certKey);
        data.put("certPem", certPem);
        this.json = JSON.toJSONString(data);
        int len = this.json.length();
        getHead().setLength(super.getLength() + len);
    }

    private MsgDomainCert() {
        super(Message.DOMAIN_CERT);
    }

    private MsgDomainCert(MsgHead head) {
        super(Message.DOMAIN_CERT, head);
    }


    @Override
    public void write(OutputStream out) throws IOException {
        getHead().write(out);
        log.debug("send {}",this.toString());
        // 写入authToken
        byte[] data = this.json.getBytes();
        out.write(data);
        out.flush();
    }

    @Override
    public void read(InputStream in) throws IOException {
//        int len = this.getLength() - MsgHead.HEAD_LENGTH;
//        // 读取ip
//        byte bs[] = new byte[len];
//        in.read(bs);
//        this.authToken = Utils.getString(bs, 0, len);
        throw new IOException("不支持读取");
    }


}
