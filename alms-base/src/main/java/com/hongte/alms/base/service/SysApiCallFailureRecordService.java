package com.hongte.alms.base.service;

import java.util.List;

import com.hongte.alms.base.entity.SysApiCallFailureRecord;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * API调用失败记录表 服务类
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-06-26
 */
public interface SysApiCallFailureRecordService extends BaseService<SysApiCallFailureRecord> {

	List<SysApiCallFailureRecord> queryCallFailedDataByApiCode(String apiCode);
}
