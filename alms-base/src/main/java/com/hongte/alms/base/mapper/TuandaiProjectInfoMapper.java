package com.hongte.alms.base.mapper;

import com.hongte.alms.base.entity.TuandaiProjectInfo;
import com.hongte.alms.common.mapper.SuperMapper;

/**
 * <p>
 * 团贷网平台业务上标信息 Mapper 接口
 * </p>
 *
 * @author 王继光
 * @since 2018-05-16
 */
public interface TuandaiProjectInfoMapper extends SuperMapper<TuandaiProjectInfo> {

	public TuandaiProjectInfo selectProjectInfoByProjPlanId(String projPlanId);
}
