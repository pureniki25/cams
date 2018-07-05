package com.hongte.alms.base.service;

import java.util.List;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.dto.ConfirmRepaymentReq;
import com.hongte.alms.base.dto.MoneyPoolManagerReq;
import com.hongte.alms.base.dto.RepaymentRegisterInfoDTO;
import com.hongte.alms.base.entity.MoneyPool;
import com.hongte.alms.base.entity.RepaymentResource;
import com.hongte.alms.base.vo.finance.MoneyPoolManagerVO;
import com.hongte.alms.base.vo.module.MatchedMoneyPoolVO;
import com.hongte.alms.base.vo.module.MoneyPoolVO;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.service.BaseService;
import com.hongte.alms.common.vo.PageResult;

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
	List<MatchedMoneyPoolVO> listMatchedMoneyPool(String businessId , String afterId,Boolean notConfirmed);
//	Boolean saveRepaymentRegisterInfo(RepaymentRegisterInfoDTO registerInfoDTO);
	Boolean deleteRepaymentRegeisterInfo(String moneyPoolId,String userId);
	MoneyPoolVO getMoneyPool(String moneyPoolId);
	Result addCustomerRepayment(RepaymentRegisterInfoDTO registerInfoDTO);
	Result updateCustomerRepayment(RepaymentRegisterInfoDTO registerInfoDTO);
	Result deleteCustermerRepayment(RepaymentRegisterInfoDTO registerInfoDTO);
	Result matchBankStatement(List<MoneyPool> moneyPools,String businessId,String afterId);
	/**
	 * 财务确认后修改流水状态
	 * @author 王继光
	 * 2018年5月26日 下午4:58:28
	 * @param req
	 */
	void confirmRepaidUpdateMoneyPool(ConfirmRepaymentReq req);
	/**
	 * 撤销还款后修改流水状态
	 * @author 王继光
	 * 2018年5月26日 下午7:46:51
	 * @param repaymentResource
	 */
	void revokeConfirmRepaidUpdateMoneyPool(RepaymentResource repaymentResource);
	
	/**
	 * 分页查导入流水列表的数据
	 * @author 王继光
	 * 2018年6月8日 上午11:30:08
	 * @param page
	 * @param ew
	 * @return
	 */
	Page<MoneyPoolManagerVO> selectMoneyPoolManagerList(MoneyPoolManagerReq req);
}
