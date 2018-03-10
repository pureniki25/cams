/*
 * Copyright (C), 2017-2018 广东鸿特信息咨询有限公司
 * FileName: DocUploadRequest.java
 * Author:   伦惠峰
 * Date:     2018/1/27 10:07
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间         版本号             描述
 */
package com.hongte.alms.base.vo.module.doc;

import org.springframework.web.multipart.MultipartFile;

/**
 * <br>
 * <br>
 * 文档上传请求包
 * @author 伦惠峰
 * @Date 2018/1/27 10:07
 */
public class DocUploadRequest {
    /**
     *原文档ID(如果存在则原文件做更新，如果不存在，则文件做新增)
     */
    private String oldDocId;

    /**
     *当前业务编码
     */
    private String businessId;

    /**
     *文件名称
     */
    private String fileName;

    /**
     *
     *当前文档类型
     */
    private DocTypeItem docTypeItem;

    /**
     *当前上传的文件JSON
     */
    private String docTypeItemJson;

    /**
     *当前上传的文件
     */
    private MultipartFile[] myFile;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public DocTypeItem getDocTypeItem() {
        return docTypeItem;
    }

    public void setDocTypeItem(DocTypeItem docTypeItem) {
        this.docTypeItem = docTypeItem;
    }

    public String getDocTypeItemJson() {
        return docTypeItemJson;
    }

    public void setDocTypeItemJson(String docTypeItemJson) {
        this.docTypeItemJson = docTypeItemJson;
    }

    public MultipartFile[] getMyFile() {
        return myFile;
    }

    public void setMyFile(MultipartFile[] myFile) {
        this.myFile = myFile;
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
}
