package com.hongte.alms.base.service;

import com.hongte.alms.base.entity.WithholdingFlowRecord;
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
}
