package com.hongte.alms.base.service;

import com.baomidou.mybatisplus.plugins.Page;
import com.hongte.alms.base.entity.ApplyDerateProcess;
import com.hongte.alms.base.vo.module.ApplyDerateProcessReq;
import com.hongte.alms.base.process.vo.ProcessLogReq;
import com.hongte.alms.base.vo.module.ApplyDerateListSearchReq;
import com.hongte.alms.base.vo.module.ApplyDerateVo;
import com.hongte.alms.common.service.BaseService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 曾坤
 * @since 2018-02-05
 */
public interface ApplyDerateProcessService extends BaseService<ApplyDerateProcess> {

    /**
     * 存储申请减免的信息
     * @param req
     */
    void saveApplyDerateProcess(ApplyDerateProcessReq req) throws IllegalAccessException, InstantiationException;

    /**
     * 存储减免审批的记录信息
     * @param req
     */
    void saveApplyDerateProcessLog(ProcessLogReq req) throws IllegalAccessException, InstantiationException;


    /**
     * 分页查询
     * @param key
     * @return
     */
    Page<ApplyDerateVo> selectApplyDeratePage(ApplyDerateListSearchReq key);



    /**
     * 查询所有，不分页
     * @param key
     * @return
     */
    List<ApplyDerateVo> selectApplyDerateList(ApplyDerateListSearchReq key);


}
