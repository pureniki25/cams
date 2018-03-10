package com.hongte.alms.base.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.CollectionPersonSet;
import com.hongte.alms.base.vo.module.CollectionStrategyPersonSettingReq;
import com.hongte.alms.base.vo.module.CollectionStrategyPersonSettingVo;
import com.hongte.alms.common.service.BaseService;

import java.util.List;

/**
 * <p>
 * 清算人员设置表 服务类
 * </p>
 *
 * @author dengzhiming
 * @since 2018-03-05
 */
public interface CollectionPersonSetService extends BaseService<CollectionPersonSet> {
    Page<CollectionStrategyPersonSettingVo> getCollectionPersonSettingList(CollectionStrategyPersonSettingReq req);
}
