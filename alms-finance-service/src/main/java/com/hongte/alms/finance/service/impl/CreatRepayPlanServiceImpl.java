package com.hongte.alms.finance.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.RepayPlan.dto.*;
import com.hongte.alms.base.baseException.CreatRepaymentExcepiton;
import com.hongte.alms.base.entity.*;
import com.hongte.alms.base.enums.*;
import com.hongte.alms.base.enums.repayPlan.RepayPlanStatus;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.enums.repayPlan.*;
import com.hongte.alms.base.service.*;
import com.hongte.alms.common.util.ClassCopyUtil;
import com.hongte.alms.base.RepayPlan.req.*;
import com.hongte.alms.finance.service.CreatRepayPlanService;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.StringUtil;

import org.apache.commons.collections.CollectionUtils;
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
import java.util.concurrent.Executor;

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
    @Qualifier("RepaymentProjPlanListService")
    RepaymentProjPlanListService  repaymentProjPlanListService;

    @Autowired
    @Qualifier("RepaymentProjPlanListDetailService")
    RepaymentProjPlanListDetailService repaymentProjPlanListDetailService;

    @Autowired
    @Qualifier("RepaymentBizPlanService")
    RepaymentBizPlanService repaymentBizPlanService;

    @Autowired
    @Qualifier("RepaymentBizPlanListService")
    RepaymentBizPlanListService repaymentBizPlanListService;

    @Autowired
    @Qualifier("RepaymentBizPlanListDetailService")
    RepaymentBizPlanListDetailService repaymentBizPlanListDetailSevice;

    @Autowired
    @Qualifier("ProfitItemSetService")
    ProfitItemSetService profitItemSetService;

    @Autowired
    @Qualifier("BasicBusinessService")
    BasicBusinessService basicBusinessService;

    @Autowired
    @Qualifier("TuandaiProjectInfoService")
    TuandaiProjectInfoService tuandaiProjectInfoService;

    @Autowired
    Executor executor;

    @Autowired
    @Qualifier("IssueSendOutsideLogService")
    IssueSendOutsideLogService issueSendOutsideLogService;


    @Autowired
    @Qualifier("TuandaiProjectCarService")
    TuandaiProjectCarService tuandaiProjectCarService;

    @Autowired
    @Qualifier("TuandaiProjectHouseService")
    TuandaiProjectHouseService tuandaiProjectHouseService;

    @Autowired
    @Qualifier("BasicBizCustomerService")
    BasicBizCustomerService basicBizCustomerService;


//    @Autowired
//    @Qualifier("BaiscBizExtRateService")
//    BaiscBizExtRateService baiscBizExtRateService;


    @Autowired
    @Qualifier("BasicBusinessTypeService")
    BasicBusinessTypeService basicBusinessTypeService;
    
    @Autowired
    @Qualifier("RepaymentProjFactRepayService")
    private RepaymentProjFactRepayService repaymentProjFactRepayService;


    @Autowired
    @Qualifier("ProjExtRateService")
    private  ProjExtRateService projExtRateService;



//    @Autowired
//    Validator globalValidator;

    //进位方式枚举
    private  RoundingMode roundingMode=RoundingMode.UP;
    //保留的小数位数
    public Integer smallNum=4;

    //本金字符串
    private static final  String principal_str = "principal";
    //利息字符串
    private static final  String interest_str = "interest";



    @Override
    @Transactional(rollbackFor = Exception.class)
    public PlanReturnInfoDto creatRepayPlan(CreatRepayPlanReq creatRepayPlanReq) throws InstantiationException, IllegalAccessException {
        PlanReturnInfoDto planReturnInfoDto = new PlanReturnInfoDto();

        List<RepaymentBizPlanDto>  retList = new LinkedList<>();
        planReturnInfoDto.setRepaymentBizPlanDtos(retList);



//        List<CarBusinessAfterDto>  carBizAfterList = new LinkedList<>();
//        planReturnInfoDto.setCarBusinessAfterDtoList(carBizAfterList);
        //设置进位方式枚举和保留的小数位数
        smallNum = creatRepayPlanReq.getSmallNum();
        if(creatRepayPlanReq.getRondmode()==null){
            logger.error("请传入进位方式："+JSON.toJSONString(creatRepayPlanReq));
            throw  new CreatRepaymentExcepiton("请传入进位方式：rondmode");
        }
        roundingMode = RoundingMode.valueOf(creatRepayPlanReq.getRondmode());

        //1、根据业务和标的对应关系，判断出需要生成几个还款计划，每个还款计划相对于业务总金额占的比例
        // 1))业务基本信息
        BusinessBasicInfoReq businessBasicInfo = creatRepayPlanReq.getBusinessBasicInfoReq();

        List<ProjInfoReq> tuandaiProjReqInfos = creatRepayPlanReq.getProjInfoReqs();

        //计算每个标 在占整个业务的比例
        Map<String,BigDecimal>  projPersent = new HashMap<>();
        Integer  projIndex =0;
        BigDecimal setedPersent = new BigDecimal(0);
        for(ProjInfoReq projInfoReq:tuandaiProjReqInfos){
            projIndex++;
            //如果是最后一个，则用减的
            if(projIndex.equals(tuandaiProjReqInfos.size())){
                BigDecimal persent = new BigDecimal(1).subtract(setedPersent);
                projPersent.put(projInfoReq.getProjectId(),persent);
            }else{
                BigDecimal persent = projInfoReq.getFullBorrowMoney().divide(businessBasicInfo.getBorrowMoney(),10,roundingMode);
                setedPersent = setedPersent.add(persent);
                projPersent.put(projInfoReq.getProjectId(),persent);
            }
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
                businessBasicInfo,
                projPlanDetailTotalMap,
                projPlanListTotalMap,creatRepayPlanReq);
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
            retList.add(planDto);
            planDto.setRepaymentBizPlan(bizPlanMap.get(batchId));

            List<RepaymentBizPlanList> bizPlanLists =bizPlanListMap.get(batchId);
            Map<String,List<RepaymentBizPlanListDetail>>  bizDetailMaps = bizPlanListDetialMap.get(batchId);
            List<RepaymentBizPlanListDto>  bizPlanListDtos = new LinkedList<>();
            planDto.setBizPlanListDtos(bizPlanListDtos);
            //业务还款计划list
            for (RepaymentBizPlanList bizPlanList:bizPlanLists){
                RepaymentBizPlanListDto bizPlanListDto = new RepaymentBizPlanListDto();
                bizPlanListDto.setRepaymentBizPlanList(bizPlanList);
                bizPlanListDtos.add(bizPlanListDto);
                //业务还款计划detail
                List<RepaymentBizPlanListDetail> bizPlanListDetails = bizDetailMaps.get(bizPlanList.getPlanListId());
                bizPlanListDto.setBizPlanListDetails(bizPlanListDetails);

            }

            //标还款计划列表
            List<RepaymentProjPlan> projPlans = repaymentProjPlanMap.get(batchId);
            List<RepaymentProjPlanDto>  projPlanDtos = new LinkedList<>();
            planDto.setProjPlanDtos(projPlanDtos);

            //标还款计划List Map
            Map<String,List<RepaymentProjPlanList>> projPListMap = projPlanListTotalMap.get(batchId);

            //标还款计划Detail Map
            Map<String,Map<String,List<RepaymentProjPlanListDetail>>>  pPlanDMap = projPlanDetailTotalMap.get(batchId);

//            Map<String,Map<String,Map<String,List<RepaymentProjPlanListDetail>>>> projPlanDetailTotalMap = new HashMap<>();


            for(RepaymentProjPlan projPlan :projPlans){
                RepaymentProjPlanDto projPlanDto = new RepaymentProjPlanDto();
                projPlanDtos.add(projPlanDto);
                projPlanDto.setRepaymentProjPlan(projPlan);

                List<RepaymentProjPlanList>  projPlanLists =  projPListMap.get(projPlan.getProjPlanId());
                List<RepaymentProjPlanListDto> projPlanListDtos = new LinkedList<>();
                projPlanDto.setProjPlanListDtos(projPlanListDtos);

                for(RepaymentProjPlanList projPlanList: projPlanLists){
                    RepaymentProjPlanListDto  projPlanListDto  = new RepaymentProjPlanListDto();
                    projPlanListDtos.add(projPlanListDto);

                    projPlanListDto.setRepaymentProjPlanList(projPlanList);

                    List<RepaymentProjPlanListDetail> projPlanListDetails = pPlanDMap.get(projPlan.getProjPlanId()).get(projPlanList.getProjPlanListId());
                    projPlanListDto.setProjPlanListDetails(projPlanListDetails);

                }
            }
        }


        //整理成信贷的还款计划格式
        List<XdPlanDto> xdPlanDtos = xdPlanDtoHandle(retList, businessBasicInfo);
        planReturnInfoDto.setXdPlanDtos(xdPlanDtos);

        //////  整理成 返回数据的格式  将信息返回出去  结束  ///////////



        return planReturnInfoDto;
    }

    /**
     * 根据贷后的还款计划，计算信贷的还款计划
     * @param retList
     * @param businessBasicInfo
     * @return
     */
	private List<XdPlanDto> xdPlanDtoHandle(List<RepaymentBizPlanDto> retList, BusinessBasicInfoReq businessBasicInfo) {
		List<XdPlanDto> xdPlanDtos = new LinkedList<>();
		
		for(RepaymentBizPlanDto bizPlanDto:retList) {
            RepaymentBizPlan repaymentBizPlan = bizPlanDto.getRepaymentBizPlan();
            List<RepaymentBizPlanListDto> repaymentBizPlanListDtos = bizPlanDto.getBizPlanListDtos();
            XdPlanDto xdPlanDto = new XdPlanDto();
            xdPlanDtos.add(xdPlanDto);
            List<CarBusinessAfterDto> bizAfterDtos = new LinkedList<>();
            xdPlanDto.setBatchUUid(repaymentBizPlan.getRepaymentBatchId());
            List<String> projIds= new LinkedList<>();
            xdPlanDto.setProjectIds(projIds);
            List<RepaymentProjPlanDto> projPlanDtos = bizPlanDto.getProjPlanDtos();
            for(RepaymentProjPlanDto repaymentProjPlanDto: projPlanDtos){
                projIds.add(repaymentProjPlanDto.getRepaymentProjPlan().getProjectId());
            }

            xdPlanDto.setCarBusinessAfterDtoList(bizAfterDtos);

            //排序 由低到高
            Collections.sort(repaymentBizPlanListDtos, new Comparator<RepaymentBizPlanListDto>() {
                @Override
                public int compare(RepaymentBizPlanListDto o1, RepaymentBizPlanListDto o2) {
                    return o1.getRepaymentBizPlanList().getPeriod().compareTo(o2.getRepaymentBizPlanList().getPeriod());

//                    if(o1.getBeginPeroid().compareTo(o2.getBeginPeroid())==0){
//                        return Integer.valueOf(o1.getBeginPeroid().compareTo(o2.getBeginPeroid()));
//                    }else{
//                        return o1.getBeginPeroid().compareTo(o2.getBeginPeroid());
//                    }
                }
            });

            BigDecimal payedPrincial = new BigDecimal(0);

            for(RepaymentBizPlanListDto repaymentBizPlanListDto:repaymentBizPlanListDtos){
                RepaymentBizPlanList repaymentBizPlanList = repaymentBizPlanListDto.getRepaymentBizPlanList();
                List<RepaymentBizPlanListDetail> repaymentBizPlanListDetails = repaymentBizPlanListDto.getBizPlanListDetails();
                CarBusinessAfterDto bizAfterDto = new CarBusinessAfterDto();
                bizAfterDtos.add(bizAfterDto);
                bizAfterDto.setCarBusinessId(repaymentBizPlan.getOriginalBusinessId());//业务id
                bizAfterDto.setCarBusinessAfterId(repaymentBizPlanList.getAfterId());//[当前还款期数]
                BasicBusinessType basicBusinessType =basicBusinessTypeService.selectById(businessBasicInfo.getBusinessType());
                if(basicBusinessType ==null){
                    logger.error("业务类型在 BasicBusinessType 表中不存在 业务信息："+JSON.toJSONString(businessBasicInfo));
                    throw  new CreatRepaymentExcepiton("业务类型不存在  业务类型："+businessBasicInfo.getBusinessType());
                }
//                bizAfterDto.setParatype(basicBusinessType.getBusinessTypeName());
//                bizAfterDto.setCustomerName(businessBasicInfo.getCustomerName());
                bizAfterDto.setOperatorName(businessBasicInfo.getOperatorName()); //业务主办人
                bizAfterDto.setOperatorDept(businessBasicInfo.getCompanyId()); //业务主办人部门
//                bizAfterDto.setCreateTime(new Date()); //新建时间
                bizAfterDto.setRepaymentType(RepayPlanRepayIniCalcWayEnum.getByKey(businessBasicInfo.getRepaymentTypeId()).getName()); //还款方式
                bizAfterDto.setBorrowMoney(repaymentBizPlan.getBorrowMoney().toPlainString());//借款金额
                bizAfterDto.setOddcorpus(repaymentBizPlan.getBorrowMoney().subtract(payedPrincial).toPlainString());//剩余本金

                //本期应还本金
                BigDecimal currentPrinciple = null;

                //本期应还利息
                BigDecimal currentAccrual = null;
                //本期应还分公司服务费
                BigDecimal currentSerivceFee = new BigDecimal(0).setScale(smallNum ,  roundingMode);;
                BigDecimal otherFee = new BigDecimal(0).setScale(smallNum ,  roundingMode);
                for(RepaymentBizPlanListDetail repaymentBizPlanListDetail:repaymentBizPlanListDetails){
                    if(repaymentBizPlanListDetail.getPlanItemType().equals(RepayPlanFeeTypeEnum.INTEREST.getValue())){
                        currentAccrual =repaymentBizPlanListDetail.getPlanAmount();
                    }else if(repaymentBizPlanListDetail.getPlanItemType().equals(RepayPlanFeeTypeEnum.PRINCIPAL.getValue())){
                        currentPrinciple = repaymentBizPlanListDetail.getPlanAmount();
                    }else if(repaymentBizPlanListDetail.getPlanItemType().equals(RepayPlanFeeTypeEnum.SUB_COMPANY_CHARGE.getValue())){//分公司服务费，归到服务费
                        currentSerivceFee = currentSerivceFee.add(repaymentBizPlanListDetail.getPlanAmount());
                    }else if(repaymentBizPlanListDetail.getPlanItemType().equals(RepayPlanFeeTypeEnum.PLAT_CHARGE.getValue())){//平台月收服务费，归到服务费
                        currentSerivceFee = currentSerivceFee.add(repaymentBizPlanListDetail.getPlanAmount());
                    }
                    else {
                        otherFee = otherFee.add(repaymentBizPlanListDetail.getPlanAmount());
                    }
                }
                if(currentPrinciple == null){
                    logger.error("找不到本期应还本金 ："+JSON.toJSONString(repaymentBizPlanListDto));
                    throw  new CreatRepaymentExcepiton("找不到本期应还本金 ："+JSON.toJSONString(repaymentBizPlanListDto));

                }
                if(currentAccrual  == null){
                    logger.error("找不到本期应还利息 ："+JSON.toJSONString(repaymentBizPlanListDto));
                    throw  new CreatRepaymentExcepiton("找不到本期应还利息 ："+JSON.toJSONString(repaymentBizPlanListDto));
                }

                bizAfterDto.setCurrentPrincipa(currentPrinciple);//本期应还本金
                bizAfterDto.setCurrentAccrual(currentAccrual.toPlainString());//本期应还利息
                bizAfterDto.setBorrowDate(repaymentBizPlanList.getDueDate());//还款日期
                bizAfterDto.setCarBusinessAfterType("还款中");//[还款状态分类]：还款中，已还款，逾期
                bizAfterDto.setRepayService(currentSerivceFee);//本期应还分公司服务费
//                bizAfterDto.setOtherMoney(otherFee.toPlainString()); //其他费用(这是实还值)
                bizAfterDto.setCurrentOtherMoney(otherFee); //其他费用（这是应还值）
//                bizAfterDto.setCreatedate(new Date());
                bizAfterDto.setRepayedFlag(0);
                bizAfterDto.setReserve2("还款中");
//                bizAfterDto.setConfirmFlag(0);
//                bizAfterDto.setAccountantConfirmStatus(0);
//                bizAfterDto.setTuandaiAdvanceStatus(0);
//                bizAfterDto.setTuandaiProfitStatus(0);
//                bizAfterDto.setTuandaiDistributeFundStatus(0); // 资金充值状态
//                bizAfterDto.setIssueAfterType("未还款"); // 平台还款状态
                bizAfterDto.setBusinessAfterGuid(repaymentBizPlan.getRepaymentBatchId()); // 还款计划guid
                bizAfterDto.setInterestPaid(0); // 本息还款状态

                List<CarBusinessAfterDetailDto>  bizAfterDetailDtos = new LinkedList<>();
                bizAfterDto.setCarBizDetailDtos(bizAfterDetailDtos);
                for(RepaymentBizPlanListDetail bizPlanListDetail: repaymentBizPlanListDetails){
                    CarBusinessAfterDetailDto afterDetailDto = new CarBusinessAfterDetailDto();
                    bizAfterDetailDtos.add(afterDetailDto);

                    afterDetailDto.setBusinessId(bizAfterDto.getCarBusinessId());//业务编号
                    afterDetailDto.setBusinessAfterId(bizAfterDto.getCarBusinessAfterId());//期数
                    afterDetailDto.setFeeId(bizPlanListDetail.getFeeId());//费用项ID
                    afterDetailDto.setFeeName(bizPlanListDetail.getPlanItemName());//费用名称
                    afterDetailDto.setAfterFeeType(RepayPlanFeeTypeEnum.getByKey(bizPlanListDetail.getPlanItemType()).getXd_value());//费用对应贷后主表的分类ID
                    afterDetailDto.setPlanFeeValue(bizPlanListDetail.getPlanAmount());//本期应还金额
                    afterDetailDto.setPlanRepaymentDate(bizPlanListDetail.getDueDate());//应还日期
//                    afterDetailDto.setCreateTime(new Date());//创建日期

                }
                //计算已还的本金
                payedPrincial = payedPrincial.add(currentPrinciple);
            }









        }
		return xdPlanDtos;
	}

    @Override
    @Transactional(rollbackFor = Exception.class)
    public PlanReturnInfoDto creatAndSaveRepayPlan(CreatRepayPlanReq creatRepayPlanReq) throws IllegalAccessException, InstantiationException {
        //异步存储调用此接口传入的参数
        executor.execute(new Runnable() {
            @Override
            public void run() {
                IssueSendOutsideLog log=new IssueSendOutsideLog();
                log.setCreateTime(new Date());
                log.setCreateUserId(Constant.ADMIN_ID);
                log.setInterfacecode("CreatRepayPlanService_creatAndSaveRepayPlan");
                log.setInterfacename("创建并存储还款计划");
                log.setSendJsonEncrypt(creatRepayPlanReq.getBusinessBasicInfoReq().getBusinessId());
                log.setSendJson(JSON.toJSONString(creatRepayPlanReq));
                log.setReturnJson(null);
//                log.setReturnJsonDecrypt(JSON.toJSONString(dtos));
                log.setSystem("");
                log.setSendUrl("");

                issueSendOutsideLogService.insert(log);
            }
        });

        //判断是否重传
        checkResave(creatRepayPlanReq);

        //   输入数据校验
        checkInputRep(creatRepayPlanReq);

        // 计算还款计划
        PlanReturnInfoDto  planReturnInfoDto = creatRepayPlan(creatRepayPlanReq);
        List<RepaymentBizPlanDto>  dtos = planReturnInfoDto.getRepaymentBizPlanDtos();

        /////  存储还款计划相关信息   开始  ////////////////
        for(RepaymentBizPlanDto bizPlanDto:dtos){
            RepaymentBizPlan bizPlan = bizPlanDto.getRepaymentBizPlan();
            repaymentBizPlanService.insert(bizPlan);

            List<RepaymentBizPlanListDto> bizPlanListDtos = bizPlanDto.getBizPlanListDtos();
            for(RepaymentBizPlanListDto bizPlanListDto:bizPlanListDtos){
                RepaymentBizPlanList  bizPlanList = bizPlanListDto.getRepaymentBizPlanList();
                repaymentBizPlanListService.insert(bizPlanList);
                List<RepaymentBizPlanListDetail>  bizPlanListDetails = bizPlanListDto.getBizPlanListDetails();
                repaymentBizPlanListDetailSevice.insertBatch(bizPlanListDetails);
            }

            List<RepaymentProjPlanDto> projPlanDtos = bizPlanDto.getProjPlanDtos();
            for(RepaymentProjPlanDto projPlanDto:projPlanDtos){
                RepaymentProjPlan projPlan = projPlanDto.getRepaymentProjPlan();
                repaymentProjPlanService.insert(projPlan);

                List<RepaymentProjPlanListDto> projPlanListDtos = projPlanDto.getProjPlanListDtos();
                for(RepaymentProjPlanListDto projPlanListDto:projPlanListDtos){
                    RepaymentProjPlanList projPlanList = projPlanListDto.getRepaymentProjPlanList();
                    repaymentProjPlanListService.insert(projPlanList);
                    List<RepaymentProjPlanListDetail> repaymentProjPlanListDetails = projPlanListDto.getProjPlanListDetails();
                    repaymentProjPlanListDetailService.insertBatch(repaymentProjPlanListDetails);
                }
            }
        }
        /////  存储还款计划相关信息   开始  ////////////////

        /////  存储传入的相关信息  开始  //////////////

        BusinessBasicInfoReq  businessBasicInfoReq = creatRepayPlanReq.getBusinessBasicInfoReq();
        //存储业务信息
        BasicBusiness  basicBusiness = ClassCopyUtil.copy(businessBasicInfoReq,BusinessBasicInfoReq.class,BasicBusiness.class);
        basicBusiness.setBorrowLimitUnit(1);
        basicBusiness.setOutputPlatformId(1);//默认为团贷网p2p业务
        basicBusiness.setIsTuandaiRepay(1);//默认为使用平台还款
        basicBusiness.setAssetId("ht_xindai");//默认为鸿特信贷中心
        basicBusiness.setSrcType(RepayPlanCreateSysEnum.ALMS.getValue());
        BasicBusiness  oldBasicBusiness = basicBusinessService.selectOne(new EntityWrapper<BasicBusiness>().eq("business_id",basicBusiness.getBusinessId()));
        String isMainCustomerIdentityCard="";//主借款人身份证号码
        for(int i=0;i<creatRepayPlanReq.getBizCusInfoReqs().size();i++) {
        	if(creatRepayPlanReq.getBizCusInfoReqs().get(i).getIsmainCustomer()==1) {
        		isMainCustomerIdentityCard=creatRepayPlanReq.getBizCusInfoReqs().get(i).getIdentifyCard();
        	}
        	
        }
        if(oldBasicBusiness!=null){
            basicBusiness.setCreateUser(oldBasicBusiness.getCreateUser());
            basicBusiness.setCreateTime(oldBasicBusiness.getCreateTime());
            basicBusiness.setUpdateTime(new Date());
            basicBusiness.setCustomerIdentifyCard(isMainCustomerIdentityCard);
            basicBusiness.setUpdateUser(Constant.ADMIN_ID);
        }else{
            basicBusiness.setCreateUser(Constant.ADMIN_ID);
            basicBusiness.setCreateTime(new Date());
            basicBusiness.setCustomerIdentifyCard(isMainCustomerIdentityCard);
        }
        basicBusinessService.insertOrUpdate(basicBusiness);

        List<BusinessCustomerInfoReq> bizCusInfoReqs = creatRepayPlanReq.getBizCusInfoReqs();
        //存储业务客户信息
        List<BasicBizCustomer> bizCustomers = new LinkedList<>();
        //先删除旧的客户信息，再重新写入 zk 2018-7-10
        basicBizCustomerService.delete(new EntityWrapper<BasicBizCustomer>().eq("business_id",basicBusiness.getBusinessId()));
        for(BusinessCustomerInfoReq bCustInfo:bizCusInfoReqs){
            BasicBizCustomer bizCusInfo =  ClassCopyUtil.copy(bCustInfo,BusinessCustomerInfoReq.class,BasicBizCustomer.class);
            bizCusInfo.setBusinessId(basicBusiness.getBusinessId());
            bizCusInfo.setCreateUser(Constant.ADMIN_ID);
            bizCusInfo.setCreateTime(new Date());
//            List<BasicBizCustomer> customerList =  basicBizCustomerService.selectList(
//                    new EntityWrapper<BasicBizCustomer>().eq("business_id",businessBasicInfoReq.getBusinessId())
//                            .eq("customer_id",bCustInfo.getCustomerId()).eq("identify_card",bCustInfo.getIdentifyCard()));
//            if(customerList.size()>0){
//                BasicBizCustomer oldCustomerInfo =customerList.get(0);
//                bizCusInfo.setId(oldCustomerInfo.getId());
//            }
             basicBizCustomerService.insert(bizCusInfo);
        }
//        basicBizCustomerService.delete(new EntityWrapper<BasicBizCustomer>().eq("business_id",basicBusiness.getBusinessId()));
//        basicBizCustomerService.insertBatch(bizCustomers);

//        //存储业务额外费用信息
//        if(creatRepayPlanReq.getBizExtRateReqs()!=null){
//            List<BusinessExtRateReq> bizExtRateReqs = creatRepayPlanReq.getBizExtRateReqs();
//            List<BaiscBizExtRate> bizExtRates = new LinkedList<>();
//            for(BusinessExtRateReq rateReq: bizExtRateReqs){
//                BaiscBizExtRate bizExtRate =  ClassCopyUtil.copy(rateReq,BusinessExtRateReq.class,BaiscBizExtRate.class);
//                bizExtRate.setBusinessId(basicBusiness.getBusinessId());
//                bizExtRate.setCreateUser(Constant.ADMIN_ID);
//                bizExtRate.setCreateTime(new Date());
//
//                bizExtRates.add(bizExtRate);
//            }
//            baiscBizExtRateService.delete(new EntityWrapper<BaiscBizExtRate>().eq("business_id",basicBusiness.getBusinessId()));
//            baiscBizExtRateService.insertBatch(bizExtRates);
//        }

        List<ProjInfoReq>  projInfoReqs = creatRepayPlanReq.getProjInfoReqs();
        //存储标信息
        for(ProjInfoReq projInfoReq:projInfoReqs){
            TuandaiProjectInfo  projInfo = ClassCopyUtil.copy(projInfoReq,ProjInfoReq.class,TuandaiProjectInfo.class);
            projInfo.setBusinessId(basicBusiness.getBusinessId());
            projInfo.setQueryFullSuccessDate(projInfoReq.getQueryFullsuccessDate());
            projInfo.setBorrowLimit(basicBusiness.getBorrowLimit());

            //设置年化利率
            BigDecimal yearRate = getYearRate(projInfoReq.getRate(),RepayPlanBorrowRateUnitEnum.getByKey(projInfoReq.getRateUnitType()));
            projInfo.setInterestRate(yearRate);

            //设置标的batchId
            boolean setBatchFlage = false;
            for(RepaymentBizPlanDto bizPlanDto: dtos){
                List<RepaymentBizPlanListDto>  bizPlanListDtos = bizPlanDto.getBizPlanListDtos();
                for(RepaymentBizPlanListDto bizPlanListDto : bizPlanListDtos){
                    List<RepaymentProjPlanDto> projPlanDtos = bizPlanDto.getProjPlanDtos();
                    for(RepaymentProjPlanDto projPlanDto:projPlanDtos){
                        RepaymentProjPlan projPlan = projPlanDto.getRepaymentProjPlan();
                        if(projPlan.getProjectId().equals(projInfo.getProjectId())){
                            setBatchFlage = true;
                            projInfo.setBusinessAfterGuid(projPlan.getRepaymentBatchId());
                            break;
                        }
                    }
                    if(setBatchFlage) break;
                }
            }
            TuandaiProjectInfo  oldProjInfp =tuandaiProjectInfoService.selectOne(new EntityWrapper<TuandaiProjectInfo>().eq("project_id",projInfo.getProjectId()));




            if(oldProjInfp!=null){
                projInfo.setCreateUser(oldProjInfp.getCreateUser());
                projInfo.setCreateTime(oldProjInfp.getCreateTime());
                projInfo.setUpdateTime(new Date());
                projInfo.setUpdateUser(Constant.ADMIN_ID);
            }else{
                projInfo.setCreateUser(Constant.ADMIN_ID);
                projInfo.setCreateTime(new Date());
            }

            tuandaiProjectInfoService.insertOrUpdate(projInfo);

            //如果有车辆信息，存储车辆信息
            if(projInfoReq.getIsHaveCar().equals(BooleanEnum.YES.getValue())){
                List<ProjectCarInfoReq> projectCarInfoReqs = projInfoReq.getProjCarInfos();

                String projectId = projInfo.getProjectId();
                List<TuandaiProjectCar> tuandaiProjectCars = new LinkedList<>();
                for (ProjectCarInfoReq  pCarInfoReq:projectCarInfoReqs){
                    TuandaiProjectCar tuandaiProjectCar = ClassCopyUtil.copy(pCarInfoReq,ProjectCarInfoReq.class,TuandaiProjectCar.class);
                    tuandaiProjectCar.setProjectId(projInfo.getProjectId());
                    tuandaiProjectCar.setCreateTime(new Date());
                    tuandaiProjectCar.setCreateUser(Constant.ADMIN_ID);
                    tuandaiProjectCars.add(tuandaiProjectCar);
                }
                tuandaiProjectCarService.delete(new EntityWrapper<TuandaiProjectCar>().eq("project_id",projectId));
                tuandaiProjectCarService.insertBatch(tuandaiProjectCars);

            }
            //如果有房屋信息，存储房屋信息
            if(projInfoReq.getIsHaveHouse().equals(BooleanEnum.YES.getValue())){


                List<ProjectHouseInfoReq> projectHouseInfoReqs = projInfoReq.getProjHouseInfos();

                String projectId = projInfo.getProjectId();
                List<TuandaiProjectHouse> tuandaiProjectHouses = new LinkedList<>();
                for (ProjectHouseInfoReq  pHouseInfoReq:projectHouseInfoReqs){
                    TuandaiProjectHouse tuandaiProjectHosue = ClassCopyUtil.copy(pHouseInfoReq,ProjectHouseInfoReq.class,TuandaiProjectHouse.class);
                    tuandaiProjectHosue.setProjectId(projInfo.getProjectId());
                    tuandaiProjectHosue.setCreateTime(new Date());
                    tuandaiProjectHosue.setCreateUser(Constant.ADMIN_ID);
                    tuandaiProjectHouses.add(tuandaiProjectHosue);
                }
                tuandaiProjectHouseService.delete(new EntityWrapper<TuandaiProjectHouse>().eq("project_id",projectId));
                tuandaiProjectHouseService.insertBatch(tuandaiProjectHouses);


            }

            //////    存储标额外的费用信息   开始   /////////////////////
            List<ProjExtRateReq>  projExtRateReqs = projInfoReq.getProjExtRateReqs();
//            Map<String,List<ProjExtRateReq>> projExtRateReqMap = projInfoReq.getProjExtRateReqMap();
            //1.如果没有传入数据，则本金违约金、月收服务费违约金、平台服务费违约金 需要写一段代码，设置一个默认的信息进去
            boolean principal_penalty_in_flage = false;  //本金违约金传入标志位
            boolean sub_company_penalty_in_flage = false; //月收服务费违约金传入标志位
            boolean plat_penalty_in_flage = false;  //平台服务费违约金传入标志位

            List<ProjExtRate> projExtRates = new LinkedList<>();

            //2.存储传入的额外费用
            if(projExtRateReqs!=null&&projExtRateReqs.size()>0){

                for(ProjExtRateReq projExtRateReq: projExtRateReqs){
                    ProjExtRate projExtRate = ClassCopyUtil.copyObject(projExtRateReq,ProjExtRate.class);
                    projExtRates.add(projExtRate);
                    projExtRate.setBusinessId(businessBasicInfoReq.getBusinessId());
                    projExtRate.setProjectId(projInfoReq.getProjectId());
                    projExtRate.setCreateTime(new Date());
                    projExtRate.setCreateUser(Constant.ADMIN_ID);
                    projExtRate.setBeginPeroid(projExtRateReq.getPeriod());
                    projExtRate.setEndPeroid(projExtRateReq.getPeriod());
                    if(projExtRateReq.getFeeId().equals(RepayPlanFeeTypeEnum.PRINCIPAL_PENALTY.getUuid())){
                        principal_penalty_in_flage = true;
                    }
                    if(projExtRateReq.getFeeId().equals(RepayPlanFeeTypeEnum.SUB_COMPANY_PENALTY.getUuid())){
                        sub_company_penalty_in_flage = true;
                    }
                    if(projExtRateReq.getFeeId().equals(RepayPlanFeeTypeEnum.PLAT_PENALTY.getUuid())){
                        plat_penalty_in_flage = true;
                    }
                }
            }

            if(!principal_penalty_in_flage){
                //未传入本金违约金默认写入默认配置
            }
            if(!sub_company_penalty_in_flage){
                //未传入月收服务费违约金写入默认配置
                if(basicBusiness.getBusinessType().equals(BusinessTypeEnum.CREDIT_TYPE.getValue())){//信用贷的业务，默认月收服务费违约金
                    //如果是信用贷类的业务
                    List<ProjFeeReq> feeReqs = projInfoReq.getProjFeeInfos();
                    ProjFeeReq comPenalty = null;
                    for(ProjFeeReq feeReq:feeReqs ){
                        //月收的分公司服务费
                        if(feeReq.getFeeType().equals(RepayPlanFeeTypeEnum.SUB_COMPANY_CHARGE.getValue())
                                &&feeReq.getChargeType().equals(RepayPlanChargeTypeEnum.BY_MONTH.getKey())){
                            comPenalty = feeReq;
                        }
                    }
                    //传入了月收分公司服务费，根据月收分公司服务费计算
                    if(comPenalty!=null){
                        ProjExtRate projExtRate = new ProjExtRate();
//                        projExtRates.add(projExtRate);

                        projExtRate.setBusinessId(basicBusiness.getBusinessId());
                        projExtRate.setProjectId(projInfo.getProjectId());
                        projExtRate.setRateType(RepayPlanFeeTypeEnum.SUB_COMPANY_PENALTY.getValue());
                        projExtRate.setRateName(RepayPlanFeeTypeEnum.SUB_COMPANY_PENALTY.getDesc());
                        //2倍的月收分公司服务费
                        projExtRate.setRateValue(comPenalty.getFeeValue().multiply(new BigDecimal("2")));
                        projExtRate.setCalcWay(PepayPlanProjExtRatCalEnum.RATE_VALUE.getValue());//计算方式
                        projExtRate.setFeeId(RepayPlanFeeTypeEnum.SUB_COMPANY_PENALTY.getUuid()); //费率FeeId
                        projExtRate.setFeeName(RepayPlanFeeTypeEnum.SUB_COMPANY_PENALTY.getDesc()); //费率名
                        projExtRate.setBeginPeroid(1);
                        projExtRate.setEndPeroid(projInfoReq.getPeriodMonth()-1);//最后一期不用收取违约金
                        projExtRate.setCreateTime(new Date());
                        projExtRate.setCreateUser(Constant.ADMIN_ID);
                        //如果是分段收费的
                        if(comPenalty.getIsTermRange().equals(BooleanEnum.YES.getValue())){
                            List<ProjFeeDetailReq>  comPenaltyDetailReqs = comPenalty.getFeeDetailReqList();
                            for(ProjFeeDetailReq projFeeDetailReq: comPenaltyDetailReqs){
                                ProjExtRate projExtRate1 = ClassCopyUtil.copyObject(projExtRate,ProjExtRate.class);
                                projExtRate1.setBeginPeroid(projFeeDetailReq.getPeriod());
                                projExtRate1.setEndPeroid(projFeeDetailReq.getPeriod());
                                //2倍的月收分公司服务费
                                projExtRate1.setRateValue(projFeeDetailReq.getFeeValue().multiply(new BigDecimal("2")));
                                projExtRates.add(projExtRate1);
                            }

                        }else{
                            projExtRate.setBeginPeroid(1);
                            projExtRate.setEndPeroid(projInfoReq.getPeriodMonth()-1);//最后一期不用收取违约金
                            projExtRates.add(projExtRate);
                        }
                    }
                }
            }
            if(!plat_penalty_in_flage){
                //平台服务费违约金写入默认配置
                if(basicBusiness.getBusinessType().equals(BusinessTypeEnum.CREDIT_TYPE.getValue())){//信用贷的业务，默认月收服务费违约金
                    //如果是信用贷类的业务
                    List<ProjFeeReq> feeReqs = projInfoReq.getProjFeeInfos();
                    ProjFeeReq platPenalty = null;
                    for(ProjFeeReq feeReq:feeReqs ){
                        //月收的平台服务费
                        if(feeReq.getFeeType().equals(RepayPlanFeeTypeEnum.PLAT_CHARGE.getValue())
                                &&feeReq.getChargeType().equals(RepayPlanChargeTypeEnum.BY_MONTH.getKey())){
                            platPenalty = feeReq;
                        }
                    }
                    //传入了月收平台服务费，根据月收平台服务费计算
                    if(platPenalty!=null){
                        ProjExtRate projExtRate = new ProjExtRate();


                        projExtRate.setBusinessId(basicBusiness.getBusinessId());
                        projExtRate.setProjectId(projInfo.getProjectId());
                        projExtRate.setRateType(RepayPlanFeeTypeEnum.PLAT_PENALTY.getValue());
                        projExtRate.setRateName(RepayPlanFeeTypeEnum.PLAT_PENALTY.getDesc());
                        //2倍的月收平台服务费
                        projExtRate.setRateValue(platPenalty.getFeeValue().multiply(new BigDecimal("2")));
                        projExtRate.setCalcWay(PepayPlanProjExtRatCalEnum.RATE_VALUE.getValue());//计算方式
                        projExtRate.setFeeId(RepayPlanFeeTypeEnum.PLAT_PENALTY.getUuid()); //费率FeeId
                        projExtRate.setFeeName(RepayPlanFeeTypeEnum.PLAT_PENALTY.getDesc()); //费率名
//                        projExtRate.setBeginPeroid(1);
//                        projExtRate.setEndPeroid(projInfoReq.getPeriodMonth()-1);//最后一期不用收取违约金
                        projExtRate.setCreateTime(new Date());
                        projExtRate.setCreateUser(Constant.ADMIN_ID);
                        //如果是分段收费的
                        if(platPenalty.getIsTermRange().equals(BooleanEnum.YES.getValue())){
                            List<ProjFeeDetailReq>  comPenaltyDetailReqs = platPenalty.getFeeDetailReqList();
                            for(ProjFeeDetailReq projFeeDetailReq: comPenaltyDetailReqs){
                                ProjExtRate projExtRate1 = ClassCopyUtil.copyObject(projExtRate,ProjExtRate.class);
                                projExtRate1.setBeginPeroid(projFeeDetailReq.getPeriod());
                                projExtRate1.setEndPeroid(projFeeDetailReq.getPeriod());
                                //2倍的月收平台服务费
                                projExtRate1.setRateValue(projFeeDetailReq.getFeeValue().multiply(new BigDecimal("2")));
                                projExtRates.add(projExtRate1);
                            }

                        }else{
                            projExtRate.setBeginPeroid(1);
                            projExtRate.setEndPeroid(projInfoReq.getPeriodMonth()-1);//最后一期不用收取违约金
                            projExtRates.add(projExtRate);
                        }
                    }
                }
            }

            //3.存储传入的标费用信息中类型为一次性收取的费用
            List<ProjFeeReq> projFeeReqs = projInfoReq.getProjFeeInfos();
            for(ProjFeeReq projFeeReq:projFeeReqs){
                if(projFeeReq.getChargeType().equals(RepayPlanChargeTypeEnum.ONE_TIME.getKey())){
                    ProjExtRate projExtRate = new ProjExtRate();
                    projExtRate.setBusinessId(businessBasicInfoReq.getBusinessId());
                    projExtRate.setProjectId(projInfoReq.getProjectId());
                    projExtRate.setRateType(projFeeReq.getFeeType());
                    projExtRate.setRateName(projFeeReq.getFeeTypeName());
                    projExtRate.setRateValue(projFeeReq.getFeeValue());
                    projExtRate.setCalcWay(PepayPlanProjExtRatCalEnum.RATE_VALUE.getValue());
                    projExtRate.setFeeId(projFeeReq.getFeeItemId());
                    projExtRate.setFeeName(projFeeReq.getFeeItemName());
                    projExtRate.setCreateUser(Constant.ADMIN_ID);
                    projExtRate.setCreateTime(new Date());
                    //如果是分段收费的
                    if(projFeeReq.getIsTermRange().equals(BooleanEnum.YES.getValue())){
                        List<ProjFeeDetailReq>  projFeeDetailReqs = projFeeReq.getFeeDetailReqList();
                        for(ProjFeeDetailReq projFeeDetailReq: projFeeDetailReqs){
                            ProjExtRate projExtRate1 = ClassCopyUtil.copyObject(projExtRate,ProjExtRate.class);
                            projExtRate1.setBeginPeroid(projFeeDetailReq.getPeriod());
                            projExtRate1.setEndPeroid(projFeeDetailReq.getPeriod());
                            projExtRate1.setRateValue(projFeeDetailReq.getFeeValue());
                            projExtRates.add(projExtRate1);
                        }

                    }else {
                        projExtRate.setBeginPeroid(0);
                        projExtRate.setEndPeroid(0);
                        projExtRates.add(projExtRate);
                    }


                }
            }
            projExtRateService.delete(new EntityWrapper<ProjExtRate>().eq("project_id",projInfoReq.getProjectId()));
            if(projExtRates.size()>0){
                projExtRateService.insertBatch(projExtRates);
            }

            //////    存储标额外的费用信息   结束   /////////////////////
        }
        /////////   存储传入的相关信息   结束   ////////////

        //异步存储调用此接口传入的参数
        executor.execute(new Runnable() {
            @Override
            public void run() {
                IssueSendOutsideLog log=new IssueSendOutsideLog();
                log.setCreateTime(new Date());
                log.setCreateUserId(Constant.ADMIN_ID);
                log.setInterfacecode("CreatRepayPlanService_creatAndSaveRepayPlan");
                log.setInterfacename("创建并存储还款计划");
                log.setSendJsonEncrypt(creatRepayPlanReq.getBusinessBasicInfoReq().getBusinessId());
                log.setSendJson(JSON.toJSONString(creatRepayPlanReq));
                log.setReturnJson(JSON.toJSONString(planReturnInfoDto));
//                log.setReturnJsonDecrypt(JSON.toJSONString(dtos));
                log.setSystem("");
                log.setSendUrl("");

                issueSendOutsideLogService.insert(log);
            }
        });


        return planReturnInfoDto;

    }

    /**
     * 判断是否重传
     * @param creatRepayPlanReq
     * @return
     */
    private Boolean checkResave(CreatRepayPlanReq creatRepayPlanReq){
        for(ProjInfoReq projInfoReq:creatRepayPlanReq.getProjInfoReqs() ){
            List<RepaymentProjPlan> projList =  repaymentProjPlanService.selectList(new EntityWrapper<RepaymentProjPlan>().eq("project_id",projInfoReq.getProjectId()));



            if(projList.size()>0){
//                for(RepaymentProjPlan projPlan:projList){
//                    Integer diffDays = DateUtil.getDiffDays(projPlan.getCreateTime(),projInfoReq.getQueryFullsuccessDate());
//                    //如果同一个标的满标时间与还款计划生成的时间相差一天以内
//                    if(diffDays <= 1){
                logger.error("已存在当前标的还款计划  projId:"+projInfoReq.getProjectId());
                        throw  new CreatRepaymentExcepiton("已存在当前标的还款计划");
//                    }
//                }
            }
        }
        return true;
    }

    /**
     * 输入数据校验
     * @param creatRepayPlanReq
     * @return
     */
    private Boolean checkInputRep(CreatRepayPlanReq creatRepayPlanReq){
        //业务基础信息校验
        BusinessBasicInfoReq  businessBasicInfoReq = creatRepayPlanReq.getBusinessBasicInfoReq();
        if(businessBasicInfoReq.getSourceType().equals(BusinessSourceTypeEnum.SETTLE_NEW.getValue())||
                businessBasicInfoReq.getSourceType().equals(BusinessSourceTypeEnum.SETTLE_NEW.getValue())){
            if(businessBasicInfoReq.getSourceBusinessId()==null){
                throw  new CreatRepaymentExcepiton("结清再贷业务必须填写原始来源业务的业务编号");
            }
        }


        //业务用户信息校验
        List<BusinessCustomerInfoReq> bizCusInfoReqs = creatRepayPlanReq.getBizCusInfoReqs();
        if(bizCusInfoReqs == null ||bizCusInfoReqs.size()==0){
            throw  new CreatRepaymentExcepiton("请填写业务用户信息");
        }

        //个人用户或者主借款人必须传入customerId
        for(BusinessCustomerInfoReq businessCustomerInfoReq: bizCusInfoReqs){
            if(businessCustomerInfoReq.getIsmainCustomer().compareTo(1)==0){
                if(businessCustomerInfoReq.getCustomerId() == null){
                    throw  new CreatRepaymentExcepiton("主借款人必须填写用户Id(customerId)");
                }
            }
            if(businessCustomerInfoReq.getCustomerType().equals("个人")){
                if(businessCustomerInfoReq.getCustomerId() == null){
                    throw  new CreatRepaymentExcepiton("个人用户必须填写用户Id(customerId)");
                }
            }
        }


        //判断是否有主借人  开始
        Boolean mainBorrowPersonFlage = false;
        Integer mainCount=0;
        for(BusinessCustomerInfoReq bizCusInfoReq:bizCusInfoReqs){
            if(bizCusInfoReq.getIsmainCustomer().equals(BooleanEnum.YES.getValue())){
                mainBorrowPersonFlage = true;
                mainCount ++;
//                break;
            }
        }
//        if(!mainBorrowPersonFlage){
//            List<BasicBizCustomer> customerList =  basicBizCustomerService.selectList(
//                    new EntityWrapper<BasicBizCustomer>().eq("business_id",businessBasicInfoReq.getBusinessId()));
//            for(BasicBizCustomer basicBizCustomer:customerList){
//                if(basicBizCustomer.getIsmainCustomer().equals(BooleanEnum.YES.getValue())){
//                    mainBorrowPersonFlage = true;
//                    break;
//                }
//            }
//        }
        if(!mainBorrowPersonFlage){
            throw  new  CreatRepaymentExcepiton("业务用户信息列表中没有主借人信息");
        }
        if(mainCount>1){
            throw  new  CreatRepaymentExcepiton("一个业务只能有一个主借人");
        }
        //判断是否有主借人  结束

        for(BusinessCustomerInfoReq bizCusInfoReq:bizCusInfoReqs){
            if(!bizCusInfoReq.getCustomerType().equals(BizCustomerTypeEnum.PERSON.getName())&&
                    !bizCusInfoReq.getCustomerType().equals(BizCustomerTypeEnum.COMPANY.getName())){
                logger.error("客户类型需为'个人'或'公司'  bizId:"+businessBasicInfoReq.getBusinessId()
                        +"  customerinfo:"+JSON.toJSONString(bizCusInfoReq));
                throw  new CreatRepaymentExcepiton("客户类型需为'个人'或'公司'");
            }

            if(bizCusInfoReq.getCustomerType().equals(BizCustomerTypeEnum.COMPANY.getName())){
                if(bizCusInfoReq.getIsCompanyBankAccount()==null){
                    logger.error("公司用户 必须填写是否提供公账  bizId:"+businessBasicInfoReq.getBusinessId()
                            +"  customerinfo:"+JSON.toJSONString(bizCusInfoReq));
                    throw  new CreatRepaymentExcepiton("公司用户 必须填写是否提供公账  customerId:"+bizCusInfoReq.getCustomerId());
                }

                if(bizCusInfoReq.getIsCompanyBankAccount().equals(BooleanEnum.YES.getValue())
                        &&bizCusInfoReq.getIsCompanyBankAccount().equals(BooleanEnum.NO.getValue())){
                    logger.error("公司用户 是否提供公账 字段输入值不正确  bizId:"+businessBasicInfoReq.getBusinessId()
                            +"  customerinfo:"+JSON.toJSONString(bizCusInfoReq));
                    throw  new CreatRepaymentExcepiton("用户信息  是否提供公账 字段输入值不正确  customerId:"+bizCusInfoReq.getCustomerId());
                }

                if(bizCusInfoReq.getIsMergedCertificate()==null){
                    logger.error("公司用户 必须填写是否三证合一  bizId:"+businessBasicInfoReq.getBusinessId()
                            +"  customerinfo:"+JSON.toJSONString(bizCusInfoReq));
                    throw  new CreatRepaymentExcepiton("公司用户 必须填写是否三证合一 customerId:"+bizCusInfoReq.getCustomerId());
                }

                if(bizCusInfoReq.getIsCompanyBankAccount().equals(BooleanEnum.YES.getValue())
                        &&bizCusInfoReq.getIsCompanyBankAccount().equals(BooleanEnum.NO.getValue())){
                    logger.error("公司用户  是否三证合一 字段输入值不正确  bizId:"+businessBasicInfoReq.getBusinessId()
                            +"  customerinfo:"+JSON.toJSONString(bizCusInfoReq));
                    throw  new CreatRepaymentExcepiton("公司用户  是否三证合一 字段输入值不正确  customerId:"+bizCusInfoReq.getCustomerId());
                }

                if(bizCusInfoReq.getIsCompanyBankAccount().equals(BooleanEnum.YES.getValue())){
                    if(bizCusInfoReq.getUnifiedCode()==null){
                        logger.error("三证合一的公司用户 需填写统一社会信用代码  bizId:"+businessBasicInfoReq.getBusinessId()
                                +"  customerinfo:"+JSON.toJSONString(bizCusInfoReq));
                        throw  new CreatRepaymentExcepiton("三证合一的公司用户 需填写统一社会信用代码 customerId:"+bizCusInfoReq.getCustomerId());

                    }
                }
                if(bizCusInfoReq.getIsCompanyBankAccount().equals(BooleanEnum.NO.getValue())){
                    if(bizCusInfoReq.getBusinessLicence()==null){
                        logger.error("非三证合一的公司用户 需填写营业执照号  bizId:"+businessBasicInfoReq.getBusinessId()
                                +"  customerinfo:"+JSON.toJSONString(bizCusInfoReq));
                        throw  new CreatRepaymentExcepiton("非三证合一的公司用户 需填写营业执照号 customerId:"+bizCusInfoReq.getCustomerId());
                    }
                }
                if(bizCusInfoReq.getRegisterProvince()==null){
                    logger.error("公司用户 需填写企业注册地址所在省份  bizId:"+businessBasicInfoReq.getBusinessId()
                            +"  customerinfo:"+JSON.toJSONString(bizCusInfoReq));
                    throw  new CreatRepaymentExcepiton("公司用户 需填写企业注册地址所在省份 customerId:"+bizCusInfoReq.getCustomerId());
                }
                if(bizCusInfoReq.getCompanyLegalPerson()==null){
                    logger.error("公司用户 需填写企业法人  bizId:"+businessBasicInfoReq.getBusinessId()
                            +"  customerinfo:"+JSON.toJSONString(bizCusInfoReq));
                    throw  new CreatRepaymentExcepiton("公司用户 需填写企业法人 customerId:"+bizCusInfoReq.getCustomerId());
                }
                if(bizCusInfoReq.getLegalPersonIdentityCard()==null){
                    logger.error("公司用户 需填写企业法人身份证  bizId:"+businessBasicInfoReq.getBusinessId()
                            +"  customerinfo:"+JSON.toJSONString(bizCusInfoReq));
                    throw  new CreatRepaymentExcepiton("公司用户 需填写企业法人身份证 customerId:"+bizCusInfoReq.getCustomerId());
                }
                if(bizCusInfoReq.getCompanyLegalPerson()==null){
                    logger.error("公司用户 需填写企业法人是否大陆居民  bizId:"+businessBasicInfoReq.getBusinessId()
                            +"  customerinfo:"+JSON.toJSONString(bizCusInfoReq));
                    throw  new CreatRepaymentExcepiton("公司用户 需填写企业法人是否大陆居民 customerId:"+bizCusInfoReq.getCustomerId());
                }
            }
        }
        
        
        List<ProjInfoReq>  projInfoReqs = creatRepayPlanReq.getProjInfoReqs();
//        if(projInfoReqs.size()==0) {
//        	if(businessBasicInfoReq.getOutputPlatformid()==PaymentPlatformEnums.YUECAI.getValue()) {
//            	projInfoReqs= createProjInfoReqsList(businessBasicInfoReq, bizCusInfoReqs);
//            }
//          
//        }
        

        ////////   传入的标信息  校验  开始   ///////////////////////////

        for(ProjInfoReq projInfoReq :projInfoReqs){
        	
        	
        	  if(projInfoReq.getUserTypeId()==1){
                  if(projInfoReq.getTdUserId() == null){
                      throw  new CreatRepaymentExcepiton("个人用户必须填写资金端用户ID(tdUserId)");
                  }
              }
        	
            //标的车辆信息校验
            if(projInfoReq.getIsHaveCar().equals(BooleanEnum.YES.getValue())){
                if(projInfoReq.getProjCarInfos()==null|| projInfoReq.getProjCarInfos().size()==0){
                    logger.error("有车辆信息的标必须把车辆信息列表传入  projId:"+projInfoReq.getProjectId()
                            +"  projInfoReq:"+JSON.toJSONString(projInfoReq));
                    throw  new CreatRepaymentExcepiton("有车辆信息的标必须把车辆信息列表传入  projId:" +projInfoReq.getProjectId() );
                }
            }
            //标的房产信息校验
            if(projInfoReq.getIsHaveHouse().equals(BooleanEnum.YES.getValue())){
                if(projInfoReq.getProjHouseInfos()==null || projInfoReq.getProjHouseInfos().size()==0){
                    logger.error("有房产信息的标必须把房产信息列表传入  projId:"+projInfoReq.getProjectId()
                            +"  projInfoReq:"+JSON.toJSONString(projInfoReq));
                    throw  new CreatRepaymentExcepiton("有房产信息的标必须把房产信息列表传入 projId:" +projInfoReq.getProjectId());
                }
            }


            //////   分期还本付息  传入的每期应还本金总和是否与传入的借款金额一致校验   开始

            if(projInfoReq.getRepayType().equals(RepayPlanRepayIniCalcWayEnum.INT_AND_PRIN_EVERYTIME.getKey())){
                List<PrincipleReq> list = projInfoReq.getPrincipleReqList();
                if(list == null || list.size() == 0){
                    logger.error("分期还本付息的标必须把每期应还金额传入  projId:"+projInfoReq.getProjectId()
                            +"  projInfoReq:"+JSON.toJSONString(projInfoReq));
                    throw  new CreatRepaymentExcepiton("分期还本付息的标必须把每期应还金额列表（principleReqList）传入 projId:" +projInfoReq.getProjectId());
                }
                if(!(projInfoReq.getBorrowLimit().compareTo(list.size())==0)){
                    logger.error("分期还本付息的标每期应还金额列表（principleReqList）长度应与还款期限（BorrowLimit）一致  projId:"+projInfoReq.getProjectId()
                            +"  projInfoReq:"+JSON.toJSONString(projInfoReq));
                    throw  new CreatRepaymentExcepiton("分期还本付息的标必须把每期应还金额列表（principleReqList）长度应与还款期限（BorrowLimit）一致 projId:" +projInfoReq.getProjectId());

                }
                BigDecimal totalPricipal = new BigDecimal("0");

                for(PrincipleReq principleReq:list){
                    totalPricipal =  totalPricipal.add(principleReq.getPrinciple());
                }

                if(!(totalPricipal.compareTo(projInfoReq.getFullBorrowMoney())== 0)){
                    logger.error("分期还本付息的标每期应还金额（principleReqList.Principle）总和应与总借款金额（FullBorrowMoney）一致  projId:"+projInfoReq.getProjectId()
                            +"  projInfoReq:"+JSON.toJSONString(projInfoReq));
                    throw  new CreatRepaymentExcepiton("分期还本付息的标每期应还金额（principleReqList.Principle）总和应与总借款金额（FullBorrowMoney）一致 projId:" +projInfoReq.getProjectId());

                }
//                System.out.println(new BigDecimal("1.2").compareTo(new BigDecimal("1.20")) == 0); //输出true
            }




            /////   分期还本付息  传入的每期应还本金总和是否与传入的借款金额一致校验   结束

            //标的额外费用信息校验  开始  ------------------
            List<ProjExtRateReq> projExtRateReqs = projInfoReq.getProjExtRateReqs();
            Map<String,List<ProjExtRateReq>> projExtRateReqMap = new HashMap<>();
            projInfoReq.setProjExtRateReqMap(projExtRateReqMap);
            //校验输入的开始期数，结束期数是否是连续的，是否等于总期数
            if(projExtRateReqs !=null &&projExtRateReqs.size()>0){
                for(ProjExtRateReq projExtRateReq:projExtRateReqs){
                    String feeId = projExtRateReq.getFeeId();
                    List<ProjExtRateReq> projExtRateReqList =projExtRateReqMap.get(feeId);
                    if(projExtRateReqList==null){
                        projExtRateReqList = new LinkedList<>();
                        projExtRateReqMap.put(feeId,projExtRateReqList);
                    }
                    projExtRateReqList.add(projExtRateReq);
                }
            }

            for(String feeId:projExtRateReqMap.keySet() ){

                List<ProjExtRateReq> rateReqs = projExtRateReqMap.get(feeId);
                //排序 由低到高
                Collections.sort(rateReqs, new Comparator<ProjExtRateReq>() {
                    @Override
                    public int compare(ProjExtRateReq o1, ProjExtRateReq o2) {
                        if(o1.getPeriod().compareTo(o2.getPeriod())==0){
                            return Integer.valueOf(o1.getPeriod().compareTo(o2.getPeriod()));
                        }else{
                            return o1.getPeriod().compareTo(o2.getPeriod());
                        }
                    }
                });
                //判断开始结束时间是否是连续的
//                Integer lastEndPeriod=0;
//                for(ProjExtRateReq projExtRateReq:rateReqs){
//                    Integer beginPeroid = projExtRateReq.getBeginPeroid();
//                    if(!(beginPeroid-lastEndPeriod==1)){
//                        logger.error("额外费率期数设置不连续  List:"+JSON.toJSONString(rateReqs)
//                                +"  ProjExtRateReq:"+JSON.toJSONString(projExtRateReq));
//                        throw  new CreatRepaymentExcepiton("额外费率期数设置不连续 List:"+JSON.toJSONString(rateReqs)
//                                +"  ProjExtRateReq:"+JSON.toJSONString(projExtRateReq));
//                    }
//                    lastEndPeriod = projExtRateReq.getEndPeroid();
//                }
//                if(lastEndPeriod!=projInfoReq.getPeriodMonth()){
//                    logger.error("额外费率期数设置不足  List:"+JSON.toJSONString(rateReqs)
//                            +"  标的期数:"+projInfoReq.getPeriodMonth());
//                    throw  new CreatRepaymentExcepiton("额外费率期数设置不足  List:"+JSON.toJSONString(rateReqs)
//                            +"  标的期数:"+projInfoReq.getPeriodMonth());
//
//                }

            }
            //标的额外费用信息校验  结束  ------------------


            //  你我金融的单    传入费用项  大类里面只能包含一个小类 校验  开始  --------------
//            platformManageFee	平台管理费	decimal	Y	当期应还平台管理费
//            commissionGuaranteFee	担保服务费	decimal	Y	当期应还担保服务费

            if(projInfoReq.getPlateType().equals(ProjPlatTypeEnum.NIWO_JR.getKey())){
                List<ProjFeeReq> projFeeReqs = projInfoReq.getProjFeeInfos();

                Map<Integer, List<ProjFeeReq>>  map = new HashMap<>();

                for(ProjFeeReq feeRe: projFeeReqs){
                	if(feeRe.getChargeType()==RepayPlanChargeTypeEnum.ONE_TIME.getKey()) {
                		continue;
                	}
                    Integer feeType = feeRe.getFeeType();
                    List<ProjFeeReq> l = map.get(feeType);
                    if(l==null){
                        l = new LinkedList<>();
                        map.put(feeType,l);
                    }
                    l.add(feeRe);
                }

                for(Integer ff: map.keySet()){
                    if(map.get(ff).size()>1){
                        logger.error("你我金融的业务，每种费用大项只能有一条子项  projInfo:"+JSON.toJSONString(projInfoReq));
                        throw  new CreatRepaymentExcepiton("你我金融的业务，每种费用大项只能有一条子项  List:"+JSON.toJSONString(projInfoReq));

                    }
                }
            }


            //  你我金融的单    传入费用项  大类里面只能包含一个小类 校验  结束  --------------


        }

        ////////   传入的标信息  校验  结束   ///////////////////////////
        return true;
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
            bizPlan.setPlanStatus(RepayPlanSettleStatusEnum.REPAYINF.getValue());  // 还款计划状态
            bizPlan.setIsDefer(businessBasicInfo.getIsRenewBusiness()); //是否展期还款计划
            bizPlan.setXdAfterGuid(null);       //对应原信贷的还款批次号  置位为空
            bizPlan.setXdOutId(null);       //对应原信贷的出款计划ID  置位为空
            bizPlan.setSrcType(RepayPlanCreateSysEnum.ALMS.getValue());       //还款计划生成系统标志
            bizPlan.setCreateTime(new Date());       //创建日期
            bizPlan.setCreateUser(Constant.ADMIN_ID);       //创建用户

            //更新标的还款计划主表的 plan_id
            BigDecimal bizPlanBorrowMoney= new BigDecimal(0);
            for (RepaymentProjPlan projPlan:projPlans ){
                projPlan.setPlanId(bizPlan.getPlanId());
                BigDecimal projBorrowMoney =projPlan.getBorrowMoney();
                projBorrowMoney = projBorrowMoney.setScale(smallNum,roundingMode);
                bizPlanBorrowMoney = bizPlanBorrowMoney.add(projBorrowMoney);
                projPlan.setBorrowMoney(projBorrowMoney);
            }
            //bizPlanBorrowMoney = bizPlanBorrowMoney.setScale(smallNum,roundingMode);
            bizPlan.setBorrowMoney(bizPlanBorrowMoney);// 生成还款计划对应的借款总额(元)

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
                    BigDecimal projTBAmount = projPList.getTotalBorrowAmount();
                    projTBAmount = projTBAmount.setScale(smallNum,roundingMode);
                    totalBorrowAmount = totalBorrowAmount.add(projTBAmount);
                    projPList.setTotalBorrowAmount(projTBAmount);
                }
               // totalBorrowAmount = totalBorrowAmount.setScale(smallNum,roundingMode);
                bizPlanList.setTotalBorrowAmount(totalBorrowAmount);   //应还金额
                bizPlanList.setOverdueAmount(null);   //总应还滞纳金(元)
                bizPlanList.setCurrentStatus(RepayPlanStatus.REPAYING.getName());   //当前还款状态
                bizPlanList.setCurrentSubStatus(null);   //当前还款子状态
                bizPlanList.setRepayFlag(RepayPlanPayedTypeEnum.PAYING.getValue());   // 已还款类型标记
                bizPlanList.setCreateTime(new Date());   // 创建日期
                bizPlanList.setSrcType(bizPlan.getSrcType());   // 来源类型
                bizPlanList.setCreateUser(Constant.ADMIN_ID);   // 创建用户


                //更新标的 list 业务还款计划相关Id
                for(RepaymentProjPlanList projPlanList:planLists){
                    projPlanList.setPlanId(bizPlan.getPlanId());
                    projPlanList.setPlanListId(bizPlanList.getPlanListId());
                }


                //业务这一期  还款明细列表
                List<RepaymentBizPlanListDetail>  bizPlanListDetails = new LinkedList<>();
                bizPlanListDetailMap.put(bizPlanList.getPlanListId(),bizPlanListDetails);

                if(projpDetialMap!=null&&projpDetialMap.size()>0){
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
                            BigDecimal pPAmount = pDetail.getProjPlanAmount();
                            pPAmount=  pPAmount.setScale(smallNum,roundingMode);
                            planAmount = planAmount.add(pPAmount);
                            pDetail.setProjPlanAmount(pPAmount);
                        }
                       // planAmount = planAmount.setScale(smallNum,roundingMode);
                        bizpDetial.setPlanAmount(planAmount);  //项目计划应还总金额(元)

                        bizpDetial.setPlanRate(null);  //项目计划应还比例(%)，如0.5%则存0.5，可空
                        bizpDetial.setFeeId(feeId);  //资产端费用项ID，用于资产端区分同名的项目
                        bizpDetial.setPlanItemName(projpDetials.get(0).getPlanItemName());  //应还项目名称
                        bizpDetial.setPlanItemType(projpDetials.get(0).getPlanItemType());  //应还项目所属分类
                        bizpDetial.setAccountStatus(projpDetials.get(0).getAccountStatus());  // 分账标记
                        bizpDetial.setCreateDate(new Date());  // 创建日期
                        bizpDetial.setSrcType(RepayPlanCreateSysEnum.ALMS.getValue());  // 来源类型
                        bizpDetial.setCreateUser(Constant.ADMIN_ID);  // 创建者
                        bizpDetial.setDueDate(bizPlanList.getDueDate());
                        //更新标的detail   业务还款计划相关Id
                        for(RepaymentProjPlanListDetail projPlanListDetail: projpDetials){
                            projPlanListDetail.setPlanListId(bizPlanList.getPlanListId());
                            projPlanListDetail.setPlanDetailId(bizpDetial.getPlanDetailId());
                        }

                    }
                }

            }
        }
    }



    /**
     * 根据表信息计算批次(按满标时间计算)
     * @param tuandaiProjReqInfos
     * @return
     */
    private Map<String,List<ProjInfoReq>> getProjInfoReqMap(List<ProjInfoReq> tuandaiProjReqInfos){
        Map<String,List<ProjInfoReq>>  projInfoReqMap = new HashMap<>();
        for (ProjInfoReq projInfoReq:tuandaiProjReqInfos){
            Date fullTime = projInfoReq.getBeginTime();
            if(fullTime==null) {//add by czs 如果是试算的话，Fulltime为默认今天时间
            	fullTime=new Date();
            	projInfoReq.setBeginTime(fullTime);
            }
            //业务的还款计划还是按每日满标的标合并
            String fullTimeStr =  DateUtil.formatDate(fullTime);//UUID.randomUUID().toString();
            List<ProjInfoReq>  batchProj =projInfoReqMap.get(fullTimeStr);
            if(batchProj == null){
                batchProj = new LinkedList<>();
                projInfoReqMap.put(fullTimeStr,batchProj);
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
     * @param businessBasicInfo  业务基础信息
     * @param projPlanDetailTotalMap  Map<批次,Map<标还款计划主表Id，Map<标还款计划list表Id，标的还款计划detail列表>>
     * @param projPlanListTotalMap  Map<批次,Map<标还款计划主表Id，标的还款计划list列表>>
     */


    private void  calcProjRepayments(Map<String,List<ProjInfoReq>>projInfoReqMap,
                                     Map<String,Map<Integer,List<RepaymentProjPlanList>>>  repaymentPlanListBatchMap,
                                     Map<String,Map<Integer,Map<String,List<RepaymentProjPlanListDetail>>>>  RepaymentProjPlanListDetailBatchMap,
                                     Map<String,List<RepaymentProjPlan>> repaymentProjPlanMap,
                                     BusinessBasicInfoReq  businessBasicInfo,
                                     Map<String,Map<String,Map<String,List<RepaymentProjPlanListDetail>>>> projPlanDetailTotalMap,
                                     Map<String,Map<String,List<RepaymentProjPlanList>>> projPlanListTotalMap,CreatRepayPlanReq creatRepayPlanReq) throws IllegalAccessException, InstantiationException {
        Integer planIndex = 0;
        //****************根据启标时间排序*******************//  add by chenzs
        List<String> beginDays=new ArrayList<String>();
        beginDays.addAll(projInfoReqMap.keySet());
		Collections.sort(beginDays, new Comparator<String>() {
			@Override
			public int compare(String f1, String f2) {
				return f1.compareTo(f2); 
			}

		});
		 //****************根据启标时间排序*******************//
        for(String beginDay:beginDays){
            planIndex++;
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


                ///////  标还款计划表   一次出款 生成一条记录
                RepaymentProjPlan repaymentProjPlan = new RepaymentProjPlan(); //ClassCopyUtil.copy(projInfoReq,ProjInfoReq.class,RepaymentProjPlan.class);
                repaymentProjPlan.setProjPlanId(UUID.randomUUID().toString());
                repaymentProjPlan.setProjectId(projInfoReq.getProjectId());
                repaymentProjPlan.setBusinessId(businessBasicInfo.getBusinessId());
                repaymentProjPlan.setOriginalBusinessId(businessBasicInfo.getOrgBusinessId());
                repaymentProjPlan.setRepaymentBatchId(batchId);  //还款计划批次号
                repaymentProjPlan.setPlanId("");   //业务还款计划ID
                repaymentProjPlan.setBorrowMoney(projInfoReq.getFullBorrowMoney());  //生成还款计划对应的借款总额
                repaymentProjPlan.setBorrowRate(projInfoReq.getRate());  //生成还款计划对应的借款利率
                repaymentProjPlan.setBorrowRateUnit(projInfoReq.getRateUnitType());//利率类型
                repaymentProjPlan.setBorrowLimit(projInfoReq.getPeriodMonth());//借款期限
                repaymentProjPlan.setBorrowLimitUnit(RepayPlanBorrowLimitUnitEnum.MONTH.getValue());//借款期限单位
                repaymentProjPlan.setPlanStatus(RepayPlanSettleStatusEnum.REPAYINF.getValue());//还款计划状态
                repaymentProjPlan.setIsDefer(RepayPlanIsDeferEnum.NO.getValue());//是否展期还款计划
                repaymentProjPlan.setCreateTime(new Date());
                repaymentProjPlan.setCreateUser(Constant.ADMIN_ID);
                repaymentProjPlan.setCreatSysType(RepayPlanCreateSysEnum.ALMS.getValue());
                repaymentProjPlan.setPlateType(projInfoReq.getPlateType());
                repaymentProjPlan.setOnLineOverDueRate(projInfoReq.getOnLineOverDueRate());
                repaymentProjPlan.setOnLineOverDueRateType(projInfoReq.getOnLineOverDueRateType());
                repaymentProjPlan.setOffLineOutOverDueRate(projInfoReq.getOffLineOutOverDueRate());
                repaymentProjPlan.setOffLineOutOverDueRateType(projInfoReq.getOffLineOutOverDueRateType());
                repaymentProjPlan.setOffLineInOverDueRate(projInfoReq.getOffLineInOverDueRate());
                repaymentProjPlan.setOffLineInOverDueRateType(projInfoReq.getOffLineInOverDueRateType());
                repaymentProjPlan.setQueryFullSuccessDate(projInfoReq.getQueryFullsuccessDate());
                repaymentProjPlan.setRequestNo(projInfoReq.getRequestNo());//资产端上标编号
                repaymentProjPlan.setRondmode(creatRepayPlanReq.getRondmode());
                repaymentProjPlan.setSmallNum(creatRepayPlanReq.getSmallNum());



                projPlans.add(repaymentProjPlan);
                List<ProjFeeReq> projFeeReqs =  projInfoReq.getProjFeeInfos();

                ///////  标的还款计划00期   一次性收取的费用信息  开始   ///////////////
                ////00期List 信息
//                RepaymentProjPlanList  zeroList =  creatRepaymentProjPlanList(repaymentProjPlan,0,planIndex);// new RepaymentProjPlanList();
//                zeroList.setCurrentStatus(RepayPlanStatus.REPAYED.getName()); //当前还款状态  00期的直接置位为已还款
//                //将标的00期写入还款计划map
//                addPlanListToMap(repaymentPlanListPeriorMap,projPlanListPMap,zeroList, 0);
//
//
//
//
//                List<RepaymentProjPlanListDetail>  zeroListDetails = new LinkedList<>();
//
//                //轮询标的费用项列表
//                if(projFeeReqs !=null && projFeeReqs.size()>0){
//                    for(ProjFeeReq feeReq: projFeeReqs){
//                        //如果费用是一次性收取
//                        if(feeReq.getChargeType().equals(RepayPlanChargeTypeEnum.ONE_TIME.getKey())){
//                            String feeItemId = getFeeItemId(feeReq.getFeeItemId(),feeReq.getFeeType());
//                            RepaymentProjPlanListDetail   zeroListDetail = creatProjListDetail(zeroList);
//                            zeroListDetail.setProjPlanAmount(feeReq.getFeeValue());//项目计划应还总金额(元)
//                            zeroListDetail.setFeeId(feeItemId);//资产端费用项ID，用于资产端区分同名的项目，若不存在同名费用项，可为空
//                            zeroListDetail.setPlanItemName(feeReq.getFeeTypeName());//应还项目名称
//                            zeroListDetail.setPlanItemType(feeReq.getFeeType());//应还项目所属分类
//                            zeroListDetail.setAccountStatus(feeReq.getAccountStatus());//分账标记
//
//                            //将第0期费用项添加到Map中
//                            addDetialToMap(  repaymentProjPlanListDetailPeriorMap,
//                                    projdetailListMap,
//                                    feeItemId, zeroListDetail,0,
//                                    zeroList.getProjPlanId());
//                            //添加到第0期详情列表中
//                            zeroListDetails.add(zeroListDetail);
//                        }
//                        else{
//                            //不是一次性收取的话就计算出每一期需要交的费用项
//                            String feeItemId = getFeeItemId(feeReq.getFeeItemId(),feeReq.getFeeType());
//                        }
//                    }
//                }
//
//                BigDecimal zeroProidTotol = new BigDecimal(0);
//
//                for(RepaymentProjPlanListDetail detail:zeroListDetails){
//                    zeroProidTotol = zeroProidTotol.add(detail.getProjPlanAmount());
//                }
//                zeroList.setTotalBorrowAmount(zeroProidTotol);

//                ///////  标的还款计划00期   一次性收取的费用信息  结束   ///////////////

                //////   标的其他期还款计划   按月收取费用信息   开始  ///////////////

                RepayPlanBorrowRateUnitEnum rateUnitEnum = RepayPlanBorrowRateUnitEnum.getByKey(projInfoReq.getRateUnitType());
                RepayPlanRepayIniCalcWayEnum repayType = RepayPlanRepayIniCalcWayEnum.getByKey(projInfoReq.getRepayType());
                //计算每月应还本金利息
                Map<Integer,Map<String,BigDecimal>> repayPrinAndIni = calculateRepayPrinAndIni(
                        projInfoReq.getPeriodMonth(),projInfoReq.getFullBorrowMoney(),
                        projInfoReq.getRate(),rateUnitEnum,repayType,projInfoReq.getPrincipleReqList() );

                for(int i=1;i<projInfoReq.getPeriodMonth()+1;i++){
                    //还款计划详情项列表
                    List<RepaymentProjPlanListDetail>  priodListDetails = new LinkedList<>();
                    //创建还款计划list
                    RepaymentProjPlanList  projPlanList =  creatRepaymentProjPlanList(repaymentProjPlan,i,planIndex);// new RepaymentProjPlanList();
                    Date date = DateUtil.addMonth2Date(i,projInfoReq.getBeginTime());
                    date = DateUtil.addDay2Date(-1,date);
                    projPlanList.setDueDate(DateUtil.getDayStart(date));
                    //将标的第i期写入还款计划map
                    addPlanListToMap(repaymentPlanListPeriorMap,projPlanListPMap,projPlanList, i);

                    //本金detail
                    RepaymentProjPlanListDetail   prinDetail = creatProjListDetail(projPlanList);
                    prinDetail.setProjPlanAmount(repayPrinAndIni.get(i).get(principal_str));//项目计划应还总金额(元)
                    prinDetail.setFeeId(RepayPlanFeeTypeEnum.PRINCIPAL.getUuid());//资产端费用项ID，用于资产端区分同名的项目，若不存在同名费用项，可为空
                    prinDetail.setPlanItemName(RepayPlanFeeTypeEnum.PRINCIPAL.getDesc());//应还项目名称
                    prinDetail.setPlanItemType(RepayPlanFeeTypeEnum.PRINCIPAL.getValue());//应还项目所属分类
                    prinDetail.setAccountStatus(RepayPlanAccountStatusEnum.DIVISION_TO_PLAT.getValue());//分账标记
                    //分润顺序项
                    logger.info("分润顺序获取开始==============businessType:{0},itemType:{1},feeId:{2}",businessBasicInfo.getBusinessType().toString(),prinDetail.getPlanItemType(),prinDetail.getFeeId());
                    Integer shareProfitIndex =  profitItemSetService.getLevel(businessBasicInfo.getBusinessType().toString(),prinDetail.getPlanItemType(),prinDetail.getFeeId())
                            .get("feeLevel");
                    logger.info("分润顺序获取结束==============shareProfitIndex:{0}",shareProfitIndex);
                    prinDetail.setShareProfitIndex(shareProfitIndex);

                    addDetialToMap(  repaymentProjPlanListDetailPeriorMap,
                            projdetailListMap,
                            RepayPlanFeeTypeEnum.PRINCIPAL.getUuid(), prinDetail,i,
                            projPlanList.getProjPlanId() );
                    priodListDetails.add(prinDetail);

                    //利息
                    RepaymentProjPlanListDetail   iniDetail = creatProjListDetail(projPlanList);
                    iniDetail.setProjPlanAmount(repayPrinAndIni.get(i).get(interest_str));//项目计划应还总金额(元)
                    iniDetail.setFeeId(RepayPlanFeeTypeEnum.INTEREST.getUuid());//资产端费用项ID，用于资产端区分同名的项目，若不存在同名费用项，可为空
                    iniDetail.setPlanItemName(RepayPlanFeeTypeEnum.INTEREST.getDesc());//应还项目名称
                    iniDetail.setPlanItemType(RepayPlanFeeTypeEnum.INTEREST.getValue());//应还项目所属分类
                    iniDetail.setAccountStatus(RepayPlanAccountStatusEnum.DIVISION_TO_PLAT.getValue());//分账标记
                    //分润顺序项
                    Integer inishareProfitIndex =  profitItemSetService.getLevel(businessBasicInfo.getBusinessType().toString(),iniDetail.getPlanItemType(),iniDetail.getFeeId())
                            .get("feeLevel");
                    iniDetail.setShareProfitIndex(inishareProfitIndex);
                    addDetialToMap(  repaymentProjPlanListDetailPeriorMap,
                            projdetailListMap,
                            RepayPlanFeeTypeEnum.INTEREST.getUuid(), iniDetail,i,
                            projPlanList.getProjPlanId());
                    priodListDetails.add(iniDetail);


                    //列表费用
                    if(projFeeReqs !=null && projFeeReqs.size()>0) {
                        for (ProjFeeReq feeReq : projFeeReqs) {
                            if(feeReq.getChargeType().equals(RepayPlanChargeTypeEnum.BY_MONTH.getKey())){
                                //最后一期，期初收取的费用不收
                                if(i==projInfoReq.getPeriodMonth()){
                                    if(feeReq.getRepaymentFlag().equals(PepayPlanRepayFlageEnum.BEGIN.getValue())){
                                        continue;
                                    }
                                }

                                RepaymentProjPlanListDetail   peroidFeelDetail = creatProjListDetail(projPlanList);
                                String feeItemId = getFeeItemId(feeReq.getFeeItemId(),feeReq.getFeeType());
                                peroidFeelDetail.setFeeId(feeItemId);//资产端费用项ID，用于资产端区分同名的项目，若不存在同名费用项，可为空
                                peroidFeelDetail.setPlanItemName(feeReq.getFeeItemName());//应还项目名称
                                peroidFeelDetail.setPlanItemType(feeReq.getFeeType());//应还项目所属分类
                                peroidFeelDetail.setAccountStatus(feeReq.getAccountStatus());//分账标记
                                //分润顺序项
                                Integer itemshareProfitIndex =  profitItemSetService.getLevel(businessBasicInfo.getBusinessType().toString(),peroidFeelDetail.getPlanItemType(),peroidFeelDetail.getFeeId())
                                        .get("feeLevel");
                                peroidFeelDetail.setShareProfitIndex(itemshareProfitIndex);
                                if(feeReq.getIsTermRange().equals(BooleanEnum.YES.getValue())){
                                    //是分段收费  需要从分段收费信息列表中

                                    List<ProjFeeDetailReq>  feeDetailReqMap = feeReq.getFeeDetailReqList();
                                    if(feeDetailReqMap==null||feeDetailReqMap.size()==0){
                                        throw new CreatRepaymentExcepiton("分段收费的费用必须包含费用详情信息");
                                    }
                                    if(feeDetailReqMap.size()<projInfoReq.getPeriodMonth()){
                                        throw new CreatRepaymentExcepiton("分段收费的费用详情条数不能少于期数");
                                    }
                                    ProjFeeDetailReq feeDetail = null;
                                    for(ProjFeeDetailReq feeDetailReq: feeDetailReqMap) {
                                        if (feeDetailReq.getPeriod().equals(i)) {
                                            feeDetail = feeDetailReq;
                                        }
                                    }
                                    if(feeDetail==null){
                                        throw new CreatRepaymentExcepiton("分段收费的费用详情找不到  期数："+i
                                                  + "    费用ItemId："+feeReq.getFeeItemId());
                                    }
                                    peroidFeelDetail.setProjPlanAmount(feeDetail.getFeeValue());
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
                        proidTotol=proidTotol.add(detail.getProjPlanAmount());
                    }
                    projPlanList.setTotalBorrowAmount(proidTotol);
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
            return RepayPlanFeeTypeEnum.getByKey(feeType).getUuid();
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
    private   RepaymentProjPlanList  creatRepaymentProjPlanList(RepaymentProjPlan repaymentProjPlan,Integer period,Integer planIndex){
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
        projPlanList.setAfterId(calcAfterId(repaymentProjPlan.getBusinessId(),period,isRenew,planIndex)); // 总批次期数，  核对原来信贷还款计划是怎么写的
        projPlanList.setDueDate(new Date()); //应还日期 怎么设置， 需要核对
        projPlanList.setTotalBorrowAmount(new BigDecimal(0));// 总计划应还金额   需要按照每一项计算
        projPlanList.setOverdueAmount(new BigDecimal(0)); //总应还滞纳金
        projPlanList.setOverdueDays(new BigDecimal(0)); //逾期天数
        projPlanList.setCurrentStatus(RepayPlanStatus.REPAYING.getName()); //当前还款状态
        projPlanList.setRepayFlag(RepayPlanPayedTypeEnum.GET_MONEY_PAY.getValue()); //已还款类型标记
        projPlanList.setCreateTime(new Date());//设置创建时间
        projPlanList.setCreateUser(Constant.ADMIN_ID);//设置创建用户
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
     * @param planIndex   还款计划排序，这一次生成的所有还款计划
     * @return
     */
    private  String  calcAfterId(String businessId,Integer period,boolean isRenew,Integer planIndex){

         List<RepaymentBizPlan> bizPlans =  repaymentBizPlanService.selectList(new EntityWrapper<RepaymentBizPlan>().eq("business_id",businessId));
         Integer size = bizPlans==null?0:bizPlans.size();
         size = size+planIndex;

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
            RepayPlanRepayIniCalcWayEnum repayType,
            List<PrincipleReq> principleMap
            ){

        Map<Integer,Map<String,BigDecimal>>  retMap  = new HashMap<>();

        BigDecimal monthRate = getMonthRate(rate,rateUnit);

        switch (repayType){
            case PAY_LAST:  //到期还本息
            calcPrincipalLast(fullBorrowMoney,monthRate
            ,periodMonth,retMap);
            break;
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
                        ,periodMonth,retMap,principleMap);
                break;
            case INT_AND_PRIN_AVERAGE:  //等本等息
                calcIntAndPrinAverage(fullBorrowMoney,monthRate
                        ,periodMonth,retMap);
                break;
            default:
                    throw  new CreatRepaymentExcepiton("还款方式未支持");
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
                                         Integer periodMonth ,Map<Integer,Map<String,BigDecimal>>  retList,
            List<PrincipleReq> principleMap){

        if(principleMap == null || principleMap.size()<=0){
            throw new  CreatRepaymentExcepiton("分期还本付息没有每期应还本金对应信息");
        }

        //每月应还本金
//        BigDecimal priciple = fullBorrowMoney.divide(new BigDecimal(periodMonth),smallNum,roundingMode);
        //前期已还本金
        BigDecimal payedPriciple = new BigDecimal(0);
        for(int i=0;i<periodMonth;i++){
            Integer p = i+1;
            BigDecimal priciple = null;
            for(PrincipleReq  principleReq:principleMap){
                if(principleReq.getPeriod().equals(p)){
                    priciple =principleReq.getPrinciple();
                    break;
                }
            }
            if(priciple == null){
                throw new  CreatRepaymentExcepiton("分期还本付息，找不到当前期:第"+(i+1)+"期应还本金");
            }
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
        BigDecimal priciple = fullBorrowMoney.divide(new BigDecimal(periodMonth),10,roundingMode);
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
                monthRate = rate.divide(new BigDecimal(12),10,roundingMode);
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
        return monthRate.divide(new BigDecimal(100),10,roundingMode);
    }
    private BigDecimal getYearRate(BigDecimal rate,RepayPlanBorrowRateUnitEnum rateUnit){
        BigDecimal yearRate;
        switch (rateUnit){
            case YEAR_RATE:
                yearRate = rate;
                break;
            case MONTH_RATE:
                yearRate = rate.multiply(new BigDecimal(12));
                break;
            case DAY_RATE:
                yearRate = rate.multiply(new BigDecimal(30)).multiply(new BigDecimal(12));
                break;
            default:
                yearRate = rate;
                break;
        }
        return yearRate.divide(new BigDecimal(100),10,roundingMode);
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
        projPlanListDetail.setOrigBusinessId(projPlanList.getOrigBusinessId());//原业务Id
        projPlanListDetail.setPeriod(projPlanList.getPeriod()); //所属期数
        projPlanListDetail.setCreateDate(new Date());
        projPlanListDetail.setCreateUser(Constant.ADMIN_ID);
        projPlanListDetail.setCreatSysType(projPlanList.getCreatSysType()); //创建系统标志
        projPlanListDetail.setPlateType(projPlanList.getPlateType()); //平台类型标志
        projPlanListDetail.setDueDate(projPlanList.getDueDate());
        return projPlanListDetail;
    }

	@Override
	public PlanReturnInfoDto queryRepayPlanByBusinessId(String businessId) {
		
		try {
			PlanReturnInfoDto planReturnInfoDto = null;

			List<RepaymentBizPlanDto> repaymentBizPlanDtos = new ArrayList<>();
			
			/*
			 * 1、根据 businessId 找到对应的业务还款计划信息
			 */
			List<RepaymentBizPlan> repaymentBizPlans = repaymentBizPlanService
					.selectList(new EntityWrapper<RepaymentBizPlan>().eq("business_id", businessId));
			
			if (CollectionUtils.isNotEmpty(repaymentBizPlans)) {
				
				String origBusinessId = repaymentBizPlans.get(0).getOriginalBusinessId();
				
				if (StringUtil.isEmpty(origBusinessId)) {
					return planReturnInfoDto;
				}
				
				BasicBusiness basicBusiness = basicBusinessService.selectOne(new EntityWrapper<BasicBusiness>().eq("business_id", origBusinessId));
				
				if (basicBusiness == null) {
					return planReturnInfoDto;
				}
				
				BusinessBasicInfoReq req = new BusinessBasicInfoReq();
				req.setBusinessType(basicBusiness.getBusinessType());
				req.setOperatorName(basicBusiness.getOperatorName());
				req.setCompanyId(basicBusiness.getCompanyId());
				req.setRepaymentTypeId(basicBusiness.getRepaymentTypeId());
				
				// 遍历所有业务还款计划
				for (RepaymentBizPlan repaymentBizPlan : repaymentBizPlans) {
					
					// 得到 plan_id 
					String planId = repaymentBizPlan.getPlanId();
					
					RepaymentBizPlanDto repaymentBizPlanDto = new RepaymentBizPlanDto();
					
					repaymentBizPlanDto.setRepaymentBizPlan(repaymentBizPlan);
					
					// 根据 plan_id 找到对应的 业务还款计划 信息
					queryRapaymentBizPlanInfo(planId, repaymentBizPlanDto);
					
					// 根据 plan_id 找到对应的 标的还款计划
					queryProjPlanInfo(planId, repaymentBizPlanDto);
					
					repaymentBizPlanDtos.add(repaymentBizPlanDto);
					
				}
				// 拼装 XdPlanDto
				List<XdPlanDto> xdPlanDtos = xdPlanDtoHandle(repaymentBizPlanDtos, req);
				planReturnInfoDto = new PlanReturnInfoDto();
				planReturnInfoDto.setRepaymentBizPlanDtos(repaymentBizPlanDtos);
				planReturnInfoDto.setXdPlanDtos(xdPlanDtos);
			}
			
			return planReturnInfoDto;
			
		} catch (Exception e) {
			logger.error("根据业务ID查找还款计划失败！", e);
			throw new ServiceRuntimeException("根据业务ID查找还款计划失败！");
		}
		
	}

	/**
	 * 根据 plan_id 找到对应的 业务还款计划 信息
	 * @param planId
	 * @param repaymentBizPlanDto
	 */
	private void queryRapaymentBizPlanInfo(String planId, RepaymentBizPlanDto repaymentBizPlanDto) {
		
		List<RepaymentBizPlanListDto> repaymentBizPlanListDtos = new ArrayList<>();
		/*
		 * 1.1、根据 plan_id 找到对应的 业务还款计划列表
		 */
		List<RepaymentBizPlanList> repaymentBizPlanLists = repaymentBizPlanListService
				.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("plan_id", planId));
		
		if (CollectionUtils.isNotEmpty(repaymentBizPlanLists)) {
			for (RepaymentBizPlanList repaymentBizPlanList : repaymentBizPlanLists) {
				
				// 得到 plan_list_id
				String planListId = repaymentBizPlanList.getPlanListId();
				
				RepaymentBizPlanListDto repaymentBizPlanListDto = new RepaymentBizPlanListDto();
				repaymentBizPlanListDto.setRepaymentBizPlanList(repaymentBizPlanList);
				
				/*
				 * 1.2、根据 plan_list_id 找到对应的 还款计划应还项目明细
				 */
				List<RepaymentBizPlanListDetail> repaymentBizPlanListDetails = repaymentBizPlanListDetailSevice
						.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().eq("plan_list_id", planListId));
				repaymentBizPlanListDto.setBizPlanListDetails(repaymentBizPlanListDetails);
				repaymentBizPlanListDtos.add(repaymentBizPlanListDto);
			}
		}
		
		repaymentBizPlanDto.setBizPlanListDtos(repaymentBizPlanListDtos);
	}

	/**
	 * 根据 plan_id 找到对应的 标的还款计划
	 * @param planId
	 * @param repaymentBizPlanDto
	 */
	private void queryProjPlanInfo(String planId, RepaymentBizPlanDto repaymentBizPlanDto) {
		List<RepaymentProjPlanDto> repaymentProjPlanDtos = new ArrayList<>();
		
		/*
		 * 2.1、根据 plan_id 找到对应的 标的还款计划信息
		 */
		List<RepaymentProjPlan> repaymentProjPlans = repaymentProjPlanService
				.selectList(new EntityWrapper<RepaymentProjPlan>().eq("plan_id", planId));
		if (CollectionUtils.isNotEmpty(repaymentProjPlans)) {
			for (RepaymentProjPlan repaymentProjPlan : repaymentProjPlans) {
				
				RepaymentProjPlanDto repaymentProjPlanDto = new RepaymentProjPlanDto();
				repaymentProjPlanDto.setRepaymentProjPlan(repaymentProjPlan);
				
				// 得到 proj_plan_id
				String projPlanId = repaymentProjPlan.getProjPlanId();
				
				List<RepaymentProjPlanListDto> repaymentProjPlanListDtos = new ArrayList<>();
				
				/*
				 * 2.2、根据 proj_plan_id 找到对应的 标的还款计划列表
				 */
				List<RepaymentProjPlanList> repaymentProjPlanLists = repaymentProjPlanListService
						.selectList(new EntityWrapper<RepaymentProjPlanList>().eq("proj_plan_id", projPlanId));
				if (CollectionUtils.isNotEmpty(repaymentProjPlanLists)) {
					for (RepaymentProjPlanList repaymentProjPlanList : repaymentProjPlanLists) {
						
						RepaymentProjPlanListDto repaymentProjPlanListDto = new RepaymentProjPlanListDto();
						repaymentProjPlanListDto.setRepaymentProjPlanList(repaymentProjPlanList);
						
						// 得到 proj_plan_list_id
						String projPlanListId = repaymentProjPlanList.getProjPlanListId();
						
						/*
						 * 2.3 根据 proj_plan_list_id 找到对应的 标的还款计划应还项目明细
						 */
						List<RepaymentProjPlanListDetail> repaymentProjPlanListDetails = repaymentProjPlanListDetailService
								.selectList(new EntityWrapper<RepaymentProjPlanListDetail>()
										.eq("proj_plan_list_id", projPlanListId));
						repaymentProjPlanListDto.setProjPlanListDetails(repaymentProjPlanListDetails);
						repaymentProjPlanListDtos.add(repaymentProjPlanListDto);
					}
				}
				repaymentProjPlanDto.setProjPlanListDtos(repaymentProjPlanListDtos);
				repaymentProjPlanDtos.add(repaymentProjPlanDto);
			}
			repaymentBizPlanDto.setProjPlanDtos(repaymentProjPlanDtos);
		}
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void deleteRepayPlanByConditions(String businessId, String repaymentBatchId) {
		try {
			
			/*
			 * 1、根据 businessId 和 repaymentBatchId 查询 贷后管理生成 的 业务还款计划信息
			 */
			List<RepaymentBizPlan> repaymentBizPlans = repaymentBizPlanService
					.selectList(new EntityWrapper<RepaymentBizPlan>().eq("business_id", businessId)
							.eq("repayment_batch_id", repaymentBatchId).eq("src_type", 2));
			
			List<String> planIds = new LinkedList<>();
			List<String> planListIds = new LinkedList<>();
			List<String> planDetailIds = new LinkedList<>();
			List<String> projPlanIds = new LinkedList<>();
			List<String> projPlanListIds = new LinkedList<>();
			List<String> projPlanDetailIds = new LinkedList<>();
			
			if (CollectionUtils.isNotEmpty(repaymentBizPlans)) {
				
				for (RepaymentBizPlan repaymentBizPlan : repaymentBizPlans) {
					
					planIds.add(repaymentBizPlan.getPlanId());
					
				}
				
				/*
				 * 2、根据 plan_id 查询 标的还款计划信息
				 */
				List<RepaymentProjPlan> repaymentProjPlans = repaymentProjPlanService
						.selectList(new EntityWrapper<RepaymentProjPlan>().in("plan_id", planIds));
				
				if (CollectionUtils.isNotEmpty(repaymentProjPlans)) {
					for (RepaymentProjPlan repaymentProjPlan : repaymentProjPlans) {
						projPlanIds.add(repaymentProjPlan.getProjPlanId());
					}
				}
				
				if (!projPlanIds.isEmpty()) {
					/*
					 * 3、根据 proj_plan_id 查询 标的还款计划列表
					 */
					List<RepaymentProjPlanList> repaymentProjPlanLists = repaymentProjPlanListService
							.selectList(new EntityWrapper<RepaymentProjPlanList>().in("proj_plan_id", projPlanIds));
					
					if (CollectionUtils.isNotEmpty(repaymentProjPlanLists)) {
						
						for (RepaymentProjPlanList repaymentProjPlanList : repaymentProjPlanLists) {
							
							projPlanListIds.add(repaymentProjPlanList.getProjPlanListId());
							
						}
					}
					
				}
				
				if (!projPlanListIds.isEmpty()) {
					/*
					 * 4、根据 proj_plan_list_id 查询 标 的还款计划明细
					 */
					List<RepaymentProjPlanListDetail> repaymentProjPlanListDetails = repaymentProjPlanListDetailService
							.selectList(new EntityWrapper<RepaymentProjPlanListDetail>().in("proj_plan_list_id", projPlanListIds));
					
					if (CollectionUtils.isNotEmpty(repaymentProjPlanListDetails)) {
						
						for (RepaymentProjPlanListDetail repaymentProjPlanListDetail : repaymentProjPlanListDetails) {
							
							projPlanDetailIds.add(repaymentProjPlanListDetail.getProjPlanDetailId());
							
						}
					}
				}
				
				if (!projPlanDetailIds.isEmpty()) {
					/*
					 * 5、根据 proj_plan_detail_id 查询标实还明细
					 */
					List<RepaymentProjFactRepay> repaymentProjFactRepays = repaymentProjFactRepayService.selectList(
							new EntityWrapper<RepaymentProjFactRepay>().in("proj_plan_detail_id", projPlanDetailIds).eq("is_cancelled", 0));
					// 若产生任意实还金额, 则不允许删除还款计划
					if (CollectionUtils.isNotEmpty(repaymentProjFactRepays)) {
						throw new ServiceRuntimeException("此还款计划已产生实还项目，不允许删除");
					}else {
						
						List<RepaymentBizPlanList> repaymentBizPlanLists = repaymentBizPlanListService
								.selectList(new EntityWrapper<RepaymentBizPlanList>().in("plan_id", planIds));
						
						if (CollectionUtils.isNotEmpty(repaymentBizPlanLists)) {
							for (RepaymentBizPlanList repaymentBizPlanList : repaymentBizPlanLists) {
								planListIds.add(repaymentBizPlanList.getPlanListId());
							}
						}
						
						if (!planListIds.isEmpty()) {
							List<RepaymentBizPlanListDetail> repaymentBizPlanListDetails = repaymentBizPlanListDetailSevice
									.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().in("plan_list_id", planListIds));
							
							if (CollectionUtils.isNotEmpty(repaymentBizPlanListDetails)) {
								for (RepaymentBizPlanListDetail repaymentBizPlanListDetail : repaymentBizPlanListDetails) {
									planDetailIds.add(repaymentBizPlanListDetail.getPlanDetailId());
								}
							}
						}
						
						repaymentBizPlanService.deleteBatchIds(planIds);
						repaymentBizPlanListService.deleteBatchIds(planListIds);
						repaymentBizPlanListDetailSevice.deleteBatchIds(planDetailIds);
						repaymentProjPlanService.deleteBatchIds(projPlanIds);
						repaymentProjPlanListService.deleteBatchIds(projPlanListIds);
						repaymentBizPlanListDetailSevice.deleteBatchIds(projPlanDetailIds);
					}
				}
			}else {
				throw new ServiceRuntimeException("没有找到相关数据，撤销还款计划失败");
			}
		} catch (Exception e) {
			logger.error("按条件删除还款计划失败！", e);
			throw new ServiceRuntimeException(e.getMessage(), e);
		}
	}
	
	private  List<ProjInfoReq>  createProjInfoReqsList(BusinessBasicInfoReq businessBasicInfoReq,List<BusinessCustomerInfoReq> bizCusInfoReqs ){
	      List<ProjInfoReq>  projInfoReqs = new ArrayList();
	      BusinessCustomerInfoReq customerReq=null;
	      for(BusinessCustomerInfoReq businessCustomerInfoReq:bizCusInfoReqs) {
	    	  if(businessCustomerInfoReq.getIsmainCustomer()==1) {
	    		  customerReq=businessCustomerInfoReq;
	    	  }
	      }
        ProjInfoReq projInfoReq = creatProjInfoReq2(businessBasicInfoReq, customerReq);
        projInfoReqs.add(projInfoReq);
		return projInfoReqs;
  
        
	}
	private  ProjInfoReq creatProjInfoReq2(BusinessBasicInfoReq  businessBasicInfoReq,BusinessCustomerInfoReq main){
		ProjInfoReq p = new ProjInfoReq();
        String projectId = UUID.randomUUID().toString() ;
        p.setProjFeeInfos(new ArrayList<>());
        p.setIsHaveCar(0);
        p.setIsHaveHouse(0);
        p.setRate(businessBasicInfoReq.getBorrowRate());
        p.setRateUnitType(businessBasicInfoReq.getBorrowRateUnit());
        p.setOffLineInOverDueRate(new BigDecimal(0.1));
        p.setOffLineInOverDueRateType(2);
        p.setOffLineOutOverDueRate(new BigDecimal(0.1));
        p.setOffLineOutOverDueRateType(2);
        p.setOnLineOverDueRate(new BigDecimal(0.06));
		p.setOnLineOverDueRateType(2);
		p.setRepayType(businessBasicInfoReq.getRepaymentTypeId());
		p.setProjectId(projectId);
		p.setCustomerId(main.getCustomerId());
		p.setTdUserId("");
		p.setStatusFlag("2");
		p.setBeginTime(businessBasicInfoReq.getInputTime());
		p.setFullBorrowMoney(businessBasicInfoReq.getBorrowMoney());
		p.setBorrowLimit(businessBasicInfoReq.getBorrowLimit());
		p.setExtendFlag(0);
		p.setOrgIssueId("");
		p.setMasterIssueId(projectId);
		p.setIssueOrder(1);
		p.setQueryFullsuccessDate(businessBasicInfoReq.getInputTime());
		p.setTelNo(main.getPhoneNumber()==null?"":main.getPhoneNumber());
		p.setIdentityCard(main.getIdentifyCard());
		p.setRealName(main.getCustomerName());
		p.setBankAccountNo("");
		p.setBankType(0);
		p.setBankProvice("");
		p.setBankCity("");
		p.setOpenBankName("");
		p.setPeriodMonth(businessBasicInfoReq.getBorrowLimit());
		p.setRepaymentType(businessBasicInfoReq.getRepaymentTypeId());
		p.setAmount(businessBasicInfoReq.getBorrowMoney());
		p.setBranchCompanyId("");
		p.setControlDesc("");
		p.setTdStatus(4);
		p.setProjectType(0);
		p.setEnterpriseUserId("");
		p.setAviCreditGrantingAmount(new BigDecimal(0));
		p.setOverRate(new BigDecimal(0.1));
		p.setUserTypeId(main.getCustomerType().equals("个人")?1:2);
		p.setTuandaiAmount(new BigDecimal(0));
		p.setGuaranteeRate(new BigDecimal(0));
		p.setSubCompanyRate(new BigDecimal(0));
		p.setSubCompanyCharge(new BigDecimal(0));
		p.setAgencyId("");
		p.setAgencyRate(new BigDecimal(0));
		p.setAgencyAmount(new BigDecimal(0));
		p.setDepositAmount(new BigDecimal(0));
		p.setFreedAmount(BigDecimal.ZERO);
		p.setFreedRate(new BigDecimal(0));
		p.setCooperativeTdComUserId("");
		p.setCooperativeTdComRate(BigDecimal.ZERO);
		p.setCooperativeTdComAmount(BigDecimal.ZERO);
		p.setBorrowerRate(BigDecimal.ZERO);
		p.setBorrowAmount(businessBasicInfoReq.getBorrowMoney());
		p.setProjectFrom(0);
		p.setMonthPrincipalAmount(BigDecimal.ZERO);
		p.setPlateType(0);
		p.setTuandaiRate(BigDecimal.ZERO);
		p.setGuaranteeAmount(BigDecimal.ZERO);
		return p ;
	}
    public static void main(String[] args) {

//        BusinessBasicInfoReq  businessBasicInfo = new BusinessBasicInfoReq();
//        businessBasicInfo.setBorrowMoney(new BigDecimal(3000));
//
//
//        List<ProjInfoReq>tuandaiProjReqInfos  = new LinkedList<>();
//        ProjInfoReq  req1= new ProjInfoReq();
//        tuandaiProjReqInfos.add(req1);
//        req1.setFullBorrowMoney(new BigDecimal(1000));
//        req1.setProjectId("11");
//
//        ProjInfoReq  req2= new ProjInfoReq();
//        tuandaiProjReqInfos.add(req2);
//        req2.setFullBorrowMoney(new BigDecimal(1000));
//        req2.setProjectId("22");
//
//        ProjInfoReq  req3= new ProjInfoReq();
//        tuandaiProjReqInfos.add(req3);
//        req3.setFullBorrowMoney(new BigDecimal(1000));
//        req3.setProjectId("33");
//
//        Integer projIndex = 0;
//        BigDecimal setedPersent = new BigDecimal(0);
//        Map<String,BigDecimal>  projPersent = new HashMap<>();
//        for(ProjInfoReq projInfoReq:tuandaiProjReqInfos){
//            projIndex++;
//            //如果是最后一个，则用减的
//            if(projIndex.equals(tuandaiProjReqInfos.size())){
//                BigDecimal persent = new BigDecimal(1).subtract(setedPersent);
//                projPersent.put(projInfoReq.getProjectId(),persent);
//            }else{
//                BigDecimal persent = projInfoReq.getFullBorrowMoney().divide(businessBasicInfo.getBorrowMoney(),4,0);
//                setedPersent = setedPersent.add(persent);
//                projPersent.put(projInfoReq.getProjectId(),persent);
//            }
//        }
//        System.out.println(JSON.toJSONString(projPersent));

//
//        List<ProjExtRateReq> rateReqs = new LinkedList<>();
//
//        ProjExtRateReq rateReq = new ProjExtRateReq();
//        rateReqs.add(rateReq);
//        rateReq.setRateType(70);
//        rateReq.setRateValue(new BigDecimal(0.5));
//        rateReq.setCalcWay(1);
//        rateReq.setFeeId("79069922-e13a-4229-8656-2a1e19b44879");
//        rateReq.setBeginPeroid(1);
//
//
//        ProjExtRateReq rateReq1 = new ProjExtRateReq();
//        rateReqs.add(rateReq1);
//        rateReq1.setRateType(70);
//        rateReq1.setRateValue(new BigDecimal(0.5));
//        rateReq1.setCalcWay(1);
//        rateReq1.setFeeId("79069922-e13a-4229-8656-2a1e19b44879");
//        rateReq1.setBeginPeroid(3);
//
//        ProjExtRateReq rateReq2 = new ProjExtRateReq();
//        rateReqs.add(rateReq2);
//        rateReq2.setRateType(70);
//        rateReq2.setRateValue(new BigDecimal(0.5));
//        rateReq2.setCalcWay(1);
//        rateReq2.setFeeId("79069922-e13a-4229-8656-2a1e19b44879");
//        rateReq2.setBeginPeroid(2);
//
//
//
//        Collections.sort(rateReqs, new Comparator<ProjExtRateReq>() {
//            @Override
//            public int compare(ProjExtRateReq o1, ProjExtRateReq o2) {
//                if(o1.getBeginPeroid().compareTo(o2.getBeginPeroid())==0){
//                    return Integer.valueOf(o1.getBeginPeroid().compareTo(o2.getBeginPeroid()));
//                }else{
//                    return o1.getBeginPeroid().compareTo(o2.getBeginPeroid());
//                }
//            }
//        });
//
//        System.out.println(JSON.toJSONString(rateReqs));




//        BigDecimal t1 = new BigDecimal(7415.8400000);
//        t1= t1.setScale(2,BigDecimal.ROUND_HALF_DOWN);
//
//        BigDecimal t2 = new BigDecimal(7415.84);
//        t2= t2.setScale(2,BigDecimal.ROUND_HALF_DOWN);
//
////        new BigDecimal(7415.84).setScale(creatRepayPlanReq.getSmallNum(),creatRepayPlanReq.getRondmode())
//
//        Integer tt =t1.compareTo(t2);
//        System.out.println(tt);

//                bizPlanList.getTotalBorrowAmount().compareTo(new BigDecimal(7415.84));

//
//        //进位方式枚举
//         RoundingMode roundingMode=RoundingMode.HALF_UP;
//        //保留的小数位数
//         Integer smallNum=4;
//
//         BigDecimal projTBAmount = new BigDecimal(12.3333335476554634532213234);
//
//        projTBAmount= projTBAmount.setScale(2,BigDecimal.ROUND_HALF_DOWN);
//
//        System.out.println(projTBAmount);
// System.out.println(UUID.randomUUID().toString());
// System.out.println(UUID.randomUUID().toString());
// System.out.println(UUID.randomUUID().toString());
//
//        String periodStr=(new DecimalFormat("00")).format(120);
//        System.out.println(periodStr);

        //---------  还款本息   测试   开始----------//
//        Integer periodMonth = 36;
//        BigDecimal fullBorrowMoney = new BigDecimal(20000);
//        BigDecimal rate =new BigDecimal(11);
////        Integer periodMonth = 12;
////        BigDecimal fullBorrowMoney = new BigDecimal(120000);
////        BigDecimal rate =new BigDecimal(12);
//
//        RepayPlanBorrowRateUnitEnum rateUnit = RepayPlanBorrowRateUnitEnum.YEAR_RATE;
////        RepayPlanRepayIniCalcWayEnum repayType = RepayPlanRepayIniCalcWayEnum.INT_AND_PRIN_AVERAGE;//等本等息
////        RepayPlanRepayIniCalcWayEnum repayType = RepayPlanRepayIniCalcWayEnum.PRINCIPAL_LAST;//先息后本
//        RepayPlanRepayIniCalcWayEnum repayType = RepayPlanRepayIniCalcWayEnum.INT_AND_PRIN_EQUAL; //等额本息
////        RepayPlanRepayIniCalcWayEnum repayType = RepayPlanRepayIniCalcWayEnum.INT_AND_PRIN_EVERYTIME; //分期还本付息
//
//
//
//
//        CreatRepayPlanServiceImpl impl = new CreatRepayPlanServiceImpl();
//        impl.smallNum = 3;
//        List<PrincipleReq> principleMap = new LinkedList<>();
//
//        Map<Integer,Map<String,BigDecimal>> map = impl.calculateRepayPrinAndIni(periodMonth,fullBorrowMoney,rate,rateUnit,repayType,principleMap);
//
//
//        System.out.println(JSON.toJSONString(map));


//        BigDecimal ttt = new BigDecimal("2.1111589965");
//
//        ttt

        //---------  还款本息   测试   结束----------//
        //
//        BigDecimal big1 = new BigDecimal(100);
//        BigDecimal big2 = new BigDecimal(50);
//        System.out.println(big2.divide(big1));
    	RoundingMode roundingMode=RoundingMode.UP;
    	BigDecimal rate=BigDecimal.valueOf(11);
    	BigDecimal  monthRate = rate.divide(new BigDecimal(12),10,roundingMode);
    	System.out.println(monthRate);
    }


	
}
