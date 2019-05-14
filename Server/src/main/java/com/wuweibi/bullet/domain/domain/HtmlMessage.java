package com.wuweibi.bullet.domain.domain;

import java.util.Date;


public class HtmlMessage extends Message{
	private int command; //消息命令
	private long sequence; //消息序列
	private Date time;
	

	private String fromUser;
	private String toUser;
	private String content;
	
	
	public HtmlMessage() {
		this.setCommand(Message.MSG_TEXT);
	}
	 
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getFromUser() {
		return fromUser;
	}
	public void setFromUser(String fromUser) {
		this.fromUser = fromUser;
	}
	public String getToUser() {
		return toUser;
	}
	public void setToUser(String toUser) {
		this.toUser = toUser;
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
	 
	
	
	
	
	
	
}
