package com.hongte.alms.base.feignClient.dto;

import java.io.Serializable;
import java.util.List;

import lombok.Data;

/**
 * @author:陈泽圣
 * 客户信息
 * @date: 2018/6/4
 */
@Data
public class KuaiQianRechargeReqDto implements Serializable {
	 private static final long serialVersionUID = -4095682638492039883L;
      private String interactiveStatus;
      private String spFlag;
      private String txnType;
      private String merchantId;
      private String terminalId;
      private String externalRefNumber;
      private String entryTime;
      private String cardNo;
      private String idType;
      private String cardHolderName;
      private String cardHolderId;
      private String amount;
      private String customerId;
      private String payToken;
      private String tr3Url;
      private ExtMap extMap;
    
}
