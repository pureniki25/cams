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
import com.hongte.alms.base.entity.SysParameter;
import com.hongte.alms.base.entity.WithholdingChannel;
import com.hongte.alms.base.enums.PlatformEnum;
import com.hongte.alms.base.enums.SysParameterEnums;
import com.hongte.alms.base.feignClient.dto.BankCardInfo;
import com.hongte.alms.base.feignClient.dto.CustomerInfoDto;
import com.hongte.alms.base.feignClient.dto.ThirdPlatform;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.base.service.SysBankLimitService;
import com.hongte.alms.base.service.SysParameterService;
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

	@Autowired
	@Qualifier("SysParameterService")
	SysParameterService sysParameterService;

	@Override
	public void withholding() {
		List<SysParameter> repayStatusList = sysParameterService.selectList(new EntityWrapper<SysParameter>()
				.eq("param_type", SysParameterEnums.REPAY_DAYS.getKey()).eq("status", 1).orderBy("row_Index"));
		Integer days = Integer.valueOf(repayStatusList.get(0).getParamValue());
		List<RepaymentBizPlanList> pLists = repaymentBizPlanListService.selectAutoRepayList(days);// 查询一个周期内(30天)要代扣的记录
		for (RepaymentBizPlanList pList : pLists) {
			// 是否符合自动代扣规则
			if (rechargeService.EnsureAutoPayIsEnabled(pList, days)) {
				autoRepayPerList(pList);
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
		List<BankCardInfo> bankCardInfos = customerDto.getList();
		BankCardInfo bankCardInfo = rechargeService.getBankCardInfo(bankCardInfos);
		BankCardInfo ThirtyCardInfo = rechargeService.getThirtyPlatformInfo(bankCardInfos);

		if (bankCardInfo != null) {
			// 银行代扣
			BankCharge(basic, bankCardInfo, pList, bankCardInfos,null);

		} else if (ThirtyCardInfo != null && bankCardInfo == null) {// 第三方代扣
			ThirdRepaymentCharge(basic, ThirtyCardInfo, pList, null);
		} else {
			logger.debug(
					"业务编号为" + pList.getOrigBusinessId() + "期数为" + pList.getAfterId() + "代扣失败，没有找到银行代扣和第三方代扣相关绑定信息");
		}
	}

	/**
	 * 银行代扣
	 */
	private String BankCharge(BasicBusiness basic, BankCardInfo bankCardInfo, RepaymentBizPlanList pList,
			List<BankCardInfo> bankCardInfos,BigDecimal handRepayMoney) {
		BigDecimal onlineAmount = rechargeService.getOnlineAmount(pList);
		BigDecimal underAmount = rechargeService.getUnderlineAmount(pList);
		Integer platformId = (Integer) PlatformEnum.YH_FORM.getValue();
		boolean isUseThirdRepay = false;// 是否调用第三方代扣
		// 获取所有银行代扣渠道,先扣线上费用
		List<WithholdingChannel> channels = withholdingChannelService
				.selectList(new EntityWrapper<WithholdingChannel>().eq("platform_id", PlatformEnum.YH_FORM.getValue())
						.eq("channel_status", 1).eq("bank_code", bankCardInfo.getBankCode()).orderBy("level"));
		WithholdingChannel channel = null;
		if (channels != null && channels.size() > 0) {
			channel = channels.get(0);
		}
        
		SysBankLimit sysBankLimit = sysBankLimitService.selectOne(
				new EntityWrapper<SysBankLimit>().eq("platform_id", channel.getPlatformId()).eq("status", 1));
		if (sysBankLimit == null) {
			logger.debug("第三方代扣限额信息platformId:" + channel.getPlatformId() + "无效/不存在");
		} else {
			// 本期线上剩余应还金额,剩余应还金额减去线下金额
			BigDecimal repayMoney = rechargeService.getRestAmount(pList).subtract(underAmount);
			if(handRepayMoney!=null) {//说明是手工代扣
				if(handRepayMoney.compareTo(repayMoney)<0) {
					repayMoney=handRepayMoney;
				}
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
					Result result = rechargeService.recharge(basic, pList, repayMoney.doubleValue(), boolLastRepay,
							boolPartRepay, bankCardInfo, channel);
					if (result.getCode().equals("1")) {
						// 成功跳出
						// rechargeService.recordRepaymentLog(result, pList, basic,
						// bankCardInfo,platformId, 1, boolPartRepay, merchOrderId, 0,
						// repayMoney);
						return "";
					} else {
						// 如果是余额不足，则跳出循环
						if (IsNoEnoughMoney(result.getMsg())) {
							return "";
						} else {
							// 使用第三方代扣
							// rechargeService.recordRepaymentLog(result, pList, basic, bankCardInfo,
							// platformId, 1, boolPartRepay, merchOrderId, 0,
							// repayMoney);
							isUseThirdRepay = true;
						}
					}

					// 分多笔代扣
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
						BigDecimal currentAmount = BigDecimal.valueOf(0);// 本次代扣金额
						if (i == last && remainder.compareTo(BigDecimal.valueOf(0)) > 0) {
							boolLastRepay = 1;
							currentAmount = remainder;
						} else {
							currentAmount = eachMax;
						}
						Result result = rechargeService.recharge(basic, pList, currentAmount.doubleValue(),
								boolLastRepay, boolPartRepay, bankCardInfo, channel);
						if (result.getCode().equals("1")) {
							if (i == last) {// 说明是最后一次代扣
								break;
							} else {

								continue;
							}

						} else {
							// 如果是余额不足，则跳出最外层循环
							if (IsNoEnoughMoney(result.getMsg())) {
								// TODO,可能还要作这样的操作：扣除线下费用再尝试代扣一次

								break;
							} else {
								// 第一次银行代扣就失败就调用第三方平台的
								if (i == 0) {
									isUseThirdRepay = true;

									break;
								} else {
									// 非第一次银行代扣失败就跳出循环，不能混合代扣

									break;
								}

							}
						}
					}

				}
			}
		}

		BankCardInfo thirtyCardInfo = rechargeService.getThirtyPlatformInfo(bankCardInfos);
		// 第一次银行代扣就失败,切换第三方代扣
		if (isUseThirdRepay) {
			Integer successCount = rechargeService.getBankRepaySuccessCount(pList);
			if (successCount > 0) {// 如果当前期在银行代扣成功过,就不能用第三方代扣,直接条出
				return "";
			}

			if (thirtyCardInfo != null) {// 绑定了第三方平台
				ThirdRepaymentCharge(basic, thirtyCardInfo, pList, null);

			} else { // 没有绑定第三方平台直接跳出
				return "";
			}

			// 银行代扣成功，线下逾期费用调用第三方代扣
		} else {

			if (thirtyCardInfo != null) {// 绑定了第三方平台
				if (underAmount.compareTo(BigDecimal.valueOf(0)) > 0) {
					ThirdRepaymentCharge(basic, thirtyCardInfo, pList, underAmount);
				} else { // 没有线下逾期费用直接跳出
					return "";
				}
			} else { // 没有绑定第三方平台直接跳出
				return "";
			}
		}

	}

	/**
	 * 第三方代扣 underAmount不为null的话，说明只扣线下费用
	 */
	private void ThirdRepaymentCharge(BasicBusiness basic, BankCardInfo thirtyCardInfo, RepaymentBizPlanList pList,
			BigDecimal underAmount) {

		// 获取所有第三方代扣渠道
		List<WithholdingChannel> channels = withholdingChannelService
				.selectList(new EntityWrapper<WithholdingChannel>().ne("platform_id", PlatformEnum.YH_FORM.getValue())
						.eq("channel_status", 1).eq("bank_code", thirtyCardInfo.getBankCode()).orderBy("level"));
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
					new EntityWrapper<SysBankLimit>().eq("platform_id", channel.getPlatformId()).eq("status", 1));
			if (sysBankLimit == null) {
				logger.debug("第三方代扣限额信息platformId:" + channel.getPlatformId() + "无效/不存在");
				continue;
			} else {

				// 本期剩余应还金额
				BigDecimal repayMoney = pList.getTotalBorrowAmount().add(pList.getOverdueAmount())
						.subtract(rechargeService.getRestAmount(pList));

				if (underAmount != null && underAmount.compareTo(BigDecimal.valueOf(0)) > 0) {
					repayMoney = underAmount;
				}
				// 如果应还金额大于0
				if (repayMoney.compareTo(BigDecimal.valueOf(0)) > 0) {

					// 当第一次扣款额度没有超过银行每次的扣款额度时候，尝试一次性扣款
					if (sysBankLimit.getHasOnceLimit() == 0 || sysBankLimit.getOnceLimit().compareTo(repayMoney) > 0) {
						String merchOrderId = rechargeService.getMerchOrderId();// 获取商户订单号
						Integer boolPartRepay = 0;// 表示本期是否分多笔代扣,0:一次性代扣，1:分多笔代扣
						Integer boolLastRepay = 1;// 表示本期是否分多笔代扣中的最后一笔代扣，若非多笔代扣，本字段存1。 0:非最后一笔代扣，1:最后一笔代扣
						if (pList.getTotalBorrowAmount().add(pList.getOverdueAmount())
								.subtract(sysBankLimit.getOnceLimit()).compareTo(BigDecimal.valueOf(0)) > 0) {
							boolPartRepay = 1;

						}
						Result result = rechargeService.recharge(basic, pList, repayMoney.doubleValue(), boolLastRepay,
								boolPartRepay, thirtyCardInfo, channel);
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
						Integer repayCount = repayMoney.divide(eachMax, RoundingMode.CEILING).intValue();
						// 余数
						BigDecimal remainder = repayMoney.divideAndRemainder(eachMax)[1];
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
							String merchOrderId = rechargeService.getMerchOrderId();// 获取商户订单号
							Result result = rechargeService.recharge(basic, pList, currentAmount.doubleValue(),
									boolLastRepay, boolPartRepay, thirtyCardInfo, channel);
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
