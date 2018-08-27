package com.hongte.alms.base.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.collection.enums.CollectionSetWayEnum;
import com.hongte.alms.base.collection.enums.CollectionStatusEnum;
import com.hongte.alms.base.collection.service.CollectionStatusService;
import com.hongte.alms.base.collection.vo.StaffBusinessVo;
import com.hongte.alms.base.entity.TransferLitigationHouse;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.mapper.TransferLitigationHouseMapper;
import com.hongte.alms.base.process.entity.Process;
import com.hongte.alms.base.process.enums.ProcessApproveResult;
import com.hongte.alms.base.process.enums.ProcessStatusEnums;
import com.hongte.alms.base.process.enums.ProcessTypeEnums;
import com.hongte.alms.base.process.service.ProcessService;
import com.hongte.alms.base.process.vo.ProcessLogReq;
import com.hongte.alms.base.service.TransferLitigationHouseService;
import com.hongte.alms.base.service.TransferOfLitigationService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;

/**
 * <p>
 * 房贷移交法务信息表 服务实现类
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-03-06
 */
@Service("TransfLitigationHouseService")
public class TransferLitigationHouseServiceImpl
		extends BaseServiceImpl<TransferLitigationHouseMapper, TransferLitigationHouse>
		implements TransferLitigationHouseService {
	private static final Logger LOG = LoggerFactory.getLogger(TransferLitigationHouseServiceImpl.class);

	@Autowired
	@Qualifier("ProcessService")
	private ProcessService processService;

	@Autowired
	@Qualifier("CollectionStatusService")
	private CollectionStatusService collectionStatusService;

	@Autowired
	@Qualifier("TransferOfLitigationService")
	private TransferOfLitigationService transferOfLitigationService;

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void saveHouseProcessApprovalResult(ProcessLogReq req, String sendUrl) {
		try {
			// 存储审批结果信息
			Process process = processService.saveProcessApprovalResult(req, ProcessTypeEnums.HOUSE_LOAN_LITIGATION);
			String businessId = process.getBusinessId();
			String processId = process.getProcessId();
			List<TransferLitigationHouse> houses = this.selectList(new EntityWrapper<TransferLitigationHouse>()
					.eq("business_id", businessId).eq("process_id", processId));
			Integer status = process.getStatus();
			Integer processResult = process.getProcessResult();
			if (!CollectionUtils.isEmpty(houses) && status == ProcessStatusEnums.END.getKey()
					&& processResult == ProcessApproveResult.PASS.getKey()) {
				transferOfLitigationService.sendTransferLitigationData(businessId, sendUrl, req.getCrpId(), 2);
				// 更新贷后状态为 移交诉讼
//				collectionStatusService.setBussinessAfterStatus(req.getBusinessId(), req.getCrpId(), "",
//						CollectionStatusEnum.TO_LAW_WORK, CollectionSetWayEnum.MANUAL_SET);
				// 同时更新信贷的贷后状态
				List<StaffBusinessVo> voList = new LinkedList<>();
				StaffBusinessVo vo = new StaffBusinessVo();
				vo.setCrpId(req.getCrpId());
				vo.setBusinessId(req.getBusinessId());
				voList.add(vo);
				collectionStatusService.SyncBusinessColStatusToXindai(voList, null, "界面设置移交诉讼",
						CollectionStatusEnum.TO_LAW_WORK.getPageStr());
			}
		} catch (Exception e) {
			LOG.error("---saveHouseProcessApprovalResult--- 存储房贷审批结果信息失败！", e);
			throw new ServiceRuntimeException(e.getMessage(), e);
		}
	}
}
