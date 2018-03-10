package com.hongte.alms.base.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import com.hongte.alms.base.entity.RepaymentBizPlan;
import com.hongte.alms.base.vo.module.RepaymentOpenServiceVO;
import com.hongte.alms.common.mapper.SuperMapper;

/**
 * <p>
 * 业务还款计划信息 Mapper 接口
 * </p>
 *
 * @author 王继光
 * @since 2018-03-06
 */
public interface RepaymentBizPlanMapper extends SuperMapper<RepaymentBizPlan> {
	

    /**
     * 
     * @author chenzs
     * @param key
     * @return
     */
    List<RepaymentOpenServiceVO> selectRepaymentOpenServiceList(@Param("originalBusinessId")String originalBusinessId,@Param("afterId") String afterId);

}
