package com.hongte.alms.base.service;


import java.text.ParseException;
import java.util.List;

import com.hongte.alms.base.entity.BankIncomeDat;
import com.hongte.alms.base.entity.CustomerDat;
import com.hongte.alms.base.entity.JtDat;
import com.hongte.alms.base.invoice.vo.InvoiceBankExel;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 银收 服务类
 * </p>
 *
 * @author czs
 * @since 2019-03-11
 */
public interface BankIncomeDatService extends BaseService<BankIncomeDat> {
	public void addIncomeDat(String riQi,String keMuDaiMa,String bankSubject,String bianHao,String hangHao,String pingZhengHao,String bankType,String feeName,String companyName, CustomerDat customerDat, 
			String shuLiang,String productDate,String faPiaoHao,String buyType,String hanShuiJine,String almsAmount,String shuie,String danJia,boolean isKouChu,String uuid) throws InstantiationException, IllegalAccessException;
	public void addIncomeExcels(List<InvoiceBankExel> incomes,String companyName, String openDate, String bankSubject,String bankType) throws InstantiationException, IllegalAccessException, ParseException;
	public void delete(String companyName, String openDate,String bankSubject) throws InstantiationException, IllegalAccessException;
    public boolean isHaveYhzf(String companyName,String bankSubject,String openDate);
	public String getPingZhengHao(String companyName, int month);
    
    /**
     * 更新银付模块的支付上月个税(1002-01)
     */
    public void updateZhiFuShuiJin(String companyName,String openDate) throws Exception;
    
    /**
     * 转换计提税金成支付税金
     */
    public void tranferJtTaxToZhiFu(List<JtDat> list);
    
    //手动增加
    public void saveIncomeDat(List<BankIncomeDat>  addDats,String openDate,String companyName,String customerCode);

    //通用增加凭证
    public void saveCommonDat(BankIncomeDat incomeDat,String openDate,String companyName,String prekeMuDaiMa,String aftKeMuDaiMa);
}
