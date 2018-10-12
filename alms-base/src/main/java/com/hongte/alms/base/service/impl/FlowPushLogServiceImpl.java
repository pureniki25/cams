package com.hongte.alms.base.service.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.AgencyRechargeLog;
import com.hongte.alms.base.entity.FlowPushLog;
import com.hongte.alms.base.entity.RepaymentAdvanceRepayFlow;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentBizPlanListDetail;
import com.hongte.alms.base.entity.RepaymentPlatformList;
import com.hongte.alms.base.entity.RepaymentPlatformListBorrower;
import com.hongte.alms.base.entity.RepaymentPlatformListGuarantee;
import com.hongte.alms.base.entity.TdrepayRechargeLog;
import com.hongte.alms.base.enums.BusinessTypeEnum;
import com.hongte.alms.base.enums.repayPlan.RepayPlanFeeTypeEnum;
import com.hongte.alms.base.feignClient.AccountListHandlerMsgClient;
import com.hongte.alms.base.feignClient.EipRemote;
import com.hongte.alms.base.mapper.FlowPushLogMapper;
import com.hongte.alms.base.service.AgencyRechargeLogService;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.base.service.FlowPushLogService;
import com.hongte.alms.base.service.RepaymentAdvanceRepayFlowService;
import com.hongte.alms.base.service.RepaymentBizPlanListDetailService;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.base.service.RepaymentPlatformListBorrowerService;
import com.hongte.alms.base.service.RepaymentPlatformListGuaranteeService;
import com.hongte.alms.base.service.RepaymentPlatformListService;
import com.hongte.alms.base.service.TdrepayRechargeLogService;
import com.hongte.alms.base.util.UUIDHtGenerator;
import com.hongte.alms.base.vo.cams.CamsMessage;
import com.hongte.alms.base.vo.cams.CreateBatchFlowCommand;
import com.hongte.alms.base.vo.cams.CreateBatchFlowCommand.Business;
import com.hongte.alms.base.vo.cams.CreateBatchFlowCommand.Flow;
import com.hongte.alms.base.vo.cams.CreateBatchFlowCommand.FlowAccountIdentifier;
import com.hongte.alms.base.vo.cams.CreateBatchFlowCommand.FlowDetail;
import com.hongte.alms.base.vo.compliance.DistributeFundRecordVO;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.DateUtil;
import com.ht.ussp.core.Result;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 刘正全
 * @since 2018-09-05
 */
@Service("FlowPushLogService")
public class FlowPushLogServiceImpl extends BaseServiceImpl<FlowPushLogMapper, FlowPushLog> implements FlowPushLogService {

	@Autowired
	private EipRemote eipRemote;
	
    @Autowired
    private AccountListHandlerMsgClient accountListHandlerMsgClient;
	
	@Autowired
	@Qualifier("RepaymentPlatformListService")
	RepaymentPlatformListService repaymentPlatformListService;
	
	@Autowired
	@Qualifier("RepaymentAdvanceRepayFlowService")
	RepaymentAdvanceRepayFlowService repaymentAdvanceRepayFlowService;
	
	@Autowired
	@Qualifier("RepaymentPlatformListBorrowerService")
	RepaymentPlatformListBorrowerService repaymentPlatformListBorrowerService;
	
	@Autowired
	@Qualifier("RepaymentPlatformListGuaranteeService")
	RepaymentPlatformListGuaranteeService repaymentPlatformListGuaranteeService;
	
    @Autowired
    @Qualifier("FlowPushLogService")
    FlowPushLogService flowPushLogService;
    
    @Autowired
    @Qualifier("AgencyRechargeLogService")
    AgencyRechargeLogService agencyRechargeLogService;
    
    @Autowired
    @Qualifier("RepaymentBizPlanListService")
    RepaymentBizPlanListService repaymentBizPlanListService;
    
    @Autowired
    @Qualifier("RepaymentBizPlanListDetailService")
    RepaymentBizPlanListDetailService repaymentBizPlanListDetailService;
    
    @Autowired
    @Qualifier("BasicBusinessService")
    BasicBusinessService basicBusinessService;
    
    @Autowired
    @Qualifier("TdrepayRechargeLogService")
    TdrepayRechargeLogService tdrepayRechargeLogService;
    
    @Autowired
    private FlowPushLogMapper flowPushLogMapper;
    
    private String mainIdTouZi = "97ff0bff-e93d-11e7-94ed-94c69109b34a";//投资人
    private String dMainIdTouZi = "97ff0bff-e93d-11e7-94ed-94c69109b34a";
    
    private String mainIdPlatfrom = "91441900MA4ULXKB38";//平台
    private String dMainIdPlatfrom = "51F2CBF5-9076-4AC9-9EC4-0373CF803070";
    
    private String danbao = "深圳市天大联合融资担保有限公司";
    private String mainIdDanbao = "45BC0637-412F-45AF-A44A-14348BEB400C";//深圳市天大联合融资担保有限公司
    private String dMainIdDanbao = "45BC0637-412F-45AF-A44A-14348BEB400C";
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FlowPushLogServiceImpl.class);
	
	@Override
	public Result queryDistributeFundRecord(String projectId) {
		Result queryProjectPaymentResult = null;
	    Result advanceShareProfitResult = null;
	    
	    Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("projectId", projectId);
		DistributeFundRecordVO distributeFundRecordVO = new DistributeFundRecordVO();
	    try {
	      LOGGER.info("标的还款信息查询接口/eip/td/repayment/queryProjectPayment参数信息，{}", paramMap);
	      queryProjectPaymentResult = eipRemote.queryProjectPayment(paramMap); // 标的还款信息
	      LOGGER.info("标的还款信息查询接口/eip/td/repayment/queryProjectPayment返回信息，{}", JSONObject.toJSONString(queryProjectPaymentResult));
	      String returnCode = queryProjectPaymentResult.getReturnCode();
	      if("0000".equals(returnCode)) {
	    	 Map<String,Object> retData = (Map<String, Object>) queryProjectPaymentResult.getData();
	    	 String message = retData.get("message").toString();
	    	 //标的ID
	    	 String projectId1 = projectId;//retData.get("projectId").toString();
	    	 int status = Integer.parseInt(retData.get("status").toString());
	    	 //projectPayment还款信息list
	    	 List<Map<String,Object>> projectPayments = (List<Map<String, Object>>) retData.get("projectPayments");
	    	 RepaymentPlatformList repaymentPlatformList = null;
	    	 RepaymentPlatformListBorrower repaymentPlatformListBorrower = null;
	    	 RepaymentPlatformListGuarantee repaymentPlatformListGuarantee = null;
	    	 if(null != projectPayments) {
	    		 for (Map<String, Object> map : projectPayments) {
	    			//还款状态1 已结清 0逾期
	    			 
	    			 BigDecimal totalAmountRepay = new BigDecimal(0);
	    			 BigDecimal totalAmountBorrower = new BigDecimal(0);
	    			 BigDecimal totalAmountGuarantee = new BigDecimal(0);
	    			 
	    			 int projectPaymentsStatus = Integer.parseInt(map.get("status").toString());
	    			 if(projectPaymentsStatus == 1 || projectPaymentsStatus == 0) {
	    			 repaymentPlatformList = new RepaymentPlatformList();
	    			 //期数
	    			 int period = Integer.parseInt(map.get("period").toString());
	    			 //还款日期 yyyy-MM-dd
	    			 String addDate =  map.get("addDate").toString();
	    			 //实还总金额
	    			 BigDecimal totalAmount = null == map.get("totalAmount")?null:new BigDecimal(map.get("totalAmount").toString());
	    			 if(null != totalAmount) {
	    				 totalAmountRepay = totalAmount;
	    			 }
	    			 //borrowerPayment借款人还款信息
	    			 //实还本息
	    			 BigDecimal principalAndInterest = null == map.get("principalAndInterest")?null:new BigDecimal(map.get("principalAndInterest").toString());
	    			 //实还平台服务费
	    			 BigDecimal tuandaiAmount = null == map.get("tuandaiAmount")?null:new BigDecimal(map.get("tuandaiAmount").toString());
	    			 //实还资产端服务费
	    			 BigDecimal orgAmount = null == map.get("orgAmount")?null:new BigDecimal(map.get("orgAmount").toString());
	    			 //实还担保公司服务费
	    			 BigDecimal guaranteeAmount = null == map.get("guaranteeAmount")?null:new BigDecimal(map.get("guaranteeAmount").toString());
	    			 //实还仲裁服务费
	    			 BigDecimal arbitrationAmount = null == map.get("arbitrationAmount")?null:new BigDecimal(map.get("arbitrationAmount").toString());
	    			 //实还中介服务费
	    			 BigDecimal agencyAmount = null == map.get("agencyAmount")?null:new BigDecimal(map.get("agencyAmount").toString());
	    			 //保险费
	    			 BigDecimal insuranceAmount = null == map.get("insuranceAmount")?null:new BigDecimal(map.get("insuranceAmount").toString());
	    			 //特权包
	    			 BigDecimal privilegePackageAmount = null == map.get("privilegePackageAmount")?null:new BigDecimal(map.get("privilegePackageAmount").toString());
	    			 //滞纳金
	    			 BigDecimal penaltyAmount = null == map.get("penaltyAmount")?null:new BigDecimal(map.get("penaltyAmount").toString());
	    			
	    			 repaymentPlatformList.setProjectId(projectId1);
	    			 repaymentPlatformList.setPeriod(period);
	    			 repaymentPlatformList.setAddDate(addDate);
	    			 repaymentPlatformList.setRepayStatus(projectPaymentsStatus);
	    			 repaymentPlatformList.setTotalAmount(totalAmount);
	    			 repaymentPlatformList.setPrincipalAndinterest(principalAndInterest);
	    			 repaymentPlatformList.setTuandaiAmount(tuandaiAmount);
	    			 repaymentPlatformList.setOrgAmount(orgAmount);
	    			 repaymentPlatformList.setGuaranteeAmount(guaranteeAmount);
	    			 repaymentPlatformList.setArbitrationAmount(arbitrationAmount);
	    			 repaymentPlatformList.setAgencyAmount(agencyAmount);
	    			 repaymentPlatformList.setInsuranceAmount(insuranceAmount);
	    			 repaymentPlatformList.setPrivilegePackageAmount(privilegePackageAmount);
	    			 repaymentPlatformList.setPenaltyAmount(penaltyAmount);
	    			 
	    			 //borrowerPayment借款人还款信息list，字段描述
	    			 Map<String,Object> borrowerPaymentMap = (Map<String, Object>) map.get("borrowerPayment");
	    			 if(null != borrowerPaymentMap) {
	    				 //实还本息
		    			 BigDecimal principalAndInterestBorrower = null == borrowerPaymentMap.get("principalAndInterest")?null:new BigDecimal(borrowerPaymentMap.get("principalAndInterest").toString());
		    			 if(null != principalAndInterestBorrower) {
		    				 totalAmountBorrower = totalAmountBorrower.add(principalAndInterestBorrower);
	    				 }
		    			 //实还平台服务费
		    			 BigDecimal tuandaiAmountBorrower = null == borrowerPaymentMap.get("tuandaiAmount")?null:new BigDecimal(borrowerPaymentMap.get("tuandaiAmount").toString());
		    			 if(null != tuandaiAmountBorrower) {
		    				 totalAmountBorrower = totalAmountBorrower.add(tuandaiAmountBorrower);
	    				 }
		    			 //实还资产端服务费
		    			 BigDecimal orgAmountBorrower = null == borrowerPaymentMap.get("orgAmount")?null:new BigDecimal(borrowerPaymentMap.get("orgAmount").toString());
		    			 if(null != principalAndInterestBorrower) {
		    				 tuandaiAmountBorrower = totalAmountBorrower.add(principalAndInterestBorrower);
	    				 }
		    			 //实还担保公司服务费
		    			 BigDecimal guaranteeAmountBorrower = null == borrowerPaymentMap.get("guaranteeAmount")?null:new BigDecimal(borrowerPaymentMap.get("guaranteeAmount").toString());
		    			 if(null != guaranteeAmountBorrower) {
		    				 totalAmountBorrower = totalAmountBorrower.add(guaranteeAmountBorrower);
	    				 }
		    			 //实还仲裁服务费
		    			 BigDecimal arbitrationAmountBorrower = null == borrowerPaymentMap.get("arbitrationAmount")?null:new BigDecimal(borrowerPaymentMap.get("arbitrationAmount").toString());
		    			 if(null != arbitrationAmountBorrower) {
		    				 totalAmountBorrower = totalAmountBorrower.add(arbitrationAmountBorrower);
	    				 }
		    			 //实还中介服务费
		    			 BigDecimal agencyAmountBorrower = null == borrowerPaymentMap.get("agencyAmount")?null:new BigDecimal(borrowerPaymentMap.get("agencyAmount").toString());
		    			 if(null != agencyAmountBorrower) {
		    				 totalAmountBorrower = totalAmountBorrower.add(agencyAmountBorrower);
	    				 }
		    			 //保险费
		    			 BigDecimal insuranceAmountBorrower = null == borrowerPaymentMap.get("insuranceAmount")?null:new BigDecimal(borrowerPaymentMap.get("insuranceAmount").toString());
		    			 if(null != insuranceAmountBorrower) {
		    				 totalAmountBorrower = totalAmountBorrower.add(insuranceAmountBorrower);
	    				 }
		    			 //特权包
		    			 BigDecimal privilegePackageAmountBorrower = null == borrowerPaymentMap.get("privilegePackageAmount")?null:new BigDecimal(borrowerPaymentMap.get("privilegePackageAmount").toString());
		    			 if(null != privilegePackageAmountBorrower) {
		    				 totalAmountBorrower = totalAmountBorrower.add(privilegePackageAmountBorrower);
	    				 }
		    			 
		    			 repaymentPlatformListBorrower = new RepaymentPlatformListBorrower();
		    			 repaymentPlatformListBorrower.setProjectId(projectId1);
		    			 repaymentPlatformListBorrower.setPeriod(period);
		    			 repaymentPlatformListBorrower.setRepayStatus(status);
		    			 repaymentPlatformListBorrower.setAddDate(addDate);
		    			 repaymentPlatformListBorrower.setPrincipalAndinterest(principalAndInterestBorrower);
		    			 repaymentPlatformListBorrower.setTuandaiAmount(tuandaiAmountBorrower);
		    			 repaymentPlatformListBorrower.setOrgAmount(orgAmountBorrower);
		    			 repaymentPlatformListBorrower.setGuaranteeAmount(guaranteeAmountBorrower);
		    			 repaymentPlatformListBorrower.setArbitrationAmount(arbitrationAmountBorrower);
		    			 repaymentPlatformListBorrower.setAgencyAmount(agencyAmountBorrower);
		    			 repaymentPlatformListBorrower.setInsuranceAmount(insuranceAmountBorrower);
		    			 repaymentPlatformListBorrower.setPrivilegePackageAmount(privilegePackageAmountBorrower);
		    			 repaymentPlatformListBorrower.setCreateMan("平台接口获取");
		    			 repaymentPlatformListBorrower.setCreateTime(new Date());
	    			 }
	    			 
	    			//guaranteePayment担保公司垫付信息，字段描述
	    			 Map<String,Object> guaranteePaymentMap = (Map<String, Object>) map.get("guaranteePayment");
	    			 if(null != guaranteePaymentMap) {
	    				
	    				 //本金利息
	    				 BigDecimal principalAndInterestGuarantee = null == guaranteePaymentMap.get("principalAndInterest")?null:new BigDecimal(guaranteePaymentMap.get("principalAndInterest").toString());
	    				 if(null != principalAndInterestGuarantee) {
	    					 totalAmountGuarantee = totalAmountGuarantee.add(principalAndInterestGuarantee);
	    				 }
	    				 //滞纳金
	    				 BigDecimal penaltyAmountGuarantee = null == guaranteePaymentMap.get("penaltyAmount")?null:new BigDecimal(guaranteePaymentMap.get("penaltyAmount").toString());
	    				 if(null != penaltyAmountGuarantee) {
	    					 totalAmountGuarantee = totalAmountGuarantee.add(penaltyAmountGuarantee);
	    				 }
	    				 //实还平台服务费
	    				 BigDecimal tuandaiAmountGuarantee = null == guaranteePaymentMap.get("tuandaiAmount")?null:new BigDecimal(guaranteePaymentMap.get("tuandaiAmount").toString());
	    				 if(null != tuandaiAmountGuarantee) {
	    					 totalAmountGuarantee = totalAmountGuarantee.add(tuandaiAmountGuarantee);
	    				 }
	    				 //实还资产端服务费
	    				 BigDecimal orgAmountGuarantee = null == guaranteePaymentMap.get("orgAmount")?null:new BigDecimal(guaranteePaymentMap.get("orgAmount").toString());
	    				 if(null != orgAmountGuarantee) {
	    					 totalAmountGuarantee = totalAmountGuarantee.add(orgAmountGuarantee);
	    				 }
	    				 //实还担保公司服务费
	    				 BigDecimal guaranteeAmountGuarantee = null == guaranteePaymentMap.get("guaranteeAmount")?null:new BigDecimal(guaranteePaymentMap.get("guaranteeAmount").toString());
	    				 if(null != guaranteeAmountGuarantee) {
	    					 totalAmountGuarantee = totalAmountGuarantee.add(guaranteeAmountGuarantee);
	    				 }
	    				 //实还仲裁服务费
	    				 BigDecimal arbitrationAmountGuarantee = null == guaranteePaymentMap.get("arbitrationAmount")?null:new BigDecimal(guaranteePaymentMap.get("arbitrationAmount").toString());
	    				 if(null != arbitrationAmountGuarantee) {
	    					 totalAmountGuarantee = totalAmountGuarantee.add(arbitrationAmountGuarantee);
	    				 }
	    				 //实还中介服务费
	    				 BigDecimal agencyAmountGuarantee = null == guaranteePaymentMap.get("agencyAmount")?null:new BigDecimal(guaranteePaymentMap.get("agencyAmount").toString());
	    				 if(null != agencyAmountGuarantee) {
	    					 totalAmountGuarantee = totalAmountGuarantee.add(agencyAmountGuarantee);
	    				 }
	    				 repaymentPlatformListGuarantee = new RepaymentPlatformListGuarantee();
	    				 repaymentPlatformListGuarantee.setProjectId(projectId1);
	    				 repaymentPlatformListGuarantee.setPeriod(period);
	    				 repaymentPlatformListGuarantee.setRepayStatus(status);
	    				 repaymentPlatformListGuarantee.setAddDate(addDate);
	    				 repaymentPlatformListGuarantee.setPrincipalAndinterest(principalAndInterestGuarantee);
	    				 repaymentPlatformListGuarantee.setPenaltyAmount(penaltyAmountGuarantee);
	    				 repaymentPlatformListGuarantee.setTuandaiAmount(tuandaiAmountGuarantee);
	    				 repaymentPlatformListGuarantee.setOrgAmount(orgAmountGuarantee);
	    				 repaymentPlatformListGuarantee.setGuaranteeAmount(guaranteeAmountGuarantee);
	    				 repaymentPlatformListGuarantee.setArbitrationAmount(arbitrationAmountGuarantee);
	    				 repaymentPlatformListGuarantee.setAgencyAmount(agencyAmountGuarantee);
	    				 repaymentPlatformListGuarantee.setCreateMan("平台接口获取");
	    				 repaymentPlatformListGuarantee.setCreateTime(new Date());
	    				 repaymentPlatformListGuarantee.setTotalAmount(totalAmountGuarantee);
	    			 }
	    			 
	    			//通过标号和期号取平台还款记录 ,如果有比较总金额 相同则不改 不同则修改 没有记录则插入
	    			 RepaymentPlatformList repaymentPlatformListOld = repaymentPlatformListService.selectOne(new EntityWrapper<RepaymentPlatformList>().eq("project_id", projectId1).eq("period", period));
	    			 int updateType = 0;
	    			 
	    			 //如果平台还款 总金额 》 还款+垫付金额 暂不记录  totalAmountRepay.compareTo(totalAmountBorrower.add(totalAmountGuarantee)
	    			 if(totalAmountBorrower.compareTo(new BigDecimal(0)) == 0 
	    					 && totalAmountGuarantee.compareTo(new BigDecimal(0)) == 0) {
	    				 updateType = 2;
	    			 }
	    			 
	    			 if(repaymentPlatformListOld == null && 2 != updateType) {
	    				 updateType = 1;
	    				 repaymentPlatformList.setCreateMan("平台接口获取");
		    			 repaymentPlatformList.setCreateTime(new Date());
	    				 repaymentPlatformListService.insert(repaymentPlatformList);
	    			 }
	    			 if(null != repaymentPlatformListBorrower && 1 == updateType) {
	    				 repaymentPlatformListBorrower.setRepayId(repaymentPlatformList.getId());
	    				 repaymentPlatformListBorrowerService.insert(repaymentPlatformListBorrower);
	    			 }
	    			 if(null != repaymentPlatformListGuarantee && 1 == updateType) {
	    				 repaymentPlatformListGuarantee.setRepayId(repaymentPlatformList.getId());
	    				 System.err.println(JSONObject.toJSONString(repaymentPlatformListGuarantee));
	    				 repaymentPlatformListGuaranteeService.insert(repaymentPlatformListGuarantee);
	    			 }
	    			}
	    		 }
	    	 }
	      }
//	      LOGGER.info("还垫付信息查询接口/eip/td/repayment/returnAdvanceShareProfit参数信息，{}", paramMap);
//	      advanceShareProfitResult = eipRemote.returnAdvanceShareProfit(paramMap); // 还垫付信息
//	      LOGGER.info("还垫付信息查询接口/eip/td/repayment/returnAdvanceShareProfit返回信息，{}", advanceShareProfitResult);
	    } catch (Exception e) {
	      LOGGER.error(e.getMessage(), e);
	    }
	    
	    //推送平台还款流水
//	    pushFlow(7);
	    //推送垫付流水
//	    pushFlow(8);
		return queryProjectPaymentResult;
	}
	
    public Result<Object> pushFlow(int actionId) {
    	//核心流程推送流程
    	//1# step1 查出未推送和推送失败的业务 list  tb_basic_business加3列 最后推送时间 最后推送状态 最后推送备注   另增加推送流水表
    	Map<String,Object> paramBusinessMap = new HashMap<>();
    	List<Map<String,Object>> listMap = getPushFlowList(actionId,paramBusinessMap);
    	addBusinessCamsFlow(listMap,7);
    	return Result.buildSuccess(0);
    }
	
	/**
	 * @Title: addBusinessCamsFlow  
	 * @param listMap 要推送的流水集合
	 * @param actionId  要推送的流水 交易活動  7平台还款,8垫付 81 还垫付
	 * @return void 返回类型  
	 */
	private void addBusinessCamsFlow(List<Map<String, Object>> listMap,int actionId) {
		//2# step2 循环业务list，去除每一条业务的流水list
		int realActionId = actionId;
		if(actionId == 201) {
			realActionId = 2;
		}
    	for(Map<String,Object> businessMapInfo : listMap) {
    		CreateBatchFlowCommand command = new CreateBatchFlowCommand();
    		
    		String confirmLogId = businessMapInfo.get("confirm_log_id")+"";
    		//交易活动,0满标分润,1提现放款,2正常还款,3提前结清,4业务退费,5资金分发,6展期确认,7平台还款,8垫付,9账户提现,10账户充值,11账户转账,12暂收款登记
        	String batchId = confirmLogId;
        	FlowPushLog flowPushLog = new FlowPushLog();
        	flowPushLog.setPushKey(batchId);
        	flowPushLog.setPushLogType(realActionId);
        	flowPushLog.setPushTo(1);
        	flowPushLog.setPushStarttime(new Date());
        	flowPushLog.setPushStatus(0);
        	//是否业务交易明细,1是,0否
        	int businessFlag = Integer.parseInt(businessMapInfo.get("businessFlag").toString());
        	//所属资产端
        	int businessFrom = Integer.parseInt(businessMapInfo.get("businessFrom")==null?"1":businessMapInfo.get("businessFrom").toString());
        	String clientId = "ALMS"; //businessMapInfo.get("plate_type")+
        	Date createTime = new Date();//(Date) businessMapInfo.get("create_time");
        	String createUser = businessMapInfo.get("create_user")==null?"":businessMapInfo.get("create_user").toString();
        	String messageId = confirmLogId+"-"+actionId;
        	
        	String branchId = businessMapInfo.get("company_id")==null?"":businessMapInfo.get("company_id").toString();
        	String branchName = businessMapInfo.get("company_name")==null?"":businessMapInfo.get("company_name").toString();
        	String businessId = businessMapInfo.get("business_id")==null?"":businessMapInfo.get("business_id").toString();
        	String businessType = businessMapInfo.get("business_type_name")==null?"":businessMapInfo.get("business_type_name").toString();//business_type
        	String businessCtype = businessMapInfo.get("business_ctype")==null?"":businessMapInfo.get("business_ctype").toString();
        	if(!StringUtils.isBlank(businessCtype)) {
        		businessType = businessCtype;
        	}
        	String businessTypeId = businessMapInfo.get("business_type")==null?"":businessMapInfo.get("business_type").toString();
        	String customerName = businessMapInfo.get("customer_name")==null?"":businessMapInfo.get("customer_name").toString();
        	String exhibitionId = businessMapInfo.get("business_id")==null?"":businessMapInfo.get("business_id").toString();
        	Business business = new Business();
        	business.setBranchId(branchId);
        	business.setBranchName(branchName);
        	business.setBusinessId(businessId);
        	business.setBusinessType(businessType);
        	business.setBusinessTypeId(businessTypeId);
        	business.setCustomerName(customerName);
        	business.setExhibitionId(exhibitionId);
        	
        	//取流水List集合  分为线上代扣流水 和 线下转账财务确认流水
        	//2 取流水信息
        	Map<String,Object> paramOnlineFlowMap = new HashMap<>();
        	paramOnlineFlowMap.put("confirmLogId", confirmLogId);
        	List<Map<String,Object>> listFlow = getFlowInfoById(actionId,paramOnlineFlowMap,business);
        	//3# step3 循环流水list，取出每条流水明细集合
        	List<Flow> flows = new ArrayList<>();
        	List<FlowAccountIdentifier> accountIdentifiers = new ArrayList<>();
        	List<FlowDetail> flowDetails = new ArrayList<>();
        	for (Map<String, Object> flowMap : listFlow) {
        		String sId = UUIDHtGenerator.getUUID();
        		String tId = UUIDHtGenerator.getUUID();
        		if(actionId == 201) {
        			sId = null;
        			tId = null;
        		}
        		//还款账号
            	FlowAccountIdentifier flowAccountIdentifier = new FlowAccountIdentifier();
            	String accountName = flowMap.get("account_name")==null?"":flowMap.get("account_name").toString();
            	String bankCardNo = flowMap.get("bank_card_no")==null?"":flowMap.get("bank_card_no").toString();
            	
            	String targetAccountId = flowMap.get("target_account_id")==null?"":flowMap.get("target_account_id").toString();
            	String targetBankCardNo = flowMap.get("target_bank_card_no")==null?"":flowMap.get("target_bank_card_no").toString();
            	
            	String depositoryId = null;//存管编号
            	//flowMap.get("main_id")+"";
            	String identifierId = tId;
            	Boolean personal = true;
            	
            	if(actionId != 201) {//你我金融没有账户信息
	            	int accountType = Integer.parseInt(flowMap.get("account_type").toString());
	            	String mainId = null==flowMap.get("main_id")?null:flowMap.get("main_id").toString();
	            	String openBank = flowMap.get("open_bank")==null?"":flowMap.get("open_bank").toString();
	            	if(actionId == 7 || actionId == 81) {//平台还款的客户为资金分发的用户
	            		accountName = customerName;
	            		mainId = null == businessMapInfo.get("td_user_id")?null:businessMapInfo.get("td_user_id").toString();
	            		bankCardNo = mainId;
	            	}
	            	
	            	flowAccountIdentifier.setAccountType(accountType);
	            	flowAccountIdentifier.setDepositoryId(bankCardNo);
	            	flowAccountIdentifier.setBankCardNo(bankCardNo);
	            	flowAccountIdentifier.setAccountName(accountName);
	            	flowAccountIdentifier.setIdentifierId(identifierId);
	            	flowAccountIdentifier.setPersonal(personal);
	            	if(!StringUtils.isBlank(mainId)) {
	            		flowAccountIdentifier.setMainId(mainId);
	            	}
	            	accountIdentifiers.add(flowAccountIdentifier);
	            	
	            	int targetAccountType = Integer.parseInt(flowMap.get("target_account_type").toString());
	            	String targetMainId = flowMap.get("target_main_id").toString();
	            	personal = false;
	            	
	            	FlowAccountIdentifier flowAccountIdentifier2 = new FlowAccountIdentifier();
	            	flowAccountIdentifier2.setAccountType(targetAccountType);
	            	flowAccountIdentifier2.setDepositoryId(targetBankCardNo);
	            	flowAccountIdentifier2.setIdentifierId(sId);
	            	flowAccountIdentifier2.setPersonal(personal);
	            	flowAccountIdentifier2.setAccountName(targetAccountId);
	            	if(!StringUtils.isBlank(targetMainId)) {
	            		flowAccountIdentifier2.setMainId(targetMainId);
	            	}
	            	accountIdentifiers.add(flowAccountIdentifier2);
            	}
            	
            	Flow flow = new Flow();
            	
            	Date accountTime = null;
            	if(actionId == 7 || actionId == 8 || actionId == 81)
            	{
            		accountTime = null == flowMap.get("account_time")?null:DateUtil.getDateTime(flowMap.get("account_time").toString());
            	}
            	else {
            		accountTime = (Date) flowMap.get("account_time");
            	}
            	
            	String afterId = null == flowMap.get("after_id")?null:flowMap.get("after_id").toString();
            	BigDecimal amount = new BigDecimal(flowMap.get("amount")==null?"0":flowMap.get("amount").toString());
            	String externalId = "";
            	int inOut = Integer.parseInt(flowMap.get("in_out").toString());
            	String issueId = flowMap.get("issue_id")==null?"":flowMap.get("issue_id").toString();
            	String memo = flowMap.get("memo")==null?"":flowMap.get("memo").toString();;
            	String remark = flowMap.get("remark")==null?"":flowMap.get("remark").toString();
            	Date segmentationDate = (Date) flowMap.get("segmentation_date");
            	String sourceAccountIdentifierId = sId;
            	String targetAccountIdentifierId = tId;
            	String listId = flowMap.get("list_id")==null?"":flowMap.get("list_id").toString();
            	int repayType = Integer.parseInt(flowMap.get("repay_type").toString());
            	
            	//收入流水   平台还款有多个收入流水
            	flow.setAccountTime(accountTime);
            	flow.setAfterId(afterId);
            	flow.setAmount(amount);
            	flow.setExternalId(externalId);
            	flow.setInOut(inOut);
            	flow.setIssueId(issueId);
            	flow.setMemo(memo);
            	flow.setRemark(remark);
            	flow.setRepayType(repayType);
            	flow.setSegmentationDate(segmentationDate);
            	flow.setSourceAccountIdentifierId(targetAccountIdentifierId);
            	flow.setTargetAccountIdentifierId(sourceAccountIdentifierId);
            	flows.add(flow);
            	
            	if(actionId != 201) {  //你我金融流水 不构建支出流水
	            	//支出流水
	            	Flow flow1 = new Flow();
	            	flow1.setAccountTime(accountTime);
	            	flow1.setAfterId(afterId);
	            	flow1.setAmount(amount);
	            	flow1.setExternalId(externalId);
	            	flow1.setInOut(1);
	            	flow1.setIssueId(issueId);
	            	flow1.setMemo(memo);
	            	flow1.setRemark(remark);
	            	flow1.setRepayType(repayType);
	            	flow1.setSegmentationDate(segmentationDate);
	            	flow1.setSourceAccountIdentifierId(sourceAccountIdentifierId);
	            	flow1.setTargetAccountIdentifierId(targetAccountIdentifierId);
	            	if(actionId != 7) {
	            		flows.add(flow1);
	            	}
            	}
            	
            	Map<String,Object> paramFlowItemMap = new HashMap<>();
            	paramFlowItemMap.put("repaySourceId", listId);
            	List<Map<String,Object>> listFlowItem = getFlowItemInfoById(actionId,paramFlowItemMap);
            	//封裝流水明細
            	for (Map<String, Object> listFlowItemMap : listFlowItem) {
            		Date detailAccountTime = accountTime;//(Date) listFlowItemMap.get("account_date");
            		String detailAfterId = afterId;//listFlowItemMap.get("after_id").toString();
            		BigDecimal detailAmount = new BigDecimal(listFlowItemMap.get("amount").toString());
            		String detailFeeId = listFlowItemMap.get("fee_id").toString();
            		String detailFeeName = listFlowItemMap.get("fee_name").toString();
            		if(StringUtils.isBlank(detailFeeId)) {
            			detailFeeId = detailFeeName;
            		}
            		String detailIssueId = issueId;//listFlowItemMap.get("issue_id")==null?"":listFlowItemMap.get("issue_id").toString();
            		int detailRegisterType = 0;//StringUtils.isBlank(flowMap.get("register_type")+"")?0:Integer.parseInt(listFlowItemMap.get("register_type").toString());
            		Date detailSegmentationDate = segmentationDate;//(Date) listFlowItemMap.get("segmentation_date");
            		
            		FlowDetail flowDetail = new FlowDetail();
            		flowDetail.setAccountTime(detailAccountTime);
            		flowDetail.setAfterId(detailAfterId);
            		flowDetail.setAmount(detailAmount);
            		flowDetail.setFeeId(detailFeeId);
            		flowDetail.setFeeName(detailFeeName);
            		flowDetail.setIssueId(detailIssueId);
            		flowDetail.setRegisterType(detailRegisterType);
            		flowDetail.setSegmentationDate(detailSegmentationDate);
            		flowDetails.add(flowDetail);
				}
            	paramFlowItemMap.put("businessId", businessId);
        	}
        		//3# step4 按业务组装流水 消息对象tb_money_pool
            	command.setActionId(realActionId);
            	command.setBatchId(batchId);
            	command.setBusinessFlag(businessFlag);
            	command.setBusinessFrom(businessFrom);
            	command.setClientId(clientId);
            	command.setCreateTime(createTime);
            	command.setCreateUser(createUser);
            	command.setMessageId(messageId);
        		command.setBusiness(business); 
        		
        		if(actionId == 7) {//平台还款拆分入账流水
            		addFlows(flows,accountIdentifiers,confirmLogId,business);
            	}
        		
        		command.setFlows(flows);
        		command.setAccountIdentifiers(accountIdentifiers);
        		command.setFlowDetails(flowDetails);
        		
    	    	command.setTriggerEventSystem("1");
    	    	command.setEventType("RepayFlow");
    	    	command.setTriggerEventType("CreateBatchFlowCommand");
    	    	
        		//4# step4 按业务组装流水 消息对象tb_money_pool
        		//5# step5 调用核心推送接口推送
            	System.err.println(JSONObject.toJSONString(command));
            	//Result<Object> ret = accountListHandlerClient.addBatchFlow(command);
            	
            	CamsMessage camsMessage = new CamsMessage();
            	camsMessage.setClientId("ALMS");
            	camsMessage.setExchangeName("cams.account.ms.exchange");
            	camsMessage.setHostPort(0);
            	camsMessage.setHostUrl("192.168.14.245");
            	camsMessage.setMessageId(UUID.randomUUID().toString());
            	camsMessage.setQueueName("cams.account.ms.queue.accountListCreatedQueueBatch");
            	camsMessage.setMessage(command);
            	
            	updatePushLog(actionId,confirmLogId, flowPushLog, camsMessage);
        	}
	}

	private void addFlows(List<Flow> flows, List<FlowAccountIdentifier> accountIdentifiers,String confirmLogId,Business business) {
		//取流水List集合  分为线上代扣流水 和 线下转账财务确认流水
    	//2 取流水信息
    	Map<String,Object> paramOnlineFlowMap = new HashMap<>();
    	paramOnlineFlowMap.put("confirmLogId", confirmLogId);
    	List<Map<String,Object>> listFlow = getFlowInfoById(71,paramOnlineFlowMap,business);
    	//3# step3 循环流水list，取出每条流水明细集合
    	for (Map<String, Object> flowMap : listFlow) {
    		String sId = UUIDHtGenerator.getUUID();
    		String tId = UUIDHtGenerator.getUUID();
    		//还款账号
        	FlowAccountIdentifier flowAccountIdentifier = new FlowAccountIdentifier();
        	String accountName = flowMap.get("account_name")==null?"":flowMap.get("account_name").toString();
        	String bankCardNo = flowMap.get("bank_card_no")==null?"":flowMap.get("bank_card_no").toString();
        	
        	String targetAccountId = flowMap.get("target_account_id")==null?"":flowMap.get("target_account_id").toString();
        	String targetBankCardNo = flowMap.get("target_bank_card_no")==null?"":flowMap.get("target_bank_card_no").toString();
        	
        	String depositoryId = null;//存管编号
        	//flowMap.get("main_id")+"";
        	String identifierId = tId;
        	Boolean personal = true;
        	
        	int accountType = null == flowMap.get("account_type")?1:Integer.parseInt(flowMap.get("account_type").toString());
        	String mainId = null == flowMap.get("main_id")?null:flowMap.get("main_id").toString();
        	String openBank = flowMap.get("open_bank")==null?"":flowMap.get("open_bank").toString();
        	flowAccountIdentifier.setAccountType(accountType);
        	flowAccountIdentifier.setDepositoryId(bankCardNo);
        	flowAccountIdentifier.setAccountName(accountName);
        	flowAccountIdentifier.setIdentifierId(sId);
        	flowAccountIdentifier.setPersonal(personal);
        	if(!StringUtils.isBlank(mainId)) {
        		flowAccountIdentifier.setMainId(mainId);
        	}
        	accountIdentifiers.add(flowAccountIdentifier);
        	
        	int targetAccountType = null == flowMap.get("target_account_type")?1:Integer.parseInt(flowMap.get("target_account_type").toString());
        	String targetMainId = flowMap.get("target_main_id")==null?null:flowMap.get("target_main_id").toString();
        	String targetAccountName = flowMap.get("target_account_name")==null?"":flowMap.get("target_account_name").toString();
        	personal = false;
        	//收入账号
        	FlowAccountIdentifier flowAccountIdentifier2 = new FlowAccountIdentifier();
        	flowAccountIdentifier2.setAccountName(targetAccountName);
        	flowAccountIdentifier2.setAccountType(targetAccountType);
        	flowAccountIdentifier2.setDepositoryId(targetBankCardNo);
        	flowAccountIdentifier2.setIdentifierId(tId);
        	flowAccountIdentifier2.setPersonal(personal);
        	flowAccountIdentifier2.setAccountName(targetAccountName);
        	if(!StringUtils.isBlank(targetMainId)) {
        		flowAccountIdentifier2.setMainId(targetMainId);
        	}
        	accountIdentifiers.add(flowAccountIdentifier2);
        	
        	Flow flow = new Flow();
        	
        	Date accountTime = null == flowMap.get("account_time")?null:DateUtil.getDateTime(flowMap.get("account_time").toString());
        	
        	String afterId = null == flowMap.get("after_id")?null:flowMap.get("after_id").toString();
        	BigDecimal amount = new BigDecimal(flowMap.get("amount")==null?"0":flowMap.get("amount").toString());
        	String externalId = "";
        	int inOut = Integer.parseInt(flowMap.get("in_out").toString());
        	String issueId = flowMap.get("issue_id")==null?"":flowMap.get("issue_id").toString();
        	String memo = flowMap.get("memo")==null?"":flowMap.get("memo").toString();;
        	String remark = flowMap.get("remark")==null?"":flowMap.get("remark").toString();
        	Date segmentationDate = (Date) flowMap.get("segmentation_date");
        	String sourceAccountIdentifierId = sId;
        	String targetAccountIdentifierId = tId;
        	String listId = flowMap.get("list_id")==null?"":flowMap.get("list_id").toString();
        	int repayType = Integer.parseInt(flowMap.get("repay_type").toString());
        	
        	//收入流水   平台还款有多个收入流水
        	flow.setAccountTime(accountTime);
        	flow.setAfterId(afterId);
        	flow.setAmount(amount);
        	flow.setExternalId(externalId);
        	flow.setInOut(inOut);
        	flow.setIssueId(issueId);
        	flow.setMemo(memo);
        	flow.setRemark(remark);
        	flow.setRepayType(repayType);
        	flow.setSegmentationDate(segmentationDate);
        	flow.setSourceAccountIdentifierId(targetAccountIdentifierId);
        	flow.setTargetAccountIdentifierId(sourceAccountIdentifierId);
        	flows.add(flow);
    	}
	}

	private void updatePushLog(int actionId, String confirmLogId, FlowPushLog flowPushLog,CamsMessage camsMessage) {
		String retStr = "";
		switch (actionId) {
		case 7://平台还款
			RepaymentPlatformList repaymentPlatformList = repaymentPlatformListService.selectById(confirmLogId);
			try {
    			int newStatus = repaymentPlatformList.getLastPushStatus();
    			if(newStatus == 1 || newStatus == 4) {
    				LOGGER.error("该流水已推送过"+confirmLogId); 
    				return;
    			}
        		Result<Object> ret = accountListHandlerMsgClient.addMessageFlow(camsMessage);
        		LOGGER.debug(JSON.toJSONString(camsMessage));
        		LOGGER.debug(JSONObject.toJSONString(ret));;
            	retStr = JSON.toJSONString(ret);
            	
            	repaymentPlatformList.setLastPushStatus(1);
            	repaymentPlatformList.setLastPushRemark(retStr);
        		flowPushLog.setPushStatus(1);
//        		break;//跳出循环
    		} catch (Exception e) {
    			repaymentPlatformList.setLastPushStatus(2);
    			repaymentPlatformList.setLastPushRemark(retStr);
        		flowPushLog.setPushStatus(2);
        		
        		retStr = e.getMessage();
    			e.printStackTrace();
    			LOGGER.debug(JSON.toJSONString(camsMessage));
    			LOGGER.debug(JSON.toJSONString(retStr));
			}
			//更新推送状态
			repaymentPlatformList.setLastPushDatetime(new Date());
			repaymentPlatformListService.updateById(repaymentPlatformList);
			
			//记录推送日志
			flowPushLog.setPushEndtime(new Date());
			flowPushLogService.insert(flowPushLog);
			break;
		case 8://垫付
			RepaymentPlatformListGuarantee repaymentPlatformListGuarantee = repaymentPlatformListGuaranteeService.selectById(confirmLogId);
			try {
    			int newStatus = repaymentPlatformListGuarantee.getLastPushStatus();
    			if(newStatus == 1 || newStatus == 4) {
    				LOGGER.error("该流水已推送过"+confirmLogId); 
    				return;
    			}
        		Result<Object> ret = accountListHandlerMsgClient.addMessageFlow(camsMessage);
        		LOGGER.debug(JSON.toJSONString(camsMessage));
        		LOGGER.debug(JSONObject.toJSONString(ret));;
            	retStr = JSON.toJSONString(ret);
            	
            	repaymentPlatformListGuarantee.setLastPushStatus(1);
            	repaymentPlatformListGuarantee.setLastPushRemark(retStr);
        		flowPushLog.setPushStatus(1);
//        		break;//跳出循环
    		} catch (Exception e) {
    			repaymentPlatformListGuarantee.setLastPushStatus(2);
    			repaymentPlatformListGuarantee.setLastPushRemark(retStr);
        		flowPushLog.setPushStatus(2);
        		
        		retStr = e.getMessage();
    			e.printStackTrace();
    			LOGGER.debug(JSON.toJSONString(camsMessage));
    			LOGGER.debug(JSON.toJSONString(retStr));
			}
			//更新推送状态
			repaymentPlatformListGuarantee.setLastPushDatetime(new Date());
			repaymentPlatformListGuaranteeService.updateById(repaymentPlatformListGuarantee);
			
			//记录推送日志
			flowPushLog.setPushEndtime(new Date());
			flowPushLogService.insert(flowPushLog);
			break;
		case 81://还垫付
			RepaymentAdvanceRepayFlow repaymentAdvanceRepayFlow = repaymentAdvanceRepayFlowService.selectById(confirmLogId);
			try {
    			int newStatus = repaymentAdvanceRepayFlow.getLastPushStatus();
    			if(newStatus == 1 || newStatus == 4) {
    				LOGGER.error("该流水已推送过"+confirmLogId); 
    				return;
    			}
        		Result<Object> ret = accountListHandlerMsgClient.addMessageFlow(camsMessage);
        		LOGGER.debug(JSON.toJSONString(camsMessage));
        		LOGGER.debug(JSONObject.toJSONString(ret));;
            	retStr = JSON.toJSONString(ret);
            	
            	repaymentAdvanceRepayFlow.setLastPushStatus(1);
            	repaymentAdvanceRepayFlow.setLastPushRemark(retStr);
        		flowPushLog.setPushStatus(1);
//        		break;//跳出循环
    		} catch (Exception e) {
    			repaymentAdvanceRepayFlow.setLastPushStatus(2);
    			repaymentAdvanceRepayFlow.setLastPushRemark(retStr);
        		flowPushLog.setPushStatus(2);
        		
        		retStr = e.getMessage();
    			e.printStackTrace();
    			LOGGER.debug(JSON.toJSONString(camsMessage));
    			LOGGER.debug(JSON.toJSONString(retStr));
			}
			//更新推送状态
			repaymentAdvanceRepayFlow.setLastPushDatetime(new Date());
			repaymentAdvanceRepayFlowService.updateById(repaymentAdvanceRepayFlow);
			
			//记录推送日志
			flowPushLog.setPushEndtime(new Date());
			flowPushLogService.insert(flowPushLog);
			break;
		case 201://你我金融
			RepaymentBizPlanList repaymentBizPlanList = repaymentBizPlanListService.selectById(confirmLogId);
        		try {
        			int newStatus = repaymentBizPlanList.getLastPushStatus();
        			if(newStatus == 1 || newStatus == 4) {
        				LOGGER.error("该流水已推送过"+confirmLogId); 
        				return;
        			}
            		Result<Object> ret = accountListHandlerMsgClient.addMessageFlow(camsMessage);
            		LOGGER.debug(JSON.toJSONString(camsMessage));
            		LOGGER.debug(JSONObject.toJSONString(ret));;
                	retStr = JSON.toJSONString(ret);
                	
                	repaymentBizPlanList.setLastPushStatus(1);
                	repaymentBizPlanList.setLastPushRemark(retStr);
            		flowPushLog.setPushStatus(1);
//            		break;//跳出循环
        		} catch (Exception e) {
        			repaymentBizPlanList.setLastPushStatus(2);
        			repaymentBizPlanList.setLastPushRemark(retStr);
            		flowPushLog.setPushStatus(2);
            		
            		retStr = e.getMessage();
        			e.printStackTrace();
        			LOGGER.debug(JSON.toJSONString(camsMessage));
        			LOGGER.debug(JSON.toJSONString(retStr));
				}
			//更新推送状态
			repaymentBizPlanList.setLastPushDatetime(new Date());
			repaymentBizPlanListService.updateById(repaymentBizPlanList);
			
			//记录推送日志
			flowPushLog.setPushEndtime(new Date());
			flowPushLogService.insert(flowPushLog);
			break;	
		case 10://你我金融
			AgencyRechargeLog agencyRechargeLog = agencyRechargeLogService.selectById(confirmLogId);
        		try {
        			int newStatus = agencyRechargeLog.getLastPushStatus();
        			if(newStatus == 1 || newStatus == 4) {
        				LOGGER.error("该流水已推送过"+confirmLogId); 
        				return;
        			}
            		Result<Object> ret = accountListHandlerMsgClient.addMessageFlow(camsMessage);
            		LOGGER.debug(JSON.toJSONString(camsMessage));
            		LOGGER.debug(JSONObject.toJSONString(ret));;
                	retStr = JSON.toJSONString(ret);
                	
                	agencyRechargeLog.setLastPushStatus(1);
                	agencyRechargeLog.setLastPushRemark(retStr);
            		flowPushLog.setPushStatus(1);
//            		break;//跳出循环
        		} catch (Exception e) {
        			agencyRechargeLog.setLastPushStatus(2);
        			agencyRechargeLog.setLastPushRemark(retStr);
            		flowPushLog.setPushStatus(2);
            		
            		retStr = e.getMessage();
        			e.printStackTrace();
        			LOGGER.debug(JSON.toJSONString(camsMessage));
        			LOGGER.debug(JSON.toJSONString(retStr));
				}
			//更新推送状态
        	agencyRechargeLog.setLastPushDatetime(new Date());
        	agencyRechargeLogService.updateById(agencyRechargeLog);
			
			//记录推送日志
			flowPushLog.setPushEndtime(new Date());
			flowPushLogService.insert(flowPushLog);
			break;
		default:
			break;
		}
	}

	/**
	 * @Title: getPushFlowList  
	 * @Description: 获取本次要推送的所有流水
	 * @param actionId
	 * @param paramBusinessMap
	 * @return List<Map<String,Object>>    返回类型  
	 * @throws  
	 */
	private List<Map<String, Object>> getPushFlowList(int actionId, Map<String, Object> paramBusinessMap) {
		List<Map<String, Object>> pushFlowList = new ArrayList<>();
		switch (actionId) {
		case 7://平台还款
			pushFlowList = repaymentPlatformListService.selectPushPlatformRepayFlowList(paramBusinessMap);
			//平台还款 没有标的信息 从资金分发补充公司 业务 客户信息
			for (Map<String, Object> pushFlowMap : pushFlowList) {
				String projectId = null == pushFlowMap.get("project_id")?null:pushFlowMap.get("project_id").toString();
				if(null == projectId) {
					continue;
				}
				TdrepayRechargeLog tdrepayRechargeLog = tdrepayRechargeLogService.selectOne(new EntityWrapper<TdrepayRechargeLog>().eq("project_id", projectId).eq("process_status", 2));
				if(null == tdrepayRechargeLog) {
					continue;
				}
				pushFlowMap.put("business_id", tdrepayRechargeLog.getOrigBusinessId());
				pushFlowMap.put("business_type", tdrepayRechargeLog.getBusinessType());
				pushFlowMap.put("business_type_name", BusinessTypeEnum.getName(tdrepayRechargeLog.getBusinessType()));
				pushFlowMap.put("customer_name", tdrepayRechargeLog.getCustomerName());
				pushFlowMap.put("company_name", tdrepayRechargeLog.getCompanyName());
				pushFlowMap.put("td_user_id", tdrepayRechargeLog.getTdUserId());
			}
			break;
		case 8://垫付
			pushFlowList = repaymentPlatformListService.selectPushAdvancePayFlowList(paramBusinessMap);
			//平台还款 没有标的信息 从资金分发补充公司 业务 客户信息
			for (Map<String, Object> pushFlowMap : pushFlowList) {
				String projectId = null == pushFlowMap.get("project_id")?null:pushFlowMap.get("project_id").toString();
				if(null == projectId) {
					continue;
				}
				TdrepayRechargeLog tdrepayRechargeLog = tdrepayRechargeLogService.selectOne(new EntityWrapper<TdrepayRechargeLog>().eq("project_id", projectId).eq("process_status", 2));
				if(null == tdrepayRechargeLog) {
					continue;
				}
				pushFlowMap.put("business_id", tdrepayRechargeLog.getOrigBusinessId());
				pushFlowMap.put("business_type", tdrepayRechargeLog.getBusinessType());
				pushFlowMap.put("business_type_name", BusinessTypeEnum.getName(tdrepayRechargeLog.getBusinessType()));
				pushFlowMap.put("customer_name", tdrepayRechargeLog.getCustomerName());
				pushFlowMap.put("company_name", tdrepayRechargeLog.getCompanyName());
				pushFlowMap.put("td_user_id", tdrepayRechargeLog.getTdUserId());
			}
			break;
		case 81://还垫付
			//平台还款 没有标的信息 从资金分发补充公司 业务 客户信息
			pushFlowList = repaymentPlatformListService.selectPushAdvanceRepayFlowList(paramBusinessMap);
			for (Map<String, Object> pushFlowMap : pushFlowList) {
				String projectId = null == pushFlowMap.get("project_id")?null:pushFlowMap.get("project_id").toString();
				if(null == projectId) {
					continue;
				}
				TdrepayRechargeLog tdrepayRechargeLog = tdrepayRechargeLogService.selectOne(new EntityWrapper<TdrepayRechargeLog>().eq("project_id", projectId).eq("process_status", 2));
				if(null == tdrepayRechargeLog) {
					continue;
				}
				pushFlowMap.put("business_id", tdrepayRechargeLog.getOrigBusinessId());
				pushFlowMap.put("business_type", tdrepayRechargeLog.getBusinessType());
				pushFlowMap.put("business_type_name", BusinessTypeEnum.getName(tdrepayRechargeLog.getBusinessType()));
				pushFlowMap.put("customer_name", tdrepayRechargeLog.getCustomerName());
				pushFlowMap.put("company_name", tdrepayRechargeLog.getCompanyName());
				pushFlowMap.put("td_user_id", tdrepayRechargeLog.getTdUserId());
			}
			break;
		case 201://你我金融流水
			pushFlowList = repaymentPlatformListService.selectPushNiWoRepayFlowList(paramBusinessMap);
			break;
		case 10://充值流水
			pushFlowList = repaymentPlatformListService.selectPushRechargeFlowList(paramBusinessMap);
			break;
		default:
			break;
		}
		return pushFlowList;
	}
	
	/**
	 * @Title: getFlowInfoById  
	 * @Description: 获取流水详情  一次推送的流水
	 * @param actionId 交易活动编号
	 * @param paramOnlineFlowMap 流水参数
	 * @return List<Map<String,Object>>    返回类型  
	 * @throws  
	 */
	private List<Map<String, Object>> getFlowInfoById(int actionId, Map<String, Object> paramFlowMap,Business business) {
		List<Map<String, Object>> flowList = new ArrayList<>();
		switch (actionId) {
		case 7://平台还款
			flowList = repaymentPlatformListService.selectPushPlatformRepayFlow(paramFlowMap);
			break;
		case 8://垫付
			flowList = repaymentPlatformListService.selectPushAdvancePayFlow(paramFlowMap);
			if(flowList != null && !flowList.isEmpty()) {
				//处理流水中的账户信息
				for (Map<String, Object> flowMap : flowList) {
				String projectId = null == flowMap.get("issue_id")?null:flowMap.get("issue_id").toString();
				if(null == projectId) {
					continue;
				}
				TdrepayRechargeLog tdrepayRechargeLog = tdrepayRechargeLogService.selectOne(new EntityWrapper<TdrepayRechargeLog>().eq("project_id", projectId).eq("process_status", 2));
				if(null == tdrepayRechargeLog) {
					continue;
				}
				flowMap.put("account_name", danbao);
				flowMap.put("bank_card_no", dMainIdDanbao);
				flowMap.put("account_type", 1);
				flowMap.put("main_id", mainIdDanbao);
				
				flowMap.put("target_account_id", tdrepayRechargeLog.getCustomerName());
				flowMap.put("target_bank_card_no", tdrepayRechargeLog.getTdUserId());
				flowMap.put("target_account_type", 1);
				flowMap.put("target_main_id", tdrepayRechargeLog.getTdUserId());
				}
			}
			break;
		case 81://还垫付
			flowList = repaymentPlatformListService.selectPushAdvanceRepayFlow(paramFlowMap);
			break;
		case 201://你我金融流水
			flowList = repaymentPlatformListService.selectPushNiWoRepayFlow(paramFlowMap);
			if(flowList.size()>1) {
				return flowList.subList(0, 1);
			}
			break;
		case 10://充值流水
			flowList = repaymentPlatformListService.selectPushRechargeFlow(paramFlowMap);
			break;
		case 71://平台还款 拆分流水
			List<Map<String, Object>> flowListOld = repaymentPlatformListService.selectPushPlatformRepayFlow(paramFlowMap);
			if(flowListOld != null && !flowListOld.isEmpty()) {
			//流水转换 一个变为4个
			String confirmLogId = paramFlowMap.get("confirmLogId").toString();
			RepaymentPlatformList repaymentPlatformList = repaymentPlatformListService.selectById(confirmLogId);
			Map<String, Object> flowMap = flowListOld.get(0);
			 //实还本息
			 BigDecimal principalAndInterest = repaymentPlatformList.getPrincipalAndinterest();
			 //实还平台服务费
			 BigDecimal tuandaiAmount = repaymentPlatformList.getTuandaiAmount();
			 //实还资产端服务费
			 BigDecimal orgAmount = repaymentPlatformList.getOrgAmount();
			 //实还担保公司服务费
			 BigDecimal guaranteeAmount = repaymentPlatformList.getGuaranteeAmount();
			 //滞纳金
			 BigDecimal penaltyAmount = repaymentPlatformList.getPenaltyAmount();
			//重新组装流水 
			//投资人流水
			 if(principalAndInterest != null && principalAndInterest.compareTo(new BigDecimal(0)) > 0) {
				 Map<String, Object> flowMapTouZi = new HashMap<>();
				 flowMapTouZi.putAll(flowMap);
				 flowMapTouZi.put("amount", principalAndInterest);
				 flowMapTouZi.put("in_out", 1);
				 flowMapTouZi.put("target_main_id", mainIdTouZi);
				 flowMapTouZi.put("target_bank_card_no", dMainIdTouZi);
				 flowMapTouZi.put("target_main_type", 1);
				 flowMapTouZi.put("target_account_name", "投资人");
				 flowMapTouZi.put("target_account_type", "10");
				 flowList.add(flowMapTouZi);
			 }
			//分公司流水
			 if(orgAmount != null && orgAmount.compareTo(new BigDecimal(0)) > 0) {
				 Map<String, Object> flowMapCompany = new HashMap<>();
//				 BasicBusiness basicBusiness = basicBusinessService.selectById(business.getBusinessId());
				 flowMapCompany.putAll(flowMap);
				 flowMapCompany.put("amount", orgAmount);
				 flowMapCompany.put("target_main_id", null);
				 flowMapCompany.put("in_out", 1);
				 flowMapCompany.put("target_main_type", 1);
				 flowMapCompany.put("target_account_type", "1");
				 if(null != business.getBranchName()) {
					 flowMapCompany.put("target_account_name", "广东鸿特信息咨询有限公司"+business.getBranchName());
					 flowMapCompany.put("target_bank_card_no", DigestUtils.md5Hex(business.getBranchName()));
				 }
				 flowMapCompany.put("target_main_id", null);
				 flowList.add(flowMapCompany);
			 }
			//平台费
			 if(tuandaiAmount != null && tuandaiAmount.compareTo(new BigDecimal(0)) > 0) {
				 Map<String, Object> flowMapPlatform = new HashMap<>();
				 flowMapPlatform.putAll(flowMap);
				 flowMapPlatform.put("amount", tuandaiAmount);
				 flowMapPlatform.put("target_main_id", null);
				 flowMapPlatform.put("target_main_type", 1);
				 flowMapPlatform.put("in_out", 1);
				 flowMapPlatform.put("target_main_id", mainIdPlatfrom);
				 flowMapPlatform.put("target_bank_card_no", dMainIdPlatfrom);
				 flowMapPlatform.put("target_account_name", "东莞团贷网互联网科技服务有限公司");
				 flowMapPlatform.put("target_account_type", "6");
				 flowList.add(flowMapPlatform);
			 }
			//担保公司
			 if(guaranteeAmount == null) {
				 guaranteeAmount = new BigDecimal(0);
			 }
			 if(penaltyAmount == null ) {
				 penaltyAmount = new BigDecimal(0);
			 }
			 if(guaranteeAmount.compareTo(new BigDecimal(0)) > 0 ||
					 penaltyAmount.compareTo(new BigDecimal(0)) > 0) {
				 Map<String, Object> flowMapDanbao = new HashMap<>();
				 flowMapDanbao.putAll(flowMap);
				 flowMapDanbao.put("amount", guaranteeAmount.add(penaltyAmount));
				 flowMapDanbao.put("target_main_id", "45BC0637-412F-45AF-A44A-14348BEB400C");
				 flowMapDanbao.put("target_bank_card_no", "45BC0637-412F-45AF-A44A-14348BEB400C");
				 flowMapDanbao.put("target_main_type", 1);
				 flowMapDanbao.put("in_out", 1);
				 flowMapDanbao.put("target_account_name", "深圳市天大联合融资担保有限公司");
				 flowMapDanbao.put("target_account_type", "3");
				 flowList.add(flowMapDanbao);
			  }
			}
			break;
		default:
			break;
		}
		return flowList;
	}

	
	/**
	 * @Title: getFlowItemInfoById  
	 * @Description: 获取流水费用项
	 * @param actionId 交易活动
 	 * @param paramOnlineFlowMap 线上列表
	 * @return List<Map<String,Object>>    返回类型  
	 * @throws  
	 */
	private List<Map<String, Object>> getFlowItemInfoById(int actionId, Map<String, Object> paramFlowItemMap) {
		List<Map<String, Object>> flowItemList = new ArrayList<>();
		switch (actionId) {
		case 7://平台还款
			//flowItemList = repaymentPlatformListService.selectPushPlatformRepayFlowItem(paramFlowItemMap);
			String confirmLogId = paramFlowItemMap.get("repaySourceId").toString();
			RepaymentPlatformList repaymentPlatformList = repaymentPlatformListService.selectById(confirmLogId);
			 //实还本息
			 BigDecimal principalAndInterest = repaymentPlatformList.getPrincipalAndinterest();
			 addPlatformRepayFlowItem(flowItemList, "实还本息", principalAndInterest);
			 //实还平台服务费
			 BigDecimal tuandaiAmount = repaymentPlatformList.getTuandaiAmount();
			 addPlatformRepayFlowItem(flowItemList, "实还平台服务费", tuandaiAmount);
			 //实还资产端服务费
			 BigDecimal orgAmount = repaymentPlatformList.getOrgAmount();
			 addPlatformRepayFlowItem(flowItemList, "实还资产端服务费", orgAmount);
			 //实还担保公司服务费
			 BigDecimal guaranteeAmount = repaymentPlatformList.getGuaranteeAmount();
			 addPlatformRepayFlowItem(flowItemList, "实还担保公司服务费", guaranteeAmount);
			 //实还仲裁服务费
			 BigDecimal arbitrationAmount = repaymentPlatformList.getArbitrationAmount();
			 addPlatformRepayFlowItem(flowItemList, "实还仲裁服务费", arbitrationAmount);
			 //实还中介服务费
			 BigDecimal agencyAmount = repaymentPlatformList.getAgencyAmount();
			 addPlatformRepayFlowItem(flowItemList, "实还中介服务费", agencyAmount);
			 //保险费
			 BigDecimal insuranceAmount = repaymentPlatformList.getInsuranceAmount();
			 addPlatformRepayFlowItem(flowItemList, "保险费", insuranceAmount);
			 //特权包
			 BigDecimal privilegePackageAmount = repaymentPlatformList.getPrivilegePackageAmount();
			 addPlatformRepayFlowItem(flowItemList, "特权包", privilegePackageAmount);
			 //滞纳金
			 BigDecimal penaltyAmount = repaymentPlatformList.getPenaltyAmount();
			 addPlatformRepayFlowItem(flowItemList, "滞纳金", penaltyAmount);
			break;
		case 8://垫付
			//flowItemList = repaymentPlatformListService.selectPushAdvancePayFlowItem(paramFlowItemMap);
			 String guaranteeLogId = paramFlowItemMap.get("repaySourceId").toString();
			 RepaymentPlatformListGuarantee repaymentPlatformListGuarantee = repaymentPlatformListGuaranteeService.selectById(guaranteeLogId);
			 //实还本息
			 BigDecimal principalAndInterestGuarantee = repaymentPlatformListGuarantee.getPrincipalAndinterest();
			 addPlatformRepayFlowItem(flowItemList, "本金利息", principalAndInterestGuarantee);
			 //实还平台服务费
			 BigDecimal tuandaiAmountGuarantee = repaymentPlatformListGuarantee.getTuandaiAmount();
			 addPlatformRepayFlowItem(flowItemList, "实还平台服务费", tuandaiAmountGuarantee);
			 //实还资产端服务费
			 BigDecimal orgAmountGuarantee = repaymentPlatformListGuarantee.getOrgAmount();
			 addPlatformRepayFlowItem(flowItemList, "实还资产端服务费", orgAmountGuarantee);
			 //实还担保公司服务费
			 BigDecimal guaranteeAmountGuarantee = repaymentPlatformListGuarantee.getGuaranteeAmount();
			 addPlatformRepayFlowItem(flowItemList, "实还担保公司服务费", guaranteeAmountGuarantee);
			 //实还仲裁服务费
			 BigDecimal arbitrationAmountGuarantee = repaymentPlatformListGuarantee.getArbitrationAmount();
			 addPlatformRepayFlowItem(flowItemList, "实还仲裁服务费", arbitrationAmountGuarantee);
			 //实还中介服务费
			 BigDecimal agencyAmountGuarantee = repaymentPlatformListGuarantee.getAgencyAmount();
			 addPlatformRepayFlowItem(flowItemList, "实还中介服务费", agencyAmountGuarantee);
			 //滞纳金
			 BigDecimal penaltyAmountGuarantee = repaymentPlatformListGuarantee.getPenaltyAmount();
			 addPlatformRepayFlowItem(flowItemList, "滞纳金", penaltyAmountGuarantee);			
			break;
		case 81://还垫付
			//flowItemList = repaymentPlatformListService.selectPushAdvanceRepayFlowItem(paramFlowItemMap);
			 String advanceRepayLogId = paramFlowItemMap.get("repaySourceId").toString();
			 RepaymentAdvanceRepayFlow repaymentAdvanceRepayFlow = repaymentAdvanceRepayFlowService.selectById(advanceRepayLogId);
			 //实还本息
			 BigDecimal principalAndInterestAdvanceRepay = repaymentAdvanceRepayFlow.getPrincipalAndinterest();
			 addPlatformRepayFlowItem(flowItemList, "本金利息", principalAndInterestAdvanceRepay);
			 //实还平台服务费
			 BigDecimal tuandaiAmountAdvanceRepay = repaymentAdvanceRepayFlow.getTuandaiAmount();
			 addPlatformRepayFlowItem(flowItemList, "平台服务费", tuandaiAmountAdvanceRepay);
			 //实还资产端服务费
			 BigDecimal orgAmountAdvanceRepay = repaymentAdvanceRepayFlow.getOrgAmount();
			 addPlatformRepayFlowItem(flowItemList, "资产端服务费", orgAmountAdvanceRepay);
			 //实还担保公司服务费
			 BigDecimal guaranteeAmountAdvanceRepay = repaymentAdvanceRepayFlow.getGuaranteeAmount();
			 addPlatformRepayFlowItem(flowItemList, "担保公司服务费", guaranteeAmountAdvanceRepay);
			 //实还仲裁服务费
			 BigDecimal arbitrationAmountAdvanceRepay = repaymentAdvanceRepayFlow.getArbitrationAmount();
			 addPlatformRepayFlowItem(flowItemList, "仲裁服务费", arbitrationAmountAdvanceRepay);
			 //滞纳金
			 BigDecimal overdueAmountAdvanceRepay = repaymentAdvanceRepayFlow.getOverdueAmount();
			 addPlatformRepayFlowItem(flowItemList, "逾期费用（罚息）", overdueAmountAdvanceRepay);	
			break;
		case 201://你我金融
			String niWoConfirmLogId = paramFlowItemMap.get("repaySourceId").toString();
			List<RepaymentBizPlanListDetail> repaymentBizPlanListDetailList = repaymentBizPlanListDetailService.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().eq("plan_list_id", niWoConfirmLogId));
			for (RepaymentBizPlanListDetail repaymentBizPlanListDetail : repaymentBizPlanListDetailList) {
				String freeId = repaymentBizPlanListDetail.getFeeId();
				String freeName = repaymentBizPlanListDetail.getPlanItemName();
				BigDecimal factAmount = repaymentBizPlanListDetail.getFactAmount();
				if(null != factAmount && factAmount.compareTo(new BigDecimal(0)) != 0) {
					 if("滞纳金".equals(freeName) && !StringUtils.isBlank(freeId)) {
	            			String detailFeeName1 = RepayPlanFeeTypeEnum.feeIdOf(freeId).getDesc();
	            			if(!StringUtils.isBlank(detailFeeName1)) {
	            				freeName = detailFeeName1;
	            			}
	            		}
					 Map<String, Object> map = new HashMap<>();
					 map.put("amount", factAmount);
					 map.put("fee_id", freeId);
					 map.put("fee_name", freeName);
					 flowItemList.add(map);
				 }
			}
			break;
		case 10://充值 没有明细
			break;
		default:
			break;
		}
		return flowItemList;
	}

	private void addPlatformRepayFlowItem(List<Map<String, Object>> flowItemList, String freeName,BigDecimal principalAndInterest) {
		if(null != principalAndInterest && principalAndInterest.compareTo(new BigDecimal(0)) != 0) {
			 Map<String, Object> map = new HashMap<>();
			 map.put("amount", principalAndInterest);
			 map.put("fee_id", freeName);
			 map.put("fee_name", freeName);
			 flowItemList.add(map);
		 }
	}

	@Override
	public Result pullAdvanceRepayInfo(String projectId) {
		Result advanceShareProfitResult = null;
	    
	    Map<String, Object> paramMap = new HashMap<>();
		paramMap.put("projectId", projectId);
		DistributeFundRecordVO distributeFundRecordVO = new DistributeFundRecordVO();
	    try {
		  LOGGER.info("还垫付信息查询接口/eip/td/repayment/returnAdvanceShareProfit参数信息，{}", paramMap);
		  advanceShareProfitResult = eipRemote.returnAdvanceShareProfit(paramMap); // 还垫付信息
		  LOGGER.info("还垫付信息查询接口/eip/td/repayment/returnAdvanceShareProfit返回信息，{}", advanceShareProfitResult);
	      String returnCode = advanceShareProfitResult.getReturnCode();
	      if("0000".equals(returnCode)) {
	    	 Map<String,Object> retData = (Map<String, Object>) advanceShareProfitResult.getData();
	    	 String message = retData.get("message").toString();
	    	 //标的ID
	    	 String projectId1 = projectId;//retData.get("projectId").toString();
	    	 int status = Integer.parseInt(retData.get("status").toString());
	    	 //projectPayment还款信息list
	    	 List<Map<String,Object>> returnAdvanceShareProfits = (List<Map<String, Object>>) retData.get("returnAdvanceShareProfits");
	    	 if(null != returnAdvanceShareProfits) {
	    		 for (Map<String, Object> map : returnAdvanceShareProfits) {
	    			 RepaymentAdvanceRepayFlow repaymentAdvanceRepayFlow = new RepaymentAdvanceRepayFlow();
	    			 //期数
	    			 int period = Integer.parseInt(map.get("period").toString());
	    			 //还款日期 格式：yyyy-MM-dd HH:mm:ss
	    			 String refundDate =  map.get("refundDate").toString();
	    			 //还款状态1 已结清 0 未结清
	    			 int repayStatus = Integer.parseInt(map.get("status").toString());
	    			 //还款总金额
	    			 BigDecimal totalAmount = null == map.get("totalAmount")?null:new BigDecimal(map.get("totalAmount").toString());
	    			 //borrowerPayment借款人还款信息
	    			 //实还本息
	    			 BigDecimal principalAndInterest = null == map.get("principalAndInterest")?null:new BigDecimal(map.get("principalAndInterest").toString());
	    			 //实还平台服务费
	    			 BigDecimal tuandaiAmount = null == map.get("tuandaiAmount")?null:new BigDecimal(map.get("tuandaiAmount").toString());
	    			 //实还资产端服务费
	    			 BigDecimal orgAmount = null == map.get("orgAmount")?null:new BigDecimal(map.get("orgAmount").toString());
	    			 //实还担保公司服务费
	    			 BigDecimal guaranteeAmount = null == map.get("guaranteeAmount")?null:new BigDecimal(map.get("guaranteeAmount").toString());
	    			 //实还仲裁服务费
	    			 BigDecimal arbitrationAmount = null == map.get("arbitrationAmount")?null:new BigDecimal(map.get("arbitrationAmount").toString());
	    			 //逾期费用（罚息）
	    			 BigDecimal overDueAmount = null == map.get("overDueAmount")?null:new BigDecimal(map.get("overDueAmount").toString());
	    			 repaymentAdvanceRepayFlow.setProjectId(projectId1);
	    			 repaymentAdvanceRepayFlow.setPeriod(period);
	    			 repaymentAdvanceRepayFlow.setRepayStatus(repayStatus);
	    			 repaymentAdvanceRepayFlow.setRefundDate(refundDate);
	    			 repaymentAdvanceRepayFlow.setTotalAmount(totalAmount);
	    			 repaymentAdvanceRepayFlow.setPrincipalAndinterest(principalAndInterest);
	    			 repaymentAdvanceRepayFlow.setTuandaiAmount(tuandaiAmount);
	    			 repaymentAdvanceRepayFlow.setOrgAmount(orgAmount);
	    			 repaymentAdvanceRepayFlow.setGuaranteeAmount(guaranteeAmount);
	    			 repaymentAdvanceRepayFlow.setArbitrationAmount(arbitrationAmount);
	    			 repaymentAdvanceRepayFlow.setOverdueAmount(overDueAmount);
	    			 //通过标号和期号取平台还款记录 ,如果有比较总金额 相同则不改 不同则修改 没有记录则插入
	    			 RepaymentAdvanceRepayFlow repaymentAdvanceRepayFlowOld = repaymentAdvanceRepayFlowService.selectOne(new EntityWrapper<RepaymentAdvanceRepayFlow>().eq("project_id", projectId1).eq("period", period));
	    			 int updateType = 0;
	    			 if(repaymentAdvanceRepayFlowOld == null) {
	    				 updateType = 1;
	    				 repaymentAdvanceRepayFlow.setCreateMan("平台接口获取");
	    				 repaymentAdvanceRepayFlow.setCreateTime(new Date());
	    				 repaymentAdvanceRepayFlowService.insert(repaymentAdvanceRepayFlow);
	    			 }
	    		 }
	    	 }
	      }
	    } catch (Exception e) {
	      LOGGER.error(e.getMessage(), e);
	    }
		return advanceShareProfitResult;
	}

	@Override
	public void pushPlatformRepayFlowToCams(String projectId) {
    	Map<String,Object> paramBusinessMap = new HashMap<>();
    	if(!StringUtils.isBlank(projectId)) {
    		paramBusinessMap.put("projectId", projectId);
    	}
    	List<Map<String,Object>> listMap = getPushFlowList(7,paramBusinessMap);
    	addBusinessCamsFlow(listMap,7);
	}

	@Override
	public void pushAdvancePayFlowToCams(String projectId) {
    	Map<String,Object> paramBusinessMap = new HashMap<>();
    	if(!StringUtils.isBlank(projectId)) {
    		paramBusinessMap.put("projectId", projectId);
    	}
    	List<Map<String,Object>> listMap = getPushFlowList(8,paramBusinessMap);
    	addBusinessCamsFlow(listMap,8);
	}

	@Override
	public void pushAdvanceRepayFlowToCams(String projectId) {
    	Map<String,Object> paramBusinessMap = new HashMap<>();
    	if(!StringUtils.isBlank(projectId)) {
    		paramBusinessMap.put("projectId", projectId);
    	}
    	List<Map<String,Object>> listMap = getPushFlowList(81,paramBusinessMap);
    	addBusinessCamsFlow(listMap,81);
	}

	@Override
	public List<Map<String, Object>> selectPushProjectList() {
		Map<String, Object> mapParam = new HashMap<>();
		return flowPushLogMapper.selectPushProjectList(mapParam);
	}

	@Override
	public void pushNiWoRepayFlowToCams(String planListId) {
    	Map<String,Object> paramBusinessMap = new HashMap<>();
    	if(!StringUtils.isBlank(planListId)) {
    		paramBusinessMap.put("planListId", planListId);
    	}
    	List<Map<String,Object>> listMap = getPushFlowList(201,paramBusinessMap);
    	addBusinessCamsFlow(listMap,201);		
	}

	@Override
	public void pushRechargeFlowToCams(String logId) {
    	Map<String,Object> paramBusinessMap = new HashMap<>();
    	if(!StringUtils.isBlank(logId)) {
    		paramBusinessMap.put("logId", logId);
    	}
    	List<Map<String,Object>> listMap = getPushFlowList(10,paramBusinessMap);
    	addBusinessCamsFlow(listMap,10);
	}

}
