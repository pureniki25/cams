/**
 * Copyright © 2018 HONGTE. All rights reserved.
 *
 * @Title: BusinessAttchInfo.java
 * @Prject: alms-base
 * @Package: com.hongte.alms.base.vo.cams
 * @Description: 附件信息
 * @author: adolp
 * @date: 2018年11月29日 下午8:25:45
 * @version: V1.0
 */
package com.hongte.alms.base.vo.cams;

import lombok.Data;

/**
 * @ClassName: BusinessAttchInfo
 * @Description: 附件信息
 * @author: adolp
 * @date: 2018年11月29日 下午8:25:45
 *
 */
@Data
public class BusinessAttchInfo {

    /**
     * 附件名称
     */
    String attachmentName;

    /**
     * 附件文件URL
     */
    String attachmentURL;

}
