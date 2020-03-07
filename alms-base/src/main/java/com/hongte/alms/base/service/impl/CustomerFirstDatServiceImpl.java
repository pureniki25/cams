package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.CustomerFirstDat;
import com.hongte.alms.base.entity.SubjectFirstDat;
import com.hongte.alms.base.invoice.vo.CustomerFirstExcel;
import com.hongte.alms.base.invoice.vo.SubjectFirstExcel;
import com.hongte.alms.base.mapper.CustomerFirstDatMapper;
import com.hongte.alms.base.service.CustomerFirstDatService;
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
 * 单位期初余额表 服务实现类
 * </p>
 *
 * @author czs
 * @since 2020-02-11
 */
@Service("CustomerFirstDatService")
public class CustomerFirstDatServiceImpl extends BaseServiceImpl<CustomerFirstDatMapper, CustomerFirstDat> implements CustomerFirstDatService {

	@Override
	public void importCustomerFirst(MultipartFile file, String companyName) throws Exception {
		String fileName = file.getName();
		ImportParams importParams = new ImportParams();
		List<CustomerFirstExcel> excels = ExcelImportUtil.importExcel(file.getInputStream(), CustomerFirstExcel.class, importParams);
		CustomerFirstDat dat=null;
		for(CustomerFirstExcel excel:excels) {
			CustomerFirstDat firstDat=selectOne(new EntityWrapper<CustomerFirstDat>().eq("customer_code", excel.getCustomerCode()).eq("company_name", companyName));
			if(null!=firstDat) {
				firstDat.setFirstAmount(excel.getFirstAmount());
				firstDat.setCompanyName(companyName);
				firstDat.setCreateTime(new Date());
				firstDat.setCustomerCode(excel.getCustomerCode());
				firstDat.setCustomerName(excel.getCustomerName());
				firstDat.setPeriod(String.valueOf(DateUtil.getYear(new Date())));
				updateById(firstDat);
			}else {
				dat=new CustomerFirstDat();
				dat.setFirstAmount(excel.getFirstAmount());
				dat.setCompanyName(companyName);
				dat.setCreateTime(new Date());
				dat.setCustomerCode(excel.getCustomerCode());
				dat.setCustomerName(excel.getCustomerName());
				dat.setPeriod(String.valueOf(DateUtil.getYear(new Date())));
				insert(dat);
			}
		}
		
	}

}
