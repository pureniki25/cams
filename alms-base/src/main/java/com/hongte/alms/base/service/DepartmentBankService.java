package com.hongte.alms.base.service;

import java.util.List;

import com.hongte.alms.base.entity.DepartmentBank;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 线下还款账户配置表 服务类
 * </p>
 *
 * @author 王继光
 * @since 2018-03-05
 */
public interface DepartmentBankService extends BaseService<DepartmentBank> {
	List<DepartmentBank> listDepartmentBank() ;
}
