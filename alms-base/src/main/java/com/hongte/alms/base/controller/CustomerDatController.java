package com.hongte.alms.base.controller;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.jni.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.BankIncomeDat;
import com.hongte.alms.base.entity.CamsCompany;
import com.hongte.alms.base.entity.CamsProductProperties;
import com.hongte.alms.base.entity.CamsSubject;
import com.hongte.alms.base.entity.CamsTax;
import com.hongte.alms.base.entity.CustomerDat;
import com.hongte.alms.base.entity.ProductDat;
import com.hongte.alms.base.entity.SellDat;
import com.hongte.alms.base.entity.SysUserRole;
import com.hongte.alms.base.enums.SubjectEnum;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.service.CustomerDatService;
import com.hongte.alms.base.service.ProductDatService;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.CamsUtil;
import com.hongte.alms.common.util.Convert;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.JsonUtil;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.common.vo.PageResult;

import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 客户与供应商表 前端控制器
 * </p>
 *
 * @author czs
 * @since 2019-01-20
 */
@RestController
@RequestMapping("/customerDat")
public class CustomerDatController {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomerDatController.class);

	@Autowired
	@Qualifier("CustomerDatService")
	private CustomerDatService customerDatService;
	
	
	@Autowired
	@Qualifier("productDatService")
	private ProductDatService productDatService;
	
	
	
	
    @ApiOperation(value = "查询客户与供应商列表")
    @RequestMapping("/search")
    public PageResult search(@RequestBody Page<CustomerDat> page) {
        // Map<String,Object> searchParams = Servlets.getParametersStartingWith(request,
        // "search_");
        // page = new Page<>(layTableQuery.getPage(), layTableQuery.getLimit());
        // departmentBankService.selectPage()
//    	page.getCondition().put("EQ_is_del", 0);
    	String GE_open_date=(String) page.getCondition().get("GE_open_date");
    	String LE_open_date=(String) page.getCondition().get("LE_open_date");
    	if(StringUtil.isEmpty(GE_open_date)) {
    		 page.getCondition().put("GE_open_date", DateUtil.getLastFirstDate());
    	}
    	if(StringUtil.isEmpty(LE_open_date)) {
   		 page.getCondition().put("LE_open_date", DateUtil.getLastEndDate());
   	    }
        page.setOrderByField("customerCode").setOrderByField("companyCode").setAsc(true);
        customerDatService.selectByPage(page);
        return PageResult.success(page.getRecords(), page.getTotal());
    }
    
	@ApiOperation(value = "获取所有单位")
	@RequestMapping("/findAllCustomer")
	public Result findAll(@RequestBody BankIncomeDat dat) {
		LOGGER.info("@findAll@获取所有单位--开始[]");
		Result result = null;
		Map<String, JSONArray> retMap = new HashMap<String, JSONArray>();
		List<CustomerDat> list=customerDatService.selectList(new EntityWrapper<CustomerDat>().eq("company_code", dat.getCompanyName()));
		retMap.put("customers", (JSONArray) JSON.toJSON(list, JsonUtil.getMapping()));
		result = Result.success(retMap);
		LOGGER.info("@findAll@获取所有单位--结束[{}]", result);
		return result;
	}
	
	@ApiOperation(value = "导入往来单位excel")
	@RequestMapping("/importCustomerFlowExcel")
	public Result importCustomerFlowExcel(@RequestParam("file") MultipartFile file, HttpServletRequest request,
			MultipartRequest req) {
		Result result = null;
		try {
			Map<String, String[]> map = request.getParameterMap();
			String companyName = map.get("companyName")[0];
           if(StringUtil.isEmpty(companyName)) {
        	   return Result.error("请选择公司名");
           }
       
           if(!file.getOriginalFilename().contains(companyName)) {
        	   return Result.error("选择的公司与导入的公司不一致");
           }
   
			LOGGER.info("====>>>>>导入往来单位excel开始[{}]", file);
			customerDatService.importCustomerDat(file, companyName);

			result = Result.success();
		} catch (ServiceRuntimeException se) {
			result = Result.error(se.getErrorCode(), se.getMessage());
			LOGGER.error("====>>>>>导入往来单位出错{}", se.getMessage());
		} catch (Exception e) {
			result = Result.error("500", "导入出错");
			LOGGER.error("====>>>>>导入往来单位出错{}", e);
		}
		LOGGER.info("====>>>>>导入往来单位excel结束");
		return result;
	}
	
	@ApiOperation(value = "删除")
	@RequestMapping("/delete")
	public Result delete(@RequestBody List<CustomerDat> customerDats) {
		try {
			for(CustomerDat dat:customerDats) {
				customerDatService.delete(new EntityWrapper<CustomerDat>().eq("customer_code", dat.getCustomerCode()).eq("company_code", dat.getCompanyCode()));
			}
			
			return Result.success();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return Result.error(e.getMessage());
		}
	}
	
	

	public String fileName="10001_test.dat";//文件名称
	public String urlPath="D:/datPath";//文件存放路径
	
	
	
	   @ApiOperation(value = "查询供应商列表")
	   @PostMapping("/export")
	    public void export(HttpServletResponse response,HttpServletRequest request,@ModelAttribute CustomerDat customertDat ) throws IOException {
	        // Map<String,Object> searchParams = Servlets.getParametersStartingWith(request,
	        // "search_");
	        // page = new Page<>(layTableQuery.getPage(), layTableQuery.getLimit());
	        // departmentBankService.selectPage()
//	    	page.getCondition().put("EQ_is_del", 0);
//		   page.setSize(10000);
//	        page.setOrderByField("companyName").setOrderByField("productCode").setOrderByField("createTime").setAsc(false);
//	       page= productDatService.selectByPage(page);
//	       List<ProductDat> dats=page.getRecords();
			Map<String, String[]> map =request.getParameterMap();
    		String beginTimeStr=map.get("beginTime")[0];
    		String endTimeStr=map.get("endTime")[0];
    		String openBeginTimeStr = map.get("openBeginTime")[0];
    		String openEndTimeStr = map.get("openEndTime")[0];
    		String beginDate=null;
    		String endDate=null;
    		String openBeginTime=null;
    		String openEndTime=null;
    		if (!StringUtil.isEmpty(beginTimeStr)) {
    			beginDate = DateUtil.formatDate(new Date(beginTimeStr));
    		}
    		if (!StringUtil.isEmpty(endTimeStr)) {
    			endDate = DateUtil.formatDate(new Date(endTimeStr));
    		}
    		if (!StringUtil.isEmpty(openBeginTimeStr)) {
    			openBeginTime = DateUtil.formatDate(new Date(openBeginTimeStr));
    		}
    		if (!StringUtil.isEmpty(openEndTimeStr)) {
    			openEndTime = DateUtil.formatDate(new Date(openEndTimeStr));
    		}
			 Wrapper<CustomerDat> wrapperCustomerDat= new EntityWrapper<CustomerDat>();
			 if(!StringUtil.isEmpty(customertDat.getCompanyCode())) {
				 wrapperCustomerDat.eq("company_code", customertDat.getCompanyCode());
			 }
			 if(!StringUtil.isEmpty(customertDat.getCustomerCode())) {
				 wrapperCustomerDat.eq("customer_code", customertDat.getCustomerCode());
			 }
			 if(!StringUtil.isEmpty(customertDat.getCustomerName())) {
				 wrapperCustomerDat.eq("customer_name", customertDat.getCustomerName());
			 }
			 if(beginDate!=null) {
				 wrapperCustomerDat.ge("create_time", beginDate);
			 }
			 if(endDate!=null) {
				 wrapperCustomerDat.le("create_time", endDate);
			 }
			if (!StringUtil.isEmpty(openBeginTime)) {
				wrapperCustomerDat.ge("open_date", openBeginTime);
			}
			if (!StringUtil.isEmpty(openEndTime)) {
				wrapperCustomerDat.le("open_date", openEndTime);
			}
			 
			 List<CustomerDat> dats= customerDatService.selectList(wrapperCustomerDat);
	       
	       response.setContentType("dat/plain");
	       response.setCharacterEncoding("UTF-8");
           String fileName="往来单位Customer";
           response.setHeader("Content-Disposition","attachment; filename=" + URLEncoder.encode(fileName, "UTF-8")+ ".dat");//导出中文名
           BufferedOutputStream buff = null;
    	   ServletOutputStream outSTr = null;
    	   outSTr = response.getOutputStream();// 建立     
    	   buff = new BufferedOutputStream(outSTr); 
			StringBuffer strTemp=new StringBuffer();
	       try {
	    	
				for(int i=0;i<dats.size();i++){//模拟文件中的内容
					strTemp.delete(0, strTemp.length());
					strTemp.append(dats.get(i).getCustomerCode()==null?"":dats.get(i).getCustomerCode()+"	");
					strTemp.append(dats.get(i).getCustomerName()==null?"":dats.get(i).getCustomerName()+"	");
					strTemp.append(dats.get(i).getCustomerType()==null?"":dats.get(i).getCustomerType()+"	");
					strTemp.append(dats.get(i).getCustomerCategory()==null?"":dats.get(i).getCustomerCategory()+"		");
					strTemp.append(dats.get(i).getFuKuanTiaoJian()==null?"":dats.get(i).getFuKuanTiaoJian()+"\t");
					strTemp.append(dats.get(i).getXinYongEDu()==null?"":dats.get(i).getXinYongEDu()+"	");
					strTemp.append(dats.get(i).getZheKouLv()==null?"":dats.get(i).getZheKouLv()+"												 	");
					strTemp.append(dats.get(i).getYuanGong()==null?"":dats.get(i).getYuanGong()+"\t");
					strTemp.append(dats.get(i).getDiQu()==null?"":dats.get(i).getDiQu()+"\t");
					strTemp.append(dats.get(i).getLianXiRen()==null?"":dats.get(i).getLianXiRen()+"\t");
					strTemp.append(dats.get(i).getZhiWu()==null?"":dats.get(i).getZhiWu()+"\t");
					strTemp.append(dats.get(i).getDianHua1()==null?"":dats.get(i).getDianHua1()+"\t");
					strTemp.append(dats.get(i).getDianHua2()==null?"":dats.get(i).getDianHua2()+"\t");
					strTemp.append(dats.get(i).getChuanZhen()==null?"":dats.get(i).getChuanZhen()+"\t");
					strTemp.append(dats.get(i).getDianZiYouJian()==null?"":dats.get(i).getDianZiYouJian()+"\t");
					strTemp.append(dats.get(i).getShuiHao()==null?"":dats.get(i).getShuiHao()+"\t");
					strTemp.append(dats.get(i).getDiZhi()==null?"":dats.get(i).getDiZhi()+"\t");
					strTemp.append(dats.get(i).getYouBian()==null?"":dats.get(i).getYouBian()+"\t");
					strTemp.append(dats.get(i).getBeiZhu()==null?"":dats.get(i).getBeiZhu()+"\t");
					strTemp.append(dats.get(i).getYingshouCategory()==null?"":dats.get(i).getYingshouCategory()+"	");
					strTemp.append(dats.get(i).getYingfuCategory()==null?"":dats.get(i).getYingfuCategory()+"			");
					strTemp.append(dats.get(i).getYingshouZhekouCategory()==null?"":dats.get(i).getYingshouZhekouCategory()+"\t");
					strTemp.append(dats.get(i).getYingfuZhekouCategory()==null?"":dats.get(i).getYingfuZhekouCategory()+"");
					strTemp.append(dats.get(i).getKaiHuRiQi()==null?"":dats.get(i).getKaiHuRiQi()+"		");
					strTemp.append(dats.get(i).getGuanBiRiQi()==null?"":dats.get(i).getGuanBiRiQi()+"	");
					strTemp.append(dats.get(i).getTingYongBiaoZhi()==null?"":dats.get(i).getTingYongBiaoZhi()+"\t");
					strTemp.append(dats.get(i).getTiexiZhekouBiaoZhi()==null?"":dats.get(i).getTiexiZhekouBiaoZhi()+"\t");
					strTemp.append(dats.get(i).getJishiTongxunHao()==null?"":dats.get(i).getJishiTongxunHao()+"		");
					strTemp.append(dats.get(i).getXieTongSheZhi()==null?"":dats.get(i).getXieTongSheZhi()+"\t");
					strTemp.append(dats.get(i).getJiaGeDengJi()==null?"":dats.get(i).getJiaGeDengJi()+"\t");
					strTemp.append(dats.get(i).getShouJi()==null?"":dats.get(i).getShouJi()+"\t");
					strTemp.append(dats.get(i).getYingYeZhiZhao()==null?"":dats.get(i).getYingYeZhiZhao()+"\t");
					strTemp.append(dats.get(i).getYaoYeXuKe()==null?"":dats.get(i).getYaoYeXuKe()+"\t");
					strTemp.append(dats.get(i).getYaoYeHeGe()==null?"":dats.get(i).getYaoYeHeGe()+"\t");
					strTemp.append(dats.get(i).getQiYeWangZhan()==null?"":dats.get(i).getQiYeWangZhan()+"\t");
					strTemp.append("\r\n");
					buff.write(strTemp.toString().getBytes("GBK"));
				}
				System.out.println("生成文件成功");
		         buff.flush();     
	                buff.close();  
			} catch (Exception e) {
			
			}finally{
				  buff.close();
				  outSTr.close(); 
			}
	       
	       
	    }
	
	
	
	
	
	
	
	
	@GetMapping("/dat")
	@ResponseBody
	public void create() throws IOException {
	List<CustomerDat> dats=	customerDatService.selectList(new EntityWrapper<CustomerDat>());
	
		String  FileNamePath = urlPath + "/" + fileName;
		FileOutputStream fis=new FileOutputStream(FileNamePath); 
		OutputStreamWriter out = new OutputStreamWriter(fis,"GBK");
		try {
			StringBuffer strTemp=new StringBuffer();
			for(int i=0;i<dats.size();i++){//模拟文件中的内容
				strTemp.delete(0, strTemp.length());
				strTemp.append(dats.get(i).getCustomerCode()==null?"":dats.get(i).getCustomerCode()+"	");
				strTemp.append(dats.get(i).getCustomerName()==null?"":dats.get(i).getCustomerName()+"	");
				strTemp.append(dats.get(i).getCustomerType()==null?"":dats.get(i).getCustomerType()+"	");
				strTemp.append(dats.get(i).getCustomerCategory()==null?"":dats.get(i).getCustomerCategory()+"		");
				strTemp.append(dats.get(i).getFuKuanTiaoJian()==null?"":dats.get(i).getFuKuanTiaoJian()+"\t");
				strTemp.append(dats.get(i).getXinYongEDu()==null?"":dats.get(i).getXinYongEDu()+"	");
				strTemp.append(dats.get(i).getZheKouLv()==null?"":dats.get(i).getZheKouLv()+"												 	");
				strTemp.append(dats.get(i).getYuanGong()==null?"":dats.get(i).getYuanGong()+"\t");
				strTemp.append(dats.get(i).getDiQu()==null?"":dats.get(i).getDiQu()+"\t");
				strTemp.append(dats.get(i).getLianXiRen()==null?"":dats.get(i).getLianXiRen()+"\t");
				strTemp.append(dats.get(i).getZhiWu()==null?"":dats.get(i).getZhiWu()+"\t");
				strTemp.append(dats.get(i).getDianHua1()==null?"":dats.get(i).getDianHua1()+"\t");
				strTemp.append(dats.get(i).getDianHua2()==null?"":dats.get(i).getDianHua2()+"\t");
				strTemp.append(dats.get(i).getChuanZhen()==null?"":dats.get(i).getChuanZhen()+"\t");
				strTemp.append(dats.get(i).getDianZiYouJian()==null?"":dats.get(i).getDianZiYouJian()+"\t");
				strTemp.append(dats.get(i).getShuiHao()==null?"":dats.get(i).getShuiHao()+"\t");
				strTemp.append(dats.get(i).getDiZhi()==null?"":dats.get(i).getDiZhi()+"\t");
				strTemp.append(dats.get(i).getYouBian()==null?"":dats.get(i).getYouBian()+"\t");
				strTemp.append(dats.get(i).getBeiZhu()==null?"":dats.get(i).getBeiZhu()+"\t");
				strTemp.append(dats.get(i).getYingshouCategory()==null?"":dats.get(i).getYingshouCategory()+"	");
				strTemp.append(dats.get(i).getYingfuCategory()==null?"":dats.get(i).getYingfuCategory()+"			");
				strTemp.append(dats.get(i).getYingshouZhekouCategory()==null?"":dats.get(i).getYingshouZhekouCategory()+"\t");
				strTemp.append(dats.get(i).getYingfuZhekouCategory()==null?"":dats.get(i).getYingfuZhekouCategory()+"");
				strTemp.append(dats.get(i).getKaiHuRiQi()==null?"":dats.get(i).getKaiHuRiQi()+"		");
				strTemp.append(dats.get(i).getGuanBiRiQi()==null?"":dats.get(i).getGuanBiRiQi()+"	");
				strTemp.append(dats.get(i).getTingYongBiaoZhi()==null?"":dats.get(i).getTingYongBiaoZhi()+"\t");
				strTemp.append(dats.get(i).getTiexiZhekouBiaoZhi()==null?"":dats.get(i).getTiexiZhekouBiaoZhi()+"\t");
				strTemp.append(dats.get(i).getJishiTongxunHao()==null?"":dats.get(i).getJishiTongxunHao()+"		");
				strTemp.append(dats.get(i).getXieTongSheZhi()==null?"":dats.get(i).getXieTongSheZhi()+"\t");
				strTemp.append(dats.get(i).getJiaGeDengJi()==null?"":dats.get(i).getJiaGeDengJi()+"\t");
				strTemp.append(dats.get(i).getShouJi()==null?"":dats.get(i).getShouJi()+"\t");
				strTemp.append(dats.get(i).getYingYeZhiZhao()==null?"":dats.get(i).getYingYeZhiZhao()+"\t");
				strTemp.append(dats.get(i).getYaoYeXuKe()==null?"":dats.get(i).getYaoYeXuKe()+"\t");
				strTemp.append(dats.get(i).getYaoYeHeGe()==null?"":dats.get(i).getYaoYeHeGe()+"\t");
				strTemp.append(dats.get(i).getQiYeWangZhan()==null?"":dats.get(i).getQiYeWangZhan()+"\t");
				strTemp.append("\r\n");
				out.write(strTemp.toString());
			}
			out.flush(); 
			out.close();
			fis.close();
			System.out.println("生成文件"+FileNamePath+"成功");
			
		} catch (Exception e) {
			if(fis!=null)
				fis.close();
			if(out!=null){
				out.close();
			}
			e.printStackTrace();
		}finally{
			try{
				if(fis!=null)
					fis.close();// 关闭文件流
				if(out!=null)
					out.close();
			}catch (Exception e) {
				System.out.println("CreateDayFiles关闭文件流失败");
			}
		}

	}
       public static void main(String[] args) {
    	   String str1="异硬脂醇异硬脂酸酯 DUB ISIS(ISOSTEARATE D'ISOSTEARYLE)";
    	   String str2="异硬脂醇异硬脂酸酯DUB ISIS（ISOSTEARATE DˊISOSTEARYLE)";
    	   str1=CamsUtil.getTempProductName(str1);
    	   str2=CamsUtil.getTempProductName(str2);
    	   System.out.println(str1);
    	   System.out.println(str2);
    	   System.out.println(str1.equals(str2));
		
	}
}
