package com.hongte.alms.base.controller;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.hongte.alms.base.enums.TokenTypeEnum;
import com.hongte.alms.base.service.CamsCompanyService;
import com.hongte.alms.common.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.CustomerFirstDat;
import com.hongte.alms.base.entity.SubjectFirstDat;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.service.CustomerFirstDatService;
import com.hongte.alms.base.service.SubjectFirstDatService;
import com.hongte.alms.base.vo.cams.CustomerRestVo;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.vo.PageResult;

import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 单位期初余额表 前端控制器
 * </p>
 *
 * @author czs
 * @since 2020-02-11
 */
@RestController
@RequestMapping("/customerFirstDat")
public class CustomerFirstDatController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SubjectFirstDatController.class);

	@Autowired
	@Qualifier("CustomerFirstDatService")
	private CustomerFirstDatService customerFirstDatService;

	@Autowired
	@Qualifier("CamsCompanyService")
	private CamsCompanyService camsCompanyService;

	
	@ApiOperation(value = "查询单位期初余额列表")
	@RequestMapping("/searchCustomerFirst")
	public PageResult searchSubjectFirst(@RequestBody Page<CustomerFirstDat> page) {
		
		page.setOrderByField("createTime").setAsc(false);
		customerFirstDatService.selectByPage(page);
		List<CustomerFirstDat> list=page.getRecords();
		 for(CustomerFirstDat customerRestVo:list) {
			 if(customerRestVo.getCustomerCode().contains("A")) {
				 customerRestVo.setSubject("1131"); 
				 customerRestVo.setSubjectName("应收账款");
			 }else {
				 customerRestVo.setSubject("2121"); 
				 customerRestVo.setSubjectName("应付账款");
			 }
		 }
		return PageResult.success(page.getRecords(), page.getTotal());
	}
	
	@ApiOperation(value = "导入单位期初余额")
	@RequestMapping("/importCustomerFirstExcel")
	public Result importSubjectFirstExcel(@RequestParam("file") MultipartFile file, HttpServletRequest request,
			MultipartRequest req) {
		Result<String> result = null;
		try {
			result=camsCompanyService.getCompany(request, TokenTypeEnum.COOKIES);
			String companyName="";
			if(result.getCode().equals("1")){
				companyName= (String) result.getData();
			}
			Map<String, String[]> map = request.getParameterMap();
			if(StringUtil.isEmpty(companyName)) {
				return Result.error("请选择公司名");
			}
			
			LOGGER.info("====>>>>>导入单位期初余额开始[{}]", file);
			customerFirstDatService.importCustomerFirst(file, companyName);

			result = Result.success();
		} catch (ServiceRuntimeException se) {
			result = Result.error(se.getErrorCode(), se.getMessage());
			LOGGER.error("====>>>>>导入单位期初余额出错{}", se.getMessage());
		} catch (Exception e) {
			result = Result.error("500", "导入出错");
			LOGGER.error("====>>>>>导入单位期初余额出错{}", e);
		}
		LOGGER.info("====>>>>>导入单位期初余额excel结束");
		return result;
	}
}

