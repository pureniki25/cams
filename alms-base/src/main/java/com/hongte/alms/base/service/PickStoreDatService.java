package com.hongte.alms.base.service;

import org.springframework.web.multipart.MultipartFile;

import com.hongte.alms.base.entity.CamsSubject;
import com.hongte.alms.base.entity.PickStoreDat;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 领料入库表 服务类
 * </p>
 *
 * @author czs
 * @since 2019-07-22
 */
public interface PickStoreDatService extends BaseService<PickStoreDat> {
	
	/**
	 * 导入领料入库excel
	 * @param file
	 * @param companyName
	 * @param type  1：领料单 2：入库单
	 * @param openDate
	 * @throws Exception
	 */
	void importPick(MultipartFile file, String companyName, String type, String openDate,CamsSubject subject)
			throws Exception;
	
	void addPick( String companyName,  String openDate,PickStoreDat dat)
			throws Exception;
	
	void generatePcik( String companyName,  String openDate,PickStoreDat dat)
			throws Exception;
}
