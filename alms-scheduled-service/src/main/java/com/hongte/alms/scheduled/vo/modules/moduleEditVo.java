package com.hongte.alms.scheduled.vo.modules;

import io.swagger.annotations.ApiModelProperty;

/**
 * @Author: 黄咏康
 * @Date: 2018/1/18 0018 下午 9:22
 */
public class moduleEditVo {
    @ApiModelProperty
    private String moduleId;
    @ApiModelProperty(required = true)
    private String moduleName;
    @ApiModelProperty(required = true)
    private String moduleLevel;
    @ApiModelProperty(required = true)
    private String parentModuleName;
    @ApiModelProperty(required = true)
    private String moduleUrl;
    @ApiModelProperty(required = true)
    private String deviceType;
    @ApiModelProperty(required = true)
    private boolean moduleStatus;
    private String moduleOrder;
    private String moduleIcon;
    private String moduleDesc;

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleLevel() {
        return moduleLevel;
    }

    public void setModuleLevel(String moduleLevel) {
        this.moduleLevel = moduleLevel;
    }

    public String getParentModuleName() {
        return parentModuleName;
    }

    public void setParentModuleName(String parentModuleName) {
        this.parentModuleName = parentModuleName;
    }

    public String getModuleUrl() {
        return moduleUrl;
    }

    public void setModuleUrl(String moduleUrl) {
        this.moduleUrl = moduleUrl;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public boolean isModuleStatus() {
        return moduleStatus;
    }

    public void setModuleStatus(boolean moduleStatus) {
        this.moduleStatus = moduleStatus;
    }

    public String getModuleOrder() {
        return moduleOrder;
    }

    public void setModuleOrder(String moduleOrder) {
        this.moduleOrder = moduleOrder;
    }

    public String getModuleIcon() {
        return moduleIcon;
    }

    public void setModuleIcon(String moduleIcon) {
        this.moduleIcon = moduleIcon;
    }

    public String getModuleDesc() {
        return moduleDesc;
    }

    public void setModuleDesc(String moduleDesc) {
        this.moduleDesc = moduleDesc;
    }
}
