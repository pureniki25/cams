package com.hongte.alms.base.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.hongte.alms.base.vo.litigation.BusinessCar;
import com.hongte.alms.base.vo.litigation.TransferOfLitigationVO;
import com.hongte.alms.base.vo.litigation.house.HouseLoanVO;
import com.hongte.alms.base.vo.litigation.house.HousePlanInfo;
import com.hongte.alms.base.vo.litigation.house.MortgageInfo;

@Repository("transferOfLitigationMapper")
public interface TransferOfLitigationMapper {
	/**
	 * 查询车贷诉讼相关数据
	 * @return 车贷诉讼相关数据
	 * @param businessId 业务编号
	 */
	Map<String, Object> queryCarLoanData(@Param(value="businessId") String businessId);
	
	/**
	 * 查询房贷诉讼相关数据
	 * @return 车贷诉讼相关数据
	 * @param businessId 业务编号
	 */
	HouseLoanVO queryHouseLoanData(@Param(value="businessId") String businessId);
	
	/**
	 * 查询移交诉讼相关数据
	 * @param businessId 业务编号
	 * @return
	 */
	TransferOfLitigationVO queryTransferLitigationData(@Param(value="businessId") String businessId, @Param(value="crpId")  String crpId);
	
	/**
	 * 判断业务类型
	 * @param businessId
	 * @return
	 */
	Integer queryBusinessType(@Param(value="businessId") String businessId);
	
	/**
	 * 查询车辆信息
	 * @param businessId
	 * @return
	 */
	List<BusinessCar> queryTransferLitigationCarData(@Param(value="businessId") String businessId);
	
	/**
	 * 查询房贷诉讼还款计划信息
	 * @param businessId
	 * @return
	 */
	List<HousePlanInfo> queryRepaymentPlanHouse(@Param(value="businessId") String businessId);
	
	/**
	 * 查询房贷诉讼还款计划信息
	 * @param businessId
	 * @return
	 */
	List<MortgageInfo> queryMortgageInfoByBusinessId(@Param(value="businessId") String businessId);
	
	/**
	 * 查询车贷相关利息及服务费
	 * @param businessId
	 * @return
	 */
	Map<String, Object> queryCarLoanFees(@Param(value="businessId") String businessId, @Param(value="billDate") Date billDate);
}
