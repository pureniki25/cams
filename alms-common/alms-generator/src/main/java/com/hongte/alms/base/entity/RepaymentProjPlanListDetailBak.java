package com.hongte.alms.base.entity;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * 标的还款计划应还项目明细表
 * </p>
 *
 * @author 王继光
 * @since 2018-06-06
 */
@ApiModel
@TableName("tb_repayment_proj_plan_list_detail_bak")
public class RepaymentProjPlanListDetailBak extends Model<RepaymentProjPlanListDetailBak> {

    private static final long serialVersionUID = 1L;

    /**
     * 标的应还项目明细ID(主键)
     */
    @TableId("proj_plan_detail_id")
	@ApiModelProperty(required= true,value = "标的应还项目明细ID(主键)")
	private String projPlanDetailId;
    /**
     * 所属标的还款计划列表ID(外键，对应tb_repayment_proj_plan_list.proj_plan_list_id)
     */
	@TableField("proj_plan_list_id")
	@ApiModelProperty(required= true,value = "所属标的还款计划列表ID(外键，对应tb_repayment_proj_plan_list.proj_plan_list_id)")
	private String projPlanListId;
    /**
     * 分润顺序（根据分润配置计算）
     */
	@TableField("share_profit_index")
	@ApiModelProperty(required= true,value = "分润顺序（根据分润配置计算）")
	private Integer shareProfitIndex;
    /**
     * 所属还款计划列表详情ID(外键，对应tb_repayment_biz_plan_list_detail.plan_detail_id)
     */
	@TableField("plan_detail_id")
	@ApiModelProperty(required= true,value = "所属还款计划列表详情ID(外键，对应tb_repayment_biz_plan_list_detail.plan_detail_id)")
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
     * 还款计划所属原业务编号
     */
	@TableField("orig_business_id")
	@ApiModelProperty(required= true,value = "还款计划所属原业务编号")
	private String origBusinessId;
    /**
     * 所属期数
     */
	@ApiModelProperty(required= true,value = "所属期数")
	private Integer period;
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
     * 应还项目所属分类，10：本金，20：利息，25：利差，30：资产端分公司服务费，40：担保公司费用，50：资金端平台服务费，60：滞纳金，70：违约金，80：中介费，90：押金类费用，100：冲应收,110:返点（返点都是不线上分账的）
     */
	@TableField("plan_item_type")
	@ApiModelProperty(required= true,value = "应还项目所属分类，10：本金，20：利息，25：利差，30：资产端分公司服务费，40：担保公司费用，50：资金端平台服务费，60：滞纳金，70：违约金，80：中介费，90：押金类费用，100：冲应收,110:返点（返点都是不线上分账的）")
	private Integer planItemType;
    /**
     * 分账标记(冲应收还款，根据冲应收明细进行分账)，0：不线上分账，10：分账到借款人账户，20：分账到资产端账户，30：分账到资金端账户(平台)，40：分账到担保公司账户
     */
	@TableField("account_status")
	@ApiModelProperty(required= true,value = "分账标记(冲应收还款，根据冲应收明细进行分账)，0：不线上分账，10：分账到借款人账户，20：分账到资产端账户，30：分账到资金端账户(平台)，40：分账到担保公司账户")
	private Integer accountStatus;
    /**
     * 实还日期
     */
	@TableField("fact_repay_date")
	@ApiModelProperty(required= true,value = "实还日期")
	private Date factRepayDate;
    /**
     * 应还日期
     */
	@TableField("due_date")
	@ApiModelProperty(required= true,value = "应还日期")
	private Date dueDate;
    /**
     * 标的资产端计划还款金额(元)
     */
	@TableField("proj_plan_amount")
	@ApiModelProperty(required= true,value = "标的资产端计划还款金额(元)")
	private BigDecimal projPlanAmount;
    /**
     * 标的减免金额
     */
	@TableField("derate_amount")
	@ApiModelProperty(required= true,value = "标的减免金额")
	private BigDecimal derateAmount;
    /**
     * 标的资产端实还金额(元)
     */
	@TableField("proj_fact_amount")
	@ApiModelProperty(required= true,value = "标的资产端实还金额(元)")
	private BigDecimal projFactAmount;
    /**
     * 生成系统类型：1.信贷生成，2.贷后管理生成
     */
	@TableField("creat_sys_type")
	@ApiModelProperty(required= true,value = "生成系统类型：1.信贷生成，2.贷后管理生成")
	private Integer creatSysType;
    /**
     * 平台标志：1.团贷网，2.你我金融
     */
	@TableField("plate_type")
	@ApiModelProperty(required= true,value = "平台标志：1.团贷网，2.你我金融")
	private Integer plateType;
    /**
     * 创建日期
     */
	@TableField("create_date")
	@ApiModelProperty(required= true,value = "创建日期")
	private Date createDate;
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
	private Date updateDate;
    /**
     * 更新用户
     */
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "更新用户")
	private String updateUser;
	@TableField("confirm_log_id")
	@ApiModelProperty(required= true,value = "")
	private String confirmLogId;


	public String getProjPlanDetailId() {
		return projPlanDetailId;
	}

	public void setProjPlanDetailId(String projPlanDetailId) {
		this.projPlanDetailId = projPlanDetailId;
	}

	public String getProjPlanListId() {
		return projPlanListId;
	}

	public void setProjPlanListId(String projPlanListId) {
		this.projPlanListId = projPlanListId;
	}

	public Integer getShareProfitIndex() {
		return shareProfitIndex;
	}

	public void setShareProfitIndex(Integer shareProfitIndex) {
		this.shareProfitIndex = shareProfitIndex;
	}

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

	public String getOrigBusinessId() {
		return origBusinessId;
	}

	public void setOrigBusinessId(String origBusinessId) {
		this.origBusinessId = origBusinessId;
	}

	public Integer getPeriod() {
		return period;
	}

	public void setPeriod(Integer period) {
		this.period = period;
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

	public Date getFactRepayDate() {
		return factRepayDate;
	}

	public void setFactRepayDate(Date factRepayDate) {
		this.factRepayDate = factRepayDate;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public BigDecimal getProjPlanAmount() {
		return projPlanAmount;
	}

	public void setProjPlanAmount(BigDecimal projPlanAmount) {
		this.projPlanAmount = projPlanAmount;
	}

	public BigDecimal getDerateAmount() {
		return derateAmount;
	}

	public void setDerateAmount(BigDecimal derateAmount) {
		this.derateAmount = derateAmount;
	}

	public BigDecimal getProjFactAmount() {
		return projFactAmount;
	}

	public void setProjFactAmount(BigDecimal projFactAmount) {
		this.projFactAmount = projFactAmount;
	}

	public Integer getCreatSysType() {
		return creatSysType;
	}

	public void setCreatSysType(Integer creatSysType) {
		this.creatSysType = creatSysType;
	}

	public Integer getPlateType() {
		return plateType;
	}

	public void setPlateType(Integer plateType) {
		this.plateType = plateType;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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

	public String getConfirmLogId() {
		return confirmLogId;
	}

	public void setConfirmLogId(String confirmLogId) {
		this.confirmLogId = confirmLogId;
	}

	@Override
	protected Serializable pkVal() {
		return this.projPlanDetailId;
	}

	@Override
	public String toString() {
		return "RepaymentProjPlanListDetailBak{" +
			", projPlanDetailId=" + projPlanDetailId +
			", projPlanListId=" + projPlanListId +
			", shareProfitIndex=" + shareProfitIndex +
			", planDetailId=" + planDetailId +
			", planListId=" + planListId +
			", businessId=" + businessId +
			", origBusinessId=" + origBusinessId +
			", period=" + period +
			", feeId=" + feeId +
			", planItemName=" + planItemName +
			", planItemType=" + planItemType +
			", accountStatus=" + accountStatus +
			", factRepayDate=" + factRepayDate +
			", dueDate=" + dueDate +
			", projPlanAmount=" + projPlanAmount +
			", derateAmount=" + derateAmount +
			", projFactAmount=" + projFactAmount +
			", creatSysType=" + creatSysType +
			", plateType=" + plateType +
			", createDate=" + createDate +
			", createUser=" + createUser +
			", updateDate=" + updateDate +
			", updateUser=" + updateUser +
			", confirmLogId=" + confirmLogId +
			"}";
	}
}
