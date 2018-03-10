package com.hongte.alms.base.service;

import com.hongte.alms.base.entity.DocType;
import com.hongte.alms.base.vo.module.doc.DocPageInfo;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 文档类型 服务类
 * </p>
 *
 * @author dengzhiming
 * @since 2018-02-28
 */
public interface DocTypeService extends BaseService<DocType> {
    DocPageInfo getDocPageInfo(String typeCode, String businessID);
}
