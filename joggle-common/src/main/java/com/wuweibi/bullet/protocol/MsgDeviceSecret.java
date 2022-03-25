package com.wuweibi.bullet.protocol;
/**
 * Created by marker on 2017/12/7.
 */

import com.wuweibi.bullet.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * 设备秘钥消息
 *
 * @author marker
 * @create 2020-06-27 下午1:13
 **/
public class MsgDeviceSecret extends Message {

    private String secret;


    /**
     * 构造
     */
    public MsgDeviceSecret() {
        super(Message.DEVICE_SECRET);
    }

    public MsgDeviceSecret(MsgHead head) {
        super(Message.DEVICE_SECRET, head);
    }


    @Override
    public void write(OutputStream out) throws IOException {
        getHead().setLength(super.getLength() + secret.getBytes().length);
        getHead().write(out);
        out.write(secret.getBytes());
        out.flush();
    }

    @Override
    public void read(InputStream in) throws IOException {
        int len = getLength() - 24;
        byte bs[] = new byte[len];
        in.read(bs);
        this.secret = Utils.getString(bs, 0, len);
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }
}
