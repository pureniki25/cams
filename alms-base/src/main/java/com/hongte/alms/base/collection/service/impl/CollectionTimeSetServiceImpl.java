package com.hongte.alms.base.collection.service.impl;


import com.hongte.alms.base.collection.entity.CollectionTimeSet;
import com.hongte.alms.base.collection.mapper.CollectionTimeSetMapper;
import com.hongte.alms.base.collection.service.CollectionTimeSetService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 清算时间设置表 服务实现类
 * </p>
 *
 * @author 曾坤
 * @since 2018-03-06
 */
@Service("CollectionTimeSetService")
public class CollectionTimeSetServiceImpl extends BaseServiceImpl<CollectionTimeSetMapper, CollectionTimeSet> implements CollectionTimeSetService {

}
