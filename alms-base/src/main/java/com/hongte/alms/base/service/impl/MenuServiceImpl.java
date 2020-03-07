package com.hongte.alms.base.service.impl;

import com.hongte.alms.base.entity.Menu;
import com.hongte.alms.base.mapper.MenuMapper;
import com.hongte.alms.base.service.MenuService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 菜单表 服务实现类
 * </p>
 *
 * @author wjg
 * @since 2018-12-31
 */
@Service("MenuService")
public class MenuServiceImpl extends BaseServiceImpl<MenuMapper, Menu> implements MenuService {

}
