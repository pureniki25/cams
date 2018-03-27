package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.assets.car.enums.CarStatusEnums;
import com.hongte.alms.base.entity.CarBasic;
import com.hongte.alms.base.entity.CarBasicStatusLog;
import com.hongte.alms.base.mapper.CarBasicMapper;
import com.hongte.alms.base.service.CarBasicService;
import com.hongte.alms.base.service.CarBasicStatusLogService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 车辆基本信息 服务实现类
 * </p>
 *
 * @author dengzhiming
 * @since 2018-02-24
 */
@Service("CarBasicService")
public class CarBasicServiceImpl extends BaseServiceImpl<CarBasicMapper, CarBasic> implements CarBasicService {

    @Autowired
    @Qualifier("CarBasicStatusLogService")
    CarBasicStatusLogService carBasicStatusLogService;

    @Override
    public boolean updateCarStatusToSettled(String businessId){

        CarBasic carBasic =  selectById(businessId);
        if(carBasic == null){
            return  true;
        }

        if(carBasic.getStatus().equals(CarStatusEnums.SETTLED.getStatusCode())){
            return  true;
        }

        CarBasicStatusLog log = new CarBasicStatusLog();
        log.setBusinessId(businessId);
        log.setCarBasicLogId(UUID.randomUUID().toString());
        log.setCreateTime(new Date());
        log.setCreateUser(Constant.ADMIN_ID);
        log.setOldStatus(carBasic.getStatus());
        log.setNewStatus(CarStatusEnums.SETTLED.getStatusCode());

        carBasicStatusLogService.insert(log);

        carBasic.setStatus(CarStatusEnums.SETTLED.getStatusCode());
        carBasic.setUpdateTime(new Date());
        carBasic.setUpdateUser(Constant.ADMIN_ID);

        updateById(carBasic);

        return true;
    }

    @Override
    public boolean revokeCarStatus(String businessId) {

        CarBasic carBasic =  selectById(businessId);
        if(carBasic == null){
            return  true;
        }

        if(!carBasic.getStatus().equals(CarStatusEnums.SETTLED.getStatusCode())){
            return  true;
        }


        List<CarBasicStatusLog>  oldLogs = carBasicStatusLogService.selectList(new EntityWrapper<CarBasicStatusLog>().
                eq("business_id",businessId)
                .eq("new_status",CarStatusEnums.SETTLED.getStatusCode())
                .orderBy("create_time desc"));
        CarBasicStatusLog  lodLog = null;
        if(oldLogs.size()==0){
            return true;
        }
        lodLog = oldLogs.get(0);

        CarBasicStatusLog log = new CarBasicStatusLog();
        log.setBusinessId(businessId);
        log.setCarBasicLogId(UUID.randomUUID().toString());
        log.setCreateTime(new Date());
        log.setCreateUser(Constant.ADMIN_ID);
        log.setOldStatus(carBasic.getStatus());
        log.setNewStatus(lodLog.getOldStatus());

        carBasicStatusLogService.insert(log);

        carBasic.setStatus(lodLog.getOldStatus());
        carBasic.setUpdateTime(new Date());
        carBasic.setUpdateUser(Constant.ADMIN_ID);

        updateById(carBasic);

        return true;
    }

}
