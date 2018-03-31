package com.hongte.alms.base.process.mapper;

import java.util.List;

import com.hongte.alms.base.process.entity.ProcessTypeStep;
import com.hongte.alms.base.vo.module.ProcessStepSearchReq;
import com.hongte.alms.base.vo.module.ProcessTypeStepVO;
import com.hongte.alms.common.mapper.SuperMapper;

/**
 * <p>
 * 流程类型步骤 Mapper 接口
 * </p>
 *
 * @author 曾坤
 * @since 2018-02-05
 */
public interface ProcessTypeStepMapper extends SuperMapper<ProcessTypeStep> {
	
    public List<ProcessTypeStepVO> getProcessTypeStepList(ProcessStepSearchReq req);

}
