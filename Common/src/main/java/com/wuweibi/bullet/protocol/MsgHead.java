package com.wuweibi.bullet.protocol;


import com.wuweibi.bullet.utils.Sequence;
import com.wuweibi.bullet.utils.Utils;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MsgHead {

	private int  length;//消息长度
	private int  command;//消息命令
	private long sequence1;// 命令产生的日期和时间
	private long sequence2;// 0开始递增（支持最大32位）

	public MsgHead() {
		length = 24;
		this.sequence1 = System.currentTimeMillis();
		this.sequence2 = Sequence.next();
    }
	
	
	public void write(OutputStream out) throws IOException {
		out.write(Utils.IntToBytes4(length));
		out.write(Utils.IntToBytes4(command));
		out.write(Utils.LongToBytes8(sequence1));
		out.write(Utils.LongToBytes8(sequence2));
	}

	public void read(InputStream in) throws IOException {
		// 读取头部信息20个字节
		byte head[] = new byte[24];
		int rc = in.read(head);
		if (rc < 24) {
			throw new EOFException(String.valueOf(rc));
		} else {
			length    = Utils.Bytes4ToInt(head, 0);
			command   = Utils.Bytes4ToInt(head, 4);
			sequence1 = Utils.Bytes8ToLong(head, 8);
			sequence2 = Utils.Bytes8ToLong(head, 16);
		}
	}
	
	
	
	public String toString() {
		return new StringBuffer().append("head:").append("len=").append(length)
				.append(",cmd=").append(command).append(",seq=")
				.append(getSequence())
				.toString();
	}
	
	public String getSequence() {
		return sequence1+""+sequence2;
	}
	
	public long getSequence1() {
		return sequence1;
	}

	public void setSequence1(long sequence1) {
		this.sequence1 = sequence1;
	}

	public long getSequence2() {
		return sequence2;
	}

	public void setSequence2(long sequence2) {
		this.sequence2 = sequence2;
	}

	public int getCommand() {
		return command;
	}

	public void setCommand(int command) {
		this.command = command;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}

}
