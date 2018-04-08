package com.hongte.alms.core.controller;


import com.aliyun.oss.ServiceException;
import com.hongte.alms.base.service.DocService;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.core.storage.StorageService;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;


/**
 * @author zengkun
 * @since 2018/2/25
 */
@RestController
@RequestMapping("downLoadController")
@Api(tags = "DownLoadController", description = "文件下载相关接口")
public class DownLoadController  implements Serializable {
	private static final Logger LOG = LoggerFactory.getLogger(DownLoadController.class);

    private final StorageService storageService;
    
    @Autowired
	@Qualifier("DocService")
	private DocService docService;

    public DownLoadController(StorageService storageService) {
        this.storageService = storageService;
    }
    
    @ApiOperation(value = "下载Excel文件接口")
    @RequestMapping("excelFiles")
    public void downloadExcel(HttpServletRequest request, HttpServletResponse response,@RequestParam("filename") String filename) {
        storageService.downloadExcel(request,response,filename);
    }

    @RequestMapping(value = "/download", method = RequestMethod.GET)
	public void download(@RequestParam("downloadFile") String downloadFile, @RequestParam("docUrl") String docUrl) {
		if (StringUtil.isEmpty(downloadFile) || StringUtil.isEmpty(docUrl)) {
			LOG.error("非法参数！downloadFile：" + downloadFile + "，docUrl：" + docUrl);
			return;
		}

		try {
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes();
			HttpServletResponse response = requestAttributes.getResponse();
			docService.download(downloadFile, docUrl, response);
			LOG.info("附件下载成功！");
		} catch (Exception e) {
			LOG.error("文件下载失败：" + e.getMessage());
		}
	}


}
