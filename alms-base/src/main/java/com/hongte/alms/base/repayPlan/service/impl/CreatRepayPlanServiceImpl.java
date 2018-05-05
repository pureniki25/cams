package com.hongte.alms.base.repayPlan.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.baseException.CreatRepaymentExcepiton;
import com.hongte.alms.base.entity.*;
import com.hongte.alms.base.enums.RepayPlanStatus;
import com.hongte.alms.base.repayPlan.dto.RepaymentBizPlanDto;
import com.hongte.alms.base.repayPlan.dto.RepaymentBizPlanListDto;
import com.hongte.alms.base.repayPlan.enums.*;
import com.hongte.alms.base.repayPlan.req.*;
import com.hongte.alms.base.repayPlan.service.CreatRepayPlanService;
import com.hongte.alms.base.service.ProfitItemSetService;
import com.hongte.alms.base.service.RepaymentBizPlanService;
import com.hongte.alms.base.service.RepaymentProjPlanService;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

/**
 * @author zengkun
 * @since 2018/4/24
 * 创建还款计划的服务
 */
@Service("CreatRepayPlanService")
public class CreatRepayPlanServiceImpl  implements CreatRepayPlanService {


    private  static Logger logger = LoggerFactory.getLogger(CreatRepayPlanServiceImpl.class);


    @Autowired
    @Qualifier("RepaymentProjPlanService")
    RepaymentProjPlanService  repaymentProjPlanService;

    @Autowired
    @Qualifier("RepaymentBizPlanService")
    RepaymentBizPlanService RepaymentBizPlanService;

    @Autowired
    @Qualifier("ProfitItemSetService")
    ProfitItemSetService profitItemSetService;


    //进位方式枚举
    private  RoundingMode roundingMode=RoundingMode.HALF_UP;
    //保留的小数位数
    private  Integer smallNum=4;

    //本金字符串
    private static final  String principal_str = "principal";
    //利息字符串
    private static final  String interest_str = "interest";



    //需要判断是否重复传入
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<RepaymentBizPlanDto> creatRepayPlan(CreatRepayPlanReq creatRepayPlanReq) {

        List<RepaymentBizPlanDto>  retList = new LinkedList<>();

        //设置进位方式枚举和保留的小数位数
        smallNum = creatRepayPlanReq.getSmallNum();
        roundingMode = RoundingMode.valueOf(creatRepayPlanReq.getRondmode());

        //1、根据业务和标的对应关系，判断出需要生成几个还款计划，每个还款计划相对于业务总金额占的比例
        // 1))业务基本信息
        BusinessBasicInfoReq  businessBasicInfo = creatRepayPlanReq.getBusinessBasicInfoReq();

        List<ProjInfoReq> tuandaiProjReqInfos = creatRepayPlanReq.getTuandaiProjReqInfos();

        //计算每个标 在占整个业务的比例
        Map<String,BigDecimal>  projPersent = new HashMap<>();
        for(ProjInfoReq projInfoReq:tuandaiProjReqInfos){
            BigDecimal persent = projInfoReq.getFullBorrowMoney().divide(businessBasicInfo.getBorrowMoney());
            projPersent.put(projInfoReq.getProjectId(),persent);
        }

//        String bizPlanId = UUID.randomUUID().toString();
        Map<String,List<RepaymentProjPlan>> repaymentProjPlanMap = new HashMap<>();


        //期数对应的标List列表    以业务批次计
        //Map<批次，Map<期数，标还款计划费用详情列表>>
        Map<String,Map<Integer,List<RepaymentProjPlanList>>>  repaymentPlanListBatchMap = new HashMap<>();

        //标还款计划ListMap
//        Map<批次,Map<标还款计划主表Id，标的还款计划list列表>>
        Map<String,Map<String,List<RepaymentProjPlanList>>> projPlanListTotalMap = new HashMap<>();



        //期数 费用类型对应的标ListDetail列表  以业务批次计
        //Map<批次,Map<期数，Map<费用类型fee_id,标还款计划费用详情列表>>>
        Map<String,Map<Integer,Map<String,List<RepaymentProjPlanListDetail>>>>  RepaymentProjPlanListDetailBatchMap = new HashMap<>();
        //标还款计划ListMap
//        Map<批次,Map<标还款计划主表Id，Map<标还款计划list表Id，标的还款计划detail列表>>
        Map<String,Map<String,Map<String,List<RepaymentProjPlanListDetail>>>> projPlanDetailTotalMap = new HashMap<>();



        // Map<起标日期字符串,Map<批次UUID,List<ProjInfoReq>>>
        //计算批次
        Map<String,List<ProjInfoReq>>  projInfoReqMap = getProjInfoReqMap(tuandaiProjReqInfos);


        ////////   计算每个标的还款计划列表   开始 ////////////
        calcProjRepayments(
                projInfoReqMap,
                repaymentPlanListBatchMap,
                RepaymentProjPlanListDetailBatchMap,
                repaymentProjPlanMap,
                creatRepayPlanReq.getPlateType(),
                businessBasicInfo,
                projPlanDetailTotalMap,
                projPlanListTotalMap );
        ////////   计算每个标的还款计划列表   结束 ////////////
//    //        Map<批次,Map<标还款计划主表Id，Map<标还款计划list表Id，标的还款计划detail列表>>
//    Map<String,Map<String,Map<String,List<RepaymentProjPlanListDetail>>>> projPlanDetailTotalMap = new HashMap<>();
//    //        Map<批次,Map<标还款计划主表Id，标的还款计划list列表>>
//    Map<String,Map<String,List<RepaymentProjPlanList>>> projPlanListTotalMap = new HashMap<>();
        /////  根据标的的还款计划  生成 业务的还款计划  开始   //////////////



//                //期数对应的标List列表    以业务批次计
//                //Map<批次，Map<期数，标还款计划费用详情列表>>
//                Map<String,Map<Integer,List<RepaymentProjPlanList>>>  repaymentPlanListBatchMap = new HashMap<>();
//
//                //期数 费用类型对应的标ListDetail列表  以业务批次计
//                //Map<批次,Map<期数，Map<费用类型fee_id,标还款计划费用详情列表>>>
//                Map<String,Map<Integer,Map<String,List<RepaymentProjPlanListDetail>>>>  RepaymentProjPlanListDetailBatchMap = new HashMap<>();

        //业务还款计划 Map
        //Map<批次Id，业务还款计划>
        Map<String,RepaymentBizPlan>  bizPlanMap = new HashMap<>();

        //业务还款计划List  Map
        //Map<批次Id，业务还款计划List列表>
        Map<String,List<RepaymentBizPlanList>>  bizPlanListMap = new HashMap<>();

        //业务还款计划Detail Map
        //Map<批次Id，业务还款计划Id，业务还款计划Detail列表>
        Map<String,Map<String,List<RepaymentBizPlanListDetail>>> bizPlanListDetialMap =  new HashMap<>();


        //根据标的还款计划  生成 业务的还款计划信息
        calcBizRepayPlans(
                repaymentPlanListBatchMap,
                repaymentProjPlanMap,
                RepaymentProjPlanListDetailBatchMap,
                bizPlanMap,
                bizPlanListMap,
                bizPlanListDetialMap,
                businessBasicInfo
        );

        /////  根据标的的还款计划  生成 业务的还款计划  结束   //////////////


        //////  整理成 返回数据的格式  将信息返回出去  开始  ///////////

        for(String batchId:bizPlanMap.keySet()){
            RepaymentBizPlanDto planDto = new RepaymentBizPlanDto();
            planDto.setRepaymentBizPlan(bizPlanMap.get(batchId));

            List<RepaymentBizPlanList> bizPlanLists =bizPlanListMap.get(batchId);
            Map<String,List<RepaymentBizPlanListDetail>>  bizDetailMaps = bizPlanListDetialMap.get(batchId);
            List<RepaymentBizPlanListDto>  bizPlanListDtos = new LinkedList<>();
            planDto.setBizPlanListDtos(bizPlanListDtos);
            for (RepaymentBizPlanList bizPlanList:bizPlanLists){
                RepaymentBizPlanListDto bizPlanListDto = new RepaymentBizPlanListDto();
                bizPlanListDto.setRepaymentBizPlanList(bizPlanList);

                List<RepaymentBizPlanListDetail> bizPlanListDetails = bizDetailMaps.get(bizPlanList.getPlanListId());
                bizPlanListDto.setBizPlanListDetails(bizPlanListDetails);

            }







        }


        //////  整理成 返回数据的格式  将信息返回出去  结束  ///////////


        return null;
    }


    /**
     * 根据标的的还款计划  生成 业务的还款计划信息
     * @param repaymentPlanListBatchMap  标的还款计划list表Map
     * @param repaymentProjPlanMap       标的还款计划主表Map
     * @param RepaymentProjPlanListDetailBatchMap  标的还款计划Detail表Map
     * @param bizPlanMap            业务还款计划主表Map
     * @param bizPlanListMap        业务还款计划List表Map
     * @param bizPlanListDetialMap  业务还款计划Detail表Map
     * @param businessBasicInfo     业务基础信息
     */
    private void calcBizRepayPlans(Map<String,Map<Integer,List<RepaymentProjPlanList>>>  repaymentPlanListBatchMap,
                                   Map<String,List<RepaymentProjPlan>> repaymentProjPlanMap,
                                   Map<String,Map<Integer,Map<String,List<RepaymentProjPlanListDetail>>>> RepaymentProjPlanListDetailBatchMap,
                                   Map<String,RepaymentBizPlan>  bizPlanMap,
                                   Map<String,List<RepaymentBizPlanList>>  bizPlanListMap,
                                   Map<String,Map<String,List<RepaymentBizPlanListDetail>>> bizPlanListDetialMap,
                                   BusinessBasicInfoReq  businessBasicInfo
                                   ){

        for(String batchId:repaymentPlanListBatchMap.keySet()){
            List<RepaymentProjPlan> projPlans = repaymentProjPlanMap.get(batchId);
            Map<Integer,List<RepaymentProjPlanList>> planListPeroidMap = repaymentPlanListBatchMap.get(batchId);
            Map<Integer,Map<String,List<RepaymentProjPlanListDetail>>> planListDetailPeriodMap = RepaymentProjPlanListDetailBatchMap.get(batchId);

            //业务还款计划列表 list
            List<RepaymentBizPlanList> bizPlanLists =new LinkedList<>();
            bizPlanListMap.put(batchId,bizPlanLists);
            //业务还款计划详情  Map
            Map<String,List<RepaymentBizPlanListDetail>> bizPlanListDetailMap = new HashMap<>();
            bizPlanListDetialMap.put(batchId,bizPlanListDetailMap);

            //业务还款计划主表
            RepaymentBizPlan bizPlan = new RepaymentBizPlan();
            bizPlanMap.put(batchId,bizPlan);
            bizPlan.setPlanId(UUID.randomUUID().toString());
            bizPlan.setBusinessId(businessBasicInfo.getBusinessId()); //业务Id
            bizPlan.setOriginalBusinessId(businessBasicInfo.getOrgBusinessId());  //原业务Id
            bizPlan.setRepaymentBatchId(batchId);     //还款计划批次号
            bizPlan.setBorrowMoney(new BigDecimal(0));  //  生成还款计划对应的借款总额(元)    需要后续计算
            bizPlan.setBorrowRate(projPlans.get(0).getBorrowRate());  //  生成还款计划对应的借款利率(%)
            bizPlan.setBorrowRateUnit(projPlans.get(0).getBorrowRateUnit());  //  生成还款计划对应的借款利率类型     将此批次第一个标的信息赋值过来
            bizPlan.setBorrowLimit(projPlans.get(0).getBorrowLimit());  //  生成还款计划对应的借款期限    将此批次第一个标的信息赋值过来
            bizPlan.setBorrowLimitUnit(projPlans.get(0).getBorrowLimitUnit());  //  生成还款计划对应的借款期限单位     将此批次第一个标的信息赋值过来
            bizPlan.setPlanStatus(RepayPlanPayStatusEnum.REPAYINF.getValue());  // 还款计划状态
            bizPlan.setIsDefer(businessBasicInfo.getIsRenewBusiness()); //是否展期还款计划
            bizPlan.setXdAfterGuid(null);       //对应原信贷的还款批次号  置位为空
            bizPlan.setXdOutId(null);       //对应原信贷的出款计划ID  置位为空
            bizPlan.setActive(RepayPlanActiveEnum.ACTIVE.getValue());       //是否有效状态
            bizPlan.setSrcType(RepayPlanCreateSysEnum.ALMS.getValue());       //还款计划生成系统标志
            bizPlan.setCreateTime(new Date());       //创建日期
            bizPlan.setCreateUser(Constant.SYS_DEFAULT_USER);       //创建用户

            //更新标的还款计划主表的 plan_id
            for (RepaymentProjPlan projPlan:projPlans ){
                projPlan.setPlanId(bizPlan.getPlanId());
            }

            for(Integer period : planListPeroidMap.keySet()){
                //这一期 标还款计划列表
                List<RepaymentProjPlanList> planLists = planListPeroidMap.get(period);
                //这一期 标还款计划详情 Map
                Map<String,List<RepaymentProjPlanListDetail>>   projpDetialMap = planListDetailPeriodMap.get(period);
                //业务还款计划list
                RepaymentBizPlanList bizPlanList = new RepaymentBizPlanList();
                bizPlanLists.add(bizPlanList);

                bizPlanList.setPlanListId(UUID.randomUUID().toString());  //还款计划列表Id
                bizPlanList.setPlanId(bizPlan.getPlanId());  //还款计划Id
                bizPlanList.setBusinessId(bizPlan.getBusinessId()); //业务Id
                bizPlanList.setOrigBusinessId(bizPlan.getOriginalBusinessId());  //原业务Id
                bizPlanList.setPeriod(period);  //期数
                bizPlanList.setAfterId(planLists.get(0).getAfterId());  //期数编码
                bizPlanList.setDueDate(planLists.get(0).getDueDate());  //应还日期
                //总计划应还金额
                BigDecimal  totalBorrowAmount = new BigDecimal(0);
                for(RepaymentProjPlanList  projPList:planLists){
                    totalBorrowAmount.add(projPList.getTotalBorrowAmount());
                }
                bizPlanList.setTotalBorrowAmount(totalBorrowAmount);   //应还金额
                bizPlanList.setOverdueAmount(null);   //总应还滞纳金(元)
                bizPlanList.setCurrentStatus(RepayPlanStatus.REPAYING.getName());   //当前还款状态
                bizPlanList.setCurrentSubStatus(null);   //当前还款子状态
                bizPlanList.setRepayFlag(RepayPlanPayedTypeEnum.PAYING.getValue());   // 已还款类型标记
                bizPlanList.setActive(RepayPlanActiveEnum.ACTIVE.getValue());   // 是否有效状态
                bizPlanList.setCreateTime(new Date());   // 创建日期
                bizPlanList.setSrcType(bizPlanList.getSrcType());   // 来源类型
                bizPlanList.setCreateUser(Constant.SYS_DEFAULT_USER);   // 创建用户


                //更新标的 list 业务还款计划相关Id
                for(RepaymentProjPlanList projPlanList:planLists){
                    projPlanList.setPlanId(bizPlan.getPlanId());
                    projPlanList.setPlanListId(bizPlanList.getPlanListId());
                }


                //业务这一期  还款明细列表
                List<RepaymentBizPlanListDetail>  bizPlanListDetails = new LinkedList<>();
                bizPlanListDetailMap.put(bizPlanList.getPlanListId(),bizPlanListDetails);

                for( String feeId:projpDetialMap.keySet()){
                    List<RepaymentProjPlanListDetail>  projpDetials = projpDetialMap.get(feeId);

                    //业务这一期  还款明细具体项
                    RepaymentBizPlanListDetail  bizpDetial = new RepaymentBizPlanListDetail();
                    bizPlanListDetails.add(bizpDetial);

                    bizpDetial.setPlanDetailId(UUID.randomUUID().toString());   //主键
                    bizpDetial.setPlanListId(bizPlanList.getPlanListId());  //planListId
                    bizpDetial.setBusinessId(bizPlanList.getBusinessId());  //业务Id
                    bizpDetial.setPeriod(bizPlanList.getPeriod());          //期数
                    bizpDetial.setShareProfitIndex(projpDetials.get(0).getShareProfitIndex());  //分润顺序

                    //项目计划应还总金额(元)
                    BigDecimal planAmount = new BigDecimal(0);
                    for(RepaymentProjPlanListDetail pDetail: projpDetials){
                        planAmount.add(pDetail.getProjPlanAmount());
                    }
                    bizpDetial.setPlanAmount(planAmount);  //项目计划应还总金额(元)

                    bizpDetial.setPlanRate(null);  //项目计划应还比例(%)，如0.5%则存0.5，可空
                    bizpDetial.setFeeId(feeId);  //资产端费用项ID，用于资产端区分同名的项目
                    bizpDetial.setPlanItemName(projpDetials.get(0).getPlanItemName());  //应还项目名称
                    bizpDetial.setPlanItemType(projpDetials.get(0).getPlanItemType());  //应还项目所属分类
                    bizpDetial.setAccountStatus(projpDetials.get(0).getAccountStatus());  // 分账标记
                    bizpDetial.setActive(RepayPlanActiveEnum.ACTIVE.getValue());  // 是否有效状态
                    bizpDetial.setCreateDate(new Date());  // 创建日期
                    bizpDetial.setSrcType(RepayPlanCreateSysEnum.ALMS.getValue());  // 来源类型
                    bizpDetial.setCreateUser(Constant.SYS_DEFAULT_USER);  // 来源类型

                    //更新标的detail   业务还款计划相关Id
                    for(RepaymentProjPlanListDetail projPlanListDetail: projpDetials){
                        projPlanListDetail.setPlanListId(bizPlanList.getPlanListId());
                        projPlanListDetail.setPlanDetailId(bizpDetial.getPlanDetailId());
                    }

                }
            }
        }
    }



    /**
     * 根据表信息计算批次
     * @param tuandaiProjReqInfos
     * @return
     */
    private Map<String,List<ProjInfoReq>> getProjInfoReqMap(List<ProjInfoReq> tuandaiProjReqInfos){
        Map<String,List<ProjInfoReq>>  projInfoReqMap = new HashMap<>();
        for (ProjInfoReq projInfoReq:tuandaiProjReqInfos){
            Date beginTime = projInfoReq.getBeginTime();
            String beginDateStr =  DateUtil.formatDate(beginTime);
            List<ProjInfoReq>  batchProj =projInfoReqMap.get(beginDateStr);
            if(batchProj == null){
                batchProj = new LinkedList<>();
                projInfoReqMap.put(beginDateStr,batchProj);
            }
            batchProj.add(projInfoReq);
        }

        return projInfoReqMap;
    }






    /**
     * 计算标的还款计划信息
     * @param projInfoReqMap  输入的标出款信息
     * @param repaymentPlanListBatchMap  标的还款计划列表Map
     * @param RepaymentProjPlanListDetailBatchMap  标的还款计划详情Map
     * @param repaymentProjPlanMap  标的还款计划Map
     * @param plateType  平台类型
     * @param businessBasicInfo  业务基础信息
     * @param projPlanDetailTotalMap  Map<批次,Map<标还款计划主表Id，Map<标还款计划list表Id，标的还款计划detail列表>>
     * @param projPlanListTotalMap  Map<批次,Map<标还款计划主表Id，标的还款计划list列表>>
     */


    private void  calcProjRepayments(Map<String,List<ProjInfoReq>>projInfoReqMap,
                                     Map<String,Map<Integer,List<RepaymentProjPlanList>>>  repaymentPlanListBatchMap,
                                     Map<String,Map<Integer,Map<String,List<RepaymentProjPlanListDetail>>>>  RepaymentProjPlanListDetailBatchMap,
                                     Map<String,List<RepaymentProjPlan>> repaymentProjPlanMap,
                                     Integer plateType,
                                     BusinessBasicInfoReq  businessBasicInfo,
                                     Map<String,Map<String,Map<String,List<RepaymentProjPlanListDetail>>>> projPlanDetailTotalMap,
                                     Map<String,Map<String,List<RepaymentProjPlanList>>> projPlanListTotalMap){
        for(String beginDay:projInfoReqMap.keySet()){
            List<ProjInfoReq> reqList = projInfoReqMap.get(beginDay);
            //批次Id
            String batchId = UUID.randomUUID().toString();
            //期数对应的标List列表    以业务计
            //Map<期数，标还款计划费用详情列表>
            Map<Integer,List<RepaymentProjPlanList>>  repaymentPlanListPeriorMap = new HashMap<>();
            repaymentPlanListBatchMap.put(batchId,repaymentPlanListPeriorMap);

//            Map<标还款计划主表Id，标的还款计划list列表>>
            Map<String,List<RepaymentProjPlanList>> projPlanListPMap = new HashMap<>();
            projPlanListTotalMap.put(batchId,projPlanListPMap);

            //期数 费用类型对应的标ListDetail列表  以业务计
            //Map<期数，Map<费用类型fee_id,标还款计划费用详情列表>>
            Map<Integer,Map<String,List<RepaymentProjPlanListDetail>>>  repaymentProjPlanListDetailPeriorMap = new HashMap<>();
            RepaymentProjPlanListDetailBatchMap.put(batchId,repaymentProjPlanListDetailPeriorMap);

            //Map<标还款计划主表Id，Map<标还款计划list表Id，标的还款计划detail列表>>
            Map<String,Map<String,List<RepaymentProjPlanListDetail>>> projdetailListMap = new HashMap<>();
            projPlanDetailTotalMap.put(batchId,projdetailListMap);

            List<RepaymentProjPlan> projPlans = new LinkedList<>();
            repaymentProjPlanMap.put(batchId,projPlans);

            for(ProjInfoReq projInfoReq:reqList){
                List<RepaymentProjPlan> projList =  repaymentProjPlanService.selectList(new EntityWrapper<RepaymentProjPlan>().eq("project_id",projInfoReq.getProjectId()));

                if(projList.size()>0){
                    for(RepaymentProjPlan projPlan:projList){
                        if(projPlan.getActive().equals(RepayPlanActiveEnum.ACTIVE.getValue())){
                            Integer diffDays = DateUtil.getDiffDays(projPlan.getCreateTime(),projInfoReq.getQueryFullsuccessDate());
                            //如果同一个标的满标时间与还款计划生成的时间相差一天以内
                            if(diffDays <= 1){
                                throw  new CreatRepaymentExcepiton("已存在时间相近的还款计划");
                            }
                        }

                    }
                }

                ///////  标还款计划表   一次出款 生成一条记录
                RepaymentProjPlan repaymentProjPlan = new RepaymentProjPlan();
                repaymentProjPlan.setProjPlanId(UUID.randomUUID().toString());
                repaymentProjPlan.setProjectId(projInfoReq.getProjectId());
                repaymentProjPlan.setBusinessId(businessBasicInfo.getBusinessId());
                repaymentProjPlan.setOriginalBusinessId(businessBasicInfo.getOrgBusinessId());
                repaymentProjPlan.setRepaymentBatchId(batchId);  //还款计划批次号
                repaymentProjPlan.setPlanId("");   //业务还款计划ID
                repaymentProjPlan.setBorrowMoney(projInfoReq.getAmount());  //生成还款计划对应的借款总额
                repaymentProjPlan.setBorrowRate(projInfoReq.getRate());  //生成还款计划对应的借款利率
                repaymentProjPlan.setBorrowRateUnit(projInfoReq.getRateUnitType());//利率类型
                repaymentProjPlan.setBorrowLimit(projInfoReq.getPeriodMonth());//借款期限
                repaymentProjPlan.setBorrowLimitUnit(RepayPlanBorrowLimitUnitEnum.MONTH.getValue());//借款期限单位
                repaymentProjPlan.setPlanStatus(RepayPlanStatus.REPAYING.getKey());//还款计划状态
                repaymentProjPlan.setIsDefer(RepayPlanIsDeferEnum.NO.getValue());//是否展期还款计划
                repaymentProjPlan.setActive(RepayPlanActiveEnum.ACTIVE.getValue());//是否有效标志位
                repaymentProjPlan.setCreateTime(new Date());
                repaymentProjPlan.setCreateUser(Constant.SYS_DEFAULT_USER);
                repaymentProjPlan.setCreatSysType(RepayPlanCreateSysEnum.ALMS.getValue());
                repaymentProjPlan.setPlateType(plateType);

                projPlans.add(repaymentProjPlan);


                ///////  标的还款计划00期   一次性收取的费用信息  开始   ///////////////
                ////00期List 信息
                RepaymentProjPlanList  zeroList =  creatRepaymentProjPlanList(repaymentProjPlan,0);// new RepaymentProjPlanList();
                zeroList.setCurrentStatus(RepayPlanStatus.REPAYED.getName()); //当前还款状态  00期的直接置位为已还款
                //将标的00期写入还款计划map
                addPlanListToMap(repaymentPlanListPeriorMap,projPlanListPMap,zeroList, 0);


                List<ProjFeeReq> projFeeReqs =  projInfoReq.getProjFeeInfos();

                List<RepaymentProjPlanListDetail>  zeroListDetails = new LinkedList<>();

                //轮询标的费用项列表
                if(projFeeReqs !=null && projFeeReqs.size()>0){
                    for(ProjFeeReq feeReq: projFeeReqs){
                        //如果费用是一次性收取
                        if(feeReq.getIsOneTimeCharge().equals(RepayPlanIsOneTimeChargeEnum.ONE_TIME.getKey())){
                            String feeItemId = getFeeItemId(feeReq.getFeeItemId(),feeReq.getFeeType());
                            RepaymentProjPlanListDetail   zeroListDetail = creatProjListDetail(zeroList);
                            zeroListDetail.setProjPlanAmount(feeReq.getFeeValue());//项目计划应还总金额(元)
                            zeroListDetail.setFeeId(feeItemId);//资产端费用项ID，用于资产端区分同名的项目，若不存在同名费用项，可为空
                            zeroListDetail.setPlanItemName(feeReq.getFeeTypeName());//应还项目名称
                            zeroListDetail.setPlanItemType(feeReq.getFeeType());//应还项目所属分类
                            zeroListDetail.setAccountStatus(feeReq.getAccountStatus());//分账标记

                            //将第0期费用项添加到Map中
                            addDetialToMap(  repaymentProjPlanListDetailPeriorMap,
                                    projdetailListMap,
                                    feeItemId, zeroListDetail,0,
                                    zeroList.getProjPlanId());
                            //添加到第0期详情列表中
                            zeroListDetails.add(zeroListDetail);
                        }
                        else{
                            //不是一次性收取的话就计算出每一期需要交的费用项
                            String feeItemId = getFeeItemId(feeReq.getFeeItemId(),feeReq.getFeeType());
                        }
                    }
                }

                BigDecimal zeroProidTotol = new BigDecimal(0);
                for(RepaymentProjPlanListDetail detail:zeroListDetails){
                    zeroProidTotol.add(detail.getProjPlanAmount());
                }
                zeroList.setTotalBorrowAmount(zeroProidTotol);

                ///////  标的还款计划00期   一次性收取的费用信息  结束   ///////////////

                //////   标的其他期还款计划   按月收取费用信息   开始  ///////////////

                RepayPlanBorrowRateUnitEnum  rateUnitEnum = RepayPlanBorrowRateUnitEnum.getByKey(projInfoReq.getRateUnitType());
                RepayPlanRepayTypeEnum repayType = RepayPlanRepayTypeEnum.getByKey(projInfoReq.getRepayType());
                //计算每月应还本金利息
                Map<Integer,Map<String,BigDecimal>> repayPrinAndIni = calculateRepayPrinAndIni(
                        projInfoReq.getPeriodMonth(),projInfoReq.getFullBorrowMoney(),
                        projInfoReq.getRate(),rateUnitEnum,repayType );

                for(int i=1;i<projInfoReq.getPeriodMonth()+1;i++){
                    //还款计划详情项列表
                    List<RepaymentProjPlanListDetail>  priodListDetails = new LinkedList<>();
                    //创建还款计划list
                    RepaymentProjPlanList  projPlanList =  creatRepaymentProjPlanList(repaymentProjPlan,i);// new RepaymentProjPlanList();
                    Date date = DateUtil.addMonth2Date(i,projInfoReq.getBeginTime());
                    date = DateUtil.addDay2Date(-1,date);
                    projPlanList.setDueDate(date);
                    //将标的第i期写入还款计划map
                    addPlanListToMap(repaymentPlanListPeriorMap,projPlanListPMap,projPlanList, i);

                    //本金detail
                    RepaymentProjPlanListDetail   prinDetail = creatProjListDetail(projPlanList);
                    prinDetail.setProjPlanAmount(repayPrinAndIni.get(i).get(principal_str));//项目计划应还总金额(元)
                    prinDetail.setFeeId(RepayPlanItemTypeEnum.PRINCIPAL.getUuid());//资产端费用项ID，用于资产端区分同名的项目，若不存在同名费用项，可为空
                    prinDetail.setPlanItemName(RepayPlanItemTypeEnum.PRINCIPAL.getDesc());//应还项目名称
                    prinDetail.setPlanItemType(RepayPlanItemTypeEnum.PRINCIPAL.getValue());//应还项目所属分类
                    prinDetail.setAccountStatus(RepayPlanAccountStatusEnum.DIVISION_TO_PLAT.getValue());//分账标记

                    addDetialToMap(  repaymentProjPlanListDetailPeriorMap,
                            projdetailListMap,
                            RepayPlanItemTypeEnum.PRINCIPAL.getUuid(), prinDetail,i,
                            projPlanList.getProjPlanId() );
                    priodListDetails.add(prinDetail);

                    //利息
                    RepaymentProjPlanListDetail   iniDetail = creatProjListDetail(projPlanList);
                    iniDetail.setProjPlanAmount(repayPrinAndIni.get(i).get(interest_str));//项目计划应还总金额(元)
                    iniDetail.setFeeId(RepayPlanItemTypeEnum.INTEREST.getUuid());//资产端费用项ID，用于资产端区分同名的项目，若不存在同名费用项，可为空
                    iniDetail.setPlanItemName(RepayPlanItemTypeEnum.INTEREST.getDesc());//应还项目名称
                    iniDetail.setPlanItemType(RepayPlanItemTypeEnum.INTEREST.getValue());//应还项目所属分类
                    iniDetail.setAccountStatus(RepayPlanAccountStatusEnum.DIVISION_TO_PLAT.getValue());//分账标记

                    addDetialToMap(  repaymentProjPlanListDetailPeriorMap,
                            projdetailListMap,
                            RepayPlanItemTypeEnum.INTEREST.getUuid(), iniDetail,i,
                            projPlanList.getProjPlanId());
                    priodListDetails.add(iniDetail);


                    //列表费用
                    if(projFeeReqs !=null && projFeeReqs.size()>0) {
                        for (ProjFeeReq feeReq : projFeeReqs) {
                            if(feeReq.getIsOneTimeCharge().equals(RepayPlanIsOneTimeChargeEnum.BY_MONTH.getKey())){
                                RepaymentProjPlanListDetail   peroidFeelDetail = creatProjListDetail(projPlanList);
                                String feeItemId = getFeeItemId(feeReq.getFeeItemId(),feeReq.getFeeType());
                                peroidFeelDetail.setFeeId(feeItemId);//资产端费用项ID，用于资产端区分同名的项目，若不存在同名费用项，可为空
                                peroidFeelDetail.setPlanItemName(feeReq.getFeeTypeName());//应还项目名称
                                peroidFeelDetail.setPlanItemType(feeReq.getFeeType());//应还项目所属分类
                                peroidFeelDetail.setAccountStatus(feeReq.getAccountStatus());//分账标记
                                if(feeReq.getIsTermRange().equals(FeeIsTermRangeEnum.YES.getValue())){
                                    //是分段收费  需要从分段收费信息列表中
                                    List<ProjFeeDetailReq>  feeDetailReqs = feeReq.getProjFeeDetailInfos();
                                    if(feeDetailReqs==null||feeDetailReqs.size()==0){
                                        throw new CreatRepaymentExcepiton("分段收费的费用必须包含分段列表信息");
                                    }
                                    for(ProjFeeDetailReq  feeDetail: feeDetailReqs){
                                        if(i<=feeDetail.getFeeTermRangeMax() && i>=feeDetail.getFeeTermRangeMin()){
                                            peroidFeelDetail.setProjPlanAmount(feeDetail.getFeeValue());
                                        }
                                    }
                                }else{
                                    //不是分段收费直接存储应还总金额
                                    peroidFeelDetail.setProjPlanAmount(feeReq.getFeeValue());//项目计划应还总金额(元)
                                }
                                addDetialToMap(  repaymentProjPlanListDetailPeriorMap,
                                        projdetailListMap,
                                        feeItemId, peroidFeelDetail,i,
                                        projPlanList.getProjPlanId());
                                priodListDetails.add(peroidFeelDetail);

                            }
                        }
                    }

                    //计算这当前标这一期  总应还金额
                    BigDecimal proidTotol = new BigDecimal(0);
                    for(RepaymentProjPlanListDetail detail:priodListDetails){
                        proidTotol.add(detail.getProjPlanAmount());
                    }
                    projPlanList.setTotalBorrowAmount(zeroProidTotol);
                }

                //////   标的其他期还款计划   按月收取费用信息   结束  ///////////////




            }
        }
    }




    /**
     * 取得FeeID
     * @param intemId
     * @param feeType
     * @return
     */
    private String getFeeItemId(String intemId,Integer feeType){

        String feeItemId = intemId;

        if(feeItemId==null||feeItemId.equals("")){
            return RepayPlanItemTypeEnum.getByKey(feeType).getUuid();
        }

        return feeItemId;
    }



    /**
     * 把标的还款计划添加到Map中
     */
    private void addPlanListToMap(Map<Integer,List<RepaymentProjPlanList>>repaymentPlanListPeriorMap,
                                  Map<String,List<RepaymentProjPlanList>> projPlanListPMap,
                                  RepaymentProjPlanList peroidPlanList, Integer peroid ){

        //将标的每一期写入还款计划map
        List<RepaymentProjPlanList> planList = repaymentPlanListPeriorMap.get(peroid);
        if(planList == null){
            planList = new LinkedList<>();
            repaymentPlanListPeriorMap.put(peroid,planList);
        }

        List<RepaymentProjPlanList> pList1 = projPlanListPMap.get(peroidPlanList.getProjPlanId());
        if(pList1 == null){
            pList1 = new LinkedList<>();
            projPlanListPMap.put(peroidPlanList.getProjPlanId(),pList1);
        }

        planList.add(peroidPlanList);
        pList1.add(peroidPlanList);
    }


    /**
     * 把标的还款计划详情添加到Map中
     * @param RepaymentProjPlanListDetailPeriorMap
     * @param feeItemId
     * @param detail
     */
    private  void addDetialToMap(Map<Integer,Map<String,List<RepaymentProjPlanListDetail>>>  RepaymentProjPlanListDetailPeriorMap,
                                 Map<String,Map<String,List<RepaymentProjPlanListDetail>>> projdetailListMap,
                                 String feeItemId, RepaymentProjPlanListDetail detail,Integer period,String projPlanId){
        Map<String,List<RepaymentProjPlanListDetail>> feeIdMap = RepaymentProjPlanListDetailPeriorMap.get(period);
        if(feeIdMap == null){
            feeIdMap = new HashMap<>();
            RepaymentProjPlanListDetailPeriorMap.put(period,feeIdMap);
        }
        List<RepaymentProjPlanListDetail> pDetailList = feeIdMap.get(feeItemId);
        if(pDetailList == null){
            pDetailList = new LinkedList<>();
            feeIdMap.put(feeItemId,pDetailList);
        }

        Map<String,List<RepaymentProjPlanListDetail>>  projPlanIdMap = projdetailListMap.get(projPlanId);
        if(projPlanIdMap == null){
            projPlanIdMap = new HashMap<>();
            projdetailListMap.put(projPlanId,projPlanIdMap);
        }

        List<RepaymentProjPlanListDetail> projPListIdList = projPlanIdMap.get(detail.getProjPlanListId());
        if(projPListIdList == null){
            projPListIdList = new LinkedList<>();
            projPlanIdMap.put(detail.getProjPlanListId(),projPListIdList);
        }

        pDetailList.add(detail);
        projPListIdList.add(detail);

    }



    //创建标的还款计划，并设置基本信息
    private   RepaymentProjPlanList  creatRepaymentProjPlanList(RepaymentProjPlan repaymentProjPlan,Integer period){
        Boolean isRenew = false;
        if(!repaymentProjPlan.getBusinessId().equals(repaymentProjPlan.getOriginalBusinessId())){
            isRenew = true;
        }

        RepaymentProjPlanList  projPlanList = new RepaymentProjPlanList();
        projPlanList.setProjPlanListId(UUID.randomUUID().toString());
        projPlanList.setProjPlanId(repaymentProjPlan.getProjPlanId());
        projPlanList.setPlanListId("");//对应业务还款计划列表编号(外键，对应tb_repayment_biz_plan_list.plan_list_id)
        projPlanList.setPlanId(""); //所属还款计划编号(外键，对应tb_repayment_biz_plan.plan_id)
        projPlanList.setBusinessId(repaymentProjPlan.getBusinessId());  //还款计划所属业务编号(若当前业务为展期，则存展期业务编号)
        projPlanList.setOrigBusinessId(repaymentProjPlan.getOriginalBusinessId());  //还款计划所属原业务编号
        projPlanList.setPeriod(period);  //还款计划期数
        projPlanList.setAfterId(calcAfterId(repaymentProjPlan.getBusinessId(),period,isRenew)); // 总批次期数，  核对原来信贷还款计划是怎么写的
        projPlanList.setDueDate(new Date()); //应还日期 怎么设置， 需要核对
        projPlanList.setTotalBorrowAmount(new BigDecimal(0));// 总计划应还金额   需要按照每一项计算
        projPlanList.setOverdueAmount(new BigDecimal(0)); //总应还滞纳金
        projPlanList.setOverdueDays(new BigDecimal(0)); //逾期天数
        projPlanList.setCurrentStatus(RepayPlanStatus.REPAYING.getName()); //当前还款状态
        projPlanList.setRepayFlag(RepayPlanPayedTypeEnum.GET_MONEY_PAY.getValue()); //已还款类型标记
        projPlanList.setActive(RepayPlanActiveEnum.ACTIVE.getValue());//设置是否有效标志位
        projPlanList.setCreateTime(new Date());//设置创建时间
        projPlanList.setCreateUser(Constant.SYS_DEFAULT_USER);//设置创建用户
        projPlanList.setCreatSysType(repaymentProjPlan.getCreatSysType()); //创建系统标志
        projPlanList.setPlateType(repaymentProjPlan.getPlateType()); //平台类型标志
//        projPlanList.setP

        return projPlanList;
    }

    /**
     * 计算还款计划的afterId
     * @param businessId
     * @param period
     * @param isRenew
     * @return
     */
    private  String  calcAfterId(String businessId,Integer period,boolean isRenew){

         List<RepaymentBizPlan> bizPlans =  RepaymentBizPlanService.selectList(new EntityWrapper<RepaymentBizPlan>().eq("business_id",businessId));
         Integer size = bizPlans.size();
         size++;

        String periodStr=(new DecimalFormat("00")).format(period);
         String  afterId;
         if(isRenew){
            afterId = size+"-ZQ-"+periodStr;
         }else{
             afterId = size+"-"+periodStr;
         }

        return afterId;
    }


    /**
     * 计算还款计划每月应还本金和利息
     * @param periodMonth   还款月数
     * @param fullBorrowMoney  总的借款金额
     * @param rate    利率
     * @param rateUnit  利率单位
     * @param repayType  还款方式
     * @return
     * Map包括两个字段： principal   本金； interest  利息
     */
    private Map<Integer,Map<String,BigDecimal>> calculateRepayPrinAndIni(
            Integer periodMonth,BigDecimal fullBorrowMoney,
            BigDecimal rate,RepayPlanBorrowRateUnitEnum rateUnit,
            RepayPlanRepayTypeEnum repayType
            ){

        Map<Integer,Map<String,BigDecimal>>  retMap  = new HashMap<>();

        BigDecimal monthRate = getMonthRate(rate,rateUnit);

        switch (repayType){
            case PRINCIPAL_LAST:  //先息后本
                calcPrincipalLast(fullBorrowMoney,monthRate
                ,periodMonth,retMap);
                break;
            case INT_AND_PRIN_EQUAL://等额本息
                calcIntAndPrinEqual(fullBorrowMoney,monthRate
                        ,periodMonth,retMap);
                break;
            case INT_AND_PRIN_EVERYTIME:   //分期还本付息
                calcintAndPrinEverytime(fullBorrowMoney,monthRate
                        ,periodMonth,retMap);
                break;
            case INT_AND_PRIN_AVERAGE:  //等本等息
                calcIntAndPrinAverage(fullBorrowMoney,monthRate
                        ,periodMonth,retMap);
                break;
        }
        return retMap;
    }

    /**
     * 计算先息后本 每月本金 利息列表
     * @param fullBorrowMoney
     * @param monthRate
     * @param periodMonth
     * @param retMap
     */
    private void calcPrincipalLast(BigDecimal fullBorrowMoney,BigDecimal monthRate,
                                   Integer periodMonth ,Map<Integer,Map<String,BigDecimal>>  retMap )
    {
        //每月利息
        BigDecimal interest =  fullBorrowMoney.multiply(monthRate);
        for (int i=0;i<periodMonth;i++){
            //每月本金
            BigDecimal priciple = new BigDecimal(0);
            //如果是最后一期
            if(i==periodMonth-1){
                priciple = fullBorrowMoney;
            }
            addPAndIToList(priciple,interest,retMap,i);
        }
    }

    /**
     * 等额本息 每月本金  利息列表
     * @param fullBorrowMoney
     * @param monthRate
     * @param periodMonth
     * @param retList
     */
    private void calcIntAndPrinEqual(BigDecimal fullBorrowMoney,BigDecimal monthRate,
                                     Integer periodMonth ,Map<Integer,Map<String,BigDecimal>>  retList
                                    ){
//先计算 每期总应还   再计算 应还本金   再相减得出应还利息
        //最后一期  应还本金 为总应还本金 - 之前期已还本金总和

        //每月总应还
        //     应还=贷款金额*月利率*(1+月利率)^贷款期限/(((1+月利率)^贷款期限)-1)
        BigDecimal rateMi = monthRate.add(new BigDecimal(1)).pow(periodMonth);

        BigDecimal monthTotalRepay =  fullBorrowMoney.multiply(monthRate).multiply(rateMi)
                .divide((rateMi.subtract(new BigDecimal(1))),smallNum,roundingMode);
//        BigDecimal monthTotalRepay =  fullBorrowMoney.multiply(monthRate).multiply(monthRate.add(new BigDecimal(1))).pow(periodMonth)
//                .divide(((monthRate.add(new BigDecimal(1))).pow(periodMonth).subtract(new BigDecimal(1))));

        //之前期已还金额
        BigDecimal payedPriciple = new BigDecimal(0);

        for(int i=0;i<periodMonth;i++){
            //不是最后一期
            BigDecimal priciple = new BigDecimal(0);
            if(i!=periodMonth-1){
//                    本金=贷款金额*月利率*（1+月利率）^(第N次还款-1)/（(（1+月利率）^贷款期限)-1）
                BigDecimal rateNtimes = monthRate.add(new BigDecimal(1)).pow(i+1-1);
                priciple = fullBorrowMoney.multiply(monthRate).multiply(rateNtimes)
                        .divide((rateMi.subtract(new BigDecimal(1))),smallNum,roundingMode);
                payedPriciple = payedPriciple.add(priciple);
            }else{
                //是最后一期
                //本金
                priciple = fullBorrowMoney.subtract(payedPriciple);
            }
            //利息
            BigDecimal interest = monthTotalRepay.subtract(priciple);
            addPAndIToList(priciple,interest,retList,i);
        }
    }

    /**
     *  计算分期还本付息 每月本金  利息列表
     * @param fullBorrowMoney
     * @param monthRate
     * @param periodMonth
     * @param retList
     */
    private void calcintAndPrinEverytime(BigDecimal fullBorrowMoney,BigDecimal monthRate,
                                         Integer periodMonth ,Map<Integer,Map<String,BigDecimal>>  retList
            ){
        //每月应还本金
        BigDecimal priciple = fullBorrowMoney.divide(new BigDecimal(periodMonth),smallNum,roundingMode);
        //前期已还本金
        BigDecimal payedPriciple = new BigDecimal(0);
        for(int i=0;i<periodMonth;i++){
            //每月应还利息
            BigDecimal interest = (fullBorrowMoney.subtract(payedPriciple)).multiply(monthRate);
            addPAndIToList(priciple,interest,retList,i);
            payedPriciple = payedPriciple.add(priciple);
        }

    }

    /**
     *  计算等本等息 每月本金 利息列表
     * @param fullBorrowMoney
     * @param monthRate
     * @param periodMonth
     * @param retMap
     */
    private void calcIntAndPrinAverage(BigDecimal fullBorrowMoney,BigDecimal monthRate,
                                       Integer periodMonth ,Map<Integer,Map<String,BigDecimal>>  retMap
                                   ){
        //每月利息
        BigDecimal interest =  fullBorrowMoney.multiply(monthRate);
        //每月本金
        BigDecimal priciple = fullBorrowMoney.divide(new BigDecimal(periodMonth),smallNum,roundingMode);
        for (int i=0;i<periodMonth;i++){
            addPAndIToList(priciple,interest,retMap,i);
        }
    }




    /**
     * 把本金和利息添加到列表中
     * @param priciple 每月本金
     * @param interest 每月利息
     * @param retList  本金和利息列表
     */
    private void addPAndIToList(BigDecimal priciple,BigDecimal interest,Map<Integer,Map<String,BigDecimal>>  retList,Integer peroid ){
        Map<String,BigDecimal> monthMap = new HashMap<>();
        monthMap.put(principal_str,priciple);
        monthMap.put(interest_str,interest);
        retList.put(peroid+1,monthMap);
    }

    /**
     * 计算出月利率
     * @param rate
     * @param rateUnit
     * @return
     */
    private BigDecimal getMonthRate(BigDecimal rate,RepayPlanBorrowRateUnitEnum rateUnit){
        BigDecimal monthRate;
        switch (rateUnit){
            case YEAR_RATE:
                monthRate = rate.divide(new BigDecimal(12));
                break;
            case MONTH_RATE:
                monthRate = rate;
                break;
            case DAY_RATE:
                monthRate = rate.multiply(new BigDecimal(30));
                break;
            default:
                monthRate = rate;
                break;
        }
        return monthRate.divide(new BigDecimal(100));
    }


    /**
     * 创建标还款计划详情
     * @param projPlanList
     * @return
     */
    private RepaymentProjPlanListDetail creatProjListDetail(RepaymentProjPlanList projPlanList){
        RepaymentProjPlanListDetail   projPlanListDetail = new RepaymentProjPlanListDetail();
        projPlanListDetail.setProjPlanDetailId(UUID.randomUUID().toString());
        projPlanListDetail.setProjPlanListId(projPlanList.getProjPlanListId());
        projPlanListDetail.setPlanDetailId("");//  所属还款计划列表详情ID(外键，tb_repayment_biz_plan_list_detail.plan_detail_id)
        projPlanListDetail.setPlanListId("");//  所属还款计划列表ID(外键，对应tb_repayment_biz_plan_list.plan_list_id)
        projPlanListDetail.setBusinessId(projPlanList.getBusinessId());//  所属还款计划列表详情ID(外键，对应tb_repayment_biz_plan_list.plan_list_id)
        projPlanListDetail.setPeriod(projPlanList.getPeriod()); //所属期数
        projPlanListDetail.setActive(RepayPlanActiveEnum.ACTIVE.getValue());
        projPlanListDetail.setCreateDate(new Date());
        projPlanListDetail.setCreateUser(Constant.SYS_DEFAULT_USER);
        projPlanListDetail.setCreatSysType(projPlanList.getCreatSysType()); //创建系统标志
        projPlanListDetail.setPlateType(projPlanList.getPlateType()); //平台类型标志
        //分润顺序项
        Integer shareProfitIndex =  profitItemSetService.getLevel(projPlanListDetail.getBusinessId(),projPlanListDetail.getPlanItemType(),projPlanListDetail.getFeeId())
                .get("feeLevel");
        projPlanListDetail.setShareProfitIndex(shareProfitIndex);
        return projPlanListDetail;
    }



    public static void main(String[] args) {


//        System.out.println(UUID.randomUUID().toString());

        String periodStr=(new DecimalFormat("00")).format(120);
        System.out.println(periodStr);

        //---------  还款本息   测试   开始----------//
//        Integer periodMonth = 12;
//        BigDecimal fullBorrowMoney = new BigDecimal(120000);
//        BigDecimal rate =new BigDecimal(12);
//        RepayPlanBorrowRateUnitEnum rateUnit = RepayPlanBorrowRateUnitEnum.YEAR_RATE;
////        RepayPlanRepayTypeEnum repayType = RepayPlanRepayTypeEnum.INT_AND_PRIN_AVERAGE;//等本等息
////        RepayPlanRepayTypeEnum repayType = RepayPlanRepayTypeEnum.PRINCIPAL_LAST;//先息后本
//        RepayPlanRepayTypeEnum repayType = RepayPlanRepayTypeEnum.INT_AND_PRIN_EQUAL; //等额本息
////        RepayPlanRepayTypeEnum repayType = RepayPlanRepayTypeEnum.INT_AND_PRIN_EVERYTIME; //分期还本付息
//
//        CreatRepayPlanServiceImpl impl = new CreatRepayPlanServiceImpl();
//
//        Map<Integer,Map<String,BigDecimal>> map = impl.calculateRepayPrinAndIni(periodMonth,fullBorrowMoney,rate,rateUnit,repayType);
//
//        System.out.println(JSON.toJSONString(map));

        //---------  还款本息   测试   结束----------//
        //
//        BigDecimal big1 = new BigDecimal(100);
//        BigDecimal big2 = new BigDecimal(50);
//        System.out.println(big2.divide(big1));
    }
}
