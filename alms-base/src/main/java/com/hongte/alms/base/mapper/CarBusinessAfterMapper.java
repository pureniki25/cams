package com.hongte.alms.base.mapper;

import com.hongte.alms.base.entity.CarBusinessAfter;
import com.hongte.alms.base.vo.module.CollectionReq;
import com.hongte.alms.common.mapper.SuperMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 曾坤
 * @since 2018-04-10
 */
public interface CarBusinessAfterMapper extends SuperMapper<CarBusinessAfter> {

    /**
     * 分页查找未同步的电催记录
     * @param req
     * @return
     */
    List<CarBusinessAfter> queryNotTransferCollectionLog(CollectionReq req);
    List<CarBusinessAfter> queryNotTransferCollectionLog();

    /**
     * 
     * @return
     */
    Integer queryNotTransferPhoneUserCount();
}
