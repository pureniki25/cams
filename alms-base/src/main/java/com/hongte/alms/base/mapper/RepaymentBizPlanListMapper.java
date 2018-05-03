package com.hongte.alms.base.mapper;

import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.common.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 业务还款计划列表 Mapper 接口
 * </p>
 *
 * @author 王继光
 * @since 2018-03-02
 */
public interface RepaymentBizPlanListMapper extends SuperMapper<RepaymentBizPlanList> {


    /**
     * 选择需要设置催收信息的一般业务还款计划列表
     *
     * @return
     */
    List<RepaymentBizPlanList> selectNeedSetColInfoNormalBizPlansBycomId(
            @Param("companyId") String companyId,
            @Param("overDueDays") Integer overDueDays,
            @Param("colStatus") Integer colStatus);
    
    String queryRepaymentBizPlanListByConditions(@Param(value="businessId") String businessId, @Param(value="afterId") String afterId);


/*    *//**
     * 选择需要设置催收信息的展期业务还款计划列表
     *
     * @return
     *//*
    List<RepaymentBizPlanList> selectNeedSetColInfoRenewBizPlansBycomId(
            @Param("companyId") String companyId,
            @Param("overDueDays") Integer overDueDays,
            @Param("colStatus") Integer colStatus);*/
    /**
     * 获取首期逾期到今天的天数
     * @param businessId
     * @return
     */
    Integer queryFirstPeriodOverdueByBusinessId(@Param(value = "businessId") String businessId);
    /**
     * 获取利息第一次逾期到今天的天数
     * @param businessId
     * @return
     */
    Integer queryInterestOverdueByBusinessId(@Param(value = "businessId") String businessId);
    /**
     * 获取本金第一次逾期到今天的天数
     * @param businessId
     * @return
     */
    Integer queryPrincipalOverdueByBusinessId(@Param(value = "businessId") String businessId);

}
