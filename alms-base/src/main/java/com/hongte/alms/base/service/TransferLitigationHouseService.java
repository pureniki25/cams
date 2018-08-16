package com.hongte.alms.base.service;

import com.hongte.alms.base.entity.TransferLitigationHouse;
import com.hongte.alms.base.process.vo.ProcessLogReq;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 房贷移交法务信息表 服务类
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-03-06
 */
public interface TransferLitigationHouseService extends BaseService<TransferLitigationHouse> {
	
	void saveHouseProcessApprovalResult(ProcessLogReq req, String sendUrl);
}
