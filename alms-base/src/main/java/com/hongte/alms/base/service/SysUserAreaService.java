package com.hongte.alms.base.service;

import com.hongte.alms.base.entity.SysUserArea;
import com.hongte.alms.base.vo.module.UserAreaReq;
import com.hongte.alms.base.vo.module.UserAreaVO;
import com.hongte.alms.common.service.BaseService;
import com.hongte.alms.common.vo.PageRequest;
import com.hongte.alms.common.vo.PageResult;

import java.util.List;

/**
 * <p>
 * 用户管理的区域表 服务类
 * </p>
 *
 * @author 曾坤
 * @since 2018-03-03
 */
public interface SysUserAreaService extends BaseService<SysUserArea> {

    /**
     * 根据用户ID查找出用户拥有的区域列表
     * @param userId
     * @return
     */
    List<String> selectUserAreas(String userId);
    
    /**
     * 分页获取用户管理的区域
     * @author 王继光
     * 2018年3月20日 上午11:16:13
     * @param req
     * @return
     */
    PageResult<UserAreaVO> page(UserAreaReq req);
}
