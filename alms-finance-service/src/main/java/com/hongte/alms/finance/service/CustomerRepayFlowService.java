package com.hongte.alms.finance.service;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

public interface CustomerRepayFlowService {
    public String customerFlowExcelWorkBook(Workbook workbook) throws Exception;

    void importCustomerFlowExcel(MultipartFile file) throws Exception;
}
