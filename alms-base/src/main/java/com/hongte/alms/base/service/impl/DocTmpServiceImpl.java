package com.hongte.alms.base.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.assets.car.vo.FileVo;
import com.hongte.alms.base.entity.DocTmp;
import com.hongte.alms.base.mapper.DocTmpMapper;
import com.hongte.alms.base.mapper.DocTypeMapper;
import com.hongte.alms.base.service.DocTmpService;
import com.hongte.alms.base.vo.module.doc.DocItem;
import com.hongte.alms.base.vo.module.doc.DocTypeItem;
import com.hongte.alms.base.vo.module.doc.UpLoadResult;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.AliyunHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 上传文件临时表 服务实现类
 * </p>
 *
 * @author 王继光
 * @since 2018-03-08
 */
@Service("DocTmpService")
public class DocTmpServiceImpl extends BaseServiceImpl<DocTmpMapper, DocTmp> implements DocTmpService {
    @Autowired
    private DocTypeMapper docTypeMapper;
    
    @Autowired
    private AliyunHelper ossClient;
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
                        DocTmp doc = null;
                        String docID = UUID.randomUUID().toString();
                        //如果存在原文档编码，则尝试获取原文档信息，并做更新替换用，如果是不存在，则做新增处理
                        String oldDocId = fileVo.getOldDocId();
                        if (oldDocId != null && !oldDocId.equals("")) {
                            doc = this.selectOne(new EntityWrapper<DocTmp>().eq("doc_id", oldDocId));
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
                            doc = new DocTmp();
                            doc.setDocId(docID);
                            doc.setBusinessId(businessId);
                            doc.setCreateUser(userId);
                            doc.setCreateTime(new Date());
                        }
                        //查询文件类型
                        List<DocTypeItem> docTypes=docTypeMapper.getDocTypeByTypeCode(fileVo.getBusType());
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
