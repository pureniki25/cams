package com.hongte.alms.base.service.impl;

import com.hongte.alms.base.entity.BizOutputRecord;
import com.hongte.alms.base.mapper.BizOutputRecordMapper;
import com.hongte.alms.base.service.BizOutputRecordService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 业务实际出款记录表 服务实现类
 * </p>
 *
 * @author 王继光
 * @since 2018-03-06
 */
@Service("BizOutputRecordService")
public class BizOutputRecordServiceImpl extends BaseServiceImpl<BizOutputRecordMapper, BizOutputRecord> implements BizOutputRecordService {

}
