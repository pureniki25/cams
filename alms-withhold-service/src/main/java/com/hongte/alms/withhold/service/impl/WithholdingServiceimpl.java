package com.hongte.alms.withhold.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.aliyun.oss.ServiceException;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.SysBankLimit;
import com.hongte.alms.base.entity.SysParameter;
import com.hongte.alms.base.entity.WithholdingChannel;
import com.hongte.alms.base.entity.WithholdingRepaymentLog;
import com.hongte.alms.base.enums.PlatformEnum;
import com.hongte.alms.base.enums.SysParameterEnums;
import com.hongte.alms.base.enums.WithholdTypeEnum;
import com.hongte.alms.base.feignClient.dto.BankCardInfo;
import com.hongte.alms.base.feignClient.dto.CustomerInfoDto;
import com.hongte.alms.base.feignClient.dto.ThirdPlatform;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.base.service.SysBankLimitService;
import com.hongte.alms.base.service.SysParameterService;
import com.hongte.alms.base.service.WithholdingChannelService;
import com.hongte.alms.base.service.WithholdingRepaymentLogService;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.withhold.service.RechargeService;
import com.hongte.alms.withhold.service.WithholdingService;
import com.hongte.alms.withhold.util.MyRedisTemple;

/**
 * @author czs
 * @since 2018/5/24 自动代扣的服务
 */
@Service("WithholdingService")
public class WithholdingServiceimpl implements WithholdingService {

	private static Logger logger = LoggerFactory.getLogger(WithholdingServiceimpl.class);
	@Autowired
	Executor executor;
	@Autowired
	@Qualifier("RepaymentBizPlanListService")
	RepaymentBizPlanListService repaymentBizPlanListService;

	@Autowired
	@Qualifier("BasicBusinessService")
	BasicBusinessService basicBusinessService;

	@Autowired
	@Qualifier("WithholdingChannelService")
	WithholdingChannelService withholdingChannelService;

	@Autowired
	@Qualifier("SysBankLimitService")
	SysBankLimitService sysBankLimitService;

	@Autowired
	@Qualifier("RechargeService")
	RechargeService rechargeService;

	@Autowired
	@Qualifier("SysParameterService")
	SysParameterService sysParameterService;
	

	@Autowired
	@Qualifier("WithholdingRepaymentLogService")
	WithholdingRepaymentLogService withholdingRepaymentLogService;
	@Autowired
	private  MyRedisTemple myRedisTemplate;
	@Override
	public void withholding() {
		List<SysParameter> repayStatusList = sysParameterService.selectList(new EntityWrapper<SysParameter>()
				.eq("param_type", SysParameterEnums.REPAY_DAYS.getKey()).eq("status", 1).orderBy("row_Index"));
		Integer days = Integer.valueOf(repayStatusList.get(0).getParamValue());

		
		
		executor.execute(new Runnable() {
			
			@Override
			public void run() {
				List<RepaymentBizPlanList> pLists = repaymentBizPlanListService.selectAutoRepayList(days);// 查询一个周期内(30天)要代扣的记录
//				for (RepaymentBizPlanList pList : pLists) {
//					if(pList.getPlanListId().equals("de6ee923-9df2-4e44-8f57-7abadf40d7e8")) {
//						System.out.println("STOP");
//					}
//					//获取该还款计划最早一期没有还的代扣
//					pList=rechargeService.getEarlyPeriod(pList);
//					// 是否符合自动代扣规则
//					if (rechargeService.EnsureAutoPayIsEnabled(pList, days).getCode().equals("1")) {
//						autoRepayPerList(pList,WithholdTypeEnum.AUTORUN.getValue().toString());
//					} else {
//						continue;
//					}
//				} 
				//把集合按planId分组
			    Map<String, List<RepaymentBizPlanList>> map =pLists.stream().collect(Collectors.groupingBy(RepaymentBizPlanList::getPlanId));
			    map.values().stream().forEach(lists -> {
			    	executor.execute(new Runnable() {
						@Override
						public void run() {
							for(RepaymentBizPlanList pList:lists) {
				        		//获取该还款计划最早一期没有还的代扣
				    			pList=rechargeService.getEarlyPeriod(pList);
				    				if((myRedisTemplate.get(pList.getPlanListId())==null||myRedisTemplate.get(pList.getPlanListId()).equals("0"))) {//没有被其他线程执行才能代扣
					    				myRedisTemplate.set(pList.getPlanListId(),"1");
					    				myRedisTemplate.expire(pList.getPlanListId(), 3600, TimeUnit.SECONDS);
					    				try {
					    				    // 是否符合自动代扣规则
							    			if (rechargeService.EnsureAutoPayIsEnabled(pList, days).getCode().equals("1")) {
							    				autoRepayPerList(pList,WithholdTypeEnum.AUTORUN.getValue().toString());
							    			} 
					    				}catch (Exception e) {
											logger.error("代扣出错 pListId:"+pList.getPlanListId());
										}finally {
											myRedisTemplate.set(pList.getPlanListId(),"0");//代扣结束
										}
					    			}
				    			}
				    		
				    			
				        	}					
					});
		        
		        });
			}
		});

		


	    
	}
	@Override
	public Result appWithholding(RepaymentBizPlanList pList) {
		List<SysParameter> repayStatusList = sysParameterService.selectList(new EntityWrapper<SysParameter>()
				.eq("param_type", SysParameterEnums.REPAY_DAYS.getKey()).eq("status", 1).orderBy("row_Index"));
		Integer days = Integer.valueOf(repayStatusList.get(0).getParamValue());
		if (rechargeService.EnsureAutoPayIsEnabled(pList, days).getCode().equals("1")) {
			Result result=autoRepayPerList(pList,WithholdTypeEnum.APPRUN.getValue().toString());
			return result;
		} else {
			return rechargeService.EnsureAutoPayIsEnabled(pList, days);
		}
		
	}
	/*
	 * 每一期自动代扣
	 * appType 渠道类型  
	 * app_run,auto_run,
	 */

	private Result autoRepayPerList(RepaymentBizPlanList pList,String appType) {
		Result result=new Result();
		BasicBusiness basic = basicBusinessService
				.selectOne(new EntityWrapper<BasicBusiness>().eq("business_id", pList.getOrigBusinessId()));
		List<BankCardInfo> bankCardInfos = rechargeService.getCustomerInfo(basic.getCustomerIdentifyCard());
		if(bankCardInfos==null) {
			logger.debug(
					"业务编号为" + pList.getOrigBusinessId() + "期数为" + pList.getAfterId() + "代扣失败，没有找到银行代扣和第三方代扣相关绑定信息");
			result.setCode("-1");
			result.setMsg("业务编号为" + pList.getOrigBusinessId() + "期数为" + pList.getAfterId() + "代扣失败，没有找到银行代扣和第三方代扣相关绑定信息");
			rechargeService.RecordExceptionLog(pList.getOrigBusinessId(), pList.getAfterId(), "代扣失败，没有找到银行代扣和第三方代扣相关绑定信息");
			return result;
		}
		BankCardInfo bankCardInfo = rechargeService.getBankCardInfo(bankCardInfos);
		BankCardInfo ThirtyCardInfo = rechargeService.getThirtyPlatformInfo(bankCardInfos);
		BigDecimal thirtyRepayAmount=getThirtyRepayAmount(pList);
		if (bankCardInfo != null&&thirtyRepayAmount.compareTo(BigDecimal.valueOf(0))==0) {
			//判断是否开启协议代扣开关
			SysParameter  aggreeSwitch = sysParameterService.selectOne(
					new EntityWrapper<SysParameter>().eq("param_type", "agreement_withholding")
							.eq("status", 1).orderBy("param_value"));
			if(aggreeSwitch.getParamValue().equals("1")) {
				if(bankCardInfo.getSignedProtocolList().size()>0) {
					// 银行代扣
					result=BankCharge(basic, bankCardInfo, pList, bankCardInfos,appType);
				}else if(ThirtyCardInfo != null&&bankCardInfo.getSignedProtocolList().size()==0) {
					result=ThirdRepaymentCharge(basic, ThirtyCardInfo, pList, null,appType);// 第三方代扣
				}
			}else {
				result=BankCharge(basic, bankCardInfo, pList, bankCardInfos,appType);
			}
		
			
		
		} else if (ThirtyCardInfo != null && (bankCardInfo == null||(bankCardInfo != null&&thirtyRepayAmount.compareTo(BigDecimal.valueOf(0))>0))) {// 第三方代扣
			result=ThirdRepaymentCharge(basic, ThirtyCardInfo, pList, null,appType);
		} else {
			logger.debug(
					"业务编号为" + pList.getOrigBusinessId() + "期数为" + pList.getAfterId() + "代扣失败，没有找到银行代扣和第三方代扣相关绑定信息");
			result.setCode("-1");
			result.setMsg("业务编号为" + pList.getOrigBusinessId() + "期数为" + pList.getAfterId() + "代扣失败，没有找到银行代扣和第三方代扣相关绑定信息");
			rechargeService.RecordExceptionLog(pList.getOrigBusinessId(), pList.getAfterId(), "代扣失败，没有找到银行代扣和第三方代扣相关绑定信息");
			return result;
		}
		return result;
	}

	/**
	 * 银行代扣
	 */
	private Result BankCharge(BasicBusiness basic, BankCardInfo bankCardInfo, RepaymentBizPlanList pList,
			List<BankCardInfo> bankCardInfos,String appType) {
		Result result=new Result();
		BigDecimal onlineAmount = rechargeService.getOnlineAmount(pList);
		BigDecimal underAmount = rechargeService.getUnderlineAmount(pList);
	
		
		Integer platformId = (Integer) PlatformEnum.YH_FORM.getValue();
		boolean isUseThirdRepay = false;// 是否调用第三方代扣
		// 获取所有银行代扣渠道,先扣线上费用
		List<WithholdingChannel> channels = withholdingChannelService
				.selectList(new EntityWrapper<WithholdingChannel>().eq("platform_id", PlatformEnum.YH_FORM.getValue())
						.eq("channel_status", 1).orderBy("channel_level"));
		
		//判断是否开启协议代扣开关
		SysParameter  aggreeSwitch = sysParameterService.selectOne(
				new EntityWrapper<SysParameter>().eq("param_type", "agreement_withholding")
						.eq("status", 1).orderBy("param_value"));
		if(aggreeSwitch.getParamValue().equals("1")) {//筛选协议代扣的通道和配置的通道的交集
			channels=rechargeService.getBankChannels(channels, bankCardInfo);
	    }
		
	
		if(channels.size()==0) {
			result.setCode("-1");
	    	result.setMsg("没有找到可用的银行代扣渠道");
			rechargeService.RecordExceptionLog(pList.getOrigBusinessId(), pList.getAfterId(), result.getMsg());
	    	return result;
		}

		outerloop: for(int j=0;j<channels.size();j++) {
        	SysBankLimit sysBankLimit = sysBankLimitService.selectOne(
    				new EntityWrapper<SysBankLimit>().eq("platform_id", channels.get(j).getPlatformId()).eq("sub_platform_id", channels.get(j).getSubPlatformId()).eq("bank_code", bankCardInfo.getBankCode().trim()).eq("status", 1));
    		if (sysBankLimit == null) {
    			if(j==channels.size()-1) {//说明是已经扣完最后一个渠道了
        			logger.debug("银行代扣限额信息platformId:" + channels.get(j).getPlatformId() + "无效/不存在");
        			rechargeService.RecordExceptionLog(pList.getOrigBusinessId(), pList.getAfterId(), "银行代扣限额信息platformId:\" + channel.getPlatformId() + \"无效/不存在\"");
				}else {
					continue;
				}
    		} else {
    			// 本期线上剩余应还金额,剩余应还金额减去线下金额
    			BigDecimal repayMoney = rechargeService.getRestAmount(pList).subtract(underAmount);
    			logger.info("期线上剩余应还金额:" + repayMoney);
    			// 应还金额大于0
    			if (repayMoney.compareTo(BigDecimal.valueOf(0)) > 0) {
    				// 当第一次扣款额度没有超过银行每次的扣款额度时候，尝试一次性扣款
    				if (sysBankLimit.getHasOnceLimit() == 0 || sysBankLimit.getOnceLimit().compareTo(repayMoney) >= 0) {
    					Integer boolPartRepay = 0;// 表示本期是否分多笔代扣,0:一次性代扣，1:分多笔代扣
    					Integer boolLastRepay = 1;// 表示本期是否分多笔代扣中的最后一笔代扣 0:非最后一笔代扣，1:最后一笔代扣
    					// 计算本来这一期线上费用是否需要多期扣款
    					if (onlineAmount.subtract(sysBankLimit.getOnceLimit()).compareTo(BigDecimal.valueOf(0)) > 0) {
    						boolPartRepay = 1;
    					}
    					 result = rechargeService.recharge(basic, pList, repayMoney.doubleValue(), boolLastRepay,
    							boolPartRepay, bankCardInfo, channels.get(j),appType);
    					if (result.getCode().equals("1")) {
    						// 成功跳出
    						// rechargeService.recordRepaymentLog(result, pList, basic,
    						// bankCardInfo,platformId, 1, boolPartRepay, merchOrderId, 0,
    						// repayMoney);
    						break;
    					} else if(result.getCode().equals("2")){
    						result.setCode("-1");
							result.setMsg("银行代扣处理中");
							rechargeService.RecordExceptionLog(pList.getOrigBusinessId(), pList.getAfterId(), "银行代扣处理中");
							return result;
    					}else {
    						// 如果是余额不足，则跳出循环
    						if (IsNoEnoughMoney(result.getMsg())) {
    							result.setCode("-1");
    							result.setMsg("余额不足");
    							rechargeService.RecordExceptionLog(pList.getOrigBusinessId(), pList.getAfterId(), "银行卡余额不足");
    							return result;
    						} else {
    							if(j==channels.size()-1) {//说明是已经扣完最后一个渠道了
    								isUseThirdRepay = true;
    								break outerloop;
    							}else {
    								continue;
    							}
    						
    						
    						}
    					}

    					// 分多笔代扣
    				} else {
    					if(j==channels.size()-1) {//说明是已经扣完最后一个渠道了
							isUseThirdRepay = true;
							break outerloop;
						}else {
							continue;
						}
    					/**
    					 * 
    					 
    					// 获取代扣每次最高额
    					BigDecimal eachMax = sysBankLimit.getOnceLimit();
    					// 代扣次数
    					Integer repayCount = repayMoney.divide(eachMax, RoundingMode.CEILING).intValue();
    					// 余数
    					BigDecimal remainder = repayMoney.divideAndRemainder(eachMax)[1];
    					if(remainder.compareTo(BigDecimal.valueOf(0)) > 0) {
    						repayCount=repayCount+1;
    					}
    					logger.info("本次代扣每笔限额:{0}代扣次数:{1}余数:{2}" ,eachMax,repayCount,remainder);					
    					int last = repayCount - 1;
    					Integer boolPartRepay = 1;// 表示本期是否分多笔代扣,0:一次性代扣，1:分多笔代扣
    					Integer boolLastRepay = 0;// 表示本期是否分多笔代扣中的最后一笔代扣，若非多笔代扣，本字段存1。 0:非最后一笔代扣，1:最后一笔代扣
    					for (int i = 0; i < repayCount; i++) {
    						BigDecimal currentAmount = BigDecimal.valueOf(0);// 本次代扣金额
    						if (i == last && remainder.compareTo(BigDecimal.valueOf(0)) > 0) {
    							boolLastRepay = 1;
    							currentAmount = remainder;
    						} else {
    							currentAmount = eachMax;
    						}
    					
    						 result = rechargeService.recharge(basic, pList, currentAmount.doubleValue(),
    								boolLastRepay, boolPartRepay, bankCardInfo, channels.get(i),appType);
    						if (result.getCode().equals("1")) {
    							if (i == last) {// 说明是最后一次代扣
    								break outerloop;
    							} else {

    								continue;
    							}

    						} else {
    							// 如果是余额不足，则跳出最外层循环
    							if (IsNoEnoughMoney(result.getMsg())) {
    								result.setCode("-1");
    								result.setMsg("余额不足");
    								rechargeService.RecordExceptionLog(pList.getOrigBusinessId(), pList.getAfterId(), "银行卡余额不足");
    								return result;
    							} else {
    								// 第一次银行代扣就失败就调用第三方平台的
    								if (i == 0&&j==channels.size()) {
    									isUseThirdRepay = true;
    									break;
    								} else {
    									// 非第一次银行代扣失败就跳出循环，不能混合代扣

    									break;
    								}

    							}
    						}
    					}
    					 */
    				}
    			}
    		}
        }
	

		BankCardInfo thirtyCardInfo = rechargeService.getThirtyPlatformInfo(bankCardInfos);
		// 第一次银行代扣就失败,切换第三方代扣
		if (isUseThirdRepay) {
			Integer successCount = rechargeService.getBankRepaySuccessCount(pList);
			if (successCount > 0) {// 如果当前期在银行代扣成功过,就不能用第三方代扣,直接条出
				result.setCode("-1");
				result.setMsg("当前期在银行代扣存在成功记录或处理中记录,暂不能用第三方代扣");
				rechargeService.RecordExceptionLog(pList.getOrigBusinessId(), pList.getAfterId(), result.getMsg());
				return result;
			}

			if (thirtyCardInfo != null) {// 绑定了第三方平台
				 result=ThirdRepaymentCharge(basic, thirtyCardInfo, pList, null,appType);

			} else { // 没有绑定第三方平台直接跳出
				result.setCode("-1");
				result.setMsg("没有绑定第三方平台");
				rechargeService.RecordExceptionLog(pList.getOrigBusinessId(), pList.getAfterId(), "没有绑定第三方平台");
				return result;
			}

			// 银行代扣成功，线下逾期费用调用第三方代扣
		} else {

			if (thirtyCardInfo != null) {// 绑定了第三方平台
				if (underAmount.compareTo(BigDecimal.valueOf(0)) > 0) {
					if(!rechargeService.isForgiveDayOutside(pList)) {//判断是否在宽限期内，如果是则不代扣线下滞纳金,
						underAmount=BigDecimal.valueOf(0);
						result.setCode("-1");
						result.setMsg("在宽限期内,不代扣线下滞纳金");
						rechargeService.RecordExceptionLog(pList.getOrigBusinessId(), pList.getAfterId(), result.getMsg());
						return result;
					}else {
						result=ThirdRepaymentCharge(basic, thirtyCardInfo, pList, underAmount,appType);
					}
				}
			
			} else { // 没有绑定第三方平台直接跳出
				result.setCode("-1");
				result.setMsg("没有绑定第三方平台");
				rechargeService.RecordExceptionLog(pList.getOrigBusinessId(), pList.getAfterId(), "没有绑定第三方平台");
				return result;
			}
		}
		return result;

	}

	/**
	 * 第三方代扣 underAmount不为空的话，说明只扣线下费用
	 * 如果handRepayAmount不为空的话，说明是手工代扣
	 */
	private Result ThirdRepaymentCharge(BasicBusiness basic, BankCardInfo thirtyCardInfo, RepaymentBizPlanList pList,
			BigDecimal underAmount,String appType) {
		
		
		Result result=new Result();
		// 获取所有第三方代扣渠道
		List<WithholdingChannel> channels = withholdingChannelService
				.selectList(new EntityWrapper<WithholdingChannel>().ne("platform_id", PlatformEnum.YH_FORM.getValue())
						.eq("channel_status", 1).orderBy("channel_level"));
		
		if(channels.size()==0) {
			result.setCode("-1");
	    	result.setMsg("没有找到可用的第三方代扣渠道");
			rechargeService.RecordExceptionLog(pList.getOrigBusinessId(), pList.getAfterId(), result.getMsg());
	    	return result;
		}
		List<ThirdPlatform> thirdPlatforms = thirtyCardInfo.getThirdPlatformList();

		List<WithholdingChannel> newChanels = new ArrayList();
		// 筛选出客户绑定的第三方平台列表在系统渠道表种生效的有哪几个
		for (int i = 0; i < channels.size(); i++) {
			for (int j = 0; j < thirdPlatforms.size(); j++) {
				if (channels.get(i).getPlatformId() == thirdPlatforms.get(j).getPlatformID()) {
					newChanels.add(channels.get(i));
				}
			}
		}
		outerloop: for (WithholdingChannel channel : newChanels) {
			SysBankLimit sysBankLimit = sysBankLimitService.selectOne(
					new EntityWrapper<SysBankLimit>().eq("platform_id", channel.getPlatformId()).eq("status", 1).eq("bank_code", thirtyCardInfo.getBankCode().trim()));
			if (sysBankLimit == null) {
				logger.debug("第三方代扣限额信息platformId:" + channel.getPlatformId() + "无效/不存在");
				continue;
			} else {

				// 本期剩余应还金额
				BigDecimal repayMoney = rechargeService.getRestAmount(pList);

				if(!rechargeService.isForgiveDayOutside(pList)) {//判断是否在宽限期外，如果是,要扣线下滞纳金,否则不扣
					BigDecimal amount = rechargeService.getUnderlineAmount(pList);
					repayMoney=repayMoney.subtract(amount);
				}
				if (underAmount != null && underAmount.compareTo(BigDecimal.valueOf(0)) > 0) {//如果UnderAmount不等于NULL,说明是银行代扣完之后完过来代扣线下滞纳金的
					repayMoney = underAmount;
				}
				
				logger.info("期线上剩余应还金额:" + repayMoney);
				// 如果应还金额大于0
				if (repayMoney.compareTo(BigDecimal.valueOf(0)) > 0) {

					// 当第一次扣款额度没有超过银行每次的扣款额度时候，尝试一次性扣款
					if (sysBankLimit.getHasOnceLimit() == 0 || sysBankLimit.getOnceLimit().compareTo(repayMoney) > 0) {
						Integer boolPartRepay = 0;// 表示本期是否分多笔代扣,0:一次性代扣，1:分多笔代扣
						Integer boolLastRepay = 1;// 表示本期是否分多笔代扣中的最后一笔代扣，若非多笔代扣，本字段存1。 0:非最后一笔代扣，1:最后一笔代扣
						if (pList.getTotalBorrowAmount().add(pList.getOverdueAmount()==null?BigDecimal.valueOf(0):pList.getOverdueAmount())
								.subtract(sysBankLimit.getOnceLimit()).compareTo(BigDecimal.valueOf(0)) > 0) {
							boolPartRepay = 1;

						}
						 result = rechargeService.recharge(basic, pList, repayMoney.doubleValue(), boolLastRepay,
								boolPartRepay, thirtyCardInfo, channel,appType);
						if (result.getCode().equals("1")) {

							break;
						} else {
							// 如果是余额不足，则跳出循环
							if (IsNoEnoughMoney(result.getMsg())) {
								result.setCode("-1");
								result.setMsg("余额不足");
								rechargeService.RecordExceptionLog(pList.getOrigBusinessId(), pList.getAfterId(), "银行卡余额不足");
								break;
							} else {

								continue;
							}
						}

						// 第三方代扣，非一次性扣款
					} else {
						
						// 获取代扣每次最高额
						BigDecimal eachMax = sysBankLimit.getOnceLimit();
						// 代扣次数
						Integer repayCount = repayMoney.divide(eachMax, RoundingMode.FLOOR).intValue();
						// 余数
						
						BigDecimal remainder = repayMoney.divideAndRemainder(eachMax)[1];
						logger.info("本次代扣每笔限额:{0}代扣次数:{1}余数:{2}" ,eachMax,repayCount,remainder);	
						if(remainder.compareTo(BigDecimal.valueOf(0)) > 0) {
							repayCount=repayCount+1;
						}
						int last = repayCount - 1;
						Integer boolPartRepay = 1;// 表示本期是否分多笔代扣,0:一次性代扣，1:分多笔代扣
						Integer boolLastRepay = 0;// 表示本期是否分多笔代扣中的最后一笔代扣，若非多笔代扣，本字段存1。 0:非最后一笔代扣，1:最后一笔代扣
						for (int i = 0; i < repayCount; i++) {
							BigDecimal currentAmount = BigDecimal.valueOf(0);// 本次代扣金额
							if (i == last && remainder.compareTo(BigDecimal.valueOf(0)) > 0) {
								currentAmount = remainder;
								boolLastRepay = 1;
							} else {
								currentAmount = eachMax;
							}
							 result = rechargeService.recharge(basic, pList, currentAmount.doubleValue(),
									boolLastRepay, boolPartRepay, thirtyCardInfo, channel,appType);
							if (result.getCode().equals("1")) {
								if (i == last) {// 说明是最后一次代扣

									break outerloop;
								} else {

									continue;
								}

							} else {
								// 如果是余额不足，则跳出最外层循环
								if (IsNoEnoughMoney(result.getMsg())) {
									// TODO,可能还要作这样的操作：扣除线下费用再尝试代扣一次
									result.setCode("-1");
									result.setMsg("余额不足");
									rechargeService.RecordExceptionLog(pList.getOrigBusinessId(), pList.getAfterId(), "银行卡余额不足");
									break outerloop;
								} else {

									break;
								}
							}
						}

					}
				}

			}
		}
		return result;
	}

	private boolean IsNoEnoughMoney(String errMessage) {
		boolean isNoEnoughMoney = false;
		if (!StringUtil.isEmpty(errMessage)) {
			if (errMessage.contains("资金不足") || errMessage.contains("余额不足")) {
				isNoEnoughMoney = true;
			}
		}
		return isNoEnoughMoney;
	}

	@Override
	public Result handBankRecharge(BasicBusiness basic, BankCardInfo bankCardInfo, RepaymentBizPlanList pList,
			 BigDecimal handRepayMoney) {
		
		
		Result result=new Result();
	
				BigDecimal onlineAmount = rechargeService.getOnlineAmount(pList);
				BigDecimal underAmount = rechargeService.getUnderlineAmount(pList);
				Integer platformId = (Integer) PlatformEnum.YH_FORM.getValue();
				// 获取所有银行代扣渠道,先扣线上费用
				List<WithholdingChannel> channels = withholdingChannelService
						.selectList(new EntityWrapper<WithholdingChannel>().eq("platform_id", PlatformEnum.YH_FORM.getValue())
								.eq("channel_status", 1).orderBy("channel_level"));
				
				//判断是否开启协议代扣开关
				SysParameter  aggreeSwitch = sysParameterService.selectOne(
						new EntityWrapper<SysParameter>().eq("param_type", "agreement_withholding")
								.eq("status", 1).orderBy("param_value"));
				if(aggreeSwitch.getParamValue().equals("1")) {//筛选协议代扣的通道和配置的通道的交集
					channels=rechargeService.getBankChannels(channels, bankCardInfo);
			    }
				
				if(channels.size()==0) {
					result.setCode("-1");
			    	result.setMsg("没有找到可用的银行代扣渠道");
					rechargeService.RecordExceptionLog(pList.getOrigBusinessId(), pList.getAfterId(), result.getMsg());
			    	return result;
				}
//				WithholdingChannel channel = null;
//				if (channels != null && channels.size() > 0) {
//					channel = channels.get(0);
//				}
		        for(int j=0;j<channels.size();j++) {
		        	SysBankLimit sysBankLimit = sysBankLimitService.selectOne(
							new EntityWrapper<SysBankLimit>().eq("platform_id", channels.get(j).getPlatformId()).eq("bank_code", bankCardInfo.getBankCode().trim()).eq("sub_platform_id", channels.get(j).getSubPlatformId()).eq("status", 1));
					if (sysBankLimit == null) {
						if(j==channels.size()-1) {//说明是已经扣完最后一个渠道了
							logger.info("银行代扣限额信息platformId:{0},bankCode:{1},无效/不存在",channels.get(j).getPlatformId(),bankCardInfo.getBankName());
							result.setCode("-1");
					    	result.setMsg("银行代扣限额信息platformId:"+channels.get(j).getPlatformId()+",bankCode:"+bankCardInfo.getBankName()+",无效/不存在");
							return result;
						}else {
							continue;
						}
					} else {
						// 本期线上剩余应还金额,剩余应还金额减去线下金额
						BigDecimal repayMoney = rechargeService.getRestAmount(pList).subtract(underAmount);
					    if(repayMoney.compareTo(BigDecimal.valueOf(0))==0) {
					    	result.setCode("-1");
					    	result.setMsg("本期线上剩余应还金额为0,不能代扣");
					    }
						if(handRepayMoney.compareTo(repayMoney)<0) {//如果手工代扣的金额大于剩余未还得金额，则取剩余未还得金额，否则取手工代扣的金额
							repayMoney=handRepayMoney;
						}
				
						// 应还金额大于0
						if (repayMoney.compareTo(BigDecimal.valueOf(0)) > 0) {
							// 当第一次扣款额度没有超过银行每次的扣款额度时候，尝试一次性扣款
							if (sysBankLimit.getHasOnceLimit() == 0 || sysBankLimit.getOnceLimit().compareTo(repayMoney) >= 0) {
								Integer boolPartRepay = 0;// 表示本期是否分多笔代扣,0:一次性代扣，1:分多笔代扣
								Integer boolLastRepay = 1;// 表示本期是否分多笔代扣中的最后一笔代扣 0:非最后一笔代扣，1:最后一笔代扣
								// 计算本来这一期线上费用是否需要多期扣款
								if (onlineAmount.subtract(sysBankLimit.getOnceLimit()).compareTo(BigDecimal.valueOf(0)) > 0) {
									boolPartRepay = 1;
								}
								 result = rechargeService.recharge(basic, pList, repayMoney.doubleValue(), boolLastRepay,
										boolPartRepay, bankCardInfo, channels.get(j),"");
								if (result.getCode().equals("1")) {
									// 成功跳出
									// rechargeService.recordRepaymentLog(result, pList, basic,
									// bankCardInfo,platformId, 1, boolPartRepay, merchOrderId, 0,
									// repayMoney);
									return result;
								} else if(result.getCode().equals("2")){
		    						result.setCode("1");
									result.setMsg("银行代扣处理中");
									rechargeService.RecordExceptionLog(pList.getOrigBusinessId(), pList.getAfterId(), "银行代扣处理中");
									return result;
		    					}else {
									// 如果是余额不足，则跳出
									if (IsNoEnoughMoney(result.getMsg())) {
										return result;
									}else {
		    							if(j==channels.size()-1) {//说明是已经扣完最后一个渠道了
		    								break;
		    							}else {
		    								continue;
		    							}
		    						
		    						
		    						}
								}
			
								// 分多笔代扣
							} else {
							
								if(j==channels.size()-1) {//说明是已经扣完最后一个渠道了
									result.setCode("-1");
									result.setMsg("本次代扣金额超过单笔限额");
									return result;
    							}else {
    								continue;
    							}
								/**
								 * 
								
								// 获取代扣每次最高额
								BigDecimal eachMax = sysBankLimit.getOnceLimit();
								// 代扣次数
								Integer repayCount = repayMoney.divide(eachMax, RoundingMode.CEILING).intValue();
								// 余数
								BigDecimal remainder = repayMoney.divideAndRemainder(eachMax)[1];
								if(remainder.compareTo(BigDecimal.valueOf(0)) > 0) {
									repayCount=repayCount+1;
								}
								int last = repayCount - 1;
								Integer boolPartRepay = 1;// 表示本期是否分多笔代扣,0:一次性代扣，1:分多笔代扣
								Integer boolLastRepay = 0;// 表示本期是否分多笔代扣中的最后一笔代扣，若非多笔代扣，本字段存1。 0:非最后一笔代扣，1:最后一笔代扣
								for (int i = 0; i < repayCount; i++) {
									BigDecimal currentAmount = BigDecimal.valueOf(0);// 本次代扣金额
									if (i == last && remainder.compareTo(BigDecimal.valueOf(0)) > 0) {
										boolLastRepay = 1;
										currentAmount = remainder;
									} else {
										currentAmount = eachMax;
									}
									 result = rechargeService.recharge(basic, pList, currentAmount.doubleValue(),
											boolLastRepay, boolPartRepay, bankCardInfo, channels.get(j),"");
									if (result.getCode().equals("1")) {
										if (i == last) {// 说明是最后一次代扣
											break;
										} else {
			
											continue;
										}
			
									} else {
											return result;
									}
								}
			 */
							}
						}
					}
		        }
			
	
		return result;
	}

	@Override
	public Result handThirdRepaymentCharge(BasicBusiness basic, BankCardInfo thirtyCardInfo, RepaymentBizPlanList pList,
			 Integer platformId,BigDecimal handRepayAmount) {
                    Result result=new Result();
                    
		// 获取所有第三方代扣渠道
				List<WithholdingChannel> channels = withholdingChannelService
						.selectList(new EntityWrapper<WithholdingChannel>().eq("platform_id",platformId)
								.eq("channel_status", 1).orderBy("channel_level"));
				if(channels.size()==0) {
					result.setCode("-1");
			    	result.setMsg("没有找到可用的第三方代扣渠道");
			    	return result;
				}
				List<ThirdPlatform> thirdPlatforms = thirtyCardInfo.getThirdPlatformList();

				List<WithholdingChannel> newChanels = new ArrayList();
				// 筛选出客户绑定的第三方平台列表在系统渠道表种生效的有哪几个
				for (int i = 0; i < channels.size(); i++) {
					for (int j = 0; j < thirdPlatforms.size(); j++) {
						if (channels.get(i).getPlatformId() == thirdPlatforms.get(j).getPlatformID()) {
							newChanels.add(channels.get(i));
						}
					}
				}
				if(newChanels.size()==0) {
					result.setCode("-1");
					result.setMsg("找不到客户绑定的代扣平台");
					logger.info("找不到客户绑定的代扣平台");
					return result;
				}
				outerloop: for (WithholdingChannel channel : newChanels) {
					SysBankLimit sysBankLimit = sysBankLimitService.selectOne(
							new EntityWrapper<SysBankLimit>().eq("platform_id", channel.getPlatformId()).eq("status", 1).eq("bank_code", thirtyCardInfo.getBankCode()));
					if (sysBankLimit == null) {
						logger.info("第三方代扣限额信息platformId:{0},bankCode:{1},无效/不存在",channel.getPlatformId(),thirtyCardInfo.getBankName());
						result.setCode("-1");
					 	result.setMsg("第三方代扣限额信息platformId:"+channel.getPlatformId()+",bankCode:"+thirtyCardInfo.getBankName()+",无效/不存在");
						continue;
					} else {

						// 本期剩余应还金额
						BigDecimal repayMoney = rechargeService.getRestAmount(pList);
					    if(repayMoney.compareTo(BigDecimal.valueOf(0))==0) {
					    	result.setCode("-1");
					    	result.setMsg("本期剩余应还金额为0,不能代扣");
					    }
						
						if(handRepayAmount.compareTo(repayMoney)<0) {//如果手工代扣金额小于剩余应还金额，取手工代扣的金额
							repayMoney=handRepayAmount;
						}
						// 如果应还金额大于0
						if (repayMoney.compareTo(BigDecimal.valueOf(0)) > 0) {

							// 当第一次扣款额度没有超过银行每次的扣款额度时候，尝试一次性扣款
							if (sysBankLimit.getHasOnceLimit() == 0 || sysBankLimit.getOnceLimit().compareTo(repayMoney) > 0) {
								Integer boolPartRepay = 0;// 表示本期是否分多笔代扣,0:一次性代扣，1:分多笔代扣
								Integer boolLastRepay = 1;// 表示本期是否分多笔代扣中的最后一笔代扣，若非多笔代扣，本字段存1。 0:非最后一笔代扣，1:最后一笔代扣
								if (pList.getTotalBorrowAmount().add(pList.getOverdueAmount()==null?BigDecimal.valueOf(0):pList.getOverdueAmount())
										.subtract(sysBankLimit.getOnceLimit()).compareTo(BigDecimal.valueOf(0)) > 0) {
									boolPartRepay = 1;

								}
								 result = rechargeService.recharge(basic, pList, repayMoney.doubleValue(), boolLastRepay,
										boolPartRepay, thirtyCardInfo, channel,"");
								if (result.getCode().equals("1")) {

									break;
								} else {
									// 如果是余额不足，则跳出循环
									if (IsNoEnoughMoney(result.getMsg())) {
										break;
									} else {

										continue;
									}
								}

								// 第三方代扣，非一次性扣款
							} else {
								// 获取代扣每次最高额
								BigDecimal eachMax = sysBankLimit.getOnceLimit();
								// 代扣次数
								Integer repayCount = repayMoney.divide(eachMax, RoundingMode.FLOOR).intValue();
								// 余数
								BigDecimal remainder = repayMoney.divideAndRemainder(eachMax)[1];
								if(remainder.compareTo(BigDecimal.valueOf(0)) > 0) {
									repayCount=repayCount+1;
								}
								int last = repayCount - 1;
								Integer boolPartRepay = 1;// 表示本期是否分多笔代扣,0:一次性代扣，1:分多笔代扣
								Integer boolLastRepay = 0;// 表示本期是否分多笔代扣中的最后一笔代扣，若非多笔代扣，本字段存1。 0:非最后一笔代扣，1:最后一笔代扣
								for (int i = 0; i < repayCount; i++) {
									BigDecimal currentAmount = BigDecimal.valueOf(0);// 本次代扣金额
									if (i == last && remainder.compareTo(BigDecimal.valueOf(0)) > 0) {
										currentAmount = remainder;
										boolLastRepay = 1;
									} else {
										currentAmount = eachMax;
									}
									 result = rechargeService.recharge(basic, pList, currentAmount.doubleValue(),
											boolLastRepay, boolPartRepay, thirtyCardInfo, channel,"");
									if (result.getCode().equals("1")) {
										if (i == last) {// 说明是最后一次代扣

											break outerloop;
										} else {

											continue;
										}

									} else {
										// 如果是余额不足，则跳出最外层循环
										if (IsNoEnoughMoney(result.getMsg())) {
											// TODO,可能还要作这样的操作：扣除线下费用再尝试代扣一次

											break outerloop;
										} else {

											break;
										}
									}
								}

							}
						}

					}
				}
				return result;
		
	}

   private BigDecimal getThirtyRepayAmount(RepaymentBizPlanList pList) {
	   BigDecimal repayAmount=BigDecimal.valueOf(0);
	   List<WithholdingRepaymentLog> logs=withholdingRepaymentLogService.selectList(new EntityWrapper<WithholdingRepaymentLog>().eq("original_business_id", pList.getOrigBusinessId()).ne("bind_platform_id",PlatformEnum.YH_FORM).ne("repay_status", 0).eq("after_id", pList.getAfterId()));
	   for(WithholdingRepaymentLog log:logs) {
		   repayAmount=repayAmount.add(log.getCurrentAmount()==null?BigDecimal.valueOf(0):log.getCurrentAmount());
	   }
	return repayAmount;
   }
   
   public static void main(String[] args) {
	  int count= BigDecimal.valueOf(12218.90).divide(BigDecimal.valueOf(650.00), RoundingMode.FLOOR).intValue();
		BigDecimal remainder = BigDecimal.valueOf(12218.90).divideAndRemainder(BigDecimal.valueOf(650.00))[1];
	  System.out.println(remainder);
}
}
