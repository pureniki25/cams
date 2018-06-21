package com.hongte.alms.base.mapper;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.hongte.alms.base.entity.SysFinancialOrder;
import com.hongte.alms.base.vo.finance.SysFinancialOrderVO;
import com.hongte.alms.common.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 财务跟单设置 Mapper 接口
 * </p>
 *
 * @author 曾坤
 * @since 2018-05-29
 */
public interface SysFinancialOrderMapper extends SuperMapper<SysFinancialOrder> {
    List<SysFinancialOrderVO> search(Pagination page, @Param("businessTypeId") Integer businessTypeId,@Param("areaId")  String areaId,@Param("companyId")  String companyId ,@Param("userName")  String userName);
}
