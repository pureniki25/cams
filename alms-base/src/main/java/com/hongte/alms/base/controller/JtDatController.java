package com.hongte.alms.base.controller;


import java.io.BufferedOutputStream;
import java.io.IOException;
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
import com.hongte.alms.base.entity.JtDat;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.service.JtDatService;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.CamsUtil;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.common.vo.PageResult;

import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 计提表 前端控制器
 * </p> 
 *
 * @author czs
 * @since 2019-09-01
 */
@RestController
@RequestMapping("/jtDatController")
public class JtDatController {
	private static final Logger LOGGER = LoggerFactory.getLogger(JtDatController.class);
	@Autowired
	@Qualifier("JtDatService")
	private JtDatService jtDatService;
	
	
	
	
	@ApiOperation(value = "导入固定资产")
	@RequestMapping("/importZiChan")
	public Result importZiChan(@RequestParam("file") MultipartFile file, HttpServletRequest request,
			MultipartRequest req) {
		Result result = null;
		try {
			Map<String, String[]> map = request.getParameterMap();
			String companyName = map.get("companyName")[0];
			String feeType = map.get("feeType")[0];// 费用类型
			String fileName = CamsUtil.getCompanyName(file.getOriginalFilename());
			if (!fileName.equals(companyName)) {
				return Result.error("选择的公司与导入的公司不一致");
			}
			String openTime = map.get("openDate")[0];
			Date date = new Date(openTime);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cale = Calendar.getInstance();
			cale.setTime(date);
			// 获取前月的最后一天
			cale.add(Calendar.MONTH, 1);
			cale.set(Calendar.DAY_OF_MONTH, 0);
			String lastday = format.format(cale.getTime());
			LOGGER.info("====>>>>>导入资产excel开始[{}]", file);
			jtDatService.importZiChan(file, companyName, lastday);
			result = Result.success();
		} catch (ServiceRuntimeException se) {
			result = Result.error(se.getErrorCode(), se.getMessage());
			LOGGER.error("====>>>>>导入资产出错{}", se.getMessage());
		} catch (Exception e) {
			result = Result.error("500", "导入出错");
			LOGGER.error("====>>>>>导入资产出错{}", e);
		}
		LOGGER.info("====>>>>>导入费用excel结束");
		return result;
	}
	
	
	@ApiOperation(value = "导入税金excel")
	@RequestMapping("/importTaxExcel")
	public Result importTaxExcel(@RequestParam("file") MultipartFile file, HttpServletRequest request,
			MultipartRequest req) {
		Result result = null;
		try {
			Map<String, String[]> map = request.getParameterMap();
		
			String openTime = map.get("openDate")[0];
			Date date = new Date(openTime);
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Calendar cale = Calendar.getInstance();
			cale.setTime(date);
			// 获取前月的最后一天
			cale.add(Calendar.MONTH, 1);
			cale.set(Calendar.DAY_OF_MONTH, 0);
			String lastday = format.format(cale.getTime());
			LOGGER.info("====>>>>>导入税金excel开始[{}]", file);
			jtDatService.importTax(file, "3", lastday);

			result = Result.success();
		} catch (Exception e) {
			result = Result.error("500",e.getMessage());
			LOGGER.error("====>>>>>导入工资出错{}", e);
		}
		LOGGER.info("====>>>>>导入工资excel结束");
		return result;
	}
	
	
	@ApiOperation(value = "查询费列表")
	@RequestMapping("/search")
	public PageResult search(@RequestBody Page<JtDat> page) {
		
		page.getCondition().put("EQ_deduction_type", 1);
		page.setOrderByField("pingZhengHao").setAsc(true);
		jtDatService.selectByPage(page);
		return PageResult.success(page.getRecords(), page.getTotal());
	}
	@ApiOperation(value = "导出计提列表")
	@PostMapping("/export")
	public void export(HttpServletResponse response, HttpServletRequest request, @ModelAttribute JtDat jtDat)
			throws IOException {
		Map<String, String[]> map = request.getParameterMap();
		String openBeginTimeStr = map.get("openBeginTime")[0];
		String openEndTimeStr = map.get("openEndTime")[0];
		String jtType = map.get("jtType")[0];
		String openBeginTime = null;
		String openEndTime = null;
	

		if (!StringUtil.isEmpty(openBeginTimeStr)) {
			openBeginTime = DateUtil.formatDate(new Date(openBeginTimeStr));
		}
		if (!StringUtil.isEmpty(openEndTimeStr)) {
			openEndTime = DateUtil.formatDate(new Date(openEndTimeStr));
		}

		Wrapper<JtDat> wrapperDat = new EntityWrapper<JtDat>();
		if (!StringUtil.isEmpty(jtDat.getCompanyName())) {
			wrapperDat.eq("company_name", jtDat.getCompanyName());
		}

		if (!StringUtil.isEmpty(openBeginTime)) {
			wrapperDat.ge("ping_zheng_ri_qi", openBeginTime);
		}
		if (!StringUtil.isEmpty(openEndTime)) {
			wrapperDat.le("ping_zheng_ri_qi", openEndTime);
		}
		if (!StringUtil.isEmpty(jtType)) {
			wrapperDat.eq("jt_type", jtType);
		}
	
		
		wrapperDat.orderBy("ping_zheng_Hao", true);
		List<JtDat> dats = jtDatService.selectList(wrapperDat);

		response.setContentType("dat/plain");
		String fileName = "计提单Voucher";
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
				strTemp.append((dats.get(i).getQiJian() == null ? "" : dats.get(i).getQiJian().trim().trim()) + "	");
				strTemp.append(
						(dats.get(i).getPingZhengRiQi() == null ? "" : dats.get(i).getPingZhengRiQi().trim().trim())
								+ "	");
				strTemp.append((dats.get(i).getPingZhengZi() == null ? "" : dats.get(i).getPingZhengZi().trim().trim())
						+ "	");
				strTemp.append(
						(dats.get(i).getPingZhengHao() == null ? "" : dats.get(i).getPingZhengHao().trim().trim())
								+ "	");
				strTemp.append((dats.get(i).getZhaiYao() == null ? "" : dats.get(i).getZhaiYao().trim().trim()) + "	");
				strTemp.append(
						(dats.get(i).getKeMuDaiMa() == null ? "" : dats.get(i).getKeMuDaiMa().trim().trim()) + "	");
				strTemp.append(
						(dats.get(i).getHuoBiDaiMa() == null ? "" : dats.get(i).getHuoBiDaiMa().trim().trim()) + "	");
				strTemp.append((dats.get(i).getHuiLv() == null ? "" : dats.get(i).getHuiLv().trim()) + "	");
				strTemp.append(dats.get(i).getLocalAmount() == null ? "" : dats.get(i).getLocalAmount().trim() + " 	");
				strTemp.append(
						dats.get(i).getBorrowAmount() == null ? "" : dats.get(i).getBorrowAmount().trim() + " 	");
				strTemp.append((dats.get(i).getAlmsAmount() == null ? "" : dats.get(i).getAlmsAmount().trim()) + " 	");
				strTemp.append((dats.get(i).getShuLiang() == null ? "0" : dats.get(i).getShuLiang().trim()) + "	");
				strTemp.append((dats.get(i).getDanJia() == null ? "" : dats.get(i).getDanJia().trim()) + "	");
				strTemp.append((dats.get(i).getZhiDanRen() == null ? "" : dats.get(i).getZhiDanRen().trim()) + "	");
				strTemp.append((dats.get(i).getShenHeRen() == null ? "" : dats.get(i).getShenHeRen().trim()) + "	");
				strTemp.append(
						(dats.get(i).getGuoZhangRen() == null ? "" : dats.get(i).getGuoZhangRen().trim()) + "	");
				strTemp.append(dats.get(i).getFuDanJuShu() == null ? "" : dats.get(i).getFuDanJuShu().trim() + "\t");

				strTemp.append(dats.get(i).getShiFouYiGuoZhang() == null ? ""
						: dats.get(i).getShiFouYiGuoZhang().trim() + "	");

				strTemp.append(dats.get(i).getMoBan() == null ? "" : dats.get(i).getMoBan().trim() + "\t");

				strTemp.append(dats.get(i).getHangHao() == null ? "" : dats.get(i).getHangHao().trim() + "\t");

				strTemp.append((dats.get(i).getDanWei() == null ? "" : dats.get(i).getDanWei().trim()) + "\t");

				strTemp.append((dats.get(i).getBuMen() == null ? "" : dats.get(i).getBuMen().trim()) + "\t");

				strTemp.append((dats.get(i).getYuanGong() == null ? "" : dats.get(i).getYuanGong().trim()) + "\t");

				strTemp.append((dats.get(i).getTongJi() == null ? "" : dats.get(i).getTongJi().trim()) + "\t");

				strTemp.append((dats.get(i).getXiangMu() == null ? "" : dats.get(i).getXiangMu().trim()) + "\t");

				strTemp.append(
						(dats.get(i).getFuKuanFangFa() == null ? "" : dats.get(i).getFuKuanFangFa().trim()) + "\t");

				strTemp.append((dats.get(i).getPiaoJuHao() == null ? "" : dats.get(i).getPiaoJuHao().trim()) + "\t");

				strTemp.append("0" + "\t");

				strTemp.append(dats.get(i).getPingZhengLaiYuan() == null ? ""
						: dats.get(i).getPingZhengLaiYuan().trim() + "\t");

				strTemp.append(dats.get(i).getLaiYuanPingZheng() == null ? ""
						: dats.get(i).getLaiYuanPingZheng().trim() + "\t");

				strTemp.append(dats.get(i).getDaiDaYin() == null ? "" : dats.get(i).getDaiDaYin().trim() + "\t");

				strTemp.append(
						dats.get(i).getZuoFeiBiaoZhi() == null ? "" : dats.get(i).getZuoFeiBiaoZhi().trim() + "\t");

				strTemp.append(
						dats.get(i).getCuoWuBiaoZhi() == null ? "" : dats.get(i).getCuoWuBiaoZhi().trim() + "\t");

				strTemp.append(
						dats.get(i).getPingZhengCeHao() == null ? "" : dats.get(i).getPingZhengCeHao().trim() + "\t");

				strTemp.append((dats.get(i).getChuNaRen() == null ? "" : dats.get(i).getChuNaRen().trim()) + "\t");

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
	

	@ApiOperation(value = "删除计提")
	@RequestMapping("/delete")
	public Result delete(@RequestBody List<JtDat> jtDats) {
		try {
			for (JtDat dat : jtDats) {
				jtDatService.deleteById(dat.getId());
				List<JtDat> list = jtDatService.selectList(new EntityWrapper<JtDat>().eq("uuid", dat.getUuid()));
				for (JtDat extDat : list) {
					jtDatService.deleteById(extDat);
				}
			}

			return Result.success();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return Result.error(e.getMessage());
		}
	}
}

