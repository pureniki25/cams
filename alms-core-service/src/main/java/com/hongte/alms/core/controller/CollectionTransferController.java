package com.hongte.alms.core.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.collection.entity.CollectionLog;
import com.hongte.alms.base.collection.entity.CollectionStatus;
import com.hongte.alms.base.collection.service.CollectionLogService;
import com.hongte.alms.base.collection.service.CollectionStatusService;
import com.hongte.alms.base.entity.Collection;
import com.hongte.alms.base.entity.CollectionLogXd;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.TransferFailLog;
import com.hongte.alms.base.service.CollectionLogXdService;
import com.hongte.alms.base.service.CollectionService;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.base.service.TransferFailLogService;
import com.hongte.alms.base.vo.module.CollectionReq;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.StringUtil;
import com.ht.ussp.bean.LoginUserInfoHelper;
import com.ht.ussp.client.dto.LoginInfoDto;
import io.swagger.annotations.Api;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * @author:喻尊龙
 * @date: 2018/3/29
 */
@RestController
@RequestMapping("/alms")
@Api(tags = "CollectionTrackLogController", description = "信贷数据库历史催收数据导入相关接口")
public class CollectionTransferController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CollectionTransferController.class);

	@Autowired
	@Qualifier("CollectionService")
	private CollectionService collectionService;

	@Autowired
	@Qualifier("CollectionLogXdService")
	private CollectionLogXdService collectionLogXdService;

	@Autowired
	private LoginUserInfoHelper loginUserInfoHelper;

	@Autowired
	@Qualifier("CollectionStatusService")
	private CollectionStatusService collectionStatusService;

	@Autowired
	@Qualifier("CollectionLogService")
	private CollectionLogService collectionLogService;

	@Autowired
	@Qualifier("TransferFailLogService")
	private TransferFailLogService transferFailLogService;

	@Autowired
	@Qualifier("RepaymentBizPlanListService")
	private RepaymentBizPlanListService repaymentBizPlanListService;

	@GetMapping("/transfer")
	@ResponseBody
	public Result transferCollection() {

		int count = collectionService.queryNotTransferCollectionCount();
		for (int i = 0; i <= count / 100 + 1; i++) {

			CollectionReq req = new CollectionReq();
			req.setOffSet(i * 100);
			req.setPageSize(100);

			List<Collection> collectionList = collectionService.queryNotTransferCollection(req);

			if (CollectionUtils.isEmpty(collectionList)) {
				continue;
			}

			for (Collection collection : collectionList) {
				Map<String, Object> map = getStatus(collection);
				if (map == null) {
					continue;
				}
				CollectionStatus collectionStatus = (CollectionStatus) map.get("status");
				CollectionLog collectionLog = (CollectionLog) map.get("log");

                CollectionStatus status = collectionStatusService.selectOne(new EntityWrapper<CollectionStatus>().eq("business_id",collectionStatus.getBusinessId()).eq("crp_id",collectionStatus.getCrpId()));
                if(status == null){
                    collectionStatusService.insertOrUpdate(collectionStatus);
                    transferFailLogService.delete(new EntityWrapper<TransferFailLog>().eq("business_id",collection.getBusinessId()).eq("after_id",collection.getAfterId()));
                }
                CollectionLog log = collectionLogService.selectOne(new EntityWrapper<CollectionLog>().eq("business_id",collectionLog.getBusinessId()).eq("crp_id",collectionLog.getCrpId()));
                if(log == null){
                    collectionLogService.insertOrUpdate(collectionLog);
                    transferFailLogService.delete(new EntityWrapper<TransferFailLog>().eq("business_id",collection.getBusinessId()).eq("after_id",collection.getAfterId()));
                }

			}
		}

		count = collectionLogXdService.queryNotTransferCollectionLogCount();
		for (int i = 0; i <= count / 100 + 1; i++) {

			CollectionReq req = new CollectionReq();
			req.setOffSet(i * 100);
			req.setPageSize(100);

			List<CollectionLogXd> xdList = collectionLogXdService.queryNotTransferCollectionLog(req);

			if (CollectionUtils.isEmpty(xdList)) {
				continue;
			}

			for (CollectionLogXd collectionLogXd : xdList) {
				Map<String, Object> map = getStatus(collectionLogXd);
				if (map == null) {
					continue;
				}
				CollectionStatus collectionStatus = (CollectionStatus) map.get("status");
				CollectionLog collectionLog = (CollectionLog) map.get("log");
                CollectionStatus status = collectionStatusService.selectOne(new EntityWrapper<CollectionStatus>().eq("business_id",collectionStatus.getBusinessId()).eq("crp_id",collectionStatus.getCrpId()));
                if(status == null){
                    collectionStatusService.insertOrUpdate(collectionStatus);
                    transferFailLogService.delete(new EntityWrapper<TransferFailLog>().eq("business_id",collectionLogXd.getBusinessId()).eq("after_id",collectionLogXd.getAfterId()));

                }
                CollectionLog log = collectionLogService.selectOne(new EntityWrapper<CollectionLog>().eq("business_id",collectionLog.getBusinessId()).eq("crp_id",collectionLog.getCrpId()));
                if(log == null){
                    collectionLogService.insertOrUpdate(collectionLog);
                    transferFailLogService.delete(new EntityWrapper<TransferFailLog>().eq("business_id",collectionLogXd.getBusinessId()).eq("after_id",collectionLogXd.getAfterId()));
                }
			}

		}

		return Result.success();
	}

	private Map<String, Object> getStatus(Collection collection) {

		Map<String, Object> map = new HashMap<>();

		RepaymentBizPlanList repaymentBizPlanList = repaymentBizPlanListService
				.selectOne(new EntityWrapper<RepaymentBizPlanList>().eq("business_id", collection.getBusinessId())
						.eq("after_id", collection.getAfterId()));
		if (repaymentBizPlanList == null) {
			TransferFailLog failLog = new TransferFailLog();
			failLog.setBusinessId(collection.getBusinessId());
			failLog.setAfterId(collection.getAfterId());
			failLog.setFailReason(1);
			TransferFailLog transferFailLog = transferFailLogService.selectOne(new EntityWrapper<TransferFailLog>()
					.eq("business_id", collection.getBusinessId()).eq("after_id", collection.getAfterId()));
			if (transferFailLog == null) {
				transferFailLogService.insert(failLog);
			}

			return null;
		}

		// 根据信贷userId 获取贷后userId
		LoginInfoDto dto = loginUserInfoHelper.getUserInfoByUserId("", collection.getCollectionUser());
		if (dto == null) {
			dto = new LoginInfoDto();
		}
		CollectionStatus status = new CollectionStatus();
		CollectionLog log = new CollectionLog();

		status.setBusinessId(collection.getBusinessId());
		status.setCrpId(repaymentBizPlanList.getPlanListId());
		// 判断状态
		int collectionStatus = 0;
		if ("催收中".equals(collection.getStatus())) {
			collectionStatus = 50;
			status.setVisitStaff(dto.getUserId());
			log.setCollectionUser(dto.getUserId());
			if (StringUtil.isEmpty(dto.getUserId())) {
				TransferFailLog failLog = new TransferFailLog();
				failLog.setBusinessId(collection.getBusinessId());
				failLog.setAfterId(collection.getAfterId());
				failLog.setFailReason(2);
				TransferFailLog transferFailLog = transferFailLogService.selectOne(new EntityWrapper<TransferFailLog>()
						.eq("business_id", collection.getBusinessId()).eq("after_id", collection.getAfterId()));
				if (transferFailLog == null) {
					transferFailLogService.insert(failLog);
				}

				return null;
			}
		} else if ("已移交法务".equals(collection.getStatus())) {
			log.setCollectionUser("admin");
			collectionStatus = 100;
		} else if ("已完成".equals(collection.getStatus())) {
			collectionStatus = 200;
			log.setCollectionUser("admin");
		} else if ("二押已赎回".equals(collection.getStatus())) {
			log.setCollectionUser("admin");
			collectionStatus = 250;
		} else if ("已委外催收".equals(collection.getStatus())) {
			log.setCollectionUser("admin");
			collectionStatus = 300;
		} else if ("待分配".equals(collection.getStatus())) {
			log.setCollectionUser("admin");
			collectionStatus = 350;
		} else if ("推迟移交法务".equals(collection.getStatus())) {
			log.setCollectionUser("admin");
			collectionStatus = 400;
		} else {
			log.setCollectionUser("admin");
		}
		status.setCollectionStatus(collectionStatus);
		status.setCreateTime(new Date());
		status.setCreateUser("admin");
		status.setUpdateTime(new Date());
		status.setUpdateUser("admin");
		status.setDescribe("信贷历史数据导入");
		status.setSetWay(0);
		status.setCrpType(ifPlanListIsLast(collection.getBusinessId(), repaymentBizPlanList.getPlanListId()) ? 2 : 1);

		log.setBusinessId(collection.getBusinessId());
		log.setCrpId(repaymentBizPlanList.getPlanListId());
		log.setAfterStatus(collectionStatus);
		log.setCreateTime(new Date());
		log.setCreateUser("admin");
		log.setUpdateTime(new Date());
		log.setUpdateUser("admin");
		log.setDescribe("信贷历史数据导入");
		log.setSetWay(0);
		log.setBeforeStatus(null);
		log.setSetTypeStatus(collectionStatus);

		map.put("status", status);
		map.put("log", log);

		return map;
	}

	/**
	 * 判断还款计划是否是最后一个
	 *
	 * @param
	 * @return
	 */
	public boolean ifPlanListIsLast(String businessId, String planListId) {
		RepaymentBizPlanList pList = repaymentBizPlanListService.selectOne(
				new EntityWrapper<RepaymentBizPlanList>().eq("business_id", businessId).orderBy("due_date desc"));
		return pList.getPlanListId().equals(planListId);
	}

	private Map<String, Object> getStatus(CollectionLogXd collectionLogXd) {

		Map<String, Object> map = new HashMap<>();

		RepaymentBizPlanList repaymentBizPlanList = repaymentBizPlanListService
				.selectOne(new EntityWrapper<RepaymentBizPlanList>().eq("business_id", collectionLogXd.getBusinessId())
						.eq("after_id", collectionLogXd.getAfterId()));

		if (repaymentBizPlanList == null) {
			TransferFailLog failLog = new TransferFailLog();
			failLog.setBusinessId(collectionLogXd.getBusinessId());
			failLog.setAfterId(collectionLogXd.getAfterId());
			failLog.setFailReason(1);
			TransferFailLog transferFailLog = transferFailLogService.selectOne(
					new EntityWrapper<TransferFailLog>().eq("business_id", collectionLogXd.getBusinessId())
							.eq("after_id", collectionLogXd.getAfterId()));
			if (transferFailLog == null) {
				transferFailLogService.insert(failLog);
			}

			return null;
		}

		// 根据信贷userId 获取贷后userId
		LoginInfoDto dto = loginUserInfoHelper.getUserInfoByUserId("", collectionLogXd.getCollectionUser());
		if (dto == null) {
			dto = new LoginInfoDto();
		}
		CollectionStatus status = new CollectionStatus();
		CollectionLog log = new CollectionLog();

		status.setBusinessId(collectionLogXd.getBusinessId());
		status.setCrpId(repaymentBizPlanList.getPlanListId());

		status.setCollectionStatus(1);
		if (StringUtil.isEmpty(dto.getUserId())) {
			TransferFailLog failLog = new TransferFailLog();
			failLog.setBusinessId(collectionLogXd.getBusinessId());
			failLog.setAfterId(collectionLogXd.getAfterId());
			failLog.setFailReason(2);
			TransferFailLog transferFailLog = transferFailLogService.selectOne(new EntityWrapper<TransferFailLog>()
					.eq("business_id", collectionLogXd.getBusinessId()).eq("after_id", collectionLogXd.getAfterId()));
			if (transferFailLog == null) {
				transferFailLogService.insert(failLog);
			}

			return null;
		}
		status.setPhoneStaff(dto.getUserId());
		status.setCreateTime(new Date());
		status.setCreateUser("admin");
		status.setUpdateTime(new Date());
		status.setUpdateUser("admin");
		status.setDescribe("信贷历史数据导入");
		status.setSetWay(0);
		status.setCrpType(
				ifPlanListIsLast(collectionLogXd.getBusinessId(), repaymentBizPlanList.getPlanListId()) ? 2 : 1);

		log.setBusinessId(collectionLogXd.getBusinessId());
		log.setCrpId(repaymentBizPlanList.getPlanListId());
		log.setAfterStatus(1);
		log.setCollectionUser(dto.getUserId());
		log.setCreateTime(new Date());
		log.setCreateUser("admin");
		log.setUpdateTime(new Date());
		log.setUpdateUser("admin");
		log.setDescribe("信贷历史数据导入");
		log.setSetWay(0);
		log.setBeforeStatus(null);
		log.setSetTypeStatus(1);

		map.put("status", status);
		map.put("log", log);

		return map;

	}

}
