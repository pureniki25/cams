package com.hongte.alms.base.controller;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.CamsCompany;
import com.hongte.alms.base.enums.CamsConstant;
import com.hongte.alms.base.enums.TokenTypeEnum;
import com.hongte.alms.base.service.CamsCompanyService;
import com.hongte.alms.base.service.CustomerRestDatService;
import com.hongte.alms.base.service.SubjectRestDatService;
import com.hongte.alms.base.vo.cams.BalanceVo;
import com.hongte.alms.base.vo.cams.SubjectRestVo;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.common.vo.PageResult;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContextException;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 科目余额表 前端控制器
 * </p>
 *
 * @author czs
 * @since 2020-01-19
 */
@RestController
@RequestMapping("/subjectRestDat")
public class SubjectRestDatController {
	private static final Logger LOGGER = LoggerFactory.getLogger(SubjectRestDatController.class);
	@Autowired
	@Qualifier("SubjectRestDatService")
	private SubjectRestDatService subjectRestDatService;
	
	@Autowired
	@Qualifier("CamsCompanyService")
	private CamsCompanyService camsCompanyService;
	
	@Autowired
	@Qualifier("CustomerRestDatService")
	private CustomerRestDatService customerRestDatService;
	
	//期初数
	public static final String FIRST="first";
	
	//期末数
	public static final String REST="rest";
	

	@ApiOperation(value = "科目余额表查询")
	@RequestMapping("/selectSubjectRest")
	public PageResult<List<SubjectRestVo>> selectSubjectRest(@RequestBody SubjectRestVo vo,HttpServletRequest request) throws InstantiationException, IllegalAccessException {
		Result<String> result=camsCompanyService.getCompany(request, TokenTypeEnum.TOKEN);
		String companyName="";
		if(result.getCode().equals("1")){
			companyName=result.getData();
			vo.setCompanyName(companyName);
		}
		if(StringUtil.isEmpty(vo.getCompanyName())) {
			throw new ApplicationContextException("请选择公司名称");
		}
		Page<SubjectRestVo> pages = subjectRestDatService.selectSubjectRest(vo);
		return PageResult.success(pages.getRecords(), pages.getTotal());
	}
	
	@ApiOperation(value = "资产负债表查询")
	@RequestMapping("/selectBalace")
	public PageResult<List<BalanceVo>> selectBalace(@RequestBody SubjectRestVo vo, HttpServletRequest request) throws Exception {
		Result<String> result=camsCompanyService.getCompany(request, TokenTypeEnum.TOKEN);
		String companyName="";
		if(result.getCode().equals("1")){
			companyName=result.getData();
			vo.setCompanyName(companyName);
		}
		if(StringUtil.isEmpty(vo.getCompanyName())) {
			throw new ApplicationContextException("请选择公司名称");
		}
		Page<BalanceVo> pages = subjectRestDatService.selectBalance(vo);
		return PageResult.success(pages.getRecords(), pages.getTotal());
	}
	
	@ApiOperation(value = "利润表查询")
	@RequestMapping("/selectProfit")
	public PageResult<List<BalanceVo>> selectProfit(@RequestBody SubjectRestVo vo,HttpServletRequest request) throws Exception {
		Result<String> result=camsCompanyService.getCompany(request, TokenTypeEnum.TOKEN);
		String companyName="";
		if(result.getCode().equals("1")){
			companyName=result.getData();
			vo.setCompanyName(companyName);
		}
		if(StringUtil.isEmpty(vo.getCompanyName())) {
			throw new ApplicationContextException("请选择公司名称");
		}
		if(vo.getBeginDate()==null) {
			vo.setBeginDate(DateUtil.getYearFirst(DateUtil.getYear(new Date())));
		}
		if(vo.getEndDate()==null) {
			vo.setEndDate(DateUtil.getLastEndDate());
		}
		Page<BalanceVo> pages = subjectRestDatService.selectProfit(vo);
		return PageResult.success(pages.getRecords(), pages.getTotal());
	}
	
	
	@ApiOperation(value = "导出资产负债列表")
	@PostMapping("/exportBalance")
	public void export(HttpServletResponse response, HttpServletRequest request, @ModelAttribute SubjectRestVo vo)
			throws Exception {
		Result<String> result=camsCompanyService.getCompany(request, TokenTypeEnum.COOKIES);
		String companyName="";
		if(result.getCode().equals("1")){
			companyName=result.getData();
		}
		Map<String, String[]> map = request.getParameterMap();
		String openBeginTimeStr = map.get("openBeginTime")[0];
		String openEndTimeStr = map.get("openEndTime")[0];
		String openBeginTime = null;
		String openEndTime = null;
	
		CamsCompany company=camsCompanyService.selectOne(new EntityWrapper<CamsCompany>().eq("company_name", companyName));
		if (!StringUtil.isEmpty(openBeginTimeStr)) {
			openBeginTime = DateUtil.formatDate(new Date(openBeginTimeStr));
		}
		if (!StringUtil.isEmpty(openEndTimeStr)) {
			openEndTime =DateUtil.formatDate(new Date(openEndTimeStr));
		}
		vo.setBeginDate(DateUtil.getDate(openBeginTime));
		vo.setEndDate(DateUtil.getDate(openEndTime));
		if(vo.getBeginDate()==null) {
			vo.setBeginDate(DateUtil.getYearFirst(DateUtil.getYear(new Date())));
		}
		if(vo.getEndDate()==null) {
			vo.setEndDate(DateUtil.getLastEndDate());
		}
		vo.setCompanyName(companyName);
		vo.setPage(1);
		vo.setLimit(2000);
		Page<BalanceVo> page =subjectRestDatService.selectBalance(vo);
	    List<BalanceVo>	list=page.getRecords();
	    Map<Integer, List<BalanceVo>> rowMap=list.stream().collect(Collectors.groupingBy(BalanceVo::getRowNum));
	    Map<String,Object> beans=new HashMap<String,Object>();
	    beans.put("public.author", company.getCompanyOwner());
	    beans.put("public.companyNo", company.getCompanyNo());
	    beans.put("public.companyName", vo.getCompanyName());
	    beans.put("public.beginYear", DateUtil.getYear(vo.getBeginDate()));
	    beans.put("public.beginMonth", DateUtil.getMonth(vo.getBeginDate()));
	    beans.put("public.beginDay", DateUtil.getDay(vo.getBeginDate()));
	    beans.put("public.endYear", DateUtil.getYear(vo.getEndDate()));
	    beans.put("public.endMonth", DateUtil.getMonth(vo.getEndDate()));
	    beans.put("public.endDay", DateUtil.getDay(vo.getEndDate()));
	    String exportTemp=CamsConstant.COMPANY_TEMP;
	    if(company.getCompanyRule().equals(CamsConstant.CompanyRuleEnum.QI_YE.getValue().toString())) {
	        for(Map.Entry<Integer, List<BalanceVo>> entry : rowMap.entrySet()) {
		    	List<BalanceVo> temp=rowMap.get(entry.getKey());
		    	if(!CollectionUtils.isEmpty(temp)) {
		    		String firstKey="balance.first.";
		    		String endKey="balance.end.";
		    		firstKey=firstKey+entry.getKey();
		    		endKey=endKey+entry.getKey();
		    		beans.put(firstKey,temp.get(0).getFirstAmount());
		    		beans.put(endKey, temp.get(0).getRestAmount());
		    	}
		    }
			subjectRestDatService.downLoadFile(response,exportTemp,beans,vo.getCompanyName());
	    	
	    }else {
	    	   exportTemp=CamsConstant.SMALL_COMPANY_TEMP;
	    	   for(Map.Entry<Integer, List<BalanceVo>> entry : rowMap.entrySet()) {
			    	List<BalanceVo> temp=rowMap.get(entry.getKey());
			    	if(!CollectionUtils.isEmpty(temp)) {
			    		String firstKey="balance.first.";
			    		String endKey="balance.end.";
			    		firstKey=firstKey+entry.getKey();
			    		endKey=endKey+entry.getKey();
			    		beans.put(firstKey,temp.get(0).getFirstAmount());
			    		beans.put(endKey, temp.get(0).getRestAmount());
			    	}
			    }
	    	   
	    		//查询科目余额表数据
	   		Page<SubjectRestVo> page2=subjectRestDatService.selectSubjectRest(vo);
	   	    List<SubjectRestVo>	list2=page2.getRecords();
	   	    Map<String, List<SubjectRestVo>> subjectType=list2.stream().collect(Collectors.groupingBy(SubjectRestVo::getSubject));
			Double first_1211 = subjectRestDatService.getBalaceAmount("1211", FIRST, subjectType); // 原材料
			Double end_1211 = subjectRestDatService.getBalaceAmount("1211", REST, subjectType); // 原材料
			Double first_1241 = subjectRestDatService.getBalaceAmount("1241", FIRST, subjectType); // 在产品
			Double end_1241 = subjectRestDatService.getBalaceAmount("1241", REST, subjectType); // 在产品
			Double first_1243 = subjectRestDatService.getBalaceAmount("1243", FIRST, subjectType); // 库存商品
			Double end_1243 = subjectRestDatService.getBalaceAmount("1243", REST, subjectType); // 库存商品
			Double first_1411 = subjectRestDatService.getBalaceAmount("1411", FIRST, subjectType); // 周转材料
			Double end_1411 = subjectRestDatService.getBalaceAmount("1411", REST, subjectType); // 周转材料
			Double first_1621 = subjectRestDatService.getBalaceAmount("1621", FIRST, subjectType); // 生物性生物资产
			Double end_1621 = subjectRestDatService.getBalaceAmount("1621", REST, subjectType); // 生物性生物资产
			Double first_1706 = subjectRestDatService.getBalaceAmount("1706", FIRST, subjectType); // 开发支出
			Double end_1706 = subjectRestDatService.getBalaceAmount("1706", REST, subjectType); // 开发支出
			Double first_2231 = subjectRestDatService.getBalaceAmount("2231", FIRST, subjectType); // 应付利息
			Double end_2231 = subjectRestDatService.getBalaceAmount("2231", REST, subjectType); // 应付利息
			
			Double first_2232 = subjectRestDatService.getBalaceAmount("2232", FIRST, subjectType); // 应付利润
			Double end_2232 = subjectRestDatService.getBalaceAmount("2232", REST, subjectType); // 应付利润
			
			Double first_2401 = subjectRestDatService.getBalaceAmount("2401", FIRST, subjectType); // 递延收益
			Double end_2401 = subjectRestDatService.getBalaceAmount("2401", REST, subjectType); // 递延收益
			
			beans.put("balance.first.1211", first_1211);
			beans.put("balance.end.1211", end_1211);

			beans.put("balance.first.1241", first_1241);
			beans.put("balance.end.1241", end_1241);

			beans.put("balance.first.1243", first_1243);
			beans.put("balance.end.1243", end_1243);

			beans.put("balance.first.1411", first_1411);
			beans.put("balance.end.1411", end_1411);

			beans.put("balance.first.1621", first_1621);
			beans.put("balance.end.1621", end_1621);

			beans.put("balance.first.1706", first_1706);
			beans.put("balance.end.1706", end_1706);
			
			beans.put("balance.first.2231", first_2231);
			beans.put("balance.end.2231", end_2231);
			
			beans.put("balance.first.2232", first_2232);
			beans.put("balance.end.2232", end_2232);
			
			beans.put("balance.first.2401", first_2401);
			beans.put("balance.end.2401", end_2401);
			
			//其他非流动资产
	   	    Double first_1711= subjectRestDatService.getBalaceAmount("1711", FIRST, subjectType); //商誉
	   	    Double end_1711= subjectRestDatService.getBalaceAmount("1711", REST, subjectType); //商誉
	   	    Double first_1712= subjectRestDatService.getBalaceAmount("1712", FIRST, subjectType); //商誉减值准备
	   	    Double end_1712= subjectRestDatService.getBalaceAmount("1712", REST, subjectType); //商誉减值准备
	   	    Double first_1801= subjectRestDatService.getBalaceAmount("1801", FIRST, subjectType); //长期待摊费用
	   	    Double end_1801= subjectRestDatService.getBalaceAmount("1801", REST, subjectType); //长期待摊费用
	   	    Double first_1811= subjectRestDatService.getBalaceAmount("1811", FIRST, subjectType); //递延所得税资产
	   	    Double end_1811= subjectRestDatService.getBalaceAmount("1811", REST, subjectType); //递延所得税资产
	   	    Double first_1901= subjectRestDatService.getBalaceAmount("1901", FIRST, subjectType); //待处理财产损溢
	   	    Double end_1901= subjectRestDatService.getBalaceAmount("1901", REST, subjectType); //待处理财产损溢
	   	    beans.put("balance.first.2800", first_1711+first_1712+first_1801+first_1811+first_1901);
	   	    beans.put("balance.end.2800", end_1711+end_1712+end_1801+end_1811+end_1901);
	   		
			//其他非流动负债
	   	    Double first_2191= subjectRestDatService.getBalaceAmount("2191", FIRST, subjectType); //预提费用
	   	    Double end_2191= subjectRestDatService.getBalaceAmount("2191", REST, subjectType); //预提费用
	   	    Double first_2201= subjectRestDatService.getBalaceAmount("2201", FIRST, subjectType); //待转资产价值
	   	    Double end_2201= subjectRestDatService.getBalaceAmount("2201", REST, subjectType); //待转资产价
	   	    Double first_2211= subjectRestDatService.getBalaceAmount("2211", FIRST, subjectType); //预计负债
	   	    Double end_2211= subjectRestDatService.getBalaceAmount("2211", REST, subjectType); //预计负债
	   	    Double first_2331= subjectRestDatService.getBalaceAmount("2331", FIRST, subjectType); //专项应付款
	   	    Double end_2331= subjectRestDatService.getBalaceAmount("2331", REST, subjectType); //专项应付款
	   	    Double first_2341= subjectRestDatService.getBalaceAmount("2341", FIRST, subjectType); //递延税款
	   	    Double end_2341= subjectRestDatService.getBalaceAmount("2341", REST, subjectType); //递延税
	   	    
	   	    beans.put("balance.first.4500", first_1711+first_1712+first_1801+first_1811+first_1901);
	   	    beans.put("balance.end.4500", end_1711+end_1712+end_1801+end_1811+end_1901);
	   	    
	   	    //非流动资产合计
			Double first_34 = Double.valueOf(beans.get("balance.first.34").toString());
			Double first_32 = Double.valueOf( beans.get("balance.first.32").toString());
			Double first_39 = Double.valueOf(beans.get("balance.first.39").toString());
			Double first_40 = Double.valueOf( beans.get("balance.first.40").toString());
			Double first_43 = Double.valueOf(beans.get("balance.first.43").toString());
			Double first_45 = Double.valueOf( beans.get("balance.first.45").toString());
			Double first_44 = Double.valueOf( beans.get("balance.first.44").toString());
			Double first_46 = Double.valueOf( beans.get("balance.first.46").toString());
			Double first_51 = Double.valueOf( beans.get("balance.first.51").toString());
			Double first_52 = Double.valueOf( beans.get("balance.first.52").toString());
			
			Double end_34 = Double.valueOf( beans.get("balance.end.34").toString());
			Double end_32 = Double.valueOf( beans.get("balance.end.32").toString());
			Double end_39 = Double.valueOf( beans.get("balance.end.39").toString());
			Double end_40 = Double.valueOf( beans.get("balance.end.40").toString());
			Double end_43 = Double.valueOf( beans.get("balance.end.43").toString());
			Double end_45 = Double.valueOf( beans.get("balance.end.45").toString());
			Double end_44 = Double.valueOf( beans.get("balance.end.44").toString());
			Double end_46 = Double.valueOf( beans.get("balance.end.46").toString());
			Double end_51 = Double.valueOf( beans.get("balance.end.51").toString());
			Double end_52 = Double.valueOf( beans.get("balance.end.52").toString());
			
	   
			
			Double first_2900=first_34+first_32+first_39+first_40+first_43+first_45+first_44+first_46+first_51+first_52+first_1711+first_1712+first_1801+first_1811+first_1901;
			Double end_2900=end_34+end_32+end_39+end_40+end_43+end_45+end_44+end_46+end_51+end_52+end_1711+end_1712+end_1801+end_1811+end_1901;
	  	
	   	    
	   	   
		    beans.put("balance.first.2900", first_2900);
	   	    beans.put("balance.end.2900", end_2900);  

			// 非流动负债合计
			Double first_101 = Double.valueOf( beans.get("balance.first.101").toString());
			Double first_103 = Double.valueOf( beans.get("balance.first.103").toString());
			Double end_101 = Double.valueOf( beans.get("balance.end.101").toString());
			Double end_103 = Double.valueOf( beans.get("balance.end.103").toString());
	
		    beans.put("balance.first.4600", first_101+first_103+first_1711+first_1712+first_1801+first_1811+first_1901);
	   	    beans.put("balance.end.4600", end_101+end_103+end_1711+end_1712+end_1801+end_1811+end_1901);
		
	    	   
				subjectRestDatService.downLoadFile(response,exportTemp,beans,vo.getCompanyName());
	    	   
	    	   
	    }
	    
	    }
	
	
	
	
	@ApiOperation(value = "导出利润表")
	@PostMapping("/exportProfit")
	public void exportProfit(HttpServletResponse response, HttpServletRequest request, @ModelAttribute SubjectRestVo vo)
			throws Exception {
		Result<String> result=camsCompanyService.getCompany(request, TokenTypeEnum.COOKIES);
		String companyName="";
		if(result.getCode().equals("1")){
			companyName=result.getData();
		}
		Map<String, String[]> map = request.getParameterMap();
		String openBeginTimeStr = map.get("openBeginTime")[0];
		String openEndTimeStr = map.get("openEndTime")[0];
		String openBeginTime = null;
		String openEndTime = null;
	
		CamsCompany company=camsCompanyService.selectOne(new EntityWrapper<CamsCompany>().eq("company_name", companyName));
		if (!StringUtil.isEmpty(openBeginTimeStr)) {
			openBeginTime = DateUtil.formatDate(new Date(openBeginTimeStr));
		}
		if (!StringUtil.isEmpty(openEndTimeStr)) {
			openEndTime =DateUtil.formatDate(new Date(openEndTimeStr));
		}
		vo.setBeginDate(DateUtil.getDate(openBeginTime));
		vo.setEndDate(DateUtil.getDate(openEndTime));
		if(vo.getBeginDate()==null) {
			vo.setBeginDate(DateUtil.getYearFirst(DateUtil.getYear(new Date())));
		}
		if(vo.getEndDate()==null) {
			vo.setEndDate(DateUtil.getLastEndDate());
		}
		vo.setCompanyName(companyName);
		vo.setPage(1);
		vo.setLimit(2000);
		Page<BalanceVo> page =subjectRestDatService.selectProfit(vo);
	    List<BalanceVo>	list=page.getRecords();
	    Map<Integer, List<BalanceVo>> rowMap=list.stream().collect(Collectors.groupingBy(BalanceVo::getRowNum));
	    Map<String,Object> beans=new HashMap<String,Object>();
	    beans.put("public.author", company.getCompanyOwner());
	    beans.put("public.companyNo", company.getCompanyNo());
	    beans.put("public.companyName", vo.getCompanyName());
	    beans.put("public.beginYear", DateUtil.getYear(vo.getBeginDate()));
	    beans.put("public.beginMonth", DateUtil.getMonth(vo.getBeginDate()));
	    beans.put("public.beginDay", DateUtil.getDay(vo.getBeginDate()));
	    beans.put("public.endYear", DateUtil.getYear(vo.getEndDate()));
	    beans.put("public.endMonth", DateUtil.getMonth(vo.getEndDate()));
	    beans.put("public.endDay", DateUtil.getDay(vo.getEndDate()));
	    String exportTemp=CamsConstant.COMPANY_TEMP;
	    if(company.getCompanyRule().equals(CamsConstant.CompanyRuleEnum.QI_YE.getValue().toString())) {
		    for(Map.Entry<Integer, List<BalanceVo>> entry : rowMap.entrySet()) {
		    	List<BalanceVo> temp=rowMap.get(entry.getKey());
		    	if(!CollectionUtils.isEmpty(temp)) {
		    		String firstKey="profit.first.";
		    		String endKey="profit.end.";
		    		firstKey=firstKey+entry.getKey();
		    		endKey=endKey+entry.getKey();
		    		beans.put(firstKey,temp.get(0).getFirstAmount());
		    		beans.put(endKey, temp.get(0).getRestAmount());
		    	}
		    }
			subjectRestDatService.downLoadFile(response,exportTemp,beans,vo.getCompanyName());
	    }else {
	    	exportTemp=CamsConstant.SMALL_COMPANY_TEMP;
	        for(Map.Entry<Integer, List<BalanceVo>> entry : rowMap.entrySet()) {
		    	List<BalanceVo> temp=rowMap.get(entry.getKey());
		    	if(!CollectionUtils.isEmpty(temp)) {
		    		String firstKey="profit.first.";
		    		String endKey="profit.end.";
		    		firstKey=firstKey+entry.getKey();
		    		endKey=endKey+entry.getKey();
		    		beans.put(firstKey,temp.get(0).getFirstAmount());
		    		beans.put(endKey, temp.get(0).getRestAmount());
		    	}
		    }
			subjectRestDatService.downLoadFile(response,exportTemp,beans,vo.getCompanyName());
	    }


	}
}

