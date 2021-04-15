package com.hongte.alms.base.mapper;

import java.util.List;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.hongte.alms.base.entity.ProductDat;
import com.hongte.alms.base.vo.cams.RestProductVo;
import com.hongte.alms.common.mapper.SuperMapper;

/**
 * <p>
 * 产品表 Mapper 接口
 * </p>
 *
 * @author czs
 * @since 2019-01-26
 */
public interface ProductDatMapper extends SuperMapper<ProductDat> {
	/**
	 * 库存余额表查询
	 * 
	 * @param pages
	 * @param vo
	 * @return
	 */
	List<RestProductVo> inventoryPage(Pagination pages, RestProductVo vo);
	
	void syncProductRestData();
	int updateOne(ProductDat vo);
}
