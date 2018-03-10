package com.hongte.alms.base.service.impl;

import com.hongte.alms.base.entity.NoticeFile;
import com.hongte.alms.base.mapper.NoticeFileMapper;
import com.hongte.alms.base.service.NoticeFileService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * [公告附件登记表] 服务实现类
 * </p>
 *
 * @author 王继光
 * @since 2018-03-07
 */
@Service("NoticeFileService")
public class NoticeFileServiceImpl extends BaseServiceImpl<NoticeFileMapper, NoticeFile> implements NoticeFileService {

}
