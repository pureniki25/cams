package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.hongte.alms.base.entity.BuyDat;
import com.hongte.alms.base.entity.CamsSubject;
import com.hongte.alms.base.entity.PickStoreDat;
import com.hongte.alms.base.enums.SubjectEnum;
import com.hongte.alms.base.invoice.vo.InvoiceBuyExel;
import com.hongte.alms.base.invoice.vo.PickExcel;
import com.hongte.alms.base.mapper.PickStoreDatMapper;
import com.hongte.alms.base.service.BankPaymentDatService;
import com.hongte.alms.base.service.PickStoreDatService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.CamsUtil;
import com.ht.ussp.util.DateUtil;

import java.io.InputStream;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
	@Autowired
	@Qualifier("PickStoreDatService")
	PickStoreDatService pickStoreDatService;

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

	public static void main(String[] args) {
		System.out.println(DateUtil.getMonth(DateUtil.getDate("2019-04-30")) + "");
	}

}
