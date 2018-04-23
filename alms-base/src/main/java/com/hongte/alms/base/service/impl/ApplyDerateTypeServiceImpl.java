package com.hongte.alms.base.service.impl;

import com.hongte.alms.base.entity.ApplyDerateType;
import com.hongte.alms.base.mapper.ApplyDerateProcessMapper;
import com.hongte.alms.base.mapper.ApplyDerateTypeMapper;
import com.hongte.alms.base.service.ApplyDerateTypeService;
import com.hongte.alms.base.vo.module.ApplyTypeVo;
import com.hongte.alms.common.service.impl.BaseServiceImpl;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 申请减免项目类型表 服务实现类
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-04-08
 */
@Service("ApplyDerateTypeService")
public class ApplyDerateTypeServiceImpl extends BaseServiceImpl<ApplyDerateTypeMapper, ApplyDerateType> implements ApplyDerateTypeService {
    @Autowired
    ApplyDerateTypeMapper applyDerateTypeMap;

	 public ApplyTypeVo getApplyTypeVo(@Param(value="processId") String processId) {
		 return applyDerateTypeMap.getApplyTypeVo(processId);
	 }


	 
}
