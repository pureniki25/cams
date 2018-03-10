package com.hongte.alms.common.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * layTable 数据请求参数
 * @Author: 黄咏康
 * @Date: 2018/1/14 0014 下午 8:55
 */
@ApiModel(value="PageRequest对象",description="基础分页请求对象")
public class PageRequest {
    @ApiModelProperty(value="当前页码",name="page",example="1",required = true)
    private int page;
    @ApiModelProperty(value="每页数据量",name="limit",example="10",required = true)
    private int limit;

    public int getPage() {
        return page<=0?1:page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getLimit() {
        return limit<=0?10:limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }


}
