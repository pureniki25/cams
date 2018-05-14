package com.hongte.alms.base.mapper;

import com.hongte.alms.base.dto.UserPermissionBusinessDto;
import com.hongte.alms.base.vo.module.BusinessInfoForApplyDerateVo;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.common.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 基础业务信息表 Mapper 接口
 * </p>
 *
 * @author 曾坤
 * @since 2018-02-02
 */
public interface BasicBusinessMapper extends SuperMapper<BasicBusiness> {


    /**
     * 查询申请减免界面业务基本信息
     * @param crpId
     * @return
     */
    List<BusinessInfoForApplyDerateVo> selectBusinessInfoForApplyDerateVo(@Param("crpId") String crpId,@Param("isDefer") Integer isDefer,@Param("originalBusinessId") String originalBusinessId);

    /**
     * 根据公司列表查询业务简单几个字段信息
     * @param companyIds
     * @return
     */
    List<UserPermissionBusinessDto> selectUserPermissionBusinessDtos(@Param("companyIds") List<String> companyIds);
    
    Double queryPayedPrincipal(@Param("businessId") String businessId);
    
    /**
     * 一次性收取的分公司费用
     * @param original_business_id
     * @return
     */
    Double getPreCharge(@Param("original_business_id") String original_business_id);
    
    
    /**
     * 期初收取的月收分公司服务费+平台费+担保费
     * @param original_business_id
     * @return
     */
    Double getPreFees(@Param("original_business_id") String original_business_id);
    
    

	/**
	 * 	结清最终缴纳的金额 
	 * @param original_business_id
	 * @return
	 */
	 Double getSettleTotalFactSum(@Param("original_business_id") String original_business_id);
	 
	/**
	 * 	 月收等费用总额
	 * @param original_business_id
	 * @return
	 */
	 Double getMonthSumFactAmount(@Param("original_business_id") String original_business_id);
	 /**
	 * 	 （所有还款计划中）的应还利息 和（所有还款计划中）的应还月收服务费
	 * @param original_business_id
	 * @return
	 */
	 Map<String, Object>  getNeedPay(@Param("original_business_id") String original_business_id);
	 /**
	 * 	 月收平台服务费的业务
	 * @param original_business_id
	 * @return
	 */
	 Double  getMonthPlatformAmount(@Param("crpId") String crpId);
	 
	 Integer getMonthPlatformAmountCount(@Param("crpId") String crpId);
	 /**
	 * 	 月收公司服务费的业务
	 * @param original_business_id
	 * @return
	 */
	 Double  getMonthCompanyAmount(@Param("crpId") String crpId);
	
	 List<String> queryFiveLevelClassify();
	 /**
	 * 	 获取展期的借款期数
	 * @param crpId
	 * @return
	 */
	 Integer   getBorrowLlimitZQ(@Param("crpId") String crpId);
	 
	
	 
}


