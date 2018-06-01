package com.hongte.alms.withhold.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.SysBankLimit;
import com.hongte.alms.base.entity.WithholdingChannel;
import com.hongte.alms.base.enums.PlatformEnum;
import com.hongte.alms.base.feignClient.dto.CustomerInfoDto;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.base.service.SysBankLimitService;
import com.hongte.alms.base.service.WithholdingChannelService;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.withhold.service.RechargeService;
import com.hongte.alms.withhold.service.WithholdingService;

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
	@Qualifier("basicBusinessService")
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

	@Override
	public void withholding() {
		List<RepaymentBizPlanList> pLists = repaymentBizPlanListService.selectAutoRepayList();
		for (RepaymentBizPlanList pList : pLists) {
			// 不是最后一期才能代扣
			if (!rechargeService.istLastPeriod(pList)) {

			} else {
				continue;
			}
		}
	}

	/*
	 * 每一期自动代扣
	 */

	private void autoRepayPerList(RepaymentBizPlanList pList) {

		BasicBusiness basic = basicBusinessService
				.selectOne(new EntityWrapper<BasicBusiness>().eq("business_id", pList.getOrigBusinessId()));
		CustomerInfoDto customerDto = rechargeService.getCustomerInfo(basic.getCustomerIdentifyCard());
		if (Integer.valueOf(customerDto.getPlatformId()) != PlatformEnum.YH_FORM.getValue()) {
			// 第三方代扣
			ThirdRepaymentCharge(basic, customerDto, pList,null);
		}else {
			//银行代扣
			BankCharge(basic, customerDto, pList);
		}
	}
	/**
	 * 银行代扣
	 */
	private void BankCharge(BasicBusiness basic, CustomerInfoDto customerDto, RepaymentBizPlanList pList) {
		BigDecimal onlineAmount=rechargeService.getOnlineAmount(pList);
		BigDecimal underAmount=rechargeService.getUnderlineAmount(pList);
		boolean isUseThirdRepay=false;//是否调用第三方代扣
		// 获取所有银行代扣渠道,先扣线上费用
		List<WithholdingChannel> channels = withholdingChannelService.selectList(new EntityWrapper<WithholdingChannel>()
				.eq("platform_id", PlatformEnum.YH_FORM.getValue()).eq("channel_status", 1).orderBy("level"));
			outerloop: for (WithholdingChannel channel : channels) {
				SysBankLimit sysBankLimit = sysBankLimitService.selectOne(
						new EntityWrapper<SysBankLimit>().eq("platform_id", channel.getPlatformId()).eq("status", 1));
				if (sysBankLimit == null) {
					logger.debug("第三方代扣限额信息platformId:" + channel.getPlatformId() + "无效/不存在");
					continue;
				}else {
					// 本期线上剩余应还金额,剩余应还金额减去线下金额
					BigDecimal repayMoney = rechargeService.getRestAmount(pList).subtract(underAmount);
							
					// 应还金额大于0
					if (repayMoney.compareTo(BigDecimal.valueOf(0)) > 0) {
						// 当第一次扣款额度没有超过银行每次的扣款额度时候，尝试一次性扣款
						if (sysBankLimit.getHasOnceLimit() == 0 || sysBankLimit.getOnceLimit().compareTo(repayMoney) > 0) {
							String merchOrderId = rechargeService.getMerchOrderId();// 获取商户订单号
							Integer boolPartRepay = 0;// 表示本期是否分多笔代扣,0:一次性代扣，1:分多笔代扣
							if (onlineAmount.subtract(sysBankLimit.getOnceLimit()).compareTo(BigDecimal.valueOf(0)) > 0) {
								boolPartRepay = 1;
							}
							Result result = rechargeService.recharge(basic.getBusinessId(), pList.getAfterId(),
									customerDto.getBankBindCardNo(), repayMoney.doubleValue(), customerDto.getPlatformId(),
									merchOrderId);
							if (result.getCode().equals("1")) {
								rechargeService.recordRepaymentLog(result, pList, basic, customerDto,
										channel.getPlatformId(), 1, boolPartRepay, merchOrderId, 0,
										repayMoney);
								break;
							} else {
								// 如果是余额不足，则跳出循环
								if (IsNoEnoughMoney(result.getMsg())) {
									break;
								} else {
									rechargeService.recordRepaymentLog(result, pList, basic, customerDto,
											channel.getPlatformId(), 1, boolPartRepay, merchOrderId, 0,
											repayMoney);
									isUseThirdRepay=true;
									break;
								}
							}
							
							//分多笔代扣
						}else {
							// 获取代扣每次最高额
							BigDecimal eachMax = sysBankLimit.getOnceLimit();
							// 代扣次数
							Integer repayCount = repayMoney.divide(eachMax, RoundingMode.CEILING).intValue();
							// 余数
							BigDecimal remainder = repayMoney.divideAndRemainder(eachMax)[1];
							int last = repayCount - 1;
							Integer boolPartRepay = 1;// 表示本期是否分多笔代扣,0:一次性代扣，1:分多笔代扣
							Integer boolLastRepay = 0;// 表示本期是否分多笔代扣中的最后一笔代扣，若非多笔代扣，本字段存1。 0:非最后一笔代扣，1:最后一笔代扣
							for (int i = 0; i < repayCount; i++) {
								BigDecimal currentAmount=BigDecimal.valueOf(0);//本次代扣金额
								if(i==last&&remainder.compareTo(BigDecimal.valueOf(0))>0) {
									currentAmount=remainder;
								}else {
									currentAmount=eachMax;
								}
								String merchOrderId = rechargeService.getMerchOrderId();// 获取商户订单号
								Result result = rechargeService.recharge(basic.getBusinessId(), pList.getAfterId(),
										customerDto.getBankBindCardNo(), currentAmount.doubleValue(),
										customerDto.getPlatformId(), merchOrderId);
								if (result.getCode().equals("1")) {
									if(i==last) {//说明是最后一次代扣
										boolLastRepay=1;
										rechargeService.recordRepaymentLog(result, pList, basic, customerDto,
												channel.getPlatformId(), boolLastRepay, boolPartRepay, merchOrderId, 0,
												currentAmount);
									   break outerloop;
									}else {
									rechargeService.recordRepaymentLog(result, pList, basic, customerDto,
											channel.getPlatformId(), boolLastRepay, boolPartRepay, merchOrderId, 0,
											currentAmount);
									   continue;
									}

								} else {
									// 如果是余额不足，则跳出最外层循环
									if (IsNoEnoughMoney(result.getMsg())) {
										// TODO,可能还要作这样的操作：扣除线下费用再尝试代扣一次
										rechargeService.recordRepaymentLog(result, pList, basic, customerDto,
												channel.getPlatformId(), 1, boolPartRepay, merchOrderId, 0,
												currentAmount);
										break outerloop;
									} else {
										//第一次银行代扣就失败就调用第三方平台的
										if(i==0) {
											isUseThirdRepay=true;
											rechargeService.recordRepaymentLog(result, pList, basic, customerDto,
													channel.getPlatformId(), 1, boolPartRepay, merchOrderId, 0,
													currentAmount);
										
										}else {
											//非第一次银行代扣失败就跳出循环，不能混合代扣
											rechargeService.recordRepaymentLog(result, pList, basic, customerDto,
													channel.getPlatformId(), 1, boolPartRepay, merchOrderId, 0,
													currentAmount);
											break outerloop;
										}
									
									}
								}
							}
							
						}
					}
				}
				
			}
		

		//调用第三方代扣,调用后面逻辑不执行
		if(isUseThirdRepay) {
			Integer successCount=rechargeService.getBankRepaySuccessCount(pList);
			if(successCount>0) {//如果当前期在银行代扣成功过,就不能用第三方代扣,直接条出
				return;
			}
			if(customerDto.getIsBindThirdParty()==1) {//绑定了第三方平台
				if(underAmount.compareTo(BigDecimal.valueOf(0))>0) {
					ThirdRepaymentCharge(basic, customerDto, pList, underAmount);
				}else {	//没有线下逾期费用直接跳出		
					return;
				}
		   	
			}else {  //没有绑定第三方平台直接跳出
				return;
			}
		}
		
		
	
		
	}
	
	
	
	
	/**
	 * 第三方代扣
	 */
	private void ThirdRepaymentCharge(BasicBusiness basic, CustomerInfoDto customerDto, RepaymentBizPlanList pList,BigDecimal underAmount) {

		// 获取所有第三方代扣渠道
		List<WithholdingChannel> channels = withholdingChannelService.selectList(new EntityWrapper<WithholdingChannel>()
				.ne("platform_id", PlatformEnum.YH_FORM.getValue()).eq("channel_status", 1).orderBy("level"));
		WithholdingChannel temp = new WithholdingChannel();
		temp.setPlatformId(Integer.valueOf(customerDto.getPlatformId()));
		channels.add(0, temp);
		outerloop: for (WithholdingChannel channel : channels) {
			SysBankLimit sysBankLimit = sysBankLimitService.selectOne(
					new EntityWrapper<SysBankLimit>().eq("platform_id", channel.getPlatformId()).eq("status", 1));
			if (sysBankLimit == null) {
				logger.debug("第三方代扣限额信息platformId:" + channel.getPlatformId() + "无效/不存在");
				continue;
			} else {
				
				// 本期剩余应还金额
				BigDecimal repayMoney = pList.getTotalBorrowAmount().add(pList.getOverdueAmount())
						.subtract(rechargeService.getRestAmount(pList));
				
				if(underAmount!=null&&underAmount.compareTo(BigDecimal.valueOf(0))>0) {
					repayMoney=underAmount;
				}
				// 如果应还金额大于0
				if (repayMoney.compareTo(BigDecimal.valueOf(0)) > 0) {

					// 当第一次扣款额度没有超过银行每次的扣款额度时候，尝试一次性扣款
					if (sysBankLimit.getHasOnceLimit() == 0 || sysBankLimit.getOnceLimit().compareTo(repayMoney) > 0) {
						String merchOrderId = rechargeService.getMerchOrderId();// 获取商户订单号
						Integer boolPartRepay = 0;// 表示本期是否分多笔代扣,0:一次性代扣，1:分多笔代扣
						if (pList.getTotalBorrowAmount().add(pList.getOverdueAmount())
								.subtract(sysBankLimit.getOnceLimit()).compareTo(BigDecimal.valueOf(0)) > 0) {
							boolPartRepay = 1;
						}
						Result result = rechargeService.recharge(basic.getBusinessId(), pList.getAfterId(),
								customerDto.getBankBindCardNo(), repayMoney.doubleValue(), customerDto.getPlatformId(),
								merchOrderId);
						if (result.getCode().equals("1")) {
							rechargeService.recordRepaymentLog(result, pList, basic, customerDto,
									channel.getPlatformId(), 1, boolPartRepay, merchOrderId, 0,
									repayMoney);
							break;
						} else {
							// 如果是余额不足，则跳出循环
							if (IsNoEnoughMoney(result.getMsg())) {
								break;
							} else {
								rechargeService.recordRepaymentLog(result, pList, basic, customerDto,
										channel.getPlatformId(), 1, boolPartRepay, merchOrderId, 0,
										repayMoney);
								continue;
							}
						}

						// 第三方代扣，非一次性扣款
					} else {
						// 获取代扣每次最高额
						BigDecimal eachMax = sysBankLimit.getOnceLimit();
						// 代扣次数
						Integer repayCount = repayMoney.divide(eachMax, RoundingMode.CEILING).intValue();
						// 余数
						BigDecimal remainder = repayMoney.divideAndRemainder(eachMax)[1];
						int last = repayCount - 1;
						Integer boolPartRepay = 1;// 表示本期是否分多笔代扣,0:一次性代扣，1:分多笔代扣
						Integer boolLastRepay = 0;// 表示本期是否分多笔代扣中的最后一笔代扣，若非多笔代扣，本字段存1。 0:非最后一笔代扣，1:最后一笔代扣
						for (int i = 0; i < repayCount; i++) {
							BigDecimal currentAmount=BigDecimal.valueOf(0);//本次代扣金额
							if(i==last&&remainder.compareTo(BigDecimal.valueOf(0))>0) {
								currentAmount=remainder;
							}else {
								currentAmount=eachMax;
							}
							String merchOrderId = rechargeService.getMerchOrderId();// 获取商户订单号
							Result result = rechargeService.recharge(basic.getBusinessId(), pList.getAfterId(),
									customerDto.getBankBindCardNo(), currentAmount.doubleValue(),
									customerDto.getPlatformId(), merchOrderId);
							if (result.getCode().equals("1")) {
								if(i==last) {//说明是最后一次代扣
									boolLastRepay=1;
									rechargeService.recordRepaymentLog(result, pList, basic, customerDto,
											channel.getPlatformId(), boolLastRepay, boolPartRepay, merchOrderId, 0,
											currentAmount);
								   break outerloop;
								}else {
								rechargeService.recordRepaymentLog(result, pList, basic, customerDto,
										channel.getPlatformId(), boolLastRepay, boolPartRepay, merchOrderId, 0,
										currentAmount);
								   continue;
								}

							} else {
								// 如果是余额不足，则跳出最外层循环
								if (IsNoEnoughMoney(result.getMsg())) {
									// TODO,可能还要作这样的操作：扣除线下费用再尝试代扣一次
									rechargeService.recordRepaymentLog(result, pList, basic, customerDto,
											channel.getPlatformId(), 1, boolPartRepay, merchOrderId, 0,
											currentAmount);
									break outerloop;
								} else {
									rechargeService.recordRepaymentLog(result, pList, basic, customerDto,
											channel.getPlatformId(), 1, boolPartRepay, merchOrderId, 0,
											currentAmount);
									break;
								}
							}
						}

					}
				}

			}
		}
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
	

}
