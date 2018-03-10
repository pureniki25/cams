package com.hongte.alms.base.service.impl;

import com.hongte.alms.base.entity.Notice;
import com.hongte.alms.base.mapper.NoticeMapper;
import com.hongte.alms.base.service.NoticeService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * [通知公告详情表] 服务实现类
 * </p>
 *
 * @author 王继光
 * @since 2018-03-07
 */
@Service("NoticeService")
public class NoticeServiceImpl extends BaseServiceImpl<NoticeMapper, Notice> implements NoticeService {

}
