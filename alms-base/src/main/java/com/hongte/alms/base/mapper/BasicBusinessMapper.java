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
	 * 	结清最终缴纳的金额 
	 * @param original_business_id
	 * @return
	 */
	 Double getSettleTotalFactSum(@Param("original_business_id") String original_business_id);
}


