package com.hongte.alms.base.feignClient.dto;

import java.io.Serializable;
import java.util.List;

/**
 * @author:陈泽圣
 * 你我金融  标的还款计划
 * @date: 2018/6/13
 */
public class NiWoProjPlanDto implements Serializable {
	 private static final long serialVersionUID = -4095682638492039883L;
     private String orderNo;//机构用户生成全局唯一的申请单号。最多32位
     private Integer orderStatus;//订单状态 0：进件中 1：通过 2：拒绝
     private Integer projectStatus;//标的状态，订单状态为通过才有信息，0：投资中 1：还款中 2：已结清 3：流标
     private String orderMsg;//订单状态描述，订单处于拒绝状态才有信息
     private Integer withdrawStatus;//0：未提现 1：提现中 2:提现成功 3：提现失败
	private String withdrawMsg;//格式：1524016406846 毫秒时间戳
     private long withdrawTime;//格式：1524016406846 毫秒时间戳
     private long withdrawSuccessTime;//1：代扣主卡，2：代扣附属卡 代扣主卡表示自动代扣默认使用的卡
     private String contractUrl;//合同地址，投资完成放款后该字段值，未满标前该字段值为空
     private List<NiWoProjPlanListDetailDto>  repaymentPlans;//还款计划Json数组，订单状态为还款中，已结清才会返回该内容，其它状态该数组为空
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getOrderMsg() {
		return orderMsg;
	}
	public void setOrderMsg(String orderMsg) {
		this.orderMsg = orderMsg;
	}

	public String getWithdrawMsg() {
		return withdrawMsg;
	}
	public void setWithdrawMsg(String withdrawMsg) {
		this.withdrawMsg = withdrawMsg;
	}
	public long getWithdrawTime() {
		return withdrawTime;
	}
	public void setWithdrawTime(long withdrawTime) {
		this.withdrawTime = withdrawTime;
	}
	public long getWithdrawSuccessTime() {
		return withdrawSuccessTime;
	}
	public void setWithdrawSuccessTime(long withdrawSuccessTime) {
		this.withdrawSuccessTime = withdrawSuccessTime;
	}
	public String getContractUrl() {
		return contractUrl;
	}
	public void setContractUrl(String contractUrl) {
		this.contractUrl = contractUrl;
	}

	public Integer getOrderStatus() {
		return orderStatus;
	}
	public void setOrderStatus(Integer orderStatus) {
		this.orderStatus = orderStatus;
	}
	public Integer getProjectStatus() {
		return projectStatus;
	}
	public void setProjectStatus(Integer projectStatus) {
		this.projectStatus = projectStatus;
	}
	public Integer getWithdrawStatus() {
		return withdrawStatus;
	}
	public void setWithdrawStatus(Integer withdrawStatus) {
		this.withdrawStatus = withdrawStatus;
	}
	
    public List<NiWoProjPlanListDetailDto> getRepaymentPlans() {
		return repaymentPlans;
	}
	public void setRepaymentPlan(List<NiWoProjPlanListDetailDto> repaymentPlans) {
		this.repaymentPlans = repaymentPlans;
	}


   
}
