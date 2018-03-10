package com.hongte.alms.base.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.assets.car.vo.FileVo;
import com.hongte.alms.base.entity.Doc;
import com.hongte.alms.base.mapper.DocMapper;
import com.hongte.alms.base.mapper.DocTypeMapper;
import com.hongte.alms.base.service.DocService;
import com.hongte.alms.base.vo.module.doc.DocItem;
import com.hongte.alms.base.vo.module.doc.DocTypeItem;
import com.hongte.alms.base.vo.module.doc.DocUploadRequest;
import com.hongte.alms.base.vo.module.doc.UpLoadResult;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.AliyunHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * <p>
 * 上传文件 服务实现类
 * </p>
 *
 * @author dengzhiming
 * @since 2018-02-28
 */
@Service("DocService")
public class DocServiceImpl extends BaseServiceImpl<DocMapper, Doc> implements DocService {

    @Autowired
    private AliyunHelper ossClient;
    
    @Autowired
    private DocTypeMapper docTypeMapper;

    /**
     * TODO<一句话功能描述><br>
     * TODO<功能详细描述><br>
     * 上传
     *
     * @param request 上传文件请求包
     * @param userId  当前用户ID
     * @return com.ht.litigation.service.vo.UpLoadResult
     * @author 伦惠峰
     * @Date 2018/1/29 13:53
     */
    @Override
    public UpLoadResult upload(DocUploadRequest request, String userId) {
        UpLoadResult upLoadResult = new UpLoadResult();
        upLoadResult.setUploaded(true);
        DocItem docItem =null;
        List<DocItem> docItemList=new ArrayList<>();
        if (request == null) {
            upLoadResult.setUploaded(false);
            upLoadResult.setMessage("请求信息为空");
            return upLoadResult;
        } else {
            try {
                String docTypeItemJson = request.getDocTypeItemJson();
                String businessId = request.getBusinessId();
                DocTypeItem docTypeItem = null;
                if (docTypeItemJson != null && !docTypeItemJson.equals("")) {
                    docTypeItem = JSONObject.parseObject(docTypeItemJson, DocTypeItem.class);
                }
                if (docTypeItem == null) {
                    upLoadResult.setUploaded(false);
                    upLoadResult.setMessage("docTypeItem为空!");
                    return upLoadResult;
                }
                if (businessId == null || businessId.equals("")) {
                    upLoadResult.setUploaded(false);
                    upLoadResult.setMessage("businessId为空!");
                    return upLoadResult;
                }
                MultipartFile[] fileList=request.getMyFile();
                if(fileList==null||fileList.length==0)
                {
                    upLoadResult.setUploaded(false);
                    upLoadResult.setMessage("没有找到上传的文件!");
                    return upLoadResult;
                }
                for (MultipartFile multipartFile : fileList) {
                    byte[] content = multipartFile.getBytes();

                    if (docTypeItem != null) {
                        docItem = new DocItem();
                        Doc doc = null;
                        String docID = UUID.randomUUID().toString();
                        //如果存在原文档编码，则尝试获取原文档信息，并做更新替换用，如果是不存在，则做新增处理
                        String oldDocId = request.getOldDocId();
                        if (oldDocId != null && !oldDocId.equals("")) {
                            doc = this.selectOne(new EntityWrapper<Doc>().eq("doc_id", oldDocId));
                            if (doc != null) {
                                docID = doc.getDocId();
                            }
                        }

                        String fileName = request.getFileName();
                        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
                        //扩展名称强制转换成小写
                        if(fileType!=null&&!fileType.contains(""))
                        {
                            fileType=fileType.toLowerCase();
                        }
                        String docUrl = "upload/" + businessId + "/" + docID + "." + fileType;
                        //新增文档
                        if (doc == null) {
                            doc = new Doc();
                            doc.setDocId(docID);
                            doc.setBusinessId(businessId);
                            doc.setCreateUser(userId);
                            doc.setCreateTime(new Date());
                        }
                        //修改文档
                        doc.setDocName(docTypeItem.getText());
                        doc.setDocUrl(docUrl);
                        doc.setOriginalName(request.getFileName());
                        doc.setFileSize((long) content.length);
                        doc.setFileType(fileType);
                        doc.setUpdateUser(userId);
                        doc.setUpdateTime(new Date());
                        doc.setDocTypeId(docTypeItem.getDocTypeID());
                        //step1 先写入OSS
                        ossClient.putObject(docUrl, content);
                        //step2 记录文档记录
                        this.insertOrUpdate(doc);
                        //step3 返回当前OSS文档信息

                        String readUrl = ossClient.getReadUrl();
                        docItem.setTitle(docTypeItem.getText());
                        docItem.setUrl(readUrl + docUrl);
                        docItem.setDocID(docID);
                        docItem.setBusinessID(businessId);
                        docItem.setDocTypeID(docTypeItem.getDocTypeID());
                        docItem.setExt(fileType);
                        docItemList.add(docItem);

                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                upLoadResult.setUploaded(false);
                upLoadResult.setMessage("上传失败," + e.getMessage());
                return upLoadResult;
            }
        }
        String docItemListJson=JSONObject.toJSONString(docItemList);
        upLoadResult.setDocItemListJson(docItemListJson);
        return upLoadResult;
    }

    /**
     * 删除文档列表
     *
     * @param docIdList 文档ID列表
     * @return void
     * @author 伦惠峰
     * @Date 2018/1/29 14:53
     */
    @Override
    public void delDoc(List<String> docIdList) {
        if (docIdList != null) {
            List<String> delKeyList = new ArrayList<>();
            //step 1 通过ID获取文档集合列表
            List<Doc> list = this.selectBatchIds(docIdList);
            for (Doc doc : list) {
                delKeyList.add(doc.getDocUrl());
            }

            if(delKeyList.size()>0)
            {
                //step 2 批量删除文件
                ossClient.deleteObjectList(delKeyList);

                //step 3 根据文档ID集合，批量删除节点
                this.deleteBatchIds(docIdList);
            }

        }
    }

    /**
     * 删除单个文件
     *
     * @param docId
     * @return void
     * @author 伦惠峰
     * @Date 2018/1/29 15:26
     */
    @Override
    public void delOneDoc(String docId) {
        Doc doc = this.selectOne(new EntityWrapper<Doc>().eq("doc_id", docId));
        if (doc != null) {
            //删除文件
            ossClient.deleteObject(doc.getDocUrl());
            //删除数据
            this.deleteById(docId);
        }
    }
    /**
     * 支持业务操作中批量单个文件上传
     * @param fileVo
     * @param userId
     * @return
     */
    public UpLoadResult upload(FileVo fileVo, String userId) {
        UpLoadResult upLoadResult = new UpLoadResult();
        upLoadResult.setUploaded(true);
        DocItem docItem =null;
        List<DocItem> docItemList=new ArrayList<>();
        if (fileVo == null) {
            upLoadResult.setUploaded(false);
            upLoadResult.setMessage("请求信息为空");
            return upLoadResult;
        } else {
            try {

                String businessId = fileVo.getBusinessId();

                if (businessId == null || businessId.equals("")) {
                    upLoadResult.setUploaded(false);
                    upLoadResult.setMessage("businessId为空!");
                    return upLoadResult;
                }
                MultipartFile file=fileVo.getFile();
                if(file==null)
                {
                    upLoadResult.setUploaded(false);
                    upLoadResult.setMessage("没有找到上传的文件!");
                    return upLoadResult;
                }
           
                    byte[] content = file.getBytes();

         
                        docItem = new DocItem();
                        Doc doc = null;
                        String docID = UUID.randomUUID().toString();
                        //如果存在原文档编码，则尝试获取原文档信息，并做更新替换用，如果是不存在，则做新增处理
                        String oldDocId = fileVo.getOldDocId();
                        if (oldDocId != null && !oldDocId.equals("")) {
                            doc = this.selectOne(new EntityWrapper<Doc>().eq("doc_id", oldDocId));
                            if (doc != null) {
                                docID = doc.getDocId();
                            }
                        }

                        String fileName = fileVo.getFileName();
                        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
                        //扩展名称强制转换成小写
                        if(fileType!=null&&!fileType.contains(""))
                        {
                            fileType=fileType.toLowerCase();
                        }
                        String docUrl = "upload/" + businessId + "/" + docID + "." + fileType;
                        //新增文档
                        if (doc == null) {
                            doc = new Doc();
                            doc.setDocId(docID);
                            doc.setBusinessId(businessId);
                            doc.setCreateUser(userId);
                            doc.setCreateTime(new Date());
                        }
                        //查询文件类型
                        List<DocTypeItem> docTypes=docTypeMapper.getDocTypeByTypeCode("AfterLoan_Material_ReturnReg");
                        if(docTypes==null||docTypes.size()!=1) {
                        
                         upLoadResult.setUploaded(false);
                         upLoadResult.setMessage("该业务没有上传附件的权限!");
                         return upLoadResult;
                     
                        }
                        DocTypeItem docType=docTypes.get(0);
                        //修改文档
                        doc.setDocName(docType.getText());
                        doc.setDocUrl(docUrl);
                        doc.setOriginalName(fileVo.getFileName());
                        doc.setFileSize((long) content.length);
                        doc.setFileType(fileType);
                        doc.setUpdateUser(userId);
                        doc.setUpdateTime(new Date());
                        doc.setDocTypeId(docType.getDocTypeID());
                        //step1 先写入OSS
                        ossClient.putObject(docUrl, content);
                        //step2 记录文档记录
                        this.insertOrUpdate(doc);
                        //step3 返回当前OSS文档信息

                        String docItemListJson=JSONObject.toJSONString(doc);
                        upLoadResult.setDocItemListJson(docItemListJson);

                    
                
            } catch (Exception e) {
                e.printStackTrace();
                upLoadResult.setUploaded(false);
                upLoadResult.setMessage("上传失败," + e.getMessage());
                return upLoadResult;
            }
        }
  
        return upLoadResult;
    }

}
