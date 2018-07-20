package com.hongte.alms.base.vo.module;

import com.hongte.alms.common.vo.PageRequest;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel(value="展期信息列表页查询请求对象",description="展期信息列表页查询请求对象")
public class LoanExtListReq extends PageRequest{
	private String dateStart ;
	private String dateEnd ;
	private String deptId ;
	private String businessId ;
	private String salesman ;
	private String customer ;
	private String businessType ;
	private String extensionId ;
	private Integer needPermission = 1;
	private String userId;
}
