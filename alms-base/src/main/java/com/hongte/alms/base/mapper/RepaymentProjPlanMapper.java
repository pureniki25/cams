package com.hongte.alms.base.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.hongte.alms.base.entity.RepaymentProjPlan;
import com.hongte.alms.base.entity.TuandaiProjectInfo;
import com.hongte.alms.common.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 标的还款计划信息 Mapper 接口
 * </p>
 *
 * @author 曾坤
 * @since 2018-05-03
 */
public interface RepaymentProjPlanMapper extends SuperMapper<RepaymentProjPlan> {

	/**
	 * 查询tb_repayment_proj_plan left join tb_tuandai_project_info 的信息
	 * <br><br>返回 isMaster(boolean,是否主借标),masterIssueId(string),projectId(string),realName(string),amount(BigDecimal)
	 * @author 王继光
	 * 2018年5月17日 上午9:35:19
	 * @param businessId
	 * @return
	 */
	public List<Map<String,Object>> selectProjPlanProjectInfo(String businessId);

	public  BigDecimal countRepayPlanAmount(@Param("businessId") String businessId,@Param("planId") String planId);
}
