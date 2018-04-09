package com.hongte.alms.core.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.SysParameter;
import com.hongte.alms.base.enums.SysParameterTypeEnums;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.base.service.SysParameterService;
import com.hongte.alms.base.vo.module.BasicBusinessVo;
import com.hongte.alms.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    @Qualifier("SysParameterService")
    SysParameterService sysParameterService;


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

}

