package com.hongte.alms.open.service;



import feign.Headers;
import feign.RequestLine;

/**
 * @author chenzesheng
 * @date 2018/3/3 17:37
 */
public interface WithHoldingXinDaiService {
    @RequestLine("POST /api/ltgproject/dod ")
    @Headers("Content-Type: application/json")
    String doold(String content);
}
