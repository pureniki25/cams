package com.hongte.alms.base.service.impl;

import com.hongte.alms.base.entity.ProductRestDat;
import com.hongte.alms.base.mapper.ProductRestDatMapper;
import com.hongte.alms.base.service.ProductRestDatService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 库存余额表 服务实现类
 * </p>
 *
 * @author czs
 * @since 2020-03-22
 */
@Service("ProductRestDatService")
public class ProductRestDatServiceImpl extends BaseServiceImpl<ProductRestDatMapper, ProductRestDat> implements ProductRestDatService {

}
