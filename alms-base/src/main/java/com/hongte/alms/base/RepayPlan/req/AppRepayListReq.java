package com.hongte.alms.base.RepayPlan.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

import javax.validation.constraints.NotNull;

/**
 * @author czs
 * @since 2018/7/18
 */
@Data
@ApiModel("提供APP的还款计划的请求对象")
public class AppRepayListReq {

    /**
     * 身份证号码
     */
    @ApiModelProperty(value = "身份证号码")
    @NotNull(message = "身份证号码(identifyCard)不能为空")
    private String identifyCard;
    /**
     * 需要的业务类型的集合
     */
    @ApiModelProperty(value = "业务类型")
    @NotNull(message = "业务类型(businessTypes)不能为空")
    private List<String> businessTypes;
    
    /**
     * 当前是第几页
     */
    @ApiModelProperty(value = "当前是第几页")
    @NotNull(message = "当前是第几页(curPageNo)不能为空")
    private Integer curPageNo;
    
    
    /**
     * 每页记录数
     */
    @NotNull(message = "每页记录数(pageSize)不能为空")
    @ApiModelProperty(value = "每页记录数")
    private Integer pageSize;
    
    
    /**
     * 是否过滤结清数据 1：过滤  其他：返回全部
     */ 
    @ApiModelProperty(value = "是否过滤结清数据")
    private Integer isSettle;


}
