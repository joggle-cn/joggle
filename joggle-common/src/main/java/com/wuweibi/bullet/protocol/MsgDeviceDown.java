package com.wuweibi.bullet.protocol;
/**
 * Created by marker on 2017/12/7.
 */

import com.wuweibi.bullet.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 设备下线消息
 * @author marker
 * @create 2017-12-07 下午1:13
 **/
public class MsgDeviceDown extends Message {


    // deviceNo
    private String deviceNo;



    /**
     * 构造
     */
    public MsgDeviceDown(String deviceNo) {
        super(Message.DEVICE_DOWN);
        this.deviceNo = deviceNo;
        getHead().setLength(super.getLength() + deviceNo.length());
    }

    public MsgDeviceDown() {
        super(Message.DEVICE_DOWN);
    }

    public MsgDeviceDown(MsgHead head) {
        super(Message.DEVICE_DOWN, head);
    }


    @Override
    public void write(OutputStream out) throws IOException {
        getHead().write(out);
        int len = deviceNo.getBytes().length;
        // 写入IP地址
        byte bs[] = new byte[len];
        System.arraycopy(deviceNo.getBytes(), 0, bs, 0, len);
        out.write(bs);
        out.flush();
    }

    @Override
    public void read(InputStream in) throws IOException {
        // 读取deviceNo
        int len = getHead().getLength() - 24;

        byte bs[] = new byte[len];
        in.read(bs);
        this.deviceNo = Utils.getString(bs, 0, len);

    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
        getHead().setLength(super.getLength() + deviceNo.length());
    }
}
