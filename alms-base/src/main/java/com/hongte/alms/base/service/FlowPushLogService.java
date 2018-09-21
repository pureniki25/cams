package com.hongte.alms.base.service;

import java.util.List;

import com.hongte.alms.base.entity.FlowPushLog;
import com.hongte.alms.base.vo.compliance.DistributeFundRecordVO;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 刘正全
 * @since 2018-09-05
 */
public interface FlowPushLogService extends BaseService<FlowPushLog> {

	List<DistributeFundRecordVO> queryDistributeFundRecord(String projectId);

}
