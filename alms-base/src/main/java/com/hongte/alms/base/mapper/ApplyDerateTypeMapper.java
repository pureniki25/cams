package com.hongte.alms.base.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.hongte.alms.base.entity.ApplyDerateType;
import com.hongte.alms.base.vo.module.ApplyTypeVo;
import com.hongte.alms.common.mapper.SuperMapper;

/**
 * <p>
 * 申请减免项目类型表 Mapper 接口
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-04-08
 */
public interface ApplyDerateTypeMapper extends SuperMapper<ApplyDerateType> {
	ApplyTypeVo getApplyTypeVo(@Param(value = "processId") String processId);

	List<ApplyDerateType> getApplyTypeByBusinessIdAndCrpId(@Param(value = "businessId") String businessId,
			@Param(value = "planListId") String planListId);

	/**
	 * 根据planlistid查找未使用过,且减免流程审批通过,且不用于结清的减免项
	 * 
	 * @author 王继光 2018年7月13日 下午9:22:38
	 * @param planListId
	 * @return
	 */
	List<ApplyDerateType> listBizPlanListUnusedDerate(String planListId);
}
