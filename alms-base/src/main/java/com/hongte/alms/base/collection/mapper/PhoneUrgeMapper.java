package com.hongte.alms.base.collection.mapper;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.hongte.alms.base.collection.vo.AfterLoanStandingBookReq;
import com.hongte.alms.base.collection.vo.AfterLoanStandingBookVo;
import com.hongte.alms.base.collection.vo.StaffBusinessReq;
import com.hongte.alms.base.collection.vo.StaffBusinessVo;
import com.hongte.alms.common.mapper.SuperMapper;

import java.util.List;

/**
 * <p>
 * 电催列表 Mapper 接口
 * </p>
 *
 * @author zengkun
 * @since 2018-01-24
 */
public interface PhoneUrgeMapper extends SuperMapper<StaffBusinessVo> {
    /**
     *
     * @param pages
     * @param key
     * @return
     */
    List<StaffBusinessVo> selectphoneurgepage(Pagination pages, StaffBusinessReq key);


    /**
     * 查询贷后台账
     * @param pages
     * @param key
     * @return
     */
    List<AfterLoanStandingBookVo> selectAfterLoadStanding(Pagination pages, AfterLoanStandingBookReq key);
    List<AfterLoanStandingBookVo> selectAfterLoadStanding(AfterLoanStandingBookReq key);
    
    /**
     * 代扣管理台账
     * @param pages
     * @param key
     * @return
     */
    List<AfterLoanStandingBookVo> selectRepayManage(Pagination pages, AfterLoanStandingBookReq key);
    



}
