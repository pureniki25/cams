package com.hongte.alms.base.service.impl;

import com.hongte.alms.base.entity.ProfitFeeSet;
import com.hongte.alms.base.entity.ProfitItemSet;
import com.hongte.alms.base.entity.RepaymentBizPlanListDetail;
import com.hongte.alms.base.entity.SysParameter;
import com.hongte.alms.base.mapper.ProfitItemSetMapper;
import com.hongte.alms.base.service.ProfitItemSetService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.StringUtil;
import com.ht.ussp.bean.LoginUserInfoHelper;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-05-02
 */

@Transactional(rollbackFor = Exception.class)
@Service("ProfitItemSetService")
public class ProfitItemSetServiceImpl extends BaseServiceImpl<ProfitItemSetMapper, ProfitItemSet> implements ProfitItemSetService {
    @Autowired
    LoginUserInfoHelper loginUserInfoHelper;
	@Override
	public void saveItemTypes(List<SysParameter> itemTypes, Integer businessTypeId) {
		ProfitItemSet itemSet=new ProfitItemSet();
		itemTypes.forEach(item->{
			itemSet.setProfitItemSetId(UUID.randomUUID().toString());
			itemSet.setBusinessTypeId(businessTypeId);
			itemSet.setItemLevel(StringUtil.notEmpty(item.getParamValue2())?Integer.valueOf(item.getParamValue2()):0);
			itemSet.setItemName(item.getParamName());
			itemSet.setItemType(StringUtil.notEmpty(item.getParamValue())?Integer.valueOf(item.getParamValue()):0);
			itemSet.setUpdateTime(new Date());
			itemSet.setUpdateUserId(loginUserInfoHelper.getUserId());
			insertOrUpdate(itemSet);
		});
		
	}
	@Override
	public void updateItemTypes(List<ProfitItemSet> itemTypes) {

		updateBatchById(itemTypes);
		
	}


}
