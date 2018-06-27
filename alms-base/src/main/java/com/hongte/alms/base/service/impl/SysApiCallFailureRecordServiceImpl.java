package com.hongte.alms.base.service.impl;

import com.hongte.alms.base.entity.SysApiCallFailureRecord;
import com.hongte.alms.base.mapper.SysApiCallFailureRecordMapper;
import com.hongte.alms.base.service.SysApiCallFailureRecordService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * API调用失败记录表 服务实现类
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-06-26
 */
@Service("SysApiCallFailureRecordService")
public class SysApiCallFailureRecordServiceImpl extends BaseServiceImpl<SysApiCallFailureRecordMapper, SysApiCallFailureRecord> implements SysApiCallFailureRecordService {

	@Autowired
	private SysApiCallFailureRecordMapper sysApiCallFailureRecordMapper;

	@Override
	public List<SysApiCallFailureRecord> queryCallFailedDataByApiCode(String apiCode) {
		return sysApiCallFailureRecordMapper.queryCallFailedDataByApiCode(apiCode);
	}

}
