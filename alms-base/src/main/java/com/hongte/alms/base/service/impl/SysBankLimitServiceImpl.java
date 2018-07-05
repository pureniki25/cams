package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.SysBankLimit;
import com.hongte.alms.base.mapper.SysBankLimitMapper;
import com.hongte.alms.base.service.SysBankLimitService;
import com.hongte.alms.base.vo.withhold.WithholdLimitListReq;
import com.hongte.alms.base.vo.withhold.WithholdLimitListVo;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * <p>
 * 代扣平台银行限额表 服务实现类
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-05-29
 */
@Service("SysBankLimitService")
public class SysBankLimitServiceImpl extends BaseServiceImpl<SysBankLimitMapper, SysBankLimit> implements SysBankLimitService {


    @Autowired
    private SysBankLimitMapper sysBankLimitMapper;


    @Override
    public Page<WithholdLimitListVo> getWithholdLimitPageList(WithholdLimitListReq withholdLimitListReq) throws Exception {
        Page<WithholdLimitListVo> page = new Page<>();
        List<WithholdLimitListVo> withholdFlowList = sysBankLimitMapper.getWithholdLimitPageList(withholdLimitListReq);
        int count = sysBankLimitMapper.countWithholdLimitPageList(withholdLimitListReq);
        page.setTotal(count);
        page.setRecords(withholdFlowList);
        return page;
    }

    @Override
    public void addOrEditWithholdChannel(SysBankLimit sysBankLimit) throws Exception {
        if (!StringUtil.isEmpty(sysBankLimit.getLimitId())) {
            sysBankLimit.setUpdateTime(new Date());
            if(sysBankLimit.getDayLimit() !=null){
                sysBankLimit.setHasDayLimit(1);
            }
            if(sysBankLimit.getMonthLimit() !=null){
                sysBankLimit.setHasMonthLimit(1);
            }
            if(sysBankLimit.getOnceLimit() !=null){
                sysBankLimit.setHasOnceLimit(1);
            }
            sysBankLimitMapper.updateById(sysBankLimit);
        } else {
            sysBankLimit.setUpdateTime(new Date());
            sysBankLimit.setCreateTime(new Date());
            sysBankLimit.setHasDayLimit(1);
            sysBankLimit.setHasMonthLimit(1);
            sysBankLimit.setHasOnceLimit(1);

            sysBankLimitMapper.insert(sysBankLimit);
        }
    }

    @Override
    public SysBankLimit getWithholdLimit(String limitId) throws Exception {
        return sysBankLimitMapper.selectById(limitId);
    }

    @Override
    public void updateWithholdLimitStatus(String limitId,int status) throws Exception {
        SysBankLimit sysBankLimit=new SysBankLimit();
        sysBankLimit.setLimitId(limitId);
        sysBankLimit.setStatus(status);
        sysBankLimitMapper.updateById(sysBankLimit);
    }

    @Override
    public List<SysBankLimit> getSysBankLimitByPlatformId(int platformId) throws Exception {
        return sysBankLimitMapper.selectList(new EntityWrapper<SysBankLimit>().eq("platform_id",platformId));
    }

    @Override
    public BigDecimal selectOnceLimit(String bankCode) {
        return sysBankLimitMapper.selectOnceLimit(bankCode);
    }

    @Override
    public BigDecimal selectMaxDayLimit(String bankCode) {
        return sysBankLimitMapper.selectMaxDayLimit(bankCode);
    }

}
