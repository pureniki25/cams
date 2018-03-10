package com.hongte.alms.scheduled.service.impl;

import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.mapper.BasicBusinessMapper;

import com.hongte.alms.scheduled.service.IXDSyncDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: 黄咏康
 * @Date: 2018/1/27 0027 下午 1:31
 */
@Service
public class XDSyncDataServiceImp implements IXDSyncDataService {

    @Autowired
    private BasicBusinessMapper basicBusinessMapper;

    @Override
    public Integer syncBaseBusinessData() {
        BasicBusiness checkObj = basicBusinessMapper.selectById("11");
        return 1;
    }
}
