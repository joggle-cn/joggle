package com.wuweibi.bullet.protocol;

import com.wuweibi.bullet.utils.Utils;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@Slf4j
@MessageType(Message.DEVICE_LATENCY)
public class MsgDeviceLatency extends Message {

    private String data;

    public MsgDeviceLatency(String data) {
        super(Message.DEVICE_LATENCY);
        getHead().setLength(super.getLength() + data.length());
        this.data = data;
    }

    public MsgDeviceLatency() {
        super(Message.DEVICE_LATENCY);
    }

    public MsgDeviceLatency(MsgHead head) {
        super(Message.DEVICE_LATENCY, head);
    }

    @Override
    public void write(OutputStream out) throws IOException {
        getHead().write(out);
        log.debug("send {}", this.toString());
        int len = data.getBytes().length;
        byte bs[] = new byte[len];
        System.arraycopy(data.getBytes(), 0, bs, 0, len);
        out.write(bs);
        out.flush();
    }

    @Override
    public void read(InputStream in) throws IOException {
        int len = this.getLength() - MsgHead.HEAD_LENGTH;
        byte bs[] = new byte[len];
        in.read(bs);
        this.data = Utils.getString(bs, 0, len);
    }

    public String getData() {
        return data;
    }
}
