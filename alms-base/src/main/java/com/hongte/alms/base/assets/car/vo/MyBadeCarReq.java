package com.hongte.alms.base.assets.car.vo;

import com.hongte.alms.common.vo.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author dengzhiming
 * @date 2018/5/30 18:12
 */
@ApiModel(value="我报过价的车辆请求对象",description="我报过价的车辆请求对象")
public class MyBadeCarReq extends PageRequest {
    /**
     * 用户Id
     */
    @ApiModelProperty(value = "用户Id", name = "userId")
    private String userId;

    /**
     * 品牌
     */
    @ApiModelProperty(value = "品牌", name = "vehicleBrand")
    private String vehicleBrand;
    /**
     * 车辆型号
     */
    @ApiModelProperty(value = "车辆型号", name = "carModel")
    private String carModel;

    public String getUserId() {
        return userId;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getVehicleBrand() {
        return vehicleBrand;
    }
    public void setVehicleBrand(String vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }
    public String getCarModel() {
        return carModel;
    }
    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

}
