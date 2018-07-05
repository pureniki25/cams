package com.hongte.alms.base.mapper;

import com.hongte.alms.base.entity.SysBankLimit;
import com.hongte.alms.base.vo.withhold.WithholdChannelListVo;
import com.hongte.alms.base.vo.withhold.WithholdLimitListReq;
import com.hongte.alms.base.vo.withhold.WithholdLimitListVo;
import com.hongte.alms.common.mapper.SuperMapper;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 代扣平台银行限额表 Mapper 接口
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-05-29
 */
public interface SysBankLimitMapper extends SuperMapper<SysBankLimit> {

    List<WithholdLimitListVo> getWithholdLimitPageList(WithholdLimitListReq withholdLimitListReq);

    int countWithholdLimitPageList(WithholdLimitListReq withholdLimitListReq);

    BigDecimal selectOnceLimit(String bankCode);

    BigDecimal selectMaxDayLimit(String bankCode);
}
