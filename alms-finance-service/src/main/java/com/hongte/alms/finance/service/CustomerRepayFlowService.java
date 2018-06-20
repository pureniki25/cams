package com.hongte.alms.finance.service;

import com.hongte.alms.base.customer.vo.CustomerRepayFlowOptReq;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

public interface CustomerRepayFlowService {
    public String customerFlowExcelWorkBook(Workbook workbook) throws Exception;

    void importCustomerFlowExcel(MultipartFile file) throws Exception;

    void auditOrRejectCustomerFlow(CustomerRepayFlowOptReq customerRepayFlowOptReq) throws Exception;
}
