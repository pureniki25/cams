/**
 * 
 */
package com.hongte.alms.common.util;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

/**
 * 导出文件
 * @author 王继光
 * 2018年6月9日 下午5:40:30
 */
public class ExportFileUtil {

	/**
	 * 根据 filePath 和 exportName 导出文件(适用范围springmvc view层)
	 * @author 王继光
	 * 2018年6月9日 下午5:45:34
	 * @param filePath
	 * @param exportName
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public static ResponseEntity<byte[]> download(String filePath,String exportName) throws UnsupportedEncodingException , IOException {
		File file = new File(filePath);
        HttpHeaders headers = new HttpHeaders();    
        String fileName=new String(exportName.getBytes("UTF-8"),"iso-8859-1");//为了解决中文名称乱码问题  
        headers.setContentDispositionFormData("attachment", fileName);   
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);   
        return new ResponseEntity<byte[]>(FileUtils.readFileToByteArray(file),  headers, HttpStatus.CREATED);    
	}
}
