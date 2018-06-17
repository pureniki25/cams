package com.hongte.alms.base.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.SysFinancialOrder;
import com.hongte.alms.base.vo.finance.SysFinancialOrderVO;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 财务跟单设置 服务类
 * </p>
 *
 * @author 曾坤
 * @since 2018-05-29
 */
public interface SysFinancialOrderService extends BaseService<SysFinancialOrder> {
    /**
     *  自定义分页查询
     *
     * @param [page, businessTypeId, areaId, companyId, userName]
     * @return com.baomidou.mybatisplus.plugins.Page<com.hongte.alms.base.vo.finance.SysFinancialOrderVO>
     * @author 张贵宏
     * @date 2018/6/17 15:26
     */
    Page<SysFinancialOrderVO> search(Page<SysFinancialOrderVO> page, Integer businessTypeId, String areaId, String companyId , String userName);
}
