package com.hongte.alms.base.service;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.customer.vo.BankWithholdFlowVo;
import com.hongte.alms.base.customer.vo.BfWithholdFlowVo;
import com.hongte.alms.base.customer.vo.WithholdFlowReq;
import com.hongte.alms.base.customer.vo.YbWithholdFlowVo;
import com.hongte.alms.base.entity.WithholdingRepaymentLog;
import com.hongte.alms.base.enums.PlatformEnum;
import com.hongte.alms.base.vo.module.RepaymentLogReq;
import com.hongte.alms.base.vo.module.RepaymentLogVO;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 还款计划代扣日志流水表 服务类
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-03-08
 */
public interface WithholdingRepaymentLogService extends BaseService<WithholdingRepaymentLog> {

    /**
     * 查询指定清算日期范围内的代扣记录
     */
    List<WithholdingRepaymentLog> findByLiquidationDate(PlatformEnum pe, String beginTime, String endTime);

    /**
     * 分页查询
     * @param key
     * @return
     */
    Page<RepaymentLogVO> selectRepaymentLogPage(RepaymentLogReq key);
    
    
    
     List<RepaymentLogVO> selectRepaymentLogExcel(RepaymentLogReq key);

     
     /**
      * 根据业务ID查找代扣业务条数
      * @param key
      * @return
      */
     RepaymentLogVO selectSumByBusinessId(RepaymentLogReq key);
     
     
     /**
      * 根据业务ID查找代扣成功流水条数
      * @param key
      * @return
      */
     RepaymentLogVO selectSumByLogId(RepaymentLogReq key);
     
     /**
      * 根据业务ID,afterId
      * @param key
      * @return
      */
     List<WithholdingRepaymentLog> selectRepaymentLogForAutoRepay(String businessId, String afterId,Integer platformId);
     
     /**
      * 查询处理中的代扣记录
      * @param key
      * @return
      */
     List<WithholdingRepaymentLog> selectRepaymentLogForResult();

    /**
     * 银行代扣流水列表
     * @param bankWithholdFlowReq
     * @return
     */
    Page<BankWithholdFlowVo> getBankWithholdFlowPageList(WithholdFlowReq bankWithholdFlowReq);

    /**
     * 宝付代扣流水列表
     * @param withholdFlowReq
     * @return
     */
    Page<BfWithholdFlowVo> getBfWithholdFlowPageList(WithholdFlowReq withholdFlowReq);
    /**
     * 易宝代扣流水列表
     * @param withholdFlowReq
     * @return
     */
    Page<YbWithholdFlowVo> getYbWithholdFlowPageList(WithholdFlowReq withholdFlowReq);
}
