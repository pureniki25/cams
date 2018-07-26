package com.hongte.alms.core.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import com.baomidou.mybatisplus.entity.Columns;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.collect.Lists;
import com.hongte.alms.base.entity.BasicCompany;
import com.hongte.alms.base.entity.SysUser;
import com.hongte.alms.base.entity.SysUserArea;
import com.hongte.alms.base.enums.AreaLevel;
import com.hongte.alms.base.service.BasicCompanyService;
import com.hongte.alms.base.service.SysUserAreaService;
import com.hongte.alms.base.service.SysUserPermissionService;
import com.hongte.alms.base.service.SysUserService;
import com.hongte.alms.base.vo.module.UserAreaReq;
import com.hongte.alms.base.vo.module.doc.BasicCompanyVo;
import com.hongte.alms.common.exception.ExceptionCodeEnum;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.vo.PageResult;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 用户管理的区域表 前端控制器
 * </p>
 *
 * @author 王继光
 * @since 2018-03-20
 */
@RestController
@RequestMapping("/sys/userarea")
@Api(tags = "SysUserAreaController", description = "系统用户区域设置相关接口")
public class SysUserAreaController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SysUserAreaController.class);

	@Autowired
	@Qualifier("SysUserAreaService")
	SysUserAreaService sysUserAreaService;

	@Autowired
	@Qualifier("BasicCompanyService")
	BasicCompanyService basicCompanyService;
	
	@Autowired
	@Qualifier("SysUserPermissionService")
	private SysUserPermissionService sysUserPermissionService;

	@Autowired
	@Qualifier("SysUserService")
	private SysUserService sysUserService;

	@Autowired
    Executor executor;

	@GetMapping("/page")
	@ResponseBody
	@ApiOperation(value = "分页获取用户管理区域/公司数据")
	public PageResult page(UserAreaReq req) {
		return sysUserAreaService.page(req);
	}

	@PostMapping("/add")
	@ResponseBody
	@ApiOperation(value = "新增用户管理区域/公司数据")
	public Result add(@RequestBody List<SysUserArea> userAreas) {
		/* boolean res = userArea.insert();
		if (res) {
			updateUserPermission(userArea.getUserId());
			return Result.success();
		} else {
			return Result.error("500", "数据新增失败");
		} */
		try{
			for(SysUserArea userArea: userAreas){
				userArea.insert();
				updateUserPermission(userArea.getUserId());
			}
			return Result.success();
		}catch(Exception e){
			return Result.error("操作失败");
		}
	}

	@PostMapping("/del")
	@ResponseBody
	@ApiOperation(value = "删除用户管理区域/公司数据")
	public Result del(@RequestBody SysUserArea userArea) {
		boolean res = userArea.deleteById();
		if (res) {
			updateUserPermission(userArea.getUserId());
			return Result.success();
		} else {
			return Result.error("500", "数据删除失败");
		}
	}

	@GetMapping("/listArea")
	@ResponseBody
	@ApiOperation(value = "根据areaLevel罗列公司数据")
	public Result listArea(String areaLevel) {
		List<BasicCompany> list = basicCompanyService.selectList(new EntityWrapper<BasicCompany>()
				.setSqlSelect(Columns.create().column("area_id", "areaId").column("area_name"))
				.eq("area_level", areaLevel));
		return Result.success(list);
	}

	@GetMapping("/listAreaAndBranchCompany")
	@ResponseBody
	@ApiOperation("获取区域和分公司列表")
	public Result listAreaAndBranchCompany(){
		try {
			List<BasicCompany> lists = Lists.newArrayList();
			List<BasicCompany> areaList = basicCompanyService.selectList(new EntityWrapper<BasicCompany>().eq("area_level", AreaLevel.AREA_LEVEL.getKey()));
			List<BasicCompany> branchCompanyList = basicCompanyService.selectList(new EntityWrapper<BasicCompany>().eq("area_level", AreaLevel.COMPANY_LEVEL.getKey()));
			lists.addAll(areaList);
			lists.addAll(branchCompanyList);
			return Result.success(lists);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return Result.error(e.getMessage());
		}
	}

	@ApiOperation(value="获取片区信息数")
    @RequestMapping("/getAreaList")
    @ResponseBody
    public Result getAreaList(){

        //区域
        List<BasicCompany> area_list = basicCompanyService.selectList(new EntityWrapper<BasicCompany>().eq("area_level", AreaLevel.AREA_LEVEL.getKey()));
        List<BasicCompanyVo> voList = new ArrayList<>();
        for (BasicCompany basicCompany:area_list) {
            BasicCompanyVo vo = new BasicCompanyVo();
            BeanUtils.copyProperties(basicCompany,vo);
            vo.setTitle(basicCompany.getAreaName());
            voList.add(vo);
        }
        return Result.success(voList);
    }

    @ApiOperation(value="获取分公司")
    @RequestMapping("/getCompanyList")
    @ResponseBody
    public Result getCompanyList(@RequestParam("areaPid") String areaPid){
        //分公司
        List<BasicCompany> companyList = basicCompanyService.selectList(new EntityWrapper<BasicCompany>().eq("area_level", AreaLevel.COMPANY_LEVEL.getKey()).eq("area_pid",areaPid));
        List<String> companyNames = new ArrayList<>();
        for (BasicCompany basicCompany:companyList) {
            companyNames.add(basicCompany.getAreaName());
        }
        Map<String,Object> map = new HashMap<>();
        map.put("allcompanys",companyList);
        map.put("allcompanyNames",companyNames);

        return Result.success(companyList);
    }
	
	/**
	 * @Title: updateUserPermission  
	 * @Description: 用户区域信息变更后自动修改用户对应权限
	 * @param @param userId  参数  
	 * @return void    返回类型  
	 */
	public void updateUserPermission(String userId) {
		/* try {
			List<SysUser> ll = sysUserService.selectList(new EntityWrapper<SysUser>().eq("user_id",userId).or("user_name",userId));

			if(ll!=null && ll.size()>0){
				for(SysUser su:ll){
					sysUserPermissionService.setUserPermissons(su.getUserId());
				}
			}else{
				LOGGER.error(ExceptionCodeEnum.NO_USER.getValue().toString(),ExceptionCodeEnum.NO_USER.getDesc());
			}
		} catch (Exception e) {
			LOGGER.error("--AdminController--设置所指定用户户可访问业务对照关系失败！ 用户ID："+userId, e);
		}  */


		executor.execute(()->{
			try {
			List<SysUser> ll = sysUserService.selectList(new EntityWrapper<SysUser>().eq("user_id",userId).or("user_name",userId));

			if(ll!=null && ll.size()>0){
				for(SysUser su:ll){
					sysUserPermissionService.setUserPermissons(su.getUserId());
				}
			}else{
				LOGGER.error(ExceptionCodeEnum.NO_USER.getValue().toString(),ExceptionCodeEnum.NO_USER.getDesc());
			}
		} catch (Exception e) {
			LOGGER.error("--AdminController--设置所指定用户户可访问业务对照关系失败！ 用户ID："+userId, e);
			Thread.currentThread().interrupt();
		}
		});
		
	}

}
