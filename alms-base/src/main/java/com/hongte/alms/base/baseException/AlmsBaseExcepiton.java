package com.hongte.alms.base.baseException;

public class AlmsBaseExcepiton extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
    private String msg;
    private String code = "9999";
    
    public AlmsBaseExcepiton(String msg) {
		super(msg);
		this.msg = msg;
	}
	
	public AlmsBaseExcepiton(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}
	
	public AlmsBaseExcepiton(String msg, String code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}
	
	public AlmsBaseExcepiton(String msg, String code, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	

}
