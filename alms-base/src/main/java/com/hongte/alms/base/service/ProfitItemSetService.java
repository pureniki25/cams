package com.hongte.alms.base.service;

import java.util.List;

import com.hongte.alms.base.entity.ProfitItemSet;
import com.hongte.alms.base.entity.RepaymentBizPlanListDetail;
import com.hongte.alms.base.entity.SysParameter;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-05-02
 */
public interface ProfitItemSetService extends BaseService<ProfitItemSet> {
    /**
     * 保存项目所属分类分润顺序
     * @param businessTypeId
     * @param itemTypes
     * @return
     */
    void saveItemTypes(List<SysParameter> itemTypes,Integer businessTypeId);

    /**
     * 更新项目所属分类分润顺序
     * @param itemTypes
     * @return
     */
    
    void updateItemTypes(List<ProfitItemSet> itemTypes);

}
