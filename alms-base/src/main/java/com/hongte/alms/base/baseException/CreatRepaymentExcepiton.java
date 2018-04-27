package com.hongte.alms.base.baseException;

/**
 * 创建还款计划 相关异常定义
 */
public class CreatRepaymentExcepiton extends RuntimeException {

	private static final long serialVersionUID = 1L;

    private String msg;
    private String code = "9889";

    public CreatRepaymentExcepiton(String msg) {
		super(msg);
		this.msg = msg;
	}

	public CreatRepaymentExcepiton(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}

	public CreatRepaymentExcepiton(String msg, String code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}

	public CreatRepaymentExcepiton(String msg, String code, Throwable e) {
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
