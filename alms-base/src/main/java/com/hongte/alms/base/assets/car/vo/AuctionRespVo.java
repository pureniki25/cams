package com.hongte.alms.base.assets.car.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.hongte.alms.base.entity.Doc;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel(value = "车辆拍卖信息", description = "返回车辆拍卖信息到app端")
public class AuctionRespVo implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    /**
     * 拍卖ID
     */
    @ApiModelProperty(value = "拍卖ID", name = "auctionId")
    private String auctionId;

    /**
     * 业务编号id
     */
    @ApiModelProperty(value = "业务编号id", name = "businessId")
    private String businessId;

    /**
     * 起拍价
     */
    @ApiModelProperty(value = "起拍价", name = "startPrice")
    private BigDecimal startPrice; //起拍价

    /**
     * 活动开始时间
     */
    @ApiModelProperty(value = "活动开始时间", name = "startPriceDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date startPriceDate;

    /**
     * 活动结束时间
     */
    @ApiModelProperty(value = "活动结束时间", name = "endPriceDate")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endPriceDate;


    /**
     * 咨询联系人
     */
    @ApiModelProperty(value = "咨询联系人", name = "contacts")
    private String contacts;

    /**
     * 咨询电话
     */
    @ApiModelProperty(value = "咨询电话", name = "telephone")
    private String telephone;

    /**
     * 支付方式
     */
    @ApiModelProperty(value = "支付方式", name = "paymentMethod")
    private String paymentMethod;

    /**
     * 交易地点
     */
    @ApiModelProperty(value = "交易地点", name = "auctionPosition")
    private String auctionPosition;

    /**
     * 拍卖备注
     */
    @ApiModelProperty(value = "拍卖备注", name = "remark")
    private String remark;

    /**
     * 车辆品牌
     */
    @ApiModelProperty(value = "车辆品牌", name = "vehicleBrand")
    private String vehicleBrand;

    /**
     * 汽车产地
     */
    @ApiModelProperty(value = "汽车产地", name = "carProduction")
    private String carProduction;

    /**
     * 车辆颜色
     */
    @ApiModelProperty(value = "车辆颜色", name = "carColour")
    private String carColour;

    /**
     * 车辆型号
     */
    @ApiModelProperty(value = "车辆型号", name = "carModel")
    private String carModel;

    /**
     * 排量
     */
    @ApiModelProperty(value = "排量", name = "displacement")
    private String displacement;

    /**
     * 发动机号
     */
    @ApiModelProperty(value = "发动机号", name = "engineNumber")
    private String engineNumber;

    /**
     * 车架号
     */
    @ApiModelProperty(value = "车架号", name = "frameNumber")
    private String frameNumber;

    /**
     * 车辆属地
     */
    @ApiModelProperty(value = "车辆属地", name = "vehicleTerritory")
    private String vehicleTerritory;

    /**
     * 使用性质
     */
    @ApiModelProperty(value = "使用性质", name = "useProperty")
    private String useProperty;

    /**
     * 保险到期日
     */
    @ApiModelProperty(value = "保险到期日", name = "insuranceDate ")
    @JsonFormat(pattern = "yyyy-MM-dd ", timezone = "GMT+8")
    private Date insuranceDate;

    /**
     * 年检到期日
     */
    @ApiModelProperty(value = "年检到期日", name = "inspectionDate ")
    private String inspectionDate;

    /**
     * 车显里程
     */
    @ApiModelProperty(value = "车显里程", name = "mileage ")
    private int mileage;

    /**
     * 首次登记年月
     */
    @ApiModelProperty(value = "首次登记年月", name = "registerDate")
    @JsonFormat(pattern = "yyyy-MM-dd ", timezone = "GMT+8")
    private Date registerDate;

    /**
     * 车辆抵押状态
     */
    @ApiModelProperty(value = "车辆抵押状态", name = "mortgageState")
    private String mortgageState;

    /**
     * 违章未处理记录
     */
    @ApiModelProperty(value = "违章未处理记录", name = "illegal")
    private String illegal;

    /**
     * 随车工具
     */
    @ApiModelProperty(value = "随车工具", name = "tools")
    private String tools;

    /**
     * 交易税费
     */
    @ApiModelProperty(value = "交易税费", name = "taxation")
    private String taxation;

    /**
     * 出价人数量
     */
    @ApiModelProperty(value = "出价人数量", name = "bidderCount")
    private Integer bidderCount;

    public String getAuctionId() {
        return auctionId;
    }

    public void setAuctionId(String auctionId) {
        this.auctionId = auctionId;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public BigDecimal getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(BigDecimal startPrice) {
        this.startPrice = startPrice;
    }

    public Date getStartPriceDate() {
        return startPriceDate;
    }

    public void setStartPriceDate(Date startPriceDate) {
        this.startPriceDate = startPriceDate;
    }

    public Date getEndPriceDate() {
        return endPriceDate;
    }

    public void setEndPriceDate(Date endPriceDate) {
        this.endPriceDate = endPriceDate;
    }

    public String getContacts() {
        return contacts;
    }

    public void setContacts(String contacts) {
        this.contacts = contacts;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getAuctionPosition() {
        return auctionPosition;
    }

    public void setAuctionPosition(String auctionPosition) {
        this.auctionPosition = auctionPosition;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getVehicleBrand() {
        return vehicleBrand;
    }

    public void setVehicleBrand(String vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }

    public String getCarProduction() {
        return carProduction;
    }

    public void setCarProduction(String carProduction) {
        this.carProduction = carProduction;
    }

    public String getCarColour() {
        return carColour;
    }

    public void setCarColour(String carColour) {
        this.carColour = carColour;
    }

    public String getCarModel() {
        return carModel;
    }

    public void setCarModel(String carModel) {
        this.carModel = carModel;
    }

    public String getDisplacement() {
        return displacement;
    }

    public void setDisplacement(String displacement) {
        this.displacement = displacement;
    }

    public String getEngineNumber() {
        return engineNumber;
    }

    public void setEngineNumber(String engineNumber) {
        this.engineNumber = engineNumber;
    }

    public String getFrameNumber() {
        return frameNumber;
    }

    public void setFrameNumber(String frameNumber) {
        this.frameNumber = frameNumber;
    }

    public String getVehicleTerritory() {
        return vehicleTerritory;
    }

    public void setVehicleTerritory(String vehicleTerritory) {
        this.vehicleTerritory = vehicleTerritory;
    }

    public String getUseProperty() {
        return useProperty;
    }

    public void setUseProperty(String useProperty) {
        this.useProperty = useProperty;
    }

    public Date getInsuranceDate() {
        return insuranceDate;
    }

    public void setInsuranceDate(Date insuranceDate) {
        this.insuranceDate = insuranceDate;
    }

    public String getInspectionDate() {
        return inspectionDate;
    }

    public void setInspectionDate(String inspectionDate) {
        this.inspectionDate = inspectionDate;
    }

    public int getMileage() {
        return mileage;
    }

    public void setMileage(int mileage) {
        this.mileage = mileage;
    }

    public Date getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(Date registerDate) {
        this.registerDate = registerDate;
    }

    public String getMortgageState() {
        return mortgageState;
    }

    public void setMortgageState(String mortgageState) {
        this.mortgageState = mortgageState;
    }

    public String getIllegal() {
        return illegal;
    }

    public void setIllegal(String illegal) {
        this.illegal = illegal;
    }

    public String getTools() {
        return tools;
    }

    public void setTools(String tools) {
        this.tools = tools;
    }

    public String getTaxation() {
        return taxation;
    }

    public void setTaxation(String taxation) {
        this.taxation = taxation;
    }

    public String getTransactionMode() {
        return transactionMode;
    }

    public void setTransactionMode(String transactionMode) {
        this.transactionMode = transactionMode;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public List<AuctionDocVo> getDocs() {
        return docs;
    }

    public void setDocs(List<AuctionDocVo> docs) {
        this.docs = docs;
    }

    public Integer getBidderCount() {
        return bidderCount;
    }

    public void setBidderCount(Integer bidderCount) {
        this.bidderCount = bidderCount;
    }

    /**
     * 交易方式
     */
    @ApiModelProperty(value = "交易方式", name = "transactionMode ")
    private String transactionMode;

    /**
     * 车辆位置
     */
    @ApiModelProperty(value = "车辆位置", name = "position")
    private String position;

    /**
     * 提供文件
     */
    @ApiModelProperty(value = "提供文件", name = "file")
    private String file;

    /**
     * 车辆备注
     */
    @ApiModelProperty(value = "车辆备注", name = "remarks")
    private String remarks;

    /**
     * 图片列表
     */
    @ApiModelProperty(value = "图片列表", name = "docs")
    private List<AuctionDocVo> docs;


}
