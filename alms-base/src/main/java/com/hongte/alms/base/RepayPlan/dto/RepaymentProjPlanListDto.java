package com.hongte.alms.base.RepayPlan.dto;

import com.hongte.alms.base.entity.RepaymentProjPlanList;
import com.hongte.alms.base.entity.RepaymentProjPlanListDetail;

import java.io.Serializable;
import java.util.List;

/**
 * @author zengkun
 * @since 2018/5/5
 * 标的还款计划List表Dto
 */
public class RepaymentProjPlanListDto implements Serializable {

    private RepaymentProjPlanList repaymentProjPlanList;

    private List<RepaymentProjPlanListDetail> projPlanListDetails;
    
    private List<RepaymentProjPlanListDetailDto> repaymentProjPlanListDetailDtos ;

    public RepaymentProjPlanList getRepaymentProjPlanList() {
        return repaymentProjPlanList;
    }

    public void setRepaymentProjPlanList(RepaymentProjPlanList repaymentProjPlanList) {
        this.repaymentProjPlanList = repaymentProjPlanList;
    }

    public List<RepaymentProjPlanListDetail> getProjPlanListDetails() {
        return projPlanListDetails;
    }

    public void setProjPlanListDetails(List<RepaymentProjPlanListDetail> projPlanListDetails) {
        this.projPlanListDetails = projPlanListDetails;
    }

	/**
	 * @return the repaymentProjPlanListDetailDtos
	 */
	public List<RepaymentProjPlanListDetailDto> getRepaymentProjPlanListDetailDtos() {
		return repaymentProjPlanListDetailDtos;
	}

	/**
	 * @param repaymentProjPlanListDetailDtos the repaymentProjPlanListDetailDtos to set
	 */
	public void setRepaymentProjPlanListDetailDtos(List<RepaymentProjPlanListDetailDto> repaymentProjPlanListDetailDtos) {
		this.repaymentProjPlanListDetailDtos = repaymentProjPlanListDetailDtos;
	}
}
