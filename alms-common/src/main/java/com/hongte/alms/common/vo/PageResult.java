package com.hongte.alms.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: 黄咏康
 * @Date: 2018/1/14 0014 下午 8:59
 */
@ApiModel
public class PageResult<T> {
    /**
     * 数据状态
     */
    @ApiModelProperty(required = true, value = "错误代码")
    private int code;
    /**
     * 状态信息
     */
    @ApiModelProperty(required = true, value = "错误描述")
    private String msg;
    /**
     * 数据总数
     */
    private int count;
    /**
     * 数据列表
     */
    @ApiModelProperty(value = "传递给请求者的数据")
    private T data;

    /**
     * 构造请求成功的结果对象<br>
     *
     * @date 2017年11月20日 下午3:47:10
     * @return 请求的结果对象
     */
    public static <T> PageResult<T> success(int count) {
        PageResult<T> PageResult = new PageResult<T>();
        PageResult.setCode(0);//layui中的表格组件，当返回0时表示获取数据成功
        PageResult.setMsg("请求成功！");
        return PageResult;
    }

    /**
     * 构造请求成功的结果对象<br>
     * @date 2017年11月20日 下午3:47:36
     * @param data 返回的数据集
     * @return 请求的结果对象
     */
    public static <T> PageResult<T> success(T data,int count) {
        PageResult<T> PageResult = new PageResult<T>();
        PageResult.setCode(0);//layui中的表格组件，当返回0时表示获取数据成功
        PageResult.setCount(count);
        PageResult.setMsg("请求成功！");
        PageResult.setData(data);
        return PageResult;
    }

    /**
     * 构造请求失败的结果对象<br>
     *
     * @date 2017年11月20日 下午3:48:01
     * @param code 结果代码
     * @param msg 结果描述
     * @return 请求的结果对象
     */
    public static <T> PageResult<T> error(int code, String msg) {
        PageResult<T> PageResult = new PageResult<T>();
        PageResult.setCode(code);
        PageResult.setMsg(msg);
        return PageResult;
    }

    /**
     * 构造结果对象<br>
     *
     * @date 2017年11月20日 下午3:48:01
     * @param code 结果代码
     * @param msg 结果描述
     * @param data 返回的数据集
     * @return 请求的结果对象
     */
    public static <T> PageResult<T> build(int code, String msg, T data) {
        PageResult<T> PageResult = new PageResult<T>();
        PageResult.setCode(code);
        PageResult.setMsg(msg);
        PageResult.setData(data);
        return PageResult;
    }

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

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
