/**
 * 
 */
package com.hongte.alms.base.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.hongte.alms.base.RepayPlan.dto.PlanListDetailShowPayDto;
import com.hongte.alms.base.RepayPlan.dto.RepaymentProjPlanDto;
import com.hongte.alms.base.RepayPlan.dto.RepaymentProjPlanSettleDto;

/**
 * @author 王继光
 * 2018年7月26日 下午3:49:49
 */
public class ProjPlanDtoUtil {

	/**
	 * 将标的排序
	 * @author 王继光
	 * 2018年7月26日 下午3:51:33
	 * @param repaymentProjPlanDtos
	 */
	public static void sort(List<RepaymentProjPlanDto> repaymentProjPlanDtos) {
		Collections.sort(repaymentProjPlanDtos, new Comparator<RepaymentProjPlanDto>() {
            // 排序规则说明 需补充 从小标到大标，再到主借标
            //同等
            @Override
            public int compare(RepaymentProjPlanDto arg0, RepaymentProjPlanDto arg1) {
                if (arg0.getTuandaiProjectInfo().getMasterIssueId().equals(arg0.getTuandaiProjectInfo().getProjectId())) {
                    return 1;
                }else if (arg1.getTuandaiProjectInfo().getMasterIssueId().equals(arg1.getTuandaiProjectInfo().getProjectId())) {
                    return -1;
				}
                if (arg0.getRepaymentProjPlan().getBorrowMoney()
                        .compareTo(arg1.getRepaymentProjPlan().getBorrowMoney()) < 0) {
                    return -1;
                }
                if (arg0.getTuandaiProjectInfo().getQueryFullSuccessDate()
                        .before(arg1.getTuandaiProjectInfo().getQueryFullSuccessDate())) {
                    return -1;
                }else if (arg0.getTuandaiProjectInfo().getQueryFullSuccessDate()
                        .after(arg1.getTuandaiProjectInfo().getQueryFullSuccessDate())) {
					return 1;
				}
                return 0;
            }

        });
	}

	/**
	 * 将标的排序
	 * @author 王继光
	 * 2018年7月26日 下午3:51:33
	 * @param repaymentProjPlanDtos
	 */
    public static void sortSettleDtos(List<RepaymentProjPlanSettleDto> repaymentProjPlanDtos) {
        Collections.sort(repaymentProjPlanDtos, new Comparator<RepaymentProjPlanSettleDto>() {
            // 排序规则说明 需补充 从小标到大标，再到主借标
            //同等
            @Override
            public int compare(RepaymentProjPlanSettleDto arg0, RepaymentProjPlanSettleDto arg1) {
                if (arg0.getTuandaiProjectInfo().getMasterIssueId().equals(arg0.getTuandaiProjectInfo().getProjectId())) {
                    return 1;
                }else if (arg1.getTuandaiProjectInfo().getMasterIssueId().equals(arg1.getTuandaiProjectInfo().getProjectId())) {
                    return -1;
                }
                if (arg0.getRepaymentProjPlan().getBorrowMoney()
                        .compareTo(arg1.getRepaymentProjPlan().getBorrowMoney()) < 0) {
                    return -1;
                }
                if (arg0.getTuandaiProjectInfo().getQueryFullSuccessDate()
                        .before(arg1.getTuandaiProjectInfo().getQueryFullSuccessDate())) {
                    return -1;
                }else if (arg0.getTuandaiProjectInfo().getQueryFullSuccessDate()
                        .after(arg1.getTuandaiProjectInfo().getQueryFullSuccessDate())) {
                    return 1;
                }
                return 0;
            }

        });
    }
    
    /**
     * 应还项根据shareProfitIndex
     * @author 王继光
     * 2018年7月27日 下午9:46:12
     * @param list
     */
    public static void sortFeeByShareProfitIndex(List<PlanListDetailShowPayDto> list) {
    	Collections.sort(list, new Comparator<PlanListDetailShowPayDto>() {

			@Override
			public int compare(PlanListDetailShowPayDto o1, PlanListDetailShowPayDto o2) {
				if (o1.getShareProfitIndex()<o2.getShareProfitIndex()) {
					return -1;
				}
				if (o1.getShareProfitIndex()>o2.getShareProfitIndex()) {
					return 1;
				}
				return 0;
			}
    		
    	});
    }
}
