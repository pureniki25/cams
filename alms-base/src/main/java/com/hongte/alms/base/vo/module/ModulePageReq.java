package com.hongte.alms.base.vo.module;

import com.hongte.alms.common.vo.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: 黄咏康
 * @Date: 2018/1/15 0015 上午 1:23
 */
@ApiModel(value="菜单分页查询请求对象",description="菜单分页查询请求对象")
public class ModulePageReq extends PageRequest{
    @ApiModelProperty(value="查询菜单名",name="moduleName",example="test")
    private String moduleName;

    @ApiModelProperty(value="查询菜单级别",name="moduleLevel",example="test",dataType = "int")
    private Integer moduleLevel;

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public Integer getModuleLevel() {
        return moduleLevel;
    }

    public void setModuleLevel(Integer moduleLevel) {
        this.moduleLevel = moduleLevel;
    }
}
