package com.wuweibi.bullet.protocol;
/**
 * Created by marker on 2017/12/7.
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * 心跳消息
 *
 * @author marker
 * @create 2017-12-07 下午1:13
 **/
public class MsgHeart extends Message {



    /**
     * 构造
     */
    public MsgHeart() {
        super(Message.Heart);
    }

    public MsgHeart(MsgHead head) {
        super(Message.Heart, head);
    }


    @Override
    public void write(OutputStream out) throws IOException {
        getHead().write(out);
        out.flush();
    }

    @Override
    public void read(InputStream in) throws IOException {
        int len = getLength() - 24;
        byte bs[] = new byte[len];
        in.read(bs);
    }
}
