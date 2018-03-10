package com.hongte.alms.base.service;

import com.hongte.alms.base.dto.UserPermissionBusinessDto;
import com.hongte.alms.base.vo.module.BusinessInfoForApplyDerateVo;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.common.service.BaseService;

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

}
