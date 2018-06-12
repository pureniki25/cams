package com.hongte.alms.platrepay.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.hongte.alms.base.dto.PlatRepayDto;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.RepaymentProjPlanList;
import com.hongte.alms.base.entity.TuandaiProjectInfo;
import com.hongte.alms.base.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.dto.RechargeModalDTO;
import com.hongte.alms.base.entity.AgencyRechargeLog;
import com.hongte.alms.base.feignClient.EipRemote;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.CommonUtil;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.platrepay.service.TdrepayRechargeService;
import com.hongte.alms.platrepay.vo.RechargeModalVO;
import com.ht.ussp.bean.LoginUserInfoHelper;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/toPlatRepay")
public class ToPlatRepayController {
	private static final Logger LOG = LoggerFactory.getLogger(ToPlatRepayController.class);

	@Autowired
	private EipRemote eipRemote;

	@Autowired
	private LoginUserInfoHelper loginUserInfoHelper;

	@Autowired
	@Qualifier("AgencyRechargeLogService")
	private AgencyRechargeLogService agencyRechargeLogService;
	
	@Autowired
	@Qualifier("TdrepayRechargeService")
	private TdrepayRechargeService tdrepayRechargeService;

	@Autowired
	@Qualifier("DepartmentBankService")
	private DepartmentBankService departmentBankService;

	@Autowired
	@Qualifier("TuandaiProjectInfoService")
	private TuandaiProjectInfoService tuandaiProjectInfoService;


	@Autowired
	@Qualifier("BasicBusinessService")
	private BasicBusinessService basicBusinessService;

	@Autowired
	@Qualifier("RepaymentProjPlanListService")
	RepaymentProjPlanListService repaymentProjPlanListService;

	



	@ApiOperation(value = "对接合规还款接口")
	@GetMapping("/repay")
	@ResponseBody
	public Result reapy(String projectId,String afterId) {
		LOG.info("@对接合规还款接口 开始 @输入参数 projectId:[{}}  afterId[{}]",projectId,afterId);
		try {
			PlatRepayDto platRepayDto = new PlatRepayDto();

			platRepayDto.setProjectId(projectId);
			platRepayDto.setAfterId(afterId);
			TuandaiProjectInfo tuandaiProjectInfo = tuandaiProjectInfoService.selectOne(new EntityWrapper<TuandaiProjectInfo>().eq("project_id",projectId));

			if(tuandaiProjectInfo == null){
				LOG.error("@对接合规还款接口@  查不到上标信息 输入参数 projectId:[{}}  ",projectId);
				return Result.error("500","查不到上标信息");
			}


			platRepayDto.setAssetType(1);  //业务所属资产端，1、鸿特信息，2、 一点车贷
			BasicBusiness  basicBusiness =  basicBusinessService.selectById(tuandaiProjectInfo.getBusinessId());
			platRepayDto.setOrigBusinessId(basicBusiness.getSourceBusinessId());

			repaymentProjPlanListService.selectList(new EntityWrapper<RepaymentProjPlanList>().eq("",projectId).eq("",afterId));


//			/**
//			 * 原业务编号
//			 */
//			@TableField("orig_business_id")
//			@ApiModelProperty(required= true,value = "原业务编号")
//			private String origBusinessId;
//			/**
//			 * 业务类型(1:车易贷展期,2:房速贷展期,3:金融仓储,4:三农金融,9:车易贷,11:房速贷,12车全垫资代采,13:扶贫贷,14:汽车融资租赁,15:二手车商贷,20:一点车贷,25:信用贷)
//			 */
//			@TableField("business_type")
//			@ApiModelProperty(required= true,value = "业务类型(1:车易贷展期,2:房速贷展期,3:金融仓储,4:三农金融,9:车易贷,11:房速贷,12车全垫资代采,13:扶贫贷,14:汽车融资租赁,15:二手车商贷,20:一点车贷,25:信用贷)")
//			private Integer businessType;
//			/**
//			 * 实还日期
//			 */
//			@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//			@TableField("fact_repay_date")
//			@ApiModelProperty(required= true,value = "实还日期")
//			private Date factRepayDate;
//			/**
//			 * 借款人
//			 */
//			@TableField("customer_name")
//			@ApiModelProperty(required= true,value = "借款人")
//			private String customerName;
//			/**
//			 * 分公司
//			 */
//			@TableField("company_name")
//			@ApiModelProperty(required= true,value = "分公司")
//			private String companyName;
//			/**
//			 * 还款来源，1:线下转账,2:第三方代扣,3:银行代扣,4:APP网关充值,5:协议代扣
//			 */
//			@TableField("repay_source")
//			@ApiModelProperty(required= true,value = "还款来源，1:线下转账,2:第三方代扣,3:银行代扣,4:APP网关充值,5:协议代扣")
//			private Integer repaySource;
//			/**
//			 * 资产端期数
//			 */
//			@TableField("after_id")
//			@ApiModelProperty(required= true,value = "资产端期数")
//			private String afterId;
//			/**
//			 * 财务确认时间或成功代扣时间
//			 */
//			@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//			@TableField("confirm_time")
//			@ApiModelProperty(required= true,value = "财务确认时间或成功代扣时间")
//			private Date confirmTime;
//			/**
//			 * (代充值资金分发接口参数)团贷网用户唯一编号
//			 */
//			@TableField("td_user_id")
//			@ApiModelProperty(required= true,value = "(代充值资金分发接口参数)团贷网用户唯一编号")
//			private String tdUserId;
//			/**
//			 * (代充值资金分发接口参数)批次分发总金额(保留两位小数)
//			 */
//			@TableField("total_amount")
//			@ApiModelProperty(required= true,value = "(代充值资金分发接口参数)批次分发总金额(保留两位小数)")
//			private BigDecimal totalAmount;
//			/**
//			 * (代充值资金分发接口参数)分发批次号(批次唯一标识)
//			 */
//			@TableField("batch_id")
//			@ApiModelProperty(required= true,value = "(代充值资金分发接口参数)分发批次号(批次唯一标识)")
//			private String batchId;
//			/**
//			 * 本次还款对应团贷网的期次
//			 */
//			@ApiModelProperty(required= true,value = "本次还款对应团贷网的期次")
//			private Integer period;
//			/**
//			 * (代充值资金分发接口参数)出款方帐号
//			 */
//			@TableField("user_id")
//			@ApiModelProperty(required= true,value = "(代充值资金分发接口参数)出款方帐号")
//			private String userId;
//			/**
//			 * (代充值资金分发接口参数)充值金额
//			 */
//			@TableField("recharge_amount")
//			@ApiModelProperty(required= true,value = "(代充值资金分发接口参数)充值金额")
//			private BigDecimal rechargeAmount;
//			/**
//			 * 标记当前资金分发操作是否为结清操作，0：非结清，10：正常结清，11：逾期结清，20：展期原标结清，30：坏账结清
//			 */
//			@TableField("settle_type")
//			@ApiModelProperty(required= true,value = "标记当前资金分发操作是否为结清操作，0：非结清，10：正常结清，11：逾期结清，20：展期原标结清，30：坏账结清")
//			private Integer settleType;
//			/**
//			 * 实收总金额
//			 */
//			@TableField("fact_repay_amount")
//			@ApiModelProperty(required= true,value = "实收总金额")
//			private BigDecimal factRepayAmount;
//			/**
//			 * 流水合计
//			 */
//			@TableField("resource_amount")
//			@ApiModelProperty(required= true,value = "流水合计")
//			private BigDecimal resourceAmount;
//			/**
//			 * 0：只垫付本息，1：全额垫付
//			 */
//			@TableField("advance_type")
//			@ApiModelProperty(required= true,value = "0：只垫付本息，1：全额垫付")
//			private Integer advanceType;
//			/**
//			 * 当期结清状态 0：未结清,1：已结清（目前一点调还垫付接口时使用）
//			 */
//			@TableField("is_complete")
//			@ApiModelProperty(required= true,value = "当期结清状态 0：未结清,1：已结清（目前一点调还垫付接口时使用）")
//			private Integer isComplete;
//			/**
//			 * 标的还款计划列表ID（实还流水ID）
//			 */
//			@TableField("proj_plan_list_id")
//			@ApiModelProperty(required= true,value = "标的还款计划列表ID（实还流水ID）")
//			private String projPlanListId;
//			/**
//			 * (代充值资金分发接口参数)订单唯一标识
//			 */
//			@TableField("request_no")
//			@ApiModelProperty(required= true,value = "(代充值资金分发接口参数)订单唯一标识")
//			private String requestNo;
//			/**
//			 * (代充值资金分发接口参数)客户端IP
//			 */
//			@TableField("user_ip")
//			@ApiModelProperty(required= true,value = "(代充值资金分发接口参数)客户端IP")
//			private String userIp;
//			/**
//			 * (代充值资金分发接口参数)资产端账户唯一编号
//			 */
//			@TableField("oid_partner")
//			@ApiModelProperty(required= true,value = "(代充值资金分发接口参数)资产端账户唯一编号")
//			private String oidPartner;
//			/**
//			 * 平台状态 1：已还款，2：逾期 3：待还款
//			 */
//			@TableField("plat_status")
//			@ApiModelProperty(required= true,value = "平台状态 1：已还款，2：逾期 3：待还款")
//			private String platStatus;
//			/**
//			 * 平台还款状态(1：待还款，2：已还款 3、已垫付 4：逾期)
//			 */
//			@TableField("platform_repay_status")
//			@ApiModelProperty(required= true,value = "平台还款状态(1：待还款，2：已还款 3、已垫付 4：逾期)")
//			private Integer platformRepayStatus;
//			/**
//			 * 分发状态（0：待分发，1：分发处理中，2：分发成功，3，分发失败）
//			 */
//			@TableField("process_status")
//			@ApiModelProperty(required= true,value = "分发状态（0：待分发，1：分发处理中，2：分发成功，3，分发失败）")
//			private Integer processStatus;
//			/**
//			 * 资产端对团贷网通用合规化还款流程处理状态(0:未处理,1:处理中,2:成功,3:失败)
//			 */
//			@ApiModelProperty(required= true,value = "资产端对团贷网通用合规化还款流程处理状态(0:未处理,1:处理中,2:成功,3:失败)")
//			private Integer status;
//			/**
//			 * 备注
//			 */
//			@ApiModelProperty(required= true,value = "备注")
//			private String remark;
//			/**
//			 * 创建人
//			 */
//			@TableField("create_user")
//			@ApiModelProperty(required= true,value = "创建人")
//			private String createUser;
//			/**
//			 * 创建时间
//			 */
//			@TableField("create_time")
//			@ApiModelProperty(required= true,value = "创建时间")
//			private Date createTime;
//			/**
//			 * 更新人
//			 */
//			@TableField("update_user")
//			@ApiModelProperty(required= true,value = "更新人")
//			private String updateUser;
//			/**
//			 * 更新时间
//			 */
//			@TableField("update_time")
//			@ApiModelProperty(required= true,value = "更新时间")
//			private Date updateTime;


			return Result.success(departmentBankService.listDepartmentBank());
		} catch (Exception e) {
			LOG.error("获取所有的线下还款账户失败", e);
			return Result.error("500", "获取所有的线下还款账户失败！");
		}
	}

	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "查询代充值账户余额")
	@GetMapping("/queryUserAviMoney")
	@ResponseBody
	public Result queryUserAviMoney(@RequestParam("rechargeAccountType") String rechargeAccountType) {

		Map<String, Object> paramMap = new HashMap<>();

		String userId = tdrepayRechargeService.handleAccountType(rechargeAccountType);

		paramMap.put("userId", userId);
		
		com.ht.ussp.core.Result result = null;
		try {
			result = eipRemote.queryUserAviMoney(paramMap);
		} catch (Exception e) {
			return Result.error("500", "调用外联平台查询代充值账户余额接口失败！");
		}
		
		return Result.success(result);
	}

	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "提交充值数据")
	@PostMapping("/commitRechargeData")
	@ResponseBody
	public Result commitRechargeData(@RequestBody RechargeModalVO vo) {

		if (vo == null) {
			return Result.error("500", "不能空数据！");
		}

		String clientIp = CommonUtil.getClientIp();

		String rechargeAccountType = vo.getRechargeAccountType();

		String rechargeUserId = tdrepayRechargeService.handleAccountType(rechargeAccountType);

		if (StringUtil.isEmpty(rechargeUserId)) {
			return Result.error("500", "没有找到代充值账户用户ID");
		}

		String cmOrderNo = UUID.randomUUID().toString();

		RechargeModalDTO dto = new RechargeModalDTO();
		dto.setAmount(vo.getRechargeAmount());
		// 若转账类型为1 对公，则在银行编码后 + "2B"
		dto.setBankCode("1".equals(vo.getTransferType()) ? vo.getBankCode() + "2B" : vo.getBankCode());
		dto.setClientIp(clientIp);
		dto.setChargeType("3"); // 1：网关、2：快捷、3：代充值
		dto.setCmOrderNo(cmOrderNo);
		String oIdPartner = tdrepayRechargeService.handleOIdPartner(rechargeAccountType);
		dto.setoIdPartner(oIdPartner);
		dto.setRechargeUserId(rechargeUserId);

		
		AgencyRechargeLog agencyRechargeLog = handleAgencyRechargeLog(vo, clientIp, cmOrderNo, dto, oIdPartner);
		
		agencyRechargeLogService.insert(agencyRechargeLog);
		
		com.ht.ussp.core.Result result = null;
		
		try {
			
			result = eipRemote.agencyRecharge(dto);

			if (result == null) {
				return Result.error("500", "接口调用失败！");
			}
		} catch (Exception e) {
			agencyRechargeLog.setResultJson(e.getMessage());
			handleUpdateLogFail(cmOrderNo, agencyRechargeLog);
			LOG.error("提交充值数据失败", e);
			return Result.error("500", "提交充值数据失败！");
		}		
		
		try {
			if ("0000".equals(result.getReturnCode())) {
				
				agencyRechargeLog.setResultJson(JSONObject.toJSONString(result));
				agencyRechargeLog.setUpdateTime(new Date());
				agencyRechargeLog.setUpdateUser(loginUserInfoHelper.getUserId());
				
				agencyRechargeLogService.update(agencyRechargeLog,
						new EntityWrapper<AgencyRechargeLog>().eq("cm_order_no", cmOrderNo));
				return Result.success(result);
			} else {
				agencyRechargeLog.setResultJson(JSONObject.toJSONString(result));
				handleUpdateLogFail(cmOrderNo, agencyRechargeLog);
				return Result.error("500", result.getCodeDesc());
			}
		} catch (Exception e) {
			agencyRechargeLog.setResultJson(e.getMessage());
			handleUpdateLogFail(cmOrderNo, agencyRechargeLog);
			LOG.error("提交充值数据失败", e);
			return Result.error("500", "提交充值数据失败！");
		}

	}

	private void handleUpdateLogFail(String cmOrderNo, AgencyRechargeLog agencyRechargeLog) {
		agencyRechargeLog.setUpdateTime(new Date());
		agencyRechargeLog.setUpdateUser(loginUserInfoHelper.getUserId());
		agencyRechargeLog.setHandleStatus("3");
		agencyRechargeLogService.update(agencyRechargeLog,
				new EntityWrapper<AgencyRechargeLog>().eq("cm_order_no", cmOrderNo));
	}

	private AgencyRechargeLog handleAgencyRechargeLog(RechargeModalVO vo, String clientIp, String cmOrderNo,
			RechargeModalDTO dto, String oIdPartner) {
		AgencyRechargeLog agencyRechargeLog = new AgencyRechargeLog();
		agencyRechargeLog.setParamJson(JSONObject.toJSONString(vo) + "|" + JSONObject.toJSONString(dto));
		agencyRechargeLog.setCmOrderNo(cmOrderNo);
		agencyRechargeLog.setBankCode(vo.getBankCode());
		agencyRechargeLog.setChargeType(dto.getChargeType());
		agencyRechargeLog.setClientIp(clientIp);
		agencyRechargeLog.setoIdPartner(dto.getoIdPartner());
		agencyRechargeLog.setRechargeAccountBalance(
				BigDecimal.valueOf(vo.getRechargeAccountBalance() == null ? 0 : vo.getRechargeAccountBalance()));
		agencyRechargeLog.setRechargeAccountType(vo.getRechargeAccountType());
		agencyRechargeLog
				.setRechargeAmount(BigDecimal.valueOf(vo.getRechargeAmount() == null ? 0 : vo.getRechargeAmount()));
		agencyRechargeLog.setRechargeSourseAccount(vo.getRechargeSourseAccount());
		agencyRechargeLog.setRechargeUserId(dto.getRechargeUserId());
		agencyRechargeLog.setTransferType(vo.getTransferType());
		agencyRechargeLog.setRechargeAccountType(vo.getRechargeAccountType());
		agencyRechargeLog.setCreateTime(new Date());
		agencyRechargeLog.setCreateUser(loginUserInfoHelper.getUserId());
		agencyRechargeLog.setoIdPartner(oIdPartner);
		return agencyRechargeLog;
	}

	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "查询充值订单")
	@GetMapping("/queryRechargeOrder")
	@ResponseBody
	public Result queryRechargeOrder(@RequestParam("oidPartner") String oidPartner,
			@RequestParam("cmOrderNo") String cmOrderNo) {
		try {
			if (StringUtil.isEmpty(cmOrderNo) || StringUtil.isEmpty(oidPartner)) {
				return Result.error("500", "订单号或资产端唯一编号不能为空");
			}
			agencyRechargeLogService.queryRechargeOrder(oidPartner, cmOrderNo, loginUserInfoHelper.getUserId());
			return Result.success();
		} catch (Exception e) {
			LOG.error("查询充值订单失败", e);
			return Result.error("500", "查询充值订单失败！");
		}
	}
}
