package com.wuweibi.bullet.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 抽象消息类
 *
 * @author marker
 */
public abstract class Message {

    public static final int DEVICE_NO = 0x8;
    public static final int CONNECT_RESP = 0x80000001;
    public static final int PROXY = 0x2;
    public static final int TERMINATE_RESP = 0x80000002;
    public static final int Heart = 0x3;// 心跳消息
    public static final int SUBMIT_RESP = 0x80000003;
    public static final int DELIVER = 0x4;
    public static final int DELIVER_RESP = 0x80000004;
    public static final int REPORT = 0x5;
    public static final int REPORT_RESP = 0x80000005;
    public static final int GET_DEVICE_STATUS = 0x11;
    public static final int GET_DEVICE_STATUS_RESP = 0x12;
    public static final int DEVICE_DOWN = 0x13;

    public static final int DEVICE_SECRET = 0x10000001;// 设备秘钥
    public static final int LOG_MAPPING_STATUS = 0x10000010; // 日志开关消息
    public static final int LOG_MAPPING_LOG = 0x10000011; // 日志 消息
    public static final int SWITCH_LINE = 0x10000012; // 切换线路消息
    public static final int DEVICE_DOOR = 0x10000013; // 设备任意门
    public static final int P2P_STUN = 0x10000014; // p2p

    public static final int DEVICE_METRICS = 0x10000015; // 统计数据


    public static final int NEW_MAPPING = 0x6;
    public static final int NEW_UNMAPPING = 0x60000001;
    public static final int NEW_BINDIP = 0x7;
    public static final int NEW_WOLMAC = 0x9;
    public static final int UNBIND = 0x10; // 解绑定设备
    public static final int AUTH = 0xa; // 认证
    public static final int AUTH_RESP = 0xa000001; //



    //** 服务器管理端与服务端的消息
    public static final int CONTROL_SERVER_WRAPPER = 1; // 管理端与服务器之间的消息
    public static final int CONTROL_CLIENT_WRAPPER = 2; // 管理端与客户端之间的消息

    public static final int CONTROL_WHITE_IPS = 3; // ip白名单

    //消息头
    private MsgHead head;


    public Message(int cmdId) {
        head = new MsgHead();
        head.setCommand(cmdId);
    }

    public Message(int cmdId, MsgHead head) {
        head.setCommand(cmdId);
        this.head = head;
    }

    public MsgHead getHead() {
        return head;
    }

    public void setHead(MsgHead head) {
        this.head = head;
    }

    public int getCommand() {
        return head.getCommand();
    }

    public String getSequence() {
        return head.getSequence();
    }

    public int getLength() {
        return head.getLength();
    }

    public abstract void write(OutputStream outputstream) throws IOException;

    public abstract void read(InputStream inputstream) throws IOException;


    /**
     * 根据头len 读取残余数据
     *
     * @param inputstream inputstream
     * @param bodyLen     协议消息长度
     * @throws IOException
     */
    protected void readResidue(InputStream inputstream, int bodyLen) throws IOException {
        // 兼容协议扩展，读取多余内容
        int lastLen = this.getLength() - MsgHead.HEAD_LENGTH - bodyLen;
        if (lastLen <= 0) {
            return;
        }
        byte[] bs = new byte[lastLen];
        inputstream.read(bs);
    }
}
