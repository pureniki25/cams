package com.hongte.alms.common.service;

import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;



public interface BaseService<T> extends IService<T>{
    /**
     * 封装分页查询
     * @auther 张贵宏
     * @param page 分页数据
     * @param wrapper
     * @return
     */
    Page<T> selectByPage(Page<T> page, Wrapper<T> wrapper);

    /**
     * 封装分页查询
     * @auther 张贵宏
     * @param page 分页数据
     * @return
     */
    Page<T> selectByPage(Page<T> page);
}
 