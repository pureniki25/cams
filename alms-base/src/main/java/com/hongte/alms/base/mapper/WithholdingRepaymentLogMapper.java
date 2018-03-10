package com.hongte.alms.base.mapper;

import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.hongte.alms.base.entity.WithholdingRepaymentLog;
import com.hongte.alms.base.vo.module.InfoSmsListSearchReq;
import com.hongte.alms.base.vo.module.InfoSmsListSearchVO;
import com.hongte.alms.base.vo.module.RepaymentLogReq;
import com.hongte.alms.base.vo.module.RepaymentLogVO;
import com.hongte.alms.common.mapper.SuperMapper;
 
/**
 * <p>
 * 还款计划代扣日志流水表 Mapper 接口
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-03-08
 */
public interface WithholdingRepaymentLogMapper extends SuperMapper<WithholdingRepaymentLog> {
    /**
     * 分页查询
     * @param key
     * @return
     */
    List<RepaymentLogVO> selectRepaymentLogList(Pagination pages,RepaymentLogReq key);
    
    /**
     * 分页查询
     * @param key
     * @return
     */
    List<RepaymentLogVO> selectRepaymentLogList(RepaymentLogReq key);

}
