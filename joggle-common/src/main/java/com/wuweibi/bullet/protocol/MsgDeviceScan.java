package com.wuweibi.bullet.protocol;
/**
 * Created by marker on 2017/12/7.
 */

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * 设备内网扫描
 *
 * @author marker
 * @create 2025-04-18 下午 11:13
 **/
@Slf4j
@MessageType(Message.DEVICE_SCAN)
public class MsgDeviceScan extends Message {



    /**
     * 构造
     */
    public MsgDeviceScan( ) {
        super(Message.DEVICE_SCAN);
        // mac 17位
        getHead().setLength(super.getLength() );
    }


    public MsgDeviceScan(MsgHead head) {
        super(Message.DEVICE_SCAN, head);
    }


    @Override
    public void write(OutputStream out) throws IOException {
        getHead().write(out);
        log.debug("send {}",this.toString());

        out.flush();
    }

    @Override
    public void read(InputStream in) throws IOException {
        throw new UnsupportedOperationException("不支持读取");
    }

}
