package com.hongte.alms.base.vo.module.doc;

import java.util.List;

/**
 * 附件上传、查看页面绑定的实体对象
 * @author dengzhiming
 * @date 2018/2/28 15:44
 */
public class DocPageInfo {

    /**
     *业务编码
     */
    private String businessID;

    /**
     * 文档分类列表
     */
    private List<DocTypeItem> docTypeList;

    /**
     * 所有的文档列表
     */
    private List<DocItem> allDocList;

    public List<DocItem> getAllDocList() {
        return allDocList;
    }

    public void setAllDocList(List<DocItem> allDocList) {
        this.allDocList = allDocList;
    }

    public String getBusinessID() {
        return businessID;
    }

    public void setBusinessID(String businessID) {
        this.businessID = businessID;
    }

    public List<DocTypeItem> getDocTypeList() {
        return docTypeList;
    }

    public void setDocTypeList(List<DocTypeItem> docTypeList) {
        this.docTypeList = docTypeList;
    }
}
