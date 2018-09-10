package com.hongte.alms.open.controller;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.hongte.alms.base.entity.FiveLevelClassifyBusinessChangeLog;
import com.hongte.alms.base.entity.SysParameter;
import com.hongte.alms.base.service.BusinessParameterService;
import com.hongte.alms.base.service.FiveLevelClassifyBusinessChangeLogService;
import com.hongte.alms.base.service.SysParameterService;
import com.hongte.alms.base.vo.module.classify.ClassifyConditionVO;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.open.vo.BusinessFiveLevelClassifyInfoVO;

import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/businessParameter")
public class BusinessParameterController {
	private static final Logger LOG = LoggerFactory.getLogger(BusinessParameterController.class);

	@Autowired
	@Qualifier("SysParameterService")
	private SysParameterService sysParameterService;

	@Autowired
	@Qualifier("BusinessParameterService")
	private BusinessParameterService businessParameterService;

	@Autowired
	@Qualifier("FiveLevelClassifyBusinessChangeLogService")
	private FiveLevelClassifyBusinessChangeLogService fiveLevelClassifyBusinessChangeLogService;

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

	@PostMapping("/fiveLevelClassifyForBusiness")
	@ResponseBody
	public Result<String> fiveLevelClassifyForBusiness(@RequestBody ClassifyConditionVO classifyConditionVO) {
		try {
			if (classifyConditionVO == null || StringUtil.isEmpty(classifyConditionVO.getBusinessId())
					|| StringUtil.isEmpty(classifyConditionVO.getOpSourse())) {
				return Result.error("500", "参数不能为空！");
			}
			return Result.success(businessParameterService.fiveLevelClassifyForBusiness(classifyConditionVO));
		} catch (Exception e) {
			LOG.error("匹配业务类别失败！", e);
			throw new ServiceException("系统异常，匹配业务类别失败！");
		}
	}

	@PostMapping("/businessChangeLog")
	@ResponseBody
	public Result<String> businessChangeLog(@RequestBody ClassifyConditionVO classifyConditionVO) {
		try {
			if (classifyConditionVO == null || StringUtil.isEmpty(classifyConditionVO.getBusinessId())
					|| StringUtil.isEmpty(classifyConditionVO.getOpSourse())
					|| StringUtil.isEmpty(classifyConditionVO.getOpSourseId())
					|| StringUtil.isEmpty(classifyConditionVO.getOpUserId())
					|| StringUtil.isEmpty(classifyConditionVO.getOpUsername())) {
				return Result.error("500", "参数不能为空！");
			}
			fiveLevelClassifyBusinessChangeLogService.businessChangeLog(classifyConditionVO);
			return Result.success();
		} catch (Exception e) {
			LOG.error("业务类别变更记录失败！", e);
			throw new ServiceException("系统异常，业务类别变更记录失败！");
		}
	}

	@GetMapping("/querySelectedConditionDesc")
	@ResponseBody
	public Result<Map<String, Object>> querySelectedConditionDesc(
			@RequestParam(value = "businessId") String businessId) {
		try {
			if (StringUtil.isEmpty(businessId)) {
				return Result.error("-500", "业务编号不能为空！");
			}
			Map<String, Object> resultMap = new HashMap<>();
			FiveLevelClassifyBusinessChangeLog changeLog = fiveLevelClassifyBusinessChangeLogService
					.selectOne(new EntityWrapper<FiveLevelClassifyBusinessChangeLog>()
							.eq("orig_business_id", businessId).eq("valid_status", "1"));
			if (changeLog != null) {
				String borrowerConditionDesc = changeLog.getBorrowerConditionDesc();
				if (StringUtil.notEmpty(borrowerConditionDesc)) {
					String[] arrBorrower = borrowerConditionDesc.split(Constant.FIVE_LEVEL_CLASSIFY_SPLIT);
					resultMap.put("borrowerConditionDesc", Arrays.asList(arrBorrower));
				}
				
				String guaranteeConditionDesc = changeLog.getGuaranteeConditionDesc();
				
				if (StringUtil.notEmpty(guaranteeConditionDesc)) {
					String[] arrBorrower = guaranteeConditionDesc.split(Constant.FIVE_LEVEL_CLASSIFY_SPLIT);
					resultMap.put("guaranteeConditionDesc", Arrays.asList(arrBorrower));
				}
			}
			return Result.success(resultMap);
		} catch (Exception e) {
			LOG.error("--queryConditionDesc--获取参数描述列表失败！", e);
			throw new ServiceException("系统异常，获取参数描述列表失败！");
		}
	}

	@ApiOperation("根据时间段查询业务五级分类信息")
	@PostMapping("/queryBusinessFiveLevelClassify")
	@ResponseBody
	public Result<List<Map<String, Object>>> queryBusinessFiveLevelClassify(@RequestBody BusinessFiveLevelClassifyInfoVO vo) {
		try {
			List<FiveLevelClassifyBusinessChangeLog> changeLogs = fiveLevelClassifyBusinessChangeLogService
					.selectList(new EntityWrapper<FiveLevelClassifyBusinessChangeLog>().gt("op_time", vo.getStartDate())
							.lt("op_time", vo.getEndDate()).eq("valid_status", "1"));
			
			List<Map<String, Object>> resultList = new LinkedList<>();
			if (CollectionUtils.isNotEmpty(changeLogs)) {
				for (FiveLevelClassifyBusinessChangeLog changeLog : changeLogs) {
					Map<String, Object> resultMap = new HashMap<>();
					resultMap.put("businessId", changeLog.getOrigBusinessId());
					resultMap.put("levelName", changeLog.getClassName());
					resultList.add(resultMap);
				}
			}
			return Result.success(resultList);
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			return Result.error("系统异常：" + e.getMessage());
		}
	}
	
}
