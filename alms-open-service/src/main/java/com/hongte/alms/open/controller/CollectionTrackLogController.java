package com.hongte.alms.open.controller;

import com.hongte.alms.base.collection.vo.CollectionTrackLogVo;
import com.hongte.alms.base.collection.vo.CollectionTrckLogReq;
import com.hongte.alms.base.service.TrackLogService;
import com.hongte.alms.common.vo.PageResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author:喻尊龙
 * @date: 2018/3/19
 */


@RestController
@RequestMapping(value = "/collection")
public class CollectionTrackLogController {

    @Autowired
    @Qualifier("TrackLogService")
    private TrackLogService trackLogService;

    @PostMapping("/selectCollectionTrackLogPage")
    @ResponseBody
    public PageResult<List<CollectionTrackLogVo>> selectCollectionTrackLogPage(@ModelAttribute CollectionTrckLogReq req){

        return  trackLogService.selectCollectionTrackLogPage(req);
    }
}
