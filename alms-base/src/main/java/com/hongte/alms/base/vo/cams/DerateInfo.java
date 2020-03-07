/**
 * Copyright © 2018 HONGTE. All rights reserved.
 *
 * @Title: DerateInfo.java
 * @Prject: alms-base
 * @Package: com.hongte.alms.base.vo.cams
 * @Description: TODO
 * @author: adolp
 * @date: 2018年11月29日 下午2:55:01
 * @version: V1.0
 */
package com.hongte.alms.base.vo.cams;

import java.util.Date;

import lombok.Data;

/**
 * @ClassName: DerateInfo
 * @Description: 平面化存储预同步到核心的减免信息
 * @author: adolp
 * @date: 2018年11月29日 下午2:55:01
 *
 */
@Data
public class DerateInfo {

    /**
     * 减免记录id
     */
    String ids;
    /**
     * 减免状态，0正常 1撤销
     */
    Integer statusFlag;
    /**
     * 减免信息状态更新时间
     */
    Date statusTime;
    /**
     * 减免单号
     */
    String businessId;
    /**
     * 减免期号
     */
    String afterId;
    /**
     * 业务单的分公司名称
     */
    String branchName;
    /**
     * 主借人
     */
    String persons;
    /**
     * 业务类型
     */
    String businessProduct;
    /**
     * 资产端
     */
    Integer assetType;
    /**
     * 发标平台
     */
    Integer biddingPlatform;
    /**
     * 减免类型 0月还减免 1结清减免
     */
    Integer type;
    /**
     * 减免编号 还款核心流水记录id
     */
    String derateId;
    /**
     * 减免申请日期
     */
    Date applyDate;
    /**
     * 减免通过日期
     */
    Date passDate;
    /**
     * 费用名称
     */
    String feeName;
    /**
     * 费用金额（实际减免金额）
     */
    Double feeValue;
    /**
     * 申请流程编号
     */
    String processId;
    /**
     * 同步次数
     */
    Integer isSync;

}
