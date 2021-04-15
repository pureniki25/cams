package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.hongte.alms.base.entity.BuyDat;
import com.hongte.alms.base.entity.BuyDatExe;
import com.hongte.alms.base.entity.SellDat;
import com.hongte.alms.base.enums.YNEnum;
import com.hongte.alms.base.mapper.BuyDatMapper;
import com.hongte.alms.base.service.BuyDatExeService;
import com.hongte.alms.base.service.BuyDatService;
import com.hongte.alms.base.service.CamsProductPropertiesService;
import com.hongte.alms.base.service.CustomerDatService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.CamsUtil;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.StringUtil;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 采购单表 服务实现类
 * </p>
 *
 * @author czs
 * @since 2019-02-07
 */
@Service("BuyDatService")
public class BuyDatServiceImpl extends BaseServiceImpl<BuyDatMapper, BuyDat> implements BuyDatService {

	
	@Autowired
	@Qualifier("BuyDatExeService")
	BuyDatExeService buyDatExeService;
	
	@Override
	public BuyDat addBuyDat(String companyName, String productCode, String customerCode, String accountPeriod,
			String produceDate, String invoiceNumber, String rowNumber, String calUnit, String number, String unitPrice,
			String hanShuiJine, String buHanShuiJine, String taxRate, String originalTax, String localhostTax,String buyType,Integer idx ,String isAssets) {
		
		if(!StringUtil.isEmpty(number)) {//保留2位小数
			 DecimalFormat df = new DecimalFormat("#0.00");
			 number=df.format(Double.valueOf(number)).toString();
		}
		int month=DateUtil.getMonth(DateUtil.getDate(produceDate));
	
		
		String documentNo="";
		/*获取单据号 */
		 Wrapper<BuyDat> wrapperBuyDat= new EntityWrapper<BuyDat>();
			 wrapperBuyDat.eq("company_code",companyName);
			 wrapperBuyDat.and(" month(produce_date)="+month);
			 wrapperBuyDat.orderBy("document_no",false); 
			 List<BuyDat> sellDats=selectList(wrapperBuyDat);
			 if(sellDats.size()==0) {
				 documentNo="0001";
			 }else {
				 BuyDat dat=selectOne(new EntityWrapper<BuyDat>().eq("company_code", companyName).eq("invoice_number", invoiceNumber));
				 if(dat!=null) {
					 documentNo=dat.getDocumentNo();
				 }else {
					 documentNo= CamsUtil.generateCode(sellDats.get(0).getDocumentNo());
				 }
			 }
		  /*获取行号 */
			 Wrapper<BuyDat> wrapperBuyDat2= new EntityWrapper<BuyDat>();
			 wrapperBuyDat2.eq("company_code",companyName);
			 wrapperBuyDat2.eq("invoice_number",invoiceNumber);
			 wrapperBuyDat2.orderBy("row_number",false); 
			 List<BuyDat> sellDats2=selectList(wrapperBuyDat2);
			 Collections.sort(sellDats2, new Comparator<BuyDat>() {
				    @Override
				    public int compare(BuyDat o1, BuyDat o2) {
				        return Integer.valueOf(o2.getRowNumber())-(Integer.valueOf(o1.getRowNumber()));
				    }
				});
			 if(sellDats2.size()==0) {
				 rowNumber="1";
			 }else {
				 rowNumber= CamsUtil.generateCode(sellDats2.get(0).getRowNumber());
			 }
			 
		BuyDat buyDat=new BuyDat();
		if(("普票").equals(buyType)) {
			buyDat.setDeductionType("3");//不能抵扣
			buyDat.setTaxRate("0%");
			buyDat.setLocalAmount(hanShuiJine);
			buyDat.setOriginalAmount(hanShuiJine);
		}else {
			buyDat.setDeductionType("1");//待抵扣
            if(taxRate!=null&&!taxRate.contains("%")) {
            	taxRate="待抵扣"+CamsUtil.getBuyTax(taxRate)+"%";
			}else if(taxRate==null){
				taxRate="";
			}else {
				taxRate="待抵扣"+CamsUtil.getBuyTax(taxRate);
			}
			buyDat.setTaxRate(taxRate);
			buyDat.setLocalAmount(buHanShuiJine);
			buyDat.setOriginalAmount(buHanShuiJine);
		}
		buyDat.setBuyType(buyType);
		buyDat.setDocumentNo(documentNo);
		buyDat.setidx(idx);
		buyDat.setIsZanGu(YNEnum.NO.getValue().toString());
		buyDat.setCompanyCode(companyName);
		buyDat.setProduceCode(productCode);
		buyDat.setCustomerCode(customerCode);
		buyDat.setAccountPeriod(accountPeriod);
		buyDat.setProduceDate(produceDate);
		buyDat.setDueDate(produceDate);
		buyDat.setOpenDate(produceDate);
		buyDat.setInvoiceNumber(invoiceNumber);
		buyDat.setRowNumber(rowNumber);
		buyDat.setCalUnit(calUnit);
		buyDat.setNumber(number);
		buyDat.setUnitPrice(unitPrice);
		buyDat.setOriginalTax(originalTax);
		buyDat.setLocalhostTax(localhostTax);
		buyDat.setDanJuLeiXing("2");
		buyDat.setYeWuLeiXing("1");
		buyDat.setKuaiJiNianDu(String.valueOf(DateUtil.getYear(DateUtil.getDate(produceDate))));
		buyDat.setCreateTime(new Date());
		buyDat.setMoBan("商品采购清单");
		
		

		insertOrUpdate(buyDat);
		
        //判断是否要新增资产
		if(!StringUtil.isEmpty(isAssets)) {
			BuyDatExe ext=new BuyDatExe();
			ext.setId(UUID.randomUUID().toString());
			ext.setBuyId(buyDat.getBuyId());
			ext.setAmount(buyDat.getLocalAmount());
			ext.setTax(buyDat.getLocalhostTax());
			ext.setBuyDate(buyDat.getOpenDate());
			ext.setCompanyName(buyDat.getCompanyCode());
			ext.setCreateTime(new Date());
            ext.setInvoiceNumber(buyDat.getInvoiceNumber());
            ext.setProductCode(buyDat.getProduceCode());
            buyDatExeService.insert(ext);
		}
		
		return buyDat;
	}

}
