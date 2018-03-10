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
 * 文档类型
 * </p>
 *
 * @author dengzhiming
 * @since 2018-02-28
 */
@ApiModel
@TableName("tb_doc_type")
public class DocType extends Model<DocType> {

    private static final long serialVersionUID = 1L;

    /**
     * 文档分类ID
     */
    @TableId("doc_type_id")
	@ApiModelProperty(required= true,value = "文档分类ID")
	private String docTypeId;
    /**
     * 文档分类编码
     */
	@TableField("type_code")
	@ApiModelProperty(required= true,value = "文档分类编码")
	private String typeCode;
    /**
     * 文档分类名称
     */
	@TableField("doc_type_name")
	@ApiModelProperty(required= true,value = "文档分类名称")
	private String docTypeName;
    /**
     * 节点类型 0 文件 1 文件夹
     */
	@TableField("node_type")
	@ApiModelProperty(required= true,value = "节点类型 0 文件 1 文件夹")
	private Integer nodeType;
    /**
     * 分类行号
     */
	@TableField("row_index")
	@ApiModelProperty(required= true,value = "分类行号")
	private Integer rowIndex;
    /**
     * 文档的父级分类ID
     */
	@TableField("parent_type_id")
	@ApiModelProperty(required= true,value = "文档的父级分类ID")
	private String parentTypeId;
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


	public String getDocTypeId() {
		return docTypeId;
	}

	public void setDocTypeId(String docTypeId) {
		this.docTypeId = docTypeId;
	}

	public String getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(String typeCode) {
		this.typeCode = typeCode;
	}

	public String getDocTypeName() {
		return docTypeName;
	}

	public void setDocTypeName(String docTypeName) {
		this.docTypeName = docTypeName;
	}

	public Integer getNodeType() {
		return nodeType;
	}

	public void setNodeType(Integer nodeType) {
		this.nodeType = nodeType;
	}

	public Integer getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(Integer rowIndex) {
		this.rowIndex = rowIndex;
	}

	public String getParentTypeId() {
		return parentTypeId;
	}

	public void setParentTypeId(String parentTypeId) {
		this.parentTypeId = parentTypeId;
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
		return this.docTypeId;
	}

	@Override
	public String toString() {
		return "DocType{" +
			", docTypeId=" + docTypeId +
			", typeCode=" + typeCode +
			", docTypeName=" + docTypeName +
			", nodeType=" + nodeType +
			", rowIndex=" + rowIndex +
			", parentTypeId=" + parentTypeId +
			", createUser=" + createUser +
			", createTime=" + createTime +
			", updateUser=" + updateUser +
			", updateTime=" + updateTime +
			"}";
	}
}
