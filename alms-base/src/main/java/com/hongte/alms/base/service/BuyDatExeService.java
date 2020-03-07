package com.hongte.alms.base.service;

import com.hongte.alms.base.entity.BuyDatExe;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 资产表 服务类
 * </p>
 *
 * @author czs
 * @since 2019-11-06
 */
public interface BuyDatExeService extends BaseService<BuyDatExe> {
	void addAssetJt(BuyDatExe  exe) throws Exception;
}
