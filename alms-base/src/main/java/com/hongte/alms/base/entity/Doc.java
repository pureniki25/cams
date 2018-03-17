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
 * 上传文件
 * </p>
 *
 * @author dengzhiming
 * @since 2018-02-28
 */
@ApiModel
@TableName("tb_doc")
public class Doc extends Model<Doc> {

    private static final long serialVersionUID = 1L;

    /**
     * 文档ID
     */
    @TableId("doc_id")
	@ApiModelProperty(required= true,value = "文档ID")
	private String docId;
    /**
     * 业务编号
     */
	@TableField("business_id")
	@ApiModelProperty(required= true,value = "业务编号")
	private String businessId;
    /**
     * 文档新名称
     */
	@TableField("doc_name")
	@ApiModelProperty(required= true,value = "文档新名称")
	private String docName;
    /**
     * 文档路径
     */
	@TableField("doc_url")
	@ApiModelProperty(required= true,value = "文档路径")
	private String docUrl;
    /**
     * 文件原名
     */
	@TableField("original_name")
	@ApiModelProperty(required= true,value = "文件原名")
	private String originalName;
    /**
     * 文件长度
     */
	@TableField("file_size")
	@ApiModelProperty(required= true,value = "文件长度")
	private Long fileSize;
    /**
     * 文件类型
     */
	@TableField("file_type")
	@ApiModelProperty(required= true,value = "文件类型")
	private String fileType;
    /**
     * 文档分类ID
     */
	@TableField("doc_type_id")
	@ApiModelProperty(required= true,value = "文档分类ID")
	private String docTypeId;
    /**
     * 创建用户
     */
	@TableField("create_user")
	@ApiModelProperty(required= true,value = "创建用户")
	private String createUser;
    /**
     * 创建时间
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建时间")
	private Date createTime;
    /**
     * 更新用户
     */
	@TableField("update_user")
	@ApiModelProperty(required= true,value = "更新用户")
	private String updateUser;
    /**
     * 更新时间
     */
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "更新时间")
	private Date updateTime;
	
	@TableField("doc_attr")
	@ApiModelProperty(required= true,value = "文件属性")
	private String docAttr;


	public String getDocId() {
		return docId;
	}

	public void setDocId(String docId) {
		this.docId = docId;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getDocName() {
		return docName;
	}

	public void setDocName(String docName) {
		this.docName = docName;
	}

	public String getDocUrl() {
		return docUrl;
	}

	public void setDocUrl(String docUrl) {
		this.docUrl = docUrl;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public String getDocTypeId() {
		return docTypeId;
	}

	public void setDocTypeId(String docTypeId) {
		this.docTypeId = docTypeId;
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
		return this.docId;
	}

	public String getDocAttr() {
		return docAttr;
	}

	public void setDocAttr(String docAttr) {
		this.docAttr = docAttr;
	}

	@Override
	public String toString() {
		return "Doc{" +
			", docId=" + docId +
			", businessId=" + businessId +
			", docName=" + docName +
			", docUrl=" + docUrl +
			", originalName=" + originalName +
			", fileSize=" + fileSize +
			", fileType=" + fileType +
			", docTypeId=" + docTypeId +
			", createUser=" + createUser +
			", createTime=" + createTime +
			", updateUser=" + updateUser +
			", updateTime=" + updateTime +
			", docAttr=" + docAttr +
			"}";
	}
}
