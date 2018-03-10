package com.hongte.alms.scheduled.vo.modules.car;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author dengzhiming
 * @date 2018/2/26 19:03
 */
public class CarDragRegistrationInfo {

    public Date getDragDate() {
        return dragDate;
    }

    public void setDragDate(Date dragDate) {
        this.dragDate = dragDate;
    }

    public String getDragHandler() {
        return dragHandler;
    }

    public void setDragHandler(String dragHandler) {
        this.dragHandler = dragHandler;
    }

    public BigDecimal getFee() {
        return fee;
    }

    public void setFee(BigDecimal fee) {
        this.fee = fee;
    }

    public BigDecimal getOtherFee() {
        return otherFee;
    }

    public void setOtherFee(BigDecimal otherFee) {
        this.otherFee = otherFee;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getDetailAddress() {
        return detailAddress;
    }

    public void setDetailAddress(String detailAddress) {
        this.detailAddress = detailAddress;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    /**
     * [业务单号]
     */

    private String businessId;

    /**
     * [拖车日期]
     */
    private Date dragDate;

    /**
     * [拖车经办人]
     */
    private String dragHandler;

    /**
     * [拖车费]
     */
    private BigDecimal fee;
    /**
     * 其他相关费用
     */

    private BigDecimal otherFee;
    /**
     * [备注]
     */

    private String remark;

    /**
     * [拖车省份]
     */
    private String province;

    /**
     * [拖车市]
     */
    private String city;

    /**
     * [拖车县]
     */
    private String county;

    /**
     * [拖车地址]
     */
    private String detailAddress;
}
