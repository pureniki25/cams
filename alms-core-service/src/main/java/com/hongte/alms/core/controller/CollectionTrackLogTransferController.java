package com.hongte.alms.core.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.collection.entity.CollectionTrackLog;
import com.hongte.alms.base.collection.entity.Parametertracelog;
import com.hongte.alms.base.collection.enums.CollectionSetWayEnum;
import com.hongte.alms.base.collection.enums.CollectionStatusEnum;
import com.hongte.alms.base.collection.service.CollectionTrackLogService;
import com.hongte.alms.base.collection.service.ParametertracelogService;
import com.hongte.alms.base.collection.vo.StaffBusinessVo;
import com.hongte.alms.base.entity.*;
import com.hongte.alms.base.collection.entity.Collection;
import com.hongte.alms.base.enums.SysParameterTypeEnums;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.base.service.RepaymentBizPlanService;
import com.hongte.alms.base.service.SysParameterService;
import com.hongte.alms.base.service.TransferFailLogService;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.util.StringUtil;
import com.ht.ussp.bean.LoginUserInfoHelper;
import com.ht.ussp.client.dto.LoginInfoDto;
import io.swagger.annotations.Api;
import io.swagger.models.auth.In;
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
 * @author:曾坤
 * @date: 2018/5/15
 */
@RestController
@RequestMapping("/collectionTrackLog")
@Api(tags = "CollectionTrackLogController", description = "将信贷的历史跟踪记录导入到贷后系统来")
public class CollectionTrackLogTransferController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CollectionTrackLogTransferController.class);

	private static Boolean runningFlage = false;

	@Autowired
	private LoginUserInfoHelper loginUserInfoHelper;

	@Autowired
	@Qualifier("ParametertracelogService")
	ParametertracelogService parametertracelogService;


	@Autowired
	@Qualifier("CollectionTrackLogService")
	CollectionTrackLogService collectionTrackLogService;

	@Autowired
	@Qualifier("RepaymentBizPlanService")
	RepaymentBizPlanService repaymentBizPlanService;

	@Autowired
	@Qualifier("RepaymentBizPlanListService")
	RepaymentBizPlanListService repaymentBizPlanListService;

	@Autowired
	@Qualifier("TransferFailLogService")
	TransferFailLogService transferFailLogService;

	@Autowired
	@Qualifier("SysParameterService")
	SysParameterService sysParameterService;

	@GetMapping("/transfer")
	@ResponseBody
	public Result transferCollectionTransfer() {

		if(runningFlage){
		  return Result.error("111111","同步程序执行中，请执行完再访问");
		}

		runningFlage = true;

		try{
			List<Parametertracelog> parametertracelogs = parametertracelogService.selectUnTransParametertracelogs();

			if(parametertracelogs!=null){
				for(Parametertracelog parametertracelog:parametertracelogs){
					String carBusinessId = parametertracelog.getCarBusinessId()!=null?parametertracelog.getCarBusinessId():UUID.randomUUID().toString();
					String carBusinessAfterId = parametertracelog.getCarBusinessAfterId() != null?parametertracelog.getCarBusinessAfterId():"default-2";

					try{

						CollectionTrackLog collectionTrackLog = new CollectionTrackLog();

						List<CollectionTrackLog> collectionTrackLogs = collectionTrackLogService.selectList(new EntityWrapper<CollectionTrackLog>().eq("xd_index_id",parametertracelog.getId()));
						if(collectionTrackLogs.size()>0){
							continue;
						}

						List<RepaymentBizPlanList> planLists =  repaymentBizPlanListService.selectList(new EntityWrapper<RepaymentBizPlanList>().
								eq("business_id",carBusinessId).
								and("after_id",carBusinessAfterId));

						if(planLists == null ||planLists.size() == 0){
							recordErrorInfo(carBusinessId,carBusinessAfterId,
									NoList,"1","无还款计划数据");
							continue;
						}else if(planLists.size()>1){
							recordErrorInfo(carBusinessId,carBusinessAfterId,
									DoubleList,"1","查出两条以上还款计划数据");
							continue;
						}


						collectionTrackLog.setRbpId(planLists.get(0).getPlanListId());

						String  bmUserId =parametertracelog.getCreateUser();
						// 根据信贷userId 获取贷后userId
						LoginInfoDto dto = new LoginInfoDto();
						if(bmUserId!=null&&!bmUserId.equals("")){
							dto = loginUserInfoHelper.getUserInfoByUserId("", bmUserId);
							if(dto==null){
//						collectionTrackLog.setRecorderUser(Constant.ADMIN_ID);
								recordErrorInfo(carBusinessId,carBusinessAfterId,
										NoUser,"1","没有用户信息");
							}
							collectionTrackLog.setRecorderUser(dto.getUserId());
						}else{
							collectionTrackLog.setRecorderUser(Constant.ADMIN_ID);
						}

						collectionTrackLog.setRecordDate(parametertracelog.getTranceDate());
						Integer defaultStatus =8;
						if(parametertracelog.getStatus()!=null){
							defaultStatus = parametertracelog.getStatus();
						}
						collectionTrackLog.setTrackStatusId(defaultStatus.toString());
						List<SysParameter> list = sysParameterService.selectList(new EntityWrapper<SysParameter>()
								.eq("param_type", SysParameterTypeEnums.COLLECTION_FOLLOW_STATUS.getKey())
								.eq("param_value",defaultStatus));
						String statusName = "";

						if(list.size()>0){
							statusName = list.get(0).getParamName();
						}
						collectionTrackLog.setTrackStatusName(statusName);
						collectionTrackLog.setIsSend(0);//是否传输平台，0：否，1：是
						collectionTrackLog.setContent(parametertracelog.getTranceContent());//记录内容
						collectionTrackLog.setCreateTime(new Date());
						collectionTrackLog.setCreateUser(Constant.SYS_DEFAULT_USER);
						collectionTrackLog.setUpdateTime(new Date());
						collectionTrackLog.setUpdateUser(Constant.SYS_DEFAULT_USER);
						collectionTrackLog.setUniqueId(UUID.randomUUID().toString());
						collectionTrackLog.setXdIndexId(parametertracelog.getId());

						collectionTrackLogService.insert(collectionTrackLog);



					}catch (Exception e){
						e.printStackTrace();
						LOGGER.error("同步历史贷后跟踪记录，同步数据出现异常信息"+e.getMessage());

						recordErrorInfo(carBusinessId,carBusinessAfterId,
								Exception,"1","同步数据出现异常信息");
					}

				}
			}

		}catch (Exception e){
			e.printStackTrace();
			LOGGER.error("同步历史贷后跟踪记录，查询列表异常"+e.getMessage());
			recordErrorInfo(UUID.randomUUID().toString(),"default_1",
					Exception,"","查询列表异常");
			return Result.error("200","同步历史贷后跟踪记录，查询列表异常");
		}

		LOGGER.error("完成一次历史贷后跟踪记录同步");
		return Result.success();

	}

	/**
	 *
	 * @param businessId
	 * @param afterId
	 * @param reson
	 * @param status
	 * @param failReson
	 */
	private  static  final  Integer DoubleList=4;//查出两条以上还款计划数据
	private  static  final  Integer NoList=1;//无还款计划数据
	private  static  final  Integer NoUser=2;//没有用户信息
	private  static  final  Integer NoStatusEnum=3;//状态信息不匹配
	private  static  final  Integer Exception=5;//catch到异常

	private void recordErrorInfo(String businessId ,String afterId,Integer reson,String status,String failReson){
		TransferFailLog failLog = new TransferFailLog();
		failLog.setBusinessId(businessId);
		failLog.setAfterId(afterId);
		failLog.setFailReason(reson);
		failLog.setTransType(2);//贷后跟踪记录同步
		TransferFailLog transferFailLog = transferFailLogService.selectOne(new EntityWrapper<TransferFailLog>()
				.eq("business_id", businessId).eq("after_id", afterId).eq("trans_type",2));
		LOGGER.error("信贷历史催收数据导入错误，"+failReson +"  businessID:"+businessId+"     afterId:"+afterId+"   status:"+status);
		if (transferFailLog == null) {
			transferFailLogService.insert(failLog);
		}
	}

	private  void deleteErrorInfo(String businessId,String afterId){

		TransferFailLog transferFailLog = transferFailLogService.selectOne(new EntityWrapper<TransferFailLog>()
				.eq("business_id", businessId).eq("after_id", afterId).eq("trans_type",2));
		if(transferFailLog !=null){
			transferFailLogService.deleteById(transferFailLog.getId());
		}
	}

	/**
	 *
	 * @param businessId
	 * @param afterId
	 * @param status
	 * @return
	 * Map<>:crpId userId staffType
	 */
//	private Map<String, String>  getStatus(String businessId,String afterId,String status,String bmUserId) {
//
//		Map<String, String> map = new HashMap<>();
//
//		List<RepaymentBizPlanList>  l  = repaymentBizPlanListService
//				.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("orig_business_id", businessId)
//						.eq("after_id", afterId));
//		RepaymentBizPlanList repaymentBizPlanList = l.size()>0?l.get(0):null;
//		if(l.size()>1){
//			recordErrorInfo(businessId ,afterId, DoubleList,status,"查出两条以上还款计划数据");
//			return null;
//		}
//		if (repaymentBizPlanList == null) {
//			recordErrorInfo(businessId ,afterId, NoList,status,"无还款计划数据");
//			return null;
//		}
//		map.put("crpId",repaymentBizPlanList.getPlanListId());
//
//		// 根据信贷userId 获取贷后userId
//		LoginInfoDto dto = new LoginInfoDto();
//		if(bmUserId!=null&&!bmUserId.equals("")){
//			dto = loginUserInfoHelper.getUserInfoByUserId("", bmUserId);
//		}
//
//		// 判断状态
//		String staffType ;
//		//催收中，电催需要 判断用户信息
//		if("电催".equals(status) ||"催收中".equals(status)||"催款中".equals(status)){
//			if (StringUtil.isEmpty(dto.getUserId())) {
//				recordErrorInfo(businessId ,afterId, NoUser,status,"没有用户信息");
//				return null;
//			}
//			map.put("userId",dto.getUserId());
//		}
//		if("电催".equals(status)){
//			staffType = CollectionStatusEnum.PHONE_STAFF.getPageStr();
//		}
//		else if ("催收中".equals(status)||"催款中".equals(status)) {
//			staffType = CollectionStatusEnum.COLLECTING.getPageStr();
//
//		}else if ("已拖车登记".equals(status)) {
//			staffType = CollectionStatusEnum.TRAILER_REG.getPageStr();
//
//		} else if ("已移交法务".equals(status)) {
//			staffType = CollectionStatusEnum.TO_LAW_WORK.getPageStr();
//
//		} else if ("已完成".equals(status)) {
//			staffType = CollectionStatusEnum.CLOSED.getPageStr();
////			collectionStatus = 200;
////			log.setCollectionUser("admin");
//		} else if ("二押已赎回".equals(status)) {
//			staffType = CollectionStatusEnum.REDEMPTION_REDEEMED.getPageStr();
////			log.setCollectionUser("admin");
////			collectionStatus = 250;
//		} else if ("已委外催收".equals(status)) {
//			staffType = CollectionStatusEnum.OUTSIDE_COLLECT.getPageStr();
////			log.setCollectionUser("admin");
////			collectionStatus = 300;
//		} else if ("待分配".equals(status)) {
//			staffType = CollectionStatusEnum.WAIT_TO_SET.getPageStr();
////			log.setCollectionUser("admin");
////			collectionStatus = 350;
//		} else if ("推迟移交法务".equals(status)) {
//				staffType = CollectionStatusEnum.DELAY_TO_LAW.getPageStr();
////			log.setCollectionUser("admin");
////			collectionStatus = 400;
//		} else {
//			recordErrorInfo(businessId ,afterId, NoStatusEnum,status,"状态信息不匹配");
//			return null;
//
//		}
//		map.put("staffType",staffType);
//
//		return map;
//	}



//	private Map<String, Object> getStatus(Collection collection) {
//
//		Map<String, Object> map = new HashMap<>();
//
//		List<RepaymentBizPlanList>  l  = repaymentBizPlanListService
//				.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("orig_business_id", collection.getBusinessId())
//						.eq("after_id", collection.getAfterId()));
//		RepaymentBizPlanList repaymentBizPlanList = l.size()>0?l.get(0):null;
//		if(l.size()>1){
//			LOGGER.error("还款计划查出两条以上数据：collectionLogXd   ， businessID:"+collection.getBusinessId()+"     afterId:"+collection.getAfterId());
//		}
//		if (repaymentBizPlanList == null) {
//			TransferFailLog failLog = new TransferFailLog();
//			failLog.setBusinessId(collection.getBusinessId());
//			failLog.setAfterId(collection.getAfterId());
//			failLog.setFailReason(1);
//			TransferFailLog transferFailLog = transferFailLogService.selectOne(new EntityWrapper<TransferFailLog>()
//					.eq("business_id", collection.getBusinessId()).eq("after_id", collection.getAfterId()));
//			if (transferFailLog == null) {
//				transferFailLogService.insert(failLog);
//			}
//
//			return null;
//		}
//
//		// 根据信贷userId 获取贷后userId
//		LoginInfoDto dto = loginUserInfoHelper.getUserInfoByUserId("", collection.getCollectionUser());
//		if (dto == null) {
//			dto = new LoginInfoDto();
//		}
//		CollectionStatus status = new CollectionStatus();
//		CollectionLog log = new CollectionLog();
//
//		status.setBusinessId(collection.getBusinessId());
//		status.setCrpId(repaymentBizPlanList.getPlanListId());
//		// 判断状态
//		int collectionStatus = 0;
//		if ("催收中".equals(collection.getStatus())) {
//			collectionStatus = 50;
//			status.setVisitStaff(dto.getUserId());
//			log.setCollectionUser(dto.getUserId());
//			if (StringUtil.isEmpty(dto.getUserId())) {
//				TransferFailLog failLog = new TransferFailLog();
//				failLog.setBusinessId(collection.getBusinessId());
//				failLog.setAfterId(collection.getAfterId());
//				failLog.setFailReason(2);
//				TransferFailLog transferFailLog = transferFailLogService.selectOne(new EntityWrapper<TransferFailLog>()
//						.eq("business_id", collection.getBusinessId()).eq("after_id", collection.getAfterId()));
//				if (transferFailLog == null) {
//					transferFailLogService.insert(failLog);
//				}
//
//				return null;
//			}
//		} else if ("已移交法务".equals(collection.getStatus())) {
//			log.setCollectionUser("admin");
//			collectionStatus = 100;
//		} else if ("已完成".equals(collection.getStatus())) {
//			collectionStatus = 200;
//			log.setCollectionUser("admin");
//		} else if ("二押已赎回".equals(collection.getStatus())) {
//			log.setCollectionUser("admin");
//			collectionStatus = 250;
//		} else if ("已委外催收".equals(collection.getStatus())) {
//			log.setCollectionUser("admin");
//			collectionStatus = 300;
//		} else if ("待分配".equals(collection.getStatus())) {
//			log.setCollectionUser("admin");
//			collectionStatus = 350;
//		} else if ("推迟移交法务".equals(collection.getStatus())) {
//			log.setCollectionUser("admin");
//			collectionStatus = 400;
//		} else {
//			log.setCollectionUser("admin");
//		}
//		status.setCollectionStatus(collectionStatus);
//		status.setCreateTime(new Date());
//		status.setCreateUser("admin");
//		status.setUpdateTime(new Date());
//		status.setUpdateUser("admin");
//		status.setDescribe("信贷历史数据导入");
//		status.setSetWay(0);
//		status.setCrpType(ifPlanListIsLast(collection.getBusinessId(), repaymentBizPlanList.getPlanListId()) ? 2 : 1);
//
//		log.setBusinessId(collection.getBusinessId());
//		log.setCrpId(repaymentBizPlanList.getPlanListId());
//		log.setAfterStatus(collectionStatus);
//		log.setCreateTime(new Date());
//		log.setCreateUser("admin");
//		log.setUpdateTime(new Date());
//		log.setUpdateUser("admin");
//		log.setDescribe("信贷历史数据导入");
//		log.setSetWay(0);
//		log.setBeforeStatus(null);
//		log.setSetTypeStatus(collectionStatus);
//
//		map.put("status", status);
//		map.put("log", log);
//
//		return map;
//	}

	/**
	 * 判断还款计划是否是最后一个
	 *
	 * @param
	 * @return
	 */
	public boolean ifPlanListIsLast(String businessId, String planListId) {
		RepaymentBizPlanList pList = repaymentBizPlanListService.selectOne(
				new EntityWrapper<RepaymentBizPlanList>().eq("orig_business_id", businessId).orderBy("due_date desc"));
		return pList.getPlanListId().equals(planListId);
	}




//	private Map<String, Object> getStatus(CollectionLogXd collectionLogXd) {
//
//		Map<String, Object> map = new HashMap<>();
//
//		RepaymentBizPlanList repaymentBizPlanList = repaymentBizPlanListService
//				.selectOne(new EntityWrapper<RepaymentBizPlanList>().eq("orig_business_id", collectionLogXd.getBusinessId())
//						.eq("after_id", collectionLogXd.getAfterId()));
//
//		if (repaymentBizPlanList == null) {
//			TransferFailLog failLog = new TransferFailLog();
//			failLog.setBusinessId(collectionLogXd.getBusinessId());
//			failLog.setAfterId(collectionLogXd.getAfterId());
//			failLog.setFailReason(1);
//			TransferFailLog transferFailLog = transferFailLogService.selectOne(
//					new EntityWrapper<TransferFailLog>().eq("business_id", collectionLogXd.getBusinessId())
//							.eq("after_id", collectionLogXd.getAfterId()));
//			if (transferFailLog == null) {
//				transferFailLogService.insert(failLog);
//			}
//
//			return null;
//		}
//
//		// 根据信贷userId 获取贷后userId
//		LoginInfoDto dto = loginUserInfoHelper.getUserInfoByUserId("", collectionLogXd.getCollectionUser());
//		if (dto == null) {
//			dto = new LoginInfoDto();
//		}
//		CollectionStatus status = new CollectionStatus();
//		CollectionLog log = new CollectionLog();
//
//		status.setBusinessId(collectionLogXd.getBusinessId());
//		status.setCrpId(repaymentBizPlanList.getPlanListId());
//
//		status.setCollectionStatus(1);
//		if (StringUtil.isEmpty(dto.getUserId())) {
//			TransferFailLog failLog = new TransferFailLog();
//			failLog.setBusinessId(collectionLogXd.getBusinessId());
//			failLog.setAfterId(collectionLogXd.getAfterId());
//			failLog.setFailReason(2);
//			TransferFailLog transferFailLog = transferFailLogService.selectOne(new EntityWrapper<TransferFailLog>()
//					.eq("business_id", collectionLogXd.getBusinessId()).eq("after_id", collectionLogXd.getAfterId()));
//			if (transferFailLog == null) {
//				transferFailLogService.insert(failLog);
//			}
//
//			return null;
//		}
//		status.setPhoneStaff(dto.getUserId());
//		status.setCreateTime(new Date());
//		status.setCreateUser("admin");
//		status.setUpdateTime(new Date());
//		status.setUpdateUser("admin");
//		status.setDescribe("信贷历史数据导入");
//		status.setSetWay(0);
//		status.setCrpType(
//				ifPlanListIsLast(collectionLogXd.getBusinessId(), repaymentBizPlanList.getPlanListId()) ? 2 : 1);
//
//		log.setBusinessId(collectionLogXd.getBusinessId());
//		log.setCrpId(repaymentBizPlanList.getPlanListId());
//		log.setAfterStatus(1);
//		log.setCollectionUser(dto.getUserId());
//		log.setCreateTime(new Date());
//		log.setCreateUser("admin");
//		log.setUpdateTime(new Date());
//		log.setUpdateUser("admin");
//		log.setDescribe("信贷历史数据导入");
//		log.setSetWay(0);
//		log.setBeforeStatus(null);
//		log.setSetTypeStatus(1);
//
//		map.put("status", status);
//		map.put("log", log);
//
//		return map;
//
//	}

}
