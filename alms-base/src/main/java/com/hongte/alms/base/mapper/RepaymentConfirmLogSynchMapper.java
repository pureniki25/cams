package com.hongte.alms.base.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hongte.alms.base.dto.FactRepayReq;
import com.hongte.alms.base.entity.RepaymentConfirmLogSynch;
import com.hongte.alms.common.mapper.SuperMapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author wangjiguang
 * @since 2018-10-09
 */
public interface RepaymentConfirmLogSynchMapper extends SuperMapper<RepaymentConfirmLogSynch> {

	public int countNeedSynch();
	
	public int addRepaymentConfirmLogSynch();

	public int updateRepaymentConfirmLog();

	public int updateBaiscBusiness();
	
	public int updateUser() ;

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
	
	public int count(@Param("req") FactRepayReq req);
	
	public List<RepaymentConfirmLogSynch> select(@Param("req") FactRepayReq req);
}
