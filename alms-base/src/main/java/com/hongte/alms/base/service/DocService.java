package com.hongte.alms.base.service;

import com.hongte.alms.base.assets.car.vo.FileVo;
import com.hongte.alms.base.entity.Doc;
import com.hongte.alms.base.vo.module.doc.DocUploadRequest;
import com.hongte.alms.base.vo.module.doc.UpLoadResult;
import com.hongte.alms.common.service.BaseService;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

/**
 * <p>
 * 上传文件 服务类
 * </p>
 *
 * @author dengzhiming
 * @since 2018-02-28
 */
public interface DocService extends BaseService<Doc> {
	/**
	 * 上传文件
	 */
	UpLoadResult upload(DocUploadRequest request, String userId);

	/**
	 * 删除文件
	 */
	void delDoc(List<String> docIdList);

	/**
	 * 删除单个文件
	 */
	void delOneDoc(String docId);

	/**
	 * 上传文件
	 */
	public UpLoadResult upload(FileVo fileVo, String userId);

	/**
	 * 下载文件
	 * 
	 * @param downloadFile
	 *            下载到本地的文件名
	 * @param fileUrl
	 *            文件路径
	 */
	void download(String downloadFile, String fileUrl, HttpServletResponse response);
}
