package com.hongte.alms.core.vo.modules.car;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * @author dengzhiming
 * @date 2018/2/26 19:03
 */
public class CarDragRegistrationInfo {

    public String getDragDate() {
        return dragDate;
    }

    public void setDragDate(String dragDate) {
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

    public List<CarDragDoc> getAttachments() {
        return attachments;
    }

    public void setAttachments(List<CarDragDoc> attachments) {
        this.attachments = attachments;
    }

    private List<CarDragDoc> attachments;

    /**
     * [业务单号]
     */

    private String businessId;

    /**
     * [拖车日期]
     */
    private String dragDate;

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
