package com.hongte.alms.base.RepayPlan.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author zengkun
 * @since 2018/5/12
 * 信贷还款计划返回信息  每一个批次一条记录
 */
public class XdPlanDto  implements Serializable {

    //批次UUid
    private  String batchUUid;

    private  List<String> projectIds;

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

    public List<String> getProjectIds() {
        return projectIds;
    }

    public void setProjectIds(List<String> projectIds) {
        this.projectIds = projectIds;
    }
}
