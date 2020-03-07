package com.hongte.alms.base.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;

import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.ProductDat;
import com.hongte.alms.base.invoice.vo.InvoiceProductExcel;
import com.hongte.alms.base.vo.cams.RestProductVo;
import com.hongte.alms.common.service.BaseService;
import com.hongte.alms.common.vo.PageResult;

/**
 * <p>
 * 产品表 服务类
 * </p>
 *
 * @author czs
 * @since 2019-01-26
 */
public interface ProductDatService extends BaseService<ProductDat> {
	ProductDat addProductDat(String openDate, String productType, String productName, String companyName,
			String productPropertiesName, String minCalUnit, String restCalUnit, String productUnit, String calUnit,
			String packageUnit) throws UnsupportedEncodingException;

	/**
	 * 从销售单，采购单导入商品
	 * 
	 * @param productCode
	 * @param productType
	 * @param productName
	 * @param companyName
	 * @param productPropertiesName
	 * @param minCalUnit
	 * @param restCalUnit
	 * @param productUnit
	 * @param calUnit
	 * @param packageUnit
	 * @throws UnsupportedEncodingException
	 */
	void addProductDatByExcel(String productCode, String productType, String productName, String companyName,
			String productPropertiesName, String minCalUnit, String restCalUnit, String productUnit, String calUnit,
			String packageUnit) throws UnsupportedEncodingException;

	/**
	 * 导入期初商品数据
	 * 
	 * @param file
	 * @param companyName
	 * @param productPropertiesName
	 * @throws Exception
	 */
	void importProductDat(MultipartFile file, String companyName, String productPropertiesName) throws Exception;

	/**
	 * 
	 * @param excel
	 * @param companyName
	 * @throws Exception
	 */
	void updateKuCunLiang(MultipartFile file, String companyName) throws Exception;

	public Page<RestProductVo> inventoryPage(RestProductVo vo);
}
