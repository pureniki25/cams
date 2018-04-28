/**
 * 
 */
package com.hongte.alms.open.controller;

import com.hongte.alms.base.service.CarBasicService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hongte.alms.base.collection.enums.CollectionSetWayEnum;
import com.hongte.alms.base.collection.enums.CollectionStatusEnum;
import com.hongte.alms.base.collection.service.CollectionLogService;
import com.hongte.alms.base.collection.service.CollectionStatusService;
import com.hongte.alms.common.result.Result;

import io.swagger.annotations.Api;

/**
 * @author 王继光
 * 2018年3月14日 下午8:05:02
 */
@RestController
@RequestMapping(value = "/collection", method = RequestMethod.POST)
@Api(value = "collection", description = "贷后系统  催收 对外接口")
public class  CollectionController {
	
	private Logger logger = LoggerFactory.getLogger(CollectionController.class);
	
	@Autowired
	@Qualifier("CollectionStatusService")
	CollectionStatusService collectionStatusService ;

	@Autowired
	@Qualifier("CarBasicService")
	CarBasicService carBasicService;

	@Autowired
	@Qualifier("CollectionLogService")
	CollectionLogService collectionLogService ;

	/**
	 * 财务确认结清，信贷调用此接口  更新贷后相关状态
	 * @param businessId
	 * @return
	 */
	@PostMapping("/update")
    @ResponseBody
	@Transactional
	public Result update(String businessId) {

		if (businessId==null||businessId.equals("")) {
			return Result.error("500", "businessId 不能为空");
		}
		logger.info("信贷确认结清调用 ，businessId:"+businessId);

//		String businessId,
//		String crpId,
//		String describe,
//		CollectionStatusEnum  satusEnum,
//		CollectionSetWayEnum setWayEnum);

		//设置催收状态为已关闭
		Boolean bl=  collectionStatusService.setBussinessAfterStatus(
				businessId
				,null
				,"信贷结清调用"
				,CollectionStatusEnum.CLOSED
		,CollectionSetWayEnum.XINDAI_CALL);


		//设置车辆管理状态为已结清
		Boolean retBl = carBasicService.updateCarStatusToSettled(businessId);

		if(bl && retBl){
			logger.info("信贷确认结清调用  更新成功 ，businessId:"+businessId);
			return Result.success(businessId);
		}else{
			logger.info("信贷确认结清调用  失败 ，businessId:"+businessId);
			return Result.error("500", "数据更新失败");
		}


//		List<CollectionStatus> list = collectionStatusService.selectList(new EntityWrapper<CollectionStatus>().eq("business_id", businessId)) ;
//		if (list==null||list.size()==0) {
//			return Result.success(businessId);
//		}
//		logger.info("list:"+JSON.toJSONString(list));
//
//		List<CollectionLog> logs = new ArrayList<>() ;
//		for (CollectionStatus collectionStatus : list) {
//			CollectionLog log = new CollectionLog();
//			collectionStatus.setCollectionStatus(CollectionStatusEnum.CLOSED.getKey());
//			log.setBusinessId(collectionStatus.getBusinessId());
//			log.setCrpId(collectionStatus.getCrpId());
//			log.setAfterStatus(CollectionStatusEnum.CLOSED.getKey());
//			log.setCollectionUser(Constant.DEFAULT_SYS_USER);
//			log.setCreateTime(new Date());
//			log.setCreateUser(Constant.DEFAULT_SYS_USER);
//			log.setDescribe(CollectionSetWayEnum.XINDAI_CALL.getName());
//			log.setSetWay(CollectionSetWayEnum.XINDAI_CALL.getKey());
//			logs.add(log);
//		}
//		boolean statusUpdateResult = collectionStatusService.updateBatchById(list);
//		boolean logInsertResult = collectionLogService.insertBatch(logs);
//		if (statusUpdateResult&&logInsertResult) {
//			return Result.success(businessId);
//		}else {
//			return Result.error("500", "数据更新失败");
//		}
	}


	/**
	 *财务撤销结清，信贷调用此接口   更新贷后相关状态
	 * @param businessId
	 * @return
	 */
	@PostMapping("/settleRevoke")
	@ResponseBody
	@Transactional
	public Result  settleRevoke(String businessId){
		if (businessId==null||businessId.equals("")) {
			return Result.error("500", "businessId 不能为空");
		}
		logger.info("businessId:"+businessId);
		//撤销设置贷后跟踪状态为关闭
		Boolean bl=  collectionStatusService.revokeClosedStatus(businessId);


		//撤销设置车辆状态为关闭
		Boolean retBl = carBasicService.revokeCarStatus(businessId);

		if(bl && retBl){
			return Result.success(businessId);
		}else{
			return Result.error("500", "数据更新失败");
		}


	}


}
