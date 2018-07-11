package com.hongte.alms.base.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;

import java.math.BigDecimal;
import java.util.*;

import com.hongte.alms.base.collection.enums.CollectionStatusEnum;
import com.hongte.alms.base.dto.FinanceManagerListReq;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentResource;
import com.hongte.alms.base.entity.SysBankLimit;
import com.hongte.alms.base.enums.RepayCurrentStatusEnums;
import com.hongte.alms.base.enums.RepayedFlag;
import com.hongte.alms.base.feignClient.XindaiFeign;
import com.hongte.alms.base.feignClient.dto.BankCardInfo;
import com.hongte.alms.base.mapper.RepaymentBizPlanListMapper;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.base.service.RepaymentResourceService;
import com.hongte.alms.base.service.SysBankLimitService;
import com.hongte.alms.base.vo.finance.ConfirmWithholdListVO;
import com.hongte.alms.base.vo.module.FinanceManagerListVO;
import com.hongte.alms.base.dto.FinanceManagerListReq;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.util.DESC;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.EncryptionResult;
import com.hongte.alms.common.vo.PageResult;

import com.hongte.alms.common.vo.RequestData;
import com.hongte.alms.common.vo.ResponseData;
import com.hongte.alms.common.vo.ResponseEncryptData;
import feign.Feign;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 业务还款计划列表 服务实现类
 * </p>
 *
 * @author 曾坤
 * @since 2018-03-06
 */
@Service("RepaymentBizPlanListService")
public class RepaymentBizPlanListServiceImpl extends BaseServiceImpl<RepaymentBizPlanListMapper, RepaymentBizPlanList> implements RepaymentBizPlanListService {


    @Autowired
    @Qualifier("RepaymentResourceService")
    RepaymentResourceService repaymentResourceService;

    private static Logger logger = LoggerFactory.getLogger(RepaymentBizPlanListServiceImpl.class);
    @Autowired
    RepaymentBizPlanListMapper repaymentBizPlanListMapper;

    @Autowired
    @Qualifier("BasicBusinessService")
    private BasicBusinessService basicBusinessService;

    @Value(value = "${bmApi.apiUrl:http://127.0.0.1}")
    private String apiUrl;

    @Autowired
    @Qualifier("SysBankLimitService")
    private SysBankLimitService sysBankLimitService;

    @Override
    public List<RepaymentBizPlanList> selectNeedPhoneUrgNorBiz(String companyId, Integer overDueDays, Integer businessType) {
//        if(overDueDays.equals(0)){
//            overDueDays = 1000;
//        }
        return repaymentBizPlanListMapper.selectNeedSetColInfoNormalBizPlansBycomId(
                companyId,
                overDueDays,
                CollectionStatusEnum.PHONE_STAFF.getKey(), businessType);
    }

    @Override
    public List<RepaymentBizPlanList> selectNeedVisitNorBiz(String companyId, Integer overDueDays, Integer businessType) {
        return repaymentBizPlanListMapper.selectNeedSetColInfoNormalBizPlansBycomId(
                companyId,
                overDueDays,
                CollectionStatusEnum.COLLECTING.getKey(), businessType);

    }

    @Override
    public List<RepaymentBizPlanList> selectNeedLawNorBiz(Integer overDueDays, Integer businessType) {
        return repaymentBizPlanListMapper.selectNeedSetColInfoNormalBizPlansBycomId(
                null,
                overDueDays,
                CollectionStatusEnum.TO_LAW_WORK.getKey(), businessType);
    }

    @Override
    public String queryRepaymentBizPlanListByConditions(String businessId, String afterId) {
        return repaymentBizPlanListMapper.queryRepaymentBizPlanListByConditions(businessId, afterId);
    }
    
    @Override
    public PageResult selectByFinanceManagerListReq(FinanceManagerListReq req) {
        int count = repaymentBizPlanListMapper.conutFinanceManagerList(req);
        Set<String> businessSet = new HashSet<>();

        List<FinanceManagerListVO> list = repaymentBizPlanListMapper.selectFinanceMangeList(req);
        for (FinanceManagerListVO financeManagerListVO : list) {
            businessSet.add(financeManagerListVO.getBusinessId());
        }

        try {
            for (FinanceManagerListVO financeManagerListVO : list) {
                financeManagerListVO.setCanWithhold(false);
                financeManagerListVO.setCanWithholdDesc(Constant.CANWITHHOLD_NO);

                for (String businessId : businessSet) {
                    if (financeManagerListVO.getBusinessId().equals(businessId)) {
                     BigDecimal planRepayAmount=financeManagerListVO.getPlanRepayAmount();
                        //查询身份证信息
                        BasicBusiness basicBusiness = basicBusinessService.selectOne(new EntityWrapper<BasicBusiness>().eq("business_id", businessId));
                        String identityCard = basicBusiness.getCustomerIdentifyCard();
                        RequestData requestData = new RequestData();
                        JSONObject data = new JSONObject();
                        data.put("identityCard", identityCard);
                        requestData.setData(JSON.toJSONString(data));
                        requestData.setMethodName("BankCard_GetBankCardByIdCard");
                        String encryptStr = JSON.toJSONString(requestData);
                        // 请求数据加密
                        encryptStr = encryptPostData(encryptStr);
                        XindaiFeign XindaiFeignService = Feign.builder().target(XindaiFeign.class, apiUrl);

                        String respStr = XindaiFeignService.getBankcardInfo(encryptStr);
                        // 返回数据解密
                        ResponseData respData = getRespData(respStr);
                        logger.info("客户根据身份证号:" + identityCard + "获取银行卡信息，接口返回数据:" + respData.getData() + "," + respData.getReturnMessage());
                        if (respData.getData() != null) {

                           JSONArray jsonObject = JSON.parseArray(respData.getData());

                           List<BankCardInfo> bankList= jsonObject.toJavaList(BankCardInfo.class);
                            if (bankList !=null) {
                                financeManagerListVO.setCanWithhold(true); //有绑定平台可以进行代扣
                                String bankCode = bankList.get(0).getBankCode();
                                //查询单笔最高金额
                                BigDecimal onceLimit = sysBankLimitService.selectOnceLimit(bankCode);
                                //查询单日最高金额
                                BigDecimal dayLimit = sysBankLimitService.selectMaxDayLimit(bankCode);

                                //若未绑定代扣平台取“否”，鼠标移到否时需提示文案为“该卡当前不支持代扣，请及时更换”
                                //若为”是“则需情况进行文案提示：
                                // A.该卡单次代扣限额>=本次还款金额，提示“限额足够，可一次代扣”；
                                // B.该卡单次代扣限额<本次还款金额<=单日代扣限额，提示“单次限额不够，本次还款需要代扣x次。X=本次还款金额/单笔代扣额度 进1一次
                                // C.卡单日代扣限额<本次还款金额，提示“代扣限额不足，请换卡”
                                //planRepayAmount
                                if(planRepayAmount.compareTo(onceLimit)<=0){
                                    financeManagerListVO.setCanWithholdDesc(Constant.CANWITHHOLD_YES_1);
                                }else if(planRepayAmount.compareTo(onceLimit)>0 && planRepayAmount.compareTo(dayLimit)<=0){
                                    BigDecimal divide = planRepayAmount.divide(onceLimit).setScale(0, BigDecimal.ROUND_UP);
                                    financeManagerListVO.setCanWithholdDesc(String.format(Constant.CANWITHHOLD_YES_2,divide.intValue()));
                                }else if(planRepayAmount.compareTo(dayLimit)>0){
                                    financeManagerListVO.setCanWithholdDesc(Constant.CANWITHHOLD_YES_3);
                                }
                            }
                        }
                    }
                }
            }


        } catch (Exception e) {
            logger.error("获取绑卡信息失败{}", e.getMessage());
        }
        logger.info("=======财务列表list{}",JSON.toJSONString(list));
        return PageResult.success(list, count);
    }


    public PageResult selectByFinanceManagerListReqBak(FinanceManagerListReq req) {
        int count = repaymentBizPlanListMapper.conutFinanceManagerList(req);
        List<String> businessList = new ArrayList<>();
        Set<String> businessSet = new HashSet();

        List<FinanceManagerListVO> list = repaymentBizPlanListMapper.selectFinanceMangeList(req);
        for (FinanceManagerListVO financeManagerListVO : list) {
            businessSet.add(financeManagerListVO.getBusinessId());
            RepaymentResource repaymentResource = repaymentResourceService.selectOne(new EntityWrapper<RepaymentResource>().eq("org_business_id", financeManagerListVO.getOrgBusinessId()).eq("after_id", financeManagerListVO.getAfterId()));
//            if (repaymentResource != null) {
//                if (repaymentResource.getRepaySource().equals("30") || repaymentResource.getRepaySource().equals("31")) {//银行代扣
//                    financeManagerListVO.setBankRepay(true);
//                }
//            } else {//为空代表没有代扣过
//                financeManagerListVO.setBankRepay(false);
//            }
            /*未代扣确认的不能代扣*/
//			if (financeManagerListVO.getConfirmFlag()==null||financeManagerListVO.getConfirmFlag().equals(0)) {
//				financeManagerListVO.setCanWithhold(false);
//				continue;
//			}
//			/*已还款的不能代扣*/
//			if (financeManagerListVO.getStatus().equals(RepayCurrentStatusEnums.已还款.toString())) {
//				financeManagerListVO.setCanWithhold(false);
//				continue;
//			}
//			if (financeManagerListVO.getBusinessId().equals(financeManagerListVO.getOrgBusinessId())) {
//				/*正常业务最后一期不能代扣*/
//				if (financeManagerListVO.getBorrowLimit().equals(financeManagerListVO.getPeriod().toString())) {
//					financeManagerListVO.setCanWithhold(false);
//					continue;
//				}
//			}else {
//				/*展期业务00期不能代扣*/
//				if (financeManagerListVO.getPeriod().equals(0)) {
//					financeManagerListVO.setCanWithhold(false);
//					continue;
//				}
//				/*展期业务最后一期不能代扣*/
//				if (financeManagerListVO.getBorrowLimit().equals(financeManagerListVO.getPeriod().toString())) {
//					financeManagerListVO.setCanWithhold(false);
//					continue;
//				}
//			}
//			financeManagerListVO.setCanWithhold(true);


        }

        try {
            Map<String, Map<String, BigDecimal>> map = new HashMap<>();
            for (FinanceManagerListVO financeManagerListVO : list) {
                financeManagerListVO.setCanWithhold(false);
                financeManagerListVO.setCanWithholdDesc(Constant.CANWITHHOLD_NO);

                for (String businessId : businessSet) {
                    if (financeManagerListVO.getBusinessId().equals(businessId)) {
                     BigDecimal planRepayAmount=financeManagerListVO.getPlanRepayAmount();
                        //查询身份证信息
                        BasicBusiness basicBusiness = basicBusinessService.selectOne(new EntityWrapper<BasicBusiness>().eq("business_id", businessId));
                        String identityCard = basicBusiness.getCustomerIdentifyCard();
                        RequestData requestData = new RequestData();
                        JSONObject data = new JSONObject();
                        data.put("identityCard", identityCard);
                        requestData.setData(JSON.toJSONString(data));
                        requestData.setMethodName("BankCard_GetBankCardByIdCard");
                        String encryptStr = JSON.toJSONString(requestData);
                        // 请求数据加密
                        encryptStr = encryptPostData(encryptStr);
                        XindaiFeign XindaiFeignService = Feign.builder().target(XindaiFeign.class, apiUrl);

                        String respStr = XindaiFeignService.getBankcardInfo(encryptStr);
                        // 返回数据解密
                        ResponseData respData = getRespData(respStr);
                        logger.info("客户根据身份证号:" + identityCard + "获取银行卡信息，接口返回数据:" + respData.getData() + "," + respData.getReturnMessage());
                        if (respData.getData() != null) {

                           JSONArray jsonObject = JSON.parseArray(respData.getData());

                           List<BankCardInfo> bankList= jsonObject.toJavaList(BankCardInfo.class);
                            if (bankList !=null) {
                                financeManagerListVO.setCanWithhold(true); //有绑定平台可以进行代扣
                                String bankCode = bankList.get(0).getBankCode();
                                //查询单笔最高金额
                                BigDecimal onceLimit = sysBankLimitService.selectOnceLimit(bankCode);
                                //查询单日最高金额
                                BigDecimal dayLimit = sysBankLimitService.selectMaxDayLimit(bankCode);

                                //若未绑定代扣平台取“否”，鼠标移到否时需提示文案为“该卡当前不支持代扣，请及时更换”
                                //若为”是“则需情况进行文案提示：
                                // A.该卡单次代扣限额>=本次还款金额，提示“限额足够，可一次代扣”；
                                // B.该卡单次代扣限额<本次还款金额<=单日代扣限额，提示“单次限额不够，本次还款需要代扣x次。X=本次还款金额/单笔代扣额度 进1一次
                                // C.卡单日代扣限额<本次还款金额，提示“代扣限额不足，请换卡”
                                //planRepayAmount
                                if(planRepayAmount.compareTo(onceLimit)<=0){
                                    financeManagerListVO.setCanWithholdDesc(Constant.CANWITHHOLD_YES_1);
                                }else if(planRepayAmount.compareTo(onceLimit)>0 && planRepayAmount.compareTo(dayLimit)<=0){
                                    BigDecimal divide = planRepayAmount.divide(onceLimit).setScale(0, BigDecimal.ROUND_UP);
                                    financeManagerListVO.setCanWithholdDesc(String.format(Constant.CANWITHHOLD_YES_2,divide.intValue()));
                                }else if(planRepayAmount.compareTo(dayLimit)>0){
                                    financeManagerListVO.setCanWithholdDesc(Constant.CANWITHHOLD_YES_3);
                                }
                            }
                        }
                    }
                }
            }


        } catch (Exception e) {
            logger.error("获取绑卡信息失败{}", e.getMessage());
        }
        logger.info("=======财务列表list{}",JSON.toJSONString(list));
        return PageResult.success(list, count);
    }

    // 加密
    private String encryptPostData(String str) throws Exception {

        DESC desc = new DESC();
        str = desc.Encryption(str);

        return str;
    }

    // 返回数据解密
    private ResponseData getRespData(String str) throws Exception {
        ResponseEncryptData resp = JSON.parseObject(str, ResponseEncryptData.class);
        String decryptStr = decryptRespData(resp);
        EncryptionResult result = JSON.parseObject(decryptStr, EncryptionResult.class);
        ResponseData respData = JSON.parseObject(result.getParam(), ResponseData.class);

        return respData;

    }

    // 解密
    private String decryptRespData(ResponseEncryptData data) throws Exception {

        DESC desc = new DESC();
        String str = desc.Decode(data.getA(), data.getUUId());
        return str;
    }

    @Override
    public RepaymentBizPlanList findCurrentPeriod(Date settleDate, List<RepaymentBizPlanList> planLists) {
        RepaymentBizPlanList finalPeriod = planLists.get(planLists.size() - 1);
        int diff = DateUtil.getDiffDays(settleDate, finalPeriod.getDueDate());
        RepaymentBizPlanList currentPeriod = null;

        // 提前还款结清
        if (diff > 0) {
            RepaymentBizPlanList temp = new RepaymentBizPlanList();
            temp.setDueDate(settleDate);
            temp.setBusinessId("temp");
            // 把提前结清的日期放进PlanLists一起比较
            planLists.add(temp);
            planLists.sort(
                    (RepaymentBizPlanList a1, RepaymentBizPlanList a2) -> a1.getDueDate().compareTo(a2.getDueDate()));
            for (int i = 0; i < planLists.size(); i++) {
                if (planLists.get(i).getBusinessId().equals("temp")) {
                    currentPeriod = planLists.get(i + 1);
                }
            }

            // 筛选temp记录
            for (Iterator<RepaymentBizPlanList> it = planLists.iterator(); it.hasNext(); ) {
                RepaymentBizPlanList pList = it.next();
                if (pList.getBusinessId().equals("temp")) {
                    it.remove();
                }
            }

        } else {
            // 期外
            currentPeriod = finalPeriod;
        }


        return currentPeriod;
    }


//    @Override
//    public List<RepaymentBizPlanList> selectNeedPhoneUrgRenewBiz(String companyId,Integer beforeDueDays) {
//        if(beforeDueDays.equals(0)){
//            beforeDueDays = 1000;
//        }
//       return repaymentBizPlanListMapper.selectNeedSetColInfoRenewBizPlansBycomId(
//               companyId,
//               0-beforeDueDays,
//               CollectionStatusEnum.PHONE_STAFF.getKey()
//       );
//    }
//
//    @Override
//    public List<RepaymentBizPlanList> selectNeedVisitRenewBiz(String companyId,Integer overDueDays) {
//        return repaymentBizPlanListMapper.selectNeedSetColInfoRenewBizPlansBycomId(
//                companyId,
//                overDueDays,
//                CollectionStatusEnum.COLLECTING.getKey()
//        );
//    }
//
//    @Override
//    public List<RepaymentBizPlanList> selectNeedLawRenewBiz(Integer overDueDays) {
//        return repaymentBizPlanListMapper.selectNeedSetColInfoRenewBizPlansBycomId(
//                null,
//                overDueDays,
//                CollectionStatusEnum.TO_LAW_WORK.getKey()
//        );
//    }

    @Override
    public Integer queryFirstPeriodOverdueByBusinessId(String businessId) {
        return repaymentBizPlanListMapper.queryFirstPeriodOverdueByBusinessId(businessId);
    }

    @Override
    public Integer queryInterestOverdueByBusinessId(String businessId) {
        return repaymentBizPlanListMapper.queryInterestOverdueByBusinessId(businessId);
    }

    @Override
    public Integer queryPrincipalOverdueByBusinessId(String businessId) {
        return repaymentBizPlanListMapper.queryPrincipalOverdueByBusinessId(businessId);
    }

    @Override
    public List<RepaymentBizPlanList> selectAutoRepayList(Integer days) {
        return repaymentBizPlanListMapper.selectAutoRepayList(days);
    }

    @Override
    public List<ConfirmWithholdListVO> listConfirmWithhold(String businessId) {
        return repaymentBizPlanListMapper.listConfirmWithhold(businessId);
    }

    @Override
    public List<RepaymentBizPlanList> getPlanListForCalLateFee(String planListId) {

        return repaymentBizPlanListMapper.getPlanListForCalLateFee(planListId);
    }
}
