package com.hongte.alms.common.service.impl;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.baomidou.mybatisplus.mapper.Condition;
import com.baomidou.mybatisplus.mapper.SqlHelper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hongte.alms.common.service.BaseService;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class BaseServiceImpl<M extends BaseMapper<T>, T> extends ServiceImpl<BaseMapper<T>, T> implements BaseService<T> {

    /**
     * 封装分页查询
     *
     * @param page 分页数据
     * @param wrapper
     * @return
     * @auther 张贵宏
     */
    @Override
    public Page<T> selectByPage(Page<T> page, Wrapper<T> wrapper) {
        if (null != page) {
            // wrapper 不存创建一个 Condition
            if (SqlHelper.isEmptyOfWrapper(wrapper)) {
                wrapper = Condition.create();
            }
            // 排序
            if (page.isOpenSort() && StringUtils.isNotEmpty(page.getOrderByField())) {
                wrapper.orderBy(page.getOrderByField(), page.isAsc());
            }

            // MAP 参数查询
            /*if (MapUtils.isNotEmpty(page.getCondition())) {
                wrapper.allEq(page.getCondition());
            }*/

            Map<String, Object> whereParams = page.getCondition();
            if (whereParams != null && whereParams.size() > 0) {
                for (Map.Entry<String, Object> ent : whereParams.entrySet()) {
                    // 过滤掉空值
                    String key = ent.getKey();
                    Object value = ent.getValue();
                    if (StringUtils.isBlank(String.valueOf(value))) {
                        //whereParams.remove(key);
                        continue;
                    }
                    int split = StringUtils.indexOf(key, "_");

                    // 拆分operator与filedAttribute
                    String operator = StringUtils.substring(key, 0, split);

                    key = StringUtils.substring(key, split + 1, key.length());

                    if (operator == null) {
                        continue;
                    }

                    switch (operator.toUpperCase()) {
                        case "EQ":
                            wrapper.eq(key, value);
                            break;
                        case "NE":
                            wrapper.ne(key, value);
                            break;
                        case "GT":
                            wrapper.gt(key, value);
                            break;
                        case "LT":
                            wrapper.lt(key, value);
                            break;
                        case "GE":
                            wrapper.ge(key, value);
                            break;
                        case "LE":
                            wrapper.le(key, value);
                            break;
                        case "LIKE":
                            wrapper.like(key, value.toString());
                            break;
                    }
                }
            }

        }
        page.setRecords(baseMapper.selectPage(page, wrapper));
        return page;
    }

    /**
     * 封装分页查询
     * @auther 张贵宏
     * @param page 分页数据
     * @return
     */
    @Override
    public Page<T> selectByPage(Page<T> page) {
        return selectByPage(page, null);
    }
}
