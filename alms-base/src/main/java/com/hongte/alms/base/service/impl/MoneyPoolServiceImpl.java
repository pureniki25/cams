package com.hongte.alms.base.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.collection.vo.AfterLoanStandingBookVo;
import com.hongte.alms.base.dto.ConfirmRepaymentReq;
import com.hongte.alms.base.dto.MoneyPoolManagerReq;
import com.hongte.alms.base.dto.RepaymentRegisterInfoDTO;
import com.hongte.alms.base.entity.MoneyPool;
import com.hongte.alms.base.entity.MoneyPoolRepayment;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.entity.RepaymentResource;
import com.hongte.alms.base.enums.RepayCurrentStatusEnums;
import com.hongte.alms.base.enums.RepayRegisterFinanceStatus;
import com.hongte.alms.base.enums.RepayRegisterState;
import com.hongte.alms.base.enums.repayPlan.RepayPlanAccountConfirmStatusEnum;
import com.hongte.alms.base.enums.repayPlan.RepayPlanRepaySrcEnum;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.mapper.MoneyPoolMapper;
import com.hongte.alms.base.mapper.MoneyPoolRepaymentMapper;
import com.hongte.alms.base.mapper.RepaymentBizPlanListMapper;
import com.hongte.alms.base.mapper.RepaymentConfirmLogMapper;
import com.hongte.alms.base.service.MoneyPoolService;
import com.hongte.alms.base.service.XindaiService;
import com.hongte.alms.base.vo.finance.MoneyPoolManagerVO;
import com.hongte.alms.base.vo.module.MatchedMoneyPoolVO;
import com.hongte.alms.base.vo.module.MoneyPoolRepaymentXindaiDTO;
import com.hongte.alms.base.vo.module.MoneyPoolVO;
import com.hongte.alms.base.vo.module.MoneyPoolXindaiDTO;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.DESC;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.EncryptionResult;
import com.hongte.alms.common.vo.RequestData;
import com.hongte.alms.common.vo.ResponseData;
import com.hongte.alms.common.vo.ResponseEncryptData;
import com.ht.ussp.bean.LoginUserInfoHelper;

import feign.Feign;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.javassist.runtime.Desc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 财务款项池表 服务实现类
 * </p>
 *
 * @author 王继光
 * @since 2018-02-28
 */
@Service("MoneyPoolService")
@RefreshScope
public class MoneyPoolServiceImpl extends BaseServiceImpl<MoneyPoolMapper, MoneyPool> implements MoneyPoolService {
	private Logger logger = LoggerFactory.getLogger(MoneyPoolServiceImpl.class);
	
	final String URLPREFIX = "http://172.16.200.104:8084/apitest" ;
	final String XINDAI_ADDORUPDATE = "AfterLoanRepayment_AddOrUpdateRepayment" ;
	final String XINDAI_DEL = "AfterLoanRepayment_delRepayment" ;
	
	@Value("${bmApi.apiUrl}")
	String xindaiAplUrlUrl ;
	
	@Autowired
	MoneyPoolMapper moneyPoolMapper ;
	@Autowired
	MoneyPoolRepaymentMapper moneyPoolRepaymentMapper ;
	@Autowired
	RepaymentBizPlanListMapper repaymentBizPlanListMapper ;

	@Autowired
	RepaymentConfirmLogMapper confirmLogMapper;
	@Autowired
	LoginUserInfoHelper loginUserInfoHelper ;
	@Override
	public List<MoneyPoolVO> listMoneyPool(String businessId, String afterId) {
		return null;
	}

	@Override
	public List<MatchedMoneyPoolVO> listMatchedMoneyPool(String businessId, String afterId,Boolean notConfirmed) {
		return moneyPoolMapper.listMatchedMoneyPool( businessId,  afterId , notConfirmed);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public Boolean saveRepaymentRegisterInfo(RepaymentRegisterInfoDTO registerInfoDTO) {
		Boolean result = false ;
		try {
			if (registerInfoDTO.getMoneyPoolId()==null||registerInfoDTO.getMoneyPoolId().equals("")) {
				MoneyPool nMoneyPool = new MoneyPool();
				
				Integer moneyPoolRows = moneyPoolMapper.insert(nMoneyPool);
				RepaymentBizPlanList repaymentBizPlanList = new RepaymentBizPlanList() ;
				repaymentBizPlanList.setBusinessId(registerInfoDTO.getBusinessId());
				repaymentBizPlanList.setAfterId(registerInfoDTO.getAfterId());
				repaymentBizPlanList = repaymentBizPlanListMapper.selectOne(repaymentBizPlanList);
				MoneyPoolRepayment moneyPoolRepayment = new MoneyPoolRepayment() ;
				moneyPoolRepayment.setBankAccount(registerInfoDTO.getAcceptBank());
				moneyPoolRepayment.setCertificatePictureUrl(registerInfoDTO.getCert());
				Date now = new Date() ;
				moneyPoolRepayment.setCreateTime(now);
				moneyPoolRepayment.setUpdateTime(now);
				moneyPoolRepayment.setUpdateUser(registerInfoDTO.getUserId());
				moneyPoolRepayment.setCreateUser(registerInfoDTO.getUserId());
				moneyPoolRepayment.setFactTransferName(registerInfoDTO.getFactRepaymentUser());
				moneyPoolRepayment.setMoneyPoolId(nMoneyPool.getMoneyPoolId());
				moneyPoolRepayment.setPlanListId(repaymentBizPlanList.getPlanListId());
				moneyPoolRepayment.setRemark(registerInfoDTO.getRemark());
				moneyPoolRepayment.setState(nMoneyPool.getFinanceStatus());
				moneyPoolRepayment.setTradePlace(registerInfoDTO.getTradePlace());
				moneyPoolRepayment.setTradeType(registerInfoDTO.getTradeType());
				
				Integer moneyPoolRepaymentRows = moneyPoolRepaymentMapper.insert(moneyPoolRepayment) ;
				if (moneyPoolRows==1&&moneyPoolRepaymentRows==1) {
					
					
					ResponseData responseData = callRemoteService(new MoneyPoolRepaymentXindaiDTO(moneyPoolRepayment,registerInfoDTO.getBusinessId(),registerInfoDTO.getAfterId()), XINDAI_ADDORUPDATE);
					if (responseData==null) {
						throw new RuntimeException();
					}
					if (!responseData.getReturnCode().equals("1")) {
						throw new RuntimeException();
					}
					String  data = responseData.getData();
					if (data!=null) {
						JSONObject json = JSONObject.parseObject(data);
						String moneyPoolId = json.getString("moneyPoolId");
						String moneyPoolRepaymentId = json.getString("moneyPoolRepaymentId");
						moneyPoolRepayment.setXdMatchingId(moneyPoolRepaymentRows);
						nMoneyPool.setXdPoolId(Integer.valueOf(moneyPoolId));
						moneyPoolRepayment.setXdPoolId(Integer.valueOf(moneyPoolId));
						moneyPoolMapper.updateById(nMoneyPool);
						moneyPoolRepaymentMapper.updateById(moneyPoolRepayment);
					}
					
				}else {
					throw new RuntimeException("添加数据失败");
				}
			}else {
				MoneyPool moneyPool = new MoneyPool();
				moneyPool = moneyPoolMapper.selectOne(moneyPool);

				MoneyPoolRepayment moneyPoolRepayment = new MoneyPoolRepayment() ;
				moneyPoolRepayment = moneyPoolRepaymentMapper.selectOne(moneyPoolRepayment);
				
				if (moneyPoolRepayment==null) {
					throw new RuntimeException("moneyPoolRepayment was not found");
				}
				
				if (moneyPool==null) {
					throw new RuntimeException("moneyPool was not found");
				}
				if (moneyPool.getFinanceStatus().equals(RepayRegisterFinanceStatus.财务确认已还款.toString())||
						moneyPool.getFinanceStatus().equals(RepayRegisterFinanceStatus.财务指定银行流水.toString())) {
					throw new RuntimeException("财务确认已还款/财务指定银行流水 的记录不能编辑");
				}
				
				SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
				moneyPool.setRemitBank(registerInfoDTO.getFactRepaymentUser());
				moneyPool.setAcceptBank(registerInfoDTO.getAcceptBank());
				BigDecimal accountMoney = new BigDecimal(registerInfoDTO.getRepaymentMoney());
				moneyPool.setAccountMoney(accountMoney);
				moneyPool.setTradeDate(format.parse(registerInfoDTO.getRepaymentDate()));
				moneyPool.setTradePlace(registerInfoDTO.getTradePlace());
				moneyPool.setTradeType(registerInfoDTO.getTradeType());
				moneyPool.setUpdateTime(new Date());
				moneyPool.setUpdateUser(registerInfoDTO.getUserId());
				moneyPool.setMoneyPoolId(registerInfoDTO.getMoneyPoolId());
				Integer moneyPoolRows = moneyPoolMapper.updateById(moneyPool);
				if (moneyPool.getFinanceStatus().equals(RepayRegisterFinanceStatus.还款登记被拒绝.toString())||
						moneyPool.getFinanceStatus().equals(RepayRegisterFinanceStatus.还款待确认.toString())) {
					moneyPool.setFinanceStatus(RepayRegisterFinanceStatus.未关联银行流水.toString());
				}
				
				moneyPoolRepayment.setBankAccount(registerInfoDTO.getAcceptBank());
				moneyPoolRepayment.setCertificatePictureUrl(registerInfoDTO.getCert());
				moneyPoolRepayment.setUpdateTime(new Date());
				moneyPoolRepayment.setUpdateUser(registerInfoDTO.getUserId());
				moneyPoolRepayment.setFactTransferName(registerInfoDTO.getFactRepaymentUser());
				moneyPoolRepayment.setRemark(registerInfoDTO.getRemark());
				moneyPoolRepayment.setState(moneyPool.getFinanceStatus());
				moneyPoolRepayment.setTradePlace(registerInfoDTO.getTradePlace());
				moneyPoolRepayment.setTradeType(registerInfoDTO.getTradeType());
				
				Integer moneyPoolRepaymentRows = moneyPoolRepaymentMapper.updateById(moneyPoolRepayment);
				if (moneyPoolRows==1&&moneyPoolRepaymentRows==1) {
					
				}else {
					throw new RuntimeException("编辑数据失败");
				}
			}
			
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		} 
		return result ;
	}

	@Override
	public Boolean deleteRepaymentRegeisterInfo(String moneyPoolId,String userId) {
		boolean result = false ;
		if (userId==null) {
			throw new RuntimeException("userId can't be null");
		}
		if (moneyPoolId==null) {
			throw new RuntimeException("moneyPoolId can't be null");
		}
		MoneyPoolRepayment moneyPoolRepayment = new MoneyPoolRepayment() ;
		moneyPoolRepayment = moneyPoolRepaymentMapper.selectOne(moneyPoolRepayment);
		if (moneyPoolRepayment==null) {
			throw new RuntimeException("moneyPoolRepayment was not found");
		}
		moneyPoolRepayment.setIsDeleted(1);
		moneyPoolRepayment.setDeleteTime(new Date());
		moneyPoolRepayment.setDeleteUser(userId);
		Integer rows = moneyPoolRepaymentMapper.updateById(moneyPoolRepayment);
		if (rows==1) {
			result = true ;
		}else {
			throw new RuntimeException("update moneyPoolRepayment fail , please check sql");
		}
		return result;
	}

	@Override
	public MoneyPoolVO getMoneyPool(String moneyPoolId) {
		List<MoneyPoolRepayment> moneyPoolRepayments = moneyPoolRepaymentMapper.selectList(new EntityWrapper<MoneyPoolRepayment>().eq("money_pool_id", moneyPoolId).ne("state",RepayRegisterFinanceStatus.未关联银行流水.toString()));
		if (moneyPoolRepayments==null) {
			throw new ServiceRuntimeException("找不到对应的还款登记");
		}
		MoneyPoolRepayment moneyPoolRepayment = moneyPoolRepayments.get(0);
		
		MoneyPool moneyPool = new MoneyPool() ;
		moneyPool = moneyPoolMapper.selectOne(moneyPool);
		if (moneyPool==null) {
			throw new RuntimeException("moneyPool was not found");
		}
		MoneyPoolVO moneyPoolVO = new MoneyPoolVO() ;
		moneyPoolVO.setAcceptBank(moneyPool.getAcceptBank());
		moneyPoolVO.setId(moneyPool.getMoneyPoolId());
		moneyPoolVO.setRealRepaymentUser(moneyPool.getRemitBank());
		moneyPoolVO.setRepaymentDate(moneyPool.getTradeDate());
		moneyPoolVO.setRepaymentMoney(moneyPool.getAccountMoney().toString());
		moneyPoolVO.setStatus(moneyPool.getFinanceStatus());
		moneyPoolVO.setTradePlace(moneyPool.getTradePlace());
		moneyPoolVO.setTradeType(moneyPool.getTradeType());
		moneyPoolVO.setUpdateTime(moneyPool.getUpdateTime());
		moneyPoolVO.setUpdateUser(moneyPool.getUpdateUser());
		moneyPoolVO.setRemark(moneyPool.getTradeRemark());
		moneyPoolVO.setCertUrl(moneyPoolRepayment.getCertificatePictureUrl());
		return moneyPoolVO;
	}

	@Override
	public Page<MoneyPoolVO> listMoneyPoolByPage(String businessId, String afterId, Integer page, Integer limit) {
		Page<MoneyPoolVO> pages = new Page<>();
        pages.setCurrent(page);
        pages.setSize(limit);
        int count = 0;
        pages.setTotal(count);
        List<MoneyPoolVO> list = null;
        pages.setRecords(list);
        return pages ;
	}
	
	public ResponseData callRemoteService(MoneyPoolRepaymentXindaiDTO moneyPoolRepaymentXindaiDTO,String methodName) throws RuntimeException {
		logger.info("调用callRemoteService");
		if (xindaiAplUrlUrl==null) {
			logger.error("没有配置xindaiAplUrlUrlPreFix");
			throw new RuntimeException("没有配置xindaiAplUrlUrlPreFix");
		}else {
			logger.info("xindaiAplUrlUrl:"+xindaiAplUrlUrl);
		}
		DESC desc = new DESC();
		RequestData requestData = new RequestData();
		requestData.setMethodName(methodName);
		if (methodName.equals(XINDAI_DEL)) {
			requestData.setData(moneyPoolRepaymentXindaiDTO.getId().toString());
		}else if (methodName.equals(XINDAI_ADDORUPDATE)) {
			requestData.setData(JSONObject.toJSONString(moneyPoolRepaymentXindaiDTO));
		}
		logger.info("原始数据-开始");
		logger.info(JSON.toJSONString(requestData));
		logger.info("原始数据-结束");
		String encryptStr = JSON.toJSONString(requestData);
		// 请求数据加密
		encryptStr = desc.Encryption(encryptStr);
		logger.info("请求数据-开始");
		logger.info(encryptStr);
		logger.info("请求数据-结束");
		XindaiService xindaiService = Feign.builder().target(XindaiService.class, xindaiAplUrlUrl);
		String response = xindaiService.dod(encryptStr);

		// 返回数据解密
		ResponseEncryptData resp = JSON.parseObject(response, ResponseEncryptData.class);
		String decryptStr = desc.Decode(resp.getA(), resp.getUUId());
		EncryptionResult res = JSON.parseObject(decryptStr, EncryptionResult.class);
		ResponseData respData = JSON.parseObject(res.getParam(), ResponseData.class);
		
		logger.info("信贷返回数据解密-开始");
		logger.info(JSON.toJSONString(respData));
		logger.info("信贷返回数据解密-结束");
		return respData ;
	}

	/* (non-Javadoc)
	 * @see com.hongte.alms.base.service.MoneyPoolService#addCustomerRepayment(com.hongte.alms.base.dto.RepaymentRegisterInfoDTO)
	 */
	@Override
	public Result addCustomerRepayment(RepaymentRegisterInfoDTO registerInfoDTO) {
		RepaymentBizPlanList repaymentBizPlanList = new RepaymentBizPlanList() ;
		repaymentBizPlanList.setOrigBusinessId(registerInfoDTO.getBusinessId());
		repaymentBizPlanList.setAfterId(registerInfoDTO.getAfterId());
		 repaymentBizPlanList = repaymentBizPlanListMapper.selectOne(repaymentBizPlanList);

		if (repaymentBizPlanList==null) {
			return Result.error("500", "找不到对应的还款计划");
		}
		MoneyPoolRepayment moneyPoolRepayment = new MoneyPoolRepayment(registerInfoDTO);
		moneyPoolRepayment.setPlanListId(repaymentBizPlanList.getPlanListId());
		moneyPoolRepayment.setCreateTime(new Date());
		moneyPoolRepayment.setCreateUser(registerInfoDTO.getUserId());
		moneyPoolRepayment.setCreateUserRole("客户");
		moneyPoolRepayment.setIsFinanceMatch(0);
		moneyPoolRepayment.setState(RepayRegisterFinanceStatus.未关联银行流水.toString());	
		moneyPoolRepayment.setOriginalBusinessId(registerInfoDTO.getBusinessId());
		moneyPoolRepayment.setAfterId(registerInfoDTO.getAfterId());
		boolean result = moneyPoolRepayment.insert();
		if (result) {
			ResponseData responseData = callRemoteService(new MoneyPoolRepaymentXindaiDTO(moneyPoolRepayment, registerInfoDTO.getBusinessId(), registerInfoDTO.getAfterId()), XINDAI_ADDORUPDATE);
			if (responseData==null) {
				throw new RuntimeException();
			}
			if (!responseData.getReturnCode().equals("1")) {
				throw new RuntimeException();
			}
			String  data = responseData.getData();
			if (data!=null) {
				moneyPoolRepayment.setXdMatchingId(Integer.valueOf(data));
				moneyPoolRepaymentMapper.updateById(moneyPoolRepayment);
				return Result.success();
			}else {
				throw new RuntimeException("信贷解密数据为null");
			}
		}else {
			return Result.error("500", "数据新增失败");
		}
	}

	/* (non-Javadoc)
	 * @see com.hongte.alms.base.service.MoneyPoolService#updateCustomerRepayment(com.hongte.alms.base.dto.RepaymentRegisterInfoDTO)
	 */
	@Override
	public Result updateCustomerRepayment(RepaymentRegisterInfoDTO registerInfoDTO) {
		if (registerInfoDTO==null) {
			return Result.error("500", "参数不能为空");
		}
		if (registerInfoDTO.getMoneyPoolId()==null) {
			return Result.error("500", "id未知"); 
		}
		MoneyPoolRepayment moneyPoolRepayment = moneyPoolRepaymentMapper.selectById(registerInfoDTO.getMoneyPoolId());
		if (moneyPoolRepayment==null) {
			return Result.error("500", "查不到此还款登记"); 
		}
		moneyPoolRepayment.update(registerInfoDTO);
		boolean result = moneyPoolRepayment.updateAllColumnById();
		
		if (result) {
			ResponseData responseData = callRemoteService(new MoneyPoolRepaymentXindaiDTO(moneyPoolRepayment, registerInfoDTO.getBusinessId(), registerInfoDTO.getAfterId()), XINDAI_ADDORUPDATE);
			if (responseData==null) {
				throw new RuntimeException();
			}
			if (!responseData.getReturnCode().equals("1")) {
				throw new RuntimeException();
			}
			String  data = responseData.getData();
			if (data!=null) {
				moneyPoolRepayment.setXdMatchingId(Integer.valueOf(data));
				moneyPoolRepaymentMapper.updateById(moneyPoolRepayment);
				return Result.success();
			}else {
				throw new RuntimeException("信贷解密数据为null");
			}
		}else {
			return Result.error("500", "数据更新失败");
		}
	}

	/* (non-Javadoc)
	 * @see com.hongte.alms.base.service.MoneyPoolService#deleteCustermerRepayment(com.hongte.alms.base.dto.RepaymentRegisterInfoDTO)
	 */
	@Override
	public Result deleteCustermerRepayment(RepaymentRegisterInfoDTO registerInfoDTO) {
		if (registerInfoDTO == null) {
			return Result.error("500", "参数不能为空");
		}
		if (registerInfoDTO.getMoneyPoolId() == null) {
			return Result.error("500", "id未知");
		}
		MoneyPoolRepayment moneyPoolRepayment = moneyPoolRepaymentMapper.selectById(registerInfoDTO.getMoneyPoolId());
		if (moneyPoolRepayment == null) {
			return Result.error("500", "查不到此还款登记");
		}
		if (moneyPoolRepayment.getState().equals(RepayRegisterFinanceStatus.财务指定银行流水.toString())) {
			return Result.error("500", "财务指定银行流水,不能删除");
		}
		if (moneyPoolRepayment.getState().equals(RepayRegisterFinanceStatus.财务确认已还款.toString())) {
			return Result.error("500", "财务确认已还款,不能删除");
		}

		boolean result = moneyPoolRepayment.deleteById();
		if (result) {
			ResponseData responseData = callRemoteService(new MoneyPoolRepaymentXindaiDTO(moneyPoolRepayment, registerInfoDTO.getBusinessId(), registerInfoDTO.getAfterId()), XINDAI_DEL);
			if (responseData==null) {
				throw new RuntimeException();
			}
			if (!responseData.getReturnCode().equals("1")) {
				throw new RuntimeException(responseData.getReturnMessage());
			}
			String  data = responseData.getData();
			if (data!=null) {
				return Result.success();
			}else {
				throw new RuntimeException("信贷解密数据为null");
			}
		}else {
			return Result.error("500", "数据删除失败");
		}
	}

	/* (non-Javadoc)
	 * @see com.hongte.alms.base.service.MoneyPoolService#matchBankStatement(java.util.List, java.lang.String, java.lang.String)
	 */
	@Override
	public Result matchBankStatement(List<MoneyPool> moneyPools, String businessId, String afterId) {
		List<RepaymentBizPlanList> planLists = repaymentBizPlanListMapper.selectList(new EntityWrapper<RepaymentBizPlanList>().eq("business_id", businessId).eq("after_id", afterId));
		if (planLists==null||planLists.size()==0) {
			return Result.error("500", "找不到对应的还款计划");
		}
		for (MoneyPool moneyPool : moneyPools) {
			MoneyPoolRepayment moneyPoolRepayment = copy(moneyPool, businessId, afterId);
			moneyPoolRepayment.setIsFinanceMatch(1);
			
		}
		return null;
	}
	
	private MoneyPoolRepayment copy(MoneyPool moneyPool, String businessId, String afterId) {
		if (moneyPool==null) {
			throw new RuntimeException();
		}
		Date now = new Date();
		String userId = loginUserInfoHelper.getUserId();
		
		MoneyPoolRepayment repayment = new MoneyPoolRepayment();
		repayment.setAccountMoney(moneyPool.getAccountMoney());
		repayment.setAfterId(afterId);
		repayment.setBankAccount(moneyPool.getAcceptBank());
		repayment.setClaimDate(now);
		repayment.setFactTransferName(moneyPool.getRemitBank());
		repayment.setIncomeType(moneyPool.getIncomeType());
		repayment.setMoneyPoolId(moneyPool.getMoneyPoolId());
		repayment.setOperateId(userId);
		repayment.setOperateName(userId);
		repayment.setTradeType(moneyPool.getTradeType());
		repayment.setTradePlace(moneyPool.getTradePlace());
		repayment.setTradeDate(moneyPool.getTradeDate());
		
		return repayment;
		
	}

	@Override
	public void confirmRepaidUpdateMoneyPool(ConfirmRepaymentReq req) {
		if (req.getMprIds()==null||req.getMprIds().size()==0) {
			return ;
		}
		List<MoneyPoolRepayment> moneyPoolRepayments = moneyPoolRepaymentMapper.selectBatchIds(req.getMprIds());
		for (MoneyPoolRepayment mpr : moneyPoolRepayments) {
			mpr.setState(RepayRegisterFinanceStatus.财务确认已还款.toString());
			MoneyPool moneyPool = moneyPoolMapper.selectById(mpr.getMoneyPoolId());
			moneyPool.setStatus(RepayRegisterState.完成.toString());
			moneyPool.setFinanceStatus(RepayRegisterFinanceStatus.财务确认已还款.toString());
			mpr.updateById();
			moneyPool.updateById();
		}
	}

	@Override
	public void revokeConfirmRepaidUpdateMoneyPool(RepaymentResource repaymentResource) {
		if (repaymentResource!=null&&repaymentResource.getRepaySource().equals(
				RepayPlanRepaySrcEnum.OFFLINE_TRANSFER.getValue().toString())) {
			MoneyPoolRepayment mpr = moneyPoolRepaymentMapper.selectById(repaymentResource.getRepaySourceRefId());
			MoneyPool moneyPool = moneyPoolMapper.selectById(mpr.getMoneyPoolId()); 
			mpr.setState(RepayRegisterFinanceStatus.未关联银行流水.toString());
			mpr.setIsFinanceMatch(0);
			moneyPool.setStatus(RepayRegisterState.待领取.toString());
			moneyPool.setFinanceStatus(RepayRegisterFinanceStatus.未关联银行流水.toString());
			mpr.updateById();
			moneyPool.updateById();
		}
	}


	@Override
	public Page<MoneyPoolManagerVO> selectMoneyPoolManagerList(MoneyPoolManagerReq req) {
		Page<MoneyPoolManagerVO> page = new Page<>(req.getCurPage(),req.getPageSize());
		int total = moneyPoolMapper.conutMoneyPoolManagerList(req);
		List<MoneyPoolManagerVO> moneyPoolManagerVOs = moneyPoolMapper.selectMoneyPoolManagerList(req);
		page.setTotal(total);
		page.setRecords(moneyPoolManagerVOs);
		return page;
	}

}
