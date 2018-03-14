package com.hongte.alms.core.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.TransferLitigationCar;
import com.hongte.alms.base.entity.TransferLitigationHouse;
import com.hongte.alms.base.process.enums.ProcessTypeEnums;
import com.hongte.alms.base.process.service.ProcessService;
import com.hongte.alms.base.process.vo.ProcessLogReq;
import com.hongte.alms.base.service.TransferLitigationCarService;
import com.hongte.alms.base.service.TransferLitigationHouseService;
import com.hongte.alms.base.service.TransferOfLitigationService;
import com.hongte.alms.base.vo.billing.CarLoanBilVO;
import com.hongte.alms.base.vo.litigation.TransferOfLitigationVO;
import com.hongte.alms.base.vo.litigation.house.HouseLoanVO;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.JsonUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/transferOfLitigation")
@Api(tags = "TransferOfLitigationControllerApi", description = "诉讼移交API", hidden = true)
public class TransferOfLitigationController {

	private static final Logger LOG = LoggerFactory.getLogger(TransferOfLitigationController.class);

	@Autowired
	@Qualifier("transferLitigationService")
	private TransferOfLitigationService transferOfLitigationService;

	@Autowired
	@Qualifier("TransfLitigationHouseService")
	private TransferLitigationHouseService transferLitigationHouseService;

	@Autowired
	@Qualifier("TransfLitigationCarService")
	private TransferLitigationCarService transferLitigationCarService;

	@Autowired
	@Qualifier("ProcessService")
	private ProcessService processService;

	/**
	 * 获取车贷诉讼相关数据
	 * 
	 * @author huweiqian
	 * @date 2018/02/27
	 * @return 车贷诉讼相关数据
	 */
	@ApiOperation(value = "获取车贷诉讼相关数据")
	@GetMapping("/queryCarLoanData")
	@ResponseBody
	public Result<Map<String, Object>> queryCarLoanData(@RequestParam String businessId,
			@RequestParam(value = "processId", required = false) String processId) {
		try {
			Map<String, Object> carLoanData = transferOfLitigationService.queryCarLoanData(businessId);
			if (carLoanData != null && !carLoanData.isEmpty()) {

				carLoanData.put("baseInfo", JSON.toJSON(carLoanData, JsonUtil.getMapping()));

				if (processId != null) {
					List<TransferLitigationCar> applyList = transferLitigationCarService
							.selectList(new EntityWrapper<TransferLitigationCar>().eq("process_id", processId));
					
					String houseAddr = applyList.get(0).getHouseAddress();
					StringBuilder builder = new StringBuilder();
					String[] houseArr = houseAddr.split("#");
					
					int count = 1;
					for (String hArr : houseArr) {
						String[] split = hArr.split("\\^");
						String area = split[0];
						String area2 = area.replace("[", "");
						String area3 = area2.replace("]", "");
						List<String> areaList = Arrays.asList(area3.split(","));
						if (count < houseArr.length) {
							builder.append("房产地址" + count++ + "：").append(areaList.get(0)).append(areaList.get(1)).append(areaList.get(2)).append(" " + split[1]).append("；房产抵押情况：").append(split[2]).append("#");
						}else {
							builder.append("房产地址" + count++ + "：").append(areaList.get(0)).append(areaList.get(1)).append(areaList.get(2)).append(" " + split[1]).append("；房产抵押情况：").append(split[2]);
						}
					}
					
					carLoanData.put("houseAddress", builder.toString());
					carLoanData.put("carList", (JSONArray) JSON.toJSON(applyList, JsonUtil.getMapping()));
				}

				processService.getProcessShowInfo(carLoanData, processId, ProcessTypeEnums.CAR_LOAN_LITIGATION);

				return Result.success(carLoanData);
			} else {
				return Result.error("0", "没有数据");
			}
		} catch (Exception e) {
			LOG.error("获取车贷诉讼相关数据异常!!!", e);
			return Result.error("500", "系统异常");
		}
	}

	/**
	 * 获取房贷诉讼相关数据
	 * 
	 * @author huweiqian
	 * @date 2018/02/27
	 * @return 房贷诉讼相关数据
	 */
	@ApiOperation(value = "获取房贷诉讼相关数据")
	@GetMapping("/queryHouseLoanData")
	@ResponseBody
	public Result<Map<String, Object>> queryHouseLoanData(@RequestParam String businessId,
			@RequestParam(value = "processId", required = false) String processId) {
		Map<String, Object> resultMap = new HashMap<>();
		try {
			HouseLoanVO houseLoanData = transferOfLitigationService.queryHouseLoanData(businessId);
			if (houseLoanData == null) {
				return Result.error("0", "没有数据");
			}

			if (processId != null) {
				List<TransferLitigationHouse> applyList = transferLitigationHouseService
						.selectList(new EntityWrapper<TransferLitigationHouse>().eq("process_id", processId));
				resultMap.put("houseList", (JSONArray) JSON.toJSON(applyList, JsonUtil.getMapping()));
			}
			resultMap.put("baseInfo", JSON.toJSON(houseLoanData, JsonUtil.getMapping()));
			processService.getProcessShowInfo(resultMap, processId, ProcessTypeEnums.HOUSE_LOAN_LITIGATION);

			return Result.success(resultMap);
		} catch (Exception e) {
			LOG.error("-- queryTransferLitigationData -- 获取房贷诉讼相关数据异常！！！", e);
			return Result.error("500", "系统异常");
		}
	}

	@ApiOperation(value = "存储房贷移交诉讼信息")
	@PostMapping("/saveTransferLitigationHouse")
	@ResponseBody
	public Result<String> saveTransferLitigationHouse(@RequestBody TransferLitigationHouse req) {
		try {
			transferOfLitigationService.saveTransferLitigationHouse(req);
			return Result.success();
		} catch (Exception ex) {
			LOG.error(ex.getMessage());
			return Result.error("500", ex.getMessage());
		}
	}

	@SuppressWarnings({ "unchecked" })
	@ApiOperation(value = "存储车贷移交诉讼信息")
	@PostMapping("/saveTransferLitigationCar")
	@ResponseBody
	public Result<String> saveTransferLitigationCar(@RequestBody Map<String, Object> req) {
		try {
			TransferLitigationCar car = new TransferLitigationCar();
			car.setAlmsOpinion((String)req.get("almsOpinion"));
			car.setBusinessId((String)req.get("businessId"));
			car.setCarCondition((String)req.get("carCondition"));
			car.setCrpId((String)req.get("crpId"));
			car.setDelayHandover((String)req.get("delayHandover"));
			car.setDelayHandoverDesc((String)req.get("delayHandoverDesc"));
			car.setEstates((String)req.get("estates"));
			car.setProcessStatus((String)req.get("processStatus"));
			
			StringBuilder houseAddress = new StringBuilder();
			List<LinkedHashMap<String, Object>> componentOptions = (List<LinkedHashMap<String, Object>>) req.get("componentOption");
			for (LinkedHashMap<String, Object> componentOption : componentOptions) {
				LinkedHashMap<String, Object> registrationInfoForm = (LinkedHashMap<String, Object>) componentOption.get("registrationInfoForm");
				List<String> houseAreas = (List<String>) registrationInfoForm.get("houseArea");
				String detailAddress = (String) registrationInfoForm.get("detailAddress");
				String mortgageSituation = (String) registrationInfoForm.get("mortgageSituation");
				houseAddress.append(houseAreas).append("^").append(detailAddress).append("^").append(mortgageSituation).append("#");
			}
			
			car.setHouseAddress(houseAddress.toString());
			
			transferOfLitigationService.saveTransferLitigationCar(car);
			return Result.success();
		} catch (Exception ex) {
			LOG.error(ex.getMessage());
			return Result.error("500", ex.getMessage());
		}
	}

	@ApiOperation(value = "存储移交诉讼审批信息")
	@PostMapping("/saveApprovalLogInfo")
	@ResponseBody
	public Result<String> saveApprovalLogInfo(@RequestBody ProcessLogReq req) {
		try {
			// 存储审批结果信息
			processService.saveProcessApprovalResult(req, ProcessTypeEnums.HOUSE_LOAN_LITIGATION);
			return Result.success();
		} catch (Exception ex) {
			LOG.error(ex.getMessage());
			return Result.error("500", ex.getMessage());
		}

	}

	@ApiOperation(value = "根据流程ID查找房贷移交法务申请信息")
	@GetMapping("/getTransferLitigationHouseByProcessId")
	@ResponseBody
	public Result<TransferLitigationHouse> getTransferLitigationHouseByProcessId(
			@RequestParam("processId") String processId) {
		List<TransferLitigationHouse> pList = transferLitigationHouseService
				.selectList(new EntityWrapper<TransferLitigationHouse>().eq("process_id", processId));

		if (!CollectionUtils.isEmpty(pList)) {
			return Result.success(pList.get(0));
		} else {
			return Result.error("500", "无数据");
		}

	}

	@ApiOperation(value = "根据流程ID查找车贷移交法务申请信息")
	@GetMapping("/getTransferLitigationCarByProcessId")
	@ResponseBody
	public Result<TransferLitigationCar> getTransferLitigationCarByProcessId(
			@RequestParam("processId") String processId) {
		List<TransferLitigationCar> pList = transferLitigationCarService
				.selectList(new EntityWrapper<TransferLitigationCar>().eq("process_id", processId));

		if (!CollectionUtils.isEmpty(pList)) {
			return Result.success(pList.get(0));
		} else {
			return Result.error("500", "无数据");
		}

	}

	/**
	 * 获取移交诉讼相关数据
	 * 
	 * @author huweiqian
	 * @date 2018/02/27
	 * @return 车贷诉讼相关数据
	 */
	@ApiOperation(value = "移交诉讼API")
	@GetMapping("/queryTransferLitigationData")
	@ResponseBody
	public Result<TransferOfLitigationVO> queryTransferLitigationData(@RequestParam String businessId,
			@RequestParam String crpId) {
		try {
			TransferOfLitigationVO litigationData = transferOfLitigationService.sendTransferLitigationData(businessId,
					crpId);
			if (litigationData != null) {
				return Result.success(litigationData);
			} else {
				return Result.error("0", "没有数据");
			}
		} catch (Exception e) {
			LOG.error("-- queryTransferLitigationData -- 移交诉讼失败！！！", e);
			return Result.error("500", "系统异常");
		}
	}
	
	/**
	 * 查询车贷结清试算明细
	 * 
	 * @author huweiqian
	 * @date 2018/03/13
	 * @return 
	 */
	@ApiOperation(value = "查询车贷结清试算明细")
	@GetMapping("/queryCarLoanBilDetail")
	@ResponseBody
	public Result<Map<String, Object>> queryCarLoanBilDetail(@RequestParam String businessId, @RequestParam Date billDate) {
		try {
			Map<String, Object> resultMap = transferOfLitigationService.queryCarLoanBilDetail(businessId, billDate);
			if (resultMap != null && !resultMap.isEmpty()) {
				return Result.success(resultMap);
			} else {
				return Result.error("0", "没有数据");
			}
		} catch (Exception e) {
			LOG.error("-- queryCarLoanBilDetail -- 查询车贷结清试算明细！！！", e);
			return Result.error("500", "系统异常");
		}
	}
	
	/**
	 * 车贷结清试算
	 * 
	 * @author huweiqian
	 * @date 2018/03/13
	 * @return 
	 */
	@ApiOperation(value = "车贷结清试算")
	@PostMapping("/carLoanBilling")
	@ResponseBody
	public Result<Map<String, Object>> carLoanBilling(@RequestBody CarLoanBilVO carLoanBilVO) {
		try {
			Map<String, Object> resultMap = transferOfLitigationService.carLoanBilling(carLoanBilVO);
			if (resultMap != null && !resultMap.isEmpty()) {
				return Result.success(resultMap);
			} else {
				return Result.error("0", "没有数据");
			}
		} catch (Exception e) {
			LOG.error("-- carLoanBilling -- 车贷结清试算失败！！！", e);
			return Result.error("500", "系统异常");
		}
	}

	public static void main(String[] args) {
		System.out.println(UUID.randomUUID());
		System.out.println(UUID.randomUUID());
		System.out.println(UUID.randomUUID());
		System.out.println(UUID.randomUUID());
		System.out.println(UUID.randomUUID());
	}
}
