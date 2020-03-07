package com.hongte.alms.base.entity;

import java.io.Serializable;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
/**
 * <p>
 * 
 * </p>
 *
 * @author 刘正全
 * @since 2018-11-21
 */
@ApiModel
@TableName("tb_split_info")
@Getter@Setter@ToString
public class SplitInfo extends Model<SplitInfo> {

    private static final long serialVersionUID = 1L;
    
    

    /**
     * 还款计划编号(主键)
     */
	@TableId("id")
	@ApiModelProperty(required= true,value = "主键")
	private Long id;
    
    @TableField("plan_id")
	@ApiModelProperty(required= true,value = "还款计划编号(主键)")
	private String planId;
    
    @TableField("business_id")
	@ApiModelProperty(required= true,value = "还款计划编号(主键)")
	private String businessId;
    
    @TableField("borrow_limit")
	@ApiModelProperty(required= true,value = "分期总数")
	private Integer borrowLimit;
    
    /**
     * 拆标状态:0未拆标 1拆标未完成  2拆标完成 3 数据核对完成 
     */
	@TableField(value="split_version")
	@ApiModelProperty(required= true,value = "拆标检查结果版本1开始 ")
	private Integer splitVersion;
    
    /**
     * 拆标状态:0未拆标 1拆标未完成  2拆标完成 3 数据核对完成 
     */
	@TableField("split_status")
	@ApiModelProperty(required= true,value = "拆标状态:0未拆标 1拆标未完成  2拆标完成 3 数据核对完成 ")
	private Integer splitStatus;
    /**
     * 拆标备注
     */
	@TableField("split_remark")
	@ApiModelProperty(required= true,value = "拆标备注")
	private String splitRemark;
    /**
     * 拆标时间
     */
	@TableField("split_time")
	@ApiModelProperty(required= true,value = "拆标时间")
	private Date splitTime;
    /**
     * 0可拆标 1.上标金额 != 借款金额2.检查数据完整性-还款计划有明细失败3.检查数据完整性-还款计划明细有详情4.不是信贷系统生成的还款计划5.还款计划不存在标信多个标的信息6.已结清的还款计划
     */
	@TableField("split_type")
	@ApiModelProperty(required= true,value = "0可拆标 1.上标金额 != 借款金额2.检查数据完整性-还款计划有明细失败3.检查数据完整性-还款计划明细有详情4.不是信贷系统生成的还款计划5.还款计划不存在标信多个标的信息6.已结清的还款计划")
	private Integer splitType;
    /**
     * 过滤名称
     */
	@TableField("split_name")
	@ApiModelProperty(required= true,value = "拆标过滤规则名称")
	private String split_name;
    /**
     * 还款期数
     */
	@TableField("after_id")
	@ApiModelProperty(required= true,value = "还款期数")
	private String afterId;
	/**
     * 检查位置1前置 2后置
     */
	@TableField("check_position")
	@ApiModelProperty(required= true,value = "检查位置1前置 2后置")
	private String checkPosition;
	/**
     * 创建时间
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建时间")
	private Date createTime;
	/**
     * 创建人
     */
	@TableField("create_man")
	@ApiModelProperty(required= true,value = "创建人")
	private String createMan;
	/**
     * 最后修改时间
     */
	@TableField("last_update_time")
	@ApiModelProperty(required= true,value = "最后修改时间")
	private Date lastUpdateTime;
	/**
     * 最后更新人
     */
	@TableField("last_update_man")
	@ApiModelProperty(required= true,value = "最后更新人")
	private String lastUpdateMan;
	/**
     * 检测功能点
     */
	@TableField("split_check_point")
	@ApiModelProperty(required= true,value = "检测功能点")
	private String splitCheckPoint;
	
	@Override
	protected Serializable pkVal() {
		return this.planId;
	}

	public SplitInfo() {
		super();
	}

	public SplitInfo(Long id, String planId, String businessId, Integer borrowLimit, Integer splitVersion,
			Integer splitStatus, String splitRemark, Date splitTime, Integer splitType, String afterId,
			String checkPosition, Date createTime, String createMan, Date lastUpdateTime, String lastUpdateMan,
			String splitCheckPoint) {
		super();
		this.id = id;
		this.planId = planId;
		this.businessId = businessId;
		this.borrowLimit = borrowLimit;
		this.splitVersion = splitVersion;
		this.splitStatus = splitStatus;
		this.splitRemark = splitRemark;
		this.splitTime = splitTime;
		this.splitType = splitType;
		this.afterId = afterId;
		this.checkPosition = checkPosition;
		this.createTime = createTime;
		this.createMan = createMan;
		this.lastUpdateTime = lastUpdateTime;
		this.lastUpdateMan = lastUpdateMan;
		this.splitCheckPoint = splitCheckPoint;
	}

}
