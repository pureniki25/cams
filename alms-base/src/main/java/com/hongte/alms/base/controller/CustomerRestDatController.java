package com.hongte.alms.base.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContextException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.CustomerRestDat;
import com.hongte.alms.base.service.CustomerRestDatService;
import com.hongte.alms.base.service.SubjectRestDatService;
import com.hongte.alms.base.vo.cams.CustomerRestVo;
import com.hongte.alms.base.vo.cams.SubjectRestVo;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.common.vo.PageResult;

import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 单位余额表 前端控制器
 * </p>
 *
 * @author czs
 * @since 2020-02-10
 */
@RestController
@RequestMapping("/customerRestDat")
public class CustomerRestDatController {
	@Autowired
	@Qualifier("CustomerRestDatService")
	private CustomerRestDatService customerRestDatService;
	
	@ApiOperation(value = "单位余额表查询")
	@RequestMapping("/selectCustomerRest")
	public PageResult<List<CustomerRestVo>> selectCustomerRest(@RequestBody CustomerRestVo vo) throws InstantiationException, IllegalAccessException {
		if(StringUtil.isEmpty(vo.getCompanyName())) {
			throw new ApplicationContextException("请选择公司名称");
		}
		Page<CustomerRestVo> pages = customerRestDatService.selectCustomerRestList(vo);
		return PageResult.success(pages.getRecords(), pages.getTotal());
	}
}

