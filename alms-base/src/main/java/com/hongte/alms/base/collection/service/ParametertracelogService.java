package com.hongte.alms.base.collection.service;

import com.hongte.alms.base.collection.entity.Parametertracelog;
import com.hongte.alms.common.service.BaseService;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 曾坤
 * @since 2018-05-15
 */
public interface ParametertracelogService extends BaseService<Parametertracelog> {


    /**
     * 选择未同步的信贷历史记录
     * @return
     */
    List<Parametertracelog> selectUnTransParametertracelogs();

}
