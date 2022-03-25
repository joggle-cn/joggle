package com.wuweibi.bullet.protocol;
/**
 * Created by marker on 2017/12/7.
 */

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 设备解除绑定
 *
 * @author marker
 * @create 2021-07-31 下午11:13
 **/
@Slf4j
public class MsgUnBind extends Message {

    /**
     * 构造
     */
    public MsgUnBind() {
        super(Message.UNBIND);
    }

    public MsgUnBind(MsgHead head) {
        super(Message.UNBIND, head);
    }


    @Override
    public void write(OutputStream out) throws IOException {
        getHead().write(out);
        log.debug("send {}", this.toString());
        out.flush();
    }

    @Override
    public void read(InputStream in) throws IOException {
        log.debug("reciver {}", this.toString());
    }


}
