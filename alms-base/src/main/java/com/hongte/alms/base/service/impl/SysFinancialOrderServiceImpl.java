package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.SysFinancialOrder;
import com.hongte.alms.base.mapper.SysFinancialOrderMapper;
import com.hongte.alms.base.service.SysFinancialOrderService;
import com.hongte.alms.base.vo.finance.SysFinancialOrderVO;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 财务跟单设置 服务实现类
 * </p>
 *
 * @author 曾坤
 * @since 2018-05-29
 */
@Service("SysFinancialOrderService")
public class SysFinancialOrderServiceImpl extends BaseServiceImpl<SysFinancialOrderMapper, SysFinancialOrder> implements SysFinancialOrderService {

    /**
     *  自定义分页查询
     *
     * @param [page, businessTypeId, areaId, companyId, userName]
     * @return com.baomidou.mybatisplus.plugins.Page<com.hongte.alms.base.vo.finance.SysFinancialOrderVO>
     * @author 张贵宏
     * @date 2018/6/17 15:25
     */
    @Override
    public Page<SysFinancialOrderVO> search(Page<SysFinancialOrderVO> page, Integer businessTypeId, String areaId, String companyId, String userName) {
        List<SysFinancialOrderVO>  voList = ((SysFinancialOrderMapper)super.baseMapper).search(page, businessTypeId, areaId, companyId, userName);
        return page.setRecords(voList);
    }
}
