package com.hongte.alms.core.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.aliyun.oss.ServiceException;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.BasicBusinessType;
import com.hongte.alms.base.entity.FiveLevelClassify;
import com.hongte.alms.base.entity.FiveLevelClassifyBusinessChangeLog;
import com.hongte.alms.base.entity.FiveLevelClassifyCondition;
import com.hongte.alms.base.entity.SysParameter;
import com.hongte.alms.base.service.BasicBusinessTypeService;
import com.hongte.alms.base.service.BusinessParameterService;
import com.hongte.alms.base.service.FiveLevelClassifyBusinessChangeLogService;
import com.hongte.alms.base.service.FiveLevelClassifyService;
import com.hongte.alms.base.service.SysParameterService;
import com.hongte.alms.base.vo.module.classify.FiveLevelClassifyConditionVO;
import com.hongte.alms.base.vo.module.classify.FiveLevelClassifyVO;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.common.vo.PageResult;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/businessParameter")
public class BusinessParameterController {
	private static final Logger LOG = LoggerFactory.getLogger(BusinessParameterController.class);

	@Autowired
	@Qualifier("BusinessParameterService")
	private BusinessParameterService businessParameterService;

	@Autowired
	@Qualifier("FiveLevelClassifyService")
	private FiveLevelClassifyService fiveLevelClassifyService;

	@Autowired
	@Qualifier("BasicBusinessTypeService")
	private BasicBusinessTypeService basicBusinessTypeService;

	@Autowired
	@Qualifier("SysParameterService")
	private SysParameterService sysParameterService;

	@Autowired
	@Qualifier("FiveLevelClassifyBusinessChangeLogService")
	private FiveLevelClassifyBusinessChangeLogService fiveLevelClassifyBusinessChangeLogService;

	@SuppressWarnings("rawtypes")
	@GetMapping("/queryFiveLevelClassifys")
	@ResponseBody
	public PageResult queryFiveLevelClassifys(Integer page, Integer limit) {
		try {
			Page<FiveLevelClassify> pageParam = businessParameterService.queryFiveLevelClassifys(page, limit);
			if (pageParam != null && CollectionUtils.isNotEmpty(pageParam.getRecords())) {
				return PageResult.success(pageParam.getRecords(), pageParam.getTotal());
			}
			return PageResult.success(0);
		} catch (Exception e) {
			LOG.error("--queryFiveLevelClassifys--获取五级分类设置信息列表失败！", e);
			throw new ServiceException("系统异常，获取五级分类设置信息列表失败！");
		}
	}

	@GetMapping("/queryTypes")
	@ResponseBody
	public Result<Map<String, Object>> queryTypes() {
		try {
			Map<String, Object> resultMap = new HashMap<>();
			Map<String, Object> paramTypeMap = new HashMap<>();
			List<BasicBusinessType> basicBusinessTypes = basicBusinessTypeService
					.selectList(new EntityWrapper<BasicBusinessType>().orderBy("business_type_id"));
			resultMap.put("basicBusinessTypes", basicBusinessTypes);

			List<SysParameter> sysParameters = sysParameterService
					.selectList(new EntityWrapper<SysParameter>().like("param_type", Constant.FIVE_LEVEL_CLASSIFY));
			if (CollectionUtils.isNotEmpty(sysParameters)) {
				for (SysParameter sysParameter : sysParameters) {
					if (paramTypeMap.get(sysParameter.getParamType()) != null) {
						continue;
					} else {
						paramTypeMap.put(sysParameter.getParamType(), sysParameter);
					}
				}
			}
			List<SysParameter> subSysParameters = new ArrayList<>();
			if (!paramTypeMap.isEmpty()) {
				for (Map.Entry<String, Object> entry : paramTypeMap.entrySet()) {
					subSysParameters.add((SysParameter) paramTypeMap.get(entry.getKey()));
				}
			}
			Collections.reverse(subSysParameters);
			resultMap.put("sysParameters", subSysParameters);
			return Result.success(resultMap);
		} catch (Exception e) {
			LOG.error("--queryTypes--获取参数类型列表失败！", e);
			throw new ServiceException("系统异常，获取参数类型列表失败！");
		}
	}

	@GetMapping("/queryConditionDesc")
	@ResponseBody
	public Result<Map<String, Object>> queryConditionDesc() {
		try {
			List<SysParameter> sysParameters = sysParameterService
					.selectList(new EntityWrapper<SysParameter>().like("param_type", Constant.FIVE_LEVEL_CLASSIFY));

			Map<String, Object> resultMap = new HashMap<>();

			if (CollectionUtils.isNotEmpty(sysParameters)) {
				for (SysParameter sysParameter : sysParameters) {
					String paramType = sysParameter.getParamType();
					String paramName = sysParameter.getParamName();
					@SuppressWarnings("unchecked")
					List<String> lstSysParamNames = (List<String>) resultMap.get(paramType);
					if (lstSysParamNames == null) {
						List<String> list = new ArrayList<>();
						list.add(paramName);
						resultMap.put(paramType, list);
					} else {
						lstSysParamNames.add(paramName);
						resultMap.put(paramType, lstSysParamNames);
					}
				}
			}
			return Result.success(resultMap);
		} catch (Exception e) {
			LOG.error("--queryConditionDesc--获取参数描述列表失败！", e);
			throw new ServiceException("系统异常，获取参数描述列表失败！");
		}
	}

	@SuppressWarnings("rawtypes")
	@PostMapping("/saveFiveLevelClassify")
	@ResponseBody
	public Result saveFiveLevelClassify(@RequestBody FiveLevelClassifyVO param) {
		try {
			if (param == null || StringUtil.isEmpty(param.getBusinessType())
					|| StringUtil.isEmpty(param.getClassName())) {
				return Result.error("500", "参数不能为空！");
			}
			int classNameCount = fiveLevelClassifyService
					.selectCount(new EntityWrapper<FiveLevelClassify>().eq("business_type", param.getBusinessType())
							.eq("class_name", param.getClassName()).eq("valid_status", "1"));
			if (classNameCount > 0) {
				return Result.error("500", "类别重复，请重新输入");
			}
			int classLevelCount = fiveLevelClassifyService
					.selectCount(new EntityWrapper<FiveLevelClassify>().eq("business_type", param.getBusinessType())
							.eq("class_level", param.getClassLevel()).eq("valid_status", "1"));
			if (classLevelCount > 0) {
				return Result.error("500", "等级重复，请重新输入");
			}
			businessParameterService.saveFiveLevelClassify(param);
			return Result.success();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ServiceException(e.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	@PostMapping("/updateFiveLevelClassify")
	@ResponseBody
	public Result updateFiveLevelClassify(@RequestBody FiveLevelClassifyVO param) {
		try {
			if (param == null) {
				return Result.error("500", "参数不能为空！");
			}
			businessParameterService.updateFiveLevelClassify(param);
			return Result.success();
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			if (e.getMessage().contains("idx_business_type_class_name")) {
				return Result.error("500", "已存在该类型，请重新输入");
			}
			throw new ServiceException(e.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	@PostMapping("/deleteFiveLevelClassify")
	@ResponseBody
	public Result deleteFiveLevelClassify(@RequestBody Map<String, Object> param) {
		try {
			businessParameterService.deleteFiveLevelClassify(param);
			return Result.success();
		} catch (Exception e) {
			LOG.error("--deleteFiveLevelClassify--删除五级分类设置信息失败！", e);
			throw new ServiceException("系统异常，删除五级分类设置信息失败！");
		}
	}

	@GetMapping("/queryDataByCondition")
	@ResponseBody
	public Result<List<FiveLevelClassify>> queryDataByCondition(
			@RequestParam(value = "businessType") String businessType,
			@RequestParam(value = "className") String className) {
		try {
			if (StringUtil.isEmpty(businessType) && StringUtil.isEmpty(className)) {
				return Result.error("500", "请输入查询条件");
			}
			Map<String, Object> paramMap = new HashMap<>();
			if (StringUtil.notEmpty(businessType)) {
				paramMap.put("businessType", businessType);
			}
			if (StringUtil.notEmpty(className)) {
				paramMap.put("className", className);
			}
			List<FiveLevelClassify> fiveLevelClassifies = fiveLevelClassifyService.queryDataByCondition(paramMap);
			if (CollectionUtils.isNotEmpty(fiveLevelClassifies)) {
				return Result.build("0", "请求成功！", fiveLevelClassifies);
			}
			return Result.build("0", "", null);
		} catch (Exception e) {
			LOG.error("--queryDataByCondition--查询五级分类设置信息失败！", e);
			throw new ServiceException("系统异常，查询五级分类设置信息失败！");
		}
	}

	@GetMapping("/queryFiveLevelClassifyCondition")
	@ResponseBody
	public Result<List<FiveLevelClassifyConditionVO>> queryFiveLevelClassifyCondition(
			@RequestParam(value = "className") String className,
			@RequestParam(value = "businessType") String businessType) {
		try {
			if (StringUtil.isEmpty(businessType) || StringUtil.isEmpty(className)) {
				return Result.error("500", "参数不能为空");
			}
			List<FiveLevelClassifyConditionVO> classifyConditionVOs = businessParameterService
					.queryFiveLevelClassifyCondition(className, businessType);
			if (CollectionUtils.isNotEmpty(classifyConditionVOs)) {
				Collections.sort(classifyConditionVOs, new Comparator<FiveLevelClassifyConditionVO>() {
					public int compare(FiveLevelClassifyConditionVO conditionVO1,
							FiveLevelClassifyConditionVO conditionVO2) {
						return conditionVO1.getSubClassName().compareTo(conditionVO2.getSubClassName());
					}
				});
				return Result.build("0", "请求成功！", classifyConditionVOs);
			}
			return Result.build("0", "没有找到数据！", null);
		} catch (Exception e) {
			LOG.error("获取条件列表信息失败！", e);
			throw new ServiceException("系统异常，获取条件列表信息失败！");
		}
	}

	@SuppressWarnings("rawtypes")
	@PostMapping("/saveConditionForClassify")
	@ResponseBody
	public Result saveConditionForClassify(@RequestBody Map<String, Object> paramMap) {
		try {
			if (paramMap == null) {
				return Result.error("500", "参数不能为空！");
			}
			if (paramMap.get("subClassName") == null || StringUtil.isEmpty((String) paramMap.get("subClassName"))) {
				return Result.error("500", "条件名称不能为空！");
			}
			businessParameterService.saveConditionForClassify(paramMap);
			return Result.success();
		} catch (Exception e) {
			LOG.error("保存业务类别-条件明细失败！", e);
			return Result.error("-500", e.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	@PostMapping("/updateConditionForClassify")
	@ResponseBody
	public Result updateConditionForClassify(@RequestBody Map<String, Object> paramMap) {
		try {
			if (paramMap == null) {
				return Result.error("500", "参数不能为空！");
			}
			if (paramMap.get("subClassName") == null || StringUtil.isEmpty((String) paramMap.get("subClassName"))) {
				return Result.error("500", "条件名称不能为空！");
			}
			businessParameterService.updateConditionForClassify(paramMap);
			return Result.success();
		} catch (Exception e) {
			LOG.error("更新业务类别-条件明细失败！", e);
			return Result.error("-500", e.getMessage());
		}
	}

	@GetMapping("/queryConditionForClassify")
	@ResponseBody
	public Result<FiveLevelClassifyConditionVO> queryConditionForClassify(
			@RequestParam(value = "className") String className,
			@RequestParam(value = "businessType") String businessType,
			@RequestParam(value = "subClassName") String subClassName) {
		try {
			if (StringUtil.isEmpty(subClassName) || StringUtil.isEmpty(className) || StringUtil.isEmpty(businessType)) {
				return Result.error("500", "参数不能为空！");
			}
			FiveLevelClassifyConditionVO vo = businessParameterService.queryConditionForClassify(className,
					businessType, subClassName);
			return Result.success(vo);
		} catch (Exception e) {
			LOG.error("查找业务类别-条件明细失败！", e);
			throw new ServiceException("系统异常，查找业务类别-条件明细失败！");
		}
	}

	@SuppressWarnings("rawtypes")
	@GetMapping("/queryMayBeUsed")
	@ResponseBody
	public Result queryMayBeUsed(@RequestParam(value = "className") String className,
			@RequestParam(value = "businessType") String businessType) {
		try {
			if (StringUtil.isEmpty(className) || StringUtil.isEmpty(businessType)) {
				return Result.error("500", "参数不能为空！");
			}
			Integer count = businessParameterService.queryMayBeUsed(businessType, className);
			if (count != null && count.intValue() > 0) {
				return Result.build("-500", "已有业务所属该类别，不允许删除", null);
			}
			return Result.success();
		} catch (Exception e) {
			LOG.error("查找业务类别是否被分配失败！", e);
			throw new ServiceException("系统异常，查找业务类别是否被分配失败！");
		}
	}

	@SuppressWarnings("rawtypes")
	@PostMapping("/deleteConditionParamModal")
	@ResponseBody
	public Result deleteConditionParamModal(@RequestBody FiveLevelClassifyCondition condition) {
		try {
			if (condition == null || StringUtil.isEmpty(condition.getBusinessType())
					|| StringUtil.isEmpty(condition.getClassName())
					|| StringUtil.isEmpty(condition.getSubClassName())) {
				return Result.error("500", "参数不能为空！");
			}
			businessParameterService.deleteConditionParamModal(condition);
			return Result.success();
		} catch (Exception e) {
			LOG.error("删除业务类别-条件明细失败！", e);
			throw new ServiceException("系统异常，删除业务类别-条件明细失败！");
		}
	}

	@GetMapping("/fiveLevelClassifySchedule")
	@ResponseBody
	public Result<String> fiveLevelClassifySchedule() {
		try {
			fiveLevelClassifyService.fiveLevelClassifySchedule();
			return Result.success();
		} catch (Exception e) {
			LOG.error("手工执行五级分类调度任务失败！", e);
			throw new ServiceException("系统异常，手工执行五级分类调度任务失败！");
		}
	}

	@ApiOperation("根据时间段查询业务五级分类信息")
	@PostMapping("/queryBusinessFiveLevelClassify")
	@ResponseBody
	public Result<Map<String, Object>> queryBusinessFiveLevelClassify(@RequestBody Map<String, Object> paramMap) {
		try {
			if (paramMap == null || paramMap.isEmpty() || !paramMap.containsKey("startDate")
					|| !paramMap.containsKey("endDate")) {
				return Result.error("参数不能为空！");
			}

			Date startDate = (Date) paramMap.get("startDate");
			Date endDate = DateUtil.addDay2Date(1, (Date) paramMap.get("endDate"));

			List<FiveLevelClassifyBusinessChangeLog> changeLogs = fiveLevelClassifyBusinessChangeLogService
					.selectList(new EntityWrapper<FiveLevelClassifyBusinessChangeLog>().gt("op_time", startDate)
							.lt("op_time", endDate).eq("valid_status", "1"));
			
			if (CollectionUtils.isNotEmpty(changeLogs)) {
				Map<String, Object> resultMap = new HashMap<>();
				for (FiveLevelClassifyBusinessChangeLog changeLog : changeLogs) {
					resultMap.put(changeLog.getOrigBusinessId(), changeLog.getClassName());
				}
				return Result.success(resultMap);
			}else {
				return Result.success();
			}
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return Result.error("系统异常：" + e.getMessage());
		}
	}

	public static void main(String[] args) {
		String d = "2018-09-08";
		System.out.println(DateUtil.addDay2Date(1, DateUtil.getDate(d)));
	}
}
