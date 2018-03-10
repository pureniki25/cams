package com.hongte.alms.base.collection.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.hongte.alms.base.collection.entity.CollectionTrackLog;
import com.hongte.alms.base.collection.mapper.CollectionTrackLogMapper;
import com.hongte.alms.base.collection.service.CollectionTrackLogService;
import com.hongte.alms.base.collection.vo.AfterLoanStandingBookVo;
import com.hongte.alms.base.collection.vo.CollectionTrackLogVo;
import com.hongte.alms.base.collection.vo.CollectionTrckLogReq;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 贷后跟踪记录表 服务实现类
 * </p>
 *
 * @author 曾坤
 * @since 2018-02-01
 */
@Service("CollectionTrackLogService")
public class CollectionTrackLogServiceImpl extends BaseServiceImpl<CollectionTrackLogMapper, CollectionTrackLog> implements CollectionTrackLogService {

    @Autowired
    CollectionTrackLogMapper collectionTrackLogMapper;


    @Override
    public Page<CollectionTrackLogVo> selectCollectionTrackLogByRbpId(CollectionTrckLogReq req){
        Page<CollectionTrackLogVo> pages = new Page<>();
        pages.setCurrent(req.getPage());
        pages.setSize(req.getLimit());

        pages.setRecords(collectionTrackLogMapper.selectCollectionTrackLogByRbpId(pages,req.getRbpId()));

        return pages;

    }


}
