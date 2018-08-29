/**
 * 
 */
package com.hongte.alms.base.dto;

import javax.validation.constraints.NotNull;

import com.hongte.alms.base.entity.MoneyPool;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author 王继光
 * 2018年5月14日 下午9:15:26
 */
@Data
public class ConfirmRepaymentReq {
	//必填,业务ID
	@NotNull(message="业务id不能为空")
	private String businessId ;
	//必填,期数afterId
	@NotNull(message="还款计划期数afterId不能为空")
	private String afterId ;
	//线下转账选填项,线下逾期费
	private BigDecimal offlineOverDue ;
	//线下转账选填项,线上逾期费
	private BigDecimal onlineOverDue ;
	//线下转账选填项,结余金额
	private BigDecimal surplusFund ;
	//线下转账/代扣/充值 选填项,备注
	private String remark ;
	//线下转账必填项,匹配的流水ID
	private List<String> mprIds ;
	//TODO 线下代扣ids
	//代扣必填项, 银行代扣ids
	private List<Integer> logIds;
	//网关充值/快捷充值必填项,充值记录ids
	private List<String> rechargeIds;

	/**
	 * 手动选择的实还日期
	 */
	private String repayDate ;
	
	/*审批调用专用设置*/
	/**
	 * 客户还款登记流水-审批标志位
	 */
	private boolean shenpibiaozhi ;
	/**
	 * 审批流水
	 */
	private MoneyPool moneyPool ;
	/*审批调用专用设置*/
	/**
	 * 调用方标志位  10：财务人员还款确认（线下转账），20：自动线下代扣，21：人工线下代扣，30：自动银行代扣，31：人工银行代扣;40:PC网关充值;50:APP快捷充值;
	 */
	@NotNull(message = "调用方标志位(callFlage) 不能为空")
	private Integer callFlage;
}
