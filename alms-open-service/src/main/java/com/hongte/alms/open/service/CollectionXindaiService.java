package com.hongte.alms.open.service;

import feign.Headers;
import feign.RequestLine;

/**
 * @author zengkun
 * @since 2018/5/31
 */
public interface CollectionXindaiService {
    @RequestLine("POST /api/ltgproject/dod ")
    @Headers("Content-Type: application/json")
    String transferOnePhoneSet(String content);



    @RequestLine("POST /api/ltgproject/dod ")
    @Headers("Content-Type: application/json")
    String transferOneVisitSet(String content);




    @RequestLine("POST /api/ltgproject/dod ")
    @Headers("Content-Type: application/json")
    String transferOneCollectionLog(String content);


    @RequestLine("POST /api/ltgproject/dod ")
    @Headers("Content-Type: application/json")
    String deleteXdCollectionLogById(String content);


    /**
     * 更新信贷还款计划接口，具体方法名在参数中指定
     *
     * @param content 加密后的请求数据及请求方法名称
     * @return 加密的结果数据
     * @author 张贵宏
     * @date 2018/6/21 14:44
     */
    @RequestLine("POST /api/ltgproject/dod")
    @Headers(("Content-Type: application/json"))
    String updateRepaymentPlan(String content);
}
