package com.hongte.alms.base.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.hongte.alms.base.entity.SysBank;
import com.hongte.alms.base.vo.module.BankLimitReq;
import com.hongte.alms.base.vo.module.BankLimitVO;
import com.hongte.alms.common.mapper.SuperMapper;
import com.hongte.alms.common.vo.PageRequest;

/**
 * <p>
 * 注册存管银行配置表 Mapper 接口
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-03-10
 */
public interface SysBankMapper extends SuperMapper<SysBank> {

	    List<BankLimitVO> selectBankLimitList(Pagination pages,BankLimitReq key,@Param("bankCode")String bankCode,@Param("platformId") String platformId );
}
