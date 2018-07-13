package com.hongte.alms.finance.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.collection.vo.CollectionTrackLogVo;
import com.hongte.alms.base.customer.vo.CustomerRepayFlowDto;
import com.hongte.alms.base.customer.vo.CustomerRepayFlowExel;
import com.hongte.alms.base.customer.vo.CustomerRepayFlowListReq;
import com.hongte.alms.base.customer.vo.CustomerRepayFlowOptReq;
import com.hongte.alms.base.entity.BasicCompany;
import com.hongte.alms.base.entity.DepartmentBank;
import com.hongte.alms.base.enums.AreaLevel;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.service.BasicCompanyService;
import com.hongte.alms.base.service.DepartmentBankService;
import com.hongte.alms.base.service.MoneyPoolRepaymentService;
import com.hongte.alms.base.util.CompanySortByPINYINUtil;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.JsonUtil;
import com.hongte.alms.common.vo.PageResult;
import com.hongte.alms.finance.service.CustomerRepayFlowService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.poi.ss.usermodel.Workbook;
import org.jeecgframework.poi.excel.ExcelExportUtil;
import org.jeecgframework.poi.excel.entity.ExportParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@RestController
@RequestMapping("/customer")
@Api(tags = "CustomerRepayFlowController", description = "客户还款登记流水", hidden = true)
public class CustomerRepayFlowController {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerRepayFlowController.class);

    @Autowired
    @Qualifier("MoneyPoolRepaymentService")
    private MoneyPoolRepaymentService moneyPoolRepaymentService;

    @Autowired
    @Qualifier("BasicCompanyService")
    BasicCompanyService basicCompanyService;

    @Autowired
    @Qualifier("DepartmentBankService")
    DepartmentBankService departmentBankService;

    @Autowired
    CustomerRepayFlowService customerRepayFlowService;


    /**
     * 获取客户还款流水列表
     *
     * @param customerRepayFlowListReq
     * @return
     */
    @ApiOperation(value = "获取客户还款流水列表")
    @GetMapping("/getCustomerRepayFlowList")
    @ResponseBody
    public PageResult<List<CustomerRepayFlowDto>> getCustomerRepayFlowList(CustomerRepayFlowListReq customerRepayFlowListReq) {
        LOGGER.info("====>>>>>获取客户还款流水列表开始[{}]", JSON.toJSONString(customerRepayFlowListReq));


        try {
            Page<CustomerRepayFlowDto> pages = moneyPoolRepaymentService.getCustomerRepayFlowPageList(customerRepayFlowListReq);


            return PageResult.success(pages.getRecords(), pages.getTotal());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            return PageResult.error(500, "数据库访问异常");
        }
    }

    @ApiOperation(value = "导出客户流水excel")
    @PostMapping("/downloadCustomerFlowExcel")
    @ResponseBody
    public Result downloadCustomerFlowExcel(CustomerRepayFlowListReq customerRepayFlowListReq) {
        LOGGER.info("====>>>>>导出客户流水excel开始[{}]", JSON.toJSONString(customerRepayFlowListReq));
        Result result = null;
        try {

            List<CustomerRepayFlowExel> customerRepayFlowList = moneyPoolRepaymentService.getCustomerRepayFlowList(customerRepayFlowListReq);
            Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), CustomerRepayFlowExel.class, customerRepayFlowList);


            String ossUrl = customerRepayFlowService.customerFlowExcelWorkBook(workbook);
            result = Result.success(ossUrl);
        } catch (ServiceRuntimeException se) {
            result = Result.error(se.getErrorCode(), se.getMessage());
            LOGGER.error("====>>>>>导出客户流水excel出错{}", se.getMessage());
        } catch (Exception e) {
            result = Result.error("500", "导出客户流水出错");
            LOGGER.error("====>>>>>导出客户流水excel出错{}", e);
        }

        LOGGER.info("====>>>>>导出客户流水excel结束{}", result);
        return result;
    }

    @ApiOperation(value = "导出客户流水excel")
    @PostMapping("/downloadExcel")
    @ResponseBody
    public void downloadExcel(CustomerRepayFlowListReq customerRepayFlowListReq, HttpServletResponse response) {
        LOGGER.info("====>>>>>导出客户流水excel开始[{}]", JSON.toJSONString(customerRepayFlowListReq));

        InputStream in = null;
        OutputStream out = null;
        try {
            List<CustomerRepayFlowExel> customerRepayFlowList = moneyPoolRepaymentService.getCustomerRepayFlowList(customerRepayFlowListReq);
            Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), CustomerRepayFlowExel.class, customerRepayFlowList);

            String prefix = UUID.randomUUID().toString();
            String suffix = ".xls";
            File tempFile = File.createTempFile(prefix, suffix);

            LOGGER.info("临时文件所在的本地路径：" + tempFile.getCanonicalPath());

            FileOutputStream os = new FileOutputStream(tempFile);

            workbook.write(os);
            os.close();
            in = new BufferedInputStream(new FileInputStream(tempFile));

            out = new BufferedOutputStream(response.getOutputStream());
            String downloadFile = "客户还款登记流水";
            response.setCharacterEncoding("utf-8");//设置编码集,文件名不会发生中文乱码
            response.setHeader("Content-Disposition", "attachment;filename=" +new String(downloadFile.getBytes("gbk"), "iso8859-1")+".xls");
            response.setHeader("Content-type", "application/octet-stream");


            byte[] car = new byte[1024];
            int count = 0;
            while ((count = in.read(car)) != -1) {
                out.write(car, 0, count);

            }
            tempFile.deleteOnExit();

        } catch (Exception e) {
            if (e instanceof IOException) {
                e.printStackTrace();
                LOGGER.error("文件下载发生异常!", e);
            } else if (e instanceof UnsupportedEncodingException) {
                e.printStackTrace();
                LOGGER.error("设置编码格式发生异常!", e);
            } else {
                e.printStackTrace();
                LOGGER.error("文件下载发生异常!", e);
            }
        } finally {
            try {
                in.close();
                out.flush();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        LOGGER.info("====>>>>>导出客户流水excel结束{}");
    }


    @ApiOperation(value = "获取客户还款流水列表请求参数列表")
    @GetMapping("/getCustomerRepayFlowSelectsData")
    @ResponseBody
    public Result<Map<String, JSONArray>> getCustomerRepayFlowSelectsData() {
        LOGGER.info("====>>>>>获取客户还款流水列表请求参数列表开始");
        Map<String, JSONArray> retMap = new HashMap<String, JSONArray>();

        //公司
        List<BasicCompany> company_list = basicCompanyService.selectList(new EntityWrapper<BasicCompany>().eq("area_level", AreaLevel.COMPANY_LEVEL.getKey()));
        CompanySortByPINYINUtil.sortByPINYIN(company_list);
        retMap.put("company", (JSONArray) JSON.toJSON(company_list, JsonUtil.getMapping()));

        //账户
        List<DepartmentBank> bank_list = departmentBankService.listDepartmentBank();
        retMap.put("bank", (JSONArray) JSON.toJSON(bank_list, JsonUtil.getMapping()));
        LOGGER.info("====>>>>>获取客户还款流水列表请求参数列表结束");
        return Result.success(retMap);
    }

    @ApiOperation(value = "导入客户还款流水excel")
    @RequestMapping("/importCustomerFlowExcel")
    public Result importCustomerFlowExcel(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        LOGGER.info("====>>>>>导入客户还款流水excel开始[{}]", file);
        Result result = null;
        try {
            customerRepayFlowService.importCustomerFlowExcel(file);

            result = Result.success();
        } catch (ServiceRuntimeException se) {
            result = Result.error(se.getErrorCode(), se.getMessage());
            LOGGER.error("====>>>>>导入客户还款流水出错{}", se.getMessage());
        } catch (Exception e) {
            result = Result.error("500", "导入客户流水出错");
            LOGGER.error("====>>>>>导入客户还款流水出错{}", e);
        }
        LOGGER.info("====>>>>>导入客户还款流水excel结束");
        return result;
    }

    @ApiOperation(value = "审核/拒绝客户还款流水记录")
    @RequestMapping("/auditOrRejectCustomerFlow")
    public Result auditOrRejectCustomerFlow(CustomerRepayFlowOptReq customerRepayFlowOptReq) {
        LOGGER.info("====>>>>>审核/拒绝客户还款流水记录开始[{}]", customerRepayFlowOptReq);
        Result result = null;
        try {
            customerRepayFlowService.auditOrRejectCustomerFlow(customerRepayFlowOptReq);

            result = Result.success();
        } catch (ServiceRuntimeException se) {
            result = Result.error(se.getErrorCode(), se.getMessage());
            LOGGER.error("====>>>>>审核/拒绝客户还款流水记录{}", se.getMessage());
        } catch (Exception e) {
            result = Result.error("500", "审核/拒绝客户还款流水记录出错");
            LOGGER.error("====>>>>>审核/拒绝客户还款流水记录出错{}", e);
        }
        LOGGER.info("====>>>>>审核/拒绝客户还款流水记录结束");
        return result;
    }

}
