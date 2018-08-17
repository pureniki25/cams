package com.hongte.alms.base.mapper;

import com.hongte.alms.base.RepayPlan.dto.RepaymentSettleMoneyDto;
import com.hongte.alms.base.dto.FinanceManagerListReq;
import com.hongte.alms.base.dto.RepaymentPlanInfoDTO;
import com.hongte.alms.base.dto.RepaymentProjInfoDTO;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.vo.finance.ConfirmWithholdListVO;
import com.hongte.alms.base.vo.module.FinanceManagerListVO;
import com.hongte.alms.common.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
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
            @Param("colStatus") Integer colStatus,
            @Param("businessType") Integer businessType,
            @Param("businessId") String businessId
    		);
    
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

    int conutFinanceManagerList(FinanceManagerListReq req) ;
    List<FinanceManagerListVO> selectFinanceMangeList(FinanceManagerListReq req) ;
    
    List<RepaymentBizPlanList> selectAutoRepayList(@Param(value = "days") Integer days);
    /**
     * 根据源业务编号获取还款计划信息
     * @param businessId
     * @return
     */
    List<RepaymentPlanInfoDTO> queryRepaymentPlanInfoByBusinessId(@Param(value = "businessId") String businessId);
    /**
     * 根据业务还款计划列表ID获取所有对应的标的应还还款计划信息
     * @param planListId
     * @return
     */
    List<RepaymentProjInfoDTO> queryPlanRepaymentProjInfoByPlanListId(@Param(value = "planListId") String planListId);
    /**
     * 根据业务还款计划列表ID获取所有对应的标的实还还款计划信息
     * @param planListId
     * @return
     */
    List<RepaymentProjInfoDTO> queryActualRepaymentProjInfoByPlanListId(@Param(value = "planListId") String planListId);
    
    /**
     * 计算业务还款计划应还金额(已减去减免)
     * @author 王继光
     * 2018年5月26日 下午3:01:53
     * @param planListId
     * @return
     */
    BigDecimal caluBizPlanListPlanAmount(String planListId);
    /**
     * 计算业务还款计划实还金额
     * @author 王继光
     * 2018年5月26日 下午3:02:25
     * @param planListId
     * @return
     */
    BigDecimal caluBizPlanListFactAmount(String planListId);
    /**
     * 计算业务还款计划未还金额
     * @author 王继光
     * 2018年5月26日 下午3:02:41
     * @param planListId
     * @return
     */
    BigDecimal caluBizPlanListUnpaid(String planListId);
    
    /**
     * 查询业务的代扣确认列表信息
     * @author 王继光
     * 2018年5月28日 下午4:09:31
     * @param businessId
     * @return
     */
    List<ConfirmWithholdListVO> listConfirmWithhold(String businessId);
    
    
    
	/**
	 * 找出符合条件的记录进行滞纳金计算
	 * @return
	 */
	List<RepaymentBizPlanList> getPlanListForCalLateFee(@Param(value = "planListId") String planListId);

    List<RepaymentSettleMoneyDto> selectProjPlanMoney(int flage, String businessId, Integer period);
}
