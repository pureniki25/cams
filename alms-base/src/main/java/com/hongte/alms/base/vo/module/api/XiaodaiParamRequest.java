/*
 * Copyright (C), 2017-2018 广东鸿特信息咨询有限公司
 * FileName: LawSysRequest.java
 * Author:   伦惠峰
 * Date:     2018/1/30 16:11
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间         版本号             描述
 */
package com.hongte.alms.base.vo.module.api;

import com.alibaba.fastjson.JSON;


/**
 * 调用信贷系统请求包
 * @author 伦惠峰
 * @Date 2018/1/30 16:11
 */
public class XiaodaiParamRequest {
    /**
     *当前请求ID(一个请求ID,只能使用一次)
     */
    private String requestId;
    /**
     *当前登录用户ID
     */
    private String userId;
    /**
     *当前调用时间(调用与被调用的服务器之间不能相差30分钟)
     */
    private String time;
    /**
     *调用的方法
     */
    private String functionCode;

    /**
     *业务编码
     */
    private String bussineId;

    /**
     *签名
     */
    private String sign;

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFunctionCode() {
        return functionCode;
    }

    public void setFunctionCode(String functionCode) {
        this.functionCode = functionCode;
    }

    public String getBussineId() {
        return bussineId;
    }

    public void setBussineId(String bussineId) {
        this.bussineId = bussineId;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    /**
     * 返回当前对象的JSON字符串
     * @return java.lang.String
     * @author 伦惠峰
     * @Date 2018/1/31 16:17
     */
    public String toJson()
    {
      return   JSON.toJSONString(this);
    }

    /**
     * 返回加密后的Base64字符串
     * @param
     * @return java.lang.String
     * @author 伦惠峰
     * @Date 2018/1/31 16:27
     */
    public String toJsonBasic64()
    {

        //step 1先转换成json
        String json=toJson();
        String code=""; //DESC.base64Encoder(json);
        return code;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
