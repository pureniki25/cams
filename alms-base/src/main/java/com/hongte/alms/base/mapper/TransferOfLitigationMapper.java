package com.hongte.alms.base.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.hongte.alms.base.vo.billing.PreviousFeesVO;
import com.hongte.alms.base.vo.litigation.BusinessCar;
import com.hongte.alms.base.vo.litigation.LitigationBorrowerDetailed;
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
	TransferOfLitigationVO queryTransferLitigationData(@Param(value="businessId") String businessId);
	
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
	 * @param billDate
	 * @return
	 */
	Map<String, Object> queryCarLoanFees(@Param(value="businessId") String businessId, @Param(value="billDate") Date billDate);
	
	/**
	 * 结清试算功能查询往期少交费用
	 * @param businessId
	 * @param billDate
	 * @return
	 */
	List<PreviousFeesVO> queryPreviousFees(@Param(value="businessId") String businessId, @Param(value="billDate") Date billDate);
	
	/**
	 * 查询最后一期的应还日期
	 * @param businessId
	 * @return
	 */
	Map<String, Object> queryMaxDueDateByBusinessId(@Param(value="businessId") String businessId);
	
	/**
	 * 根据业务编号查询 借款人明细
	 * @param businessId
	 * @return
	 */
	List<LitigationBorrowerDetailed> queryLitigationBorrowerDetailed(@Param(value="businessId") String businessId);
}
