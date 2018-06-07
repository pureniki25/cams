package com.hongte.alms.finance.service.impl;

import com.alibaba.fastjson.JSON;
import com.hongte.alms.base.RepayPlan.dto.PlanReturnInfoDto;
import com.hongte.alms.base.RepayPlan.dto.RepaymentBizPlanDto;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.RepaymentBizPlan;
import com.hongte.alms.base.enums.BooleanEnum;
import com.hongte.alms.base.enums.UserTypeEnum;
import com.hongte.alms.base.enums.repayPlan.*;
import com.hongte.alms.base.mapper.BasicBizCustomerMapper;
import com.hongte.alms.base.mapper.BasicBusinessMapper;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.finance.FinanceServiceApplication;
import com.hongte.alms.base.RepayPlan.req.*;
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

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertNull;
import static junit.framework.TestCase.assertTrue;

/**
 * @author zengkun
 * @since 2018/5/7
 */
/**
 * @author 王继光
 * 2018年5月18日 下午6:04:49
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FinanceServiceApplication.class)
public class CreatRepayPlanServiceImplTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(CreatRepayPlanServiceImplTest.class);


    @Autowired
    @Qualifier("CreatRepayPlanService")
    CreatRepayPlanService creatRepayPlanService;
    @Autowired
    BasicBusinessMapper basicBusinessMapper ;
    @Autowired
	BasicBizCustomerMapper basicBizCustomerMapper ;

//    @Test
    public void creatRepayPlanTest() throws Exception {

        CreatRepayPlanReq creatRepayPlanReq = new CreatRepayPlanReq();

        creatRepayPlanReq.setRondmode(0);
        creatRepayPlanReq.setSmallNum(4);
//        creatRepayPlanReq.setPlateType(1);



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

        PlanReturnInfoDto planReturnInfoDto =creatRepayPlanService.creatRepayPlan(creatRepayPlanReq);
        List<RepaymentBizPlanDto>  planDtos =planReturnInfoDto.getRepaymentBizPlanDtos();
        LOGGER.error(JSON.toJSONString(planDtos));

        System.out.println(planDtos);


    }



//    @Test
    //无分段费用 创建还款计划  测试  需要核对与原信贷生成的还款计划的差异有多大
    public void  noFeeDetailCreatRepayPlanTest(){
        CreatRepayPlanReq creatRepayPlanReq =creatNoFeeCreatReq();

        List<RepaymentBizPlanDto>  planDtos = null;
        try {
            PlanReturnInfoDto planReturnInfoDto= creatRepayPlanService.creatRepayPlan(creatRepayPlanReq);
            planDtos = planReturnInfoDto.getRepaymentBizPlanDtos();

        } catch (InstantiationException e) {
            assertNotNull(e);
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            assertNotNull(e);
            e.printStackTrace();
        }
        LOGGER.error(JSON.toJSONString(planDtos));

//        planDtos.get(0).getBizPlanListDtos().get(0).getRepaymentBizPlanList()

        System.out.println("ttttttttttttttt");
    }



    @Test
    //创建并存储还款计划
    public void SaveInfoTest() {
        CreatRepayPlanReq creatRepayPlanReq =creatNoFeeCreatReq();

        try {
            PlanReturnInfoDto planReturnInfoDto= creatRepayPlanService.creatAndSaveRepayPlan(creatRepayPlanReq);
            LOGGER.error(JSON.toJSONString(planReturnInfoDto));
            List<RepaymentBizPlanDto> dto =planReturnInfoDto.getRepaymentBizPlanDtos();
            RepaymentBizPlanDto bizPlanDto = dto.get(0);
            RepaymentBizPlan repaymentBizPlan = bizPlanDto.getRepaymentBizPlan();

//            boolean b= repaymentBizPlan.getBorrowMoney().compareTo(new BigDecimal("50000.00"))==0;

//            assertTrue(repaymentBizPlan.getBorrowMoney().compareTo(new BigDecimal(50000.00))==0);
//            assertTrue(repaymentBizPlan.getBorrowRate().compareTo(new BigDecimal(9.5))==0);
//            assertTrue(repaymentBizPlan.getBorrowLimit().equals(6));


//            List<RepaymentBizPlanListDto>  bizPlanListDtos = BizPlanDto.getBizPlanListDtos();

//            for(RepaymentBizPlanListDto bizPlanDto1: bizPlanListDtos){
//                RepaymentBizPlanList bizPlanList = bizPlanDto1.getRepaymentBizPlanList();
//                if(bizPlanList.getPeriod().equals(1)){
//                    assertTrue(bizPlanList.getAfterId().equals("1-01"));
//                    BigDecimal gd1 =new BigDecimal(7415.84).setScale(creatRepayPlanReq.getSmallNum(),creatRepayPlanReq.getRondmode());
//                    BigDecimal gd2 = bizPlanList.getTotalBorrowAmount();
//                    Integer tt =bizPlanList.getTotalBorrowAmount().compareTo(gd1);
//                    assertTrue(bizPlanList.getTotalBorrowAmount().compareTo(new BigDecimal(7415.84).setScale(creatRepayPlanReq.getSmallNum(),creatRepayPlanReq.getRondmode()))==0);
//                }
//                if(bizPlanList.getPeriod().equals(2)){
//                    assertTrue(bizPlanList.getAfterId().equals("1-02"));
//                    assertTrue(bizPlanList.getTotalBorrowAmount().compareTo(new BigDecimal(7360.42).setScale(creatRepayPlanReq.getSmallNum(),creatRepayPlanReq.getRondmode()))==0);
//                }
//                if(bizPlanList.getPeriod().equals(3)){
//                    assertTrue(bizPlanList.getAfterId().equals("1-03"));
//                    assertTrue(bizPlanList.getTotalBorrowAmount().compareTo(new BigDecimal(7305).setScale(creatRepayPlanReq.getSmallNum(),creatRepayPlanReq.getRondmode()))==0);
//                }
//                if(bizPlanList.getPeriod().equals(4)){
//                    assertTrue(bizPlanList.getAfterId().equals("1-04"));
//                    assertTrue(bizPlanList.getTotalBorrowAmount().compareTo(new BigDecimal(7249.59).setScale(creatRepayPlanReq.getSmallNum(),creatRepayPlanReq.getRondmode()))==0);
//                }
//                if(bizPlanList.getPeriod().equals(5)){
//                    assertTrue(bizPlanList.getAfterId().equals("1-05"));
//                    assertTrue(bizPlanList.getTotalBorrowAmount().compareTo(new BigDecimal(7194.17).setScale(creatRepayPlanReq.getSmallNum(),creatRepayPlanReq.getRondmode()))==0);
//                }
//                if(bizPlanList.getPeriod().equals(6)){
//                    assertTrue(bizPlanList.getAfterId().equals("1-06"));
//                    assertTrue(bizPlanList.getTotalBorrowAmount().compareTo(new BigDecimal(15138.75).setScale(creatRepayPlanReq.getSmallNum(),creatRepayPlanReq.getRondmode()))==0);
//                }
//            }

//            50000.0000


        } catch (IllegalAccessException e) {
            assertNull(e);
            e.printStackTrace();
        } catch (InstantiationException e) {
            assertNull(e);
            e.printStackTrace();
        }
    }


    public CreatRepayPlanReq  creatNoFeeCreatReq(){
        CreatRepayPlanReq creatRepayPlanReq = new CreatRepayPlanReq();

        creatRepayPlanReq.setRondmode(0);
        creatRepayPlanReq.setSmallNum(2);
//        creatRepayPlanReq.setPlateType(1);



        BusinessBasicInfoReq  businessBasicInfoReq =creatBusinessBasicInfoReq();
        creatRepayPlanReq.setBusinessBasicInfoReq(businessBasicInfoReq);

        businessBasicInfoReq.setBusinessId("TDF1012018032101");
        businessBasicInfoReq.setOrgBusinessId("TDF1012018032101");
        businessBasicInfoReq.setInputTime(DateUtil.getDateTime("2018/3/21"));
        businessBasicInfoReq.setRepaymentTypeId(5);  //1：到期还本息，2：每月付息到期还本，5：等额本息，9：分期还本付息
        businessBasicInfoReq.setBorrowMoney(new BigDecimal("30000"));  //借款总额
        businessBasicInfoReq.setBorrowLimit(6);  //借款期限
        businessBasicInfoReq.setBorrowRateUnit(1);  //借款期限


        List<BusinessCustomerInfoReq> businessCustomerInfoReqs = new LinkedList<>();
        creatRepayPlanReq.setBizCusInfoReqs(businessCustomerInfoReqs);

        BusinessCustomerInfoReq businessCustomerInfoReq1 = new BusinessCustomerInfoReq();
        businessCustomerInfoReqs.add(businessCustomerInfoReq1);
        businessCustomerInfoReq1.setCustomerId(UUID.randomUUID().toString());  //客户ID，资产端主键
        businessCustomerInfoReq1.setIsmainCustomer(0); //是否主借款人，0：否，1：是
        businessCustomerInfoReq1.setCustomerName("测试-个人"); //客户名称，个人则填个人名称，企业则填企业名称
        businessCustomerInfoReq1.setIsReceiptAccount(0); //是否收款账户，0：否，1：是
        businessCustomerInfoReq1.setCustomerType("个人"); //客户类型：个人，企业
        businessCustomerInfoReq1.setIdentifyCard("442374790-42122355676"); //客户身份证唯一标识，当客户为个人时，此字段存身份证，当客户类型为企业时，三证合一时，存统一社会信用代码，非三证合一时存营业执照号
        businessCustomerInfoReq1.setPhoneNumber("183203498543821"); //客户接收短信的手机号码，当客户类型为企业时，此字段保存联系人手机号码
//        businessCustomerInfoReq1.setIsmainlandResident(1); //客户是否大陆居民，0或null：否，1：是
//        businessCustomerInfoReq1.setIsCompanyBankAccount(0); //是否提供对公账号,null或0为否，1为是 客户为企业需要填写
//        businessCustomerInfoReq1.setIsMergedCertificate(0); //是否三证合一,null或0为否，1为是 客户为企业需要填写，三证合一是指用【统一社会信用代码】代替以上三证。如果是三证合一，那么只需填写【统一社会信用代码】（必填），如果非三证合一，那么需填写【开户许可证】（必填）、【组织机构代码】、【营业执照编号】（必填）
//        businessCustomerInfoReq1.setUnifiedCode(""); //统一社会信用代码 客户为企业并且为三证合一时必须填写并作为tb_business表，绑卡表的外键
//        businessCustomerInfoReq1.setBusinessLicence("1111111"); //营业执照号 客户为企业时并且非三证合一时必须填写，并作为tb_business，绑卡表的外键
//        businessCustomerInfoReq1.setRegisterProvince("1111111"); //企业注册地址所在省份 客户为企业时需要填写
//        businessCustomerInfoReq1.setCompanyLegalPerson("1111111"); //企业法人 客户为企业时需要填写
//        businessCustomerInfoReq1.setLegalPersonIdentityCard("1111111"); //企业法人身份证 客户为企业且提供对公账号时需要填写
//        businessCustomerInfoReq1.setLegalPersonIsmainCustomer(true); //企业法人身份证 客户为企业且提供对公账号时需要填写


        BusinessCustomerInfoReq businessCustomerInfoReq2 = new BusinessCustomerInfoReq();
        businessCustomerInfoReqs.add(businessCustomerInfoReq2);
        businessCustomerInfoReq2.setCustomerId(UUID.randomUUID().toString());  //客户ID，资产端主键
        businessCustomerInfoReq2.setIsmainCustomer(1); //是否主借款人，0：否，1：是
        businessCustomerInfoReq2.setCustomerName("测试-企业"); //客户名称，个人则填个人名称，企业则填企业名称
        businessCustomerInfoReq2.setIsReceiptAccount(1); //是否收款账户，0：否，1：是
        businessCustomerInfoReq2.setCustomerType("企业"); //客户类型：个人，企业
        businessCustomerInfoReq2.setIdentifyCard("442374790-42122355676-213"); //客户身份证唯一标识，当客户为个人时，此字段存身份证，当客户类型为企业时，三证合一时，存统一社会信用代码，非三证合一时存营业执照号
        businessCustomerInfoReq2.setPhoneNumber("183203498543821"); //客户接收短信的手机号码，当客户类型为企业时，此字段保存联系人手机号码
        businessCustomerInfoReq2.setIsmainlandResident(1); //客户是否大陆居民，0或null：否，1：是
        businessCustomerInfoReq2.setIsCompanyBankAccount(1); //是否提供对公账号,null或0为否，1为是 客户为企业需要填写
        businessCustomerInfoReq2.setIsMergedCertificate(0); //是否三证合一,null或0为否，1为是 客户为企业需要填写，三证合一是指用【统一社会信用代码】代替以上三证。如果是三证合一，那么只需填写【统一社会信用代码】（必填），如果非三证合一，那么需填写【开户许可证】（必填）、【组织机构代码】、【营业执照编号】（必填）
        businessCustomerInfoReq2.setUnifiedCode(""); //统一社会信用代码 客户为企业并且为三证合一时必须填写并作为tb_business表，绑卡表的外键
        businessCustomerInfoReq2.setBusinessLicence("1111111"); //营业执照号 客户为企业时并且非三证合一时必须填写，并作为tb_business，绑卡表的外键
        businessCustomerInfoReq2.setRegisterProvince("1111111"); //企业注册地址所在省份 客户为企业时需要填写
        businessCustomerInfoReq2.setCompanyLegalPerson("1111111"); //企业法人 客户为企业时需要填写
        businessCustomerInfoReq2.setLegalPersonIdentityCard("1111111"); //企业法人身份证 客户为企业且提供对公账号时需要填写
        businessCustomerInfoReq2.setLegalPersonIsmainCustomer(true); //企业法人身份证 客户为企业且提供对公账号时需要填写





        List<ProjInfoReq> tuandaiProjReqInfos   = new LinkedList<>();
        creatRepayPlanReq.setProjInfoReqs(tuandaiProjReqInfos);

        //        private List<ProjFeeReq> projFeeInfos;
        ProjInfoReq req1 =creatProjInfoReq(businessBasicInfoReq);
        tuandaiProjReqInfos.add(req1);

        List<PrincipleReq> principleReqs = new LinkedList<>();
        req1.setPrincipleReqList(principleReqs);
        PrincipleReq principleReq1 = new PrincipleReq();
        principleReq1.setPeriod(1);
        principleReq1.setPrinciple(new BigDecimal("2500"));
        principleReqs.add(principleReq1);

        PrincipleReq principleReq2 = new PrincipleReq();
        principleReq2.setPeriod(2);
        principleReq2.setPrinciple(new BigDecimal("2500"));
        principleReqs.add(principleReq2);

        PrincipleReq principleReq3 = new PrincipleReq();
        principleReq3.setPeriod(3);
        principleReq3.setPrinciple(new BigDecimal("2500"));
        principleReqs.add(principleReq3);

        PrincipleReq principleReq4 = new PrincipleReq();
        principleReq4.setPeriod(4);
        principleReq4.setPrinciple(new BigDecimal("2500"));
        principleReqs.add(principleReq4);

        PrincipleReq principleReq5 = new PrincipleReq();
        principleReq5.setPeriod(5);
        principleReq5.setPrinciple(new BigDecimal("2500"));
        principleReqs.add(principleReq5);

        PrincipleReq principleReq6 = new PrincipleReq();
        principleReq6.setPeriod(6);
        principleReq6.setPrinciple(new BigDecimal("2500"));
        principleReqs.add(principleReq6);

//        Map<Integer,BigDecimal> principleMap1 = new HashMap<>();
//        req1.setPrincipleReqList(principleMap1);
//        principleMap1.put(1,new BigDecimal(4200));
//        principleMap1.put(2,new BigDecimal(4200));
//        principleMap1.put(3,new BigDecimal(4200));
//        principleMap1.put(4,new BigDecimal(4200));
//        principleMap1.put(5,new BigDecimal(4200));
//        principleMap1.put(6,new BigDecimal(9000));
        String mainProjectId = UUID.randomUUID().toString() ;
        req1.setProjectId(mainProjectId);
        req1.setStatusFlag("4");
        req1.setBeginTime(DateUtil.getDateTime("2018/3/21")); // 启标时间(用于生成还款计划)
        req1.setFullBorrowMoney(new BigDecimal("15000")); // 满标金额
        req1.setQueryFullsuccessDate(DateUtil.getDateTime("2018/3/21"));//满标时间（用于分还款计划）
//        req1.setTdLoanMoney(new BigDecimal(30000)); // 放款金额
        req1.setExtendFlag(0); // 是否是展期(0:不是展期,1:是展期)
//        req1.setCatsedAmount(new BigDecimal(30000)); // 投资者已投金额
        req1.setAmount(new BigDecimal("15000")); // 总金额(元)
        req1.setMasterIssueId(mainProjectId); // 主借标ID
        req1.setRate(new BigDecimal("9.5")); // 利率
        req1.setRateUnitType(1); // 利率单位：1 年利率; 2 月利率; 3 日利率
        req1.setOffLineInOverDueRate(new BigDecimal("12")); // 逾期滞纳金费率(%)
        req1.setOffLineInOverDueRateType(1); // 逾期滞纳金费率类型，1：年利率，2：月利率，3：日利率
        req1.setRepayType(RepayPlanRepayIniCalcWayEnum.INT_AND_PRIN_EVERYTIME.getKey()); // 还款方式：1：到期还本息，2：每月付息到期还本，5：等额本息，9：分期还本付息,11:等本等息
        req1.setPeriodMonth(6); // 借款期限  月



        List<ProjFeeReq>   feeList1 = new LinkedList<>();
        req1.setProjFeeInfos(feeList1);



        ProjFeeReq feeReq10 = creatProjFeeReq(req1);
        feeList1.add(feeReq10);
//        feeReq10.setProjId("137e8a4a-0727-4551-b20a-48b0d6679cfa");
        feeReq10.setFeeItemId("203e172b-9102-4820-9e06-76a64aa119e3"); //费用项目ID
        feeReq10.setFeeItemName("月收分公司服务费"); //费用项目名称
        feeReq10.setFeeType(RepayPlanFeeTypeEnum.SUB_COMPANY_CHARGE.getValue()); //费用项类型
        feeReq10.setFeeTypeName("分公司费用"); //费用项名称
        feeReq10.setFeeValue(new BigDecimal("6")); //业务应收取费用值，如果按月收取，则存储按月收取的值，如800元/月收取服务费，此字段存储800。如果一次性收取，则存储应收总费用值
        feeReq10.setChargeType(RepayPlanChargeTypeEnum.BY_MONTH.getKey()); //是否一次收取，1为按月收取，2为一次收取
        feeReq10.setRepaymentFlag(2); //标记该项费用的还款类型，1:期初收取,2:期末收取


        ProjFeeReq feeReq11 = creatProjFeeReq(req1);
        feeList1.add(feeReq11);
        feeReq11.setFeeItemId("72f5c955-7a80-4758-a159-779f0e714042"); //费用项目ID
        feeReq11.setFeeItemName("平台费"); //费用项目名称
        feeReq11.setFeeType(RepayPlanFeeTypeEnum.PLAT_CHARGE.getValue()); //费用项类型
        feeReq11.setFeeTypeName("团贷网平台费用"); //费用项名称
        feeReq11.setFeeValue(new BigDecimal("3")); //业务应收取费用值，如果按月收取，则存储按月收取的值，如800元/月收取服务费，此字段存储800。如果一次性收取，则存储应收总费用值
        feeReq11.setChargeType(2); //是否一次收取，1为按月收取，2为一次收取
        feeReq11.setRepaymentFlag(1); //标记该项费用的还款类型，1:期初收取,2:期末收取


        ProjFeeReq feeReq12 = creatProjFeeReq(req1);
        feeList1.add(feeReq12);
        feeReq12.setFeeItemId("b163247d-41b7-40db-9b59-7c9337a5c659"); //费用项目ID
        feeReq12.setFeeItemName("分公司服务费"); //费用项目名称
        feeReq12.setFeeType(RepayPlanFeeTypeEnum.SUB_COMPANY_CHARGE.getValue()); //费用项类型
        feeReq12.setFeeTypeName("分公司费用    "); //费用项名称
        feeReq12.setFeeValue(new BigDecimal("2.1")); //业务应收取费用值，如果按月收取，则存储按月收取的值，如800元/月收取服务费，此字段存储800。如果一次性收取，则存储应收总费用值
        feeReq12.setChargeType(2); //是否一次收取，1为按月收取，2为一次收取
        feeReq12.setRepaymentFlag(1); //标记该项费用的还款类型，1:期初收取,2:期末收取



        ProjFeeReq feeReq13 = creatProjFeeReq(req1);
        feeList1.add(feeReq13);
        feeReq13.setFeeItemId("d0c27c1d-8963-401d-b909-e08725c43171"); //费用项目ID
        feeReq13.setFeeItemName("月收平台费"); //费用项目名称
        feeReq13.setFeeType(RepayPlanFeeTypeEnum.PLAT_CHARGE.getValue()); //费用项类型
        feeReq13.setFeeTypeName("团贷网平台费用"); //费用项名称
        feeReq13.setFeeValue(new BigDecimal("6")); //业务应收取费用值，如果按月收取，则存储按月收取的值，如800元/月收取服务费，此字段存储800。如果一次性收取，则存储应收总费用值
        feeReq13.setChargeType(1); //是否一次收取，1为按月收取，2为一次收取
        feeReq13.setRepaymentFlag(2); //标记该项费用的还款类型，1:期初收取,2:期末收取

        ProjFeeReq feeReq14 = creatProjFeeReq(req1);
        feeList1.add(feeReq14);
        feeReq14.setFeeItemId("db58adad-cb24-4b69-abba-26eba989a23f"); //费用项目ID
        feeReq14.setFeeItemName("担保费"); //费用项目名称
        feeReq14.setFeeType(RepayPlanFeeTypeEnum.BOND_COMPANY_CHARGE.getValue()); //费用项类型
        feeReq14.setFeeTypeName("担保公司费用  "); //费用项名称
        feeReq14.setFeeValue(new BigDecimal("3.3")); //业务应收取费用值，如果按月收取，则存储按月收取的值，如800元/月收取服务费，此字段存储800。如果一次性收取，则存储应收总费用值
        feeReq14.setChargeType(2); //是否一次收取，1为按月收取，2为一次收取
        feeReq14.setRepaymentFlag(1); //标记该项费用的还款类型，1:期初收取,2:期末收取



//        List<ProjInfoReq> tuandaiProjReqInfos2   = new LinkedList<>();
//        creatRepayPlanReq.setProjInfoReqs(tuandaiProjReqInfos2);

        //        private List<ProjFeeReq> projFeeInfos;
        ProjInfoReq req2 =creatProjInfoReq(businessBasicInfoReq);
        tuandaiProjReqInfos.add(req2);


        List<PrincipleReq> principleReqs2 = new LinkedList<>();
        req2.setPrincipleReqList(principleReqs2);
        PrincipleReq principlereq21 = new PrincipleReq();
        principlereq21.setPeriod(1);
        principlereq21.setPrinciple(new BigDecimal("2500"));
        principleReqs2.add(principlereq21);

        PrincipleReq principleReq22 = new PrincipleReq();
        principleReq22.setPeriod(2);
        principleReq22.setPrinciple(new BigDecimal("2500"));
        principleReqs2.add(principleReq22);

        PrincipleReq principleReq23 = new PrincipleReq();
        principleReq23.setPeriod(3);
        principleReq23.setPrinciple(new BigDecimal("2500"));
        principleReqs2.add(principleReq23);

        PrincipleReq principleReq24 = new PrincipleReq();
        principleReq24.setPeriod(4);
        principleReq24.setPrinciple(new BigDecimal("2500"));
        principleReqs2.add(principleReq24);

        PrincipleReq principleReq25 = new PrincipleReq();
        principleReq25.setPeriod(5);
        principleReq25.setPrinciple(new BigDecimal("2500"));
        principleReqs2.add(principleReq25);

        PrincipleReq principleReq26 = new PrincipleReq();
        principleReq26.setPeriod(6);
        principleReq26.setPrinciple(new BigDecimal("2500"));
        principleReqs2.add(principleReq26);


//        ProjInfoReq req2 =creatProjInfoReq(businessBasicInfoReq);
//        tuandaiProjReqInfos.add(req2);
//
//        Map<Integer,BigDecimal> principleMap2 = new HashMap<>();
//        req2.setPrincipleReqList(principleMap2);
//        principleMap2.put(1,new BigDecimal(2800));
//        principleMap2.put(2,new BigDecimal(2800));
//        principleMap2.put(3,new BigDecimal(2800));
//        principleMap2.put(4,new BigDecimal(2800));
//        principleMap2.put(5,new BigDecimal(2800));
//        principleMap2.put(6,new BigDecimal(6000));



        req2.setProjectId(UUID.randomUUID().toString());
        req2.setStatusFlag("4");
        req2.setBeginTime(DateUtil.getDateTime("2018/3/22")); // 启标时间(用于生成还款计划)
        req2.setFullBorrowMoney(new BigDecimal("15000")); // 满标金额
        req2.setQueryFullsuccessDate(DateUtil.getDateTime("2018/3/22"));//满标时间（用于分还款计划）
//        req2.setTdLoanMoney(new BigDecimal(20000)); // 放款金额
        req2.setExtendFlag(0); // 是否是展期(0:不是展期,1:是展期)
//        req2.setCatsedAmount(new BigDecimal(20000)); // 投资者已投金额
        req2.setAmount(new BigDecimal("15000")); // 总金额(元)
        req2.setMasterIssueId(mainProjectId); // 主借标ID
        req2.setRate(new BigDecimal("9.5")); // 利率
        req2.setRateUnitType(1); // 利率单位：1 年利率; 2 月利率; 3 日利率
        req2.setOffLineInOverDueRate(new BigDecimal("12")); // 逾期滞纳金费率(%)
        req2.setOffLineInOverDueRateType(1); // 逾期滞纳金费率类型，1：年利率，2：月利率，3：日利率
        req2.setRepayType(RepayPlanRepayIniCalcWayEnum.INT_AND_PRIN_EVERYTIME.getKey()); // 还款方式：1：到期还本息，2：每月付息到期还本，5：等额本息，9：分期还本付息,11:等本等息
        req2.setPeriodMonth(6); // 借款期限  月




        List<ProjFeeReq>   feeList2 = new LinkedList<>();
        req2.setProjFeeInfos(feeList2);
        ProjFeeReq feeReq20 = creatProjFeeReq(req2);
        feeList2.add(feeReq20);
//        feeReq20.setProjId("670d149e-6b63-4810-b437-f993b0bc9af9");
        feeReq20.setFeeItemId("203e172b-9102-4820-9e06-76a64aa119e3"); //费用项目ID
        feeReq20.setFeeItemName("月收分公司服务费"); //费用项目名称
        feeReq20.setFeeType(RepayPlanFeeTypeEnum.SUB_COMPANY_CHARGE.getValue()); //费用项类型
        feeReq20.setFeeTypeName("分公司费用"); //费用项名称
        feeReq20.setFeeValue(new BigDecimal("4")); //业务应收取费用值，如果按月收取，则存储按月收取的值，如800元/月收取服务费，此字段存储800。如果一次性收取，则存储应收总费用值
        feeReq20.setChargeType(1); //是否一次收取，1为按月收取，2为一次收取
        feeReq20.setRepaymentFlag(2); //标记该项费用的还款类型，1:期初收取,2:期末收取



        ProjFeeReq feeReq21 = creatProjFeeReq(req2);
        feeList2.add(feeReq21);
//        feeReq21.setProjId("670d149e-6b63-4810-b437-f993b0bc9af9");
        feeReq21.setFeeItemId("72f5c955-7a80-4758-a159-779f0e714042"); //费用项目ID
        feeReq21.setFeeItemName("平台费"); //费用项目名称
        feeReq21.setFeeType(RepayPlanFeeTypeEnum.PLAT_CHARGE.getValue()); //费用项类型
        feeReq21.setFeeTypeName("团贷网平台费用"); //费用项名称
        feeReq21.setFeeValue(new BigDecimal("2")); //业务应收取费用值，如果按月收取，则存储按月收取的值，如800元/月收取服务费，此字段存储800。如果一次性收取，则存储应收总费用值
        feeReq21.setChargeType(2); //是否一次收取，1为按月收取，2为一次收取
        feeReq21.setRepaymentFlag(1); //标记该项费用的还款类型，1:期初收取,2:期末收取



        ProjFeeReq feeReq22 = creatProjFeeReq(req2);
        feeList2.add(feeReq22);
//        feeReq22.setProjId("670d149e-6b63-4810-b437-f993b0bc9af9");
        feeReq22.setFeeItemId("b163247d-41b7-40db-9b59-7c9337a5c659"); //费用项目ID
        feeReq22.setFeeItemName("分公司服务费"); //费用项目名称
        feeReq22.setFeeType(RepayPlanFeeTypeEnum.SUB_COMPANY_CHARGE.getValue()); //费用项类型
        feeReq22.setFeeTypeName("分公司费用"); //费用项名称
        feeReq22.setFeeValue(new BigDecimal("1.4")); //业务应收取费用值，如果按月收取，则存储按月收取的值，如800元/月收取服务费，此字段存储800。如果一次性收取，则存储应收总费用值
        feeReq22.setChargeType(2); //是否一次收取，1为按月收取，2为一次收取
        feeReq22.setRepaymentFlag(1); //标记该项费用的还款类型，1:期初收取,2:期末收取


        ProjFeeReq feeReq23 = creatProjFeeReq(req2);
        feeList2.add(feeReq23);
//        feeReq23.setProjId("670d149e-6b63-4810-b437-f993b0bc9af9");
        feeReq23.setFeeItemId("d0c27c1d-8963-401d-b909-e08725c43171"); //费用项目ID
        feeReq23.setFeeItemName("月收平台费"); //费用项目名称
        feeReq23.setFeeType(RepayPlanFeeTypeEnum.PLAT_CHARGE.getValue()); //费用项类型
        feeReq23.setFeeTypeName("团贷网平台费用"); //费用项名称
        feeReq23.setFeeValue(new BigDecimal("4")); //业务应收取费用值，如果按月收取，则存储按月收取的值，如800元/月收取服务费，此字段存储800。如果一次性收取，则存储应收总费用值
        feeReq23.setChargeType(1); //是否一次收取，1为按月收取，2为一次收取
        feeReq23.setRepaymentFlag(2); //标记该项费用的还款类型，1:期初收取,2:期末收取

        ProjFeeReq feeReq24 = creatProjFeeReq(req2);
        feeList2.add(feeReq24);
//        feeReq24.setProjId("670d149e-6b63-4810-b437-f993b0bc9af9");
        feeReq24.setFeeItemId("db58adad-cb24-4b69-abba-26eba989a23f"); //费用项目ID
        feeReq24.setFeeItemName("担保费"); //费用项目名称
        feeReq24.setFeeType(RepayPlanFeeTypeEnum.BOND_COMPANY_CHARGE.getValue()); //费用项类型
        feeReq24.setFeeTypeName("担保公司费用"); //费用项名称
        feeReq24.setFeeValue(new BigDecimal("2.2")); //业务应收取费用值，如果按月收取，则存储按月收取的值，如800元/月收取服务费，此字段存储800。如果一次性收取，则存储应收总费用值
        feeReq24.setChargeType(2); //是否一次收取，1为按月收取，2为一次收取
        feeReq24.setRepaymentFlag(1); //标记该项费用的还款类型，1:期初收取,2:期末收取

        return creatRepayPlanReq;

    }


//    @Test
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
        businessBasicInfoReq.setBorrowMoney(new BigDecimal("50000"));  //借款总额
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
        businessBasicInfoReq.setBorrowMoney(new BigDecimal("24000"));  //借款总额
        businessBasicInfoReq.setBorrowLimit(12);  //借款期限
        businessBasicInfoReq.setBorrowRate(new BigDecimal("12"));  //借款利率
        businessBasicInfoReq.setBorrowRateUnit(1);  //借款利率类型，1：年利率，2：月利率，3：日利率
        businessBasicInfoReq.setOperatorId("主办人111");  //业务主办人ID
        businessBasicInfoReq.setOperatorName("主办人 ttt");  //业务主办人姓名
//        businessBasicInfoReq.setAssetId(UUID.randomUUID().toString());  //业务所属资产端编号
        businessBasicInfoReq.setCompanyId("东莞总部");  //业务所属分公司编号
//        businessBasicInfoReq.setOutputPlatformId(1);  //出款平台ID，0：线下出款，1：团贷网P2P上标
        businessBasicInfoReq.setIssueSplitType(1);  //标识是否P2P拆标业务，0：非P2P拆标业务，1：P2P拆标业务
        businessBasicInfoReq.setSourceType(1);  //业务来源：0-常规录入 1-结清续贷新业务 2-结清续贷续贷业务 3-线下历史导入 4-扫码业务 5-优质车抵贷 6 -一点授信
        businessBasicInfoReq.setSourceBusinessId("9999999");  //原始来源业务的业务编号(当业务来源为结清再贷时，必填)
//        businessBasicInfoReq.setIsTuandaiRepay(1);  //是否需要进行平台还款，1：是，0：否
        businessBasicInfoReq.setIsRenewBusiness(1);  //是否展期业务，1：是，0：否

        return businessBasicInfoReq;

    }

    
    
    /**
     * 查tb_basic_business
     * @author 王继光
     * 2018年5月18日 下午6:04:51
     * @param businessId
     * @return
     */
    private BusinessBasicInfoReq  creatBusinessBasicInfoReq(String businessId){

        BusinessBasicInfoReq  businessBasicInfoReq = new BusinessBasicInfoReq();
       BasicBusiness business =  basicBusinessMapper.selectById(businessId);
        businessBasicInfoReq.setBusinessId(business.getBusinessId());
        businessBasicInfoReq.setOrgBusinessId(business.getBusinessId());
        businessBasicInfoReq.setInputTime(business.getCreateTime());
        businessBasicInfoReq.setBusinessType(business.getBusinessType());
        businessBasicInfoReq.setBusinessCtype(business.getBusinessCtype());  //业务所属子类型，若无则为空
        businessBasicInfoReq.setBusinessStype(business.getBusinessStype());  //业务所属孙类型，若无则为空
        businessBasicInfoReq.setCustomerId(business.getCustomerId());  //客户资产端唯一编号
        businessBasicInfoReq.setCustomerName(business.getCustomerName());  //客户姓名
        businessBasicInfoReq.setRepaymentTypeId(business.getRepaymentTypeId());  //1：到期还本息，2：每月付息到期还本，5：等额本息，9：分期还本付息
        businessBasicInfoReq.setBorrowMoney(business.getBorrowMoney());  //借款总额
        businessBasicInfoReq.setBorrowLimit(business.getBorrowLimit());  //借款期限
        businessBasicInfoReq.setBorrowRate(business.getBorrowRate());  //借款利率
        businessBasicInfoReq.setBorrowRateUnit(business.getBorrowRateUnit());  //借款利率类型，1：年利率，2：月利率，3：日利率
        businessBasicInfoReq.setOperatorId(business.getOperatorId());  //业务主办人ID
        businessBasicInfoReq.setOperatorName(business.getOperatorName());  //业务主办人姓名
//        businessBasicInfoReq.setAssetId(UUID.randomUUID().toString());  //业务所属资产端编号
        businessBasicInfoReq.setCompanyId(business.getCompanyId());  //业务所属分公司编号
//        businessBasicInfoReq.setOutputPlatformId(1);  //出款平台ID，0：线下出款，1：团贷网P2P上标
        businessBasicInfoReq.setIssueSplitType(1);  //标识是否P2P拆标业务，0：非P2P拆标业务，1：P2P拆标业务
        businessBasicInfoReq.setSourceType(0);  //业务来源：0-常规录入 1-结清续贷新业务 2-结清续贷续贷业务 3-线下历史导入 4-扫码业务 5-优质车抵贷 6 -一点授信
        businessBasicInfoReq.setSourceBusinessId("9999999");  //原始来源业务的业务编号(当业务来源为结清再贷时，必填)
//        businessBasicInfoReq.setIsTuandaiRepay(1);  //是否需要进行平台还款，1：是，0：否
        businessBasicInfoReq.setIsRenewBusiness(0);  //是否展期业务，1：是，0：否

        return businessBasicInfoReq;

    }

    /**
     * 创建标的信息
     * @param businessBasicInfoReq
     * @return
     */
    private  ProjInfoReq creatProjInfoReq(BusinessBasicInfoReq  businessBasicInfoReq){
        ProjInfoReq req1 = new ProjInfoReq();

        req1.setPlateType(1);
        req1.setProjectId("TestProjId111");
        req1.setStatusFlag("2");
        req1.setBeginTime(DateUtil.getDateTime("2018-5-1")); // 启标时间(用于生成还款计划)
        req1.setFullBorrowMoney(new BigDecimal("12000")); // 满标金额
//        req1.setAccounterConfirmUserId("user1111"); //  财务确认放款用户编号
//        req1.setAccounterConfirmUserName("用户111"); //  财务确认放款人名称
//        req1.setTdLoanTime(DateUtil.getDateTime("2018-5-1")); // 放款时间
//        req1.setTdLoanMoney(new BigDecimal(12000)); // 放款金额
        req1.setExtendFlag(0); // 是否是展期(0:不是展期,1:是展期)
//        req1.setCatsedAmount(new BigDecimal(12000)); // 投资者已投金额
        req1.setOrgIssueId("proj111111111"); // 原业务上标编号
        req1.setMasterIssueId("proj112133"); // 主借标ID
        req1.setIssueOrder(1); // 超额拆标共借项目的序号
//        req1.setBusinessAfterGuid("b-fater-guid"); // 还款计划guid,只适合房贷
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
        req1.setAmount(new BigDecimal("12000")); // 总金额(元)
        req1.setLowerUnit(new BigDecimal("100")); // 最小投资单位(元)
        req1.setBranchCompanyId("东莞总部"); // 标的来源(所属分公司的分润用户ID)
        req1.setControlDesc("风险控制措施"); // 风险控制措施
        req1.setImageUrl("http:www.baidu.com"); // 标题图片
        req1.setTitleImageId("标题图片编号"); // 标题图片编号
        req1.setRemark("备注"); // 备注
        req1.setTdStatus(4); // 上标状态
//        req1.setProjectType(businessBasicInfoReq.getBusinessType()); // 上标状态
        req1.setResultContent("上标结果"); // 上标结果
        req1.setEnterpriseUserId("utryrtwer"); // 担保方编号(所属担保公司分润用户ID)
        req1.setAviCreditGrantingAmount(new BigDecimal("120000000")); // 担保公司可用金额

        req1.setCustomerId("customerId");   //用户ID

        req1.setOverRate(new BigDecimal("0.01")); //逾期年利率

        req1.setRate(new BigDecimal("12")); // 利率
        req1.setRateUnitType(1); // 利率单位：1 年利率; 2 月利率; 3 日利率
        req1.setOffLineInOverDueRate(new BigDecimal("11")); // 线下期内逾期滞纳金费率(%)
        req1.setOffLineInOverDueRateType(1); // 线下期内逾期滞纳金费率类型，1：年利率，2：月利率，3：日利率
        req1.setOffLineOutOverDueRate(new BigDecimal("12")); // 线下期外逾期滞纳金费率(%)
        req1.setOffLineOutOverDueRateType(1); // 线下期外逾期滞纳金费率类型，1：年利率，2：月利率，3：日利率
        req1.setOnLineOverDueRate(new BigDecimal("0.6")); // 线上逾期滞纳金费率(%)
        req1.setOnLineOverDueRateType(1); // 线上逾期滞纳金费率类型，1：年利率，2：月利率，3：日利率

        req1.setRepayType(RepayPlanRepayIniCalcWayEnum.PRINCIPAL_LAST.getKey()); // 还款方式：1：到期还本息，2：每月付息到期还本，5：等额本息，9：分期还本付息,11:等本等息
        req1.setSex(PeayPlanSexEnum.MAN.getValue()); // 性别
        req1.setCredTypeId(1); // 证件类型
        req1.setBirthday(DateUtil.getDateTime("1987-5-1")); // 生日
        req1.setRiskAssessment("风险评估意见"); // 风险评估意见
//        req1.setPlateUserId("团贷用户ID"); // 团贷用户ID
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
        feeReq.setFeeDetailReqList(null);
//        feeReq.setProjId(projInfoReq.getProjectId());
        feeReq.setFeeItemId(UUID.randomUUID().toString()); //费用项目ID
        feeReq.setFeeItemName("费用项目名称"); //费用项目名称
        feeReq.setFeeType(RepayPlanFeeTypeEnum.SUB_COMPANY_CHARGE.getValue()); //费用项类型
        feeReq.setFeeTypeName("费用项名称"); //费用项名称
        feeReq.setAccountStatus(RepayPlanAccountStatusEnum.DIVISION_TO_ASSET.getValue()); //分账标记
//        feeReq.setFeeChargingType(2); //费用收取方式，1为按比例，2为按固定金额
//        feeReq.setNewSystemDefaultRate(new BigDecimal(0)); //系统默认匹配的费用比例（当收取方式为2时，此字段存零）
        feeReq.setFeeValue(new BigDecimal("800")); //业务应收取费用值，如果按月收取，则存储按月收取的值，如800元/月收取服务费，此字段存储800。如果一次性收取，则存储应收总费用值
//        feeReq.setWithdrawId(111); //对应的提现编号
        feeReq.setChargeType(RepayPlanChargeTypeEnum.BY_MONTH.getKey()); //是否一次收取，1为按月收取，2为一次收取
//        feeReq.setWithdrawPlace(1); //放款去处 1:提到银行卡  2：转到可用金额
//        feeReq.setOutputFlag(1); //标记该项费用是否单独收取，null或0:不单独收取，1:单独收取
//        feeReq.setRepaymentFlag(1); //标记该项费用的还款类型，1:期初收取,2:期末收取

//        feeReq.setIsP2PMainmarkCollect(1); //是否P2P主标收取,1为是，0为否
//        feeReq.setIsRecordIncome(BooleanEnum.YES.getValue()); //是否可记为收入,1为是，0为否

        return feeReq;

    }

    @Test
    public void testQueryRepayPlanByBusinessId() {
    	PlanReturnInfoDto dto = creatRepayPlanService.queryRepayPlanByBusinessId("TSYD1012018051901");
    	System.out.println(dto);
    }


    private void  testJson(){
//        {
//            "bizCusInfoReqs": [{
//            "businessLicence": "",
//                    "companyLegalPerson": "",
//                    "customerId": "38e2702b-2b64-48e7-b296-3f0a55fadfa4",
//                    "customerName": "徐友灵",
//                    "customerType": "个人",
//                    "identifyCard": "420702198111296306",
//                    "isReceiptAccount": 0,
//                    "ismainCustomer": 0,
//                    "ismainlandResident": 1,
//                    "legalPersonIdentityCard": "",
//                    "phoneNumber": "13800001111",
//                    "registerProvince": "",
//                    "unifiedCode": ""
//        }, {
//            "businessLicence": "",
//                    "companyLegalPerson": "",
//                    "customerId": "d6149112-98d1-4433-a890-2ba53b561103",
//                    "customerName": "胡阳秋",
//                    "customerType": "个人",
//                    "identifyCard": "330701197009205794",
//                    "isReceiptAccount": 0,
//                    "ismainCustomer": 0,
//                    "ismainlandResident": 1,
//                    "legalPersonIdentityCard": "",
//                    "phoneNumber": "13800008888",
//                    "registerProvince": "",
//                    "unifiedCode": ""
//        }, {
//            "businessLicence": "",
//                    "companyLegalPerson": "",
//                    "customerId": "1c6f0089-41fb-4229-83c5-18c199774a0c",
//                    "customerName": "郭禄",
//                    "customerType": "个人",
//                    "identifyCard": "140624199808290019",
//                    "isReceiptAccount": 0,
//                    "ismainCustomer": 1,
//                    "ismainlandResident": 1,
//                    "legalPersonIdentityCard": "",
//                    "phoneNumber": "13800007952",
//                    "registerProvince": "",
//                    "unifiedCode": ""
//        }],
//            "businessBasicInfoReq": {
//            "borrowLimit": 36,
//                    "borrowMoney": 50000.00,
//                    "borrowRate": 11.0000,
//                    "borrowRateUnit": 1,
//                    "businessCtype": "业务信用贷用信",
//                    "businessId": "TSYD1012018060403",
//                    "businessStype": "",
//                    "businessType": 25,
//                    "companyId": "东莞总部",
//                    "companyName": "东莞总部",
//                    "customerId": "1c6f0089-41fb-4229-83c5-18c199774a0c",
//                    "customerName": "郭禄",
//                    "districtId": "30f1c8d6-d087-4e72-915f-7b463cb4d740",
//                    "districtName": "华南片区",
//                    "inputTime": 1528134383000,
//                    "isRenewBusiness": 0,
//                    "issueSplitType": 1,
//                    "operatorId": "xyd_xdzy",
//                    "operatorName": "xyd_xdzy",
//                    "orgBusinessId": "TSYD1012018060403",
//                    "originalName": "xyd_xdzy",
//                    "originalUserid": "xyd_xdzy",
//                    "repaymentTypeId": 5,
//                    "sourceType": 0
//        },
//            "projInfoReqs": [{
//            "customerId": "1c6f0089-41fb-4229-83c5-18c199774a0c",
//                    "borrowLimit":12,
//                    "telNo": "15999795945",
//                    "overRate":0.01,
//                    "monthPrincipalAmount":1000.00,
//                    "address": "广东省东莞市南城区111",
//                    "agencyAmount": 0.00,
//                    "agencyId": "3AFAC66B-BBE5-4E5D-A70B-5955090D79F9",
//                    "agencyRate": 0.0000,
//                    "amount": 10000.00,
//                    "aviCreditGrantingAmount": 55693872392.0000,
//                    "bankAccountNo": "6217000830000123038",
//                    "bankCity": "东莞市",
//                    "bankProvice": "广东省",
//                    "bankType": 4,
//                    "beginTime": 1528025833000,
//                    "birthday": 375840000000,
//                    "borrowAmount": 7300.00,
//                    "borrowerRate": 73.0000,
//                    "branchCompanyId": "9BD2E3E7-CB56-4749-B9CE-2BA581AD8203",
//                    "controlDesc": "",
//                    "cooperativeTdComAmount": 0.0000,
//                    "cooperativeTdComRate": 0.00,
//                    "cooperativeTdComUserId": "",
//                    "credTypeId": 0,
//                    "creditorId": "00000000-0000-0000-0000-000000000000",
//                    "depositAmount": 0.00,
//                    "enterpriseUserId": "E74D6597-C46A-435B-969D-72AFAAD3E661",
//                    "extendFlag": 0,
//                    "freedAmount": 0.00,
//                    "freedRate": 0.0000,
//                    "fullBorrowMoney": 10000.00,
//                    "fundUse": "用于购物消费",
//                    "guaranteeAmount": 900.00,
//                    "guaranteeRate": 4.0000,
//                    "identityCard": "420702198111296306",
//                    "imageUrl": "upload/Issue/52c1f608-2fec-4b3a-9b1e-b3c0d8f73062/18060406262402.jpg",
//                    "isHaveCar": 0,
//                    "isHaveHouse": 0,
//                    "issueOrder": 3,
//                    "lowerUnit": 50.00,
//                    "marriage": "未婚",
//                    "masterIssueId": "e58ee63d-6ea4-48a3-bbbb-1b6a46989ebf",
//                    "nickName": "fdefad45578e",
//                    "offLineInOverDueRate": 0.1,
//                    "offLineInOverDueRateType": 2,
//                    "offLineOutOverDueRate": 0.2,
//                    "offLineOutOverDueRateType": 2,
//                    "onLineOverDueRate": 0.06,
//                    "onLineOverDueRateType": 5,
//                    "openBankName": "东莞分行",
//                    "orgIssueId": "",
//                    "periodMonth": 36,
//                    "plateType": 1,
//                    "principleReqList": [],
//            "projCarInfos": [],
//            "projFeeInfos": [{
//                "accountStatus": 0,
//                        "chargeType": 0,
//                        "feeDetailReqList": [],
//                "feeItemId": "424c2302-e6a0-439d-bc57-9a5182005e91",
//                        "feeItemName": "业主信用贷返点",
//                        "feeType": 110,
//                        "feeTypeName": "返点",
//                        "feeValue": 0.00,
//                        "isTermRange": 0,
//                        "repaymentFlag": 1
//            }, {
//                "accountStatus": 30,
//                        "chargeType": 2,
//                        "feeDetailReqList": [],
//                "feeItemId": "72f5c955-7a80-4758-a159-779f0e714042",
//                        "feeItemName": "平台费",
//                        "feeType": 50,
//                        "feeTypeName": "团贷网平台费用",
//                        "feeValue": 900.00,
//                        "isTermRange": 0,
//                        "repaymentFlag": 1
//            }, {
//                "accountStatus": 20,
//                        "chargeType": 1,
//                        "feeDetailReqList": [],
//                "feeItemId": "74a99887-137e-4e1a-b47b-0003d89d4dd9",
//                        "feeItemName": "月收分公司服务费",
//                        "feeType": 30,
//                        "feeTypeName": "分公司费用",
//                        "feeValue": 2.00,
//                        "isTermRange": 0,
//                        "repaymentFlag": 2
//            }, {
//                "accountStatus": 20,
//                        "chargeType": 2,
//                        "feeDetailReqList": [],
//                "feeItemId": "b163247d-41b7-40db-9b59-7c9337a5c659",
//                        "feeItemName": "分公司服务费",
//                        "feeType": 30,
//                        "feeTypeName": "分公司费用",
//                        "feeValue": 1400.00,
//                        "isTermRange": 0,
//                        "repaymentFlag": 1
//            }, {
//                "accountStatus": 40,
//                        "chargeType": 2,
//                        "feeDetailReqList": [],
//                "feeItemId": "db58adad-cb24-4b69-abba-26eba989a23f",
//                        "feeItemName": "担保费",
//                        "feeType": 40,
//                        "feeTypeName": "担保公司费用",
//                        "feeValue": 400.00,
//                        "isTermRange": 0,
//                        "repaymentFlag": 1
//            }],
//            "projHouseInfos": [],
//            "projectFrom": 0,
//                    "projectId": "52c1f699-2fec-4b3a-219b1e-b3c0d8f733062",
//                    "projectType": 37,
//                    "queryAuditDate": 1528137111000,
//                    "queryFullsuccessDate": 1528112233000,
//                    "rate": 11.00,
//                    "rateUnitType": 1,
//                    "realName": "徐友灵",
//                    "repayType": 5,
//                    "repaymentAssure": "111",
//                    "repaymentType": 5,
//                    "resultContent": "状态说明:0;平台审核时间:2018/6/4 18:31:51;审核结果:",
//                    "riskAssessment": "111",
//                    "sex": 2,
//                    "statusFlag": "4",
//                    "subCompanyCharge": 1400.00,
//                    "subCompanyRate": 14.0000,
//                    "tdStatus": 2,
//                    "tdUserId": "1446DDC6-9675-46BB-8998-9F11A1E91534",
//                    "title": "【东莞市】【TDW-YXD20180604004】",
//                    "titleImageId": "e663d4c7-ba38-4421-a49c-0d5c8cb0cd9b",
//                    "tuandaiAmount": 900.00,
//                    "tuandaiRate": 9.0000,
//                    "userTypeId": 1
//        }, {
//            "customerId": "1c6f0089-41fb-4229-83c5-18c199774a0c",
//                    "borrowLimit":12,
//                    "telNo": "15999795945",
//                    "overRate":0.01,
//                    "monthPrincipalAmount":1000.00,
//                    "address": "广东省东莞市南城区111",
//                    "agencyAmount": 0.00,
//                    "agencyId": "3AFAC66B-BBE5-4E5D-A70B-5955090D79F9",
//                    "agencyRate": 0.0000,
//                    "amount": 20000.00,
//                    "aviCreditGrantingAmount": 55693872392.0000,
//                    "bankAccountNo": "6227001291082482737",
//                    "bankCity": "东莞市",
//                    "bankProvice": "广东省",
//                    "bankType": 4,
//                    "beginTime": 1528112295000,
//                    "birthday": 22636800000,
//                    "borrowAmount": 14600.00,
//                    "borrowerRate": 73.0000,
//                    "branchCompanyId": "9BD2E3E7-CB56-4749-B9CE-2BA581AD8203",
//                    "controlDesc": "",
//                    "cooperativeTdComAmount": 0.0000,
//                    "cooperativeTdComRate": 0.00,
//                    "cooperativeTdComUserId": "",
//                    "credTypeId": 0,
//                    "creditorId": "00000000-0000-0000-0000-000000000000",
//                    "depositAmount": 0.00,
//                    "enterpriseUserId": "E74D6597-C46A-435B-969D-72AFAAD3E661",
//                    "extendFlag": 0,
//                    "freedAmount": 0.00,
//                    "freedRate": 0.0000,
//                    "fullBorrowMoney": 20000.00,
//                    "fundUse": "用于购物消费",
//                    "guaranteeAmount": 1800.00,
//                    "guaranteeRate": 4.0000,
//                    "identityCard": "330701197009205794",
//                    "imageUrl": "upload/Issue/7d06117d-43a7-40b2-b25f-c00e6a4fa541/18060406271176.jpg",
//                    "isHaveCar": 0,
//                    "isHaveHouse": 0,
//                    "issueOrder": 2,
//                    "lowerUnit": 50.00,
//                    "marriage": "未婚",
//                    "masterIssueId": "e58ee63d-6ea4-48a3-bbbb-1b6a46989ebf",
//                    "nickName": "61369f74ea47",
//                    "offLineInOverDueRate": 0.1,
//                    "offLineInOverDueRateType": 2,
//                    "offLineOutOverDueRate": 0.2,
//                    "offLineOutOverDueRateType": 2,
//                    "onLineOverDueRate": 0.06,
//                    "onLineOverDueRateType": 5,
//                    "openBankName": "东莞分行",
//                    "orgIssueId": "",
//                    "periodMonth": 36,
//                    "plateType": 1,
//                    "principleReqList": [],
//            "projCarInfos": [],
//            "projFeeInfos": [{
//                "accountStatus": 0,
//                        "chargeType": 0,
//                        "feeDetailReqList": [],
//                "feeItemId": "424c2302-e6a0-439d-bc57-9a5182005e91",
//                        "feeItemName": "业主信用贷返点",
//                        "feeType": 110,
//                        "feeTypeName": "返点",
//                        "feeValue": 0.00,
//                        "isTermRange": 0,
//                        "repaymentFlag": 1
//            }, {
//                "accountStatus": 30,
//                        "chargeType": 2,
//                        "feeDetailReqList": [],
//                "feeItemId": "72f5c955-7a80-4758-a159-779f0e714042",
//                        "feeItemName": "平台费",
//                        "feeType": 50,
//                        "feeTypeName": "团贷网平台费用",
//                        "feeValue": 1800.00,
//                        "isTermRange": 0,
//                        "repaymentFlag": 1
//            }, {
//                "accountStatus": 20,
//                        "chargeType": 1,
//                        "feeDetailReqList": [],
//                "feeItemId": "74a99887-137e-4e1a-b47b-0003d89d4dd9",
//                        "feeItemName": "月收分公司服务费",
//                        "feeType": 30,
//                        "feeTypeName": "分公司费用",
//                        "feeValue": 4.00,
//                        "isTermRange": 0,
//                        "repaymentFlag": 2
//            }, {
//                "accountStatus": 20,
//                        "chargeType": 2,
//                        "feeDetailReqList": [],
//                "feeItemId": "b163247d-41b7-40db-9b59-7c9337a5c659",
//                        "feeItemName": "分公司服务费",
//                        "feeType": 30,
//                        "feeTypeName": "分公司费用",
//                        "feeValue": 2800.00,
//                        "isTermRange": 0,
//                        "repaymentFlag": 1
//            }, {
//                "accountStatus": 40,
//                        "chargeType": 2,
//                        "feeDetailReqList": [],
//                "feeItemId": "db58adad-cb24-4b69-abba-26eba989a23f",
//                        "feeItemName": "担保费",
//                        "feeType": 40,
//                        "feeTypeName": "担保公司费用",
//                        "feeValue": 800.00,
//                        "isTermRange": 0,
//                        "repaymentFlag": 1
//            }],
//            "projHouseInfos": [],
//            "projectFrom": 0,
//                    "projectId": "7d06897d-43a7-40b2-b25f-c00e78a4fa541",
//                    "projectType": 37,
//                    "queryAuditDate": 1528137098000,
//                    "queryFullsuccessDate": 1528112295000,
//                    "rate": 11.00,
//                    "rateUnitType": 1,
//                    "realName": "胡阳秋",
//                    "repayType": 5,
//                    "repaymentAssure": "111",
//                    "repaymentType": 5,
//                    "resultContent": "状态说明:0;平台审核时间:2018/6/4 18:31:38;审核结果:",
//                    "riskAssessment": "111",
//                    "sex": 1,
//                    "statusFlag": "4",
//                    "subCompanyCharge": 2800.00,
//                    "subCompanyRate": 14.0000,
//                    "tdStatus": 2,
//                    "tdUserId": "994E62BC-5859-4383-B0B9-D36BDD1E2F0D",
//                    "title": "【东莞市】【TDW-YXD20180604003】",
//                    "titleImageId": "0b598fe7-3374-4a8e-8de2-f4fe546a228f",
//                    "tuandaiAmount": 1800.00,
//                    "tuandaiRate": 9.0000,
//                    "userTypeId": 1
//        }, {
//            "customerId": "1c6f0089-41fb-4229-83c5-18c199774a0c",
//                    "borrowLimit":12,
//                    "telNo": "15999795945",
//                    "overRate":0.01,
//                    "monthPrincipalAmount":1000.00,
//                    "address": "福建省漳州市漳浦县绥安镇礼泉村22号",
//                    "agencyAmount": 0.00,
//                    "agencyId": "3AFAC66B-BBE5-4E5D-A70B-5955090D79F9",
//                    "agencyRate": 0.0000,
//                    "amount": 20000.00,
//                    "aviCreditGrantingAmount": 55693872392.0000,
//                    "bankAccountNo": "6222021001111244952",
//                    "bankCity": "龙岩市",
//                    "bankProvice": "福建省",
//                    "bankType": 2,
//                    "beginTime": 1528112369000,
//                    "birthday": 904348800000,
//                    "borrowAmount": 14600.00,
//                    "borrowerRate": 73.0000,
//                    "branchCompanyId": "9BD2E3E7-CB56-4749-B9CE-2BA581AD8203",
//                    "controlDesc": "",
//                    "cooperativeTdComAmount": 0.0000,
//                    "cooperativeTdComRate": 0.00,
//                    "cooperativeTdComUserId": "",
//                    "credTypeId": 0,
//                    "creditorId": "00000000-0000-0000-0000-000000000000",
//                    "depositAmount": 0.00,
//                    "enterpriseUserId": "E74D6597-C46A-435B-969D-72AFAAD3E661",
//                    "extendFlag": 0,
//                    "freedAmount": 0.00,
//                    "freedRate": 0.0000,
//                    "fullBorrowMoney": 20000.00,
//                    "fundUse": "用于购物消费",
//                    "guaranteeAmount": 1800.00,
//                    "guaranteeRate": 4.0000,
//                    "identityCard": "140624199808290019",
//                    "imageUrl": "upload/Issue/e58ee63d-6ea4-48a3-bbbb-1b6a46989ebf/18060406255483.jpg",
//                    "isHaveCar": 0,
//                    "isHaveHouse": 0,
//                    "issueOrder": 1,
//                    "lowerUnit": 50.00,
//                    "marriage": "未婚",
//                    "masterIssueId": "e58ee63d-6ea4-48a3-bbbb-1b6a46989ebf",
//                    "nickName": "7bab5d6c7f05",
//                    "offLineInOverDueRate": 0.1,
//                    "offLineInOverDueRateType": 2,
//                    "offLineOutOverDueRate": 0.2,
//                    "offLineOutOverDueRateType": 2,
//                    "onLineOverDueRate": 0.06,
//                    "onLineOverDueRateType": 5,
//                    "openBankName": "中国银行福建省总行",
//                    "orgIssueId": "",
//                    "periodMonth": 36,
//                    "plateType": 1,
//                    "principleReqList": [],
//            "projCarInfos": [],
//            "projFeeInfos": [{
//                "accountStatus": 0,
//                        "chargeType": 0,
//                        "feeDetailReqList": [],
//                "feeItemId": "424c2302-e6a0-439d-bc57-9a5182005e91",
//                        "feeItemName": "业主信用贷返点",
//                        "feeType": 110,
//                        "feeTypeName": "返点",
//                        "feeValue": 0.00,
//                        "isTermRange": 0,
//                        "repaymentFlag": 1
//            }, {
//                "accountStatus": 30,
//                        "chargeType": 2,
//                        "feeDetailReqList": [],
//                "feeItemId": "72f5c955-7a80-4758-a159-779f0e714042",
//                        "feeItemName": "平台费",
//                        "feeType": 50,
//                        "feeTypeName": "团贷网平台费用",
//                        "feeValue": 1800.00,
//                        "isTermRange": 0,
//                        "repaymentFlag": 1
//            }, {
//                "accountStatus": 20,
//                        "chargeType": 1,
//                        "feeDetailReqList": [],
//                "feeItemId": "74a99887-137e-4e1a-b47b-0003d89d4dd9",
//                        "feeItemName": "月收分公司服务费",
//                        "feeType": 30,
//                        "feeTypeName": "分公司费用",
//                        "feeValue": 4.00,
//                        "isTermRange": 0,
//                        "repaymentFlag": 2
//            }, {
//                "accountStatus": 20,
//                        "chargeType": 2,
//                        "feeDetailReqList": [],
//                "feeItemId": "b163247d-41b7-40db-9b59-7c9337a5c659",
//                        "feeItemName": "分公司服务费",
//                        "feeType": 30,
//                        "feeTypeName": "分公司费用",
//                        "feeValue": 2800.00,
//                        "isTermRange": 0,
//                        "repaymentFlag": 1
//            }, {
//                "accountStatus": 40,
//                        "chargeType": 2,
//                        "feeDetailReqList": [],
//                "feeItemId": "db58adad-cb24-4b69-abba-26eba989a23f",
//                        "feeItemName": "担保费",
//                        "feeType": 40,
//                        "feeTypeName": "担保公司费用",
//                        "feeValue": 800.00,
//                        "isTermRange": 0,
//                        "repaymentFlag": 1
//            }],
//            "projHouseInfos": [],
//            "projectFrom": 0,
//                    "projectId": "e58ee63d-645a4-48a3-bbttb-1b6a46989ebf",
//                    "projectType": 35,
//                    "queryAuditDate": 1528137136000,
//                    "queryFullsuccessDate": 1528112369000,
//                    "rate": 11.00,
//                    "rateUnitType": 1,
//                    "realName": "郭禄",
//                    "repayType": 5,
//                    "repaymentAssure": "111",
//                    "repaymentType": 5,
//                    "resultContent": "状态说明:0;平台审核时间:2018/6/4 18:32:16;审核结果:",
//                    "riskAssessment": "111",
//                    "sex": 1,
//                    "statusFlag": "4",
//                    "subCompanyCharge": 2800.00,
//                    "subCompanyRate": 14.0000,
//                    "tdStatus": 2,
//                    "tdUserId": "57E738DB-5E6C-4341-861D-03CA6F718344",
//                    "title": "【东莞市】业务信用贷用信【TDW-CYD20180604002】",
//                    "titleImageId": "468222ce-ef12-4f6d-bff9-7d41f79ec7ba",
//                    "tuandaiAmount": 1800.00,
//                    "tuandaiRate": 9.0000,
//                    "userTypeId": 1
//        }],
//            "rondmode": 0,
//                "smallNum": 2
//        }

    }


}