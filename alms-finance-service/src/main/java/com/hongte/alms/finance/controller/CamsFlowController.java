package com.hongte.alms.finance.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.util.IdGenerator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.fasterxml.jackson.annotation.ObjectIdGenerators.UUIDGenerator;
import com.hongte.alms.base.entity.AccountantOverRepayLog;
import com.hongte.alms.base.entity.FlowPushLog;
import com.hongte.alms.base.entity.RepaymentConfirmLog;
import com.hongte.alms.base.entity.TdrepayRechargeRecord;
import com.hongte.alms.base.enums.repayPlan.RepayPlanFeeTypeEnum;
import com.hongte.alms.base.feignClient.AccountListHandlerMsgClient;
import com.hongte.alms.base.service.AccountantOverRepayLogService;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.base.service.FlowPushLogService;
import com.hongte.alms.base.service.RepaymentConfirmLogService;
import com.hongte.alms.base.service.TdrepayRechargeRecordService;
import com.hongte.alms.base.util.UUIDHtGenerator;
import com.hongte.alms.base.vo.cams.CamsMessage;
import com.hongte.alms.base.vo.cams.CancelBizAccountListCommand;
import com.hongte.alms.base.vo.cams.CreateBatchFlowCommand;
import com.hongte.alms.base.vo.cams.CreateBatchFlowCommand.Business;
import com.hongte.alms.base.vo.cams.CreateBatchFlowCommand.Flow;
import com.hongte.alms.base.vo.cams.CreateBatchFlowCommand.FlowAccountIdentifier;
import com.hongte.alms.base.vo.cams.CreateBatchFlowCommand.FlowDetail;
import com.ht.ussp.core.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RefreshScope
@RestController
@RequestMapping("/camsFlowSync")
@Api(tags = "CamsFlowSyncController", description = "核心账号同步测试", hidden = true)
public class CamsFlowController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CamsFlowController.class);
    
    //虚拟账号定义
    private String mainIdYb = "97f6369b-e93d-11e7-94ed-94c69109b34a";//易宝
    private String mainIdBf = "98075bd6-e93d-11e7-94ed-94c69109b34a";//宝付
    private String dMainIdYb = "97f6364a-e93d-11e7-94ed-94c69109b34a";//易宝
    private String dMainIdBf = "98075b88-e93d-11e7-94ed-94c69109b34a";//宝付
    private String mainIdCard = "a98903d8-08b2-11e8-90c4-94c69109b34a";//刷卡账户
    private String mainIdCash = "bfefb0ec-08b2-11e8-90c4-94c69109b34a";//现金账户
    private String dMainIdCard = "7d4a3442bda446dc9df227024524d062";//刷卡账户
    private String dMainIdCash = "85dbddef27e3475c966da7b6ed0a343e";//现金账户
    private String mainIdnull = "无";//无的虚拟账号
    private String dMainIdnull = "786fd138695a4c53a7a45f3f323c8b0e";//无的现金账号
//    @Autowired
//    private AccountListHandlerClient accountListHandlerClient;
    
    @Autowired
    private AccountListHandlerMsgClient accountListHandlerMsgClient;
    
    @Autowired
    @Qualifier("RepaymentConfirmLogService")
    private RepaymentConfirmLogService repaymentConfirmLogService;
    
    @Autowired
    @Qualifier("BasicBusinessService")
    BasicBusinessService basicBusinessService;
    
    @Autowired
    @Qualifier("TdrepayRechargeRecordService")
    TdrepayRechargeRecordService tdrepayRechargeRecordService;
    
    @Autowired
    @Qualifier("FlowPushLogService")
    FlowPushLogService flowPushLogService;
    
    @Autowired
    @Qualifier("AccountantOverRepayLogService")
    AccountantOverRepayLogService accountantOverRepayLogService;
    
    /**
     * 批量新增账户流水
     * @param bankWithholdFlowReq
     * @return
     */
    @ApiOperation(value = "批量新增账户流水")
    @GetMapping("/addBatchFlow")
    @ResponseBody
    public Result<Object> addBatchFlow() {
    	//核心流程推送流程
    	//1# step1 查出未推送和推送失败的业务 list    tb_basic_business加3列 最后推送时间 最后推送状态 最后推送备注   另增加推送流水表
    	Map<String,Object> paramBusinessMap = new HashMap<>();
    	List<Map<String,Object>> listMap = basicBusinessService.selectlPushBusiness(paramBusinessMap);
    	addBusinessFlow(listMap);
    	return Result.buildSuccess("还款结清流水同步完成");
    }
    
    /**
     * 批量新增资金分发账户流水
     * @param bankWithholdFlowReq
     * @return
     */
    @ApiOperation(value = "批量新增资金分发流水")
    @GetMapping("/addBatchFenFaFlow")
    @ResponseBody
    public Result<Object> addBatchFenFaFlow() {
    	//核心流程推送流程
    	//1# step1 查出未推送和推送失败的业务 list    tb_basic_business加3列 最后推送时间 最后推送状态 最后推送备注   另增加推送流水表
    	Map<String,Object> paramBusinessMap = new HashMap<>();
    	List<Map<String,Object>> listMap = basicBusinessService.selectlPushFenFaBusiness(paramBusinessMap);
    	addBusinessFenFaFlow(listMap);
    	return Result.buildSuccess(0);
    }

	/**
     * 指定业务ID推送账户流水
     * @param bankWithholdFlowReq
     * @return
     */
    @ApiOperation(value = "指定业务ID新增账户流水")
    @GetMapping("/addFlowByBusinessId")
    @ResponseBody
    public Result<Object> addFlowByBusinessId(String businessId) {
    	//核心流程推送流程
    	//1# step1 查出未推送和推送失败的业务 list    tb_basic_business加3列 最后推送时间 最后推送状态 最后推送备注   另增加推送流水表
//    	Map<String,Object> paramMap = new HashMap<>();
    	Map<String,Object> paramBusinessMap = new HashMap<>();
    	paramBusinessMap.put("businessId",businessId);
    	List<Map<String,Object>> listMap = basicBusinessService.selectlPushBusiness(paramBusinessMap);
    	addBusinessFlow(listMap);
    	return Result.buildSuccess("指定业务ID新增账户流水成功");
    }
    
	/**
     * 指定业务ID和期数推送账户流水
     * @param bankWithholdFlowReq
     * @return
     */
    @ApiOperation(value = "指定业务ID和期数新增账户流水")
    @GetMapping("/addFlowByBusinessIdAndAfterId")
    @ResponseBody
    public Result<Object> addFlowByBusinessIdAndAfterId(String businessId,String afterId) {
    	//核心流程推送流程
    	//1# step1 查出未推送和推送失败的业务 list    tb_basic_business加3列 最后推送时间 最后推送状态 最后推送备注   另增加推送流水表
//    	Map<String,Object> paramMap = new HashMap<>();
    	Map<String,Object> paramBusinessMap = new HashMap<>();
    	paramBusinessMap.put("businessId",businessId);
    	paramBusinessMap.put("afterId",afterId);
    	List<Map<String,Object>> listMap = basicBusinessService.selectlPushBusiness(paramBusinessMap);
    	addBusinessFlow(listMap);
    	return Result.buildSuccess("指定业务ID新增账户流水成功");
    }
    
    /**
     * 指定confireLogID推送账户流水
     * @param bankWithholdFlowReq
     * @return
     */
    @ApiOperation(value = "指定confirmLogId同步账户流水到核心")
    @GetMapping("/addFlowByConfireLogID")
    @ResponseBody
    public Result<Object> addFlowByConfireLogID(String confirmLogId) {
    	//核心流程推送流程
    	//1# step1 查出未推送和推送失败的业务 list    tb_basic_business加3列 最后推送时间 最后推送状态 最后推送备注   另增加推送流水表
    	Map<String,Object> paramBusinessMap = new HashMap<>();
    	paramBusinessMap.put("confirmLogId", confirmLogId);
    	List<Map<String,Object>> listMap = basicBusinessService.selectlPushBusiness(paramBusinessMap);
    	addBusinessFlow(listMap);
    	return Result.buildSuccess("指定confirmLogId同步账户流水到核心成功");
    }

	private void addBusinessFenFaFlow(List<Map<String, Object>> listMap) {
		//2# step2 循环业务list，去除每一条业务的流水list
    	for(Map<String,Object> businessMapInfo : listMap) {
    		CreateBatchFlowCommand command = new CreateBatchFlowCommand();
    		String confirmLogId = businessMapInfo.get("confirm_log_id")+"";
    		//交易活动,0满标分润,1提现放款,2正常还款,3提前结清,4业务退费,5资金分发,6展期确认,7平台还款,8垫付,9账户提现,10账户充值,11账户转账,12暂收款登记
        	int actionId = Integer.parseInt(businessMapInfo.get("action_id").toString());
        	String batchId = confirmLogId;//businessMapInfo.get("repayment_batch_id")+"";
        	TdrepayRechargeRecord tdrepayRechargeRecord = tdrepayRechargeRecordService.selectOne(new EntityWrapper<TdrepayRechargeRecord>().eq("log_id", batchId).eq("is_valid", 1));
        	FlowPushLog flowPushLog = new FlowPushLog();
        	flowPushLog.setPushKey(batchId);
        	flowPushLog.setPushLogType(1);
        	flowPushLog.setPushTo(1);
        	flowPushLog.setPushStarttime(new Date());
        	flowPushLog.setPushStatus(0);
        	//是否业务交易明细,1是,0否
        	int businessFlag = Integer.parseInt(businessMapInfo.get("businessFlag").toString());
        	//所属资产端
        	int businessFrom = Integer.parseInt(businessMapInfo.get("businessFrom").toString());
        	String clientId = "ALMS"; //businessMapInfo.get("plate_type")+
        	Date createTime = new Date();//(Date) businessMapInfo.get("create_time");
        	String createUser = businessMapInfo.get("create_user")+"";
        	String messageId = confirmLogId;
        	
        	String branchId = businessMapInfo.get("company_id")+"";
        	String branchName = businessMapInfo.get("company_name")+"";
        	String businessId = businessMapInfo.get("business_id")+"";
        	String businessType = businessMapInfo.get("business_type")+"";
        	String businessTypeId = businessMapInfo.get("business_type_name")+"";
        	String customerName = businessMapInfo.get("customer_name")+"";
        	String exhibitionId = businessMapInfo.get("business_id")+"";
        	Business business = new Business();
        	business.setBranchId(branchId);
        	business.setBranchName(branchName);
        	business.setBusinessId(businessId);
        	business.setBusinessType(businessType);
        	business.setBusinessTypeId(businessTypeId);
        	business.setCustomerName(customerName);
        	business.setExhibitionId(exhibitionId);
        	
        	//取流水List集合  分为线上代扣流水 和 线下转账财务确认流水
        	//2.1 取线资金分发流水
        	Map<String,Object> paramOnlineFlowMap = new HashMap<>();
        	paramOnlineFlowMap.put("confirmLogId", confirmLogId);
        	List<Map<String,Object>> listOnlineFlow = basicBusinessService.selectlPushBusinessFenFaFlow(paramOnlineFlowMap);
        	
        	//3# step3 循环流水list，取出每条流水明细集合
        	List<Flow> flows = new ArrayList<>();
        	List<FlowAccountIdentifier> accountIdentifiers = new ArrayList<>();
        	List<FlowDetail> flowDetails = new ArrayList<>();
        	for (Map<String, Object> flowMap : listOnlineFlow) {
            	FlowAccountIdentifier flowAccountIdentifier = new FlowAccountIdentifier();
            	String accountName = flowMap.get("account_name")+"";
            	String bankCardNo = flowMap.get("bank_card_no")+"";
//            	String depositoryId = UUIDHtGenerator.getUUID();//存管编号
            	String depositoryId = null;
            	String identifierId = flowMap.get("main_id")+"";
            	Boolean personal = true;
            	int mainType = flowMap.get("main_type")==null||StringUtils.isBlank(flowMap.get("main_type").toString())?1:Integer.parseInt(flowMap.get("main_type")+"");
            	int accountType = mainType;
            	if(mainType == 2) {
            		personal = false;
            	}
            	String mainId = flowMap.get("main_id")+"";
            	
            	int repayType = Integer.parseInt(flowMap.get("repay_type").toString());
            	if(2 == repayType) {
            		mainId = mainIdBf;
            		depositoryId = dMainIdBf;
            	}
            	if(3 == repayType) {
            		mainId = mainIdYb;
            		depositoryId = dMainIdYb;
            	}
            	
            	if(repayType == 2 || repayType == 3) {
	            	String openBank = flowMap.get("open_bank")+"";
	            	flowAccountIdentifier.setAccountName(accountName);
	            	flowAccountIdentifier.setAccountType(accountType);
	            	flowAccountIdentifier.setBankCardNo(bankCardNo);
	            	flowAccountIdentifier.setDepositoryId(dMainIdYb);
	            	flowAccountIdentifier.setIdentifierId(identifierId);
	            	flowAccountIdentifier.setPersonal(personal);
	            	flowAccountIdentifier.setMainId(mainId);
	            	flowAccountIdentifier.setOpenBank(openBank);
	            	accountIdentifiers.add(flowAccountIdentifier);
            	}
            	
            	Flow flow = new Flow();
            	Date accountTime = new Date();
            	String afterId = flowMap.get("after_id")+"";
            	BigDecimal amount = new BigDecimal(flowMap.get("amount")==null?"0":flowMap.get("amount").toString());
            	String externalId = "";
            	int inOut = Integer.parseInt(flowMap.get("in_out").toString());
            	String issueId = flowMap.get("issue_id")+"";
            	String memo = "";
            	String remark = flowMap.get("remark")+"";
            	Date segmentationDate = (Date) flowMap.get("segmentation_date");
            	String sourceAccountIdentifierId = flowMap.get("target_account_id")+"";
            	String targetAccountIdentifierId = flowMap.get("target_bank_card_no")+"";
            	String listId = flowMap.get("list_id")+"";
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
            	flow.setSourceAccountIdentifierId(sourceAccountIdentifierId);
            	flow.setTargetAccountIdentifierId(targetAccountIdentifierId);
            	flows.add(flow);
        		
            	Map<String,Object> paramFlowItemMap = new HashMap<>();
            	paramFlowItemMap.put("repaySourceId", listId);
            	List<Map<String,Object>> listFlowItem = basicBusinessService.selectlPushBusinessFenFaFlowItem(paramFlowItemMap);
            	for (Map<String, Object> listFlowItemMap : listFlowItem) {
            		Date detailAccountTime = (Date) listFlowItemMap.get("account_date");
            		String detailAfterId = listFlowItemMap.get("after_id")+"";
            		BigDecimal detailAmount = new BigDecimal(listFlowItemMap.get("amount")+"");
            		String detailFeeId = listFlowItemMap.get("fee_id")+"";
            		String detailFeeName = listFlowItemMap.get("fee_name")+"";
            		String detailIssueId = listFlowItemMap.get("issue_id")+"";
            		int detailRegisterType = StringUtils.isBlank(flowMap.get("register_type")+"")?0:Integer.parseInt(listFlowItemMap.get("register_type")+"");
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
        		
        		//4# step4 按业务组装流水 消息对象tb_money_pool
        		//5# step5 调用核心推送接口推送
            	setBusiness(command);
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
                    	retStr = JSON.toJSONString(camsMessage);
                		break;//跳出循环
            		} catch (Exception e) {
            			System.err.println(e.getMessage());
						try {
							Thread.sleep(100);
						} catch (InterruptedException e1) {
							e1.printStackTrace();
						}
            			retryTimes++;
    				}
            	}
            	
            	if(StringUtils.isNotBlank(retStr) && retStr.contains("执行成功")) {
            		tdrepayRechargeRecord.setLastPushStatus(1);
            		tdrepayRechargeRecord.setLastPushRemark(retStr);
            		flowPushLog.setPushStatus(1);
            		flowPushLog.setPushRet(retStr);
            	}else {
            		tdrepayRechargeRecord.setLastPushStatus(2);
            		tdrepayRechargeRecord.setLastPushRemark(retStr);
            		flowPushLog.setPushStatus(2);
            		flowPushLog.setPushRet(retStr);
            	}
            	//更新推送状态
            	tdrepayRechargeRecord.setLastPushDatetime(new Date());
            	tdrepayRechargeRecordService.update(tdrepayRechargeRecord,new EntityWrapper<TdrepayRechargeRecord>().eq("log_id", confirmLogId).eq("is_valid", 1));
            	
            	//记录推送日志
            	flowPushLog.setPushEndtime(new Date());
            	flowPushLogService.insert(flowPushLog);
        	}
    	}
	}
	
	private void addBusinessFlow(List<Map<String, Object>> listMap) {
		//2# step2 循环业务list，去除每一条业务的流水list
    	for(Map<String,Object> businessMapInfo : listMap) {
    		CreateBatchFlowCommand command = new CreateBatchFlowCommand();
    		String confirmLogId = businessMapInfo.get("confirm_log_id")+"";
    		//交易活动,0满标分润,1提现放款,2正常还款,3提前结清,4业务退费,5资金分发,6展期确认,7平台还款,8垫付,9账户提现,10账户充值,11账户转账,12暂收款登记
        	int actionId = Integer.parseInt(businessMapInfo.get("action_id").toString());
        	String batchId = confirmLogId;
        	RepaymentConfirmLog repaymentConfirmLog = repaymentConfirmLogService.selectById(batchId);
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
        	String planStatus = businessMapInfo.get("plan_status")==null?"":businessMapInfo.get("plan_status").toString();
        	if(!StringUtils.isBlank(businessCtype)) {
        		businessType = businessCtype;
        	}
        	if(planStatus.equals("31") || planStatus.equals("32") || planStatus.equals("35")) {
        		actionId = Integer.parseInt(planStatus);
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
        	//2.1 取线上代扣 流水
        	Map<String,Object> paramOnlineFlowMap = new HashMap<>();
        	paramOnlineFlowMap.put("confirmLogId", confirmLogId);
        	List<Map<String,Object>> listOnlineFlow = basicBusinessService.selectlPushBusinessOnlineFlow(paramOnlineFlowMap);
        	//2.2 取线下代扣流水
        	Map<String,Object> paramOfflineFlowMap = new HashMap<>();
        	paramOfflineFlowMap.put("confirmLogId", confirmLogId);
        	List<Map<String,Object>> listOfflineFlow = basicBusinessService.selectlPushBusinessOfflineFlow(paramOfflineFlowMap);
        	//2.3 合并流水
        	listOnlineFlow.addAll(listOfflineFlow);
        	
        	//3# step3 循环流水list，取出每条流水明细集合
        	List<Flow> flows = new ArrayList<>();
        	List<FlowAccountIdentifier> accountIdentifiers = new ArrayList<>();
        	List<FlowDetail> flowDetails = new ArrayList<>();
        	int flagLeft = 0;//结余标记
        	int flagLeftToRepay = 0;//结余转还款标记
        	for (Map<String, Object> flowMap : listOnlineFlow) {
        		String sId = UUIDHtGenerator.getUUID();
        		String tId = UUIDHtGenerator.getUUID();
        		//还款账号
            	FlowAccountIdentifier flowAccountIdentifier = new FlowAccountIdentifier();
            	String accountName = flowMap.get("account_name")==null?"":flowMap.get("account_name").toString();
            	String bankCardNo = flowMap.get("bank_card_no")==null?"":flowMap.get("bank_card_no").toString();
            	String depositoryId = null;//存管编号
            	String tAreaId = flowMap.get("t_area_id")==null?"":flowMap.get("t_area_id").toString();
            	String tAreaName = flowMap.get("t_area_name")==null?"":flowMap.get("t_area_name").toString();
            	//flowMap.get("main_id")+"";
            	String identifierId = sId;
            	Boolean personal = true;
            	int mainType = flowMap.get("main_type")==null||StringUtils.isBlank(flowMap.get("main_type").toString())?1:Integer.parseInt(flowMap.get("main_type")+"");
            	if(mainType == 2) {
            		personal = false;
            	}
            	int accountType = 0;
            	String mainId = flowMap.get("main_id")==null?"":flowMap.get("main_id").toString();
            	int repayType = Integer.parseInt(flowMap.get("repay_type").toString());
            	
            	if(repayType == 1) {//手动
            		accountType = 9;
            	}
            	
            	if(repayType == 7) {
            		depositoryId = dMainIdnull;
            		mainId = mainIdnull;
            		accountName = "无";
            	}
            	
            	if("刷卡".equals(accountName)) {
            		depositoryId = dMainIdCard;
            		mainId = mainIdCard;
            	}
            	
            	if("现金".equals(accountName)) {
            		depositoryId = dMainIdCash;
            		mainId = mainIdCash;
            	}
            	
            	String openBank = flowMap.get("open_bank")+"";
            	flowAccountIdentifier.setAccountName(accountName);
            	flowAccountIdentifier.setAccountType(accountType);
            	flowAccountIdentifier.setBankCardNo(bankCardNo);
            	flowAccountIdentifier.setDepositoryId(depositoryId);
            	flowAccountIdentifier.setIdentifierId(identifierId);
            	flowAccountIdentifier.setPersonal(personal);
            	flowAccountIdentifier.setMainId(mainId);
            	flowAccountIdentifier.setOpenBank(openBank);
            	flowAccountIdentifier.setBranchId(tAreaId);
            	flowAccountIdentifier.setBranchName(tAreaName);
            	accountIdentifiers.add(flowAccountIdentifier);
            	accountType = 1;
            	//收入账号
            	if(2 == repayType) {
            		depositoryId = dMainIdBf;
            		mainId = mainIdBf;
            		accountName = "宝付";
            		
            	}
            	if(3 == repayType) {
            		depositoryId = dMainIdYb;
            		mainId = mainIdYb;
            		accountName = "易宝";
            	}
            	if(repayType == 2 || repayType == 3) {
	            	FlowAccountIdentifier flowAccountIdentifier2 = new FlowAccountIdentifier();
	            	flowAccountIdentifier2.setAccountName(accountName);
	            	flowAccountIdentifier2.setAccountType(4);
	            	flowAccountIdentifier2.setBankCardNo("");
	            	flowAccountIdentifier2.setDepositoryId(depositoryId);
	            	flowAccountIdentifier2.setIdentifierId(tId);
	            	flowAccountIdentifier2.setPersonal(personal);
	            	flowAccountIdentifier2.setMainId(mainId);
	            	flowAccountIdentifier2.setOpenBank("");
	            	accountIdentifiers.add(flowAccountIdentifier2);
            	}
            	if(repayType == 4) {
            		FlowAccountIdentifier flowAccountIdentifier2 = new FlowAccountIdentifier();
            		flowAccountIdentifier2.setAccountName(accountName);
                	flowAccountIdentifier2.setAccountType(9);
                	flowAccountIdentifier2.setBankCardNo(bankCardNo);
                	flowAccountIdentifier2.setDepositoryId(depositoryId);
                	flowAccountIdentifier2.setIdentifierId(tId);
                	flowAccountIdentifier2.setPersonal(personal);
                	flowAccountIdentifier2.setMainId(mainId);
                	flowAccountIdentifier2.setOpenBank(openBank);
                	accountIdentifiers.add(flowAccountIdentifier2);
            	}
            	
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
            	if(repayType == 7) {
            		inOut = -1;
            		amount = new BigDecimal(0);
            	}
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
            	if(4 == repayType) {//银行代扣
            		flow.setSourceAccountIdentifierId(sourceAccountIdentifierId);
                	flow.setTargetAccountIdentifierId(targetAccountIdentifierId);
            	}else if(repayType != 2 && repayType != 3) {//线下还款
            		flow.setSourceAccountIdentifierId(sourceAccountIdentifierId);
                	flow.setTargetAccountIdentifierId(null);
            	}
            	if(repayType != 7 || listOnlineFlow.size() == 1) {
            		flows.add(flow);
            	}
            	Map<String,Object> paramFlowItemMap = new HashMap<>();
            	paramFlowItemMap.put("repaySourceId", listId);
            	List<Map<String,Object>> listFlowItem = basicBusinessService.selectlPushBusinessFlowItem(paramFlowItemMap);
            	for (Map<String, Object> listFlowItemMap : listFlowItem) {
            		Date detailAccountTime = (Date) listFlowItemMap.get("account_date");
            		String detailAfterId = listFlowItemMap.get("after_id").toString();
            		BigDecimal detailAmount = new BigDecimal(listFlowItemMap.get("amount").toString());
            		String detailFeeId = listFlowItemMap.get("fee_id").toString();
            		String detailFeeName = listFlowItemMap.get("fee_name").toString();
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
            	//结余 费用
            	String surplusRefId = repaymentConfirmLog.getSurplusRefId();
            	if(!StringUtils.isBlank(surplusRefId)) {
            		AccountantOverRepayLog accountantOverRepayLog = accountantOverRepayLogService.selectById(surplusRefId);
            		if(null != accountantOverRepayLog && flagLeft == 0) {
            			flagLeft = 1;
            			FlowDetail flowDetail = new FlowDetail();
                		flowDetail.setAccountTime(accountantOverRepayLog.getCreateTime());
                		flowDetail.setAfterId(repaymentConfirmLog.getAfterId());
                		flowDetail.setAmount(accountantOverRepayLog.getOverRepayMoney());
                		flowDetail.setFeeId("8d76bc55-f80a-11e7-94ed-94c69109b34a");
                		flowDetail.setFeeName("结余");
                		flowDetail.setIssueId(issueId);
                		flowDetail.setRegisterType(0);
                		flowDetail.setSegmentationDate(segmentationDate);
                		flowDetails.add(flowDetail);
            		}
            	}
            	
            	//结余 费用
            	String surplusUseRefId = repaymentConfirmLog.getSurplusUseRefId();
            	if(!StringUtils.isBlank(surplusUseRefId)) {
            		AccountantOverRepayLog accountantOverRepayLog = accountantOverRepayLogService.selectById(surplusUseRefId);
            		if(null != accountantOverRepayLog && flagLeftToRepay == 0) {
            			flagLeftToRepay = 1;
            			FlowDetail flowDetail = new FlowDetail();
                		flowDetail.setAccountTime(accountantOverRepayLog.getCreateTime());
                		flowDetail.setAfterId(repaymentConfirmLog.getAfterId());
                		flowDetail.setAmount(accountantOverRepayLog.getOverRepayMoney());
                		flowDetail.setFeeId("de273023-20e6-11e8-a070-000c296e4c97");
                		flowDetail.setFeeName("结余转还款");
                		flowDetail.setIssueId(issueId);
                		flowDetail.setRegisterType(0);
                		flowDetail.setSegmentationDate(segmentationDate);
                		flowDetails.add(flowDetail);
            		}
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
            	setBusiness(command);
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
            	
            	if(StringUtils.isNotBlank(retStr) && retStr.contains("执行成功")) {
            		repaymentConfirmLog.setLastPushStatus(1);
            		repaymentConfirmLog.setLastPushRemark(retStr);
            		flowPushLog.setPushStatus(1);
            	}else {
            		repaymentConfirmLog.setLastPushStatus(2);
            		repaymentConfirmLog.setLastPushRemark(retStr);
            		flowPushLog.setPushStatus(2);
            	}
            	//更新推送状态
            	repaymentConfirmLog.setLastPushDatetime(new Date());
            	repaymentConfirmLogService.updateById(repaymentConfirmLog);
            	
            	//记录推送日志
            	flowPushLog.setPushEndtime(new Date());
            	flowPushLogService.insert(flowPushLog);
        	}
    	}

	private void setBusiness(CreateBatchFlowCommand command) {

	}
	
    @ApiOperation(value = "插入临时表")
    @GetMapping("/insertFlowToTemp")
    @ResponseBody
	public Result<Object> insertFlowToTemp(){
    	
    	//插入临时流水表
    	Map<String,Object> paramLastDayMap = new HashMap<>();
    	//Date preDate = DateUtil.addDay2Date(-1, new Date());
    	paramLastDayMap.put("syncDay1","2018-09-03");//DateUtil.getThatDayBegin(preDate)
    	paramLastDayMap.put("syncDay2","2018-09-03");//DateUtil.getThatDayEnd(preDate)
    	//paramLastDayMap.put("syncDay", DateUtil.toDateString(preDate, DateUtil.DEFAULT_FORMAT_DATE));
    	basicBusinessService.deleteLastDayFlow(paramLastDayMap);
    	basicBusinessService.addLastDayFlow(paramLastDayMap);
        //插入临时流水明细表
    	basicBusinessService.deleteLastDayFlowItem(paramLastDayMap);
        basicBusinessService.addLastDayFlowItem(paramLastDayMap);
    	return Result.buildSuccess("同步还款流水到临时表成功");
	}
	
    /**
     * 撤销业务流水
     * @param bankWithholdFlowReq
     * @return
     */
    @ApiOperation(value = "撤销业务还款流水")
    @GetMapping("/cancelRepayFlow")
    @ResponseBody
    public Result<Object> cancelRepayFlow() {
    	// 还款 结清撤销
    	Map<String,Object> paramRepayFlowMap = new HashMap<>();
    	List<Map<String,Object>> listRepayFlow = basicBusinessService.selectlCancelRepayFlow(paramRepayFlowMap);
    	
    	for (Map<String, Object> mapFlow : listRepayFlow) {
			String businessId = mapFlow.get("business_id").toString();
			String afterId = mapFlow.get("after_id").toString();
			Date queryFullsuccessDate = (Date) mapFlow.get("queryFullsuccessDate");
			String logId = mapFlow.get("confirm_log_id").toString();
			//String flowType = mapFlow.get("flowType").toString();
			String clientId = "ALMS";
			
			//记录日志
			RepaymentConfirmLog repaymentConfirmLog = repaymentConfirmLogService.selectById(logId);
	    	FlowPushLog flowPushLog = new FlowPushLog();
	    	flowPushLog.setPushKey(logId);
	    	flowPushLog.setPushLogType(1);
	    	flowPushLog.setPushTo(1);
	    	flowPushLog.setPushStarttime(new Date());
	    	flowPushLog.setPushStatus(0);
			
	    	CancelBizAccountListCommand command = new CancelBizAccountListCommand();
	    	command.setAfterId(afterId);
	    	command.setBusinessId(businessId);
	    	command.setClientId(clientId);
	    	command.setCreateTime(new Date());//queryFullsuccessDate
	    	command.setTriggerEventSystem("1");
	    	command.setEventType("FlowCanceled");
	    	command.setTriggerEventType("CanceledFlow");
	    	command.setSegmentationDate(queryFullsuccessDate);
	    	command.setMessageId(logId);
	    	
	    	CamsMessage camsMessage = new CamsMessage();
	    	camsMessage.setClientId(clientId);
	    	camsMessage.setExchangeName("cams.account.ms.exchange");
	    	camsMessage.setHostPort(0);
	    	camsMessage.setHostUrl("192.168.14.245");
	    	camsMessage.setMessageId(UUID.randomUUID().toString());
	    	camsMessage.setQueueName("cams.account.ms.queue.bizAccountListCanceledQueue");
	    	camsMessage.setMessage(command);
	    	
	    	int retryTimes = 0;
	    	String retStr = "";
	    	while(retryTimes < 3) {
	    		try {
	    			// 撤销已推送
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
	    	
	    	if(StringUtils.isNotBlank(retStr) && retStr.contains("执行成功")) {
	    		repaymentConfirmLog.setLastPushStatus(4);
	    		repaymentConfirmLog.setLastPushRemark(retStr);
				flowPushLog.setPushStatus(4);
			}else {
				repaymentConfirmLog.setLastPushStatus(5);
				repaymentConfirmLog.setLastPushRemark(retStr);
				flowPushLog.setPushStatus(5);
			}
	    	
	    	//更新推送状态
	    	repaymentConfirmLog.setLastPushDatetime(new Date());
	    	repaymentConfirmLogService.updateById(repaymentConfirmLog);
	    	
	    	//记录推送日志
	    	flowPushLog.setPushEndtime(new Date());
	    	flowPushLogService.insert(flowPushLog);
		}
    	return Result.buildSuccess("撤销业务还款流水成功");
    }
    
    
    @ApiOperation(value = "撤销资金分发还款流水")
    @GetMapping("/cancelFenFaFlow")
    @ResponseBody
    public Result<Object> cancelFenFaFlow() {
  
    	// 资金分发撤销
    	Map<String,Object> paramFenFaFlowMap = new HashMap<>();
    	List<Map<String,Object>> listFenFaFlow = basicBusinessService.selectlCancelFenFaFlow(paramFenFaFlowMap);
    	
    	for (Map<String, Object> mapFlow : listFenFaFlow) {
			String businessId = mapFlow.get("business_id").toString();
			String afterId = mapFlow.get("after_id").toString();
			Date queryFullsuccessDate = (Date) mapFlow.get("queryFullsuccessDate");
			String logId = mapFlow.get("confirm_log_id").toString();
			//String flowType = mapFlow.get("flowType").toString();
			String clientId = "ALMS";
			
			//记录日志
			TdrepayRechargeRecord tdrepayRechargeRecord = tdrepayRechargeRecordService.selectOne(new EntityWrapper<TdrepayRechargeRecord>().eq("log_id", logId).eq("is_valid", 2));
			FlowPushLog flowPushLog = new FlowPushLog();
	    	flowPushLog.setPushKey(logId);
	    	flowPushLog.setPushLogType(1);
	    	flowPushLog.setPushTo(1);
	    	flowPushLog.setPushStarttime(new Date());
	    	flowPushLog.setPushStatus(0);
			
	    	CancelBizAccountListCommand command = new CancelBizAccountListCommand();
	    	command.setAfterId(afterId);
	    	command.setBusinessId(businessId);
	    	command.setClientId(clientId);
	    	command.setCreateTime(queryFullsuccessDate);
	    	command.setTriggerEventSystem("1");
	    	command.setEventType("FlowCanceled");
	    	command.setTriggerEventType("CanceledFlow");
	    	command.setSegmentationDate(queryFullsuccessDate);
	    	command.setMessageId(logId);
	    	
	    	CamsMessage camsMessage = new CamsMessage();
	    	camsMessage.setClientId(clientId);
	    	camsMessage.setExchangeName("cams.account.ms.exchange");
	    	camsMessage.setHostPort(0);
	    	camsMessage.setHostUrl("192.168.14.245");
	    	camsMessage.setMessageId(UUID.randomUUID().toString());
	    	camsMessage.setQueueName("cams.account.ms.queue.bizAccountListCanceledQueue");
	    	camsMessage.setMessage(command);
	    	
	    	int retryTimes = 0;
	    	String retStr = "";
	    	while(retryTimes < 3) {
	    		try {
	    			// 撤销已推送
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
	    	
	    	if(StringUtils.isNotBlank(retStr) && retStr.contains("执行成功")) {
	    		tdrepayRechargeRecord.setLastPushStatus(4);
	    		tdrepayRechargeRecord.setLastPushRemark(retStr);
				flowPushLog.setPushStatus(4);
			}else {
				tdrepayRechargeRecord.setLastPushStatus(5);
				tdrepayRechargeRecord.setLastPushRemark(retStr);
				flowPushLog.setPushStatus(5);
			}
	    	
        	//更新推送状态
	    	tdrepayRechargeRecord.setLastPushDatetime(new Date());
	    	tdrepayRechargeRecordService.update(tdrepayRechargeRecord,new EntityWrapper<TdrepayRechargeRecord>().eq("log_id", logId).eq("is_valid", 2));
        	
        	//记录推送日志
        	flowPushLog.setPushEndtime(new Date());
        	flowPushLogService.insert(flowPushLog);
		}
    	
    	return Result.buildSuccess(0);
    }
}
