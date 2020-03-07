package com.hongte.alms.base.service;


import com.hongte.alms.base.entity.BankPaymentDat;
import com.hongte.alms.base.entity.CustomerDat;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 银付 服务类
 * </p>
 *
 * @author czs
 * @since 2019-03-11
 */
public interface BankPaymentDatService extends BaseService<BankPaymentDat> {
	public void addPaymentDat(String feeName,String companyName, CustomerDat customerDat, 
			String shuLiang,String productDate,String faPiaoHao,String buyType,String hanShuiJine,String buHanShuiJine,String shuie,String danJia) throws InstantiationException, IllegalAccessException;

}
