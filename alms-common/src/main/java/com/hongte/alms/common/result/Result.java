/*
 * 文件名 Result.java
 * 版权 Copyright 2017 团贷网
 * 创建人 谭荣巧
 * 创建时间 2017年11月20日 下午3:40:50 
 */
package com.hongte.alms.common.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * Rest接口请求结果对象<br>
 * 
 * @author 谭荣巧
 * @date 2017年11月20日 下午3:40:50
 * @since alms-common 1.0-SNAPSHOT
 */
@ApiModel
public class Result<T> {
	/**
	 * 错误代码
	 */
	@ApiModelProperty(required = true, value = "错误代码")
	private String code;
	/**
	 * 错误描述
	 */
	@ApiModelProperty(required = true, value = "错误描述")
	private String msg;
	/**
	 * 传递给请求者的数据
	 */
	@ApiModelProperty(value = "传递给请求者的数据")
	private T data;

	public Result() {
		super();
	}

	/**
	 * 构造请求成功的结果对象<br>
	 * 
	 * @author 谭荣巧
	 * @date 2017年11月20日 下午3:47:10
	 * @return 请求的结果对象
	 * @since alms-common 1.0-SNAPSHOT
	 */
	public static <T> Result<T> success() {
		Result<T> result = new Result<T>();
		result.setCode("1");
		result.setMsg("请求成功！");
		return result;
	}

	/**
	 * 构造请求成功的结果对象<br>
	 * 
	 * @author 谭荣巧
	 * @date 2017年11月20日 下午3:47:36
	 * @param data
	 *            返回的数据集
	 * @return 请求的结果对象
	 * @since alms-common 1.0-SNAPSHOT
	 */
	public static <T> Result<T> success(T data) {
		Result<T> result = new Result<T>();
		result.setCode("1");
		result.setMsg("请求成功！");
		result.setData(data);
		return result;
	}

	/**
	 *  构造请求失败的结果对象<br>
	 *
	 * @param msg 结果描述
	 * @return com.hongte.alms.common.result.Result<T>  请求的结果对象
	 * @author 张贵宏
	 * @date 2018/6/14 11:06
	 */
	public static <T> Result<T> error(String msg){
		return error("500", msg);
	}

	/**
	 * 构造请求失败的结果对象<br>
	 * 
	 * @author 谭荣巧
	 * @date 2017年11月20日 下午3:48:01
	 * @param code
	 *            结果代码
	 * @param msg
	 *            结果描述
	 * @return 请求的结果对象
	 * @since alms-common 1.0-SNAPSHOT
	 */
	public static <T> Result<T> error(String code, String msg) {
		Result<T> result = new Result<T>();
		result.setCode(code);
		result.setMsg(msg);
		return result;
	}

	/**
	 * 构造结果对象<br>
	 * 
	 * @author 谭荣巧
	 * @date 2017年11月20日 下午3:48:01
	 * @param code
	 *            结果代码
	 * @param msg
	 *            结果描述
	 * @param data
	 *            返回的数据集
	 * @return 请求的结果对象
	 * @since alms-common 1.0-SNAPSHOT
	 */
	public static <T> Result<T> build(String code, String msg, T data) {
		Result<T> result = new Result<T>();
		result.setCode(code);
		result.setMsg(msg);
		result.setData(data);
		return result;
	}

	/**
	 * @return the code
	 */
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            the code to set
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return the msg
	 */
	public String getMsg() {
		return msg;
	}

	/**
	 * @param msg
	 *            the msg to set
	 */
	public void setMsg(String msg) {
		this.msg = msg;
	}

	/**
	 * @return the data
	 */
	public T getData() {
		return data;
	}

	/**
	 * @param data
	 *            the data to set
	 */
	public void setData(T data) {
		this.data = data;
	}

}
