/**
 * Copyright © 2018 HONGTE. All rights reserved.
 *
 * @Title: BusinessFeeInfo.java
 * @Prject: alms-base
 * @Package: com.hongte.alms.base.vo.cams
 * @Description: 业务单期费用信息
 * @author: adolp
 * @date: 2018年11月29日 下午8:24:53
 * @version: V1.0
 */
package com.hongte.alms.base.vo.cams;

import lombok.Data;

/**
 * @ClassName: BusinessFeeInfo
 * @Description: 业务单期费用信息
 * @author: adolp
 * @date: 2018年11月29日 下午8:24:53
 *
 */
@Data
public class BusinessFeeInfo {

    /**
     * 费用名称
     */
    String feeName;

    /**
     * 费用金额
     */
    Double feeValue;

}