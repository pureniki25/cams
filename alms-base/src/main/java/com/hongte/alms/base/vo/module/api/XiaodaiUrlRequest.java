/*
 * Copyright (C), 2017-2018 广东鸿特信息咨询有限公司
 * FileName: LawSysUrlRequest.java
 * Author:   伦惠峰
 * Date:     2018/1/30 16:25
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间         版本号             描述
 */
package com.hongte.alms.base.vo.module.api;

/**
 * <br>
 * <br>
 *
 * @author 伦惠峰
 * @Date 2018/1/30 16:25
 */
public class XiaodaiUrlRequest {
    /**
     * 打开的地址
     */
   private String openUrl;

    /**
     * 传递请求的结果包
     */
   private String message;

    public String getOpenUrl() {
        return openUrl;
    }

    public void setOpenUrl(String openUrl) {
        this.openUrl = openUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
