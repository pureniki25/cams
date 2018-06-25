package com.hongte.alms.core.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.*;
import com.hongte.alms.base.enums.BusinessTypeEnum;
import com.hongte.alms.base.enums.SysParameterTypeEnums;
import com.hongte.alms.base.enums.repayPlan.RepayPlanSettleStatusEnum;
import com.hongte.alms.base.service.*;
import com.hongte.alms.base.vo.module.BasicBusinessVo;
import com.hongte.alms.base.vo.module.LiquidationVO;
import com.hongte.alms.base.vo.module.LitigationVO;
import com.hongte.alms.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 业务基本信息 前端控制器
 * </p>
 *
 * @author 曾坤
 * @since 2018-02-1
 */
@RestController
@RequestMapping("/BasicBusinessController")
@Api(tags = "BasicBusinessController", description = "业务信息相关接口")
public class BasicBusinessController {


    private Logger logger = LoggerFactory.getLogger(BasicBusinessController.class);

    @Autowired
    @Qualifier("BasicBusinessService")
    BasicBusinessService basicBusinessService;

    @Autowired
    @Qualifier("RepaymentBizPlanService")
    RepaymentBizPlanService repaymentBizPlanService;


    @Autowired
    @Qualifier("SysParameterService")
    SysParameterService sysParameterService;
    
    @Autowired
    @Qualifier("CarDragService")
    CarDragService carDragService;
    
    @Autowired
    @Qualifier("CarReturnRegService")
    CarReturnRegService carReturnRegService;
    
    @ApiOperation(value = "根据ID查找")
    @GetMapping("/selectById")
    @ResponseBody
    public Result<BasicBusinessVo> selectById(@RequestParam("id") String id){

        try{

            List<BasicBusiness> businesses =  basicBusinessService.selectList(new EntityWrapper<BasicBusiness>().eq("business_id",id));

            BasicBusiness business =  basicBusinessService.selectById(id);
            BasicBusinessVo vo = BasicBusinessVo.creatByClone(business);

            List<SysParameter> repartmentType = sysParameterService.selectList(new EntityWrapper<SysParameter>().eq("param_type", SysParameterTypeEnums.REPAYMENT_TYPE.getKey()).eq("param_value",business.getRepaymentTypeId()));
            List<SysParameter> unit = sysParameterService.selectList(new EntityWrapper<SysParameter>().eq("param_type", SysParameterTypeEnums.BORROW_LIMIT_UNIT.getKey()).eq("param_value",business.getBorrowLimitUnit()));

            //根据业务还款计划判断业务还款状态
            List<RepaymentBizPlan> plans =  repaymentBizPlanService.selectList(new EntityWrapper<RepaymentBizPlan>().eq("business_id",business.getBusinessId()));
            vo.setStatus("已结清");
            for(RepaymentBizPlan plan: plans){
                if(plan.getPlanStatus().equals(RepayPlanSettleStatusEnum.REPAYINF.getValue())){
                    vo.setStatus("还款中");
                    break;
                }
            }


            if(repartmentType.size()>0){
                vo.setRepaymentTypeName(repartmentType.get(0).getParamName());
            }
            if(unit.size()>0){
                vo.setBorrowLimitStr(business.getBorrowLimit()+unit.get(0).getParamName());
            }

            List<SysParameter> collectionTrackLogStatus = sysParameterService.selectList(new EntityWrapper<SysParameter>().eq("param_type", SysParameterTypeEnums.COLLECTION_FOLLOW_STATUS.getKey()));
            vo.setCollectionTackLogStatu(collectionTrackLogStatus);

            if(business==null){

                return Result.error("500","查询失败");
            }else{
                return Result.success(vo);
            }
        }catch (Exception ex){
            logger.error(ex.getMessage());
            return Result.error("500", ex.getMessage());
        }

    }
    
    
    @ApiOperation(value="获取清算一分配信息")
    @GetMapping("/selectLiquidationOne")
    public Result<Map<String,JSONArray>> selectLiquidationOne(
    		@RequestParam("crpId") String crpId
    ) {
        logger.info("@贷后详情页面@获取清算一分配信息--开始[{}]" , crpId);
        Map<String,JSONArray> retMap = new HashMap<String,JSONArray>();
        List<LiquidationVO> liquidationOnes= basicBusinessService.selectLiquidationOne(crpId);
        retMap.put("liquidationOnes",(JSONArray) JSON.toJSON(liquidationOnes));
        logger.info("\"@贷后详情页面@获取清算一分配信息--结束[{}]" , retMap);
        return Result.success(retMap);
    }
    
    
    @ApiOperation(value="获取清算二分配信息")
    @GetMapping("/selectLiquidationTwo")
    public Result<Map<String,JSONArray>> selectLiquidationTwo(
    		@RequestParam("crpId") String crpId
    ) {
        logger.info("@贷后详情页面@获取清算二分配信息--开始[{}]" , crpId);
        Map<String,JSONArray> retMap = new HashMap<String,JSONArray>();
        List<LiquidationVO> liquidationTwos= basicBusinessService.selectLiquidationTwo(crpId);
        retMap.put("liquidationTwos",(JSONArray) JSON.toJSON(liquidationTwos));
        logger.info("\"@贷后详情页面@获取清算二分配信息--结束[{}]" , retMap);
        return Result.success(retMap);
    }
    
    
    
    @ApiOperation(value="获取移交法务信息")
    @GetMapping("/selectLitigation")
    public Result<Map<String,JSONArray>> selectLitigation(
    		@RequestParam("crpId") String crpId,@RequestParam("businessTypeId") Integer businessTypeId
    ) {
    	

       logger.info("@贷后详情页面@获取移交法务信息--开始[{}]" , crpId);
       Map<String,JSONArray> retMap = new HashMap<String,JSONArray>();
    	List<LitigationVO> litigations=null;
    	if(BusinessTypeEnum.CYD_TYPE.getValue()==businessTypeId||BusinessTypeEnum.CYDZQ_TYPE.getValue()==businessTypeId) {
    	    litigations= basicBusinessService.selectLitigationCarVO(crpId);
    	}else {
    		litigations= basicBusinessService.selectLitigationHouseVO(crpId);

     }
        retMap.put("litigations",(JSONArray) JSON.toJSON(litigations));
        logger.info("\"@贷后详情页面@获取移交法务信息--结束[{}]" , retMap);
        return Result.success(retMap);
    }
    
    
    
    @ApiOperation(value="获取拖车登记,车辆归还记录")
    @GetMapping("/selectCarInfo")
    public Result<Map<String,Object>> selectCarInfo(
    		@RequestParam("businessId") String businessId
    ) {
    	
       logger.info("@贷后详情页面@获取拖车登记,车辆归还记录--开始[{}]" , businessId);
       Map<String,Object> retMap = new HashMap<String,Object>();
    	List<LitigationVO> litigations=null;
    	
    	List<CarDrag> carDrags=carDragService.selectList(new EntityWrapper<CarDrag>().eq("business_id", businessId).orderBy("drag_date", false));
    	if(carDrags!=null&&carDrags.size()>0) {
    		CarDrag carDrag=carDrags.get(0);
    		CarReturnReg carReturnReg=carReturnRegService.selectOne(new EntityWrapper<CarReturnReg>().eq("business_id", businessId).eq("drag_id", carDrag.getId()));
    		if(carReturnReg==null) {
    		    retMap.put("carDrag",JSON.toJSON(carDrag));
    		}else {
    		    retMap.put("carReturnReg",JSON.toJSON(carReturnReg));
    		}
    	}
        logger.info("\"@贷后详情页面@获取拖车登记,车辆归还记录--结束[{}]" , retMap);
        return Result.success(retMap);
    }
}


