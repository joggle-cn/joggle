package com.wuweibi.bullet.protocol;
/**
 * Created by marker on 2017/12/7.
 */

import com.wuweibi.bullet.utils.Utils;
import lombok.extern.slf4j.Slf4j;

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
@Slf4j
public class MsgBindIP extends Message {


    /**
     * ip地址
     */
    private String ip;

    /**
     * mac 地址
     */
    private String mac;
    /**
     * 版本
     */
    private String version;


    /**
     * 构造
     */
    public MsgBindIP(String ip) {
        super(Message.NEW_BINDIP);
        // IP 20 位
        // mac 17位
        // version 8
        getHead().setLength(super.getLength() + 20 + 17 + 8);
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
        log.debug("send {}", this.toString());

        // 写入IP地址
        byte bs[] = new byte[20];
        int len = ip.getBytes().length;

        System.arraycopy(ip.getBytes(), 0, bs, 0, len);
        out.write(bs);

        // 写入mac地址
        byte bs2[] = new byte[17];
        System.arraycopy(mac.getBytes(), 0, bs2, 0, mac.getBytes().length);
        out.write(bs2);
        out.flush();

        // 写入version
        bs2 = new byte[8];
        System.arraycopy(version.getBytes(), 0, bs2, 0, version.getBytes().length);
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
        // 读取version
        bs = new byte[8];
        in.read(bs);
        this.version = Utils.getString(bs, 0, 8);

        // 兼容协议扩展，读取多余内容
        super.readResidue(in, 45);
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

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }
}
