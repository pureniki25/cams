package com.hongte.alms.base.vo.module;

import com.hongte.alms.common.vo.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author chenzs
 * @since 2018/3/28
 */
@ApiModel(value="流程步骤请求对象",description="流程步骤请求对象")
public class ProcessStepSearchReq extends PageRequest{
	
	
    @ApiModelProperty(value="流程类型ID",name="typeId",example="test" ,dataType = "String")
    private String typeId      	;     
    @ApiModelProperty(value="步骤名称",name="stepName",example="test" ,dataType = "String")
    private String stepName;  	
    
    private String typeStepId;
	public String getTypeStepId() {
		return typeStepId;
	}
	public void setTypeStepId(String typeStepId) {
		this.typeStepId = typeStepId;
	}
	public String getTypeId() {
		return typeId;
	}
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	public String getStepName() {
		return stepName;
	}
	public void setStepName(String stepName) {
		this.stepName = stepName;
	} 
 
    
}
