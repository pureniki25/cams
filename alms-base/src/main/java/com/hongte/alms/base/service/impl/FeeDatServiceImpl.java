package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.hongte.alms.base.entity.CamsCash;
import com.hongte.alms.base.entity.CamsSubject;
import com.hongte.alms.base.entity.CustomerDat;
import com.hongte.alms.base.entity.FeeDat;
import com.hongte.alms.base.entity.ProductDat;
import com.hongte.alms.base.enums.CamsConstant;
import com.hongte.alms.base.enums.FeeTypeEnum;
import com.hongte.alms.base.enums.SubjectEnum;
import com.hongte.alms.base.enums.CamsConstant.ProductTypeEnum;
import com.hongte.alms.base.mapper.FeeDatMapper;
import com.hongte.alms.base.service.FeeDatService;
import com.hongte.alms.base.service.ProductDatService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.CamsUtil;
import com.hongte.alms.common.util.ClassCopyUtil;
import com.hongte.alms.common.util.StringUtil;
import com.ht.ussp.util.DateUtil;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 费用表 服务实现类
 * </p>
 *
 * @author czs
 * @since 2019-03-02
 */
@Service("FeeDatService")
public class FeeDatServiceImpl extends BaseServiceImpl<FeeDatMapper, FeeDat> implements FeeDatService {
	@Autowired
	@Qualifier("ProductDatService")
	ProductDatService productDatService;
	
	@SuppressWarnings("unused")
	@Override
	public void addFeeDat(String feeName, String companyName, CustomerDat customerDat, String shuLiang,
			String produceDate, String faPiaoHao, String buyType, String hanShuiJine, String buHanShuiJine,
			String shuie, String danJia,String feeType) throws InstantiationException, IllegalAccessException {
        String uuid=UUID.randomUUID().toString();
 		String customerCode = customerDat.getCustomerCode();
		feeName = CamsUtil.getProductName(feeName);
		if (!StringUtil.isEmpty(shuLiang)) {// 保留2位小数
			DecimalFormat df = new DecimalFormat("#0.00");
			shuLiang = df.format(Double.valueOf(shuLiang)).toString();
		}
		int month = DateUtil.getMonth(DateUtil.getDate(produceDate));

		String pingZhengHao = "";
		boolean isExistSamePingZheng = false;
		/* 获取凭证号 */
		Wrapper<FeeDat> wrapperFeeDat = new EntityWrapper<FeeDat>();
		wrapperFeeDat.eq("company_name", companyName);
		wrapperFeeDat.eq("fee_type", feeType);
		wrapperFeeDat.and("month(ping_zheng_ri_qi)=" + month);
		wrapperFeeDat.orderBy("ping_zheng_hao", false);
		List<FeeDat> feeDats = selectList(wrapperFeeDat);
		if (feeDats.size() == 0) {
			if(CamsConstant.FeeTypeEnum.FEI_YONG_XIAN_FU.getValue().toString().equals(feeType)) {
				pingZhengHao="150"; 
			}else {
				pingZhengHao = "100";
			}
	
		} else {
			pingZhengHao = getSamePingZhengHao(companyName, customerCode, month,feeType);
			if (pingZhengHao == null) {
				pingZhengHao = getSamePingZhengHao(companyName, customerCode, month,feeType);
				pingZhengHao = CamsUtil.generateCode(feeDats.get(0).getPingZhengHao());
			}else if(feeType.equals(CamsConstant.FeeTypeEnum.SELL.getValue().toString())||feeType.equals(CamsConstant.FeeTypeEnum.BUY.getValue().toString())) {
				pingZhengHao = CamsUtil.generateCode(feeDats.get(0).getPingZhengHao());
			} else {
				pingZhengHao = getSamePingZhengHao(companyName, customerCode, month,feeType);
				isExistSamePingZheng = true;// 存在需要合并的凭证
			}

		}
		

		FeeDat keMuDaiMa = selectOne(new EntityWrapper<FeeDat>().eq("company_name", companyName)
				.eq("customer_code", customerCode).eq("zhai_yao", feeName).eq("fee_type", feeType));
		FeeDat feeDat = new FeeDat();
		feeDat.setPingZhengRiQi(produceDate);
		feeDat.setPingZhengZi("记");
		feeDat.setFeeType("1");
		feeDat.setPingZhengHao(pingZhengHao);
		feeDat.setZhaiYao(feeName);
		if (keMuDaiMa != null) {
			feeDat.setKeMuDaiMa(keMuDaiMa.getKeMuDaiMa());
		}
		feeDat.setHuoBiDaiMa("RMB");
		feeDat.setLocalTax(shuie);
		feeDat.setHuiLv("1");
		feeDat.setFeeType(feeType);
		feeDat.setShuLiang(shuLiang);
		feeDat.setDanJia(danJia);
		feeDat.setZhiDanRen("系统主管");
		feeDat.setShenHeRen("");
		feeDat.setGuoZhangRen("");
		feeDat.setFuDanJuShu("0");
		feeDat.setShiFouYiGuoZhang("0");
		feeDat.setMoBan("11记账凭证");
		feeDat.setCustomerCode(customerCode);
		feeDat.setBuMen("");
		feeDat.setYuanGong("");
		feeDat.setTongJi("");
		feeDat.setXiangMu("");
		feeDat.setFuKuanFangFa("");
		feeDat.setPiaoJuHao("");
		feeDat.setYuanBiFuKuanJinE("0");
		feeDat.setPingZhengLaiYuan("1");
		feeDat.setLaiYuanPingZheng(",,,");
		feeDat.setDaiDaYin("");
		feeDat.setZuoFeiBiaoZhi("0");
		feeDat.setCuoWuBiaoZhi("0");
		feeDat.setPingZhengCeHao("00");
		feeDat.setChuNaRen("");
		feeDat.setCompanyName(companyName);
		feeDat.setDeductionType("1");
		feeDat.setCustomerCode(customerCode);
		feeDat.setInvoiceNumber(faPiaoHao);
		feeDat.setQiJian(String.valueOf(month));
		feeDat.setCreateTime(new Date());
		feeDat.setUuid(uuid);
		
		SubjectEnum subjectEnum=null;
		//判断费用类型，如果是费用现付，科目代码都是1001
		if(feeType.equals(FeeTypeEnum.FEE_PAY.getValue().toString())) {
			subjectEnum=SubjectEnum.CASH_SUBJECT;
		}else {
			subjectEnum=SubjectEnum.CUSTOMER_SUBJECT;
		}
		if (("普票").equals(buyType)) {
			feeDat.setDeductionType("1");// 不能抵扣
			feeDat.setBuyType("普票");
			feeDat.setLocalAmount(hanShuiJine);
			feeDat.setBorrowAmount(hanShuiJine);
			feeDat.setAlmsAmount("0.00");

			FeeDat customerFeeDat = ClassCopyUtil.copyObject(feeDat, FeeDat.class);
			customerFeeDat.setZhaiYao(customerDat.getCustomerName());
			customerFeeDat.setDeductionType("0");
			customerFeeDat.setDanWei(customerDat.getCustomerCode());
			customerFeeDat.setAlmsAmount(hanShuiJine);
			customerFeeDat.setLocalAmount(hanShuiJine);
			customerFeeDat.setKeMuDaiMa(subjectEnum.getValue().toString());
			customerFeeDat.setBorrowAmount("0.00");

			if (isExistSamePingZheng) {
				String hangHao = getHangHao(companyName, customerCode, month, pingZhengHao,feeType);
				feeDat.setHangHao(addHangHao(hangHao));
				customerFeeDat.setHangHao(addHangHao(addHangHao(hangHao)));
			} else {
				feeDat.setHangHao("1");
				customerFeeDat.setHangHao("2");
			}
			insertOrUpdate(feeDat);
			insertOrUpdate(customerFeeDat);

		} else if ("专票".equals(buyType)) {
			feeDat.setDeductionType("1");// 不能抵扣
			feeDat.setBuyType("专票");
			feeDat.setLocalAmount(buHanShuiJine);
			feeDat.setBorrowAmount(buHanShuiJine);
			feeDat.setAlmsAmount("0.00");

			FeeDat waitTaxFeeDat = ClassCopyUtil.copyObject(feeDat, FeeDat.class);
			waitTaxFeeDat.setZhaiYao("待抵扣进项税额" + faPiaoHao);
			waitTaxFeeDat.setLocalAmount(shuie);
			waitTaxFeeDat.setDeductionType("0");
			waitTaxFeeDat.setBorrowAmount(shuie);
			waitTaxFeeDat.setKeMuDaiMa(SubjectEnum.WAIT_TAX_SUBJECT.getValue().toString());

			FeeDat customerFeeDat = ClassCopyUtil.copyObject(feeDat, FeeDat.class);
			customerFeeDat.setZhaiYao(customerDat.getCustomerName());
			customerFeeDat.setDanWei(customerDat.getCustomerCode());
			customerFeeDat.setDeductionType("0");
			customerFeeDat.setAlmsAmount(hanShuiJine);
			customerFeeDat.setKeMuDaiMa(subjectEnum.getValue().toString());
			customerFeeDat.setLocalAmount(hanShuiJine);
			customerFeeDat.setBorrowAmount("0.00");
			if (isExistSamePingZheng) {
				String hangHao = getHangHao(companyName, customerCode, month, pingZhengHao,feeType);
				feeDat.setHangHao(addHangHao(hangHao));
				waitTaxFeeDat.setHangHao(addHangHao(addHangHao(hangHao)));
				customerFeeDat.setHangHao(addHangHao(addHangHao(addHangHao(hangHao))));
			} else {
				feeDat.setHangHao("1");
				waitTaxFeeDat.setHangHao("2");
				customerFeeDat.setHangHao("3");
			}
			insertOrUpdate(feeDat);
			insertOrUpdate(waitTaxFeeDat);
			insertOrUpdate(customerFeeDat);
		}
		//如果是4107或4201-01，要结转成本
		if(keMuDaiMa!=null) {
		    if(keMuDaiMa.equals("4107")) {
	        	FeeDat chengBenFeeDat = ClassCopyUtil.copyObject(feeDat, FeeDat.class);
	    		chengBenFeeDat.setZhaiYao("结转劳务成本");
	    		chengBenFeeDat.setDeductionType("0");
	    		chengBenFeeDat.setAlmsAmount("0.00");
	    		chengBenFeeDat.setLocalAmount(hanShuiJine);
	    		chengBenFeeDat.setKeMuDaiMa(SubjectEnum.JIE_ZHUAN_CHENG_BEN_SUBJECT.getValue().toString());
	    		chengBenFeeDat.setBorrowAmount(hanShuiJine);
	    		String pingZhengH=Integer.valueOf(chengBenFeeDat.getPingZhengCeHao())+1+"";
	    		chengBenFeeDat.setPingZhengCeHao(pingZhengH);
	    		chengBenFeeDat.setHangHao("4");
	    		insertOrUpdate(chengBenFeeDat);
	        }
	     if(keMuDaiMa.equals("4201-01")) {
	        	FeeDat chengBenFeeDat = ClassCopyUtil.copyObject(feeDat, FeeDat.class);
	    		chengBenFeeDat.setZhaiYao("结转工程成本");
	    		chengBenFeeDat.setDeductionType("0");
	    		chengBenFeeDat.setAlmsAmount("0.00");
	    		chengBenFeeDat.setLocalAmount(hanShuiJine);
	    		chengBenFeeDat.setKeMuDaiMa(SubjectEnum.JIE_ZHUAN_CHENG_BEN_SUBJECT.getValue().toString());
	    		chengBenFeeDat.setBorrowAmount(hanShuiJine);
	    		String pingZhengH=Integer.valueOf(chengBenFeeDat.getPingZhengCeHao())+1+"";
	    		chengBenFeeDat.setPingZhengCeHao(pingZhengH);
	    		chengBenFeeDat.setHangHao("4");
	    		insertOrUpdate(chengBenFeeDat);
	        }
		}
	 
		

	}

	@Override
	public void addSellPingZheng(String feeName, String companyName, CustomerDat customerDat, String shuLiang,
			String produceDate, String faPiaoHao, String buyType, String hanShuiJine, String buHanShuiJine,
			String shuie, String danJia, String feeType,String productCode) throws InstantiationException, IllegalAccessException {
        String uuid=UUID.randomUUID().toString();
 		String customerCode = customerDat.getCustomerCode();
		feeName = CamsUtil.getProductName(feeName);
		if (!StringUtil.isEmpty(shuLiang)) {// 保留2位小数
			DecimalFormat df = new DecimalFormat("#0.00");
			shuLiang = df.format(Double.valueOf(shuLiang)).toString();
		}
		int month = DateUtil.getMonth(DateUtil.getDate(produceDate));

		String pingZhengHao = "";
		boolean isExistSamePingZheng = false;
		/* 获取凭证号 */
		Wrapper<FeeDat> wrapperFeeDat = new EntityWrapper<FeeDat>();
		wrapperFeeDat.eq("company_name", companyName);
		wrapperFeeDat.eq("fee_type", feeType);
		wrapperFeeDat.and("month(ping_zheng_ri_qi)=" + month);
		wrapperFeeDat.orderBy("ping_zheng_hao", false);
		List<FeeDat> feeDats = selectList(wrapperFeeDat);
		if (feeDats.size() == 0) {
			if(CamsConstant.FeeTypeEnum.SELL.getValue().toString().equals(feeType)) {
				pingZhengHao="500"; 
			}else {
				pingZhengHao = "600";
			}
	
		} else {
			pingZhengHao = getSamePingZhengHao(companyName, customerCode, month,feeType);
			if (pingZhengHao == null) {
				pingZhengHao = getSamePingZhengHao(companyName, customerCode, month,feeType);
				pingZhengHao = CamsUtil.generateCode(feeDats.get(0).getPingZhengHao());
			}else {
				pingZhengHao = CamsUtil.generateCode(feeDats.get(0).getPingZhengHao());
		    }
		}
		

		FeeDat keMuDaiMa = selectOne(new EntityWrapper<FeeDat>().eq("company_name", companyName)
				.eq("customer_code", customerCode).eq("zhai_yao", feeName).eq("fee_type", feeType));
		FeeDat feeDat = new FeeDat();
		feeDat.setPingZhengRiQi(produceDate);
		feeDat.setPingZhengZi("记");
		feeDat.setFeeType("1");
		feeDat.setPingZhengHao(pingZhengHao);
		feeDat.setZhaiYao(feeName);
		feeDat.setHuoBiDaiMa("RMB");
		feeDat.setLocalTax(shuie);
		feeDat.setHuiLv("1");
		feeDat.setDanWei(customerCode);
		feeDat.setFeeType(feeType);
		feeDat.setShuLiang(shuLiang);
		feeDat.setDanJia(danJia);
		feeDat.setZhiDanRen("系统主管");
		feeDat.setShenHeRen("");
		feeDat.setGuoZhangRen("");
		feeDat.setFuDanJuShu("0");
		feeDat.setShiFouYiGuoZhang("0");
		feeDat.setMoBan("11记账凭证");
		feeDat.setCustomerCode(customerCode);
		feeDat.setBuMen("");
		feeDat.setYuanGong("");
		feeDat.setTongJi("");
		feeDat.setXiangMu("");
		feeDat.setFuKuanFangFa("");
		feeDat.setPiaoJuHao("");
		feeDat.setYuanBiFuKuanJinE("0");
		feeDat.setPingZhengLaiYuan("1");
		feeDat.setLaiYuanPingZheng(",,,");
		feeDat.setDaiDaYin("");
		feeDat.setZuoFeiBiaoZhi("0");
		feeDat.setCuoWuBiaoZhi("0");
		feeDat.setPingZhengCeHao("00");
		feeDat.setChuNaRen("");
		feeDat.setCompanyName(companyName);
		feeDat.setDeductionType("1");
		feeDat.setCustomerCode(customerCode);
		feeDat.setInvoiceNumber(faPiaoHao);
		feeDat.setQiJian(String.valueOf(month));
		feeDat.setCreateTime(new Date());
		feeDat.setUuid(uuid);
		FeeDat buHanShuiDat = ClassCopyUtil.copyObject(feeDat, FeeDat.class);
		FeeDat shuieDat = ClassCopyUtil.copyObject(feeDat, FeeDat.class);
		feeDat.setDeductionType("1");// 不能抵扣
		ProductDat product=productDatService.selectOne(new EntityWrapper<ProductDat>().eq("product_code", productCode).eq("company_name", companyName));
		if(CamsConstant.FeeTypeEnum.SELL.getValue().toString().equals(feeType)) {
			feeDat.setBuyType("销售发票");
			feeDat.setKeMuDaiMa(SubjectEnum.CLIENT_SUBJECT.getValue().toString());
			shuieDat.setKeMuDaiMa("2171-01-05"); //销项税额科目
			buHanShuiDat.setKeMuDaiMa("5101-01");
		}else {
			feeDat.setBuyType("采购发票");
			feeDat.setKeMuDaiMa(SubjectEnum.CUSTOMER_SUBJECT.getValue().toString());
			shuieDat.setKeMuDaiMa("2171-01-01"); //应交税金-进项税额
			if(product.getProductProperties().equals(ProductTypeEnum.SHANG_PIN.getValue().toString())
					||product.getProductProperties().equals(ProductTypeEnum.CHAN_PIN.getValue().toString())) {
				buHanShuiDat.setKeMuDaiMa("1243");
			}else {
				buHanShuiDat.setKeMuDaiMa("1211");
			}
	
		}
		feeDat.setLocalAmount(hanShuiJine);
		feeDat.setBorrowAmount(hanShuiJine);
		feeDat.setAlmsAmount("0.00");
		

	
		buHanShuiDat.setDeductionType("0");
		buHanShuiDat.setLocalAmount(buHanShuiJine);
		buHanShuiDat.setBorrowAmount("0.00");
		buHanShuiDat.setAlmsAmount(buHanShuiJine);
	


		shuieDat.setDeductionType("0");
		shuieDat.setLocalAmount(shuie);
		shuieDat.setBorrowAmount("0.00");
		shuieDat.setAlmsAmount(shuie);
	
		feeDat.setHangHao("1");
		buHanShuiDat.setHangHao("2");
		shuieDat.setHangHao("3");
		insertOrUpdate(feeDat);
		insertOrUpdate(buHanShuiDat);
		if(!shuie.equals("0")&&!shuie.equals("0.00")) { //如果税额为0，不插入记录
			insertOrUpdate(shuieDat);	
		}
	}

	@Override
	public void addBuyPingZheng(String feeName, String companyName, CustomerDat customerDat, String shuLiang,
			String productDate, String faPiaoHao, String buyType, String hanShuiJine, String buHanShuiJine,
			String shuie, String danJia, String feeType) throws InstantiationException, IllegalAccessException {
		// TODO Auto-generated method stub
		
	}
	
	
	private String getSamePingZhengHao(String companyName, String customerCode, int month,String feeType) {
		Wrapper<FeeDat> wrapperFeeDat = new EntityWrapper<FeeDat>();
		wrapperFeeDat.eq("company_name", companyName);
		wrapperFeeDat.eq("customer_code", customerCode);
		wrapperFeeDat.eq("fee_type", feeType);
		wrapperFeeDat.and("month(ping_zheng_ri_qi)=" + month);
		List<FeeDat> feeDats = selectList(wrapperFeeDat);
		if (feeDats.size() > 0) {
			return feeDats.get(0).getPingZhengHao();
		} else {
			return null;
		}
	}

	private String getCashSamePingZhengHao(String companyName, int month,String type) {
		Wrapper<FeeDat> wrapperFeeDat = new EntityWrapper<FeeDat>();
		wrapperFeeDat.eq("company_name", companyName);
		wrapperFeeDat.eq("fee_type", type);
		wrapperFeeDat.and("month(ping_zheng_ri_qi)=" + month);
		List<FeeDat> feeDats = selectList(wrapperFeeDat);
		if (feeDats.size() > 0) {
			return feeDats.get(0).getPingZhengHao();
		} else {
			return null;
		}
	}

	private String getHangHao(String companyName, String customerCode, int month, String pingZhengHao,String feeType) {
		String hanghao = "";
		Wrapper<FeeDat> wrapperFeeDat = new EntityWrapper<FeeDat>();
		wrapperFeeDat.eq("company_name", companyName);
		wrapperFeeDat.eq("customer_code", customerCode);
		wrapperFeeDat.eq("ping_zheng_hao", pingZhengHao);
		wrapperFeeDat.eq("fee_type", feeType);
		wrapperFeeDat.and("month(ping_zheng_ri_qi)=" + month);
		wrapperFeeDat.orderBy("hang_hao", false);
		List<FeeDat> feeDats = selectList(wrapperFeeDat);
		if (feeDats.size() > 0) {
			hanghao = feeDats.get(0).getHangHao();
		}

		return hanghao;
	}

	private String getCashHangHao(String companyName, int month, String pingZhengHao) {
		String hanghao = "";
		Wrapper<FeeDat> wrapperFeeDat = new EntityWrapper<FeeDat>();
		wrapperFeeDat.eq("company_name", companyName);
		wrapperFeeDat.eq("fee_type", "2");
		wrapperFeeDat.eq("ping_zheng_hao", pingZhengHao);
		wrapperFeeDat.and("month(ping_zheng_ri_qi)=" + month);
		wrapperFeeDat.orderBy("hang_hao", false);
		List<FeeDat> feeDats = selectList(wrapperFeeDat);
		if (feeDats.size() > 0) {
			hanghao = feeDats.get(0).getHangHao();
		}

		return hanghao;
	}

	private String addHangHao(String hangHao) {
		return String.valueOf((Integer.valueOf(hangHao) + 1));

	}

	@Override
	public void deleteFee(String openDate, String companyName) throws Exception {
		delete(new EntityWrapper<FeeDat>().eq("company_name", companyName).eq("ping_zheng_ri_qi", openDate));
	}

	@Override
	public void saveCash(String feeName, String companyName, String produceDate, String danJia, String keMuDaiMa,String type)
			throws Exception {
		int month = DateUtil.getMonth(DateUtil.getDate(produceDate));
		produceDate=DateUtil.formatDate(DateUtil.getDate(produceDate));
		String pingZhengHao = ""; 
		String uuid=UUID.randomUUID().toString();
		/* 获取凭证号 */
		Wrapper<FeeDat> wrapperFeeDat = new EntityWrapper<FeeDat>();
		wrapperFeeDat.eq("company_name", companyName);
		wrapperFeeDat.eq("fee_type", "2");
		wrapperFeeDat.and("month(ping_zheng_ri_qi)=" + month);
		wrapperFeeDat.orderBy("ping_zheng_hao", false);
		List<FeeDat> feeDats = selectList(wrapperFeeDat);
		if (feeDats.size() == 0) {
			pingZhengHao = "300";
		} else {
			pingZhengHao = getCashSamePingZhengHao(companyName, month,type);
			if (pingZhengHao != null) {
				pingZhengHao = CamsUtil.generateCode(feeDats.get(0).getPingZhengHao());
			}

		}
		// 获取行号
		// todo
		FeeDat feeDat = new FeeDat();
		feeDat.setPingZhengRiQi(produceDate);
		feeDat.setPingZhengZi("记");
		feeDat.setPingZhengHao(pingZhengHao);
		feeDat.setZhaiYao(feeName);
		feeDat.setKeMuDaiMa(keMuDaiMa);
		feeDat.setHuoBiDaiMa("RMB");
		feeDat.setHuiLv("1");
		feeDat.setShuLiang("0");
		feeDat.setFeeType("2");
		feeDat.setDanJia("0");
		feeDat.setZhiDanRen("系统主管");
		feeDat.setShenHeRen("");
		feeDat.setGuoZhangRen("");
		feeDat.setFuDanJuShu("0");
		feeDat.setShiFouYiGuoZhang("0");
		feeDat.setMoBan("11记账凭证");
		feeDat.setCustomerCode("");
		feeDat.setBuMen("");
		feeDat.setYuanGong("");
		feeDat.setTongJi("");
		feeDat.setXiangMu("");
		feeDat.setFuKuanFangFa("");
		feeDat.setPiaoJuHao("");
		feeDat.setYuanBiFuKuanJinE("0");
		feeDat.setPingZhengLaiYuan("1");
		feeDat.setLaiYuanPingZheng(",,,");
		feeDat.setDaiDaYin("");
		feeDat.setZuoFeiBiaoZhi("0");
		feeDat.setCuoWuBiaoZhi("0");
		feeDat.setPingZhengCeHao("00");
		feeDat.setChuNaRen("");
		feeDat.setCompanyName(companyName);
		feeDat.setDeductionType("1");
		feeDat.setCustomerCode("");
		feeDat.setInvoiceNumber("");
		feeDat.setQiJian(String.valueOf(month));
		feeDat.setCreateTime(new Date());
		feeDat.setUuid(uuid);

		feeDat.setDeductionType("1");// 不能抵扣
		feeDat.setBuyType("");
		feeDat.setLocalAmount(danJia);
		feeDat.setBorrowAmount(danJia);
		feeDat.setAlmsAmount("0.00");

		FeeDat customerFeeDat = ClassCopyUtil.copyObject(feeDat, FeeDat.class);
		customerFeeDat.setDeductionType("0");
		customerFeeDat.setAlmsAmount(danJia);
		customerFeeDat.setLocalAmount("0.00");
		customerFeeDat.setKeMuDaiMa(SubjectEnum.CASH_SUBJECT.getValue().toString());
		customerFeeDat.setBorrowAmount("0.00");

		feeDat.setHangHao("1");
		customerFeeDat.setHangHao("2");

		insertOrUpdate(feeDat);
		insertOrUpdate(customerFeeDat);

	}

	@Override
	public void saveShuiJin(List<CamsSubject> list,String companyName, String produceDate, String type) throws Exception {
		int month = DateUtil.getMonth(DateUtil.getDate(produceDate));
		produceDate=DateUtil.formatDate(DateUtil.getDate(produceDate));
		String pingZhengHao = ""; 

		/* 获取凭证号 */
		Wrapper<FeeDat> wrapperFeeDat = new EntityWrapper<FeeDat>();
		wrapperFeeDat.eq("company_name", companyName);
		wrapperFeeDat.eq("fee_type", type);
		wrapperFeeDat.and("month(ping_zheng_ri_qi)=" + month);
		wrapperFeeDat.orderBy("ping_zheng_hao", false);
		List<FeeDat> feeDats = selectList(wrapperFeeDat);
		if (feeDats.size() == 0) {
			pingZhengHao = "400";
		} else {
			pingZhengHao = getCashSamePingZhengHao(companyName, month,type);
			if (pingZhengHao != null) {
				pingZhengHao = CamsUtil.generateCode(feeDats.get(0).getPingZhengHao());
			}

		}
		
		
		List<FeeDat> fees=new ArrayList();
		BigDecimal jine=BigDecimal.ZERO;
		//税金凭证
		for (CamsSubject camsSubject : list) {
			String uuid=UUID.randomUUID().toString();
			if (StringUtil.isEmpty(camsSubject.getJine())) {
				continue;
			}
		
			jine=jine.add(new BigDecimal(camsSubject.getJine()));
			// 获取行号
			// todo
			FeeDat feeDat = new FeeDat();
			feeDat.setPingZhengRiQi(produceDate);
			feeDat.setPingZhengZi("记");
			feeDat.setPingZhengHao(pingZhengHao);
			feeDat.setZhaiYao(camsSubject.getSubjectName());
			feeDat.setKeMuDaiMa(camsSubject.getId());
			feeDat.setHuoBiDaiMa("RMB");
			feeDat.setHuiLv("1");
			feeDat.setShuLiang("0");
			feeDat.setFeeType(type);
			feeDat.setDanJia("0");
			feeDat.setZhiDanRen("系统主管");
			feeDat.setShenHeRen("");
			feeDat.setGuoZhangRen("");
			feeDat.setFuDanJuShu("0");
			feeDat.setShiFouYiGuoZhang("0");
			feeDat.setMoBan("11记账凭证");
			feeDat.setCustomerCode("");
			feeDat.setBuMen("");
			feeDat.setYuanGong("");
			feeDat.setTongJi("");
			feeDat.setXiangMu("");
			feeDat.setFuKuanFangFa("");
			feeDat.setPiaoJuHao("");
			feeDat.setYuanBiFuKuanJinE("0");
			feeDat.setPingZhengLaiYuan("1");
			feeDat.setLaiYuanPingZheng(",,,");
			feeDat.setDaiDaYin("");
			feeDat.setZuoFeiBiaoZhi("0");
			feeDat.setCuoWuBiaoZhi("0");
			feeDat.setPingZhengCeHao("00");
			feeDat.setChuNaRen("");
			feeDat.setCompanyName(companyName);
			feeDat.setDeductionType("1");
			feeDat.setCustomerCode("");
			feeDat.setInvoiceNumber("");
			feeDat.setQiJian(String.valueOf(month));
			feeDat.setCreateTime(new Date());
			feeDat.setUuid(uuid);

			feeDat.setDeductionType("1");// 不能抵扣
			feeDat.setBuyType("");
			feeDat.setLocalAmount(camsSubject.getJine());
			feeDat.setBorrowAmount(camsSubject.getJine());
			feeDat.setAlmsAmount("0.00");
			feeDat.setHangHao("1");
			fees.add(feeDat);
		}
		if(fees.size()>0) {
			FeeDat customerFeeDat = ClassCopyUtil.copyObject(fees.get(0), FeeDat.class);
			customerFeeDat.setDeductionType("0");
			customerFeeDat.setAlmsAmount(jine.toString());
			customerFeeDat.setLocalAmount("0.00");
			customerFeeDat.setKeMuDaiMa(SubjectEnum.CASH_SUBJECT.getValue().toString());
			customerFeeDat.setBorrowAmount("0.00");
			customerFeeDat.setHangHao("1");
            fees.add(customerFeeDat);
		    insertBatch(fees);
		}
		//计提凭证
		List<FeeDat> jiTis=new ArrayList<FeeDat>();
		BigDecimal jine5402=BigDecimal.ZERO;
		for (FeeDat jiTiDat : fees) {
		    if(jiTiDat.getKeMuDaiMa().equals("2171-12")||jiTiDat.getKeMuDaiMa().equals("2171-15")||jiTiDat.getKeMuDaiMa().equals("2171-14")
		    		||jiTiDat.getKeMuDaiMa().equals("2171-13")||jiTiDat.getKeMuDaiMa().equals("2171-08")||jiTiDat.getKeMuDaiMa().equals("2171-02")
		    		||jiTiDat.getKeMuDaiMa().equals("2171-06")) {
		    	FeeDat copyDat = ClassCopyUtil.copyObject(jiTiDat, FeeDat.class);
		    	jine5402=jine5402.add(new BigDecimal(copyDat.getBorrowAmount()));
		    	copyDat.setDeductionType("0");
		    	copyDat.setZhaiYao(CamsConstant.JiTiSubjectEnum.getName(jiTiDat.getKeMuDaiMa()));
		    	copyDat.setAlmsAmount(copyDat.getBorrowAmount());
		    	copyDat.setBorrowAmount("0.00");
		    	copyDat.setPingZhengHao(Integer.valueOf(pingZhengHao)+1+"");
		    	copyDat.setHangHao("1");
		    	jiTis.add(copyDat);
		    }
			
		}
		if(jiTis.size()>0) {
			FeeDat dat5402 = ClassCopyUtil.copyObject(jiTis.get(0), FeeDat.class);
			dat5402.setZhaiYao(CamsConstant.JiTiSubjectEnum.SHUI_JIN_5402.getDesc());
			dat5402.setKeMuDaiMa(CamsConstant.JiTiSubjectEnum.SHUI_JIN_5402.getValue().toString());
			dat5402.setBorrowAmount(jine5402.toString());
			dat5402.setLocalAmount(jine5402.toString());
			dat5402.setAlmsAmount("0.00");
			jiTis.add(dat5402);
			  insertBatch(jiTis);
		}
		
 

		
	}

	@Override
	public String getFeePingZhengHao(String companyName, String customerCode, String date, String feeType)
			throws Exception {
		int month = DateUtil.getMonth(DateUtil.getDate(date));

		String pingZhengHao = "";
		/* 获取凭证号 */
		Wrapper<FeeDat> wrapperFeeDat = new EntityWrapper<FeeDat>();
		wrapperFeeDat.eq("company_name", companyName);
		wrapperFeeDat.eq("fee_type", feeType);
		wrapperFeeDat.and("month(ping_zheng_ri_qi)=" + month);
		wrapperFeeDat.orderBy("ping_zheng_hao", false);
		List<FeeDat> feeDats = selectList(wrapperFeeDat);
		if (feeDats.size() == 0) {
			if(CamsConstant.FeeTypeEnum.FEI_YONG_XIAN_FU.getValue().toString().equals(feeType)) {
				pingZhengHao="150";
			}else {
				pingZhengHao = "100";
			}
	
		} else {
				pingZhengHao = CamsUtil.generateCode(feeDats.get(0).getPingZhengHao());

		}

		return pingZhengHao;
	}


}
