package com.wuweibi.bullet.protocol;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 抽象消息类
 * @author marker
 * */
public abstract class Message {

	public static final int CONNECT        = 0x1;



	public static final int CONNECT_RESP   = 0x80000001;
	public static final int Proxy_Http      = 0x2;
	public static final int TERMINATE_RESP = 0x80000002;
	public static final int Heart          = 0x3;// 心跳消息
	public static final int SUBMIT_RESP    = 0x80000003;
	public static final int DELIVER        = 0x4;
	public static final int DELIVER_RESP   = 0x80000004;
	public static final int REPORT         = 0x5;
	public static final int REPORT_RESP    = 0x80000005;
	public static final int ACTIVE_TEST    = 0x10000001;//链路测试命令
	public static final int ACTIVE_TEST_RESP = 0x10000010;


	public static final int NEW_MAPPING      = 0x6;
	public static final int NEW_UNMAPPING      = 0x60000001;
	public static final int NEW_BINDIP      = 0x7;

	
	
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
}
