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
     * 映射Id
     */
    private Long mappingId;

    /**
     * 日志开关 1 打开， 0 关闭
     */
    private int open;



    /**
     * 构造
     */
    public MsgLogOpen(Long mappingId, int open) {
        super(Message.LOG_MAPPING_STATUS);
        this.mappingId = mappingId;
        this.open = open;
        getHead().setLength(super.getLength() + 9);
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
        out.write( Utils.LongToBytes8(this.mappingId));
        out.write( Utils.IntToByte(this.open));
        out.flush();
    }

    @Override
    public void read(InputStream in) throws IOException {
        // 读取deviceNo
        byte bs[] = new byte[9];
        in.read(bs);
        this.mappingId = Utils.Bytes8ToLong(bs);
        this.open = Utils.ByteToInt(bs[9]);
    }



}
