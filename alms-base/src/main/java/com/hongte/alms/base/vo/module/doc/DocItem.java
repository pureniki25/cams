package com.hongte.alms.base.vo.module.doc;

/**
 * 文档对象
 * @author dengzhiming
 * @date 2018/2/28 15:45
 */
public class DocItem {
    /**
     *文件名称
     */
    private String title;

    /**
     *文件路径
     */
    private String url;

    /**
     *文档ID
     */
    private String docID;

    /**
     *业务编码
     */
    private String businessID;

    /**
     *文件类型ID
     */
    private String docTypeID;

    /**
     * 文件扩展名
     */
    private String ext;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public String getBusinessID() {
        return businessID;
    }

    public void setBusinessID(String businessID) {
        this.businessID = businessID;
    }

    public String getDocTypeID() {
        return docTypeID;
    }

    public void setDocTypeID(String docTypeID) {
        this.docTypeID = docTypeID;
    }

    public String getExt() {
        return ext;
    }

    public void setExt(String ext) {
        this.ext = ext;
    }
}
