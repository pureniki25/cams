package com.hongte.alms.base.service;

import com.hongte.alms.base.dto.UserPermissionBusinessDto;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.vo.billing.CarLoanBilVO;
import com.hongte.alms.base.vo.module.BusinessInfoForApplyDerateVo;
import com.hongte.alms.base.vo.module.ExpenseSettleVO;
import com.hongte.alms.base.vo.module.LiquidationVO;
import com.hongte.alms.base.vo.module.LitigationVO;
import com.hongte.alms.common.service.BaseService;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 基础业务信息表 服务类
 * </p>
 *
 * @author 曾坤
 * @since 2018-02-02
 */
public interface BasicBusinessService extends BaseService<BasicBusiness> {

	/**
	 * 根据用户还款计划ID查找出申请减免界面的业务基本信息
	 * 
	 * @param crpId
	 * @return
	 */
	List<BusinessInfoForApplyDerateVo> selectBusinessInfoForApplyDerateVo(String crpId, Integer isDefer,
			String originalBusinessId);

	BusinessInfoForApplyDerateVo selectBusinessInfoForApplyDerateVoOne(String crpId, Integer isDefer,
			String originalBusinessId);

	/**
	 * 根据公司ID列表查找出用户允许访问的业务信息列表
	 * 
	 * @param companyIds
	 * @return
	 */
	List<UserPermissionBusinessDto> selectUserPermissionBusinessDtos(List<String> companyIds);

	List<String> selectCompanysBusinessIds(List<String> companyIds);

	/**
	 * 前置费用:一次性收取的分公司费用+ 期初收取的月收分公司服务费+平台费+担保费
	 * 
	 * @param original_business_id
	 * @return
	 */
	BigDecimal getPreChargeAndPreFees(String original_business_id);

    /**
     * 查询所有业务ID
     *
     * @param companyId    分公司ID
     * @param businessType 业务类型
     * @return 业务ID集合
     * @author 张贵宏
     */
    List<String> findBusinessIds(String companyId, Integer businessType);

	/**
	 * 结清最终缴纳的金额
	 * 
	 * @param original_business_id
	 * @return
	 */
	Double getSettleTotalFactSum(@Param("original_business_id") String original_business_id);

	/**
	 * 月收等费用总额
	 * 
	 * @param original_business_id
	 * @return
	 */
	Double getMonthSumFactAmount(@Param("original_business_id") String original_business_id);

	/**
	 * （所有还款计划中）的应还利息 和（所有还款计划中）的应还月收服务费
	 * 
	 * @param original_business_id
	 * @return
	 */
	Map<String, Object> getNeedPay(@Param("original_business_id") String original_business_id);

	/**
	 * 月收平台服务费的业务
	 * 
	 * @param original_business_id
	 * @return
	 */
	Double getMonthPlatformAmount(@Param("crpId") String crpId);

	/**
	 * 月收公司服务费的业务
	 * 
	 * @param original_business_id
	 * @return
	 */
	Double getMonthCompanyAmount(@Param("crpId") String crpId);

	/**
	 * 获取提前结清违约金
	 * 
	 * @param original_business_id
	 * @return
	 */

	ExpenseSettleVO getPreLateFees(String crpId, String original_business_id, String repayType, String businessTypeId,
			Date settleDate, Date ContractDate, Date firstRepayDate, String restPeriods,Double needPayPrincipal) throws Exception;

	/**
	 * 获取展期的借款期数
	 * 
	 * @param crpId
	 * @return
	 */
	Integer getBorrowLlimitZQ(@Param("crpId") String crpId);

	/**
	 * 月收平台服务费的业务记录条数
	 * 
	 * @param original_business_id
	 * @return
	 */

	Integer getMonthPlatformAmountCount(@Param("crpId") String crpId);

	/**
	 * 车贷业务减免申请的逾期利息
	 */
	Map<String, Object> carLoanBilling(CarLoanBilVO carLoanBilVO, Integer overdueDays);

	/**
	 * 获取线上线下逾期费用
	 */
	Map<String, Object> getOverDueMoney(String pListId, String onLineFeeId, String underLineFeeId);
	

	/**
	 * 清算一分配信息
	 */
	List<LiquidationVO> selectLiquidationOne(String crpId);
	/**
	 * 清算二分配信息
	 */
	List<LiquidationVO> selectLiquidationTwo( String crpId);
	
	/**
	 * 房贷移交法务信息
	 */
	List<LitigationVO> selectLitigationHouseVO( String crpId);
	
	/**
	 * 车贷移交法务信息
	 */
	List<LitigationVO> selectLitigationCarVO( String crpId);
	 

}
