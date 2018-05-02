package com.hongte.alms.base.entity;

import java.io.Serializable;

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
 * 
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-05-02
 */
@ApiModel
@TableName("tb_profit_fee_set")
public class ProfitFeeSet extends Model<ProfitFeeSet> {

    private static final long serialVersionUID = 1L;

    /**
     * 大类费用项级别设置ID
     */
    @TableId("profit_fee_set_id")
	@ApiModelProperty(required= true,value = "大类费用项级别设置ID")
	private String profitFeeSetId;

    /**
     * 所属费用类型分顺顺序设置ID
     */
    @TableId("profit_Item_set_id")
	@ApiModelProperty(required= true,value = "所属费用类型分顺顺序设置ID")
	private String profitItemSetId;
    
    
    
    /**
     * 业务类型ID
     */
	@TableField("business_type_id")
	@ApiModelProperty(required= true,value = "业务类型ID")
	private Integer businessTypeId;
    /**
     * 大类费用项名称
     */
	@TableField("fee_name")
	@ApiModelProperty(required= true,value = "大类费用项名称")
	private String feeName;
    /**
     * 大类费用项类型
     */
	@TableField("fee_id")
	@ApiModelProperty(required= true,value = "大类费用项类型")
	private String feeId;
    /**
     * 级别
     */
	@TableField("fee_level")
	@ApiModelProperty(required= true,value = "级别")
	private Integer feeLevel;
    /**
     * 创建人用户编号
     */
	@TableField("update_user_id")
	@ApiModelProperty(required= true,value = "创建人用户编号")
	private String updateUserId;
    /**
     * 创建时间
     */
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "创建时间")
	private Date updateTime;


	public String getProfitItemSetId() {
		return profitItemSetId;
	}

	public void setProfitItemSetId(String profitItemSetId) {
		this.profitItemSetId = profitItemSetId;
	}

	public String getProfitFeeSetId() {
		return profitFeeSetId;
	}

	public void setProfitFeeSetId(String profitFeeSetId) {
		this.profitFeeSetId = profitFeeSetId;
	}

	public Integer getBusinessTypeId() {
		return businessTypeId;
	}

	public void setBusinessTypeId(Integer businessTypeId) {
		this.businessTypeId = businessTypeId;
	}

	public String getFeeName() {
		return feeName;
	}

	public void setFeeName(String feeName) {
		this.feeName = feeName;
	}

	public String getFeeId() {
		return feeId;
	}

	public void setFeeId(String feeId) {
		this.feeId = feeId;
	}

	public Integer getFeeLevel() {
		return feeLevel;
	}

	public void setFeeLevel(Integer feeLevel) {
		this.feeLevel = feeLevel;
	}

	public String getUpdateUserId() {
		return updateUserId;
	}

	public void setUpdateUserId(String updateUserId) {
		this.updateUserId = updateUserId;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.profitFeeSetId;
	}

	@Override
	public String toString() {
		return "ProfitFeeSet{" +
			", profitFeeSetId=" + profitFeeSetId +
			", businessTypeId=" + businessTypeId +
			", feeName=" + feeName +
			", feeId=" + feeId +
			", feeLevel=" + feeLevel +
			", updateUserId=" + updateUserId +
			", updateTime=" + updateTime +
			"}";
	}
}
