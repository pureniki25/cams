package com.hongte.alms.base.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.BankIncomeDat;
import com.hongte.alms.base.entity.JtDat;
import com.hongte.alms.base.enums.TokenTypeEnum;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.service.CamsCompanyService;
import com.hongte.alms.base.service.JtDatService;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.CamsUtil;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.common.vo.PageResult;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

	@Autowired
	@Qualifier("CamsCompanyService")
	private CamsCompanyService camsCompanyService;
	
	
	@ApiOperation(value = "导入固定资产")
	@RequestMapping("/importZiChan")
	public Result importZiChan(@RequestParam("file") MultipartFile file, HttpServletRequest request,
			MultipartRequest req) {
		Result<String> result=camsCompanyService.getCompany(request, TokenTypeEnum.COOKIES);
		String companyName="";
		if(result.getCode().equals("1")){
			companyName=result.getData();
		}
		try {
			Map<String, String[]> map = request.getParameterMap();
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
	
	@ApiOperation(value = "新增")
	@RequestMapping("/save")
	public Result save(@RequestParam Map<String, Object> map,HttpServletRequest request) {
		Result<String> result=camsCompanyService.getCompany(request, TokenTypeEnum.TOKEN);
		String companyName="";
		if(result.getCode().equals("1")){
			companyName=result.getData();
		}
		String selects = (String) map.get("selects");
		List<JtDat>  addDats = JSONObject.parseArray(selects, JtDat.class);
		String openDateStr = (String) map.get("date");
		String customerCode = (String) map.get("customerCode");
		String openDate = CamsUtil.getLastDate(openDateStr);
		String jtType = (String) map.get("jtType");
		if(StringUtil.isEmpty(companyName)) {
			Result.error("公司名称不能为空");
		}
		if(StringUtil.isEmpty(openDate)) {
			Result.error("开票日期不能为空");
		}	
		if(StringUtil.isEmpty(jtType)) {
			Result.error("计提类型不能为空");
		}		
		jtDatService.saveJtDat(addDats, openDate, companyName, customerCode,jtType);
		
			return Result.success();
	
	}
	
	@ApiOperation(value = "查询费列表")
	@RequestMapping("/search")
	public PageResult search(@RequestBody Page<JtDat> page, HttpServletRequest request) {
		Result<String> result=camsCompanyService.getCompany(request, TokenTypeEnum.TOKEN);
		String companyName="";
		if(result.getCode().equals("1")){
			companyName=result.getData();
			page.getCondition().put("EQ_company_name",companyName);
		}
		page.setOrderByField("pingZhengHao").setAsc(true);
		jtDatService.selectByPage(page);
		return PageResult.success(page.getRecords(), page.getTotal());
	}
	@ApiOperation(value = "导出计提列表")
	@PostMapping("/export")
	public void export(HttpServletResponse response, HttpServletRequest request, @ModelAttribute JtDat jtDat)
			throws IOException {
		Result<String> result=camsCompanyService.getCompany(request, TokenTypeEnum.COOKIES);
		String companyName="";
		if(result.getCode().equals("1")){
			companyName=result.getData();
			jtDat.setCompanyName(companyName);
		}
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
	
    @ApiOperation(value = "编辑")
    @RequestMapping("/update")
    public Result edit(@RequestBody BankIncomeDat vo) {
    	JtDat dat=jtDatService.selectOne(new EntityWrapper<JtDat>().eq("id", vo.getId()));
    	dat.setPingZhengHao(vo.getPingZhengHao());
    	dat.setZhaiYao(vo.getZhaiYao());
    	dat.setKeMuDaiMa(vo.getKeMuDaiMa());
    	dat.setLocalAmount(vo.getLocalAmount());
    	if(!StringUtil.isEmpty(vo.getBorrowAmount())&&!(vo.getBorrowAmount().equals("0")||vo.getBorrowAmount().equals("0.0")||vo.getBorrowAmount().equals("0.00"))) {
    		dat.setBorrowAmount(vo.getLocalAmount());
    	}
    	if(!StringUtil.isEmpty(vo.getAlmsAmount())&&!(vo.getAlmsAmount().equals("0")||vo.getAlmsAmount().equals("0.0")||vo.getAlmsAmount().equals("0.00"))) {
    		dat.setAlmsAmount(vo.getLocalAmount());
    	}
    	dat.setInvoiceNumber(vo.getInvoiceNumber());
    	dat.setCreateTime(new Date());
    	jtDatService.updateById(dat);
    	return Result.success();
    }
}

