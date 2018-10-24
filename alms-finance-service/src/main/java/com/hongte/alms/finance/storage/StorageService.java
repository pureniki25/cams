package com.hongte.alms.finance.storage;

import org.apache.poi.ss.usermodel.Workbook;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public interface StorageService {


    /**
     * 存储workBook到本地路径
     * @param workbook
     * @param filename
     * @return
     */
    Map<String,String> storageExcelWorkBook(Workbook workbook, String filename);


    void downloadExcel(HttpServletRequest request, HttpServletResponse response, String filename);


}
