package com.hongte.alms.base.mapper;

import java.util.List;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.hongte.alms.base.entity.CustomerRestDat;
import com.hongte.alms.base.vo.cams.CustomerRestVo;
import com.hongte.alms.base.vo.cams.SubjectRestVo;
import com.hongte.alms.common.mapper.SuperMapper;

/**
 * <p>
 * 单位余额表 Mapper 接口
 * </p>
 *
 * @author czs
 * @since 2020-02-10
 */
public interface CustomerRestDatMapper extends SuperMapper<CustomerRestDat> {
	/**
	 * 同步所有凭证表的数据到一张单位余额表里
	 */
	void syncCustomerRestData();
	
	/**
	 * 删除单位余额表数据
	 */
	void deleteCustomerRest();
	
	
	/**
	 * 单位余额表查询
	 * 
	 * @param pages
	 * @param vo
	 * @return
	 */
	List<CustomerRestVo> selectCustomerRestList(Pagination pages, CustomerRestVo vo);
}
