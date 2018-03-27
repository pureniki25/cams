package com.hongte.alms.base.collection.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.collection.dto.CollectionStatusCountDto;
import com.hongte.alms.base.collection.entity.*;
import com.hongte.alms.base.collection.enums.CollectionCrpTypeEnum;
import com.hongte.alms.base.collection.enums.CollectionSetWayEnum;
import com.hongte.alms.base.collection.enums.CollectionStatusEnum;
import com.hongte.alms.base.collection.enums.StaffPersonType;
import com.hongte.alms.base.collection.mapper.CollectionStatusMapper;
import com.hongte.alms.base.collection.service.*;
import com.hongte.alms.base.collection.vo.StaffBusinessVo;
import com.hongte.alms.base.entity.RepaymentBizPlan;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.service.RepaymentBizPlanListDetailService;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.base.service.RepaymentBizPlanService;
import com.hongte.alms.base.service.TransferOfLitigationService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.util.RandomUtil;
import com.ht.ussp.bean.LoginUserInfoHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 贷后催收状态表 服务实现类
 * </p>
 *
 * @author 曾坤
 * @since 2018-01-25
 */
@Transactional  //事务声明
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

    /**
     * 设置电催/人员(界面手动设置)
     * @param voList 业务信息列表
     * @param staffUserId 催收人ID
     * @param describe 描述
     * @param staffType 催收类型
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean setBusinessStaff(
            List<StaffBusinessVo> voList,
            String staffUserId,
            String describe ,
            String staffType,
            CollectionSetWayEnum  setWayEnum
    ){

        String  userId= loginUserInfoHelper.getUserId();
        if(userId ==null){
            userId = Constant.DEFAULT_SYS_USER;
        }
//        String  userId= "programer_zk";



//        if(staffType.equals(StaffPersonType.PHONE_STAFF.getKey())){
//            collectionStatus = CollectionStatusEnum.PHONE_STAFF.getKey();
//        }else if (staffType.equals(StaffPersonType.VISIT_STAFF.getKey())){
//            collectionStatus =CollectionStatusEnum.COLLECTING.getKey();
//        }else if(staffType.equals(StaffPersonType.LAW.getKey())){
//            collectionStatus =CollectionStatusEnum.TO_LAW_WORK.getKey();
//        }else{
//            collectionStatus =CollectionStatusEnum.CLOSED.getKey();
//        }

        //如果是移交法务、关闭、拖车登记
        //需要更新此业务所有的历史记录为移交法务/关闭 /拖车登记
        if(staffType.equals(CollectionStatusEnum.TO_LAW_WORK.getPageStr())
                || staffType.equals(CollectionStatusEnum.CLOSED.getPageStr())
                || staffType.equals(CollectionStatusEnum.TRAILER_REG.getPageStr())){
            //1、更新历史记录为移交法务 /关闭  /拖车登记
            for(StaffBusinessVo vo:voList){
                List<CollectionStatus> list = selectList(new EntityWrapper<CollectionStatus>().eq("business_id",vo.getBusinessId()));
                List<CollectionLog> logs = new ArrayList<>() ;
                for(CollectionStatus status:list){
                    //跳过crpId一致的记录
                    if(status.getCrpId().equals(vo.getCrpId())) {
                        continue;
                    }
                    status.setCollectionStatus(CollectionStatusEnum.getByPageStr(staffType).getKey());
                    status.setSetWay(setWayEnum.getKey());
                    status.setUpdateTime(new Date());
                    switch (setWayEnum){
                        case XINDAI_CALL:
                        case AUTO_SET:
                            status.setUpdateUser(Constant.DEFAULT_SYS_USER);
                            break;
                        case MANUAL_SET:
                            status.setUpdateUser(loginUserInfoHelper.getUserId()==null?Constant.SYS_DEFAULT_USER:loginUserInfoHelper.getUserId());
                            break;
                    }
                    CollectionLog log = new CollectionLog();
                    log.setBusinessId(status.getBusinessId());
                    log.setCrpId(status.getCrpId());
                    log.setAfterStatus(CollectionStatusEnum.getByPageStr(staffType).getKey());
                    log.setCollectionUser("".equals(staffUserId)?Constant.SYS_DEFAULT_USER:staffUserId);
                    log.setCreateTime(new Date());
                    log.setCreateUser(status.getUpdateUser());
                    log.setDescribe(setWayEnum.getName());
                    log.setSetWay(setWayEnum.getKey());
                    logs.add(log);
                }
                if(list.size()>0){
                    updateBatchById(list);
                }
                if(logs.size()>0){
                    collectionLogService.insertBatch(logs);
                }
            }
        }else if(staffType.equals(CollectionStatusEnum.PHONE_STAFF.getPageStr())
                || staffType.equals(CollectionStatusEnum.COLLECTING.getPageStr())){
            //如果是设置电催或者催收 判断业务是否已经拖车登记/关闭/移交法务
            for(StaffBusinessVo vo:voList){
                List<CollectionStatus> list = selectList(new EntityWrapper<CollectionStatus>().eq("business_id",vo.getBusinessId()));
                for(CollectionStatus status:list) {
                    if (status.getCollectionStatus().equals(CollectionStatusEnum.TO_LAW_WORK.getKey())
                    ||status.getCollectionStatus().equals(CollectionStatusEnum.CLOSED.getKey())
                    ||status.getCollectionStatus().equals(CollectionStatusEnum.TRAILER_REG.getKey())){
                        String statStr = CollectionStatusEnum.getByKey(status.getCollectionStatus()).getName();
                        logger.error("此业务已"+statStr+"，不能再设置催收或者电催！  businessId:"+ status.getBusinessId()+"     crpId:"+status.getCrpId());
                        throw  new  RuntimeException("此业务已"+statStr+"，不能再设置催收或者电催！");
                    }
                }
            }
        }


        Integer setTypeStatus = CollectionStatusEnum.getByPageStr(staffType).getKey();
        for(StaffBusinessVo vo:voList){
            CollectionStatus status = new CollectionStatus();
            //1、插入或更新催收状态表
            List<CollectionStatus> list = selectList(new EntityWrapper<CollectionStatus>().eq("business_id",vo.getBusinessId()).eq("crp_id",vo.getCrpId()));
            if(list.size()>0){
                status = list.get(0);
            }else{
                //如果是设置关闭状态，又没有写过催收的状态记录则跳过，不增加催收状态记录
                if(CollectionStatusEnum.getByPageStr(staffType).equals(CollectionStatusEnum.CLOSED)){
                    continue;
                }
            }
            Integer beforeStatus = status.getCollectionStatus();
            Integer afterStatus = getCurrentColStatu(status,setTypeStatus);
            status.setBusinessId(vo.getBusinessId());
            status.setCollectionStatus(afterStatus);
            status.setCrpId(vo.getCrpId());
            if(staffType.equals(CollectionStatusEnum.PHONE_STAFF.getPageStr())){
                status.setPhoneStaff(staffUserId);
            }else if (staffType.equals(CollectionStatusEnum.COLLECTING.getPageStr())){
                status.setVisitStaff(staffUserId);
            }
            status.setUpdateUser(userId);
            status.setCreateUser(userId);
            status.setDescribe(describe);
            status.setCreateTime(new Date());
            status.setUpdateTime(new Date());
            status.setSetWay(setWayEnum.getKey());
            RepaymentBizPlanList planList = repaymentBizPlanListService.selectById(vo.getCrpId());
            if(ifPlanListIsLast(planList)){
                status.setCrpType(CollectionCrpTypeEnum.LAST.getKey());
            }else{
                status.setCrpType(CollectionCrpTypeEnum.NORMAL.getKey());
            }

            if(list.size()>0){
                update(status,new EntityWrapper<CollectionStatus>().eq("business_id",vo.getBusinessId()).eq("crp_id",vo.getCrpId()));
            }else{
                insert(status);
            }
//            insertOrUpdate(status);
            //2、记录移交记录表
            CollectionLog log = new CollectionLog();
            log.setAfterStatus(afterStatus);
            log.setBusinessId(vo.getBusinessId());
            log.setCollectionUser("".equals(staffUserId)?Constant.SYS_DEFAULT_USER:staffUserId);
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

        if(status.getCollectionStatus()==null){
            return setTypeStatus;
        }else if(status.getCollectionStatus().equals(CollectionStatusEnum.CLOSED.getKey())){
            //当前状态已经是已关闭的，则不刷新状态
            return  status.getCollectionStatus();
        }else if(status.getCollectionStatus().equals(CollectionStatusEnum.TO_LAW_WORK.getKey())){
            //当前状态是已移交诉讼
            if(setTypeStatus.equals(CollectionStatusEnum.CLOSED.getKey())){
                //设置的状态为 关闭 才刷新状态
                return setTypeStatus;
            }else{
                return status.getCollectionStatus();
            }
        }else if (status.getCollectionStatus().equals(CollectionStatusEnum.TRAILER_REG.getKey())){
            //当前状态为已拖车登记
            if(setTypeStatus.equals(CollectionStatusEnum.CLOSED.getKey())
                    || setTypeStatus.equals(CollectionStatusEnum.TO_LAW_WORK.getKey())){
                //设置的状态为 关闭  移交法务 才刷新状态
                return setTypeStatus;
            }else{
                return status.getCollectionStatus();
            }
        }else if (status.getCollectionStatus().equals(CollectionStatusEnum.COLLECTING.getKey())){
            //当前状态为已拖车登记
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
        // 将StaffBusinessVo 放入list
        voList.add(vo);
        return setBusinessStaff(
                voList,
                staffUserId,
                "定时任务自动分配",
                staffType,
                CollectionSetWayEnum.AUTO_SET);
    };

    /**
     * 自动移交：
     */
    @Override
    public void autoSetBusinessStaff(){
        //查找分配了电催人员的分公司列表
        List<CollectionPersonSet> list = collectionPersonSetService.selectList(new EntityWrapper<CollectionPersonSet>());
        CollectionTimeSet phoneTimeSet = collectionTimeSetService.selectOne(new EntityWrapper<CollectionTimeSet>().eq("col_type",CollectionStatusEnum.PHONE_STAFF.getKey()));
        CollectionTimeSet visitTimeSet = collectionTimeSetService.selectOne(new EntityWrapper<CollectionTimeSet>().eq("col_type",CollectionStatusEnum.COLLECTING.getKey()));
         Integer daysBeforeOverDue = phoneTimeSet!=null?phoneTimeSet.getOverDueDays():0;
        Integer visitDaysAfterOverDue = visitTimeSet!=null?visitTimeSet.getOverDueDays():31;

        for(CollectionPersonSet set:list){
            String companyId = set.getCompanyCode();

            List<String>  phonePersons = getStaffPersons(set.getColPersonId(),StaffPersonType.PHONE_STAFF);
            List<String>  visitPersons = getStaffPersons(set.getColPersonId(),StaffPersonType.VISIT_STAFF);

//            List<CollectionPersonSetDetailService>
//            String[] phonePersons = set.getCollect1Person().split(",");
//            String[] visitPersons = set.getCollect2Person().split(",");

            //自动移交电催
            if(phonePersons.size()>0){
                setBusinessPhoneStaff(companyId, phonePersons, daysBeforeOverDue);
            }
            //自动移交催收
            if(visitPersons.size()>0){
                settBusinessVisitStaff(companyId, visitPersons, visitDaysAfterOverDue );
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
    public void setBusinessPhoneStaff(String companyId,List<String> phonePersons,Integer daysBeforeOverDue){
        ////////////  分配电催  开始 ////////////////////
        //末期逾期  电催 跟进人员列表 带计数
        List<CollectionStatusCountDto> lastPlanPFPersonlist = selecLastPlanPhoneFollwPList(phonePersons);
        //单月逾期 电催 跟进人员列表 带计数
        List<CollectionStatusCountDto> monthPlanPFPersonlist = selecMonthPlanPhoneFollwPList(phonePersons);


        //一般业务
        List<RepaymentBizPlanList> planLists = repaymentBizPlanListService.selectNeedPhoneUrgNorBiz(companyId,daysBeforeOverDue);
//        //展期业务
//        List<RepaymentBizPlanList> renewPlanLists = repaymentBizPlanListService.selectNeedPhoneUrgRenewBiz(companyId,daysBeforeOverDue);
//        planLists.addAll(renewPlanLists);
        for(RepaymentBizPlanList planList:planLists){

            // yzl  判断是否分配过催收时，需要按催收方式分类判断
            CollectionStatus collectionStatus =  selectOne(new EntityWrapper<CollectionStatus>().eq("business_id",planList.getBusinessId()).eq("collection_status",CollectionStatusEnum.PHONE_STAFF.getKey()));
            if(collectionStatus!=null){
                try{
                    setAutoBusinessStaff(planList.getBusinessId(),planList.getPlanListId(),
                            collectionStatus.getPhoneStaff(),StaffPersonType.PHONE_STAFF.getKey());
                }catch (Exception e){
                    e.printStackTrace();
                    logger.error("自动分配电催 延续上一期分配  数据存储异常 businessID:"+planList.getBusinessId()+
                            "  planListId:"+ planList.getPlanListId());
                }

                continue;
            }
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
        ////////////  分配电催  结束 ////////////////////
    }





    /**
     * 设置指定公司业务上门催收（符合规则的）
     * @param companyId
     * @param visitPersons
     * @param visitDaysAfterOverDue
     */
    public  void settBusinessVisitStaff(String companyId,List<String> visitPersons, Integer visitDaysAfterOverDue ){
        ////////////  分配上门催收  开始 ////////////////////
        //末期逾期  电催 跟进人员列表 带计数
        List<CollectionStatusCountDto> lastPlanVisitPersonlist = selecLastPlanVisitFollwPList(visitPersons);
        //单月逾期 电催 跟进人员列表 带计数
        List<CollectionStatusCountDto> monthPlanVisitersonlist = selecMonthPlanVisitFollwPList(visitPersons);


        //一般业务
        List<RepaymentBizPlanList> visitPlanLists = repaymentBizPlanListService.selectNeedVisitNorBiz(companyId,visitDaysAfterOverDue);
//        //展期业务
//        List<RepaymentBizPlanList> visitRnewPlanLists = repaymentBizPlanListService.selectNeedVisitRenewBiz(companyId,visitDaysAfterOverDue);
//        visitPlanLists.addAll(visitRnewPlanLists);
        for(RepaymentBizPlanList planList:visitPlanLists){

            // yzl  判断是否分配过催收时，需要按催收方式分类判断
            CollectionStatus collectionStatus =  selectOne(new EntityWrapper<CollectionStatus>().eq("business_id",planList.getBusinessId()).eq("collection_status",CollectionStatusEnum.COLLECTING.getKey()));
            if(collectionStatus!=null){
                try{
                    setAutoBusinessStaff(planList.getBusinessId(),planList.getPlanListId(),
                            // yzl 取上门催收人员
                            collectionStatus.getVisitStaff(),StaffPersonType.VISIT_STAFF.getKey());
                }catch (Exception e){
                    e.printStackTrace();
                    logger.error("自动分配电催 月还逾期 第一次分配  数据存储异常 businessID:"+planList.getBusinessId()+
                            "  planListId:"+ planList.getPlanListId());
                }

                continue;
            }
            /////////  此业务第一次分配 则区分月还逾期与末期逾期 ///////////
            if(ifPlanListIsLast(planList)){//是末期逾期
                Integer minIndex = getLimitCountIndex(lastPlanVisitPersonlist);
                setAutoBusinessStaff(planList.getBusinessId(),planList.getPlanListId(),
                        // yzl 取上门催收人员
                        lastPlanVisitPersonlist.get(minIndex).getVisitStaff(),
                        StaffPersonType.VISIT_STAFF.getKey());
                lastPlanVisitPersonlist.get(minIndex).setCounts(lastPlanVisitPersonlist.get(minIndex).getCounts()+1);

            }else{//是月还逾期
                Integer minIndex = getLimitCountIndex(monthPlanVisitersonlist);
                setAutoBusinessStaff(planList.getBusinessId(),planList.getPlanListId(),
                        // yzl 取上门催收人员
                        monthPlanVisitersonlist.get(minIndex).getVisitStaff(),
                        StaffPersonType.VISIT_STAFF.getKey());
                monthPlanVisitersonlist.get(minIndex).setCounts(monthPlanVisitersonlist.get(minIndex).getCounts()+1);
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
        List<RepaymentBizPlanList> planLists = repaymentBizPlanListService.selectNeedLawNorBiz(lawDaysAfterOverDue);
//        //展期业务
//        List<RepaymentBizPlanList> renewPlanLists = repaymentBizPlanListService.selectNeedLawRenewBiz(lawDaysAfterOverDue);
//        planLists.addAll(renewPlanLists);
        for(RepaymentBizPlanList planList:planLists) {
            RepaymentBizPlan repaymentBizPlan =  repaymentBizPlanService.selectById(planList.getPlanId());

            try{
                //调用移交诉讼接口
                transferLitigationService.sendTransferLitigationData(
                        repaymentBizPlan.getOriginalBusinessId(),sendUrl);
                //修改状态
                setBussinessAfterStatus(
                        repaymentBizPlan.getOriginalBusinessId(),
                        planList.getPlanListId(),
                        "自动移交法务",
                        CollectionStatusEnum.TO_LAW_WORK,
                        CollectionSetWayEnum.AUTO_SET);
            }catch (Exception e){
                logger.error("自動移交法务异常",e);
                e.printStackTrace();
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
            for(CollectionStatusCountDto dto :list){
                if(dto.getPhoneStaff().equals(personName)){
                    followFlage = true;
                    break;
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

            CollectionLog  newLog = new CollectionLog();
            log.setAfterStatus(log.getBeforeStatus());
            log.setBusinessId(businessId);
            log.setCollectionUser(Constant.ADMIN_ID);
            log.setCrpId(status.getCrpId());
            log.setUpdateUser(Constant.ADMIN_ID);
            log.setCreateUser(Constant.ADMIN_ID);
            log.setDescribe("信贷系统撤销结清 回退状态");
            log.setCreateTime(new Date());
            log.setUpdateTime(new Date());
            log.setSetWay(CollectionSetWayEnum.XINDAI_CALL.getKey());
            log.setBeforeStatus(CollectionStatusEnum.CLOSED.getKey());
            log.setSetTypeStatus(CollectionStatusEnum.REVOKE.getKey());

            collectionLogService.insert(newLog);

            status.setUpdateUser(Constant.ADMIN_ID);
            status.setUpdateTime(new Date());
            status.setSetWay(CollectionSetWayEnum.XINDAI_CALL.getKey());
            status.setDescribe("信贷系统撤销结清 回退状态");
            status.setCollectionStatus(log.getBeforeStatus());
            updateById(status);
        }



        return retBoolean;
    }


    public  static  void main(String[] args){
        for(int i=0;i<100;i++){
//            Random rd = new Random();
//            System.out.println(rd.nextInt());

//            int v = (int)Math.random()*1000;
//            System.out.println(v);
            System.out.println(RandomUtil.generateRandomInt(0,4));

//            int max=20;
//            int min=0;
//            Random random = new Random();
//
//            int s = random.nextInt(max)%(max-min+1) + min;
//            System.out.println(s);
        }

//        public static void main(String[] args) {
//            int max=20;
//            int min=10;
//            Random random = new Random();
//
//            int s = random.nextInt(max)%(max-min+1) + min;
//            System.out.println(s);
//        }

    }

}
