package com.hongte.alms.base.service.impl;

import com.hongte.alms.base.collection.enums.CollectionStatusEnum;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.mapper.RepaymentBizPlanListMapper;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 业务还款计划列表 服务实现类
 * </p>
 *
 * @author 曾坤
 * @since 2018-03-06
 */
@Service("RepaymentBizPlanListService")
public class RepaymentBizPlanListServiceImpl extends BaseServiceImpl<RepaymentBizPlanListMapper, RepaymentBizPlanList> implements RepaymentBizPlanListService {

    @Autowired
    RepaymentBizPlanListMapper repaymentBizPlanListMapper;


    @Override
    public List<RepaymentBizPlanList> selectNeedPhoneUrgNorBiz(String companyId,Integer overDueDays) {
//        if(overDueDays.equals(0)){
//            overDueDays = 1000;
//        }
       return repaymentBizPlanListMapper.selectNeedSetColInfoNormalBizPlansBycomId(
                companyId,
                overDueDays,
                CollectionStatusEnum.PHONE_STAFF.getKey());
    }

    @Override
    public List<RepaymentBizPlanList> selectNeedVisitNorBiz(String companyId,Integer overDueDays ) {
        return repaymentBizPlanListMapper.selectNeedSetColInfoNormalBizPlansBycomId(
                companyId,
                overDueDays,
                CollectionStatusEnum.COLLECTING.getKey());

    }

    @Override
    public List<RepaymentBizPlanList> selectNeedLawNorBiz(Integer overDueDays) {
        return repaymentBizPlanListMapper.selectNeedSetColInfoNormalBizPlansBycomId(
                null,
                overDueDays,
                CollectionStatusEnum.TO_LAW_WORK.getKey());
    }
    
    @Override
    public RepaymentBizPlanList queryRepaymentBizPlanListByConditions(String businessId, String afterId) {
    	return repaymentBizPlanListMapper.queryRepaymentBizPlanListByConditions(businessId, afterId);
    }

//    @Override
//    public List<RepaymentBizPlanList> selectNeedPhoneUrgRenewBiz(String companyId,Integer beforeDueDays) {
//        if(beforeDueDays.equals(0)){
//            beforeDueDays = 1000;
//        }
//       return repaymentBizPlanListMapper.selectNeedSetColInfoRenewBizPlansBycomId(
//               companyId,
//               0-beforeDueDays,
//               CollectionStatusEnum.PHONE_STAFF.getKey()
//       );
//    }
//
//    @Override
//    public List<RepaymentBizPlanList> selectNeedVisitRenewBiz(String companyId,Integer overDueDays) {
//        return repaymentBizPlanListMapper.selectNeedSetColInfoRenewBizPlansBycomId(
//                companyId,
//                overDueDays,
//                CollectionStatusEnum.COLLECTING.getKey()
//        );
//    }
//
//    @Override
//    public List<RepaymentBizPlanList> selectNeedLawRenewBiz(Integer overDueDays) {
//        return repaymentBizPlanListMapper.selectNeedSetColInfoRenewBizPlansBycomId(
//                null,
//                overDueDays,
//                CollectionStatusEnum.TO_LAW_WORK.getKey()
//        );
//    }
}
