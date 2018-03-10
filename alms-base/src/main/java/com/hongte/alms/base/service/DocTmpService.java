package com.hongte.alms.base.service;

import com.hongte.alms.base.assets.car.vo.FileVo;
import com.hongte.alms.base.entity.DocTmp;
import com.hongte.alms.base.vo.module.doc.UpLoadResult;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 上传文件临时表 服务类
 * </p>
 *
 * @author 王继光
 * @since 2018-03-08
 */
public interface DocTmpService extends BaseService<DocTmp> {
	   public UpLoadResult upload(FileVo fileVo, String userId);
}
