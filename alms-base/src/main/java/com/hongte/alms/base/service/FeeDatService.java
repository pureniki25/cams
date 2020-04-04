package com.hongte.alms.base.service;

import java.math.BigDecimal;
import java.util.List;

import com.hongte.alms.base.entity.CamsCash;
import com.hongte.alms.base.entity.CamsSubject;
import com.hongte.alms.base.entity.CustomerDat;
import com.hongte.alms.base.entity.FeeDat;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 费用表 服务类
 * </p>
 *
 * @author czs
 * @since 2019-03-02
 */
public interface FeeDatService extends BaseService<FeeDat> {
	public void addFeeDat(String feeName,String companyName, CustomerDat customerDat, 
			String shuLiang,String productDate,String faPiaoHao,String buyType,String hanShuiJine,String buHanShuiJine,String shuie,String danJia,String feeType) throws InstantiationException, IllegalAccessException;

	public void addSellPingZheng(String feeName,String companyName, CustomerDat customerDat, 
			String shuLiang,String productDate,String faPiaoHao,String buyType,String hanShuiJine,String buHanShuiJine,String shuie,String danJia,String feeType,String productCode,BigDecimal cash) throws InstantiationException, IllegalAccessException;

	
	public void addBuyPingZheng(String feeName,String companyName, CustomerDat customerDat, 
			String shuLiang,String productDate,String faPiaoHao,String buyType,String hanShuiJine,String buHanShuiJine,String shuie,String danJia,String feeType) throws InstantiationException, IllegalAccessException;

	
	void deleteFee(String openDate,String companyName) throws Exception;
	
	
	/**
	 * 保存现金费用
	 * @param feeName  费用名称
	 * @param companyName  公司名称
	 * @param productDate 开票日期
	 * @param danJia 单价
	 * @param keMuDaiMa 科目代码
	 * @param type 费用类型
	 * @throws Exception
	 */
	public void saveCash(String feeName,String companyName, 
			String productDate,String danJia,String keMuDaiMa,String type) throws Exception;
	

	/**
	 * 保存税金
	 * @param companyName
	 * @param keMuDaiMa
	 * @param type
	 * @throws Exception
	 */
	public void saveShuiJin(List<CamsSubject> list,String companyName, String productDate,String type) throws Exception;
	

	/**
	 * 获取凭证号
	 * @param companyName
	 * @param keMuDaiMa
	 * @param type
	 * @throws Exception
	 */
	public String getFeePingZhengHao(String companyName, String customerCode,String date,String feeType) throws Exception;
	
	   
}
