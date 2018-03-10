package com.hongte.alms.base.service;
import com.hongte.alms.base.entity.MoneyPool;
import com.hongte.alms.base.entity.MoneyPoolRepayment;

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
}
