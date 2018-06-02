package com.hongte.alms.base.RepayPlan.dto.app;

import java.io.Serializable;
import java.util.List;

/**
 * 业务信息
 * @author zengkun
 * @since 2018/5/23
 */
public class BizDto  implements Serializable {
    private String  businessId; //业务编号

    private String orgBusinessId; //原始业务编号

    private Integer businessType ;//业务类型
    private Boolean hasDeffer; //是否已经展期

    private Boolean isOver;//是否已经结清

//    private  String tip; //已展期/已结清（待还款列表为空时显示）


    private List<BizPlanDto> planDtoList;//还款计划列表

    private List<BizDto> renewBizs;//已申请展期的展期信息列表

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getOrgBusinessId() {
        return orgBusinessId;
    }

    public void setOrgBusinessId(String orgBusinessId) {
        this.orgBusinessId = orgBusinessId;
    }

    public Boolean getHasDeffer() {
        return hasDeffer;
    }

    public void setHasDeffer(Boolean hasDeffer) {
        this.hasDeffer = hasDeffer;
    }

    public Boolean getOver() {
        return isOver;
    }

    public void setOver(Boolean over) {
        isOver = over;
    }

//    public String getTip() {
//        return tip;
//    }
//
//    public void setTip(String tip) {
//        this.tip = tip;
//    }

    public List<BizPlanDto> getPlanDtoList() {
        return planDtoList;
    }

    public void setPlanDtoList(List<BizPlanDto> planDtoList) {
        this.planDtoList = planDtoList;
    }

    public List<BizDto> getRenewBizs() {
        return renewBizs;
    }

    public void setRenewBizs(List<BizDto> renewBizs) {
        this.renewBizs = renewBizs;
    }

	/**
	 * @return the businessType
	 */
	public Integer getBusinessType() {
		return businessType;
	}

	/**
	 * @param businessType the businessType to set
	 */
	public void setBusinessType(Integer businessType) {
		this.businessType = businessType;
	}
}
