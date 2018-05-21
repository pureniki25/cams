/**
 * 
 */
package com.hongte.alms.finance.dto.repayPlan;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.hongte.alms.base.dto.ConfirmRepaymentReq;
import com.hongte.alms.base.entity.RepaymentProjPlanListDetail;
import com.hongte.alms.base.entity.TuandaiProjectInfo;
import com.hongte.alms.base.enums.repayPlan.RepayPlanFeeTypeEnum;
import com.hongte.alms.base.vo.finance.CurrPeriodProjDetailVO;

/**
 * @author 王继光
 * 2018年5月18日 下午2:00:26
 */
public class ConfirmRepaymentPreviewDto {
	


	private RepaymentBizPlanDto bizPlanDto ;
	private List<CurrPeriodProjDetailVO> list ;
	public RepaymentBizPlanDto getBizPlanDto() {
		return bizPlanDto;
	}
	public void setBizPlanDto(RepaymentBizPlanDto bizPlanDto) {
		this.bizPlanDto = bizPlanDto;
	}
	public List<CurrPeriodProjDetailVO> getList() {
		return list;
	}
	public void setList(List<CurrPeriodProjDetailVO> list) {
		if (this.list==null) {
			this.list = list;
		}else{
			for (CurrPeriodProjDetailVO currPeriodProjDetailVO : this.list) {
				for (CurrPeriodProjDetailVO vo : list) {
					if (currPeriodProjDetailVO.getProject().equals(vo.getProject())) {
						currPeriodProjDetailVO.setItem10(currPeriodProjDetailVO.getItem10().add(vo.getItem10()));
						currPeriodProjDetailVO.setItem20(currPeriodProjDetailVO.getItem20().add(vo.getItem20()));
						currPeriodProjDetailVO.setItem30(currPeriodProjDetailVO.getItem30().add(vo.getItem30()));
						currPeriodProjDetailVO.setItem50(currPeriodProjDetailVO.getItem50().add(vo.getItem50()));
						currPeriodProjDetailVO.setOfflineOverDue(currPeriodProjDetailVO.getOfflineOverDue().add(vo.getOfflineOverDue()));
						currPeriodProjDetailVO.setOnlineOverDue(currPeriodProjDetailVO.getOnlineOverDue().add(vo.getOnlineOverDue()));
						currPeriodProjDetailVO.setSubTotal(currPeriodProjDetailVO.getSubTotal().add(vo.getSubTotal()));
						currPeriodProjDetailVO.setTotal(currPeriodProjDetailVO.getTotal().add(vo.getTotal()));
					}
				}
			}
		}
	}
	
	/**
	 * 计算每个标的占比
	 * 
	 * @author 王继光 2018年5月18日 上午10:43:18
	 * @param dto
	 * @return
	 */
	private  void caluProportion(RepaymentBizPlanDto dto) {
		BigDecimal count = new BigDecimal(0);
		for (RepaymentProjPlanDto projPlanDto : dto.getProjPlanDtos()) {
			count = count.add(projPlanDto.getRepaymentProjPlan().getBorrowMoney());
		}
		for (RepaymentProjPlanDto projPlanDto : dto.getProjPlanDtos()) {
			BigDecimal proportion = projPlanDto.getRepaymentProjPlan().getBorrowMoney().divide(count).setScale(10,
					BigDecimal.ROUND_HALF_UP);
			projPlanDto.setProportion(proportion);
		}
	}

	/**
	 * 计算每个标的分配下来的金额
	 * 
	 * @author 王继光 2018年5月18日 上午10:43:18
	 * @param dto
	 * @return
	 */
	private  void distributiveMoney(BigDecimal money, RepaymentBizPlanDto dto) {
		BigDecimal moneyCopy = money;
		for (int i = 0; i < dto.getProjPlanDtos().size(); i++) {
			RepaymentProjPlanDto repaymentProjPlanDto = dto.getProjPlanDtos().get(i);
			if (i == dto.getProjPlanDtos().size() - 1) {
				repaymentProjPlanDto.setDistributiveMoney(moneyCopy);
			} else {
				BigDecimal dmoney = money.multiply(repaymentProjPlanDto.getProportion());
				repaymentProjPlanDto.setDistributiveMoney(dmoney);
				moneyCopy = moneyCopy.subtract(dmoney);
			}
		}
	}
	
	/**
	 * 计算每个标的分配下来的线上滞纳金金额
	 * 
	 * @author 王继光 2018年5月18日 上午10:43:18
	 * @param dto
	 * @return
	 */
	private  void distributiveOnlineOveryDueMoney(BigDecimal money, RepaymentBizPlanDto dto) {
		BigDecimal moneyCopy = money;
		for (int i = 0; i < dto.getProjPlanDtos().size(); i++) {
			RepaymentProjPlanDto repaymentProjPlanDto = dto.getProjPlanDtos().get(i);
			if (i == dto.getProjPlanDtos().size() - 1) {
				repaymentProjPlanDto.setOnlineOverDue(moneyCopy);
			} else {
				BigDecimal dmoney = money.multiply(repaymentProjPlanDto.getProportion());
				repaymentProjPlanDto.setOnlineOverDue(dmoney);
				moneyCopy = moneyCopy.subtract(dmoney);
			}
		}
	}
	
	/**
	 * 计算每个标的分配下来的线下滞纳金金额
	 * 
	 * @author 王继光 2018年5月18日 上午10:43:18
	 * @param dto
	 * @return
	 */
	private  void distributiveOfflineOveryDueMoney(BigDecimal money, RepaymentBizPlanDto dto) {
		BigDecimal moneyCopy = money;
		for (int i = 0; i < dto.getProjPlanDtos().size(); i++) {
			RepaymentProjPlanDto repaymentProjPlanDto = dto.getProjPlanDtos().get(i);
			if (i == dto.getProjPlanDtos().size() - 1) {
				repaymentProjPlanDto.setOfflineOverDue(moneyCopy);
			} else {
				BigDecimal dmoney = money.multiply(repaymentProjPlanDto.getProportion());
				repaymentProjPlanDto.setOfflineOverDue(dmoney);
				moneyCopy = moneyCopy.subtract(dmoney);
			}
		}
	}

	/**
	 * 填充每一项
	 * 
	 * @author 王继光 2018年5月18日 上午11:04:26
	 * @param dto
	 */
	public  ConfirmRepaymentPreviewDto fillItem(ConfirmRepaymentReq req , BigDecimal money, RepaymentBizPlanDto dto,boolean preview) {
		BigDecimal onlineOverDue = req.getOnlineOverDue() ;
		BigDecimal offlineOverDue = req.getOfflineOverDue() ;
		if (onlineOverDue!=null) {
			money = money.subtract(onlineOverDue);
			distributiveOnlineOveryDueMoney(onlineOverDue, dto);
		}
		if (offlineOverDue!=null) {
			money = money.subtract(offlineOverDue);
			distributiveOfflineOveryDueMoney(offlineOverDue, dto);
		}
		/* 计算占比 */
		caluProportion(dto);
		/* 分配资金 */
		distributiveMoney(money, dto);
		
		
		
		List<RepaymentProjPlanDto> list = dto.getProjPlanDtos();
		List<CurrPeriodProjDetailVO> currPeriodProjDetailVOs = new ArrayList<>();
		for (RepaymentProjPlanDto repaymentProjPlanDto : list) {
			BigDecimal distributiveMoney = repaymentProjPlanDto.getDistributiveMoney();
			List<RepaymentProjPlanListDto> projPlanListDtos = repaymentProjPlanDto.getProjPlanListDtos();

			/* 开始渲染每个标的还款信息 */
			CurrPeriodProjDetailVO detailVO = new CurrPeriodProjDetailVO();
			TuandaiProjectInfo projectInfo = repaymentProjPlanDto.getTuandaiProjectInfo();
			String userName = projectInfo.getRealName();
			boolean isMaster = projectInfo.getMasterIssueId().equals(projectInfo.getProjectId());
			detailVO.setMaster(isMaster);
			detailVO.setUserName(userName);
			detailVO.setProjAmount(projectInfo.getAmount());
			BigDecimal surplusFund = new BigDecimal(0);

			for (RepaymentProjPlanListDto projPlanListDto : projPlanListDtos) {
				/* 计算总还金额 */
				BigDecimal repayAmount = caluProjPlanListSurplusFund(projPlanListDto);
				int c = repayAmount.compareTo(distributiveMoney);
				RepaymentBizPlanListDto repaymentBizPlanListDto = findRepaymentBizPlanListDto(projPlanListDto.getRepaymentProjPlanList().getPlanListId(),dto);
				/* 比较总还金额与分配金额 */
				if (c == -1) {
					/* 总还金额<分配金额,有余额,且每一项都填满 */
					surplusFund = distributiveMoney.subtract(repayAmount);
					BigDecimal surplusFundAdd = repaymentProjPlanDto.getSurplusMoney().add(surplusFund);
					repaymentProjPlanDto.setSurplusMoney(surplusFundAdd);
				} else {
					/* 总还金额==分配金额,没有余额,且每一项都填满 */
					/* 总还金额>分配金额,没有余额,有没填满的项 */
				}

				List<RepaymentProjPlanListDetail> details = projPlanListDto.getProjPlanListDetails();
				for (RepaymentProjPlanListDetail repaymentProjPlanListDetail : details) {
					Integer itemType = repaymentProjPlanListDetail.getPlanItemType();
					String feeId = repaymentProjPlanListDetail.getFeeId();
					/* 子项金额 */
					BigDecimal itemPlanAmount = repaymentProjPlanListDetail.getProjPlanAmount();
					BigDecimal factPlanAmount = repaymentProjPlanListDetail.getProjFactAmount();
					if (factPlanAmount==null) {
						factPlanAmount = new BigDecimal(0);
					}
					if (itemType.equals(new Integer(60))) {
						boolean online = RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid().equals(feeId);
						boolean offline = RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid().equals(feeId);
						if (offline) {
							int cr = itemPlanAmount.compareTo(repaymentProjPlanDto.getOfflineOverDue());
							if (cr>=0) {
								/*cr==0,itemPlanAmount==repaymentProjPlanDto.getOfflineOverDue(),刚好足够*/
								/*cr==1,itemPlanAmount>repaymentProjPlanDto.getOfflineOverDue(),不足*/
								repaymentProjPlanListDetail.setProjFactAmount(factPlanAmount.add(repaymentProjPlanDto.getOfflineOverDue()));
								detailVO.setOfflineOverDue(repaymentProjPlanDto.getOfflineOverDue());
							}else {
								repaymentProjPlanListDetail.setProjFactAmount(factPlanAmount.add(itemPlanAmount));
								detailVO.setOfflineOverDue(itemPlanAmount);
								surplusFund = surplusFund.add(repaymentProjPlanDto.getOfflineOverDue().subtract(itemPlanAmount));
								/*cr==-1,itemPlanAmount<repaymentProjPlanDto.getOfflineOverDue(),填满有余*/
							}
							
							
						}
						
						if (online) {
							int cr = itemPlanAmount.compareTo(repaymentProjPlanDto.getOnlineOverDue());
							if (cr>=0) {
								/*cr==0,itemPlanAmount==repaymentProjPlanDto.getOnlineOverDue(),刚好足够*/
								/*cr==1,itemPlanAmount>repaymentProjPlanDto.getOnlineOverDue(),不足*/
								repaymentProjPlanListDetail.setProjFactAmount(factPlanAmount.add(repaymentProjPlanDto.getOnlineOverDue()));
								detailVO.setOfflineOverDue(repaymentProjPlanDto.getOnlineOverDue());
							}else {
								repaymentProjPlanListDetail.setProjFactAmount(factPlanAmount.add(itemPlanAmount));
								detailVO.setOfflineOverDue(itemPlanAmount);
								surplusFund = surplusFund.add(repaymentProjPlanDto.getOnlineOverDue().subtract(itemPlanAmount));
								/*cr==-1,itemPlanAmount<repaymentProjPlanDto.getOnlineOverDue(),填满有余*/
							}
							
							
						}
						
					}else {
						int cr = itemPlanAmount.compareTo(distributiveMoney);
						
						/* 比较子项金额与分配金额 */
						if (cr >= 0) {
							/* 子项金额>分配金额,子项实还填入剩余的分配金额,退出循环 */
							/* 子项金额==分配金额,子项实还填入剩余的分配金额,退出循环 */
							repaymentProjPlanListDetail.setProjFactAmount(factPlanAmount.add(distributiveMoney));
							
							
							
							/* 将分润的信息填写到CurrPeriodProjDetailVO中 */
							renderCurrPeriodProjDetailVO(repaymentProjPlanListDetail, detailVO, distributiveMoney);
							distributiveMoney = new BigDecimal(0);

							break;
						} else {
							/* 子项金额<分配金额,子项实还填入子项应还 */
							distributiveMoney = distributiveMoney.subtract(itemPlanAmount);
							/* 将分润的信息填写到CurrPeriodProjDetailVO中 */
							renderCurrPeriodProjDetailVO(repaymentProjPlanListDetail, detailVO, itemPlanAmount);
							repaymentProjPlanListDetail.setProjFactAmount(factPlanAmount.add(itemPlanAmount));
						}
					}
					
					
				}

				if (distributiveMoney.equals(new BigDecimal(0))) {
					break;
				}

			}
			currPeriodProjDetailVOs.add(detailVO);
			repaymentProjPlanDto.setSurplusMoney(surplusFund);
		}
		ConfirmRepaymentPreviewDto confirmRepaymentPreviewDto = new ConfirmRepaymentPreviewDto();
		confirmRepaymentPreviewDto.setBizPlanDto(dto);
		confirmRepaymentPreviewDto.setList(currPeriodProjDetailVOs);
		return confirmRepaymentPreviewDto;
	}

	
	/**
	 * 计算标的还款计划列表总共需要还款的金额
	 * 
	 * @author 王继光 2018年5月18日 上午11:05:05
	 * @param projPlanListDto
	 * @return
	 */
	private  BigDecimal caluProjPlanListSurplusFund(RepaymentProjPlanListDto projPlanListDto) {
		BigDecimal res = new BigDecimal(0);
		List<RepaymentProjPlanListDetail> list = projPlanListDto.getProjPlanListDetails();
		for (RepaymentProjPlanListDetail detail : list) {
			BigDecimal planAmount = detail.getProjPlanAmount();
			BigDecimal factAmount = detail.getProjFactAmount();
			if (factAmount == null) {
				res = res.add(planAmount);
			} else {
				res = res.add(planAmount.subtract(factAmount));
			}
		}
		return res;
	}

	/**
	 * 将分润的信息填写到CurrPeriodProjDetailVO中
	 * 
	 * @author 王继光 2018年5月18日 下午2:35:53
	 * @param detail
	 * @param vo
	 * @param money
	 */
	private  void renderCurrPeriodProjDetailVO(RepaymentProjPlanListDetail detail, CurrPeriodProjDetailVO vo,
			BigDecimal money) {
		switch (detail.getPlanItemType()) {
		case 10:
			vo.setItem10(money);
			break;
		case 20:
			vo.setItem20(money);
			break;
		case 30:
			vo.setItem30(money);
			break;
		case 50:
			vo.setItem50(money);
			break;
		/*case 60:
			String feeId = detail.getFeeId();
			boolean onlineOverDue = RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid().equals(feeId);
			boolean offlineOvueDue = RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid().equals(feeId);
			if (offlineOvueDue) {
				vo.setOfflineOverDue(money);
			}
			if (onlineOverDue) {
				vo.setOnlineOverDue(money);
			}
			break;*/
		default:
			break;
		}
	}
	
	
	private  RepaymentBizPlanListDto findRepaymentBizPlanListDto(String planListId,RepaymentBizPlanDto dto) {
		List<RepaymentBizPlanListDto> list =  dto.getBizPlanListDtos();
		for (RepaymentBizPlanListDto repaymentBizPlanListDto : list) {
			if (repaymentBizPlanListDto.getRepaymentBizPlanList().getPlanListId().equals(planListId)) {
				return repaymentBizPlanListDto;
			}
		}
		return null;
		
	}
}
