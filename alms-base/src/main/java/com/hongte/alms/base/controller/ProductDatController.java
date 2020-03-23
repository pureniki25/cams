package com.hongte.alms.base.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.CustomerDat;
import com.hongte.alms.base.entity.ProductDat;
import com.hongte.alms.base.entity.SellDat;
import com.hongte.alms.base.entity.TempProductDat;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.service.CustomerDatService;
import com.hongte.alms.base.service.ProductDatService;
import com.hongte.alms.base.service.TempProductDatService;
import com.hongte.alms.base.vo.cams.RestProductVo;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.common.vo.PageResult;

import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 产品表 前端控制器
 * </p>
 *
 * @author czs
 * @since 2019-01-26
 */
@RestController
@RequestMapping("/productDat")
public class ProductDatController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ProductDatController.class);
	@Autowired
	@Qualifier("ProductDatService")
	private ProductDatService productDatService;

	@Autowired
	@Qualifier("TempProductDatService")
	private TempProductDatService tempProductDatService;

	public String fileName = "product.dat";// 文件名称
	public String urlPath = "D:/datPath";// 文件存放路径

	@ApiOperation(value = "查询产品列表")
	@RequestMapping("/search")
	public PageResult search(@RequestBody Page<ProductDat> page) {
		// Map<String,Object> searchParams = Servlets.getParametersStartingWith(request,
		// "search_");
		// page = new Page<>(layTableQuery.getPage(), layTableQuery.getLimit());
		// departmentBankService.selectPage()
		// page.getCondition().put("EQ_is_del", 0);

		page.setOrderByField("productCode").setOrderByField("company_name").setAsc(true);
		productDatService.selectByPage(page);
		return PageResult.success(page.getRecords(), page.getTotal());
	}

	@ApiOperation(value = "库存余额表查询")
	@RequestMapping("/inventoryPage")
	public PageResult<List<RestProductVo>> inventoryPage(@RequestBody RestProductVo vo) {
		Page<RestProductVo> pages = productDatService.inventoryPage(vo);
		return PageResult.success(pages.getRecords(), pages.getTotal());
	}

	@ApiOperation(value = "导入商品excel")
	@RequestMapping("/importProductFlowExcel")
	public Result importProductFlowExcel(@RequestParam("file") MultipartFile file, HttpServletRequest request,
			MultipartRequest req) {
		Result result = null;
		try {
			Map<String, String[]> map = request.getParameterMap();
			String companyName = map.get("companyName")[0];
			String productPropertiesName = map.get("productPropertiesName")[0];
			String importType = map.get("importType")[0]; // 导入类型 1：往来单位 2：商品期初
			if (StringUtil.isEmpty(companyName)) {
				return Result.error("请选择公司名");
			}
			if (StringUtil.isEmpty(importType)) {
				return Result.error("导入类型");
			}
			if (!file.getOriginalFilename().contains(companyName)) {
				return Result.error("选择的公司与导入的公司不一致");
			}

			LOGGER.info("====>>>>>导入往来商品excel开始[{}]", file);
			if (importType.equals("1")) {
				productDatService.importProductDat(file, companyName, productPropertiesName);
			} else {
				productDatService.updateKuCunLiang(file, companyName);
			}

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
	
	
	
	@ApiOperation(value = "导入库存期初余额")
	@RequestMapping("/importProductRestExcel")
	public Result importProductRestExcel(@RequestParam("file") MultipartFile file, HttpServletRequest request,
			MultipartRequest req) {
		Result result = null;
		try {
			Map<String, String[]> map = request.getParameterMap();
			String companyName = map.get("companyName")[0];
			if (StringUtil.isEmpty(companyName)) {
				return Result.error("请选择公司名");
			}
		
			LOGGER.info("====>>>>>导入库存期初余额[{}]", file);
				productDatService.updateKuCunLiang(file, companyName);

			result = Result.success();
		} catch (ServiceRuntimeException se) {
			result = Result.error(se.getErrorCode(), se.getMessage());
			LOGGER.error("====>>>>>导入库存期初余额{}", se.getMessage());
		} catch (Exception e) {
			result = Result.error("500", "导入出错");
			LOGGER.error("====>>>>>导入库存期初余额{}", e);
		}
		LOGGER.info("====>>>>>导入库存期初余额excel结束");
		return result;
	}

	@ApiOperation(value = "删除")
	@RequestMapping("/delete")
	public Result delete(@RequestBody List<ProductDat> productDats) {
		try {
			for (ProductDat dat : productDats) {
				productDatService.delete(new EntityWrapper<ProductDat>().eq("product_code", dat.getProductCode())
						.eq("company_name", dat.getCompanyName()));
				tempProductDatService.delete(new EntityWrapper<TempProductDat>()
						.eq("product_code", dat.getProductCode()).eq("company_name", dat.getCompanyName()));
			}

			return Result.success();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return Result.error(e.getMessage());
		}
	}

	@ApiOperation(value = "导出产品列表")
	@PostMapping("/export")
	public void export(HttpServletResponse response, HttpServletRequest request, @ModelAttribute ProductDat productDat)
			throws IOException {
		// Map<String,Object> searchParams = Servlets.getParametersStartingWith(request,
		// "search_");
		// page = new Page<>(layTableQuery.getPage(), layTableQuery.getLimit());
		// departmentBankService.selectPage()
		// page.getCondition().put("EQ_is_del", 0);
		// page.setSize(10000);
		// page.setOrderByField("companyName").setOrderByField("produuctCode").setOrderByField("createTime").setAsc(false);
		// page= productDatService.selectByPage(page);
		// List<ProductDat> dats=page.getRecords();
		Map<String, String[]> map = request.getParameterMap();
		String beginTimeStr = map.get("beginTime")[0];
		String endTimeStr = map.get("endTime")[0];
		String openBeginTimeStr = map.get("openBeginTime")[0];
		String openEndTimeStr = map.get("openEndTime")[0];
		String beginDate = null;
		String endDate = null;
		String openBeginTime = null;
		String openEndTime = null;
		if (beginTimeStr != null) {
			beginDate = DateUtil.formatDate(new Date(beginTimeStr));
		}
		if (endTimeStr != null) {
			endDate = DateUtil.formatDate(new Date(endTimeStr));
		}
		if (!StringUtil.isEmpty(openBeginTimeStr)) {
			openBeginTime = DateUtil.formatDate(new Date(openBeginTimeStr));
		}
		if (!StringUtil.isEmpty(openEndTimeStr)) {
			openEndTime = DateUtil.formatDate(new Date(openEndTimeStr));
		}
		Wrapper<ProductDat> wrapperProductDat = new EntityWrapper<ProductDat>();
		if (!StringUtil.isEmpty(productDat.getCompanyName())) {
			wrapperProductDat.eq("company_name", productDat.getCompanyName());
		}
		if (!StringUtil.isEmpty(productDat.getProductCode())) {
			wrapperProductDat.eq("product_code", productDat.getProductCode());
		}
		if (!StringUtil.isEmpty(productDat.getProductName())) {
			wrapperProductDat.eq("product_name", productDat.getProductName());
		}
		if (beginDate != null) {
			wrapperProductDat.ge("create_time", beginDate);
		}
		if (endDate != null) {
			wrapperProductDat.le("create_time", endDate);
		}
		if (!StringUtil.isEmpty(openBeginTime)) {
			wrapperProductDat.ge("open_date", openBeginTime);
		}
		if (!StringUtil.isEmpty(openEndTime)) {
			wrapperProductDat.le("open_date", openEndTime);
		}

		List<ProductDat> dats = productDatService.selectList(wrapperProductDat);

		response.setContentType("dat/plain");
		String fileName = "产品Item";
		response.setHeader("Content-Disposition",
				"attachment; filename=" + URLEncoder.encode(fileName, "UTF-8") + ".dat");// 导出中文名
		BufferedOutputStream buff = null;
		ServletOutputStream outSTr = null;
		outSTr = response.getOutputStream();// 建立
		buff = new BufferedOutputStream(outSTr);
		StringBuffer strTemp = new StringBuffer();
		try {

			for (int i = 0; i < dats.size(); i++) {// 模拟文件中的内容
				strTemp.delete(0, strTemp.length());
				strTemp.append(
						dats.get(i).getProductCode() == null ? "" : dats.get(i).getProductCode().trim().trim() + "\t");
				strTemp.append(
						dats.get(i).getProductName() == null ? "" : dats.get(i).getProductName().trim().trim() + "\t");
				strTemp.append((dats.get(i).getProductType() == null ? "" : dats.get(i).getProductType().trim().trim())
						+ "\t");
				strTemp.append(dats.get(i).getFengcunBiaozhi() == null ? ""
						: dats.get(i).getFengcunBiaozhi().trim().trim() + "\t");
				strTemp.append(dats.get(i).getProductProperties() == null ? ""
						: dats.get(i).getProductProperties().trim().trim() + "\t");
				strTemp.append(dats.get(i).getProductCategory() == null ? ""
						: dats.get(i).getProductCategory().trim().trim() + "\t");
				strTemp.append(
						dats.get(i).getMinCalUnit() == null ? "" : dats.get(i).getMinCalUnit().trim().trim() + "\t");
				strTemp.append(
						dats.get(i).getRestCalUnit() == null ? "" : dats.get(i).getRestCalUnit().trim().trim() + "\t");
				strTemp.append(dats.get(i).getProductUnit() == null ? ""
						: dats.get(i).getProductUnit().trim().trim() + "				");
				strTemp.append(dats.get(i).getChanDi() == null ? "" : dats.get(i).getChanDi().trim() + "\t");
				strTemp.append(
						dats.get(i).getGongYingShang() == null ? "" : dats.get(i).getGongYingShang().trim() + "\t");
				strTemp.append(
						dats.get(i).getShangPinHuoHao() == null ? "" : dats.get(i).getShangPinHuoHao().trim() + "\t");
				strTemp.append(dats.get(i).getZuiXiaoKuCunLiang() == null ? ""
						: dats.get(i).getZuiXiaoKuCunLiang().trim() + "\t");
				strTemp.append(
						dats.get(i).getZuiDaKuCunLiang() == null ? "" : dats.get(i).getZuiDaKuCunLiang().trim() + "\t");
				strTemp.append(
						dats.get(i).getTiQianShiJian() == null ? "" : dats.get(i).getTiQianShiJian().trim() + "\t");
				strTemp.append(dats.get(i).getHanShuiCaiGouJia() == null ? ""
						: dats.get(i).getHanShuiCaiGouJia().trim() + "\t");
				strTemp.append(dats.get(i).getBuHanShuiCaiGouJia() == null ? ""
						: dats.get(i).getBuHanShuiCaiGouJia().trim() + "\t");
				strTemp.append(dats.get(i).getHanShuiXiaoShouJia() == null ? ""
						: dats.get(i).getHanShuiXiaoShouJia().trim() + "\t");
				strTemp.append(dats.get(i).getBuHanShuiXiaosShouJia() == null ? ""
						: dats.get(i).getBuHanShuiXiaosShouJia().trim() + "\t");
				strTemp.append(dats.get(i).getBuHanShuiJiHuaJia() == null ? ""
						: dats.get(i).getBuHanShuiJiHuaJia().trim() + "\t");
				strTemp.append(dats.get(i).getHanShuiLingShouJia() == null ? ""
						: dats.get(i).getHanShuiLingShouJia().trim() + "\t");
				strTemp.append(dats.get(i).getBaoZhiQi() == null ? "" : dats.get(i).getBaoZhiQi().trim() + "\t");
				strTemp.append(dats.get(i).getPiCiGuanLiBiaoZhi() == null ? ""
						: dats.get(i).getPiCiGuanLiBiaoZhi().trim() + "\t");
				strTemp.append(dats.get(i).getZuJianShangPinBiaoZhi() == null ? ""
						: dats.get(i).getZuJianShangPinBiaoZhi().trim() + "\t");
				strTemp.append(
						dats.get(i).getZuZhuangBiaoZhi() == null ? "" : dats.get(i).getZuZhuangBiaoZhi().trim() + "\t");
				strTemp.append(dats.get(i).getShouTuoShangPinBiaoZhi() == null ? ""
						: dats.get(i).getShouTuoShangPinBiaoZhi().trim() + "	 							");
				strTemp.append(dats.get(i).getBeiZhu() == null ? "" : dats.get(i).getBeiZhu().trim() + "\t");
				strTemp.append(
						dats.get(i).getZiDingXiangMu0() == null ? "" : dats.get(i).getZiDingXiangMu0().trim() + "\t");
				strTemp.append(
						dats.get(i).getZiDingXiangMu1() == null ? "" : dats.get(i).getZiDingXiangMu1().trim() + "\t");
				strTemp.append(
						dats.get(i).getZiDingXiangMu2() == null ? "" : dats.get(i).getZiDingXiangMu2().trim() + "\t");
				strTemp.append(
						dats.get(i).getZiDingXiangMu3() == null ? "" : dats.get(i).getZiDingXiangMu3().trim() + "\t");
				strTemp.append(
						dats.get(i).getZiDingXiangMu4() == null ? "" : dats.get(i).getZiDingXiangMu4().trim() + "\t");
				strTemp.append(
						dats.get(i).getZiDingXiangMu5() == null ? "" : dats.get(i).getZiDingXiangMu5().trim() + "\t");
				strTemp.append(dats.get(i).getKuCunLiang() == null ? "" : dats.get(i).getKuCunLiang().trim() + "\t");
				strTemp.append(dats.get(i).getBuHanShuiZuiDiXiaoShouJia() == null ? ""
						: dats.get(i).getBuHanShuiZuiDiXiaoShouJia().trim() + "\t");
				strTemp.append(dats.get(i).getBuHanXiaoShouJiaBuDaZheJine() == null ? ""
						: dats.get(i).getBuHanXiaoShouJiaBuDaZheJine().trim() + "\t");
				strTemp.append(
						dats.get(i).getCalUnit() == null ? "" : dats.get(i).getCalUnit().trim() + "					");
				strTemp.append(
						dats.get(i).getFaPiaoPinMing() == null ? "" : dats.get(i).getFaPiaoPinMing().trim() + "\t");
				strTemp.append(dats.get(i).getTiaoMa() == null ? "" : dats.get(i).getTiaoMa().trim() + "\t");
				strTemp.append(
						dats.get(i).getPiZhunWenHao() == null ? "" : dats.get(i).getPiZhunWenHao().trim() + "\t");
				strTemp.append(
						dats.get(i).getZhuCeShangBiao() == null ? "" : dats.get(i).getZhuCeShangBiao().trim() + "\t");
				strTemp.append(dats.get(i).getPackageUnit() == null ? "" : dats.get(i).getPackageUnit().trim() + "\t");
				strTemp.append(
						dats.get(i).getBaoZhuangGuiGe() == null ? "" : dats.get(i).getBaoZhuangGuiGe().trim() + "\t");
				strTemp.append(dats.get(i).getTiJiDanWei() == null ? "" : dats.get(i).getTiJiDanWei().trim() + "\t");
				strTemp.append(dats.get(i).getZhongLiangDanWei() == null ? ""
						: dats.get(i).getZhongLiangDanWei().trim() + "\t");
				strTemp.append(dats.get(i).getPiCiXiaoShouJiaJiaLv() == null ? ""
						: dats.get(i).getPiCiXiaoShouJiaJiaLv().trim() + "		");
				strTemp.append(
						dats.get(i).getShangPinMing() == null ? "" : dats.get(i).getShangPinMing().trim() + "\t");
				strTemp.append(dats.get(i).getZuiGaoCaiGouJia() == null ? ""
						: dats.get(i).getZuiGaoCaiGouJia().trim() + "		");
				strTemp.append(
						dats.get(i).getZuiDiCaiGouJia() == null ? "" : dats.get(i).getZuiDiCaiGouJia().trim() + "\t");
				strTemp.append(dats.get(i).getABCDengJi() == null ? "" : dats.get(i).getABCDengJi().trim() + "\t");
				strTemp.append(dats.get(i).getWangShangXiaoShouBiaoZhi() == null ? ""
						: dats.get(i).getWangShangXiaoShouBiaoZhi().trim() + "\t");
				System.out.println(strTemp.toString().length());
				strTemp.append("\r\n");
				buff.write(strTemp.toString().getBytes("GBK"));
			}
			System.out.println("生成文件成功");
			buff.flush();
			buff.close();
		} catch (Exception e) {

		} finally {
			buff.close();
			outSTr.close();
		}

	}

}
