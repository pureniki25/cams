package com.hongte.alms.service.test;

import com.google.gson.annotations.SerializedName;

/**
 * @author dengzhiming
 * @date 2018/3/3 18:21
 */
public class ApiResult {
    /*因为信贷返回的是大写，所以要设置别名*/
    @SerializedName("Status")
    private String status;
    @SerializedName("Message")
    private String message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
