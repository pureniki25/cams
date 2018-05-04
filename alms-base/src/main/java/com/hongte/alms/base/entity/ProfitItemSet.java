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
@TableName("tb_profit_item_set")
public class ProfitItemSet extends Model<ProfitItemSet> {

    private static final long serialVersionUID = 1L;

    /**
     * 大类费用项级别设置ID
     */
    @TableId("profit_item_set_id")
	@ApiModelProperty(required= true,value = "大类费用项级别设置ID")
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
	@TableField("item_name")
	@ApiModelProperty(required= true,value = "大类费用项名称")
	private String itemName;
    /**
     * 大类费用项类型
     */
	@TableField("item_type")
	@ApiModelProperty(required= true,value = "大类费用项类型")
	private Integer itemType;
    /**
     * 级别
     */
	@TableField("item_level")
	@ApiModelProperty(required= true,value = "级别")
	private Integer itemLevel;
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

	public Integer getBusinessTypeId() {
		return businessTypeId;
	}

	public void setBusinessTypeId(Integer businessTypeId) {
		this.businessTypeId = businessTypeId;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public Integer getItemType() {
		return itemType;
	}

	public void setItemType(Integer itemType) {
		this.itemType = itemType;
	}

	public Integer getItemLevel() {
		return itemLevel;
	}

	public void setItemLevel(Integer itemLevel) {
		this.itemLevel = itemLevel;
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
		return this.profitItemSetId;
	}

	@Override
	public String toString() {
		return "ProfitItemSet{" +
			", profitItemSetId=" + profitItemSetId +
			", businessTypeId=" + businessTypeId +
			", itemName=" + itemName +
			", itemType=" + itemType +
			", itemLevel=" + itemLevel +
			", updateUserId=" + updateUserId +
			", updateTime=" + updateTime +
			"}";
	}
}
