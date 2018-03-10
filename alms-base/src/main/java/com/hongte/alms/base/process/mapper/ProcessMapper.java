package com.hongte.alms.base.process.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.hongte.alms.base.process.entity.Process;
import com.hongte.alms.base.process.vo.ProcessPageRequest;
import com.hongte.alms.base.process.vo.ProcessReq;
import com.hongte.alms.base.process.vo.ProcessStartPageRequest;
import com.hongte.alms.base.process.vo.ProcessVo;
import com.hongte.alms.base.vo.module.ApplyDerateListSearchReq;
import com.hongte.alms.base.vo.module.ApplyDerateVo;
import com.hongte.alms.common.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 流程信息 Mapper 接口
 * </p>
 *
 * @author 曾坤
 * @since 2018-02-05
 */
public interface ProcessMapper extends SuperMapper<Process> {

    /**
     *
     * @param pages
     * @param key
     * @return
     */
//    List<ProcessLitigationVo> selectPage4Me(Pagination pages, @Param(value="key") String key);

    List<ProcessVo> GetList(Pagination pages, @Param(value = "key") String key);

    /**
     *更新审批节点的步骤及审核人ID
     */
    void updateProcessStep(@Param(value="processID") String processID,@Param(value="approveUserID") String approveUserID,@Param(value="currentStep")Integer currentStep);

    List<Process> getRelatedProcess(@Param(value="businessId") String businessId, @Param(value="processTypeID")  String processTypeID);

    /**
     *更新审批节点的回退信息
     */
    void updateProcessBack(@Param(value="processID") String processID,@Param(value="backStep") Integer backStep,@Param(value="isDirectBack")Integer isDirectBack);

    /**
     *流程审批查询页面
     */
    List<Map<String, Object>> getProcessManagerList(Page<Map<String, Object>> pages, @Param(value = "requestInfo") ProcessPageRequest requestInfo);

    /**
     *流程发起查询页面
     */
    List<Map<String, Object>> getProcessStartList(Page<Map<String, Object>> pages,@Param(value = "requestInfo") ProcessStartPageRequest requestInfo);

    /**
     *动态SQL查询
     */
    List<Map<String, Object>> querySql(@Param(value = "sql") String sql);

    /**
     *根据业务编码和流程类别，返回该业务最新流程信息
     */
   Process  getLastProcess(@Param(value = "processId") String processId,@Param(value = "typeId") String typeId);


//    public Page<ApplyDerateVo> selectApplyDeratePage(ApplyDerateListSearchReq key){
    /**
     * 分页查询
     * @param key
     * @return
     */
    List<ProcessVo> selectProcessVoList(Pagination pages, ProcessReq key);

    /**
     * 查询所有，不分页
     * @param key
     * @return
     */
    List<ProcessVo> selectProcessVoList(ProcessReq key);

}
