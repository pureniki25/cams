package com.hongte.alms.base.vo.module;

import java.util.Date;

/**
 * @author chenzs
 * @since 2018/5/21 移交法务信息
 */
public class LitigationVO {
    private Integer rowNo;//序号
	private String operator;// 移交人
	private Date operatTime;// 移交时间
	private String pecuniaryCondition;//其他财务情况（分公司/区域贷后跟进结果）
	private String relatedPersonnel;//资产管理部经办人催收调查结果（分公司/区域贷后跟进结果）
	
	private String carCondition;//客户车辆目前情况
	private String almsOpinion;//贷后意见
	
	public String getCarCondition() {
		return carCondition;
	}
	public void setCarCondition(String carCondition) {
		this.carCondition = carCondition;
	}
	public String getAlmsOpinion() {
		return almsOpinion;
	}
	public void setAlmsOpinion(String almsOpinion) {
		this.almsOpinion = almsOpinion;
	}
	public Integer getRowNo() {
		return rowNo;
	}
	public void setRowNo(Integer rowNo) {
		this.rowNo = rowNo;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public Date getOperatTime() {
		return operatTime;
	}
	public void setOperatTime(Date operatTime) {
		this.operatTime = operatTime;
	}
	public String getPecuniaryCondition() {
		return pecuniaryCondition;
	}
	public void setPecuniaryCondition(String pecuniaryCondition) {
		this.pecuniaryCondition = pecuniaryCondition;
	}
	public String getRelatedPersonnel() {
		return relatedPersonnel;
	}
	public void setRelatedPersonnel(String relatedPersonnel) {
		this.relatedPersonnel = relatedPersonnel;
	}

	


   
}
