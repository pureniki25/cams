package com.hongte.alms.base.vo.project;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotEmpty;

import com.hongte.alms.base.entity.ProjExtRate;
import com.hongte.alms.base.enums.repayPlan.PepayPlanProjExtRatCalEnum;

import org.apache.commons.beanutils.BeanUtils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * 标的额外信息存储表
 * </p>
 *
 * @author 张贵宏
 * @since 2018-07-23
 */
@ApiModel
public class ProjExtRateVO implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public ProjExtRateVO(){}
	public ProjExtRateVO(ProjExtRate projExtRate){
		try {
			BeanUtils.copyProperties(this, projExtRate);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		this.calcWayName= PepayPlanProjExtRatCalEnum.nameOf(projExtRate.getCalcWay());
	}

	/**
     * 主键 自增
     */
	@ApiModelProperty(required= true,value = "主键 自增")
	private Integer id;
    /**
     * 业务ID
     */
	@NotEmpty(message="业务ID不能为空")
	@ApiModelProperty(required= true,value = "业务ID")
	private String businessId;
    /**
     * 标的ID
     */

	@NotEmpty(message="标的ID不能为空")
	@ApiModelProperty(required= true,value = "标的ID")
	private String projectId;
    /**
     * 费率类型
     */
	@ApiModelProperty(required= true,value = "费率类型")
	private Integer rateType;
    /**
     * 费率类型名称
     */
	@NotEmpty(message="费率类型名称不能为空")
	@ApiModelProperty(required= true,value = "费率类型名称")
	private String rateName;
    /**
     * 费率值
     */
	@ApiModelProperty(required= true,value = "费率值")
	private BigDecimal rateValue;
    /**
     * 计算方式
     */
	@ApiModelProperty(required= true,value = "计算方式")
	private Integer calcWay;
    /**
     * 费率UUID
     */
	@NotEmpty(message="费率UUID不能为空")
	@ApiModelProperty(required= true,value = "费率UUID")
	private String feeId;
    /**
     * 费率名称
     */
	@NotEmpty(message="费率名称不能为空")
	@ApiModelProperty(required= true,value = "费率名称")
	private String feeName;
    /**
     * 开始期数
     */
	@ApiModelProperty(required= true,value = "开始期数")
	private Integer beginPeroid;
    /**
     * 结束期数
     */
	@ApiModelProperty(required= true,value = "结束期数")
	private Integer endPeroid;
    /**
     * 创建日期
     */
	@ApiModelProperty(required= true,value = "创建日期")
	private Date createTime;
    /**
     * 创建用户
     */
	@ApiModelProperty(required= true,value = "创建用户")
	private String createUser;
    /**
     * 更新日期
     */
	@ApiModelProperty(required= true,value = "更新日期")
	private Date updateTime;
    /**
     * 更新用户
     */
	@ApiModelProperty(required= true,value = "更新用户")
	private String updateUser;

	/**
	 * 计算方式名称
	 */
	@ApiModelProperty(required=true, value="计算方式名称")
	private String calcWayName;


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

	/**
	 * @param calcWayName the calcWayName to set
	 */
	public void setCalcWayName(String calcWayName) {
		this.calcWayName = calcWayName;
	}

	/**
	 * @return the calcWayName
	 */
	public String getCalcWayName() {
		return calcWayName;
	}
}
