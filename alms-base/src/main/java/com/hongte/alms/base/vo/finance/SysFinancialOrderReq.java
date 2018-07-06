package com.hongte.alms.base.vo.finance;

import lombok.Data;

import java.util.List;

/**
 * Created by 张贵宏 on 2018/7/4 16:51
 */
@Data
public class SysFinancialOrderReq {

    private List<String> collectionGroup1Users;

    private List<String> collectionGroup2Users;

    private List<String> companyId;

    private List<Integer> businessType;

    //旧的业务类型
    private Integer oldBizType;


    private String areaId;

    //编辑的标志位 1：编辑 0：新增
    private Integer editFalge;

}
