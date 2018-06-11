package com.hongte.alms.core.controller;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.collection.service.CollectionLogService;
import com.hongte.alms.base.collection.service.CollectionStatusService;
import com.hongte.alms.base.collection.service.PhoneUrgeService;
import com.hongte.alms.base.collection.vo.AfterLoanStandingBookReq;
import com.hongte.alms.base.collection.vo.AfterLoanStandingBookVo;
import com.hongte.alms.base.collection.vo.DeductionVo;
import com.hongte.alms.base.entity.ApplyDerateProcess;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.BasicBusinessType;
import com.hongte.alms.base.entity.BasicCompany;
import com.hongte.alms.base.entity.InfoSms;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentBizPlanListDetail;
import com.hongte.alms.base.entity.SysBank;
import com.hongte.alms.base.entity.SysParameter;
import com.hongte.alms.base.entity.WithholdingPlatform;
import com.hongte.alms.base.entity.WithholdingRecordLog;
import com.hongte.alms.base.enums.AreaLevel;
import com.hongte.alms.base.enums.SysParameterTypeEnums;
import com.hongte.alms.base.enums.repayPlan.RepayPlanFeeTypeEnum;
import com.hongte.alms.base.process.entity.Process;
import com.hongte.alms.base.process.entity.ProcessLog;
import com.hongte.alms.base.process.entity.ProcessTypeStep;
import com.hongte.alms.base.process.enums.ProcessTypeEnums;
import com.hongte.alms.base.process.service.ProcessLogService;
import com.hongte.alms.base.process.service.ProcessService;
import com.hongte.alms.base.process.service.ProcessTypeService;
import com.hongte.alms.base.process.service.ProcessTypeStepService;
import com.hongte.alms.base.vo.module.ApplyDerateProcessReq;
import com.hongte.alms.base.process.vo.ProcessLogReq;
import com.hongte.alms.base.process.vo.ProcessLogVo;
import com.hongte.alms.base.service.*;
import com.hongte.alms.base.vo.module.ApplyDerateListSearchReq;
import com.hongte.alms.base.vo.module.ApplyDerateVo;
import com.hongte.alms.base.vo.module.BankLimitReq;
import com.hongte.alms.base.vo.module.BankLimitVO;
import com.hongte.alms.base.vo.module.BusinessInfoForApplyDerateVo;
import com.hongte.alms.base.vo.module.InfoSmsListSearchReq;
import com.hongte.alms.base.vo.module.InfoSmsListSearchVO;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.ClassCopyUtil;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.util.EasyPoiExcelExportUtil;
import com.hongte.alms.common.util.JsonUtil;
import com.hongte.alms.common.vo.PageRequest;
import com.hongte.alms.common.vo.PageResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import com.hongte.alms.base.feignClient.CustomerInfoXindaiRemoteApi;
import com.hongte.alms.base.feignClient.dto.BankCardInfo;

import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 * 执行代扣 前端控制器
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-03-06
 */
@RestController
@RequestMapping("/DeductionController")
@Api(tags = "CollectionTrackLogController", description = "执行代扣相关接口")
public class DeductionController {


    private Logger logger = LoggerFactory.getLogger(DeductionController.class);
  
    @Autowired
    @Qualifier("DeductionService")
    DeductionService deductionService;
    
    @Autowired
    @Qualifier("BasicBusinessService")
    BasicBusinessService basicBusinessService;
    
    @Autowired
    @Qualifier("WithholdingPlatformService")
    WithholdingPlatformService withholdingplatformService;
    
    @Autowired
    @Qualifier("WithholdingRecordLogService")
    WithholdingRecordLogService withholdingRecordLogService;
    
    @Autowired
    @Qualifier("RepaymentBizPlanListService")
    RepaymentBizPlanListService repaymentBizPlanListService;
    
    @Autowired
    @Qualifier("SysBankService")
    SysBankService sysBankService;
    
    @Autowired
    CustomerInfoXindaiRemoteApi customerInfoXindaiRemoteApi;
    
    @ApiOperation(value = "根据Plan_list_id查找代扣信息")
    @GetMapping("/selectDeductionInfoByPlayListId")
    @ResponseBody
    public Result<DeductionVo> selectDeductionInfoByPlayListId(
            @RequestParam("planListId") String planListId
    ){
    	RepaymentBizPlanList planList=repaymentBizPlanListService.selectById(planListId);
    	BasicBusiness business=basicBusinessService.selectById(planList.getOrigBusinessId());
    	List<BankCardInfo> bankCardInfos=null;
    	BankCardInfo bankCardInfo=null;
    	try {
    		 bankCardInfos=customerInfoXindaiRemoteApi.getBankcardInfo(business.getCustomerIdentifyCard());
    		 if(bankCardInfos!=null&&bankCardInfos.size()>0) {
    			
    			 for(int i=0;i<bankCardInfos.size();i++) {
        			 //看看是否有对应资金端的ID
        			 if(bankCardInfos.get(i).getPlatformType()==business.getOutputPlatformId()) {
        				 bankCardInfo=bankCardInfos.get(i);
        			 }
        		 }
    		 }else {
    			 return Result.error("-1", "该客户找不到对应银行卡信息");
    		 }
    	
    		 if(bankCardInfo==null) {
    			 return Result.error("-1", "该客户信息找不到对应业务的资金端类型");
    		 }
		} catch (Exception e) {
	 	 	 return Result.error("-1", "调用信贷获取客户银行卡信息接口出错");
		}
        try{
            //执行代扣信息
            DeductionVo deductionVo=  deductionService.selectDeductionInfoByPlanListId(planListId);
            deductionVo.setBankCardInfo(bankCardInfo);
            deductionVo.setStrType(business.getSrcType());
            if(deductionVo!=null) {
            	if(istLastPeriod(planList)) {
            	 	 return Result.error("-1", "最后一期不能代扣");
            	}
            	if(bankCardInfo!=null) {
            		deductionVo.setPhoneNumber(bankCardInfo.getMobilePhone());
            		deductionVo.setBankCard(bankCardInfo.getBankCardNumber());
            		deductionVo.setBankName(bankCardInfo.getBankCardName());
            		deductionVo.setPlatformId(5);
            		deductionVo.setBusiness(business);
            		deductionVo.setpList(planList);
            	}
                Map<String, Object> map=basicBusinessService.getOverDueMoney(planListId, RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid(), RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid());
            	BigDecimal onLineOverDueMoney=BigDecimal.valueOf(Double.valueOf(map.get("onLineOverDueMoney").toString()));
            	BigDecimal underLineOverDueMoney=BigDecimal.valueOf(Double.valueOf(map.get("underLineOverDueMoney").toString()));
            	deductionVo.setOnLineOverDueMoney(onLineOverDueMoney);
            	deductionVo.setUnderLineOverDueMoney(underLineOverDueMoney);
            	
            	//还款成功和还款中的数据
        		List<WithholdingRecordLog> loglist=withholdingRecordLogService.selectList(new EntityWrapper<WithholdingRecordLog>().eq("original_business_id", deductionVo.getOriginalBusinessId()).eq("after_id", deductionVo.getAfterId()).ne("repay_status", 0));
        		//还款中的数据
        		List<WithholdingRecordLog> repayingList=withholdingRecordLogService.selectList(new EntityWrapper<WithholdingRecordLog>().eq("original_business_id", deductionVo.getOriginalBusinessId()).eq("after_id", deductionVo.getAfterId()).eq("repay_status", 2));
        		
        		//查看是否共借标，共借标不能银行代扣
        		 business=basicBusinessService.selectOne(new EntityWrapper<BasicBusiness>().eq("business_id", deductionVo.getOriginalBusinessId()));
        		deductionVo.setIssueSplitType(business.getIssueSplitType());
        		BigDecimal repayAmount=BigDecimal.valueOf(0);
        		BigDecimal repayingAmount=BigDecimal.valueOf(0);
        		for(WithholdingRecordLog log:loglist) {
        			repayAmount=repayAmount.add(log.getCurrentAmount());
        		}
        		for(WithholdingRecordLog log:repayingList) {
        			repayingAmount=repayingAmount.add(log.getCurrentAmount());
        		}
        		if(loglist!=null&&loglist.size()>0) {
        			deductionVo.setRepayAllAmount(repayAmount);
        			deductionVo.setRepayingAmount(repayingAmount);
        			deductionVo.setRestAmount(BigDecimal.valueOf(deductionVo.getTotal()).subtract(repayAmount));
        			deductionVo.setTotal(deductionVo.getTotal()-deductionVo.getRepayingAmount().doubleValue());
        		}
                return Result.success(deductionVo);
	
            }else {
            	 return Result.error("-1", "找不到代扣信息");
            }
      
        }catch (Exception ex){
            ex.printStackTrace();
            logger.error(ex.getMessage());
            return Result.error("500", ex.getMessage());
        }

    }

  



/*
    * 获取平台信息
    * @author chenzs
    * @date 2018年3月7日
    * @return 代扣平台信息
    */
   @ApiOperation(value = "获取代扣平台信息")
   @GetMapping("/getDeductionPlatformInfo")
   @ResponseBody
   public Result<Map<String,Object>> getDeductionPlatformInfo(
   ){
	      List<WithholdingPlatform>  platformList = withholdingplatformService.selectList(new EntityWrapper<WithholdingPlatform>());

	      Map<String,Object> retMap = new HashMap<>();
    	   retMap.put("platformList",(JSONArray) JSON.toJSON(platformList, JsonUtil.getMapping()));
    	    return Result.success(retMap);
       }
   
   /*
    * 获取银行信息
    * @author chenzs
    * @date 2018年3月10日
    * @return 银行信息
    */
   @ApiOperation(value = "获取银行信息")
   @GetMapping("/getBank")
   @ResponseBody
   public Result<Map<String,Object>> getBank(
   ){
	      List<SysBank>  bankList = sysBankService.selectList(new EntityWrapper<SysBank>());

	      Map<String,Object> retMap = new HashMap<>();
    	   retMap.put("bankList",(JSONArray) JSON.toJSON(bankList, JsonUtil.getMapping()));
    	    return Result.success(retMap);
       }
   /*
    * 获取平台信息
    * @author chenzs
    * @date 2018年3月10日
    * @return 代扣平台信息
    */
   @ApiOperation(value = "获取银行额度列表信息")
   @GetMapping("/getBankLimit")
   @ResponseBody

   
   public PageResult<List<BankLimitVO>> getBankLimit(@ModelAttribute BankLimitReq  req){

       try{
           Page<BankLimitVO> pages = sysBankService.selectBankLimitList(req.getBankCode(),req.getPlatformId(),  req);
           return PageResult.success(pages.getRecords(),pages.getTotal());
       }catch (Exception ex){
           ex.printStackTrace();
           logger.error(ex.getMessage());
           return PageResult.error(500, "数据库访问异常");
       }
   }

   /**
    * 
    * 判断每期还款计划是否为最后一期
    * @param projPlanList
    * @return
    */
	private boolean istLastPeriod(RepaymentBizPlanList pList) {
		boolean isLast=false;
		List<RepaymentBizPlanList> pLists=repaymentBizPlanListService.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("plan_id", pList.getPlanId()));
		RepaymentBizPlanList lastpList=pLists.stream().max(new Comparator<RepaymentBizPlanList>() {
			@Override
			public int compare(RepaymentBizPlanList o1, RepaymentBizPlanList o2) {
				return o1.getDueDate().compareTo(o2.getDueDate());
			}
		}).get();
		
		if(pList.getPlanListId().equals(lastpList.getPlanListId())) {
			isLast=true;
		}
		return isLast;
	}	
   

   }
   








