package com.hongte.alms.base.vo.litigation;

import java.io.Serializable;

public class LitigationResponse implements Serializable{
	private static final long serialVersionUID = 1L;
	private int code;
	private String msg;
	private LitigationResponseData data;

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public LitigationResponseData getData() {
		return data;
	}

	public void setData(LitigationResponseData data) {
		this.data = data;
	}

	@Override
	public String toString() {
		return "LitigationResponse [code=" + code + ", msg=" + msg + ", data=" + data + "]";
	}

}
