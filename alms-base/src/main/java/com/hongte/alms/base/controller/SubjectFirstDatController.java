package com.hongte.alms.base.controller;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

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

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.CamsSubject;
import com.hongte.alms.base.entity.PickStoreDat;
import com.hongte.alms.base.entity.SubjectFirstDat;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.service.SubjectFirstDatService;
import com.hongte.alms.base.service.SubjectRestDatService;
import com.hongte.alms.base.vo.cams.SubjectRestVo;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.vo.PageResult;

import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 科目期初余额表 前端控制器
 * </p>
 *
 * @author czs
 * @since 2020-01-20
 */
@RestController
@RequestMapping("/subjectFirstDat")
public class SubjectFirstDatController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(SubjectFirstDatController.class);

	@Autowired
	@Qualifier("SubjectFirstDatService")
	private SubjectFirstDatService subjectFirstDatService;
	
	@ApiOperation(value = "查询科目期初余额列表")
	@RequestMapping("/searchSubjectFirst")
	public PageResult searchSubjectFirst(@RequestBody Page<SubjectFirstDat> page) {
		
		page.setOrderByField("createTime").setAsc(false);
		subjectFirstDatService.selectByPage(page);
		List<SubjectFirstDat> list=page.getRecords();
		list=list.stream().sorted(Comparator.comparing(SubjectFirstDat::getSubject)).collect(Collectors.toList());
		page.setRecords(list);
		return PageResult.success(page.getRecords(), page.getTotal());
	}
	
	@ApiOperation(value = "导入科目期初余额")
	@RequestMapping("/importSubjectFirstExcel")
	public Result importSubjectFirstExcel(@RequestParam("file") MultipartFile file, HttpServletRequest request,
			MultipartRequest req) {
		Result result = null;
		try {
			Map<String, String[]> map = request.getParameterMap();
			String companyName = map.get("companyName")[0];
			if (!file.getOriginalFilename().contains(companyName)) {
				return Result.error("选择的公司与导入的公司不一致");
			}
			
			LOGGER.info("====>>>>>导入科目期初余额开始[{}]", file);
			subjectFirstDatService.importSubjectFirst(file, companyName);

			result = Result.success();
		} catch (ServiceRuntimeException se) {
			result = Result.error(se.getErrorCode(), se.getMessage());
			LOGGER.error("====>>>>>导入领料出错{}", se.getMessage());
		} catch (Exception e) {
			result = Result.error("500", "导入出错");
			LOGGER.error("====>>>>>导入领料出错{}", e);
		}
		LOGGER.info("====>>>>>导入领料excel结束");
		return result;
	}
}

