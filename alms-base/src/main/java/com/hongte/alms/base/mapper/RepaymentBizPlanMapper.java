package com.hongte.alms.base.mapper;

import java.util.List;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.hongte.alms.base.RepayPlan.vo.RepayingPlanDto;
import org.apache.ibatis.annotations.Param;
import com.hongte.alms.base.entity.RepaymentBizPlan;
import com.hongte.alms.base.vo.module.RepaymentOpenServiceVO;
import com.hongte.alms.common.mapper.SuperMapper;

/**
 * <p>
 * 业务还款计划信息 Mapper 接口
 * </p>
 *
 * @author 曾坤
 * @since 2018-05-03
 */
public interface RepaymentBizPlanMapper extends SuperMapper<RepaymentBizPlan> {

    /**
     *
     * @author chenzs
     * @param key
     * @return
     */
    List<RepaymentOpenServiceVO> selectRepaymentOpenServiceList(@Param("originalBusinessId")String originalBusinessId,@Param("afterId") String afterId);


    /**
     * 查询指定客户的还款计划列表，分页
     * @param identifyCard
     * @return
     */
    List<RepayingPlanDto> queryCustomerRepayPlan(@Param("identifyCard") String identifyCard);
    List<RepayingPlanDto> queryCustomerRepayPlan(Pagination page, @Param("identifyCard") String identifyCard);

}
