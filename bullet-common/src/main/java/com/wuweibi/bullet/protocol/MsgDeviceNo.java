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
 * @author marker
 * @create 2017-12-07 下午1:13
 **/
public class MsgDeviceNo extends Message {


    // deviceNo
    private String deviceNo;



    /**
     * 构造
     */
    public MsgDeviceNo(String deviceNo) {
        super(Message.DEVICE_NO);
        this.deviceNo = deviceNo;
        getHead().setLength(super.getLength() + 32);
    }

    public MsgDeviceNo() {
        super(Message.DEVICE_NO);
    }

    public MsgDeviceNo(MsgHead head) {
        super(Message.DEVICE_NO, head);
    }


    @Override
    public void write(OutputStream out) throws IOException {
        getHead().write(out);

        // 写入IP地址
        byte bs[] = new byte[32];
        System.arraycopy(deviceNo.getBytes(), 0, bs, 0, deviceNo.getBytes().length);
        out.write(bs);
        out.flush();
    }

    @Override
    public void read(InputStream in) throws IOException {
        // 读取deviceNo
        byte bs[] = new byte[32];
        in.read(bs);
        this.deviceNo = Utils.getString(bs, 0, 32);

    }

    public String getDeviceNo() {
        return deviceNo;
    }

    public void setDeviceNo(String deviceNo) {
        this.deviceNo = deviceNo;
    }
}
