package com.hongte.alms.finance.controller;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.dto.FactRepayReq;
import com.hongte.alms.base.entity.RepaymentConfirmLogSynch;
import com.hongte.alms.base.service.RepaymentConfirmLogSynchService;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.DateUtil;

import cn.afterturn.easypoi.entity.vo.MapExcelConstants;
import cn.afterturn.easypoi.entity.vo.NormalExcelConstants;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.view.PoiBaseView;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wangjiguang
 * @since 2018-10-09
 */
@Controller
@RequestMapping("/factRepay")
@Api(tags = "RepaymentConfirmLogSynchController", description = "实还记录")
public class RepaymentConfirmLogSynchController {

	private static Logger LOGGER = LoggerFactory.getLogger(RepaymentConfirmLogSynchController.class);
	
	@Autowired
	@Qualifier("RepaymentConfirmLogSynchService")
	private RepaymentConfirmLogSynchService synchService ;
	
	@Autowired
    private Executor msgThreadAsync;
	
	@PostMapping("/list")
	@ResponseBody
	@ApiOperation(value = "获取实还纪录")
	public Page<RepaymentConfirmLogSynch> list (@RequestBody FactRepayReq req){
		if (LOGGER.isInfoEnabled()) {
			LOGGER.info("@factRepay@list--开始{}",JSON.toJSONString(req));
		}
		return synchService.page(req) ;
	}
	
	@RequestMapping("/export")
	@ApiOperation(value = "导出实还纪录")
    public void download(ModelMap modelMap ,@RequestBody FactRepayReq req ,  HttpServletRequest request,HttpServletResponse response) {
		req.setCurPage(0);
        List<RepaymentConfirmLogSynch> repaymentConfirmLogSynchs = synchService.select(req);
        ExportParams params = new ExportParams("导出文件", "测试", ExcelType.XSSF);
        modelMap.put(NormalExcelConstants.DATA_LIST, repaymentConfirmLogSynchs); 
        modelMap.put(NormalExcelConstants.CLASS, RepaymentConfirmLogSynch.class);
        modelMap.put(NormalExcelConstants.PARAMS, params);//参数
        modelMap.put(NormalExcelConstants.FILE_NAME, params.getTitle());//文件名称
        
        PoiBaseView.render(modelMap, request, response, NormalExcelConstants.EASYPOI_EXCEL_VIEW);
//        return MapExcelConstants.EASYPOI_MAP_EXCEL_VIEW;//View名称

    }
	
	@RequestMapping("/synch")
	@ResponseBody
	@ApiOperation(value = "同步实还纪录")
	public Result synch() {
		long start = System.currentTimeMillis();
		int synch = synchService.synch() ;
		long end = System.currentTimeMillis();
		JSONObject result = new JSONObject() ;
		result.put("total", synch);
		result.put("time", (end-synch)/1000 % 60 );
		return Result.success(result);
	}
	
	@RequestMapping("/synchForScheduled")
	@ResponseBody
	public Result synchForScheduled() {
		msgThreadAsync.execute(new Runnable() {
			@Override
			public void run() {
				int synch = synchService.synch() ;
			}
		});
		return Result.success();
	}
}

