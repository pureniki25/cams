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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hongte.alms.base.feignClient.AccountListHandlerClient;
import com.hongte.alms.base.feignClient.AccountListHandlerMsgClient;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.base.vo.cams.CamsMessage;
import com.hongte.alms.base.vo.cams.CancelBizAccountListCommand;
import com.hongte.alms.base.vo.cams.CreateBatchFlowCommand;
import com.hongte.alms.base.vo.cams.CreateBatchFlowCommand.Business;
import com.hongte.alms.base.vo.cams.CreateBatchFlowCommand.Flow;
import com.hongte.alms.base.vo.cams.CreateBatchFlowCommand.FlowAccountIdentifier;
import com.hongte.alms.base.vo.cams.CreateBatchFlowCommand.FlowDetail;
import com.hongte.alms.common.util.DateUtil;
import com.ht.ussp.core.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RefreshScope
@RestController
@RequestMapping("/camsFlowSync")
@Api(tags = "CamsFlowSyncController", description = "核心账号同步测试", hidden = true)
public class CamsFlowController {

    private static final Logger LOGGER = LoggerFactory.getLogger(CamsFlowController.class);

    @Autowired
    private AccountListHandlerClient accountListHandlerClient;
    
    @Autowired
    private AccountListHandlerMsgClient accountListHandlerMsgClient;
    
    @Autowired
    @Qualifier("BasicBusinessService")
    BasicBusinessService basicBusinessService;
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
    	return Result.buildSuccess();
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
    	return Result.buildSuccess();
    }
    
    /**
     * 指定confireLogID推送账户流水
     * @param bankWithholdFlowReq
     * @return
     */
    @ApiOperation(value = "指定业务ID新增账户流水")
    @GetMapping("/addFlowByConfireLogID")
    @ResponseBody
    public Result<Object> addFlowByConfireLogID(String confirmLogId) {
    	//核心流程推送流程
    	//1# step1 查出未推送和推送失败的业务 list    tb_basic_business加3列 最后推送时间 最后推送状态 最后推送备注   另增加推送流水表
    	Map<String,Object> paramBusinessMap = new HashMap<>();
    	paramBusinessMap.put("confirmLogId", confirmLogId);
    	List<Map<String,Object>> listMap = basicBusinessService.selectlPushBusiness(paramBusinessMap);
    	addBusinessFlow(listMap);
    	return Result.buildSuccess();
    }

	private void addBusinessFlow(List<Map<String, Object>> listMap) {
		//2# step2 循环业务list，去除每一条业务的流水list
    	for(Map<String,Object> businessMapInfo : listMap) {
    		CreateBatchFlowCommand command = new CreateBatchFlowCommand();
    		String confirmLogId = businessMapInfo.get("confirm_log_id")+"";
    		//交易活动,0满标分润,1提现放款,2正常还款,3提前结清,4业务退费,5资金分发,6展期确认,7平台还款,8垫付,9账户提现,10账户充值,11账户转账,12暂收款登记
        	int actionId = 2;
        	String batchId = businessMapInfo.get("repayment_batch_id")+"";
        	//是否业务交易明细,1是,0否
        	int businessFlag = 1;
        	//所属资产端
        	int businessFrom = 2;
        	String clientId = "ALMS"; //businessMapInfo.get("plate_type")+
        	Date createTime = (Date) businessMapInfo.get("create_time");
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
        	for (Map<String, Object> flowMap : listOnlineFlow) {
            	FlowAccountIdentifier flowAccountIdentifier = new FlowAccountIdentifier();
            	String accountName = flowMap.get("account_name")+"";
            	String bankCardNo = flowMap.get("bank_card_no")+"";
            	String depositoryId = "";//存管编号
            	String identifierId = flowMap.get("main_id")+"";
            	Boolean personal = true;
            	int mainType = flowMap.get("main_type")==null||StringUtils.isBlank(flowMap.get("main_type").toString())?1:Integer.parseInt(flowMap.get("main_type")+"");
            	int accountType = mainType;
            	if(mainType == 2) {
            		personal = false;
            	}
            	String mainId = flowMap.get("main_id")+"";
            	String openBank = flowMap.get("open_bank")+"";
            	flowAccountIdentifier.setAccountName(accountName);
            	flowAccountIdentifier.setAccountType(accountType);
            	flowAccountIdentifier.setBankCardNo(bankCardNo);
            	flowAccountIdentifier.setDepositoryId(depositoryId);
            	flowAccountIdentifier.setIdentifierId(identifierId);
            	flowAccountIdentifier.setPersonal(personal);
            	flowAccountIdentifier.setMainId(mainId);
            	flowAccountIdentifier.setOpenBank(openBank);
            	accountIdentifiers.add(flowAccountIdentifier);
            	
            	Flow flow = new Flow();
            	Date accountTime = new Date();
            	String afterId = flowMap.get("after_id")+"";
            	BigDecimal amount = new BigDecimal(flowMap.get("amount")==null?"0":flowMap.get("amount").toString());
            	String externalId = "";
            	int inOut = 1;
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
            	flow.getRepayType();
            	flow.setSegmentationDate(segmentationDate);
            	flow.setSourceAccountIdentifierId(sourceAccountIdentifierId);
            	flow.setTargetAccountIdentifierId(targetAccountIdentifierId);
            	flows.add(flow);
        		
            	Map<String,Object> paramFlowItemMap = new HashMap<>();
            	paramFlowItemMap.put("repaySourceId", listId);
            	List<Map<String,Object>> listFlowItem = basicBusinessService.selectlPushBusinessFlowItem(paramFlowItemMap);
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
            	
            	if(retStr.contains("执行成功")) {
            		
            	}else {
            		
            	}
        	}
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
    	Date preDate = DateUtil.addDay2Date(-1, new Date());
    	paramLastDayMap.put("syncDay1","2018-09-03");//DateUtil.getThatDayBegin(preDate)
    	paramLastDayMap.put("syncDay2","2018-09-03");//DateUtil.getThatDayEnd(preDate)
    	//paramLastDayMap.put("syncDay", DateUtil.toDateString(preDate, DateUtil.DEFAULT_FORMAT_DATE));
    	basicBusinessService.deleteLastDayFlow(paramLastDayMap);
    	basicBusinessService.addLastDayFlow(paramLastDayMap);
        //插入临时流水明细表
    	basicBusinessService.deleteLastDayFlowItem(paramLastDayMap);
        basicBusinessService.addLastDayFlowItem(paramLastDayMap);
    	return Result.buildSuccess();
	}
	
    /**
     * 撤销业务流水
     * @param bankWithholdFlowReq
     * @return
     */
    @ApiOperation(value = "撤销业务流水")
    @GetMapping("/cancelFlow")
    @ResponseBody
    public Result<Object> cancelFlow() {
    	CancelBizAccountListCommand command = new CancelBizAccountListCommand();
    	return accountListHandlerClient.cancelFlow(command);
    }
}
