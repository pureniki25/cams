package com.hongte.alms.base.mapper;

import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import com.hongte.alms.base.entity.RepaymentBizPlanListDetail;
import com.hongte.alms.base.vo.billing.PreviousFeesVO;
import com.hongte.alms.base.vo.litigation.BusinessCar;
import com.hongte.alms.base.vo.litigation.LitigationBorrowerDetailed;
import com.hongte.alms.base.vo.litigation.TransferLitigationDTO;
import com.hongte.alms.base.vo.litigation.TransferOfLitigationVO;
import com.hongte.alms.base.vo.litigation.house.HouseLoanVO;
import com.hongte.alms.base.vo.litigation.house.HousePlanInfo;
import com.hongte.alms.base.vo.litigation.house.MortgageInfo;

@Repository("transferOfLitigationMapper")
public interface TransferOfLitigationMapper {
	/**
	 * 查询车贷诉讼相关数据
	 * 
	 * @return 车贷诉讼相关数据
	 * @param businessId
	 *            业务编号
	 */
	Map<String, Object> queryCarLoanData(@Param(value = "businessId") String businessId);

	/**
	 * 查询逾期天数
	 * 
	 * @return
	 * @param businessId
	 *            业务编号
	 */
	Map<String, Object> getOverDueDatys(@Param(value = "businessId") String businessId);

	/**
	 * 查询房贷诉讼相关数据
	 * 
	 * @return 车贷诉讼相关数据
	 * @param businessId
	 *            业务编号
	 */
	HouseLoanVO queryHouseLoanData(@Param(value = "businessId") String businessId);

	/**
	 * 查询移交诉讼相关数据
	 * 
	 * @param businessId
	 *            业务编号
	 * @return
	 */
	TransferOfLitigationVO queryTransferLitigationData(@Param(value = "businessId") String businessId);

	/**
	 * 判断业务类型
	 * 
	 * @param businessId
	 * @return
	 */
	Integer queryBusinessType(@Param(value = "businessId") String businessId);

	/**
	 * 查询车辆信息
	 * 
	 * @param businessId
	 * @return
	 */
	List<BusinessCar> queryTransferLitigationCarData(@Param(value = "businessId") String businessId);

	/**
	 * 查询房贷诉讼还款计划信息
	 * 
	 * @param businessId
	 * @return
	 */
	List<HousePlanInfo> queryRepaymentPlanHouse(@Param(value = "businessId") String businessId);

	/**
	 * 查询房贷诉讼还款计划信息
	 * 
	 * @param businessId
	 * @return
	 */
	List<MortgageInfo> queryMortgageInfoByBusinessId(@Param(value = "businessId") String businessId);

	/**
	 * 查询车贷相关利息及服务费
	 * 
	 * @param businessId
	 * @param billDate
	 * @return
	 */
	Map<String, Object> queryCarLoanFees(@Param(value = "businessId") String businessId,
			@Param(value = "billDate") Date billDate);

	/**
	 * 结清试算功能查询往期少交费用明细
	 * 
	 * @param businessId
	 * @param billDate
	 * @return
	 */
	List<PreviousFeesVO> queryPreviousFees(@Param(value = "businessId") String businessId,
			@Param(value = "billDate") Date billDate);

	/**
	 * 查询最后一期的应还日期
	 * 
	 * @param businessId
	 * @return
	 */
	Map<String, Object> queryMaxDueDateByBusinessId(@Param(value = "businessId") String businessId);

	/**
	 * 根据业务编号查询 借款人明细
	 * 
	 * @param businessId
	 * @return
	 */
	List<LitigationBorrowerDetailed> queryLitigationBorrowerDetailed(@Param(value = "businessId") String businessId);

	/**
	 * 查询状态为'逾期', '还款中'的最早应还日期
	 * 
	 * @param businessId
	 * @return
	 */
	Date queryMinNoRepaymentDueDateByBusinessId(@Param(value = "businessId") String businessId);

	/**
	 * 查询往期少交费用总和
	 * 
	 * @param billDate
	 * @return
	 */
	Double queryBalanceDueByBillDate(@Param(value = "businessId") String businessId,
			@Param(value = "billDate") Date billDate);

	/**
	 * 根据业务编号统计逾期次数
	 * 
	 * @param businessId
	 * @return
	 */
	Integer countOverdueByBusinessId(@Param(value = "businessId") String businessId);

	/**
	 * 根据业务编号查询还款方式
	 * 
	 * @param businessId
	 * @return
	 */
	String queryRepaymentTypeByBusinessId(@Param(value = "businessId") String businessId);

	/**
	 * 根据业务编号查找等额本息还款状态为逾期的最早一期时的剩余本金
	 * 
	 * @param businessId
	 * @return
	 */
	Double queryMatchingRepaymentPlanAccrualSurplusPrincipal(@Param(value = "businessId") String businessId);

	/**
	 * 根据业务编号查找结余金额
	 * 
	 * @param businessId
	 * @return
	 */
	Double queryOverRepayMoneyByBusinessId(@Param(value = "businessId") String businessId);

	/**
	 * 根据业务编号查找押金转还款金额
	 * 
	 * @param businessId
	 * @return
	 */
	Double queryRefundMoneyByBusinessId(@Param(value = "businessId") String businessId);

	/**
	 * 根据业务编号查找剩余期数（包含当前期）的服务费
	 * 
	 * @param businessId
	 * @return
	 */
	List<RepaymentBizPlanListDetail> querySurplusServiceChargeByBusinessId(
			@Param(value = "businessId") String businessId, @Param(value = "billDate") Date billDate);

	/**
	 * 移交诉讼信息查询接口相关信息
	 * 
	 * @param businessId
	 * @return
	 */
	List<TransferLitigationDTO> queryTransferLitigationInfo(@Param(value = "businessId") String businessId,
			@Param(value = "page") Integer page, @Param(value = "limit") Integer limit);

	/**
	 * 统计移交诉讼信息查询接口相关信息条数
	 * 
	 * @param businessId
	 * @return
	 */
	Integer countTransferLitigationInfo(@Param(value = "businessId") String businessId,
			@Param(value = "page") Integer page, @Param(value = "limit") Integer limit);
}
