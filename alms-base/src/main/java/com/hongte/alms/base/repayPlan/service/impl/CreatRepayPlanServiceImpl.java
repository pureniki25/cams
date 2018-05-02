package com.hongte.alms.base.repayPlan.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.baseException.CreatRepaymentExcepiton;
import com.hongte.alms.base.entity.RepaymentProjPlan;
import com.hongte.alms.base.entity.RepaymentProjPlanList;
import com.hongte.alms.base.entity.RepaymentProjPlanListDetail;
import com.hongte.alms.base.enums.RepayPlanStatus;
import com.hongte.alms.base.repayPlan.enums.*;
import com.hongte.alms.base.repayPlan.req.*;
import com.hongte.alms.base.repayPlan.service.CreatRepayPlanService;
import com.hongte.alms.base.service.RepaymentProjPlanService;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.util.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import springfox.documentation.spring.web.json.Json;

import java.math.BigDecimal;
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


    //需要判断是否重复传入
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean creatRepayPlan(CreatRepayPlanReq creatRepayPlanReq) {

        //1、根据业务和标的对应关系，判断出需要生成几个还款计划，每个还款计划相对于业务总金额占的比例
        // 1))业务基本信息
        BusinessBasicInfoReq  businessBasicInfo = creatRepayPlanReq.getBusinessBasicInfoReq();

        List<BizOutPutPlanReq> outPutPlanList  = creatRepayPlanReq.getOutPutPlanList();

        List<TuandaiProjInfoReq> tuandaiProjReqInfos = creatRepayPlanReq.getTuandaiProjReqInfos();

        //计算每个标 在占整个业务的比例
        Map<String,BigDecimal>  projPersent = new HashMap<>();
        for(TuandaiProjInfoReq projInfoReq:tuandaiProjReqInfos){
            BigDecimal persent = projInfoReq.getFullBorrowMoney().divide(businessBasicInfo.getBorrowMoney());
            projPersent.put(projInfoReq.getProjectId(),persent);
        }

        String bizPlanId = UUID.randomUUID().toString();
        List<RepaymentProjPlan> repaymentProjPlanList = new LinkedList<>();

        //标的planId对应的List列表
        Map<String,List<RepaymentProjPlanList>>  repaymentPlanListMap = new HashMap<>();

        //期数对应的标List列表
        //Map<期数，标还款计划费用详情列表>
        Map<Integer,List<RepaymentProjPlanList>>  repaymentPlanListPeriorMap = new HashMap<>();

        //期数 费用类型对应的标ListDetail列表
        //Map<期数，Map<费用类型fee_id,标还款计划费用详情列表>>
        Map<Integer,Map<String,List<RepaymentProjPlanListDetail>>>  RepaymentProjPlanListDetailPeriorMap = new HashMap<>();


        //标的还款计划List对应的detail列表
        Map<String,List<RepaymentProjPlanListDetail>> repaymentPlanListDetailMap = new HashMap<>();


        //计算每个标的还款计划列表
        for(TuandaiProjInfoReq projInfoReq:tuandaiProjReqInfos){
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
            repaymentProjPlan.setBusinessId(projInfoReq.getBusinessId());
            repaymentProjPlan.setOriginalBusinessId(projInfoReq.getBusinessId());
            repaymentProjPlan.setRepaymentBatchId(UUID.randomUUID().toString());  //还款计划批次号  需要与咏康核对 怎么写
            repaymentProjPlan.setPlanId(bizPlanId);   //业务还款计划ID
            repaymentProjPlan.setBorrowMoney(projInfoReq.getAmount());  //生成还款计划对应的借款总额
            repaymentProjPlan.setBorrowRate(projInfoReq.getInterestRate());  //生成还款计划对应的借款利率
            repaymentProjPlan.setBorrowRateUnit(RepayPlanBorrowRateUnitEnum.YEAR_RATE.getValue());//利率类型
            repaymentProjPlan.setBorrowLimit(projInfoReq.getPeriodMonth());//借款期限
            repaymentProjPlan.setBorrowLimitUnit(RepayPlanBorrowLimitUnitEnum.MONTH.getValue());//借款期限单位
            repaymentProjPlan.setPlanStatus(RepayPlanStatus.REPAYING.getKey());//还款计划状态
            repaymentProjPlan.setIsDefer(RepayPlanIsDeferEnum.NO.getValue());//是否展期还款计划
            repaymentProjPlan.setActive(RepayPlanActiveEnum.ACTIVE.getValue());//是否有效标志位
            repaymentProjPlan.setCreateTime(new Date());
            repaymentProjPlan.setCreateUser(Constant.SYS_DEFAULT_USER);

            repaymentProjPlanList.add(repaymentProjPlan);


            ///////  标的还款计划00期   一次性收取的费用信息  开始   ///////////////
            ////00期List 信息
            RepaymentProjPlanList  zeroList =  creatRepaymentProjPlanList(repaymentProjPlan,0);// new RepaymentProjPlanList();
//            zeroList.setProjPlanListId(UUID.randomUUID().toString());
//            zeroList.setProjPlanId(repaymentProjPlan.getProjPlanId());
//            zeroList.setPlanListId("");//对应业务还款计划列表编号(外键，对应tb_repayment_biz_plan_list.plan_list_id)
//            zeroList.setPlanId(""); //所属还款计划编号(外键，对应tb_repayment_biz_plan.plan_id)
//            zeroList.setBusinessId(repaymentProjPlan.getBusinessId());  //还款计划所属业务编号(若当前业务为展期，则存展期业务编号)
//            zeroList.setOrigBusinessId(repaymentProjPlan.getOriginalBusinessId());  //还款计划所属原业务编号
//            zeroList.setPeriod(0);  //还款计划期数
//            zeroList.setAfterId(""); // 总批次期数，  核对原来信贷还款计划是怎么写的
//            zeroList.setDueDate(new Date()); //应还日期 怎么设置， 需要核对
//            zeroList.setTotalBorrowAmount(new BigDecimal(0));// 总计划应还金额   需要按照每一项计算
//            zeroList.setOverdueAmount(new BigDecimal(0)); //总应还滞纳金
//            zeroList.setOverdueDays(new BigDecimal(0)); //逾期天数
//            zeroList.setCurrentStatus(RepayPlanStatus.REPAYED.getName()); //当前还款状态  00期的直接置位为已还款
//            zeroList.setRepayFlag(RepayPlanPayedTypeEnum.GET_MONEY_PAY.getValue()); //已还款类型标记
//            zeroList.setConfirmFlag(RepayPlanConfirmFlageEnum.NEVER_CHECK.getValue()); //财务确认状态
//            zeroList.setAccountantConfirmStatus(RepayPlanAccountConfirmStautsEnum.WAIT.getValue());//会计确认状态
//            zeroList.setProjDueDate(new Date());//标的资金端（平台）应还日期
//            zeroList.setProjOverdueAmount(new BigDecimal(0));//标的当前期资金端总应还滞纳金(元)
//            zeroList.setProjAmount(new BigDecimal(0));//标的当前期资金端总应还金额(元)，不含滞纳金
//            zeroList.setActive(RepayPlanActiveEnum.ACTIVE.getValue());//设置是否有效标志位
//            zeroList.setCreateTime(new Date());//设置创建时间
//            zeroList.setCreateUser(Constant.SYS_DEFAULT_USER);//设置创建用户
            //将标的00期写入还款计划map
            List<RepaymentProjPlanList> planList = repaymentPlanListPeriorMap.get(0);
            if(planList == null){
                planList = new LinkedList<>();
                repaymentPlanListPeriorMap.put(0,planList);
            }
            planList.add(zeroList);



            List<ProjOutputFeeReq> projOutputFeeReqs =  projInfoReq.getProjOutputFeeInfos();

            List<RepaymentProjPlanListDetail>  zeroListDetails = new LinkedList<>();

            if(projOutputFeeReqs!=null && projOutputFeeReqs.size()>0){
                for(ProjOutputFeeReq feeReq:projOutputFeeReqs){
                    //如果费用是一次性收取
                    if(feeReq.getIsOneTimeCharge().equals(RepayPlanIsOneTimeChargeEnum.ONE_TIME.getKey())){
                        RepaymentProjPlanListDetail   zeroListDetail = creatProjListDetail(zeroList);
                        zeroListDetail.setPlanAmount(feeReq.getFeeValue());//项目计划应还总金额(元)
                        zeroListDetail.setPlanRate(new BigDecimal(0));//项目计划应还比例(%)，如0.5%则存0.5，可空
                        zeroListDetail.setFeeId(feeReq.getFeeItemId());//资产端费用项ID，用于资产端区分同名的项目，若不存在同名费用项，可为空
                        zeroListDetail.setPlanItemName(feeReq.getFeeTypeName());//应还项目名称
                        zeroListDetail.setPlanItemType(feeReq.getFeeType());//应还项目所属分类
                        zeroListDetail.setAccountStatus(feeReq.getAccountStatus());//分账标记
                        //分润顺序项 需要填
//                        Map<Integer,Map<String,List<RepaymentProjPlanListDetail>>>  RepaymentProjPlanListDetailPeriorMap = new HashMap<>();
                        Map<String,List<RepaymentProjPlanListDetail>> feeIdMap = RepaymentProjPlanListDetailPeriorMap.get(0);
                        if(feeIdMap == null){
                            feeIdMap = new HashMap<>();
                            RepaymentProjPlanListDetailPeriorMap.put(0,feeIdMap);
                        }
                        List<RepaymentProjPlanListDetail> pDetailList = feeIdMap.get(feeReq.getFeeItemId());
                        if(pDetailList == null){
                            pDetailList = new LinkedList<>();
                            feeIdMap.put(feeReq.getFeeItemId(),pDetailList);
                        }
                        pDetailList.add(zeroListDetail);
                    }
                }
            }

            ///////  标的还款计划00期   一次性收取的费用信息  开始   ///////////////

            //////   标的其他期还款计划   按月收取费用信息   开始  ///////////////

            for(int i=1;i<projInfoReq.getPeriodMonth()+1;i++){



            }



            //////   标的其他期还款计划   按月收取费费用信息   结束  ///////////////




        }

        return null;
    }


    //创建标的还款计划，并设置基本信息
    private   RepaymentProjPlanList  creatRepaymentProjPlanList(RepaymentProjPlan repaymentProjPlan,Integer period){
        RepaymentProjPlanList  projPlanList = new RepaymentProjPlanList();
        projPlanList.setProjPlanListId(UUID.randomUUID().toString());
        projPlanList.setProjPlanId(repaymentProjPlan.getProjPlanId());
        projPlanList.setPlanListId("");//对应业务还款计划列表编号(外键，对应tb_repayment_biz_plan_list.plan_list_id)
        projPlanList.setPlanId(""); //所属还款计划编号(外键，对应tb_repayment_biz_plan.plan_id)
        projPlanList.setBusinessId(repaymentProjPlan.getBusinessId());  //还款计划所属业务编号(若当前业务为展期，则存展期业务编号)
        projPlanList.setOrigBusinessId(repaymentProjPlan.getOriginalBusinessId());  //还款计划所属原业务编号
        projPlanList.setPeriod(period);  //还款计划期数
        projPlanList.setAfterId(""); // 总批次期数，  核对原来信贷还款计划是怎么写的
        projPlanList.setDueDate(new Date()); //应还日期 怎么设置， 需要核对
        projPlanList.setTotalBorrowAmount(new BigDecimal(0));// 总计划应还金额   需要按照每一项计算
        projPlanList.setOverdueAmount(new BigDecimal(0)); //总应还滞纳金
        projPlanList.setOverdueDays(new BigDecimal(0)); //逾期天数
        projPlanList.setCurrentStatus(RepayPlanStatus.REPAYED.getName()); //当前还款状态  00期的直接置位为已还款
        projPlanList.setRepayFlag(RepayPlanPayedTypeEnum.GET_MONEY_PAY.getValue()); //已还款类型标记
        projPlanList.setConfirmFlag(RepayPlanConfirmFlageEnum.NEVER_CHECK.getValue()); //财务确认状态
        projPlanList.setAccountantConfirmStatus(RepayPlanAccountConfirmStautsEnum.WAIT.getValue());//会计确认状态
        projPlanList.setProjDueDate(new Date());//标的资金端（平台）应还日期
        projPlanList.setProjOverdueAmount(new BigDecimal(0));//标的当前期资金端总应还滞纳金(元)
        projPlanList.setProjAmount(new BigDecimal(0));//标的当前期资金端总应还金额(元)，不含滞纳金
        projPlanList.setActive(RepayPlanActiveEnum.ACTIVE.getValue());//设置是否有效标志位
        projPlanList.setCreateTime(new Date());//设置创建时间
        projPlanList.setCreateUser(Constant.SYS_DEFAULT_USER);//设置创建用户

        return projPlanList;
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
            RepayPlanRepayTypeEnum repayType){

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
    private void calcPrincipalLast(BigDecimal fullBorrowMoney,BigDecimal monthRate,Integer periodMonth ,Map<Integer,Map<String,BigDecimal>>  retMap)
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
     * 计算分期还本付息  每月本金  利息列表
     * @param fullBorrowMoney
     * @param monthRate
     * @param periodMonth
     * @param retList
     */
    private void calcIntAndPrinEqual(BigDecimal fullBorrowMoney,BigDecimal monthRate,Integer periodMonth ,Map<Integer,Map<String,BigDecimal>>  retList){
//先计算 每期总应还   再计算 应还本金   再相减得出应还利息
        //最后一期  应还本金 为总应还本金 - 之前期已还本金总和

        //每月总应还
        //     应还=贷款金额*月利率*(1+月利率)^贷款期限/(((1+月利率)^贷款期限)-1)
        BigDecimal monthTotalRepay =  fullBorrowMoney.multiply(monthRate).multiply(monthRate.add(new BigDecimal(1))).pow(periodMonth)
                .divide(((monthRate.add(new BigDecimal(1))).pow(periodMonth).subtract(new BigDecimal(1))));

        //之前期已还金额
        BigDecimal payedPriciple = new BigDecimal(0);

        for(int i=0;i<periodMonth;i++){
            //不是最后一期
            BigDecimal priciple = new BigDecimal(0);
            if(i!=periodMonth-1){
//                    本金=贷款金额*月利率*（1+月利率）^(第N次还款-1)/（(（1+月利率）^贷款期限)-1）
                priciple = fullBorrowMoney.multiply(monthRate).multiply(monthRate.add(new BigDecimal(1))).pow(i-1)
                        .divide((monthRate.add(new BigDecimal(1))).pow(periodMonth).subtract(new BigDecimal(1)));
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
    private void calcintAndPrinEverytime(BigDecimal fullBorrowMoney,BigDecimal monthRate,Integer periodMonth ,Map<Integer,Map<String,BigDecimal>>  retList){
        //每月应还本金
        BigDecimal priciple = fullBorrowMoney.divide(new BigDecimal(periodMonth));
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
    private void calcIntAndPrinAverage(BigDecimal fullBorrowMoney,BigDecimal monthRate,Integer periodMonth ,Map<Integer,Map<String,BigDecimal>>  retMap){
        //每月利息
        BigDecimal interest =  fullBorrowMoney.multiply(monthRate);
        //每月本金
        BigDecimal priciple = fullBorrowMoney.divide(new BigDecimal(periodMonth));
        for (int i=0;i<periodMonth;i++){
            addPAndIToList(priciple,interest,retMap,i);
        }
    }



    /**
     * 把本金和利息添加到列表中
     * @param priciple
     * @param interest
     * @param retList
     */
    private void addPAndIToList(BigDecimal priciple,BigDecimal interest,Map<Integer,Map<String,BigDecimal>>  retList,Integer peroid ){
        Map<String,BigDecimal> monthMap = new HashMap<>();
        monthMap.put("principal",priciple);
        monthMap.put("interest",interest);
        retList.put(peroid+1,monthMap);
    }


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


    private RepaymentProjPlanListDetail creatProjListDetail(RepaymentProjPlanList projPlanList){
        RepaymentProjPlanListDetail   projPlanListDetail = new RepaymentProjPlanListDetail();
        projPlanListDetail.setProjPlanDetailId(UUID.randomUUID().toString());
        projPlanListDetail.setProjPlanListId(projPlanList.getProjPlanListId());
        projPlanListDetail.setPlanDetailId("");//  所属还款计划列表详情ID(外键，对应tb_repayment_biz_plan_list.plan_list_id)
        projPlanListDetail.setPlanListId("");//  所属还款计划列表详情ID(外键，对应tb_repayment_biz_plan_list.plan_list_id)
        projPlanListDetail.setBusinessId(projPlanList.getBusinessId());//  所属还款计划列表详情ID(外键，对应tb_repayment_biz_plan_list.plan_list_id)
        projPlanListDetail.setPeriod(projPlanList.getPeriod()); //所属期数
        projPlanListDetail.setActive(RepayPlanActiveEnum.ACTIVE.getValue());
        projPlanListDetail.setCreateDate(new Date());
        projPlanListDetail.setCreateUser(Constant.SYS_DEFAULT_USER);

        return projPlanListDetail;
    }



    public static void main(String[] args) {
//        Integer periodMonth,BigDecimal fullBorrowMoney,
//                BigDecimal rate,RepayPlanBorrowRateUnitEnum rateUnit,
//                RepayPlanRepayTypeEnum repayType
        Integer periodMonth = 12;
        BigDecimal fullBorrowMoney = new BigDecimal(120000);
        BigDecimal rate =new BigDecimal(12);
        RepayPlanBorrowRateUnitEnum rateUnit = RepayPlanBorrowRateUnitEnum.YEAR_RATE;
//        RepayPlanRepayTypeEnum repayType = RepayPlanRepayTypeEnum.INT_AND_PRIN_AVERAGE;//等本等息
        RepayPlanRepayTypeEnum repayType = RepayPlanRepayTypeEnum.PRINCIPAL_LAST;//先息后本
//        RepayPlanRepayTypeEnum repayType = RepayPlanRepayTypeEnum.INT_AND_PRIN_EQUAL; //等额本息
//        RepayPlanRepayTypeEnum repayType = RepayPlanRepayTypeEnum.INT_AND_PRIN_EVERYTIME; //分期还本付息

        CreatRepayPlanServiceImpl impl = new CreatRepayPlanServiceImpl();

        Map<Integer,Map<String,BigDecimal>> map = impl.calculateRepayPrinAndIni(periodMonth,fullBorrowMoney,rate,rateUnit,repayType);

        System.out.println(JSON.toJSONString(map));
//
//        BigDecimal big1 = new BigDecimal(100);
//        BigDecimal big2 = new BigDecimal(50);
//        System.out.println(big2.divide(big1));
    }
}
