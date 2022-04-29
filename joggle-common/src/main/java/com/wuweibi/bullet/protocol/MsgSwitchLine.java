package com.wuweibi.bullet.protocol;
/**
 * Created by marker on 2017/12/7.
 */

import com.wuweibi.bullet.utils.Utils;
import lombok.Data;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

/**
 * 客户端日志开关消息
 *
 * @author marker
 * @create 2017-12-07 下午1:13
 **/

@Data
public class MsgSwitchLine extends Message {

    /**
     * 设备编号 20字节
     */
    private String deviceNo;

    /**
     * 服务器地址 100字节
     */
    private String serverAddr;


    /**
     * 构造
     */
    public MsgSwitchLine(String deviceNo, String serverAddr) {
        super(Message.SWITCH_LINE);
        this.deviceNo = deviceNo;
        this.serverAddr = serverAddr;
        getHead().setLength(super.getLength() + 120);
    }

    public MsgSwitchLine() {
        super(Message.SWITCH_LINE);
        getHead().setLength(super.getLength() + 120);
    }

    public MsgSwitchLine(MsgHead head) {
        super(Message.SWITCH_LINE, head);
        getHead().setLength(super.getLength() + 120);

    }


    @Override
    public void write(OutputStream out) throws IOException {
        getHead().write(out);

        byte bs[] = Arrays.copyOf(this.deviceNo.getBytes(StandardCharsets.UTF_8), 20);
        out.write(bs);
        bs = Arrays.copyOf(this.serverAddr.getBytes(), 100);
        out.write(bs);
        out.flush();
    }

    @Override
    public void read(InputStream in) throws IOException {
        // 读取deviceNo
        byte bs[] = new byte[20];
        in.read(bs);
        this.deviceNo = Utils.getString(bs, 0, 20);
        bs = new byte[100];
        in.read(bs);
        this.serverAddr = Utils.getString(bs, 0, 100);
    }


}
