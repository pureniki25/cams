package com.hongte.alms.base.vo.module;

import java.util.Date;

/**
 * @author chenzs
 * @since 2018/5/20 贷后清算详情
 */
public class LiquidationVO {
    private Integer rowNo;//序号
	private String liquidationOneId;// 清算一ID
	private String liquidationOneName;// 清算一名字
	private String liquidationTwoId;
	private String liquidationTwoName;
	private String describe;//分配备注
	private Integer setWay;//设置催收的方式：1，界面手动设置；2，定时器自动设置;3,信贷回调设置
	private String setWayStr;
	private Date setTime;//分配时间
	

	public Integer getRowNo() {
		return rowNo;
	}
	public void setRowNo(Integer rowNo) {
		this.rowNo = rowNo;
	}
	public String getSetWayStr() {
		return setWayStr;
	}
	public void setSetWayStr(String setWayStr) {
		this.setWayStr = setWayStr;
	}
	public String getLiquidationOneId() {
		return liquidationOneId;
	}
	public void setLiquidationOneId(String liquidationOneId) {
		this.liquidationOneId = liquidationOneId;
	}
	public String getLiquidationOneName() {
		return liquidationOneName;
	}
	public void setLiquidationOneName(String liquidationOneName) {
		this.liquidationOneName = liquidationOneName;
	}
	public String getLiquidationTwoId() {
		return liquidationTwoId;
	}
	public void setLiquidationTwoId(String liquidationTwoId) {
		this.liquidationTwoId = liquidationTwoId;
	}
	public String getLiquidationTwoName() {
		return liquidationTwoName;
	}
	public void setLiquidationTwoName(String liquidationTwoName) {
		this.liquidationTwoName = liquidationTwoName;
	}
	public String getDescribe() {
		return describe;
	}
	public void setDescribe(String describe) {
		
		this.describe = describe;
	}
	public Integer getSetWay() {
		
		return setWay;
	}
	public void setSetWay(Integer setWay) {
		if(setWay==1) {
			setSetWayStr("手动");
		}else {
			setSetWayStr("自动");
			setDescribe("自动");
		}
	
		this.setWay = setWay;
	}
	public Date getSetTime() {
		return setTime;
	}
	public void setSetTime(Date setTime) {
		this.setTime = setTime;
	}
	
 

   
}
