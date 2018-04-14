package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.Collection;
import com.hongte.alms.base.mapper.CollectionMapper;
import com.hongte.alms.base.service.CollectionService;
import com.hongte.alms.base.vo.module.CollectionReq;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * [催收信息主表] 服务实现类
 * </p>
 *
 * @author 喻尊龙
 * @since 2018-03-29
 */
@Service("CollectionService")
public class CollectionServiceImpl extends BaseServiceImpl<CollectionMapper, Collection> implements CollectionService {

    @Autowired
    private CollectionMapper collectionMapper;

    @Override
    public List<Collection> queryNotTransferCollection(CollectionReq req) {
        return collectionMapper.queryNotTransferCollection(req);
    }

    @Override
    public List<Collection> queryNotTransferCollection() {
        return collectionMapper.queryNotTransferCollection();
    }

    @Override
    public int queryNotTransferCollectionCount() {
        return collectionMapper.queryNotTransferCollectionCount();
    }
}
