package com.hongte.alms.base.vo.module;

import com.hongte.alms.common.vo.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author chenzs
 * @since 2018/3/02
 */
@ApiModel(value="短信查询请求对象",description="短信查询请求对象")
public class InfoSmsListSearchReq extends PageRequest{
	
	
    @ApiModelProperty(value="关键字",name="keyName",example="test" ,dataType = "String")
    private String keyName      	;     //关键字
    @ApiModelProperty(value="短信类型",name="smsType",example="test" ,dataType = "String")
    private String smsType  			; //区域ID
    @ApiModelProperty(value="分公司ID",name="companyId",example="test" ,dataType = "String")
    private String companyId			; //分公司ID
    @ApiModelProperty(value="状态",name="status",example="test" ,dataType = "String")
    private String   status		; //状态
    @ApiModelProperty(value="发送时间",name="sendDateBegin",example="test" ,dataType = "java.util.Date")
    private Date sendDateBegin	; 	//发送时间 开始
    public String getKeyName() {
		return keyName;
	}
	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}
	public String getSmsType() {
		return smsType;
	}
	public void setSmsType(String smsType) {
		this.smsType = smsType;
	}
	public String getCompanyId() {
		return companyId;
	}
	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Date getSendDateBegin() {
		return sendDateBegin;
	}
	public void setSendDateBegin(Date sendDateBegin) {
		this.sendDateBegin = sendDateBegin;
	}
	public Date getSendDateEnd() {
		return sendDateEnd;
	}
	public void setSendDateEnd(Date sendDateEnd) {
		this.sendDateEnd = sendDateEnd;
	}
	@ApiModelProperty(value="发送时间",name="sendDateEnd",example="test" ,dataType = "java.util.Date")
    private Date    sendDateEnd		; //发送时间 结束

 
}
