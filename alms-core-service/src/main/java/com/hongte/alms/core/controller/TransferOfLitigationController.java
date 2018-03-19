package com.hongte.alms.core.controller;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.hongte.alms.base.collection.enums.CollectionSetWayEnum;
import com.hongte.alms.base.collection.enums.CollectionStatusEnum;
import com.hongte.alms.base.collection.service.CollectionStatusService;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
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
import com.hongte.alms.base.process.entity.Process;
import com.hongte.alms.base.process.entity.ProcessTypeStep;
import com.hongte.alms.base.process.enums.ProcessTypeEnums;
import com.hongte.alms.base.process.service.ProcessService;
import com.hongte.alms.base.process.service.ProcessTypeService;
import com.hongte.alms.base.process.service.ProcessTypeStepService;
import com.hongte.alms.base.process.vo.ProcessLogReq;
import com.hongte.alms.base.service.TransferLitigationCarService;
import com.hongte.alms.base.service.TransferLitigationHouseService;
import com.hongte.alms.base.service.TransferOfLitigationService;
import com.hongte.alms.base.vo.billing.CarLoanBilVO;
import com.hongte.alms.base.vo.litigation.TransferOfLitigationVO;
import com.hongte.alms.base.vo.litigation.house.HouseLoanVO;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.JsonUtil;
import com.hongte.alms.common.util.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/transferOfLitigation")
@Api(tags = "TransferOfLitigationControllerApi", description = "诉讼移交API", hidden = true)
public class TransferOfLitigationController {

	private static final Logger LOG = LoggerFactory.getLogger(TransferOfLitigationController.class);

	@Autowired
	@Qualifier("TransferOfLitigationService")
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

	@Autowired
    @Qualifier("ProcessTypeService")
    private ProcessTypeService processTypeService;

	@Autowired
	@Qualifier("ProcessTypeStepService")
	private ProcessTypeStepService processTypeStepService;

	@Value("${ht.litigation.url:http://172.16.200.110:30906/api/importLitigation}")
	private String sendUrl;

	@Autowired
	@Qualifier("CollectionStatusService")
	private CollectionStatusService collectionStatusService;

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
					String[] houseArr = houseAddr.split("--#separator#--");

					int count = 1;
					if (houseAddr.length() > 0) {
						for (String hArr : houseArr) {
							if (count < houseArr.length) {
								builder.append("房产地址" + count++ + "：").append(hArr).append("--#separator#--");
							}else {
								builder.append("房产地址" + count++ + "：").append(hArr);
							}
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
			transferOfLitigationService.saveTransferLitigationHouse(req, sendUrl);
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
			car.setAlmsOpinion((String) req.get("almsOpinion"));
			car.setBusinessId((String) req.get("businessId"));
			car.setCarCondition((String) req.get("carCondition"));
			car.setCrpId((String) req.get("crpId"));
			car.setDelayHandover((String) req.get("delayHandover"));
			car.setDelayHandoverDesc((String) req.get("delayHandoverDesc"));
			car.setEstates((String) req.get("estates"));
			car.setProcessStatus((String) req.get("processStatus"));

			StringBuilder houseAddress = new StringBuilder();
			List<LinkedHashMap<String, Object>> componentOptions = (List<LinkedHashMap<String, Object>>) req
					.get("componentOption");
            if(componentOptions!=null) {
                for (LinkedHashMap<String, Object> componentOption : componentOptions) {
                    LinkedHashMap<String, Object> registrationInfoForm = (LinkedHashMap<String, Object>) componentOption
                            .get("registrationInfoForm");
                    List<String> houseAreas = (List<String>) registrationInfoForm.get("houseArea");
                    String detailAddress = (String) registrationInfoForm.get("detailAddress");
                    String mortgageSituation = (String) registrationInfoForm.get("mortgageSituation");

                    if (!CollectionUtils.isEmpty(houseAreas) && !StringUtil.isEmpty(detailAddress)
                            && !StringUtil.isEmpty(mortgageSituation)) {
                        String houseAreasStr = houseAreas.toString().replace("[", "").replace("]", "").replace(",", "");
                        houseAddress.append(houseAreasStr).append(" ").append(detailAddress).append("，房产抵押情况：")
                                .append(mortgageSituation).append("--#separator#--");
                    }
                }
			}

			car.setHouseAddress(houseAddress.toString());

			transferOfLitigationService.saveTransferLitigationCar(car, sendUrl);
			return Result.success();
		} catch (Exception ex) {
			LOG.error(ex.getMessage());
			return Result.error("500", ex.getMessage());
		}
	}

	@ApiOperation(value = "存储房贷移交诉讼审批信息")
	@PostMapping("/saveHouseApprovalLogInfo")
	@ResponseBody
	public Result<String> saveHouseApprovalLogInfo(@RequestBody ProcessLogReq req) {
		try {
			transferOfLitigationService.saveHouseProcessApprovalResult(req, sendUrl);
			return Result.success();
		} catch (Exception ex) {
			LOG.error(ex.getMessage());
			return Result.error("500", ex.getMessage());
		}

	}

	@ApiOperation(value = "存储车贷移交诉讼审批信息")
	@PostMapping("/saveCarApprovalLogInfo")
	@ResponseBody
	public Result<String> saveCarApprovalLogInfo(@RequestBody ProcessLogReq req) {
		try {
			transferOfLitigationService.saveCarProcessApprovalResult(req, sendUrl);
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
					crpId, sendUrl);
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
	public Result<Map<String, Object>> queryCarLoanBilDetail(@RequestParam String businessId) {
		try {
			Map<String, Object> resultMap = transferOfLitigationService.queryCarLoanData(businessId);
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
