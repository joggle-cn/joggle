package com.wuweibi.bullet.protocol;
/**
 * Created by marker on 2017/12/7.
 */

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 检查更新消息
 *
 * @author marker
 * @create 2023-05-06 下午1:13
 **/
public class MsgCheckUpdate extends Message {



    public MsgCheckUpdate() {
        super(Message.CHECK_UPDATE);
    }

    public MsgCheckUpdate(MsgHead head) {
        super(Message.CHECK_UPDATE, head);

    }


    @Override
    public void write(OutputStream out) throws IOException {
        getHead().write(out);
        out.flush();
    }

    @Override
    public void read(InputStream in) throws IOException {
        throw new EOFException("不支持的操作");
    }


}
