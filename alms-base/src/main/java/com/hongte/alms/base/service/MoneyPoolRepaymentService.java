package com.hongte.alms.base.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.collection.vo.CollectionTrackLogVo;
import com.hongte.alms.base.customer.vo.CustomerRepayFlowExel;
import com.hongte.alms.base.customer.vo.CustomerRepayFlowListReq;
import com.hongte.alms.base.entity.MoneyPoolRepayment;
import com.hongte.alms.common.service.BaseService;

import java.util.List;

/**
 * <p>
 * 款项池业务关联表 服务类
 * </p>
 *
 * @author 王继光
 * @since 2018-03-23
 */
public interface MoneyPoolRepaymentService extends BaseService<MoneyPoolRepayment> {
	/**
	 * 删除财务新增流水
	 * @author 王继光
	 * 2018年6月5日 上午9:56:53
	 * @param mprId
	 * @return
	 */
	public boolean deleteFinanceAddStatement(String mprId);

	/**
	 * 查询客户还款流水列表
	 * @param customerRepayFlowListReq
	 * @return
	 */
	List<CustomerRepayFlowExel> getCustomerRepayFlowList(CustomerRepayFlowListReq customerRepayFlowListReq);

    Page<CollectionTrackLogVo> getCustomerRepayFlowPageList(CustomerRepayFlowListReq customerRepayFlowListReq);
}
