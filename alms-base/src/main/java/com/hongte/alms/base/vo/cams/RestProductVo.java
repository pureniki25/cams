package com.hongte.alms.base.vo.cams;

import java.io.Serializable;

import java.math.BigDecimal;
import java.util.Date;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hongte.alms.common.vo.PageRequest;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 产品表
 * </p>
 *
 * @author czs
 * @since 2019-01-26
 */
@Data
@ApiModel
public class RestProductVo extends PageRequest {
	private static final long serialVersionUID = 1L;

	private String openDate;
	private String productCode;
    private String companyName;
	private String productName;
	private String productType;
	private String unit;

	private String kuCunLiang;

	private String qiChuJine;

	private String incomeCount;

	private String incomeJine;

	private String outcomeCount;

	private String outcomeJine;

	private String restKuCunLiang;
	
	private String restJieCunJine;

	private String qiMoDanJia;

	private String productProperties;

	private BigDecimal unitPrice;

	private BigDecimal percent;

	private BigDecimal localAmount;

	private BigDecimal chengBenNum;
	@JsonFormat(timezone = "GMT-16",pattern = "yyyy-MM-dd")
	private Date beginDate;
	
	@JsonFormat(timezone = "GMT-16",pattern = "yyyy-MM-dd")
	private Date endDate;
	
	@JsonFormat(timezone = "GMT-16",pattern = "yyyy-MM-dd")
	private Date localBeginDate;
	
	@JsonFormat(timezone = "GMT-16",pattern = "yyyy-MM-dd")
	private Date localEndDate;
	
   


}
