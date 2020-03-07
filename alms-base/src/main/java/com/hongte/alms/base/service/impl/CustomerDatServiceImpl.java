package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.base.CharMatcher;
import com.hongte.alms.base.entity.CustomerDat;
import com.hongte.alms.base.excel.CustomerExel;
import com.hongte.alms.base.invoice.vo.InvoiceBankExel;
import com.hongte.alms.base.invoice.vo.InvoiceCustomerExcel;
import com.hongte.alms.base.mapper.CustomerDatMapper;
import com.hongte.alms.base.service.CustomerDatService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.CamsUtil;
import com.hongte.alms.common.util.StringUtil;



import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 客户与供应商表 服务实现类
 * </p>
 *
 * @author czs
 * @since 2019-01-20
 */
@Service("CustomerDatService")
public class CustomerDatServiceImpl extends BaseServiceImpl<CustomerDatMapper, CustomerDat> implements CustomerDatService {
	private Logger logger = LoggerFactory.getLogger(CustomerDatServiceImpl.class);
	@Override
	public CustomerDat addCustomerDat(String customerName,String type,String companyName,String kaiPiaoRiQi) {
		CustomerDat customerDat=null;
		if(customerName.contains("曼秀")) {
			System.out.println("stop");
		}
		if(customerName!=null) {
			customerName=customerName.replace("?", "");
			customerName=customerName.replace("？", "");
			customerName=StringUtil.cToe(customerName).trim();
			customerName=CharMatcher.WHITESPACE.trimFrom(customerName);
		}
		 customerDat=selectOne(new EntityWrapper<CustomerDat>().eq("customer_name",  customerName).eq("company_code", companyName));
         if(customerDat==null) {
        	 customerDat=new CustomerDat();
        	 customerDat.setCompanyCode(companyName);
        	 
        	List<CustomerDat> dats=selectList(new EntityWrapper<CustomerDat>().eq("company_code", companyName).eq("type", type).orderBy("customer_code",false));
        	if(type.equals("1")&&dats.size()==0) {//客户
        		customerDat.setCustomerCode("A0001");
        	}else if(type.equals("2")&&dats.size()==0) {//供应商
        		customerDat.setCustomerCode("B0001");
          	}else if(dats.size()>0) {
        	  String customerCode=CamsUtil.generateCode(dats.get(0).getCustomerCode());
        	  customerDat.setCustomerCode(customerCode);
        	}
        	customerDat.setType(Integer.valueOf(type));
        	customerDat.setCustomerType("3");
        	customerDat.setCustomerName(customerName);
        	customerDat.setCustomerCategory("003");
        	customerDat.setXinYongEDu("0");
        	customerDat.setYingshouCategory("1131");
        	customerDat.setYingfuCategory("2121");
        	customerDat.setOpenDate(kaiPiaoRiQi);
        	customerDat.setCreateTime(new Date());
        	insert(customerDat);
         }
         return customerDat;
		
	}

	@Override
	public void importCustomerDat(MultipartFile file,String companyName) throws  Exception {
		InputStream fis = getFis(file);
		ImportParams importParams = new ImportParams();
		List<InvoiceCustomerExcel> excels = ExcelImportUtil.importExcel(file.getInputStream(), InvoiceCustomerExcel.class, importParams);
	
		for(InvoiceCustomerExcel excel:excels) {
			addCustomerDatByExcel(excel.getCustomerCode(), excel.getCustomerName(), "", companyName);
		}
	}
	private InputStream getFis(MultipartFile file) {
		InputStream fis = null;
		try {
			fis = file.getInputStream();
			byte[] buffer = null;
			ByteArrayOutputStream bos = new ByteArrayOutputStream();

			byte[] b = new byte[1024];

			int n;

			while ((n = fis.read(b)) != -1) {
				bos.write(b, 0, n);
			}
			bos.flush();
			buffer = bos.toByteArray();

		} catch (Exception e) {
			logger.info("上传oss过程失败 {}", e);
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return fis;

	}
	@Override
	public void addCustomerDatByExcel(String customerCode, String customerName, String type,
			String companyName) {
		
		CustomerDat customerDat=selectOne(new EntityWrapper<CustomerDat>().eq("customer_code",  customerCode).eq("company_code", companyName));
	
		if(customerDat!=null) {
			return;
		}else {
			if(customerCode.contains("A")) {//客户
        		type="1";
        	}else {//供应商
        		type="2";
          	}
		    customerDat=new CustomerDat();
        	customerDat.setCustomerCode(customerCode);
        	customerDat.setType(Integer.valueOf(type));
        	customerDat.setCustomerType("3");
        	customerDat.setCustomerName(customerName);
        	customerDat.setCompanyCode(companyName);
        	customerDat.setCustomerCategory("003");
        	customerDat.setXinYongEDu("0");
        	customerDat.setYingshouCategory("1131");
        	customerDat.setYingfuCategory("2121");
        	customerDat.setCreateTime(new Date());
        	insert(customerDat);
		}
	}
	   public static void main(String[] args) {
			  String str="上海高力国际物业服务有限公司万胜广场物业服务中心　";
			  str=str.replace(" ", "");
			  System.out.println(CharMatcher.WHITESPACE.trimFrom(str.trim()));
		}
}
