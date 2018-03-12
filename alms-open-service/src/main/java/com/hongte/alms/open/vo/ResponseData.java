package com.hongte.alms.open.vo;
/**
 * Created by chenzs on 2018/3/08.
 */
public class ResponseData {
  private String token;
  private String returnCode;
  private String returnMessage;
  private String date;
  private String now;
  private String data;
public String getToken() {
	return token;
}
public String getData() {
	return data;
}
public void setData(String data) {
	this.data = data;
}
public void setToken(String token) {
	this.token = token;
}
public String getReturnCode() {
	return returnCode;
}
public void setReturnCode(String returnCode) {
	this.returnCode = returnCode;
}
public String getReturnMessage() {
	return returnMessage;
}
public void setReturnMessage(String returnMessage) {
	this.returnMessage = returnMessage;
}
public String getDate() {
	return date;
}
public void setDate(String date) {
	this.date = date;
}
public String getNow() {
	return now;
}
public void setNow(String now) {
	this.now = now;
}
}
