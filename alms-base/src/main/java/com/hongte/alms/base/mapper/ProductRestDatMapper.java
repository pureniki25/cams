package com.hongte.alms.base.mapper;

import java.util.List;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.hongte.alms.base.entity.ProductRestDat;
import com.hongte.alms.base.vo.cams.RestProductVo;
import com.hongte.alms.common.mapper.SuperMapper;

/**
 * <p>
 * 库存余额表 Mapper 接口
 * </p>
 *
 * @author czs
 * @since 2020-03-22
 */
public interface ProductRestDatMapper extends SuperMapper<ProductRestDat> {
	/**
	 * 库存余额表查询
	 * 
	 * @param pages
	 * @param vo
	 * @return
	 */
	List<RestProductVo> productRestPage(Pagination pages, RestProductVo vo);

}
