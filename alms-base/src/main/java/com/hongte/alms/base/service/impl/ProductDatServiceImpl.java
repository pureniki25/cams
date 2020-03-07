package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.CamsProductProperties;
import com.hongte.alms.base.entity.PickStoreDat;
import com.hongte.alms.base.entity.ProductDat;
import com.hongte.alms.base.entity.SellDat;
import com.hongte.alms.base.entity.TempProductDat;
import com.hongte.alms.base.enums.CamsConstant;
import com.hongte.alms.base.invoice.vo.BeginProductExcel;
import com.hongte.alms.base.invoice.vo.InvoiceCustomerExcel;
import com.hongte.alms.base.invoice.vo.InvoiceProductExcel;
import com.hongte.alms.base.mapper.ProductDatMapper;
import com.hongte.alms.base.service.CamsProductPropertiesService;
import com.hongte.alms.base.service.PickStoreDatService;
import com.hongte.alms.base.service.ProductDatService;
import com.hongte.alms.base.service.TempProductDatService;
import com.hongte.alms.base.vo.cams.RestProductVo;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.CamsUtil;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.common.util.StringUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jeecgframework.poi.excel.ExcelImportUtil;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 产品表 服务实现类
 * </p>
 *
 * @author czs
 * @since 2019-01-26
 */
@Service("ProductDatService")
public class ProductDatServiceImpl extends BaseServiceImpl<ProductDatMapper, ProductDat> implements ProductDatService {

	@Autowired
	@Qualifier("CamsProductPropertiesService")
	private CamsProductPropertiesService camsProductPropertiesService;

	@Autowired
	@Qualifier("TempProductDatService")
	private TempProductDatService tempProductDatService;

	@Autowired
	@Qualifier("PickStoreDatService")
	private PickStoreDatService pickStoreDatService;
	
	@Autowired
	private ProductDatMapper productDatMapper;

	@Override
	public ProductDat addProductDat(String openDate, String productType, String productName, String companyName,
			String productPropertiesName, String minCalUnit, String restCalUnit, String productUnit, String calUnit,
			String packageUnit) throws UnsupportedEncodingException {

		CamsProductProperties properties = camsProductPropertiesService.selectOne(
				new EntityWrapper<CamsProductProperties>().eq("product_properties_name", productPropertiesName));
		productName = CamsUtil.getProductName(productName);
		ProductDat productDat = null;

		String productNameTemp = getShangPingMingChenTemp(productName);
		String productTypeTemp = getGuiGeTemp(productName);
		String tempProductName = "";
		if (!StringUtil.isEmpty(productType)) {
			productType = productType.replace("*", "x");
			productType = productType.replace("＊", "x");
			productType = productType.replace("﹡", "x");
		}
		if (productTypeTemp.equals("")) {// 如果产品名称经过中文和英文分离，规格为空的话，说明是纯中文，按照原来的判断逻辑判断是否含有相同产品
			tempProductName = productNameTemp + (productType == null ? "" : productType);
			tempProductName = CamsUtil.getTempProductName(tempProductName);

		} else {// 如果产品名称包括英文的规格,就分开去查是由含有相同产品
			tempProductName = productNameTemp + productTypeTemp;
			tempProductName = CamsUtil.getTempProductName(tempProductName);

		}

		Wrapper<TempProductDat> tempProductDatWrapper = new EntityWrapper<TempProductDat>();
		tempProductDatWrapper.eq("temp_product_name", tempProductName).eq("company_name", companyName);
		if (!StringUtil.isEmpty(packageUnit)) {
			tempProductDatWrapper.eq("unit", packageUnit);
		}
		TempProductDat tempProductDat = tempProductDatService.selectOne(tempProductDatWrapper);
		if (tempProductDat != null) {
			productDat = selectOne(new EntityWrapper<ProductDat>().eq("company_name", companyName).eq("product_code",
					tempProductDat.getProductCode()));
		} else {
			tempProductDat = new TempProductDat();
			List<ProductDat> dats = selectList(
					new EntityWrapper<ProductDat>().eq("company_name", companyName).orderBy("product_code", false));
			if (dats.size() == 0) {//
				tempProductDat.setProductCode("C0001");
			} else {
				String productCode = CamsUtil.generateCode(dats.get(0).getProductCode());
				tempProductDat.setProductCode(productCode);

			}

			tempProductDat.setCreateTime(new Date());
			tempProductDat.setCompanyName(companyName);
			tempProductDat.setProductName(productName);
			tempProductDat.setProductType(productType);
			tempProductDat.setUnit(packageUnit);
			tempProductDat.setTempProductName(tempProductName);
			tempProductDatService.insert(tempProductDat);

			if (properties.getProductPropertiesName().equals("费用")) {// 费用
				restCalUnit = "无";
				minCalUnit = restCalUnit;
				calUnit = restCalUnit;
			} else {
				minCalUnit = restCalUnit + "1";// 如果是存货，最小计量单位等于存货计量单位 + 货位，货位默认等于"1"
				calUnit = minCalUnit + "," + "1" + " " + restCalUnit + "," + "100";
			}
			productDat = new ProductDat();
			productDat.setProductCode(tempProductDat.getProductCode());
			productDat.setProductName(productName);
			productDat.setProductType(productType);
			productDat.setProductCategory(properties.getProductPropertiesId());// 商品类别
			productDat.setCompanyName(companyName);
			productDat.setProductProperties(properties.getProductPropertiesName());
			productDat.setCalUnit(calUnit);
			productDat.setMinCalUnit(minCalUnit);
			productDat.setRestCalUnit(restCalUnit);
			productDat.setProductUnit("1");
			productDat.setCalUnit(calUnit);
			productDat.setPackageUnit(packageUnit);
			productDat.setCreateTime(new Date());
			productDat.setOpenDate(openDate);
			insert(productDat);
		}
		
		return productDat;
	}

	// if(productTypeTemp.equals(""))
	// {//如果产品名称经过中文和英文分离，规格为空的话，说明是纯中文，按照原来的判断逻辑判断是否含有相同产品
	//
	// //先用原始数据的产品名称和产品型号分开去查是由含有相同产品
	// Wrapper<ProductDat> wrapperBuyDat= new EntityWrapper<ProductDat>();
	// wrapperBuyDat.eq("company_name", companyName);
	// wrapperBuyDat.eq( "product_name",productName);
	// wrapperBuyDat.eq( "package_unit",packageUnit);
	// if(productType==null) {
	// wrapperBuyDat.and( "product_type is null");
	// }else {
	// wrapperBuyDat.and( "product_type like '%"+productType+"%'");
	// }
	// productDat=selectOne(wrapperBuyDat);
	//
	// if(productDat!=null) {
	// return productDat;
	// }
	//
	//
	// if(productType!=null) {
	// //再用原始数据的产品名称和产品型号连接一起去查是由含有相同产品
	// Wrapper<ProductDat> wrapperBuyDat2= new EntityWrapper<ProductDat>();
	// wrapperBuyDat2.eq("company_name", companyName);
	// wrapperBuyDat2.eq( "product_name",new
	// String((productName+productType).getBytes("gbk"),"GBK"));
	// wrapperBuyDat2.eq( "package_unit",packageUnit);
	// productDat=selectOne(wrapperBuyDat2);
	// if(productDat!=null) {
	// return productDat;
	// }
	//
	// //再用原始数据的产品名称和产品型号中间间隔一个" "连接一起去查是由含有相同产品
	// Wrapper<ProductDat> wrapperBuyDat3= new EntityWrapper<ProductDat>();
	// wrapperBuyDat3.eq("company_name", companyName);
	// wrapperBuyDat3.eq( "product_name",new String((productName+"
	// "+productType).getBytes("gbk"),"GBK"));
	// wrapperBuyDat3.eq( "package_unit",packageUnit);
	// productDat=selectOne(wrapperBuyDat3);
	// if(productDat!=null) {
	// return productDat;
	// }
	//
	//
	// }
	// }else {//如果产品名称包括英文的规格,就分开去查是由含有相同产品
	// Wrapper<ProductDat> wrapperBuyDat= new EntityWrapper<ProductDat>();
	// wrapperBuyDat.eq("company_name", companyName);
	// wrapperBuyDat.eq( "product_name",productNameTemp);
	// wrapperBuyDat.eq( "product_type",productTypeTemp);
	// wrapperBuyDat.eq( "package_unit",packageUnit);
	// productDat=selectOne(wrapperBuyDat);
	// if(productDat!=null) {
	// return productDat;
	// }
	//
	//
	// //再用原始数据的产品名称和产品型号连接一起去查是由含有相同产品
	// Wrapper<ProductDat> wrapperBuyDat2= new EntityWrapper<ProductDat>();
	// wrapperBuyDat2.eq("company_name", companyName);
	// wrapperBuyDat2.eq( "product_name",productNameTemp+productTypeTemp);
	// wrapperBuyDat2.eq( "package_unit",packageUnit);
	// productDat=selectOne(wrapperBuyDat2);
	// if(productDat!=null) {
	// return productDat;
	// }
	//
	// //再用原始数据的产品名称和产品型号中间间隔一个" "连接一起去查是由含有相同产品
	// Wrapper<ProductDat> wrapperBuyDat3= new EntityWrapper<ProductDat>();
	// wrapperBuyDat3.eq("company_name", companyName);
	// wrapperBuyDat3.eq( "product_name",productNameTemp+" "+productTypeTemp);
	// wrapperBuyDat3.eq( "package_unit",packageUnit);
	// productDat=selectOne(wrapperBuyDat3);
	// if(productDat!=null) {
	// return productDat;
	// }
	//
	// }
	//

	// if(productDat==null) {
	// productDat=new ProductDat();
	//
	//
	// List<ProductDat> dats=selectList(new
	// EntityWrapper<ProductDat>().eq("company_name",
	// companyName).orderBy("product_code",false));
	// if(dats.size()==0) {//
	// productDat.setProductCode("C0001");
	// }else {
	// String productCode=CamsUtil.generateCode(dats.get(0).getProductCode());
	// productDat.setProductCode(productCode);
	//
	// }

	private boolean isSameProduct(String companyName, String productName, String productType) {
		String productNameTemp = getShangPingMingChenTemp(productName);
		String productTypeTemp = getGuiGeTemp(productName);

		ProductDat productDat = null;

		Wrapper<ProductDat> wrapperBuyDat = new EntityWrapper<ProductDat>();
		wrapperBuyDat.eq("company_name", companyName);
		wrapperBuyDat.eq("product_name", productNameTemp);
		wrapperBuyDat.eq("product_type", productTypeTemp);
		if (productType == null) {
			wrapperBuyDat.and("product_type  is null");
		} else {
			wrapperBuyDat.and("product_type like '%" + productType + "%'");
		}
		productDat = selectOne(wrapperBuyDat);
		return false;
	}

	private String getShangPingMingChenTemp(String s) {
		int position = 0;
		for (int index = s.length() - 1; index >= 0; index--) {
			// 将字符串拆开成单个的字符
			String w = s.substring(index, index + 1);
			if (w.compareTo("\u4e00") > 0 && w.compareTo("\u9fa5") < 0) {// \u4e00-\u9fa5 中文汉字的范围
				position = index;
				break;
			}
		}

		return s.substring(0, position + 1).trim();

	}

	private String getGuiGeTemp(String s) {
		int position = 0;
		for (int index = s.length() - 1; index >= 0; index--) {
			// 将字符串拆开成单个的字符
			String w = s.substring(index, index + 1);
			if (w.compareTo("\u4e00") > 0 && w.compareTo("\u9fa5") < 0) {// \u4e00-\u9fa5 中文汉字的范围
				position = index;
				break;
			}
		}
		String guiGe = s.substring(position + 1, s.length());

		return s.substring(position + 1, s.length()).trim();
	}


	@Override
	public void addProductDatByExcel(String productCode, String productType, String productName, String companyName,
			String productPropertiesName, String minCalUnit, String restCalUnit, String productUnit, String calUnit,
			String packageUnit) throws UnsupportedEncodingException {
		CamsProductProperties properties = camsProductPropertiesService.selectOne(
				new EntityWrapper<CamsProductProperties>().eq("product_properties_name", productPropertiesName));
		ProductDat productDat = null;
		TempProductDat tempProductDat = null;
		String productNameTemp = getShangPingMingChenTemp(productName);
		String productTypeTemp = getGuiGeTemp(productName);
		String tempProductName = "";
		tempProductName = CamsUtil.getTempProductName(tempProductName);
		if (!StringUtil.isEmpty(productType)) {
			productType = productType.replace("*", "x");
			productType = productType.replace("＊", "x");
			productType = productType.replace("﹡", "x");
		}
		if (productTypeTemp.equals("")) {// 如果产品名称经过中文和英文分离，规格为空的话，说明是纯中文，按照原来的判断逻辑判断是否含有相同产品
			tempProductName = productNameTemp + (productType == null ? "" : productType);
			tempProductName = CamsUtil.getTempProductName(tempProductName);

		} else {// 如果产品名称包括英文的规格,就分开去查是由含有相同产品
			tempProductName = productNameTemp + productTypeTemp;
			tempProductName = CamsUtil.getTempProductName(tempProductName);

		}
		Wrapper<TempProductDat> tempProductDatWrapper = new EntityWrapper<TempProductDat>();
		tempProductDatWrapper.eq("temp_product_name", tempProductName).eq("company_name", companyName);
		if (!StringUtil.isEmpty(packageUnit)) {
			tempProductDatWrapper.eq("unit", packageUnit);
		}
		tempProductDat = tempProductDatService.selectOne(tempProductDatWrapper);

		if (tempProductDat != null) {
			return;
		}

		tempProductDat = new TempProductDat();
		tempProductDat.setProductCode(productCode);
		tempProductDat.setCreateTime(new Date());
		tempProductDat.setCompanyName(companyName);
		tempProductDat.setProductName(productName);
		tempProductDat.setProductType(productType);
		tempProductDat.setUnit(packageUnit);
		tempProductDat.setTempProductName(tempProductName);
		tempProductDatService.insert(tempProductDat);

		if (properties.getCategoryName().equals("费用")) {// 费用
			restCalUnit = "无";
			minCalUnit = restCalUnit;
			calUnit = restCalUnit;
		} else {
			minCalUnit = restCalUnit + "1";// 如果是存货，最小计量单位等于存货计量单位 + 货位，货位默认等于"1"
			calUnit = minCalUnit + "," + "1" + " " + restCalUnit + "," + "100";
		}
		productDat = new ProductDat();
		productDat.setProductCode(tempProductDat.getProductCode());
		productDat.setProductName(productName);
		productDat.setProductType(productType);
		productDat.setProductCategory(properties.getProductPropertiesId());// 商品类别
		productDat.setCompanyName(companyName);
		productDat.setProductProperties(properties.getProductPropertiesName());
		productDat.setCalUnit(calUnit);
		productDat.setMinCalUnit(minCalUnit);
		productDat.setRestCalUnit(restCalUnit);
		productDat.setProductUnit("1");
		productDat.setCalUnit(calUnit);
		productDat.setPackageUnit(packageUnit);
		productDat.setCreateTime(new Date());
		productDat.setOpenDate("");
		insert(productDat);
	}

	@Override
	@Transactional
	public void importProductDat(MultipartFile file, String companyName, String productPropertiesName)
			throws Exception {
		ImportParams importParams = new ImportParams();
		List<InvoiceProductExcel> excels = ExcelImportUtil.importExcel(file.getInputStream(), InvoiceProductExcel.class,
				importParams);

		for (InvoiceProductExcel excel : excels) {
			String danwei = excel.getUnit();

			if (excel.getUnit() != null) {
				if (excel.getUnit() != null
						&& (excel.getUnit().equals("千克") || excel.getUnit().equals("KG")
								|| excel.getUnit().equals("kg"))
						|| excel.getUnit().equals("Kg") || excel.getUnit().equals("斤")) {
					danwei = "公斤";
				} else if (excel.getUnit().contains("株")) {
					danwei = "棵";
				} else if (excel.getUnit().contains("平方") || excel.getUnit().contains("m2")
						|| excel.getUnit().contains("m²") || excel.getUnit().contains("㎡")) {
					danwei = "平方米";
				} else if (excel.getUnit().contains("盘")) {
					danwei = "盆";
				} else if (excel.getUnit() != null && (excel.getUnit().equals("只") || excel.getUnit().equals("支")
						|| excel.getUnit().equals("支/只") || excel.getUnit().equals("只/支"))) {
					danwei = "支";
				} else {
					danwei = excel.getUnit();
				}
			} else {
				danwei = "无";
			}
			addProductDatByExcel(excel.getProductCode(), excel.getProductType(), excel.getProductName(), companyName,
					productPropertiesName, danwei, danwei, danwei, danwei, danwei);

		}
	}

	@Override
	public void updateKuCunLiang(MultipartFile file, String companyName) throws Exception {
		ImportParams importParams = new ImportParams();
		List<BeginProductExcel> excels = ExcelImportUtil.importExcel(file.getInputStream(), BeginProductExcel.class,
				importParams);
		for (BeginProductExcel excel1 : excels) {
			ProductDat product = selectOne(new EntityWrapper<ProductDat>().eq("product_code", excel1.getProductCode())
					.eq("company_name", companyName));
			if (product != null) {
				product.setKuCunLiang(excel1.getKuCunLiang());
				product.setQiChuDanJia(excel1.getQiChuDanJia());
				product.setQiChuJine(excel1.getQiChuJine());
				update(product, new EntityWrapper<ProductDat>().eq("product_code", excel1.getProductCode())
						.eq("company_name", companyName));
			}

		}

	}

	@Override
	public Page<RestProductVo> inventoryPage(RestProductVo vo) {
		Page<RestProductVo> pages = new Page<>();
		pages.setCurrent(vo.getPage());
		pages.setSize(vo.getLimit());
	
		if(vo.getBeginDate()==null) {
			Calendar cale = Calendar.getInstance();
			cale.setTime(new Date());
			// 获取当前时间的上一个月 1号
			cale.add(Calendar.MONTH, -1);
			cale.set(Calendar.DAY_OF_MONTH, 1);
			vo.setBeginDate(cale.getTime());
		}
		if(vo.getEndDate()==null) {
			Calendar cale = Calendar.getInstance();
			cale.setTime(new Date());
			// 获取当前时间的上一个月 1号
			cale.set(Calendar.DAY_OF_MONTH, 1);
			vo.setEndDate(cale.getTime());
		}
		List<RestProductVo> list = productDatMapper.inventoryPage(pages, vo);
		for(RestProductVo product:list) {
			if(product.getProductCode().equals("C0001")) {
				System.out.println("123");
			}
			vo.setProductCode(product.getProductCode());
			Map<String,Object> kuCunLiangMap=getBeforeKuCunLiang(vo);
			Double beforPickCount=(Double) kuCunLiangMap.get("beforPickCount");
			Double beforStoreCount=(Double) kuCunLiangMap.get("beforStoreCount");
			Double beforPickJine= (Double) kuCunLiangMap.get("beforPickJine");
			Double beforeStoreJine= (Double) kuCunLiangMap.get("beforeStoreJine");
			Double thisPickCount=(Double) kuCunLiangMap.get("thisPickCount");
			Double thisStoreCount=(Double) kuCunLiangMap.get("thisStoreCount");
			Double thisPickJine= (Double) kuCunLiangMap.get("thisPickJine");
			Double thisStoreJine= (Double) kuCunLiangMap.get("thisStoreJine");
			Double danJia= Double.valueOf((String) kuCunLiangMap.get("qiMoDanJia"));
		
			//本期发出数
			Double outComeCount=(product.getOutcomeCount()==null?0:Double.valueOf(product.getOutcomeCount()))+thisPickCount;
			//本期发出金额
			Double outComeJine=(product.getOutcomeJine()==null?0.0:Double.valueOf(product.getOutcomeJine()))+(thisPickCount.doubleValue()*danJia);
			
			//本期收入数
			Double incomeCount=(product.getIncomeCount()==null?0:Double.valueOf(product.getIncomeCount()))+thisStoreCount;
			//本期收入金额
			Double incomeJine=(product.getIncomeJine()==null?0.0:Double.valueOf(product.getIncomeJine()))+(thisStoreCount.doubleValue()*danJia);
			Double kuCunLiang=(product.getKuCunLiang()==null?0.0:Double.valueOf(product.getKuCunLiang()));
			Double restKuCunLiang=product.getRestKuCunLiang()==null?0.0:Double.valueOf(product.getRestKuCunLiang());
			restKuCunLiang=restKuCunLiang+kuCunLiang-beforPickCount-thisPickCount+beforStoreCount+thisStoreCount;
		    
		    
			product.setOutcomeCount(outComeCount.toString());
			product.setIncomeCount(incomeCount.toString());
			product.setOutcomeJine(outComeJine.toString());
			product.setRestKuCunLiang(restKuCunLiang.toString());
			product.setIncomeJine(incomeJine.toString());
			product.setQiMoDanJia(danJia.toString());
		}
		pages.setRecords(list);
		return pages;
	}
	
	
	/**
	 * 获取本开票日期之前的领料单和入库单的库存数量
	 * @param vo
	 * @return
	 */
	private Map<String,Object> getBeforeKuCunLiang(RestProductVo vo) {
		Map<String,Object> map=new HashMap<String,Object>();
		Double beforPickCount=0.0;  //本期之前的领料数
		Double beforStoreCount=0.0; //本期之前的入库数
		Double thisPickCount=0.0;   //本期领料数
		Double thisStoreCount=0.0;  //本期入库数
		Double beforPickJine=0.0;  
		Double beforeStoreJine=0.0;
		Double thisPickJine=0.0;  
		Double thisStoreJine=0.0;
		
		List<PickStoreDat> beforPickList=pickStoreDatService.selectList(new EntityWrapper<PickStoreDat>().eq("company_name", vo.getCompanyName()).eq("produce_code", vo.getProductCode()).lt("open_date", vo.getBeginDate()).eq("pick_store_type", CamsConstant.PickStoreTypeEnum.PICK.getValue().toString()));
		List<PickStoreDat> beforeStoreList=pickStoreDatService.selectList(new EntityWrapper<PickStoreDat>().eq("company_name", vo.getCompanyName()).eq("produce_code", vo.getProductCode()).lt("open_date", vo.getBeginDate()).eq("pick_store_type", CamsConstant.PickStoreTypeEnum.STORE.getValue().toString()));
		
		List<PickStoreDat> thisPickList=pickStoreDatService.selectList(new EntityWrapper<PickStoreDat>().eq("company_name", vo.getCompanyName()).eq("produce_code", vo.getProductCode()).gt("open_date", vo.getBeginDate()).lt("open_date", vo.getEndDate()).eq("pick_store_type", CamsConstant.PickStoreTypeEnum.PICK.getValue().toString()));
		List<PickStoreDat> thisStoreList=pickStoreDatService.selectList(new EntityWrapper<PickStoreDat>().eq("company_name", vo.getCompanyName()).eq("produce_code", vo.getProductCode()).gt("open_date", vo.getBeginDate()).lt("open_date", vo.getEndDate()).eq("pick_store_type", CamsConstant.PickStoreTypeEnum.STORE.getValue().toString()));
		
		String danJia="0";
		
		for(PickStoreDat pick:beforPickList) {
			beforPickCount=pick.getNumber()==null?0:Double.valueOf(pick.getNumber())+beforPickCount;
			beforPickJine=pick.getLocalAmount()==null?0.0:Double.valueOf(pick.getLocalAmount());
		}
		for(PickStoreDat store:beforeStoreList) {
			beforStoreCount=store.getNumber()==null?0:Double.valueOf(store.getNumber())+beforStoreCount;
			beforeStoreJine=store.getLocalAmount()==null?0.0:Double.valueOf(store.getLocalAmount());
		}
		
		for(PickStoreDat pick:thisPickList) {
			thisPickCount=pick.getNumber()==null?0:Double.valueOf(pick.getNumber())+thisPickCount;
			thisPickJine=pick.getLocalAmount()==null?0.0:Double.valueOf(pick.getLocalAmount());
			if(!StringUtil.isEmpty(pick.getUnitPrice())) {
				danJia=pick.getUnitPrice();
			}
		}
		
		for(PickStoreDat store:thisStoreList) {
			thisStoreCount=store.getNumber()==null?0:Double.valueOf(store.getNumber())+thisStoreCount;
			thisStoreJine=store.getLocalAmount()==null?0.0:Double.valueOf(store.getLocalAmount());
			if(!StringUtil.isEmpty(store.getUnitPrice())) {
				danJia=store.getUnitPrice();
			}
		}
		
		map.put("beforPickCount", beforPickCount);
		map.put("beforStoreCount", beforStoreCount);
		map.put("beforPickJine", beforPickJine);
		map.put("beforeStoreJine", beforeStoreJine);
		
		map.put("thisPickCount",thisPickCount);
		map.put("thisStoreCount", thisStoreCount);
		map.put("thisPickJine", thisPickJine);
		map.put("thisStoreJine", thisStoreJine);
		
		map.put("qiMoDanJia", danJia);
		return map;
		
	}
	
	public static void main(String[] args) {

		Calendar cale = Calendar.getInstance();
		cale.setTime(new Date());
		// 获取当前时间的上一个月
		cale.add(Calendar.MONTH, -1);
		
		System.out.println(DateUtil.formatDate(cale.getTime()));
	}
}
