package com.hongte.alms.base.service;

import com.hongte.alms.base.entity.Api;
import com.hongte.alms.base.vo.module.api.XiaodaiParamRequest;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * api对接配置 服务类
 * </p>
 *
 * @author 曾坤
 * @since 2018-03-01
 */
public interface ApiService extends BaseService<Api> {
    /**
     * 返回对接小贷的token
     */
    String getXiaoDaiToken();

    /**
     * 返回调用小贷页面的请求包
     */
    XiaodaiParamRequest getXiaodaiParamRequest(String functionCode, String bussineId, String userId) throws Exception;

}
