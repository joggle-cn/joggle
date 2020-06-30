package com.wuweibi.bullet.protocol;
/**
 * Created by marker on 2017/12/7.
 */

import com.wuweibi.bullet.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 客户端日志消息
 *
 * @author marker
 * @create 2017-12-07 下午1:13
 **/
public class MsgCommandLog extends Message {

    /**
     * 映射Id
     */
    private Long mappingId;

    /**
     * 一行日志
     */
    private String line;

    /**
     * 构造
     */
    public MsgCommandLog(Long mappingId, String line) {
        super(Message.LOG_MAPPING_LOG);
        this.mappingId = mappingId;
        this.line = line;
    }

    public MsgCommandLog() {
        super(Message.LOG_MAPPING_LOG);
    }

    public MsgCommandLog(MsgHead head) {
        super(Message.LOG_MAPPING_LOG, head);
    }


    @Override
    public void write(OutputStream out) throws IOException {
        byte[] strs = line.getBytes();
        getHead().setLength(super.getLength() + strs.length);
        getHead().write(out);
        out.write(Utils.LongToBytes8(this.mappingId));
        out.write(strs);
        out.flush();
    }

    @Override
    public void read(InputStream in) throws IOException {
        int strlen = this.getHead().getLength() - 24 - 8;
        // 读取deviceNo
        byte bs[] = new byte[8];
        in.read(bs);
        this.mappingId = Utils.Bytes8ToLong(bs);
        byte bs2[] = new byte[strlen];
        in.read(bs2);
        this.line = Utils.getString(bs2, 0, strlen);
    }

    public Long getMappingId() {
        return mappingId;
    }

    public void setMappingId(Long mappingId) {
        this.mappingId = mappingId;
    }

    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }
}
