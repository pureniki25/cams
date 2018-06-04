package com.hongte.alms.open.service;

import feign.Headers;
import feign.RequestLine;

/**
 * @author zengkun
 * @since 2018/5/31
 */
public interface CollectionXindaiService {
    @RequestLine("POST /api/collection/phoneSetTrans ")
    @Headers("Content-Type: application/json")
    String transferOnePhoneSet(String content);



    @RequestLine("POST /api/collection/visitSetTrans ")
    @Headers("Content-Type: application/json")
    String transferOneVisitSet(String content);




    @RequestLine("POST /api/collection/colLogTrans ")
    @Headers("Content-Type: application/json")
    String transferOneCollectionLog(String content);


    @RequestLine("POST /api/collection/deleteColLog ")
    @Headers("Content-Type: application/json")
    String deleteXdCollectionLogById(String content);
}
