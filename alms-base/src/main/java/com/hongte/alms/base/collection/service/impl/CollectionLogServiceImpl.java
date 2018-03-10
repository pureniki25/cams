package com.hongte.alms.base.collection.service.impl;

import com.hongte.alms.base.collection.entity.CollectionLog;
import com.hongte.alms.base.collection.mapper.CollectionLogMapper;
import com.hongte.alms.base.collection.service.CollectionLogService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 贷后移交催收日志 服务实现类
 * </p>
 *
 * @author 曾坤
 * @since 2018-01-25
 */
@Service("CollectionLogService")
public class CollectionLogServiceImpl extends BaseServiceImpl<CollectionLogMapper, CollectionLog> implements CollectionLogService {

}
