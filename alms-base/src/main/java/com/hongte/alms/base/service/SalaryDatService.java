package com.hongte.alms.base.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.hongte.alms.base.entity.SalaryDat;
import com.hongte.alms.base.enums.CamsConstant;
import com.hongte.alms.common.service.BaseService;

/**
 * <p>
 * 薪资表 服务类
 * </p>
 *
 * @author czs
 * @since 2019-09-01
 */
public interface SalaryDatService extends BaseService<SalaryDat> {
	public void importSalary(MultipartFile file, String companyName, String type, String openDate) throws Exception ;
	
	public void devide(Map<String,Object> map) throws Exception;
	
	public void importSheBaoExcel(MultipartFile file, String companyName, String openDate) throws Exception ;
	public void addJtSalary(String keMuDaiMa,String openDate,String companyName,String localAmount,CamsConstant.DirectionEnum directionEnum,String pingZhengHao) throws Exception;

	public void insertGongJiJin(List<SalaryDat> salarys, String openDate, String companyName) throws Exception ;

	public BigDecimal getChengBenJine(String openDate, String companyName) throws Exception;
	
	
	/**
	 * 
	 * @param cardNo 身份证号
	 * @param openDate  计算当月之前的工资总和
	 * @param companyName 公司名称
	 * @return
	 * @throws Exception
	 */
	public Map<String,BigDecimal> getPersonSalaryBefore(String cardNo, String openDate, String companyName) throws Exception ;
	
	
	public void createPersonTax(List<SalaryDat> salaryDats) throws Exception;
}



