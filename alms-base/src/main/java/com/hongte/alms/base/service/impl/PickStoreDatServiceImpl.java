package com.hongte.alms.base.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.hongte.alms.base.entity.CamsSubject;
import com.hongte.alms.base.entity.PickStoreDat;
import com.hongte.alms.base.entity.SellDat;
import com.hongte.alms.base.invoice.vo.PickExcel;
import com.hongte.alms.base.mapper.PickStoreDatMapper;
import com.hongte.alms.base.service.PickStoreDatService;
import com.hongte.alms.base.service.SellDatService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.CamsUtil;
import com.ht.ussp.util.DateUtil;

/**
 * <p>
 * 领料入库表 服务实现类
 * </p>
 *
 * @author czs
 * @since 2019-07-22
 */
@Service("PickStoreDatService")
public class PickStoreDatServiceImpl extends BaseServiceImpl<PickStoreDatMapper, PickStoreDat>
		implements PickStoreDatService {
	private static final Logger LOGGER = LoggerFactory.getLogger(PickStoreDatServiceImpl.class);
	
	static final String PICK_TYPE="1"; //领料
	static final String STORE_TYPE="2"; //入库

	@Autowired
	@Qualifier("PickStoreDatService")
	PickStoreDatService pickStoreDatService;
	
	@Autowired
	@Qualifier("SellDatService")
	SellDatService sellDatService;


	@Override
	public void importPick(MultipartFile file, String companyName, String type, String openDate,CamsSubject subject) throws Exception {
		String fileName = file.getName();
		ImportParams importParams = new ImportParams();
		List<PickExcel> excels = ExcelImportUtil.importExcel(file.getInputStream(), PickExcel.class, importParams);
		String documentNo="";
		/*获取单据号 */
		 Wrapper<PickStoreDat> wrapperPickStoreDat= new EntityWrapper<PickStoreDat>();
		 wrapperPickStoreDat.eq("company_name",companyName);
		 wrapperPickStoreDat.eq("open_date",openDate);
		 wrapperPickStoreDat.eq("pick_store_type",type);
		 wrapperPickStoreDat.orderBy("document_no",false); 
			 List<PickStoreDat> pickStoreDats=selectList(wrapperPickStoreDat);
			 if(pickStoreDats.size()==0) {
				 documentNo="0001";
			 }else {
			     documentNo= CamsUtil.generateCode(pickStoreDats.get(0).getDocumentNo());
			 }
		for (int i = 0; i < excels.size(); i++) {
			PickStoreDat dat = new PickStoreDat();
		
			dat.setKuaiJiNianDu(DateUtil.getYear(new Date()) + "");
			dat.setAccountPeriod(DateUtil.getMonth(DateUtil.getDate(openDate)) + "");
			dat.setDocumentNo(documentNo); //单据号
			dat.setCustomerCode("");
			dat.setCompanyName(companyName);
			dat.setProduceDate(openDate);
			dat.setZhiDanRen("系统主管");
			dat.setHuiLv("1");
			dat.setBiZhong("RMB");
			dat.setDueDate(openDate);
			dat.setDaoQiRiQi(openDate);
			dat.setOpenDate(openDate);
			dat.setFaPiaoLeiXing("0");
			dat.setKaiPiaoBiaoZhi("0");
			dat.setRowNumber(i +1+ "");
			dat.setProduceCode(excels.get(i).getProductCode());
			dat.setHuoWei("01");
			dat.setCalUnit(excels.get(i).getUnit().trim());
			dat.setNumber(excels.get(i).getShuLiang().trim());
			dat.setUnitPrice(excels.get(i).getDanJia().trim());
			dat.setKouLv("100");
			dat.setOriginalAmount(excels.get(i).getJine().trim()); //原币金额
			dat.setLocalAmount(excels.get(i).getJine().trim()); //本币金额
			dat.setOriginalTax("0");
			dat.setLocalhostTax("0");
			dat.setBaoZhiQi("0");
			dat.setChengBenChaYi("0");
			dat.setChengBenJinE(excels.get(i).getJine().trim());
			dat.setGuanBiBiaoZhi("0");
			dat.setDaiDaYinBiaoZhi("0");
			dat.setDaiShiXianXiaoXiangShui("0");
			dat.setPingJunChengBenJine(excels.get(i).getJine().trim());
			dat.setZengPinBiaoZhi("0");
			dat.setCreateTime(new Date());
			dat.setMoBan(subject.getTemp());
			dat.setKeMu(subject.getId());
			dat.setChuRuLeiBie(subject.getType());
			dat.setPickStoreType(Integer.valueOf(type));
			if (type.equals("1")) {
				dat.setYeWuLeiXing("19");
				dat.setDanJuLeiXing("21");
			
			}else {
				dat.setYeWuLeiXing("8");
				dat.setDanJuLeiXing("9");
			}
			pickStoreDatService.insert(dat);
		}

	}



	@Override
	public void addPick(String companyName, String openDate,PickStoreDat pickStoreDat) throws Exception {
		String percent=pickStoreDat.getPencent();
		String month=DateUtil.formatDate("yyyy-MM", DateUtil.getDate(openDate));
		List<SellDat> sellDats=sellDatService.selectList(new EntityWrapper<SellDat>().eq("company_code", companyName).like("open_date", month));
		if(sellDats==null||sellDats.size()==0) {
			throw new Exception("该日期没有找到销售单数据");
		}
		BigDecimal sum=BigDecimal.ZERO;
		//计算当月销售单总和
		for(SellDat sellDat:sellDats) {
			sum=sum.add(new BigDecimal(sellDat.getLocalAmount()==null?"0":sellDat.getLocalAmount()));
		}
		//按照百分比计算原料成本金额
		BigDecimal chengBen=sum.multiply(new BigDecimal(percent)).setScale(2, BigDecimal.ROUND_HALF_UP);
		List<SellDat> insertPickDats=new ArrayList();
		//按产品分组
		Map<String, List<SellDat>> sellMap= sellDats.stream().collect(Collectors.groupingBy(SellDat::getProduceCode));
		for(String productCode:sellMap.keySet()) {
			List<SellDat> list=	sellMap.get(productCode);
			if(list.size()>0) {
				SellDat temp=list.get(0);
				//计算成本数量，单价，金额
				BigDecimal localPercent=new BigDecimal(temp.getLocalAmount()).divide(sum,4, BigDecimal.ROUND_HALF_UP);
				System.out.println("成本金额："+chengBen.toString()+",计算百分比:"+localPercent.toString()+",产品编码:"+temp.getProduceCode()+",原销售总金额"+temp.getLocalAmount());
				BigDecimal localAmount=chengBen.multiply(localPercent);
				BigDecimal danJia=localAmount.divide(new BigDecimal(temp.getNumber()),2, BigDecimal.ROUND_HALF_UP);
				temp.setUnitPrice(danJia.toString());
				temp.setPercent(localPercent);
				temp.setLocalAmount(localAmount.toString());
				insertPickDats.add(temp);
			}
		}
		
		
		String documentNo="";
		/*获取单据号 */
		 Wrapper<PickStoreDat> wrapperPickStoreDat= new EntityWrapper<PickStoreDat>();
		 wrapperPickStoreDat.eq("company_name",companyName);
		 wrapperPickStoreDat.eq("open_date",openDate);
		 wrapperPickStoreDat.eq("pick_store_type",PICK_TYPE);
		 wrapperPickStoreDat.orderBy("document_no",false); 
			 List<PickStoreDat> pickStoreDats=selectList(wrapperPickStoreDat);
			 if(pickStoreDats.size()==0) {
				 documentNo="0001";
			 }else {
			     documentNo= CamsUtil.generateCode(pickStoreDats.get(0).getDocumentNo());
			 }
		//插入领料单
		for(SellDat insertdat:insertPickDats) {
			Integer i=1;
			PickStoreDat dat = new PickStoreDat();
			dat.setKuaiJiNianDu(DateUtil.getYear(new Date()) + "");
			dat.setAccountPeriod(DateUtil.getMonth(DateUtil.getDate(openDate)) + "");
			dat.setDocumentNo(documentNo); //单据号
			dat.setCustomerCode("");
			dat.setCompanyName(companyName);
			dat.setProduceDate(openDate);
			dat.setZhiDanRen("系统主管");
			dat.setHuiLv("1");
			dat.setBiZhong("RMB");
			dat.setDueDate(openDate);
			dat.setDaoQiRiQi(openDate);
			dat.setOpenDate(openDate);
			dat.setFaPiaoLeiXing("0");
			dat.setKaiPiaoBiaoZhi("0");
			dat.setRowNumber((++i).toString());
			dat.setProduceCode(insertdat.getProduceCode());
			dat.setHuoWei("01");
			dat.setCalUnit(insertdat.getCalUnit());
			dat.setNumber(insertdat.getNumber());
			dat.setUnitPrice(insertdat.getUnitPrice());
			dat.setKouLv("100");
			dat.setOriginalAmount(insertdat.getLocalAmount()); //原币金额
			dat.setLocalAmount(insertdat.getLocalAmount()); //本币金额
			dat.setOriginalTax("0");
			dat.setLocalhostTax("0");
			dat.setBaoZhiQi("0");
			dat.setChengBenChaYi("0");
			dat.setChengBenJinE(insertdat.getLocalAmount());
			dat.setGuanBiBiaoZhi("0");
			dat.setDaiDaYinBiaoZhi("0");
			dat.setDaiShiXianXiaoXiangShui("0");
			dat.setPingJunChengBenJine(insertdat.getLocalAmount());
			dat.setZengPinBiaoZhi("0");
			dat.setCreateTime(new Date());
			dat.setMoBan("领料单");
			dat.setKeMu("4101-01"); //基本生产成本科目
			dat.setChuRuLeiBie("1001");
			dat.setPickStoreType(Integer.valueOf(PICK_TYPE));
			dat.setYeWuLeiXing("19");
			dat.setDanJuLeiXing("21");
	
			pickStoreDatService.insert(dat);
		}
		
		
		
		/*获取单据号 */
		 Wrapper<PickStoreDat> wrapperPickStoreDat2= new EntityWrapper<PickStoreDat>();
		 wrapperPickStoreDat2.eq("company_name",companyName);
		 wrapperPickStoreDat2.eq("open_date",openDate);
		 wrapperPickStoreDat2.eq("pick_store_type",STORE_TYPE);
		 wrapperPickStoreDat2.orderBy("document_no",false); 
			 List<PickStoreDat> pickStoreDats2=selectList(wrapperPickStoreDat);
			 if(pickStoreDats2.size()==0) {
				 documentNo="0001";
			 }else {
			     documentNo= CamsUtil.generateCode(pickStoreDats2.get(0).getDocumentNo());
			 }
		
	    //人工费用和辅料费用总和
	    BigDecimal salarySum=pickStoreDat.getSalary().add(pickStoreDat.getOtherSalary());
		//插入入库单
		for(SellDat insertdat:insertPickDats) {
			//根据比例把人工费用和辅料费用分摊到每个领料单
			BigDecimal localPercent=insertdat.getPercent();
			BigDecimal localAmount=salarySum.multiply(localPercent).add(new BigDecimal(insertdat.getLocalAmount())).setScale(2, RoundingMode.HALF_UP);
			BigDecimal newUnitPrice=localAmount.divide(new BigDecimal(insertdat.getNumber()),2,RoundingMode.HALF_UP);
			insertdat.setLocalAmount(localAmount.toString());
			insertdat.setUnitPrice(newUnitPrice.toString());
			Integer i=1;
			PickStoreDat dat = new PickStoreDat();
			dat.setKuaiJiNianDu(DateUtil.getYear(new Date()) + "");
			dat.setAccountPeriod(DateUtil.getMonth(DateUtil.getDate(openDate)) + "");
			dat.setDocumentNo(documentNo); //单据号
			dat.setCustomerCode("");
			dat.setCompanyName(companyName);
			dat.setProduceDate(openDate);
			dat.setZhiDanRen("系统主管");
			dat.setHuiLv("1");
			dat.setBiZhong("RMB");
			dat.setDueDate(openDate);
			dat.setDaoQiRiQi(openDate);
			dat.setOpenDate(openDate);
			dat.setFaPiaoLeiXing("0");
			dat.setKaiPiaoBiaoZhi("0");
			dat.setRowNumber((++i).toString());
			dat.setProduceCode(insertdat.getProduceCode());
			dat.setHuoWei("01");
			dat.setCalUnit(insertdat.getCalUnit());
			dat.setNumber(insertdat.getNumber());
			dat.setUnitPrice(insertdat.getUnitPrice());
			dat.setKouLv("100");
			dat.setOriginalAmount(insertdat.getLocalAmount()); //原币金额
			dat.setLocalAmount(insertdat.getLocalAmount()); //本币金额
			dat.setOriginalTax("0");
			dat.setLocalhostTax("0");
			dat.setBaoZhiQi("0");
			dat.setChengBenChaYi("0");
			dat.setChengBenJinE(insertdat.getLocalAmount());
			dat.setGuanBiBiaoZhi("0");
			dat.setDaiDaYinBiaoZhi("0");
			dat.setDaiShiXianXiaoXiangShui("0");
			dat.setPingJunChengBenJine(insertdat.getLocalAmount());
			dat.setZengPinBiaoZhi("0");
			dat.setCreateTime(new Date());
			dat.setMoBan("产成品入库单");
			dat.setKeMu("1237"); 
			dat.setChuRuLeiBie("1010");
			dat.setPickStoreType(Integer.valueOf(STORE_TYPE));
			dat.setYeWuLeiXing("8");
			dat.setDanJuLeiXing("9");
			pickStoreDatService.insert(dat);
		}
		
	}
 
}

        

































































