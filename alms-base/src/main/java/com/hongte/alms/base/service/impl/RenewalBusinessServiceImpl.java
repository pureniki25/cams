package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.RenewalBusiness;
import com.hongte.alms.base.mapper.RenewalBusinessMapper;
import com.hongte.alms.base.service.RenewalBusinessService;
import com.hongte.alms.base.vo.module.LoanExtListReq;
import com.hongte.alms.base.vo.module.LoanExtListVO;
import com.hongte.alms.common.service.impl.BaseServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 业务展期续借信息表 服务实现类
 * </p>
 *
 * @author 王继光
 * @since 2018-03-06
 */
@Service("RenewalBusinessService")
public class RenewalBusinessServiceImpl extends BaseServiceImpl<RenewalBusinessMapper, RenewalBusiness> implements RenewalBusinessService {

	@Autowired
	RenewalBusinessMapper renewalBusinessMapper ;
	
	@Override
	public Page<LoanExtListVO> listLoanExt(LoanExtListReq req) {
		Page<LoanExtListVO> page = new Page<>(req.getPage(),req.getLimit());
		int count = renewalBusinessMapper.listLoanExtCount(req);
		page.setTotal(count);
		List<LoanExtListVO> list = renewalBusinessMapper.listLoanExt(req);
		page.setRecords(list);
		return page;
	}
	
}
