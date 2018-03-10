/*
 * Copyright (C), 2017-2018 广东鸿特信息咨询有限公司
 * FileName: DocItem.java
 * Author:   伦惠峰
 * Date:     2018/1/25 16:34
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间         版本号             描述
 */
package com.hongte.alms.base.vo.module.doc;

import java.util.List;

/**
 * <br>
 * <br>
 * 文档节点
 * @author 伦惠峰
 * @Date 2018/1/25 16:34
 */
public class DocTypeItem {
    /**
     *节点名称
     */
    private String text;
    /**
     *节点值
     */
    private String  value;
    /**
     *子节点列表
     */
    private List<DocTypeItem> sons;
    /**
     *节点相关的文件
     */
    private List<DocItem> files;

    /**
     *类型节点ID
     */
    private String docTypeID;

    /**
     *分类编码
     */
    private String typeCode;

    /**
     *节点类型 0 文件 1 文件夹
     */
    private Integer nodeType;

    /**
     *父类ID
     */
    private String parentID;

    /**
     *行号
     */
    private Integer rowIndex;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public List<DocTypeItem> getSons() {
        return sons;
    }

    public void setSons(List<DocTypeItem> sons) {
        this.sons = sons;
    }

    public List<DocItem> getFiles() {
        return files;
    }

    public void setFiles(List<DocItem> files) {
        this.files = files;
    }

    public String getDocTypeID() {
        return docTypeID;
    }

    public void setDocTypeID(String docTypeID) {
        this.docTypeID = docTypeID;
    }

    public String getTypeCode() {
        return typeCode;
    }

    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
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

    public String getParentID() {
        return parentID;
    }

    public void setParentID(String parentID) {
        this.parentID = parentID;
    }
}
