package com.hongte.alms.base.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.Collection;
import com.hongte.alms.base.vo.module.CollectionReq;
import com.hongte.alms.common.service.BaseService;

import java.util.List;

/**
 * <p>
 * [催收信息主表] 服务类
 * </p>
 *
 * @author 喻尊龙
 * @since 2018-03-29
 */
public interface CollectionService extends BaseService<Collection> {

    List<Collection> queryNotTransferCollection(CollectionReq req);

    List<Collection> queryNotTransferCollection();

    int queryNotTransferCollectionCount();

}
