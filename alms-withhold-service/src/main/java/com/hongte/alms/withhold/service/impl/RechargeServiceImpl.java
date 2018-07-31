package com.hongte.alms.withhold.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.AgencyRechargeLog;
import com.hongte.alms.base.entity.ApplyDerateProcess;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.BizOutputRecord;
import com.hongte.alms.base.entity.MoneyPoolRepayment;
import com.hongte.alms.base.entity.RepaymentBizPlan;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentBizPlanListDetail;
import com.hongte.alms.base.entity.RepaymentConfirmLog;
import com.hongte.alms.base.entity.RepaymentProjPlanList;
import com.hongte.alms.base.entity.SysExceptionLog;
import com.hongte.alms.base.entity.SysParameter;
import com.hongte.alms.base.entity.TuandaiProjectInfo;
import com.hongte.alms.base.entity.WithholdingChannel;
import com.hongte.alms.base.entity.WithholdingRepaymentLog;
import com.hongte.alms.base.enums.BusinessTypeEnum;
import com.hongte.alms.base.enums.PlatformEnum;
import com.hongte.alms.base.enums.SysParameterEnums;
import com.hongte.alms.base.enums.repayPlan.RepayPlanFeeTypeEnum;
import com.hongte.alms.base.enums.repayPlan.RepayResultCodeEnum;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.feignClient.CustomerInfoXindaiRemoteApi;
import com.hongte.alms.base.feignClient.EipRemote;
import com.hongte.alms.base.feignClient.PlatformRepaymentFeignClient;
import com.hongte.alms.base.feignClient.dto.BankCardInfo;
import com.hongte.alms.base.feignClient.dto.BankRechargeReqDto;
import com.hongte.alms.base.feignClient.dto.BaofuRechargeReqDto;
import com.hongte.alms.base.feignClient.dto.CustomerInfoDto;
import com.hongte.alms.base.feignClient.dto.SignedProtocol;
import com.hongte.alms.base.feignClient.dto.ThirdPlatform;
import com.hongte.alms.base.feignClient.dto.YiBaoRechargeReqDto;
import com.hongte.alms.base.process.service.ProcessService;
import com.hongte.alms.base.service.ApplyDerateProcessService;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.base.service.BizOutputRecordService;
import com.hongte.alms.base.service.MoneyPoolRepaymentService;
import com.hongte.alms.base.service.RepaymentBizPlanListDetailService;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.base.service.RepaymentBizPlanService;
import com.hongte.alms.base.service.RepaymentConfirmLogService;
import com.hongte.alms.base.service.RepaymentProjPlanListService;
import com.hongte.alms.base.service.SendMessageService;
import com.hongte.alms.base.service.SysExceptionLogService;
import com.hongte.alms.base.service.SysParameterService;
import com.hongte.alms.base.service.TuandaiProjectInfoService;
import com.hongte.alms.base.service.WithholdingChannelService;
import com.hongte.alms.base.service.WithholdingRepaymentLogService;
import com.hongte.alms.base.vo.withhold.ResultData;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.CommonUtil;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.MerchOrderUtil;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.withhold.feignClient.EipOutRechargeRemote;
import com.hongte.alms.withhold.feignClient.FinanceClient;
import com.hongte.alms.withhold.service.RechargeService;
import com.hongte.alms.withhold.service.RedisService;
import com.ht.ussp.bean.LoginUserInfoHelper;
import com.hongte.alms.base.process.entity.Process;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("RechargeService")
public class RechargeServiceImpl implements RechargeService {
	private static Logger logger = LoggerFactory.getLogger(RechargeServiceImpl.class);

	
	
	@Autowired
	@Qualifier("MoneyPoolRepaymentService")
	MoneyPoolRepaymentService moneyPoolRepaymentService;
	@Autowired
	@Qualifier("RepaymentBizPlanListService")
	RepaymentBizPlanListService repaymentBizPlanListService;

	@Autowired
	@Qualifier("RepaymentBizPlanListDetailService")
	RepaymentBizPlanListDetailService repaymentBizPlanListDetailService;

	@Autowired
	@Qualifier("BasicBusinessService")
	BasicBusinessService basicBusinessService;
	
	@Autowired
	@Qualifier("RepaymentProjPlanListService")
	RepaymentProjPlanListService repaymentProjPlanListService;

	@Autowired
	@Qualifier("WithholdingRepaymentLogService")
	WithholdingRepaymentLogService withholdingRepaymentLogService;
	
	@Autowired
	@Qualifier("ApplyDerateProcessService")
	ApplyDerateProcessService applyDerateProcessService;

	@Autowired
	@Qualifier("WithholdingChannelService")
	WithholdingChannelService withholdingChannelService;

	@Autowired
	@Qualifier("BizOutputRecordService")
	BizOutputRecordService bizOutputRecordService;
	
	@Autowired
	@Qualifier("ProcessService")
	ProcessService processService;
	
	@Autowired
	@Qualifier("RepaymentConfirmLogService")
	RepaymentConfirmLogService repaymentConfirmLogService;
	
	@Autowired
	@Qualifier("SysExceptionLogService")
	SysExceptionLogService sysExceptionLogService;
	
    @Autowired
    CustomerInfoXindaiRemoteApi customerInfoXindaiRemoteApi;
    
	@Autowired
	EipRemote eipRemote;

	@Autowired
	FinanceClient financeClient;

	@Autowired
	private RedisService redisService;

	@Value("${tuandai_pay_cm_orderno}")
	private String oidPartner;

	@Value("${tuandai_org_username}")
	private String orgUserName;



	@Value("${yibao.merchantaccount}")
	private String merchantaccount;

	@Autowired
	@Qualifier("SysParameterService")
	SysParameterService sysParameterService;

	@Autowired
	LoginUserInfoHelper loginUserInfoHelper;
	
	@Autowired
	@Qualifier("SendMessageService")
	SendMessageService sendMessageService;
	
	@Autowired
	@Qualifier("TuandaiProjectInfoService")
	TuandaiProjectInfoService tuandaiProjectInfoService;
	
	@Autowired
	@Qualifier("RepaymentBizPlanService")
	RepaymentBizPlanService repaymentBizPlanService;
	
	
	@Autowired
	PlatformRepaymentFeignClient platformRepaymentFeignClient;
	
	
	
	
	

	@Override
	public Result recharge(BasicBusiness business, RepaymentBizPlanList pList, Double amount, Integer boolLastRepay,
			Integer boolPartRepay, BankCardInfo bankCardInfo, WithholdingChannel channel,String appType) {

		Result result = new Result();
		// 获取商户订单号
		String merchOrderId = getMerchOrderId();
		Integer failCount = 0;
		// 执行之前先检查一下同一个渠道，当前的失败或者执行中的日志有没超过对应渠道的失败次数，超过则不执行
		// 同一个渠道，同一天，最多失败或者执行中运行2次
		List<WithholdingRepaymentLog> logs = withholdingRepaymentLogService
				.selectRepaymentLogForAutoRepay(business.getBusinessId(), pList.getAfterId(), channel.getPlatformId());
		failCount = logs.size();
		Integer maxFailCount = channel.getFailTimes();

		
		
		
		
		
		//获取还款计划的借款金额
		RepaymentBizPlan plan=repaymentBizPlanService.selectOne(new EntityWrapper<RepaymentBizPlan>().eq("plan_id", pList.getPlanId()));

		if (failCount >= maxFailCount) {
			result.setData(null);
			result.setCode("-1");
			result.setMsg("当前失败或者执行中次数为:" + failCount + ",超过限制次数，不允许执行。");
			RecordExceptionLog(pList.getOrigBusinessId(), pList.getAfterId(), result.getMsg());
		} else {
			//****************************************************************易宝代扣开始*****************************************************************************//
			if (channel.getPlatformId() == PlatformEnum.YB_FORM.getValue()) {
			
				YiBaoRechargeReqDto dto = new YiBaoRechargeReqDto();
				dto.setMerchantaccount(merchantaccount);
				dto.setOrderid(merchOrderId);
				dto.setTranstime((int) (System.currentTimeMillis()));
				dto.setAmount((int)(amount * 100));// 易宝代扣要转换单位:分
				dto.setProductname("");
				dto.setIdentityid(bankCardInfo.getIdentityNo());
				dto.setIdentitytype("5");
				dto.setCard_top(bankCardInfo.getBankCardNumber().substring(0, 6));
				dto.setCard_last(bankCardInfo.getBankCardNumber().substring(
						bankCardInfo.getBankCardNumber().length() - 4, bankCardInfo.getBankCardNumber().length()));
				dto.setCallbackurl("http://alms.hongte.info");
				dto.setUserip("127.0.0.1");
				dto.setProductname(merchOrderId);
				
				/*
				 * 调用接口前线插入记录 status 代扣状态(1:成功,0:失败;2:处理中)
				 */
				Integer status = 2;
				WithholdingRepaymentLog log = recordRepaymentLog("", status, pList, business, bankCardInfo,
						channel.getPlatformId(), boolLastRepay, boolPartRepay, merchOrderId,merchantaccount, 0,
						BigDecimal.valueOf(amount),appType);
				com.ht.ussp.core.Result remoteResult=null;
			
				try {
					
					logger.info("========调用外联易宝代扣开始========");
					remoteResult = eipRemote.directBindPay(dto);
					logger.info("========调用外联易宝代扣结束,结果为："+remoteResult.toString()+"====================================");
				} catch (Exception e) {
					// 调接口异常也默认是处理中
					log.setRepayStatus(2);
					log.setRemark("调用接口异常");
					withholdingRepaymentLogService.updateById(log);
				}
				ResultData	 resultData = getYBResultMsg(remoteResult);
				 //*****************挡板测试代码开始************************//
				SysParameter  thirtyRepayTestResult = sysParameterService.selectOne(
							new EntityWrapper<SysParameter>().eq("param_type", "thirtyRepayTest")
									.eq("status", 1).orderBy("param_value"));
				if(thirtyRepayTestResult!=null) {
					if(thirtyRepayTestResult.getParamValue().equals("0000")) {
						resultData.setResultMsg("交易成功");
						remoteResult.setReturnCode("0000");
					}else if(thirtyRepayTestResult.getParamValue().equals("1111")){
						resultData.setResultMsg("银行卡余额不足");
						remoteResult.setReturnCode("1111");
					}else if(thirtyRepayTestResult.getParamValue().equals("2222")){
						resultData.setResultMsg("处理中");
						remoteResult.setReturnCode("EIP_TD_HANDLER_EXECEPTION");
					}else {
						resultData.setResultMsg("代扣失败");
						remoteResult.setReturnCode("9999");
					}
				}
				 //*****************挡板测试代码结束************************//
				if (remoteResult.getReturnCode().equals("0000") && resultData.getResultMsg().equals("交易成功")) {
					result.setCode("1");
					result.setMsg(resultData.getResultMsg());
					log.setRepayStatus(1);
					log.setRemark(resultData.getResultMsg());
					log.setUpdateTime(new Date());
					withholdingRepaymentLogService.updateById(log);
			
		
				} else if (resultData.getResultMsg().contains("系统异常")) {
					result.setCode("2");
					result.setMsg(resultData.getResultMsg());
					log.setRepayStatus(2);
					log.setRemark(resultData.getResultMsg());
					log.setUpdateTime(new Date());
					withholdingRepaymentLogService.updateById(log);

				} else if (!remoteResult.getReturnCode().equals("0000")) {
					result.setCode("-1");
					result.setMsg(resultData.getResultMsg());
					log.setRepayStatus(0);
					log.setRemark(resultData.getResultMsg());
					log.setUpdateTime(new Date());
					withholdingRepaymentLogService.updateById(log);
					
				}
				
				try {
					Thread.sleep(5000);
					getYBResult(log);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			
			//*********************************************************************宝付代扣开始******************************************************************//
			if (channel.getPlatformId() == PlatformEnum.BF_FORM.getValue()) {
			
				BaofuRechargeReqDto dto = new BaofuRechargeReqDto();
				dto.setBizType("0000");
				dto.setPayCode(bankCardInfo.getBankCode().trim());
				dto.setPayCm("2");
				dto.setAccNo(bankCardInfo.getBankCardNumber());
				dto.setIdCardType("01");
				dto.setIdCard(bankCardInfo.getIdentityNo());
				dto.setIdHolder(bankCardInfo.getBankCardName());
				dto.setMobile(bankCardInfo.getMobilePhone());
				dto.setTransId(merchOrderId);
				dto.setTxnAmt((int) ((amount*100)));// 宝付代扣要转换单位:分
				dto.setTransSerialNo(merchOrderId);
				dto.setTradeDate(
						String.valueOf(Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()))));
				/*
				 * 调用接口前线插入记录 status 代扣状态(1:成功,0:失败;2:处理中)
				 */
				Integer status = 2;
				WithholdingRepaymentLog log = recordRepaymentLog("", status, pList, business, bankCardInfo,
						channel.getPlatformId(), boolLastRepay, boolPartRepay, merchOrderId,"", 0,
						BigDecimal.valueOf(amount),appType);
				com.ht.ussp.core.Result remoteResult = null;
				try {
					logger.info("========调用外联平台宝付代扣开始========");
					remoteResult = eipRemote.baofuRecharge(dto);
					logger.info("========调用外联平台宝付代扣结束,结果为："+remoteResult.toString()+"====================================");
				} catch (Exception e) {
					// 调接口异常也默认是处理中
					log.setRepayStatus(2);
					log.setRemark("调用接口异常");
					withholdingRepaymentLogService.updateById(log);
				}

				ResultData resultData = getBFResultMsg(remoteResult);
				 //*****************挡板测试代码开始************************//
				SysParameter  thirtyRepayTestResult = sysParameterService.selectOne(
							new EntityWrapper<SysParameter>().eq("param_type", "thirtyRepayTest")
									.eq("status", 1).orderBy("param_value"));
				if(thirtyRepayTestResult!=null) {
					if(thirtyRepayTestResult.getParamValue().equals("0000")) {
						resultData.setResultMsg("充值成功");
						remoteResult.setReturnCode("0000");
					}else if(thirtyRepayTestResult.getParamValue().equals("1111")){
						resultData.setResultMsg("银行卡余额不足");
						remoteResult.setReturnCode("1111");
					}else if(thirtyRepayTestResult.getParamValue().equals("2222")){
						resultData.setResultMsg("处理中");
						remoteResult.setReturnCode("EIP_TD_HANDLER_EXECEPTION");
					}else {
						resultData.setResultMsg("代扣失败");
						remoteResult.setReturnCode("9999");
					}
				}
				 //*****************挡板测试代码结束************************//
				
				
				if ((remoteResult.getReturnCode().equals(RepayResultCodeEnum.BF0000.getValue())||remoteResult.getReturnCode().equals(RepayResultCodeEnum.BF00114.getValue()))) {
					result.setCode("1");
					result.setMsg(resultData.getResultMsg());
					log.setRepayStatus(1);
					log.setRemark(resultData.getResultMsg());
					log.setUpdateTime(new Date());
					withholdingRepaymentLogService.updateById(log);
				

				} else if (remoteResult.getReturnCode().equals(RepayResultCodeEnum.BF00100.getValue())
						|| remoteResult.getReturnCode().equals(RepayResultCodeEnum.BF00112.getValue())
						|| remoteResult.getReturnCode().equals(RepayResultCodeEnum.BF00113.getValue())
						|| remoteResult.getReturnCode().equals(RepayResultCodeEnum.BF00115.getValue())
						|| remoteResult.getReturnCode().equals(RepayResultCodeEnum.BF00144.getValue())
						|| remoteResult.getReturnCode().equals(RepayResultCodeEnum.BF00202.getValue())) {
					result.setCode("2");
					result.setMsg(resultData.getResultMsg());
					log.setRepayStatus(2);
					log.setRemark(resultData.getResultMsg());
					log.setUpdateTime(new Date());
					withholdingRepaymentLogService.updateById(log);
				
				} else {
					result.setCode("-1");
					result.setMsg(resultData.getResultMsg());
					log.setRepayStatus(0);
					log.setRemark(resultData.getResultMsg());
					log.setUpdateTime(new Date());
					withholdingRepaymentLogService.updateById(log);
		
				}
				
				try {
					Thread.sleep(5000);
					getBFResult(log);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			//**********************************************************************银行代扣开始****************************************************************************//
			if (channel.getPlatformId() == PlatformEnum.YH_FORM.getValue()) {
				
				
				//判断是否开启协议代扣开关
				SysParameter  aggreeSwitch = sysParameterService.selectOne(
						new EntityWrapper<SysParameter>().eq("param_type", "agreement_withholding")
								.eq("status", 1).orderBy("param_value"));
				
				List channles=null;
				if(aggreeSwitch.getParamValue().equals("1")) {
					channles= bankCardInfo.getSignedProtocolList();
					if(channles.size()==0) {
						result.setCode("-1");
						result.setMsg("没有找到相关协议代扣渠道");
						RecordExceptionLog(pList.getOrigBusinessId(), pList.getAfterId(), result.getMsg());
						return result;
					}
				}else {
				  channles= withholdingChannelService.selectList(new EntityWrapper<WithholdingChannel>().eq("platform_id", PlatformEnum.YH_FORM.getValue()).eq("channel_status", 1).orderBy("channel_level", false));
					if(channles.size()==0) {
						result.setCode("-1");
						result.setMsg("没有找到相关代扣渠道");
						RecordExceptionLog(pList.getOrigBusinessId(), pList.getAfterId(), result.getMsg());
						return result;
					}
				}
				
				
				/*
				 * 调用接口前线插入记录 status 代扣状态(1:成功,0:失败;2:处理中)
				 */
				Integer status = 2;
				com.hongte.alms.common.result.Result merchAccountResult=platformRepaymentFeignClient.getOIdPartner(getBankSubBusinessType(business));
				String oIdPartner="";
				String tdUserName="";
				if(merchAccountResult.getCode().equals("1")) {
					Map map= (Map) merchAccountResult.getData();
					oIdPartner=map.get("oIdPartner").toString();
					tdUserName=map.get("tdUserName").toString();
					
				}else {
					logger.error("获取资产端唯一编号失败,pListId:{0}",pList.getPlanListId());
					result.setCode("-1");
					result.setMsg("获取资产端唯一编号失败,pListId:"+pList.getPlanListId());
					return result;
				}
				WithholdingRepaymentLog log = recordRepaymentLog("", status, pList, business, bankCardInfo,
						channel.getPlatformId(), boolLastRepay, boolPartRepay, merchOrderId,oIdPartner, 0,
						BigDecimal.valueOf(amount),appType);

				BankRechargeReqDto dto = new BankRechargeReqDto();
				for (Object channelObject : channles) {// 需要循环签约子渠道
	
					dto.setAmount(amount);
					if(aggreeSwitch.getParamValue().equals("1")) {
						dto.setChannelType(((SignedProtocol)channelObject).getChannelType().toString());// 子渠道
					}else {
						dto.setChannelType(((WithholdingChannel)channelObject).getSubPlatformId().toString());// 子渠道
					}
					logger.info("============================银行代扣，调用的子渠道是:"+dto.getChannelType()+"=====================================");
					dto.setRechargeUserId(bankCardInfo.getPlatformUserID());
					dto.setCmOrderNo(merchOrderId);
					dto.setOidPartner(oIdPartner);
					dto.setOrgUserName(tdUserName);
					dto.setUserIP("127.0.0.1");
					com.ht.ussp.core.Result remoteResult = null;
					try {
						logger.info("========调用外联平台银行代扣开始========");
						remoteResult = eipRemote.bankRecharge(dto);
						logger.info("========调用外联平台银行代扣结束,结果为："+remoteResult.toString()+"====================================");
						if (remoteResult == null) {
							return Result.error("500", "接口银行代扣调用失败！");
						}
					} catch (Exception e) {
						// 调接口异常也默认是处理中
						log.setRepayStatus(2);
						log.setRemark("调用接口异常");
						withholdingRepaymentLogService.updateById(log);

					}
					ResultData resultData = getBankResultMsg(remoteResult);
					SysParameter  bankRepayTestResult = sysParameterService.selectOne(
							new EntityWrapper<SysParameter>().eq("param_type", "bankRepayTest")
									.eq("status", 1).orderBy("param_value"));
				
					if(bankRepayTestResult!=null) {
						if(bankRepayTestResult.getParamValue().equals("0000")) {
							resultData.setResultMsg("充值成功");
							remoteResult.setReturnCode("0000");
							break;
						}else if(bankRepayTestResult.getParamValue().equals("1111")){
							resultData.setResultMsg("银行卡余额不足");
							remoteResult.setReturnCode("1111");
							break;
						}else if(bankRepayTestResult.getParamValue().equals("2222")){
							resultData.setResultMsg("处理中");
							remoteResult.setReturnCode("EIP_TD_HANDLER_EXECEPTION");
							break;
						}else {
							resultData.setResultMsg("代扣失败");
							remoteResult.setReturnCode("9999");
						}
					}
					if (remoteResult.getReturnCode().equals("0000") && resultData.getResultMsg().equals("充值成功")) {
						result.setCode("1");
						result.setMsg(resultData.getResultMsg());
						log.setRepayStatus(1);
						log.setRemark(resultData.getResultMsg());
						log.setUpdateTime(new Date());
						withholdingRepaymentLogService.updateById(log);
						break;
					} else if (remoteResult.getReturnCode().equals(RepayResultCodeEnum.YH_HANDLER_EXCEPTION.getValue())||remoteResult.getReturnCode().equals("INTERNAL_ERROR")||remoteResult.getReturnCode().equals(RepayResultCodeEnum.YH_HANDLER_TIMEOU.getValue())) {
						result.setCode("2");
						result.setMsg(resultData.getResultMsg());
						log.setRepayStatus(2);
						log.setRemark(resultData.getResultMsg());
						log.setUpdateTime(new Date());
						withholdingRepaymentLogService.updateById(log);

						break;
					} else if(resultData.getResultMsg().equals("服务调用异常")){
						result.setCode("2");
						result.setMsg(resultData.getResultMsg());
						log.setRepayStatus(2);
						log.setRemark(resultData.getResultMsg());
						log.setUpdateTime(new Date());
						withholdingRepaymentLogService.updateById(log);

						break;
						
					} if (!remoteResult.getReturnCode().equals("0000")&&(!remoteResult.getReturnCode().equals(RepayResultCodeEnum.YH_HANDLER_EXCEPTION.getValue()))&&(!remoteResult.getReturnCode().equals("INTERNAL_ERROR"))) {
							result.setCode("-1");
						result.setMsg(resultData.getResultMsg());
						log.setRepayStatus(0);
						log.setRemark(resultData.getResultMsg());
						log.setUpdateTime(new Date());
						withholdingRepaymentLogService.updateById(log);
						// 失败，重试其他子渠道
						
					continue;
					}
					

				}
				try {
					Thread.sleep(5000);
					getBankResult(log,oIdPartner);
				} catch (Exception e) {
					logger.debug("查询银行代扣结果出错"+e);
				}
			}

		}
		return result;

	}

	
	
	/**
	 * 获取业务子类型
	 */
	private Integer getBankSubBusinessType(BasicBusiness business) {
		Integer businessType=business.getBusinessType();
		if(business.getBusinessType()==BusinessTypeEnum.CREDIT_TYPE.getValue()&&business.getBusinessCtype()!=null&&business.getBusinessCtype().equals("业主信用贷用信")) {
			 businessType=26;
			 return businessType;
		}
		if(business.getBusinessType()==BusinessTypeEnum.CREDIT_TYPE.getValue()&&business.getBusinessCtype()!=null&&business.getBusinessCtype().equals("小微企业贷用信")) {
			 businessType=30; 
			return businessType;
		}
		
		return businessType;
		
	}
	
	/**
	 * 获取银行代扣结果
	 * 
	 * @param remoteResult
	 * @return 

	 */
	private ResultData getBankResultMsg(com.ht.ussp.core.Result remoteResult) {
		ResultData resultData=new ResultData();
		if(remoteResult.getData()!=null) {
			String dataJson = JSONObject.toJSONString(remoteResult.getData());
			Map<String, Object> resultMap = JSONObject.parseObject(dataJson, Map.class);
			resultData.setResultMsg((String) resultMap.get("result"));
			resultData.setStatus((String) resultMap.get("status"));
		}else {
			resultData.setResultMsg(remoteResult.getCodeDesc());
		}
		return resultData;
	
	}
	

	
	private ResultData getBankSearchResultMsg(com.ht.ussp.core.Result remoteResult) {
		ResultData resultData=new ResultData();
		if(remoteResult.getData()!=null) {
			String dataJson = JSONObject.toJSONString(remoteResult.getData());
			Map<String, Object> resultMap = JSONObject.parseObject(dataJson, Map.class);
			resultData.setResultMsg((String) resultMap.get("message"));
			resultData.setStatus((String) resultMap.get("status"));
		}else {
			resultData.setResultMsg(remoteResult.getCodeDesc());
		}
		return resultData;
	
	}

	/**
	 * 获取宝付代扣结果
	 * 
	 * @param remoteResult
	 * @return
	 */
	private ResultData getBFResultMsg(com.ht.ussp.core.Result remoteResult) {
		ResultData resultData=new ResultData();
		if(remoteResult.getData()!=null) {
		String dataJson = JSONObject.toJSONString(remoteResult.getData());
		Map<String, Object> resultMap = JSONObject.parseObject(dataJson, Map.class);
		resultData.setResultMsg((String) resultMap.get("respMsg"));
		}else {
			resultData.setResultMsg(remoteResult.getCodeDesc());
		}
		return resultData;
	}
	
	
	
	/**
	 * 获取易宝代扣结果
	 * 
	 * @param remoteResult
	 * @return
	 */
	private ResultData getYBResultMsg(com.ht.ussp.core.Result remoteResult) {
		ResultData resultData=new ResultData();
		if(remoteResult.getData()!=null) {
		String dataJson = JSONObject.toJSONString(remoteResult.getData());
		Map<String, Object> resultMap = JSONObject.parseObject(dataJson, Map.class);
		String yborderid = (String) resultMap.get("yborderid");
		String status = (String) resultMap.get("status");
			if(!StringUtil.isEmpty(yborderid)) {
				resultData.setResultMsg("交易成功");
				resultData.setStatus(status);
			}else {
				resultData.setResultMsg(remoteResult.getCodeDesc());
			}
		}else {
			resultData.setResultMsg(remoteResult.getCodeDesc());
		}
		return resultData;
	}

	// private Result excuteEipRemote(Integer platformId,Integer failCount,Double
	// amount,BankCardInfo info,String merchOrderId) {
	// Result result=new Result();
	//
	// Integer maxFailCount=3;//渠道最大失败次数
	// if(platformId!=null) {
	// WithholdingChannel chanel=withholdingChannelService.selectOne(new
	// EntityWrapper<WithholdingChannel>().eq("platform_id", platformId));
	// maxFailCount=chanel.getFailTimes();
	// }
	// if(failCount>=maxFailCount) {
	// result.setData(null);
	// result.setCode("-1");
	// result.setMsg("当前失败或者执行中次数为:" + failCount + ",超过限制次数，不允许执行。");
	// }else {
	// if(platformId==PlatformEnum.YB_FORM.getValue()) {
	// YiBaoRechargeReqDto dto=new YiBaoRechargeReqDto();
	// dto.setMerchantaccount(merchOrderId);
	// dto.setOrderid(merchOrderId);
	// dto.setTranstime(Long.parseLong(new
	// SimpleDateFormat("yyyyMMddHHmmss").format(new Date())));
	// dto.setAmount(amount);
	// dto.setProductname("");
	// dto.setIdentityid(info.getIdentityNo());
	// dto.setIdentitytype("01");
	// dto.setCard_top(info.getBankCardNumber().substring(0, 6));
	// dto.setCard_last(info.getBankCardNumber().substring(info.getBankCardNumber().length()-4,
	// info.getBankCardNumber().length()));
	// dto.setCallbackurl("172.0.0.1");
	// dto.setUserip("172.0.0.1");
	// eipRemote.yibaoRecharge(dto);
	// }
	// if(platformId==PlatformEnum.BF_FORM.getValue()) {
	// BaofuRechargeReqDto dto=new BaofuRechargeReqDto();
	// dto.setPayCode(info.getBankCode());
	// dto.setPayCm("2");
	// dto.setAccNo(info.getBankCardNumber());
	// dto.setIdCardType("01");
	// dto.setIdHolder(info.getBankCardName());
	// dto.setMobile(info.getMobilePhone());
	// dto.setTransId(merchOrderId);
	// dto.setTxnAmt(amount);
	// dto.setTradeDate(String.valueOf(Long.parseLong(new
	// SimpleDateFormat("yyyyMMddHHmmss").format(new Date()))));
	// dto.setTransSerialNo(merchOrderId);
	// eipRemote.baofuRecharge(dto);
	// }
	// if(platformId==PlatformEnum.YH_FORM.getValue()) {
	// List<SysParameter> bankChannels = sysParameterService.selectList(new
	// EntityWrapper<SysParameter>().eq("param_type",
	// SysParameterEnums.BANK_CHANNEL.getKey()).eq("status",1).orderBy("row_Index"));
	//
	// BankRechargeReqDto dto=new BankRechargeReqDto();
	// dto.setAmount(amount);
	// dto.setChannelType("102");//todo需要循环子渠道
	// dto.setRechargeUserId(info.getPlatformUserID());
	// dto.setCmOrderNo(merchOrderId);
	// dto.setOidPartner(oidPartner);
	// dto.setOrgUserName(orgUserName);
	// com.ht.ussp.core.Result result1=eipRemote.bankRecharge(dto);
	// }
	//
	// }
	// return result;
	//
	// }
	
	
	/**
	 * 判断当前期有没有客户还款登记
	 * 
	 */
	
	private boolean isSign(RepaymentBizPlanList pList) {
		boolean isSign = false;
		int i=moneyPoolRepaymentService.selectCount(new EntityWrapper<MoneyPoolRepayment>().eq("original_business_id", pList.getOrigBusinessId()).eq("after_id", pList.getAfterId()).eq("is_deleted", 0));
		int j=moneyPoolRepaymentService.selectCount(new EntityWrapper<MoneyPoolRepayment>().eq("original_business_id", pList.getOrigBusinessId()).eq("after_id", pList.getAfterId()).isNull("is_deleted"));
		if((i+j)>0) {
			isSign=true;
		}else {
			isSign=false;
		}
		return isSign;
	}
	
	@Override
	public boolean istLastPeriod(RepaymentBizPlanList pList) {
		boolean isLast = false;
		List<RepaymentBizPlanList> pLists = repaymentBizPlanListService
				.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("plan_id", pList.getPlanId()));
		RepaymentBizPlanList lastpList = pLists.stream().max(new Comparator<RepaymentBizPlanList>() {
			@Override
			public int compare(RepaymentBizPlanList o1, RepaymentBizPlanList o2) {
				return o1.getDueDate().compareTo(o2.getDueDate());
			}
		}).get();

		if (pList.getPlanListId().equals(lastpList.getPlanListId())) {
			isLast = true;
		}
		return isLast;
	}

	@Override
	public List<BankCardInfo>  getCustomerInfo(String identifyCard) {
		CustomerInfoDto dto = new CustomerInfoDto();
		List<BankCardInfo> bankCardInfos = null;
		try {
		Result result = customerInfoXindaiRemoteApi.getBankcardInfo(identifyCard);
		if(result.getCode().equals("1")) {
			 bankCardInfos=JSON.parseArray(result.getData().toString(), BankCardInfo.class);
		}
		} catch (Exception e) {
			logger.info("获取客户银行卡信息出错"+e);
		}
		// todo 调信贷接口
		return bankCardInfos;
	}

	@Override
	public String getMerchOrderId() {
		String merchOrderId = "";
		while (true) {
			merchOrderId = MerchOrderUtil.getMerchOrderId();
			if (redisService.hasKey(merchOrderId) == false) {
				redisService.set(merchOrderId, merchOrderId);
				break;
			} else {
				continue;
			}
		}
		return merchOrderId;
	}

	public WithholdingRepaymentLog recordRepaymentLog(String msg, Integer status, RepaymentBizPlanList list,
			BasicBusiness business, BankCardInfo dto, Integer platformId, Integer boolLastRepay, Integer boolPartRepay,
			String merchOrderId,String PlatformUserID ,Integer settlementType, BigDecimal currentAmount,String appType) {
		WithholdingRepaymentLog log = new WithholdingRepaymentLog();
		log.setAfterId(list.getAfterId());
		log.setBankCard(dto.getBankCardNumber());
		log.setBindPlatformId(platformId);
		log.setBoolLastRepay(boolLastRepay);
		log.setBoolPartRepay(boolPartRepay);
		log.setCreateTime(new Date());
		log.setIdentityCard(business.getCustomerIdentifyCard());
		log.setMerchOrderId(merchOrderId);
		log.setOriginalBusinessId(business.getBusinessId());
		log.setPhoneNumber(dto.getMobilePhone());
		log.setCustomerName(dto.getBankCardName());
		log.setMerchantAccount(PlatformUserID);
		log.setRepayStatus(status);
		log.setRemark(msg);
		log.setThirdOrderId(merchOrderId);
		log.setSettlementType(settlementType);
		log.setBankCode(dto.getBankCode());
		log.setBankName(dto.getBankName());
		if(list.getOverdueAmount()!=null) {
			log.setPlanTotalRepayMoney(list.getTotalBorrowAmount().add(list.getOverdueAmount()));
		}else {
			log.setPlanTotalRepayMoney(list.getTotalBorrowAmount());
		}

		log.setUpdateTime(new Date());
		if(StringUtil.isEmpty(appType)) {
			if (loginUserInfoHelper != null && !StringUtil.isEmpty(loginUserInfoHelper.getUserId())) {
				log.setUpdateUser(loginUserInfoHelper.getUserId());
				log.setCreateUser(loginUserInfoHelper.getUserId());
			} else {
				log.setCreateUser(appType);
				log.setUpdateUser(appType);
			}
		}else {
			log.setCreateUser(appType);
			log.setUpdateUser(appType);
		}
	

		log.setCurrentAmount(currentAmount);// 本次代扣金额
		withholdingRepaymentLogService.insertOrUpdate(log);
		return log;
	}

	@Override
	public BigDecimal getRestAmount(RepaymentBizPlanList list) {
		List<WithholdingRepaymentLog> logs = withholdingRepaymentLogService.selectList(
				new EntityWrapper<WithholdingRepaymentLog>().eq("original_business_id", list.getOrigBusinessId())
						.eq("after_id", list.getAfterId()).eq("repay_status", "2"));//处理中的数据
		BigDecimal hasRepayAmount = BigDecimal.valueOf(0);
		for (WithholdingRepaymentLog log : logs) {
			hasRepayAmount = hasRepayAmount.add(log.getCurrentAmount());
		}
		
		hasRepayAmount=hasRepayAmount.add(getPerListFactAmountSum(list));
		
		BigDecimal restAmount = null;
		if(list.getOverdueAmount()!=null) {
			 restAmount = list.getTotalBorrowAmount().add(list.getOverdueAmount()).subtract(hasRepayAmount);
		}else {
			 restAmount = list.getTotalBorrowAmount().subtract(hasRepayAmount);
		}
		
		return restAmount;
	}

	@Override
	public BigDecimal getOnlineAmount(RepaymentBizPlanList list) {
		List<RepaymentBizPlanListDetail> details = repaymentBizPlanListDetailService
				.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().eq("fee_id",
						RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid()).eq("plan_list_id", list.getPlanListId()));
		BigDecimal underAmount = BigDecimal.valueOf(0);
		BigDecimal onlineAmount=BigDecimal.valueOf(0);
		for (RepaymentBizPlanListDetail detail : details) {
			underAmount = underAmount.add(detail.getPlanAmount());
		}
		if(list.getOverdueAmount()!=null) {
			 onlineAmount = list.getTotalBorrowAmount().add(list.getOverdueAmount()).subtract(underAmount);
		}else {
			 onlineAmount = list.getTotalBorrowAmount().subtract(underAmount);
		}
		
		return onlineAmount;
	}

	@Override
	public BigDecimal getUnderlineAmount(RepaymentBizPlanList list) {
		List<RepaymentBizPlanListDetail> details = repaymentBizPlanListDetailService
				.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().eq("fee_id",
						RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid()).eq("plan_list_id", list.getPlanListId()));
		BigDecimal underAmount = BigDecimal.valueOf(0);
		for (RepaymentBizPlanListDetail detail : details) {
			underAmount = underAmount.add(detail.getPlanAmount());
		}
		return underAmount;
	}

	@Override
	public Integer getBankRepaySuccessCount(RepaymentBizPlanList list) {
		List<WithholdingRepaymentLog> logs = withholdingRepaymentLogService.selectList(
				new EntityWrapper<WithholdingRepaymentLog>().eq("original_business_id", list.getOrigBusinessId())
						.eq("after_id", list.getAfterId()).ne("repay_status", 0).eq("bind_platform_id", PlatformEnum.YH_FORM.getValue()));//处理成功数据

		return logs.size();
	}

	@Override
	public Result EnsureAutoPayIsEnabled(RepaymentBizPlanList pList, Integer days) {
		Result result=new Result();
		result.setCode("1");
		BizOutputRecord record = bizOutputRecordService
				.selectOne(new EntityWrapper<BizOutputRecord>().eq("business_id", pList.getBusinessId()));
//		if (DateUtil.getDiff(new Date(), pList.getDueDate()) > 30) {
//			// msg = "超出三十天不自动代扣";
//			result.setCode("-1");
//			result.setMsg("超出三十天不自动代扣");
//			return result;
//		}
		if(record==null) {
			result.setCode("-1");
			result.setMsg("没有出款记录");
			RecordExceptionLog(pList.getOrigBusinessId(), pList.getAfterId(), result.getMsg());
			return result;
		}
		if (record.getFactOutputDate()!=null&&DateUtil.getDiff(new Date(), record.getFactOutputDate()) > 0)

		{
			// msg = "借款日期大于当前日期";
			result.setCode("-1");
			result.setMsg("借款日期大于当前日期");
			RecordExceptionLog(pList.getOrigBusinessId(), pList.getAfterId(), result.getMsg());
			return result;
		}
		if (pList.getPeriod() == 0)

		{
			// msg = "自动代扣取消展期00期代扣";
			result.setCode("-1");
			result.setMsg("自动代扣取消展期第0期代扣");
			RecordExceptionLog(pList.getOrigBusinessId(), pList.getAfterId(), result.getMsg());
			return result;
		}

		if (istLastPeriod(pList)) {
			// msg="最后一期不能自动代扣"
			result.setCode("-1");
			result.setMsg("最后一期不能自动代扣");
			RecordExceptionLog(pList.getOrigBusinessId(), pList.getAfterId(), result.getMsg());
			return result;
		}
		
		if(isSign(pList)) {
			// msg="客户还款登记了不能自动代扣"
			result.setCode("-1");
			result.setMsg("客户还款登记了不能自动代扣");
			RecordExceptionLog(pList.getOrigBusinessId(), pList.getAfterId(), result.getMsg());
			return result;
		}
		if(isUnderRepay(pList)) {
			// 判断是否含有线下转账，如果有，不能自动代扣
			result.setCode("-1");
			result.setMsg("含有线下转账，不能自动代扣");
			RecordExceptionLog(pList.getOrigBusinessId(), pList.getAfterId(), result.getMsg());
			return result;
		}
		
		if(isApplyDerate(pList)) {
			//判断是否发起过减免申请,发起过的不能自动代扣
			result.setCode("-1");
			result.setMsg("发起过减免申请,不能自动代扣");
			RecordExceptionLog(pList.getOrigBusinessId(), pList.getAfterId(), result.getMsg());
			return result;
		}
       if(isRepaying(pList)) {
    		//判断是否有处理中的代扣记录
			result.setCode("-1");
			result.setMsg("有处理中的代扣记录,不能自动代扣");
			RecordExceptionLog(pList.getOrigBusinessId(), pList.getAfterId(), result.getMsg());
			return result;
       }
       
       
       
       
       
		RepaymentProjPlanList projPList=repaymentProjPlanListService.selectOne(new EntityWrapper<RepaymentProjPlanList>().eq("plan_list_id", pList.getPlanListId()));
		if(projPList!=null) {
			if(projPList.getPlateType()==2) {//你我金融的不代扣
				result.setCode("-1");
				result.setMsg("你我金融的不能自动代扣");
				RecordExceptionLog(pList.getOrigBusinessId(), pList.getAfterId(), result.getMsg());
				return result;
			}
		}
		
	
		
//		RepaymentBizPlanList next = repaymentBizPlanListService
//				.selectOne(new EntityWrapper<RepaymentBizPlanList>().eq("business_id", pList.getBusinessId())
//						.eq("plan_id", pList.getPlanId()).eq("period", pList.getPeriod() + 1));
//
//		if (next != null) {
//			Date nowDate = new Date();
//			days = 0 - days;
//			Date before30Days = DateUtil.getDate(DateUtil.formatDate(DateUtil.addDay2Date(days, nowDate)));
//			if (next.getDueDate().compareTo(before30Days) >= 0 && next.getDueDate().compareTo(nowDate) <= 0) {
//				// msg = "下一期还款时间开始停止自动代扣上一期";
//				return false;
//			}

//		}
		return result;
	}

	/**
	 * 获取该还款计划最早一期没有还的代扣 
	 */
	@Override
	public RepaymentBizPlanList getEarlyPeriod(RepaymentBizPlanList list) {
		
		List<RepaymentBizPlanList> repaymentBizPlanLists=repaymentBizPlanListService.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("plan_id", list.getPlanId()).eq("confirm_flag", 1));
		//到了应该还日期的期数集合
		List<RepaymentBizPlanList> dueDateLists=repaymentBizPlanLists.stream().filter(a->a.getDueDate().compareTo(new Date())<=1).collect(Collectors.toList());
		//还没有还款的到了应该还日期的期数集合
		List<RepaymentBizPlanList> notRepayLists=repaymentBizPlanLists.stream().filter(a->(!a.getCurrentStatus().equals("已还款"))).collect(Collectors.toList());
		//期数从小到大排序
		Optional<RepaymentBizPlanList> min=notRepayLists.stream().min((a1,a2)->a1.getPeriod().compareTo(a2.getPeriod()));
		RepaymentBizPlanList minRepayBizPlanList=min.get();
		return minRepayBizPlanList;
		
	}
	
	/**
	 * 判断是否发起过减免申请
	 */
	
	private boolean isApplyDerate(RepaymentBizPlanList list) {
		boolean isApplyDerate;
		ApplyDerateProcess applyDerateProcess=applyDerateProcessService.selectOne(new EntityWrapper<ApplyDerateProcess>().eq("crp_id", list.getPlanListId()));
		if(applyDerateProcess!=null) {
			applyDerateProcess.getProcessId();
			int i=processService.selectCount(new EntityWrapper<Process>().eq("process_result", 1));//减免申请成功通过记录条数
			int j=processService.selectCount(new EntityWrapper<Process>().isNull("process_result").ne("status", 2).ne("status", 3));//减免申请中的记录条数
			if((i+j)>0) {
				isApplyDerate=true;
			}else {
				isApplyDerate=false;
			}
		}else {
			isApplyDerate=false;
		}
	
		return isApplyDerate;
	}
	
	
	/**
	 * 如果是线上已还款，判断是否在宽限期外，如果是，就可以自动代扣，否则不能
	 */
	
//	private boolean isForgiveDayOutside(RepaymentBizPlanList list) {
//		boolean isForgiveDayOutside = false;
//		SysParameter  forgiveDayParam = sysParameterService.selectOne(
//				new EntityWrapper<SysParameter>().eq("param_type", SysParameterEnums.FORGIVE_DAYS)
//						.eq("status", 1).orderBy("param_value"));
//		BigDecimal overDays=list.getOverdueDays();
//		BigDecimal  forgiveDay=BigDecimal.valueOf(Double.valueOf(forgiveDayParam.getParamValue()));
//		if(overDays.compareTo(forgiveDay)>0) {//在宽限期外
//			 isForgiveDayOutside=true;
//		}
//		return isForgiveDayOutside;
//	}
	
	/**
	 * 判断是否含有线下转账，如果有，不能自动代扣
	 * 
	 */
	private boolean isUnderRepay(RepaymentBizPlanList list) {
		boolean isUnderRepay=false;
		int i=repaymentConfirmLogService.selectCount(new EntityWrapper<RepaymentConfirmLog>().eq("org_business_id", list.getOrigBusinessId()).eq("after_id", list.getAfterId()).eq("repay_source", 10));
		if(i>0) {
			if(list.getRepayStatus()!=null&&list.getRepayStatus()==2) {//线上已还完也可以自动代扣
				isUnderRepay=false;
			}
			isUnderRepay=true;
		}else {
			isUnderRepay=false;
		}
		return isUnderRepay;
	}
	
	@Override
	public Result shareProfit(RepaymentBizPlanList list, WithholdingRepaymentLog log) {
		
  	
		Result result=null;
		try {
			logger.info("@shareProfit@自动代扣调用核销feign--开始[{}{}{}]", list.getOrigBusinessId(),list.getAfterId(),log.getLogId());
		 result = financeClient.shareProfit(list.getOrigBusinessId(), list.getAfterId(), log.getLogId());
			logger.info("@shareProfit@自动代扣核销feign--结束[{}{}{}]", list.getOrigBusinessId(),list.getAfterId(),log.getLogId());

		}catch (Exception e) {
			logger.info("调用核销方法异常"+e);
		}
		return result;
	}

	@Override
	public BankCardInfo getThirtyPlatformInfo(List<BankCardInfo> list) {
		for (BankCardInfo card : list) {
			if (card.getWithholdingType() == 1&&card.getThirdPlatformList().size()>0) {// 等于第三方银行代扣并且是代扣主卡
				return card;
			} else {
				return null;
			}
		}
		return null;
	}

	@Override
	public BankCardInfo getBankCardInfo(List<BankCardInfo> list) {
		for (BankCardInfo card : list) {
			if (card.getPlatformType() != 0 && card.getWithholdingType() == 1) {// 不等于第三方代扣银行卡并且是代扣主卡
				return card;
			} else {
				return null;
			}
		}
		return null;
	}

	@Override
	public void getReturnResult() {
		List<WithholdingRepaymentLog> losgs = withholdingRepaymentLogService.selectRepaymentLogForResult();
		for (WithholdingRepaymentLog log : losgs) {
			try {
				if (log.getBindPlatformId() == PlatformEnum.YH_FORM.getValue()) {
					getBankResult(log,log.getMerchantAccount());
				}
				if (log.getBindPlatformId() == PlatformEnum.BF_FORM.getValue()) {
					getBFResult(log);
				}
				if (log.getBindPlatformId() == PlatformEnum.YB_FORM.getValue()) {
					getYBResult(log);
				}
			}catch(Exception e) {
				logger.error("代扣结果同步出错,logId:"+log.getLogId());
			}
		}
	}

	@Override
	public void getBankResult(WithholdingRepaymentLog log,String oidPartner) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("oidPartner", oidPartner);
		paramMap.put("cmOrderNo", log.getMerchOrderId());
		
	
		logger.info("========调用外联银行代扣订单查询开始========");
		com.ht.ussp.core.Result result = eipRemote.queryRechargeOrder(paramMap);
		logger.info("========调用外联银行代扣订单查询结束,结果为："+result.toString()+"====================================");
		//获取借款日期
		TuandaiProjectInfo tuandaiProjectInfo = tuandaiProjectInfoService.selectOne(
				new EntityWrapper<TuandaiProjectInfo>().eq("business_id", log.getOriginalBusinessId()));
		Date borrowDate = null;
		if (tuandaiProjectInfo.getQueryFullSuccessDate() != null) {
			borrowDate = tuandaiProjectInfo.getQueryFullSuccessDate();
		} else {
			borrowDate = tuandaiProjectInfo.getCreateTime();
		}
		
		RepaymentBizPlanList pList=repaymentBizPlanListService.selectOne(new EntityWrapper<RepaymentBizPlanList>().eq("orig_business_id", log.getOriginalBusinessId()).eq("after_id", log.getAfterId()));
		RepaymentBizPlan plan=repaymentBizPlanService.selectOne(new EntityWrapper<RepaymentBizPlan>().eq("plan_id", pList.getPlanId()));
         //*********************************************挡板测试代码***************************************//
		SysParameter  bankRepayTestResult = sysParameterService.selectOne(
				new EntityWrapper<SysParameter>().eq("param_type", "bankRepayTest")
						.eq("status", 1).orderBy("param_value"));
	
		if(bankRepayTestResult!=null) {
			if(bankRepayTestResult.getParamValue().equals("0000")) {
				String resultMsg="充值成功";
				result.setReturnCode("0000");
				result.msg(resultMsg);
			}else if(bankRepayTestResult.getParamValue().equals("1111")){
				String resultMsg="银行卡余额不足";
				result.setReturnCode("1111");
				result.msg(resultMsg);
			}else if(bankRepayTestResult.getParamValue().equals("2222")){
				String resultMsg="处理中";
				result.msg(resultMsg);
				result.setReturnCode("EIP_TD_HANDLER_EXECEPTION");
			}else {
				String resultMsg="代扣失败";
				result.msg(resultMsg);
				result.setReturnCode("9999");
			}
		}
		//*********************************************挡板测试代码***************************************//
		
		if (result == null) {
			log.setRepayStatus(2);
			log.setRemark("调用外联平台接口异常");
			log.setUpdateTime(new Date());
			withholdingRepaymentLogService.updateById(log);
			throw new ServiceRuntimeException("调用外联平台接口失败！");
		}
		if (result.getReturnCode().equals("0000") &&getBankSearchResultMsg(result).getResultMsg().contains("充值成功")&&getBankSearchResultMsg(result).getStatus().equals("2")) {
			log.setRepayStatus(1);
			log.setRemark(getBankSearchResultMsg(result).getResultMsg());
			log.setUpdateTime(new Date());
			withholdingRepaymentLogService.updateById(log);
			shareProfit(pList, log);
			try {
			sendMessageService.sendAfterRepaySuccessSms(log.getPhoneNumber(),log.getCustomerName(),borrowDate, plan.getBorrowMoney(), pList.getPeriod(), pList.getTotalBorrowAmount().add(pList.getOverdueAmount()==null?BigDecimal.valueOf(0):pList.getOverdueAmount()),log.getCurrentAmount());
			}catch(Exception e) {
				logger.error("银行代扣成功短信发送错误,logId:{0}",log.getLogId());
			}
		} else if (result.getReturnCode().equals(RepayResultCodeEnum.YH_HANDLER_EXCEPTION.getValue())||result.getReturnCode().equals("INTERNAL_ERROR")||result.getReturnCode().equals(RepayResultCodeEnum.YH_HANDLER_TIMEOU.getValue())) {
			log.setRepayStatus(2);
			log.setRemark(result.getReturnCode()+"："+result.getMsg());
			log.setUpdateTime(new Date());
			withholdingRepaymentLogService.updateById(log);

		} else if(result.getMsg()!=null&&result.getMsg().contains("服务调用异常")){
			log.setRepayStatus(2);
			log.setRemark(result.getMsg());
			log.setUpdateTime(new Date());
			withholdingRepaymentLogService.updateById(log);

			
		} 
		
		
		if (result.getReturnCode().equals("0000")&&getBankSearchResultMsg(result).getStatus().equals("1")) {
			log.setRepayStatus(0);
			log.setRemark(result.getMsg());
			log.setUpdateTime(new Date());
			withholdingRepaymentLogService.updateById(log);
			try {
			//sms
			sendMessageService.sendAfterRepayFailSms(log.getPhoneNumber(),log.getCustomerName(),borrowDate, plan.getBorrowMoney(), pList.getPeriod());
			}catch(Exception e) {
				logger.error("银行代扣失败短信发送错误,logId:{0}",log.getLogId());
			}
		}
		
	}

	@Override
	public void getBFResult(WithholdingRepaymentLog log) {
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("transSerialNo", log.getMerchOrderId());
		paramMap.put("origTransId", log.getMerchOrderId());
		paramMap.put("tradeDate", log.getMerchOrderId().substring(0, log.getMerchOrderId().length() - 7));
		logger.info("========调用外联宝付代扣订单查询开始========");
		com.ht.ussp.core.Result result = eipRemote.queryBaofuStatus(paramMap);
		logger.info("========调用外联宝付代扣订单查询结束,结果为："+result.toString()+"====================================");

		
		//获取借款日期
		TuandaiProjectInfo tuandaiProjectInfo = tuandaiProjectInfoService.selectOne(
				new EntityWrapper<TuandaiProjectInfo>().eq("business_id", log.getOriginalBusinessId()));
		Date borrowDate = null;
		if (tuandaiProjectInfo.getQueryFullSuccessDate() != null) {
			borrowDate = tuandaiProjectInfo.getQueryFullSuccessDate();
		} else {
			borrowDate = tuandaiProjectInfo.getCreateTime();
		}
		
		RepaymentBizPlanList pList=repaymentBizPlanListService.selectOne(new EntityWrapper<RepaymentBizPlanList>().eq("orig_business_id", log.getOriginalBusinessId()).eq("after_id", log.getAfterId()));
		RepaymentBizPlan plan=repaymentBizPlanService.selectOne(new EntityWrapper<RepaymentBizPlan>().eq("plan_id", pList.getPlanId()));

		
		
		if (result == null) {
			log.setRepayStatus(2);
			log.setRemark("调用外联平台接口异常");
			log.setUpdateTime(new Date());
			withholdingRepaymentLogService.updateById(log);
			throw new ServiceRuntimeException("调用外联平台接口失败！");
		}
		ResultData resultData=getBFResultMsg(result);
		
		//**************挡板测试代码****************************************************/
		SysParameter  thirtyRepayTestResult = sysParameterService.selectOne(
				new EntityWrapper<SysParameter>().eq("param_type", "thirtyRepayTest")
						.eq("status", 1).orderBy("param_value"));
		if(thirtyRepayTestResult!=null) {
			if(thirtyRepayTestResult.getParamValue().equals("0000")) {
				String resultMsg="充值成功";
				result.setMsg(resultMsg);
				result.setReturnCode("0000");
			}else if(thirtyRepayTestResult.getParamValue().equals("1111")){
				String resultMsg="银行卡余额不足";
				result.setMsg(resultMsg);
				result.setReturnCode("1111");
			}else if(thirtyRepayTestResult.getParamValue().equals("2222")){
				String resultMsg="处理中";
				result.setMsg(resultMsg);
				result.setReturnCode("EIP_BAOFU_BF00115");
			}else {
				String resultMsg="代扣失败";
				result.setMsg(resultMsg);
				result.setReturnCode("9999");
			}
		}
		//**************挡板测试代码****************************************************/
		if ((result.getReturnCode().equals("0000")
				&& (result.getReturnCode().equals(RepayResultCodeEnum.BF0000.getValue()))||result.getReturnCode().equals(RepayResultCodeEnum.BF00114.getValue()))) {
			log.setRepayStatus(1);
			log.setRemark(result.getMsg());
			log.setUpdateTime(new Date());
			withholdingRepaymentLogService.updateById(log);
			shareProfit(pList, log);
			try {
			sendMessageService.sendAfterRepaySuccessSms(log.getPhoneNumber(),log.getCustomerName(),borrowDate, plan.getBorrowMoney(), pList.getPeriod(), pList.getTotalBorrowAmount().add(pList.getOverdueAmount()==null?BigDecimal.valueOf(0):pList.getOverdueAmount()),log.getCurrentAmount());
			}catch(Exception e) {
				logger.error("宝付代扣成功短信发送错误,logId:{0}",log.getLogId());
			}

		} else if (result.getReturnCode().equals(RepayResultCodeEnum.BF00100.getValue())
				|| result.getReturnCode().equals(RepayResultCodeEnum.BF00112.getValue())
				|| result.getReturnCode().equals(RepayResultCodeEnum.BF00113.getValue())
				|| result.getReturnCode().equals(RepayResultCodeEnum.BF00115.getValue())
				|| result.getReturnCode().equals(RepayResultCodeEnum.BF00144.getValue())
				|| result.getReturnCode().equals(RepayResultCodeEnum.BF00202.getValue())) {
			log.setRepayStatus(2);
			log.setRemark(result.getMsg());
			log.setUpdateTime(new Date());
			withholdingRepaymentLogService.updateById(log);
		
		} else {
			log.setRepayStatus(0);
			log.setRemark(result.getMsg());
			log.setUpdateTime(new Date());
			withholdingRepaymentLogService.updateById(log);
			try {
			sendMessageService.sendAfterRepayFailSms(log.getPhoneNumber(),log.getCustomerName(),borrowDate, plan.getBorrowMoney(), pList.getPeriod());
			}catch(Exception e) {
				logger.error("宝付代扣失败短信发送错误,logId:{0}",log.getLogId());
			}
		}

		
	}

	@Override
	public void getYBResult(WithholdingRepaymentLog log) {
		YiBaoRechargeReqDto dto = new YiBaoRechargeReqDto();
		Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("merchantaccount", log.getMerchantAccount());
		paramMap.put("orderid", log.getMerchOrderId());
		//获取借款日期
		TuandaiProjectInfo tuandaiProjectInfo = tuandaiProjectInfoService.selectOne(
				new EntityWrapper<TuandaiProjectInfo>().eq("business_id", log.getOriginalBusinessId()));
		Date borrowDate = null;
		if (tuandaiProjectInfo.getQueryFullSuccessDate() != null) {
			borrowDate = tuandaiProjectInfo.getQueryFullSuccessDate();
		} else {
			borrowDate = tuandaiProjectInfo.getCreateTime();
		}
		
		RepaymentBizPlanList pList=repaymentBizPlanListService.selectOne(new EntityWrapper<RepaymentBizPlanList>().eq("orig_business_id", log.getOriginalBusinessId()).eq("after_id", log.getAfterId()));
		RepaymentBizPlan plan=repaymentBizPlanService.selectOne(new EntityWrapper<RepaymentBizPlan>().eq("plan_id", pList.getPlanId()));

		
		
		
	
		logger.info("========调用外联易宝代扣订单查询开始========");
		com.ht.ussp.core.Result result = eipRemote.queryOrder(paramMap);
		logger.info("========调用外联易宝代扣订单查询结束,结果为："+result.toString()+"====================================");
		ResultData resultData=getYBResultMsg(result);
		if (result.getReturnCode().equals("0000") && resultData.getStatus().equals("1")) {//成功
			log.setRepayStatus(1);
			log.setRemark(resultData.getResultMsg());
			log.setUpdateTime(new Date());
			withholdingRepaymentLogService.updateById(log);
			shareProfit(pList, log);
			try {
			sendMessageService.sendAfterRepaySuccessSms(log.getPhoneNumber(),log.getCustomerName(),borrowDate, plan.getBorrowMoney(), pList.getPeriod(), pList.getTotalBorrowAmount().add(pList.getOverdueAmount()==null?BigDecimal.valueOf(0):pList.getOverdueAmount()),log.getCurrentAmount());
			}catch(Exception e) {
				logger.error("易宝代扣成功短信发送错误,logId:{0}",log.getLogId());
			}

		} else if (result.getReturnCode().equals("0000") && resultData.getStatus().equals("5")) {//处理中
			log.setRepayStatus(2);
			log.setRemark(resultData.getResultMsg());
			log.setUpdateTime(new Date());
			withholdingRepaymentLogService.updateById(log);

		}else if (result.getReturnCode().equals("0000") && resultData.getStatus().equals("4")) {//失败
			log.setRepayStatus(0);
			log.setRemark(resultData.getResultMsg());
			log.setUpdateTime(new Date());
			withholdingRepaymentLogService.updateById(log);

		}else if (resultData.getResultMsg().contains("系统异常")) {
			log.setRepayStatus(2);
			log.setRemark(resultData.getResultMsg());
			log.setUpdateTime(new Date());
			withholdingRepaymentLogService.updateById(log);

		} else if (!result.getReturnCode().equals("0000")) {
			log.setRepayStatus(0);
			log.setRemark(resultData.getResultMsg());
			log.setUpdateTime(new Date());
			withholdingRepaymentLogService.updateById(log);
			try {
			sendMessageService.sendAfterRepayFailSms(log.getPhoneNumber(),log.getCustomerName(),borrowDate, plan.getBorrowMoney(), pList.getPeriod());
			}catch(Exception e) {
				logger.error("易宝代扣失败短信发送错误,logId:{0}",log.getLogId());
			}
		}
		
	}
	/**
	 * 获取每期实还金额
	 * @param pList
	 * @return
	 */
	private BigDecimal getPerListFactAmountSum(RepaymentBizPlanList pList) {
		List<RepaymentBizPlanListDetail> details=repaymentBizPlanListDetailService.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().eq("plan_list_id", pList.getPlanListId()));
		BigDecimal factAmountSum=BigDecimal.valueOf(0);
		for(RepaymentBizPlanListDetail detail:details) {
			factAmountSum=factAmountSum.add(detail.getFactAmount()==null?BigDecimal.valueOf(0):detail.getFactAmount());
		}
		return factAmountSum;

	}

	@Override
	public boolean isRepaying(RepaymentBizPlanList pList) {
		boolean isRepaying=false;
		int i=withholdingRepaymentLogService.selectCount(new EntityWrapper<WithholdingRepaymentLog>().eq("original_business_id", pList.getOrigBusinessId()).eq("after_id", pList.getAfterId()).eq("repay_status", 2));
		if(i>0) {
			isRepaying=true;
		}
		return isRepaying;
	}

	@Override
	public void RecordExceptionLog(String OriginalBusinessId,String afterId, String msg) {
		String errorMsg=MessageFormat.format("自动代扣跳出:businessId:{0},afterId:{1},{2}",OriginalBusinessId,afterId,msg);
		SysExceptionLog log=new SysExceptionLog();
		log.setLogDate(new Date());
		log.setLogLevel("1");
		log.setLogger(afterId);
		log.setBusinessId(OriginalBusinessId);
		log.setLogMessage(errorMsg);
		log.setLogType("自动代扣");
		log.insert();
		sysExceptionLogService.insertOrUpdate(log);
		
	}



}
