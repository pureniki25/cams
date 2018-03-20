package com.hongte.alms.base.vo.module;

import com.hongte.alms.common.vo.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author chenzs
 * @since 2018/3/20
 */
@ApiModel(value="流程类型请求对象",description="流程类型请求对象")
public class ProcessTypeReq extends PageRequest{
	
	
    @ApiModelProperty(value="流程类型编码",name="typeCode",example="test" ,dataType = "String")
    private String typeCode      	;     //流程类型编码
    @ApiModelProperty(value="流程类型名称",name="typeName",example="test" ,dataType = "String")
    private String typeName  			; //流程类型名称
	public String getTypeCode() {
		return typeCode;
	}
	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}
	public String getTypeName() {
		return typeName;
	}
	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
    

 
}
