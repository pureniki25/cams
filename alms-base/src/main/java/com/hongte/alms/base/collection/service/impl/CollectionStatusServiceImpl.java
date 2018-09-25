package com.hongte.alms.base.collection.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.collection.dto.CollectionStatusCountDto;
import com.hongte.alms.base.collection.entity.Collection;
import com.hongte.alms.base.collection.entity.CollectionLog;
import com.hongte.alms.base.collection.entity.CollectionPersonSet;
import com.hongte.alms.base.collection.entity.CollectionPersonSetDetail;
import com.hongte.alms.base.collection.entity.CollectionStatus;
import com.hongte.alms.base.collection.entity.CollectionTimeSet;
import com.hongte.alms.base.collection.enums.CollectionCrpTypeEnum;
import com.hongte.alms.base.collection.enums.CollectionSetWayEnum;
import com.hongte.alms.base.collection.enums.CollectionStatusEnum;
import com.hongte.alms.base.collection.enums.StaffPersonType;
import com.hongte.alms.base.collection.mapper.CollectionStatusMapper;
import com.hongte.alms.base.collection.service.CollectionLogService;
import com.hongte.alms.base.collection.service.CollectionPersonSetDetailService;
import com.hongte.alms.base.collection.service.CollectionPersonSetService;
import com.hongte.alms.base.collection.service.CollectionStatusService;
import com.hongte.alms.base.collection.service.CollectionTimeSetService;
import com.hongte.alms.base.collection.vo.StaffBusinessVo;
import com.hongte.alms.base.entity.CarBusinessAfter;
import com.hongte.alms.base.entity.RepaymentBizPlan;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.SysUser;
import com.hongte.alms.base.feignClient.AlmsCoreServiceFeignClient;
import com.hongte.alms.base.feignClient.CollectionSynceToXindaiRemoteApi;
import com.hongte.alms.base.feignClient.LitigationFeignClient;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.base.service.RepaymentBizPlanService;
import com.hongte.alms.base.service.SysUserPermissionService;
import com.hongte.alms.base.service.SysUserRoleService;
import com.hongte.alms.base.service.SysUserService;
import com.hongte.alms.base.service.TransferOfLitigationService;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.Constant;
import com.ht.ussp.bean.LoginUserInfoHelper;
import com.ht.ussp.client.dto.LoginInfoDto;

/**
 * <p>
 * 贷后催收状态表 服务实现类
 * </p>
 *
 * @author 曾坤
 * @since 2018-01-25
 */

@RefreshScope
@Service("CollectionStatusService")
public class CollectionStatusServiceImpl extends BaseServiceImpl<CollectionStatusMapper, CollectionStatus> implements CollectionStatusService {

    private static Logger logger = LoggerFactory.getLogger(CollectionStatusServiceImpl.class);

    @Autowired
    CollectionStatusMapper collectionStatusMapper;

    @Autowired
    @Qualifier("CollectionLogService")
    CollectionLogService collectionLogService;

    @Autowired
    LoginUserInfoHelper loginUserInfoHelper;
    
  

    @Autowired
    @Qualifier("CollectionPersonSetService")
    CollectionPersonSetService collectionPersonSetService;

    @Autowired
    @Qualifier("CollectionPersonSetDetailService")
    CollectionPersonSetDetailService collectionPersonSetDetailService;


    @Autowired
    @Qualifier("CollectionTimeSetService")
    CollectionTimeSetService collectionTimeSetService;

    @Autowired
    @Qualifier("RepaymentBizPlanListService")
    RepaymentBizPlanListService repaymentBizPlanListService;

    @Autowired
    @Qualifier("RepaymentBizPlanService")
    RepaymentBizPlanService repaymentBizPlanService;


    @Autowired
    @Qualifier("TransferOfLitigationService")
    TransferOfLitigationService transferLitigationService;

    @Autowired
    CollectionSynceToXindaiRemoteApi collectionSynceToXindaiRemoteApi;

    @Autowired
    @Qualifier("SysUserPermissionService")
    SysUserPermissionService sysUserPermissionService;
    
    @Autowired
	private LitigationFeignClient litigationFeignClient;

    @Autowired
    @Qualifier("SysUserRoleService")
    SysUserRoleService sysUserRoleService;
    
    @Autowired
    private Executor cunshouThreadAsync;
    
    @Autowired
    private AlmsCoreServiceFeignClient almsCoreServiceFeignClient;
    
    @Autowired
    @Qualifier("SysUserService")
    SysUserService sysUserService;

    /**
     * 设置电催/人员(界面手动设置)
     * @param voList 业务信息列表
     * @param staffUserId 催收人ID
     * @param describe 描述
     * @param staffType 催收类型
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
	public boolean setBusinessStaff(List<StaffBusinessVo> voList, String staffUserId, String describe, String staffType,
			CollectionSetWayEnum setWayEnum) {

		String userId = loginUserInfoHelper.getUserId();
		if (userId == null) {
			userId = Constant.ADMIN_ID;
		}

		// 需要设置的其他期数
		List<StaffBusinessVo> needSetOtherPlanLists = new LinkedList<>();

		// 如果是移交法务、关闭、拖车登记
		// 需要更新此业务所有的历史记录为移交法务/关闭 /拖车登记
		if (staffType.equals(CollectionStatusEnum.TO_LAW_WORK.getPageStr())
				|| staffType.equals(CollectionStatusEnum.CLOSED.getPageStr())
				|| staffType.equals(CollectionStatusEnum.TRAILER_REG.getPageStr())) {

			// 1、更新历史记录为移交法务 /关闭 /拖车登记
			for (StaffBusinessVo vo : voList) {

				/*
				 * Modifier：huweiqian Date: 2018-09-21 Desc: 如果催收状态将更新为 移交法务 /关闭
				 * /拖车登记，则需要根据原业务ID来更新所有对应的催收记录
				 */

				// 1、根据businessId找到
				List<RepaymentBizPlanList> bizPlanLists = repaymentBizPlanListService
						.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("business_id", vo.getBusinessId()));

				if (CollectionUtils.isEmpty(bizPlanLists)) {
					logger.info("根据业务ID：{}，找不到对应的还款计划！", vo.getBusinessId());
					continue;
				}

				// 2、拿到对应的 原业务ID origBusinessId
				String origBusinessId = bizPlanLists.get(0).getOrigBusinessId();

				// 3、根据 origBusinessId 取出所有对应的催收记录
				List<CollectionStatus> list = selectList(
						new EntityWrapper<CollectionStatus>().eq("original_business_id", origBusinessId));

				// 4、根据 PlanListId -- RepaymentBizPlanList 的关系存入 bizPlanListMap 中
				Map<String, RepaymentBizPlanList> bizPlanListMap = new HashMap<>();
				for (RepaymentBizPlanList repaymentBizPlanList : bizPlanLists) {
					bizPlanListMap.put(repaymentBizPlanList.getPlanListId(), repaymentBizPlanList);
				}

				// 去除这次需要设置催收信息的还款计划
				bizPlanListMap.remove(vo.getCrpId());

				List<CollectionLog> logs = new ArrayList<>();
				for (CollectionStatus status : list) {

					// 跳过crpId一致的记录
					if (status.getCrpId().equals(vo.getCrpId())) {
						continue;
					}
					// 去除已存在催收记录的还款计划
					bizPlanListMap.remove(status.getCrpId());

					status.setSetWay(setWayEnum.getKey());
					status.setUpdateTime(new Date());

					switch (setWayEnum) {
					
					case XINDAI_CALL:
					case AUTO_SET:
						status.setUpdateUser(Constant.ADMIN_ID);
						break;
						
					case MANUAL_SET:
						status.setUpdateUser(loginUserInfoHelper.getUserId() == null ? Constant.ADMIN_ID
								: loginUserInfoHelper.getUserId());
						break;
						
					default:
						break;
					}

					CollectionLog log = new CollectionLog();
					log.setBusinessId(status.getBusinessId());
					log.setCrpId(status.getCrpId());
					log.setAfterStatus(CollectionStatusEnum.getByPageStr(staffType).getKey());
					log.setCollectionUser("".equals(staffUserId) ? Constant.ADMIN_ID : staffUserId);
					log.setCreateTime(new Date());
					log.setCreateUser(status.getUpdateUser());
					log.setUpdateTime(new Date());
					log.setUpdateUser(status.getUpdateUser());
					log.setDescribe(setWayEnum.getName());
					log.setSetWay(setWayEnum.getKey());
					log.setBeforeStatus(status.getCollectionStatus());

					Integer afterStatus = getCurrentColStatu(status,
							CollectionStatusEnum.getByPageStr(staffType).getKey());

					status.setCollectionStatus(afterStatus);

					String crpId = status.getCrpId();

					status.setCrpId(null);

					update(status, new EntityWrapper<CollectionStatus>().eq("crp_id", crpId));

					logs.add(log);
				}

				// 如果有未设置催收状态的期数，则新建这一期的催收状态
				if (bizPlanListMap.size() > 0) {
					
					for (String planListId : bizPlanListMap.keySet()) {
						
						RepaymentBizPlanList rbPlanList = bizPlanListMap.get(planListId);
						
						StaffBusinessVo staffBizVO = new StaffBusinessVo();
						
						staffBizVO.setCrpId(rbPlanList.getPlanListId());
						staffBizVO.setBusinessId(rbPlanList.getBusinessId());
						staffBizVO.setOrigBusinessId(rbPlanList.getOrigBusinessId());
						
						needSetOtherPlanLists.add(staffBizVO);
					}
				}

				if (!logs.isEmpty()) {
					collectionLogService.insertBatch(logs);
				}
			}
		}

		// 将其他需要设置状态的期数加到voList中
		voList.addAll(needSetOtherPlanLists);
		
		Integer setTypeStatus = CollectionStatusEnum.getByPageStr(staffType).getKey();
		
		for (StaffBusinessVo vo : voList) {
			
			String oldStaffUserId = "";
			
			CollectionStatus status = new CollectionStatus();
			
			// 1、插入或更新催收状态表
			List<CollectionStatus> list = selectList(new EntityWrapper<CollectionStatus>().eq("crp_id", vo.getCrpId()));
			
			if (CollectionUtils.isNotEmpty(list)) {
				status = list.get(0);
				if (staffType.equals(CollectionStatusEnum.PHONE_STAFF.getPageStr())) {
					oldStaffUserId = status.getPhoneStaff();
				} else if (staffType.equals(CollectionStatusEnum.COLLECTING.getPageStr())) {
					oldStaffUserId = status.getVisitStaff();
				}
			} else {
				// //如果是设置关闭状态，又没有写过催收的状态记录则跳过，不增加催收状态记录
				// if(CollectionStatusEnum.getByPageStr(staffType).equals(CollectionStatusEnum.CLOSED)){
				// continue;
				// }
				status.setCreateTime(new Date());
				status.setCreateUser(userId);
			}
			Integer beforeStatus = status.getCollectionStatus();
			Integer afterStatus = getCurrentColStatu(status, setTypeStatus);
			status.setBusinessId(vo.getBusinessId());
			status.setCollectionStatus(afterStatus);
			status.setCrpId(vo.getCrpId());
			if (staffType.equals(CollectionStatusEnum.PHONE_STAFF.getPageStr())) {
				status.setPhoneStaff(staffUserId);
			} else if (staffType.equals(CollectionStatusEnum.COLLECTING.getPageStr())) {
				status.setVisitStaff(staffUserId);
			}
			status.setUpdateUser(userId);
			status.setDescribe(describe);
			status.setUpdateTime(new Date());
			status.setSetWay(setWayEnum.getKey());
			RepaymentBizPlanList planList = repaymentBizPlanListService.selectById(vo.getCrpId());
			status.setOriginalBusinessId(planList.getOrigBusinessId());
			if (ifPlanListIsLast(planList)) {
				status.setCrpType(CollectionCrpTypeEnum.LAST.getKey());
			} else {
				status.setCrpType(CollectionCrpTypeEnum.NORMAL.getKey());
			}

			if (CollectionUtils.isNotEmpty(list)) {
				status.setCrpId(null);
				update(status, new EntityWrapper<CollectionStatus>().eq("crp_id", vo.getCrpId()));
			} else {
				insert(status);
			}

			// 2、记录移交记录表
			CollectionLog log = new CollectionLog();
			log.setAfterStatus(afterStatus);
			log.setBusinessId(vo.getBusinessId());
			log.setCollectionUser("".equals(staffUserId) ? Constant.ADMIN_ID : staffUserId);
			log.setCrpId(vo.getCrpId());
			log.setUpdateUser(userId);
			log.setCreateUser(userId);
			log.setDescribe(describe);
			log.setCreateTime(new Date());
			log.setUpdateTime(new Date());
			log.setSetWay(setWayEnum.getKey());
			log.setBeforeStatus(beforeStatus);
			log.setSetTypeStatus(setTypeStatus);
			collectionLogService.insert(log);

			// 信贷历史数据导入则不刷新跟单人的权限信息
			if (setWayEnum.getKey() != CollectionSetWayEnum.XINDAI_LOG.getKey()) {
				// 刷新相关跟单人员的用户设置
				// 1.旧的那个跟单人的permission刷新
				if (oldStaffUserId != null) {
					if (setWayEnum.getKey() != CollectionSetWayEnum.AUTO_SET.getKey()
							&& !StringUtils.isBlank(oldStaffUserId)) {
						String oldStaffUserIds = oldStaffUserId;
						List<StaffBusinessVo> voTempList = new ArrayList<>();
						voTempList.add(vo);
						sysUserPermissionService.setUserPermissonsInBusinessList(oldStaffUserIds, voTempList);

					} else {
						SysUser sysUser = sysUserService.selectById(oldStaffUserId);
						if (null != sysUser) {
							sysUser.setLastPermissionStatus(1);
							sysUserService.updateById(sysUser);
						}
					}
				}

				// 2.新的那个跟单人的permission刷新
				if (staffType.equals(CollectionStatusEnum.PHONE_STAFF.getPageStr())
						|| staffType.equals(CollectionStatusEnum.COLLECTING.getPageStr())) {
					if (setWayEnum.getKey() != CollectionSetWayEnum.AUTO_SET.getKey()
							&& !StringUtils.isBlank(staffUserId)) {
						List<StaffBusinessVo> voTempList = new ArrayList<>();
						voTempList.add(vo);
						sysUserPermissionService.setUserPermissonsInBusinessList(staffUserId, voTempList);
					} else {
						SysUser sysUser = sysUserService.selectById(staffUserId);
						if (null != sysUser) {
							sysUser.setLastPermissionStatus(1);
							sysUserService.updateById(sysUser);
						}
					}
				}
			}

		}
		return true;
	}

    /**
     * 同步业务的贷后状态到信贷
     * @param voList
     * @param staffUserId
     * @param describe
     * @param staffType
     * @return
     */
    public  boolean SyncBusinessColStatusToXindai(
            List<StaffBusinessVo> voList,
            String staffUserId,
            String describe ,
            String staffType
    ){
        LoginInfoDto dto =loginUserInfoHelper.getUserInfoByUserId(staffUserId,null);
        for(StaffBusinessVo businessVo:voList){

            if(businessVo.getCrpId()==null){
                List<RepaymentBizPlan> plans = repaymentBizPlanService.selectList(new EntityWrapper<RepaymentBizPlan>().eq("original_business_id",businessVo.getBusinessId()).orderBy("create_time desc"));
                String crpId = "";
                if(plans.size()>0){
                    RepaymentBizPlan plan = plans.get(0);
                    List<RepaymentBizPlanList> planLists = repaymentBizPlanListService.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("plan_id",plan.getPlanId()).orderBy("create_time desc"));
                    if(planLists.size()>0){
                        crpId = planLists.get(0).getPlanListId();
                    }
                }
                businessVo.setCrpId(crpId);
            }

            RepaymentBizPlanList list =  repaymentBizPlanListService.selectById(businessVo.getCrpId());
            if(staffType.equals(CollectionStatusEnum.PHONE_STAFF.getPageStr())){
                //电催
                CarBusinessAfter carBusinessAfter  = new CarBusinessAfter();
                carBusinessAfter.setCarBusinessId(businessVo.getBusinessId());


                carBusinessAfter.setCarBusinessAfterId(list.getAfterId());


                if(dto!=null&&dto.getBmUserId()!=null){
                    carBusinessAfter.setCollectionUser(dto.getBmUserId());
                }else{
                    logger.error("设置电催人员,同步电催设置到信贷,找不到信贷用户信息，信息："+ JSON.toJSONString(businessVo)+"  staffUserId:"+staffUserId);
                    continue;
                }


                carBusinessAfter.setCollectionRemark(describe);
                Result result = null;
                try{
                    result = collectionSynceToXindaiRemoteApi.transferOnePhoneSetToXd(carBusinessAfter);
                }catch(Exception e){
                    logger.error("设置电催人员,同步电催设置到信贷失败， 异常，信息："+ JSON.toJSONString(businessVo)+"  staffUserId:"+staffUserId+"  异常："+e.getMessage());
                }

                if(result ==null || !result.getCode().equals("1")){
                    logger.error("设置电催人员,同步电催设置到信贷失败，信息："+ JSON.toJSONString(businessVo)+"  staffUserId:"+staffUserId);
                }

            }else if(staffType.equals(CollectionStatusEnum.COLLECTING.getPageStr())){
                //催收
                Collection collection = new Collection();
                collection.setBusinessId(businessVo.getBusinessId());
                collection.setAfterId(list.getAfterId());
                collection.setStatus("催款中");
                if(dto!=null&&dto.getBmUserId()!=null){
                    collection.setCollectionUser(dto.getBmUserId());
                }else{
                    logger.error("设置催收人员,同步催收设置到信贷,找不到信贷用户信息，信息："+ JSON.toJSONString(businessVo)+"  staffUserId:"+staffUserId);
                    continue;
                }
                collection.setAssignRemark(describe);
                LoginInfoDto userDro = loginUserInfoHelper.getLoginInfo();
                collection.setCreateUser(userDro.getBmUserId());
                collection.setCreateTime(new Date());
                Result result = null;
                try{
                    result = collectionSynceToXindaiRemoteApi.transferOneVisitSetToXd(collection);
                }catch(Exception e){
                    logger.error("设置电催人员,同步催收设置到信贷失败， 异常，信息："+ JSON.toJSONString(businessVo)+"  staffUserId:"+staffUserId+"  异常："+e.getMessage());
                }

                if(result== null ||!result.getCode().equals("1")){
                    logger.error("设置催收人员,同步催收设置到信贷失败，信息："+ JSON.toJSONString(businessVo)+"  staffUserId:"+staffUserId);
                }
            }
            else {
                Collection collection = new Collection();
                collection.setBusinessId(businessVo.getBusinessId());
                collection.setAfterId(list.getAfterId());
                collection.setStatus(CollectionStatusEnum.getByPageStr(staffType).getName());
                collection.setCreateUser("admin");
                collection.setCreateTime(new Date());
                Result result = null;
                try{
                    result = collectionSynceToXindaiRemoteApi.transferOneVisitSetToXd(collection);
                }catch(Exception e){
                    logger.error("设置电催人员,同步催收设置到信贷失败， 异常，信息："+ JSON.toJSONString(businessVo)+"  staffUserId:"+staffUserId+"  异常："+e.getMessage());
                }

                if(result == null || !result.getCode().equals(1)){
                    logger.error("设置催收人员,同步催收设置到信贷失败，信息："+ JSON.toJSONString(businessVo)+"  staffUserId:"+staffUserId);
                }
            }
        }

                return true;

    }








    /**
     * 根据业务逻辑取得当前应该设置的催收状态
     * @param status
     * @param setTypeStatus
     * @return
     */
    public Integer getCurrentColStatu(CollectionStatus status,Integer setTypeStatus){
//        Integer collectionStatus = CollectionStatusEnum.getByPageStr(staffType).getKey();
        List<CollectionStatus> list = selectList(new EntityWrapper<CollectionStatus>().eq("business_id",status.getBusinessId()));
        Integer bizStauts = null;
        for(CollectionStatus bizColStatus:list) {
            if(bizStauts==null){
                bizStauts = bizColStatus.getCollectionStatus();
            }else {
                //历史的催收状态为已关闭，则直接置位为已关闭
                if(bizColStatus.getCollectionStatus().equals(CollectionStatusEnum.CLOSED.getKey())){
                    bizStauts = bizColStatus.getCollectionStatus();
                }else if(bizColStatus.getCollectionStatus().equals(CollectionStatusEnum.TO_LAW_WORK.getKey())){
                    //历史的催收状态为“已移交诉讼”，则非“已关闭”的状态置位为“已移交法务”
                    if(!bizStauts.equals(CollectionStatusEnum.CLOSED.getKey())){
                        bizStauts = bizColStatus.getCollectionStatus();
                    }
                }else if(bizColStatus.getCollectionStatus().equals(CollectionStatusEnum.TRAILER_REG.getKey())){
                    //历史的催收状态为“已拖车登记”，则非“已关闭”，非“已移交法务”的状态置位为“已拖车登记”
                    if(!bizStauts.equals(CollectionStatusEnum.CLOSED.getKey())
                            &&!bizStauts.equals(CollectionStatusEnum.TO_LAW_WORK.getKey())){
                        bizStauts = bizColStatus.getCollectionStatus();
                    }
                }else if(bizColStatus.getCollectionStatus().equals(CollectionStatusEnum.COLLECTING.getKey())){
                    //历史的催收状态为“催收中”，则非“已关闭”，非“已移交法务”,非“已拖车登记”的状态置位为“催收中”
                    if(!bizStauts.equals(CollectionStatusEnum.CLOSED.getKey())
                            &&!bizStauts.equals(CollectionStatusEnum.TO_LAW_WORK.getKey())
                            &&!bizStauts.equals(CollectionStatusEnum.TRAILER_REG.getKey())){
                        bizStauts = bizColStatus.getCollectionStatus();
                    }
                }else{
                    //历史的催收状态为“电催”，则非“已关闭”，非“已移交法务”,非“已拖车登记”，非“催收中”的状态置位为“电催”
                    if(!bizStauts.equals(CollectionStatusEnum.CLOSED.getKey())
                            &&!bizStauts.equals(CollectionStatusEnum.TO_LAW_WORK.getKey())
                            &&!bizStauts.equals(CollectionStatusEnum.TRAILER_REG.getKey())
                            &&!bizStauts.equals(CollectionStatusEnum.COLLECTING.getKey())){
                        bizStauts = bizColStatus.getCollectionStatus();
                    }
                }

            }
//            if (status.getCollectionStatus().equals(CollectionStatusEnum.TO_LAW_WORK.getKey())
//                    ||status.getCollectionStatus().equals(CollectionStatusEnum.CLOSED.getKey())
//                    ||status.getCollectionStatus().equals(CollectionStatusEnum.TRAILER_REG.getKey())){
//                String statStr = CollectionStatusEnum.getByKey(status.getCollectionStatus()).getName();
//                logger.error("此业务已"+statStr+"，不能再设置催收或者电催！  businessId:"+ status.getBusinessId()+"     crpId:"+status.getCrpId());
//                throw  new  RuntimeException("此业务已"+statStr+"，不能再设置催收或者电催！");
//            }
        }


        if(bizStauts==null){
            return setTypeStatus;
        }else if(bizStauts.equals(CollectionStatusEnum.CLOSED.getKey())){
            //当前状态已经是已关闭的，则不刷新状态
            return  bizStauts;//status.getCollectionStatus();
        }else if(bizStauts.equals(CollectionStatusEnum.TO_LAW_WORK.getKey())){
            //当前状态是已移交诉讼
            if(setTypeStatus.equals(CollectionStatusEnum.CLOSED.getKey())){
                //设置的状态为 关闭 才刷新状态
                return setTypeStatus;
            }else{
                return bizStauts;//status.getCollectionStatus();
            }
        }else if (bizStauts.equals(CollectionStatusEnum.TRAILER_REG.getKey())){
            //当前状态为已拖车登记
            if(setTypeStatus.equals(CollectionStatusEnum.CLOSED.getKey())
                    || setTypeStatus.equals(CollectionStatusEnum.TO_LAW_WORK.getKey())){
                //设置的状态为 关闭  移交法务 才刷新状态
                return setTypeStatus;
            }else{
                return bizStauts;//status.getCollectionStatus();
            }
        }else if (bizStauts.equals(CollectionStatusEnum.COLLECTING.getKey())){
            //当前状态为催收中
            if(setTypeStatus.equals(CollectionStatusEnum.CLOSED.getKey())
                    || setTypeStatus.equals(CollectionStatusEnum.TO_LAW_WORK.getKey())
                    ||setTypeStatus.equals(CollectionStatusEnum.TRAILER_REG.getKey()) ){
                //设置的状态为 关闭  移交法务  拖车登记 才刷新状态
                return setTypeStatus;
            }else{
                return status.getCollectionStatus();
            }
        }else{
            //当前状态为电催 则随便设置
            return setTypeStatus;
        }
    }


    /**
     * 设置业务的贷后状态（移交法务、拖车登记、关闭 调用此方法）
     * @param businessId
     * @param crpId
     * @param describe
     * @param setWayEnum
     * @return
     */
    public boolean setBussinessAfterStatus(
            String businessId,
            String crpId,
            String describe,
            CollectionStatusEnum  satusEnum,
            CollectionSetWayEnum setWayEnum){

        if(crpId==null){
            List<RepaymentBizPlan> plans = repaymentBizPlanService.selectList(new EntityWrapper<RepaymentBizPlan>().eq("original_business_id",businessId).orderBy("create_time desc"));
            if(plans.size()>0){
                RepaymentBizPlan plan = plans.get(0);
                List<RepaymentBizPlanList> planLists = repaymentBizPlanListService.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("plan_id",plan.getPlanId()).orderBy("create_time desc"));
                if(planLists.size()>0){
                    crpId = planLists.get(0).getPlanListId();
                }
            }
        }

        List<StaffBusinessVo> voList = new LinkedList<>();
        StaffBusinessVo vo  = new StaffBusinessVo();
        vo.setCrpId(crpId);
        vo.setBusinessId(businessId);
        voList.add(vo);

        if(satusEnum!=CollectionStatusEnum.TO_LAW_WORK
                &&satusEnum!=CollectionStatusEnum.TRAILER_REG
                &&satusEnum!=CollectionStatusEnum.CLOSED){
            return false;
        }

        return  setBusinessStaff(voList,"",describe,satusEnum.getPageStr(),setWayEnum);

    }


/*    public boolean setClosed(String businessId,String crpId,CollectionSetWayEnum setWayEnum){
        List<StaffBusinessVo> voList = new LinkedList<>();
        StaffBusinessVo vo  = new StaffBusinessVo();
        vo.setCrpId(crpId);
        vo.setBusinessId(businessId);
        voList.add(vo);


        return  setBusinessStaff(voList,"","",CollectionStatusEnum.CLOSED.getPageStr(),setWayEnum);

    }*/


/*
    private CollectionStatus creatStatus(
            String businessId,
            String crpId,
            String setWay,
            String staffType,
            CollectionSetWayEnum setWayEnum){
        CollectionStatus status  = new CollectionStatus();

        status.setCollectionStatus();

        return status;
    }*/

    public  boolean setAutoBusinessStaff(String busnessId,
                                         String planListId,
                                         String staffUserId,
                                         String staffType){
        List<StaffBusinessVo> voList = new LinkedList<StaffBusinessVo>();
        StaffBusinessVo vo = new StaffBusinessVo();
        vo.setBusinessId(busnessId);
        vo.setCrpId(planListId);
        if(planListId.equals("0d9c3396-0131-4271-af05-317b623e6f41")) {
        	System.out.println("stop");
        }
        // 将StaffBusinessVo 放入list
        voList.add(vo);
        boolean bl= setBusinessStaff(
                voList,
                staffUserId,
                "定时任务自动分配",
                staffType,
                CollectionSetWayEnum.AUTO_SET);

        SyncBusinessColStatusToXindai(voList,staffUserId,"定时任务自动分配",staffType);
        return bl;
    };

    /**
     * 自动移交：
     */
    @Override
    public void autoSetBusinessStaff(){
        //查找分配了电催人员的分公司列表
        List<CollectionPersonSet> list = collectionPersonSetService.selectList(new EntityWrapper<CollectionPersonSet>());
        CollectionTimeSet phoneTimeSet = collectionTimeSetService.selectOne(new EntityWrapper<CollectionTimeSet>().eq("col_type",CollectionStatusEnum.PHONE_STAFF.getKey()).and("start_time<=NOW()"));
        CollectionTimeSet visitTimeSet = collectionTimeSetService.selectOne(new EntityWrapper<CollectionTimeSet>().eq("col_type",CollectionStatusEnum.COLLECTING.getKey()).and("start_time<=NOW()"));
         Integer daysBeforeOverDue = phoneTimeSet!=null?phoneTimeSet.getOverDueDays():0;
        Integer visitDaysAfterOverDue = visitTimeSet!=null?visitTimeSet.getOverDueDays():31;

        for(CollectionPersonSet set:list){
            String companyId = set.getCompanyCode();
            Integer businessType=set.getBusinessType();

            List<String>  phonePersons = getStaffPersons(set.getColPersonId(),StaffPersonType.PHONE_STAFF);
            List<String>  visitPersons = getStaffPersons(set.getColPersonId(),StaffPersonType.VISIT_STAFF);

//            List<CollectionPersonSetDetailService>
//            String[] phonePersons = set.getCollect1Person().split(",");
//            String[] visitPersons = set.getCollect2Person().split(",");

            if(phonePersons.size()>0){
            	//自动移交电催
                setBusinessPhoneStaff(companyId, phonePersons, daysBeforeOverDue,businessType);


            }
            //自动移交催收
            if(visitPersons.size()>0){
                settBusinessVisitStaff(companyId, visitPersons, visitDaysAfterOverDue ,businessType);
            }

        }

        //自动移交法务

        setBusinessToLaw();


    }

    public List<String> getStaffPersons(String getColPersonId,StaffPersonType pType){
        List<CollectionPersonSetDetail> phoneDetails = collectionPersonSetDetailService.selectList(
                new EntityWrapper<CollectionPersonSetDetail>()
                        .eq("col_person_id",getColPersonId)
                        .eq("team",pType.getIntKey()));
        List<String>  persons = new LinkedList<String>();
        for(CollectionPersonSetDetail detail:phoneDetails){
            //yzl 修改获取催收人员id
                persons.add(detail.getUserId());
            }


        return persons;
    }


    /**
     * 设置指定公司业务电催（符合规则的）
     * @param companyId
     * @param phonePersons
     * @param daysBeforeOverDue
     */
    public void setBusinessPhoneStaff(String companyId,List<String> phonePersons,Integer daysBeforeOverDue,Integer businessType){
        ////////////  分配电催  开始 ////////////////////
        //末期逾期  电催 跟进人员列表 带计数
        List<CollectionStatusCountDto> lastPlanPFPersonlist = selecLastPlanPhoneFollwPList(phonePersons);
        //单月逾期 电催 跟进人员列表 带计数
        List<CollectionStatusCountDto> monthPlanPFPersonlist = selecMonthPlanPhoneFollwPList(phonePersons);


        //一般业务
        List<RepaymentBizPlanList> planLists = repaymentBizPlanListService.selectNeedPhoneUrgNorBiz(companyId,daysBeforeOverDue,businessType);
       // List<RepaymentBizPlanList> planLists=repaymentBizPlanListService.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("plan_list_id", "6fa65d52-c5c3-4860-9586-083a0b0e0426"));
//        //展期业务
//        List<RepaymentBizPlanList> renewPlanLists = repaymentBizPlanListService.selectNeedPhoneUrgRenewBiz(companyId,daysBeforeOverDue);
//        planLists.addAll(renewPlanLists);
        for(RepaymentBizPlanList planList:planLists){
        	if(planList.getPlanListId().equals("70462eca-ec76-4b8b-b49e-4eb649f45a8c")||planList.getPlanListId().equals("050abc1f-0bbf-4cb3-969a-254c7603bd03")||planList.getPlanListId().equals("a90f963f-269f-4a0a-b00d-dc250e9cc926")) {
        		System.out.println("stop");
        	}

            // yzl  判断是否分配过催收时，需要按催收方式分类判断
            CollectionStatus lastCollectionStatus =getRecentlyCollectionStatus(planList.getPlanId(),planList.getPlanListId());
            CollectionStatus currentCollectionStatus =selectOne(new EntityWrapper<CollectionStatus>().eq("crp_id", planList.getPlanListId()).isNotNull("phone_staff"));
            
        
            
            if(currentCollectionStatus==null) {
            	
                if(lastCollectionStatus!=null&&lastCollectionStatus.getPhoneStaff()!=null&&phonePersons.contains(lastCollectionStatus.getPhoneStaff())){
                    try{
                        setAutoBusinessStaff(planList.getBusinessId(),planList.getPlanListId(),
                        		lastCollectionStatus.getPhoneStaff(),StaffPersonType.PHONE_STAFF.getKey());
                    }catch (Exception e){
                        e.printStackTrace();
                        logger.error("自动分配电催 延续上一期分配  数据存储异常 businessID:"+planList.getBusinessId()+
                                "  planListId:"+ planList.getPlanListId());
                    }

                    continue;
                }else if((lastCollectionStatus!=null&&lastCollectionStatus.getPhoneStaff()==null)||lastCollectionStatus==null||(!phonePersons.contains(lastCollectionStatus.getPhoneStaff()))){

                    /////////  此业务第一次分配 则区分月还逾期与末期逾期 ///////////
                    if(ifPlanListIsLast(planList)){//是末期逾期
                        Integer minIndex = getLimitCountIndex(lastPlanPFPersonlist);
                        try{
                            setAutoBusinessStaff(planList.getBusinessId(),planList.getPlanListId(),
                                    lastPlanPFPersonlist.get(minIndex).getPhoneStaff(),
                                    StaffPersonType.PHONE_STAFF.getKey());
                            lastPlanPFPersonlist.get(minIndex).setCounts(lastPlanPFPersonlist.get(minIndex).getCounts()+1);
                        }catch (Exception e){
                            e.printStackTrace();
                            logger.error("自动分配电催 末期逾期 第一次分配  数据存储异常 businessID:"+planList.getBusinessId()+
                                    "  planListId:"+ planList.getPlanListId());
                        }

                    }else{//是月还逾期
                        try{
                            Integer minIndex = getLimitCountIndex(monthPlanPFPersonlist);
                            setAutoBusinessStaff(planList.getBusinessId(),planList.getPlanListId(),
                                    // yzl 月还逾期 取monthPlanPFPersonlist
                                    monthPlanPFPersonlist.get(minIndex).getPhoneStaff(),
                                    StaffPersonType.PHONE_STAFF.getKey());
                            monthPlanPFPersonlist.get(minIndex).setCounts(monthPlanPFPersonlist.get(minIndex).getCounts()+1);
                        }catch (Exception e){
                            e.printStackTrace();
                            logger.error("自动分配电催 月还逾期 第一次分配  数据存储异常 businessID:"+planList.getBusinessId()+
                                    "  planListId:"+ planList.getPlanListId());
                        }
                    }
                }
            	
            }
        
        }
        ////////////  分配电催  结束 ////////////////////
    }





    /**
     * 设置指定公司业务上门催收（符合规则的）
     * @param companyId
     * @param visitPersons
     * @param visitDaysAfterOverDue
     */
    public  void settBusinessVisitStaff(String companyId,List<String> visitPersons, Integer visitDaysAfterOverDue,Integer businessType ){
        ////////////  分配上门催收  开始 ////////////////////
        //末期逾期  电催 跟进人员列表 带计数
        List<CollectionStatusCountDto> lastPlanVisitPersonlist = selecLastPlanVisitFollwPList(visitPersons);
        //单月逾期 电催 跟进人员列表 带计数
        List<CollectionStatusCountDto> monthPlanVisitersonlist = selecMonthPlanVisitFollwPList(visitPersons);


        //一般业务
        List<RepaymentBizPlanList> visitPlanLists = repaymentBizPlanListService.selectNeedVisitNorBiz(companyId,visitDaysAfterOverDue, businessType);
      //  List<RepaymentBizPlanList> visitPlanLists=repaymentBizPlanListService.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("plan_list_id", "6fa65d52-c5c3-4860-9586-083a0b0e0426"));

//        //展期业务
//        List<RepaymentBizPlanList> visitRnewPlanLists = repaymentBizPlanListService.selectNeedVisitRenewBiz(companyId,visitDaysAfterOverDue);
//        visitPlanLists.addAll(visitRnewPlanLists);
        for(RepaymentBizPlanList planList:visitPlanLists){

            // yzl  判断是否分配过催收时，需要按催收方式分类判断
            CollectionStatus lastCollectionStatus =getRecentlyCollectionStatus(planList.getPlanId(),planList.getPlanListId());
            CollectionStatus currentCollectionStatus =selectOne(new EntityWrapper<CollectionStatus>().eq("crp_id", planList.getPlanListId()).isNotNull("visit_staff"));
            
         
            if(currentCollectionStatus==null) {
            	
            	   if(lastCollectionStatus!=null&&lastCollectionStatus.getVisitStaff()!=null&&visitPersons.contains(lastCollectionStatus.getVisitStaff())){
                       try{
                           setAutoBusinessStaff(planList.getBusinessId(),planList.getPlanListId(),
                                   // yzl 取上门催收人员
                           		lastCollectionStatus.getVisitStaff(),StaffPersonType.VISIT_STAFF.getKey());
                       }catch (Exception e){
                           e.printStackTrace();
                           logger.error("自动分配电催 月还逾期 已分配过  数据存储异常 businessID:"+planList.getBusinessId()+
                                   "  planListId:"+ planList.getPlanListId());
                       }

                       continue;
                   }else if((lastCollectionStatus!=null&&lastCollectionStatus.getVisitStaff()==null)||lastCollectionStatus==null||(!visitPersons.contains(lastCollectionStatus.getVisitStaff()))){
                	   /////////  此业务第一次分配 则区分月还逾期与末期逾期 ///////////
                       if(ifPlanListIsLast(planList)){//是末期逾期
                           Integer minIndex = getLimitCountIndex(lastPlanVisitPersonlist);
                           try{
                           setAutoBusinessStaff(planList.getBusinessId(),planList.getPlanListId(),
                                   // yzl 取上门催收人员
                                   lastPlanVisitPersonlist.get(minIndex).getVisitStaff(),
                                   StaffPersonType.VISIT_STAFF.getKey());
                           }catch (Exception e){
                               e.printStackTrace();
                               logger.error("自动分配电催 末期逾期 第一次分配  数据存储异常 businessID:"+planList.getBusinessId()+
                                       "  planListId:"+ planList.getPlanListId());
                           }
                           lastPlanVisitPersonlist.get(minIndex).setCounts(lastPlanVisitPersonlist.get(minIndex).getCounts()+1);

                       }else{//是月还逾期
                           Integer minIndex = getLimitCountIndex(monthPlanVisitersonlist);
                           try{
                           setAutoBusinessStaff(planList.getBusinessId(),planList.getPlanListId(),
                                   // yzl 取上门催收人员
                                   monthPlanVisitersonlist.get(minIndex).getVisitStaff(),
                                   StaffPersonType.VISIT_STAFF.getKey());
                           }catch (Exception e){
                               e.printStackTrace();
                               logger.error("自动分配电催 月还逾期 第一次分配  数据存储异常 businessID:"+planList.getBusinessId()+
                                       "  planListId:"+ planList.getPlanListId());
                           }
                           monthPlanVisitersonlist.get(minIndex).setCounts(monthPlanVisitersonlist.get(minIndex).getCounts()+1);
                       }
                   }
            	
            	
            	
               
            }
        
        }

        ////////////  分配上门催收  结束 ////////////////////

    }


    /**
     * 自动移交法务
     * @param
     */
    @Value("${ht.litigation.url:http://172.16.200.110:30906/api/importLitigation}")
    private String sendUrl;
    public void setBusinessToLaw() {
        CollectionTimeSet lawTimeSet = collectionTimeSetService.selectOne(new EntityWrapper<CollectionTimeSet>().eq("col_type",CollectionStatusEnum.TO_LAW_WORK.getKey()).and("start_time <= NOW()"));

        Integer lawDaysAfterOverDue = lawTimeSet!=null?lawTimeSet.getOverDueDays():91;

        //一般业务
        List<RepaymentBizPlanList> planLists = repaymentBizPlanListService.selectNeedLawNorBiz(lawDaysAfterOverDue,null);
//        //展期业务
//        List<RepaymentBizPlanList> renewPlanLists = repaymentBizPlanListService.selectNeedLawRenewBiz(lawDaysAfterOverDue);
//        planLists.addAll(renewPlanLists);
        for(RepaymentBizPlanList planList:planLists) {
           try {
        	      setOneRepaymentBizPlanToLaw(planList); 
           }catch(Exception e){
        	   e.printStackTrace();
        	   logger.error("发送诉讼系统失败planListId:[{0}]",planList.getPlanListId());
           }
      


           /* setAutoBusinessStaff(planList.getBusinessId(),planList.getPlanListId(),
                    null,
                    StaffPersonType.VISIT_STAFF.getKey());
            List<Integer> l = new LinkedList<>();
            l.add(CollectionStatusEnum.PHONE_STAFF.getKey());
            l.add(CollectionStatusEnum.COLLECTING.getKey());
            List<CollectionStatus> oldStatus = selectList(new EntityWrapper<CollectionStatus>()
                    .in("collection_status",l)
                    .eq("business_id",planList.getBusinessId()));
            if(oldStatus.size()>0){
                for(CollectionStatus ss:oldStatus){
                    setAutoBusinessStaff(ss.getBusinessId(),ss.getCrpId(),
                            null,
                            StaffPersonType.VISIT_STAFF.getKey());
                }
            }*/
                            //[CollectionStatusEnum.PHONE_STAFF.getKey(),CollectionStatusEnum.COLLECTING.getKey()])

        }
    }

    /**
     * 将指定业务移交法务
     * @param businessId
     */
    public void setOneBusinessToLaw(String businessId){
        CollectionTimeSet lawTimeSet = collectionTimeSetService.selectOne(new EntityWrapper<CollectionTimeSet>().eq("col_type",CollectionStatusEnum.TO_LAW_WORK.getKey()).and("start_time <= NOW()"));

        Integer lawDaysAfterOverDue = lawTimeSet!=null?lawTimeSet.getOverDueDays():91;

        //一般业务
        List<RepaymentBizPlanList> planLists = repaymentBizPlanListService.selectNeedLawNorBizByBizId(lawDaysAfterOverDue,null,businessId);
        if(planLists==null || planLists.size()==0){
            throw new RuntimeException("找不到需要移交法务的业务");
        }
//        //展期业务
//        List<RepaymentBizPlanList> renewPlanLists = repaymentBizPlanListService.selectNeedLawRenewBiz(lawDaysAfterOverDue);
//        planLists.addAll(renewPlanLists);
        for(RepaymentBizPlanList planList:planLists) {

            setOneRepaymentBizPlanToLaw(planList);
        }

    }


    public  void setOneRepaymentBizPlanToLaw(RepaymentBizPlanList planList){
        RepaymentBizPlan repaymentBizPlan =  repaymentBizPlanService.selectById(planList.getPlanId());

        try{
            String originalBusinessId = repaymentBizPlan.getOriginalBusinessId();
            Result result = litigationFeignClient.isImportLitigation(originalBusinessId);
            logger.info("调用诉讼系统查询接口，参数：{}；返回信息：{}", originalBusinessId, JSONObject.toJSONString(result));
            // 调用诉讼系统查询接口，若调用失败或者非成功状态，则这条数据暂时不处理
            if (result == null || !"1".equals(result.getCode())) {
                return;
            }else {
                // 若调用成功，且状态是true，说明移交过法务
                if (!(Boolean) result.getData()) {
                    //调用移交诉讼接口
                    transferLitigationService.sendTransferLitigationData(
                            originalBusinessId,sendUrl,null, 1);
//                	Map<String, Object> paramMap = new HashMap<>();
//                	paramMap.put("businessId", originalBusinessId);
//                	paramMap.put("channel", 1);
//                	almsCoreServiceFeignClient.sendTransferLitigation(paramMap);
                }
            }

            //修改状态
          /*  setBussinessAfterStatus(
                    originalBusinessId,
                    planList.getPlanListId(),
                    "自动移交法务",
                    CollectionStatusEnum.TO_LAW_WORK,
                    CollectionSetWayEnum.AUTO_SET);*/

            List<StaffBusinessVo> voList = new LinkedList<>();
            StaffBusinessVo vo  = new StaffBusinessVo();
            vo.setCrpId(planList.getPlanListId());
            vo.setBusinessId(originalBusinessId);
            voList.add(vo);

            //同步贷后状态到信贷
//                SyncBusinessColStatusToXindai(voList,null,"定时任务自动分配",CollectionStatusEnum.TO_LAW_WORK.getPageStr());

        }catch (Exception e){
            logger.error("自動移交法务异常",e);
            e.printStackTrace();
        }
    }


    /**
     * 选择跟进电催最后一期业务的人员列表 带跟进业务条数
     * @param phonePersons
     * @return
     */
    public List<CollectionStatusCountDto>  selecLastPlanPhoneFollwPList(List<String> phonePersons){
        //末期逾期  跟进人员列表
        List<CollectionStatusCountDto> phoneFollowPersonlist = collectionStatusMapper.selectAllPersons(
                StaffPersonType.PHONE_STAFF.getKey()
                ,phonePersons
                ,CollectionStatusEnum.PHONE_STAFF.getKey()
                ,CollectionCrpTypeEnum.LAST.getKey()
        );

        addNoFollowPersons(phonePersons,phoneFollowPersonlist,StaffPersonType.PHONE_STAFF);
        return  phoneFollowPersonlist;
    }

    /**
     * 选择跟电催进单月逾期业务的人员列表  带跟进业务条数
     * @param phonePersons
     * @return
     */
   public List<CollectionStatusCountDto> selecMonthPlanPhoneFollwPList(List<String> phonePersons){
        //末期逾期  跟进人员列表
        List<CollectionStatusCountDto> phoneFollowPersonlist = collectionStatusMapper.selectAllPersons(
                StaffPersonType.PHONE_STAFF.getKey()
                ,phonePersons
                ,CollectionStatusEnum.PHONE_STAFF.getKey()
                ,CollectionCrpTypeEnum.NORMAL.getKey()
        );

        addNoFollowPersons(phonePersons,phoneFollowPersonlist,StaffPersonType.PHONE_STAFF);
        return  phoneFollowPersonlist;
    }

   /**
     * 选择跟进催收单月逾期业务的人员列表  带跟进业务条数
     * @param phonePersons
     * @return
     */
   public List<CollectionStatusCountDto> selecLastPlanVisitFollwPList(List<String> phonePersons){
        //末期逾期  跟进人员列表
        List<CollectionStatusCountDto> phoneFollowPersonlist = collectionStatusMapper.selectAllPersons(
                StaffPersonType.VISIT_STAFF.getKey()
                ,phonePersons
                ,CollectionStatusEnum.COLLECTING.getKey()
                ,CollectionCrpTypeEnum.LAST.getKey()
        );

        addNoFollowPersons(phonePersons,phoneFollowPersonlist,StaffPersonType.VISIT_STAFF);
        return  phoneFollowPersonlist;
    }
    /**
     * 选择跟进催收最后一期逾期业务的人员列表  带跟进业务条数
     * @param phonePersons
     * @return
     */
   public List<CollectionStatusCountDto> selecMonthPlanVisitFollwPList(List<String> phonePersons){
        //末期逾期  跟进人员列表
        List<CollectionStatusCountDto> phoneFollowPersonlist = collectionStatusMapper.selectAllPersons(
                StaffPersonType.VISIT_STAFF.getKey()
                ,phonePersons
                ,CollectionStatusEnum.COLLECTING.getKey()
                ,CollectionCrpTypeEnum.NORMAL.getKey()
        );

        addNoFollowPersons(phonePersons,phoneFollowPersonlist,StaffPersonType.VISIT_STAFF);
        return  phoneFollowPersonlist;
    }

    /**
     * 添加没有跟进任务的人员，与已分配任务的统一分配任务
     * @param phonePersons
     * @param list
     * @param pType
     * @return
     */
    public List<CollectionStatusCountDto> addNoFollowPersons(List<String> phonePersons,List<CollectionStatusCountDto> list,StaffPersonType pType){
        List<CollectionStatusCountDto> neverFollowPlanPersons = new LinkedList<>();
        for(int i=0;i<phonePersons.size();i++){
            String personName = phonePersons.get(i);
            boolean followFlage = false;
            if(personName == null){
                break;
            }



            for(CollectionStatusCountDto dto :list) {

                switch (pType) {
                    case PHONE_STAFF:
                        if(dto.getPhoneStaff() == null){
                            continue;
                        }
                        if (dto.getPhoneStaff().equals(personName)) {
                            followFlage = true;
                            break;
                        }
                    case VISIT_STAFF:
                        if(dto.getVisitStaff() == null){
                            continue;
                        }
                        if (dto.getVisitStaff().equals(personName)) {
                            followFlage = true;
                            break;
                        }
                }
            }
            if(!followFlage){
                CollectionStatusCountDto dto = new CollectionStatusCountDto();
                dto.setCounts(0);

                switch (pType){
                    case PHONE_STAFF:
                        dto.setPhoneStaff(personName);
                        break;
                    case VISIT_STAFF:
                        dto.setVisitStaff(personName);
                        break;
                    default:
                            break;


                }

                neverFollowPlanPersons.add(dto);
            }
        }
        list.addAll(neverFollowPlanPersons);
        return  list;
    }


    /**
     * 取跟进业务条数最小的Index
     * @param dtos
     * @return
     */
    public Integer getLimitCountIndex(List<CollectionStatusCountDto> dtos){
        if(dtos.size()==0){
            return null;
        }
        Integer minIndex = 0;
        Integer minCount = dtos.get(0).getCounts();
        for(int i=0;i<dtos.size();i++){
            CollectionStatusCountDto dto = dtos.get(i);
            if(dto.getCounts()<minCount){
                minCount = dto.getCounts();
                minIndex = i;
            }
        }
        return minIndex;
    }




    /**
     * 判断还款计划是否是最后一个
     * @param srcPList
     * @return
     */
    public boolean ifPlanListIsLast( RepaymentBizPlanList srcPList){
        RepaymentBizPlanList pList =  repaymentBizPlanListService.selectOne(new EntityWrapper<RepaymentBizPlanList>().eq("business_id",srcPList.getBusinessId()).orderBy("due_date desc"));
        if(pList.getPlanListId().equals(srcPList.getPlanListId())){
            return true;
        }
        return false;
    }


    @Override
    public List<String> selectFollowBusinessIds(String userId){
        List<CollectionStatus> statuses1 = selectList(new EntityWrapper<CollectionStatus>().eq("phone_staff",userId));
        List<CollectionStatus> statuse2 = selectList(new EntityWrapper<CollectionStatus>().eq("visit_staff",userId));


        List<String> businessIds = new LinkedList<>();

        for(CollectionStatus status:statuses1){
            businessIds.add(status.getBusinessId());
        }
        for(CollectionStatus status:statuse2){
            businessIds.add(status.getBusinessId());
        }

        return businessIds;
    }

    @Override
    @Transactional
    public boolean revokeClosedStatus(String businessId) {


        List<CollectionStatus>  collectionStatuses = selectList(new EntityWrapper<CollectionStatus>()
                .eq("business_id",businessId));
        if(collectionStatuses.size()==0){
            return true;
        }

        boolean retBoolean = true;
        for(CollectionStatus status:collectionStatuses){
            if(!status.getCollectionStatus().equals(CollectionStatusEnum.CLOSED.getKey())){
                continue;
            }
            List<CollectionLog> logs = collectionLogService.selectList(new EntityWrapper<CollectionLog>()
                .eq("business_id",status.getBusinessId())
                .eq("crp_id",status.getCrpId())
                .eq("after_status",CollectionStatusEnum.CLOSED.getKey())
                .orderBy("create_time desc"));

            if(logs.size()==0){
                logger.error("撤销贷后跟进关闭状态 找不到状态变更历史记录 businessId:"+ businessId);
                retBoolean = false;
                continue;
            }

            CollectionLog  log  = logs.get(0);
            logger.info("log:"+JSON.toJSONString(log));
            CollectionLog  newLog = new CollectionLog();
            newLog.setAfterStatus(log.getBeforeStatus());
            newLog.setBusinessId(businessId);
            newLog.setCollectionUser(Constant.ADMIN_ID);
            newLog.setCrpId(status.getCrpId());
            newLog.setUpdateUser(Constant.ADMIN_ID);
            newLog.setCreateUser(Constant.ADMIN_ID);
            newLog.setDescribe("信贷系统撤销结清 回退状态");
            newLog.setCreateTime(new Date());
            newLog.setUpdateTime(new Date());
            newLog.setSetWay(CollectionSetWayEnum.XINDAI_CALL.getKey());
            newLog.setBeforeStatus(CollectionStatusEnum.CLOSED.getKey());
            newLog.setSetTypeStatus(CollectionStatusEnum.REVOKE.getKey());

            collectionLogService.insert(newLog);

            status.setUpdateUser(Constant.ADMIN_ID);
            status.setUpdateTime(new Date());
            status.setSetWay(CollectionSetWayEnum.XINDAI_CALL.getKey());
            status.setDescribe("信贷系统撤销结清 回退状态");
            status.setCollectionStatus(log.getBeforeStatus());
            update(status, new EntityWrapper<CollectionStatus>().eq("business_id", status.getBusinessId()).eq("crp_id", status.getCrpId()));
        }

        return retBoolean;
    } 

	@Override
	public CollectionStatus getRecentlyCollectionStatus(String planId,String pListId) {
		CollectionStatus colStatus = collectionStatusMapper.getRecentlyCollectionStatus(planId,pListId);
		return colStatus;
	}



}
