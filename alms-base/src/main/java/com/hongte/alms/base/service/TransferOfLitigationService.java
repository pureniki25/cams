package com.hongte.alms.base.service;

import java.util.Map;

import com.hongte.alms.base.entity.TransferLitigationCar;
import com.hongte.alms.base.entity.TransferLitigationHouse;
import com.hongte.alms.base.process.vo.ProcessLogReq;
import com.hongte.alms.base.vo.billing.CarLoanBilVO;
import com.hongte.alms.base.vo.litigation.TransferOfLitigationVO;
import com.hongte.alms.base.vo.litigation.house.HouseLoanVO;

public interface TransferOfLitigationService {
	/**
	 * 查询车贷诉讼相关数据
	 * @return 车贷诉讼相关数据
	 * @param businessId 业务编号
	 */
	Map<String, Object> queryCarLoanData(String businessId);
	
	/**
	 * 查询车贷诉讼相关数据
	 * @return 车贷诉讼相关数据
	 * @param businessId 业务编号
	 */
	HouseLoanVO queryHouseLoanData(String businessId);
	
	/**
	 * 发送移交诉讼相关数据
	 * @param businessId 业务编号
	 * @param crpId 还款计划ID
	 * @return
	 */
	TransferOfLitigationVO sendTransferLitigationData(String businessId, String crpId, String sendUrl);
	
	/**
     * 保存房贷移交法务信息
     * @param req
     */
    void saveTransferLitigationHouse(TransferLitigationHouse req, String sendUrl);
    
    /**
     * 保存车贷移交法务信息
     * @param req
     */
    void saveTransferLitigationCar(TransferLitigationCar req, String sendUrl);
    
    /**
     * 车贷业务结清试算
     */
    Map<String, Object> carLoanBilling(CarLoanBilVO carLoanBilVO);
    
    void saveHouseProcessApprovalResult(ProcessLogReq req, String sendUrl);
    void saveCarProcessApprovalResult(ProcessLogReq req, String sendUrl);
    
}
