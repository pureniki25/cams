package com.hongte.alms.base.mapper;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.hongte.alms.base.entity.CollectionPersonSet;
import com.hongte.alms.base.vo.module.CollectionStrategyPersonSettingReq;
import com.hongte.alms.base.vo.module.CollectionStrategyPersonSettingVo;
import com.hongte.alms.common.mapper.SuperMapper;

import java.util.List;

/**
 * <p>
 * 清算人员设置表 Mapper 接口
 * </p>
 *
 * @author dengzhiming
 * @since 2018-03-05
 */
public interface CollectionPersonSetMapper extends SuperMapper<CollectionPersonSet> {
    /**
     * 分页查询
     * @param key
     * @return
     */
    List<CollectionStrategyPersonSettingVo> getCollectionPersonSettingList(Pagination pages, CollectionStrategyPersonSettingReq key);
}
