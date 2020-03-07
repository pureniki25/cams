package com.hongte.alms.base.service;

import org.springframework.web.multipart.MultipartFile;

import com.hongte.alms.base.entity.CamsSubject;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 科目表 服务类
 * </p>
 *
 * @author czs
 * @since 2019-03-03
 */
public interface CamsSubjectService extends BaseService<CamsSubject> {
	  void initSubjectExcel(MultipartFile file) throws Exception;
}
