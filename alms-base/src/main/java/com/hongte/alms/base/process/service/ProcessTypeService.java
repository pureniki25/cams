package com.hongte.alms.base.process.service;



import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.process.entity.ProcessType;
import com.hongte.alms.base.vo.module.ProcessTypeReq;
import com.hongte.alms.common.service.BaseService;

import java.util.List;

/**
 * <p>
 * 流程类型 服务类
 * </p>
 *
 * @author 伦惠峰
 * @since 2018-01-13
 */
public interface ProcessTypeService extends BaseService<ProcessType> {
    ProcessType  getProcessTypeByCode(String typeCode);

    ProcessType getProcessTypeByID(String typeID);

    /**
     *返回流程类型列表
     */
    List<ProcessType> getProcessTypeList();
    
    public Page<ProcessType> getProcessTypeListByPage(ProcessTypeReq req);
}
