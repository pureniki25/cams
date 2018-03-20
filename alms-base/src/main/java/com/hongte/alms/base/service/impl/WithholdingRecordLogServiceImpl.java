package com.hongte.alms.base.service.impl;

import com.hongte.alms.base.entity.WithholdingRecordLog;
import com.hongte.alms.base.mapper.WithholdingRecordLogMapper;
import com.hongte.alms.base.service.WithholdingRecordLogService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 执行代扣记录日志表 服务实现类
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-03-07
 */
@Service("WithholdingRecordLogService")
@Transactional
public class WithholdingRecordLogServiceImpl extends BaseServiceImpl<WithholdingRecordLogMapper, WithholdingRecordLog> implements WithholdingRecordLogService {
	@Autowired
	WithholdingRecordLogMapper withholdingRecordLogMapper;
	@Override
	public List<WithholdingRecordLog> selectWithholdingRecordLog(String originalBusinessId, String afterId) {
		List<WithholdingRecordLog> loglist=withholdingRecordLogMapper.selectWithholdingRecordLog(originalBusinessId, afterId);
		return loglist;
	}

}
