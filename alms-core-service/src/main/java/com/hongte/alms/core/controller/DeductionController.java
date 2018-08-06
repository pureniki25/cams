package com.hongte.alms.core.controller;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;

import com.hongte.alms.base.collection.vo.DeductionVo;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentBizPlanListDetail;
import com.hongte.alms.base.entity.RepaymentConfirmLog;
import com.hongte.alms.base.entity.SysBank;
import com.hongte.alms.base.entity.WithholdingPlatform;
import com.hongte.alms.base.entity.WithholdingRecordLog;
import com.hongte.alms.base.entity.WithholdingRepaymentLog;
import com.hongte.alms.base.enums.repayPlan.RepayPlanFeeTypeEnum;

import com.hongte.alms.base.service.*;
import com.hongte.alms.base.vo.module.BankLimitReq;
import com.hongte.alms.base.vo.module.BankLimitVO;

import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.JsonUtil;
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
    @Qualifier("WithholdingRepaymentLogService")
    WithholdingRepaymentLogService withholdingRepaymentLogService;
    
    @Autowired
    @Qualifier("SysBankService")
    SysBankService sysBankService;
    
    @Autowired
    @Qualifier("RepaymentConfirmLogService")
    RepaymentConfirmLogService repaymentConfirmLogService;
    @Autowired
    @Qualifier("MoneyPoolRepaymentService")
    MoneyPoolRepaymentService moneyPoolRepaymentService;

 

    @Autowired
    @Qualifier("RepaymentBizPlanListDetailService")
    RepaymentBizPlanListDetailService  repaymentBizPlanListDetailService;

    @Autowired
    CustomerInfoXindaiRemoteApi customerInfoXindaiRemoteApi;
    
    @SuppressWarnings({ "rawtypes", "unused" })
	@ApiOperation(value = "根据Plan_list_id查找代扣信息")
    @GetMapping("/selectDeductionInfoByPlayListId")
    @ResponseBody
    public Result<DeductionVo> selectDeductionInfoByPlayListId(
            @RequestParam("planListId") String planListId
    ){
        try{
        	RepaymentBizPlanList planList=repaymentBizPlanListService.selectById(planListId);
        	BasicBusiness business=basicBusinessService.selectById(planList.getOrigBusinessId());
        	List<BankCardInfo> bankCardInfos=null;
        	BankCardInfo bankCardInfo=null;
        	try {
				Result result=customerInfoXindaiRemoteApi.getBankcardInfo(business.getCustomerIdentifyCard());
				if(result.getCode().equals("1")) {
					 bankCardInfos=JSON.parseArray(result.getData().toString(), BankCardInfo.class);
					if(bankCardInfos!=null&&bankCardInfos.size()>0) {
						for (BankCardInfo card : bankCardInfos) {
							if (card.getPlatformType() == 1 && card.getWithholdingType() == 1) {// 团贷网平台注册的银行卡并且是代扣主卡
								bankCardInfo=card;
							}
						}
                        if(bankCardInfo==null) {
                    	  return Result.error("-1", "该客户找不到对应团贷网平台银行卡信息");
					    }
	        		}else {
	        			return Result.error("-1", "该客户找不到对应银行卡信息");
	        		}
				}else {
					return Result.error("-1", result.getMsg());
				}


        	} catch (Exception e) {
        		return Result.error("-1", "调用信贷获取客户银行卡信息接口出错");
        	}
        	
      
        	
            //执行代扣信息
            DeductionVo deductionVo=  deductionService.selectDeductionInfoByPlanListId(planListId);
            deductionVo.setBankCardInfo(bankCardInfo);
            deductionVo.setIdentifyCard(bankCardInfo.getIdentityNo());
            deductionVo.setStrType(business.getSrcType());
            if(deductionVo!=null) {
            	if(istLastPeriod(planList)) {
            	 	 return Result.error("-1", "最后一期不能代扣");
            	}
            	if(bankCardInfo!=null) {
            		deductionVo.setPhoneNumber(bankCardInfo.getMobilePhone());
            		deductionVo.setBankCard(bankCardInfo.getBankCardNumber());
            		deductionVo.setBankName(bankCardInfo.getBankName());
            		deductionVo.setPlatformId(5);
            		deductionVo.setBusiness(business);
            		deductionVo.setpList(planList);
            	}
                Map<String, Object> map=basicBusinessService.getOverDueMoney(planListId, RepayPlanFeeTypeEnum.OVER_DUE_AMONT_ONLINE.getUuid(), RepayPlanFeeTypeEnum.OVER_DUE_AMONT_UNDERLINE.getUuid());
            	BigDecimal onLineOverDueMoney=BigDecimal.valueOf(Double.valueOf(map.get("onLineOverDueMoney").toString()));
            	BigDecimal underLineOverDueMoney=BigDecimal.valueOf(Double.valueOf(map.get("underLineOverDueMoney").toString()));
            	BigDecimal underLineFactOverDueMoney=BigDecimal.valueOf(Double.valueOf(map.get("underLineFactOverDueMoney").toString()));
            	
            	deductionVo.setOnLineOverDueMoney(onLineOverDueMoney);
            	deductionVo.setUnderLineOverDueMoney(underLineOverDueMoney);
            	deductionVo.setUnderLineFactOverDueMoney(underLineFactOverDueMoney);
            	//查看当前期还款的总金额
        	
        		BigDecimal factAmountSum=BigDecimal.valueOf(0);
        		boolean isHaveBankRepay=false;//是否含有银行代扣
        		boolean isHaveThirtyRepay=false;//是否含有第三方
        		boolean isHaveUnderRepay=false;//是否含有线下
        		boolean isRepaying=isRepaying(planList);//判断是否有代扣中的记录
        		deductionVo.setRepaying(isRepaying);
                List<RepaymentConfirmLog> comfirmLogs=repaymentConfirmLogService.selectList(new EntityWrapper<RepaymentConfirmLog>().eq("org_business_id", planList.getOrigBusinessId()).eq("after_id", planList.getAfterId()));
        		for(RepaymentConfirmLog log:comfirmLogs) {
        			factAmountSum=factAmountSum.add(log.getFactAmount());
        			if(log.getRepaySource()==31||log.getRepaySource()==30) {
        				isHaveBankRepay=true;
        				deductionVo.setHaveBankRepay(isHaveBankRepay);
        			}
        			
        			if(log.getRepaySource()==21||log.getRepaySource()==20) {
        				isHaveThirtyRepay=true;
        				deductionVo.setHaveThirtyRepay(isHaveThirtyRepay);
        			}
        			if(log.getRepaySource()==10) {
        				isHaveUnderRepay=true;
        				deductionVo.setHaveUnderRepay(isHaveUnderRepay);
        			}
        		}
        		
        		//线上费用
        		BigDecimal onLineAmount=BigDecimal.valueOf(deductionVo.getTotal()).subtract(underLineOverDueMoney);
        		//如果是银行代扣线上部分还款的话，不能使用第三方代扣线下费用
        		if(factAmountSum.compareTo(BigDecimal.valueOf(0))>0&&factAmountSum.compareTo(onLineAmount)<0&&isHaveBankRepay==true) {
        			deductionVo.setCanUseThirty(false);
        		}else {
        			deductionVo.setCanUseThirty(true);
        		}



            	//查看是否共借标，共借标不能银行代扣
        		//deductionVo.setIssueSplitType(business.getIssueSplitType());
            	if(business.getSrcType()!=null&&business.getSrcType()==2) {
            	   	//还款成功和还款中的数据
	        		List<WithholdingRepaymentLog> loglist=withholdingRepaymentLogService.selectList(new EntityWrapper<WithholdingRepaymentLog>().eq("original_business_id", deductionVo.getOriginalBusinessId()).eq("after_id", deductionVo.getAfterId()).ne("repay_status", 0));
	        		 factAmountSum=getPerListFactAmountSum(planList);
	        		//还款中的数据
	        		List<WithholdingRepaymentLog> repayingList=withholdingRepaymentLogService.selectList(new EntityWrapper<WithholdingRepaymentLog>().eq("original_business_id", deductionVo.getOriginalBusinessId()).eq("after_id", deductionVo.getAfterId()).eq("repay_status", 2));
	        	
	        		BigDecimal repayAmount=factAmountSum;
	        		BigDecimal repayingAmount=BigDecimal.valueOf(0);

	        		for(WithholdingRepaymentLog log:repayingList) {
	        			repayingAmount=repayingAmount.add(log.getCurrentAmount());
	        		}
	        		repayAmount=repayAmount.add(repayingAmount);//已经还款的金额
	        		if(loglist!=null&&loglist.size()>0) {
	        			deductionVo.setRepayAllAmount(factAmountSum.add(repayingAmount));
	        			deductionVo.setRepayingAmount(repayingAmount);
	        			deductionVo.setRestAmount(BigDecimal.valueOf(deductionVo.getTotal()).subtract(repayAmount));
	        			deductionVo.setRepayAmount(deductionVo.getRestAmount());
	        			deductionVo.setTotal(deductionVo.getTotal()-deductionVo.getRepayingAmount().doubleValue());
	        		}else {
	        			deductionVo.setRepayAllAmount(factAmountSum);
	        			deductionVo.setRepayAmount(BigDecimal.valueOf(deductionVo.getTotal()));
	        		}
	                return Result.success(deductionVo);
            	}else {
	            	//还款成功和还款中的数据
	        		List<WithholdingRecordLog> loglist=withholdingRecordLogService.selectList(new EntityWrapper<WithholdingRecordLog>().eq("original_business_id", deductionVo.getOriginalBusinessId()).eq("after_id", deductionVo.getAfterId()).ne("repay_status", 0));
	        		//还款中的数据
	        		List<WithholdingRecordLog> repayingList=withholdingRecordLogService.selectList(new EntityWrapper<WithholdingRecordLog>().eq("original_business_id", deductionVo.getOriginalBusinessId()).eq("after_id", deductionVo.getAfterId()).eq("repay_status", 2));
	        		
	          		 factAmountSum=getPerListFactAmountSum(planList);
	          		BigDecimal repayAmount=factAmountSum;
	        		BigDecimal repayingAmount=BigDecimal.valueOf(0);


	        		for(WithholdingRecordLog log:repayingList) {
	        			repayingAmount=repayingAmount.add(log.getCurrentAmount());
	        		}
	        		repayAmount=repayAmount.add(repayingAmount);//已经还款的金额
	        		if(loglist!=null&&loglist.size()>0) {
	        			deductionVo.setRepayAllAmount(factAmountSum.add(repayingAmount));
	        			deductionVo.setRepayingAmount(repayingAmount);
	        			deductionVo.setRestAmount(BigDecimal.valueOf(deductionVo.getTotal()).subtract(repayAmount));
	        			deductionVo.setRepayAmount(deductionVo.getRestAmount());
	        			deductionVo.setTotal(deductionVo.getTotal()-deductionVo.getRepayingAmount().doubleValue());
	        		}else {
	        			deductionVo.setRepayAmount(BigDecimal.valueOf(deductionVo.getTotal()));
	        		}
	                return Result.success(deductionVo);
            	}
	
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

	private BigDecimal getPerListFactAmountSum(RepaymentBizPlanList pList) {
		List<RepaymentBizPlanListDetail> details=repaymentBizPlanListDetailService.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().eq("plan_list_id", pList.getPlanListId()));
		BigDecimal factAmountSum=BigDecimal.valueOf(0);
		for(RepaymentBizPlanListDetail detail:details) {
			factAmountSum=factAmountSum.add(detail.getFactAmount()==null?BigDecimal.valueOf(0):detail.getFactAmount());
		}
		return factAmountSum;

	}

	private boolean isRepaying(RepaymentBizPlanList pList) {
		boolean isRepaying=false;
		int i=withholdingRepaymentLogService.selectCount(new EntityWrapper<WithholdingRepaymentLog>().eq("original_business_id", pList.getOrigBusinessId()).eq("after_id", pList.getAfterId()).eq("repay_status", 2));
		if(i>0) {
			isRepaying=true;
		}
		return isRepaying;
	}
   }
   








