package com.hongte.alms.base.service.impl;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.RepaymentBizPlan;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.SysMsgTemplate;
import com.hongte.alms.base.entity.TuandaiProjectInfo;
import com.hongte.alms.base.enums.MsgCodeEnum;
import com.hongte.alms.base.feignClient.MsgRemote;
import com.hongte.alms.base.feignClient.dto.MsgRequestDto;
import com.hongte.alms.base.service.RepaymentBizPlanService;
import com.hongte.alms.base.service.RepaymentProjPlanService;
import com.hongte.alms.base.service.SendMessageService;
import com.hongte.alms.base.service.SysMsgTemplateService;
import com.hongte.alms.base.service.TuandaiProjectInfoService;
import com.hongte.alms.base.vo.withhold.RepaymentBizPlanListVo;
import com.hongte.alms.common.util.DateUtil;

/**
 * <p>
 * 发送短息和消息推送 服务实现类
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-02-02
 */
@Service("SendMessageService")
public class SendMessageServiceImpl implements SendMessageService {
	private Logger logger = LoggerFactory.getLogger(SendMessageServiceImpl.class);

	
	@Autowired
    MsgRemote msgRemote;
	
	@Autowired
	@Qualifier("SysMsgTemplateService")
	SysMsgTemplateService sysMsgTemplateService;
	
	
	@Autowired
	@Qualifier("TuandaiProjectInfoService")
	TuandaiProjectInfoService tuandaiProjectInfoService;
	
	@Autowired
	@Qualifier("RepaymentProjPlanService")
	RepaymentProjPlanService repaymentProjPlanService;
	
	@Autowired
	@Qualifier("RepaymentBizPlanService")
	RepaymentBizPlanService repaymentBizPlanService;
	
	@Override
	public void sendAfterRepaySuccessSms(String phone,String name, Date date, BigDecimal borrowAmount, Integer period,
			BigDecimal repayAmount, BigDecimal factRepayAmount) {
		
		
	
		String templateCode=MsgCodeEnum.AFTER_REPAY_SUCCESS.getValue();
		SysMsgTemplate sysMsgTemplate=sysMsgTemplateService.selectOne(new EntityWrapper<SysMsgTemplate>().eq("template_code", templateCode));

	
		if(sysMsgTemplate==null) {
			logger.info("扣款成功短信通知(贷后)短信模板为空");
			return;
		}
		Long msgModeId=Long.valueOf(sysMsgTemplate.getTemplateId());
		MsgRequestDto dto=new MsgRequestDto();
		dto.setApp("alms");
		dto.setMsgTitle("扣款成功短信通知(贷后)");
		dto.setMsgModelId(msgModeId);
		dto.setMsgTo(phone);
		//组装发送短信内容的Json数据
		JSONObject data = new JSONObject() ;
		data.put("name", name);
		data.put("date",  DateUtil.getChinaDay(date));
		data.put("period", period);
		data.put("repayAmount", repayAmount);
		data.put("borrowAmount", borrowAmount);
		data.put("factRepayAmount", factRepayAmount);
		dto.setMsgBody(data);
		String jason=JSON.toJSONString(dto);
		msgRemote.sendRequest(jason);
		
	}

	@Override
	public void sendAfterRepayFailSms(String phone,String name, Date date, BigDecimal borrowAmount, Integer period) {
		String templateCode=MsgCodeEnum.AFTER_REPAY_FAIL.getValue();
		SysMsgTemplate sysMsgTemplate=sysMsgTemplateService.selectOne(new EntityWrapper<SysMsgTemplate>().eq("template_code", templateCode));

	
		if(sysMsgTemplate==null) {
			logger.info("扣款失败短信通知(贷后)短信模板为空");
			return;
		}
		Long msgModeId=Long.valueOf(sysMsgTemplate.getTemplateId());
		MsgRequestDto dto=new MsgRequestDto();
		dto.setApp("alms");
		dto.setMsgTitle("扣款失败短信通知(贷后)");
		dto.setMsgModelId(msgModeId);
		dto.setMsgTo(phone);
		//组装发送短信内容的Json数据
		JSONObject data = new JSONObject() ;
		data.put("name", name);
		data.put("date",  DateUtil.getChinaDay(date));
		data.put("period", period);
		data.put("borrowAmount", borrowAmount);
		dto.setMsgBody(data);
		String jason=JSON.toJSONString(dto);
		msgRemote.sendRequest(jason);
		
		
	}



	@Override
	public void sendAfterUnbondMutipleRepayRemindSms(String phone, String name, List<RepaymentBizPlanList> pLists) {
		String templateCode = MsgCodeEnum.ATER_UNBOND_MUTIPLE_REPAY_REMIND.getValue();
		SysMsgTemplate sysMsgTemplate = sysMsgTemplateService
				.selectOne(new EntityWrapper<SysMsgTemplate>().eq("template_code", templateCode));

		if (sysMsgTemplate == null) {
			logger.info("多笔还款提醒（未绑卡用户)   短信模板为空");
			return;
		}
		TuandaiProjectInfo tuandaiProjectInfo = tuandaiProjectInfoService.selectOne(
				new EntityWrapper<TuandaiProjectInfo>().eq("business_id", pLists.get(0).getOrigBusinessId()));
		Date borrowDate = null;
		if (tuandaiProjectInfo.getQueryFullSuccessDate() != null) {
			borrowDate = tuandaiProjectInfo.getQueryFullSuccessDate();
		} else {
			borrowDate = tuandaiProjectInfo.getCreateTime();
		}
		RepaymentBizPlanListVo vo = null;
		List<RepaymentBizPlanListVo> pListVos = new ArrayList();
		BigDecimal totalAmount = BigDecimal.valueOf(0);// 合计应还金额
		Date dueDate = new Date();
		for (RepaymentBizPlanList pList : pLists) {
			vo = new RepaymentBizPlanListVo(pList);
			vo.setRepayAmount(vo.getTotalBorrowAmount()
					.add(vo.getOverdueAmount() == null ? BigDecimal.valueOf(0) : vo.getOverdueAmount()));
			totalAmount.add(vo.getRepayAmount());
		}
		Long msgModeId = Long.valueOf(sysMsgTemplate.getTemplateId());
		MsgRequestDto dto = new MsgRequestDto();
		dto.setApp("alms");
		dto.setMsgTitle("多笔还款提醒（未绑卡用户)");
		dto.setMsgModelId(msgModeId);
		dto.setMsgTo(phone);
		// 组装发送短信内容的Json数据
		JSONObject data = new JSONObject();
		data.put("name", name);
		data.put("list", pListVos);
		data.put("totalAmount", totalAmount);
		data.put("dueDate", DateUtil.getChinaDay(dueDate));
		dto.setMsgBody(data);
		String jason = JSON.toJSONString(dto);
		msgRemote.sendRequest(jason);
	}

	@Override
	public void sendAfterBindingMutipleRepayRemindSms(String phone,String name,List<RepaymentBizPlanList> pLists,String tailNumber) {
		String templateCode = MsgCodeEnum.ATER_BINDING_MUTIPLE_REPAY_REMIND.getValue();
		SysMsgTemplate sysMsgTemplate = sysMsgTemplateService
				.selectOne(new EntityWrapper<SysMsgTemplate>().eq("template_code", templateCode));

		if (sysMsgTemplate == null) {
			logger.info("多笔还款提醒（已绑卡用户)   短信模板为空");
			return;
		}
		TuandaiProjectInfo tuandaiProjectInfo = tuandaiProjectInfoService.selectOne(
				new EntityWrapper<TuandaiProjectInfo>().eq("business_id", pLists.get(0).getOrigBusinessId()));
		Date borrowDate = null;
		if (tuandaiProjectInfo.getQueryFullSuccessDate() != null) {
			borrowDate = tuandaiProjectInfo.getQueryFullSuccessDate();
		} else {
			borrowDate = tuandaiProjectInfo.getCreateTime();
		}
		RepaymentBizPlanListVo vo = null;
		List<RepaymentBizPlanListVo> pListVos = new ArrayList();
		BigDecimal totalAmount = BigDecimal.valueOf(0);// 合计应还金额
		Date dueDate = new Date();
		for (int i=0;i<pLists.size();i++) {
			vo = new RepaymentBizPlanListVo(pLists.get(0));
			RepaymentBizPlan plan=repaymentBizPlanService.selectOne(new EntityWrapper<RepaymentBizPlan>().eq("plan_id", pLists.get(0).getPlanId()));
			vo.setPeriod(i+1);
			vo.setDate(DateUtil.getChinaDay(borrowDate));
			vo.setBorrowAmount(plan.getBorrowMoney());
			vo.setRepayAmount(vo.getTotalBorrowAmount()
					.add(vo.getOverdueAmount() == null ? BigDecimal.valueOf(0) : vo.getOverdueAmount()));
			totalAmount=totalAmount.add(vo.getRepayAmount());
			pListVos.add(vo);
		}
		Long msgModeId = Long.valueOf(sysMsgTemplate.getTemplateId());
		MsgRequestDto dto = new MsgRequestDto();
		dto.setApp("alms");
		dto.setMsgTitle("多笔还款提醒（未绑卡用户)");
		dto.setMsgModelId(msgModeId);
		dto.setMsgTo(phone);
		// 组装发送短信内容的Json数据
		JSONObject data = new JSONObject();
		data.put("name", name);
		data.put("list", pListVos);
		data.put("totalAmount", totalAmount);
		data.put("dueDate", DateUtil.getChinaDay(pLists.get(0).getDueDate()));
		data.put("tailNumber", tailNumber);
		dto.setMsgBody(data);
		String jason = JSON.toJSONString(dto);
		msgRemote.sendRequest(jason);
	}

	@Override
	public void sendAfterUnbondRepayRemindSms(String phone, String name, Date date, BigDecimal borrowAmount,
			BigDecimal repayAmount, Integer period, Date dueDate) {
		String templateCode=MsgCodeEnum.AFTER_UNBOND_REPAY_REMIND.getValue();
		SysMsgTemplate sysMsgTemplate=sysMsgTemplateService.selectOne(new EntityWrapper<SysMsgTemplate>().eq("template_code", templateCode));

	
		if(sysMsgTemplate==null) {
			logger.info("还款提醒 （未绑卡用户），单笔还款，还款日前7天/1天提醒   短信模板为空");
			return;
		}
		Long msgModeId=Long.valueOf(sysMsgTemplate.getTemplateId());
		MsgRequestDto dto=new MsgRequestDto();
		dto.setApp("alms");
		dto.setMsgTitle("还款提醒 （未绑卡用户），单笔还款，还款日前7天/1天提醒");
		dto.setMsgModelId(msgModeId);
		dto.setMsgTo(phone);
		//组装发送短信内容的Json数据
		JSONObject data = new JSONObject() ;
		data.put("name", name);
		data.put("date",  DateUtil.getChinaDay(date));
		data.put("borrowAmount", borrowAmount);
		data.put("repayAmount", repayAmount);
		data.put("period", period);
		data.put("dueDate", dueDate);
		dto.setMsgBody(data);
		String jason=JSON.toJSONString(dto);
		msgRemote.sendRequest(jason);
		
	}

	@Override
	public void sendAfterUnbondSettleRemindSms(String phone, String name, Date date, BigDecimal borrowAmount,
			Date dueDate) {
		String templateCode=MsgCodeEnum.AFTER_UNBOND_SETTLE_REMIND.getValue();
		SysMsgTemplate sysMsgTemplate=sysMsgTemplateService.selectOne(new EntityWrapper<SysMsgTemplate>().eq("template_code", templateCode));

	
		if(sysMsgTemplate==null) {
			logger.info("结清提醒（未绑卡用户）   短信模板为空");
			return;
		}
		Long msgModeId=Long.valueOf(sysMsgTemplate.getTemplateId());
		MsgRequestDto dto=new MsgRequestDto();
		dto.setApp("alms");
		dto.setMsgTitle("结清提醒（未绑卡用户）");
		dto.setMsgModelId(msgModeId);
		dto.setMsgTo(phone);
		//组装发送短信内容的Json数据
		JSONObject data = new JSONObject() ;
		data.put("name", name);
		data.put("date",  DateUtil.getChinaDay(date));
		data.put("borrowAmount", borrowAmount);
		data.put("dueDate", DateUtil.getChinaDay(dueDate));
		dto.setMsgBody(data);
		String jason=JSON.toJSONString(dto);
		msgRemote.sendRequest(jason);		
	}

	@Override
	public void sendAfterBindingRepayRemindSms(String phone, String name, Date date, BigDecimal borrowAmount,
			BigDecimal repayAmount, Integer period, Date dueDate,String tailNum) {
		String templateCode=MsgCodeEnum.AFTER_BINDING_REPAY_REMIND.getValue();
		SysMsgTemplate sysMsgTemplate=sysMsgTemplateService.selectOne(new EntityWrapper<SysMsgTemplate>().eq("template_code", templateCode));

	
		if(sysMsgTemplate==null) {
			logger.info("还款提醒（已绑卡用户）单笔还款，还款日前7天/1天提醒   短信模板为空");
			return;
		}
		Long msgModeId=Long.valueOf(sysMsgTemplate.getTemplateId());
		MsgRequestDto dto=new MsgRequestDto();
		dto.setApp("alms");
		dto.setMsgTitle("还款提醒（已绑卡用户）单笔还款，还款日前7天/1天提醒");
		dto.setMsgModelId(msgModeId);
		dto.setMsgTo(phone);
		//组装发送短信内容的Json数据
		JSONObject data = new JSONObject() ;
		data.put("name", name);
		data.put("date",  DateUtil.getChinaDay(date));
		data.put("borrowAmount", borrowAmount);
		data.put("dueDate", DateUtil.getChinaDay(dueDate));
		data.put("repayAmount",repayAmount);
		data.put("period", period);
		data.put("tailNumber", tailNum);
		dto.setMsgBody(data);
		String jason=JSON.toJSONString(dto);
		msgRemote.sendRequest(jason);				
	}

	@Override
	public void sendAfterBindingSettleRemindSms(String phone, String name, Date date, BigDecimal borrowAmount,
			Date dueDate,String tailNum) {
		String templateCode=MsgCodeEnum.AFTER_BINDING_SETTLE_REMIND.getValue();
		SysMsgTemplate sysMsgTemplate=sysMsgTemplateService.selectOne(new EntityWrapper<SysMsgTemplate>().eq("template_code", templateCode));

	
		if(sysMsgTemplate==null) {
			logger.info("还款提醒（已绑卡用户），结清提醒，提前15天/1天提醒   短信模板为空");
			return;
		}
		Long msgModeId=Long.valueOf(sysMsgTemplate.getTemplateId());
		MsgRequestDto dto=new MsgRequestDto();
		dto.setApp("alms");
		dto.setMsgTitle("还款提醒（已绑卡用户），结清提醒，提前15天/1天提醒");
		dto.setMsgModelId(msgModeId);
		dto.setMsgTo(phone);
		//组装发送短信内容的Json数据
		JSONObject data = new JSONObject() ;
		data.put("name", name);
		data.put("date",  DateUtil.getChinaDay(date));
		data.put("borrowAmount", borrowAmount);
		data.put("dueDate", DateUtil.getChinaDay(dueDate));
		data.put("tailNumber", tailNum);
		dto.setMsgBody(data);
		String jason=JSON.toJSONString(dto);
		msgRemote.sendRequest(jason);	
		
	}

	@Override
	public void sendAfterOverdueRemindSms(String phone, String name, Integer period, Integer totalPeriods) {
		String templateCode=MsgCodeEnum.AFTER_OVERDUE_REMIND.getValue();
		SysMsgTemplate sysMsgTemplate=sysMsgTemplateService.selectOne(new EntityWrapper<SysMsgTemplate>().eq("template_code", templateCode));

	
		if(sysMsgTemplate==null) {
			logger.info("贷后逾期提醒（逾期的第1~3天）   短信模板为空");
			return;
		}
		Long msgModeId=Long.valueOf(sysMsgTemplate.getTemplateId());
		MsgRequestDto dto=new MsgRequestDto();
		dto.setApp("alms");
		dto.setMsgTitle("贷后逾期提醒（逾期的第1~3天）");
		dto.setMsgModelId(msgModeId);
		dto.setMsgTo(phone);
		//组装发送短信内容的Json数据
		JSONObject data = new JSONObject() ;
		data.put("name", name);
		data.put("period",  period);
		data.put("totalPeriods", totalPeriods);
		dto.setMsgBody(data);
		String jason=JSON.toJSONString(dto);
		msgRemote.sendRequest(jason);			
	}


	

	
}
