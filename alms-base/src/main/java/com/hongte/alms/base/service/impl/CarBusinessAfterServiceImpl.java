package com.hongte.alms.base.service.impl;

import com.hongte.alms.base.entity.CarBusinessAfter;
import com.hongte.alms.base.mapper.CarBusinessAfterMapper;
import com.hongte.alms.base.service.CarBusinessAfterService;
import com.hongte.alms.base.vo.module.CollectionReq;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 曾坤
 * @since 2018-04-10
 */
@Service("CarBusinessAfterService")
public class CarBusinessAfterServiceImpl extends BaseServiceImpl<CarBusinessAfterMapper, CarBusinessAfter> implements CarBusinessAfterService {

    @Autowired
    private CarBusinessAfterMapper  carBusinessAfterMapper;

    public  Integer queryNotTransferPhoneUserCount(){
        return carBusinessAfterMapper.queryNotTransferPhoneUserCount();
    }

    public List<CarBusinessAfter> queryNotTransferCollectionLog(CollectionReq req){
        return carBusinessAfterMapper.queryNotTransferCollectionLog(req);
    }

    public List<CarBusinessAfter> queryNotTransferCollectionLog(){
        return carBusinessAfterMapper.queryNotTransferCollectionLog();
    }
}
