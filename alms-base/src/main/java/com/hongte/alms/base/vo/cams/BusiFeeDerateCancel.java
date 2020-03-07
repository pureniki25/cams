/**
 * Copyright © 2018 HONGTE. All rights reserved.
 *
 * @Title: BusiFeeDerateCancel.java
 * @Prject: alms-base
 * @Package: com.hongte.alms.base.vo.cams
 * @Description: 业务单期费用减免撤销信息
 * @author: adolp
 * @date: 2018年11月29日 下午8:27:26
 * @version: V1.0
 */
package com.hongte.alms.base.vo.cams;

import java.util.Date;

import lombok.Data;

/**
 * @ClassName: BusiFeeDerateCancel
 * @Description: 业务单期费用减免撤销信息
 * @author: adolp
 * @date: 2018年11月29日 下午8:27:26
 *
 */
@Data
public class BusiFeeDerateCancel {

    /**
     * 减免编号
     */
    String derateId;

    /**
     * 撤销日期
     */
    Date cancelDate;

}
