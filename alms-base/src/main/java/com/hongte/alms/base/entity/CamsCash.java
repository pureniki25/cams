package com.hongte.alms.base.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

/**
 * <p>
 * 现金支付
 * </p>
 *
 * @author czs
 * @since 2019-06-17
 */
@Data
public class CamsCash extends CamsSubject {

	private static final long serialVersionUID = 1L;

	private String companyName;
	@JsonFormat(pattern = "yyyy-MM-dd",timezone="GMT+8")
	private String openDate;
	private List<CamsSubject> cashSubjects;
	private List<CamsSubject> cash2Subjects;
	private List<CamsSubject> cash3Subjects;
}
