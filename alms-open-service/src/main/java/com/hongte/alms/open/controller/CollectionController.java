/**
 * 
 */
package com.hongte.alms.open.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.hibernate.validator.internal.util.logging.Log_.logger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.collection.entity.CollectionLog;
import com.hongte.alms.base.collection.entity.CollectionStatus;
import com.hongte.alms.base.collection.enums.CollectionSetWayEnum;
import com.hongte.alms.base.collection.enums.CollectionStatusEnum;
import com.hongte.alms.base.collection.service.CollectionLogService;
import com.hongte.alms.base.collection.service.CollectionStatusService;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.util.DESC;
import com.hongte.alms.common.util.EncryptionResult;
import com.hongte.alms.common.vo.ResponseData;
import com.hongte.alms.common.vo.ResponseEncryptData;

import io.swagger.annotations.Api;

/**
 * @author 王继光
 * 2018年3月14日 下午8:05:02
 */
@RestController
@RequestMapping(value = "/collection", method = RequestMethod.POST)
@Api(value = "collection", description = "贷后系统  催收 对外接口")
public class CollectionController {
	
	private Logger logger = LoggerFactory.getLogger(CollectionController.class);
	
	@Autowired
	@Qualifier("CollectionStatusService")
	CollectionStatusService collectionStatusService ;
	
	@Autowired
	@Qualifier("CollectionLogService")
	CollectionLogService collectionLogService ;
	
	@PostMapping("/update")
    @ResponseBody
	public Result update(String jsonData) {
		/*ResponseEncryptData resp = JSON.parseObject(jsonData, ResponseEncryptData.class);
		String decryptStr = new DESC().Decode(resp.getA(), resp.getUUId());
		EncryptionResult res = JSON.parseObject(decryptStr, EncryptionResult.class);
		ResponseData respData = JSON.parseObject(res.getParam(), ResponseData.class);
		JSONObject jsonObject = JSON.parseObject(respData.getData());*/
		logger.info("jsonData:"+jsonData);
		if (jsonData==null) {
			return Result.error("500", "jsonData 不能为空");
		}
		JSONObject jsonObject = JSON.parseObject(jsonData);
		String businessId = jsonObject.getString("businessId");
		logger.info("businessId:"+businessId);
		List<CollectionStatus> list = collectionStatusService.selectList(new EntityWrapper<CollectionStatus>().eq("business_id", businessId)) ;
		if (list==null) {
			return Result.success(null);
		}
		logger.info("list:"+JSON.toJSONString(list));
		
		List<CollectionLog> logs = new ArrayList<>() ;
		for (CollectionStatus collectionStatus : list) {
			CollectionLog log = new CollectionLog();
			collectionStatus.setCollectionStatus(CollectionStatusEnum.COLSED.getKey());
			log.setBusinessId(collectionStatus.getBusinessId());
			log.setCrpId(collectionStatus.getCrpId());
			log.setAfterStatus(CollectionStatusEnum.COLSED.getKey());
			log.setCollectionUser(Constant.DEFAULT_SYS_USER);
			log.setCreateTime(new Date());
			log.setCreateUser(Constant.DEFAULT_SYS_USER);
			log.setDescribe(CollectionSetWayEnum.XINDAI_CALL.getName());
			log.setSetWay(CollectionSetWayEnum.XINDAI_CALL.getKey());
			logs.add(log);
		}
		boolean statusUpdateResult = collectionStatusService.updateBatchById(list);
		boolean logInsertResult = collectionLogService.insertBatch(logs);
		if (statusUpdateResult&&logInsertResult) {
			return Result.success(jsonData);
		}else {
			return Result.error("500", "数据更新失败");
		}
	}
}
