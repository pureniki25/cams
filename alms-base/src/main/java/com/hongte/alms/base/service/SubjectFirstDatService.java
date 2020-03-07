package com.hongte.alms.base.service;

import org.springframework.web.multipart.MultipartFile;

import com.hongte.alms.base.entity.CamsSubject;
import com.hongte.alms.base.entity.SubjectFirstDat;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 科目期初余额表 服务类
 * </p>
 *
 * @author czs
 * @since 2020-01-20
 */
public interface SubjectFirstDatService extends BaseService<SubjectFirstDat> {
	
	void importSubjectFirst(MultipartFile file, String companyName)
			throws Exception;

}
