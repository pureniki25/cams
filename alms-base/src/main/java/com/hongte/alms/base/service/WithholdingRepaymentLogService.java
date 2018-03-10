package com.hongte.alms.base.service;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.WithholdingRepaymentLog;
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
     * 分页查询
     * @param key
     * @return
     */
    Page<RepaymentLogVO> selectRepaymentLogPage(RepaymentLogReq key);
    
    
    
     List<RepaymentLogVO> selectRepaymentLogExcel(RepaymentLogReq key);

	  
}
