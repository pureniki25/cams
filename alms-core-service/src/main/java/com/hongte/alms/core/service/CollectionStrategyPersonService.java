package com.hongte.alms.core.service;

import com.hongte.alms.base.vo.module.CollectionStrategySinglePersonSettingReq;
import com.hongte.alms.common.result.Result;
import org.springframework.http.HttpHeaders;

/**
 * @author:喻尊龙
 * @date: 2018/3/17
 */
public interface CollectionStrategyPersonService {

    public Result saveStrategyPerson(CollectionStrategySinglePersonSettingReq req, HttpHeaders headers);
}
