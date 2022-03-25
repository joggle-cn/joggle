package com.wuweibi.bullet.protocol;
/**
 * Created by marker on 2017/12/7.
 */

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
public class MsgAuth extends Message {


    /**
     * authToken
     */
    private String authToken;


    /**
     * 构造
     */
    public MsgAuth(String authToken) {
        super(Message.AUTH);
        getHead().setLength(super.getLength() + authToken.length());
        this.authToken = authToken;
    }

    public MsgAuth() {
        super(Message.AUTH);
    }

    public MsgAuth(MsgHead head) {
        super(Message.AUTH, head);
    }


    @Override
    public void write(OutputStream out) throws IOException {
        getHead().write(out);
        log.debug("send {}",this.toString());

        // 写入authToken
        int len = authToken.getBytes().length;
        byte bs[] = new byte[len];

        System.arraycopy(authToken.getBytes(), 0, bs, 0, len);
        out.write(bs);
        out.flush();
    }

    @Override
    public void read(InputStream in) throws IOException {
        int len = this.getLength() - MsgHead.HEAD_LENGTH;
        // 读取ip
        byte bs[] = new byte[len];
        in.read(bs);
        this.authToken = Utils.getString(bs, 0, len);

    }

    public String getAuthToken() {
        return authToken;
    }
}
