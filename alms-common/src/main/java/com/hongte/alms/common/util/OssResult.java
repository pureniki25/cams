/*
 * Copyright (C), 2017-2018 广东鸿特信息咨询有限公司
 * FileName: UpLoadResult.java
 * Author:   伦惠峰
 * Date:     2018/1/26 20:52
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间         版本号             描述
 */
package com.hongte.alms.common.util;

/**
 * <br>
 * <br>
 * 上传结果
 * @author 伦惠峰
 * @Date 2018/1/26 20:52
 */
public class OssResult {
    /**
     * 是否上传成功
     */
   private boolean isSuccess;

    /**
     * 错误信息
     */
   private String errMessage;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }
}
