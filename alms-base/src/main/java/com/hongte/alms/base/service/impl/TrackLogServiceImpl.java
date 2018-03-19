package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.collection.mapper.CollectionTrackLogMapper;
import com.hongte.alms.base.collection.service.CollectionTrackLogService;
import com.hongte.alms.base.collection.vo.CollectionTrackLogVo;
import com.hongte.alms.base.collection.vo.CollectionTrckLogReq;
import com.hongte.alms.base.service.TrackLogService;
import com.hongte.alms.common.vo.PageResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author:喻尊龙
 * @date: 2018/3/19
 */
@Service("TrackLogService")
public class TrackLogServiceImpl implements TrackLogService {

    private Logger logger = LoggerFactory.getLogger(TrackLogServiceImpl.class);

    @Autowired
    @Qualifier("CollectionTrackLogService")
    CollectionTrackLogService collectionTrackLogService;

    @Override
    public PageResult<List<CollectionTrackLogVo>> selectCollectionTrackLogPage(CollectionTrckLogReq req) {
        try{
            Page<CollectionTrackLogVo> pages = collectionTrackLogService.selectCollectionTrackLogByRbpId(req);

            List<CollectionTrackLogVo> list = pages.getRecords();

            for(int i=1;i<list.size()+1;i++){
                list.get(i-1).setSortId(i);
            }

            return PageResult.success(pages.getRecords(),list.size());
        }catch (Exception ex){
            logger.error(ex.getMessage());
            return PageResult.error(500, "数据库访问异常");
        }
    }
}
