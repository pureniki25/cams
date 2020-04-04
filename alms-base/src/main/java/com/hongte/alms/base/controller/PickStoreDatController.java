package com.hongte.alms.base.controller;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.BuyDat;
import com.hongte.alms.base.entity.CamsSubject;
import com.hongte.alms.base.entity.PickStoreDat;
import com.hongte.alms.base.entity.SellDat;
import com.hongte.alms.base.enums.CamsConstant;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.service.CamsSubjectService;
import com.hongte.alms.base.service.PickStoreDatService;
import com.hongte.alms.base.service.SellDatService;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.common.vo.PageResult;

import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 领料入库表 前端控制器
 * </p>
 *
 * @author czs
 * @since 2019-07-22
 */
@RestController
@RequestMapping("/pickStoreDatController")
public class PickStoreDatController {

	private static final Logger LOGGER = LoggerFactory.getLogger(PickStoreDatController.class);

	@Autowired
	@Qualifier("PickStoreDatService")
	private PickStoreDatService pickStoreDatService;

	@Autowired
	@Qualifier("CamsSubjectService")
	private CamsSubjectService camsSubjectService;

	@ApiOperation(value = "导入领料excel")
	@RequestMapping("/importPickStoreExcel")
	public Result importPickExcel(@RequestParam("file") MultipartFile file, HttpServletRequest request,
			MultipartRequest req) {
		Result result = null;
		try {
			Map<String, String[]> map = request.getParameterMap();
			String companyName = map.get("companyName")[0];
			if (!file.getOriginalFilename().contains(companyName)) {
				return Result.error("选择的公司与导入的公司不一致");
			}
			String openTime = map.get("openDate")[0];
			String subjectId = map.get("subjectName")[0];
			CamsSubject subject = camsSubjectService.selectOne(new EntityWrapper<CamsSubject>().eq("id", subjectId));
			Date date = new Date(openTime);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cale = Calendar.getInstance();
			cale.setTime(date);
			// 获取前月的最后一天
			cale.add(Calendar.MONTH, 1);
			cale.set(Calendar.DAY_OF_MONTH, 0);
			String lastday = format.format(cale.getTime());
			LOGGER.info("====>>>>>导入领料excel开始[{}]", file);
			pickStoreDatService.importPick(file, companyName, "1", lastday, subject);

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

	@ApiOperation(value = "导入领料excel")
	@RequestMapping("/importStoreExcel")
	public Result importStoreExcel(@RequestParam("file") MultipartFile file, HttpServletRequest request,
			MultipartRequest req) {
		Result result = null;
		try {
			Map<String, String[]> map = request.getParameterMap();
			String companyName = map.get("companyName")[0];
			if (!file.getOriginalFilename().contains(companyName)) {
				return Result.error("选择的公司与导入的公司不一致");
			}
			String openTime = map.get("openDate")[0];
			String subjectId = map.get("subjectName")[0];
			CamsSubject subject = camsSubjectService.selectOne(new EntityWrapper<CamsSubject>().eq("id", subjectId));
			Date date = new Date(openTime);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cale = Calendar.getInstance();
			cale.setTime(date);
			// 获取前月的最后一天
			cale.add(Calendar.MONTH, 1);
			cale.set(Calendar.DAY_OF_MONTH, 0);
			String lastday = format.format(cale.getTime());
			LOGGER.info("====>>>>>导入领料excel开始[{}]", file);
			pickStoreDatService.importPick(file, companyName, "2", lastday, subject);

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

	@ApiOperation(value = "查询领料单列表")
	@RequestMapping("/searchPick")
	public PageResult searchPick(@RequestBody Page<PickStoreDat> page) {
		// Map<String,Object> searchParams = Servlets.getParametersStartingWith(request,
		// "search_");
		// page = new Page<>(layTableQuery.getPage(), layTableQuery.getLimit());
		// departmentBankService.selectPage()
		// page.getCondition().put("EQ_is_del", 0);
		page.getCondition().put("EQ_pick_store_type", 1);
		page.setOrderByField("createTime").setAsc(false);
		pickStoreDatService.selectByPage(page);
		return PageResult.success(page.getRecords(), page.getTotal());
	}

	@ApiOperation(value = "查询入库单列表")
	@RequestMapping("/searchStore")
	public PageResult searchStore(@RequestBody Page<PickStoreDat> page) {
		// Map<String,Object> searchParams = Servlets.getParametersStartingWith(request,
		// "search_");
		// page = new Page<>(layTableQuery.getPage(), layTableQuery.getLimit());
		// departmentBankService.selectPage()
		// page.getCondition().put("EQ_is_del", 0);
		page.getCondition().put("EQ_pick_store_type", 2);
		page.setOrderByField("produceCode").setAsc(false);
		pickStoreDatService.selectByPage(page);
		return PageResult.success(page.getRecords(), page.getTotal());
	}

	@ApiOperation(value = "删除")
	@RequestMapping("/delete")
	public Result delete(@RequestBody List<PickStoreDat> pickStoreDats) {
		try {
			for (PickStoreDat dat : pickStoreDats) {
				pickStoreDatService.deleteById(dat.getPickStoreId());
			}
			return Result.success();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return Result.error(e.getMessage());
		}
	}
	
	@ApiOperation(value = "自动按比例领料")
	@RequestMapping("/addPick")
	public Result addPick(@RequestBody PickStoreDat pickStoreDat) {
		String openDate= pickStoreDat.getOpenDate();
		String pencent=pickStoreDat.getPencent();
		String compoanyName=pickStoreDat.getCompanyName();
		System.out.println(123);
		return null;
	}
	

	@ApiOperation(value = "导出领料入库单")
	@PostMapping("/export")
	public void exportBuy(HttpServletResponse response, HttpServletRequest request,
			@ModelAttribute PickStoreDat pickStoreDat) throws IOException {
		Map<String, String[]> map = request.getParameterMap();
		String beginTimeStr = map.get("beginTime")[0];
		String endTimeStr = map.get("endTime")[0];
		String openBeginTimeStr = map.get("openBeginTime")[0];
		String openEndTimeStr = map.get("openEndTime")[0];
		String type = map.get("type")[0];
		String beginDate = null;
		String endDate = null;
		String openBeginTime = null;
		String openEndTime = null;
		String fileName = null;
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
		Wrapper<PickStoreDat> pickStoreDatParam = new EntityWrapper<PickStoreDat>();
		if (!StringUtil.isEmpty(pickStoreDat.getCompanyName())) {
			pickStoreDatParam.eq("company_name", pickStoreDat.getCompanyName());
		}
		if (!StringUtil.isEmpty(beginDate)) {
			pickStoreDatParam.ge("create_time", beginDate);
		}
		if (!StringUtil.isEmpty(endDate)) {
			pickStoreDatParam.le("create_time", endDate);
		}
		if (!StringUtil.isEmpty(openBeginTime)) {
			pickStoreDatParam.ge("open_date", openBeginTime);
		}
		if (!StringUtil.isEmpty(openBeginTime)) {
			pickStoreDatParam.le("open_date", openEndTime);
		}
		if (!StringUtil.isEmpty(type)) {
			pickStoreDatParam.eq("pick_store_type", type);
		}
		pickStoreDatParam.orderBy("document_no", true);
		List<PickStoreDat> dats = pickStoreDatService.selectList(pickStoreDatParam);

		response.setContentType("dat/plain");
		if (type.equals(CamsConstant.PickStoreTypeEnum.PICK.getValue().toString())) {
			fileName = "领料单ItemAc21";
		} else {
			fileName = "入库单ItemAc9";
		}
		response.setHeader("Content-Disposition",
				"attachment; filename=" + URLEncoder.encode(fileName, "UTF-8") + ".dat");// 导出中文名
		BufferedOutputStream buff = null;
		ServletOutputStream outSTr = null;
		outSTr = response.getOutputStream();// 建立
		buff = new BufferedOutputStream(outSTr);
		StringBuffer strTemp = new StringBuffer();
		try {

			for (int i = 0; i < dats.size(); i++) {// 模拟文件中的内容
				String number=dats.get(i).getNumber();
				number=new BigDecimal(number).setScale(2, BigDecimal.ROUND_UP).toString();
				strTemp.delete(0, strTemp.length());
				strTemp.append(
						(dats.get(i).getDanJuLeiXing() == null ? "" : dats.get(i).getDanJuLeiXing().trim().trim())
								+ "	");
				strTemp.append((dats.get(i).getYeWuLeiXing() == null ? "" : dats.get(i).getYeWuLeiXing().trim().trim())
						+ "	");
				strTemp.append(
						(dats.get(i).getKuaiJiNianDu() == null ? "" : dats.get(i).getKuaiJiNianDu().trim().trim())
								+ "	");
				strTemp.append(
						(dats.get(i).getAccountPeriod() == null ? "" : dats.get(i).getAccountPeriod().trim().trim())
								+ "	");
				strTemp.append(
						(dats.get(i).getDocumentNo() == null ? "" : dats.get(i).getDocumentNo().trim().trim()) + "	");
				strTemp.append(
						(dats.get(i).getCustomerCode() == null ? "" : dats.get(i).getCustomerCode().trim().trim())
								+ "	");
				strTemp.append((dats.get(i).getMoBan() == null ? "" : dats.get(i).getMoBan().trim().trim()) + "	");
				strTemp.append(
						(dats.get(i).getProduceDate() == null ? "" : dats.get(i).getProduceDate().trim()) + "	");
				strTemp.append("系统主管" + "			");
				strTemp.append(dats.get(i).getZhiYuan() == null ? "" : dats.get(i).getZhiYuan().trim() + "\t");
				strTemp.append(dats.get(i).getBuMen() == null ? "" : dats.get(i).getBuMen().trim() + "			");
				strTemp.append((dats.get(i).getKeMu() == null ? "" : dats.get(i).getKeMu().trim()) + "	");
				strTemp.append((dats.get(i).getHuiLv() == null ? "1" : dats.get(i).getHuiLv().trim()) + "	");
				strTemp.append(
						(dats.get(i).getBiZhong() == null ? "RMB" : dats.get(i).getBiZhong().trim()) + "			");
				strTemp.append(dats.get(i).getXiangMu() == null ? "" : dats.get(i).getXiangMu().trim() + "\t");
				strTemp.append(dats.get(i).getTongJi() == null ? "" : dats.get(i).getXiangMu().trim() + "			");
				strTemp.append(dats.get(i).getDueDate() == null ? "" : dats.get(i).getDueDate().trim() + "\t");
				strTemp.append(dats.get(i).getOpenDate() == null ? "" : dats.get(i).getOpenDate().trim() + "\t");
				strTemp.append(
						(dats.get(i).getFuKuanTiaoJian() == null ? "" : dats.get(i).getFuKuanTiaoJian().trim()) + "	");
				strTemp.append(
						(dats.get(i).getInvoiceNumber() == null ? "" : dats.get(i).getInvoiceNumber().trim()) + "	");
				strTemp.append(
						(dats.get(i).getFaPiaoLeiXing() == null ? "" : dats.get(i).getFaPiaoLeiXing().trim()) + "\t");
				strTemp.append(
						(dats.get(i).getKaiPiaoBiaoZhi() == null ? "" : dats.get(i).getKaiPiaoBiaoZhi().trim()) + "\t");
				strTemp.append(
						(dats.get(i).getDanWeiDiZhi() == null ? "" : dats.get(i).getDanWeiDiZhi().trim()) + "\t");
				strTemp.append((dats.get(i).getDanWeiKaiHuHang() == null ? "" : dats.get(i).getDanWeiKaiHuHang().trim())
						+ "\t");
				strTemp.append((dats.get(i).getQiYeDiZhi() == null ? "" : dats.get(i).getQiYeDiZhi().trim()) + "\t");
				strTemp.append(
						(dats.get(i).getQiYeKaiHuHang() == null ? "" : dats.get(i).getQiYeKaiHuHang().trim()) + "\t");
				strTemp.append((dats.get(i).getBeiZhu() == null ? "" : dats.get(i).getQiYeKaiHuHang().trim()) + "\t");
				strTemp.append((dats.get(i).getChongXiaoLaiYuanDanJu() == null ? ""
						: dats.get(i).getChongXiaoLaiYuanDanJu().trim()) + "	");
				strTemp.append(dats.get(i).getRowNumber() == null ? "" : dats.get(i).getRowNumber().trim() + "	");
				strTemp.append(
						(dats.get(i).getProduceCode() == null ? "" : dats.get(i).getProduceCode().trim()) + "		");
				strTemp.append(
						dats.get(i).getXuanZeDanJu() == null ? "" : dats.get(i).getXuanZeDanJu().trim() + "		");
				strTemp.append((dats.get(i).getHuoWei() == null ? "01" : dats.get(i).getHuoWei().trim()) + "	");
				strTemp.append((dats.get(i).getCalUnit() == null ? "" : dats.get(i).getCalUnit().trim()) + "	");
				strTemp.append((number == null ? "" : number.trim()) + " 	");
				strTemp.append(dats.get(i).getUnitPrice()==null?"0":dats.get(i).getUnitPrice() + "		");
				strTemp.append(
						dats.get(i).getYueDingRiQi() == null ? "" : dats.get(i).getYueDingRiQi().trim() + "		");
				strTemp.append((dats.get(i).getKouLv() == null ? "100" : dats.get(i).getKouLv().trim()) + "	");
				strTemp.append((dats.get(i).getLocalAmount() == null ? "0" : dats.get(i).getLocalAmount().trim()) +"	");
				strTemp.append((dats.get(i).getLocalAmount() == null ? "0" : dats.get(i).getLocalAmount().trim()) +"		");
				strTemp.append(dats.get(i).getTaxRate() == null ? "" : dats.get(i).getTaxRate().trim() + "	");
				strTemp.append(dats.get(i).getOriginalTax() == null ? "" : dats.get(i).getOriginalTax().trim() + "	");
				strTemp.append((dats.get(i).getLocalhostTax() == null ? "" : dats.get(i).getLocalhostTax().trim())
						+ "				");
				strTemp.append(dats.get(i).getShengChanPiHao() == null ? "" : dats.get(i).getShengChanPiHao().trim());
				strTemp.append("");
				strTemp.append("");
				strTemp.append((dats.get(i).getBaoZhiQi() == null ? "0" : dats.get(i).getBaoZhiQi().trim())
						+ "									");
				strTemp.append(dats.get(i).getGongCheng() == null ? "" : dats.get(i).getGongCheng().trim());
				strTemp.append(
						dats.get(i).getZiDingYiXiangMu0() == null ? "" : dats.get(i).getZiDingYiXiangMu0().trim());
				strTemp.append(
						dats.get(i).getZiDingYiXiangMu1() == null ? "" : dats.get(i).getZiDingYiXiangMu1().trim());
				strTemp.append(
						dats.get(i).getZiDingYiXiangMu2() == null ? "" : dats.get(i).getZiDingYiXiangMu2().trim());
				strTemp.append(
						dats.get(i).getZiDingYiXiangMu3() == null ? "" : dats.get(i).getZiDingYiXiangMu3().trim());
				strTemp.append(
						dats.get(i).getZiDingYiXiangMu4() == null ? "" : dats.get(i).getZiDingYiXiangMu4().trim());
				strTemp.append(
						dats.get(i).getZiDingYiXiangMu5() == null ? "" : dats.get(i).getZiDingYiXiangMu5().trim());
				strTemp.append((dats.get(i).getPiCiShangPinMingXi() == null ? ""
						: dats.get(i).getPiCiShangPinMingXi().trim()));
				strTemp.append(
						(dats.get(i).getChengBenChaYi() == null ? "0" : dats.get(i).getChengBenChaYi().trim()) + "	");
				strTemp.append(
						(dats.get(i).getLocalAmount() == null ? "0" : dats.get(i).getLocalAmount().trim()) + "	");// 成本金额
				strTemp.append(
						(dats.get(i).getGuanBiBiaoZhi() == null ? "0" : dats.get(i).getGuanBiBiaoZhi().trim()) + "	");
				strTemp.append(
						(dats.get(i).getDaiDaYinBiaoZhi() == null ? "0" : dats.get(i).getDaiDaYinBiaoZhi().trim())
								+ "	");
				strTemp.append((dats.get(i).getDaiShiXianXiaoXiangShui() == null ? "0"
						: dats.get(i).getDaiShiXianXiaoXiangShui().trim()) + "	");
				strTemp.append((dats.get(i).getLocalAmount() == null ? "0" : dats.get(i).getLocalAmount().trim()) + "			");// 平均成本
				strTemp.append(dats.get(i).getPinZheng0() == null ? "" : dats.get(i).getPinZheng0().trim());
				strTemp.append(dats.get(i).getPingZheng1() == null ? "" : dats.get(i).getPingZheng1().trim());
				strTemp.append((dats.get(i).getChuRuLeiBie() == null ? "" : dats.get(i).getChuRuLeiBie().trim())
						+ "			");
				strTemp.append(dats.get(i).getShenHeRen() == null ? "" : dats.get(i).getShenHeRen().trim());
				strTemp.append((dats.get(i).getHeTongHao() == null ? "" : dats.get(i).getHeTongHao().trim()));
				strTemp.append((dats.get(i).getZengPinBiaoZhi() == null ? "0" : dats.get(i).getZengPinBiaoZhi().trim())
						+ "\t");
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
