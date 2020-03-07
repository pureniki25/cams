package com.hongte.alms.base.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.DepartmentBank;
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


	@Override
	public List<DepartmentBank> listDepartmentBank() {
		return departmentBankMapper.selectList(new EntityWrapper<DepartmentBank>()
				.ne("repayment_id", "").groupBy("repayment_id").orderBy("finance_name"));
	}
}
