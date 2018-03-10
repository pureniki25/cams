package com.hongte.alms.base.entity;

import java.io.Serializable;

import com.baomidou.mybatisplus.enums.IdType;
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
 * [公告附件登记表]
 * </p>
 *
 * @author 王继光
 * @since 2018-03-07
 */
@ApiModel
@TableName("tb_notice_file")
public class NoticeFile extends Model<NoticeFile> {

    private static final long serialVersionUID = 1L;

	@TableId(value="notice_file_id", type= IdType.AUTO)
	@ApiModelProperty(required= true,value = "")
	private Integer noticeFileId;
    /**
     * 外键
     */
	@TableField("notice_id")
	@ApiModelProperty(required= true,value = "外键")
	private Integer noticeId;
    /**
     * 文件路径
     */
	@TableField("file_url")
	@ApiModelProperty(required= true,value = "文件路径")
	private String fileUrl;
    /**
     * 创建时间
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建时间")
	private Date createTime;
	@TableField("user_id")
	@ApiModelProperty(required= true,value = "")
	private String userId;
    /**
     * 名称
     */
	@TableField("file_name")
	@ApiModelProperty(required= true,value = "名称")
	private String fileName;


	public Integer getNoticeFileId() {
		return noticeFileId;
	}

	public void setNoticeFileId(Integer noticeFileId) {
		this.noticeFileId = noticeFileId;
	}

	public Integer getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(Integer noticeId) {
		this.noticeId = noticeId;
	}

	public String getFileUrl() {
		return fileUrl;
	}

	public void setFileUrl(String fileUrl) {
		this.fileUrl = fileUrl;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	@Override
	protected Serializable pkVal() {
		return this.noticeFileId;
	}

	@Override
	public String toString() {
		return "NoticeFile{" +
			", noticeFileId=" + noticeFileId +
			", noticeId=" + noticeId +
			", fileUrl=" + fileUrl +
			", createTime=" + createTime +
			", userId=" + userId +
			", fileName=" + fileName +
			"}";
	}
}
