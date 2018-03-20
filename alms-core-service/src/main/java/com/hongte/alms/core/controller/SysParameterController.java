/**
 * 
 */
package com.hongte.alms.core.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.SysParameter;
import com.hongte.alms.base.service.SysParameterService;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.vo.PageResult;
import com.ht.ussp.bean.LoginUserInfoHelper;

import io.swagger.annotations.ApiOperation;

/**
 * @author 王继光 2018年3月17日 上午11:20:33
 */
@Controller
@RequestMapping(value = "/sys/param")
public class SysParameterController {

	@Autowired
	@Qualifier("SysParameterService")
	SysParameterService sysParameterService;

	@Autowired
	LoginUserInfoHelper loginUserInfoHelper;

	@GetMapping("/page")
	@ResponseBody
	public PageResult page(Integer page, Integer limit, String key) {
		Page<SysParameter> p = new Page<>(page, limit);
		EntityWrapper<SysParameter> ew = new EntityWrapper<>();
		if (key != null && !key.equals("")) {
			ew.like("param_type_name", key).or().like("param_type", key);
		}
		ew.groupBy("param_type");
		p = sysParameterService.selectPage(p, ew);
		return PageResult.success(p.getRecords(), p.getTotal());
	}

	@PostMapping("/save")
	@ResponseBody
	public Result save(@RequestBody SysParameter parameter) {
		if (parameter.getParamId() == null) {
			parameter.setCreateTime(new Date());
			parameter.setCreateUser(loginUserInfoHelper.getUserId());
			parameter.insert();
		} else {
			parameter.setUpdateTime(new Date());
			parameter.setUpdateUser(loginUserInfoHelper.getUserId());
			parameter.updateById();
		}
		return Result.success();
	}

	@GetMapping("/getParamType")
	@ResponseBody
	@ApiOperation(value = "获取参数类型信息")
	public Result getParamType(String paramType) {
		EntityWrapper<SysParameter> ew = new EntityWrapper<>();
		ew.groupBy("param_type");
		SysParameter sysParameter = sysParameterService.selectOne(ew);
		if (sysParameter == null) {
			return Result.error("500", "参数类型不存在");
		}
		JSONObject res = new JSONObject();
		res.put("param_type", sysParameter.getParamType());
		res.put("param_type_name", sysParameter.getParamTypeName());
		res.put("remark", sysParameter.getRemark());
		res.put("status", sysParameter.getStatus());

		return Result.success(res);
	}

	@PostMapping("/addParamType")
	@ResponseBody
	@ApiOperation(value = "新增参数类型信息")
	public Result addParamType(@RequestBody JSONObject map) {
		String paramType = map.getString("paramType");
		String paramTypeName = map.getString("paramTypeName");
		String remark = map.getString("remark");
		Integer status = map.getInteger("status");
		List<SysParameter> list = sysParameterService
				.selectList(new EntityWrapper<SysParameter>().eq("param_type", paramType));
		if (list != null && list.size() > 0) {
			return Result.error("500", "已存在的参数类型编号:" + paramType);
		}
		SysParameter sysParameter = new SysParameter();
		sysParameter.setParamType(paramTypeName);
		sysParameter.setParamTypeName(paramTypeName);
		sysParameter.setRemark(remark);
		sysParameter.setStatus(status);
		sysParameter.setParamValue(Constant.SYS_PARAMETER_PLACEHOLDER.toString());
		sysParameter.setParamId(UUID.randomUUID().toString());
		sysParameter.insert();
		return Result.success();
	}

	@PostMapping("/updateParamType")
	@ResponseBody
	@ApiOperation(value = "更新参数类型信息")
	public Result updateParamType(@RequestBody JSONObject map) {
		String paramType = map.getString("paramType");
		String paramTypeName = map.getString("paramTypeName");
		String remark = map.getString("remark");
		Integer status = map.getInteger("status");
		List<SysParameter> list = sysParameterService
				.selectList(new EntityWrapper<SysParameter>().eq("param_type", paramType));
		if (list == null || list.size() == 0) {
			return Result.error("500", "不存在的参数类型编号:" + paramType);
		}

		for (SysParameter sysParameter : list) {
			sysParameter.setParamTypeName(paramTypeName);
			sysParameter.setRemark(remark);
			sysParameter.setStatus(status);
		}

		sysParameterService.updateBatchById(list);
		return Result.success();
	}

	@PostMapping("/delParamType")
	@ResponseBody
	@ApiOperation(value = "删除参数类型信息")
	public Result del(@RequestBody JSONObject jsonObject) {
		boolean result = sysParameterService
				.delete(new EntityWrapper<SysParameter>().eq("param_type", jsonObject.get("paramType")));
		if (result) {
			return Result.success();
		} else {
			return Result.error("500", "数据更新失败");
		}
	};

	@GetMapping("/listParam")
	@ResponseBody
	@ApiOperation(value = "list参数类型信息")
	public Result list(String paramType) {
		List<SysParameter> list = sysParameterService.selectList(new EntityWrapper<SysParameter>()
				.eq("param_type", paramType).andNew().ne("param_value", Constant.SYS_PARAMETER_PLACEHOLDER)
				.orderBy("row_Index").or().isNull("param_value"));
		List<JSONObject> res = new ArrayList<>();
		for (SysParameter s : list) {
			JSONObject j = new JSONObject();
			j.put("paramId", s.getParamId());
			j.put("paramName", s.getParamName());
			j.put("paramType", s.getParamType());
			j.put("paramTypeName", s.getParamTypeName());
			j.put("remark", s.getRemark());
			j.put("paramValue", s.getParamValue());
			j.put("paramValue2", s.getParamValue2());
			j.put("paramValue3", s.getParamValue3());
			j.put("paramValue4", s.getParamValue4());
			j.put("paramValue5", s.getParamValue5());
			res.add(j);
		}
		return Result.success(res);
	}

	@GetMapping("/get")
	@ResponseBody
	public Result get(String paramId) {
		SysParameter sysParameter = sysParameterService.selectById(paramId);
		if (sysParameter == null) {
			return Result.error("500", "参数不存在");
		}
		return Result.success(sysParameter);
	}

	@PostMapping("/addParm")
	@ResponseBody
	@ApiOperation(value = "新增参数")
	public Result addParm(@RequestBody List<JSONObject> params) {
		if (params == null || params.size() == 0) {
			return Result.error("500", "params不能为空");
		}
		List<SysParameter> sysParameters = new ArrayList<>();
		String paramType = params.get(0).getString("paramType");
		String paramTypeName = params.get(0).getString("paramTypeName");
		String userId = loginUserInfoHelper.getUserId();
		List<SysParameter> list = sysParameterService.selectList(new EntityWrapper<SysParameter>()
				.eq("param_type", paramType).eq("param_type_name", paramTypeName).orderBy("create_time"));

		if (list == null || list.size() == 0) {
			return Result.error("500", "找不到paramType:" + paramType + "&paramTypeName:" + paramTypeName);
		}

		SysParameter sysParameter = list.get(0);

		for (JSONObject j : params) {
			SysParameter s = new SysParameter();
			s.setParamName(j.getString("paramName"));
			s.setParamType(j.getString("paramType"));
			s.setParamTypeName(j.getString("paramTypeName"));
			s.setRemark(j.getString("remark"));
			s.setParamValue(j.getString("paramValue"));
			s.setParamValue2(j.getString("paramValue2"));
			s.setParamValue3(j.getString("paramValue3"));
			s.setParamValue4(j.getString("paramValue4"));
			s.setParamValue5(j.getString("paramValue5"));
			s.setParamId(UUID.randomUUID().toString());
			s.setCreateTime(new Date());
			s.setCreateUser(userId);
			s.setStatus(sysParameter.getStatus());
			sysParameters.add(s);
		}

		for (SysParameter s : list) {
			if (s.getParamValue() != null && s.getParamValue().equals(Constant.SYS_PARAMETER_PLACEHOLDER.toString())) {
				s.deleteById();
				break;
			}
		}

		boolean result = sysParameterService.insertBatch(sysParameters);
		if (result) {
			return Result.success();
		} else {
			return Result.error("500", "数据新增失败");
		}
	}

	@PostMapping("/updateParm")
	@ResponseBody
	@ApiOperation(value = "更新参数")
	public Result updateParm(@RequestBody JSONObject param) {
		if (param == null || param.keySet().size() == 0) {
			return Result.error("500", "param不能为空");
		}
		String paramId = param.getString("paramId");
		String paramType = param.getString("paramType");
		String paramTypeName = param.getString("paramTypeName");
		String userId = loginUserInfoHelper.getUserId();
		SysParameter sysParameter = sysParameterService.selectById(paramId);
		if (sysParameter == null) {
			return Result.error("500", "找不到param_id:" + paramId + "的参数");
		}

		String paramValue = param.getString("paramValue");
		String paramValue2 = param.getString("paramValue2");
		String paramValue3 = param.getString("paramValue3");
		String paramValue4 = param.getString("paramValue4");
		String paramValue5 = param.getString("paramValue5");
		String remark = param.getString("remark");
		String paramName = param.getString("paramName");

		sysParameter.setUpdateTime(new Date());
		sysParameter.setUpdateUser(loginUserInfoHelper.getUserId());
		sysParameter.setParamName(paramName);
		sysParameter.setParamValue(paramValue);
		sysParameter.setParamValue2(paramValue2);
		sysParameter.setParamValue3(paramValue3);
		sysParameter.setParamValue4(paramValue4);
		sysParameter.setParamValue5(paramValue5);
		sysParameter.setRemark(remark);

		boolean result = sysParameter.updateById();
		if (result) {
			return Result.success();
		} else {
			return Result.error("500", "数据更新失败");
		}
	}

	@PostMapping("/delParm")
	@ResponseBody
	@ApiOperation(value = "删除参数")
	public Result delParm(@RequestBody JSONObject jsonObject) {
		if (jsonObject == null || jsonObject.getString("paramId").equals("")) {
			return Result.error("500", "paramId不能为空");
		}
		SysParameter sysParameter = sysParameterService.selectById(jsonObject.getString("paramId"));
		if (sysParameter == null) {
			return Result.error("500", "找不到param_id:" + jsonObject.getString("paramId") + "的参数");
		}

		boolean result = sysParameter.deleteById();
		if (result) {
			int count = sysParameterService
					.selectCount(new EntityWrapper<SysParameter>().eq("param_type", jsonObject.getString("paramType"))
							.eq("param_type_name", jsonObject.getString("paramTypeName")));
			if (count == 0) {
				sysParameter.setParamValue(Constant.SYS_PARAMETER_PLACEHOLDER.toString());
				sysParameter.setParamId(null);
				sysParameter.insert();
			}
			return Result.success();
		} else {
			return Result.error("500", "数据删除失败");
		}
	}
}
