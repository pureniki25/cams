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
 * [通知公告详情表]
 * </p>
 *
 * @author 王继光
 * @since 2018-03-07
 */
@ApiModel
@TableName("tb_notice")
public class Notice extends Model<Notice> {

    private static final long serialVersionUID = 1L;

	@TableId(value="notice_id", type= IdType.AUTO)
	@ApiModelProperty(required= true,value = "")
	private Integer noticeId;
    /**
     * 标题
     */
	@TableField("notice_title")
	@ApiModelProperty(required= true,value = "标题")
	private String noticeTitle;
    /**
     * 内容
     */
	@TableField("notice_content")
	@ApiModelProperty(required= true,value = "内容")
	private String noticeContent;
    /**
     * 是否为必读公告,0为选读,1为必读
     */
	@TableField("has_read")
	@ApiModelProperty(required= true,value = "是否为必读公告,0为选读,1为必读")
	private Integer hasRead;
    /**
     * 必读公告的必读倒计时(必须读够一定时长才能关闭通知公告)
     */
	@TableField("has_read_time")
	@ApiModelProperty(required= true,value = "必读公告的必读倒计时(必须读够一定时长才能关闭通知公告)")
	private Integer hasReadTime;
    /**
     * 必读倒计时单位
     */
	@TableField("has_read_time_unit")
	@ApiModelProperty(required= true,value = "必读倒计时单位")
	private Integer hasReadTimeUnit;
    /**
     * 公告范围,参考tb_sys_org的org_code
     */
	@TableField("org_code")
	@ApiModelProperty(required= true,value = "公告范围,参考tb_sys_org的org_code")
	private String orgCode;
    /**
     * 发布渠道
     */
	@TableField("publish_channel")
	@ApiModelProperty(required= true,value = "发布渠道")
	private String publishChannel;
    /**
     * 1表示已发布，0或者null表示暂存
     */
	@TableField("is_send")
	@ApiModelProperty(required= true,value = "1表示已发布，0或者null表示暂存")
	private Integer isSend;
    /**
     * 创建人
     */
	@TableField("create_user_id")
	@ApiModelProperty(required= true,value = "创建人")
	private String createUserId;
    /**
     * 发布人
     */
	@TableField("publish_user_id")
	@ApiModelProperty(required= true,value = "发布人")
	private String publishUserId;
    /**
     * 创建时间
     */
	@TableField("create_time")
	@ApiModelProperty(required= true,value = "创建时间")
	private Date createTime;
    /**
     * 更新时间
     */
	@TableField("update_time")
	@ApiModelProperty(required= true,value = "更新时间")
	private Date updateTime;
    /**
     * 是否被删除,1被删除,0或者null未删除
     */
	@TableField("is_deleted")
	@ApiModelProperty(required= true,value = "是否被删除,1被删除,0或者null未删除")
	private Integer isDeleted;
    /**
     * 删除公告的userid
     */
	@TableField("delete_user_id")
	@ApiModelProperty(required= true,value = "删除公告的userid")
	private String deleteUserId;
    /**
     * 删除时间
     */
	@TableField("delete_time")
	@ApiModelProperty(required= true,value = "删除时间")
	private Date deleteTime;
    /**
     * 发布时间
     */
	@TableField("publish_time")
	@ApiModelProperty(required= true,value = "发布时间")
	private Date publishTime;


	public Integer getNoticeId() {
		return noticeId;
	}

	public void setNoticeId(Integer noticeId) {
		this.noticeId = noticeId;
	}

	public String getNoticeTitle() {
		return noticeTitle;
	}

	public void setNoticeTitle(String noticeTitle) {
		this.noticeTitle = noticeTitle;
	}

	public String getNoticeContent() {
		return noticeContent;
	}

	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}

	public Integer getHasRead() {
		return hasRead;
	}

	public void setHasRead(Integer hasRead) {
		this.hasRead = hasRead;
	}

	public Integer getHasReadTime() {
		return hasReadTime;
	}

	public void setHasReadTime(Integer hasReadTime) {
		this.hasReadTime = hasReadTime;
	}

	public Integer getHasReadTimeUnit() {
		return hasReadTimeUnit;
	}

	public void setHasReadTimeUnit(Integer hasReadTimeUnit) {
		this.hasReadTimeUnit = hasReadTimeUnit;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public String getPublishChannel() {
		return publishChannel;
	}

	public void setPublishChannel(String publishChannel) {
		this.publishChannel = publishChannel;
	}

	public Integer getIsSend() {
		return isSend;
	}

	public void setIsSend(Integer isSend) {
		this.isSend = isSend;
	}

	public String getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(String createUserId) {
		this.createUserId = createUserId;
	}

	public String getPublishUserId() {
		return publishUserId;
	}

	public void setPublishUserId(String publishUserId) {
		this.publishUserId = publishUserId;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Integer isDeleted) {
		this.isDeleted = isDeleted;
	}

	public String getDeleteUserId() {
		return deleteUserId;
	}

	public void setDeleteUserId(String deleteUserId) {
		this.deleteUserId = deleteUserId;
	}

	public Date getDeleteTime() {
		return deleteTime;
	}

	public void setDeleteTime(Date deleteTime) {
		this.deleteTime = deleteTime;
	}

	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}

	@Override
	protected Serializable pkVal() {
		return this.noticeId;
	}

	@Override
	public String toString() {
		return "Notice{" +
			", noticeId=" + noticeId +
			", noticeTitle=" + noticeTitle +
			", noticeContent=" + noticeContent +
			", hasRead=" + hasRead +
			", hasReadTime=" + hasReadTime +
			", hasReadTimeUnit=" + hasReadTimeUnit +
			", orgCode=" + orgCode +
			", publishChannel=" + publishChannel +
			", isSend=" + isSend +
			", createUserId=" + createUserId +
			", publishUserId=" + publishUserId +
			", createTime=" + createTime +
			", updateTime=" + updateTime +
			", isDeleted=" + isDeleted +
			", deleteUserId=" + deleteUserId +
			", deleteTime=" + deleteTime +
			", publishTime=" + publishTime +
			"}";
	}
}
