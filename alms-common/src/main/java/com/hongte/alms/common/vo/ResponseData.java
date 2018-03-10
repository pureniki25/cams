package com.hongte.alms.common.vo;

public class ResponseData {
	private String Token;
	private String ReturnCode;
	private String ReturnMessage;
	private String Data;
	private String Now;

	public String getToken() {
		return Token;
	}

	public void setToken(String token) {
		Token = token;
	}

	public String getReturnCode() {
		return ReturnCode;
	}

	public void setReturnCode(String returnCode) {
		ReturnCode = returnCode;
	}

	public String getReturnMessage() {
		return ReturnMessage;
	}

	public void setReturnMessage(String returnMessage) {
		ReturnMessage = returnMessage;
	}

	public String getData() {
		return Data;
	}

	public void setData(String data) {
		Data = data;
	}

	public String getNow() {
		return Now;
	}

	public void setNow(String now) {
		Now = now;
	}
}
