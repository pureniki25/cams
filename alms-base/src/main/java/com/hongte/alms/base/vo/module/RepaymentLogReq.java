package com.hongte.alms.base.vo.module;

import com.hongte.alms.common.vo.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author chenzs
 * @since 2018/3/08
 */
@ApiModel(value="还款计划日志查询请求对象",description="还款计划日志查询请求对象")
public class RepaymentLogReq extends PageRequest{
	  
	 
    @ApiModelProperty(value="关键字",name="keyName",example="test" ,dataType = "String")
    private String keyName      	;     //关键字
    @ApiModelProperty(value="平台ID",name="smsType",example="test" ,dataType = "String")
    private String platformId  			; //区域ID
    @ApiModelProperty(value="分公司ID",name="companyId",example="test" ,dataType = "String")
    private String companyId			; //分公司ID
    @ApiModelProperty(value="状态",name="repayStatus",example="test" ,dataType = "String")
    private String   repayStatus		; //状态
    @ApiModelProperty(value="代扣时间",name="dateBegin",example="test" ,dataType = "java.util.Date")
    private Date dateBegin	; 	//发送时间 开始

    
    @ApiModelProperty(value="业务类型",name="businessTypeId",example="test" ,dataType = "String")
    private String   businessTypeId; //状态
    
    
    @ApiModelProperty(value="用户ID",name="userId",example="test" ,dataType = "String")
    private String   userId; //用户Id
    
    private Integer needPermission = 1;
    
    
	public String getBusinessTypeId() {
		return businessTypeId;
	}


	public void setBusinessTypeId(String businessTypeId) {
		this.businessTypeId = businessTypeId;
	}


	public String getUserId() {
		return userId;
	}


	public void setUserId(String userId) {
		this.userId = userId;
	}


	public String getKeyName() {
		return keyName;
	}
	

	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}



	public String getPlatformId() {
		return platformId;
	}


	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}


	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getRepayStatus() {
		return repayStatus;
	}

	public void setRepayStatus(String repayStatus) {
		this.repayStatus = repayStatus;
	}

	public Date getDateBegin() {
		return dateBegin;
	}

	public void setDateBegin(Date dateBegin) {
		this.dateBegin = dateBegin;
	}

	public Date getDateEnd() {
		return dateEnd;
	}

	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}

	public Integer getNeedPermission() {
		return needPermission;
	}


	public void setNeedPermission(Integer needPermission) {
		this.needPermission = needPermission;
	}



	@ApiModelProperty(value="代扣时间",name="dateEnd",example="test" ,dataType = "java.util.Date")
    private Date    dateEnd		; //发送时间 结束

 
}
