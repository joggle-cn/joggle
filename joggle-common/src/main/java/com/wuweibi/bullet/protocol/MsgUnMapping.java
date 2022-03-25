package com.wuweibi.bullet.protocol;
/**
 * Created by marker on 2017/12/7.
 */

import com.wuweibi.bullet.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * 解除映射请求
 *
 * @author marker
 * @create 2017-12-07 下午1:13
 **/
public class MsgUnMapping extends Message {



    // 转发端口
    private String json;


    /**
     * 构造
     */
    public MsgUnMapping(String json) {
        super(Message.NEW_UNMAPPING);
        int len = json.getBytes().length;
        getHead().setLength(super.getLength() + len);
        this.json = json;
    }

    public MsgUnMapping() {
        super(Message.NEW_UNMAPPING);
    }

    public MsgUnMapping(MsgHead head) {
        super(Message.NEW_UNMAPPING, head);
    }


    @Override
    public void write(OutputStream out) throws IOException {
        getHead().write(out);
        out.write(json.getBytes());
        out.flush();
    }

    @Override
    public void read(InputStream in) throws IOException {
        int len = getLength() - 24;
        byte bs[] = new byte[len];
        in.read(bs);
        this.json = Utils.getString(bs, 0, len);
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

}
