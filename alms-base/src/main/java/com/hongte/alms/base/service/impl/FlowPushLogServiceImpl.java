package com.hongte.alms.base.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.FlowPushLog;
import com.hongte.alms.base.entity.RepaymentPlatformList;
import com.hongte.alms.base.entity.RepaymentPlatformListBorrower;
import com.hongte.alms.base.entity.RepaymentPlatformListGuarantee;
import com.hongte.alms.base.entity.TdrepayRechargeLog;
import com.hongte.alms.base.enums.repayPlan.RepayPlanFeeTypeEnum;
import com.hongte.alms.base.feignClient.AccountListHandlerMsgClient;
import com.hongte.alms.base.feignClient.EipRemote;
import com.hongte.alms.base.mapper.FlowPushLogMapper;
import com.hongte.alms.base.service.FlowPushLogService;
import com.hongte.alms.base.service.RepaymentPlatformListBorrowerService;
import com.hongte.alms.base.service.RepaymentPlatformListGuaranteeService;
import com.hongte.alms.base.service.RepaymentPlatformListService;
import com.hongte.alms.base.service.SysParameterService;
import com.hongte.alms.base.util.UUIDHtGenerator;
import com.hongte.alms.base.vo.cams.CamsMessage;
import com.hongte.alms.base.vo.cams.CreateBatchFlowCommand;
import com.hongte.alms.base.vo.cams.CreateBatchFlowCommand.Business;
import com.hongte.alms.base.vo.cams.CreateBatchFlowCommand.Flow;
import com.hongte.alms.base.vo.cams.CreateBatchFlowCommand.FlowAccountIdentifier;
import com.hongte.alms.base.vo.cams.CreateBatchFlowCommand.FlowDetail;
import com.hongte.alms.base.vo.compliance.DistributeFundRecordVO;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.ht.ussp.core.Result;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.apache.xmlbeans.impl.piccolo.xml.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

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
	@Qualifier("RepaymentPlatformListBorrowerService")
	RepaymentPlatformListBorrowerService repaymentPlatformListBorrowerService;
	
	@Autowired
	@Qualifier("RepaymentPlatformListGuaranteeService")
	RepaymentPlatformListGuaranteeService repaymentPlatformListGuaranteeService;
	
    @Autowired
    @Qualifier("FlowPushLogService")
    FlowPushLogService flowPushLogService;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FlowPushLogServiceImpl.class);
	
	@Override
	public List<DistributeFundRecordVO> queryDistributeFundRecord(String projectId) {
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
	    	 String projectId1 = retData.get("projectId").toString();
	    	 int status = Integer.parseInt(retData.get("status").toString());
	    	 //projectPayment还款信息list
	    	 List<Map<String,Object>> projectPayments = (List<Map<String, Object>>) retData.get("projectPayments");
	    	 if(null != projectPayments) {
	    		 for (Map<String, Object> map : projectPayments) {
	    			 RepaymentPlatformList repaymentPlatformList = new RepaymentPlatformList();
	    			 //期数
	    			 int period = Integer.parseInt(map.get("period").toString());
	    			 //还款日期 yyyy-MM-dd
	    			 String addDate =  map.get("addDate").toString();
	    			 //还款状态1 已结清 0逾期
	    			 int projectPaymentsStatus = Integer.parseInt(map.get("status").toString());
	    			 //实还总金额
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
	    			 repaymentPlatformList.setRepayStatus(status);
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
	    			 //通过标号和期号取平台还款记录 ,如果有比较总金额 相同则不改 不同则修改 没有记录则插入
	    			 RepaymentPlatformList repaymentPlatformListOld = repaymentPlatformListService.selectOne(new EntityWrapper<RepaymentPlatformList>().eq("project_id", projectId1).eq("period", period));
	    			 int updateType = 0;
	    			 if(repaymentPlatformListOld == null) {
	    				 updateType = 1;
	    				 repaymentPlatformList.setCreateMan("平台接口获取");
		    			 repaymentPlatformList.setCreateTime(new Date());
	    				 repaymentPlatformListService.insert(repaymentPlatformList);
	    			 }
	    			 
	    			 //borrowerPayment借款人还款信息list，字段描述
	    			 Map<String,Object> borrowerPaymentMap = (Map<String, Object>) map.get("borrowerPayment");
	    			 if(null != borrowerPaymentMap && 0 != updateType) {
	    				 //实还本息
		    			 BigDecimal principalAndInterestBorrower = null == borrowerPaymentMap.get("principalAndInterest")?null:new BigDecimal(borrowerPaymentMap.get("principalAndInterest").toString());
		    			 //实还平台服务费
		    			 BigDecimal tuandaiAmountBorrower = null == borrowerPaymentMap.get("tuandaiAmount")?null:new BigDecimal(borrowerPaymentMap.get("tuandaiAmount").toString());
		    			 //实还资产端服务费
		    			 BigDecimal orgAmountBorrower = null == borrowerPaymentMap.get("orgAmount")?null:new BigDecimal(borrowerPaymentMap.get("orgAmount").toString());
		    			 //实还担保公司服务费
		    			 BigDecimal guaranteeAmountBorrower = null == borrowerPaymentMap.get("guaranteeAmount")?null:new BigDecimal(borrowerPaymentMap.get("guaranteeAmount").toString());
		    			 //实还仲裁服务费
		    			 BigDecimal arbitrationAmountBorrower = null == borrowerPaymentMap.get("arbitrationAmount")?null:new BigDecimal(borrowerPaymentMap.get("arbitrationAmount").toString());
		    			 //实还中介服务费
		    			 BigDecimal agencyAmountBorrower = null == borrowerPaymentMap.get("agencyAmount")?null:new BigDecimal(borrowerPaymentMap.get("agencyAmount").toString());
		    			 //保险费
		    			 BigDecimal insuranceAmountBorrower = null == borrowerPaymentMap.get("insuranceAmount")?null:new BigDecimal(borrowerPaymentMap.get("insuranceAmount").toString());
		    			 //特权包
		    			 BigDecimal privilegePackageAmountBorrower = null == borrowerPaymentMap.get("privilegePackageAmount")?null:new BigDecimal(borrowerPaymentMap.get("privilegePackageAmount").toString());
		    			 RepaymentPlatformListBorrower repaymentPlatformListBorrower = new RepaymentPlatformListBorrower();
		    			 repaymentPlatformListBorrower.setProjectId(projectId1);
		    			 repaymentPlatformListBorrower.setPeriod(period);
		    			 repaymentPlatformListBorrower.setRepayId(repaymentPlatformList.getId());
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
		    			 repaymentPlatformListBorrowerService.insert(repaymentPlatformListBorrower);
	    			 }
	    			 
	    			//guaranteePayment担保公司垫付信息，字段描述
	    			 Map<String,Object> guaranteePaymentMap = (Map<String, Object>) map.get("guaranteePayment");
	    			 if(null != guaranteePaymentMap && 0 != updateType) {
	    				 //本金利息
	    				 BigDecimal principalAndInterestGuarantee = null == guaranteePaymentMap.get("principalAndInterest")?null:new BigDecimal(guaranteePaymentMap.get("principalAndInterest").toString());
	    				 //滞纳金
	    				 BigDecimal penaltyAmountGuarantee = null == guaranteePaymentMap.get("penaltyAmount")?null:new BigDecimal(guaranteePaymentMap.get("penaltyAmount").toString());
	    				 //实还平台服务费
	    				 BigDecimal tuandaiAmountGuarantee = null == guaranteePaymentMap.get("tuandaiAmount")?null:new BigDecimal(guaranteePaymentMap.get("tuandaiAmount").toString());
	    				 //实还资产端服务费
	    				 BigDecimal orgAmountGuarantee = null == guaranteePaymentMap.get("orgAmount")?null:new BigDecimal(guaranteePaymentMap.get("orgAmount").toString());
	    				 //实还担保公司服务费
	    				 BigDecimal guaranteeAmountGuarantee = null == guaranteePaymentMap.get("guaranteeAmount")?null:new BigDecimal(guaranteePaymentMap.get("guaranteeAmount").toString());
	    				 //实还仲裁服务费
	    				 BigDecimal arbitrationAmountGuarantee = null == guaranteePaymentMap.get("arbitrationAmount")?null:new BigDecimal(guaranteePaymentMap.get("arbitrationAmount").toString());
	    				 //实还中介服务费
	    				 BigDecimal agencyAmountGuarantee = null == guaranteePaymentMap.get("agencyAmount")?null:new BigDecimal(guaranteePaymentMap.get("agencyAmount").toString());
	    				 RepaymentPlatformListGuarantee repaymentPlatformListGuarantee = new RepaymentPlatformListGuarantee();
	    				 repaymentPlatformListGuarantee.setProjectId(projectId1);
	    				 repaymentPlatformListGuarantee.setPeriod(period);
	    				 repaymentPlatformListGuarantee.setRepayId(repaymentPlatformList.getId());
	    				 repaymentPlatformListGuarantee.setRepayStatus(status);
	    				 repaymentPlatformListGuarantee.setPrincipalAndinterest(principalAndInterestGuarantee);
	    				 repaymentPlatformListGuarantee.setPenaltyAmount(penaltyAmountGuarantee);
	    				 repaymentPlatformListGuarantee.setTuandaiAmount(tuandaiAmountGuarantee);
	    				 repaymentPlatformListGuarantee.setOrgAmount(orgAmountGuarantee);
	    				 repaymentPlatformListGuarantee.setGuaranteeAmount(guaranteeAmountGuarantee);
	    				 repaymentPlatformListGuarantee.setArbitrationAmount(arbitrationAmountGuarantee);
	    				 repaymentPlatformListGuarantee.setAgencyAmount(agencyAmountGuarantee);
	    				 repaymentPlatformListGuarantee.setCreateMan("平台接口获取");
	    				 repaymentPlatformListGuarantee.setCreateTime(new Date());
	    				 repaymentPlatformListGuaranteeService.insert(repaymentPlatformListGuarantee);
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
		return null;
	}
	
    public Result<Object> getPushFlowList() {
    	//核心流程推送流程
    	//1# step1 查出未推送和推送失败的业务 list  tb_basic_business加3列 最后推送时间 最后推送状态 最后推送备注   另增加推送流水表
    	Map<String,Object> paramBusinessMap = new HashMap<>();
    	int actionId = 7;
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
    	for(Map<String,Object> businessMapInfo : listMap) {
    		CreateBatchFlowCommand command = new CreateBatchFlowCommand();
    		
    		String confirmLogId = businessMapInfo.get("confirm_log_id")+"";
    		//交易活动,0满标分润,1提现放款,2正常还款,3提前结清,4业务退费,5资金分发,6展期确认,7平台还款,8垫付,9账户提现,10账户充值,11账户转账,12暂收款登记
        	String batchId = confirmLogId;
        	
        	RepaymentPlatformList repaymentPlatformList = repaymentPlatformListService.selectById(confirmLogId);
        	
        	FlowPushLog flowPushLog = new FlowPushLog();
        	flowPushLog.setPushKey(batchId);
        	flowPushLog.setPushLogType(2);
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
        	String messageId = confirmLogId;
        	
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
        	List<Map<String,Object>> listFlow = getFlowInfoById(actionId,paramOnlineFlowMap);
        	
        	//3# step3 循环流水list，取出每条流水明细集合
        	List<Flow> flows = new ArrayList<>();
        	List<FlowAccountIdentifier> accountIdentifiers = new ArrayList<>();
        	List<FlowDetail> flowDetails = new ArrayList<>();
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
            	String identifierId = sId;
            	Boolean personal = true;
            	int accountType = 0;
            	String openBank = flowMap.get("open_bank")==null?"":flowMap.get("open_bank").toString();
            	flowAccountIdentifier.setAccountType(accountType);
            	flowAccountIdentifier.setDepositoryId(bankCardNo);
            	flowAccountIdentifier.setIdentifierId(identifierId);
            	flowAccountIdentifier.setPersonal(personal);
            	flowAccountIdentifier.setMainId(bankCardNo);
            	accountIdentifiers.add(flowAccountIdentifier);
            	
            	accountType = 8;
            	personal = false;
            	//收入账号
            	FlowAccountIdentifier flowAccountIdentifier2 = new FlowAccountIdentifier();
            	flowAccountIdentifier2.setAccountType(accountType);
            	flowAccountIdentifier2.setDepositoryId(targetBankCardNo);
            	flowAccountIdentifier2.setIdentifierId(tId);
            	flowAccountIdentifier2.setPersonal(personal);
            	flowAccountIdentifier2.setMainId(targetBankCardNo);
            	accountIdentifiers.add(flowAccountIdentifier2);
            	
            	Flow flow = new Flow();
            	Date accountTime = (Date) flowMap.get("account_time");
            	String afterId = flowMap.get("after_id").toString();
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
            	
            	//收入流水
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
            	flows.add(flow1);
            	
            	Map<String,Object> paramFlowItemMap = new HashMap<>();
            	paramFlowItemMap.put("repaySourceId", listId);
            	List<Map<String,Object>> listFlowItem = getFlowItemInfoById(actionId,paramFlowItemMap);
            	//封裝流水明細
            	for (Map<String, Object> listFlowItemMap : listFlowItem) {
            		Date detailAccountTime = (Date) listFlowItemMap.get("account_date");
            		String detailAfterId = listFlowItemMap.get("after_id").toString();
            		BigDecimal detailAmount = new BigDecimal(listFlowItemMap.get("amount").toString());
            		String detailFeeId = listFlowItemMap.get("fee_id").toString();
            		String detailFeeName = listFlowItemMap.get("fee_name").toString();
            		if(StringUtils.isBlank(detailFeeId)) {
            			detailFeeId = detailFeeName;
            		}
            		if("滞纳金".equals(detailFeeName)) {
            			String detailFeeName1 = RepayPlanFeeTypeEnum.feeIdOf(listFlowItemMap.get("fee_id")+"").getDesc();
            			if(!StringUtils.isBlank(detailFeeName1)) {
            				detailFeeName = detailFeeName1;
            			}
            		}
            		String detailIssueId = listFlowItemMap.get("issue_id")==null?"":listFlowItemMap.get("issue_id").toString();
            		int detailRegisterType = StringUtils.isBlank(flowMap.get("register_type")+"")?0:Integer.parseInt(listFlowItemMap.get("register_type").toString());
            		Date detailSegmentationDate = (Date) listFlowItemMap.get("segmentation_date");
            		
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
            	command.setActionId(actionId);
            	command.setBatchId(batchId);
            	command.setBusinessFlag(businessFlag);
            	command.setBusinessFrom(businessFrom);
            	command.setClientId(clientId);
            	command.setCreateTime(createTime);
            	command.setCreateUser(createUser);
            	command.setMessageId(messageId);
        		command.setBusiness(business); 
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
            	
            	int retryTimes = 0;
            	String retStr = "";
            	while(retryTimes < 3) {
            		try {
                		Result<Object> ret = accountListHandlerMsgClient.addMessageFlow(camsMessage);
                		System.err.println(JSON.toJSONString(camsMessage));
                    	System.err.println(JSONObject.toJSONString(ret));
                    	retStr = JSON.toJSONString(ret);
                		break;//跳出循环
            		} catch (Exception e) {
            			retStr = e.getMessage();
            			System.err.println(e.getMessage());
						try {
							Thread.sleep(100);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
            			retryTimes++;
    				}
            	}
            	
            	if(StringUtils.isNotBlank(retStr) && !retStr.contains("-500")) {
            	  repaymentPlatformList.setLastPushStatus(1);
            	  repaymentPlatformList.setLastPushRemark(retStr);
	              flowPushLog.setPushStatus(1);
	              flowPushLog.setPushRet(retStr);
	            }else {
	              repaymentPlatformList.setLastPushStatus(2);
	              repaymentPlatformList.setLastPushRemark(retStr);
	              flowPushLog.setPushStatus(2);
	              flowPushLog.setPushRet(retStr);
	            }
            	//更新推送状态
            	repaymentPlatformList.setLastPushDatetime(new Date());
            	repaymentPlatformListService.updateById(repaymentPlatformList);
                
            	//记录推送日志
            	flowPushLog.setPushEndtime(new Date());
            	flowPushLogService.insert(flowPushLog);
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
			break;
		case 8://垫付
			pushFlowList = repaymentPlatformListService.selectPushAdvancePayFlowList(paramBusinessMap);
			break;
		case 81://还垫付
			pushFlowList = repaymentPlatformListService.selectPushAdvanceRepayFlowList(paramBusinessMap);
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
	private List<Map<String, Object>> getFlowInfoById(int actionId, Map<String, Object> paramFlowMap) {
		List<Map<String, Object>> flowList = new ArrayList<>();
		switch (actionId) {
		case 7://平台还款
			flowList = flowList = repaymentPlatformListService.selectPushPlatformRepayFlow(paramFlowMap);
			break;
		case 8://垫付
			flowList = repaymentPlatformListService.selectPushAdvancePayFlow(paramFlowMap);
			break;
		case 81://还垫付
			flowList = repaymentPlatformListService.selectPushAdvanceRepayFlow(paramFlowMap);
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
			flowItemList = repaymentPlatformListService.selectPushPlatformRepayFlowItem(paramFlowItemMap);
			break;
		case 8://垫付
			flowItemList = repaymentPlatformListService.selectPushAdvancePayFlowItem(paramFlowItemMap);
			break;
		case 81://还垫付
			flowItemList = repaymentPlatformListService.selectPushAdvanceRepayFlowItem(paramFlowItemMap);
			break;
		default:
			break;
		}
		return flowItemList;
	}

}
