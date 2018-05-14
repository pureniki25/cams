package com.hongte.alms.open.dto.repayPlan;

import java.util.List;

/**
 * @author zengkun
 * @since 2018/5/12
 * 信贷还款计划返回信息  每一个批次一条记录
 */
public class XdPlanDto {

    //批次UUid
    private  String batchUUid;

    //信贷还款计划列表
    private List<CarBusinessAfterDto> carBusinessAfterDtoList;

    public String getBatchUUid() {
        return batchUUid;
    }

    public void setBatchUUid(String batchUUid) {
        this.batchUUid = batchUUid;
    }

    public List<CarBusinessAfterDto> getCarBusinessAfterDtoList() {
        return carBusinessAfterDtoList;
    }

    public void setCarBusinessAfterDtoList(List<CarBusinessAfterDto> carBusinessAfterDtoList) {
        this.carBusinessAfterDtoList = carBusinessAfterDtoList;
    }
}
