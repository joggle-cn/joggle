package com.wuweibi.bullet.protocol;
/**
 * Created by marker on 2017/12/7.
 */

import com.wuweibi.bullet.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 客户端日志开关消息
 *
 * @author marker
 * @create 2017-12-07 下午1:13
 **/
public class MsgLogOpen extends Message {

    /**
     * 设备Id
     */
    private Long deviceId;

    /**
     * 日志开关 1 打开， 0 关闭
     */
    private int open;

    /**
     * 构造
     */
    public MsgLogOpen(Long deviceId, int open) {
        super(Message.LOG_MAPPING_STATUS);
        this.deviceId = deviceId;
        this.open = open;
        getHead().setLength(super.getLength() + 12);
    }

    public MsgLogOpen() {
        super(Message.LOG_MAPPING_STATUS);
    }

    public MsgLogOpen(MsgHead head) {
        super(Message.LOG_MAPPING_STATUS, head);
    }


    @Override
    public void write(OutputStream out) throws IOException {
        getHead().write(out);
        out.write( Utils.LongToBytes8(this.deviceId));
        out.write( Utils.IntToBytes4(this.open));
        out.flush();
    }

    @Override
    public void read(InputStream in) throws IOException {
        // 读取deviceNo
        byte bs[] = new byte[12];
        in.read(bs);
        this.deviceId = Utils.Bytes8ToLong(bs);
        this.open = Utils.Bytes4ToInt(bs, 8);
    }

    public Long getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(Long deviceId) {
        this.deviceId = deviceId;
    }

    public int getOpen() {
        return open;
    }

    public void setOpen(int open) {
        this.open = open;
    }
}
