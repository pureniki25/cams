package com.hongte.alms.core.controller;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.hongte.alms.base.entity.ProjExtRate;
import com.hongte.alms.base.enums.repayPlan.PepayPlanProjExtRatCalEnum;
import com.hongte.alms.base.service.ProjExtRateService;
import com.hongte.alms.base.vo.project.ProjExtRateVO;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.vo.PageResult;
import com.ht.ussp.bean.LoginUserInfoHelper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;

/**
 * 标的额外信息管理 
 * Created by 张贵宏 on 2018/7/23 11:16
 */
@Api(description="标的额外信息管理")
@Slf4j
@RestController
@RequestMapping("/projExtRate")
public class ProjExtRateController extends BaseController {
    @Autowired
    @Qualifier("ProjExtRateService")
    private ProjExtRateService projExtRateService;

    // @RequestMapping("/list")
    // public String list(){
    //     return "finance/projExtRate";
    // }

    @ApiOperation("标的额外费用计算方式列表")
    @PostMapping("/findProjExtRateCalWay")
    public Result findProjExtRateCalWay(){
        List<Map<String,Object>> lists = Lists.newArrayList();
        for(PepayPlanProjExtRatCalEnum enu : PepayPlanProjExtRatCalEnum.values()){
            Map<String,Object> tempMap = Maps.newHashMap();
            tempMap.put("value", enu.getValue());
            tempMap.put("desc", enu.getDesc());
            lists.add(tempMap);
        }
        return Result.success(lists);
    }

    @ApiOperation("查询")
    @PostMapping("/search")
    public PageResult<List<ProjExtRateVO>> search(@RequestBody Page<ProjExtRate> page){
       try {
           page.setOrderByField("createTime").setAsc(false);
           projExtRateService.selectByPage(page);
           List<ProjExtRateVO> voList = Lists.newArrayList();
           for(ProjExtRate per : page.getRecords()){
               voList.add(new ProjExtRateVO(per));
           }
           return PageResult.success(voList, page.getTotal());
       } catch (Exception e) {
           log.error(e.getMessage(), e);
           return PageResult.error(500, e.getMessage());
       }
    }

    @ApiOperation("编辑")
    @PostMapping("/edit")
    public Result edit(@RequestBody @Valid ProjExtRateVO projExtRateVo, BindingResult bindingResult){
        try {
            //参数验证
            // if(bindingResult.hasErrors()){
            //     StringBuilder sb= new StringBuilder();
            //     for(ObjectError objectError : bindingResult.getAllErrors()){
            //         sb.append(objectError.getDefaultMessage()+"<br />");
            //     }
            //     return Result.error(sb.toString());
            // }

            if(projExtRateVo.getId()==null || projExtRateVo.getId() == 0){
            	//验证
            	EntityWrapper<ProjExtRate> ew = new EntityWrapper<ProjExtRate>();
            	ew.eq("business_id", projExtRateVo.getBusinessId());
            	ew.eq("project_id", projExtRateVo.getProjectId());
            	ew.eq("rate_type", projExtRateVo.getRateType());
            	int count = projExtRateService.selectCount(ew);
            	if(count > 0) {
            		return Result.error("已经存在相同的业务、标ID、费率类型记录.");
            	}
                //新增
                ProjExtRate projExtRate = new ProjExtRate();
                BeanUtil.copyProperties(projExtRateVo, projExtRate);
                super.setCreateUserInfo(projExtRate);
                projExtRate.insert();
            }else{
                //更新
                ProjExtRate oldProjExtRate = projExtRateService.selectById(projExtRateVo.getId());
                BeanUtil.copyProperties(projExtRateVo, oldProjExtRate, new CopyOptions(){{ ignoreNullValue=true;}});
                super.setUpdateUserInfo(oldProjExtRate);
                oldProjExtRate.updateById();
            }

            return Result.success();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }

    @ApiOperation("删除")
    @GetMapping("/delete")
    public Result delete(Integer id){
        try {
            projExtRateService.deleteById(id);
            return Result.success();
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return Result.error(e.getMessage());
        }
    }
}