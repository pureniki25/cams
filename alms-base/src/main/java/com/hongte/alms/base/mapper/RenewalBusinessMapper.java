package com.hongte.alms.base.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hongte.alms.base.entity.RenewalBusiness;
import com.hongte.alms.base.vo.module.LoanExtListReq;
import com.hongte.alms.base.vo.module.LoanExtListVO;
import com.hongte.alms.common.mapper.SuperMapper;

/**
 * <p>
 * 业务展期续借信息表 Mapper 接口
 * </p>
 *
 * @author 王继光
 * @since 2018-03-06
 */
public interface RenewalBusinessMapper extends SuperMapper<RenewalBusiness> {
	public List<LoanExtListVO> listLoanExt(@Param("loanExtReq")LoanExtListReq req);
	public int listLoanExtCount(@Param("loanExtReq")LoanExtListReq req);
}
