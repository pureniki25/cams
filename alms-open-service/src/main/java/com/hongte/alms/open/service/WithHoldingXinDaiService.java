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
    String withholding(String content);
    
    
    @RequestLine("POST /api/ltgproject/dod ")
    @Headers("Content-Type: application/json")
    String searchRepayRecord(String content);
    
    
    @RequestLine("POST /api/ltgproject/dod ")
    @Headers("Content-Type: application/json")
    String getContractDate(String content);
    
    
    @RequestLine("POST /api/ltgproject/dod ")
    @Headers("Content-Type: application/json")
    String getBankcardInfo(String identityCard);
}
