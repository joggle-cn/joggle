package com.wuweibi.bullet.protocol;
/**
 * Created by marker on 2017/12/7.
 */

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.wuweibi.bullet.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * 新的映射请求
 *
 * @author marker
 * @create 2017-12-07 下午1:13
 **/
public class MsgMapping extends Message {



    // 转发端口
    private String json;


    /**
     * 构造
     */
    public MsgMapping(String json) {
        super(Message.NEW_MAPPING);
        int len = json.getBytes().length;
        getHead().setLength(super.getLength() + len);
        this.json = json;
    }

    public MsgMapping() {
        super(Message.NEW_MAPPING);
    }

    public MsgMapping(MsgHead head) {
        super(Message.NEW_MAPPING, head);
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

    public void saveFile(String filePath) {

        File file = new File(filePath);

        // {"protocol":1,"port":8080,"domain":"test","host":"192.168.1.4","description":"GitLab","id":16,"deviceId":6,"userId":1}

        JSONObject data = JSON.parseObject(this.json);







    }
}
