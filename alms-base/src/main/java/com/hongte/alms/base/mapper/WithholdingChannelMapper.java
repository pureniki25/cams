package com.hongte.alms.base.mapper;

import com.hongte.alms.base.entity.WithholdingChannel;
import com.hongte.alms.base.vo.withhold.WithholdChannelListReq;
import com.hongte.alms.base.vo.withhold.WithholdChannelListVo;
import com.hongte.alms.common.mapper.SuperMapper;

import java.util.List;

/**
 * <p>
 * 代扣渠道列表 Mapper 接口
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-05-26
 */
public interface WithholdingChannelMapper extends SuperMapper<WithholdingChannel> {

    List<WithholdChannelListVo> getWithholdChannelPageList(WithholdChannelListReq withholdChannelListReq);

    int countWithholdChannelPageList(WithholdChannelListReq withholdChannelListReq);
}
