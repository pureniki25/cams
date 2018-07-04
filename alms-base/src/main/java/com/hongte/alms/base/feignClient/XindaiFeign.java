package com.hongte.alms.base.feignClient;

import feign.Headers;
import feign.RequestLine;

public interface XindaiFeign {
    @RequestLine("POST /api/ltgproject/dod ")
    @Headers("Content-Type: application/json")
    String getBankcardInfo(String identityCard);
}
