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

    // mac 地址
    private String mac;



    /**
     * 构造
     */
    public MsgBindIP(String ip) {
        super(Message.NEW_BINDIP);
        // IP 20 位
        // mac 17位
        getHead().setLength(super.getLength() + 20 + 17);
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

        // 写入IP地址
        byte bs[] = new byte[20];
        System.arraycopy(ip.getBytes(), 0, bs, 0, ip.getBytes().length);
        out.write(bs);

        // 写入mac地址
        byte bs2[] = new byte[17];
        System.arraycopy(mac.getBytes(), 0, bs2, 0, mac.getBytes().length);
        out.write(bs2);
        out.flush();
    }

    @Override
    public void read(InputStream in) throws IOException {
        // 读取ip
        byte bs[] = new byte[20];
        in.read(bs);
        this.ip = Utils.getString(bs, 0, 20);

        // 读取mac
        bs = new byte[17];
        in.read(bs);
        this.mac = Utils.getString(bs, 0, 17);
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }


    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
