package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.CamsSubject;
import com.hongte.alms.base.entity.SellDat;
import com.hongte.alms.base.invoice.vo.InvoiceExel;
import com.hongte.alms.base.invoice.vo.SubjectInitExcel;
import com.hongte.alms.base.mapper.CamsSubjectMapper;
import com.hongte.alms.base.service.CamsSubjectService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;

import java.util.List;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 科目表 服务实现类
 * </p>
 *
 * @author czs
 * @since 2019-03-03
 */
@Service("CamsSubjectService")
public class CamsSubjectServiceImpl extends BaseServiceImpl<CamsSubjectMapper, CamsSubject> implements CamsSubjectService {
	@Override
	public void initSubjectExcel(MultipartFile file) throws Exception {
		ImportParams importParams = new ImportParams();
		List<SubjectInitExcel> excels = ExcelImportUtil.importExcel(file.getInputStream(), SubjectInitExcel.class, importParams);
		for(SubjectInitExcel excel:excels) {
			CamsSubject camsSubject=selectOne(new EntityWrapper<CamsSubject>().eq("id", excel.getSubject()));
			if(null!=camsSubject) {
				camsSubject.setDirection(excel.getDirection());
				updateById(camsSubject);
			}else {
				CamsSubject camsSubjectNew=new CamsSubject();
				camsSubjectNew.setId(excel.getSubject());
				camsSubjectNew.setSubjectName(excel.getSubjectName());
				camsSubjectNew.setDirection(excel.getDirection());
				insert(camsSubjectNew);
			}
	   	
		}
		
	}

}
