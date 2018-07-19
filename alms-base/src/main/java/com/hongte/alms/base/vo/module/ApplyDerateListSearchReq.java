package com.hongte.alms.base.vo.module;

import com.hongte.alms.common.vo.PageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author zengkun
 * @since 2018/2/8
 */
@ApiModel(value="贷后台账查询请求对象",description="贷后台账查询请求对象")
public class ApplyDerateListSearchReq extends PageRequest{
    @ApiModelProperty(value="业务编号",name="keyName",example="test" ,dataType = "String")
    private String keyName      	;     //业务编号 或客户名称
    @ApiModelProperty(value="区域ID",name="areaId",example="test" ,dataType = "String")
    private String areaId  			; //区域ID
    @ApiModelProperty(value="分公司ID",name="companyId",example="test" ,dataType = "String")
    private String companyId			; //分公司ID
    @ApiModelProperty(value="业务类型",name="businessType",example="test" ,dataType = "int")
    private Integer   businessType		; //业务类型
    @ApiModelProperty(value="减免时间",name="derateDateBegin",example="test" ,dataType = "java.util.Date")
    private Date derateDateBegin	; 	//减免时间 开始
    @ApiModelProperty(value="减免时间",name="derateDateEnd",example="test" ,dataType = "java.util.Date")
    private Date    derateDateEnd		; //减免时间 结束

    private BigDecimal derateMoneyBegin	;   //减免金额 开始
//    @ApiModelProperty(value="减免金额",name="derateMoneyBeginStr",example="test",dataType = "String")
//    private String derateMoneyBeginStr	;   //减免金额 开始

    private BigDecimal   derateMoneyEnd	;  //减免金额 结束
//    @ApiModelProperty(value="减免金额",name="derateMoneyEndStr",example="test" ,dataType = "String")
//    private String   derateMoneyEndStr	;  //减免金额 结束

    private String userId;
    
    private Integer needPermission = 1; 

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getAreaId() {
        return areaId;
    }




	public String getKeyName() {
		return keyName;
	}




	public void setKeyName(String keyName) {
		this.keyName = keyName;
	}




	public void setAreaId(String areaId) {
        this.areaId = areaId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public Integer getBusinessType() {
        return businessType;
    }

    public void setBusinessType(Integer businessType) {
        this.businessType = businessType;
    }

    public Date getDerateDateBegin() {
        return derateDateBegin;
    }

    public void setDerateDateBegin(Date derateDateBegin) {
        this.derateDateBegin = derateDateBegin;
    }

    public Date getDerateDateEnd() {
        return derateDateEnd;
    }

    public void setDerateDateEnd(Date derateDateEnd) {
        this.derateDateEnd = derateDateEnd;
    }

    public BigDecimal getDerateMoneyBegin() {
        return derateMoneyBegin;
    }

    public void setDerateMoneyBegin(BigDecimal derateMoneyBegin) {
        this.derateMoneyBegin = derateMoneyBegin;
    }

    public BigDecimal getDerateMoneyEnd() {
        return derateMoneyEnd;
    }

    public void setDerateMoneyEnd(BigDecimal derateMoneyEnd) {
        this.derateMoneyEnd = derateMoneyEnd;
    }

	public Integer getNeedPermission() {
		return needPermission;
	}

	public void setNeedPermission(Integer needPermission) {
		this.needPermission = needPermission;
	}
    
//    public String getDerateMoneyBeginStr() {
//        return derateMoneyBeginStr;
//    }
//
//    public void setDerateMoneyBeginStr(String derateMoneyBeginStr) {
//        this.derateMoneyBeginStr = derateMoneyBeginStr;
////        if(derateMoneyBeginStr!=null){
////            this.derateMoneyBegin = new BigDecimal(derateMoneyBeginStr);
////        }
//    }
//
//    public String getDerateMoneyEndStr() {
//        return derateMoneyEndStr;
//    }
//
//    public void setDerateMoneyEndStr(String derateMoneyEndStr) {
//        this.derateMoneyEndStr = derateMoneyEndStr;
////        if(derateMoneyEndStr!=null){
////            this.derateMoneyEnd = new BigDecimal(derateMoneyEndStr);
////        }
//    }
}
