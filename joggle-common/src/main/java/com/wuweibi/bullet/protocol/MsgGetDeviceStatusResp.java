package com.wuweibi.bullet.protocol;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 获取设备状态消息
 * 用于服务器与控制端的消息
 *
 * @author marker
 **/
public class MsgGetDeviceStatusResp extends Message {


    private JSONObject data;

    /**
     * 构造
     */
    public MsgGetDeviceStatusResp() {
        super(Message.GET_DEVICE_STATUS_RESP);
    }

    public MsgGetDeviceStatusResp(MsgHead head) {
        super(Message.GET_DEVICE_STATUS_RESP, head);
    }


    @Override
    public void write(OutputStream out) throws IOException {
        getHead().write(out);
        out.flush();
    }

    @Override
    public void read(InputStream in) throws IOException {
        int len = this.getLength() - MsgHead.HEAD_LENGTH;
        byte bs[] = new byte[len];
        in.read(bs);
        this.data = JSON.parseObject(new String(bs));
    }


    public JSONObject getData() {
        return data;
    }
}
