package com.hongte.alms.base.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.aliyun.oss.ServiceException;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.FiveLevelClassify;
import com.hongte.alms.base.entity.FiveLevelClassifyCondition;
import com.hongte.alms.base.mapper.BasicBusinessTypeMapper;
import com.hongte.alms.base.service.BusinessParameterService;
import com.hongte.alms.base.service.FiveLevelClassifyConditionService;
import com.hongte.alms.base.service.FiveLevelClassifyService;
import com.hongte.alms.base.service.RepaymentBizPlanListService;
import com.hongte.alms.base.vo.module.classify.ClassifyConditionVO;
import com.hongte.alms.base.vo.module.classify.FiveLevelClassifyConditionVO;
import com.hongte.alms.base.vo.module.classify.FiveLevelClassifyVO;
import com.hongte.alms.common.util.Constant;
import com.hongte.alms.common.util.StringUtil;
import com.ht.ussp.bean.LoginUserInfoHelper;

@Service("BusinessParameterService")
public class BusinessParameterServiceImpl implements BusinessParameterService {
	private static final Logger LOG = LoggerFactory.getLogger(BusinessParameterServiceImpl.class);

	@Autowired
	@Qualifier("FiveLevelClassifyService")
	private FiveLevelClassifyService fiveLevelClassifyService;

	@Autowired
	@Qualifier("FiveLevelClassifyConditionService")
	private FiveLevelClassifyConditionService fiveLevelClassifyConditionService;

	@Autowired
	private LoginUserInfoHelper loginUserInfoHelper;

	@Autowired
	private BasicBusinessTypeMapper basicBusinessTypeMapper;

	@Autowired
	@Qualifier("RepaymentBizPlanListService")
	private RepaymentBizPlanListService repaymentBizPlanListService;

	@Override
	public Page<FiveLevelClassify> queryFiveLevelClassifys(Integer page, Integer limit) {
		Page<FiveLevelClassify> pageParam = new Page<>(page, limit);
		Page<FiveLevelClassify> resultPage = fiveLevelClassifyService.selectPage(pageParam);
		List<FiveLevelClassify> records = resultPage.getRecords();
		List<FiveLevelClassify> list = new ArrayList<>();
		if (CollectionUtils.isNotEmpty(records)) {
			for (FiveLevelClassify fiveLevelClassify : records) {
				if ("0".equals(fiveLevelClassify.getValidStatus())) {
					list.add(fiveLevelClassify);
				}
			}
			records.removeAll(list);
			resultPage.setRecords(records);
		}
		return resultPage;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void saveFiveLevelClassify(FiveLevelClassifyVO param) {
		try {
			fiveLevelClassifyService.insert(assembleFiveLevelClassify(param));
		} catch (Exception e) {
			LOG.error("方法 saveFiveLevelClassify 执行失败！", e);
			throw new ServiceException("保存五级分类失败！", e);
		}

	}

	@Override
	public void updateFiveLevelClassify(FiveLevelClassifyVO param) {
		try {
			fiveLevelClassifyService.updateById(assembleFiveLevelClassify(param));
		} catch (Exception e) {
			LOG.error("方法 updateFiveLevelClassify 执行失败！", e);
			throw new ServiceException("更新五级分类失败！", e);
		}
	}

	private FiveLevelClassify assembleFiveLevelClassify(FiveLevelClassifyVO param) {
		FiveLevelClassify classify = new FiveLevelClassify();
		classify.setId(param.getId());
		classify.setBusinessType(param.getBusinessType());
		classify.setClassName(param.getClassName());
		classify.setClassLevel(param.getClassLevel());
		classify.setRemark(param.getRemark());
		String userId = loginUserInfoHelper.getUserId();
		String methodName = Thread.currentThread().getStackTrace()[2].getMethodName();
		if ("saveFiveLevelClassify".equals(methodName)) {
			classify.setCreateUser(userId);
			classify.setCreateTime(new Date());
		}
		if ("updateFiveLevelClassify".equals(methodName)) {
			classify.setUpdateUser(userId);
			classify.setUpdateTime(new Date());
		}
		return classify;
	}

	@Override
	public List<FiveLevelClassifyConditionVO> queryFiveLevelClassifyCondition(String className, String businessType) {
		try {
			List<FiveLevelClassifyConditionVO> conditionVOs = new ArrayList<>();
			List<FiveLevelClassifyCondition> conditions = fiveLevelClassifyConditionService
					.selectList(new EntityWrapper<FiveLevelClassifyCondition>().eq("class_name", className)
							.eq("business_type", businessType).eq("valid_status", "1").orderBy("update_time"));
			if (CollectionUtils.isNotEmpty(conditions)) {

				Map<String, Date> map = new HashMap<>();

				Map<String, List<FiveLevelClassifyCondition>> mapList = new HashMap<>();

				for (FiveLevelClassifyCondition condition : conditions) {

					handleData(map, mapList, condition);

				}

				for (Map.Entry<String, Date> entry : map.entrySet()) {
					FiveLevelClassifyConditionVO vo = new FiveLevelClassifyConditionVO();
					String subClassName = entry.getKey();
					vo.setSubClassName(subClassName);
					vo.setUpdateTime(entry.getValue());
					List<FiveLevelClassifyCondition> classifyConditions = mapList.get(subClassName);
					if (CollectionUtils.isNotEmpty(classifyConditions)) {
						FiveLevelClassifyCondition fiveLevelClassifyCondition = classifyConditions.get(0);
						vo.setBusinessType(fiveLevelClassifyCondition.getBusinessType());
						vo.setClassName(fiveLevelClassifyCondition.getClassName());
						vo.setExecuteCondition(fiveLevelClassifyCondition.getExecuteCondition());
						vo.setParentId(fiveLevelClassifyCondition.getParentId());
						vo.setSubClassName(fiveLevelClassifyCondition.getSubClassName());
					}
					vo.setClassifyConditions(classifyConditions);
					conditionVOs.add(vo);
				}
			}
			return conditionVOs;
		} catch (Exception e) {
			LOG.error("方法 queryFiveLevelClassifyCondition 执行失败！", e);
			throw new ServiceException("获取条件列表信息失败！", e);
		}
	}

	private void handleData(Map<String, Date> map, Map<String, List<FiveLevelClassifyCondition>> mapList,
			FiveLevelClassifyCondition condition) {
		if (condition == null) {
			return;
		}
		String subClassName = condition.getSubClassName();

		Date updateTime = map.get(subClassName);

		List<FiveLevelClassifyCondition> conditions = mapList.get(subClassName);

		if (updateTime == null) {
			map.put(subClassName, condition.getUpdateTime());
		} else {
			if (updateTime.before(map.get(subClassName))) {
				map.put(subClassName, condition.getUpdateTime());
			}
		}
		if (conditions == null) {

			List<FiveLevelClassifyCondition> classifyConditions = new ArrayList<>();

			classifyConditions.add(condition);

			mapList.put(subClassName, classifyConditions);

		} else {

			conditions.add(condition);

			mapList.put(subClassName, conditions);

		}
	}

	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void saveConditionForClassify(Map<String, Object> paramMap) {
		try {
			List<LinkedHashMap<String, Object>> commitSetCondition = (List<LinkedHashMap<String, Object>>) paramMap
					.get("commitSetCondition");
			if (CollectionUtils.isNotEmpty(commitSetCondition)) {
				String businessType = (String) paramMap.get("businessType");
				String subClassName = (String) paramMap.get("subClassName");
				String className = (String) paramMap.get("className");
				String parentId = (String) paramMap.get("parentId");
				String executeCondition = (String) paramMap.get("executeCondition");

				int count = fiveLevelClassifyConditionService
						.selectCount(new EntityWrapper<FiveLevelClassifyCondition>().eq("class_name", className)
								.eq("business_type", businessType).eq("sub_class_name", subClassName)
								.eq("valid_status", "1"));
				if (count > 0) {
					throw new ServiceException(
							businessType + "-" + className + "-" + subClassName + "，已经设定过，请重新命名条件名称！");
				}

				for (LinkedHashMap<String, Object> linkedHashMap : commitSetCondition) {

					Date currTime = new Date();
					String userId = loginUserInfoHelper.getUserId();
					FiveLevelClassifyCondition condition = new FiveLevelClassifyCondition();
					condition.setBusinessType(businessType);
					condition.setSubClassName(subClassName);
					condition.setClassName(className);
					condition.setParamType((String) linkedHashMap.get("conditionType"));
					condition.setParentId(parentId);
					condition.setExecuteCondition(executeCondition);
					condition.setOpType(Constant.FIVE_LEVEL_CLASSIFY_CONDITION_ADD);
					condition.setParamName((String) linkedHashMap.get("conditionDesc"));
					condition.setTypeNameRelation((String) linkedHashMap.get("relation"));
					condition.setUpdateTime(currTime);
					condition.setUpdateUser(userId);
					condition.setCreateUser(userId);
					condition.setCreateTime(currTime);
					fiveLevelClassifyConditionService.insert(condition);
				}
			}
		} catch (Exception e) {
			LOG.error("方法 saveConditionForClassify 执行失败！", e);
			throw new ServiceException(e.getMessage(), e);
		}
	}

	@SuppressWarnings("unchecked")
	@Transactional(rollbackFor = Exception.class)
	@Override
	public void updateConditionForClassify(Map<String, Object> paramMap) {
		try {
			List<LinkedHashMap<String, Object>> commitSetCondition = (List<LinkedHashMap<String, Object>>) paramMap
					.get("commitSetCondition");
			if (CollectionUtils.isNotEmpty(commitSetCondition)) {
				String businessType = (String) paramMap.get("businessType");
				String subClassName = (String) paramMap.get("subClassName");
				String className = (String) paramMap.get("className");
				String parentId = (String) paramMap.get("parentId");
				String executeCondition = (String) paramMap.get("executeCondition");

				List<FiveLevelClassifyCondition> classifyConditions = fiveLevelClassifyConditionService
						.selectList(new EntityWrapper<FiveLevelClassifyCondition>().eq("class_name", className)
								.eq("business_type", businessType).eq("sub_class_name", subClassName)
								.eq("valid_status", "1"));

				if (CollectionUtils.isNotEmpty(classifyConditions)) {
					for (FiveLevelClassifyCondition condition : classifyConditions) {
						condition.setValidStatus("0");
						condition.setOpType(Constant.FIVE_LEVEL_CLASSIFY_CONDITION_UPDATE);
						fiveLevelClassifyConditionService.updateById(condition);
					}
				}

				for (LinkedHashMap<String, Object> linkedHashMap : commitSetCondition) {

					Date currTime = new Date();
					String userId = loginUserInfoHelper.getUserId();
					FiveLevelClassifyCondition condition = new FiveLevelClassifyCondition();
					condition.setBusinessType(businessType);
					condition.setSubClassName(subClassName);
					condition.setClassName(className);
					condition.setParamType((String) linkedHashMap.get("conditionType"));
					condition.setParentId(parentId);
					condition.setExecuteCondition(executeCondition);
					condition.setOpType(Constant.FIVE_LEVEL_CLASSIFY_CONDITION_UPDATE);
					condition.setParamName((String) linkedHashMap.get("conditionDesc"));
					condition.setTypeNameRelation((String) linkedHashMap.get("relation"));
					condition.setUpdateTime(currTime);
					condition.setUpdateUser(userId);
					condition.setCreateUser(userId);
					condition.setCreateTime(currTime);
					fiveLevelClassifyConditionService.insert(condition);
				}
			}
		} catch (Exception e) {
			LOG.error("方法 saveConditionForClassify 执行失败！", e);
			throw new ServiceException("更新五级分类设置业务类别-条件失败！", e);
		}

	}

	@Override
	public FiveLevelClassifyConditionVO queryConditionForClassify(String className, String businessType,
			String subClassName) {
		List<FiveLevelClassifyCondition> conditions = fiveLevelClassifyConditionService
				.selectList(new EntityWrapper<FiveLevelClassifyCondition>().eq("class_name", className)
						.eq("business_type", businessType).eq("sub_class_name", subClassName).eq("valid_status", "1")
						.orderBy("update_time"));

		FiveLevelClassifyConditionVO vo = new FiveLevelClassifyConditionVO();

		if (CollectionUtils.isNotEmpty(conditions)) {
			vo.setExecuteCondition(conditions.get(0).getExecuteCondition());
			vo.setClassifyConditions(conditions);
		}
		return vo;
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void deleteConditionParamModal(FiveLevelClassifyCondition condition) {
		try {
			condition.setValidStatus("0");
			condition.setUpdateTime(new Date());
			condition.setUpdateUser(loginUserInfoHelper.getUserId());
			condition.setOpType(Constant.FIVE_LEVEL_CLASSIFY_CONDITION_DELETE);
			fiveLevelClassifyConditionService.update(condition,
					new EntityWrapper<FiveLevelClassifyCondition>().eq("business_type", condition.getBusinessType())
							.eq("class_name", condition.getClassName())
							.eq("sub_class_name", condition.getSubClassName()).eq("valid_status", "1"));
		} catch (Exception e) {
			LOG.error("方法 deleteConditionParamModal 执行失败！", e);
			throw new ServiceException("删除五级分类设置业务类别-条件失败！", e);
		}
	}

	@Override
	public String fiveLevelClassifyForBusiness(ClassifyConditionVO classifyConditionVO) {
		try {
			String resultClassName = "";
			String businessId = classifyConditionVO.getBusinessId();
			String opSourse = classifyConditionVO.getOpSourse();
			List<String> guaranteeConditions = classifyConditionVO.getGuaranteeConditions();
			List<String> mainBorrowerConditions = classifyConditionVO.getMainBorrowerConditions();
			/*
			 * 1、根据businessId获取到业务类型
			 */
			String businessTypeName = basicBusinessTypeMapper.queryBusinessTypeNameByBusinessId(businessId);

			/*
			 * 2、根据业务类型 获取 tb_five_level_classify 实体
			 */
			// 按严重级别降序排序，从最严重的开始匹配
			List<FiveLevelClassify> fiveLevelClassifies = fiveLevelClassifyService
					.selectList(new EntityWrapper<FiveLevelClassify>().eq("business_type", businessTypeName)
							.orderBy("class_level", false));
			if (CollectionUtils.isEmpty(fiveLevelClassifies)) {
				return resultClassName;
			}

			/*
			 * 3、获取 该业务的 利息逾期，首期逾期，本期逾期，贷后跟踪记录配置，风控配置
			 */

			Integer interestOverdue = repaymentBizPlanListService.queryInterestOverdueByBusinessId(businessId); // 利息逾期
			Integer firstPeriodOverdue = repaymentBizPlanListService.queryFirstPeriodOverdueByBusinessId(businessId); // 首期逾期
			Integer principalOverdue = repaymentBizPlanListService.queryPrincipalOverdueByBusinessId(businessId); // 本期逾期

			/*
			 * 4、根据 tb_five_level_classify 实体 id 关联 tb_five_level_classify_condition
			 * 的parent_id 且 valid_status = 1 的数据
			 */

			List<Boolean> bList = new ArrayList<>();

			for (FiveLevelClassify fiveLevelClassify : fiveLevelClassifies) {

				// 查询 是否配置对应的条件列表且状态是 1（有效）的
				List<FiveLevelClassifyCondition> conditions = fiveLevelClassifyConditionService
						.selectList(new EntityWrapper<FiveLevelClassifyCondition>()
								.eq("parent_id", fiveLevelClassify.getId()).eq("valid_status", "1"));

				if (CollectionUtils.isNotEmpty(conditions)) {
					// 按照条件名称分别装入 conditionMap
					Map<String, List<FiveLevelClassifyCondition>> conditionMap = handleConditions(conditions);

					if (!conditionMap.isEmpty()) {
						for (Map.Entry<String, List<FiveLevelClassifyCondition>> entry : conditionMap.entrySet()) {
							List<FiveLevelClassifyCondition> subConditions = entry.getValue();
							if (CollectionUtils.isNotEmpty(subConditions)) {

								String executeCondition = subConditions.get(0).getExecuteCondition();

								for (FiveLevelClassifyCondition condition : subConditions) {

									String paramType = condition.getParamType();

									String paramName = condition.getParamName();

									String relation = condition.getTypeNameRelation();

									// 首期逾期
									handleFirstPeriod(firstPeriodOverdue, bList, paramType, paramName, relation);
									// 利息逾期
									handleIntersectOverdue(interestOverdue, bList, paramType, paramName, relation);
									// 本金逾期
									handlePrincipalOverdue(principalOverdue, bList, paramType, paramName, relation);

									if (Constant.FIVE_LEVEL_CLASSIFY_OP_SOUSE_TYPE_ALMS_LOG.equals(opSourse)
											|| Constant.FIVE_LEVEL_CLASSIFY_OP_SOUSE_TYPE_ALMS_RISK_CONTROL
													.equals(opSourse)) {
										// 抵押物情况
										handleGuarantee(guaranteeConditions, bList, paramType, paramName);
										// 主借款人情况
										handleMainBorrower(mainBorrowerConditions, bList, paramType, paramName);
									}
								}
								// 满足条件的标识
								Boolean executeConditionFlag = handleExecuteCondition(bList, executeCondition);

								if (executeConditionFlag != null && executeConditionFlag) {
									resultClassName = fiveLevelClassify.getClassName();
									if (StringUtil.notEmpty(resultClassName)) {
										return resultClassName;
									}
								}
								bList.clear();
							}
						}
					}
				}
			}
			return resultClassName;
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ServiceException(e);
		}

	}

	private void handleMainBorrower(List<String> mainBorrowerConditions, List<Boolean> bList, String paramType,
			String paramName) {
		if (Constant.FIVE_LEVEL_CLASSIFY_MAIN_BORROWER.equals(paramType)) {
			if (CollectionUtils.isNotEmpty(mainBorrowerConditions)) {
				Boolean mainBorrowerFlag = mainBorrowerConditions.contains(paramName);
				bList.add(mainBorrowerFlag);
			} else {
				bList.add(false);
			}
		}
	}

	private List<Boolean> handleGuarantee(List<String> guaranteeConditions, List<Boolean> bList, String paramType,
			String paramName) {
		if (Constant.FIVE_LEVEL_CLASSIFY_GUARANTEE.equals(paramType)) {
			if (CollectionUtils.isNotEmpty(guaranteeConditions)) {
				Boolean guaranteeFlag = guaranteeConditions.contains(paramName);
				bList.add(guaranteeFlag);
			} else {
				bList.add(false);
			}
		}
		return bList;
	}

	private Boolean handleExecuteCondition(List<Boolean> bList, String executeCondition) {
		Boolean executeConditionFlag = null;
		if (!bList.isEmpty()) {
			// 1、满足所有条件
			if ("1".equals(executeCondition)) {
				for (Boolean flag : bList) {
					executeConditionFlag = executeConditionFlag == null ? flag : executeConditionFlag;
					executeConditionFlag = executeConditionFlag && flag;
				}
			}
			// 2、满足任意条件
			if ("2".equals(executeCondition)) {
				for (Boolean flag : bList) {
					executeConditionFlag = executeConditionFlag == null ? flag : executeConditionFlag;
					executeConditionFlag = executeConditionFlag || flag;
					if (executeConditionFlag) {
						break;
					}
				}
			}
		}
		return executeConditionFlag;
	}

	private List<Boolean> handleFirstPeriod(Integer firstPeriodOverdue, List<Boolean> bList, String paramType,
			String paramName, String relation) {

		if (Constant.FIVE_LEVEL_CLASSIFY_FIRST_PERIOD_OVERDUE.equals(paramType)) {

			Boolean firstPeriodOverdueFlag = false;
			firstPeriodOverdue = firstPeriodOverdue == null ? 0 : firstPeriodOverdue;

			if (Integer.valueOf(relation) == 1) {
				firstPeriodOverdueFlag = firstPeriodOverdue >= Integer.valueOf(paramName);
			} else if (Integer.valueOf(relation) == 2) {
				firstPeriodOverdueFlag = firstPeriodOverdue <= Integer.valueOf(paramName);
			} else if (Integer.valueOf(relation) == 3) {
				firstPeriodOverdueFlag = firstPeriodOverdue > Integer.valueOf(paramName);
			} else if (Integer.valueOf(relation) == 4) {
				firstPeriodOverdueFlag = firstPeriodOverdue < Integer.valueOf(paramName);
			} else if (Integer.valueOf(relation) == 5) {
				firstPeriodOverdueFlag = firstPeriodOverdue == Integer.valueOf(paramName);
			}
			bList.add(firstPeriodOverdueFlag);
		}
		return bList;
	}

	private List<Boolean> handlePrincipalOverdue(Integer principalOverdue, List<Boolean> bList, String paramType,
			String paramName, String relation) {

		if (Constant.FIVE_LEVEL_CLASSIFY_PRINCIPAL_OVERDUE.equals(paramType)) {

			Boolean principalOverdueFlag = false;
			principalOverdue = principalOverdue == null ? 0 : principalOverdue;

			if (Integer.valueOf(relation) == 1) {
				principalOverdueFlag = principalOverdue >= Integer.valueOf(paramName);
			} else if (Integer.valueOf(relation) == 2) {
				principalOverdueFlag = principalOverdue <= Integer.valueOf(paramName);
			} else if (Integer.valueOf(relation) == 3) {
				principalOverdueFlag = principalOverdue > Integer.valueOf(paramName);
			} else if (Integer.valueOf(relation) == 4) {
				principalOverdueFlag = principalOverdue < Integer.valueOf(paramName);
			} else if (Integer.valueOf(relation) == 5) {
				principalOverdueFlag = principalOverdue == Integer.valueOf(paramName);
			}
			bList.add(principalOverdueFlag);
		}
		return bList;
	}

	private List<Boolean> handleIntersectOverdue(Integer interestOverdue, List<Boolean> bList, String paramType,
			String paramName, String relation) {

		if (Constant.FIVE_LEVEL_CLASSIFY_INTEREST_OVERDUE.equals(paramType)) {

			Boolean interestOverdueFlag = false; // 是否利息逾期
			interestOverdue = interestOverdue == null ? 0 : interestOverdue;

			if (Integer.valueOf(relation) == 1) {
				interestOverdueFlag = interestOverdue >= Integer.valueOf(paramName);
			} else if (Integer.valueOf(relation) == 2) {
				interestOverdueFlag = interestOverdue <= Integer.valueOf(paramName);
			} else if (Integer.valueOf(relation) == 3) {
				interestOverdueFlag = interestOverdue > Integer.valueOf(paramName);
			} else if (Integer.valueOf(relation) == 4) {
				interestOverdueFlag = interestOverdue < Integer.valueOf(paramName);
			} else if (Integer.valueOf(relation) == 5) {
				interestOverdueFlag = interestOverdue == Integer.valueOf(paramName);
			}
			bList.add(interestOverdueFlag);
		}
		return bList;
	}

	private Map<String, List<FiveLevelClassifyCondition>> handleConditions(
			List<FiveLevelClassifyCondition> conditions) {
		Map<String, List<FiveLevelClassifyCondition>> conditionMap = new HashMap<>();

		for (FiveLevelClassifyCondition condition : conditions) {
			String subClassName = condition.getSubClassName();
			List<FiveLevelClassifyCondition> list = conditionMap.get(subClassName);
			if (list == null) {
				list = new ArrayList<>();
				list.add(condition);
				conditionMap.put(subClassName, list);
			} else {
				list.add(condition);
				conditionMap.put(subClassName, list);
			}
		}
		return conditionMap;
	}

	@Override
	public Integer queryMayBeUsed(String businessType, String className) {
		return fiveLevelClassifyService.queryMayBeUsed(businessType, className);
	}

	@Transactional(rollbackFor = Exception.class)
	@Override
	public void deleteFiveLevelClassify(Map<String, Object> paramMap) {
		try {
			String userId = loginUserInfoHelper.getUserId();
			Date currentTime = new Date();
			FiveLevelClassify classify = new FiveLevelClassify();
			String id = (String) paramMap.get("id");
			classify.setId(id);
			classify.setValidStatus("0"); // 0 、失效
			classify.setUpdateTime(currentTime);
			classify.setUpdateUser(userId);
			fiveLevelClassifyService.updateById(classify);

			FiveLevelClassifyCondition condition = new FiveLevelClassifyCondition();
			condition.setValidStatus("0");
			condition.setOpType("3");
			condition.setUpdateTime(currentTime);
			condition.setUpdateUser(userId);
			fiveLevelClassifyConditionService.update(condition,
					new EntityWrapper<FiveLevelClassifyCondition>().eq("parent_id", id));
		} catch (Exception e) {
			LOG.error(e.getMessage(), e);
			throw new ServiceException(e);
		}
	}

}
