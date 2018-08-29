package com.hongte.alms.finance.controller;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.feignClient.AccountListHandlerClient;
import com.hongte.alms.base.service.BasicBusinessService;
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

    @Autowired
    private AccountListHandlerClient accountListHandlerClient;
    
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
//    	Map<String,Object> paramMap = new HashMap<>();
    	List<Map<String,Object>> listMap = basicBusinessService.selectlPushBusiness();
    	addBusinessFlow(listMap);
    	return Result.buildSuccess();
    }

	private void addBusinessFlow(List<Map<String, Object>> listMap) {
		//2# step2 循环业务list，去除每一条业务的流水list
    	for(Map<String,Object> businessMapInfo : listMap) {
    		CreateBatchFlowCommand command = new CreateBatchFlowCommand();
    		//交易活动,0满标分润,1提现放款,2正常还款,3提前结清,4业务退费,5资金分发,6展期确认,7平台还款,8垫付,9账户提现,10账户充值,11账户转账,12暂收款登记
        	int actionId = 2;
        	String batchId = businessMapInfo.get("business_after_guid")+"";
        	//是否业务交易明细,1是,0否
        	int businessFlag = 1;
        	//所属资产端
        	int businessFrom = 2;
        	String clientId = businessMapInfo.get("plate_type")+"";
        	Date createTime = new Date();
        	String createUser = businessMapInfo.get("create_user")+"";
        	String messageId = "";
        	
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
        	paramOnlineFlowMap.put("businessId", businessId);
        	List<Map<String,Object>> listOnlineFlow = basicBusinessService.selectlPushBusinessOnlineFlow(paramOnlineFlowMap);
        	//2.2 取线下代扣流水
        	Map<String,Object> paramOfflineFlowMap = new HashMap<>();
        	paramOnlineFlowMap.put("businessId", businessId);
        	List<Map<String,Object>> listOfflineFlow = basicBusinessService.selectlPushBusinessOfflineFlow(paramOnlineFlowMap);
        	//2.3 合并流水
        	listOnlineFlow.addAll(listOfflineFlow);
        	
        	//3# step3 循环流水list，取出每条流水明细集合
        	List<Flow> flows = new ArrayList<>();
        	List<FlowAccountIdentifier> accountIdentifiers = new ArrayList<>();
        	List<FlowDetail> flowDetails = new ArrayList<>();
        	for (Map<String, Object> flowMap : listOfflineFlow) {
            	FlowAccountIdentifier flowAccountIdentifier = new FlowAccountIdentifier();
            	String accountName = "";
            	int accountType = 0;
            	String bankCardNo = "";
            	String depositoryId = "";
            	String identifierId = "";
            	Boolean personal = false;
            	String mainId = "";
            	String openBank = "";
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
            	String afterId = "";
            	BigDecimal amount = new BigDecimal(0);
            	String externalId = "";
            	int inOut = 1;
            	String issueId = "";
            	String memo = "";
            	String remark = "";
            	Date segmentationDate = new Date();
            	String sourceAccountIdentifierId = "";
            	String targetAccountIdentifierId = "";
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
            	List<Map<String,Object>> listFlowItem = basicBusinessService.selectlPushBusinessFlowItem(paramFlowItemMap);
            	for (Map<String, Object> listFlowItemMap : listFlowItem) {
            		FlowDetail flowDetail = new FlowDetail();
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
            	accountListHandlerClient.addBatchFlow(command);
        	}
    	}
	}

	private void setBusiness(CreateBatchFlowCommand command) {

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
