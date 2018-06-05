package com.hongte.alms.base.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.DepartmentBank;
import com.hongte.alms.base.mapper.BasicBusinessMapper;
import com.hongte.alms.base.mapper.DepartmentBankMapper;
import com.hongte.alms.base.service.DepartmentBankService;
import com.hongte.alms.base.vo.module.DepartmentBankVO;
import com.hongte.alms.common.service.impl.BaseServiceImpl;

/**
 * <p>
 * 线下还款账户配置表 服务实现类
 * </p>
 *
 * @author 王继光
 * @since 2018-03-05
 */
@Service("DepartmentBankService")
public class DepartmentBankServiceImpl extends BaseServiceImpl<DepartmentBankMapper, DepartmentBank> implements DepartmentBankService {
	@Autowired
	DepartmentBankMapper departmentBankMapper ;
	@Autowired
	BasicBusinessMapper basicBusinessMapper ;
	@Override
	public List<DepartmentBankVO> listDepartmentBank(String businessId) {
		BasicBusiness basicBusiness = new BasicBusiness() ;
		basicBusiness.setBusinessId(businessId);
		basicBusiness = basicBusinessMapper.selectOne(basicBusiness);
		if (basicBusiness==null) {
			throw new RuntimeException("business was not found");
		}
		EntityWrapper<DepartmentBank> ew = new EntityWrapper<>() ;
		ew.eq("dept_id", basicBusiness.getCompanyId()).orderBy("finance_name") ;
		List<DepartmentBank> list = departmentBankMapper.selectList(ew);
		List<DepartmentBankVO> voList = new ArrayList<>() ;
		for (DepartmentBank departmentBank : list) {
			voList.add(new DepartmentBankVO(departmentBank));
		}
		return voList;
	}

	@Override
	public List<DepartmentBank> listDepartmentBank() {
		return departmentBankMapper.selectList(new EntityWrapper<DepartmentBank>()
				.ne("repayment_id", "").groupBy("repayment_id").orderBy("finance_name"));
	}
}
