package com.hongte.alms.base.service.impl;

import com.hongte.alms.base.entity.BankPaymentDat;
import com.hongte.alms.base.entity.CustomerDat;
import com.hongte.alms.base.mapper.BankPaymentDatMapper;
import com.hongte.alms.base.service.BankPaymentDatService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 银付 服务实现类
 * </p>
 *
 * @author czs
 * @since 2019-03-11
 */
@Service("BankPaymentDatService")
public class BankPaymentDatServiceImpl extends BaseServiceImpl<BankPaymentDatMapper, BankPaymentDat> implements BankPaymentDatService {

	@Override
	public void addPaymentDat(String feeName, String companyName, CustomerDat customerDat, String shuLiang,
			String productDate, String faPiaoHao, String buyType, String hanShuiJine, String buHanShuiJine,
			String shuie, String danJia) throws InstantiationException, IllegalAccessException {
		// TODO Auto-generated method stub
		
	}

}
