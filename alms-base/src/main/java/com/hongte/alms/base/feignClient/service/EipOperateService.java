package com.hongte.alms.base.feignClient.service;

import com.hongte.alms.base.collection.entity.CollectionTrackLog;
import com.ht.ussp.core.Result;

import java.util.List;

/**
 * @author:喻尊龙
 * @date: 2018/3/13
 */
public interface EipOperateService {

    Result addProjectTract(CollectionTrackLog log);

    Result reAddProjectTract(List<CollectionTrackLog> trackLogList);
}
