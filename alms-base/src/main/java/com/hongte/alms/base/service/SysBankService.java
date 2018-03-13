package com.hongte.alms.base.service;


import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.SysBank;
import com.hongte.alms.base.vo.module.BankLimitReq;
import com.hongte.alms.base.vo.module.BankLimitVO;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 注册存管银行配置表 服务类
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-03-10
 */
public interface SysBankService extends BaseService<SysBank> {
	
	Page<BankLimitVO> selectBankLimitList(String bankCode,String platformId,BankLimitReq req);

}
