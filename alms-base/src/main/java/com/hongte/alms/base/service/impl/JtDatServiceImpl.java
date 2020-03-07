package com.hongte.alms.base.service.impl;

import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContextException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.hongte.alms.base.entity.BankIncomeDat;
import com.hongte.alms.base.entity.CamsCompany;
import com.hongte.alms.base.entity.CustomerDat;
import com.hongte.alms.base.entity.FeeDat;
import com.hongte.alms.base.entity.JtDat;
import com.hongte.alms.base.entity.PickStoreDat;
import com.hongte.alms.base.entity.ProductDat;
import com.hongte.alms.base.enums.SubjectEnum;
import com.hongte.alms.base.invoice.vo.InvoiceBuyExel;
import com.hongte.alms.base.invoice.vo.TaxExcel;
import com.hongte.alms.base.mapper.JtDatMapper;
import com.hongte.alms.base.service.BankIncomeDatService;
import com.hongte.alms.base.service.CamsCompanyService;
import com.hongte.alms.base.service.CustomerDatService;
import com.hongte.alms.base.service.JtDatService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.CamsUtil;
import com.hongte.alms.common.util.ClassCopyUtil;
import com.hongte.alms.common.util.StringUtil;
import com.ht.ussp.util.DateUtil;

/**
 * <p>
 * 计提表 服务实现类
 * </p>
 *
 * @author czs
 * @since 2019-09-01
 */
@Service("JtDatService")
public class JtDatServiceImpl extends BaseServiceImpl<JtDatMapper, JtDat> implements JtDatService {

	@Autowired
	@Qualifier("CamsCompanyService")
	private CamsCompanyService camsCompanyService;
	

	@Autowired
	@Qualifier("CustomerDatService")
	private CustomerDatService customerDatService;
	
	@Autowired
	@Qualifier("BankIncomeDatService")
	private BankIncomeDatService bankIncomeDatService;
	
	@Transactional
	@Override
	public void importTax(MultipartFile file,  String type, String openDate) throws Exception {
		String fileName = file.getName();
		ImportParams importParams = new ImportParams();
		//String pingZhengHao = getPingZhengHao(companyName, openDate);
		List<TaxExcel> excels = ExcelImportUtil.importExcel(file.getInputStream(), TaxExcel.class, importParams);
		for (TaxExcel taxExcel : excels) {

			CamsCompany company=camsCompanyService.selectOne(new EntityWrapper<CamsCompany>().eq("company_name", taxExcel.getCompanyName()));
		
			if(company==null) {
				continue; 
			}
			
			Map<SubjectEnum,String> map=new HashMap<SubjectEnum,String>();
			if(!StringUtil.isEmpty(taxExcel.getChenJianShui())&&!taxExcel.getChenJianShui().equals("0")&&!taxExcel.getChenJianShui().equals("0.0")
					&&!taxExcel.getChenJianShui().equals("暂无核税种")&&!taxExcel.getChenJianShui().equals("（自主有税申报）")&&!taxExcel.getChenJianShui().equals("免税")) {
				map.put(SubjectEnum.SHUI_JIN_2171_08, taxExcel.getChenJianShui());
			}
			if(!StringUtil.isEmpty(taxExcel.getDiFangJiaoYuFuJiaShui())&&!taxExcel.getDiFangJiaoYuFuJiaShui().equals("0")&&!taxExcel.getDiFangJiaoYuFuJiaShui().equals("0.0")
					&&!taxExcel.getDiFangJiaoYuFuJiaShui().equals("暂无核税种")&&!taxExcel.getDiFangJiaoYuFuJiaShui().equals("（自主有税申报）")&&!taxExcel.getDiFangJiaoYuFuJiaShui().equals("免税")) {
				map.put(SubjectEnum.SHUI_JIN_2171_14, taxExcel.getDiFangJiaoYuFuJiaShui());
			}
			if(!StringUtil.isEmpty(taxExcel.getJiaoYuFuJiaShui())&&!taxExcel.getJiaoYuFuJiaShui().equals("0")&&!taxExcel.getJiaoYuFuJiaShui().equals("0.0")
					&&!taxExcel.getJiaoYuFuJiaShui().equals("暂无核税种")&&!taxExcel.getJiaoYuFuJiaShui().equals("（自主有税申报）")&&!taxExcel.getJiaoYuFuJiaShui().equals("免税")) {
				map.put(SubjectEnum.SHUI_JIN_2171_13, taxExcel.getJiaoYuFuJiaShui());
			}
			if(!StringUtil.isEmpty(taxExcel.getYinHuaShui())&&!taxExcel.getYinHuaShui().equals("0")&&!taxExcel.getYinHuaShui().equals("0.0")
					&&!taxExcel.getYinHuaShui().equals("暂无核税种")&&!taxExcel.getYinHuaShui().equals("（自主有税申报）")&&!taxExcel.getYinHuaShui().equals("免税")) {
				BigDecimal shuie=new BigDecimal(taxExcel.getYinHuaShui());
				shuie=shuie.setScale(1, BigDecimal.ROUND_HALF_UP);
				map.put(SubjectEnum.SHUI_JIN_2171_15, shuie.toString());
			}
			if(!StringUtil.isEmpty(taxExcel.getQiYeSuoDeShui())&&!taxExcel.getQiYeSuoDeShui().equals("0")&&!taxExcel.getQiYeSuoDeShui().equals("0.0")&&!taxExcel.getQiYeSuoDeShui().equals("查账")
					&&!taxExcel.getQiYeSuoDeShui().equals("暂无核税种")&&!taxExcel.getQiYeSuoDeShui().equals("（自主有税申报）")&&!taxExcel.getQiYeSuoDeShui().equals("免税")&&!taxExcel.getQiYeSuoDeShui().equals("核定")
					&&!taxExcel.getQiYeSuoDeShui().equals("总公司申报")) {
				map.put(SubjectEnum.SHUI_JIN_2171_06, taxExcel.getQiYeSuoDeShui());
			}
			if(!StringUtil.isEmpty(taxExcel.getXiaoFeiShui())&&!taxExcel.getXiaoFeiShui().equals("0")&&!taxExcel.getXiaoFeiShui().equals("0.0")&&!taxExcel.getXiaoFeiShui().equals("查账")
					&&!taxExcel.getXiaoFeiShui().equals("暂无核税种")&&!taxExcel.getXiaoFeiShui().equals("（自主有税申报）")&&!taxExcel.getXiaoFeiShui().equals("免税")&&!taxExcel.getXiaoFeiShui().equals("核定")
					&&!taxExcel.getXiaoFeiShui().equals("总公司申报")) {
				map.put(SubjectEnum.SHUI_JIN_2171_04, taxExcel.getQiYeSuoDeShui());
			}
//			if(!StringUtil.isEmpty(taxExcel.getZengZhiShui())&&!taxExcel.getZengZhiShui().equals("0")&&!taxExcel.getZengZhiShui().equals("0.0")
//					&&!taxExcel.getZengZhiShui().equals("暂无核税种")&&!taxExcel.getZengZhiShui().equals("（自主有税申报）")&&!taxExcel.getZengZhiShui().equals("免税")) {
//				map.put(SubjectEnum.SHUI_JIN_2171_02, taxExcel.getZengZhiShui());
//			}
			
			if(!StringUtil.isEmpty(taxExcel.getWenHuaShiYejianSheShui())&&!taxExcel.getWenHuaShiYejianSheShui().equals("0")&&!taxExcel.getWenHuaShiYejianSheShui().equals("0.0")
					&&!taxExcel.getWenHuaShiYejianSheShui().equals("暂无核税种")&&!taxExcel.getWenHuaShiYejianSheShui().equals("（自主有税申报）")&&!taxExcel.getWenHuaShiYejianSheShui().equals("免税")) {
				map.put(SubjectEnum.SHUI_JIN_2171_16, taxExcel.getWenHuaShiYejianSheShui());
			}
			
			if(!StringUtil.isEmpty(taxExcel.getChengZhenTuDiShui())&&!taxExcel.getChengZhenTuDiShui().equals("0")&&!taxExcel.getChengZhenTuDiShui().equals("0.0")
					&&!taxExcel.getChengZhenTuDiShui().equals("暂无核税种")&&!taxExcel.getChengZhenTuDiShui().equals("（自主有税申报）")&&!taxExcel.getChengZhenTuDiShui().equals("免税")) {
				map.put(SubjectEnum.SHUI_JIN_2171_10, taxExcel.getChengZhenTuDiShui());
			}
			
			if(!StringUtil.isEmpty(taxExcel.getFangChanShui())&&!taxExcel.getFangChanShui().equals("0")&&!taxExcel.getFangChanShui().equals("0.0")
					&&!taxExcel.getFangChanShui().equals("暂无核税种")&&!taxExcel.getFangChanShui().equals("（自主有税申报）")&&!taxExcel.getFangChanShui().equals("免税")) {
				map.put(SubjectEnum.SHUI_JIN_2171_09, taxExcel.getFangChanShui());
			}
			if(!StringUtil.isEmpty(taxExcel.getZengZhiShui())&&!taxExcel.getZengZhiShui().equals("0")&&!taxExcel.getZengZhiShui().equals("0.0")
					&&!taxExcel.getZengZhiShui().equals("暂无核税种")&&!taxExcel.getZengZhiShui().equals("（自主有税申报）")&&!taxExcel.getZengZhiShui().equals("免税")) {
				map.put(SubjectEnum.SHUI_JIN_2171_02, taxExcel.getZengZhiShui());
			}
			if(!StringUtil.isEmpty(taxExcel.getCanJiRenBaoZhangJin())&&!taxExcel.getCanJiRenBaoZhangJin().equals("0")&&!taxExcel.getCanJiRenBaoZhangJin().equals("0.0")
					&&!taxExcel.getCanJiRenBaoZhangJin().equals("暂无核税种")&&!taxExcel.getCanJiRenBaoZhangJin().equals("（自主有税申报）")&&!taxExcel.getCanJiRenBaoZhangJin().equals("免税")) {
				map.put(SubjectEnum.SHUI_JIN_5502_23, taxExcel.getCanJiRenBaoZhangJin());
			}
			if(map.size()==0) {
				continue;
			}
			Integer i=1;
			BigDecimal shuiJinSum=BigDecimal.ZERO;
//			dat=bankIncomeDatService.selectOne(new EntityWrapper<BankIncomeDat>().eq("ping_zheng_ri_qi", openDate).eq("ke_mu_dai_ma", SubjectEnum.SHUI_JIN_2171_12.getValue().toString()).eq("company_name", company.getCompanyName()));
//			if(dat==null) {
//				throw new ApplicationContextException("请先导入"+company.getCompanyName()+"的个人所得税");
//			}
			String pingZhengHao=getPingZhengHao(company.getCompanyName(), openDate);
			JtDat dat1 =null;
			List<JtDat> jtInsertlist=new ArrayList<JtDat>();
			List<JtDat> tranferBankImcomelist=new ArrayList<JtDat>();
		    for(Map.Entry<SubjectEnum, String> entry:map.entrySet()) {
		    	SubjectEnum mapKey = entry.getKey();
		    	 String mapValue = entry.getValue();
		    		BigDecimal localJine = new BigDecimal(mapValue).setScale(2, RoundingMode.UP);
		    		dat1= new JtDat();
					dat1.setUuid(UUID.randomUUID().toString());
					dat1.setPingZhengRiQi(openDate);
					dat1.setPingZhengZi("记");
					dat1.setPingZhengHao(pingZhengHao);
					dat1.setZhaiYao(mapKey.getDesc());
					dat1.setKeMuDaiMa(mapKey.getValue().toString());
					dat1.setJtType(type);
					dat1.setHuoBiDaiMa("RMB");
					dat1.setHuiLv("1");
					dat1.setShuLiang("0");
					dat1.setDanJia("0");
					dat1.setZhiDanRen("系统主管");
					dat1.setShenHeRen("");
					dat1.setGuoZhangRen("");
					dat1.setFuDanJuShu("0");
					dat1.setShiFouYiGuoZhang("0");
					dat1.setMoBan("11记账凭证");
					dat1.setCustomerCode("");
					dat1.setBuMen("");
					dat1.setYuanGong("");
					dat1.setTongJi("");
					dat1.setXiangMu("");
					dat1.setFuKuanFangFa("");
					dat1.setPiaoJuHao("");
					dat1.setYuanBiFuKuanJinE("0");
					dat1.setPingZhengLaiYuan("1");
					dat1.setLaiYuanPingZheng(",,,");
					dat1.setDaiDaYin("");
					dat1.setHangHao(i.toString());
					dat1.setZuoFeiBiaoZhi("0");
					dat1.setCuoWuBiaoZhi("0");
					dat1.setPingZhengCeHao("00");
					dat1.setOpenDate(openDate);
					dat1.setChuNaRen("");
					dat1.setCompanyName(company.getCompanyName());
					dat1.setDeductionType("1");
					dat1.setQiJian(String.valueOf(DateUtil.getMonth(DateUtil.getDate(openDate))));
					dat1.setCreateTime(new Date());
				    dat1.setLocalAmount(localJine.toString());
					dat1.setBorrowAmount("0");
					dat1.setAlmsAmount(localJine.toString());
					dat1.setIsDeal(0);
				
				 	 
				 	 //如果是企业所得税，要单独有一条5701贷的分录
				 	 if(dat1.getKeMuDaiMa().equals(SubjectEnum.SHUI_JIN_2171_06.getValue())||dat1.getKeMuDaiMa().equals(SubjectEnum.SHUI_JIN_2171_10.getValue())
				 			||dat1.getKeMuDaiMa().equals(SubjectEnum.SHUI_JIN_2171_09.getValue())||dat1.getKeMuDaiMa().equals(SubjectEnum.SHUI_JIN_2171_16.getValue())) {
				 		continue;
				 	 }
				 
				 	 
				     //增值税不用计提
				 if(!dat1.getKeMuDaiMa().equals(SubjectEnum.SHUI_JIN_2171_02.getValue())&&!dat1.getKeMuDaiMa().equals(SubjectEnum.SHUI_JIN_5502_23.getValue())) {
						jtInsertlist.add(dat1);
					    shuiJinSum=shuiJinSum.add(localJine); 
				 }else {
					 i--;
				 }
				
					tranferBankImcomelist.add(dat1);
				 	 i++;
		    }
		    if(jtInsertlist.size()>0) {
		    	JtDat copyDat = ClassCopyUtil.copyObject(dat1, JtDat.class);
				copyDat.setLocalAmount(shuiJinSum.toString());
				copyDat.setBorrowAmount(shuiJinSum.toString());
				copyDat.setAlmsAmount("0");
				copyDat.setZhaiYao("计提税金");
				copyDat.setKeMuDaiMa("5402");
				jtInsertlist.add(0, copyDat);
				tranferBankImcomelist.add(copyDat);
				//行号重新排序
				Integer j=1;
				for(JtDat jtDat:jtInsertlist) {
					jtDat.setHangHao(j.toString());
					insert(jtDat);
					j++;
				}
		    }
			
			i=addJt(dat1, map, SubjectEnum.SHUI_JIN_2171_06, i, openDate, pingZhengHao, type, company,tranferBankImcomelist);
			i=addJt(dat1, map, SubjectEnum.SHUI_JIN_2171_10, i, openDate, pingZhengHao, type, company,tranferBankImcomelist);
			i=addJt(dat1, map, SubjectEnum.SHUI_JIN_2171_09, i, openDate, pingZhengHao, type, company,tranferBankImcomelist);
			i=addJt(dat1, map, SubjectEnum.SHUI_JIN_2171_16, i, openDate, pingZhengHao, type, company,tranferBankImcomelist);
			//计提税金转换下个月支付
			if(tranferBankImcomelist.size()>0) {
				bankIncomeDatService.tranferJtTaxToZhiFu(tranferBankImcomelist);
			}
		
		}
		
		
	}
	/**
	 * 增加待抵扣税计提记录
	 * @param localJine
	 * @param invoiceNumber
	 * @param openDate
	 * @param pingZhengHao
	 * @param type
	 * @param companyName
	 * @throws Exception
	 */
	@Override
	public void addTaxJt(String localJine,String invoiceNumber, String openDate,
			String pingZhengHao,String type,String companyName) throws Exception {

		
		   JtDat dat1 = new JtDat();
			dat1.setUuid(UUID.randomUUID().toString());
			dat1.setPingZhengRiQi(openDate);
			dat1.setPingZhengZi("记");
			dat1.setPingZhengHao(pingZhengHao);
			dat1.setZhaiYao("结转待抵扣进项税额"+" "+invoiceNumber);
			dat1.setKeMuDaiMa("2171-20");
			dat1.setJtType(type);
			dat1.setHuoBiDaiMa("RMB");
			dat1.setHuiLv("1");
			dat1.setShuLiang("0");
			dat1.setDanJia("0");
			dat1.setZhiDanRen("系统主管");
			dat1.setShenHeRen("");
			dat1.setGuoZhangRen("");
			dat1.setFuDanJuShu("0");
			dat1.setShiFouYiGuoZhang("0");
			dat1.setMoBan("11记账凭证");
			dat1.setCustomerCode("");
			dat1.setBuMen("");
			dat1.setYuanGong("");
			dat1.setTongJi("");
			dat1.setXiangMu("");
			dat1.setFuKuanFangFa("");
			dat1.setPiaoJuHao("");
			dat1.setYuanBiFuKuanJinE("0");
			dat1.setPingZhengLaiYuan("1");
			dat1.setLaiYuanPingZheng(",,,");
			dat1.setDaiDaYin("");
			dat1.setHangHao("1");
			dat1.setZuoFeiBiaoZhi("0");
			dat1.setCuoWuBiaoZhi("0");
			dat1.setPingZhengCeHao("00");
			dat1.setOpenDate(openDate);
			dat1.setChuNaRen("");
			dat1.setCompanyName(companyName);
			dat1.setDeductionType("1");
			dat1.setQiJian(String.valueOf(DateUtil.getMonth(DateUtil.getDate(openDate))));
			dat1.setCreateTime(new Date());
			dat1.setLocalAmount(localJine.toString());
			dat1.setBorrowAmount("0");
			dat1.setAlmsAmount(localJine.toString());
			dat1.setIsDeal(0);

		 JtDat copyDat = ClassCopyUtil.copyObject(dat1, JtDat.class);
		copyDat.setLocalAmount(dat1.getLocalAmount());
		copyDat.setBorrowAmount(dat1.getLocalAmount());
		copyDat.setHangHao("2");
		copyDat.setAlmsAmount("0");
		copyDat.setZhaiYao("结转待抵扣进项税额"+" "+invoiceNumber);
		copyDat.setKeMuDaiMa("2171-01-01");
		copyDat.setHangHao(String.valueOf((Integer.valueOf(copyDat.getHangHao())+1)));
		insert(copyDat);
		insert(dat1);
	
}
		private int addJt(JtDat dat1,Map<SubjectEnum,String> map,SubjectEnum subjectEnum,Integer hangHao,String openDate,
				String pingZhengHao,String type,CamsCompany company,List<JtDat> tranferBankImcomelist) throws Exception {
			List<JtDat> jtInsertlist=new ArrayList();
			    String mapValue=map.get(subjectEnum);
			    if(StringUtil.isEmpty(mapValue)) {
			    	return hangHao;
			    }
			
				BigDecimal localJine = new BigDecimal(mapValue).setScale(2, RoundingMode.UP);
				dat1 = new JtDat();
				dat1.setUuid(UUID.randomUUID().toString());
				dat1.setPingZhengRiQi(openDate);
				dat1.setPingZhengZi("记");
				dat1.setPingZhengHao(pingZhengHao);
				dat1.setZhaiYao(subjectEnum.getDesc());
				dat1.setKeMuDaiMa(subjectEnum.getValue().toString());
				dat1.setJtType(type);
				dat1.setHuoBiDaiMa("RMB");
				dat1.setHuiLv("1");
				dat1.setShuLiang("0");
				dat1.setDanJia("0");
				dat1.setZhiDanRen("系统主管");
				dat1.setShenHeRen("");
				dat1.setGuoZhangRen("");
				dat1.setFuDanJuShu("0");
				dat1.setShiFouYiGuoZhang("0");
				dat1.setMoBan("11记账凭证");
				dat1.setCustomerCode("");
				dat1.setBuMen("");
				dat1.setYuanGong("");
				dat1.setTongJi("");
				dat1.setXiangMu("");
				dat1.setFuKuanFangFa("");
				dat1.setPiaoJuHao("");
				dat1.setYuanBiFuKuanJinE("0");
				dat1.setPingZhengLaiYuan("1");
				dat1.setLaiYuanPingZheng(",,,");
				dat1.setDaiDaYin("");
				dat1.setHangHao(hangHao.toString());
				dat1.setZuoFeiBiaoZhi("0");
				dat1.setCuoWuBiaoZhi("0");
				dat1.setPingZhengCeHao("00");
				dat1.setOpenDate(openDate);
				dat1.setChuNaRen("");
				dat1.setCompanyName(company.getCompanyName());
				dat1.setDeductionType("1");
				dat1.setQiJian(String.valueOf(DateUtil.getMonth(DateUtil.getDate(openDate))));
				dat1.setCreateTime(new Date());
				dat1.setLocalAmount(localJine.toString());
				dat1.setBorrowAmount("0");
				dat1.setAlmsAmount(localJine.toString());
				dat1.setIsDeal(0);

				jtInsertlist.add(dat1);
				tranferBankImcomelist.add(dat1);
				hangHao++;
			 JtDat copyDat = ClassCopyUtil.copyObject(dat1, JtDat.class);
			copyDat.setLocalAmount(dat1.getLocalAmount());
			copyDat.setBorrowAmount(dat1.getLocalAmount());
			copyDat.setAlmsAmount("0");
			copyDat.setZhaiYao(subjectEnum.getDesc());
			copyDat.setKeMuDaiMa(subjectEnum.getDesc2());
			copyDat.setHangHao(hangHao.toString());
			insert(copyDat);
			tranferBankImcomelist.add(copyDat);
			for(JtDat jtDat:jtInsertlist) {
				jtDat.setHangHao((++hangHao).toString());
				insert(jtDat);
			}
			return ++hangHao;
	}

	
	@Override
	public String getPingZhengHao(String companyName, String openDate) {
		String pingZhengHao = "";
		/* 获取凭证号 */
		Wrapper<JtDat> wrapperDat = new EntityWrapper<JtDat>();
		wrapperDat.eq("company_name", companyName);
		wrapperDat.eq("open_date", openDate);
		wrapperDat.orderBy("ping_zheng_hao", false);
		List<JtDat> jtDats = selectList(wrapperDat);
		if (jtDats.size() == 0) {
			pingZhengHao = "500";
		} else {
			pingZhengHao = CamsUtil.generateCode(jtDats.get(0).getPingZhengHao());
		}
		return pingZhengHao;
	}
	
	@Override
	public void addGeRenSuoDeShui(String companyName, BigDecimal tax, String openDate) throws Exception {
	    if(tax==null||tax.toString().equals("0")) {
	    	return;
	    }

		//获取下月的日期
		Date lastDate=DateUtil.addMonth2Date(1, DateUtil.getDate(openDate));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cale = Calendar.getInstance();
		cale.setTime(lastDate);
		// 获取下月的最后一天
		cale.add(Calendar.MONTH, 1);
		cale.set(Calendar.DAY_OF_MONTH, 0);
		lastDate=cale.getTime();
		String lastPingZhengRiQi=DateUtil.formatDate(lastDate);
		String pingZhengHao =bankIncomeDatService.getPingZhengHao(companyName, DateUtil.getMonth(DateUtil.getDate(lastPingZhengRiQi)));

		BankIncomeDat geRendat = new BankIncomeDat();
		geRendat.setUuid(UUID.randomUUID().toString());
		geRendat.setPingZhengRiQi(lastPingZhengRiQi);
		geRendat.setBankType("2");
		geRendat.setPingZhengZi("记");
		geRendat.setPingZhengHao(pingZhengHao);
		geRendat.setZhaiYao(openDate+" "+"支付个人所得税");
		geRendat.setKeMuDaiMa(SubjectEnum.YING_JIAO_GE_REN_SUBJECT.getValue().toString());
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
		geRendat.setLocalAmount(tax.toString());
		geRendat.setBorrowAmount(tax.toString());
		geRendat.setAlmsAmount("0");
		BankIncomeDat bankDat = ClassCopyUtil.copyObject(geRendat, BankIncomeDat.class);
		bankDat.setAlmsAmount(tax.toString());
		bankDat.setBorrowAmount("0");
		bankDat.setZhaiYao(openDate+" "+"支付上月税金");
		bankDat.setHangHao("2");
		bankDat.setKeMuDaiMa(SubjectEnum.ZHI_FU_SUBJECT.getValue().toString());
		bankIncomeDatService.insert(geRendat);
		bankIncomeDatService.insert(bankDat);
	}

	@Override
	public void importZiChan(MultipartFile file, String companyName, String openDate) throws Exception {
		ImportParams importParams = new ImportParams();
		List<InvoiceBuyExel> excels = ExcelImportUtil.importExcel(file.getInputStream(), InvoiceBuyExel.class,
				importParams);
		//***先删除所有发票号码开始**************
		HashSet<String> set=new HashSet<String> ();
		for(InvoiceBuyExel excel:excels) {
			set.add(excel.getFaPiaoHaoMa());
		}
	    for(String faPiaoHaoMa:set) {
	    	delete(new EntityWrapper<JtDat>().eq("company_name", companyName).eq("invoice_number", faPiaoHaoMa));
	    }
	  //***先删除所有发票号码结束**************
		String productCode = "";
		ProductDat productDat = null;
		String kuaiJiQiJian = "";
		String kaiPiaoRiQi = "";
		String customerCode = "";
		String invoiceNumber = "";
		String produceDate = "";
		for (int i = 0; i < excels.size(); i++) {
			if (excels.get(i).getKaiPiaoRiQi() != null) {
				kaiPiaoRiQi = openDate;
			}
			if (excels.get(i).getFaPiaoHaoMa() != null) {
				CustomerDat customerDat = customerDatService.addCustomerDat(excels.get(i).getXiaoFangMingChen(), "2",
						companyName,kaiPiaoRiQi);
				customerCode = customerDat.getCustomerCode();
				invoiceNumber = excels.get(i).getFaPiaoHaoMa();
				produceDate = excels.get(i).getKaiPiaoRiQi();
				addZiChan(excels.get(i).getChanPinMingChen(), companyName, customerDat,
						excels.get(i).getShuLiang(), kaiPiaoRiQi, invoiceNumber, excels.get(i).getFaPiaoZhongLei(),
						excels.get(i).getHanShuiJine(), excels.get(i).getBuHanShuiJine(), excels.get(i).getShuie(),
						excels.get(i).getDanJia());
			}
		}
		
	}

	@Override
	public void addZiChan(String feeName, String companyName, CustomerDat customerDat, String shuLiang,
			String openDate, String faPiaoHao, String buyType, String hanShuiJine, String buHanShuiJine,
			String shuie, String danJia) throws Exception {
		String pingZhengHao = getPingZhengHao(companyName, openDate);

//		JtDat geRendat = new JtDat();
//		geRendat.setUuid(UUID.randomUUID().toString());
//		geRendat.setPingZhengRiQi(openDate);
//		geRendat.setOpenDate(openDate);
//		geRendat.setPingZhengZi("记");
//		geRendat.setPingZhengHao(pingZhengHao);
//		geRendat.setZhaiYao(feeName);
//		geRendat.setKeMuDaiMa("");
//		geRendat.setHuoBiDaiMa("RMB");
//		geRendat.setHuiLv("1");
//		geRendat.setJtType("3");
//		geRendat.setShuLiang(shuLiang);
//		geRendat.setDanJia(danJia);
//		geRendat.setZhiDanRen("系统主管");
//		geRendat.setShenHeRen("");
//		geRendat.setGuoZhangRen("");
//		geRendat.setFuDanJuShu("0");
//		geRendat.setShiFouYiGuoZhang("0");
//		geRendat.setMoBan("11记账凭证");
//		geRendat.setCustomerCode("");
//		geRendat.setBuMen("");
//		geRendat.setYuanGong("");
//		geRendat.setTongJi("");
//		geRendat.setXiangMu("");
//		geRendat.setFuKuanFangFa("");
//		geRendat.setPiaoJuHao("");
//		geRendat.setYuanBiFuKuanJinE("0");
//		geRendat.setPingZhengLaiYuan("1");
//		geRendat.setLaiYuanPingZheng(",,,");
//		geRendat.setDaiDaYin("");
//		geRendat.setHangHao("1");
//		geRendat.setZuoFeiBiaoZhi("0");
//		geRendat.setCuoWuBiaoZhi("0");
//		geRendat.setPingZhengCeHao("00");
//		geRendat.setChuNaRen("");
//		geRendat.setCompanyName(companyName);
//		geRendat.setDanJia(danJia);
//		geRendat.setDeductionType("1");
//		geRendat.setQiJian(String.valueOf(DateUtil.getMonth(DateUtil.getDate(openDate))));
//		geRendat.setCreateTime(new Date());
//		geRendat.setLocalAmount(hanShuiJine);
//		geRendat.setBorrowAmount("");
//		geRendat.setAlmsAmount(tax.toString());
//		insert(geRendat);
		
	}

}
