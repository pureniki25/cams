package com.hongte.alms.base.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hongte.alms.base.entity.TdrepayRechargeLog;
import com.hongte.alms.base.vo.module.ComplianceRepaymentVO;
import com.hongte.alms.common.mapper.SuperMapper;

/**
 * <p>
 * 团贷网合规化还款标的充值记录表 Mapper 接口
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-06-06
 */
public interface TdrepayRechargeLogMapper extends SuperMapper<TdrepayRechargeLog> {
	/**
	 * 查询合规化还款主页面列表
	 * 
	 * @param paramMap
	 * @return
	 */
	List<TdrepayRechargeLog> queryComplianceRepaymentData(ComplianceRepaymentVO vo);

	int countComplianceRepaymentData(ComplianceRepaymentVO vo);

	List<Map<String, Object>> queryTdrepayRechargeRecord(@Param(value = "projectId") String projectId,
			@Param(value = "confirmLogId") String confirmLogId);
}
