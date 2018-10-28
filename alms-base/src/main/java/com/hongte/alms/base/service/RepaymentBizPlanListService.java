package com.hongte.alms.base.service;

import java.util.Date;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hongte.alms.base.dto.FinanceManagerListReq;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.WithholdingRepaymentLog;
import com.hongte.alms.base.vo.finance.ConfirmWithholdListVO;
import com.hongte.alms.base.vo.finance.RepaymentPlanBaseInfoVo;
import com.hongte.alms.common.service.BaseService;
import com.hongte.alms.common.vo.PageResult;

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

    /**
     * 查出指定业务需要的还款计划列表
     * @return
     */
    public List<RepaymentBizPlanList> selectNeedLawNorBizByBizId(Integer overDueDays, Integer businessType,String businessId);

    String queryRepaymentBizPlanListByConditions(@Param(value="businessId") String businessId, @Param(value="afterId") String afterId);

    /**
     * 财务管理列表查询service
     * @author 王继光
     * 2018年5月3日 下午3:34:55
     * @param req
     * @return
     */
    public PageResult selectByFinanceManagerListReq(FinanceManagerListReq req) ;

    /**
     *
     * @param settleDate
     * @param planLists
     * @return
     */
    public RepaymentBizPlanList findCurrentPeriod(Date settleDate, List<RepaymentBizPlanList> planLists);

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
    /**
     * 获取首期逾期到今天的天数
     * @param businessId
     * @return
     */
    Integer queryFirstPeriodOverdueByBusinessId(String businessId);
    /**
     * 获取利息第一次逾期到今天的天数
     * @param businessId
     * @return
     */
    Integer queryInterestOverdueByBusinessId(String businessId);
    /**
     * 获取本金第一次逾期到今天的天数
     * @param businessId
     * @return
     */
    Integer queryPrincipalOverdueByBusinessId(String businessId);
    /**
     * @author czs
     * 获取符合自动代扣的数据
     * @return
     */
    List<RepaymentBizPlanList> selectAutoRepayList(Integer days);


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
	List<RepaymentBizPlanList> getPlanListForCalLateFee(String planListId);
	
	/**
     * 查询自动或手动移交法务的数据
     * @param origBusinessId
     * @return
     */
    List<RepaymentBizPlanList> queryTransferOfLitigationData(Integer overDueDays, String origBusinessId);
    
    /**
     * 查找代扣成功没有核销的记录
     */
    
    List<WithholdingRepaymentLog> searchNoCancelList();
    
    /**
     * 根据业务编号查询还款详情页面基础信息
     * @param businessId
     * @return
     */
    RepaymentPlanBaseInfoVo queryBaseInfoByBusinessId(String businessId);
}
