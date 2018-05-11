package com.hongte.alms.base.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.ApplyDerateProcess;
import com.hongte.alms.base.entity.InfoSms;
import com.hongte.alms.base.vo.module.ApplyDerateProcessReq;
import com.hongte.alms.base.process.vo.ProcessLogReq;
import com.hongte.alms.base.vo.module.ApplyDerateListSearchReq;
import com.hongte.alms.base.vo.module.ApplyDerateVo;
import com.hongte.alms.base.vo.module.InfoSmsListSearchReq;
import com.hongte.alms.base.vo.module.InfoSmsListSearchVO;
import com.hongte.alms.common.service.BaseService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author chenzs
 * @since 2018-03-02
 */
public interface InfoSmsService extends BaseService<InfoSms> {



    /**
     * 分页查询
     * @param key
     * @return
     */
    Page<InfoSmsListSearchVO> selectInfoSmsPage(InfoSmsListSearchReq key);
    /**
     * 短信查询上一条
     * @param key
     * @return
     */
    InfoSmsListSearchVO selectLastInfoSmsDetail(Integer logId);
    /**
     * 短信查询下一条
     * @param key
     * @return
     */
    InfoSmsListSearchVO selectNextInfoSmsDetail(Integer logId);

    /**
     * 短信查询当前条
     * @param key
     * @return
     */
    InfoSmsListSearchVO selectInfoSmsDetail(Integer logId);

}
