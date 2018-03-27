package com.hongte.alms.base.service;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.dto.RepaymentRegisterInfoDTO;
import com.hongte.alms.base.entity.MoneyPool;
import com.hongte.alms.base.vo.module.MatchedMoneyPoolVO;
import com.hongte.alms.base.vo.module.MoneyPoolVO;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 财务款项池表 服务类
 * </p>
 *
 * @author 王继光
 * @since 2018-02-28
 */
public interface MoneyPoolService extends BaseService<MoneyPool> {
	List<MoneyPoolVO> listMoneyPool(String businessId , String afterId) ;
	Page<MoneyPoolVO> listMoneyPoolByPage(String businessId,String afterId , Integer page,Integer limit);
	List<MatchedMoneyPoolVO> listMatchedMoneyPool(String businessId , String afterId);
	Boolean saveRepaymentRegisterInfo(RepaymentRegisterInfoDTO registerInfoDTO);
	Boolean deleteRepaymentRegeisterInfo(String moneyPoolId,String userId);
	MoneyPoolVO getMoneyPool(String moneyPoolId);
	Result addCustomerRepayment(RepaymentRegisterInfoDTO registerInfoDTO);
	Result updateCustomerRepayment(RepaymentRegisterInfoDTO registerInfoDTO);
	Result deleteCustermerRepayment(RepaymentRegisterInfoDTO registerInfoDTO);
}
