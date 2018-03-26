package com.hongte.alms.base.assets.car.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecgframework.poi.excel.annotation.ExcelTarget;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.hongte.alms.base.entity.CarAuction;
import com.hongte.alms.base.entity.CarBasic;
import com.hongte.alms.base.entity.CarDetection;

import io.swagger.annotations.ApiModelProperty;


public class AuctionAplyVo implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private CarBasic carBase;
	private CarAuction carAuction;
	private CarDetection carDetection;
	private List<FileVo> files;
	private String submitType;
	private String processId;
	private  AuditVo auditVo;
	public CarBasic getCarBase() {
		return carBase;
	}
	public void setCarBase(CarBasic carBase) {
		this.carBase = carBase;
	}
	public CarAuction getCarAuction() {
		return carAuction;
	}
	public void setCarAuction(CarAuction carAuction) {
		this.carAuction = carAuction;
	}
	public List<FileVo> getFiles() {
		return files;
	}
	public void setFiles(List<FileVo> files) {
		this.files = files;
	}
	public String getSubmitType() {
		return submitType;
	}
	public void setSubmitType(String submitType) {
		this.submitType = submitType;
	}
	public String getProcessId() {
		return processId;
	}
	public void setProcessId(String processId) {
		this.processId = processId;
	}
	public AuditVo getAuditVo() {
		return auditVo;
	}
	public void setAuditVo(AuditVo auditVo) {
		this.auditVo = auditVo;
	}
	public CarDetection getCarDetection() {
		return carDetection;
	}
	public void setCarDetection(CarDetection carDetection) {
		this.carDetection = carDetection;
	}
   
}
