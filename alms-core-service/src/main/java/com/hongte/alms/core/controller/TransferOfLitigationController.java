package com.hongte.alms.core.controller;

import java.util.ArrayList;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.CrossOrigin;
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
import com.hongte.alms.base.assets.car.vo.FileVo;
import com.hongte.alms.base.collection.service.CollectionStatusService;
import com.hongte.alms.base.entity.Doc;
import com.hongte.alms.base.entity.DocType;
import com.hongte.alms.base.entity.TransferLitigationCar;
import com.hongte.alms.base.entity.TransferLitigationHouse;
import com.hongte.alms.base.process.enums.ProcessTypeEnums;
import com.hongte.alms.base.process.service.ProcessService;
import com.hongte.alms.base.process.service.ProcessTypeService;
import com.hongte.alms.base.process.service.ProcessTypeStepService;
import com.hongte.alms.base.process.vo.ProcessLogReq;
import com.hongte.alms.base.service.DocService;
import com.hongte.alms.base.service.DocTypeService;
import com.hongte.alms.base.service.TransferLitigationCarService;
import com.hongte.alms.base.service.TransferLitigationHouseService;
import com.hongte.alms.base.service.TransferOfLitigationService;
import com.hongte.alms.base.vo.billing.CarLoanBilVO;
import com.hongte.alms.base.vo.litigation.HouseAdressVO;
import com.hongte.alms.base.vo.litigation.TransferOfLitigationVO;
import com.hongte.alms.base.vo.litigation.house.HouseLoanVO;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.JsonUtil;
import com.hongte.alms.common.util.StringUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

//@CrossOrigin
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
	
	@Autowired
	@Qualifier("DocTypeService")
	private DocTypeService docTypeService;

	@Autowired
	@Qualifier("DocService")
	private DocService docService;

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
				
				Object createTime = carLoanData.get("createTime");
				if (createTime != null) {
					carLoanData.put("createTime", DateUtil.formatDate((Date) createTime));
				}
				Object dragDate = carLoanData.get("dragDate");
				if (dragDate != null) {
					carLoanData.put("dragDate", DateUtil.formatDate((Date) dragDate));
				}
				Object factOutputDate = carLoanData.get("factOutputDate");
				if (factOutputDate != null) {
					carLoanData.put("factOutputDate", DateUtil.formatDate((Date) factOutputDate));
				}
				Object factRepayDate = carLoanData.get("factRepayDate");
				if (factRepayDate != null) {
					carLoanData.put("factRepayDate", DateUtil.formatDate((Date) factRepayDate));
				}

				if (processId != null) {
					List<TransferLitigationCar> applyList = transferLitigationCarService
							.selectList(new EntityWrapper<TransferLitigationCar>().eq("process_id", processId).eq("business_id", businessId));

					String houseAddr = applyList.get(0).getHouseAddress();
					if (StringUtil.notEmpty(houseAddr)) {
						String[] houseArrs = houseAddr.split("--\\[#separator#\\]--");
						
						if (houseArrs != null && houseArrs.length > 0) {
							
							List<HouseAdressVO> houseAdressVOs = new ArrayList<>();
							for (String houseArr : houseArrs) {
								String[] subHouseArrs = houseArr.split("--#separator#--");
								
								if (subHouseArrs != null && subHouseArrs.length == 3) {
									HouseAdressVO vo = new HouseAdressVO();
									String replace = subHouseArrs[0].replace("[", "").replace("]", "").replaceAll(" ","");
									vo.setHouseArea(Arrays.asList(replace.split(",")));
									vo.setDetailAddress(subHouseArrs[1]);
									vo.setMortgageSituation(subHouseArrs[2]);
									houseAdressVOs.add(vo);
								}
							}
							carLoanData.put("houseAddress", houseAdressVOs);
						}
					}

					carLoanData.put("carList", (JSONArray) JSON.toJSON(applyList, JsonUtil.getMapping()));
				}

				processService.getProcessShowInfo(carLoanData, processId, ProcessTypeEnums.CAR_LOAN_LITIGATION);

				return Result.success(carLoanData);
			} else {
				return Result.error("0", "没有找到数据");
			}
		} catch (Exception e) {
			LOG.error("获取车贷诉讼相关数据异常!!!", e);
			return Result.error("500", e.getMessage());
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
				return Result.error("0", "没有找到数据");
			}

			if (processId != null) {
				List<TransferLitigationHouse> applyList = transferLitigationHouseService
						.selectList(new EntityWrapper<TransferLitigationHouse>().eq("process_id", processId));
				resultMap.put("houseList", (JSONArray) JSON.toJSON(applyList, JsonUtil.getMapping()));
			}
			resultMap.put("baseInfo", JSON.toJSON(houseLoanData, JsonUtil.getMapping()));
			processService.getProcessShowInfo(resultMap, processId, ProcessTypeEnums.HOUSE_LOAN_LITIGATION);
			
			// 查询附件
			List<DocType> docTypes = docTypeService
					.selectList(new EntityWrapper<DocType>().eq("type_code", "AfterLoan_Material_Litigation"));
			if (docTypes != null && docTypes.size() == 1) {
				List<Doc> fileList = docService.selectList(new EntityWrapper<Doc>()
						.eq("doc_type_id", docTypes.get(0).getDocTypeId()).eq("business_id", businessId).orderBy("doc_id"));
				resultMap.put("returnRegFiles", fileList);
			}
			
			return Result.success(resultMap);
		} catch (Exception e) {
			LOG.error("-- queryTransferLitigationData -- 获取房贷诉讼相关数据异常！！！", e);
			return Result.error("500", e.getMessage());
		}
	}

	@ApiOperation(value = "存储房贷移交诉讼信息")
	@PostMapping("/saveTransferLitigationHouse")
	@ResponseBody
	public Result<String> saveTransferLitigationHouse(@RequestBody  Map<String, Object> req) {
		try {
			List<FileVo> files = JsonUtil.map2objList(req.get("reqRegFiles"), FileVo.class);
			List<TransferLitigationHouse> house = JsonUtil.map2objList(req.get("houseData"), TransferLitigationHouse.class);
			if (CollectionUtils.isEmpty(house)) {
				Result.error("500", "参数不能为空");
			}
			
			transferOfLitigationService.saveTransferLitigationHouse(house.get(0), sendUrl, files);
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
			List<FileVo> files = JsonUtil.map2objList(req.get("reqRegFiles"), FileVo.class);
			List<TransferLitigationCar> cars = JsonUtil.map2objList(req.get("houseData"), TransferLitigationCar.class);
			if (CollectionUtils.isEmpty(cars)) {
				Result.error("500", "参数不能为空");
			}
			TransferLitigationCar transferLitigationCar = cars.get(0);
			StringBuilder houseAddress = new StringBuilder();
			List<LinkedHashMap<String, Object>> componentOptions = (List<LinkedHashMap<String, Object>>) req
					.get("componentOption");
			if (!CollectionUtils.isEmpty(componentOptions)) {
				for (LinkedHashMap<String, Object> componentOption : componentOptions) {
					List<String> houseAreas = (List<String>) componentOption.get("houseArea");
					String detailAddress = StringUtil.isEmpty((String) componentOption.get("detailAddress")) ? " " : (String) componentOption.get("detailAddress");
					String mortgageSituation = StringUtil.isEmpty((String) componentOption.get("mortgageSituation")) ? " " : (String) componentOption.get("mortgageSituation");
					
					if (componentOptions.indexOf(componentOption) < (componentOptions.size() - 1)) {
						houseAddress.append(houseAreas).append("--#separator#--").append(detailAddress).append("--#separator#--")
						.append(mortgageSituation).append("--[#separator#]--");
					}else {
						houseAddress.append(houseAreas).append("--#separator#--").append(detailAddress).append("--#separator#--")
						.append(mortgageSituation);
					}
				}
			}

			transferLitigationCar.setHouseAddress(houseAddress.toString());

			transferOfLitigationService.saveTransferLitigationCar(transferLitigationCar, sendUrl, files);
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
	public Result<TransferOfLitigationVO> queryTransferLitigationData(@RequestParam String businessId) {
		try {
			TransferOfLitigationVO litigationData = transferOfLitigationService.sendTransferLitigationData(businessId, sendUrl);
			if (litigationData != null) {
				return Result.success(litigationData);
			} else {
				return Result.error("0", "没有找到数据");
			}
		} catch (Exception e) {
			LOG.error("-- queryTransferLitigationData -- 移交诉讼失败！！！", e);
			return Result.error("500", e.getMessage());
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
				return Result.error("0", "没有找到数据");
			}
		} catch (Exception e) {
			LOG.error("-- queryCarLoanBilDetail -- 查询车贷结清试算明细！！！", e);
			return Result.error("500", e.getMessage());
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
				return Result.error("0", "没有找到数据");
			}
		} catch (Exception e) {
			LOG.error("-- carLoanBilling -- 车贷结清试算失败！！！", e);
			return Result.error("500", e.getMessage());
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
