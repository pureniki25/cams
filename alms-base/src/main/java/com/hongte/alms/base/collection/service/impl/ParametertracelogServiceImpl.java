package com.hongte.alms.base.collection.service.impl;

import com.hongte.alms.base.collection.entity.Parametertracelog;
import com.hongte.alms.base.collection.mapper.ParametertracelogMapper;
import com.hongte.alms.base.collection.service.ParametertracelogService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 胡伟骞
 * @since 2018-05-15
 */
@Service("ParametertracelogService")
public class ParametertracelogServiceImpl extends BaseServiceImpl<ParametertracelogMapper, Parametertracelog> implements ParametertracelogService {

    @Autowired
    ParametertracelogMapper parametertracelogMapper;

    @Override
    public List<Parametertracelog> selectUnTransParametertracelogs() {
       return parametertracelogMapper.selectUnTransParametertracelogs();
    }
}
