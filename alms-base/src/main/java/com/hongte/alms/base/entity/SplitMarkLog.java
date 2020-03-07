package com.hongte.alms.base.entity;

import java.io.Serializable;

import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
/**
 * <p>
 * 拆标日志表
 * </p>
 *
 * @author 刘正全
 * @since 2018-10-22
 */
@ApiModel
@TableName("tb_split_mark_log")
public class SplitMarkLog extends Model<SplitMarkLog> {

    private static final long serialVersionUID = 1L;
    
    public SplitMarkLog() {
		super();
	}
    
	public SplitMarkLog(String businessId, String listId,String projectId,String afterId,String splitRemark) {
		super();
		this.afterId = afterId;
		this.listId = listId;
		this.splitRemark = splitRemark;
		this.projectId = projectId;
		this.businessId = businessId;
	}


	/**
     * id
     */
	@ApiModelProperty(required= true,value = "id")
	private Long id;
    /**
     * 业务id
     */
	@TableField("business_id")
	@ApiModelProperty(required= true,value = "业务id")
	private String businessId;
    /**
     * 批次期数
     */
	@TableField("after_id")
	@ApiModelProperty(required= true,value = "批次期数")
	private String afterId;
    /**
     * 拆分批次号
     */
	@TableField("batch_id")
	@ApiModelProperty(required= true,value = "拆分批次号")
	private String batchId;
    /**
     * 期数id
     */
	@TableField("list_id")
	@ApiModelProperty(required= true,value = "期数id")
	private String listId;
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
	@TableField("last_modify_time")
	@ApiModelProperty(required= true,value = "最后修改时间")
	private Date lastModifyTime;
    /**
     * 最后修改人
     */
	@TableField("last_modify_man")
	@ApiModelProperty(required= true,value = "最后修改人")
	private String lastModifyMan;
    /**
     * 拆标状态
     */
	@TableField("split_status")
	@ApiModelProperty(required= true,value = "拆标状态")
	private Integer splitStatus;
    /**
     * 拆标备注
     */
	@TableField("split_remark")
	@ApiModelProperty(required= true,value = "拆标备注")
	private String splitRemark;
    /**
     * 对应标号
     */
	@TableField("project_id")
	@ApiModelProperty(required= true,value = "对应标号")
	private String projectId;

	@TableField("operate_type")
	private Integer operateType ;
	
	@TableField("data_src_value")
	private String dataSrcValue ;
	
	@TableField("data_to_value")
	private String dataToValue ;
	
	@TableField("data_table")
	private String dataTable ;

	
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getAfterId() {
		return afterId;
	}

	public void setAfterId(String afterId) {
		this.afterId = afterId;
	}

	public String getBatchId() {
		return batchId;
	}

	public void setBatchId(String batchId) {
		this.batchId = batchId;
	}

	public String getListId() {
		return listId;
	}

	public void setListId(String listId) {
		this.listId = listId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getCreateMan() {
		return createMan;
	}

	public void setCreateMan(String createMan) {
		this.createMan = createMan;
	}

	public Date getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(Date lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public String getLastModifyMan() {
		return lastModifyMan;
	}

	public void setLastModifyMan(String lastModifyMan) {
		this.lastModifyMan = lastModifyMan;
	}

	public Integer getSplitStatus() {
		return splitStatus;
	}

	public void setSplitStatus(Integer splitStatus) {
		this.splitStatus = splitStatus;
	}

	public String getSplitRemark() {
		return splitRemark;
	}

	public void setSplitRemark(String splitRemark) {
		this.splitRemark = splitRemark;
	}

	public String getProjectId() {
		return projectId;
	}

	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}

	@Override
	protected Serializable pkVal() {
		return this.id;
	}

	@Override
	public String toString() {
		return "SplitMarkLog{" +
			", id=" + id +
			", businessId=" + businessId +
			", afterId=" + afterId +
			", batchId=" + batchId +
			", listId=" + listId +
			", createTime=" + createTime +
			", createMan=" + createMan +
			", lastModifyTime=" + lastModifyTime +
			", lastModifyMan=" + lastModifyMan +
			", splitStatus=" + splitStatus +
			", splitRemark=" + splitRemark +
			", projectId=" + projectId +
			"}";
	}

	public Integer getOperateType() {
		return operateType;
	}

	public void setOperateType(Integer operateType) {
		this.operateType = operateType;
	}

	public String getDataSrcValue() {
		return dataSrcValue;
	}

	public void setDataSrcValue(String dataSrcValue) {
		this.dataSrcValue = dataSrcValue;
	}

	public String getDataToValue() {
		return dataToValue;
	}

	public void setDataToValue(String dataToValue) {
		this.dataToValue = dataToValue;
	}

	public String getDataTable() {
		return dataTable;
	}

	public void setDataTable(String dataTable) {
		this.dataTable = dataTable;
	}
}
