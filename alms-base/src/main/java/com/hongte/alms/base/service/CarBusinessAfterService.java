package com.hongte.alms.base.service;

import com.hongte.alms.base.entity.CarBusinessAfter;
import com.hongte.alms.base.vo.module.CollectionReq;
import com.hongte.alms.common.service.BaseService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 曾坤
 * @since 2018-04-10
 */
public interface CarBusinessAfterService extends BaseService<CarBusinessAfter> {
    Integer queryNotTransferPhoneUserCount();

    List<CarBusinessAfter> queryNotTransferCollectionLog(CollectionReq req);


    public List<CarBusinessAfter> queryNotTransferCollectionLog();
}
