/**
 * 
 */
package com.hongte.alms.base.dto;

import java.util.Date;

import lombok.Data;

/**
 * 实还记录查询req
 * @author 王继光
 * 2018年10月8日 上午10:15:25
 */
@Data
public class FactRepayReq {
	/**
	 * 关键字,业务编号或客户名称,模糊查询
	 */
	private String key ;
	/**
	 * 还款方式
	 */
	private String repayType ;
	/**
	 * 确认时间开始
	 */
	private String confirmStart ;
	/**
	 * 确认时间结束
	 */
	private String confirmEnd ;
	/**
	 * 分公司
	 */
	private String companyId ;
	/**
	 * 业务类型
	 */
	private String businessType ;
	/**
	 * 所属投资端
	 */
	private String srcType ;
	
	/**
	 * 当前页=0时,查全部数据
	 */
	private Integer curPage ;
	
	private Integer pageSize ;
}
