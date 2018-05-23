package com.hongte.alms.base.collection.service;

import com.hongte.alms.base.collection.entity.CollectionLogXd;
import com.hongte.alms.base.vo.module.CollectionReq;
import com.hongte.alms.common.service.BaseService;

import java.util.List;

/**
 * <p>
 * [电话催收分配记录表] 服务类
 * </p>
 *
 * @author 喻尊龙
 * @since 2018-03-29
 */
public interface CollectionLogXdService extends BaseService<CollectionLogXd> {

    List<CollectionLogXd> queryNotTransferCollectionLog(CollectionReq req);

    int queryNotTransferCollectionLogCount();

}
