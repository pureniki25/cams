package com.hongte.alms.common.util;

import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

/**
 * @author zengkun
 * @since 2018/2/9
 * EasyPoi导出Excle报表的薄薄封装层
 */
public class EasyPoiExcelExportUtil {

    public static HttpServletResponse setResponseHead(HttpServletResponse response,String fileName) {
        // 告诉浏览器用什么软件可以打开此文件
        response.setHeader("content-Type", "application/vnd.ms-excel");
        // 下载文件的默认名称
        response.setHeader("Content-Disposition", "attachment;filename="+fileName/*AfterLoanStandingBook.xls"*/);
        return response;
    }
}
