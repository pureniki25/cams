package com.hongte.alms.base.service;

import com.hongte.alms.base.entity.WithholdingRecordLog;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 执行代扣记录日志表 服务类
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-03-07
 */
public interface WithholdingRecordLogService extends BaseService<WithholdingRecordLog> {

	WithholdingRecordLog selectWithholdingRecordLog(String originalBusinessId,String afterId);
	
}
