package com.hongte.alms.scheduled.storage;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Service("storageService")
public class FileSystemStorageService implements StorageService {



    @Value("${ht.excel.file.save.path}")
    private  String excelSavePath;


    @Override
    public Map<String,String> storageExcelWorkBook(Workbook workbook, String filename){
        Map<String,String> retMap = new HashMap<>();
        retMap.put("errorInfo","");
        retMap.put("sucFlage","true");
        OutputStream os = null;
        String  errorInfo = "";
        boolean sucFlage = true;
//        String fileName =  UUID.randomUUID().toString()+".xls";
        try {
            String path = excelSavePath;
            // 2、保存到临时文件

            File tempFile = new File(path);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            os = new FileOutputStream(tempFile.getPath() + File.separator + filename);
            workbook.write(os);

        } catch (IOException e) {
            e.printStackTrace();
            retMap.put("errorInfo",e.getMessage());
            retMap.put("sucFlage","false");
//            sucFlage =false;
//            errorInfo =e.getMessage();
        } catch (Exception e) {
            e.printStackTrace();
            retMap.put("errorInfo",e.getMessage());
            retMap.put("sucFlage","false");
//            sucFlage =false;
//            errorInfo =e.getMessage();

        } finally {
            // 完毕，关闭所有链接
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return retMap;
    }

    @Override
    public void downloadExcel(HttpServletRequest request, HttpServletResponse response, String filename){
        response.setCharacterEncoding(request.getCharacterEncoding());
        response.setContentType("application/octet-stream");
        FileInputStream fis = null;
        try {
            File file = new File(excelSavePath+filename);
            fis = new FileInputStream(file);
            response.setHeader("Content-Disposition", "attachment; filename="+file.getName());
            IOUtils.copy(fis,response.getOutputStream());
            response.flushBuffer();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

}
