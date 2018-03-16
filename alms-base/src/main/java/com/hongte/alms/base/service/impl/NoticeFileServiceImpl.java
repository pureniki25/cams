package com.hongte.alms.base.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.assets.car.vo.FileVo;
import com.hongte.alms.base.entity.Doc;
import com.hongte.alms.base.entity.NoticeFile;
import com.hongte.alms.base.mapper.NoticeFileMapper;
import com.hongte.alms.base.service.NoticeFileService;
import com.hongte.alms.base.vo.module.doc.DocItem;
import com.hongte.alms.base.vo.module.doc.DocTypeItem;
import com.hongte.alms.base.vo.module.doc.UpLoadResult;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.AliyunHelper;
import com.hongte.alms.common.util.OssResult;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * [公告附件登记表] 服务实现类
 * </p>
 *
 * @author 王继光
 * @since 2018-03-07
 */
@Service("NoticeFileService")
public class NoticeFileServiceImpl extends BaseServiceImpl<NoticeFileMapper, NoticeFile> implements NoticeFileService {
	@Autowired
    private AliyunHelper ossClient;
    
	
	public UpLoadResult upload(FileVo fileVo, String userId) {
        UpLoadResult upLoadResult = new UpLoadResult();
        upLoadResult.setUploaded(true);
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
                        String docID = UUID.randomUUID().toString();
                        //如果存在原文档编码，则尝试获取原文档信息，并做更新替换用，如果是不存在，则做新增处理
                        String fileName = file.getOriginalFilename();
                        String fileType = fileName.substring(fileName.lastIndexOf(".") + 1);
                        //扩展名称强制转换成小写
                        if(fileType!=null&&!fileType.contains(""))
                        {
                            fileType=fileType.toLowerCase();
                        }
                        String docUrl = "upload/" + businessId + "/" + docID + "." + fileType;
                        //新增文档
                        //修改文档
                        //step1 先写入OSS
                        ossClient.putObject(docUrl, content);
                        //step2 记录文档记录
                        //step3 返回当前OSS文档信息
                        
                        
                        
                        JSONObject jsonObject = new JSONObject() ;
                        jsonObject.put("docUrl", docUrl);
                        jsonObject.put("fileName", fileName);
                        jsonObject.put("fileType", fileType);
                        upLoadResult.setDocItemListJson(jsonObject.toJSONString());

                    
                
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
