package com.hongte.alms.base.mapper;

import com.hongte.alms.base.entity.CollectionLogXd;
import com.hongte.alms.base.vo.module.CollectionReq;
import com.hongte.alms.common.mapper.SuperMapper;

import java.util.List;

/**
 * <p>
 * [电话催收分配记录表] Mapper 接口
 * </p>
 *
 * @author 喻尊龙
 * @since 2018-03-29
 */
public interface CollectionLogXdMapper extends SuperMapper<CollectionLogXd> {

    List<CollectionLogXd> queryNotTransferCollectionLog(CollectionReq req);

    int queryNotTransferCollectionLogCount();

}
