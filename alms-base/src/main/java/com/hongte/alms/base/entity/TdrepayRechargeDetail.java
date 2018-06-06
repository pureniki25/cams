package com.hongte.alms.base.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
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
 * 团贷网合规化还款标的充值明细表
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-06-06
 */
@ApiModel
@TableName("tb_tdrepay_recharge_detail")
public class TdrepayRechargeDetail extends Model<TdrepayRechargeDetail> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键ID
     */
	@TableId(value="id", type= IdType.AUTO)
	@ApiModelProperty(required= true,value = "主键ID")
	private Integer id;
    /**
     * tb_tdrepay_recharge_log的主键ID
     */
	@TableField("log_id")
	@ApiModelProperty(required= true,value = "tb_tdrepay_recharge_log的主键ID")
	private String logId;
    /**
     * (资金分发接口参数)费用项目明细名称
     */
	@TableField("fee_name")
	@ApiModelProperty(required= true,value = "(资金分发接口参数)费用项目明细名称")
	private String feeName;
    /**
     * (资金分发接口参数)费用项目明细金额(元)
     */
	@TableField("fee_value")
	@ApiModelProperty(required= true,value = "(资金分发接口参数)费用项目明细金额(元)")
	private BigDecimal feeValue;
    /**
     * 费用类型(10:本金+利息;30:平台服务费;40:资产端服务费;50:担保公司服务费;60:仲裁服务费;70:逾期费用（罚息）)
     */
	@TableField("fee_type")
	@ApiModelProperty(required= true,value = "费用类型(10:本金+利息;30:平台服务费;40:资产端服务费;50:担保公司服务费;60:仲裁服务费;70:逾期费用（罚息）)")
	private Integer feeType;
    /**
     * 创建人
     */
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "创建人")
	private String createUser;
    /**
     * 创建时间
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建时间")
	private Date createTime;
    /**
     * 更新人
     */
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "更新人")
	private String updateUser;
    /**
     * 更新时间
     */
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "更新时间")
	private Date updateTime;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getLogId() {
		return logId;
	}

	public void setLogId(String logId) {
		this.logId = logId;
	}

	public String getFeeName() {
		return feeName;
	}

	public void setFeeName(String feeName) {
		this.feeName = feeName;
	}

	public BigDecimal getFeeValue() {
		return feeValue;
	}

	public void setFeeValue(BigDecimal feeValue) {
		this.feeValue = feeValue;
	}

	public Integer getFeeType() {
		return feeType;
	}

	public void setFeeType(Integer feeType) {
		this.feeType = feeType;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "TdrepayRechargeDetail{" +
			", id=" + id +
			", logId=" + logId +
			", feeName=" + feeName +
			", feeValue=" + feeValue +
			", feeType=" + feeType +
			", createUser=" + createUser +
			", createTime=" + createTime +
			", updateUser=" + updateUser +
			", updateTime=" + updateTime +
			"}";
	}
}
