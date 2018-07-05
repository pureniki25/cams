package com.hongte.alms.base.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.dto.PlatformRepaymentDto;
import com.hongte.alms.base.dto.PlatformRepaymentReq;
import com.hongte.alms.base.entity.AccountantOverRepayLog;
import com.hongte.alms.base.entity.RepaymentBizPlan;
import com.hongte.alms.base.entity.RepaymentBizPlanBak;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentBizPlanListBak;
import com.hongte.alms.base.entity.RepaymentBizPlanListDetail;
import com.hongte.alms.base.entity.RepaymentBizPlanListDetailBak;
import com.hongte.alms.base.entity.RepaymentConfirmLog;
import com.hongte.alms.base.entity.RepaymentProjFactRepay;
import com.hongte.alms.base.entity.RepaymentProjPlan;
import com.hongte.alms.base.entity.RepaymentProjPlanBak;
import com.hongte.alms.base.entity.RepaymentProjPlanList;
import com.hongte.alms.base.entity.RepaymentProjPlanListBak;
import com.hongte.alms.base.entity.RepaymentProjPlanListDetail;
import com.hongte.alms.base.entity.RepaymentProjPlanListDetailBak;
import com.hongte.alms.base.entity.RepaymentResource;
import com.hongte.alms.base.enums.RepayedFlag;
import com.hongte.alms.base.enums.repayPlan.RepayPlanFeeTypeEnum;
import com.hongte.alms.base.enums.repayPlan.SectionRepayStatusEnum;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.feignClient.PlatformRepaymentFeignClient;
import com.hongte.alms.base.mapper.AccountantOverRepayLogMapper;
import com.hongte.alms.base.mapper.RepaymentBizPlanBakMapper;
import com.hongte.alms.base.mapper.RepaymentBizPlanListBakMapper;
import com.hongte.alms.base.mapper.RepaymentBizPlanListDetailBakMapper;
import com.hongte.alms.base.mapper.RepaymentBizPlanListDetailMapper;
import com.hongte.alms.base.mapper.RepaymentBizPlanListMapper;
import com.hongte.alms.base.mapper.RepaymentBizPlanMapper;
import com.hongte.alms.base.mapper.RepaymentConfirmLogMapper;
import com.hongte.alms.base.mapper.RepaymentProjFactRepayMapper;
import com.hongte.alms.base.mapper.RepaymentProjPlanBakMapper;
import com.hongte.alms.base.mapper.RepaymentProjPlanListBakMapper;
import com.hongte.alms.base.mapper.RepaymentProjPlanListDetailBakMapper;
import com.hongte.alms.base.mapper.RepaymentProjPlanListDetailMapper;
import com.hongte.alms.base.mapper.RepaymentProjPlanListMapper;
import com.hongte.alms.base.mapper.RepaymentProjPlanMapper;
import com.hongte.alms.base.mapper.RepaymentResourceMapper;
import com.hongte.alms.base.service.MoneyPoolService;
import com.hongte.alms.base.service.RepaymentConfirmLogService;
import com.hongte.alms.base.vo.finance.CurrPeriodProjDetailVO;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.DateUtil;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * 还款确认日志记录表 服务实现类
 * </p>
 *
 * @author 王继光
 * @since 2018-05-25
 */
@Service("RepaymentConfirmLogService")
public class RepaymentConfirmLogServiceImpl extends BaseServiceImpl<RepaymentConfirmLogMapper, RepaymentConfirmLog> implements RepaymentConfirmLogService {

    private static Logger logger = LoggerFactory.getLogger(RepaymentConfirmLogServiceImpl.class);
    @Autowired
    RepaymentConfirmLogMapper confirmLogMapper;
    @Autowired
    RepaymentProjFactRepayMapper repaymentProjFactRepayMapper;

    @Autowired
    RepaymentBizPlanMapper repaymentBizPlanMapper;
    @Autowired
    RepaymentBizPlanListMapper repaymentBizPlanListMapper;
    @Autowired
    RepaymentBizPlanListDetailMapper repaymentBizPlanListDetailMapper;

    @Autowired
    RepaymentProjPlanMapper repaymentProjPlanMapper;
    @Autowired
    RepaymentProjPlanListMapper repaymentProjPlanListMapper;
    @Autowired
    RepaymentProjPlanListDetailMapper repaymentProjPlanListDetailMapper;

    @Autowired
    RepaymentBizPlanBakMapper repaymentBizPlanBakMapper;
    @Autowired
    RepaymentBizPlanListBakMapper repaymentBizPlanListBakMapper;
    @Autowired
    RepaymentBizPlanListDetailBakMapper repaymentBizPlanListDetailBakMapper;

    @Autowired
    RepaymentProjPlanBakMapper repaymentProjPlanBakMapper;
    @Autowired
    RepaymentProjPlanListBakMapper repaymentProjPlanListBakMapper;
    @Autowired
    RepaymentProjPlanListDetailBakMapper repaymentProjPlanListDetailBakMapper;

    @Autowired
    RepaymentResourceMapper repaymentResourceMapper;
    @Autowired
    @Qualifier("MoneyPoolService")
    MoneyPoolService moneyPoolService;
    @Autowired
    AccountantOverRepayLogMapper accountantOverRepayLogMapper;

    @Autowired
    private PlatformRepaymentFeignClient platformRepaymentFeignClient;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result revokeConfirm(String businessId, String afterId) throws Exception{
        /*找还款确认记录*/
        List<RepaymentConfirmLog> logs = confirmLogMapper.selectList(new EntityWrapper<RepaymentConfirmLog>().eq("business_id", businessId).eq("after_id", afterId).orderBy("`idx`", false));
        if (logs == null || logs.size() == 0) {
            return Result.error("500", "找不到任何一条相关的确认还款记录");
        }
        RepaymentConfirmLog log = logs.get(0);
        if(log.getRepaySource()!=10) {//如果不是线下转账的不能撤销
            return Result.error("500", "最后一次还款不是线下转账不能被撤销");
        }

        if (log.getCanRevoke().equals(0)) {
            return Result.error("500", "该还款记录不能被撤销");
        }


        List<RepaymentBizPlanList> repaymentBizPlanLists = repaymentBizPlanListMapper.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("business_id", businessId).eq("after_id", afterId));


		/*找实还明细记录*/
        List<RepaymentProjFactRepay> factRepays = repaymentProjFactRepayMapper
                .selectList(new EntityWrapper<RepaymentProjFactRepay>().eq("confirm_log_id", log.getConfirmLogId())
                        .orderBy("proj_plan_list_id"));

            PlatformRepaymentReq platformRepaymentReq=new PlatformRepaymentReq();
        if (factRepays != null) {
            RepaymentProjFactRepay repaymentProjFactRepay = factRepays.get(0);
            RepaymentBizPlanList repaymentBizPlanList= repaymentBizPlanLists.get(0);

            platformRepaymentReq.setProjectId(repaymentProjFactRepay.getProjectId());
            platformRepaymentReq.setConfirmLogId(repaymentBizPlanList.getPlanListId());
            Result<List<PlatformRepaymentDto>> listResult = platformRepaymentFeignClient.queryTdrepayRechargeRecord(platformRepaymentReq);


            logger.info("=========查询是否有资金分发结果{}",JSON.toJSONString(listResult));
            if("1".equals(listResult.getCode())){
                List<PlatformRepaymentDto> data = listResult.getData();
                if(!CollectionUtils.isEmpty(data)){
                    for(PlatformRepaymentDto platformRepaymentDto : data){
                        if(repaymentBizPlanList.getPlanListId().equals(platformRepaymentDto.getConfirmLogId())){ //planlistId相等
                            if(platformRepaymentDto.getProcessStatus()==1 || platformRepaymentDto.getProcessStatus()==2){
                                throw new ServiceRuntimeException("已分发记录不能被撤销");
                            }
                        }
                    }
                }
            }
        }
		/*找结余记录*/
        if (log.getSurplusRefId() != null) {
            AccountantOverRepayLog accountantOverRepayLog = accountantOverRepayLogMapper
                    .selectById(log.getSurplusRefId());
            if (accountantOverRepayLog != null) {
                accountantOverRepayLog.deleteById();
            }
        }

        if (log.getSurplusUseRefId() != null) {
            AccountantOverRepayLog accountantOverRepayLog = accountantOverRepayLogMapper
                    .selectById(log.getSurplusUseRefId());
            if (accountantOverRepayLog != null) {
                accountantOverRepayLog.deleteById();
            }
        }

        for (RepaymentProjFactRepay factRepay : factRepays) {
            RepaymentResource resource = new RepaymentResource();
            resource.setResourceId(factRepay.getRepaySourceId());
			/*找还款来源记录*/
            resource = repaymentResourceMapper.selectOne(resource);
			/*撤销银行流水与财务登记关联*/
            moneyPoolService.revokeConfirmRepaidUpdateMoneyPool(resource);
			
			/*删除实还明细记录*/
//			factRepay.deleteById();
            repaymentProjFactRepayMapper.delete(new EntityWrapper<RepaymentProjFactRepay>().eq("proj_plan_detail_repay_id", factRepay.getProjPlanDetailRepayId()));
        }

        for (RepaymentProjFactRepay factRepay : factRepays) {
            RepaymentResource resource = new RepaymentResource();
            resource.setResourceId(factRepay.getRepaySourceId());
			/*找还款来源记录*/
            resource = repaymentResourceMapper.selectOne(resource);
            if (resource != null) {
				/*删除还款来源记录*/
                resource.deleteById();
            }
        }
		/*根据 confirm_log_id 找还款计划6张表相关的备份记录*/
        List<RepaymentBizPlanBak> selectList = repaymentBizPlanBakMapper.selectList(new EntityWrapper<RepaymentBizPlanBak>().eq("confirm_log_id", log.getConfirmLogId()));
        List<RepaymentBizPlanListBak> selectList2 = repaymentBizPlanListBakMapper.selectList(new EntityWrapper<RepaymentBizPlanListBak>().eq("confirm_log_id", log.getConfirmLogId()));
        List<RepaymentBizPlanListDetailBak> selectList3 = repaymentBizPlanListDetailBakMapper.selectList(new EntityWrapper<RepaymentBizPlanListDetailBak>().eq("confirm_log_id", log.getConfirmLogId()));
        List<RepaymentProjPlanBak> selectList4 = repaymentProjPlanBakMapper.selectList(new EntityWrapper<RepaymentProjPlanBak>().eq("confirm_log_id", log.getConfirmLogId()));
        List<RepaymentProjPlanListBak> selectList5 = repaymentProjPlanListBakMapper.selectList(new EntityWrapper<RepaymentProjPlanListBak>().eq("confirm_log_id", log.getConfirmLogId()));
        List<RepaymentProjPlanListDetailBak> selectList6 = repaymentProjPlanListDetailBakMapper.selectList(new EntityWrapper<RepaymentProjPlanListDetailBak>().eq("confirm_log_id", log.getConfirmLogId()));
		
		/*根据bak的主键找现在的记录,先删除,再新增*/
        for (RepaymentProjPlanListDetailBak repaymentProjPlanListDetailBak : selectList6) {
            RepaymentProjPlanListDetail detail = new RepaymentProjPlanListDetail(repaymentProjPlanListDetailBak);
            repaymentProjPlanListDetailMapper.deleteById(detail.getProjPlanDetailId());
            detail.insert();
            repaymentProjPlanListDetailBak.delete(new EntityWrapper<>()
                    .eq("proj_plan_detail_id", repaymentProjPlanListDetailBak.getProjPlanDetailId())
                    .eq("confirm_log_id", repaymentProjPlanListDetailBak.getConfirmLogId()));
        }

        for (RepaymentProjPlanListBak repaymentProjPlanListBak : selectList5) {
            RepaymentProjPlanList list = new RepaymentProjPlanList(repaymentProjPlanListBak);
            repaymentProjPlanListMapper.deleteById(list.getProjPlanListId());
            list.insert();
            repaymentProjPlanListBak.delete(new EntityWrapper<>()
                    .eq("proj_plan_list_id", repaymentProjPlanListBak.getProjPlanListId())
                    .eq("confirm_log_id", repaymentProjPlanListBak.getConfirmLogId()));
        }

        for (RepaymentProjPlanBak repaymentProjPlanBak : selectList4) {
            RepaymentProjPlan plan = new RepaymentProjPlan(repaymentProjPlanBak);
            repaymentProjPlanMapper.deleteById(plan.getProjPlanId());
            plan.insert();
            repaymentProjPlanBak.delete(new EntityWrapper<>()
                    .eq("proj_plan_id", repaymentProjPlanBak.getProjPlanId())
                    .eq("confirm_log_id", repaymentProjPlanBak.getConfirmLogId()));
        }

        for (RepaymentBizPlanListDetailBak repaymentBizPlanListDetailBak : selectList3) {
            RepaymentBizPlanListDetail detail = new RepaymentBizPlanListDetail(repaymentBizPlanListDetailBak);
            repaymentBizPlanListDetailMapper.deleteById(detail.getPlanDetailId());
            detail.insert();
            repaymentBizPlanListDetailBak.delete(new EntityWrapper<>()
                    .eq("plan_detail_id", repaymentBizPlanListDetailBak.getPlanDetailId())
                    .eq("confirm_log_id", repaymentBizPlanListDetailBak.getConfirmLogId()));
        }

        for (RepaymentBizPlanListBak repaymentBizPlanListBak : selectList2) {
            RepaymentBizPlanList list = new RepaymentBizPlanList(repaymentBizPlanListBak);
            repaymentBizPlanListMapper.deleteById(list.getPlanListId());
            list.insert();
            repaymentBizPlanListBak.delete(new EntityWrapper<>()
                    .eq("plan_list_id", repaymentBizPlanListBak.getPlanListId())
                    .eq("confirm_log_id", repaymentBizPlanListBak.getConfirmLogId()));
        }

        for (RepaymentBizPlanBak repaymentBizPlanBak : selectList) {
            RepaymentBizPlan plan = new RepaymentBizPlan(repaymentBizPlanBak);
            repaymentBizPlanMapper.deleteById(plan.getPlanId());
            plan.insert();
            repaymentBizPlanBak.delete(new EntityWrapper<>()
                    .eq("plan_id", repaymentBizPlanBak.getPlanId())
                    .eq("confirm_log_id", repaymentBizPlanBak.getConfirmLogId()));
        }
        log.deleteById();

        //撤销成功 通知分发中心
        try{
            Result revokeListResult = platformRepaymentFeignClient.revokeTdrepayRecharge(platformRepaymentReq);
            logger.info("=========通知分发中心已经撤销{}",JSON.toJSONString(revokeListResult));
        }catch (Exception e){
            logger.error("=========通知分发中心已经撤销出错{}",e);
        }

        return Result.success();
    }

    @Override
    public List<JSONObject> selectCurrentPeriodConfirmedProjInfo(String businessId, String afterId) {
        List<RepaymentConfirmLog> list = confirmLogMapper.selectList(new EntityWrapper<RepaymentConfirmLog>().eq("business_id", businessId).eq("after_id", afterId).orderBy("`idx`", false));
        List<JSONObject> res = new ArrayList<>();
        for (RepaymentConfirmLog repaymentConfirmLog : list) {
            String json = repaymentConfirmLog.getProjPlanJson();
            List<CurrPeriodProjDetailVO> proj = JSON.parseArray(json, CurrPeriodProjDetailVO.class);
            BigDecimal item10 = new BigDecimal("0");
            BigDecimal item20 = new BigDecimal("0");
            BigDecimal item30 = new BigDecimal("0");
            BigDecimal item50 = new BigDecimal("0");
            BigDecimal offlineOverDue = new BigDecimal("0");
            BigDecimal onlineOverDue = new BigDecimal("0");
            BigDecimal subTotal = new BigDecimal("0");
            BigDecimal total = new BigDecimal("0");
            String realName = null;
            BigDecimal amount = new BigDecimal("0");
            for (CurrPeriodProjDetailVO currPeriodProjDetailVO : proj) {
                realName = currPeriodProjDetailVO.getUserName();
                amount = currPeriodProjDetailVO.getProjAmount();
                item10 = item10.add(currPeriodProjDetailVO.getItem10());
                item20 = item20.add(currPeriodProjDetailVO.getItem20());
                item30 = item30.add(currPeriodProjDetailVO.getItem30());
                item50 = item50.add(currPeriodProjDetailVO.getItem50());
                offlineOverDue = offlineOverDue.add(currPeriodProjDetailVO.getOfflineOverDue());
                onlineOverDue = onlineOverDue.add(currPeriodProjDetailVO.getOnlineOverDue());
                subTotal = subTotal.add(currPeriodProjDetailVO.getSubTotal());
                total = total.add(currPeriodProjDetailVO.getTotal());
            }

            JSONObject p = new JSONObject();
            p.put("realName", realName);
            p.put("amount", amount);
            p.put("item10", item10);
            p.put("item20", item20);
            p.put("item30", item30);
            p.put("item50", item50);
            p.put("repayDate", repaymentConfirmLog.getRepayDate());
            p.put("offlineOverDue", offlineOverDue);
            p.put("onlineOverDue", onlineOverDue);
            p.put("subTotal", subTotal);
            p.put("total", total);
            p.put("list", proj);
            p.put("type", "实际还款");
            p.put("repayDate", DateUtil.formatDate(repaymentConfirmLog.getRepayDate()));
            res.add(p);
        }

        return res;
    }


}
