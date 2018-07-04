package com.hongte.alms.base.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.SysBankLimit;
import com.hongte.alms.base.vo.withhold.WithholdLimitListReq;
import com.hongte.alms.base.vo.withhold.WithholdLimitListVo;
import com.hongte.alms.common.service.BaseService;

import java.math.BigDecimal;
import java.util.List;

/**
 * <p>
 * 代扣平台银行限额表 服务类
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-05-29
 */
public interface SysBankLimitService extends BaseService<SysBankLimit> {

    Page<WithholdLimitListVo> getWithholdLimitPageList(WithholdLimitListReq withholdLimitListReq) throws Exception;

    void addOrEditWithholdChannel(SysBankLimit sysBankLimit) throws Exception;

    SysBankLimit getWithholdLimit(String limitId) throws Exception;

    void updateWithholdLimitStatus(String limitId,int status)  throws Exception;

    List<SysBankLimit> getSysBankLimitByPlatformId(int platformId)  throws Exception;

    BigDecimal selectOnceLimit(String bankCode);

    BigDecimal selectMaxDayLimit(String bankCode);
}
