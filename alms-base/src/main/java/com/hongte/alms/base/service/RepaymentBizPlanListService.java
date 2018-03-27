package com.hongte.alms.base.service;

import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.common.service.BaseService;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 业务还款计划列表 服务类
 * </p>
 *
 * @author 曾坤
 * @since 2018-03-06
 */
public interface RepaymentBizPlanListService extends BaseService<RepaymentBizPlanList> {

    /**
     * 查出需要移交电催的正常业务还款计划列表
     * @param companyId
     * @return
     */
    public List<RepaymentBizPlanList> selectNeedPhoneUrgNorBiz(String companyId,Integer beforeDueDays);

    /**
     * 查出需要移交上门催收的正常业务还款计划列表
     * @param companyId
     * @return
     */
    public List<RepaymentBizPlanList> selectNeedVisitNorBiz(String companyId,Integer overDueDays );

    /**
     * 查出需要移交法务的正常业务还款计划列表
     * @return
     */
    public List<RepaymentBizPlanList> selectNeedLawNorBiz(Integer overDueDays );
     
    String queryRepaymentBizPlanListByConditions(@Param(value="businessId") String businessId, @Param(value="afterId") String afterId);

//    /**
//     * 查出需要移交电催的展期业务还款计划列表
//     * @param companyId
//     * @return
//     */
//    public List<RepaymentBizPlanList> selectNeedPhoneUrgRenewBiz(String companyId,Integer beforeDueDays);
//
//    /**
//     * 查出需要移交上门催收的正常业务还款计划列表
//     * @param companyId
//     * @return
//     */
//    public List<RepaymentBizPlanList> selectNeedVisitRenewBiz(String companyId,Integer overDueDays );
//
//    /**
//     * 查出需要移交法务的正常业务还款计划列表
//     * @return
//     */
//    public List<RepaymentBizPlanList> selectNeedLawRenewBiz(Integer overDueDays );
//



}
