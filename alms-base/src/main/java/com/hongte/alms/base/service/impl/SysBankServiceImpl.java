package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.SysBank;
import com.hongte.alms.base.mapper.InfoSmsMapper;
import com.hongte.alms.base.mapper.SysBankMapper;
import com.hongte.alms.base.service.SysBankService;
import com.hongte.alms.base.vo.module.BankLimitVO;
import com.hongte.alms.base.vo.module.InfoSmsListSearchVO;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.vo.PageRequest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 注册存管银行配置表 服务实现类
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-03-10
 */
@Service
public class SysBankServiceImpl extends BaseServiceImpl<SysBankMapper, SysBank> implements SysBankService {
	@Autowired
	SysBankMapper sysBankMapper;

	@Override
	public Page<BankLimitVO> selectBankLimitList(String bankName, Integer platformId,PageRequest req) {
	      Page<BankLimitVO> pages = new Page<>();
	      pages.setSize(req.getLimit());
	      pages.setCurrent(req.getPage());
	      List <BankLimitVO>list=  sysBankMapper.selectBankLimitList(pages, req, bankName, platformId);
	    		  pages.setRecords(list);
	    
		return pages;
	}

}
