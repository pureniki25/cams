package com.hongte.alms.base.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.hongte.alms.base.collection.vo.DeductionVo;
import com.hongte.alms.base.entity.ApplyDerateProcess;
import com.hongte.alms.base.entity.InfoSms;
import com.hongte.alms.base.vo.module.ApplyDerateListSearchReq;
import com.hongte.alms.base.vo.module.ApplyDerateVo;
import com.hongte.alms.base.vo.module.InfoSmsListSearchReq;
import com.hongte.alms.base.vo.module.InfoSmsListSearchVO;
import com.hongte.alms.common.mapper.SuperMapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 陈泽圣
 * @since 2018-03-02
 */
public interface DeductionMapper extends SuperMapper<DeductionVo> {




	DeductionVo selectDeductionInfoByPlanListId( @Param("planListId")String planListId);


}
