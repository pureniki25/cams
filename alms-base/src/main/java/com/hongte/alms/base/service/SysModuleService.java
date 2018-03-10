package com.hongte.alms.base.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.SysModule;
import com.hongte.alms.base.vo.module.ModulePageReq;
import com.hongte.alms.base.vo.module.moduleManageVO;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 菜单信息表 服务类
 * </p>
 *
 * @author 黄咏康
 * @since 2018-01-18
 */
public interface SysModuleService extends BaseService<SysModule> {
    /**
     * 分页查询
     * @param pages
     * @param key
     * @return
     */
    Page<moduleManageVO> selectModulePage(Page<moduleManageVO> pages, ModulePageReq key);
}
