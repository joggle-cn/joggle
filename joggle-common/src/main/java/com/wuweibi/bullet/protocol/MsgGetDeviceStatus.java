package com.wuweibi.bullet.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 获取设备状态消息
 * 用于服务器与控制端的消息
 *
 * @author marker
 **/
public class MsgGetDeviceStatus extends Message {



    /**
     * 构造
     */
    public MsgGetDeviceStatus() {
        super(Message.GET_DEVICE_STATUS);
    }

    public MsgGetDeviceStatus(MsgHead head) {
        super(Message.GET_DEVICE_STATUS, head);
    }


    @Override
    public void write(OutputStream out) throws IOException {
        getHead().write(out);
        out.flush();
    }

    @Override
    public void read(InputStream in) throws IOException {

    }
}
