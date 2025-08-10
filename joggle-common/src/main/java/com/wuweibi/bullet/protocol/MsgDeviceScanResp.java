package com.wuweibi.bullet.protocol;
/**
 * Created by marker on 2017/12/7.
 */

import com.alibaba.fastjson.JSON;
import com.wuweibi.bullet.protocol.domain.KscanResult;
import com.wuweibi.bullet.utils.Utils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * 客户端认证消息
 *
 * @author marker
 * @create 2021-03-28 下午1:13
 **/
@Slf4j
@MessageType(Message.DEVICE_SCAN_RESP)
public class MsgDeviceScanResp extends Message {

    /**
     * 设备编号
     */
    private String deviceNo;

    /**
     * kscan扫描结果  json字符串
     */
    private String data;

    /**
     * kscan扫描结果  对象
     */
    private KscanResult result;


    /**
     * 构造
     */
    public MsgDeviceScanResp(String data) {
        super(Message.DEVICE_SCAN_RESP);
        getHead().setLength(super.getLength() + data.length());
        this.data = data;
    }

    public MsgDeviceScanResp() {
        super(Message.DEVICE_SCAN_RESP);
    }

    public MsgDeviceScanResp(MsgHead head) {
        super(Message.DEVICE_SCAN_RESP, head);
    }


    @Override
    public void write(OutputStream out) throws IOException {
        getHead().write(out);
        log.debug("send {}",this.toString());

        // 写入authToken
        int len = data.getBytes().length;
        byte bs[] = new byte[len];

        System.arraycopy(data.getBytes(), 0, bs, 0, len);
        out.write(bs);
        out.flush();
    }

    @Override
    public void read(InputStream in) throws IOException {
        int len = this.getLength() - MsgHead.HEAD_LENGTH;
        // 读取ip
        byte bs[] = new byte[len];
        in.read(bs);

        this.deviceNo = Utils.getString(bs, 0, 20);

        this.data = Utils.getString(bs, 20, len-20);

        // 将this.data 解析为 KscanResult类型
        this.result = JSON.parseObject(this.data, KscanResult.class);

    }

    public String getData() {
        return data;
    }

    public KscanResult getResult() {
        return this.result;
    }

    public String getDeviceNo() {
        return deviceNo;
    }
}
