package com.hongte.alms.base.service;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.hongte.alms.base.collection.vo.AfterLoanStandingBookReq;
import com.hongte.alms.base.collection.vo.AfterLoanStandingBookVo;
import com.hongte.alms.base.customer.vo.WithholdFlowReq;
import com.hongte.alms.base.entity.WithholdingFlowRecord;
import com.hongte.alms.base.enums.PlatformEnum;
import com.hongte.alms.base.vo.withhold.WithholdingFlowRecordSummaryVo;
import com.hongte.alms.base.vo.withhold.WithholdingFlowRecordVo;
import com.hongte.alms.base.vo.withhold.WithholdingFlowyYbRecordVo;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 代扣平台代扣流水 服务类
 * </p>
 *
 * @author 张贵宏
 * @since 2018-07-16
 */
public interface WithholdingFlowRecordService extends BaseService<WithholdingFlowRecord> {

    /**
     * 导入宝付流水
     * @param settleDate 清算日期YYYY-MM-DD
     */
    void importWidthholdingFlowFromBaoFu(String settleDate);

    /**
     * 导入易宝流水
     * @param settleDate 清算日期YYYY-MM-DD
     */
    void importWidthholdingFlowFromYiBao(String settleDate);

    /**
     * 导入快钱流水
     * @param settleDate 清算日期YYYY-MM-DD
     */
    void importWidthholdingFlowFromKuaiQian(String settleDate);

    /**
     * 查询汇总数据
     */
    WithholdingFlowRecordSummaryVo querySummary(WithholdFlowReq withholdFlowReq);
    
    /**
     * 
     * @param 宝付
     * @return
     */
    Page<WithholdingFlowRecordVo> selectFlowBfRecordPage(WithholdFlowReq key);
    
    Page<WithholdingFlowyYbRecordVo> selectFlowYbRecordPage(WithholdFlowReq req);
    
}
