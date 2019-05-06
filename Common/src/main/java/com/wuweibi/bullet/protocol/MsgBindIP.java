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
 * IP
 *
 * @author marker
 * @create 2017-12-07 下午1:13
 **/
public class MsgBindIP extends Message {



    // ip地址
    private String ip;


    /**
     * 构造
     */
    public MsgBindIP(String ip) {
        super(Message.NEW_BINDIP);
        int len = ip.getBytes().length;
        getHead().setLength(super.getLength() + len);
        this.ip = ip;
    }

    public MsgBindIP() {
        super(Message.NEW_BINDIP);
    }

    public MsgBindIP(MsgHead head) {
        super(Message.NEW_BINDIP, head);
    }


    @Override
    public void write(OutputStream out) throws IOException {
        getHead().write(out);
        out.write(ip.getBytes());
        out.flush();
    }

    @Override
    public void read(InputStream in) throws IOException {
        int len = getLength() - 24;
        byte bs[] = new byte[len];
        in.read(bs);
        this.ip = Utils.getString(bs, 0, len);
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
