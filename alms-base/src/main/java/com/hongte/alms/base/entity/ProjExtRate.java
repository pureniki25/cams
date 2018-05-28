package com.hongte.alms.base.entity;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * 标的额外信息存储表
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-05-26
 */
@ApiModel
@TableName("tb_proj_ext_rate")
public class ProjExtRate extends Model<ProjExtRate> {

    private static final long serialVersionUID = 1L;

    /**
     * 主键 自增
     */
	@TableId(value="id", type= IdType.AUTO)
	@ApiModelProperty(required= true,value = "主键 自增")
	private Integer id;
    /**
     * 业务ID
     */
	@TableField("business_id")
	@ApiModelProperty(required= true,value = "业务ID")
	private String businessId;
    /**
     * 标的ID
     */
	@TableField("project_id")
	@ApiModelProperty(required= true,value = "标的ID")
	private String projectId;
    /**
     * 费率类型
     */
	@TableField("rate_type")
	@ApiModelProperty(required= true,value = "费率类型")
	private Integer rateType;
    /**
     * 费率类型名称
     */
	@TableField("rate_name")
	@ApiModelProperty(required= true,value = "费率类型名称")
	private String rateName;
    /**
     * 费率值
     */
	@TableField("rate_value")
	@ApiModelProperty(required= true,value = "费率值")
	private BigDecimal rateValue;
    /**
     * 计算方式
     */
	@TableField("calc_way")
	@ApiModelProperty(required= true,value = "计算方式")
	private Integer calcWay;
    /**
     * 费率UUID
     */
	@TableField("fee_id")
	@ApiModelProperty(required= true,value = "费率UUID")
	private String feeId;
    /**
     * 费率名称
     */
	@TableField("fee_name")
	@ApiModelProperty(required= true,value = "费率名称")
	private String feeName;
    /**
     * 开始期数
     */
	@TableField("begin_peroid")
	@ApiModelProperty(required= true,value = "开始期数")
	private Integer beginPeroid;
    /**
     * 结束期数
     */
	@TableField("end_peroid")
	@ApiModelProperty(required= true,value = "结束期数")
	private Integer endPeroid;
    /**
     * 创建日期
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建日期")
	private Date createTime;
    /**
     * 创建用户
     */
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "创建用户")
	private String createUser;
    /**
     * 更新日期
     */
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "更新日期")
	private Date updateTime;
    /**
     * 更新用户
     */
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "更新用户")
	private String updateUser;


	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	public Integer getRateType() {
		return rateType;
	}

	public void setRateType(Integer rateType) {
		this.rateType = rateType;
	}

	public String getRateName() {
		return rateName;
	}

	public void setRateName(String rateName) {
		this.rateName = rateName;
	}

	public BigDecimal getRateValue() {
		return rateValue;
	}

	public void setRateValue(BigDecimal rateValue) {
		this.rateValue = rateValue;
	}

	public Integer getCalcWay() {
		return calcWay;
	}

	public void setCalcWay(Integer calcWay) {
		this.calcWay = calcWay;
	}

	public String getFeeId() {
		return feeId;
	}

	public void setFeeId(String feeId) {
		this.feeId = feeId;
	}

	public String getFeeName() {
		return feeName;
	}

	public void setFeeName(String feeName) {
		this.feeName = feeName;
	}

	public Integer getBeginPeroid() {
		return beginPeroid;
	}

	public void setBeginPeroid(Integer beginPeroid) {
		this.beginPeroid = beginPeroid;
	}

	public Integer getEndPeroid() {
		return endPeroid;
	}

	public void setEndPeroid(Integer endPeroid) {
		this.endPeroid = endPeroid;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "ProjExtRate{" +
			", id=" + id +
			", businessId=" + businessId +
			", projectId=" + projectId +
			", rateType=" + rateType +
			", rateName=" + rateName +
			", rateValue=" + rateValue +
			", calcWay=" + calcWay +
			", feeId=" + feeId +
			", feeName=" + feeName +
			", beginPeroid=" + beginPeroid +
			", endPeroid=" + endPeroid +
			", createTime=" + createTime +
			", createUser=" + createUser +
			", updateTime=" + updateTime +
			", updateUser=" + updateUser +
			"}";
	}
}
