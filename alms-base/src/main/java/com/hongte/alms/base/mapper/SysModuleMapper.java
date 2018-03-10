package com.hongte.alms.base.mapper;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.hongte.alms.base.entity.SysModule;
import com.hongte.alms.base.vo.module.ModulePageReq;
import com.hongte.alms.base.vo.module.moduleManageVO;
import com.hongte.alms.common.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * <p>
 * 菜单信息表 Mapper 接口
 * </p>
 *
 * @author 黄咏康
 * @since 2018-01-18
 */
public interface SysModuleMapper extends SuperMapper<SysModule> {
    /**
     *
     * @param pages
     * @param key
     * @return
     */
    List<moduleManageVO> selectModulePage(Pagination pages, ModulePageReq key);
}
