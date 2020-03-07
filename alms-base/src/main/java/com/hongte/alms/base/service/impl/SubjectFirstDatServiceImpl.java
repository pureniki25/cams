package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.SubjectFirstDat;
import com.hongte.alms.base.invoice.vo.PickExcel;
import com.hongte.alms.base.invoice.vo.SubjectFirstExcel;
import com.hongte.alms.base.mapper.SubjectFirstDatMapper;
import com.hongte.alms.base.service.SubjectFirstDatService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.DateUtil;

import java.util.Date;
import java.util.List;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 科目期初余额表 服务实现类
 * </p>
 *
 * @author czs
 * @since 2020-01-20
 */
@Service("SubjectFirstDatService")
public class SubjectFirstDatServiceImpl extends BaseServiceImpl<SubjectFirstDatMapper, SubjectFirstDat> implements SubjectFirstDatService {

	@Override
	public void importSubjectFirst(MultipartFile file, String companyName) throws Exception {
		String fileName = file.getName();
		ImportParams importParams = new ImportParams();
		List<SubjectFirstExcel> excels = ExcelImportUtil.importExcel(file.getInputStream(), SubjectFirstExcel.class, importParams);
		SubjectFirstDat dat=null;
		for(SubjectFirstExcel excel:excels) {
			SubjectFirstDat firstDat=selectOne(new EntityWrapper<SubjectFirstDat>().eq("subject", excel.getSubject()).eq("company_name", companyName));
			if(null!=firstDat) {
				firstDat.setFirstAmount(excel.getFirstAmount());
				firstDat.setCompanyName(companyName);
				firstDat.setCreateTime(new Date());
				firstDat.setSubject(excel.getSubject());
				firstDat.setSubjectName(excel.getSubjectName());
				firstDat.setPeriod(String.valueOf(DateUtil.getYear(new Date())));
				updateById(firstDat);
			}else {
				dat=new SubjectFirstDat();
				dat.setCompanyName(companyName);
				dat.setCreateTime(new Date());
				dat.setSubject(excel.getSubject());
				dat.setSubjectName(excel.getSubjectName());
				dat.setFirstAmount(excel.getFirstAmount());
				dat.setPeriod(String.valueOf(DateUtil.getYear(new Date())));
				insert(dat);
			}
		}
		
	}


}
