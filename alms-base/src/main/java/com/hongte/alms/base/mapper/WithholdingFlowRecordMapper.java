package com.hongte.alms.base.mapper;

import java.util.List;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.hongte.alms.base.customer.vo.WithholdFlowReq;
import com.hongte.alms.base.entity.WithholdingFlowRecord;
import com.hongte.alms.base.vo.withhold.WithholdingFlowRecordSummaryVo;
import com.hongte.alms.base.vo.withhold.WithholdingFlowRecordVo;
import com.hongte.alms.common.mapper.SuperMapper;

/**
 * <p>
 * 代扣平台代扣流水 Mapper 接口
 * </p>
 *
 * @author 张贵宏
 * @since 2018-07-16
 */
public interface WithholdingFlowRecordMapper extends SuperMapper<WithholdingFlowRecord> {
    WithholdingFlowRecordSummaryVo querySummary(WithholdFlowReq withholdFlowReq);
    List<WithholdingFlowRecordVo> selectFlowBfRecordPage(Pagination pages,WithholdFlowReq req);
}
