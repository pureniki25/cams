package com.hongte.alms.base.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.customer.vo.CustomerRepayFlowDto;
import com.hongte.alms.base.entity.WithholdingChannel;
import com.hongte.alms.base.vo.withhold.WithholdChannelListReq;
import com.hongte.alms.base.vo.withhold.WithholdChannelListVo;
import com.hongte.alms.base.vo.withhold.WithholdChannelOptReq;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 代扣渠道列表 服务类
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-05-26
 */
public interface WithholdingChannelService extends BaseService<WithholdingChannel> {

    Page<WithholdChannelListVo> getWithholdChannelPageList(WithholdChannelListReq withholdChannelListReq) throws Exception;

    void addOrEditWithholdChannel(WithholdingChannel withholdingChannel) throws Exception;

    WithholdingChannel getWithholdChannel(int channelId);

    void delWithholdChannel(int channelId);
}
