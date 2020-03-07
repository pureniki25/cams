/**
 * Copyright © 2018 HONGTE. All rights reserved.
 *
 * @Title: BusinessNumInfo.java
 * @Prject: alms-base
 * @Package: com.hongte.alms.base.vo.cams
 * @Description: 业务单期费用减免信息
 * @author: adolp
 * @date: 2018年11月29日 下午8:23:45
 * @version: V1.0
 */
package com.hongte.alms.base.vo.cams;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * @ClassName: BusinessNumInfo
 * @Description: 业务单期费用减免信息
 * @author: adolp
 * @date: 2018年11月29日 下午8:23:45
 *
 */
@Data
public class BusinessNumInfo {

    /**
     * 业务编号
     */
    String businessId;

    /**
     * 期数
     */
    String businessAfterId;

    /**
     * 分公司名称
     */
    String branchName;

    /**
     * 借款人姓名
     */
    String persons;

    /**
     * 业务类型
     */
    String businessProduct;

    /**
     * 所属资产端
     */
    Integer assetType;

    /**
     * 发标平台
     */
    Integer biddingPlatform;

    /**
     * 减免类型
     */
    Integer type;

    /**
     * 减免编号
     */
    String derateId;

    /**
     * 交易活动
     */
    Integer actionId;

    /**
     * 申请日期
     */
    Date applyDate;

    /**
     * 通过日期
     */
    Date passDate;

    /**
     * 减免费用信息
     */
    List<BusinessFeeInfo> fees = new ArrayList<>(0);

    /**
     * 附件文件信息
     */
    List<BusinessAttchInfo> attchs = new ArrayList<>(0);
}
