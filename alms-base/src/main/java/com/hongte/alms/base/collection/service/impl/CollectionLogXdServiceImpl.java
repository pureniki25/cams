package com.hongte.alms.base.collection.service.impl;

import com.hongte.alms.base.collection.entity.CollectionLogXd;
import com.hongte.alms.base.collection.mapper.CollectionLogXdMapper;
import com.hongte.alms.base.collection.service.CollectionLogXdService;
import com.hongte.alms.base.vo.module.CollectionReq;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * [电话催收分配记录表] 服务实现类
 * </p>
 *
 * @author 喻尊龙
 * @since 2018-03-29
 */
@Service("CollectionLogXdService")
public class CollectionLogXdServiceImpl extends BaseServiceImpl<CollectionLogXdMapper, CollectionLogXd> implements CollectionLogXdService {

    @Autowired
    private CollectionLogXdMapper collectionLogXdMapper;

    @Override
    public List<CollectionLogXd> queryNotTransferCollectionLog(CollectionReq req) {
        return collectionLogXdMapper.queryNotTransferCollectionLog(req);
    }

    @Override
    public int queryNotTransferCollectionLogCount() {
        return collectionLogXdMapper.queryNotTransferCollectionLogCount();
    }
}
