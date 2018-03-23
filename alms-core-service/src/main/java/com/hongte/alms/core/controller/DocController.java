package com.hongte.alms.core.controller;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hongte.alms.base.assets.car.vo.FileVo;
import com.hongte.alms.base.service.DocService;
import com.hongte.alms.base.service.DocTmpService;
import com.hongte.alms.base.service.DocTypeService;
import com.hongte.alms.base.vo.module.doc.DocPageInfo;
import com.hongte.alms.base.vo.module.doc.DocUploadRequest;
import com.hongte.alms.base.vo.module.doc.UpLoadResult;
import com.hongte.alms.common.result.Result;

import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 上传文件 前端控制器
 * </p>
 *
 * @author 伦惠峰
 * @since 2018-01-24
 */
@RestController
@RequestMapping("/doc")
public class DocController {

	private static final Logger LOG = LoggerFactory.getLogger(DocController.class);

	@Autowired
	@Qualifier("DocTypeService")
	private DocTypeService docTypeService;

	@Autowired
	@Qualifier("DocService")
	private DocService docService;

	@Autowired
	@Qualifier("DocTmpService")
	private DocTmpService docTmpService;

	@GetMapping("getAfterLoanDocPageInfo")
	@ApiOperation(value = "获取贷后资料")
	public Result<DocPageInfo> getAfterLoanDocPageInfo(String businessId) {
		return getDocPageInfo("AfterLoan", businessId);
	}

	@GetMapping("getDocPageInfo")
	@ApiOperation(value = "获取贷后指定的资料")
	public Result<DocPageInfo> getDocPageInfo(String typeCode, String businessID) {
		DocPageInfo docPageInfo = new DocPageInfo();
		docPageInfo = docTypeService.getDocPageInfo(typeCode, businessID);
		return Result.success(docPageInfo);
	}

	/**
	 * 文件上传具体实现方法（单文件上传）
	 */
	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public UpLoadResult upload(DocUploadRequest request) throws FileNotFoundException {
		return docService.upload(request, "admin");
	}

	/**
	 * 单文件删除
	 */
	@RequestMapping(value = "/delOneDoc", method = RequestMethod.GET)
	public void delOneDoc(String docId) {
		try {
		docService.delOneDoc(docId);
		}catch (Exception e) {
			LOG.error(e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * 批量删除文件(多个文件用,隔开)
	 */
	@RequestMapping(value = "/delDocs", method = RequestMethod.GET)
	public void delDocs(String docIds) {
		List<String> docIdList = new ArrayList<>();
		if (docIds != null && !docIds.equals("")) {
			String[] items = docIds.split(",");
			for (String item : items) {
				docIdList.add(item);
			}
			if (docIdList.size() > 0) {
				docService.delDoc(docIdList);
			}
		}

	}

	@RequestMapping(value = "/singleUpload", method = RequestMethod.POST)
	public UpLoadResult upload(FileVo fileVo) throws FileNotFoundException {
		return docTmpService.upload(fileVo, "admin");
	}

}
