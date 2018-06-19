package com.hongte.alms.base.mapper;

import com.hongte.alms.base.collection.vo.CollectionTrackLogVo;
import com.hongte.alms.base.customer.vo.CustomerRepayFlowExel;
import com.hongte.alms.base.customer.vo.CustomerRepayFlowListReq;
import com.hongte.alms.base.dto.ActualPaymentSingleLogDTO;
import com.hongte.alms.base.entity.MoneyPoolRepayment;
import com.hongte.alms.common.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 款项池业务关联表 Mapper 接口
 * </p>
 *
 * @author 王继光
 * @since 2018-03-23
 */
public interface MoneyPoolRepaymentMapper extends SuperMapper<MoneyPoolRepayment> {

    /**
     * 根据ids统计财务匹配且未被删除的还款登记的金额合计
     *
     * @param ids
     * @return
     * @author 王继光 2018年5月17日 上午11:22:39
     */
    public BigDecimal sumMoneyPoolRepaymentAmountByMprIds(@Param("ids") List<String> ids);

    /**
     * 根据业务编号查找实还流水
     */
    List<ActualPaymentSingleLogDTO> queryActualPaymentByBusinessId(@Param(value = "businessId") String businessId);

    List<CustomerRepayFlowExel> getCustomerRepayFlowList(CustomerRepayFlowListReq customerRepayFlowListReq);

    List<CollectionTrackLogVo> getCustomerRepayFlowPageList(CustomerRepayFlowListReq customerRepayFlowListReq);

    int countCustomerRepayFlowList(CustomerRepayFlowListReq customerRepayFlowListReq);

    void batchUpdateMoneyPool(@Param("updateList") List<MoneyPoolRepayment> updateList);
}
