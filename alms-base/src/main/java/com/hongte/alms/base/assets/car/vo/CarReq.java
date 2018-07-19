package com.hongte.alms.base.assets.car.vo;

import com.hongte.alms.common.vo.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
/**
 * @since 2018/1/30
 * 车辆管理查询入参
 */
@ApiModel(value="车辆管理查询请求对象",description="车辆管理查询请求对象")
public class CarReq extends PageRequest {
    @ApiModelProperty(value="区域ID",name="areaId",example="test")
    private String areaId;  //区域ID

    @ApiModelProperty(value="分公司ID",name="companyId",example="test",dataType = "string")
    private String companyId; //分公司ID

    @ApiModelProperty(value="拖车日期  开始",name="showRepayDateBegin",dataType = "java.util.Date")
    private Date trailerStartDate; //拖车日期  开始
    @ApiModelProperty(value="拖车日期  开始",name="showRepayDateBegin",dataType = "java.util.Date")
    private Date trailerEndDate;  //拖车日期 结束


    @ApiModelProperty(value="业务编号",name="businessId",dataType = "String")
    private String  businessId;  //业务编号


    @ApiModelProperty(value="业务状态",name="businessStatus",dataType = "String")
    private String status;   //业务状态

    @ApiModelProperty(value="客户名称",name="customerName",dataType = "String")
    private String  customerName;  //客户名称


    @ApiModelProperty(value="车牌号",name="licensePlateNumber",dataType = "String")
    private String  licensePlateNumber;  
   
    @ApiModelProperty(value="车辆型号",name="model",dataType = "String")
    private String  model;

    private String userId;
    
    private Integer needPermission = 1;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getAreaId() {
		return areaId;
	}

	public void setAreaId(String areaId) {
		this.areaId = areaId;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public Date getTrailerStartDate() {
		return trailerStartDate;
	}

	public void setTrailerStartDate(Date trailerStartDate) {
		this.trailerStartDate = trailerStartDate;
	}

	public Date getTrailerEndDate() {
		return trailerEndDate;
	}

	public void setTrailerEndDate(Date trailerEndDate) {
		this.trailerEndDate = trailerEndDate;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}



	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getLicensePlateNumber() {
		return licensePlateNumber;
	}

	public void setLicensePlateNumber(String licensePlateNumber) {
		this.licensePlateNumber = licensePlateNumber;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public Integer getNeedPermission() {
		return needPermission;
	}

	public void setNeedPermission(Integer needPermission) {
		this.needPermission = needPermission;
	}
   
}
