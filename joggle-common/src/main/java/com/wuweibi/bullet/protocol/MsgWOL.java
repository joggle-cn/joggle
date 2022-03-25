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
 * 网络唤醒消息
 *
 * @author marker
 * @create 2021-02-12 下午6:32
 **/
@Slf4j
public class MsgWOL extends Message {


    /**
     * mac 地址
     */
    private String mac;


    /**
     * 构造
     */
    public MsgWOL(String mac) {
        super(Message.NEW_WOLMAC);
        this.mac = mac;
        // mac 17位
        getHead().setLength(super.getLength() + mac.length());
    }

    public MsgWOL() {
        super(Message.NEW_WOLMAC);
    }

    public MsgWOL(MsgHead head) {
        super(Message.NEW_WOLMAC, head);
    }


    @Override
    public void write(OutputStream out) throws IOException {
        getHead().write(out);
        log.debug("send {}",this.toString());

        // 写入mac地址
        int len = mac.getBytes().length;
        byte bs2[] = new byte[len];
        System.arraycopy(mac.getBytes(), 0, bs2, 0, len);
        out.write(bs2);
        out.flush();
    }

    @Override
    public void read(InputStream in) throws IOException {
        int len = getLength() - MsgHead.HEAD_LENGTH;
        // 读取mac
        byte[] bs = new byte[len];
        in.read(bs);
        this.mac = Utils.getString(bs, 0, len);
        log.debug("reciver {}", this.toString());
    }


    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }
}
