package com.hongte.alms.base.service.impl;

import com.hongte.alms.base.entity.DocType;
import com.hongte.alms.base.mapper.DocTypeMapper;
import com.hongte.alms.base.service.DocTypeService;
import com.hongte.alms.base.vo.module.doc.DocItem;
import com.hongte.alms.base.vo.module.doc.DocPageInfo;
import com.hongte.alms.base.vo.module.doc.DocTypeItem;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.AliyunHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 文档类型 服务实现类
 * </p>
 *
 * @author dengzhiming
 * @since 2018-02-28
 */
@Service("DocTypeService")
public class DocTypeServiceImpl extends BaseServiceImpl<DocTypeMapper, DocType> implements DocTypeService {

    @Autowired
    private DocTypeMapper docTypeMapper;

    @Autowired
    private AliyunHelper ossClient;

    /**
     * 返回该业务相关的附件列表(暂时只支持2级菜单)
     */
    @Override
    public DocPageInfo getDocPageInfo(String typeCode, String businessID) {
        String ossReadUrl=ossClient.getReadUrl();
        DocPageInfo pageInfo = new DocPageInfo();
        pageInfo.setBusinessID(businessID);
        List<DocTypeItem> allItemList = docTypeMapper.getDocTypeItemList(typeCode);
        List<DocItem> docList = docTypeMapper.getDocItemList(typeCode, businessID);
        //文档列表按照文档分类建立索引
        Map<String, List<DocItem>> docGroupMap = new HashMap<>();
        for (DocItem docItem : docList) {
            docItem.setUrl(ossReadUrl+docItem.getUrl());
            List<DocItem> list = null;
            if (!docGroupMap.containsKey(docItem.getDocTypeID())) {
                list = new ArrayList<>();
                docGroupMap.put(docItem.getDocTypeID(), list);
            } else {
                list = docGroupMap.get(docItem.getDocTypeID());
            }
            list.add(docItem);
        }

        //建立一个父类ID作为索引的哈希表
        Map<String, List<DocTypeItem>> docTypeGroupMap = new HashMap<>();
        for (DocTypeItem docTypeItem : allItemList) {
            List<DocTypeItem> list = null;
            if (!docTypeGroupMap.containsKey(docTypeItem.getParentID())) {
                list = new ArrayList<>();
                docTypeGroupMap.put(docTypeItem.getParentID(), list);
            } else {
                list = docTypeGroupMap.get(docTypeItem.getParentID());
            }
            //匹配该类别下面是否存在文档
            if (docGroupMap.containsKey(docTypeItem.getDocTypeID())) {
                //设置文档列表
                docTypeItem.setFiles(docGroupMap.get(docTypeItem.getDocTypeID()));
            }
            else
            {
                //如果没有文件，也放一个默认集合进去
                docTypeItem.setFiles(new ArrayList<>());
            }
            list.add(docTypeItem);
        }
        List<DocTypeItem> rootItemList = new ArrayList<>();
        String rootKey = null;
        if (docTypeGroupMap.containsKey(rootKey)) {
            //顶级节点
            rootItemList = docTypeGroupMap.get(rootKey);
            if (rootItemList != null) {
                for (DocTypeItem docTypeItem : rootItemList) {
                    //通过字典，匹配该顶级节点相关的明细类别
                    if (docTypeGroupMap.containsKey(docTypeItem.getDocTypeID())) {
                        docTypeItem.setSons(docTypeGroupMap.get(docTypeItem.getDocTypeID()));
                    }
                }
            }
        }
        //设置顶级节点列表
        pageInfo.setDocTypeList(rootItemList);
        pageInfo.setAllDocList(docList);
        return pageInfo;
    }
}
