package com.hongte.alms.base.service;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.hongte.alms.base.entity.CustomerRestDat;
import com.hongte.alms.base.vo.cams.CustomerRestVo;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 单位余额表 服务类
 * </p>
 *
 * @author czs
 * @since 2020-02-10
 */
public interface CustomerRestDatService extends BaseService<CustomerRestDat> {
	public void syncCustomerRestData();
		
	

	public void deleteCustomeRest();
	
	
	/**
	 * 单位余额表查询
	 * 
	 * @param pages
	 * @param vo
	 * @return
	 */
	public Page<CustomerRestVo> selectCustomerRestList(CustomerRestVo vo);
		
	
}
