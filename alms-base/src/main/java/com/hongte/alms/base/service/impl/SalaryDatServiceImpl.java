package com.hongte.alms.base.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.hongte.alms.base.entity.BankIncomeDat;
import com.hongte.alms.base.entity.BuyDat;
import com.hongte.alms.base.entity.JtDat;
import com.hongte.alms.base.entity.PickStoreDat;
import com.hongte.alms.base.entity.SalaryDat;
import com.hongte.alms.base.enums.CamsConstant;
import com.hongte.alms.base.enums.SubjectEnum;
import com.hongte.alms.base.invoice.vo.NewSalaryExcel;
import com.hongte.alms.base.invoice.vo.SalaryExcel;
import com.hongte.alms.base.mapper.SalaryDatMapper;
import com.hongte.alms.base.service.BankIncomeDatService;
import com.hongte.alms.base.service.JtDatService;
import com.hongte.alms.base.service.PickStoreDatService;
import com.hongte.alms.base.service.SalaryDatService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.CamsUtil;
import com.hongte.alms.common.util.ClassCopyUtil;
import com.hongte.alms.common.util.ReadExcelTools;
import com.hongte.alms.common.util.StringUtil;
import com.ht.ussp.util.DateUtil;

/**
 * <p>
 * 薪资表 服务实现类
 * </p>
 *
 * @author czs
 * @since 2019-09-01
 */
@Service("SalaryDatService")
public class SalaryDatServiceImpl extends BaseServiceImpl<SalaryDatMapper, SalaryDat> implements SalaryDatService {
    
	private static final  String PRODUCE_SUBJECT="4105-01";
	@Autowired
	@Qualifier("JtDatService")
	private JtDatService jtDatService;

	@Autowired
	@Qualifier("PickStoreDatService")
	private PickStoreDatService pickStoreDatService;

	
	@Autowired
	@Qualifier("BankIncomeDatService")
	private BankIncomeDatService bankIncomeDatService;
	@Override
	@Transactional
	public void importSalary(MultipartFile file, String companyName, String type, String openDate) throws Exception {
		String fileName = file.getName();
		ImportParams importParams = new ImportParams();
		List<NewSalaryExcel> excels = ExcelImportUtil.importExcel(file.getInputStream(), NewSalaryExcel.class, importParams);
		
		BigDecimal benQiShouRuSum = BigDecimal.ZERO;
		BigDecimal jiBenYangLaoBaoXianSum = BigDecimal.ZERO;
		BigDecimal jiBenYiLiaoBaoXianSum = BigDecimal.ZERO;
		BigDecimal shiYeBaoXianFeiSum = BigDecimal.ZERO;
		BigDecimal zhuFangGongJiJinSum = BigDecimal.ZERO;
       List<SalaryDat> salaryDats=new ArrayList<>();
       SalaryDat salaryDat = null;
		for (NewSalaryExcel salaryExcel : excels) {
			salaryDat = new SalaryDat();
			salaryDat.setCompanyName(companyName);
			salaryDat.setBenQiShouRu(salaryExcel.getBenQiShouRu());
			salaryDat.setCreateTime(new Date());
			salaryDat.setIdcardNo(salaryExcel.getIdcardNo());
			salaryDat.setJianChuFeiYongBiaoZhun(transferNullToZero(salaryExcel.getJianChuFeiYongBiaoZhun()));
			salaryDat.setJianMianShuiE(transferNullToZero(salaryExcel.getJianMianShuiE()));
			salaryDat.setJiBenYangLaoBaoXian(transferNullToZero(salaryExcel.getJiBenYangLaoBaoXian()));
			salaryDat.setJiBenYiLiaoBaoXian(transferNullToZero(salaryExcel.getJiBenYiLiaoBaoXian()));
			salaryDat.setLeiJiJiXuJiaoYu(transferNullToZero(salaryExcel.getLeiJiJiXuJiaoYu()));
			salaryDat.setLeiJiShanYangLaoRen(transferNullToZero(salaryExcel.getLeiJiShanYangLaoRen()));
			salaryDat.setLeiJiZhuFangDaiKuanLiXi(transferNullToZero(salaryExcel.getLeiJiZhuFangDaiKuanLiXi()));
			salaryDat.setLeiJiZhuFangZuJin(transferNullToZero(salaryExcel.getLeiJiZhuFangZuJin()));
			salaryDat.setLeiJiZiNvJiaoYu(transferNullToZero(salaryExcel.getLeiJiZiNvJiaoYu()));
			salaryDat.setBenQiMianShuiShouRu(transferNullToZero(salaryExcel.getBenQiMianShuiShouRu()));
			salaryDat.setTax(transferNullToZero(salaryExcel.getBenQiShuiE()));
			salaryDat.setName(salaryExcel.getName());
			salaryDat.setQiTa(salaryExcel.getQiTa());
			salaryDat.setQiYeNianJin(transferNullToZero(salaryExcel.getQiYeNianJin()));
			salaryDat.setShangYeJianKangBaoXian(transferNullToZero(salaryExcel.getShangYeJianKangBaoXian()));
			salaryDat.setShiYeBaoXian(transferNullToZero(salaryExcel.getShiYeBaoXian()));
			salaryDat.setShuiQianKouChuXiangMuHeJi(transferNullToZero(salaryExcel.getShuiQianKouChuXiangMuHeJi()));
			salaryDat.setShuiYanYangLaoBaoXian(transferNullToZero(salaryExcel.getShuiYanYangLaoBaoXian()));
			salaryDat.setSuoDeQiJianQi(salaryExcel.getSuoDeQiJianQi());
			salaryDat.setSuoDeQiJianZhi(salaryExcel.getSuoDeQiJianZhi());
			salaryDat.setYiKouJiaoShuiE(transferNullToZero(salaryExcel.getYiKouJiaoShuiE()));
			salaryDat.setZhuFangGongJiJin(transferNullToZero(salaryExcel.getZhuFangGongJiJin()));
			salaryDat.setZhunYuKouChuJuanZengKuan(transferNullToZero(salaryExcel.getZhunYuKouChuJuanZengKuan()));
			salaryDat.setSalaryDate(openDate);
			salaryDats.add(salaryDat);
			insert(salaryDat);
		}
		
		
		//生成银行模块个人所得税科目
		createPersonTax(salaryDats);
		
		//生成银行模块公积金科目
		insertGongJiJin(salaryDats, openDate, companyName);
	}

	 private String transferNullToZero(String jine) {
		 if(jine==null) {
			 return "0";
		 }else {
			 return jine;
		 }
		 
	 }
	@Override
	public void addJtSalary(String keMuDaiMa, String openDate, String companyName, String localAmount,CamsConstant.DirectionEnum directionEnum,String pingZhengHao)
			throws Exception {
		BigDecimal localJine = new BigDecimal(localAmount);
		JtDat dat = new JtDat();
		dat.setUuid(UUID.randomUUID().toString());
		dat.setPingZhengRiQi(openDate);
		dat.setPingZhengZi("记");
		dat.setPingZhengHao(pingZhengHao);
		dat.setZhaiYao("计提本月工资");
		dat.setKeMuDaiMa(keMuDaiMa);
		dat.setJtType(CamsConstant.JiTiTypeEnum.SALARY.getValue().toString());
		dat.setHuoBiDaiMa("RMB");
		dat.setHuiLv("1");
		dat.setShuLiang("0");
		dat.setDanJia("0");
		dat.setZhiDanRen("系统主管");
		dat.setShenHeRen("");
		dat.setGuoZhangRen("");
		dat.setFuDanJuShu("0");
		dat.setShiFouYiGuoZhang("0");
		dat.setMoBan("11记账凭证");
		dat.setCustomerCode("");
		dat.setBuMen("");
		dat.setYuanGong("");
		dat.setTongJi("");
		dat.setXiangMu("");
		dat.setFuKuanFangFa("");
		dat.setPiaoJuHao("");
		dat.setYuanBiFuKuanJinE("0");
		dat.setPingZhengLaiYuan("1");
		dat.setLaiYuanPingZheng(",,,");
		dat.setDaiDaYin("");
		dat.setHangHao("1");
		dat.setZuoFeiBiaoZhi("0");
		dat.setCuoWuBiaoZhi("0");
		dat.setPingZhengCeHao("00");
		dat.setOpenDate(openDate);
		dat.setChuNaRen("");
		dat.setCompanyName(companyName);
		dat.setDeductionType("1");
		dat.setQiJian(String.valueOf(DateUtil.getMonth(DateUtil.getDate(openDate))));
		dat.setCreateTime(new Date());
		
		if(directionEnum==CamsConstant.DirectionEnum.JIE) {
			dat.setLocalAmount(localJine.toString());
			dat.setBorrowAmount(localJine.toString());
			dat.setAlmsAmount("0");
		}else {
			dat.setLocalAmount(localJine.toString());
			dat.setBorrowAmount("0");
			dat.setAlmsAmount(localJine.toString());
		}
		jtDatService.insert(dat);
	
	}

	@Override
	@Transactional
	public void devide(Map<String, Object> map) throws Exception {
		String companyName = "";
		String selects = (String) map.get("selects");
		List<SalaryDat> salaryDats = JSONObject.parseArray(selects, SalaryDat.class);
		Map<String, List<SalaryDat>> resultList=salaryDats.stream().collect(Collectors.groupingBy(SalaryDat::getKeMuDaiMa));
		if(resultList.size()>0) {
			salaryDats=  resultList.get(PRODUCE_SUBJECT);	  	
		}
		//制造费用的工资才要生成产成品
		if(!(salaryDats.size()>0)) {
			throw new ApplicationContextException("没有制造费用工资分配");
		}
		if (salaryDats.size() > 0) {
			companyName = salaryDats.get(0).getCompanyName();
		}
		String pickDateStr = (String) map.get("date");
		String openDate = CamsUtil.getLastDate(pickDateStr);
		BigDecimal salarySum = BigDecimal.ZERO;
		BigDecimal chengBenJineSum = BigDecimal.ZERO;
		for (SalaryDat salaryDat : salaryDats) {
			salarySum = salarySum.add(new BigDecimal(salaryDat.getBenQiShouRu()))
					.add(new BigDecimal(salaryDat.getJiBenYangLaoBaoXian()))
					.add(new BigDecimal(salaryDat.getJiBenYiLiaoBaoXian()))
					.add(new BigDecimal(salaryDat.getShiYeBaoXian()))
					.add(new BigDecimal(salaryDat.getZhuFangGongJiJin()));
		}
		List<PickStoreDat> pickDats = pickStoreDatService
				.selectList(new EntityWrapper<PickStoreDat>().eq("company_name", companyName).eq("open_date", openDate)
						.eq("pick_store_type", CamsConstant.PickStoreTypeEnum.PICK.getValue().toString())
						.eq("ke_mu", "4101-01"));
		for (PickStoreDat pickDat : pickDats) {
			chengBenJineSum = chengBenJineSum.add(new BigDecimal(pickDat.getChengBenJinE()));
		}
		for (PickStoreDat pickDat : pickDats) {
			// 占比
			BigDecimal zhanBi = BigDecimal.ZERO;
			// 分摊
			BigDecimal fenTan = BigDecimal.ZERO;
			// 新单价
			BigDecimal newDanJia = BigDecimal.ZERO;
			// 新合计
			BigDecimal newChengBenJinE = BigDecimal.ZERO;
			zhanBi = new BigDecimal(pickDat.getChengBenJinE()).divide(chengBenJineSum, 2, RoundingMode.HALF_UP);
			fenTan = salarySum.multiply(zhanBi);
			newDanJia = fenTan.add(new BigDecimal(pickDat.getChengBenJinE()))
					.divide(new BigDecimal(pickDat.getNumber()), 2, RoundingMode.HALF_UP);
			newChengBenJinE = newDanJia.multiply(new BigDecimal(pickDat.getNumber()));

			// 生成产成品
			PickStoreDat storeDat = ClassCopyUtil.copyObject(pickDat, PickStoreDat.class);
			storeDat.setPickStoreId(UUID.randomUUID().toString());
			storeDat.setPickStoreType(Integer.valueOf(CamsConstant.PickStoreTypeEnum.STORE.getValue().toString()));
			storeDat.setUnitPrice(newDanJia.toString());
			storeDat.setMoBan("产成本入库单");
			storeDat.setLocalAmount(newChengBenJinE.toString());
			storeDat.setOriginalAmount(newChengBenJinE.toString());
			storeDat.setPingJunChengBenJine(newChengBenJinE.toString());
			storeDat.setChengBenJinE(newChengBenJinE.toString());
			storeDat.setKeMu("1237");
			storeDat.setYeWuLeiXing("8");
			storeDat.setDanJuLeiXing("9");
			storeDat.setCreateTime(new Date());
			pickStoreDatService.insert(storeDat);
		}

	}

	@Override
	public void importSheBaoExcel(MultipartFile file, String companyName, String openDate) throws Exception {
		List<String[]> list = ReadExcelTools.readExcel(file);
		List<SalaryDat> salarys=new ArrayList();
		for (String[] arry : list) {
			if (StringUtil.isEmpty(arry[0])) {
				continue;
			}
			
			String name = arry[0];
			String cardNo = arry[1];
			String sheBaoHao = arry[3];
			String yangLaoBaoXian = arry[6];
			String shiYeBaoXian = arry[12];
			String yiLiaoBaoXian = arry[15];
			String geRenSheBaoSum="";
			String danWeiSheBaoSum="";
             if(arry.length>25) {
            	 danWeiSheBaoSum = arry[25];
                 geRenSheBaoSum = arry[26];
			}else if(arry.length==22){
				danWeiSheBaoSum = arry[19];
                geRenSheBaoSum = arry[20];
			}else {
    			 danWeiSheBaoSum = arry[22];
    			 geRenSheBaoSum = arry[23];
			}
		
			if (StringUtil.isEmpty(cardNo)) {
				continue;
			}
			SalaryDat salary = selectOne(new EntityWrapper<SalaryDat>().eq("company_name", companyName)
					.eq("salary_date", openDate).eq("idcard_no", cardNo));
			if (salary == null) {
				salary=new SalaryDat();
				salary.setCompanyName(companyName);
				salary.setName(name);
				salary.setDanWeiSheBaoSum(danWeiSheBaoSum);
				salary.setGeRenSheBaoSum(geRenSheBaoSum);
				salary.setSalaryDate(openDate);
				salary.setCreateTime(new Date());
				insert(salary);
				salarys.add(salary);
			}else {
//			salary.setJiBenYangLaoBaoXian(yangLaoBaoXian);
//			salary.setJiBenYiLiaoBaoXian(yiLiaoBaoXian);
//			salary.setShiYeBaoXian(shiYeBaoXian);
			salary.setDanWeiSheBaoSum(danWeiSheBaoSum);
			salary.setGeRenSheBaoSum(geRenSheBaoSum);
			updateById(salary);
			salarys.add(salary);
			}
		}
	    if(salarys.size()==0) {
	    	throw new ApplicationContextException("导入的社保的人员名单对应不上当月的工资人员名单");
	    }
		if (salarys.size() > 0) {
			insertSheBao(salarys, openDate, companyName);
		}

	}

	/**
	 * 
	 * 插入支付社保记录
	 * 
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	private void insertSheBao(List<SalaryDat> salarys, String openDate, String companyName) throws Exception {

		// 计算当月计提个人工资合计，单位工资合计
		BigDecimal geRenSheBaoSum = BigDecimal.ZERO;
		BigDecimal danWeiSheBaoSum = BigDecimal.ZERO;
		BigDecimal sheBaoSum = BigDecimal.ZERO;
		for (SalaryDat salaryDat : salarys) {

			danWeiSheBaoSum = danWeiSheBaoSum
					.add(new BigDecimal(salaryDat.getDanWeiSheBaoSum() == null ? "0" : salaryDat.getDanWeiSheBaoSum()));
			geRenSheBaoSum = geRenSheBaoSum
					.add(new BigDecimal(salaryDat.getGeRenSheBaoSum() == null ? "0" : salaryDat.getGeRenSheBaoSum()));
		}
		sheBaoSum = geRenSheBaoSum.add(danWeiSheBaoSum);
		String pingZhengHao = bankIncomeDatService.getPingZhengHao(companyName, DateUtil.getMonth(DateUtil.getDate(openDate)));

		BankIncomeDat geRendat = new BankIncomeDat();
		geRendat.setUuid(UUID.randomUUID().toString());
		geRendat.setPingZhengRiQi(openDate);
		geRendat.setPingZhengZi("记");
		geRendat.setPingZhengHao(pingZhengHao);
		geRendat.setZhaiYao(openDate+" "+"代垫个人社保");
		geRendat.setKeMuDaiMa(SubjectEnum.SHE_BAO_GE_REN_SUBJECT.getValue().toString());
		geRendat.setBankType("2");
		geRendat.setHuoBiDaiMa("RMB");
		geRendat.setHuiLv("1");
		geRendat.setShuLiang("0");
		geRendat.setDanJia("0");
		geRendat.setZhiDanRen("系统主管");
		geRendat.setShenHeRen("");
		geRendat.setGuoZhangRen("");
		geRendat.setFuDanJuShu("0");
		geRendat.setShiFouYiGuoZhang("0");
		geRendat.setMoBan("11记账凭证");
		geRendat.setCustomerCode("");
		geRendat.setBuMen("");
		geRendat.setYuanGong("");
		geRendat.setTongJi("");
		geRendat.setXiangMu("");
		geRendat.setFuKuanFangFa("");
		geRendat.setPiaoJuHao("");
		geRendat.setYuanBiFuKuanJinE("0");
		geRendat.setPingZhengLaiYuan("1");
		geRendat.setLaiYuanPingZheng(",,,");
		geRendat.setDaiDaYin("");
		geRendat.setHangHao("1");
		geRendat.setZuoFeiBiaoZhi("0");
		geRendat.setCuoWuBiaoZhi("0");
		geRendat.setPingZhengCeHao("00");
		geRendat.setPingZhengRiQi(openDate);
		geRendat.setChuNaRen("");
		geRendat.setCompanyName(companyName);
		geRendat.setDeductionType("1");
		geRendat.setQiJian(String.valueOf(DateUtil.getMonth(DateUtil.getDate(openDate))));
		geRendat.setCreateTime(new Date());
		geRendat.setLocalAmount(geRenSheBaoSum.toString());
		geRendat.setBorrowAmount(geRenSheBaoSum.toString());
		geRendat.setAlmsAmount("0");
		BankIncomeDat danWei = ClassCopyUtil.copyObject(geRendat, BankIncomeDat.class);
		danWei.setKeMuDaiMa(SubjectEnum.SHE_BAO_GONG_SI_SUBJECT.getValue().toString());
		danWei.setHangHao("2");
		danWei.setDeductionType("0");
		danWei.setLocalAmount(danWeiSheBaoSum.toString());
		danWei.setZhaiYao(openDate+" "+"支付本月社保");
		danWei.setBorrowAmount(danWeiSheBaoSum.toString());
		BankIncomeDat sumDat = ClassCopyUtil.copyObject(geRendat, BankIncomeDat.class);
		sumDat.setHangHao("3");
		sumDat.setKeMuDaiMa(SubjectEnum.ZHI_FU_SUBJECT.getValue().toString());
		sumDat.setLocalAmount(sheBaoSum.toString());
		sumDat.setZhaiYao(openDate+" "+"支付本月社保");
		sumDat.setBorrowAmount("0");
		sumDat.setDeductionType("0");
		sumDat.setAlmsAmount(sheBaoSum.toString());
		bankIncomeDatService.insert(geRendat);
		bankIncomeDatService.insert(danWei);
		bankIncomeDatService.insert(sumDat);
	}
	
	/**
	 * 
	 * 插入银付公积金记录
	 * 
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	@Override
	@Transactional
	public void insertGongJiJin(List<SalaryDat> salarys, String openDate, String companyName) throws Exception {

		// 计算当月公积金合计合计
		BigDecimal gongJiJinSum = BigDecimal.ZERO;
		for (SalaryDat salaryDat : salarys) {

			gongJiJinSum = gongJiJinSum
					.add(new BigDecimal(salaryDat.getZhuFangGongJiJin() == null ? "0" : salaryDat.getZhuFangGongJiJin()));
		
		}
		if(gongJiJinSum.intValue()==0) {
			return;
		}
		String pingZhengHao = bankIncomeDatService.getPingZhengHao(companyName, DateUtil.getMonth(DateUtil.getDate(openDate)));

		BankIncomeDat geRendat = new BankIncomeDat();
		geRendat.setUuid(UUID.randomUUID().toString());
		geRendat.setBankType("2"); //银付
		geRendat.setPingZhengRiQi(openDate);
		geRendat.setPingZhengZi("记");
		geRendat.setPingZhengHao(pingZhengHao);
		geRendat.setZhaiYao("个人部分公积金");
		geRendat.setKeMuDaiMa(SubjectEnum.GONG_JI_JIN_GE_REN_SUBJECT.getValue().toString());
		geRendat.setHuoBiDaiMa("RMB");
		geRendat.setHuiLv("1");
		geRendat.setShuLiang("0");
		geRendat.setDanJia("0");
		geRendat.setZhiDanRen("系统主管");
		geRendat.setShenHeRen("");
		geRendat.setGuoZhangRen("");
		geRendat.setFuDanJuShu("0");
		geRendat.setShiFouYiGuoZhang("0");
		geRendat.setMoBan("11记账凭证");
		geRendat.setCustomerCode("");
		geRendat.setBuMen("");
		geRendat.setYuanGong("");
		geRendat.setTongJi("");
		geRendat.setXiangMu("");
		geRendat.setFuKuanFangFa("");
		geRendat.setPiaoJuHao("");
		geRendat.setYuanBiFuKuanJinE("0");
		geRendat.setPingZhengLaiYuan("1");
		geRendat.setLaiYuanPingZheng(",,,");
		geRendat.setDaiDaYin("");
		geRendat.setHangHao("1");
		geRendat.setZuoFeiBiaoZhi("0");
		geRendat.setCuoWuBiaoZhi("0");
		geRendat.setPingZhengCeHao("00");
		geRendat.setChuNaRen("");
		geRendat.setCompanyName(companyName);
		geRendat.setDeductionType("1");
		geRendat.setQiJian(String.valueOf(DateUtil.getMonth(DateUtil.getDate(openDate))));
		geRendat.setCreateTime(new Date());
		geRendat.setLocalAmount(gongJiJinSum.toString());
		geRendat.setBorrowAmount(gongJiJinSum.toString());
		geRendat.setAlmsAmount("0");
		BankIncomeDat danWei = ClassCopyUtil.copyObject(geRendat, BankIncomeDat.class);
		danWei.setKeMuDaiMa(SubjectEnum.GONG_JI_JIN_GONG_SI_SUBJECT.getValue().toString());
		danWei.setHangHao("2");
		danWei.setDeductionType("0");
		danWei.setLocalAmount(gongJiJinSum.toString());
		danWei.setZhaiYao("公司部分公积金");
		danWei.setBorrowAmount(gongJiJinSum.toString());
		BankIncomeDat sumDat = ClassCopyUtil.copyObject(geRendat, BankIncomeDat.class);
		sumDat.setHangHao("3");
		sumDat.setKeMuDaiMa(SubjectEnum.ZHI_FU_SUBJECT.getValue().toString());
		sumDat.setLocalAmount(gongJiJinSum.toString());
		sumDat.setZhaiYao("支付公积金");
		sumDat.setBorrowAmount("0");
		sumDat.setDeductionType("0");
		sumDat.setAlmsAmount(gongJiJinSum.multiply(new BigDecimal(2)).toString());
		bankIncomeDatService.insert(geRendat);
		bankIncomeDatService.insert(danWei);
		bankIncomeDatService.insert(sumDat);
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

	@Override
	public Map<String, BigDecimal> getPersonSalaryBefore(String cardNo, String openDate, String companyName)
			throws Exception {
		Map<String, BigDecimal> map=new HashMap<String, BigDecimal>();
		List<SalaryDat> salarys=selectList(new EntityWrapper<SalaryDat>().eq("idcard_no", cardNo).eq("company_name", companyName).le("salary_date", openDate));
		//工资累计总和
		BigDecimal benQiShouRuSum=BigDecimal.ZERO;
		//免税收入累计总和
		BigDecimal benQiMianShuiShouRuSum=BigDecimal.ZERO;
		//当前月之前已交税额的累计总和
		BigDecimal yiJiaoShuiESum=BigDecimal.ZERO;
		//已扣费用项累计总和
		BigDecimal beforeDeductionSum=BigDecimal.ZERO;
		//三险一金总和
		BigDecimal sheBaoGongJiJinSum=BigDecimal.ZERO;
		//减除费用标准
		BigDecimal jianChuFeiYongBiaoZhunSum=BigDecimal.ZERO;
		for(SalaryDat dat:salarys) {
			benQiShouRuSum=benQiShouRuSum.add(new BigDecimal(dat.getBenQiShouRu()));
			benQiMianShuiShouRuSum=benQiMianShuiShouRuSum.add(new BigDecimal(dat.getBenQiMianShuiShouRu()));
			yiJiaoShuiESum=yiJiaoShuiESum.add(new BigDecimal(dat.getTax()));
			sheBaoGongJiJinSum=sheBaoGongJiJinSum.add(new BigDecimal(dat.getJiBenYangLaoBaoXian()).add(new BigDecimal(dat.getJiBenYiLiaoBaoXian())).add(new BigDecimal(dat.getShiYeBaoXian())).add(new BigDecimal(dat.getZhuFangGongJiJin())));
			jianChuFeiYongBiaoZhunSum=jianChuFeiYongBiaoZhunSum.add(new BigDecimal(dat.getJianChuFeiYongBiaoZhun()));
		}
		//获取最近月份的工资
		SalaryDat salary=selectOne(new EntityWrapper<SalaryDat>().eq("idcard_no", cardNo).eq("company_name", companyName).eq("salary_date", openDate));
		beforeDeductionSum=beforeDeductionSum.add(new BigDecimal(salary.getLeiJiZiNvJiaoYu()).add(new BigDecimal(salary.getLeiJiZhuFangDaiKuanLiXi())
				.add(new BigDecimal(salary.getLeiJiShanYangLaoRen())).add(new BigDecimal(salary.getLeiJiJiXuJiaoYu())).add(new BigDecimal(salary.getQiYeNianJin()))
				.add(new BigDecimal(salary.getShangYeJianKangBaoXian())).add(new BigDecimal(salary.getShuiYanYangLaoBaoXian())).add(new BigDecimal(salary.getQiTa()))
				.add(new BigDecimal(salary.getZhunYuKouChuJuanZengKuan()))));
		map.put("benQiShouRuSum", benQiShouRuSum);
		map.put("benQiMianShuiShouRuSum", benQiMianShuiShouRuSum);
		map.put("yiJiaoShuiESum", yiJiaoShuiESum);
		map.put("beforeDeductionSum", beforeDeductionSum);
		map.put("sheBaoGongJiJinSum", sheBaoGongJiJinSum);
		map.put("jianChuFeiYongBiaoZhunSum", jianChuFeiYongBiaoZhunSum);
		return map;
	}

	@Override
	public void createPersonTax(List<SalaryDat> salaryDats) throws Exception {
		BigDecimal allPersonTaxSum=BigDecimal.ZERO;
		String companyName="";
		String openDate="";
		for (SalaryDat dat : salaryDats) {
			companyName=dat.getCompanyName();
			openDate=dat.getSalaryDate();
				allPersonTaxSum=allPersonTaxSum.add(new BigDecimal(dat.getTax()));
			
		}
//		//支付模块插入个人所得税科目记录
		jtDatService.addGeRenSuoDeShui(companyName, allPersonTaxSum, openDate);
	}

}
