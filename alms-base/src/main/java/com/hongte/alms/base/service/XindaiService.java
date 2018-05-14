package com.hongte.alms.base.service;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
/**
 * @author dengzhiming
 * @date 2018/3/2 14:16
 */
public interface XindaiService {
    @RequestLine("GET /CommonArea/OpenPhoto/ThumbnailView/{businessId}")
    String getThumbnailView(@Param(value = "businessId") String businessId);
    
    @RequestLine("POST /api/ltgproject/dod/")
    @Headers("Content-Type: application/json")
    String dod(String content) ;
    
    
    //减免申请审批通过同步费用项给信贷系统
    @RequestLine("POST /api/ltgproject/dod/")
    @Headers("Content-Type: application/json")
    String syc(String content) ;
    
    
    
    //减免申请获取合同日期
    
    @RequestLine("POST /api/ltgproject/dod ")
    @Headers("Content-Type: application/json")
    String getContractDate(String content);
}
