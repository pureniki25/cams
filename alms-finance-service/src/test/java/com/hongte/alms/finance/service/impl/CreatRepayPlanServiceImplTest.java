package com.hongte.alms.finance.service.impl;

import com.alibaba.fastjson.JSON;
import com.hongte.alms.base.enums.BooleanEnum;
import com.hongte.alms.base.enums.UserTypeEnum;
import com.hongte.alms.base.enums.repayPlan.*;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.finance.FinanceServiceApplication;
import com.hongte.alms.finance.dto.repayPlan.RepaymentBizPlanDto;
import com.hongte.alms.finance.req.repayPlan.BusinessBasicInfoReq;
import com.hongte.alms.finance.req.repayPlan.CreatRepayPlanReq;
import com.hongte.alms.finance.req.repayPlan.ProjFeeReq;
import com.hongte.alms.finance.req.repayPlan.ProjInfoReq;
import com.hongte.alms.finance.service.CreatRepayPlanService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author zengkun
 * @since 2018/5/7
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FinanceServiceApplication.class)
public class CreatRepayPlanServiceImplTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreatRepayPlanServiceImplTest.class);


    @Autowired
    @Qualifier("CreatRepayPlanService")
    CreatRepayPlanService creatRepayPlanService;

//    @Test
    public void creatRepayPlanTest() throws Exception {

        CreatRepayPlanReq creatRepayPlanReq = new CreatRepayPlanReq();

        creatRepayPlanReq.setRondmode(0);
        creatRepayPlanReq.setSmallNum(4);
        creatRepayPlanReq.setPlateType(1);



        BusinessBasicInfoReq  businessBasicInfoReq =creatBusinessBasicInfoReq();
        creatRepayPlanReq.setBusinessBasicInfoReq(businessBasicInfoReq);


        List<ProjInfoReq> tuandaiProjReqInfos   = new LinkedList<>();
        creatRepayPlanReq.setProjInfoReqs(tuandaiProjReqInfos);

        //        private List<ProjFeeReq> projFeeInfos;
        ProjInfoReq req1 =creatProjInfoReq(businessBasicInfoReq);
        tuandaiProjReqInfos.add(req1);

        List<ProjFeeReq>   feeList1 = new LinkedList<>();
        req1.setProjFeeInfos(feeList1);
        ProjFeeReq feeReq10 = creatProjFeeReq(req1);
        feeList1.add(feeReq10);
        ProjFeeReq feeReq11 = creatProjFeeReq(req1);
        feeList1.add(feeReq11);
        ProjFeeReq feeReq12 = creatProjFeeReq(req1);
        feeList1.add(feeReq12);

        ProjInfoReq req2 =creatProjInfoReq(businessBasicInfoReq);
        tuandaiProjReqInfos.add(req2);

        List<ProjFeeReq>   feeList2 = new LinkedList<>();
        req2.setProjFeeInfos(feeList2);
        ProjFeeReq feeReq20 = creatProjFeeReq(req2);
        feeList2.add(feeReq20);
        ProjFeeReq feeReq21 = creatProjFeeReq(req2);
        feeList2.add(feeReq21);
        ProjFeeReq feeReq22 = creatProjFeeReq(req2);
        feeList2.add(feeReq22);


        List<RepaymentBizPlanDto>  planDtos = creatRepayPlanService.creatRepayPlan(creatRepayPlanReq);
        LOGGER.error(JSON.toJSONString(planDtos));

        System.out.println(planDtos);


    }



    @Test
    //无分段费用 创建还款计划  测试  需要核对与原信贷生成的还款计划的差异有多大
    public void  noFeeDetailCreatRepayPlanTest(){
        CreatRepayPlanReq creatRepayPlanReq = new CreatRepayPlanReq();

        creatRepayPlanReq.setRondmode(0);
        creatRepayPlanReq.setSmallNum(2);
        creatRepayPlanReq.setPlateType(1);



        BusinessBasicInfoReq  businessBasicInfoReq =creatBusinessBasicInfoReq();
        creatRepayPlanReq.setBusinessBasicInfoReq(businessBasicInfoReq);

        businessBasicInfoReq.setBusinessId("TDF1012018031505");
        businessBasicInfoReq.setOrgBusinessId("TDF1012018031505");
        businessBasicInfoReq.setInputTime(DateUtil.getDateTime("2018/3/15"));
        businessBasicInfoReq.setRepaymentTypeId(9);  //1：到期还本息，2：每月付息到期还本，5：等额本息，9：分期还本付息
        businessBasicInfoReq.setBorrowMoney(new BigDecimal(50000));  //借款总额
        businessBasicInfoReq.setBorrowLimit(6);  //借款期限



        List<ProjInfoReq> tuandaiProjReqInfos   = new LinkedList<>();
        creatRepayPlanReq.setProjInfoReqs(tuandaiProjReqInfos);

        //        private List<ProjFeeReq> projFeeInfos;
        ProjInfoReq req1 =creatProjInfoReq(businessBasicInfoReq);
        tuandaiProjReqInfos.add(req1);

        Map<Integer,BigDecimal> principleMap1 = new HashMap<>();
        req1.setPricipleMap(principleMap1);
        principleMap1.put(1,new BigDecimal(4200));
        principleMap1.put(2,new BigDecimal(4200));
        principleMap1.put(3,new BigDecimal(4200));
        principleMap1.put(4,new BigDecimal(4200));
        principleMap1.put(5,new BigDecimal(4200));
        principleMap1.put(6,new BigDecimal(9000));

        req1.setProjectId("137e8a4a-0727-4551-b20a-48b0d6679cfa");
        req1.setStatusFlag("4");
        req1.setBeginTime(DateUtil.getDateTime("2018/3/15")); // 启标时间(用于生成还款计划)
        req1.setFullBorrowMoney(new BigDecimal(30000)); // 满标金额
        req1.setTdLoanMoney(new BigDecimal(30000)); // 放款金额
        req1.setExtendFlag(0); // 是否是展期(0:不是展期,1:是展期)
        req1.setCatsedAmount(new BigDecimal(30000)); // 投资者已投金额
        req1.setAmount(new BigDecimal(30000)); // 总金额(元)
        req1.setMasterIssueId("670d149e-6b63-4810-b437-f993b0bc9af9"); // 主借标ID
        req1.setRate(new BigDecimal(9.5)); // 利率
        req1.setRateUnitType(1); // 利率单位：1 年利率; 2 月利率; 3 日利率
        req1.setOverDueRate(new BigDecimal(12)); // 逾期滞纳金费率(%)
        req1.setOverDueRateUnit(1); // 逾期滞纳金费率类型，1：年利率，2：月利率，3：日利率
        req1.setRepayType(RepayPlanRepayIniCalcWayEnum.INT_AND_PRIN_EVERYTIME.getKey()); // 还款方式：1：到期还本息，2：每月付息到期还本，5：等额本息，9：分期还本付息,11:等本等息
        req1.setPeriodMonth(6); // 借款期限  月



        List<ProjFeeReq>   feeList1 = new LinkedList<>();
        req1.setProjFeeInfos(feeList1);



        ProjFeeReq feeReq10 = creatProjFeeReq(req1);
        feeList1.add(feeReq10);
        feeReq10.setProjId("137e8a4a-0727-4551-b20a-48b0d6679cfa");
        feeReq10.setFeeItemId("203e172b-9102-4820-9e06-76a64aa119e3"); //费用项目ID
        feeReq10.setFeeItemName("月收分公司服务费"); //费用项目名称
        feeReq10.setFeeType(RepayPlanFeeTypeEnum.SUB_COMPANY_CHARGE.getValue()); //费用项类型
        feeReq10.setFeeTypeName("分公司费用"); //费用项名称
        feeReq10.setFeeValue(new BigDecimal(6)); //业务应收取费用值，如果按月收取，则存储按月收取的值，如800元/月收取服务费，此字段存储800。如果一次性收取，则存储应收总费用值
        feeReq10.setIsOneTimeCharge(RepayPlanIsOneTimeChargeEnum.BY_MONTH.getKey()); //是否一次收取，1为按月收取，2为一次收取
        feeReq10.setRepaymentFlag(2); //标记该项费用的还款类型，1:期初收取,2:期末收取


        ProjFeeReq feeReq11 = creatProjFeeReq(req1);
        feeList1.add(feeReq11);
        feeReq11.setFeeItemId("72f5c955-7a80-4758-a159-779f0e714042"); //费用项目ID
        feeReq11.setFeeItemName("平台费"); //费用项目名称
        feeReq11.setFeeType(RepayPlanFeeTypeEnum.PLAT_CHARGE.getValue()); //费用项类型
        feeReq11.setFeeTypeName("团贷网平台费用"); //费用项名称
        feeReq11.setFeeValue(new BigDecimal(3)); //业务应收取费用值，如果按月收取，则存储按月收取的值，如800元/月收取服务费，此字段存储800。如果一次性收取，则存储应收总费用值
        feeReq11.setIsOneTimeCharge(2); //是否一次收取，1为按月收取，2为一次收取
        feeReq11.setRepaymentFlag(1); //标记该项费用的还款类型，1:期初收取,2:期末收取


        ProjFeeReq feeReq12 = creatProjFeeReq(req1);
        feeList1.add(feeReq12);
        feeReq12.setFeeItemId("b163247d-41b7-40db-9b59-7c9337a5c659"); //费用项目ID
        feeReq12.setFeeItemName("分公司服务费"); //费用项目名称
        feeReq12.setFeeType(RepayPlanFeeTypeEnum.SUB_COMPANY_CHARGE.getValue()); //费用项类型
        feeReq12.setFeeTypeName("分公司费用    "); //费用项名称
        feeReq12.setFeeValue(new BigDecimal(2.1)); //业务应收取费用值，如果按月收取，则存储按月收取的值，如800元/月收取服务费，此字段存储800。如果一次性收取，则存储应收总费用值
        feeReq12.setIsOneTimeCharge(2); //是否一次收取，1为按月收取，2为一次收取
        feeReq12.setRepaymentFlag(1); //标记该项费用的还款类型，1:期初收取,2:期末收取



        ProjFeeReq feeReq13 = creatProjFeeReq(req1);
        feeList1.add(feeReq13);
        feeReq13.setFeeItemId("d0c27c1d-8963-401d-b909-e08725c43171"); //费用项目ID
        feeReq13.setFeeItemName("月收平台费"); //费用项目名称
        feeReq13.setFeeType(RepayPlanFeeTypeEnum.PLAT_CHARGE.getValue()); //费用项类型
        feeReq13.setFeeTypeName("团贷网平台费用"); //费用项名称
        feeReq13.setFeeValue(new BigDecimal(6)); //业务应收取费用值，如果按月收取，则存储按月收取的值，如800元/月收取服务费，此字段存储800。如果一次性收取，则存储应收总费用值
        feeReq13.setIsOneTimeCharge(1); //是否一次收取，1为按月收取，2为一次收取
        feeReq13.setRepaymentFlag(2); //标记该项费用的还款类型，1:期初收取,2:期末收取

        ProjFeeReq feeReq14 = creatProjFeeReq(req1);
        feeList1.add(feeReq14);
        feeReq14.setFeeItemId("db58adad-cb24-4b69-abba-26eba989a23f"); //费用项目ID
        feeReq14.setFeeItemName("担保费"); //费用项目名称
        feeReq14.setFeeType(RepayPlanFeeTypeEnum.BOND_COMPANY_CHARGE.getValue()); //费用项类型
        feeReq14.setFeeTypeName("担保公司费用  "); //费用项名称
        feeReq14.setFeeValue(new BigDecimal(3.3)); //业务应收取费用值，如果按月收取，则存储按月收取的值，如800元/月收取服务费，此字段存储800。如果一次性收取，则存储应收总费用值
        feeReq14.setIsOneTimeCharge(2); //是否一次收取，1为按月收取，2为一次收取
        feeReq14.setRepaymentFlag(1); //标记该项费用的还款类型，1:期初收取,2:期末收取




        ProjInfoReq req2 =creatProjInfoReq(businessBasicInfoReq);
        tuandaiProjReqInfos.add(req2);

        Map<Integer,BigDecimal> principleMap2 = new HashMap<>();
        req2.setPricipleMap(principleMap2);
        principleMap2.put(1,new BigDecimal(2800));
        principleMap2.put(2,new BigDecimal(2800));
        principleMap2.put(3,new BigDecimal(2800));
        principleMap2.put(4,new BigDecimal(2800));
        principleMap2.put(5,new BigDecimal(2800));
        principleMap2.put(6,new BigDecimal(6000));



        req2.setProjectId("670d149e-6b63-4810-b437-f993b0bc9af9");
        req2.setStatusFlag("4");
        req2.setBeginTime(DateUtil.getDateTime("2018/3/15")); // 启标时间(用于生成还款计划)
        req2.setFullBorrowMoney(new BigDecimal(20000)); // 满标金额
        req2.setTdLoanMoney(new BigDecimal(20000)); // 放款金额
        req2.setExtendFlag(0); // 是否是展期(0:不是展期,1:是展期)
        req2.setCatsedAmount(new BigDecimal(20000)); // 投资者已投金额
        req2.setAmount(new BigDecimal(20000)); // 总金额(元)
        req2.setMasterIssueId("670d149e-6b63-4810-b437-f993b0bc9af9"); // 主借标ID
        req2.setRate(new BigDecimal(9.5)); // 利率
        req2.setRateUnitType(1); // 利率单位：1 年利率; 2 月利率; 3 日利率
        req2.setOverDueRate(new BigDecimal(12)); // 逾期滞纳金费率(%)
        req2.setOverDueRateUnit(1); // 逾期滞纳金费率类型，1：年利率，2：月利率，3：日利率
        req2.setRepayType(RepayPlanRepayIniCalcWayEnum.INT_AND_PRIN_EVERYTIME.getKey()); // 还款方式：1：到期还本息，2：每月付息到期还本，5：等额本息，9：分期还本付息,11:等本等息
        req2.setPeriodMonth(6); // 借款期限  月




        List<ProjFeeReq>   feeList2 = new LinkedList<>();
        req2.setProjFeeInfos(feeList2);
        ProjFeeReq feeReq20 = creatProjFeeReq(req2);
        feeList2.add(feeReq20);
        feeReq20.setProjId("670d149e-6b63-4810-b437-f993b0bc9af9");
        feeReq20.setFeeItemId("203e172b-9102-4820-9e06-76a64aa119e3"); //费用项目ID
        feeReq20.setFeeItemName("月收分公司服务费"); //费用项目名称
        feeReq20.setFeeType(RepayPlanFeeTypeEnum.SUB_COMPANY_CHARGE.getValue()); //费用项类型
        feeReq20.setFeeTypeName("分公司费用"); //费用项名称
        feeReq20.setFeeValue(new BigDecimal(4)); //业务应收取费用值，如果按月收取，则存储按月收取的值，如800元/月收取服务费，此字段存储800。如果一次性收取，则存储应收总费用值
        feeReq20.setIsOneTimeCharge(1); //是否一次收取，1为按月收取，2为一次收取
        feeReq20.setRepaymentFlag(2); //标记该项费用的还款类型，1:期初收取,2:期末收取



        ProjFeeReq feeReq21 = creatProjFeeReq(req2);
        feeList2.add(feeReq21);
        feeReq21.setProjId("670d149e-6b63-4810-b437-f993b0bc9af9");
        feeReq21.setFeeItemId("72f5c955-7a80-4758-a159-779f0e714042"); //费用项目ID
        feeReq21.setFeeItemName("平台费"); //费用项目名称
        feeReq21.setFeeType(RepayPlanFeeTypeEnum.PLAT_CHARGE.getValue()); //费用项类型
        feeReq21.setFeeTypeName("团贷网平台费用"); //费用项名称
        feeReq21.setFeeValue(new BigDecimal(2)); //业务应收取费用值，如果按月收取，则存储按月收取的值，如800元/月收取服务费，此字段存储800。如果一次性收取，则存储应收总费用值
        feeReq21.setIsOneTimeCharge(2); //是否一次收取，1为按月收取，2为一次收取
        feeReq21.setRepaymentFlag(1); //标记该项费用的还款类型，1:期初收取,2:期末收取



        ProjFeeReq feeReq22 = creatProjFeeReq(req2);
        feeList2.add(feeReq22);
        feeReq22.setProjId("670d149e-6b63-4810-b437-f993b0bc9af9");
        feeReq22.setFeeItemId("b163247d-41b7-40db-9b59-7c9337a5c659"); //费用项目ID
        feeReq22.setFeeItemName("分公司服务费"); //费用项目名称
        feeReq22.setFeeType(RepayPlanFeeTypeEnum.SUB_COMPANY_CHARGE.getValue()); //费用项类型
        feeReq22.setFeeTypeName("分公司费用"); //费用项名称
        feeReq22.setFeeValue(new BigDecimal(1.4)); //业务应收取费用值，如果按月收取，则存储按月收取的值，如800元/月收取服务费，此字段存储800。如果一次性收取，则存储应收总费用值
        feeReq22.setIsOneTimeCharge(2); //是否一次收取，1为按月收取，2为一次收取
        feeReq22.setRepaymentFlag(1); //标记该项费用的还款类型，1:期初收取,2:期末收取


        ProjFeeReq feeReq23 = creatProjFeeReq(req2);
        feeList2.add(feeReq23);
        feeReq23.setProjId("670d149e-6b63-4810-b437-f993b0bc9af9");
        feeReq23.setFeeItemId("d0c27c1d-8963-401d-b909-e08725c43171"); //费用项目ID
        feeReq23.setFeeItemName("月收平台费"); //费用项目名称
        feeReq23.setFeeType(RepayPlanFeeTypeEnum.PLAT_CHARGE.getValue()); //费用项类型
        feeReq23.setFeeTypeName("团贷网平台费用"); //费用项名称
        feeReq23.setFeeValue(new BigDecimal(4)); //业务应收取费用值，如果按月收取，则存储按月收取的值，如800元/月收取服务费，此字段存储800。如果一次性收取，则存储应收总费用值
        feeReq23.setIsOneTimeCharge(1); //是否一次收取，1为按月收取，2为一次收取
        feeReq23.setRepaymentFlag(2); //标记该项费用的还款类型，1:期初收取,2:期末收取

        ProjFeeReq feeReq24 = creatProjFeeReq(req2);
        feeList2.add(feeReq24);
        feeReq24.setProjId("670d149e-6b63-4810-b437-f993b0bc9af9");
        feeReq24.setFeeItemId("db58adad-cb24-4b69-abba-26eba989a23f"); //费用项目ID
        feeReq24.setFeeItemName("担保费"); //费用项目名称
        feeReq24.setFeeType(RepayPlanFeeTypeEnum.BOND_COMPANY_CHARGE.getValue()); //费用项类型
        feeReq24.setFeeTypeName("担保公司费用"); //费用项名称
        feeReq24.setFeeValue(new BigDecimal(2.2)); //业务应收取费用值，如果按月收取，则存储按月收取的值，如800元/月收取服务费，此字段存储800。如果一次性收取，则存储应收总费用值
        feeReq24.setIsOneTimeCharge(2); //是否一次收取，1为按月收取，2为一次收取
        feeReq24.setRepaymentFlag(1); //标记该项费用的还款类型，1:期初收取,2:期末收取


        List<RepaymentBizPlanDto>  planDtos = creatRepayPlanService.creatRepayPlan(creatRepayPlanReq);
        LOGGER.error(JSON.toJSONString(planDtos));

        System.out.println("ttttttttttttttt");
    }

    @Test
    //有分段费用 创建还款计划  测试  需要核对与原信贷生成的还款计划的差异有多大
    public  void  withFeeDetailCreatRepayPlanTest(){

    }




    /**
     * 生成无分段费用的业务信息
     * @return
     */
    private BusinessBasicInfoReq  creatnoFeeDetailBusinessBasicInfoReq(){

        BusinessBasicInfoReq  businessBasicInfoReq = creatBusinessBasicInfoReq();


        businessBasicInfoReq.setBusinessId("TDF1012018031505");
        businessBasicInfoReq.setOrgBusinessId("TDF1012018031505");
        businessBasicInfoReq.setInputTime(DateUtil.getDateTime("2018/3/15"));
        businessBasicInfoReq.setRepaymentTypeId(9);  //1：到期还本息，2：每月付息到期还本，5：等额本息，9：分期还本付息
        businessBasicInfoReq.setBorrowMoney(new BigDecimal(50000));  //借款总额
        businessBasicInfoReq.setBorrowLimit(6);  //借款期限


        return businessBasicInfoReq;

    }



    /**
     * 生成业务的基本信息
     * @return
     */
    private BusinessBasicInfoReq  creatBusinessBasicInfoReq(){

        BusinessBasicInfoReq  businessBasicInfoReq = new BusinessBasicInfoReq();

        businessBasicInfoReq.setBusinessId("TBtestB1111");
        businessBasicInfoReq.setOrgBusinessId("TBtestB1111");
        businessBasicInfoReq.setInputTime(new Date());
        businessBasicInfoReq.setBusinessType(9);
        businessBasicInfoReq.setBusinessCtype("");  //业务所属子类型，若无则为空
        businessBasicInfoReq.setBusinessStype("");  //业务所属孙类型，若无则为空
        businessBasicInfoReq.setCustomerId("testCustom111");  //客户资产端唯一编号
        businessBasicInfoReq.setCustomerName("1111");  //客户姓名
        businessBasicInfoReq.setRepaymentTypeId(5);  //1：到期还本息，2：每月付息到期还本，5：等额本息，9：分期还本付息
        businessBasicInfoReq.setBorrowMoney(new BigDecimal(24000));  //借款总额
        businessBasicInfoReq.setBorrowLimit(12);  //借款期限
        businessBasicInfoReq.setBorrowRate(new BigDecimal(12));  //借款利率
        businessBasicInfoReq.setBorrowRateUnit(1);  //借款利率类型，1：年利率，2：月利率，3：日利率
        businessBasicInfoReq.setOperatorId("主办人111");  //业务主办人ID
        businessBasicInfoReq.setOperatorName("主办人 ttt");  //业务主办人姓名
        businessBasicInfoReq.setAssetId(UUID.randomUUID().toString());  //业务所属资产端编号
        businessBasicInfoReq.setCompanyId("东莞总部");  //业务所属分公司编号
        businessBasicInfoReq.setOutputPlatformId(1);  //出款平台ID，0：线下出款，1：团贷网P2P上标
        businessBasicInfoReq.setIssueSplitType(1);  //标识是否P2P拆标业务，0：非P2P拆标业务，1：P2P拆标业务
        businessBasicInfoReq.setSourceType(1);  //业务来源：0-常规录入 1-结清续贷新业务 2-结清续贷续贷业务 3-线下历史导入 4-扫码业务 5-优质车抵贷 6 -一点授信
        businessBasicInfoReq.setSourceBusinessId("9999999");  //原始来源业务的业务编号(当业务来源为结清再贷时，必填)
        businessBasicInfoReq.setIsTuandaiRepay(1);  //是否需要进行平台还款，1：是，0：否
        businessBasicInfoReq.setIsRenewBusiness(1);  //是否展期业务，1：是，0：否

        return businessBasicInfoReq;

    }


    /**
     * 创建标的信息
     * @param businessBasicInfoReq
     * @return
     */
    private  ProjInfoReq creatProjInfoReq(BusinessBasicInfoReq  businessBasicInfoReq){
        ProjInfoReq req1 = new ProjInfoReq();

        req1.setProjectId("TestProjId111");
        req1.setStatusFlag("2");
        req1.setBeginTime(DateUtil.getDateTime("2018-5-1")); // 启标时间(用于生成还款计划)
        req1.setFullBorrowMoney(new BigDecimal(12000)); // 满标金额
        req1.setAccounterConfirmUserId("user1111"); //  财务确认放款用户编号
        req1.setAccounterConfirmUserName("用户111"); //  财务确认放款人名称
        req1.setTdLoanTime(DateUtil.getDateTime("2018-5-1")); // 放款时间
        req1.setTdLoanMoney(new BigDecimal(12000)); // 放款金额
        req1.setExtendFlag(0); // 是否是展期(0:不是展期,1:是展期)
        req1.setCatsedAmount(new BigDecimal(12000)); // 投资者已投金额
        req1.setOrgIssueId("proj111111111"); // 原业务上标编号
        req1.setMasterIssueId("proj112133"); // 主借标ID
        req1.setIssueOrder(1); // 超额拆标共借项目的序号
        req1.setBusinessAfterGuid("b-fater-guid"); // 还款计划guid,只适合房贷
        req1.setQueryFullsuccessDate(DateUtil.getDateTime("2018-5-1")); // 满标时间(标的状态查询接口)
        req1.setNickName("nickName"); //昵称
        req1.setTelNo("TelNo"); //手机号码
        req1.setEmail("Email"); // 邮箱
        req1.setIdentityCard("identityCard"); // 身份证号码
        req1.setRealName("realName"); // 真实姓名
        req1.setRealName("realName"); // 真实姓名
        req1.setBankAccountNo("bankAccountNo"); // 银行卡
        req1.setBankType(1); // 银行类型
        req1.setBankProvice("银行卡归属地省"); // 银行卡归属地省
        req1.setBankCity("银行卡归属地市"); // 银行卡归属地市
        req1.setOpenBankName("开户银行名称"); // 开户银行名称
        req1.setTitle("标题"); // 标题
        req1.setPeriodMonth(12); // 借款期限  月
        req1.setRepaymentType(RepayPlanRepayIniCalcWayEnum.PRINCIPAL_LAST.getKey()); // 台还款方式ID
        req1.setAmount(new BigDecimal(12000)); // 总金额(元)
        req1.setLowerUnit(new BigDecimal(100)); // 最小投资单位(元)
        req1.setBranchCompanyId("东莞总部"); // 标的来源(所属分公司的分润用户ID)
        req1.setControlDesc("风险控制措施"); // 风险控制措施
        req1.setImageUrl("http:www.baidu.com"); // 标题图片
        req1.setTitleImageId("标题图片编号"); // 标题图片编号
        req1.setRemark("备注"); // 备注
        req1.setTdStatus(4); // 上标状态
//        req1.setProjectType(businessBasicInfoReq.getBusinessType()); // 上标状态
        req1.setResultContent("上标结果"); // 上标结果
        req1.setEnterpriseUserId("utryrtwer"); // 担保方编号(所属担保公司分润用户ID)
        req1.setAviCreditGrantingAmount(new BigDecimal(120000000)); // 担保公司可用金额

        req1.setRate(new BigDecimal(12)); // 利率
        req1.setRateUnitType(1); // 利率单位：1 年利率; 2 月利率; 3 日利率
        req1.setOverDueRate(new BigDecimal(12)); // 逾期滞纳金费率(%)
        req1.setOverDueRateUnit(1); // 逾期滞纳金费率类型，1：年利率，2：月利率，3：日利率
        req1.setRepayType(RepayPlanRepayIniCalcWayEnum.PRINCIPAL_LAST.getKey()); // 还款方式：1：到期还本息，2：每月付息到期还本，5：等额本息，9：分期还本付息,11:等本等息
        req1.setSex(PeayPlanSexEnum.MAN.getValue()); // 性别
        req1.setCredTypeId(1); // 证件类型
        req1.setBirthday(DateUtil.getDateTime("1987-5-1")); // 生日
        req1.setRiskAssessment("风险评估意见"); // 风险评估意见
        req1.setPlateUserId("团贷用户ID"); // 团贷用户ID
        req1.setUserTypeId(UserTypeEnum.PERSON.getKey()); // 客户类型 1:个人 2:企业
        req1.setMarriage("已婚"); // 婚姻状况, 已婚、未婚 (信用贷时必填)
        req1.setAddress("居住地址,详细地址，包括省份城市 (信用贷时必填)"); // 居住地址,详细地址，包括省份城市 (信用贷时必填)
        req1.setIsHaveHouse(BooleanEnum.NO.getValue()); // 是否有房产
        req1.setIsHaveCar(BooleanEnum.NO.getValue()); // 是否有车产

        return req1;

    }


    /**
     * 创建标的的费用明细信息
     * @return
     */
    private ProjFeeReq creatProjFeeReq(ProjInfoReq projInfoReq){
        ProjFeeReq  feeReq = new ProjFeeReq();


        feeReq.setIsTermRange(BooleanEnum.NO.getValue());
        feeReq.setFeeDetailReqMap(null);
        feeReq.setProjId(projInfoReq.getProjectId());
        feeReq.setFeeItemId(UUID.randomUUID().toString()); //费用项目ID
        feeReq.setFeeItemName("费用项目名称"); //费用项目名称
        feeReq.setFeeType(RepayPlanFeeTypeEnum.SUB_COMPANY_CHARGE.getValue()); //费用项类型
        feeReq.setFeeTypeName("费用项名称"); //费用项名称
        feeReq.setAccountStatus(RepayPlanAccountStatusEnum.DIVISION_TO_ASSET.getValue()); //分账标记
        feeReq.setFeeChargingType(2); //费用收取方式，1为按比例，2为按固定金额
        feeReq.setNewSystemDefaultRate(new BigDecimal(0)); //系统默认匹配的费用比例（当收取方式为2时，此字段存零）
        feeReq.setFeeValue(new BigDecimal(800)); //业务应收取费用值，如果按月收取，则存储按月收取的值，如800元/月收取服务费，此字段存储800。如果一次性收取，则存储应收总费用值
        feeReq.setWithdrawId(111); //对应的提现编号
        feeReq.setIsOneTimeCharge(RepayPlanIsOneTimeChargeEnum.BY_MONTH.getKey()); //是否一次收取，1为按月收取，2为一次收取
        feeReq.setWithdrawPlace(1); //放款去处 1:提到银行卡  2：转到可用金额
        feeReq.setOutputFlag(1); //标记该项费用是否单独收取，null或0:不单独收取，1:单独收取
        feeReq.setRepaymentFlag(1); //标记该项费用的还款类型，1:期初收取,2:期末收取

        feeReq.setIsP2PMainmarkCollect(1); //是否P2P主标收取,1为是，0为否
        feeReq.setIsRecordIncome(BooleanEnum.YES.getValue()); //是否可记为收入,1为是，0为否

        return feeReq;

    }



}