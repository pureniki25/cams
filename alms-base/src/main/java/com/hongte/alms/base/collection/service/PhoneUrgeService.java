package com.hongte.alms.base.collection.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.hongte.alms.base.collection.vo.AfterLoanStandingBookReq;
import com.hongte.alms.base.collection.vo.AfterLoanStandingBookVo;
import com.hongte.alms.base.collection.vo.StaffBusinessReq;
import com.hongte.alms.base.collection.vo.StaffBusinessVo;
import com.hongte.alms.common.service.BaseService;

import java.util.List;

/**
 * <p>
 *电催流程 服务类
 * </p>
 *
 * @author zengkun
 * @since 2018-01-24
 */
public interface PhoneUrgeService   extends BaseService<StaffBusinessVo>{

    /**
     * 分页查询
     * @param pages
     * @param key
     * @return
     */
//    Page<StaffBusinessVo> selectPhoneUrgePage(Page<StaffBusinessVo> pages, StaffBusinessReq key);

    /**
     * 分页查询
     * @param key
     * @return
     */
    Page<AfterLoanStandingBookVo> selectAfterLoanStandingBookPage(AfterLoanStandingBookReq key);

    /**
     * 查询所有，不分页
     * @param key
     * @return
     */
    List<AfterLoanStandingBookVo> selectAfterLoanStandingBookList(AfterLoanStandingBookReq key);

    
    /**
     * 代扣管理台账
     * @param key
     * @return
     */
    Page<AfterLoanStandingBookVo> selectRepayManage(AfterLoanStandingBookReq key);


}
