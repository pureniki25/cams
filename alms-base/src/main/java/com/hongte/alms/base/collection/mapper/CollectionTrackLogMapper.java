package com.hongte.alms.base.collection.mapper;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.hongte.alms.base.collection.entity.CollectionTrackLog;
import com.hongte.alms.base.collection.vo.CollectionTrackLogVo;
import com.hongte.alms.common.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 贷后跟踪记录表 Mapper 接口
 * </p>
 *
 * @author 曾坤
 * @since 2018-02-01
 */
public interface CollectionTrackLogMapper extends SuperMapper<CollectionTrackLog> {


    /**
     * 根据还款计划ID查找还款催收记录
     * @param rbpId
     * @return
     */
    List<CollectionTrackLogVo> selectCollectionTrackLogByRbpId(@Param("rbpId")String rbpId );
    List<CollectionTrackLogVo> selectCollectionTrackLogByRbpId(Pagination pages,@Param("rbpId")String rbpId );

}
