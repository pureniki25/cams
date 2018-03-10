package com.hongte.alms.base.collection.service;


import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.collection.entity.CollectionPersonSet;
import com.hongte.alms.base.vo.module.CollectionStrategyPersonSettingReq;
import com.hongte.alms.base.vo.module.CollectionStrategyPersonSettingVo;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 清算人员设置表 服务类
 * </p>
 *
 * @author 曾坤
 * @since 2018-03-08
 */
public interface CollectionPersonSetService extends BaseService<CollectionPersonSet> {
    Page<CollectionStrategyPersonSettingVo> getCollectionPersonSettingList(CollectionStrategyPersonSettingReq req);
}
