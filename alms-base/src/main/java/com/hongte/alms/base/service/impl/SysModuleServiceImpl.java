package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.SysModule;
import com.hongte.alms.base.mapper.SysModuleMapper;
import com.hongte.alms.base.service.SysModuleService;
import com.hongte.alms.base.vo.module.ModulePageReq;
import com.hongte.alms.base.vo.module.moduleManageVO;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.naming.Name;

/**
 * <p>
 * 菜单信息表 服务实现类
 * </p>
 *
 * @author 黄咏康
 * @since 2018-01-18
 */
@Service("SysModuleService")
public class SysModuleServiceImpl extends BaseServiceImpl<SysModuleMapper, SysModule> implements SysModuleService {
    @Autowired
    SysModuleMapper sysModulemapper;

    @Override
    public Page<moduleManageVO> selectModulePage(Page<moduleManageVO> pages, ModulePageReq key) {
        pages.setRecords(this.sysModulemapper.selectModulePage(pages, key));
        return pages;
    }
}
