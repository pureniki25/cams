package com.hongte.alms.finance.controller;

import java.util.*;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.RepayPlan.dto.PlanReturnInfoDto;
import com.hongte.alms.base.RepayPlan.dto.app.BizDto;
import com.hongte.alms.base.RepayPlan.dto.app.BizPlanDto;
import com.hongte.alms.base.RepayPlan.dto.app.BizPlanListDto;
import com.hongte.alms.base.RepayPlan.dto.app.BizPlanListMessageDto;
import com.hongte.alms.base.RepayPlan.req.*;
import com.hongte.alms.base.RepayPlan.req.trial.TrailBizInfoReq;
import com.hongte.alms.base.RepayPlan.req.trial.TrailProjFeeReq;
import com.hongte.alms.base.RepayPlan.req.trial.TrailProjInfoReq;
import com.hongte.alms.base.RepayPlan.req.trial.TrailRepayPlanReq;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.BasicBusinessType;
import com.hongte.alms.base.entity.BizOutputRecord;
import com.hongte.alms.base.entity.RepaymentBizPlan;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.enums.repayPlan.RepayPlanSettleStatusEnum;
import com.hongte.alms.base.enums.repayPlan.RepayPlanStatus;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.base.service.BasicBusinessTypeService;
import com.hongte.alms.base.service.BizOutputRecordService;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.base.service.RepaymentBizPlanService;
import com.hongte.alms.base.vo.module.api.RepayLogResp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSON;
import com.hongte.alms.base.baseException.CreatRepaymentExcepiton;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.ClassCopyUtil;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.finance.req.RepayPlanReq;
import com.hongte.alms.finance.service.CreatRepayPlanService;
import com.ht.ussp.util.DateUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.Valid;

/**
 * @author zengkun
 * @since 2018/4/23
 */
@RestController
@RequestMapping("/RepayPlan")
@Api(tags = "RepayPlanController", description = "还款计划相关控制器")
public class RepayPlanController {

    Logger  logger = LoggerFactory.getLogger(RepayPlanController.class);

    @Autowired
    @Qualifier("CreatRepayPlanService")
    CreatRepayPlanService creatRepayPlanService;

    @Autowired
    @Qualifier("RepaymentBizPlanService")
    RepaymentBizPlanService repaymentBizPlanService;

    @Autowired
    @Qualifier("RepaymentBizPlanListService")
    RepaymentBizPlanListService repaymentBizPlanListService;
    
    @Autowired
    @Qualifier("BasicBusinessService")
    BasicBusinessService basicBusinessService;
    
    @Autowired
    @Qualifier("BasicBusinessTypeService")
    BasicBusinessTypeService basicBusinessTypeService;
    
    
    @Autowired
    @Qualifier("BizOutputRecordService")
    BizOutputRecordService bizOutputRecordService;

    private static Validator validator;

    //初始化
    @PostConstruct
    public void  init(){
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }


    @ApiOperation(value = "创建还款计划接口,不存储   全字段")
    @PostMapping("/creatRepayPlan")
    @ResponseBody
    public Result<PlanReturnInfoDto> creatRepayPlan(@RequestBody @Validated CreatRepayPlanReq creatRepayPlanReq){
        logger.info("@还款计划@创建还款计划接口,对业务和标的还款计划进行试算--开始[{}]" , JSON.toJSONString(creatRepayPlanReq));
        PlanReturnInfoDto  planReturnInfoDto;



//        List<RepaymentBizPlanDto>  list ;
        try{
            planReturnInfoDto = creatRepayPlanService.creatRepayPlan(creatRepayPlanReq);
        }catch (CreatRepaymentExcepiton e){
            logger.info("传入数据格式不对[{}]" , e);
            return Result.error(e.getCode(),e.getMsg());
        }catch (Exception e){
            logger.info("程序异常[{}]" , e);
            return Result.error("9889",e.getMessage());
        }
        logger.info("@还款计划@创建还款计划接口,对业务和标的还款计划进行试算--结束[{}]" , JSON.toJSONString(planReturnInfoDto));

        return Result.success(planReturnInfoDto);
    }

//    @ApiOperation(value = "测试Ret")
//    @GetMapping("/testRet")
//    @ResponseBody
//    public  Result testRet(){
//        return Result.error("111","111");
//    }

    @ApiOperation(value = "创建还款计划并将还款计划及业务和上标信息存储到数据库 接口")
    @PostMapping("/creatAndSaveRepayPlan")
    @ResponseBody
    public Result<PlanReturnInfoDto> creatAndSaveRepayPlan(@RequestBody @Valid CreatRepayPlanReq creatRepayPlanReq, BindingResult bindingResult){

        // =========   校验参数输入是否正确  开始  ================
        logger.info("创建还款计划并将还款计划及业务和上标信息存储到数据库 接口 开始 输入的请求信息：[{}]", JSON.toJSONString(creatRepayPlanReq));
        if(bindingResult.hasErrors()){
            StringBuilder retErrMsg = new StringBuilder();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                retErrMsg.append("   "+ fieldError.getDefaultMessage());
            }
            logger.info("@还款计划@创建还款计划并将还款计划及业务和上标信息存储到数据库 接口,输入参数校验错误：" ,retErrMsg.toString());
            return Result.error("9889","输入参数校验错误："+retErrMsg.toString());
        }

        //== 校验业务基础信息
        BusinessBasicInfoReq businessBasicInfoReq = creatRepayPlanReq.getBusinessBasicInfoReq();
        Set<ConstraintViolation<BusinessBasicInfoReq>> bizInfovalit =
                validator.validate( creatRepayPlanReq.getBusinessBasicInfoReq() );
        if(bizInfovalit.size()>0){
            logger.info("@还款计划@创建还款计划并将还款计划及业务和上标信息存储到数据库 接口,业务信息校验出错" , bizInfovalit.iterator().next().getMessage());
            return Result.error("9889","业务信息校验出错："+bizInfovalit.iterator().next().getMessage());
//            constraintViolations.iterator().next().getMessage();
        }


        //  ==  校验业务用户信息

        List<BusinessCustomerInfoReq> bizCusInfoReqs =creatRepayPlanReq.getBizCusInfoReqs();
        for(BusinessCustomerInfoReq bizCusInfoReq:bizCusInfoReqs ){
            Set<ConstraintViolation<BusinessCustomerInfoReq>> CusInfovalit =
                    validator.validate( bizCusInfoReq );
            if(bizInfovalit.size()>0){
                logger.info("@还款计划@创建还款计划并将还款计划及业务和上标信息存储到数据库 接口,用户信息校验出错  传入信息： "+ JSON.toJSONString(bizCusInfoReq)+"  错误信息：" , bizInfovalit.iterator().next().getMessage());
                return Result.error("9889","用户信息校验出错： 传入信息： "+JSON.toJSONString(bizCusInfoReq)+"     错误信息："+bizInfovalit.iterator().next().getMessage());
            }
        }

        //  ===校验上标信息
//        /**
//         *  上标信息
//         */
//        @ApiModelProperty(required= true,value = "上标信息")
//        private  List<ProjInfoReq> projInfoReqs;
        List<ProjInfoReq> projInfoReqs = creatRepayPlanReq.getProjInfoReqs();

        for(ProjInfoReq  projInfoReq: projInfoReqs){
            Set<ConstraintViolation<ProjInfoReq>> CusInfovalit =
                    validator.validate( projInfoReq );
            if(bizInfovalit.size()>0){
                logger.info("@还款计划@创建还款计划并将还款计划及业务和上标信息存储到数据库 接口,上标信息校验出错  传入信息： "+ JSON.toJSONString(projInfoReq)+"  错误信息：" , bizInfovalit.iterator().next().getMessage());
                return Result.error("9889","上标信息校验出错： 传入信息： "+JSON.toJSONString(projInfoReq)+"     错误信息："+bizInfovalit.iterator().next().getMessage());
            }

            //   ==  校验标的费用信息
//            /**
//             * 标的费用信息列表
//             */
//            @ApiModelProperty(required= true,value = "标的的出款费用信息列表")
//            private List<ProjFeeReq> projFeeInfos;
            List<ProjFeeReq> projFeeInfos =projInfoReq.getProjFeeInfos();
            for(ProjFeeReq projFeeReq:projFeeInfos){
                Set<ConstraintViolation<ProjFeeReq>> projFeeReqValit =
                        validator.validate( projFeeReq );
                if(projFeeReqValit.size()>0){
                    logger.info("@还款计划@创建还款计划并将还款计划及业务和上标信息存储到数据库 接口,标的的出款费用信息校验出错  传入信息： "+ JSON.toJSONString(projFeeReq)+"  错误信息：" , projFeeReqValit.iterator().next().getMessage());
                    return Result.error("9889","标的的出款费用信息校验出错： 传入信息： "+JSON.toJSONString(projFeeReq)+"     错误信息："+projFeeReqValit.iterator().next().getMessage());
                }
            }

            // 校验标的额外费率信息
            List<ProjExtRateReq> projExtRateReqs = projInfoReq.getProjExtRateReqs();
            if(projExtRateReqs!=null&&projExtRateReqs.size()>0){
                for(ProjExtRateReq projExtRateReq:projExtRateReqs){
                    Set<ConstraintViolation<ProjExtRateReq>> projExtRateReqValit =
                            validator.validate( projExtRateReq );
                    if(projExtRateReqValit.size()>0){
                        logger.info("@还款计划@创建还款计划并将还款计划及业务和上标信息存储到数据库 接口,标的额外费率信息校验出错  传入信息： "+ JSON.toJSONString(projExtRateReq)+"  错误信息：" , projExtRateReqValit.iterator().next().getMessage());
                        return Result.error("9889","标的额外费率信息校验出错： 传入信息： "+JSON.toJSONString(projExtRateReq)+"     错误信息："+projExtRateReqValit.iterator().next().getMessage());
                    }
                }
            }

        }



//        BusinessBasicInfoReq businessBasicInfoReq = creatRepayPlanReq.getBusinessBasicInfoReq();
//        Set<ConstraintViolation<BusinessBasicInfoReq>> bizInfovalit =
//                validator.validate( creatRepayPlanReq.getBusinessBasicInfoReq() );
//        if(bizInfovalit.size()>0){
//            logger.info("@还款计划@创建还款计划并将还款计划及业务和上标信息存储到数据库 接口,业务信息校验出错" , bizInfovalit.iterator().next().getMessage());
//            return Result.error("9889","业务信息校验出错："+bizInfovalit.iterator().next().getMessage());
////            constraintViolations.iterator().next().getMessage();
//        }



        // =========   校验参数输入是否正确  结束  ================



        logger.info("@还款计划@创建还款计划并将还款计划及业务和上标信息存储到数据库 接口--开始[{}]" , JSON.toJSONString(creatRepayPlanReq));
        PlanReturnInfoDto  planReturnInfoDto;
        try{
            planReturnInfoDto = creatRepayPlanService.creatAndSaveRepayPlan(creatRepayPlanReq);
        }catch (CreatRepaymentExcepiton e){
            logger.info("传入数据格式不对[{}]" , e);
            return Result.error(e.getCode(),e.getMsg());
        }catch (Exception e){
            logger.info("程序异常[{}]" , e);
            return Result.error("9889","程序异常报错："+e.getMessage());
        }

        logger.info("@还款计划@创建还款计划并将还款计划及业务和上标信息存储到数据库 接口--结束[{}]" , JSON.toJSONString(creatRepayPlanReq));
        return  Result.success(planReturnInfoDto);
    }

//    @ApiOperation(value = "测试时间转换")
//    @PostMapping("/testTime")
//    @ResponseBody
//    public Result<PlanReturnInfoDto> testTime() {
//        PlanReturnInfoDto dto = new PlanReturnInfoDto();
//        dto.setTestTime(new Date());
//        return Result.success(dto);
//    }



    @ApiOperation(value = "试算还款计划接口, 精简字段")
    @PostMapping("/trailRepayPlan")
    @ResponseBody
    public Result<PlanReturnInfoDto> trailRepayPlan(@Valid @RequestBody TrailRepayPlanReq trailRepayPlanReq , BindingResult bindingResult){
        // =========   校验参数输入是否正确  开始  ================
        logger.info("试算还款计划接口, 精简字段  开始 输入的试算信息：[{}]", JSON.toJSONString(trailRepayPlanReq));
        if(bindingResult.hasErrors()){
            StringBuilder retErrMsg = new StringBuilder();
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                retErrMsg.append("   "+ fieldError.getDefaultMessage());
            }
            logger.info("@还款计划@试算还款计划接口,输入参数校验错误：" ,retErrMsg.toString());
            return Result.error("9889","输入参数校验错误："+retErrMsg.toString());
        }

        Set<ConstraintViolation<TrailBizInfoReq>> constraintViolations =
                validator.validate( trailRepayPlanReq.getTrailBizInfoReq() );
        if(constraintViolations.size()>0){
            logger.info("@还款计划@试算还款计划接口,业务信息校验出错" , constraintViolations.iterator().next().getMessage());
            return Result.error("9889","业务信息校验出错："+constraintViolations.iterator().next().getMessage());
//            constraintViolations.iterator().next().getMessage();
        }



//        TrailProjFeeReq


        List<TrailProjInfoReq>   trailProjInfoReqs = trailRepayPlanReq.getProjInfoReqs();


        for(TrailProjInfoReq trailProjInfoReq:trailProjInfoReqs){
            Set<ConstraintViolation<TrailProjInfoReq>> constraintViolations1 =
                    validator.validate( trailProjInfoReq );
            if(constraintViolations1.size()>0){
                logger.info("@还款计划@试算还款计划接口,标的信息校验出错:  错误信息："+constraintViolations1.iterator().next().getMessage() +"传入对象："  , JSON.toJSONString(trailProjInfoReq));
                return Result.error("9889","标的信息校验出错：传入对象："+JSON.toJSONString(trailProjInfoReq)+"     错误信息："+constraintViolations1.iterator().next().getMessage());
            }
            List<TrailProjFeeReq>  feeReqList =  trailProjInfoReq.getProjFeeInfos();

            //校验费用信息
            for(TrailProjFeeReq trailProjFeeReq:feeReqList){

                Set<ConstraintViolation<TrailProjFeeReq>> trailProjFeeReqViolations1 =
                        validator.validate( trailProjFeeReq );
                if(trailProjFeeReqViolations1.size()>0){
                    logger.info("@还款计划@试算还款计划接口,标的费用项信息校验出错  传入对象："+JSON.toJSONString(trailProjFeeReq)+"     错误信息：" , trailProjFeeReqViolations1.iterator().next().getMessage());
                    return Result.error("9889","标的费用项信息校验出错：传入对象："+JSON.toJSONString(trailProjFeeReq)+"     错误信息："+trailProjFeeReqViolations1.iterator().next().getMessage());
                }
            }

        }
        // =========   校验参数输入是否正确  结束  ================




        logger.info("@还款计划@试算还款计划接口,对业务和标的还款计划进行试算--开始[{}]" , JSON.toJSONString(trailRepayPlanReq));
        CreatRepayPlanReq creatRepayPlanReq= null;
        try{
            creatRepayPlanReq =  ClassCopyUtil.copy(trailRepayPlanReq,TrailRepayPlanReq.class,CreatRepayPlanReq.class);

            TrailBizInfoReq trailBizInfoReq = trailRepayPlanReq.getTrailBizInfoReq();

            BusinessBasicInfoReq businessBasicInfoReq = ClassCopyUtil.copy(trailBizInfoReq,TrailBizInfoReq.class,BusinessBasicInfoReq.class);
            creatRepayPlanReq.setBusinessBasicInfoReq(businessBasicInfoReq);
            businessBasicInfoReq.setBusinessId("111");
            businessBasicInfoReq.setOrgBusinessId(businessBasicInfoReq.getBusinessId());

//            List<TrailProjInfoReq> trailProjInfoReqs = trailRepayPlanReq.getProjInfoReqs();

            List<ProjInfoReq>  projInfoReqs = new LinkedList<>();
            creatRepayPlanReq.setProjInfoReqs(projInfoReqs);
            businessBasicInfoReq.setRepaymentTypeId(trailProjInfoReqs.get(0).getRepayType());
            for(TrailProjInfoReq trailProjInfoReq:trailProjInfoReqs){
                ProjInfoReq projInfoReq =ClassCopyUtil.copy(trailProjInfoReq,TrailProjInfoReq.class,ProjInfoReq.class);
                projInfoReqs.add(projInfoReq);
                List<PrincipleReq> principleReqs =  trailProjInfoReq.getPricipleMap();
                projInfoReq.setPrincipleReqList(principleReqs);

                List<TrailProjFeeReq> trailProjFeeReqs = trailProjInfoReq.getProjFeeInfos();
                List<ProjFeeReq> projFeeReqs = new LinkedList<>();
                projInfoReq.setProjFeeInfos(projFeeReqs);

                for(TrailProjFeeReq trailProjFeeReq:trailProjFeeReqs){
                    ProjFeeReq projFeeReq = ClassCopyUtil.copy(trailProjFeeReq,TrailProjFeeReq.class,ProjFeeReq.class);
                    projFeeReq.setFeeType(30);//试算的时候默认把费用类型都设置成分公司服务费
                    List<ProjFeeDetailReq> projFeeDetailReqs = trailProjFeeReq.getFeeDetailReqMap();
                    projFeeReq.setFeeDetailReqList(projFeeDetailReqs);
                    projFeeReqs.add(projFeeReq);
                }

            }
        }catch (Exception e){
            logger.info("@还款计划@试算还款计划接口,对业务和标的还款计划进行试算--异常[{}]" , e);
            return Result.error("9889","程序内部异常"+e.getMessage());
        }
        logger.info("@还款计划@试算还款计划接口,对业务和标的还款计划进行试算--结束[{}]" , JSON.toJSONString(creatRepayPlanReq));
        return creatRepayPlan(creatRepayPlanReq);

//        List<RepaymentBizPlanDto>  list ;
//        try{
//            list = creatRepayPlanService.creatRepayPlan(creatRepayPlanReq);
//        }catch (CreatRepaymentExcepiton e){
//            logger.info("传入数据格式不对[{}]" , e);
//            return Result.error(e.getCode(),e.getMsg());
//        }catch (Exception e){
//            logger.info("程序异常[{}]" , e);
//            return Result.error("9889",e.getMessage());
//        }
//        logger.info("@还款计划@创建还款计划接口,对业务和标的还款计划进行试算--结束[{}]" , list);
//
//        return Result.success(list);
    }
    
	@ApiOperation(value = "根据businessId查询还款计划")
	@PostMapping("/queryRepayPlanByBusinessId")
	@ResponseBody
	public Result<PlanReturnInfoDto> queryRepayPlanByBusinessId(@RequestBody RepayPlanReq req) {
		try {
			logger.info("查询还款计划，业务编号：[{}]", req.getBusinessId());
			if (StringUtil.isEmpty(req.getBusinessId())) {
				return Result.error("9889", "业务编号不能为空！");
			}
			PlanReturnInfoDto planReturnInfoDto = creatRepayPlanService.queryRepayPlanByBusinessId(req.getBusinessId());
			if (planReturnInfoDto == null) {
                logger.error("没有找到相关数据，请检查业务编号是否输入正确！", req.getBusinessId());
				return Result.build("1", "没有找到相关数据，请检查业务编号是否输入正确！", planReturnInfoDto);
			}
            logger.info("查询还款计划，查询成功：[{}]", planReturnInfoDto);
			return Result.success(planReturnInfoDto);
		} catch (Exception e) {
			logger.error("查询还款计划异常[{}]", e);
			return Result.error("9889", e.getMessage());
		}
	}
	
	@SuppressWarnings("rawtypes")
	@ApiOperation(value = "根据条件撤销还款计划")
	@PostMapping("/deleteRepayPlanByConditions")
	@ResponseBody
	public Result deleteRepayPlanByConditions(@RequestBody RepayPlanReq req) {
		try {
			logger.info("删除还款计划，业务编号：[{}], 还款计划编号[{}]", req.getBusinessId(), req.getRepaymentBatchId());
			if (StringUtil.isEmpty(req.getBusinessId())  || StringUtil.isEmpty(req.getRepaymentBatchId())) {
				return Result.error("9889", "业务编号或还款批次号不能为空！");
			}
			creatRepayPlanService.deleteRepayPlanByConditions(req.getBusinessId(), req.getRepaymentBatchId());
			return Result.success();
		} catch (Exception e) {
			logger.error("删除还款计划异常[{}]", e);
			return Result.error("9889", e.getMessage());
		}
	}

//    @ApiOperation(value = "根据用户身份证ID获取用户待还款的业务信息列表")
//    @GetMapping("/repaymentGetRepayingList")
//    @ResponseBody
//   public PageResult<List<RepayingPlanVo>> repaymentGetRepayingList(Integer pageIndex, Integer pageSize, String identifyCard){
//        logger.info("根据用户身份证ID获取用户待还款的业务信息列表，客户身份证号：[{}], 页数[{}],每页大小[{}]", identifyCard, pageIndex,pageSize);
//        Page<RepayingPlanVo>  page = null;
//        try{
//            page = repaymentBizPlanService.queryCustomeRepayPlanInfo(identifyCard, pageIndex, pageSize) ;
//            logger.info("根据用户身份证ID获取用户待还款的业务信息列表，返回数据：[{}]", JSON.toJSONString(page));
//            return  PageResult.success(page.getRecords(),page.getTotal());
//        }catch (Exception e){
//            logger.error("根据用户身份证ID获取用户待还款的业务信息列表异常[{}]", e);
//            return  PageResult.error(9889, e.getMessage());
//        }
//   }



    @ApiOperation(value = "根据业务ID列表查找出业务账单列表")
    @PostMapping("/getRepayList")
    @ResponseBody
   public Result<List<BizDto>> getRepayList(@RequestBody List<String> businessIds){
        logger.info("根据业务ID列表查找出业务账单列表 开始，业务ID列表：[{}]", JSON.toJSONString(businessIds));

       List<BizDto> bizDtos = new LinkedList<>();

       for(String businessId:businessIds){
           BizDto bizDto =getBizDtoByBizId(businessId);
           bizDtos.add(bizDto);

       }
        logger.info("根据业务ID列表查找出业务账单列表 结束，返回数据：[{}]", JSON.toJSONString(bizDtos));

       return Result.success(bizDtos);
   }




    @ApiOperation(value = "根据业务ID查找此业务的历史账单")
    @PostMapping("/getLogBill")
    @ResponseBody
   public  Result<BizDto> getLogBill(@RequestParam(value = "businessId") String businessId){

        logger.info("根据业务ID查找此业务的历史账单 开始，业务ID列表：[{}]", JSON.toJSONString(businessId));


        //业务基本的还款计划信息
        BizDto bizDto =getBizDtoByBizId(businessId);

        List<RepaymentBizPlan> bizPlans = repaymentBizPlanService.selectList(new EntityWrapper<RepaymentBizPlan>().eq("original_business_id",businessId));

        Map<String,List<RepaymentBizPlan>> bizPlanMap = new HashMap<>();

        if(bizPlans!=null&&bizPlans.size()>0){
            for(RepaymentBizPlan bizPlan: bizPlans){
                if(bizPlan.getBusinessId().equals(bizPlan.getOriginalBusinessId())){
                    continue;
                }
                List<RepaymentBizPlan> bizPlanList = bizPlanMap.get(bizPlan.getBusinessId());
                if(bizPlanList==null){
                    bizPlanList = new LinkedList<>();
                    bizPlanMap.put(bizPlan.getBusinessId(),bizPlanList);
                }
                bizPlanList.add(bizPlan);
            }
        }

        List<BizDto> renewBizDtos = new LinkedList<>();
        bizDto.setRenewBizs(renewBizDtos);
        for(String renewBizId:bizPlanMap.keySet()){
            BizDto renewBizDto =getBizDtoByBizId(renewBizId);
            renewBizDtos.add(renewBizDto);
        }

        logger.info("根据业务ID查找此业务的历史账单 结束，返回数据：[{}]", JSON.toJSONString(bizDto));

        return Result.success(bizDto);
   }




    @ApiOperation(value = "根据还款计划ID查找出此还款计划的详情账单信息")
    @PostMapping("/getBizPlanBill")
    @ResponseBody
    public Result<BizPlanDto> getBizPlanBill(@RequestParam(value = "planId") String planId){
        logger.info("根据还款计划ID查找出此还款计划的详情账单信息 开始，还款计划ID：[{}]", JSON.toJSONString(planId));



       RepaymentBizPlan bizPlan =  repaymentBizPlanService.selectById(planId);

       if(bizPlan ==null){
           return Result.error("9889","未找到对应的还款计划");
       }
        BizPlanDto  bizPlanDto  = getBizPlanDtoByBizPlan(bizPlan);
        logger.info("根据还款计划ID查找出此还款计划的详情账单信息 结束，返回数据：[{}]", JSON.toJSONString(bizPlanDto));

       return  Result.success(bizPlanDto);
    }
    
    @ApiOperation(value = "获取业务的还款计划信息进行消息推送")
    @PostMapping("/getMessage")
    @ResponseBody
    public Result<List<BizPlanListMessageDto>> getMessage(@RequestParam(value = "messageType") String messageType,@RequestParam(value = "dateCount") Integer dateCount){
        logger.info("获取业务的还款计划信息进行消息推送 开始，推送类型和天数：[{}]", JSON.toJSONString(messageType),JSON.toJSONString(dateCount));
        List<BizPlanListMessageDto> messages=new ArrayList();
        List<RepaymentBizPlanList>  lists=null;
        if(messageType.equals("0")) {//还款提醒
    	    Date dueDate=DateUtil.getDate(DateUtil.formatDate(DateUtil.addDay2Date(dateCount, new Date())));
    	    lists=repaymentBizPlanListService.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("current_status","还款中").eq("due_date", dueDate));
    	  	   
       }
       if(messageType.equals("1")) {//1.逾期提醒
    	   dateCount=0-dateCount;
    	   Date dueDate=DateUtil.getDate(DateUtil.formatDate(DateUtil.addDay2Date(dateCount, new Date())));
    	   lists=repaymentBizPlanListService.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("current_status","逾期").eq("due_date", dueDate));
       }
       if(messageType.equals("2")) {//2.结清提醒
    	   Date dueDate=DateUtil.getDate(DateUtil.formatDate(DateUtil.addDay2Date(dateCount, new Date())));
    	   lists=repaymentBizPlanListService.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("current_status","还款中").eq("due_date", dueDate).orderBy("due_date",false));
    		//筛选是最后一期的还款记录
			for(Iterator<RepaymentBizPlanList> it = lists.iterator();it.hasNext();) {
				RepaymentBizPlanList pList=it.next();
				if(!istLastPeriod(pList)) {
					it.remove();
				}
			}
       }
       
    
       for(RepaymentBizPlanList list:lists) {
    	  BasicBusiness business= basicBusinessService.selectOne(new EntityWrapper<BasicBusiness>().eq("business_id", list.getOrigBusinessId()));
    	  BasicBusinessType type=basicBusinessTypeService.selectOne(new EntityWrapper<BasicBusinessType>().eq("business_type_id", business.getBusinessType()));
    	  List<BizOutputRecord> outputRecords=bizOutputRecordService.selectList(new EntityWrapper<BizOutputRecord>().eq("business_id", business.getBusinessId()).orderBy("fact_output_date"));
    	  Date outputDate=null;
    	  if(outputRecords!=null&&outputRecords.size()>0) {
    		  outputDate=outputRecords.get(0).getFactOutputDate();
    	  }
    	
    	  BizPlanListMessageDto message=new BizPlanListMessageDto(list.getBusinessId(), business.getCustomerName(), business.getCustomerIdentifyCard(), type.getBusinessTypeName(), outputDate, list.getTotalBorrowAmount(), list.getPeriod(), business.getBorrowMoney(), list.getDueDate());
    	  messages.add(message);
       }   
      
       if(lists ==null||lists.size()==0){
           return Result.error("9889","未找到对应的还款计划信息");
       }
        logger.info("获取业务的还款计划信息进行消息推送  结束，返回数据：[{}]", JSON.toJSONString(messages));

       return  Result.success(messages);
    }

    /**
     * 根据业务ID取得此业务的还款计划信息返回
     * @param businessId
     * @return
     */
    private BizDto getBizDtoByBizId(String businessId){
        BizDto bizDto = new BizDto();

        BasicBusiness business = basicBusinessService.selectById(businessId);
        bizDto.setBusinessId(businessId);
        bizDto.setBusinessType(business.getBusinessType());
        List<RepaymentBizPlan> bizPlans = repaymentBizPlanService.selectList(new EntityWrapper<RepaymentBizPlan>().eq("business_id",businessId));
        if(bizPlans!=null && bizPlans.size()>0){
            List<BizPlanDto> bizPlanDtos = new LinkedList<>();
            bizDto.setPlanDtoList(bizPlanDtos);
            for(RepaymentBizPlan bizPlan: bizPlans){
                BizPlanDto bizPlanDto = getBizPlanDtoByBizPlan(bizPlan);
                bizPlanDtos.add(bizPlanDto);
            }
        }
        return bizDto;
    }


    /**
     * 根据还款计划取得返回的还款计划信息
     * @param bizPlan
     * @return
     */
    private   BizPlanDto getBizPlanDtoByBizPlan(RepaymentBizPlan bizPlan){
        BizPlanDto bizPlanDto = new BizPlanDto();
        bizPlanDto.setPlanId(bizPlan.getPlanId());
        //判断是否已结清
        if (RepayPlanSettleStatusEnum.payed(bizPlan.getPlanStatus())) {
            bizPlanDto.setIsOver(true);
        } else {
            bizPlanDto.setIsOver(false);
        }

        //判断是否已申请展期
        if (RepayPlanSettleStatusEnum.renewed(bizPlan.getPlanStatus())) {
            bizPlanDto.setHasDeffer(true);
        } else {
            bizPlanDto.setHasDeffer(false);
        }


        List<RepaymentBizPlanList> bizPlanLists = repaymentBizPlanListService.selectList(
                new EntityWrapper<RepaymentBizPlanList>().eq("plan_id",bizPlan.getPlanId()).orderBy("due_date"));

        //未结清且未展期的业务计算当前期
        if(!bizPlanDto.getHasDeffer()&&!bizPlanDto.getIsOver()){
            RepaymentBizPlanList currentPlan = repaymentBizPlanListService.findCurrentPeriod(new Date(), bizPlanLists);
            if(currentPlan!=null){
                BizPlanListDto currentPeroid = new BizPlanListDto(currentPlan);
                currentPeroid.setIsCurrentPeriod(true);
                bizPlanDto.setCurrentPeroid(currentPeroid);
            }
        }

        //已支付的还款计划列表
        List<BizPlanListDto> payedPeroids = new LinkedList<>();
        bizPlanDto.setPayedPeroids(payedPeroids);
        //待支付的还款计划列表
        List<BizPlanListDto> payingPeroids = new LinkedList<>();
        bizPlanDto.setPayingPeroids(payingPeroids);

        for (RepaymentBizPlanList bizPlanList:bizPlanLists){
            if(bizPlanList.getCurrentStatus().equals(RepayPlanStatus.REPAYED.getName())){
                BizPlanListDto bizPlanListDto = new BizPlanListDto(bizPlanList);
                payedPeroids.add(bizPlanListDto);
            }else {
                BizPlanListDto bizPlanListDto = new BizPlanListDto(bizPlanList);
                payingPeroids.add(bizPlanListDto);
            }
        }
        return  bizPlanDto;
    }



	public boolean istLastPeriod(RepaymentBizPlanList pList) {
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
