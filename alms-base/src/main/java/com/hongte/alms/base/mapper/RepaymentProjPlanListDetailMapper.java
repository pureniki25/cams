package com.hongte.alms.base.mapper;

import java.util.List;
import java.util.Map;

import com.hongte.alms.base.entity.RepaymentProjPlanListDetail;
import com.hongte.alms.common.mapper.SuperMapper;

/**
 * <p>
 * 标的还款计划应还项目明细表 Mapper 接口
 * </p>
 *
 * @author 曾坤
 * @since 2018-05-03
 */
public interface RepaymentProjPlanListDetailMapper extends SuperMapper<RepaymentProjPlanListDetail> {

	/**
	 * 某个标的某期各个项剩余未还的金额
	 * @author 王继光
	 * 2018年5月17日 下午2:48:48
	 * @param proj_plan_list_id
	 * @return Map[(projPlanDetailId(string),feeId(string),planItemName(string),planItemType(string),residueAmount(bigdecimal))]
	 */
	public List<Map<String, Object>> selectProjDetailResidueAmount(String projPlanListId) ;
}
