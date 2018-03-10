package com.hongte.alms.core.controller;


import com.hongte.alms.core.storage.StorageService;


import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;


/**
 * @author zengkun
 * @since 2018/2/25
 */
@RestController
@RequestMapping("downLoadController")

public class DownLoadController  implements Serializable {

    private final StorageService storageService;


    public DownLoadController(StorageService storageService) {
        this.storageService = storageService;
    }

    @ApiOperation(value = "下载Excel文件接口")
    @RequestMapping("excelFiles")
    public void downloadExcel(HttpServletRequest request, HttpServletResponse response,@RequestParam("filename") String filename) {
        storageService.downloadExcel(request,response,filename);
    }




}
