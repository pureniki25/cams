package com.hongte.alms.finance.controller;

import java.math.BigDecimal;
import java.util.*;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.RepayPlan.dto.PlanReturnInfoDto;
import com.hongte.alms.base.RepayPlan.dto.RepaymentProjPlanDto;
import com.hongte.alms.base.RepayPlan.dto.TrailProjPlanDto;
import com.hongte.alms.base.RepayPlan.dto.app.BizDto;
import com.hongte.alms.base.RepayPlan.dto.app.BizPlanDto;
import com.hongte.alms.base.RepayPlan.dto.app.BizPlanListDto;
import com.hongte.alms.base.RepayPlan.dto.app.BizPlanListMessageDto;
import com.hongte.alms.base.RepayPlan.req.*;
import com.hongte.alms.base.RepayPlan.req.trial.TrailBizInfoReq;
import com.hongte.alms.base.RepayPlan.req.trial.TrailProjFeeReq;
import com.hongte.alms.base.RepayPlan.req.trial.TrailProjInfoReq;
import com.hongte.alms.base.RepayPlan.req.trial.TrailRepayPlanReq;
import com.hongte.alms.base.entity.*;
import com.hongte.alms.base.enums.BusinessTypeEnum;
import com.hongte.alms.base.enums.ProjPlatTypeEnum;
import com.hongte.alms.base.enums.RepayTypeEnum;
import com.hongte.alms.base.enums.repayPlan.RepayPlanBorrowLimitUnitEnum;
import com.hongte.alms.base.enums.repayPlan.RepayPlanCreateSysEnum;
import com.hongte.alms.base.enums.repayPlan.RepayPlanSettleStatusEnum;
import com.hongte.alms.base.enums.repayPlan.RepayPlanStatus;
import com.hongte.alms.base.service.*;
import com.hongte.alms.base.vo.module.api.RepayLogResp;

import com.hongte.alms.common.exception.ExceptionCodeEnum;
import com.hongte.alms.common.exception.MyException;
import com.hongte.alms.finance.service.ShareProfitService;
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
import javax.xml.soap.Detail;

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
    @Qualifier("RepaymentProjPlanService")
    RepaymentProjPlanService repaymentProjPlanService;


    @Autowired
    @Qualifier("RepaymentBizPlanListService")
    RepaymentBizPlanListService repaymentBizPlanListService;


    @Autowired
    @Qualifier("RepaymentBizPlanListDetailService")
    RepaymentBizPlanListDetailService repaymentBizPlanListDetailService;



    @Autowired
    @Qualifier("BasicBusinessService")
    BasicBusinessService basicBusinessService;
    
    @Autowired
    @Qualifier("BasicBusinessTypeService")
    BasicBusinessTypeService basicBusinessTypeService;
    
    @Autowired
    @Qualifier("BasicBizCustomerService")
    BasicBizCustomerService basicBizCustomerService;
    
    
    @Autowired
    @Qualifier("BizOutputRecordService")
    BizOutputRecordService bizOutputRecordService;
    
    @Autowired
    @Qualifier("CarBasicService")
    CarBasicService carBasicService;

    @Autowired
    @Qualifier("TuandaiProjectInfoService")
    TuandaiProjectInfoService tuandaiProjectInfoService;


    @Autowired
    @Qualifier("ShareProfitService")
    ShareProfitService shareProfitService;
    
    @Autowired
    @Qualifier("RepaymentProjPlanListService")
    RepaymentProjPlanListService repaymentProjPlanListService;
    
    @Autowired
    @Qualifier("RepaymentProjPlanListDetailService")
    RepaymentProjPlanListDetailService repaymentProjPlanListDetailService;
    


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
    public Result<PlanReturnInfoDto> creatAndSaveRepayPlan(@Validated  @RequestBody CreatRepayPlanReq creatRepayPlanReq){

        // =========   校验参数输入是否正确  开始  ================
//        logger.info("创建还款计划并将还款计划及业务和上标信息存储到数据库 接口 开始 输入的请求信息：[{}]", JSON.toJSONString(creatRepayPlanReq));
//        if(bindingResult.hasErrors()){
//            StringBuilder retErrMsg = new StringBuilder();
//            for (FieldError fieldError : bindingResult.getFieldErrors()) {
//                retErrMsg.append("   "+ fieldError.getDefaultMessage());
//            }
//            logger.info("@还款计划@创建还款计划并将还款计划及业务和上标信息存储到数据库 接口,输入参数校验错误：" ,retErrMsg.toString());
//            return Result.error("9889","输入参数校验错误："+retErrMsg.toString());
//        }

//        //== 校验业务基础信息
//        BusinessBasicInfoReq businessBasicInfoReq = creatRepayPlanReq.getBusinessBasicInfoReq();
//        Set<ConstraintViolation<BusinessBasicInfoReq>> bizInfovalit =
//                validator.validate( creatRepayPlanReq.getBusinessBasicInfoReq() );
//        if(bizInfovalit.size()>0){
//            logger.info("@还款计划@创建还款计划并将还款计划及业务和上标信息存储到数据库 接口,业务信息校验出错" , bizInfovalit.iterator().next().getMessage());
//            return Result.error("9889","业务信息校验出错："+bizInfovalit.iterator().next().getMessage());
////            constraintViolations.iterator().next().getMessage();
//        }
//
//
//        //  ==  校验业务用户信息
//
//        List<BusinessCustomerInfoReq> bizCusInfoReqs =creatRepayPlanReq.getBizCusInfoReqs();
//        for(BusinessCustomerInfoReq bizCusInfoReq:bizCusInfoReqs ){
//            Set<ConstraintViolation<BusinessCustomerInfoReq>> CusInfovalit =
//                    validator.validate( bizCusInfoReq );
//            if(bizInfovalit.size()>0){
//                logger.info("@还款计划@创建还款计划并将还款计划及业务和上标信息存储到数据库 接口,用户信息校验出错  传入信息： "+ JSON.toJSONString(bizCusInfoReq)+"  错误信息：" , bizInfovalit.iterator().next().getMessage());
//                return Result.error("9889","用户信息校验出错： 传入信息： "+JSON.toJSONString(bizCusInfoReq)+"     错误信息："+bizInfovalit.iterator().next().getMessage());
//            }
//        }
//
//        //  ===校验上标信息
////        /**
////         *  上标信息
////         */
////        @ApiModelProperty(required= true,value = "上标信息")
////        private  List<ProjInfoReq> projInfoReqs;
//        List<ProjInfoReq> projInfoReqs = creatRepayPlanReq.getProjInfoReqs();
//
//        for(ProjInfoReq  projInfoReq: projInfoReqs){
//            Set<ConstraintViolation<ProjInfoReq>> CusInfovalit =
//                    validator.validate( projInfoReq );
//            if(bizInfovalit.size()>0){
//                logger.info("@还款计划@创建还款计划并将还款计划及业务和上标信息存储到数据库 接口,上标信息校验出错  传入信息： "+ JSON.toJSONString(projInfoReq)+"  错误信息：" , bizInfovalit.iterator().next().getMessage());
//                return Result.error("9889","上标信息校验出错： 传入信息： "+JSON.toJSONString(projInfoReq)+"     错误信息："+bizInfovalit.iterator().next().getMessage());
//            }
//
//            //   ==  校验标的费用信息
////            /**
////             * 标的费用信息列表
////             */
////            @ApiModelProperty(required= true,value = "标的的出款费用信息列表")
////            private List<ProjFeeReq> projFeeInfos;
//            List<ProjFeeReq> projFeeInfos =projInfoReq.getProjFeeInfos();
//            for(ProjFeeReq projFeeReq:projFeeInfos){
//                Set<ConstraintViolation<ProjFeeReq>> projFeeReqValit =
//                        validator.validate( projFeeReq );
//                if(projFeeReqValit.size()>0){
//                    logger.info("@还款计划@创建还款计划并将还款计划及业务和上标信息存储到数据库 接口,标的的出款费用信息校验出错  传入信息： "+ JSON.toJSONString(projFeeReq)+"  错误信息：" , projFeeReqValit.iterator().next().getMessage());
//                    return Result.error("9889","标的的出款费用信息校验出错： 传入信息： "+JSON.toJSONString(projFeeReq)+"     错误信息："+projFeeReqValit.iterator().next().getMessage());
//                }
//            }
//
//            // 校验标的额外费率信息
//            List<ProjExtRateReq> projExtRateReqs = projInfoReq.getProjExtRateReqs();
//            if(projExtRateReqs!=null&&projExtRateReqs.size()>0){
//                for(ProjExtRateReq projExtRateReq:projExtRateReqs){
//                    Set<ConstraintViolation<ProjExtRateReq>> projExtRateReqValit =
//                            validator.validate( projExtRateReq );
//                    if(projExtRateReqValit.size()>0){
//                        logger.info("@还款计划@创建还款计划并将还款计划及业务和上标信息存储到数据库 接口,标的额外费率信息校验出错  传入信息： "+ JSON.toJSONString(projExtRateReq)+"  错误信息：" , projExtRateReqValit.iterator().next().getMessage());
//                        return Result.error("9889","标的额外费率信息校验出错： 传入信息： "+JSON.toJSONString(projExtRateReq)+"     错误信息："+projExtRateReqValit.iterator().next().getMessage());
//                    }
//                }
//            }
//
//        }



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

    @ApiOperation(value = "将一个业务还款计划同步到信贷 接口")
    @GetMapping("/pushOneBizRepayPlanToXD")
    @ResponseBody
    public Result pushOneBizRepayPlanToXD(String businessId){

       BasicBusiness  basicBusiness =  basicBusinessService.selectById(businessId);

       if(basicBusiness == null ){
           return Result.error(ExceptionCodeEnum.NULL.getValue().toString(),"没有这个还款计划");
       }
       if( !basicBusiness.getSrcType().equals(RepayPlanCreateSysEnum.ALMS.getValue())){
           return Result.error(ExceptionCodeEnum.NOT_THIS_SYS.getValue().toString(),"这个业务不是贷后系统生成的业务，不可同步");
       }

        try{
            shareProfitService.updateRepayPlanToLMS(businessId);
        }catch (Exception e){
            return Result.error(ExceptionCodeEnum.EXCEPTION.getValue().toString(),e.getMessage());
        }

        return Result.success();
    }




//    @ApiOperation(value = "测试时间转换")
//    @PostMapping("/testTime")
//    @ResponseBody
//    public Result<PlanReturnInfoDto> testTime() {
//        PlanReturnInfoDto dto = new PlanReturnInfoDto();
//        dto.setTestTime(new Date());
//        return Result.success(dto);
//    }

    public Result<TrailProjPlanDto> trailRepayPlanForProject(@Valid @RequestBody TrailRepayPlanReq trailRepayPlanReq , BindingResult bindingResult){
        Result<PlanReturnInfoDto> result = trailRepayPlan(trailRepayPlanReq,bindingResult);

        if(!result.getCode().equals("1")){
            return Result.error(result.getCode(),result.getMsg());
        }else{

            TrailProjPlanDto trailProjPlanDto = new TrailProjPlanDto();
            PlanReturnInfoDto planReturnInfoDto =result.getData();
            //最好用for循环处理

            List<RepaymentProjPlanDto> projPlanDtos  = planReturnInfoDto.getRepaymentBizPlanDtos().get(0).getProjPlanDtos();
            RepaymentProjPlanDto repaymentProjPlanDto =projPlanDtos.get(0);

//            for(RepaymentProjPlanDto repaymentProjPlanDto: projPlanDtos){
//                planReturnInfoDto = new
//            }
//            trailProjPlanDto.getBusinessId();
            //  根据repaymentProjPlanDto  组装  trailProjPlanDto

            return Result.success(trailProjPlanDto);

//            return
        }
    }





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
   public Result<List<BizDto>> getRepayList(@RequestBody @Validated  AppRepayListReq req){
    	List<String> needBusinessTypes=req.getBusinessTypes();
    	
    	 BasicBizCustomer customer=basicBizCustomerService.selectOne(new EntityWrapper<BasicBizCustomer>().eq("identify_card", req.getIdentifyCard()));
    	 if(customer==null) {
    		 return Result.error("9889", "找不到客户信息");
    	 }
    	 Integer isMainCustomer=customer.getIsmainCustomer();//是否主借款人
    	 req.setIsMainCustomer(isMainCustomer);
    	 try {
		    	List<TuandaiProjectInfo> infos=tuandaiProjectInfoService.selectList(new EntityWrapper<TuandaiProjectInfo>().eq("identity_card", req.getIdentifyCard()));
		    	List<BasicBusiness> basicBusinessList=new ArrayList();
		    	for(TuandaiProjectInfo info:infos) {
		    		BasicBusiness business=basicBusinessService.selectOne(new EntityWrapper<BasicBusiness>().eq("business_id", info.getBusinessId()));
		    		if(business!=null) {
		    			basicBusinessList.add(business);
		    		}
		    	}
		    	for(Iterator<BasicBusiness> it=basicBusinessList.iterator();it.hasNext();) {
		    		BasicBusiness business=it.next();
		    		BasicBusinessType businessType=basicBusinessTypeService.selectOne(new EntityWrapper<BasicBusinessType>().eq("business_type_id", business.getBusinessType()));
		    		boolean isContainsBusinessType=false;
		    		for(String needBusinessType:needBusinessTypes) {
		    			if(businessType.getXdBusinessTypeId().contains(needBusinessType)) {
		    				isContainsBusinessType=true;
		    				break;
			       		}else {
			       			isContainsBusinessType=false;
			       		}
		    		}
		    		if(!isContainsBusinessType) {
		    			it.remove();//过滤APP不需要的业务类型
		    		}
		    		
		    	}
		    	List<String> businessIds=new ArrayList();
		    	for(BasicBusiness business:basicBusinessList) {
		    		businessIds.add(business.getBusinessId());
		    	}
		        logger.info("根据业务ID列表查找出业务账单列表 开始，业务ID列表：[{}]", JSON.toJSONString(businessIds));
		
		       List<BizDto> bizDtos = new LinkedList<>();
		
		       for(String businessId:businessIds){
		           BizDto bizDto =getBizDtoByBizId(businessId,req);
		           if(bizDto.getPlanDtoList()==null) {
		        	   continue;
		           }
		           bizDtos.add(bizDto);
		       }
		        bizDtos=getPageList(bizDtos, req.getPageSize(), req.getCurPageNo());
		        logger.info("根据业务ID列表查找出业务账单列表 结束，返回数据：[{}]", JSON.toJSONString(bizDtos));
		
		       return Result.success(bizDtos);
    	 }catch(Exception e) {
    		  return Result.error("找出业务账单列表出错"+e);
    	 }
   }
    
    
    private List<BizDto> getPageList(List<BizDto> bizDtos,Integer pageSize,Integer curPage){
    	//总记录数
    	Integer totalCount=bizDtos.size();
        //分多少次处理
        Integer requestCount = totalCount / 2;
        List<BizDto> subList=null;
        for (int i = 0; i <= requestCount; i++) {
	    	Integer fromIndex=curPage-1;
	    	 //如果总数少于pageSize,为了防止数组越界,toIndex直接使用totalCount即可
	        int toIndex = Math.min(totalCount, (curPage) * pageSize);
	        if(i==(curPage-1)) {
                 subList = bizDtos.subList(fromIndex, toIndex);
          	}
	        //总数不到一页或者刚好等于一页的时候,只需要处理一次就可以退出for循环了
        if (toIndex == totalCount) {
            break;
        }
    }
		return subList;
        
        

    }
    
    

//    @ApiOperation(value = "根据业务ID列表查找出业务账单列表")
//    @PostMapping("/getRepayListNew")
//    @ResponseBody
//   public Result<List<BizDto>> getRepayListNew(@RequestBody List<String> businessIds){
//        logger.info("根据业务ID列表查找出业务账单列表 开始，业务ID列表：[{}]", JSON.toJSONString(businessIds));
//
//       List<BizDto> bizDtos = new LinkedList<>();
//
//       for(String businessId:businessIds){
//           BizDto bizDto =getBizDtoByBizId(businessId);
//           bizDtos.add(bizDto);
//
//       }
//        logger.info("根据业务ID列表查找出业务账单列表 结束，返回数据：[{}]", JSON.toJSONString(bizDtos));
//
//       return Result.success(bizDtos);
//   }


    @ApiOperation(value = "根据业务ID查找此业务的历史账单")
    @PostMapping("/getLogBill")
    @ResponseBody
   public  Result<BizDto> getLogBill(@RequestParam(value = "businessId") String businessId){

        logger.info("根据业务ID查找此业务的历史账单 开始，业务ID列表：[{}]", JSON.toJSONString(businessId));


        //业务基本的还款计划信息
        BizDto bizDto =getBizDtoByBizId(businessId,null);

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
                    bizPlanMap.put(bizPlan.getOriginalBusinessId(),bizPlanList);
                }
                bizPlanList.add(bizPlan);
            }
        }

        List<BizDto> renewBizDtos = new LinkedList<>();
        bizDto.setRenewBizs(renewBizDtos);
        for(String renewBizId:bizPlanMap.keySet()){
            BizDto renewBizDto =getBizDtoByBizId(renewBizId,null);
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
       RepaymentProjPlan projPlan=null;
       TuandaiProjectInfo tuandaiProjectInfo =null;
       boolean isNiwoPlatfrom=false;//是否你我金融平台
       if(bizPlan==null) {//如果为null，可能是你我金融的projPlanId
    	   projPlan=repaymentProjPlanService.selectById(planId);
    	   if(projPlan==null) {
    		   return Result.error("9889","未找到对应的还款计划");
    	   }else {
    		   isNiwoPlatfrom=true;
        	    tuandaiProjectInfo = tuandaiProjectInfoService.selectOne(new EntityWrapper<TuandaiProjectInfo>().eq("project_id",projPlan.getProjectId()));
        	   if(tuandaiProjectInfo == null){
                   return Result.error("9889","未找到对应的标信息");
               }
    	   }
       }else {
    	    tuandaiProjectInfo = tuandaiProjectInfoService.selectOne(new EntityWrapper<TuandaiProjectInfo>().eq("business_after_guid",bizPlan.getRepaymentBatchId()));
           if(tuandaiProjectInfo == null){
               return Result.error("9889","未找到对应的标信息");
           }

       }

       

      

        BizDto bizDto =  getBizDtoByBizId(tuandaiProjectInfo.getBusinessId(),null);
        BizPlanDto  bizPlanDto  = null;
        if(isNiwoPlatfrom) {
        	     bizPlanDto  = getProjPlanDtoByBizPlan(projPlan);
        }else {
        	     bizPlanDto  = getBizPlanDtoByBizPlan(bizPlan);
        }
     
        bizPlanDto.setBusinessType(bizDto.getBusinessType());
        bizPlanDto.setRepayWay(bizDto.getRepayWay());
        bizPlanDto.setBorrowMoney(tuandaiProjectInfo.getFullBorrowMoney());
        bizPlanDto.setBorrowLimit(bizDto.getBorrowLimit());
        bizPlanDto.setBorrowLimitUnit(bizDto.getBorrowLimitUnit());
        bizPlanDto.setInputTime(tuandaiProjectInfo.getQueryFullSuccessDate());

        logger.info("根据还款计划ID查找出此还款计划的详情账单信息 结束，返回数据：[{}]", JSON.toJSONString(bizPlanDto));

       return  Result.success(bizPlanDto);
    }





    
    @ApiOperation(value = "获取业务的还款计划信息进行消息推送")
    @PostMapping("/getMessage")
    @ResponseBody
    public Result<List<BizPlanListMessageDto>> getMessage(@RequestParam(value="messageType") String messageType,@RequestParam(value="dateCount") Integer dateCount){
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
       
       if(lists ==null||lists.size()==0){
           return Result.error("9889","未找到对应的还款计划信息");
       }else {
	       for(RepaymentBizPlanList list:lists) {
	    	   List<RepaymentBizPlanList> totalList=repaymentBizPlanListService.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("orig_business_id", list.getOrigBusinessId()));
	    	   Integer totalPeriods=totalList.size();//总期数
	    	   BasicBusiness business= basicBusinessService.selectOne(new EntityWrapper<BasicBusiness>().eq("business_id", list.getOrigBusinessId()));
	    	  if(business==null) {
	    		  continue;
	    	  }
	    	  BasicBusinessType type=basicBusinessTypeService.selectOne(new EntityWrapper<BasicBusinessType>().eq("business_type_id", business.getBusinessType()));
	    	  if(type==null) {
	    		  logger.info("businessId{},找不到对应的业务类型",business.getBusinessId());
	    		  continue;
	    	  }
	    	
	    	  List<BizOutputRecord> outputRecords=bizOutputRecordService.selectList(new EntityWrapper<BizOutputRecord>().eq("business_id", business.getBusinessId()).orderBy("fact_output_date"));
	    	  Date outputDate=null;
	    	  if(outputRecords!=null&&outputRecords.size()>0) {
	    		  outputDate=outputRecords.get(0).getFactOutputDate();
	    	  }
	    	
	    	  BizPlanListMessageDto message=new BizPlanListMessageDto(list.getBusinessId(), business.getCustomerName(), business.getCustomerIdentifyCard(), type.getBusinessTypeName(), outputDate, list.getTotalBorrowAmount(), list.getPeriod(), business.getBorrowMoney(), list.getDueDate(),totalPeriods);
	    	  CarBasic carBasic=null;
	    	  if(type.getBusinessTypeId()==BusinessTypeEnum.CYD_TYPE.getValue()||type.getBusinessTypeId()==BusinessTypeEnum.CYDZQ_TYPE.getValue()) {//如果是车贷的话，要关联车牌号码
	    		  carBasic=carBasicService.selectOne(new EntityWrapper<CarBasic>().eq("business_id", list.getOrigBusinessId()));
	    	  }
	    	  if(carBasic!=null) {
	    		  message.setCarNumber(carBasic.getLicensePlateNumber());
	    	  }
	    	  messages.add(message);
	       }   
       }
      
        logger.info("获取业务的还款计划信息进行消息推送  结束，返回数据：[{}]", JSON.toJSONString(messages));

       return  Result.success(messages);
    }








    /**
     * 根据业务ID取得此业务的还款计划信息返回
     * @param businessId
     * @return
     */
    private BizDto getBizDtoByBizId(String businessId,AppRepayListReq req){
        BizDto bizDto = new BizDto();

        BasicBusiness business = basicBusinessService.selectById(businessId);
        bizDto.setBusinessId(businessId);

        if(business ==null){
            throw  new MyException(ExceptionCodeEnum.NULL.getValue().toString(),"找不到业务信息，业务ID："+businessId);
        }

        List<TuandaiProjectInfo> ll= tuandaiProjectInfoService.selectList(new EntityWrapper<TuandaiProjectInfo>().eq("business_id",businessId).orderBy("queryFullsuccessDate",false));

        boolean isNiwoFlag=false;//判断是否你我金融的标识
        
        if(ll!=null&&ll.size()>0){
            bizDto.setPlateType(ll.get(0).getPlateType());
            //你我金融的单 返回主借款人的
//            if(bizDto.getPlateType().equals(2)){
//                bizDto.setIdentityCard(business.getCustomerIdentifyCard());
//            }
            if(bizDto.getPlateType().equals(2)){
            	isNiwoFlag=true;
            }
        }



        //有子类型则显示子类型（信用贷的按此处理）

        if(business.getBusinessType().equals(25)){
            bizDto.setBusinessType(business.getBusinessCtype());
        }else{
            //业务类型
            List<BasicBusinessType>  types =basicBusinessTypeService.selectList(new EntityWrapper<BasicBusinessType>().eq("business_type_id",business.getBusinessType()));
            bizDto.setBusinessType((types!=null&&types.size()>0)?types.get(0).getBusinessTypeName():null);
        }



        //还款方式
        bizDto.setRepayWay(RepayTypeEnum.nameOf(business.getRepaymentTypeId()));
        //借款总金额
        bizDto.setBorrowMoney(business.getBorrowMoney());
        //借款期限
        bizDto.setBorrowLimit(business.getBorrowLimit());
        //借款期限单位
        bizDto.setBorrowLimitUnit(RepayPlanBorrowLimitUnitEnum.nameOf(business.getBorrowLimitUnit()));
        //进件日期
        if(ll!=null&&ll.size()>0){
            bizDto.setInputTime(ll.get(0).getQueryFullSuccessDate());
        }else{
            bizDto.setInputTime(business.getInputTime());
        }
        List<RepaymentProjPlan> projPlans=null;
        List<RepaymentBizPlan> bizPlans=null;
        if(isNiwoFlag) {
        	  if(req!=null&&req.getIsSettle()!=null&&req.getIsSettle()==1) {//过滤结清数据
        		  if(req.getIsMainCustomer()!=null&&req.getIsMainCustomer()!=1) {//是共借人的话只能看自己的还款计划
        		        TuandaiProjectInfo info= tuandaiProjectInfoService.selectOne(new EntityWrapper<TuandaiProjectInfo>().eq("identity_card",req.getIdentifyCard()).eq("business_id", businessId));
        	       	    projPlans = repaymentProjPlanService.selectList(new EntityWrapper<RepaymentProjPlan>().eq("original_business_id",businessId).eq("project_id", info.getProjectId()).ne("plan_status", 10).ne("plan_status", 20).ne("plan_status", 30).orderBy("query_full_success_date"));
        		  }else {
        	            projPlans = repaymentProjPlanService.selectList(new EntityWrapper<RepaymentProjPlan>().eq("original_business_id",businessId).ne("plan_status", 10).ne("plan_status", 20).ne("plan_status", 30).orderBy("query_full_success_date"));
        		  }
        	  }else {
        		  if(req!=null&&req.getIsMainCustomer()!=null&&req.getIsMainCustomer()!=1) {//是共借人的话只能看自己的还款计划
	      		        TuandaiProjectInfo info= tuandaiProjectInfoService.selectOne(new EntityWrapper<TuandaiProjectInfo>().eq("identity_card",req.getIdentifyCard()).eq("business_id", businessId));
	      	            projPlans = repaymentProjPlanService.selectList(new EntityWrapper<RepaymentProjPlan>().eq("original_business_id",businessId).eq("project_id", info.getProjectId()).orderBy("query_full_success_date"));
	      		  }else {
     	 	            projPlans = repaymentProjPlanService.selectList(new EntityWrapper<RepaymentProjPlan>().eq("original_business_id",businessId).orderBy("query_full_success_date"));
                  }
          }
        }else {
        	 if(req!=null&&req.getIsSettle()!=null&&req.getIsSettle()==1) {//过滤结清数据
        	     bizPlans = repaymentBizPlanService.selectList(new EntityWrapper<RepaymentBizPlan>().eq("original_business_id",businessId).ne("plan_status", 10).ne("plan_status", 20).ne("plan_status", 30).orderBy("create_time"));
         	  }else {
         	     bizPlans = repaymentBizPlanService.selectList(new EntityWrapper<RepaymentBizPlan>().eq("original_business_id",businessId).orderBy("create_time"));
         	  }
      	
        }
        
        if((bizPlans!=null && bizPlans.size()>0)||(projPlans!=null&&projPlans.size()>0)){
            List<BizPlanDto> bizPlanDtos = new LinkedList<>();
            bizDto.setPlanDtoList(bizPlanDtos);
     
			if (!isNiwoFlag) {
				for (RepaymentBizPlan bizPlan : bizPlans) {
					BizPlanDto bizPlanDto = getBizPlanDtoByBizPlan(bizPlan);
					bizPlanDtos.add(bizPlanDto);
				}
			} else {
				for (RepaymentProjPlan projPlan : projPlans) {
					BizPlanDto bizPlanDto = getProjPlanDtoByBizPlan(projPlan);
					bizPlanDtos.add(bizPlanDto);
				}

			}
            
      
           
        }

        //根据标的结清和展期情况，设置业务的结清和展期情况
        Boolean isOver = true;
        Boolean hasDeffer =false;
        if(bizDto.getPlanDtoList()!=null) {
            for(BizPlanDto bizPlanDto :bizDto.getPlanDtoList()){
                if(!bizPlanDto.getIsOver()){
                    //其中一个还款计划未结清，则整个业务未结清
                    isOver =false;
                }
                if(bizPlanDto.getHasDeffer()){
                    //其中一个还款计划申请展期，则整个业务标志为申请展期
                    hasDeffer = true;
                }
            }
        }

        bizDto.setOver(isOver);
        bizDto.setHasDeffer(hasDeffer);

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
        BasicBusiness business=basicBusinessService.selectOne(new EntityWrapper<BasicBusiness>().eq("business_id", bizPlan.getOriginalBusinessId()));
        bizPlanDto.setPlateType(ProjPlatTypeEnum.TUANDAI.getKey());//团贷网
        bizPlanDto.setIdentifyCard(business.getCustomerIdentifyCard());//主借款人身份证号码
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
            BizPlanListDto bizPlanListDto = new BizPlanListDto(bizPlanList);
            List<RepaymentBizPlanListDetail> detials = repaymentBizPlanListDetailService.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().eq("plan_list_id",bizPlanList.getPlanListId()));
            BigDecimal realPay = new BigDecimal("0");
            if(detials!=null&&detials.size()>0){
                for(RepaymentBizPlanListDetail detail:detials){
                    if(detail.getFactAmount()!=null){
                        realPay =  realPay.add(detail.getFactAmount());
                    }
                }
            }
            bizPlanListDto.setFactPayAmount(realPay);

            if(bizPlanList.getCurrentStatus().equals(RepayPlanStatus.REPAYED.getName())){

                payedPeroids.add(bizPlanListDto);
            }else {
                payingPeroids.add(bizPlanListDto);
            }
        }

        return  bizPlanDto;
    }
    
    
    /**
     * 根据标的还款计划取得返回的还款计划信息
     * @param projPlan
     * @return
     */
    private   BizPlanDto getProjPlanDtoByBizPlan(RepaymentProjPlan projPlan){
        BizPlanDto bizPlanDto = new BizPlanDto();
        bizPlanDto.setPlanId(projPlan.getProjPlanId());
        TuandaiProjectInfo tuandaiProjectInfo = tuandaiProjectInfoService.selectOne(new EntityWrapper<TuandaiProjectInfo>().eq("project_id",projPlan.getProjectId()));
        
        bizPlanDto.setPlateType(ProjPlatTypeEnum.NIWO_JR.getKey());//你我金融
        bizPlanDto.setIdentifyCard(tuandaiProjectInfo.getIdentityCard());
        
        //判断是否已结清
        if (RepayPlanSettleStatusEnum.payed(projPlan.getPlanStatus())) {
            bizPlanDto.setIsOver(true);
        } else {
            bizPlanDto.setIsOver(false);
        }

        //判断是否已申请展期
        if (RepayPlanSettleStatusEnum.renewed(projPlan.getPlanStatus())) {
            bizPlanDto.setHasDeffer(true);
        } else {
            bizPlanDto.setHasDeffer(false);
        }


        List<RepaymentProjPlanList> projPlanLists = repaymentProjPlanListService.selectList(
                new EntityWrapper<RepaymentProjPlanList>().eq("proj_plan_id",projPlan.getProjPlanId()).orderBy("due_date"));

        //未结清且未展期的业务计算当前期
        if(!bizPlanDto.getHasDeffer()&&!bizPlanDto.getIsOver()){
            RepaymentProjPlanList currentPlan = repaymentProjPlanService.findCurrentPeriod(new Date(), projPlanLists);
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

        for (RepaymentProjPlanList projPlanList:projPlanLists){
            BizPlanListDto bizPlanListDto = new BizPlanListDto(projPlanList);
            List<RepaymentProjPlanListDetail> detials = repaymentProjPlanListDetailService.selectList(new EntityWrapper<RepaymentProjPlanListDetail>().eq("proj_plan_list_id",projPlanList.getProjPlanListId()));
            BigDecimal realPay = new BigDecimal("0");
            if(detials!=null&&detials.size()>0){
                for(RepaymentProjPlanListDetail detail:detials){
                    if(detail.getProjFactAmount()!=null){
                        realPay =  realPay.add(detail.getProjFactAmount());
                    }
                }
            }
            bizPlanListDto.setFactPayAmount(realPay);

            if(projPlanList.getCurrentStatus().equals(RepayPlanStatus.REPAYED.getName())){

                payedPeroids.add(bizPlanListDto);
            }else {
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


	public static void main(String[] args) {
	    List<Long> datas = Arrays.asList(new Long [] {1L,2L,3L,4L,5L,6L,7L,8L});
		//总记录数
    	Integer totalCount=datas.size();
	    //分多少次处理
        Integer requestCount = totalCount / 2;

        for (int i = 0; i <= requestCount; i++) {
        
	    	Integer fromIndex=i*2;
	    	 //如果总数少于pageSize,为了防止数组越界,toIndex直接使用totalCount即可
	        int toIndex = Math.min(totalCount, (i+1) * 2);
	        List<Long> subList = datas.subList(fromIndex, toIndex);
	
	         if(i==3) {
              System.out.println(subList);	
        	}
	        //总数不到一页或者刚好等于一页的时候,只需要处理一次就可以退出for循环了
	        if (toIndex == totalCount) {
	            break;
	        }
        }
    	
		
	}

}
