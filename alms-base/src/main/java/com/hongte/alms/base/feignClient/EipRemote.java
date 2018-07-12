package com.hongte.alms.base.feignClient;

import java.util.Map;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.hongte.alms.base.dto.RechargeModalDTO;
import com.hongte.alms.base.dto.compliance.DistributeFundDTO;
import com.hongte.alms.base.dto.compliance.TdAdvanceShareProfitDTO;
import com.hongte.alms.base.dto.compliance.TdDepaymentEarlierDTO;
import com.hongte.alms.base.feignClient.dto.AddProjectTrackReqDto;
import com.hongte.alms.base.feignClient.dto.BankRechargeReqDto;
import com.hongte.alms.base.feignClient.dto.BaofuRechargeReqDto;
import com.hongte.alms.base.feignClient.dto.KuaiQianRechargeReqDto;
import com.hongte.alms.base.feignClient.dto.YiBaoRechargeReqDto;
import com.hongte.alms.base.vo.comm.SmsVo;
import com.ht.ussp.core.Result;

/**
 * @author:喻尊龙
 * @date: 2018/3/12
 */
@FeignClient(value = "eip-out")
public interface EipRemote {

	@RequestMapping(value = "/eip/xiaodai/AddProjectTrack", headers = { "app=ALMS",
			"content-type=application/json" }, method = RequestMethod.POST)
	Result addProjectTrack(@RequestBody AddProjectTrackReqDto dto);

	/**
	 * 调用外联平台发送短信
	 * 
	 * @param emailVo
	 * @return
	 */
	@RequestMapping(value = "/eip/common/sendSms", headers = { "app=ALMS",
			"content-type=application/json" }, method = RequestMethod.POST)
	public Result sendSms(@RequestBody SmsVo smsVo);

	/**
	 * 查询代充值账户余额
	 * 
	 * @param paramMap
	 * @return
	 */
	@RequestMapping(value = "/eip/xiaodai/QueryUserAviMoney", headers = { "app=ALMS",
			"content-type=application/json" }, method = RequestMethod.POST)
	public Result queryUserAviMoney(@RequestBody Map<String, Object> paramMap);

	/**
	 * 代充值
	 * 
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/eip/td/assetside/agencyRecharge", headers = { "app=ALMS",
			"content-type=application/json" }, method = RequestMethod.POST)
	public Result agencyRecharge(@RequestBody RechargeModalDTO dto);

	/**
	 * 查询充值订单
	 * 
	 * @param paramMap
	 * @return
	 */
	@RequestMapping(value = "/eip/td/assetside/queryRechargeOrder", headers = { "app=ALMS",
			"content-type=application/json" }, method = RequestMethod.POST)
	public Result queryRechargeOrder(@RequestBody Map<String, Object> paramMap);

	/**
	 * 银行代扣
	 */
	@RequestMapping(value = "/eip/td/assetside/autoRecharge", headers = { "app=ALMS",
			"content-type=application/json" }, method = RequestMethod.POST)
	Result bankRecharge(@RequestBody BankRechargeReqDto dto);

	/**
	 * 宝付代扣
	 */

	@RequestMapping(value = "/eip/baofu/backTransRequest", headers = { "app=ALMS",
			"content-type=application/json" }, method = RequestMethod.POST)
	Result baofuRecharge(@RequestBody BaofuRechargeReqDto dto);

	/**
	 * 宝付查询充值订单
	 */

	@RequestMapping(value = "/eip/baofu/queryStatus", headers = { "app=ALMS",
			"content-type=application/json" }, method = RequestMethod.POST)
	Result queryBaofuStatus(@RequestBody Map<String, Object> paramMap);

	/**
	 * 查询项目正常还款明细和提前结清待还本息
	 * 
	 * @param paramMap
	 * @return
	 */
	@RequestMapping(value = "/eip/td/queryProjectRepayment", headers = { "app=ALMS",
			"content-type=application/json" }, method = RequestMethod.POST)
	public Result queryProjectRepayment(@RequestBody Map<String, Object> paramMap);

	/**
	 * 查询项目垫付记录
	 * 
	 * @param paramMap
	 * @return
	 */
	@RequestMapping(value = "/eip/td/repayment/returnAdvanceShareProfit", headers = { "app=ALMS",
			"content-type=application/json" }, method = RequestMethod.POST)
	public Result advanceShareProfit(@RequestBody Map<String, Object> paramMap);

	/**
	 * 标的还款信息查询接口
	 * 
	 * @param paramMap
	 * @return
	 */
	@RequestMapping(value = "/eip/td/assetside/getProjectPayment", headers = { "app=ALMS",
			"content-type=application/json" }, method = RequestMethod.POST)
	public Result getProjectPayment(@RequestBody Map<String, Object> paramMap);

	/**
	 * 易宝代扣
	 */

	@RequestMapping(value = "/eip/yepay/tzt/directBindPay", headers = { "app=ALMS",
			"content-type=application/json" }, method = RequestMethod.POST)
	Result directBindPay(@RequestBody YiBaoRechargeReqDto dto);

	/**
	 * 易宝绑卡
	 */

	@RequestMapping(value = "/eip/yepay/tzt/invokeBindBankCard", headers = { "app=ALMS",
			"content-type=application/json" }, method = RequestMethod.POST)
	Result invokeBindBankCard(@RequestBody Map<String, Object> paramMap);

	/**
	 * 易宝确定绑卡
	 */

	@RequestMapping(value = "/eip/yepay/tzt/confirmBindBankCard", headers = { "app=ALMS",
			"content-type=application/json" }, method = RequestMethod.POST)
	Result confirmBindBankCard(@RequestBody Map<String, Object> paramMap);

	/**
	 * 易宝支付结果查询
	 */

	@RequestMapping(value = "/eip/yepay/query/order", headers = { "app=ALMS",
			"content-type=application/json" }, method = RequestMethod.POST)
	Result queryOrder(@RequestBody Map<String, Object> paramMap);

	/**
	 * 易宝绑卡列表查询
	 */

	@RequestMapping(value = "/eip/yepay/authBind", headers = { "app=ALMS",
			"content-type=application/json" }, method = RequestMethod.POST)
	Result authBind(@RequestBody Map<String, Object> paramMap);

	/**
	 * 资金分发接口
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/eip/td/assetside/userDistributeFund", headers = { "app=ALMS",
			"content-type=application/json" }, method = RequestMethod.POST)
	Result userDistributeFund(@RequestBody DistributeFundDTO dto);
	
	/**
	 * 还垫付信息查询接口
	 * @param paramMap
	 * @return
	 */
	@RequestMapping(value = "/eip/td/repayment/returnAdvanceShareProfit", headers = { "app=ALMS",
	"content-type=application/json" }, method = RequestMethod.POST)
	Result returnAdvanceShareProfit(@RequestBody Map<String, Object> paramMap);
	
	/**
	 * 标的还款信息查询接口
	 * @param paramMap
	 * @return
	 */
	@RequestMapping(value = "/eip/td/repayment/queryProjectPayment", headers = { "app=ALMS",
	"content-type=application/json" }, method = RequestMethod.POST)
	Result queryProjectPayment(@RequestBody Map<String, Object> paramMap);
	
	/**
	 * 偿还垫付
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/eip/td/repayment/advanceShareProfit", headers = { "app=ALMS",
	"content-type=application/json" }, method = RequestMethod.POST)
	Result advanceShareProfit(@RequestBody TdAdvanceShareProfitDTO dto);
	
	
	/**
	 * 你我金融      7.10.2订单查询
	 * @param orderNo  标ID
	 * @return
	 */
	@RequestMapping(value = "/eip//niiwoo/org/queryApplyOrder", headers = { "app=ALMS",
	"content-type=application/json" }, method = RequestMethod.POST)
	Result queryApplyOrder(@RequestBody Map<String, Object> paramMap);

	/**
	 * 提前结清
	 * @param dto
	 * @return
	 */
	@RequestMapping(value = "/eip/td/repayment/repaymentEarlier", headers = { "app=ALMS",
	"content-type=application/json" }, method = RequestMethod.POST)
	Result repaymentEarlier(@RequestBody TdDepaymentEarlierDTO dto);
	
	/**
	 * 资金分发订单查询
	 * @param paramMap
	 * @return
	 */
	@RequestMapping(value = "/eip/td/assetside/queryDistributeFund", headers = { "app=ALMS",
	"content-type=application/json" }, method = RequestMethod.POST)
	Result queryDistributeFund(@RequestBody Map<String, Object> paramMap);
	
	/**
	 * 提前结清平台费用查询
	 * @param paramMap
	 * @return
	 */
	@RequestMapping(value = "/eip/td/repayment/queryRepaymentEarlier", headers = { "app=ALMS",
	"content-type=application/json" }, method = RequestMethod.POST)
	Result queryRepaymentEarlier(@RequestBody Map<String, Object> paramMap);
	
	
	
	
	/**
	 * 快钱签约申请
	 * @param paramMap
	 * @return
	 */
	@RequestMapping(value = "/eip/kq/signApply", headers = { "app=ALMS",
	"content-type=application/json" }, method = RequestMethod.POST)
	Result signApply(@RequestBody Map<String, Object> paramMap);
	
	
	
	
	
	/**
	 * 快钱签约申请确认
	 * @param paramMap
	 * @return
	 */
	@RequestMapping(value = "/eip/kq/signApplyVerify", headers = { "app=ALMS",
	"content-type=application/json" }, method = RequestMethod.POST)
	Result signApplyVerify(@RequestBody Map<String, Object> paramMap);
	
	
	
	
	/**
	 * 快钱支付
	 * @param paramMap
	 * @return
	 */
	@RequestMapping(value = "/eip/kq/pay", headers = { "app=ALMS",
	"content-type=application/json" }, method = RequestMethod.POST)
	Result pay(@RequestBody  KuaiQianRechargeReqDto kuaiQianRechargeReqDto);
	
	

}
