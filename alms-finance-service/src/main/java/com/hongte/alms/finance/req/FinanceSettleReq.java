package com.hongte.alms.finance.req;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class FinanceSettleReq {

    //必填,业务ID
    @NotNull(message="业务id不能为空")
    private String businessId ;
    //必填,期数afterId
//    @NotNull(message="还款计划期数afterId不能为空")
    private String afterId ;

    private String planId;

    //线下转账必填项,匹配的流水ID
    private List<String> mprIds;

    /**
     * 是否预览模式,true=预览,数据不存入数据库,false=保存,数据存入数据库
     */
    private Boolean preview ;


}
