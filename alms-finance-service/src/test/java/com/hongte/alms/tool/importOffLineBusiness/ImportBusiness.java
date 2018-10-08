/**
 * 
 */
package com.hongte.alms.tool.importOffLineBusiness;

import java.io.File;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.junit4.SpringRunner;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.RepayPlan.dto.PlanReturnInfoDto;
import com.hongte.alms.base.RepayPlan.req.BusinessBasicInfoReq;
import com.hongte.alms.base.RepayPlan.req.BusinessCustomerInfoReq;
import com.hongte.alms.base.RepayPlan.req.CreatRepayPlanReq;
import com.hongte.alms.base.RepayPlan.req.ProjInfoReq;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.util.DateUtil;
import com.hongte.alms.finance.FinanceServiceApplication;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.json.JSONObject;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;



/**
 * @author 王继光
 * 2018年9月11日 上午11:10:43
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = FinanceServiceApplication.class)
@EnableFeignClients
@ComponentScan({"com.hongte.alms"})
public class ImportBusiness {

	static Logger logger = LoggerFactory.getLogger(ImportBusiness.class);
	
	@Autowired
	@Qualifier("BasicBusinessService")
	BasicBusinessService basicBusinessService ;
	
	@Autowired
	@Qualifier("api")
	CreatRepayPlanRemoteApi api ;
	
	enum Dept {
		cz(208,"常州","常州分公司","常州分公司","5d98acc5-3e20-4eae-90f6-45e69265e2fc","华东片区"),
		cd(501,"成都","成都分公司","成都分公司","bb316e21-f1ef-45de-8a9c-326cc691f1a9","西北片区"),
		tj(402,"天津","天津分公司","天津分公司","57428c9c-1df4-4cfb-ae3c-d15da3d31dc8","华北片区"),
		sh(201,"上海","上海分公司","上海分公司","5d98acc5-3e20-4eae-90f6-45e69265e2fc","华东片区"),
		tx(102,"塘厦","东莞塘厦","东莞塘厦分公司","30f1c8d6-d087-4e72-915f-7b463cb4d740","华南片区"),
		yc(207,"盐城","盐城分公司","盐城分公司","5d98acc5-3e20-4eae-90f6-45e69265e2fc","华东片区"),
		zs(104,"中山","中山分公司","中山分公司","30f1c8d6-d087-4e72-915f-7b463cb4d740","华南片区"),
		wx(203,"无锡","无锡分公司","无锡分公司","5d98acc5-3e20-4eae-90f6-45e69265e2fc","华东片区"),
		hz(106,"惠州","惠州分公司","惠州分公司","30f1c8d6-d087-4e72-915f-7b463cb4d740","华南片区"),
		nn(601,"南宁","南宁分公司","南宁分公司","fcd1eb6b-8383-4caa-b576-d59e9737b5b6","西南片区"),
		gz(103,"广州","广州分公司","广州分公司","30f1c8d6-d087-4e72-915f-7b463cb4d740","华南片区"),
		dg(101,"东莞","东莞总部","东莞总部","30f1c8d6-d087-4e72-915f-7b463cb4d740","华南片区");
		private int code;
		private String desc;
		private String companyId;
		private String companyName ;
		private String districtId ;
		private String districtName ;
		private Dept(int code,String desc,String companyId,String companyName,String districtId,String districtName) {
			this.setCode(code) ;
			this.desc = desc ;
			this.setCompanyId(companyId) ;
			this.setCompanyName(companyName) ;
			setDistrictId(districtId);
			setDistrictName(districtName);
			
		}
		
		public static Dept code(String desc) {
			for (Dept d : Dept.values()) {
				if (d.desc.equals(desc)) {
					return d ;
				}
			}
			return null ;
		}

		public String getCompanyId() {
			return companyId;
		}

		public void setCompanyId(String companyId) {
			this.companyId = companyId;
		}

		public String getCompanyName() {
			return companyName;
		}

		public void setCompanyName(String companyName) {
			this.companyName = companyName;
		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}

		public String getDistrictId() {
			return districtId;
		}

		public void setDistrictId(String districtId) {
			this.districtId = districtId;
		}

		public String getDistrictName() {
			return districtName;
		}

		public void setDistrictName(String districtName) {
			this.districtName = districtName;
		}
	}
	
//	3=TDS
//	4=TDN
//	11=TDF
//	25=TDXQS
	
	enum Type{
		TDS(3,"TDS","金融仓储"),
		TDN(4,"TDN","三农金融"),
		TDC(9,"TDC","车易贷"),
		TDF(11,"TDF","房速贷"),
		TDXQS(25,"TDXQS","小微企业贷用信");
		private int code;
		private String desc;
		private String typeName ;

		private Type(int code,String desc,String typeName) {
			this.setCode(code) ;
			this.setDesc(desc) ;
			setTypeName(typeName);
		}
		
		public static Type desc(Integer code) {
			for (Type d : Type.values()) {
				if ((code).equals(d.getCode())) {
					return d ;
				}
			}
			return null ;
		}

		public String getTypeName() {
			return typeName;
		}

		public void setTypeName(String typeName) {
			this.typeName = typeName;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}

		public int getCode() {
			return code;
		}

		public void setCode(int code) {
			this.code = code;
		}
	}
	
	@Data
	class Task  {
		private Result<PlanReturnInfoDto> result ;
		private CreatRepayPlanReq req ;
	}
	
	@Test
	public void importBusiness() {
		File file = new File("需重新导入数据9.21(2).xlsx");
		//线下数据导入到贷后，需填写的字段信息-9-4(有数据).xlsx
		ImportParams sheetIndex0 = new ImportParams() ;
		sheetIndex0.setKeyIndex(0);
		List<BaseBusinessEntity> list = ExcelImportUtil.importExcel(file, BaseBusinessEntity.class,sheetIndex0) ;
		logger.info("excel.BaseBusinessEntity.size = "+list.size());
		ImportParams sheetIndex1 = new ImportParams() ;
		sheetIndex1.setStartSheetIndex(1);
		sheetIndex1.setKeyIndex(0);
		List<CustomerInfoEntity> list1 = ExcelImportUtil.importExcel(file, CustomerInfoEntity.class,sheetIndex1) ;
		logger.info("excel.CustomerInfoEntity.size = "+list.size());
		ImportParams sheetIndex2 = new ImportParams() ;
		sheetIndex2.setStartSheetIndex(2);
		sheetIndex2.setKeyIndex(0);
		List<RepayPlanEntity> list2 = ExcelImportUtil.importExcel(file, RepayPlanEntity.class,sheetIndex2) ;
		logger.info("excel.RepayPlanEntity.size = "+list.size());
		
		List<Task> tasks = new ArrayList<>() ;
		
		StringBuffer buffer = new StringBuffer() ;
		
		for (BaseBusinessEntity baseBusinessEntity : list) {
			if (baseBusinessEntity.getBusinessId()==null) {
				continue ;
			}
			String businessId = null ;
			try {
				businessId = initBusinessId(baseBusinessEntity);
			} catch (Exception e) {
				e.printStackTrace();
				System.err.println(baseBusinessEntity.getBusinessId());
				return ;
			}
			BusinessBasicInfoReq businessBasicInfoReq = new BusinessBasicInfoReq() ;
			businessBasicInfoReq.setBorrowLimit(baseBusinessEntity.getBorrowLimit());
			businessBasicInfoReq.setBorrowMoney(baseBusinessEntity.getBorrowMoney());
			BigDecimal borrowRate = new BigDecimal(baseBusinessEntity.getBorrowRate()).multiply(new BigDecimal("100"));
			businessBasicInfoReq.setBorrowRate(borrowRate);
			businessBasicInfoReq.setBorrowRateUnit(baseBusinessEntity.getBorrowRateUnit()==null?2:baseBusinessEntity.getBorrowRateUnit());
			businessBasicInfoReq.setBusinessCtype(Type.desc(baseBusinessEntity.getBusinessType()).getTypeName());
			businessBasicInfoReq.setBusinessId(businessId);
			businessBasicInfoReq.setBusinessType(baseBusinessEntity.getBusinessType());
			businessBasicInfoReq.setCompanyId(Dept.code(baseBusinessEntity.getCompanyName().trim()).getCompanyId());
			businessBasicInfoReq.setCompanyName(Dept.code(baseBusinessEntity.getCompanyName().trim()).getCompanyName());
			
			CustomerInfoEntity main = getMainCustomer(baseBusinessEntity.getBusinessId(), list1);
			List<CustomerInfoEntity> gongjie = getGongJieCustomer(baseBusinessEntity.getBusinessId(), list1);
			
			BusinessCustomerInfoReq mainCustomerInfoReq = new BusinessCustomerInfoReq() ;
			BeanUtils.copyProperties(main, mainCustomerInfoReq);
			mainCustomerInfoReq.setCustomerId(UUID.randomUUID().toString());
			mainCustomerInfoReq.setIdentifyCard(mainCustomerInfoReq.getIdentifyCard()==null?"":mainCustomerInfoReq.getIdentifyCard());
			mainCustomerInfoReq.setPhoneNumber(main.getPhoneNumber()==null?"":main.getPhoneNumber());
			mainCustomerInfoReq.setIsReceiptAccount(1);
			mainCustomerInfoReq.setIsMergedCertificate(0);
			mainCustomerInfoReq.setUnifiedCode("null");
			mainCustomerInfoReq.setBusinessLicence(main.getBusinessLicence());
			mainCustomerInfoReq.setIsCompanyBankAccount(0);
			mainCustomerInfoReq.setLegalPersonIdentityCard(main.getLegalPersonIdentityCard()==null?"":main.getLegalPersonIdentityCard());
			
			List<BusinessCustomerInfoReq> gongjieReqs = new ArrayList<>();
			for (CustomerInfoEntity customerInfoEntity : gongjie) {
				BusinessCustomerInfoReq gj = new BusinessCustomerInfoReq() ;
				BeanUtils.copyProperties(customerInfoEntity, gj);
				gj.setCustomerId(UUID.randomUUID().toString());
				gj.setIdentifyCard(customerInfoEntity.getIdentifyCard()==null?"":customerInfoEntity.getIdentifyCard());
				gj.setPhoneNumber(customerInfoEntity.getPhoneNumber()==null?"":customerInfoEntity.getPhoneNumber());
				gj.setIsReceiptAccount(0);
				gj.setIsMergedCertificate(0);
				gj.setIsCompanyBankAccount(0);
				gj.setUnifiedCode("null");
				gongjieReqs.add(gj);
			}
			
			gongjieReqs.add(mainCustomerInfoReq);
			businessBasicInfoReq.setCustomerName(mainCustomerInfoReq.getCustomerName());
			businessBasicInfoReq.setDistrictId(Dept.code(baseBusinessEntity.getCompanyName().trim()).getDistrictId());
			businessBasicInfoReq.setDistrictName(Dept.code(baseBusinessEntity.getCompanyName().trim()).getDistrictName());
			businessBasicInfoReq.setInputTime(baseBusinessEntity.getInputTime());
			businessBasicInfoReq.setIsRenewBusiness(0);
			businessBasicInfoReq.setOrgBusinessId(businessId);
			businessBasicInfoReq.setRepaymentTypeId(Integer.valueOf(baseBusinessEntity.getRepaymentTypeId()));
			businessBasicInfoReq.setSourceType(3);
			businessBasicInfoReq.setIssueSplitType(0);
			businessBasicInfoReq.setOperatorId("已离职");
			businessBasicInfoReq.setOperatorName("已离职");
			businessBasicInfoReq.setOriginalName("已离职");
			businessBasicInfoReq.setOriginalUserid("已离职");
			
			ProjInfoReq projInfoReq = creatProjInfoReq2(businessBasicInfoReq, mainCustomerInfoReq);
			
			List<ProjInfoReq> projInfoReqs = new ArrayList<>() ;
			projInfoReqs.add(projInfoReq);
			
			CreatRepayPlanReq req = new CreatRepayPlanReq() ;
			req.setBizCusInfoReqs(gongjieReqs);
			req.setBusinessBasicInfoReq(businessBasicInfoReq);
			req.setProjInfoReqs(projInfoReqs);
			req.setRondmode(0);
			req.setSmallNum(2);
			
			Result<PlanReturnInfoDto> creatRepayPlan = api.creatAndSaveRepayPlan(req);
			System.out.println(JSON.toJSONString(creatRepayPlan));
			buffer.append(baseBusinessEntity.getBusinessId()+"->"+req.getBusinessBasicInfoReq().getBusinessId()+"\r\n") ;
		}
		
		System.out.println(buffer.toString());
		
		
	} 
	
	private String initBusinessId(BaseBusinessEntity baseBusinessEntity) {
		StringBuffer businessId = new StringBuffer() ;
		businessId.append(Type.desc(baseBusinessEntity.getBusinessType()).getDesc()) ;
		if (baseBusinessEntity.getBusinessId().equals("20")) {
			System.out.println();
		}
		businessId.append(Dept.code(baseBusinessEntity.getCompanyName().trim()).getCode());
		businessId.append(DateUtil.formatDate("yyyyMMdd", baseBusinessEntity.getInputTime()));
		int selectCount = basicBusinessService.selectCount(new EntityWrapper<BasicBusiness>().like("business_id", businessId.toString()));
		businessId.append(String.format("%02d", selectCount+1));
		businessId.append("B");
		return businessId.toString() ;
	}
	
	private CustomerInfoEntity getMainCustomer(String businessId , List<CustomerInfoEntity> customerInfoEntities) {
		for (CustomerInfoEntity customerInfoEntity : customerInfoEntities) {
			if (customerInfoEntity.getBusinessId().equals(businessId)) {
				if (customerInfoEntity.getIsmainCustomer().equals(1)) {
					return customerInfoEntity ;
				}
			}
		}
		return null ;
	}
	
	private List<CustomerInfoEntity> getGongJieCustomer(String businessId , List<CustomerInfoEntity> customerInfoEntities) {
		List<CustomerInfoEntity> list = new ArrayList<>();
		for (CustomerInfoEntity customerInfoEntity : customerInfoEntities) {
			if (customerInfoEntity.getBusinessId().equals(businessId)) {
				if (customerInfoEntity.getIsmainCustomer().equals(0)) {
					list.add(customerInfoEntity);
				}
			}
		}
		return list ;
	}
	private  ProjInfoReq creatProjInfoReq2(BusinessBasicInfoReq  businessBasicInfoReq,BusinessCustomerInfoReq main){
		ProjInfoReq p = new ProjInfoReq();
        String projectId = UUID.randomUUID().toString() ;
        p.setProjFeeInfos(new ArrayList<>());
        p.setIsHaveCar(0);
        p.setIsHaveHouse(0);
        p.setRate(businessBasicInfoReq.getBorrowRate());
        p.setRateUnitType(businessBasicInfoReq.getBorrowRateUnit());
        p.setOffLineInOverDueRate(new BigDecimal(0.1));
        p.setOffLineInOverDueRateType(2);
        p.setOffLineOutOverDueRate(new BigDecimal(0.1));
        p.setOffLineOutOverDueRateType(2);
        p.setOnLineOverDueRate(new BigDecimal(0.06));
		p.setOnLineOverDueRateType(2);
		p.setRepayType(businessBasicInfoReq.getRepaymentTypeId());
		p.setProjectId(projectId);
		p.setCustomerId(main.getCustomerId());
		p.setTdUserId("");
		p.setStatusFlag("2");
		p.setBeginTime(businessBasicInfoReq.getInputTime());
		p.setFullBorrowMoney(businessBasicInfoReq.getBorrowMoney());
		p.setBorrowLimit(businessBasicInfoReq.getBorrowLimit());
		p.setExtendFlag(0);
		p.setOrgIssueId("");
		p.setMasterIssueId(projectId);
		p.setIssueOrder(1);
		p.setQueryFullsuccessDate(businessBasicInfoReq.getInputTime());
		p.setTelNo(main.getPhoneNumber()==null?"":main.getPhoneNumber());
		p.setIdentityCard(main.getIdentifyCard());
		p.setRealName(main.getCustomerName());
		p.setBankAccountNo("");
		p.setBankType(0);
		p.setBankProvice("");
		p.setBankCity("");
		p.setOpenBankName("");
		p.setPeriodMonth(businessBasicInfoReq.getBorrowLimit());
		p.setRepaymentType(businessBasicInfoReq.getRepaymentTypeId());
		p.setAmount(businessBasicInfoReq.getBorrowMoney());
		p.setBranchCompanyId("");
		p.setControlDesc("");
		p.setTdStatus(4);
		p.setProjectType(0);
		p.setEnterpriseUserId("");
		p.setAviCreditGrantingAmount(new BigDecimal(0));
		p.setOverRate(new BigDecimal(0.1));
		p.setUserTypeId(main.getCustomerType().equals("个人")?1:2);
		p.setTuandaiAmount(new BigDecimal(0));
		p.setGuaranteeRate(new BigDecimal(0));
		p.setSubCompanyRate(new BigDecimal(0));
		p.setSubCompanyCharge(new BigDecimal(0));
		p.setAgencyId("");
		p.setAgencyRate(new BigDecimal(0));
		p.setAgencyAmount(new BigDecimal(0));
		p.setDepositAmount(new BigDecimal(0));
		p.setFreedAmount(BigDecimal.ZERO);
		p.setFreedRate(new BigDecimal(0));
		p.setCooperativeTdComUserId("");
		p.setCooperativeTdComRate(BigDecimal.ZERO);
		p.setCooperativeTdComAmount(BigDecimal.ZERO);
		p.setBorrowerRate(BigDecimal.ZERO);
		p.setBorrowAmount(businessBasicInfoReq.getBorrowMoney());
		p.setProjectFrom(0);
		p.setMonthPrincipalAmount(BigDecimal.ZERO);
		p.setPlateType(0);
		p.setTuandaiRate(BigDecimal.ZERO);
		p.setGuaranteeAmount(BigDecimal.ZERO);
		return p ;
	}
	
}
