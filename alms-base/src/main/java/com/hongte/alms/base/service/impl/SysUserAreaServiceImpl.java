package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.SysUserArea;
import com.hongte.alms.base.mapper.SysUserAreaMapper;
import com.hongte.alms.base.service.SysUserAreaService;
import com.hongte.alms.base.vo.module.UserAreaReq;
import com.hongte.alms.base.vo.module.UserAreaVO;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.vo.PageResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * 用户管理的区域表 服务实现类
 * </p>
 *
 * @author 曾坤
 * @since 2018-03-03
 */
@Service("SysUserAreaService")
public class SysUserAreaServiceImpl extends BaseServiceImpl<SysUserAreaMapper, SysUserArea> implements SysUserAreaService {

	@Autowired
	SysUserAreaMapper sysUserAreaMapper ;
    @Override
    public List<String> selectUserAreas(String userId) {
        List<SysUserArea> userAreas = selectList(new EntityWrapper<SysUserArea>().eq("user_id",userId));
        List<String> areaStrs = new LinkedList<String>();
        for(SysUserArea area:userAreas){
            areaStrs.add(area.getOrgCode());
        }
        return areaStrs;
    }

	@Override
	public PageResult page(UserAreaReq req) {
		int count = sysUserAreaMapper.count(req);
		List<UserAreaVO> userAreaVOs = sysUserAreaMapper.list(req);
		return PageResult.success(userAreaVOs, count);
	}
}
