package com.hongte.alms.base.assets.car.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

import org.springframework.web.multipart.MultipartFile;
/**
 * @since 2018/1/30
 * 上传附件
 */
@ApiModel(value="文件上传信息",description="文件上传信息")
public class FileVo implements Serializable{
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@ApiModelProperty(value="文件",name="file")
    private MultipartFile file;  //文件

    @ApiModelProperty(value="文件名",name="fileName")
    private String fileName; //文件名

    @ApiModelProperty(value="业务编号",name="businessId")
    private String businessId; //文件名
    
    @ApiModelProperty(value="原文件id",name="oldDocId")
    private String oldDocId;
    
    @ApiModelProperty(value="修改后文件名",name="originalName")
    private String originalName;

    @ApiModelProperty(value="业务类型",name="busType")
    private String busType;


	public MultipartFile getFile() {
		return file;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getBusinessId() {
		return businessId;
	}

	public void setBusinessId(String businessId) {
		this.businessId = businessId;
	}

	public String getOldDocId() {
		return oldDocId;
	}

	public void setOldDocId(String oldDocId) {
		this.oldDocId = oldDocId;
	}

	public String getOriginalName() {
		return originalName;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	public String getBusType() {
		return busType;
	}

	public void setBusType(String busType) {
		this.busType = busType;
	}


   
}
