package com.wuweibi.bullet.protocol;
/**
 * Created by marker on 2017/12/7.
 */

import com.wuweibi.bullet.ByteUtils;
import com.wuweibi.bullet.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;

/**
 * @author marker
 * @create 2017-12-07 下午1:13
 **/
public class MsgProxyHttp extends Message {



    // 转发端口
    private int port;

    // 服务器地址（若没有就是本机）
    private String serverAddr;

    // 请求内容(不计入协议长度)
    private byte[] content;


    /**
     * 构造
     */
    public MsgProxyHttp(String serverAddr, int port) {
        super(Message.Proxy_Http);
        int len = serverAddr.getBytes().length;
        getHead().setLength(super.getLength() + len + 4);
        this.serverAddr = serverAddr;
        this.port = port;
    }

    public MsgProxyHttp() {
        super(Message.Proxy_Http);
    }

    public MsgProxyHttp(MsgHead head) {
        super(Message.Proxy_Http, head);
    }


    @Override
    public void write(OutputStream out) throws IOException {
        getHead().write(out);
        out.write(Utils.IntToBytes4(port));
        out.write(serverAddr.getBytes());
        out.write(content);
        out.flush();
    }

    @Override
    public void read(InputStream in) throws IOException {
        int len = getLength() - 24;
        byte bs[] = new byte[len];
        in.read(bs);
        this.port = Utils.Bytes4ToInt(bs,0);// port
        this.serverAddr = Utils.getString(bs, 4, len - 4);
        // 读取承载协议数据
        this.content    = ByteUtils.input2byte(in);
    }


    public void setContent(byte[] content) {
        this.content = content;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getServerAddr() {
        return serverAddr;
    }

    public void setServerAddr(String serverAddr) {
        this.serverAddr = serverAddr;
    }

    public byte[] getContent() {
        return content;
    }
}
