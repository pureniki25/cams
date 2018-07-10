package com.hongte.alms.base.service.impl;

import com.hongte.alms.base.entity.ProfitFeeSet;
import com.hongte.alms.base.entity.ProfitItemSet;
import com.hongte.alms.base.entity.RepaymentBizPlanListDetail;
import com.hongte.alms.base.mapper.ProfitFeeSetMapper;
import com.hongte.alms.base.service.ProfitFeeSetService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.StringUtil;
import com.ht.ussp.bean.LoginUserInfoHelper;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-05-02
 */
@Service("ProfitFeeSetService")
public class ProfitFeeSetServiceImpl extends BaseServiceImpl<ProfitFeeSetMapper, ProfitFeeSet> implements ProfitFeeSetService {
	  @Autowired
	    LoginUserInfoHelper loginUserInfoHelper;
	@Override
	public void saveFeeTypes(List<RepaymentBizPlanListDetail> Types, Integer businessTypeId,String profitItemSetId) {
		ProfitFeeSet feeSet=new ProfitFeeSet();
		Types.forEach(item->{
			feeSet.setProfitFeeSetId(UUID.randomUUID().toString());
			feeSet.setProfitItemSetId(profitItemSetId);
			feeSet.setBusinessTypeId(businessTypeId);
			feeSet.setFeeId(item.getFeeId()==null?"null":item.getFeeId());
			feeSet.setFeeLevel(Integer.valueOf(item.getShareProfitIndex()));
			feeSet.setUpdateTime(new Date());
			feeSet.setUpdateUserId(loginUserInfoHelper.getUserId());
			feeSet.setFeeName(item.getPlanItemName());
			insertOrUpdate(feeSet);
		});
		
		
	}

	@Override
	public void updateFeeTypes(List<ProfitFeeSet> feeTypes) {
		insertOrUpdateBatch(feeTypes);
		
	}

}
