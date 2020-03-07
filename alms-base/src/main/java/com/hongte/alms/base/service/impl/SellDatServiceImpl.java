package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.hongte.alms.base.entity.CamsProductProperties;
import com.hongte.alms.base.entity.CustomerDat;
import com.hongte.alms.base.entity.SellDat;
import com.hongte.alms.base.mapper.SellDatMapper;
import com.hongte.alms.base.service.CamsProductPropertiesService;
import com.hongte.alms.base.service.SellDatService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.CamsUtil;
import com.hongte.alms.common.util.StringUtil;
import com.ht.ussp.util.DateUtil;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 销售单表 服务实现类
 * </p>
 *
 * @author czs
 * @since 2019-02-07
 */
@Service("SellDatService")
public class SellDatServiceImpl extends BaseServiceImpl<SellDatMapper, SellDat> implements SellDatService {
    @Autowired
    @Qualifier("CamsProductPropertiesService")
    private CamsProductPropertiesService camsProductPropertiesService;
    
  
	@Override
	public SellDat addSellDat(String productPropertiesName,String companyName, String productCode, String customerCode, String accountPeriod,
			String produceDate, String invoiceNumber, String rowNumber, String calUnit, String number, String unitPrice,
			String hanShuiJine, String buHanShuiJine, String taxRate, String originalTax, String localhostTax,String importType,Integer idx) {
		CamsProductProperties properties= camsProductPropertiesService.selectOne(new EntityWrapper<CamsProductProperties>().eq("product_properties_name",productPropertiesName ));
		if(!StringUtil.isEmpty(number)) {//保留2位小数
			 DecimalFormat df = new DecimalFormat("#0.00");
			 number=df.format(Double.valueOf(number)).toString();
		}else {
			number="";
		}

		Date date=DateUtil.getDate(produceDate);
		produceDate=DateUtil.formatDate(date);
		int month=DateUtil.getMonth(DateUtil.getDate(produceDate));
		String documentNo="";
		/*获取单据号 */
		 Wrapper<SellDat> wrapperProductDat= new EntityWrapper<SellDat>();
			 wrapperProductDat.eq("company_code",companyName);
			 wrapperProductDat.and(" month(produce_date)="+month); 
			 wrapperProductDat.orderBy("document_no",false); 
			 List<SellDat> sellDats=selectList(wrapperProductDat);
			 if(sellDats.size()==0) {
				 documentNo="0001";
			 }else {
				 SellDat dat=selectOne(new EntityWrapper<SellDat>().eq("company_code", companyName).eq("invoice_number", invoiceNumber));
				 if(dat!=null) {
					 documentNo=dat.getDocumentNo();
				 }else {
					 documentNo= CamsUtil.generateCode(sellDats.get(0).getDocumentNo());
				 }
			 }
			 
		  /*获取行号 */
			 Wrapper<SellDat> wrapperProductDat2= new EntityWrapper<SellDat>();
			 wrapperProductDat2.eq("company_code",companyName);
			 wrapperProductDat2.eq("invoice_number",invoiceNumber);
			 wrapperProductDat2.orderBy("row_number",false); 
			 List<SellDat> sellDats2=selectList(wrapperProductDat2);
			 Collections.sort(sellDats2, new Comparator<SellDat>() {
				    @Override
				    public int compare(SellDat o1, SellDat o2) {
				        return Integer.valueOf(o2.getRowNumber())-(Integer.valueOf(o1.getRowNumber()));
				    }
				});

			 if(sellDats2.size()==0) {
				 rowNumber="1";
			 }else {
				 rowNumber= CamsUtil.generateCode(sellDats2.get(0).getRowNumber());
			 }
			 
			 
		SellDat sellDat=new SellDat();
		sellDat.setDocumentNo(documentNo);
		sellDat.setidx(idx);
		sellDat.setCompanyCode(companyName);
		sellDat.setProduceCode(productCode);
		sellDat.setCustomerCode(customerCode);
		sellDat.setAccountPeriod(accountPeriod);
		sellDat.setProduceDate(produceDate);
		sellDat.setDueDate(produceDate);
		sellDat.setOpenDate(produceDate);
		sellDat.setInvoiceNumber(invoiceNumber);
		sellDat.setRowNumber(rowNumber);
		sellDat.setCalUnit(calUnit);
		sellDat.setNumber(number);
		sellDat.setUnitPrice(unitPrice);
		sellDat.setOriginalAmount(buHanShuiJine);
		sellDat.setLocalAmount(buHanShuiJine);
		sellDat.setTaxRate(CamsUtil.getTax(taxRate));
		sellDat.setOriginalTax(originalTax);
		sellDat.setLocalhostTax(localhostTax);
		sellDat.setDanJuLeiXing("13");
		sellDat.setYeWuLeiXing("11");
		sellDat.setKuaiJiNianDu(String.valueOf(DateUtil.getYear(new Date())));
		sellDat.setCreateTime(new Date());
	    sellDat.setMoBan("销售商品清单");
	    sellDat.setSellType(productPropertiesName);
	    sellDat.setImportType(importType);
		
		insertOrUpdate(sellDat);
		
		return sellDat;
	}
	public  int countLowercaseLetters(String string) {
        return (int)string.chars().filter(Character::isLowerCase).count();
    }

public static void main(String[] args) {
	 System.out.println(String.valueOf(DateUtil.getYear(new Date())));
	 
}
}
