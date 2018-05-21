package com.hongte.alms.finance.dto.repayPlan;

import com.hongte.alms.base.entity.RepaymentProjPlan;
import com.hongte.alms.base.entity.TuandaiProjectInfo;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author zengkun
 * @since 2018/5/5
 * 标的还款计划主表
 */
public class RepaymentProjPlanDto {

    private RepaymentProjPlan repaymentProjPlan;


    private List<RepaymentProjPlanListDto>  projPlanListDtos;

    /**
     * 标的占比
     */
    private BigDecimal proportion = new BigDecimal(0) ;
    /**
     * 标的分配到的金额
     */
    private BigDecimal distributiveMoney = new BigDecimal(0); 
    
    /**
     * 标的分配到的线上滞纳金
     */
    private BigDecimal onlineOverDue = new BigDecimal(0);
    /**
     * 标的分配到的线下滞纳金
     */
    private BigDecimal offlineOverDue = new BigDecimal(0);
    
    /**
     * 分配后剩余金额
     */
    private BigDecimal surplusMoney = new BigDecimal(0);

    /**
     * 标的信息
     */
    private TuandaiProjectInfo tuandaiProjectInfo ;
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
	 * @return the distributiveMoney
	 */
	public BigDecimal getDistributiveMoney() {
		return distributiveMoney;
	}

	/**
	 * @param distributiveMoney the distributiveMoney to set
	 */
	public void setDistributiveMoney(BigDecimal distributiveMoney) {
		this.distributiveMoney = distributiveMoney;
	}

	/**
	 * @return the surplusMoney
	 */
	public BigDecimal getSurplusMoney() {
		return surplusMoney;
	}

	/**
	 * @param surplusMoney the surplusMoney to set
	 */
	public void setSurplusMoney(BigDecimal surplusMoney) {
		this.surplusMoney = surplusMoney;
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


}
