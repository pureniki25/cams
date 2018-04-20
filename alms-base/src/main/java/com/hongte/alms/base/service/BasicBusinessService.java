package com.hongte.alms.base.service;

import com.hongte.alms.base.dto.UserPermissionBusinessDto;
import com.hongte.alms.base.vo.module.BusinessInfoForApplyDerateVo;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.common.service.BaseService;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

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
     * @param crpId
     * @return
     */
    List<BusinessInfoForApplyDerateVo> selectBusinessInfoForApplyDerateVo(String crpId);
    BusinessInfoForApplyDerateVo selectBusinessInfoForApplyDerateVoOne(String crpId);


    /**
     * 根据公司ID列表查找出用户允许访问的业务信息列表
     * @param companyIds
     * @return
     */
    List<UserPermissionBusinessDto> selectUserPermissionBusinessDtos(List<String> companyIds);

    List<String> selectCompanysBusinessIds(List<String> companyIds);
    
    /**
     * 前置费用:一次性收取的分公司费用+ 期初收取的月收分公司服务费+平台费+担保费
     * @param original_business_id
     * @return
     */
     BigDecimal getPreChargeAndPreFees(String original_business_id);
    
    

     

 	/**
 	 * 	结清最终缴纳的金额 
 	 * @param original_business_id
 	 * @return
 	 */
 	 Double getSettleTotalFactSum(@Param("original_business_id") String original_business_id);


}
