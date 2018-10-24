package com.hongte.alms.base.service;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.dto.FactRepayReq;
import com.hongte.alms.base.entity.RepaymentConfirmLogSynch;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author wangjiguang
 * @since 2018-10-09
 */
public interface RepaymentConfirmLogSynchService extends BaseService<RepaymentConfirmLogSynch> {

	public int synch () ;
	
	public int addRepaymentConfirmLogSynch();

	public int updateRepaymentConfirmLog();

	public int updateBaiscBusiness();

	public int updateItem10();

	public int updateItem20();

	public int updateItem30();

	public int updateItem50();

	public int updateItem60online();

	public int updateItem60offline();

	public int updateItem70bj();

	public int updateItem70pt();

	public int updateItem70fw();

	public int updateOtherFee();

	public int updateRemark();
	
	public int updateRepayType() ;
	
	public int updatePlateType();
	
	public List<RepaymentConfirmLogSynch> select(FactRepayReq req) ;
	
	public Page<RepaymentConfirmLogSynch> page(FactRepayReq req) ;
}
