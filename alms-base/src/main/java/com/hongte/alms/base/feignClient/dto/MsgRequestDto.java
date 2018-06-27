package com.hongte.alms.base.feignClient.dto;

import java.io.Serializable;

/**
 * @author:陈泽圣
 * @date: 2018/6/24
 */

public class MsgRequestDto implements Serializable {
    private static final long serialVersionUID = -4095682638492039883L;

    /**
     * 系统标识
     */
    private String app;
  
    private String key1;
   
    private String key2;
    
    
    private String key3;
    
    
    private String key4;
    
    private String key5;

    private String msgBcc;
    /**
     * 短信消息体 jason数据
     */
    private Object msgBody;
    
    private String msgCc;
    /**
     * 模板Id
     */
    private Long msgModelId;
    private String msgTitle;
    private String msgTo;
	public String getApp() {
		return app;
	}
	public void setApp(String app) {
		this.app = app;
	}
	public String getKey1() {
		return key1;
	}
	public void setKey1(String key1) {
		this.key1 = key1;
	}
	public String getKey2() {
		return key2;
	}
	public void setKey2(String key2) {
		this.key2 = key2;
	}
	public String getKey3() {
		return key3;
	}
	public void setKey3(String key3) {
		this.key3 = key3;
	}
	public String getKey4() {
		return key4;
	}
	public void setKey4(String key4) {
		this.key4 = key4;
	}
	public String getKey5() {
		return key5;
	}
	public void setKey5(String key5) {
		this.key5 = key5;
	}
	public String getMsgBcc() {
		return msgBcc;
	}
	public void setMsgBcc(String msgBcc) {
		this.msgBcc = msgBcc;
	}
	
	public Object getMsgBody() {
		return msgBody;
	}
	public void setMsgBody(Object msgBody) {
		this.msgBody = msgBody;
	}
	public String getMsgCc() {
		return msgCc;
	}
	public void setMsgCc(String msgCc) {
		this.msgCc = msgCc;
	}
	
	public Long getMsgModelId() {
		return msgModelId;
	}
	public void setMsgModelId(Long msgModelId) {
		this.msgModelId = msgModelId;
	}
	public String getMsgTitle() {
		return msgTitle;
	}
	public void setMsgTitle(String msgTitle) {
		this.msgTitle = msgTitle;
	}
	public String getMsgTo() {
		return msgTo;
	}
	public void setMsgTo(String msgTo) {
		this.msgTo = msgTo;
	}
    
    

}

