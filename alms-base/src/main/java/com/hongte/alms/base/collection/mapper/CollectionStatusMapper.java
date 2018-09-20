package com.hongte.alms.base.collection.mapper;

import com.hongte.alms.base.collection.dto.CollectionStatusCountDto;
import com.hongte.alms.base.collection.entity.CollectionStatus;
import com.hongte.alms.common.mapper.SuperMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 贷后催收状态表 Mapper 接口
 * </p>
 *
 * @author 曾坤
 * @since 2018-01-26
 */
public interface CollectionStatusMapper extends SuperMapper<CollectionStatus> {

    /**
     * 选择跟进期数最少的人员
     * @param staffType 催收人员类型
     * @param persons  催收人员列表
     * @param colStatus 催收状态
     * @param crpType 还款计划类型
     * @return
     */
    public CollectionStatus selectLimitPerson(
            @Param("staffType") String staffType,
            @Param("staffs")List<String> persons
    ,@Param("colStatus") Integer colStatus
    ,@Param("crpType") Integer crpType);

    /**
     * 选择所有分配过跟进期数的人员
     * @param staffType
     * @param persons
     * @param colStatus
     * @param crpType
     * @return
     */
    public List<CollectionStatusCountDto> selectAllPersons(
            @Param("staffType") String staffType,
            @Param("staffs")List<String> persons
            ,@Param("colStatus") Integer colStatus
            ,@Param("crpType") Integer crpType
    );
    /**
     * 获取最近一次已分配催收的CollectionStatus
     * @param businessId
     * @return
     */
    public CollectionStatus  getRecentlyCollectionStatus(@Param("planId") String planId,@Param("pListId") String pListId);

}
