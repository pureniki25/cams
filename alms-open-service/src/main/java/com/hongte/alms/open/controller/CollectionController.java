/**
 * 
 */
package com.hongte.alms.open.controller;

import com.alibaba.fastjson.JSON;
import com.hongte.alms.base.collection.entity.Collection;
import com.hongte.alms.base.collection.entity.Parametertracelog;
import com.hongte.alms.base.entity.CarBusinessAfter;
import com.hongte.alms.base.entity.IssueSendOutsideLog;
import com.hongte.alms.base.service.CarBasicService;
import com.hongte.alms.base.service.IssueSendOutsideLogService;
import com.hongte.alms.common.util.ErroInfoUtil;
import com.hongte.alms.open.feignClient.CollectionRemoteApi;
import com.hongte.alms.open.service.CollectionXindaiService;
import com.hongte.alms.open.service.WithHoldingXinDaiService;
import com.hongte.alms.open.util.XinDaiEncryptUtil;
import com.hongte.alms.open.vo.RequestData;
import com.hongte.alms.open.vo.ResponseData;
import com.ht.ussp.bean.LoginUserInfoHelper;
import feign.Feign;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.hongte.alms.base.collection.enums.CollectionSetWayEnum;
import com.hongte.alms.base.collection.enums.CollectionStatusEnum;
import com.hongte.alms.base.collection.service.CollectionLogService;
import com.hongte.alms.base.collection.service.CollectionStatusService;
import com.hongte.alms.common.result.Result;

import io.swagger.annotations.Api;

import javax.validation.Valid;
import java.util.Date;
import java.util.concurrent.Executor;

/**
 * @author 王继光
 * 2018年3月14日 下午8:05:02
 */
@RestController
@RequestMapping(value = "/collection", method = RequestMethod.POST)
@Api(value = "collection", description = "贷后系统  催收 对外接口")
public class  CollectionController {



	private Logger logger = LoggerFactory.getLogger(CollectionController.class);

	@Value("${bmApi.apiUrl}")
	String bmApiUrl;


	@Autowired
	@Qualifier("CollectionStatusService")
	CollectionStatusService collectionStatusService ;

	@Autowired
	@Qualifier("CarBasicService")
	CarBasicService carBasicService;

	@Autowired
	@Qualifier("CollectionLogService")
	CollectionLogService collectionLogService ;

	@Autowired
	CollectionRemoteApi collectionRemoteApi;

	@Value(value="${bmApi.apiUrl:http://127.0.0.1}")
	private String apiUrl;

	@Autowired
	private Executor executor;

	@Autowired
	private LoginUserInfoHelper loginUserInfoHelper;

	@Autowired
	@Qualifier("IssueSendOutsideLogService")
	IssueSendOutsideLogService issueSendOutsideLogService;

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

		executor.execute(new Runnable() {
			@Override
			public void run() {

				IssueSendOutsideLog log=new IssueSendOutsideLog();
				log.setCreateTime(new Date());
				log.setCreateUserId(loginUserInfoHelper.getUserId());
				log.setInterfacecode("open_CollectionController_update");
				log.setInterfacename("信贷业务结清更新贷后相关状态");
				log.setSendJsonEncrypt(businessId);
				log.setSendJson(JSON.toJSONString(businessId));
				log.setReturnJson(JSON.toJSONString(retBl));
				log.setReturnJsonDecrypt(JSON.toJSONString(retBl));
				log.setSystem("xindai");
				log.setSendUrl("/collection/update");

				issueSendOutsideLogService.insert(log);
			}
		});


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
	@ApiOperation(value = "财务撤销结清撤销，信贷调用此接口   更新贷后相关状态")
	@PostMapping("/settleRevoke")
	@ResponseBody
	@Transactional
	public Result  settleRevoke(String businessId){
		if (businessId==null||businessId.equals("")) {
			return Result.error("500", "businessId 不能为空");
		}
		logger.info("businessId:"+businessId);
		logger.info("bmApiUrl:"+bmApiUrl);
		//撤销设置贷后跟踪状态为关闭
		Boolean bl=  collectionStatusService.revokeClosedStatus(businessId);


		//撤销设置车辆状态为关闭
		Boolean retBl = carBasicService.revokeCarStatus(businessId);


		executor.execute(new Runnable() {
			@Override
			public void run() {

				IssueSendOutsideLog log=new IssueSendOutsideLog();
				log.setCreateTime(new Date());
				log.setCreateUserId(loginUserInfoHelper.getUserId());
				log.setInterfacecode("open_CollectionController_settleRevoke");
				log.setInterfacename("信贷财务撤销结清撤销");
				log.setSendJsonEncrypt(businessId);
				log.setSendJson(JSON.toJSONString(businessId));
				log.setReturnJson(JSON.toJSONString(retBl));
				log.setReturnJsonDecrypt(JSON.toJSONString(retBl));
				log.setSystem("xindai");
				log.setSendUrl("/collection/settleRevoke");

				issueSendOutsideLogService.insert(log);
			}
		});


		if(bl && retBl){
			return Result.success(businessId);
		}else{
			return Result.error("500", "数据更新失败");
		}


	}

	/**
	 * 同步电催人员
	 * @param carBusinessAfter
	 * @return
	 */
	@ApiOperation(value = "同步电催人员 接口")
	@PostMapping("/setPhoneStaff")
	@ResponseBody
	@Transactional
	public Result transferOnePhoneSet(@RequestBody @Valid CarBusinessAfter carBusinessAfter, BindingResult bindingResult){
		logger.info("bmApiUrl:"+bmApiUrl);
		logger.info("同步电催人员 开始 输入的请求信息：[{}]", JSON.toJSONString(carBusinessAfter));
		if(bindingResult.hasErrors()){
			String errorStr = ErroInfoUtil.getErroeInfo(bindingResult);
			logger.info("同步电催人员 接口,输入参数校验错误：" ,errorStr);
			executor.execute(new Runnable() {
				@Override
				public void run() {

					IssueSendOutsideLog log=new IssueSendOutsideLog();
					log.setCreateTime(new Date());
					log.setCreateUserId(loginUserInfoHelper.getUserId());
					log.setInterfacecode("open_CollectionController_transferOnePhoneSet");
					log.setInterfacename("同步电催人员 接口");
					log.setSendJsonEncrypt("");
					log.setSendJson(JSON.toJSONString(carBusinessAfter));
					log.setReturnJson(JSON.toJSONString(errorStr));
					log.setReturnJsonDecrypt("");
					log.setSystem("xindai");
					log.setSendUrl("/collection/setPhoneStaff");

					issueSendOutsideLogService.insert(log);
				}
			});
			return Result.error("500","输入参数校验错误："+errorStr);
		}

		Result ret = collectionRemoteApi.transferOnePhoneSet(carBusinessAfter);

		executor.execute(new Runnable() {
			@Override
			public void run() {

				IssueSendOutsideLog log=new IssueSendOutsideLog();
				log.setCreateTime(new Date());
				log.setCreateUserId(loginUserInfoHelper.getUserId());
				log.setInterfacecode("open_CollectionController_transferOnePhoneSet");
				log.setInterfacename("同步电催人员 接口");
				log.setSendJsonEncrypt("");
				log.setSendJson(JSON.toJSONString(carBusinessAfter));
				log.setReturnJson(JSON.toJSONString(ret));
				log.setReturnJsonDecrypt("");
				log.setSystem("xindai");
				log.setSendUrl("/collection/setPhoneStaff");

				issueSendOutsideLogService.insert(log);
			}
		});

		logger.info("同步电催人员 结束，结果：" ,ret.getMsg());

		return ret;
	}

	/**
	 * 同步催收人员
	 * @param collection
	 * @return
	 */
	@ApiOperation(value = "同步催收人员 接口")
	@PostMapping("/transferOneVisitStaffSet")
	@ResponseBody
	@Transactional
	public Result transferOneVisitStaffSet(@RequestBody @Valid Collection collection, BindingResult bindingResult){
		logger.info("bmApiUrl:"+bmApiUrl);
		logger.info("同步催收人员 开始 输入的请求信息：[{}]", JSON.toJSONString(collection));
		if(bindingResult.hasErrors()){
			String errorStr = ErroInfoUtil.getErroeInfo(bindingResult);
			logger.info("同步催收人员 接口,输入参数校验错误：" ,errorStr);
			executor.execute(new Runnable() {
				@Override
				public void run() {

					IssueSendOutsideLog log=new IssueSendOutsideLog();
					log.setCreateTime(new Date());
					log.setCreateUserId(loginUserInfoHelper.getUserId());
					log.setInterfacecode("open_CollectionController_transferOneVisitStaffSet");
					log.setInterfacename("同步催收人员 接口");
					log.setSendJsonEncrypt("");
					log.setSendJson(JSON.toJSONString(collection));
					log.setReturnJson(JSON.toJSONString(errorStr));
					log.setReturnJsonDecrypt("");
					log.setSystem("xindai");
					log.setSendUrl("/collection/transferOneVisitStaffSet");

					issueSendOutsideLogService.insert(log);
				}
			});
			return Result.error("500","输入参数校验错误："+errorStr);
		}
		Result ret = collectionRemoteApi.transOneCollectSet(collection);

		executor.execute(new Runnable() {
			@Override
			public void run() {

				IssueSendOutsideLog log=new IssueSendOutsideLog();
				log.setCreateTime(new Date());
				log.setCreateUserId(loginUserInfoHelper.getUserId());
				log.setInterfacecode("open_CollectionController_transferOneVisitStaffSet");
				log.setInterfacename("同步催收人员 接口");
				log.setSendJsonEncrypt("");
				log.setSendJson(JSON.toJSONString(collection));
				log.setReturnJson(JSON.toJSONString(ret));
				log.setReturnJsonDecrypt("");
				log.setSystem("xindai");
				log.setSendUrl("/collection/transferOneVisitStaffSet");

				issueSendOutsideLogService.insert(log);
			}
		});

		logger.info("同步催收人员 结束，结果：" ,ret.getMsg());

		return ret;
	}


	/**
	 * 同步贷后跟踪信息
	 * @param parametertracelog
	 * @return
	 */
	@ApiOperation(value = "同步贷后跟踪信息 接口 数据库能找到信贷主键ID则更新，找不到则新增")
	@PostMapping("/transferOneCollectionLog")
	@ResponseBody
	@Transactional
	public Result transferOneCollectionLog(@RequestBody @Valid Parametertracelog parametertracelog, BindingResult bindingResult){
		logger.info("bmApiUrl:"+bmApiUrl);
		logger.info("同步贷后跟踪信息 开始 输入的请求信息：[{}]", JSON.toJSONString(parametertracelog));
		if(bindingResult.hasErrors()){
			String errorStr = ErroInfoUtil.getErroeInfo(bindingResult);
			logger.info("同步贷后跟踪信息 接口,输入参数校验错误：" ,errorStr);
			executor.execute(new Runnable() {
				@Override
				public void run() {

					IssueSendOutsideLog log=new IssueSendOutsideLog();
					log.setCreateTime(new Date());
					log.setCreateUserId(loginUserInfoHelper.getUserId());
					log.setInterfacecode("open_CollectionController_transferOneCollectionLog");
					log.setInterfacename("同步贷后跟踪信息 接口");
					log.setSendJsonEncrypt("");
					log.setSendJson(JSON.toJSONString(parametertracelog));
					log.setReturnJson(JSON.toJSONString(errorStr));
					log.setReturnJsonDecrypt("");
					log.setSystem("xindai");
					log.setSendUrl("/collection/transferOneCollectionLog");

					issueSendOutsideLogService.insert(log);
				}
			});
			return Result.error("500","输入参数校验错误："+errorStr);
		}

		Result ret = collectionRemoteApi.transferOneCollectionLog(parametertracelog);
		executor.execute(new Runnable() {
			@Override
			public void run() {

				IssueSendOutsideLog log=new IssueSendOutsideLog();
				log.setCreateTime(new Date());
				log.setCreateUserId(loginUserInfoHelper.getUserId());
				log.setInterfacecode("open_CollectionController_transferOneCollectionLog");
				log.setInterfacename("同步贷后跟踪信息 接口");
				log.setSendJsonEncrypt("");
				log.setSendJson(JSON.toJSONString(parametertracelog));
				log.setReturnJson(JSON.toJSONString(ret));
				log.setReturnJsonDecrypt("");
				log.setSystem("xindai");
				log.setSendUrl("/collection/transferOneCollectionLog");

				issueSendOutsideLogService.insert(log);
			}
		});
		logger.info("同步贷后跟踪信息 结束，结果：" ,ret.getMsg());



		return ret;
	}


	@ApiOperation(value = "删除贷后跟踪信息 接口 ")
	@PostMapping("/deleteByxdId")
	@ResponseBody
	@Transactional
	public Result deleteByxdId(@RequestBody Integer xdIndexId){
		logger.info("bmApiUrl:"+bmApiUrl);
		logger.info("删除贷后跟踪信息 开始，参数：" ,xdIndexId);

		Result ret = collectionRemoteApi.deleteByxdId(xdIndexId);
		executor.execute(new Runnable() {
			@Override
			public void run() {

				IssueSendOutsideLog log=new IssueSendOutsideLog();
				log.setCreateTime(new Date());
				log.setCreateUserId(loginUserInfoHelper.getUserId());
				log.setInterfacecode("open_CollectionController_deleteByxdId");
				log.setInterfacename("删除贷后跟踪信息 接口");
				log.setSendJsonEncrypt("");
				log.setSendJson(JSON.toJSONString(xdIndexId));
				log.setReturnJson(JSON.toJSONString(ret));
				log.setReturnJsonDecrypt("");
				log.setSystem("xindai");
				log.setSendUrl("/collection/deleteByxdId");

				issueSendOutsideLogService.insert(log);
			}
		});

		logger.info("删除贷后跟踪信息 结束，结果：" ,ret.getMsg());

		return ret;
	}


	//  -------------   同步信息到信贷接口 开始  ====================

	@ApiOperation(value = "同步催收人员信息到信贷 接口")
	@PostMapping("/transferOneVisitSetToXd")
	@ResponseBody
	@Transactional
	public Result transferOneVisitSetToXd(@RequestBody @Valid Collection collection, BindingResult bindingResult) throws Exception {
		logger.info("bmApiUrl:"+bmApiUrl);
		logger.info("同步催收人员信息到信贷 开始 输入的请求信息：[{}]", JSON.toJSONString(collection));

		if(bindingResult.hasErrors()){
			String errorStr = ErroInfoUtil.getErroeInfo(bindingResult);
			logger.info("同步催收人员信息到信贷 接口,输入参数校验错误：" ,JSON.toJSONString(errorStr));

			executor.execute(new Runnable() {
				@Override
				public void run() {

					IssueSendOutsideLog log=new IssueSendOutsideLog();
					log.setCreateTime(new Date());
					log.setCreateUserId(loginUserInfoHelper.getUserId());
					log.setInterfacecode("open_CollectionController_transferOneVisitSetToXd");
					log.setInterfacename("删除贷后跟踪信息 接口");
					log.setSendJsonEncrypt("");
					log.setSendJson(JSON.toJSONString(collection));
					log.setReturnJson(JSON.toJSONString(errorStr));
					log.setReturnJsonDecrypt("");
					log.setSystem("xindai");
					log.setSendUrl("/collection/transferOneVisitSetToXd");

					issueSendOutsideLogService.insert(log);
				}
			});

			return Result.error("500","输入参数校验错误："+errorStr);
		}

		RequestData requestData = new RequestData();
		requestData.setData(JSON.toJSONString(collection));
		requestData.setMethodName("Collection_VisitSetTrans");
		String tem1 =JSON.toJSONString(requestData);
		// 请求数据加密
		String encryptStr = XinDaiEncryptUtil.encryptPostData(tem1);
			/* http://172.16.200.104:8084/apites是信贷接口域名，这里本机配置的，需要配置成自己的 */
		CollectionXindaiService collectionXindaiService = Feign.builder().target(CollectionXindaiService.class,
				apiUrl);
		String respStr = collectionXindaiService.transferOneVisitSet(encryptStr);
		// 返回数据解密
		ResponseData respData = XinDaiEncryptUtil.getRespData(respStr);


		executor.execute(new Runnable() {
			@Override
			public void run() {

				IssueSendOutsideLog log=new IssueSendOutsideLog();
				log.setCreateTime(new Date());
				log.setCreateUserId(loginUserInfoHelper.getUserId());
				log.setInterfacecode("open_CollectionController_transferOneVisitSetToXd");
				log.setInterfacename("删除贷后跟踪信息 接口");
				log.setSendJsonEncrypt(encryptStr);
				log.setSendJson(JSON.toJSONString(collection));
				log.setReturnJson(JSON.toJSONString(respData));
				log.setReturnJsonDecrypt(respStr);
				log.setSystem("xindai");
				log.setSendUrl("/collection/transferOneVisitSetToXd");

				issueSendOutsideLogService.insert(log);
			}
		});

//
//			String encryptStr = JSON.toJSONString(collection);
//			// 请求数据加密
//		try {
//			encryptStr = XinDaiEncryptUtil.encryptPostData(encryptStr);
//		} catch (Exception e) {
//			logger.info("同步催收人员信息到信贷 接口,加密数据异常：" ,e.fillInStackTrace());
//			e.printStackTrace();
//			return Result.error("500","同步电催人员信息到信贷 接口,加密数据异常："+e.fillInStackTrace());
//
//		}
//		CollectionXindaiService collectionXindaiService = Feign.builder().target(CollectionXindaiService.class,
//					apiUrl);
////
//			String respStr = collectionXindaiService.transferOneVisitSet(encryptStr);
////			// 返回数据解密
//			ResponseData respData = XinDaiEncryptUtil.getRespData(respStr);
			logger.info("同步催收人员信息到信贷接口返回数据:"+respData.getData());
//			String ixIndexId = respData.getData();
			return Result.success();

	}

	@ApiOperation(value = "同步电催人员信息到信贷 接口")
	@PostMapping("/transferOnePhoneSetToXd")
	@ResponseBody
	@Transactional
	public Result transferOnePhoneSetToXd(@RequestBody @Valid CarBusinessAfter carBusinessAfter, BindingResult bindingResult) throws Exception {

		logger.info("bmApiUrl:"+bmApiUrl);
		logger.info("同步电催人员信息到信贷 开始 输入的请求信息：[{}]", JSON.toJSONString(carBusinessAfter));

		if(bindingResult.hasErrors()){
			String errorStr = ErroInfoUtil.getErroeInfo(bindingResult);
			logger.info("同步电催人员信息到信贷 接口,输入参数校验错误：" ,errorStr);
			return Result.error("500","输入参数校验错误："+errorStr);
		}

		RequestData requestData = new RequestData();
		requestData.setData(JSON.toJSONString(carBusinessAfter));
		requestData.setMethodName("Collection_PhoneSetTrans");
		String encryptStr = JSON.toJSONString(requestData);
		// 请求数据加密
		encryptStr = XinDaiEncryptUtil.encryptPostData(encryptStr);
			/* http://172.16.200.104:8084/apites是信贷接口域名，这里本机配置的，需要配置成自己的 */
		CollectionXindaiService collectionXindaiService = Feign.builder().target(CollectionXindaiService.class,
				apiUrl);
		String respStr = collectionXindaiService.transferOnePhoneSet(encryptStr);
		// 返回数据解密
		ResponseData respData = XinDaiEncryptUtil.getRespData(respStr);




//		String encryptStr = JSON.toJSONString(carBusinessAfter);
//		// 请求数据加密
//		try {
//			encryptStr = XinDaiEncryptUtil.encryptPostData(encryptStr);
//		} catch (Exception e) {
//			logger.info("同步电催人员信息到信贷 接口,加密数据异常：" ,e.fillInStackTrace());
//			e.printStackTrace();
//			return Result.error("500","同步电催人员信息到信贷 接口,加密数据异常："+e.fillInStackTrace());
//
//		}
//		CollectionXindaiService collectionXindaiService = Feign.builder().target(CollectionXindaiService.class,
//				apiUrl);
////
//		String respStr = collectionXindaiService.transferOnePhoneSet(encryptStr);
////			// 返回数据解密
//		ResponseData respData = XinDaiEncryptUtil.getRespData(respStr);
		logger.info("同步电催人员信息到信贷接口返回数据:"+respData.getData());
//		String ixIndexId = respData.getData();
		return Result.success();

	}



	@ApiOperation(value = "同步贷后跟踪信息到信贷 接口  有主键ID则更新，无主键ID则新增")
	@PostMapping("/transferOneCollectionLogToXd")
	@ResponseBody
	@Transactional
	public Result<Integer> transferOneCollectionLogToXd(@RequestBody @Valid Parametertracelog parametertracelog, BindingResult bindingResult) throws Exception {
		logger.info("bmApiUrl:"+bmApiUrl);
		logger.info("同步贷后跟踪信息到信贷 开始 输入的请求信息：[{}]", JSON.toJSONString(parametertracelog));
		if(bindingResult.hasErrors()){
			String errorStr = ErroInfoUtil.getErroeInfo(bindingResult);
			logger.info("同步贷后跟踪信息到信贷 接口,输入参数校验错误：" ,errorStr);
			return Result.error("500","输入参数校验错误："+errorStr);
		}

		RequestData requestData = new RequestData();
		requestData.setData(JSON.toJSONString(parametertracelog));
		requestData.setMethodName("Collection_ColLogTrans");
		String encryptStr = JSON.toJSONString(requestData);
		// 请求数据加密
		encryptStr = XinDaiEncryptUtil.encryptPostData(encryptStr);
			/* http://172.16.200.104:8084/apites是信贷接口域名，这里本机配置的，需要配置成自己的 */
		CollectionXindaiService collectionXindaiService = Feign.builder().target(CollectionXindaiService.class,
				apiUrl);
		String respStr = collectionXindaiService.transferOneCollectionLog(encryptStr);
		// 返回数据解密
		ResponseData respData = XinDaiEncryptUtil.getRespData(respStr);



//
//		String encryptStr = JSON.toJSONString(parametertracelog);
//		// 请求数据加密
//
//		encryptStr = XinDaiEncryptUtil.encryptPostData(encryptStr);
//
//		CollectionXindaiService collectionXindaiService = Feign.builder().target(CollectionXindaiService.class,
//				apiUrl);
////
//		String respStr = collectionXindaiService.transferOneCollectionLog(encryptStr);
////			// 返回数据解密
//		ResponseData respData = XinDaiEncryptUtil.getRespData(respStr);
		logger.info("同步贷后跟踪信息到信贷接口返回数据:"+respData.getData());
		Integer xindaiId = Integer.valueOf(respData.getData());
		return Result.success(xindaiId);
	}


	@ApiOperation(value = "根据信贷ID删除信贷贷后跟踪记录")
	@PostMapping("/deleteXdCollectionLogById")
	@ResponseBody
	@Transactional
	public Result deleteXdCollectionLogById(@RequestBody Integer xdIndex) throws Exception {
		logger.info("根据信贷ID删除信贷贷后跟踪记录 开始 输入的请求信息：[{}]", xdIndex);

		logger.info("bmApiUrl:"+bmApiUrl);
		RequestData requestData = new RequestData();
		requestData.setData(JSON.toJSONString(xdIndex));
		requestData.setMethodName("Collection_DeleteColLog");
		String encryptStr = JSON.toJSONString(requestData);
		// 请求数据加密
		encryptStr = XinDaiEncryptUtil.encryptPostData(encryptStr);
			/* http://172.16.200.104:8084/apites是信贷接口域名，这里本机配置的，需要配置成自己的 */
		CollectionXindaiService collectionXindaiService = Feign.builder().target(CollectionXindaiService.class,
				apiUrl);
		String respStr = collectionXindaiService.deleteXdCollectionLogById(encryptStr);
		// 返回数据解密
		ResponseData respData = XinDaiEncryptUtil.getRespData(respStr);


//		String encryptStr = JSON.toJSONString(xdIndex);
//		// 请求数据加密
//
//		encryptStr = XinDaiEncryptUtil.encryptPostData(encryptStr);
//
//		CollectionXindaiService collectionXindaiService = Feign.builder().target(CollectionXindaiService.class,
//				apiUrl);
////
//		String respStr = collectionXindaiService.deleteXdCollectionLogById(encryptStr);
////			// 返回数据解密
//		ResponseData respData = XinDaiEncryptUtil.getRespData(respStr);
		logger.info("同步贷后跟踪信息到信贷接口返回数据:"+respData.getData());
//		Integer xindaiId = Integer.valueOf(respData.getData());
		return Result.success();
	}

	//  -------------   同步信息到信贷接口 结束  ====================










//	@ApiOperation(value = "查询代扣记录")
//	@GetMapping("/searchRepayLog")
//	public PageResult<List<RepayLogResp>>searchRepayLog(@RequestParam("originalBusinessId") String originalBusinessId,@RequestParam("afterId") String afterId) {
//		try {
//
//			RequestData requestData = new RequestData();
//			SearchRepayRecordReq req=new SearchRepayRecordReq();
//			req.setBusinessId(originalBusinessId);
//			requestData.setData(JSON.toJSONString(req));
//			requestData.setMethodName("AfterLoanRepayment_SearRepayLog");
//			String encryptStr = JSON.toJSONString(requestData);
//			// 请求数据加密
//			encryptStr = encryptPostData(encryptStr);
//			WithHoldingXinDaiService withholdingxindaiService = Feign.builder().target(WithHoldingXinDaiService.class,
//					apiUrl);
//
//			String respStr = withholdingxindaiService.searchRepayRecord(encryptStr);
//			// 返回数据解密
//			ResponseData respData = getRespData(respStr);
//			logger.info("代扣查询接口返回数据:"+respData.getData());
//			List<RepayLogResp> list=JSON.parseArray(respData.getData(), RepayLogResp.class);
//			//筛选对应本期的代扣记录
//			for(Iterator<RepayLogResp> it = list.iterator();it.hasNext();) {
//				RepayLogResp resp=it.next();
//				if(!resp.getAfterId().equals(afterId)) {
//					it.remove();
//				}
//			}
//			RepayLogResp repayLogResp=null;
//			for(int i=0;i< list.size();i++) {
//				repayLogResp=list.get(i);
//				repayLogResp.setListId(String.valueOf(i+1));
//				if(repayLogResp.getRepayStatus().equals("1")) {
//					repayLogResp.setRepayStatus("成功");
//				}else if(repayLogResp.getRepayStatus().equals("2")){
//					repayLogResp.setRepayStatus("处理中");
//				}else {
//					repayLogResp.setRepayStatus("失败");
//				}
//				if(repayLogResp.getBindPlatformId()==PlatformEnum.AN_FORM.getValue()) {
//					repayLogResp.setBindPlatform(PlatformEnum.AN_FORM.getName());
//				}else if(repayLogResp.getBindPlatformId()==PlatformEnum.BF_FORM.getValue()) {
//					repayLogResp.setBindPlatform(PlatformEnum.BF_FORM.getName());
//				}else if(repayLogResp.getBindPlatformId()==PlatformEnum.FY_FORM.getValue()) {
//					repayLogResp.setBindPlatform(PlatformEnum.FY_FORM.getName());
//				}else if(repayLogResp.getBindPlatformId()==PlatformEnum.YB_FORM.getValue()) {
//					repayLogResp.setBindPlatform(PlatformEnum.YB_FORM.getName());
//				}else if(repayLogResp.getBindPlatformId()==PlatformEnum.YS_FORM.getValue()) {
//					repayLogResp.setBindPlatform(PlatformEnum.YS_FORM.getName());
//				}else if(repayLogResp.getBindPlatformId()==PlatformEnum.YH_FORM.getValue()) {
//					repayLogResp.setBindPlatform(PlatformEnum.YH_FORM.getName());
//				}
//			}
//			return PageResult.success(list, list.size());
//		} catch (Exception ex) {
//			logger.error(ex.getMessage());
//			return PageResult.error(9999, "查询代扣记录出错");
//		}
//
//	}




}
