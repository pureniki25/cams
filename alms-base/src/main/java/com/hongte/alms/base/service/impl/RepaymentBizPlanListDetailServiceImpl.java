package com.hongte.alms.base.service.impl;

import com.hongte.alms.base.entity.RepaymentBizPlanListDetail;
import com.hongte.alms.base.mapper.RepaymentBizPlanListDetailMapper;
import com.hongte.alms.base.service.RepaymentBizPlanListDetailService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 还款计划应还项目明细表 服务实现类
 * </p>
 *
 * @author cj
 * @since 2018-03-06
 */
@Service("RepaymentBizPlanListDetailService")
@Transactional
public class RepaymentBizPlanListDetailServiceImpl extends BaseServiceImpl<RepaymentBizPlanListDetailMapper, RepaymentBizPlanListDetail> implements RepaymentBizPlanListDetailService {

}
