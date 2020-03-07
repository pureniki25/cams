package com.hongte.alms.base.service.impl;

import com.hongte.alms.base.entity.BuyDatExe;
import com.hongte.alms.base.mapper.BuyDatExeMapper;
import com.hongte.alms.base.service.BuyDatExeService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 资产表 服务实现类
 * </p>
 *
 * @author czs
 * @since 2019-11-06
 */
@Service("BuyDatExeService")
public class BuyDatExeServiceImpl extends BaseServiceImpl<BuyDatExeMapper, BuyDatExe> implements BuyDatExeService {
	@Override
	public void addAssetJt(BuyDatExe exe) throws Exception {
		BuyDatExe temp=selectById(exe.getId());
		temp.setBankSubject(exe.getBankSubject());
		temp.setBuyDate(exe.getBuyDate());
		temp.setIsTake(exe.getIsTake());
		temp.setJtDate(exe.getJtDate());
		temp.setPeriod(exe.getPeriod());
		temp.setSubject(exe.getSubject());
		updateById(temp);
	}
  
}
