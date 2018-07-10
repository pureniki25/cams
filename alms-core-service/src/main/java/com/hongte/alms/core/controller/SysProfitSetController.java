package com.hongte.alms.core.controller;

import java.util.List;
import java.util.Map;

import io.swagger.annotations.Api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.BasicBusinessType;
import com.hongte.alms.base.entity.ProfitFeeSet;
import com.hongte.alms.base.entity.ProfitItemSet;
import com.hongte.alms.base.entity.RepaymentBizPlanListDetail;
import com.hongte.alms.base.entity.SysParameter;
import com.hongte.alms.base.enums.SysParameterTypeEnums;
import com.hongte.alms.base.service.BasicBusinessTypeService;
import com.hongte.alms.base.service.ProfitFeeSetService;
import com.hongte.alms.base.service.ProfitItemSetService;
import com.hongte.alms.base.service.RepaymentBizPlanListDetailService;
import com.hongte.alms.base.service.SysParameterService;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.JsonUtil;
import com.hongte.alms.common.util.StringUtil;

import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 分润设置控制器
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-04-28
 */
@RestController
@RequestMapping("/sys/profit")
@Api(tags = "SysProfitSetController", description = "分润顺序设置接口")
public class SysProfitSetController {
	  private Logger logger = LoggerFactory.getLogger(SysProfitSetController.class);
	@Autowired
	@Qualifier("BasicBusinessTypeService")
	BasicBusinessTypeService basicBusinessTypeService;
	
	@Autowired
	@Qualifier("SysParameterService")
	SysParameterService sysParameterService;
	
	@Autowired
	@Qualifier("ProfitItemSetService")
	ProfitItemSetService profitItemSetService;
	
	@Autowired
	@Qualifier("ProfitFeeSetService")
	ProfitFeeSetService profitFeeSetService;
	@Autowired
	@Qualifier("RepaymentBizPlanListDetailService")
	RepaymentBizPlanListDetailService repaymentBizPlanListDetailService;

	@GetMapping("/list")
	@ResponseBody
	@ApiOperation(value = "列出所有业务类型")
	public Result list() {
		List<BasicBusinessType> list = basicBusinessTypeService.selectList(new EntityWrapper<BasicBusinessType>());
		Result result = new Result<>();
		result.setCode("0");
		result.setData(list);
		return result;
	}

	@GetMapping("/itemTypeList")
	@ResponseBody
	@ApiOperation(value = "项目所属分类")
	public Result itemTypeList(@RequestParam String businessTypeId) {
        //项目费用类型
		Result result = new Result<>();
        List<SysParameter> list =  sysParameterService.selectList(new EntityWrapper<SysParameter>().eq("param_type", SysParameterTypeEnums.PROFIT_ITEM_TYPE.getKey()).eq("status",1).orderBy("row_Index"));
    	if(StringUtil.notEmpty(businessTypeId)) {
	        List<ProfitItemSet> itemTypes =profitItemSetService.selectList(new EntityWrapper<ProfitItemSet>().eq("business_type_id", businessTypeId));
	    	if(itemTypes.size()>0) {
	    		result.setCode("1");
	    		result.setData(itemTypes);
	    	}else {
				result.setCode("0");
				result.setData(list);
    	}
    	}else {
    		result.setCode("0");
			result.setData(list);
    	}
		return result;
	}
	@GetMapping("/feeTypeList")
	@ResponseBody
	@ApiOperation(value = "费用项类型")
	public Result feeTypeList(@RequestParam String businessTypeId,@RequestParam String itemType,@RequestParam String profitItemSetId) {
        //项目费用类型
		    Result result = new Result<>();
            List<RepaymentBizPlanListDetail> list=repaymentBizPlanListDetailService.selectList(new EntityWrapper<RepaymentBizPlanListDetail>().eq("plan_item_type", itemType).groupBy("fee_id"));
            List<ProfitFeeSet> feeTypeList=  profitFeeSetService.selectList(new EntityWrapper<ProfitFeeSet>().eq("profit_item_set_id", profitItemSetId).groupBy("fee_id"));
        	if(feeTypeList.size()>0) {
	    		result.setCode("1");
	    		result.setData(feeTypeList);
	    	}else {
				result.setCode("0");
				result.setData(list);
		    	}
		   
    	
		return result;
	}

	@PostMapping("/saveItem")
	@ResponseBody
	@ApiOperation(value = "保存项目所属分类顺序")
	public Result update(   @RequestBody Map<String, Object> reqMap) {
      	Integer businessTypeId=reqMap.get("businessTypeId")==null?0:Integer.valueOf(reqMap.get("businessTypeId").toString());//业务类型ID
     	String type=reqMap.get("type")==null?"add":reqMap.get("type").toString();
     
     	if("add".equals(type)) {
     	  	List<SysParameter>  itemTypes=JsonUtil.map2objList(reqMap.get("itemTypes"), SysParameter.class);
         	profitItemSetService.saveItemTypes(itemTypes, businessTypeId);	
     	}
     	if("edit".equals(type)) {
     		List<ProfitItemSet>  itemTypes=JsonUtil.map2objList(reqMap.get("itemTypes"), ProfitItemSet.class);
         	profitItemSetService.updateItemTypes(itemTypes);
     	}

  
       	     
			return Result.success();
	}
	
	
	@PostMapping("/saveFee")
	@ResponseBody
	@ApiOperation(value = "保存费用项分润顺序")
	public Result updateFee(   @RequestBody Map<String, Object> reqMap) {
      	Integer businessTypeId=reqMap.get("businessTypeId")==null?0:Integer.valueOf(reqMap.get("businessTypeId").toString());//业务类型ID
     	String type=reqMap.get("type")==null?"add":reqMap.get("type").toString();
      	String profitItemSetId=reqMap.get("profitItemSetId")==null?"":reqMap.get("profitItemSetId").toString();
     

          	if("add".equals(type)) {
        	  	List<RepaymentBizPlanListDetail>  feeTypes=JsonUtil.map2objList(reqMap.get("feeTypes"), RepaymentBizPlanListDetail.class);
               	profitFeeSetService.saveFeeTypes(feeTypes, businessTypeId,profitItemSetId);	
         	}
         	if("edit".equals(type)) {
         		List<ProfitFeeSet>  feeTypes=JsonUtil.map2objList(reqMap.get("feeTypes"), ProfitFeeSet.class);
               	profitFeeSetService.updateFeeTypes(feeTypes);	
         	}

  
       	     
			return Result.success();
	}

}
