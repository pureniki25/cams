package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.dto.FactRepayReq;
import com.hongte.alms.base.entity.RepaymentConfirmLogSynch;
import com.hongte.alms.base.mapper.RepaymentConfirmLogSynchMapper;
import com.hongte.alms.base.service.RepaymentConfirmLogSynchService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wangjiguang
 * @since 2018-10-09
 */
@Service("RepaymentConfirmLogSynchService")
public class RepaymentConfirmLogSynchServiceImpl extends BaseServiceImpl<RepaymentConfirmLogSynchMapper, RepaymentConfirmLogSynch> implements RepaymentConfirmLogSynchService {

	@Autowired
	private RepaymentConfirmLogSynchMapper synchMapper ;
	@Override
	public int addRepaymentConfirmLogSynch() {
		return synchMapper.addRepaymentConfirmLogSynch();
	}
	@Override
	public int updateRepaymentConfirmLog() {
		return synchMapper.updateRepaymentConfirmLog();
	}
	@Override
	public int updateBaiscBusiness() {
		return synchMapper.updateBaiscBusiness();
	}
	@Override
	public int updateItem10() {
		return synchMapper.updateItem10();
	}
	@Override
	public int updateItem20() {
		return synchMapper.updateItem20();
	}
	@Override
	public int updateItem30() {
		return synchMapper.updateItem30();
	}
	@Override
	public int updateItem50() {
		return synchMapper.updateItem50();
	}
	@Override
	public int updateItem60online() {
		return synchMapper.updateItem60online();
	}
	@Override
	public int updateItem60offline() {
		return synchMapper.updateItem60offline();
	}
	@Override
	public int updateItem70bj() {
		return synchMapper.updateItem70bj();
	}
	@Override
	public int updateItem70pt() {
		return synchMapper.updateItem70pt();
	}
	@Override
	public int updateItem70fw() {
		return synchMapper.updateItem70fw();
	}
	@Override
	public int updateOtherFee() {
		return synchMapper.updateOtherFee();
	}
	@Override
	public int updateRemark() {
		return synchMapper.updateRemark();
	}
	@Override
	public int updateRepayType() {
		return synchMapper.updateRepayType();
	}
	@Override
	public List<RepaymentConfirmLogSynch> select(FactRepayReq req) {
		return synchMapper.select(req);
	}
	
	@Override
	public Page<RepaymentConfirmLogSynch> page(FactRepayReq req) {
		Page<RepaymentConfirmLogSynch> page = new Page<>(req.getCurPage(), req.getPageSize());
		
		int count = synchMapper.count(req);
		List<RepaymentConfirmLogSynch> list = synchMapper.select(req);
		page.setTotal(count);
		page.setRecords(list);
		return page;
	}

}
