package com.hongte.alms.base.mapper;

import com.hongte.alms.base.dto.UserPermissionBusinessDto;
import com.hongte.alms.base.vo.module.BusinessInfoForApplyDerateVo;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.common.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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
    List<BusinessInfoForApplyDerateVo> selectBusinessInfoForApplyDerateVo(@Param("crpId") String crpId);

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
 
	 
	 
}


