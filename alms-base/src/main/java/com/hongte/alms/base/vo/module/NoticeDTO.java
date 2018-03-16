/**
 * 
 */
package com.hongte.alms.base.vo.module;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hongte.alms.base.entity.Notice;
import com.hongte.alms.base.entity.NoticeFile;

/**
 * @author 王继光
 * 2018年3月16日 下午7:31:27
 */
public class NoticeDTO implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 808605176188277832L;

	
	/**
	 * 
	 */
	public NoticeDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	private String noticeId ;
	private String noticeTitle ;
	private String noticeContent;
	private String hasRead;
	private String hasReadTime;
	private String hasReadTimeUnit;
	private String orgCode;
	private String publishChannel;
	private String publishTime;
	
	private List<NoticeFile> files ;
	/**
	 * @return the files
	 */
	public List<NoticeFile> getFiles() {
		return files;
	}
	/**
	 * @param files the files to set
	 */
	public void setFiles(List<NoticeFile> files) {
		this.files = files;
	}
	/**
	 * @return the serialversionuid
	 */
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	/**
	 * @return the noticeId
	 */
	public String getNoticeId() {
		return noticeId;
	}
	/**
	 * @param noticeId the noticeId to set
	 */
	public void setNoticeId(String noticeId) {
		this.noticeId = noticeId;
	}
	/**
	 * @return the noticeTitle
	 */
	public String getNoticeTitle() {
		return noticeTitle;
	}
	/**
	 * @param noticeTitle the noticeTitle to set
	 */
	public void setNoticeTitle(String noticeTitle) {
		this.noticeTitle = noticeTitle;
	}
	/**
	 * @return the noticeContent
	 */
	public String getNoticeContent() {
		return noticeContent;
	}
	/**
	 * @param noticeContent the noticeContent to set
	 */
	public void setNoticeContent(String noticeContent) {
		this.noticeContent = noticeContent;
	}
	/**
	 * @return the hasRead
	 */
	public String getHasRead() {
		return hasRead;
	}
	/**
	 * @param hasRead the hasRead to set
	 */
	public void setHasRead(String hasRead) {
		this.hasRead = hasRead;
	}
	/**
	 * @return the hasReadTime
	 */
	public String getHasReadTime() {
		return hasReadTime;
	}
	/**
	 * @param hasReadTime the hasReadTime to set
	 */
	public void setHasReadTime(String hasReadTime) {
		this.hasReadTime = hasReadTime;
	}
	/**
	 * @return the hasReadTimeUnit
	 */
	public String getHasReadTimeUnit() {
		return hasReadTimeUnit;
	}
	/**
	 * @param hasReadTimeUnit the hasReadTimeUnit to set
	 */
	public void setHasReadTimeUnit(String hasReadTimeUnit) {
		this.hasReadTimeUnit = hasReadTimeUnit;
	}
	/**
	 * @return the orgCode
	 */
	public String getOrgCode() {
		return orgCode;
	}
	/**
	 * @param orgCode the orgCode to set
	 */
	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}
	/**
	 * @return the publishChannel
	 */
	public String getPublishChannel() {
		return publishChannel;
	}
	/**
	 * @param publishChannel the publishChannel to set
	 */
	public void setPublishChannel(String publishChannel) {
		this.publishChannel = publishChannel;
	}
	/**
	 * @return the publishTime
	 */
	public String getPublishTime() {
		return publishTime;
	}
	/**
	 * @param publishTime the publishTime to set
	 */
	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}
}
