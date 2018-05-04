package com.hongte.alms.base.mapper;

import com.hongte.alms.base.dto.FinanceManagerListReq;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.vo.module.FinanceManagerListVO;
import com.hongte.alms.common.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 业务还款计划列表 Mapper 接口
 * </p>
 *
 * @author 王继光
 * @since 2018-03-02
 */
public interface RepaymentBizPlanListMapper extends SuperMapper<RepaymentBizPlanList> {


    /**
     * 选择需要设置催收信息的一般业务还款计划列表
     *
     * @return
     */
    List<RepaymentBizPlanList> selectNeedSetColInfoNormalBizPlansBycomId(
            @Param("companyId") String companyId,
            @Param("overDueDays") Integer overDueDays,
            @Param("colStatus") Integer colStatus,
            @Param("businessType") Integer businessType
    		);
    
    String queryRepaymentBizPlanListByConditions(@Param(value="businessId") String businessId, @Param(value="afterId") String afterId);


    int conutFinanceManagerList(FinanceManagerListReq req) ;
    List<FinanceManagerListVO> selectFinanceMangeList(FinanceManagerListReq req) ;
}
