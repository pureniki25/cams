package com.hongte.alms.scheduled.service;

/**
 * @Author: 黄咏康
 * @Date: 2018/1/27 0027 下午 1:24
 */
public interface IXDSyncDataService {
    /**
     * 同步信贷业务基础数据
     * @return 1:同步成功，0：同步失败
     */
    public Integer syncBaseBusinessData();
}
