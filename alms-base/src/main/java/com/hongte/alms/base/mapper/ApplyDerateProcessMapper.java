package com.hongte.alms.base.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.hongte.alms.base.entity.ApplyDerateProcess;
import com.hongte.alms.base.vo.module.ApplyDerateListSearchReq;
import com.hongte.alms.base.vo.module.ApplyDerateVo;
import com.hongte.alms.common.mapper.SuperMapper;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 曾坤
 * @since 2018-02-05
 */
public interface ApplyDerateProcessMapper extends SuperMapper<ApplyDerateProcess> {



    /**
     * 分页查询
     * @param key
     * @return
     */
    List<ApplyDerateVo> selectApplyDerateList(Pagination pages,ApplyDerateListSearchReq key);

    /**
     * 查询所有，不分页
     * @param key
     * @return
     */
    List<ApplyDerateVo> selectApplyDerateList(ApplyDerateListSearchReq key);
    
    String queryCurrentStatusByCondition(@Param(value="businessId") String businessId, @Param(value="planListId") String planListId);


}
