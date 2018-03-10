package com.hongte.alms.base.collection.service.impl;


import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.collection.entity.CollectionPersonSet;
import com.hongte.alms.base.collection.mapper.CollectionPersonSetMapper;
import com.hongte.alms.base.collection.service.CollectionPersonSetService;
import com.hongte.alms.base.vo.module.CollectionStrategyPersonSettingReq;
import com.hongte.alms.base.vo.module.CollectionStrategyPersonSettingVo;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 清算人员设置表 服务实现类
 * </p>
 *
 * @author 曾坤
 * @since 2018-03-08
 */
@Service("CollectionPersonSetService")
public class CollectionPersonSetServiceImpl extends BaseServiceImpl<CollectionPersonSetMapper, CollectionPersonSet> implements CollectionPersonSetService {

    @Autowired
    private CollectionPersonSetMapper collectionPersonSetMapper;

    @Override
    public Page<CollectionStrategyPersonSettingVo> getCollectionPersonSettingList(CollectionStrategyPersonSettingReq req) {
        Page<CollectionStrategyPersonSettingVo> pages = new Page<>();
        pages.setCurrent(req.getPage());
        pages.setSize(req.getLimit());

        pages.setRecords(collectionPersonSetMapper.getCollectionPersonSettingList(pages,req));

        return pages;

    }
}
