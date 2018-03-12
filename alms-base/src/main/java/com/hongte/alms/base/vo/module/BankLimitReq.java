package com.hongte.alms.base.vo.module;

import com.hongte.alms.common.vo.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;


/**
 * @author chenzs
 * @since 2018/3/10
 */
@ApiModel(value="查询银行额度表",description="查询银行额度表")
public class BankLimitReq extends PageRequest{
	
	
    public String getBankCode() {
		return bankCode;
	}
	public void setBankCode(String bankCode) {
		this.bankCode = bankCode;
	}
	public String getPlatformId() {
		return platformId;
	}
	public void setPlatformId(String platformId) {
		this.platformId = platformId;
	}
	@ApiModelProperty(value="银行编号",name="bankCode",example="test" ,dataType = "String")
    private String bankCode      	;     
    @ApiModelProperty(value="代扣Id",name="platformId",example="test" ,dataType = "String")
    private String platformId  			; 
  

 
}
