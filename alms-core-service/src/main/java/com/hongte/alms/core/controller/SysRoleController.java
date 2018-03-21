package com.hongte.alms.core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.SysRole;
import com.hongte.alms.base.service.SysRoleService;
import com.hongte.alms.common.result.Result;

import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 系统权限表 前端控制器
 * </p>
 *
 * @author 王继光
 * @since 2018-03-20
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController {

	@Autowired
	@Qualifier("SysRoleService")
	SysRoleService sysRoleService;

	@GetMapping("/list")
	@ResponseBody
	@ApiOperation(value = "列出所有role")
	public Result list() {
		List<SysRole> list = sysRoleService.selectList(new EntityWrapper<SysRole>());
		Result result = new Result<>();
		result.setCode("0");
		result.setData(list);
		return result;
	}

	@PostMapping("/add")
	@ResponseBody
	@ApiOperation(value = "新增role")
	public Result add(@RequestBody SysRole sysRole) {
		boolean res = sysRole.insert();
		if (res) {
			return Result.success();
		} else {
			return Result.error("500", "数据新增失败");
		}
	}

	@PostMapping("/update")
	@ResponseBody
	@ApiOperation(value = "修改role")
	public Result update(@RequestBody SysRole sysRole) {
		boolean res = sysRole.updateById();
		if (res) {
			return Result.success();
		} else {
			return Result.error("500", "数据修改失败");
		}
	}

	@PostMapping("/delete")
	@ResponseBody
	@ApiOperation(value = "删除role")
	public Result delete(@RequestBody SysRole sysRole) {
		boolean res = sysRole.deleteById();
		if (res) {
			return Result.success();
		} else {
			return Result.error("500", "数据删除失败");
		}
	}
}
