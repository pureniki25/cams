package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.customer.vo.BankWithholdFlowVo;
import com.hongte.alms.base.customer.vo.CustomerRepayFlowDto;
import com.hongte.alms.base.entity.WithholdingChannel;
import com.hongte.alms.base.exception.ServiceRuntimeException;
import com.hongte.alms.base.mapper.WithholdingChannelMapper;
import com.hongte.alms.base.service.WithholdingChannelService;
import com.hongte.alms.base.vo.withhold.WithholdChannelListReq;
import com.hongte.alms.base.vo.withhold.WithholdChannelListVo;
import com.hongte.alms.base.vo.withhold.WithholdChannelOptReq;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.StringUtil;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 代扣渠道列表 服务实现类
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-05-26
 */
@Service("WithholdingChannelService")
public class WithholdingChannelServiceImpl extends BaseServiceImpl<WithholdingChannelMapper, WithholdingChannel> implements WithholdingChannelService {

    @Autowired
    private WithholdingChannelMapper withholdingChannelMapper;

    @Override
    public Page<WithholdChannelListVo> getWithholdChannelPageList(WithholdChannelListReq withholdChannelListReq) throws Exception {
        Page<WithholdChannelListVo> page = new Page<>();
        List<WithholdChannelListVo> withholdFlowList = withholdingChannelMapper.getWithholdChannelPageList(withholdChannelListReq);
        int count = withholdingChannelMapper.countWithholdChannelPageList(withholdChannelListReq);
        page.setTotal(count);
        page.setRecords(withholdFlowList);
        return page;
    }

    @Override
    public void addOrEditWithholdChannel(WithholdingChannel withholdingChannel) throws Exception {

        String subPlatformId = withholdingChannel.getSubPlatformId();
        Integer platformId = withholdingChannel.getPlatformId();
        if(platformId == 0){//易宝代扣
            withholdingChannel.setSubPlatformId("010");
            withholdingChannel.setSubPlatformName("易宝代扣");
        }else if(platformId == 3){ //宝付代扣
            withholdingChannel.setSubPlatformId("310");
            withholdingChannel.setSubPlatformName("宝付代扣");
        }
        if (!StringUtil.isEmpty(subPlatformId)) {
            int count = withholdingChannelMapper.selectCount(new EntityWrapper<WithholdingChannel>().eq("sub_platform_id", subPlatformId));

            if (withholdingChannel.getChannelId() != null) {
                WithholdingChannel withholdingChannelHistory = withholdingChannelMapper.selectById(withholdingChannel.getChannelId());
                String subPlatformIdHistory = withholdingChannelHistory.getSubPlatformId();
                if(count>0 && !subPlatformId.equals(subPlatformIdHistory) ){
                    throw new ServiceRuntimeException("不能修改为已存在的渠道");
                }
            }else {
                if (count > 0) {
                    throw new ServiceRuntimeException("不能新增已存在的渠道");
                }
            }

        }
        if (withholdingChannel.getChannelId() != null) { //编辑
            withholdingChannel.setUpdateTime(new Date());
            withholdingChannelMapper.updateById(withholdingChannel);
        } else {  //新增

            withholdingChannel.setUpdateTime(new Date());
            withholdingChannel.setCreateTime(new Date());
            withholdingChannelMapper.insert(withholdingChannel);
        }

    }

    @Override
    public WithholdingChannel getWithholdChannel(int channelId) {
        return withholdingChannelMapper.selectById(channelId);
    }
}
