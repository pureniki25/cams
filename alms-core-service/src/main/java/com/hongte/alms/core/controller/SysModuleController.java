package com.hongte.alms.core.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.SysModule;
import com.hongte.alms.base.service.SysModuleService;
import com.hongte.alms.base.vo.module.ModulePageReq;
import com.hongte.alms.base.vo.module.moduleManageVO;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.FieldValidator;

import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import com.hongte.alms.core.vo.labelValue;
import com.hongte.alms.core.vo.modules.NavMenuVo;
import com.hongte.alms.core.vo.modules.moduleEditVo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import com.hongte.alms.common.vo.PageResult;

import javax.annotation.Resource;

/**
 * <p>
 * 菜单信息表 前端控制器
 * </p>
 *
 * @author 黄咏康
 * @since 2018-01-03
 */

@RestController
@RequestMapping("")
@Api(tags = "TbModuleControllerApi", description = "系统菜单相关api描述", hidden = true)
public class SysModuleController {
    private Logger logger = LoggerFactory.getLogger(SysModuleController.class);

    @Autowired
    @Qualifier("SysModuleService")
    SysModuleService moduleService;

    /**
     * 根据id查询指定菜单信息<br>
     *
     * @param moduleId 菜单编号
     * @return 菜单信息
     * @author 黄咏康
     * @date 2018年01月06日 上午10:51:44
     * @since alms-core-service 1.0-SNAPSHOT
     */
    @ApiOperation(value = "根据菜单编号查询菜单信息")
    @ApiImplicitParam(paramType = "query", name = "moduleId", dataType = "String", required = true, value = "菜单编号")
    @GetMapping("/modules/getModuleById")
    public Result<moduleEditVo> getModuleById(@RequestParam String moduleId) {
        if (FieldValidator.isEmpty(moduleId)) {
            return Result.error("400", "菜单编号不能为空");
        }

        moduleEditVo result = new moduleEditVo();

        SysModule module = moduleService.selectById(moduleId);
        if(module==null){
            return Result.error("400", "不存在此菜单");
        }
        result.setParentModuleName(module.getModulePid());
        result.setDeviceType(module.getDeviceType());
        result.setModuleName(module.getModuleName());
        result.setModuleLevel(module.getModuleLevel().toString());
        result.setModuleDesc(module.getModuleDesc());
        result.setModuleIcon(module.getModuleIcon());
        result.setModuleId(module.getModuleId());
        result.setModuleOrder(module.getModuleOrder().toString());
        result.setDeviceType(module.getDeviceType());
        result.setModuleUrl(module.getModuleUrl());
        result.setModuleStatus(module.getModuleStatus());
        return Result.success(result);
    }

    /**
     * 获取全部菜单
     * @return
     */
    @ApiOperation(value="获取子菜单")
    @GetMapping("/modules/children")
    public Result<List<NavMenuVo>> getModules(@RequestParam("parentId")String parentId) {
        List<NavMenuVo> navMenuList = new ArrayList<NavMenuVo> ();
        List<SysModule> parentModuleList = moduleService.selectList(new EntityWrapper<SysModule>().eq("module_pid", parentId));
        for (SysModule parent : parentModuleList) {
            NavMenuVo parentNavMenu = new NavMenuVo();
            parentNavMenu.setId(parent.getModuleId());
            parentNavMenu.setTitle(parent.getModuleName());
            parentNavMenu.setIcon("");
            parentNavMenu.setUrl(parent.getModuleUrl());
            parentNavMenu.setSpread(true);
            List<SysModule> children = moduleService.selectList(new EntityWrapper<SysModule>().eq("module_pid", parent.getModuleId()).orderBy("module_order",true));
            List<NavMenuVo> navMenuChildren = new ArrayList<NavMenuVo> ();
            for (SysModule child : children) {
                NavMenuVo childNavMenu = new NavMenuVo();
                childNavMenu.setId(child.getModuleId());
                childNavMenu.setTitle(child.getModuleName());
                childNavMenu.setIcon("");
                childNavMenu.setUrl(child.getModuleUrl());
                navMenuChildren.add(childNavMenu);
            }
            parentNavMenu.setChildren(navMenuChildren);
            navMenuList.add(parentNavMenu);

        }
        return Result.success(navMenuList);
    }

    @ApiOperation(value="获取一级菜单")
    @GetMapping("/modules/onelevel")
    public Result<List<NavMenuVo>> oneLevel()
    {
        List<NavMenuVo> oneLevelNavMenuList = new ArrayList<NavMenuVo> ();
        List<SysModule> parentModuleList = moduleService.selectList(new EntityWrapper<SysModule>().eq("module_pid", "0").orderBy("module_order",true));
        for (SysModule parent : parentModuleList) {
            NavMenuVo parentNavMenu = new NavMenuVo();
            parentNavMenu.setId(parent.getModuleId());
            parentNavMenu.setTitle(parent.getModuleName());
            parentNavMenu.setIcon("");
            parentNavMenu.setSpread(false);
            oneLevelNavMenuList.add(parentNavMenu);
        }
       return Result.success(oneLevelNavMenuList);
    }

    @ApiOperation(value="获取可选父级菜单")
    @ApiImplicitParam(paramType = "query", name = "moduleLevel", dataType = "int", required = true, value = "当前菜单级别")
    @GetMapping("/modules/queryVaildParentModule")
    public Result<List<labelValue>> getParentModule(@RequestParam("moduleLevel") int moduleLevel)
    {
        int p_level = (moduleLevel-1);
        List<labelValue> result = new ArrayList<>();
        List<SysModule> parentModuleList =
                moduleService.selectList(new EntityWrapper<SysModule>()
                .eq("module_level", p_level)
                .eq("module_status",1)
                .orderBy("create_time",false));
        for (SysModule parent : parentModuleList) {
            labelValue item = new labelValue();
            item.setLabel(parent.getModuleName());
            item.setValue(parent.getModuleId());
            result.add(item);
        }
        return Result.success(result);
    }

    /**
     * 获取分页菜单数据
     * @param req 分页请求数据
     * @author 黄咏康
     * @date 2018年01月15日 上午10:51:44
     * @return 菜单分页数据
     */
    @ApiOperation(value = "获取菜单分页数据")
    @GetMapping("/modules/queryModuleList")
    public PageResult<List<moduleManageVO>> queryModuleList(@ModelAttribute ModulePageReq req){
        logger.info(req.getModuleName()+","+req.getModuleLevel());
        Page<moduleManageVO> pages = new Page<>();
        pages.setCurrent(req.getPage());
        pages.setSize(req.getLimit());
        try{
            pages = moduleService.selectModulePage(pages,req);
            return PageResult.success(pages.getRecords(),pages.getTotal());
        }catch (Exception ex){
            logger.error(ex.getMessage());
            return PageResult.error(500, "数据库访问异常");
        }
    }

    @ApiOperation(value = "新增菜单")
    @PostMapping("/modules/addOrUpdateModule")
    public Result<Integer> addOrUpdateModule(@RequestBody moduleEditVo req){
        try{
            if(req.getModuleLevel()=="1") {
                req.setParentModuleName("");
            }
            String loginUser = "admin";
            Date createTime = new Date();
            UUID uuid=UUID.randomUUID();
            boolean isAdd=true;

            SysModule newModule = new SysModule();
            if(req.getModuleId()!=null){
                SysModule checkModule =moduleService.selectOne(new EntityWrapper<SysModule>().eq("module_id",req.getModuleId()));
                if(checkModule!=null){
                    newModule = checkModule;
                    isAdd=false;
                }
            }
            if(isAdd){
                newModule.setModuleId(uuid.toString());
                newModule.setCreateTime(createTime);
                newModule.setCreateUser(loginUser);
            }

            newModule.setDeviceType(req.getDeviceType());
            newModule.setModuleDesc(req.getModuleDesc());
            newModule.setModuleIcon(req.getModuleIcon());
            newModule.setModuleLevel(Integer.valueOf(req.getModuleLevel()));
            newModule.setModuleName(req.getModuleName());
            newModule.setModulePid(req.getParentModuleName());
            newModule.setModuleUrl(req.getModuleUrl());
            newModule.setModuleStatus(req.isModuleStatus());
            newModule.setModuleOrder(Integer.valueOf(req.getModuleOrder()));
            newModule.setModuleDesc(req.getModuleDesc());
            newModule.setUpdateTime(createTime);
            newModule.setUpdateUser(loginUser);
            newModule.insertOrUpdate();

            moduleService.insertOrUpdate(newModule);
            return Result.success(1);
        }catch (Exception ex){
            logger.error(ex.getMessage());
            return Result.error("500", ex.getMessage());
        }
    }

    @ApiOperation(value = "删除指定列表的菜单")
    @ApiImplicitParam(allowMultiple = true,required = true,name = "moduleIdList", paramType = "query", dataType = "string")
    @DeleteMapping("/modules/DelModule")
    public Result<Integer> DelModule(@RequestParam List<String> moduleIdList){
         if(moduleIdList==null || moduleIdList.isEmpty()){
             return Result.error("500","请选择要删除的菜单列表");
         }
         try{
             EntityWrapper<SysModule> ew = new EntityWrapper<SysModule>();
             ew.in("module_id",moduleIdList.toArray());
             //logger.info(ew.getSqlSegment());
             boolean isSuccess = moduleService.delete(ew);
             if(isSuccess){
                 return Result.success(1);
             }else{
                 return Result.error("500","删除失败");
             }
         }catch (Exception ex){
             logger.error(ex.getMessage());
             return Result.error("500",ex.getMessage());
         }
    }
}
