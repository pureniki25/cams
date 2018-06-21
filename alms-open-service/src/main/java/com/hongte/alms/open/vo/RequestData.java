package com.hongte.alms.open.vo;





/**
 * Created by chenzs on 2018/3/08.
 */
public class RequestData {
 
	private String Data;
    


	private String MethodName;


	public String getMethodName() {
		return MethodName;
	}

	public String getData() {
		return Data;
	}

	public void setData(String data) {
		Data = data;
	}

	public void setMethodName(String methodName) {
		MethodName = methodName;
	}

	public RequestData() {
	}

	public RequestData(String data, String methodName) {
		Data = data;
		MethodName = methodName;
	}
}
