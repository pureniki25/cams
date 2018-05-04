package com.hongte.alms.base.service;

import com.hongte.alms.base.dto.FinanceManagerListReq;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.vo.module.FinanceManagerListVO;
import com.hongte.alms.common.service.BaseService;
import com.hongte.alms.common.vo.PageResult;

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
    public List<RepaymentBizPlanList> selectNeedPhoneUrgNorBiz(String companyId,Integer beforeDueDays, Integer businessType);

    /**
     * 查出需要移交上门催收的正常业务还款计划列表
     * @param companyId
     * @return
     */
    public List<RepaymentBizPlanList> selectNeedVisitNorBiz(String companyId,Integer overDueDays, Integer businessType );

    /**
     * 查出需要移交法务的正常业务还款计划列表
     * @return
     */
    public List<RepaymentBizPlanList> selectNeedLawNorBiz(Integer overDueDays,Integer businessType );
     
    String queryRepaymentBizPlanListByConditions(@Param(value="businessId") String businessId, @Param(value="afterId") String afterId);

    /**
     * 财务管理列表查询service
     * @author 王继光
     * 2018年5月3日 下午3:34:55
     * @param req
     * @return
     */
    public PageResult selectByFinanceManagerListReq(FinanceManagerListReq req) ;

}
