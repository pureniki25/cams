package com.hongte.alms.base.collection.mapper;

import com.hongte.alms.base.collection.entity.Parametertracelog;
import com.hongte.alms.common.mapper.SuperMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-05-15
 */
public interface ParametertracelogMapper extends SuperMapper<Parametertracelog> {

    List<Parametertracelog> selectUnTransParametertracelogs();
}
