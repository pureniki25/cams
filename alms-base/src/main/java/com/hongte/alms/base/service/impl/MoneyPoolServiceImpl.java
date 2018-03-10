package com.hongte.alms.base.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.collection.vo.AfterLoanStandingBookVo;
import com.hongte.alms.base.dto.RepaymentRegisterInfoDTO;
import com.hongte.alms.base.entity.MoneyPool;
import com.hongte.alms.base.entity.MoneyPoolRepayment;
import com.hongte.alms.base.entity.RepaymentBizPlanList;
import com.hongte.alms.base.enums.RepayRegisterFinanceStatus;
import com.hongte.alms.base.enums.RepayRegisterState;
import com.hongte.alms.base.mapper.MoneyPoolMapper;
import com.hongte.alms.base.mapper.MoneyPoolRepaymentMapper;
import com.hongte.alms.base.mapper.RepaymentBizPlanListMapper;
import com.hongte.alms.base.service.MoneyPoolService;
import com.hongte.alms.base.service.XindaiService;
import com.hongte.alms.base.vo.module.MatchedMoneyPoolVO;
import com.hongte.alms.base.vo.module.MoneyPoolRepaymentXindaiDTO;
import com.hongte.alms.base.vo.module.MoneyPoolVO;
import com.hongte.alms.base.vo.module.MoneyPoolXindaiDTO;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.DESC;
import com.hongte.alms.common.util.EncryptionResult;
import com.hongte.alms.common.vo.RequestData;
import com.hongte.alms.common.vo.ResponseData;
import com.hongte.alms.common.vo.ResponseEncryptData;

import feign.Feign;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.javassist.runtime.Desc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class MoneyPoolServiceImpl extends BaseServiceImpl<MoneyPoolMapper, MoneyPool> implements MoneyPoolService {
	final String URLPREFIX = "http://172.16.200.104:8084/apitest" ;
	final String XINDAI_ADDORUPDATE = "AfterLoanRepayment_AddOrUpdateRepayment" ;
	final String XINDAI_DEL = "AfterLoanRepayment_delRepayment" ;
	@Autowired
	MoneyPoolMapper moneyPoolMapper ;
	@Autowired
	MoneyPoolRepaymentMapper moneyPoolRepaymentMapper ;
	@Autowired
	RepaymentBizPlanListMapper repaymentBizPlanListMapper ;

	@Override
	public List<MoneyPoolVO> listMoneyPool(String businessId, String afterId) {
		return moneyPoolMapper.listMoneyPool(businessId, afterId,null,null);
	}

	@Override
	public List<MatchedMoneyPoolVO> listMatchedMoneyPool(String businessId, String afterId) {
		return moneyPoolRepaymentMapper.listMatchedMoneyPool(businessId, afterId);
	}

	@Override
	@Transactional(rollbackFor=Exception.class)
	public Boolean saveRepaymentRegisterInfo(RepaymentRegisterInfoDTO registerInfoDTO) {
		Boolean result = false ;
		try {
			if (registerInfoDTO.getMoneyPoolId()==null||registerInfoDTO.getMoneyPoolId().equals("")) {
				MoneyPool nMoneyPool = new MoneyPool(registerInfoDTO);
				
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
					
					
					JSONObject jsonObject = new JSONObject() ;
					jsonObject.put("MoneyPool", new MoneyPoolXindaiDTO(nMoneyPool));
					jsonObject.put("PoolRepayment", new MoneyPoolRepaymentXindaiDTO(moneyPoolRepayment, nMoneyPool, registerInfoDTO.getBusinessId(), registerInfoDTO.getAfterId()));
					ResponseData responseData = callRemoteService(jsonObject, XINDAI_ADDORUPDATE, URLPREFIX);
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
//						String moneyPoolRepaymentId = json.getString("moneyPoolRepaymentId");
						nMoneyPool.setXdPoolId(Integer.valueOf(moneyPoolId));
						moneyPoolRepayment.setXdPoolId(Integer.valueOf(moneyPoolId));
						moneyPoolMapper.updateById(nMoneyPool);
						moneyPoolRepaymentMapper.updateById(moneyPoolRepayment);
					}
					
				}else {
					throw new RuntimeException("添加数据失败");
				}
			}else {
				MoneyPool moneyPool = new MoneyPool(registerInfoDTO.getMoneyPoolId());
				moneyPool = moneyPoolMapper.selectOne(moneyPool);

				MoneyPoolRepayment moneyPoolRepayment = new MoneyPoolRepayment(moneyPool.getMoneyPoolId()) ;
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
					
					JSONObject jsonObject = new JSONObject() ;
					jsonObject.put("MoneyPool", new MoneyPoolXindaiDTO(moneyPool));
					jsonObject.put("PoolRepayment", new MoneyPoolRepaymentXindaiDTO(moneyPoolRepayment, moneyPool, registerInfoDTO.getBusinessId(), registerInfoDTO.getAfterId()));
					ResponseData responseData = callRemoteService(jsonObject, XINDAI_ADDORUPDATE, URLPREFIX);
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
//						String moneyPoolRepaymentId = json.getString("moneyPoolRepaymentId");
						moneyPool.setXdPoolId(Integer.valueOf(moneyPoolId));
						moneyPoolRepayment.setXdPoolId(Integer.valueOf(moneyPoolId));
						moneyPoolMapper.updateById(moneyPool);
						moneyPoolRepaymentMapper.updateById(moneyPoolRepayment);
					}
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
		MoneyPoolRepayment moneyPoolRepayment = new MoneyPoolRepayment(moneyPoolId) ;
		moneyPoolRepayment = moneyPoolRepaymentMapper.selectOne(moneyPoolRepayment);
		if (moneyPoolRepayment==null) {
			throw new RuntimeException("moneyPoolRepayment was not found");
		}
		moneyPoolRepayment.setIsDeleted(1);
		moneyPoolRepayment.setDeleteTime(new Date());
		moneyPoolRepayment.setDeleteUser(userId);
		
		JSONObject jsonObject = new JSONObject() ;
		jsonObject.put("MoneyPoolId", moneyPoolRepayment.getXdPoolId());
		ResponseData responseData = callRemoteService(jsonObject, XINDAI_DEL, URLPREFIX);
		if (!responseData.getReturnCode().equals("1")) {
			throw new RuntimeException(responseData.getReturnMessage()) ;
		}
		
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
		MoneyPoolRepayment moneyPoolRepayment = new MoneyPoolRepayment(moneyPoolId);
		moneyPoolRepayment = moneyPoolRepaymentMapper.selectOne(moneyPoolRepayment);
		
		MoneyPool moneyPool = new MoneyPool(moneyPoolId) ;
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
        int count = moneyPoolMapper.listMoneyPoolCount(businessId, afterId);
        pages.setTotal(count);
        List<MoneyPoolVO> list = moneyPoolMapper.listMoneyPool(businessId, afterId,page,limit);
        pages.setRecords(list);
        return pages ;
	}
	
	public ResponseData callRemoteService(JSONObject jsonObject,String methodName,String urlPrefix) throws RuntimeException {
		DESC desc = new DESC();
		RequestData requestData = new RequestData();
		requestData.setData(jsonObject.toJSONString());
		requestData.setMethodName(methodName);
		String encryptStr = JSON.toJSONString(requestData);
		// 请求数据加密
		encryptStr = desc.Encryption(encryptStr);

		XindaiService xindaiService = Feign.builder().target(XindaiService.class, urlPrefix);
		String response = xindaiService.dod(encryptStr);

		// 返回数据解密
		ResponseEncryptData resp = JSON.parseObject(response, ResponseEncryptData.class);
		String decryptStr = desc.Decode(resp.getA(), resp.getUUId());
		EncryptionResult res = JSON.parseObject(decryptStr, EncryptionResult.class);
		ResponseData respData = JSON.parseObject(res.getParam(), ResponseData.class);
		System.out.println(JSON.toJSONString(respData));
		return respData ;
	}
}
