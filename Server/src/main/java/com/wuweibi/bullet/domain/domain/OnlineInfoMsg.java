package com.wuweibi.bullet.domain.domain;

import java.util.Date;


/**
 * 线上信息
 * 
 * @author marker
 * @version 1.0
 */
public class OnlineInfoMsg {
	private int command; //消息命令
	private long sequence; //消息序列
	private Date time;
	
	 
	
	/** 在线用户量 */ 
	private long totalCount;

	/** 等待用户 */
	private long waitCount;
	
	
	public OnlineInfoMsg() {
		this.command = Message.MSG_ONLINE_INFO;
		this.time = new Date(); 
	}
	
	
	public int getCommand() {
		return command;
	}

	public void setCommand(int command) {
		this.command = command;
	}

	public long getSequence() {
		return sequence;
	}

	public void setSequence(long sequence) {
		this.sequence = sequence;
	}

	public Date getTime() {
		return time;
	}

	public void setTime(Date time) {
		this.time = time;
	}


	public long getTotalCount() {
		return totalCount;
	}


	public void setTotalCount(long totalCount) {
		this.totalCount = totalCount;
	}


	public long getWaitCount() {
		return waitCount;
	}


	public void setWaitCount(long waitCount) {
		this.waitCount = waitCount;
	}

 
	
}
