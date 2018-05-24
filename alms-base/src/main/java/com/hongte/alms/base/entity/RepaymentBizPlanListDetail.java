package com.hongte.alms.base.entity;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * 还款计划应还项目明细表
 * </p>
 *
 * @author 曾坤
 * @since 2018-05-03
 */
@ApiModel
@TableName("tb_repayment_biz_plan_list_detail")
public class RepaymentBizPlanListDetail extends Model<RepaymentBizPlanListDetail> {

    private static final long serialVersionUID = 1L;

    /**
     * 应还项目明细ID(主键)
     */
    @TableId("plan_detail_id")
	@ApiModelProperty(required= true,value = "应还项目明细ID(主键)")
	private String planDetailId;
    /**
     * 所属还款计划列表ID(外键，对应tb_repayment_biz_plan_list.plan_list_id)
     */
	@TableField("plan_list_id")
	@ApiModelProperty(required= true,value = "所属还款计划列表ID(外键，对应tb_repayment_biz_plan_list.plan_list_id)")
	private String planListId;
    /**
     * 还款计划所属业务ID(若当前业务为展期，则存展期业务编号)
     */
	@TableField("business_id")
	@ApiModelProperty(required= true,value = "还款计划所属业务ID(若当前业务为展期，则存展期业务编号)")
	private String businessId;

    /**
     * 所属期数
     */
	@ApiModelProperty(required= true,value = "所属期数")
	private Integer period;
    /**
     * 分润顺序（根据分润配置计算）
     */
	@TableField("share_profit_index")
	@ApiModelProperty(required= true,value = "分润顺序（根据分润配置计算）")
	private Integer shareProfitIndex;
    /**
     * 项目计划应还总金额(元)
     */
	@TableField("plan_amount")
	@ApiModelProperty(required= true,value = "项目计划应还总金额(元)")
	private BigDecimal planAmount;
	
	/**
     * 减免金额
     */
	@TableField("derate_amount")
	@ApiModelProperty(required= true,value = "减免金额")
	private BigDecimal derateAmount;
	
    /**
     * 项目计划应还比例(%)，如0.5%则存0.5，可空
     */
	@TableField("plan_rate")
	@ApiModelProperty(required= true,value = "项目计划应还比例(%)，如0.5%则存0.5，可空")
	private BigDecimal planRate;
    /**
     * 资产端费用项ID，用于资产端区分同名的项目，若不存在同名费用项，可为空
     */
	@TableField("fee_id")
	@ApiModelProperty(required= true,value = "资产端费用项ID，用于资产端区分同名的项目，若不存在同名费用项，可为空")
	private String feeId;
    /**
     * 应还项目名称
     */
	@TableField("plan_item_name")
	@ApiModelProperty(required= true,value = "应还项目名称")
	private String planItemName;
    /**
     * 应还项目所属分类，10：本金，20：利息，30：资产端分公司服务费，40：担保公司费用，50：资金端平台服务费，60：滞纳金，70：违约金，80：中介费，90：押金类费用，100：冲应收
     */
	@TableField("plan_item_type")
	@ApiModelProperty(required= true,value = "应还项目所属分类，10：本金，20：利息，30：资产端分公司服务费，40：担保公司费用，50：资金端平台服务费，60：滞纳金，70：违约金，80：中介费，90：押金类费用，100：冲应收")
	private Integer planItemType;
    /**
     * 分账标记(冲应收还款，根据冲应收明细进行分账)，0：不线上分账，10：分账到借款人账户，20：分账到资产端账户，30：分账到资金端账户(平台)，40：分账到担保公司账户
     */
	@TableField("account_status")
	@ApiModelProperty(required= true,value = "分账标记(冲应收还款，根据冲应收明细进行分账)，0：不线上分账，10：分账到借款人账户，20：分账到资产端账户，30：分账到资金端账户(平台)，40：分账到担保公司账户")
	private Integer accountStatus;
    /**
     * 实还金额(元)
     */
	@TableField("fact_amount")
	@ApiModelProperty(required= true,value = "实还金额(元)")
	private BigDecimal factAmount;
    /**
     * 还款来源，10：线下转账，20：线下代扣，30：银行代扣
     */
	@TableField("repay_source")
	@ApiModelProperty(required= true,value = "还款来源，10：线下转账，20：线下代扣，30：银行代扣")
	private Integer repaySource;
    /**
     * 实还日期
     */
	@TableField("fact_repay_date")
	@ApiModelProperty(required= true,value = "实还日期")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	private Date factRepayDate;
	/**
	 * 应还日期
     */
	@TableField("due_date")
	@ApiModelProperty(required= true,value = "应还日期")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	private Date dueDate;
	/**
    /**
     * 是否有效状态：1 有效 ，0 无效
     */
	@ApiModelProperty(required= true,value = "是否有效状态：1 有效 ，0 无效")
	private Integer active;
    /**
     * 创建日期
     */
	@TableField("create_date")
	@ApiModelProperty(required= true,value = "创建日期")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createDate;
    /**
     * 来源类型：1.信贷生成，2.贷后管理生成
     */
	@TableField("src_type")
	@ApiModelProperty(required= true,value = "来源类型：1.信贷生成，2.贷后管理生成")
	private Integer srcType;
    /**
     * 创建用户
     */
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "创建用户")
	private String createUser;
    /**
     * 更新日期
     */
	@TableField("update_date")
	@ApiModelProperty(required= true,value = "更新日期")
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd HH:mm:ss")
	private Date updateDate;
    /**
     * 更新用户
     */
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "更新用户")
	private String updateUser;



	public String getPlanDetailId() {
		return planDetailId;
	}

	public void setPlanDetailId(String planDetailId) {
		this.planDetailId = planDetailId;
	}

	public String getPlanListId() {
		return planListId;
	}

	public void setPlanListId(String planListId) {
		this.planListId = planListId;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
	}

	public Integer getShareProfitIndex() {
		return shareProfitIndex;
	}

	public void setShareProfitIndex(Integer shareProfitIndex) {
		this.shareProfitIndex = shareProfitIndex;
	}

	public BigDecimal getPlanAmount() {
		return planAmount;
	}

	public void setPlanAmount(BigDecimal planAmount) {
		this.planAmount = planAmount;
	}

	public BigDecimal getPlanRate() {
		return planRate;
	}

	public void setPlanRate(BigDecimal planRate) {
		this.planRate = planRate;
	}

	public String getFeeId() {
		return feeId;
	}

	public void setFeeId(String feeId) {
		this.feeId = feeId;
	}

	public String getPlanItemName() {
		return planItemName;
	}

	public void setPlanItemName(String planItemName) {
		this.planItemName = planItemName;
	}

	public Integer getPlanItemType() {
		return planItemType;
	}

	public void setPlanItemType(Integer planItemType) {
		this.planItemType = planItemType;
	}

	public Integer getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(Integer accountStatus) {
		this.accountStatus = accountStatus;
	}

	public BigDecimal getFactAmount() {
		return factAmount;
	}

	public void setFactAmount(BigDecimal factAmount) {
		this.factAmount = factAmount;
	}

	public Integer getRepaySource() {
		return repaySource;
	}

	public void setRepaySource(Integer repaySource) {
		this.repaySource = repaySource;
	}

	public Date getFactRepayDate() {
		return factRepayDate;
	}

	public void setFactRepayDate(Date factRepayDate) {
		this.factRepayDate = factRepayDate;
	}

	public Integer getActive() {
		return active;
	}

	public void setActive(Integer active) {
		this.active = active;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Integer getSrcType() {
		return srcType;
	}

	public void setSrcType(Integer srcType) {
		this.srcType = srcType;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	@Override
	protected Serializable pkVal() {
		return this.planDetailId;
	}

	@Override
	public String toString() {
		return "RepaymentBizPlanListDetail{" +
			", planDetailId=" + planDetailId +
			", planListId=" + planListId +
			", businessId=" + businessId +
			", period=" + period +
			", shareProfitIndex=" + shareProfitIndex +
			", planAmount=" + planAmount +
			", planRate=" + planRate +
			", feeId=" + feeId +
			", planItemName=" + planItemName +
			", planItemType=" + planItemType +
			", accountStatus=" + accountStatus +
			", factAmount=" + factAmount +
			", repaySource=" + repaySource +
			", factRepayDate=" + factRepayDate +
			", dueDate=" + dueDate +
			", active=" + active +
			", createDate=" + createDate +
			", srcType=" + srcType +
			", createUser=" + createUser +
			", updateDate=" + updateDate +
			", updateUser=" + updateUser +
			"}";
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
}
