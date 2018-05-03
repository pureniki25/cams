package com.hongte.alms.base.collection.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.collection.entity.CollectionTrackLog;
import com.hongte.alms.base.collection.vo.CollectionTrackLogVo;
import com.hongte.alms.base.collection.vo.CollectionTrckLogReq;
import com.hongte.alms.common.service.BaseService;

import java.util.List;

/**
 * <p>
 * 贷后跟踪记录表 服务类
 * </p>
 *
 * @author 曾坤
 * @since 2018-02-01
 */
public interface CollectionTrackLogService extends BaseService<CollectionTrackLog> {

    /**
     * 根据还款计划ID查找还款跟进列表，分页
     * @param req
     * @return
     */
    Page<CollectionTrackLogVo>  selectCollectionTrackLogByRbpId(CollectionTrckLogReq req);

    /**
     * 根据还款计划编号 查询标的id
     * @param rbpId
     * @return
     */
    List<String> selectProjectIdByRbpId(String rbpId);
    
    void addOrUpdateLog(CollectionTrackLog log);
}
