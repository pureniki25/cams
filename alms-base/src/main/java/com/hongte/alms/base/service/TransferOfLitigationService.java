package com.hongte.alms.base.service;

import java.util.List;
import java.util.Map;

import com.hongte.alms.base.assets.car.vo.FileVo;
import com.hongte.alms.base.entity.TransferLitigationCar;
import com.hongte.alms.base.entity.TransferLitigationHouse;
import com.hongte.alms.base.vo.billing.CarLoanBilVO;
import com.hongte.alms.base.vo.litigation.LitigationResponse;
import com.hongte.alms.base.vo.litigation.TransferLitigationDTO;
import com.hongte.alms.base.vo.litigation.house.HouseLoanVO;

public interface TransferOfLitigationService {
	/**
	 * 查询车贷诉讼相关数据
	 * @return 车贷诉讼相关数据
	 * @param businessId 业务编号
	 */
	Map<String, Object> queryCarLoanData(String businessId);
	
	/**
	 * 查询逾期天数
	 * @return 
	 * @param businessId 业务编号
	 */
	Map<String, Object> getOverDueDatys(String businessId);
	
	/**
	 * 查询房贷诉讼相关数据
	 * @return 房贷诉讼相关数据
	 * @param businessId 业务编号
	 */
	HouseLoanVO queryHouseLoanData(String businessId);
	
	/**
	 * 发送移交诉讼相关数据
	 * @param businessId 业务编号
	 * @param planListId 还款计划列表ID
	 * @param channel 移交渠道（1、自动移交，2、手动移交）
	 * @return
	 */
	LitigationResponse sendTransferLitigationData(String businessId, String sendUrl, String planListId, Integer channel);
	
	/**
     * 保存房贷移交法务信息
     * @param req
     */
    void saveTransferLitigationHouse(TransferLitigationHouse req, String sendUrl, List<FileVo> files);
    
    /**
     * 保存车贷移交法务信息
     * @param req
     */
    void saveTransferLitigationCar(TransferLitigationCar req, String sendUrl, List<FileVo> files);
    
    /**
     * 车贷业务结清试算
     */
    Map<String, Object> carLoanBilling(CarLoanBilVO carLoanBilVO);
    
    /**
	 * 移交诉讼信息查询接口相关信息
	 * @param businessId
	 * @return
	 */
	List<TransferLitigationDTO> queryTransferLitigationInfo(Map<String, Object> paramMap);
	
	/**
	 * 统计移交诉讼信息查询接口相关信息条数
	 * @param businessId
	 * @return
	 */
	Integer countTransferLitigationInfo(Map<String, Object> paramMap);
    
}
