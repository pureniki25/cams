package com.hongte.alms.base.service;

import com.hongte.alms.base.collection.vo.CollectionTrackLogVo;
import com.hongte.alms.base.collection.vo.CollectionTrckLogReq;
import com.hongte.alms.common.vo.PageResult;

import java.util.List;

/**
 * @author:喻尊龙
 * @date: 2018/3/19
 */
public interface TrackLogService {

    PageResult<List<CollectionTrackLogVo>> selectCollectionTrackLogPage(CollectionTrckLogReq req);
}
