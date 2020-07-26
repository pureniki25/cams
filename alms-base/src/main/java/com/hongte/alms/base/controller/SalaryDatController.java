package com.hongte.alms.base.controller;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.hongte.alms.base.enums.TokenTypeEnum;
import com.hongte.alms.base.service.CamsCompanyService;
import com.hongte.alms.common.util.*;
import com.netflix.ribbon.proxy.annotation.Http;
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

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.databind.util.BeanUtil;
import com.hongte.alms.base.entity.CamsSubject;
import com.hongte.alms.base.entity.FeeDat;
import com.hongte.alms.base.entity.JtDat;
import com.hongte.alms.base.entity.PickStoreDat;
import com.hongte.alms.base.entity.ProductDat;
import com.hongte.alms.base.entity.SalaryDat;
import com.hongte.alms.base.entity.TempProductDat;
import com.hongte.alms.base.enums.CamsConstant;
import com.hongte.alms.base.enums.SubjectEnum;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.service.BankIncomeDatService;
import com.hongte.alms.base.service.JtDatService;
import com.hongte.alms.base.service.SalaryDatService;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.vo.PageResult;

import io.swagger.annotations.ApiOperation;

/**
 * <p>
 * 薪资表 前端控制器
 * </p>
 *
 * @author czs
 * @since 2019-09-01
 */
@RestController
@RequestMapping("/salaryDatController")
public class SalaryDatController {

	private static final Logger LOGGER = LoggerFactory.getLogger(SalaryDatController.class);

	@Autowired
	@Qualifier("SalaryDatService")
	private SalaryDatService salaryDatService;
	

	@Autowired
	@Qualifier("JtDatService")
	private JtDatService jtDatService;
	
	@Autowired
	@Qualifier("BankIncomeDatService")
	private BankIncomeDatService bankIncomeDatService;

	@Autowired
	@Qualifier("CamsCompanyService")
	private CamsCompanyService camsCompanyService;

	@ApiOperation(value = "导入工资excel")
	@RequestMapping("/importSalaryExcel")
	public Result importSalaryExcel(@RequestParam("file") MultipartFile file, HttpServletRequest request,
			MultipartRequest req) {
		Result result=camsCompanyService.getCompany(request, TokenTypeEnum.COOKIES);
		String companyName="";
		if(result.getCode().equals("1")){
			companyName= (String) result.getData();
		}
		try {
			Map<String, String[]> map = request.getParameterMap();
			if (!file.getOriginalFilename().contains(companyName)) {
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
			LOGGER.info("====>>>>>导入工资excel开始[{}]", file);
			salaryDatService.importSalary(file, companyName, "1", lastday);

			result = Result.success();
		} catch (ServiceRuntimeException se) {
			result = Result.error(se.getErrorCode(), se.getMessage());
			LOGGER.error("====>>>>>导入工资{}", se.getMessage());
		} catch (Exception e) {
			result = Result.error("500", "导入出错");
			LOGGER.error("====>>>>>导入工资出错{}", e);
		}
		LOGGER.info("====>>>>>导入工资excel结束");
		return result;
	}

	@ApiOperation(value = "导入社保excel") 
	@RequestMapping("/importSheBaoExcel")
	public Result importSheBaoExcel(@RequestParam("file") MultipartFile file, HttpServletRequest request,
			MultipartRequest req) {
		Result result=camsCompanyService.getCompany(request, TokenTypeEnum.COOKIES);
		String companyName="";
		if(result.getCode().equals("1")){
			companyName= (String) result.getData();
		}
		try {
			Map<String, String[]> map = request.getParameterMap();
			if (!file.getOriginalFilename().contains(companyName)) {
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
			LOGGER.info("====>>>>>导入社保excel开始[{}]", file);
			salaryDatService.importSheBaoExcel(file, companyName, lastday);

			result = Result.success();
		} catch (ServiceRuntimeException se) {
			result = Result.error(se.getErrorCode(), se.getMessage());
			LOGGER.error("====>>>>>导入社保{}", se.getMessage());
		} catch (Exception e) {
			result = Result.error("500", "导入社保Excel出错:"+e.getMessage());
			LOGGER.error("====>>>>>导入社保出错{}", e);
		}
		LOGGER.info("====>>>>>导入社保excel结束");
		return result;
	}
	
	

	@ApiOperation(value = "查询工资列表")
	@RequestMapping("/searchSalary")
	public PageResult searchSalary(@RequestBody Page<SalaryDat> page,HttpServletRequest request) {
		Result<String> result=camsCompanyService.getCompany(request, TokenTypeEnum.TOKEN);
		String companyName="";
		if(result.getCode().equals("1")){
			companyName=result.getData();
			page.getCondition().put("EQ_company_name",companyName);
		}
		String beginDate = (String) page.getCondition().get("GE_salary_date");
		String endDate = (String) page.getCondition().get("LE_salary_date");
		if(StringUtil.isEmpty(beginDate)) {
			page.getCondition().put("GE_salary_date", DateUtil.getLastFirstDate());
		}else{

			beginDate = CamsUtil.getCurrentFirstDate(beginDate.substring(0, 10));
			page.getCondition().put("GE_salary_date", beginDate);
		}
		if(StringUtil.isEmpty(endDate)) {
			page.getCondition().put("LE_salary_date", DateUtil.getLastEndDate());
		}else{
			endDate = CamsUtil.getCurrentLastDate(endDate.substring(0, 10));
			page.getCondition().put("LE_salary_date", endDate);
		}
		page.setOrderByField("createTime").setAsc(true);
		salaryDatService.selectByPage(page);
		return PageResult.success(page.getRecords(), page.getTotal());
	}

	@ApiOperation(value = "分配")
	@RequestMapping("/devide")
	public Result devide(@RequestParam Map<String, Object> map) {
		try {
			salaryDatService.devide(map);
			return Result.success();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return Result.error(e.getMessage());
		}
	}

	@ApiOperation(value = "删除")
	@RequestMapping("/delete")
	public Result delete(@RequestBody List<SalaryDat> salaryDats) {
		try {
			for (SalaryDat dat : salaryDats) {
				salaryDatService.deleteById(dat);
			}
			return Result.success();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return Result.error(e.getMessage());
		}
	}

	@ApiOperation(value = "生成计提工资")
	@RequestMapping("/addJtSalary")
	public Result addJtSalary(@RequestBody List<SalaryDat> salaryDats) {
		try {
			for (SalaryDat dat : salaryDats) {
				if (StringUtil.isEmpty(dat.getKeMuDaiMa())) {
					return Result.error("存在没被设置科目代码的工资记录，不能生成计提工资");
				}
			}
			Map<String, List<SalaryDat>> resultList = salaryDats.stream()
					.collect(Collectors.groupingBy(SalaryDat::getKeMuDaiMa));
			BigDecimal monthSalarySum=BigDecimal.ZERO;
			String pingZhengHao = getPingZhengHao(salaryDats.get(0).getCompanyName(), salaryDats.get(0).getSalaryDate());
			for (String keMuDaiMa : resultList.keySet()) {
				List<SalaryDat> salarys = resultList.get(keMuDaiMa);
				BigDecimal benQiShouRuSum = BigDecimal.ZERO;
				for (SalaryDat salary : salarys) {
					benQiShouRuSum = benQiShouRuSum.add(new BigDecimal(salary.getBenQiShouRu()==null?"0":salary.getBenQiShouRu()));
				}
				BigDecimal salarySum = benQiShouRuSum;
				monthSalarySum=monthSalarySum.add(salarySum);
				salaryDatService.addJtSalary(keMuDaiMa, salarys.get(0).getSalaryDate(), salarys.get(0).getCompanyName(),
						salarySum.toString(), CamsConstant.DirectionEnum.JIE,pingZhengHao);
			}
			salaryDatService.addJtSalary(SubjectEnum.YING_FU_GONG_ZI_SUBJECT.getValue().toString(), salaryDats.get(0).getSalaryDate(), salaryDats.get(0).getCompanyName(),
					monthSalarySum.toString(), CamsConstant.DirectionEnum.DAI,pingZhengHao);
			
			return Result.success();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			return Result.error(e.getMessage());
		}
	}

	@ApiOperation(value = "生成个人所得税")
	@RequestMapping("/createPersonTax")
	public Result createPersonTax(@RequestBody List<SalaryDat> salaryDats) throws Exception {
		BigDecimal allPersonTaxSum=BigDecimal.ZERO;
		String companyName="";
		String openDate="";
		for (SalaryDat dat : salaryDats) {
			openDate=dat.getSalaryDate();
			companyName=dat.getCompanyName();
			Map<String,BigDecimal> map=salaryDatService.getPersonSalaryBefore(dat.getIdcardNo(), dat.getSalaryDate(), dat.getCompanyName());
			BigDecimal benQiShouRuSum=map.get("benQiShouRuSum");
			BigDecimal benQiMianShuiShouRuSum=map.get("benQiMianShuiShouRuSum");
			BigDecimal yiJiaoShuiESum=map.get("yiJiaoShuiESum");
			BigDecimal beforeDeductionSum=map.get("beforeDeductionSum");
			BigDecimal sheBaoGongJiJinSum=map.get("sheBaoGongJiJinSum");
			BigDecimal jianChuFeiYongBiaoZhunSum=map.get("jianChuFeiYongBiaoZhunSum");
			//累计预扣预缴应纳税所得额=累计收入-累计免税收入-累计减除费用-累计专项扣除-累计专项附加扣除-累计依法确定的其他扣除
			BigDecimal preAmount=benQiShouRuSum.subtract(benQiMianShuiShouRuSum).subtract(sheBaoGongJiJinSum).subtract(beforeDeductionSum).subtract(jianChuFeiYongBiaoZhunSum);
			if(preAmount.doubleValue()<0) {
				continue;
			}
			//已交税额
			BigDecimal mTax=yiJiaoShuiESum;
			//本期应交个人
			BigDecimal cTax=preAmount; 
				if (cTax.doubleValue() <= 36000) {
					cTax = (cTax.multiply(new BigDecimal(0.03))).subtract(mTax);
				} else if (cTax.doubleValue()> 36000 && cTax.doubleValue() <= 144000) {
					cTax = (cTax .multiply(new BigDecimal(0.10)).subtract(new BigDecimal(2520))).subtract(mTax);
				} else if (cTax.doubleValue() > 144000 && cTax.doubleValue() <= 300000) {
					cTax = (cTax .multiply(new BigDecimal(0.20)).subtract(new BigDecimal(16920))).subtract(mTax);
				} else if (cTax.doubleValue() > 300000 && cTax.doubleValue() <= 420000) {
					cTax = (cTax .multiply(new BigDecimal(0.25)).subtract(new BigDecimal(31920))).subtract(mTax);
				} else if (cTax.doubleValue() > 420000 && cTax.doubleValue() <= 660000) {
					cTax = (cTax .multiply(new BigDecimal(0.3)).subtract(new BigDecimal(52920))).subtract(mTax);
				} else if (cTax.doubleValue() > 660000 && cTax.doubleValue() <= 960000) {
					cTax = (cTax .multiply(new BigDecimal(0.35)).subtract(new BigDecimal(85920))).subtract(mTax);
				} else {
					cTax = (cTax .multiply(new BigDecimal(0.45)).subtract(new BigDecimal(181920))).subtract(mTax);
				}
				cTax=cTax.setScale(2, BigDecimal.ROUND_UP);
				mTax=mTax.add(cTax);
				allPersonTaxSum=allPersonTaxSum.add(cTax);
				dat.setTax(cTax.toString());
				dat.setYiKouJiaoShuiE(mTax.toString());
				salaryDatService.updateById(dat);
		}
		//计提模块插入个人所得税科目记录
		jtDatService.addGeRenSuoDeShui(companyName, allPersonTaxSum, openDate);
		return Result.success();
	}

	@ApiOperation(value = "设置科目")
	@RequestMapping("/editSubject")
	public Result editSubject(@RequestParam Map<String, Object> map)
			throws InstantiationException, IllegalAccessException {
		LOGGER.info("@设置科目--开始[]");
		Result result = null;
		String selects = (String) map.get("selects");
		List<SalaryDat> salaryDats = JSONObject.parseArray(selects, SalaryDat.class);
		String smallSubjectId = (String) map.get("smallSubjectId");
	    for(SalaryDat salaryDat:salaryDats) {
			salaryDat.setKeMuDaiMa(smallSubjectId);
			salaryDatService.updateById(salaryDat);
	    }


		result = Result.success();
		LOGGER.info("@设置科目--结束[{}]", result);
		return result;
	}
	private String getPingZhengHao(String companyName, String openDate) {
		String pingZhengHao = "";
		/* 获取凭证号 */
		Wrapper<JtDat> wrapperDat = new EntityWrapper<JtDat>();
		wrapperDat.eq("company_name", companyName);
		wrapperDat.eq("open_date", openDate);
		wrapperDat.orderBy("ping_zheng_hao", false);
		List<JtDat> jtDats = jtDatService.selectList(wrapperDat);
		if (jtDats.size() == 0) {
			pingZhengHao = "500";
		} else {
			pingZhengHao = CamsUtil.generateCode(jtDats.get(0).getPingZhengHao());
		}
		return pingZhengHao;
	}
	public static void main(String[] args) {
		String str = "2019-07-31T16:00:00.000Z";
		System.out.println(str.subSequence(0, 10));
	}

}
