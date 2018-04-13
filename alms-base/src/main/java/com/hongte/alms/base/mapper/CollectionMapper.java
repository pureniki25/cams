package com.hongte.alms.base.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.Collection;
import com.hongte.alms.base.vo.module.CollectionReq;
import com.hongte.alms.common.mapper.SuperMapper;

import java.util.List;

/**
 * <p>
 * [催收信息主表] Mapper 接口
 * </p>
 *
 * @author 喻尊龙
 * @since 2018-03-29
 */
public interface CollectionMapper extends SuperMapper<Collection> {

    List<Collection> queryNotTransferCollection(CollectionReq req);
    List<Collection> queryNotTransferCollection();

    int queryNotTransferCollectionCount();

}
