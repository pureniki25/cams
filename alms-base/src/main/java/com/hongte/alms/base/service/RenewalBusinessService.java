package com.hongte.alms.base.service;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.RenewalBusiness;
import com.hongte.alms.base.vo.module.LoanExtListReq;
import com.hongte.alms.base.vo.module.LoanExtListVO;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 业务展期续借信息表 服务类
 * </p>
 *
 * @author 王继光
 * @since 2018-03-06
 */
public interface RenewalBusinessService extends BaseService<RenewalBusiness> {
	Page<LoanExtListVO> listLoanExt(LoanExtListReq req) ;
}
