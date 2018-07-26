package com.hongte.alms.base.RepayPlan.dto;

import com.hongte.alms.base.entity.RepaymentProjPlan;
import com.hongte.alms.base.entity.TuandaiProjectInfo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

/**
 * @author zengkun
 * @since 2018/5/5
 * 标的还款计划主表
 */
public class RepaymentProjPlanDto implements Serializable {

	protected RepaymentProjPlan repaymentProjPlan;


    protected List<RepaymentProjPlanListDto>  projPlanListDtos;






    /**
     * 标的占比
     */
	protected BigDecimal proportion = new BigDecimal("0") ;
    /**
     * 标的分配到的金额
     */
	protected BigDecimal divideAmount = new BigDecimal("0");
    
    /**
     * 标的分配到的线上滞纳金
     */
	protected BigDecimal onlineOverDue = new BigDecimal("0");
    
    /**
     * 标的分配到的线下滞纳金
     */
	protected BigDecimal offlineOverDue = new BigDecimal("0");
    
    /**
     * 标的信息
     */
	protected TuandaiProjectInfo tuandaiProjectInfo ;
    public RepaymentProjPlan getRepaymentProjPlan() {
        return repaymentProjPlan;
    }

    public void setRepaymentProjPlan(RepaymentProjPlan repaymentProjPlan) {
        this.repaymentProjPlan = repaymentProjPlan;
    }

    public List<RepaymentProjPlanListDto> getProjPlanListDtos() {
        return projPlanListDtos;
    }

    public void setProjPlanListDtos(List<RepaymentProjPlanListDto> projPlanListDtos) {
        this.projPlanListDtos = projPlanListDtos;
    }
    


	/**
	 * @return the proportion
	 */
	public BigDecimal getProportion() {
		return proportion;
	}

	/**
	 * @param proportion the proportion to set
	 */
	public void setProportion(BigDecimal proportion) {
		this.proportion = proportion;
	}

	/**
	 * @return the tuandaiProjectInfo
	 */
	public TuandaiProjectInfo getTuandaiProjectInfo() {
		return tuandaiProjectInfo;
	}

	/**
	 * @param tuandaiProjectInfo the tuandaiProjectInfo to set
	 */
	public void setTuandaiProjectInfo(TuandaiProjectInfo tuandaiProjectInfo) {
		this.tuandaiProjectInfo = tuandaiProjectInfo;
	}

	/**
	 * @return the offlineOverDue
	 */
	public BigDecimal getOfflineOverDue() {
		return offlineOverDue;
	}

	/**
	 * @param offlineOverDue the offlineOverDue to set
	 */
	public void setOfflineOverDue(BigDecimal offlineOverDue) {
		this.offlineOverDue = offlineOverDue;
	}

	/**
	 * @return the onlineOverDue
	 */
	public BigDecimal getOnlineOverDue() {
		return onlineOverDue;
	}

	/**
	 * @param onlineOverDue the onlineOverDue to set
	 */
	public void setOnlineOverDue(BigDecimal onlineOverDue) {
		this.onlineOverDue = onlineOverDue;
	}

	/**
	 * @return the divideAmount
	 */
	public BigDecimal getDivideAmount() {
		return divideAmount;
	}

	/**
	 * @param divideAmount the divideAmount to set
	 */
	public void setDivideAmount(BigDecimal divideAmount) {
		this.divideAmount = divideAmount;
	}



}
