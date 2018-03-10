/*
 * Copyright (C), 2017-2018 广东鸿特信息咨询有限公司
 * FileName: upLoadResult.java
 * Author:   伦惠峰
 * Date:     2018/1/29 8:51
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间         版本号             描述
 */
package com.hongte.alms.base.vo.module.doc;

/**
 * <br>
 * <br>
 * 上传后结果
 * @author 伦惠峰
 * @Date 2018/1/29 8:51
 */
public class UpLoadResult {
    /**
     * 是否上传成功
     */
    private boolean isUploaded;

    /**
     * 错误信息
     */
    private String message;

    /**
     *当前上传的文件列表
     */
    private String docItemListJson;



    public boolean isUploaded() {
        return isUploaded;
    }

    public void setUploaded(boolean uploaded) {
        isUploaded = uploaded;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDocItemListJson() {
        return docItemListJson;
    }

    public void setDocItemListJson(String docItemListJson) {
        this.docItemListJson = docItemListJson;
    }
}
