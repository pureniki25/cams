package com.hongte.alms.base.service;

import com.hongte.alms.base.assets.car.vo.FileVo;
import com.hongte.alms.base.entity.NoticeFile;
import com.hongte.alms.base.vo.module.doc.UpLoadResult;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * [公告附件登记表] 服务类
 * </p>
 *
 * @author 王继光
 * @since 2018-03-07
 */
public interface NoticeFileService extends BaseService<NoticeFile> {
	public UpLoadResult upload(FileVo fileVo, String userId) ;
}
