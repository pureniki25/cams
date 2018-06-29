package com.hongte.alms.core.controller;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.collection.enums.CollectionSetWayEnum;
import com.hongte.alms.base.collection.enums.CollectionStatusEnum;
import com.hongte.alms.base.collection.service.CollectionLogService;
import com.hongte.alms.base.collection.service.CollectionLogXdService;
import com.hongte.alms.base.collection.service.CollectionService;
import com.hongte.alms.base.collection.service.CollectionStatusService;
import com.hongte.alms.base.collection.vo.StaffBusinessVo;
import com.hongte.alms.base.entity.*;
import com.hongte.alms.base.collection.entity.Collection;
import com.hongte.alms.base.service.*;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.ListUtil;
import com.hongte.alms.common.util.StringUtil;
import com.ht.ussp.bean.LoginUserInfoHelper;
import com.ht.ussp.client.dto.LoginInfoDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.concurrent.Executor;

/**
 * @author:曾坤
 * @date: 2018/5/21
 */
@RestController
@RequestMapping("/alms")
@Api(tags = "CollectionTrackLogController", description = "信贷数据库历史催收数据导入相关接口")
public class CollectionTransferController {

	private static final Logger LOGGER = LoggerFactory.getLogger(CollectionTransferController.class);

	private static Boolean runningFlage = false;


	//异步执行线程1执行标志位
	private  static Boolean thread1Execute = false;
	//异步执行线程2执行标志位
	private  static Boolean thread2Execute = false;
	//异步执行线程3执行标志位
	private  static Boolean thread3Execute = false;
	//异步执行线程4执行标志位
	private  static Boolean thread4Execute = false;
	//异步执行线程5执行标志位
	private  static Boolean thread5Execute = false;
	//异步执行线程6执行标志位
	private  static Boolean thread6Execute = false;
	//异步执行线程7执行标志位
	private  static Boolean thread7Execute = false;
	//异步执行线程8执行标志位
	private  static Boolean thread8Execute = false;
	//异步执行线程9执行标志位
	private  static Boolean thread9Execute = false;
	//异步执行线程10执行标志位
	private  static Boolean thread10Execute = false;

	//同步单个用户的催收数据执行标志位
	private  static Boolean oneUserColRunning = false;

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


	@Autowired
	@Qualifier("CarBusinessAfterService")
	private CarBusinessAfterService  carBusinessAfterService;

	@Autowired
	Executor executor;

	@ApiModelProperty("同步电催数据")
	@GetMapping("/transfer")
	@ResponseBody
	public Result transferCollection() {

		if(runningFlage){
			return Result.error("111111","同步程序执行中，请执行完再访问");
		}

		runningFlage = true;

		try{

			//1.历史电催数据同步
			//1）.当前状态

			List<CarBusinessAfter>  afterList = carBusinessAfterService.queryNotTransferCollectionLog();
			if (!CollectionUtils.isEmpty(afterList)) {

				List<List<CarBusinessAfter>>  averageList = ListUtil.averageAssign(afterList,10);

				Integer index =0;

				//第一条线程
				List<CarBusinessAfter>  list1 = averageList.get(0);
				thread1Execute=true;
				executor.execute(new Runnable() {
					@Override
					public void run() {

						try{
							for (CarBusinessAfter carBusinessAfter : list1) {
								transPhoneSet(carBusinessAfter,null);
							}
						}catch(Exception e){
							e.printStackTrace();
							LOGGER.error("同步历史电催数据异常,列表查询异常"+e.getMessage());
						}
						thread1Execute =false;
						if(!thread1Execute&& !thread2Execute&& !thread3Execute &&  !thread4Execute && !thread5Execute
								&& !thread6Execute && !thread7Execute&& !thread8Execute && !thread9Execute && !thread10Execute){
							runningFlage =false;
						}
					}
				});

				//第二条线程
				List<CarBusinessAfter>  list2 = averageList.get(1);
				thread2Execute=true;
				executor.execute(new Runnable() {
					@Override
					public void run() {

						try{
							for (CarBusinessAfter carBusinessAfter : list2) {
								transPhoneSet(carBusinessAfter,null);
							}
						}catch(Exception e){
							e.printStackTrace();
							LOGGER.error("同步历史电催数据异常,列表查询异常"+e.getMessage());
						}
						thread2Execute =false;
						if(!thread1Execute&& !thread2Execute&& !thread3Execute &&  !thread4Execute && !thread5Execute
								&& !thread6Execute && !thread7Execute&& !thread8Execute && !thread9Execute && !thread10Execute){
							runningFlage =false;
						}
					}
				});


				//第三条线程
				List<CarBusinessAfter>  list3 = averageList.get(2);
				thread3Execute=true;
				executor.execute(new Runnable() {
					@Override
					public void run() {

						try{
							for (CarBusinessAfter carBusinessAfter : list3) {
								transPhoneSet(carBusinessAfter,null);
							}
						}catch(Exception e){
							e.printStackTrace();
							LOGGER.error("同步历史电催数据异常,列表查询异常"+e.getMessage());
						}
						thread3Execute =false;
						if(!thread1Execute&& !thread2Execute&& !thread3Execute &&  !thread4Execute && !thread5Execute
								&& !thread6Execute && !thread7Execute&& !thread8Execute && !thread9Execute && !thread10Execute){
							runningFlage =false;
						}
					}
				});

				//第四条线程
				List<CarBusinessAfter>  list4 = averageList.get(3);
				thread4Execute=true;
				executor.execute(new Runnable() {
					@Override
					public void run() {

						try{
							for (CarBusinessAfter carBusinessAfter : list4) {
								transPhoneSet(carBusinessAfter,null);
							}
						}catch(Exception e){
							e.printStackTrace();
							LOGGER.error("同步历史电催数据异常,列表查询异常"+e.getMessage());
						}
						thread4Execute =false;
						if(!thread1Execute&& !thread2Execute&& !thread3Execute &&  !thread4Execute && !thread5Execute
								&& !thread6Execute && !thread7Execute&& !thread8Execute && !thread9Execute && !thread10Execute){
							runningFlage =false;
						}
					}
				});


				//第五条线程
				List<CarBusinessAfter>  list5 = averageList.get(4);
				thread5Execute=true;
				executor.execute(new Runnable() {
					@Override
					public void run() {

						try{
							for (CarBusinessAfter carBusinessAfter : list5) {
								transPhoneSet(carBusinessAfter,null);
							}
						}catch(Exception e){
							e.printStackTrace();
							LOGGER.error("同步历史电催数据异常,列表查询异常"+e.getMessage());
						}
						thread5Execute =false;
						if(!thread1Execute&& !thread2Execute&& !thread3Execute &&  !thread4Execute && !thread5Execute
								&& !thread6Execute && !thread7Execute&& !thread8Execute && !thread9Execute && !thread10Execute){
							runningFlage =false;
						}
					}
				});

				//第六条线程
				List<CarBusinessAfter>  list6 = averageList.get(5);
				thread6Execute=true;
				executor.execute(new Runnable() {
					@Override
					public void run() {

						try{
							for (CarBusinessAfter carBusinessAfter : list6) {
								transPhoneSet(carBusinessAfter,null);
							}
						}catch(Exception e){
							e.printStackTrace();
							LOGGER.error("同步历史电催数据异常,列表查询异常"+e.getMessage());
						}
						thread6Execute =false;
						if(!thread1Execute&& !thread2Execute&& !thread3Execute &&  !thread4Execute && !thread5Execute
								&& !thread6Execute && !thread7Execute&& !thread8Execute && !thread9Execute && !thread10Execute){
							runningFlage =false;
						}
					}
				});


				//第七条线程
				List<CarBusinessAfter>  list7 = averageList.get(6);
				thread7Execute=true;
				executor.execute(new Runnable() {
					@Override
					public void run() {

						try{
							for (CarBusinessAfter carBusinessAfter : list7) {
								transPhoneSet(carBusinessAfter,null);
							}
						}catch(Exception e){
							e.printStackTrace();
							LOGGER.error("同步历史电催数据异常,列表查询异常"+e.getMessage());
						}
						thread7Execute =false;
						if(!thread1Execute&& !thread2Execute&& !thread3Execute &&  !thread4Execute && !thread5Execute
								&& !thread6Execute && !thread7Execute&& !thread8Execute && !thread9Execute && !thread10Execute){
							runningFlage =false;
						}
					}
				});

				//第八条线程
				List<CarBusinessAfter>  list8 = averageList.get(7);
				thread8Execute=true;
				executor.execute(new Runnable() {
					@Override
					public void run() {

						try{
							for (CarBusinessAfter carBusinessAfter : list8) {
								transPhoneSet(carBusinessAfter,null);
							}
						}catch(Exception e){
							e.printStackTrace();
							LOGGER.error("同步历史电催数据异常,列表查询异常"+e.getMessage());
						}
						thread8Execute =false;
						if(!thread1Execute&& !thread2Execute&& !thread3Execute &&  !thread4Execute && !thread5Execute
								&& !thread6Execute && !thread7Execute&& !thread8Execute && !thread9Execute && !thread10Execute){
							runningFlage =false;
						}
					}
				});

				//第九条线程
				List<CarBusinessAfter>  list9 = averageList.get(8);
				thread9Execute=true;
				executor.execute(new Runnable() {
					@Override
					public void run() {

						try{
							for (CarBusinessAfter carBusinessAfter : list9) {
								transPhoneSet(carBusinessAfter,null);
							}
						}catch(Exception e){
							e.printStackTrace();
							LOGGER.error("同步历史电催数据异常,列表查询异常"+e.getMessage());
						}
						thread9Execute =false;
						if(!thread1Execute&& !thread2Execute&& !thread3Execute &&  !thread4Execute && !thread5Execute
								&& !thread6Execute && !thread7Execute&& !thread8Execute && !thread9Execute && !thread10Execute){
							runningFlage =false;
						}
					}
				});

				//第十条线程
				List<CarBusinessAfter>  list10 = averageList.get(9);
				thread10Execute=true;
				executor.execute(new Runnable() {
					@Override
					public void run() {

						try{
							for (CarBusinessAfter carBusinessAfter : list10) {
								transPhoneSet(carBusinessAfter,null);
							}
						}catch(Exception e){
							e.printStackTrace();
							LOGGER.error("同步历史电催数据异常,列表查询异常"+e.getMessage());
						}
						thread10Execute =false;
						if(!thread1Execute&& !thread2Execute&& !thread3Execute &&  !thread4Execute && !thread5Execute
								&& !thread6Execute && !thread7Execute&& !thread8Execute && !thread9Execute && !thread10Execute){
							runningFlage =false;
						}
					}
				});


				Thread.sleep(10*1000);

				//等待5条线程都执行完
//				while(runningFlage){
////					if(!thread1Execute&& !thread2Execute&& !thread3Execute &&  !thread4Execute && !thread5Execute){
////						runningFlage =false;
////					}
//					Thread.sleep(5*1000);
//				}




//				for (CarBusinessAfter carBusinessAfter : afterList) {
//					transPhoneSet(carBusinessAfter);
//
//
//
//				}
//				continue;
			}

//			int neverTransPhoneCount =  carBusinessAfterService.queryNotTransferPhoneUserCount();
//			for (int i = 0; i <= neverTransPhoneCount / 5000 + 1; i++) {
//				CollectionReq req = new CollectionReq();
//				req.setOffSet(i * 5000);
//				req.setPageSize(5000);
//				List<CarBusinessAfter>  afterList =  carBusinessAfterService.queryNotTransferCollectionLog(req);
//				if (CollectionUtils.isEmpty(afterList)) {
//					continue;
//				}
//				for (CarBusinessAfter carBusinessAfter : afterList) {
//					transPhoneSet(carBusinessAfter);
//				}
//
//			}
		}catch (Exception e){

			e.printStackTrace();
			LOGGER.error("同步历史电催数据异常,列表查询异常"+e.getMessage());
			return Result.error("111111","同步历史电催数据异常"+e.getMessage());
		}
		LOGGER.error("开始一次电催数据同步（异步执行）");
//		runningFlage = false;
		return Result.success();

	}

	@ApiModelProperty("同步指定业务或期数的电催数据")
	@GetMapping("/transferOneCollection")
	@ResponseBody
	public Result transferOneCollection(String businessId, String afterId){

		List<CarBusinessAfter>  afterList = null;
		if(afterId!=null){
			afterList = carBusinessAfterService.selectList(
					new EntityWrapper<CarBusinessAfter>().
							eq("car_business_id",businessId)
							.eq("car_business_after_id",afterId));
		}else{
			afterList = carBusinessAfterService.selectList(
					new EntityWrapper<CarBusinessAfter>().
							eq("car_business_id",businessId));
		}

		if(afterList!=null){
			for (CarBusinessAfter carBusinessAfter : afterList) {
				boolean bl = transPhoneSet(carBusinessAfter,CollectionSetWayEnum.XINDAI_LOG_ONE);
			}
		}


		return Result.success();

	}


	@ApiModelProperty("同步指定业务或期数的电催数据")
	@GetMapping("/transferOneUserCollection")
	@ResponseBody
	public Result transferOneUserCollection(String userId){

		if(oneUserColRunning){
			return Result.error("111111","同步程序执行中，请执行完再访问");
		}
		if(userId!=null){
			List<CarBusinessAfter>  afterList = carBusinessAfterService.selectList(
					new EntityWrapper<CarBusinessAfter>().
							eq("collection_user",userId));
			if(afterList!=null && afterList.size()>0){
				executor.execute(new Runnable() {
					@Override
					public void run() {
						oneUserColRunning = true;
						try{
							for (CarBusinessAfter carBusinessAfter : afterList) {
								boolean bl = transPhoneSet(carBusinessAfter,CollectionSetWayEnum.XINDAI_LOG);
							}
						}catch(Exception e){
							e.printStackTrace();
							LOGGER.error("同步历史电催数据异常,列表查询异常"+e.getMessage());
						}
						oneUserColRunning=false;
					}
				});
			}

		}

		return Result.success();

	}

	@ApiModelProperty("同步上门催收数据")
	@GetMapping("/transferVisit")
	@ResponseBody
	public Result transferVisitCollection(){
		try{
			//历史催收数据同步
			runningFlage = true;
			//1.同步有设置催收人员的collection
			List<Collection> collectionList  = collectionService.selectList(new EntityWrapper<Collection>().isNotNull("collection_user"));
			for(Collection collection:collectionList){
				collection.setStatus("催收中");
				transCollectSet(collection);
			}

			//同步所有的催收信息
			collectionList = collectionService.selectList(new EntityWrapper<Collection>());

			if (!CollectionUtils.isEmpty(collectionList)) {
				List<List<Collection>>  averageList = ListUtil.averageAssign(collectionList,10);


				Integer index =0;

				//第一条线程
				List<Collection>  list1 = averageList.get(0);
				executor.execute(new Runnable() {
					@Override
					public void run() {
						thread1Execute=true;
						try{
							for (Collection collection : list1) {
								transCollectSet(collection);
							}
						}catch(Exception e){
							e.printStackTrace();
							LOGGER.error("同步历史电催数据异常,列表查询异常"+e.getMessage());
						}
						thread1Execute =false;
						if(!thread1Execute&& !thread2Execute&& !thread3Execute &&  !thread4Execute && !thread5Execute
								&& !thread6Execute && !thread7Execute&& !thread8Execute && !thread9Execute && !thread10Execute){
							runningFlage =false;
						}
					}
				});

				//第二条线程
				List<Collection>  list2 = averageList.get(1);
				executor.execute(new Runnable() {
					@Override
					public void run() {
						thread2Execute=true;
						try{
							for (Collection collection : list2) {
								transCollectSet(collection);
							}
						}catch(Exception e){
							e.printStackTrace();
							LOGGER.error("同步历史电催数据异常,列表查询异常"+e.getMessage());
						}
						thread2Execute =false;
						if(!thread1Execute&& !thread2Execute&& !thread3Execute &&  !thread4Execute && !thread5Execute
								&& !thread6Execute && !thread7Execute&& !thread8Execute && !thread9Execute && !thread10Execute){
							runningFlage =false;
						}
					}
				});


				//第三条线程
				List<Collection>  list3 = averageList.get(2);
				executor.execute(new Runnable() {
					@Override
					public void run() {
						thread3Execute=true;
						try{
							for (Collection collection : list3) {
								transCollectSet(collection);
							}
						}catch(Exception e){
							e.printStackTrace();
							LOGGER.error("同步历史电催数据异常,列表查询异常"+e.getMessage());
						}
						thread3Execute =false;
						if(!thread1Execute&& !thread2Execute&& !thread3Execute &&  !thread4Execute && !thread5Execute
								&& !thread6Execute && !thread7Execute&& !thread8Execute && !thread9Execute && !thread10Execute){
							runningFlage =false;
						}
					}
				});

				//第四条线程
				List<Collection>  list4 = averageList.get(3);
				executor.execute(new Runnable() {
					@Override
					public void run() {
						thread4Execute=true;
						try{
							for (Collection collection : list4) {
								transCollectSet(collection);
							}
						}catch(Exception e){
							e.printStackTrace();
							LOGGER.error("同步历史电催数据异常,列表查询异常"+e.getMessage());
						}
						thread4Execute =false;
						if(!thread1Execute&& !thread2Execute&& !thread3Execute &&  !thread4Execute && !thread5Execute
								&& !thread6Execute && !thread7Execute&& !thread8Execute && !thread9Execute && !thread10Execute){
							runningFlage =false;
						}
					}
				});


				//第五条线程
				List<Collection>  list5 = averageList.get(4);
				executor.execute(new Runnable() {
					@Override
					public void run() {
						thread5Execute=true;
						try{
							for (Collection collection : list5) {
								transCollectSet(collection);
							}
						}catch(Exception e){
							e.printStackTrace();
							LOGGER.error("同步历史电催数据异常,列表查询异常"+e.getMessage());
						}
						thread5Execute =false;
						if(!thread1Execute&& !thread2Execute&& !thread3Execute &&  !thread4Execute && !thread5Execute
								&& !thread6Execute && !thread7Execute&& !thread8Execute && !thread9Execute && !thread10Execute){
							runningFlage =false;
						}
					}
				});

				//第六条线程
				List<Collection>  list6 = averageList.get(5);
				executor.execute(new Runnable() {
					@Override
					public void run() {
						thread6Execute=true;
						try{
							for (Collection collection : list6) {
								transCollectSet(collection);
							}
						}catch(Exception e){
							e.printStackTrace();
							LOGGER.error("同步历史电催数据异常,列表查询异常"+e.getMessage());
						}
						thread6Execute =false;
						if(!thread1Execute&& !thread2Execute&& !thread3Execute &&  !thread4Execute && !thread5Execute
								&& !thread6Execute && !thread7Execute&& !thread8Execute && !thread9Execute && !thread10Execute){
							runningFlage =false;
						}
					}
				});


				//第七条线程
				List<Collection>  list7 = averageList.get(6);
				executor.execute(new Runnable() {
					@Override
					public void run() {
						thread7Execute=true;
						try{
							for (Collection collection : list7) {
								transCollectSet(collection);
							}
						}catch(Exception e){
							e.printStackTrace();
							LOGGER.error("同步历史电催数据异常,列表查询异常"+e.getMessage());
						}
						thread7Execute =false;
						if(!thread1Execute&& !thread2Execute&& !thread3Execute &&  !thread4Execute && !thread5Execute
								&& !thread6Execute && !thread7Execute&& !thread8Execute && !thread9Execute && !thread10Execute){
							runningFlage =false;
						}
					}
				});

				//第八条线程
				List<Collection>  list8 = averageList.get(7);
				executor.execute(new Runnable() {
					@Override
					public void run() {
						thread8Execute=true;
						try{
							for (Collection collection : list8) {
								transCollectSet(collection);
							}
						}catch(Exception e){
							e.printStackTrace();
							LOGGER.error("同步历史电催数据异常,列表查询异常"+e.getMessage());
						}
						thread8Execute =false;
						if(!thread1Execute&& !thread2Execute&& !thread3Execute &&  !thread4Execute && !thread5Execute
								&& !thread6Execute && !thread7Execute&& !thread8Execute && !thread9Execute && !thread10Execute){
							runningFlage =false;
						}
					}
				});

				//第九条线程
				List<Collection>  list9 = averageList.get(8);
				executor.execute(new Runnable() {
					@Override
					public void run() {
						thread9Execute=true;
						try{
							for (Collection collection : list9) {
								transCollectSet(collection);
							}
						}catch(Exception e){
							e.printStackTrace();
							LOGGER.error("同步历史电催数据异常,列表查询异常"+e.getMessage());
						}
						thread9Execute =false;
						if(!thread1Execute&& !thread2Execute&& !thread3Execute &&  !thread4Execute && !thread5Execute
								&& !thread6Execute && !thread7Execute&& !thread8Execute && !thread9Execute && !thread10Execute){
							runningFlage =false;
						}
					}
				});

				//第十条线程
				List<Collection>  list10 = averageList.get(9);
				executor.execute(new Runnable() {
					@Override
					public void run() {
						thread10Execute=true;
						try{
							for (Collection collection : list10) {
								transCollectSet(collection);
							}
						}catch(Exception e){
							e.printStackTrace();
							LOGGER.error("同步历史电催数据异常,列表查询异常"+e.getMessage());
						}
						thread10Execute =false;
						if(!thread1Execute&& !thread2Execute&& !thread3Execute &&  !thread4Execute && !thread5Execute
								&& !thread6Execute && !thread7Execute&& !thread8Execute && !thread9Execute && !thread10Execute){
							runningFlage =false;
						}
					}
				});


				Thread.sleep(60*1000);

				//等待5条线程都执行完
//				while(true){
////					if(thread1Execute&& thread2Execute&& thread3Execute &&  thread4Execute && thread5Execute){
////						break;
////					}
//					Thread.sleep(5*1000);
//				}


//				for (Collection collection : collectionList) {
//					transCollectSet(collection);
//				}
			}else{
				runningFlage  = false;
			}


//						int neverTransColCount =  collectionService.queryNotTransferCollectionCount();
//			for (int i = 0; i <= neverTransColCount / 5000 + 1; i++) {
//				CollectionReq req = new CollectionReq();
//				req.setOffSet(i * 5000);
//				req.setPageSize(5000);
//
//				List<Collection> collectionList = collectionService.queryNotTransferCollection(req);
//
//				if (CollectionUtils.isEmpty(collectionList)) {
//					continue;
//				}
//				for (Collection collection : collectionList) {
//					transCollectSet(collection);
//				}
//
//			}



		}catch (Exception e){

			e.printStackTrace();
			LOGGER.error("同步历史催收数据异常,列表查询异常"+e.getMessage());
			return Result.error("111111","同步历史催收数据异常"+e.getMessage());
		}




		LOGGER.error("开始一次上门催收数据同步（异步执行）");
//		runningFlage = false;
		return Result.success();


	}


	private  void deleteErrorInfo(String businessId,String afterId,Integer transType){

		TransferFailLog transferFailLog = transferFailLogService.selectOne(new EntityWrapper<TransferFailLog>()
				.eq("business_id", businessId).eq("after_id", afterId).eq("trans_type",transType));
		if(transferFailLog !=null){
			transferFailLogService.deleteById(transferFailLog.getId());
		}
	}


	/**
	 * 同步一个电催信息
	 * @param carBusinessAfter
	 * @return
	 */
	@PostMapping("/transferOnePhoneSet")
	@ResponseBody
	public Result transferOnePhoneSet(@RequestBody CarBusinessAfter carBusinessAfter){
		LOGGER.info("同步一个电催信息--开始[{}]" , JSON.toJSONString(carBusinessAfter) );

		boolean bl = transPhoneSet(carBusinessAfter,null);

		if(bl){
			LOGGER.info("同步一个电催信息--成功[{}]" );
			return Result.success();
		}else{
			LOGGER.info("同步一个电催信息--失败[{}]" );
			return Result.error("111111","同步失败");
		}
	}
	/**
	 * 同步一个催收信息
	 * @param collection
	 * @return
	 */
	@PostMapping("/transOneCollectSet")
	@ResponseBody
	public Result transOneCollectSet(@RequestBody Collection collection){
		LOGGER.info("同步一个催收信息-开始[{}]" , JSON.toJSONString(collection) );
		boolean bl =  transCollectSet(collection );
		if(bl){
			LOGGER.info("同步一个催收信息--成功[{}]" );
			return Result.success();
		}else{
			LOGGER.info("同步一个催收信息--失败[{}]" );
			return Result.error("111111","同步失败");
		}
	}



	/**
	 * 同步电催数据
	 * @param carBusinessAfter
	 * @return
	 */
	private boolean transPhoneSet(CarBusinessAfter carBusinessAfter,CollectionSetWayEnum setWayEnum){

		try {
			if(setWayEnum == null){
				setWayEnum =CollectionSetWayEnum.XINDAI_LOG;
			}
			//没有设置电催人员的数据不需要同步
			if(carBusinessAfter.getCollectionUser() == null|| carBusinessAfter.getCollectionUser().equals("")){
				return true;
			}
			Map<String, String> mapInfo = getStatus(carBusinessAfter.getCarBusinessId(),
					carBusinessAfter.getCarBusinessAfterId(),
					"电催", carBusinessAfter.getCollectionUser(),null);
			if (mapInfo == null) {
				return false;
			}

			List<StaffBusinessVo> voList = new LinkedList<>();
			StaffBusinessVo vo = new StaffBusinessVo();
			voList.add(vo);
			vo.setBusinessId(carBusinessAfter.getCarBusinessId());
			vo.setCrpId(mapInfo.get("crpId"));


			collectionStatusService.setBusinessStaff(
					voList, mapInfo.get("userId"),
					"信贷历史数据导入",
					mapInfo.get("staffType"), CollectionSetWayEnum.XINDAI_LOG);

			deleteErrorInfo(carBusinessAfter.getCarBusinessId(),carBusinessAfter.getCarBusinessAfterId(),null);


		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error(e.getMessage());
			recordErrorInfo(carBusinessAfter.getCarBusinessId(), carBusinessAfter.getCarBusinessAfterId(), Exception, "电催", "捕捉到异常信息",null);
			return false;
		}
		return true;
	}


	/**
	 * 同步催收数据
	 * @param collection
	 * @return
	 */
	private boolean transCollectSet(Collection collection ){

		try {
			Map<String, String> mapInfo = getStatus(collection.getBusinessId(),
					collection.getAfterId(),
					collection.getStatus(), collection.getCollectionUser(),1);
			if (mapInfo == null) {
				return false;
			}

			String staffType = mapInfo.get("staffType");

			//移交法务、拖车登记、关闭  需要设置整个业务的催收状态
			if(staffType.equals(CollectionStatusEnum.TRAILER_REG.getPageStr())
					||staffType.equals(CollectionStatusEnum.TO_LAW_WORK.getPageStr())
					||staffType.equals(CollectionStatusEnum.CLOSED.getPageStr())){

				collectionStatusService.setBussinessAfterStatus(
						collection.getBusinessId(),
						mapInfo.get("crpId"),
						"信贷历史数据导入",
						CollectionStatusEnum.getByPageStr(mapInfo.get("staffType")),
						CollectionSetWayEnum.XINDAI_LOG);

			}else{
				//其他状态只设置某一期的催收状态
				List<StaffBusinessVo> voList = new LinkedList<>();
				StaffBusinessVo vo = new StaffBusinessVo();
				vo.setBusinessId(collection.getBusinessId());
				vo.setCrpId(mapInfo.get("crpId"));
				voList.add(vo);
				collectionStatusService.setBusinessStaff(
						voList, mapInfo.get("userId"),
						"信贷历史数据导入",
						staffType, CollectionSetWayEnum.XINDAI_LOG);
			}

			deleteErrorInfo(collection.getBusinessId(),collection.getAfterId(),1);


		}catch (RuntimeException re){
			re.printStackTrace();
			recordErrorInfo(collection.getBusinessId(), collection.getAfterId(), RUNException, collection.getStatus(), "历史催收数据同步  报异常",1);

		}catch (Exception e) {
			e.printStackTrace();

			recordErrorInfo(collection.getBusinessId(), collection.getAfterId(), Exception, collection.getStatus(), "历史催收数据同步  报异常",1);
			return false;
		}
		return true;
	}



//collectionLogXd.getBusinessId()     collectionLogXd.getAfterId()
//	private void transferAlmsStatus(Map<String, Object> map,String businessId,String afterId){
//		CollectionStatus collectionStatus = (CollectionStatus) map.get("status");
//		CollectionLog collectionLog = (CollectionLog) map.get("log");
//		CollectionStatus status = collectionStatusService.selectOne(new EntityWrapper<CollectionStatus>().eq("business_id",collectionStatus.getBusinessId()).eq("crp_id",collectionStatus.getCrpId()));
//		if(status == null){
//			collectionStatusService.insertOrUpdate(collectionStatus);
//			transferFailLogService.delete(new EntityWrapper<TransferFailLog>().eq("business_id",businessId).eq("after_id",afterId));
//
//		}
//		CollectionLog log = collectionLogService.selectOne(new EntityWrapper<CollectionLog>().eq("business_id",collectionLog.getBusinessId()).eq("crp_id",collectionLog.getCrpId()));
//		if(log == null){
//			collectionLogService.insertOrUpdate(collectionLog);
//			transferFailLogService.delete(new EntityWrapper<TransferFailLog>().eq("business_id",businessId).eq("after_id",afterId));
//		}
//	}

	@GetMapping("/setCollectionStatus")
	@ResponseBody
	public Result setCollectionStatus(String businessId,String afterId){

		CarBusinessAfter businessAfter = carBusinessAfterService.selectOne(new EntityWrapper<CarBusinessAfter>().eq("car_business_id",businessId).eq("car_business_after_id",afterId));
		Collection collection = null;
		Boolean phoneRet;
		if(businessAfter!=null){
			phoneRet = transPhoneSet(businessAfter,CollectionSetWayEnum.XINDAI_LOG_ONE);
//            if(phoneRet){
//                return Result.success();
//            }else {
//                return Result.error("111","存储电催失败");
//            }
		}
// else{
		collection = collectionService.selectOne(new EntityWrapper<Collection>().eq("business_id",businessId).eq("after_id",afterId));
		Boolean colRet;
		if(collection!=null){
			colRet = transCollectSet(collection);
//                if(ret){
//                    return Result.success();
//                }else {
//                    return Result.error("112","存储催收失败");
//                }
		}
//        }


		if(businessAfter==null && collection == null){
			return Result.error("113","找不到信贷的历史电催或催收数据");
		}




//
//
//
//
//		if(collection!=null){
//            Boolean ret = transCollectSet(collection);
//            if(!ret){
//                return Result.error("111","设置催收失败");
//            }
//			try{
//				Map<String, Object> map = getStatus(collection);
//				if (map == null) {
//					return Result.error("eeeee","collection  getStatus   查询不出map ");
//				}else{
//					try{
//						transferAlmsStatus(map,collection.getBusinessId() ,collection.getAfterId());
//					}catch (Exception e){
//						e.printStackTrace();
//						LOGGER.error("导入数据异常：collection   ， businessID:"+collection.getBusinessId()+"     afterId:"+collection.getAfterId()+  e.getMessage());
//					}
//				}
//
//			}catch (Exception e){
//				LOGGER.error("查询collection状态异常：collection   ， businessID:"+collection.getBusinessId()+"     afterId:"+collection.getAfterId()+  e.getMessage());
//			}
//
//		}
//		LOGGER.error("完成一次数据同步");
//		CollectionLogXd collectionLogXd  = collectionLogXdService.selectOne(new EntityWrapper<CollectionLogXd>().eq("business_id",businessId).eq("after_id",afterId));
//		if(collectionLogXd!=null){
//			try{
//				Map<String, Object> map = getStatus(collectionLogXd);
//				if (map == null) {
//					return Result.error("eeeee11","collectionLogXd  getStatus   查询不出map ");
//				}else{
//					try{
//						transferAlmsStatus(map,businessId ,afterId);
//					}catch (Exception e){
//
//						e.printStackTrace();
//						LOGGER.error("导入数据异常：collectionLogXd   ， businessID:"+businessId+"     afterId:"+afterId+  e.getMessage());
//					}
//				}
//
//			}catch (Exception e){
//				LOGGER.error("查询collection状态异常：collection   ， businessID:"+businessId+"     afterId:"+businessId+  e.getMessage());
//
//			}
//
//		}
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
	private  static  final  Integer RUNException = 6; //catch到运行时异常
	private void recordErrorInfo(String businessId ,String afterId,Integer reson,String status,String failReson ,Integer transType){
		TransferFailLog failLog = new TransferFailLog();
		failLog.setBusinessId(businessId);
		failLog.setAfterId(afterId);
		failLog.setFailReason(reson);
		failLog.setTransType(transType);
		List<TransferFailLog> transferFailLogs = transferFailLogService.selectList(new EntityWrapper<TransferFailLog>()
				.eq("business_id", businessId).eq("after_id", afterId).eq("trans_type",transType));
		LOGGER.error("信贷历史催收数据导入错误，"+failReson +"  businessID:"+businessId+"     afterId:"+afterId+"   status:"+status);
		TransferFailLog transferFailLog = null;
		if(transferFailLogs!=null&&transferFailLogs.size()>0){
			for(TransferFailLog log :transferFailLogs ){
				if(log.getTransType()==null || log.getTransType()==1){
					transferFailLog = log;
				}
			}
		}
		if (transferFailLog == null) {
			transferFailLogService.insert(failLog);
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
	private Map<String, String>  getStatus(String businessId,String afterId,String status,String bmUserId,Integer transType) {

		Map<String, String> map = new HashMap<>();

		List<RepaymentBizPlanList>  l  = repaymentBizPlanListService
				.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("orig_business_id", businessId)
						.eq("after_id", afterId));
		RepaymentBizPlanList repaymentBizPlanList = l.size()>0?l.get(0):null;
		if(l.size()>1){
			recordErrorInfo(businessId ,afterId, DoubleList,status,"查出两条以上还款计划数据",transType);
			return null;
		}
		if (repaymentBizPlanList == null) {
			recordErrorInfo(businessId ,afterId, NoList,status,"无还款计划数据",transType);
			return null;
		}
		map.put("crpId",repaymentBizPlanList.getPlanListId());

		// 根据信贷userId 获取贷后userId
		LoginInfoDto dto = new LoginInfoDto();
		if(bmUserId!=null&&!bmUserId.equals("")){
			dto = loginUserInfoHelper.getUserInfoByUserId("", bmUserId);
		}
		if(dto ==null){
			recordErrorInfo(businessId ,afterId, NoUser,status,"没有用户信息",transType);
			return null;
		}

		// 判断状态
		String staffType ;
		//催收中，电催需要 判断用户信息
		if("电催".equals(status) ||"催收中".equals(status)||"催款中".equals(status)){
			if (StringUtil.isEmpty(dto.getUserId())) {
				if("电催".equals(status)){
					recordErrorInfo(businessId ,afterId, NoUser,status,"没有用户信息",transType);
					return null;
				}else{
					map.put("userId","");
				}
			}else{
				map.put("userId",dto.getUserId());
			}
		}

		if("电催".equals(status)){
			staffType = CollectionStatusEnum.PHONE_STAFF.getPageStr();
		}
		else if ("催收中".equals(status)||"催款中".equals(status)) {
			staffType = CollectionStatusEnum.COLLECTING.getPageStr();

		}else if ("已拖车登记".equals(status)) {
			staffType = CollectionStatusEnum.TRAILER_REG.getPageStr();

		} else if ("已移交法务".equals(status)) {
			staffType = CollectionStatusEnum.TO_LAW_WORK.getPageStr();

		} else if ("已完成".equals(status)) {
			staffType = CollectionStatusEnum.CLOSED.getPageStr();
//			collectionStatus = 200;
//			log.setCollectionUser("admin");
		} else if ("二押已赎回".equals(status)) {
			staffType = CollectionStatusEnum.REDEMPTION_REDEEMED.getPageStr();
//			log.setCollectionUser("admin");
//			collectionStatus = 250;
		} else if ("已委外催收".equals(status)) {
			staffType = CollectionStatusEnum.OUTSIDE_COLLECT.getPageStr();
//			log.setCollectionUser("admin");
//			collectionStatus = 300;
		} else if ("待分配".equals(status)) {
			staffType = CollectionStatusEnum.WAIT_TO_SET.getPageStr();
//			log.setCollectionUser("admin");
//			collectionStatus = 350;
		} else if ("推迟移交法务".equals(status)) {
			staffType = CollectionStatusEnum.DELAY_TO_LAW.getPageStr();
//			log.setCollectionUser("admin");
//			collectionStatus = 400;
		} else {
			recordErrorInfo(businessId ,afterId, NoStatusEnum,status,"状态信息不匹配",transType);
			return null;

		}
		map.put("staffType",staffType);

		return map;
	}



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
