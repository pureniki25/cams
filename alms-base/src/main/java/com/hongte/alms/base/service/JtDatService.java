package com.hongte.alms.base.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.hongte.alms.base.entity.BankIncomeDat;
import com.hongte.alms.base.entity.CustomerDat;
import com.hongte.alms.base.entity.JtDat;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 计提表 服务类
 * </p>
 *
 * @author czs
 * @since 2019-09-01
 */
public interface JtDatService extends BaseService<JtDat> {

	public void importTax(MultipartFile file,  String type, String openDate) throws Exception ;
	public String getPingZhengHao(String companyName, String openDate) throws Exception;
	public void addGeRenSuoDeShui(String companyName, BigDecimal tax, String openDate) throws Exception;
	
	public void importZiChan(MultipartFile file, String companyName, String lastday) throws Exception;
	
	public void addZiChan(String feeName,String companyName, CustomerDat customerDat, 
			String shuLiang,String productDate,String faPiaoHao,String buyType,String hanShuiJine,String buHanShuiJine,String shuie,String danJia) throws Exception;
	
	
	/**
	 * 增加待抵扣税计提记录
	 * @param localJine
	 * @param invoiceNumber
	 * @param openDate
	 * @param pingZhengHao
	 * @param type
	 * @param companyName
	 * @throws Exception
	 */
	public void addTaxJt(String localJine,String invoiceNumber, String openDate,
			String pingZhengHao,String type,String companyName) throws Exception;
	
    //手动增加
    public void saveJtDat(List<JtDat>  addDats,String openDate,String companyName,String customerCode,String jtType);

	//通用增加凭证
	public void saveCommonDat(JtDat jtDat,String openDate,String companyName,String prekeMuDaiMa,String aftKeMuDaiMa);
		
}
