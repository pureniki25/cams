package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.fasterxml.jackson.databind.ser.impl.IndexedStringListSerializer;
import com.hongte.alms.base.entity.BankIncomeDat;
import com.hongte.alms.base.entity.CamsSubject;
import com.hongte.alms.base.entity.SubjectFirstDat;
import com.hongte.alms.base.entity.SubjectRestDat;
import com.hongte.alms.base.enums.CamsConstant;
import com.hongte.alms.base.enums.CamsConstant.SubjectTypeEnum;
import com.hongte.alms.base.mapper.SubjectRestDatMapper;
import com.hongte.alms.base.service.CamsSubjectService;
import com.hongte.alms.base.service.CustomerRestDatService;
import com.hongte.alms.base.service.SubjectFirstDatService;
import com.hongte.alms.base.service.SubjectRestDatService;
import com.hongte.alms.base.vo.cams.BalanceVo;
import com.hongte.alms.base.vo.cams.CustomerRestVo;
import com.hongte.alms.base.vo.cams.RestProductVo;
import com.hongte.alms.base.vo.cams.SubjectRestVo;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.ClassCopyUtil;
import com.hongte.alms.common.util.DateUtil;
import com.mysql.cj.core.util.StringUtils;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.catalina.connector.OutputBuffer;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Workbook;
import org.omg.PortableServer.RequestProcessingPolicyOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

/**
 * <p>
 * 科目余额表 服务实现类
 * </p>
 *
 * @author czs
 * @since 2020-01-19
 */
@Service("SubjectRestDatService")
public  class SubjectRestDatServiceImpl extends BaseServiceImpl<SubjectRestDatMapper, SubjectRestDat> implements SubjectRestDatService {

	@Autowired
	private SubjectRestDatMapper subjectRestDatMapper;
	
	@Autowired
	@Qualifier("SubjectFirstDatService")
	private SubjectFirstDatService subjectFirstDatService;
	
	@Autowired
	@Qualifier("CamsSubjectService")
	private CamsSubjectService camsSubjectService;
	
	@Autowired
	@Qualifier("CustomerRestDatService")
	private CustomerRestDatService customerRestDatService;
	
	//期初数
	public static final String FIRST="first";
	
	//期末数
	public static final String REST="rest";
	
	@Value("${cams.excel.file.save.path}")
	private String path;
	
	@Override
	public void syncPingZhengData() {
		subjectRestDatMapper.syncPingZhengData();
		
	}

	@Override
	public void deleteSubjectRest() {
		subjectRestDatMapper.deleteSubjectRest();
		
	}

	@Override
	public Page<SubjectRestVo> selectSubjectRest(SubjectRestVo vo) throws InstantiationException, IllegalAccessException {
		Page<SubjectRestVo> pages = new Page<>();
		pages.setCurrent(vo.getPage());
		pages.setSize(vo.getLimit());
		List<SubjectRestVo> list=subjectRestDatMapper.selectSubjectRestList(pages, vo);
		String beginDate=String.valueOf(DateUtil.getYear(vo.getBeginDate()));
		List<SubjectFirstDat> firstDats= subjectFirstDatService.selectList(new EntityWrapper<SubjectFirstDat>().eq("company_name", vo.getCompanyName()).eq("period", beginDate));
		
		//初始化所有有期初余额的科目为flase
		HashMap<SubjectFirstDat,Boolean> resultMap=new HashMap<SubjectFirstDat,Boolean>();
		for(SubjectFirstDat firstDat:firstDats) {
			resultMap.put(firstDat, false);
		}
		//如果查询的科目有期初余额，该科目标记为true，剩下的是没有查询出来的记录，但是有期初余额的记录
		for(SubjectRestVo restVo:list) {
			for(SubjectFirstDat firstDat:firstDats) {
				if(restVo.getSubject().equals(firstDat.getSubject())) {
					restVo.setSubjectName(firstDat.getSubjectName());
					resultMap.put(firstDat, true);
				}
			}
			
		}
		//把剩下的是没有查询记录，但是有期初余额的记录生成SubjectRestVo
		for(Map.Entry<SubjectFirstDat, Boolean> entry : resultMap.entrySet()){
			SubjectFirstDat firstDatKey = entry.getKey();
		    boolean mapValue = entry.getValue();
		    SubjectRestVo temp=null;
		    if(null!=firstDatKey.getFirstAmount()&&Double.valueOf(firstDatKey.getFirstAmount())==0) {
		    	continue;
		    }
	
		    if(mapValue==false) {
		    	 temp = new SubjectRestVo();
		    	 temp.setCompanyName(vo.getCompanyName());
		    	 temp.setSubject(firstDatKey.getSubject());
		    	 temp.setFirstAmount(firstDatKey.getFirstAmount());
		    	 temp.setSubjectName(firstDatKey.getSubjectName());
		    	 temp.setBorrowAmount("0");
		    	 temp.setAlmsAmount("0");
		    	 CamsSubject camsSubjectTemp= camsSubjectService.selectOne(new EntityWrapper<CamsSubject>().eq("id", temp.getSubject()));
		    	 if(camsSubjectTemp==null) {
		    		 System.out.println("stop");
		    	 }
		    	 String restAmountStr=getRestAmount(temp.getFirstAmount(), temp.getBorrowAmount(), temp.getAlmsAmount(), camsSubjectTemp.getDirection());
		    	 temp.setRestAmount(restAmountStr);
		    	 list.add(temp);
		    }
		}
	     List<SubjectRestVo> result=new ArrayList();
		//按科目分组
		Map<String, List<SubjectRestVo>> secondAndThirdSubjects=list.stream().collect(Collectors.groupingBy(SubjectRestVo::getGroupBySubject));
		for(Map.Entry<String, List<SubjectRestVo>> entry : secondAndThirdSubjects.entrySet()) {
			List<SubjectRestVo> tempList=secondAndThirdSubjects.get(entry.getKey());
			if(entry.getKey().equals("1002-01")) {
				System.out.println("stop");
			}
	        //如果只有1个一级科目,不用处理
			if(tempList.size()==1&&tempList.get(0).getSubject().equals(entry.getKey())) {
				SubjectRestVo restVo=tempList.get(0);
				   CamsSubject camsSubjectTemp= camsSubjectService.selectOne(new EntityWrapper<CamsSubject>().eq("id", restVo.getSubject()));
				   if(camsSubjectTemp==null) {
					   System.out.println("stop");
				   }
				   System.out.println(camsSubjectTemp.getId()+"====================================");
				   String restAmountStr=getRestAmount(restVo.getFirstAmount(), restVo.getBorrowAmount(), restVo.getAlmsAmount(), camsSubjectTemp.getDirection());
				   restVo.setRestAmount(restAmountStr);
				   if(entry.getKey().equals("1002-01")) {  //1002-01科目取科目期初余额表的科目名称
					   SubjectFirstDat dat= subjectFirstDatService.selectOne(new EntityWrapper<SubjectFirstDat>().eq("company_name", vo.getCompanyName()).eq("subject", entry.getKey()));
				       if(null!=dat) {
				    	   restVo.setSubjectName(dat.getSubjectName());
				       }
				   }
				
			//把原本的一级科目剔除，统计所有二级科目重新汇总成新的一级科目
			}else {
				Iterator<SubjectRestVo> it=tempList.iterator();
				   while(it.hasNext()){
					     //二级科目的金额
					     Double secondFirstAmount=0.0;
						 Double secondBorrowAmount=0.0;
						 Double secondeAlmsAmount=0.0;
					   SubjectRestVo tempVo=it.next();
					   if(tempVo.getSubject().equals(entry.getKey())) {
						   continue;
					   }else {
						   for(SubjectRestVo restVo:tempList) {
							   //三级科目的金额汇总成二级科目
							   if(restVo.getSubject().length()==10) {
								   //如果是这4个科目，金额要变成负数
								   if(restVo.getSubject().equals("2171-01-01")||restVo.getSubject().equals("2171-01-02")
										   ||restVo.getSubject().equals("2171-01-03")||restVo.getSubject().equals("2171-01-04")) {
									   secondFirstAmount=0-(Double.valueOf(restVo.getFirstAmount()==null?"0.0":restVo.getFirstAmount()))+secondFirstAmount;
									   secondBorrowAmount=0-(Double.valueOf(restVo.getBorrowAmount()==null?"0.0":restVo.getBorrowAmount()))+secondBorrowAmount;
									   secondeAlmsAmount=0-(Double.valueOf(restVo.getAlmsAmount()==null?"0.0":restVo.getAlmsAmount()))+secondeAlmsAmount;
								   }else {
									   secondFirstAmount=Double.valueOf(restVo.getFirstAmount()==null?"0.0":restVo.getFirstAmount())+secondFirstAmount;
									   secondBorrowAmount=Double.valueOf(restVo.getBorrowAmount()==null?"0.0":restVo.getBorrowAmount())+secondBorrowAmount;
									   secondeAlmsAmount=Double.valueOf(restVo.getAlmsAmount()==null?"0.0":restVo.getAlmsAmount())+secondeAlmsAmount;
								   }
								
							   }
						   }
					   }
					   for(SubjectRestVo restVo:tempList) {
						   //设置二级科目的金额成三级科目的总和
						   if(restVo.getSubject().length()==7) {
							   restVo.setFirstAmount(secondFirstAmount.toString());
							   restVo.setBorrowAmount(secondBorrowAmount.toString());
							   restVo.setAlmsAmount(secondeAlmsAmount.toString());
							   CamsSubject camsSubjectTemp= camsSubjectService.selectOne(new EntityWrapper<CamsSubject>().eq("id", restVo.getSubject()));
							   String restAmountStr=getRestAmount(restVo.getFirstAmount(), restVo.getBorrowAmount(), restVo.getAlmsAmount(), camsSubjectTemp.getDirection());
							   restVo.setRestAmount(restAmountStr);
						   }
					   }
			        }
				   
		
			
				
			
//				   temp.setSubject(entry.getKey());
//				   CamsSubject camsSubject= camsSubjectService.selectOne(new EntityWrapper<CamsSubject>().eq("id", temp.getSubject()));
//				   temp.setSubjectName(camsSubject.getSubjectName());
//				   temp.setCompanyName(vo.getCompanyName()); 
//				   temp.setFirstAmount(firstAmount.toString());
//		    	   temp.setBorrowAmount(borrowAmount.toString());
//		    	   temp.setAlmsAmount(almsAmount.toString());
//		    	   String restAmountStr=getRestAmount(temp.getFirstAmount(), temp.getBorrowAmount(), temp.getAlmsAmount(), camsSubject.getDirection());
//			       temp.setRestAmount(restAmountStr);
//			       tempList.add(0, temp);
			}
			result.addAll(tempList);
		}
		  List<SubjectRestVo> result2=new ArrayList();
		//按二级科目分组
		Map<String, List<SubjectRestVo>> firstSubjects=result.stream().collect(Collectors.groupingBy(SubjectRestVo::getGroupByFirstSubject));
		for(Map.Entry<String, List<SubjectRestVo>> entry : firstSubjects.entrySet()) {
			if(entry.getKey().equals("1002")) {
				System.out.println();
			}
			List<SubjectRestVo> tempList=firstSubjects.get(entry.getKey());
			if(tempList.size()==1&&tempList.get(0).getSubject().length()==4) {
				result2.addAll(tempList);
				continue;
			}
			   //一级科目金额
			 Double firstAmount=0.0;
			 Double borrowAmount=0.0;
			 Double almsAmount=0.0; 
			   for(SubjectRestVo restVo:tempList) {
				   //二级科目的金额汇总成一级科目
				   if(restVo.getSubject().length()==7) {
					   firstAmount=Double.valueOf(restVo.getFirstAmount()==null?"0.0":restVo.getFirstAmount())+firstAmount;
					   borrowAmount=Double.valueOf(restVo.getBorrowAmount()==null?"0.0":restVo.getBorrowAmount())+borrowAmount;
					   almsAmount=Double.valueOf(restVo.getAlmsAmount()==null?"0.0":restVo.getAlmsAmount())+almsAmount;
				   }
			   }
			   boolean hasFirstSubject=false; //是否有1级科目的记录
			   
			   for(SubjectRestVo restVo:tempList) {
				   //二级科目的金额汇总成一级科目
				   if(restVo.getSubject().length()==4) {
					   restVo.setFirstAmount(firstAmount.toString());
					   restVo.setBorrowAmount(borrowAmount.toString());
					   restVo.setAlmsAmount(almsAmount.toString());
					   CamsSubject camsSubjectTemp= camsSubjectService.selectOne(new EntityWrapper<CamsSubject>().eq("id", restVo.getSubject()));
					   String restAmountStr=getRestAmount(restVo.getFirstAmount(), restVo.getBorrowAmount(), restVo.getAlmsAmount(), camsSubjectTemp.getDirection());
					   restVo.setRestAmount(restAmountStr);
					   hasFirstSubject=true;
				   }
			   }
			   if(!hasFirstSubject) { //如果没有1级科目，新建一个
				   SubjectRestVo temp=new SubjectRestVo();
				   temp.setSubject(entry.getKey());
				   CamsSubject camsSubject= camsSubjectService.selectOne(new EntityWrapper<CamsSubject>().eq("id", temp.getSubject()));
				   temp.setSubjectName(camsSubject.getSubjectName());
				   temp.setCompanyName(vo.getCompanyName()); 
				   temp.setFirstAmount(firstAmount.toString());
		    	   temp.setBorrowAmount(borrowAmount.toString());
		    	   temp.setAlmsAmount(almsAmount.toString());
		    	   String restAmountStr=getRestAmount(temp.getFirstAmount(), temp.getBorrowAmount(), temp.getAlmsAmount(), camsSubject.getDirection());
			       temp.setRestAmount(restAmountStr);
			       tempList.add(0, temp);
			   }
			   result2.addAll(tempList);
		}
		  List<SubjectRestVo> finalResult=new ArrayList();
		  
		  //按科目排序
		  result2 =result2.stream().sorted(Comparator.comparing(SubjectRestVo::getSubject)).collect(Collectors.toList());
		//按科目类型分组
		Map<String, List<SubjectRestVo>> subjectType=result2.stream().collect(Collectors.groupingBy(SubjectRestVo::getGroupByType));
		for(Map.Entry<String, List<SubjectRestVo>> entry : subjectType.entrySet()) {
			List<SubjectRestVo> tempList=subjectType.get(entry.getKey());
	      
				Iterator<SubjectRestVo> it=tempList.iterator();
				 SubjectRestVo temp=new SubjectRestVo();
				 Double firstAmount=0.0;
				 Double borrowAmount=0.0;
				 Double almsAmount=0.0;
				 SubjectTypeEnum subjectEnum=CamsConstant.SubjectTypeEnum.getEnum(entry.getKey());
				   while(it.hasNext()){
					   SubjectRestVo tempVo=it.next();
						   if(tempVo.getSubject().length()==4) {
							   if(subjectEnum==SubjectTypeEnum.ZI_CHAN) {
								   CamsSubject camsSubjectTemp= camsSubjectService.selectOne(new EntityWrapper<CamsSubject>().eq("id", tempVo.getSubject()));
								   if(camsSubjectTemp.getDirection().equals(CamsConstant.DirectionEnum.DAI.getValue().toString())) { //如果是资产类型，方向是贷，金额是负数
									   firstAmount=0-Double.valueOf(tempVo.getFirstAmount()==null?"0.0":tempVo.getFirstAmount())+firstAmount;
									   borrowAmount=0-Double.valueOf(tempVo.getBorrowAmount()==null?"0.0":tempVo.getBorrowAmount())+borrowAmount;
									   almsAmount=0-Double.valueOf(tempVo.getAlmsAmount()==null?"0.0":tempVo.getAlmsAmount())+almsAmount; 
								   }else {
									   firstAmount=Double.valueOf(tempVo.getFirstAmount()==null?"0.0":tempVo.getFirstAmount())+firstAmount;
									   borrowAmount=Double.valueOf(tempVo.getBorrowAmount()==null?"0.0":tempVo.getBorrowAmount())+borrowAmount;
									   almsAmount=Double.valueOf(tempVo.getAlmsAmount()==null?"0.0":tempVo.getAlmsAmount())+almsAmount; 
								   }
								 
							   }else {
									   CamsSubject camsSubjectTemp= camsSubjectService.selectOne(new EntityWrapper<CamsSubject>().eq("id", tempVo.getSubject()));
									   if(camsSubjectTemp.getDirection().equals(CamsConstant.DirectionEnum.JIE.getValue().toString())) { //如果是非资产类型，方向是借，金额是负数
										   firstAmount=0-Double.valueOf(tempVo.getFirstAmount()==null?"0.0":tempVo.getFirstAmount())+firstAmount;
										   borrowAmount=0-Double.valueOf(tempVo.getBorrowAmount()==null?"0.0":tempVo.getBorrowAmount())+borrowAmount;
										   almsAmount=0-Double.valueOf(tempVo.getAlmsAmount()==null?"0.0":tempVo.getAlmsAmount())+almsAmount; 
									   }else {
										   firstAmount=Double.valueOf(tempVo.getFirstAmount()==null?"0.0":tempVo.getFirstAmount())+firstAmount;
										   borrowAmount=Double.valueOf(tempVo.getBorrowAmount()==null?"0.0":tempVo.getBorrowAmount())+borrowAmount;
										   almsAmount=Double.valueOf(tempVo.getAlmsAmount()==null?"0.0":tempVo.getAlmsAmount())+almsAmount; 
									   }
						      }
							
				          }
					}
				   temp.setSubject(CamsConstant.SubjectTypeEnum.getDesc(entry.getKey())+"小计");
				   String direction=CamsConstant.SubjectTypeEnum.getDirect(entry.getKey());
				   temp.setCompanyName(vo.getCompanyName());
				   temp.setFirstAmount(firstAmount.toString());
		    	   temp.setBorrowAmount(borrowAmount.toString());
		    	   temp.setAlmsAmount(almsAmount.toString());
		    	   String restAmountStr=getRestAmount(temp.getFirstAmount(), temp.getBorrowAmount(), temp.getAlmsAmount(), direction);
			       temp.setRestAmount(restAmountStr);
			       tempList.add(temp);
			       subjectType.put(entry.getKey(), tempList);
			       finalResult.addAll(tempList);
	
		}
		for(SubjectRestVo subjectRestVo:finalResult) {
			BigDecimal firstAmount2 = new BigDecimal(Double.valueOf(subjectRestVo.getFirstAmount()==null?"0":subjectRestVo.getFirstAmount())).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal restAmount2 = new BigDecimal(Double.valueOf(subjectRestVo.getRestAmount())).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal borrowAmount2 = new BigDecimal(Double.valueOf(subjectRestVo.getBorrowAmount())).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal almsAmount2 = new BigDecimal(Double.valueOf(subjectRestVo.getAlmsAmount())).setScale(2, BigDecimal.ROUND_HALF_UP);
			subjectRestVo.setFirstAmount(firstAmount2.toString());
			subjectRestVo.setBorrowAmount(borrowAmount2.toString());
			subjectRestVo.setRestAmount(restAmount2.toString());
			subjectRestVo.setAlmsAmount(almsAmount2.toString());
		}
		pages.setRecords(finalResult);
		return pages;
	}
	@SuppressWarnings("unused")
	private String getRestAmount(String firstAmount,String borrowAmount,String almsAmount,String direction) {
		if(null!=direction&&direction.equals(CamsConstant.DirectionEnum.DAI.getValue().toString())) {
		    Double restAmountStr=Double.valueOf(firstAmount==null?"0":firstAmount)-Double.valueOf(borrowAmount)+Double.valueOf(almsAmount);
		    return restAmountStr.toString();
		}else {
			 Double restAmountStr=Double.valueOf(firstAmount==null?"0":firstAmount)+Double.valueOf(borrowAmount)-Double.valueOf(almsAmount);
			    return restAmountStr.toString();
		}
		
	}

	@Override
	public Page<BalanceVo> selectBalance(SubjectRestVo vo) throws Exception {
		//查询单位余额表数据
		CustomerRestVo customerRestVo=new CustomerRestVo();
		customerRestVo.setCompanyName(vo.getCompanyName());
		customerRestVo.setBeginDate(vo.getBeginDate());
		customerRestVo.setEndDate(vo.getEndDate());
		customerRestVo.setLimit(2000);
		customerRestVo.setPage(1);
		Page<CustomerRestVo> custtomerRestPage=customerRestDatService.selectCustomerRestList(customerRestVo);
		List<CustomerRestVo> customerList=custtomerRestPage.getRecords();
		
		Double first_yszk=0.0; //1131 单位期初预收账款
		Double rest_yszk=0.0; //1131 单位期末预收账款
	
		Double first_yfzk=0.0;//2121 单位期初预付账款
		Double rest_yfzk=0.0;//2121 单位期末预付账款
	
	
		Map<String, List<CustomerRestVo>> customerSubject=customerList.stream().collect(Collectors.groupingBy(CustomerRestVo::getSubject));
		for(Map.Entry<String, List<CustomerRestVo>> entry : customerSubject.entrySet()) {
			List<CustomerRestVo> tempList=customerSubject.get(entry.getKey());
			for(CustomerRestVo customerTemp:tempList) {
				if(entry.getKey().equals("1131")) { //应收账款负数代表预收账款
					if(customerTemp.getFirstAmount().contains("-")) { 
						first_yszk=first_yszk+Math.abs(Double.valueOf(customerTemp.getFirstAmount()));
					}
	                if(customerTemp.getRestAmount().contains("-")) { 
	                	rest_yszk=rest_yszk+Math.abs(Double.valueOf(customerTemp.getRestAmount()));
						
					}
				}else if(entry.getKey().equals("2121")) { //应付账款负数代表预付账款
					if(customerTemp.getFirstAmount().contains("-")) { 
						first_yfzk=first_yfzk+Math.abs(Double.valueOf(customerTemp.getFirstAmount()));
					}
	                if(customerTemp.getRestAmount().contains("-")) { 
	                	rest_yfzk=rest_yfzk+Math.abs(Double.valueOf(customerTemp.getRestAmount()));
						
					}
				}
			
			}
		}
		//查询科目余额表数据
		Page<SubjectRestVo> page=selectSubjectRest(vo);
	    List<SubjectRestVo>	list=page.getRecords();
	    Map<String, List<SubjectRestVo>> subjectType=list.stream().collect(Collectors.groupingBy(SubjectRestVo::getSubject));
	    
		Page<BalanceVo> pages = new Page<>();
		pages.setCurrent(vo.getPage());
		pages.setSize(vo.getLimit());
	
	    List<BalanceVo> balanceVos=new ArrayList();
	    /**货币资金**/
	    BalanceVo vo1=new BalanceVo();
	    vo1.setBalanceType("资产");
	    vo1.setRowNum(1);
	    vo1.setBalanceSubeType("流动资产");
	    vo1.setBalanceName("货币资金");
	    Double firstAmount_1001=getBalaceAmount("1001", FIRST, subjectType);
	    Double firstAmount_1002=getBalaceAmount("1002", FIRST, subjectType);
	    Double firstAmount_1009=getBalaceAmount("1009", FIRST, subjectType);
	    Double restAmount_1001=getBalaceAmount("1001", REST, subjectType);
	    Double restAmount_1002=getBalaceAmount("1002", REST, subjectType);
	    Double restAmount_1009=getBalaceAmount("1009", REST, subjectType);
	    String firstAmountVo1=String.valueOf((firstAmount_1001+firstAmount_1002+firstAmount_1009));
	    String restAmountVo1=String.valueOf((restAmount_1001+restAmount_1002+restAmount_1009));
	    vo1.setFirstAmount(firstAmountVo1);
	    vo1.setRestAmount(restAmountVo1);
        vo1.setCompanyName(vo.getCompanyName());
        balanceVos.add(vo1);
        
	    /**短期资金**/
	    BalanceVo vo2=new BalanceVo();
	    vo2.setBalanceType("资产");
	    vo2.setRowNum(2);
	    vo2.setBalanceSubeType("流动资产");
	    vo2.setBalanceName("短期资金");
	    Double firstAmount_1101=getBalaceAmount("1101", FIRST, subjectType);
	    Double firstAmount_1102=getBalaceAmount("1102", FIRST, subjectType);
	    Double restAmount_1101=getBalaceAmount("1101", REST, subjectType);
	    Double restAmount_1102=getBalaceAmount("1102", REST, subjectType);
	    String firstAmountVo2=String.valueOf((firstAmount_1101-firstAmount_1102));
	    String restAmountVo2=String.valueOf((restAmount_1101-restAmount_1102));
	    vo2.setFirstAmount(firstAmountVo2);
	    vo2.setRestAmount(restAmountVo2);
	    vo2.setCompanyName(vo.getCompanyName());
        balanceVos.add(vo2);
        
	    /**应收票据**/
	    BalanceVo vo3=new BalanceVo();
	    vo3.setBalanceType("资产");
	    vo3.setRowNum(3);
	    vo3.setBalanceSubeType("流动资产");
	    vo3.setBalanceName("应收票据");
	    Double firstAmount_1111=getBalaceAmount("1111", FIRST, subjectType);
	    Double restAmount_1111=getBalaceAmount("1111", REST, subjectType);
	    String firstAmountVo3=String.valueOf((firstAmount_1111));
	    String restAmountVo3=String.valueOf((restAmount_1111));
	    vo3.setFirstAmount(firstAmountVo3);
	    vo3.setRestAmount(restAmountVo3);
	    vo3.setCompanyName(vo.getCompanyName());
        balanceVos.add(vo3);
        
        /**应收股利**/
	    BalanceVo vo4=new BalanceVo();
	    vo4.setBalanceType("资产");
	    vo4.setRowNum(4);
	    vo4.setBalanceSubeType("流动资产");
	    vo4.setBalanceName("应收股利");
	    Double firstAmount_1121=getBalaceAmount("1121", FIRST, subjectType);
	    Double restAmount_1121=getBalaceAmount("1121", REST, subjectType);
	    String firstAmountVo4=String.valueOf((firstAmount_1121));
	    String restAmountVo4=String.valueOf((restAmount_1121));
	    vo4.setFirstAmount(firstAmountVo4);
	    vo4.setRestAmount(restAmountVo4);
	    vo4.setCompanyName(vo.getCompanyName());
        balanceVos.add(vo4);
        
        /** 应收利息**/
	    BalanceVo vo5=new BalanceVo();
	    vo5.setBalanceType("资产");
	    vo5.setRowNum(5);
	    vo5.setBalanceSubeType("流动资产");
	    vo5.setBalanceName("应收利息");
	    Double firstAmount_1122=getBalaceAmount("1122", FIRST, subjectType);
	    Double restAmount_1122=getBalaceAmount("1122", REST, subjectType);
	    String firstAmountVo5=String.valueOf((firstAmount_1122));
	    String restAmountVo5=String.valueOf((restAmount_1122));
	    vo5.setFirstAmount(firstAmountVo5);
	    vo5.setRestAmount(restAmountVo5);
	    vo5.setCompanyName(vo.getCompanyName());
        balanceVos.add(vo5);
        

        /**应收账款**/
	    BalanceVo vo6=new BalanceVo();
	    vo6.setBalanceType("资产");
	    vo6.setRowNum(6);
	    vo6.setBalanceSubeType("流动资产");
	    vo6.setBalanceName("应收账款");
	    Double firstAmount_1131=getBalaceAmount("1131", FIRST, subjectType)+first_yszk;
	    Double firstAmount_1141=getBalaceAmount("1141", FIRST, subjectType);
	    Double firstAmount_2131=getBalaceAmount("2131", FIRST, subjectType);
	    Double restAmount_1131=getBalaceAmount("1131", REST, subjectType)+rest_yszk;
	    Double restAmount_1141=getBalaceAmount("1141", REST, subjectType);
	    Double restAmount_2131=getBalaceAmount("2131", REST, subjectType);
	    String firstAmountVo6=String.valueOf((firstAmount_1131-firstAmount_1141+firstAmount_2131));
	    String restAmountVo6=String.valueOf((restAmount_1131-restAmount_1141+restAmount_2131));
	    vo6.setFirstAmount(firstAmountVo6);
	    vo6.setRestAmount(restAmountVo6);
	    vo6.setCompanyName(vo.getCompanyName());
        balanceVos.add(vo6);
        
        /**其他应收款**/
	    BalanceVo vo7=new BalanceVo();
	    vo7.setBalanceType("资产");
	    vo7.setRowNum(7);
	    vo7.setBalanceSubeType("流动资产");
	    vo7.setBalanceName("其他应收款");
	    Double firstAmount_1133=getBalaceAmount("1133", FIRST, subjectType);
	    Double restAmount_1133=getBalaceAmount("1133", REST, subjectType);
	    String firstAmountVo7=String.valueOf((firstAmount_1133-firstAmount_1141));
	    String restAmountVo7=String.valueOf((restAmount_1133-restAmount_1141));
	    vo7.setFirstAmount(firstAmountVo7);
	    vo7.setRestAmount(restAmountVo7);
	    vo7.setCompanyName(vo.getCompanyName());
        balanceVos.add(vo7);
        
        /** 预付账款**/
	    BalanceVo vo8=new BalanceVo();
	    vo8.setBalanceType("资产");
	    vo8.setRowNum(8);
	    vo8.setBalanceSubeType("流动资产");
	    vo8.setBalanceName(" 预付账款");
	    Double firstAmount_1151=getBalaceAmount("1151", FIRST, subjectType);
	    Double firstAmount_2121=first_yfzk;
	    Double restAmount_1151=getBalaceAmount("1151", REST, subjectType);
	    Double restAmount_2121=rest_yfzk;
	    String firstAmountVo8=String.valueOf((firstAmount_1151+firstAmount_2121));
	    String restAmountVo8=String.valueOf((restAmount_1151+restAmount_2121));
	    vo8.setFirstAmount(firstAmountVo8);
	    vo8.setRestAmount(restAmountVo8);
	    vo8.setCompanyName(vo.getCompanyName());
        balanceVos.add(vo8);
        
        /**应收补贴款**/
	    BalanceVo vo9=new BalanceVo();
	    vo9.setBalanceType("资产");
	    vo9.setRowNum(9);
	    vo9.setBalanceSubeType("流动资产");
	    vo9.setBalanceName("应收补贴款");
	    Double firstAmount_1161=getBalaceAmount("1161", FIRST, subjectType);
	    Double restAmount_1161=getBalaceAmount("1161", REST, subjectType);
	    String firstAmountVo9=String.valueOf((firstAmount_1161));
	    String restAmountVo9=String.valueOf((restAmount_1161));
	    vo9.setFirstAmount(firstAmountVo9);
	    vo9.setRestAmount(restAmountVo9);
	    vo9.setCompanyName(vo.getCompanyName());
        balanceVos.add(vo9);
        
        /**存货**/
	    BalanceVo vo10=new BalanceVo();
	    vo10.setBalanceType("资产");
	    vo10.setRowNum(10);
	    vo10.setBalanceSubeType("流动资产");
	    vo10.setBalanceName("存货");
	    Double firstAmount_1201=getBalaceAmount("1201", FIRST, subjectType);
	    Double firstAmount_1211=getBalaceAmount("1211", FIRST, subjectType);
	    Double firstAmount_1221=getBalaceAmount("1221", FIRST, subjectType);
	    Double firstAmount_1231=getBalaceAmount("1231", FIRST, subjectType);
	    Double firstAmount_1232=getBalaceAmount("1232", FIRST, subjectType);
	    Double firstAmount_1241=getBalaceAmount("1241", FIRST, subjectType);
	    Double firstAmount_1243=getBalaceAmount("1243", FIRST, subjectType);
	    Double firstAmount_1244=getBalaceAmount("1244", FIRST, subjectType);
	    Double firstAmount_1251=getBalaceAmount("1251", FIRST, subjectType);
	    Double firstAmount_1261=getBalaceAmount("1261", FIRST, subjectType);
	    Double firstAmount_1271=getBalaceAmount("1271", FIRST, subjectType);
	    Double firstAmount_1291=getBalaceAmount("1291", FIRST, subjectType);
	    Double firstAmount_1281=getBalaceAmount("1281", FIRST, subjectType);
	    Double firstAmount_2141=getBalaceAmount("2141", FIRST, subjectType);
	    
	    Double restAmount_1201=getBalaceAmount("1201", REST, subjectType);
	    Double restAmount_1211=getBalaceAmount("1211", REST, subjectType);
	    Double restAmount_1221=getBalaceAmount("1221", REST, subjectType);
	    Double restAmount_1231=getBalaceAmount("1231", REST, subjectType);
	    Double restAmount_1232=getBalaceAmount("1232", REST, subjectType);
	    Double restAmount_1241=getBalaceAmount("1241", REST, subjectType);
	    Double restAmount_1243=getBalaceAmount("1243", REST, subjectType);
	    Double restAmount_1244=getBalaceAmount("1244", REST, subjectType);
	    Double restAmount_1251=getBalaceAmount("1251", REST, subjectType);
	    Double restAmount_1261=getBalaceAmount("1261", REST, subjectType);
	    Double restAmount_1271=getBalaceAmount("1271", REST, subjectType);
	    Double restAmount_1291=getBalaceAmount("1291", REST, subjectType);
	    Double restAmount_1281=getBalaceAmount("1281", REST, subjectType);
	    Double restAmount_2141=getBalaceAmount("2141", REST, subjectType);
	    
	    String firstAmountVo10=String.valueOf((firstAmount_1201+firstAmount_1211+firstAmount_1221+firstAmount_1231
	    		+firstAmount_1232+firstAmount_1241+firstAmount_1243+firstAmount_1244+firstAmount_1251+firstAmount_1261+firstAmount_1271+firstAmount_1291+firstAmount_1281+firstAmount_2141));
	    String restAmountVo10=String.valueOf((restAmount_1201+restAmount_1211+restAmount_1221+restAmount_1231+restAmount_1232
	    		+restAmount_1241+restAmount_1243+restAmount_1244+restAmount_1251+restAmount_1261+restAmount_1271+restAmount_1291+restAmount_1281+restAmount_2141));
	    vo10.setFirstAmount(firstAmountVo10);
	    vo10.setRestAmount(restAmountVo10);
	    vo10.setCompanyName(vo.getCompanyName());
        balanceVos.add(vo10);
        
        /**待摊费用**/
	    BalanceVo vo11=new BalanceVo();
	    vo11.setBalanceType("资产");
	    vo11.setRowNum(11);
	    vo11.setBalanceSubeType("流动资产");
	    vo11.setBalanceName("待摊费用");
	    Double firstAmount_1301=getBalaceAmount("1301", FIRST, subjectType);
	    Double firstAmount_2191=getBalaceAmount("2191", FIRST, subjectType);
	    Double restAmount_1301=getBalaceAmount("1301", REST, subjectType);
	    Double restAmount_2191=getBalaceAmount("2191", REST, subjectType);
	    String firstAmountVo11=String.valueOf((firstAmount_1301+firstAmount_2191));
	    String restAmountVo11=String.valueOf((restAmount_1301+restAmount_2191));
	    vo11.setFirstAmount(firstAmountVo11);
	    vo11.setRestAmount(restAmountVo11);
	    vo11.setCompanyName(vo.getCompanyName());
        balanceVos.add(vo11);
        
        /**一年内到期的长期债券投资**/
	    BalanceVo vo21=new BalanceVo();
	    vo21.setBalanceType("资产");
	    vo21.setRowNum(21);
	    vo21.setBalanceSubeType("流动资产");
	    vo21.setBalanceName("一年内到期的长期债券投资");
	    String firstAmountVo21=String.valueOf("0.0");
	    String restAmountVo21=String.valueOf("0.0");
	    vo21.setFirstAmount(firstAmountVo21);
	    vo21.setRestAmount(restAmountVo21);
	    vo21.setCompanyName(vo.getCompanyName());
        balanceVos.add(vo21);
        
        /**其它流动资产**/
	    BalanceVo vo24=new BalanceVo();
	    vo24.setBalanceType("资产");
	    vo24.setRowNum(24);
	    vo24.setBalanceSubeType("流动资产");
	    vo24.setBalanceName("其它流动资产");
	    String firstAmountVo24=String.valueOf("0.0");
	    String restAmountVo24=String.valueOf("0.0");
	    vo24.setFirstAmount(firstAmountVo24);
	    vo24.setRestAmount(restAmountVo24);
	    vo24.setCompanyName(vo.getCompanyName());
        balanceVos.add(vo24);
        
        /**流动资产合计**/
	    BalanceVo vo31=new BalanceVo();
	    vo31.setBalanceType("资产");
	    vo31.setRowNum(31);
	    vo31.setBalanceSubeType("流动资产");
	    vo31.setBalanceName("流动资产合计");
	    String firstAmountVo31=String.valueOf(Double.valueOf(firstAmountVo1)+Double.valueOf(firstAmountVo2)+Double.valueOf(firstAmountVo3)+Double.valueOf(firstAmountVo4)
							    +Double.valueOf(firstAmountVo5)+Double.valueOf(firstAmountVo6)+Double.valueOf(firstAmountVo7)+Double.valueOf(firstAmountVo8)
							    +Double.valueOf(firstAmountVo9)+Double.valueOf(firstAmountVo10)+Double.valueOf(firstAmountVo11));
	    String restAmountVo31=String.valueOf(Double.valueOf(restAmountVo1)+Double.valueOf(restAmountVo2)+Double.valueOf(restAmountVo3)+Double.valueOf(restAmountVo4)
							    +Double.valueOf(restAmountVo5)+Double.valueOf(restAmountVo6)+Double.valueOf(restAmountVo7)+Double.valueOf(restAmountVo8)
							    +Double.valueOf(restAmountVo9)+Double.valueOf(restAmountVo10)+Double.valueOf(restAmountVo11));
	    vo31.setFirstAmount(firstAmountVo31);
	    vo31.setRestAmount(restAmountVo31);
	    vo31.setCompanyName(vo.getCompanyName());
        balanceVos.add(vo31);
        
        /**长期股权投资**/
   	    BalanceVo vo32=new BalanceVo();
		vo32.setBalanceType("资产");
		vo32.setRowNum(32);
		vo32.setBalanceSubeType("长期投资");
		vo32.setBalanceName("长期股权投资");
   	    Double firstAmount_1401=getBalaceAmount("1401", FIRST, subjectType);
   	    Double firstAmount_1421=getBalaceAmount("1421", FIRST, subjectType);
   	    Double restAmount_1401=getBalaceAmount("1401", REST, subjectType);
   	    Double restAmount_1421=getBalaceAmount("1421", REST, subjectType);
   	    String firstAmountVo32=String.valueOf((firstAmount_1401-firstAmount_1421));
   	    String restAmountVo32=String.valueOf((restAmount_1401-restAmount_1421));
		vo32.setFirstAmount(firstAmountVo32);
		vo32.setRestAmount(restAmountVo32);
		vo32.setCompanyName(vo.getCompanyName());
        balanceVos.add(vo32);
        
        /**长期债权投资**/
		BalanceVo vo34 = new BalanceVo();
		vo34.setBalanceType("资产");
		vo34.setRowNum(34);
		vo34.setBalanceSubeType("长期投资");
		vo34.setBalanceName("长期债权投资");
		Double firstAmount_1402 = getBalaceAmount("1402", FIRST, subjectType);
		Double firstAmount_1431 = getBalaceAmount("1431", FIRST, subjectType);
		Double restAmount_1402 = getBalaceAmount("1402", REST, subjectType);
		Double restAmount_1431 = getBalaceAmount("1431", REST, subjectType);
		String firstAmountVo34 = String.valueOf((firstAmount_1402 + firstAmount_1431 - firstAmount_1421));
		String restAmountVo34 = String.valueOf((restAmount_1402 + restAmount_1431 - restAmount_1421));
		vo34.setFirstAmount(firstAmountVo34);
		vo34.setRestAmount(restAmountVo34);
		vo34.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo34);

		/**长期投资合计 **/
		BalanceVo vo38 = new BalanceVo();
		vo38.setBalanceType("资产");
		vo38.setRowNum(38);
		vo38.setBalanceSubeType("长期投资");
		vo38.setBalanceName("长期投资合计");
		String firstAmountVo38 = String.valueOf(Double.valueOf(firstAmountVo32)+Double.valueOf(firstAmountVo34));
		String restAmountVo38 = String.valueOf(Double.valueOf(restAmountVo32)+Double.valueOf(restAmountVo34));
		vo38.setFirstAmount(firstAmountVo38);
		vo38.setRestAmount(restAmountVo38);
		vo38.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo38);
		
		/** 长期债权投资 **/
		BalanceVo vo39 = new BalanceVo();
		vo39.setBalanceType("资产");
		vo39.setRowNum(39);
		vo39.setBalanceSubeType("固定资产");
		vo39.setBalanceName("固定资产原价");
		Double firstAmount_1501 = getBalaceAmount("1501", FIRST, subjectType);
		Double restAmount_1501 = getBalaceAmount("1501", REST, subjectType);
		String firstAmountVo39 = String.valueOf((firstAmount_1501));
		String restAmountVo39 = String.valueOf((restAmount_1501));
		vo39.setFirstAmount(firstAmountVo39);
		vo39.setRestAmount(restAmountVo39);
		vo39.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo39);
		
		/**减：累计折价**/
		BalanceVo vo40 = new BalanceVo();
		vo40.setBalanceType("资产");
		vo40.setRowNum(40);
		vo40.setBalanceSubeType("固定资产");
		vo40.setBalanceName("减：累计折价");
		Double firstAmount_1502 = getBalaceAmount("1502", FIRST, subjectType);
		Double restAmount_1502 = getBalaceAmount("1502", REST, subjectType);
		String firstAmountVo40 = String.valueOf((firstAmount_1502));
		String restAmountVo40 = String.valueOf((restAmount_1502));
		vo40.setFirstAmount(firstAmountVo40);
		vo40.setRestAmount(restAmountVo40);
		vo40.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo40);
		
		/**固定资产净值**/
		BalanceVo vo41 = new BalanceVo();
		vo41.setBalanceType("资产");
		vo41.setRowNum(41);
		vo41.setBalanceSubeType("固定资产");
		vo41.setBalanceName("固定资产净值");
		String firstAmountVo41 = String.valueOf(Double.valueOf(firstAmountVo39)-Double.valueOf(firstAmountVo40));
		String restAmountVo41 =String.valueOf(Double.valueOf(restAmountVo39)-Double.valueOf(restAmountVo40));
		vo41.setFirstAmount(firstAmountVo41);
		vo41.setRestAmount(restAmountVo41);
		vo41.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo41);
		
		/**减：固定资产减值准备**/
		BalanceVo vo42 = new BalanceVo();
		vo42.setBalanceType("资产");
		vo42.setRowNum(42);
		vo42.setBalanceSubeType("固定资产");
		vo42.setBalanceName("减：固定资产减值准备");
		Double firstAmount_1505 = getBalaceAmount("1505", FIRST, subjectType);
		Double restAmount_1505 = getBalaceAmount("1505", REST, subjectType);
		String firstAmountVo42 = String.valueOf((firstAmount_1505));
		String restAmountVo42 = String.valueOf((restAmount_1505));
		vo42.setFirstAmount(firstAmountVo42);
		vo42.setRestAmount(restAmountVo42);
		vo42.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo42);
		
		/** 固定资产净额**/
		BalanceVo vo43 = new BalanceVo();
		vo43.setBalanceType("资产");
		vo43.setRowNum(43);
		vo43.setBalanceSubeType("固定资产");
		vo43.setBalanceName(" 固定资产净额");
		String firstAmountVo43 = String.valueOf(Double.valueOf(firstAmountVo41)-Double.valueOf(firstAmountVo42));
		String restAmountVo43 = String.valueOf(Double.valueOf(restAmountVo41)-Double.valueOf(restAmountVo42));
		vo43.setFirstAmount(firstAmountVo43);
		vo43.setRestAmount(restAmountVo43);
		vo43.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo43);
		
		/**工程物资**/
		BalanceVo vo44 = new BalanceVo();
		vo44.setBalanceType("资产");
		vo44.setRowNum(44);
		vo44.setBalanceSubeType("固定资产");
		vo44.setBalanceName("工程物资");
		Double firstAmount_1601 = getBalaceAmount("1601", FIRST, subjectType);
		Double restAmount_1601 = getBalaceAmount("1601", REST, subjectType);
		String firstAmountVo44 = String.valueOf((firstAmount_1601));
		String restAmountVo44 = String.valueOf((restAmount_1601));
		vo44.setFirstAmount(firstAmountVo44);
		vo44.setRestAmount(restAmountVo44);
		vo44.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo44);
		
		/**在建工程**/
		BalanceVo vo45 = new BalanceVo();
		vo45.setBalanceType("资产");
		vo45.setRowNum(45);
		vo45.setBalanceSubeType("固定资产");
		vo45.setBalanceName("在建工程");
		Double firstAmount_1603 = getBalaceAmount("1603", FIRST, subjectType);
		Double firstAmount_1605 = getBalaceAmount("1605", FIRST, subjectType);
		Double restAmount_1603 = getBalaceAmount("1603", REST, subjectType);
		Double restAmount_1605 = getBalaceAmount("1605", REST, subjectType);
		String firstAmountVo45 = String.valueOf((firstAmount_1603-firstAmount_1605));
		String restAmountVo45 = String.valueOf((restAmount_1603-restAmount_1605));
		vo45.setFirstAmount(firstAmountVo45);
		vo45.setRestAmount(restAmountVo45);
		vo45.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo45);
		
		/**固定资产清理**/
		BalanceVo vo46 = new BalanceVo();
		vo46.setBalanceType("资产");
		vo46.setRowNum(46);
		vo46.setBalanceSubeType("固定资产");
		vo46.setBalanceName("固定资产清理");
		Double firstAmount_1701 = getBalaceAmount("1701", FIRST, subjectType);
		Double restAmount_1701 = getBalaceAmount("1701", REST, subjectType);
		String firstAmountVo46 = String.valueOf((firstAmount_1701));
		String restAmountVo46 = String.valueOf((restAmount_1701));
		vo46.setFirstAmount(firstAmountVo46);
		vo46.setRestAmount(restAmountVo46);
		vo46.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo46);
		
		/**固定资产合计**/
		BalanceVo vo50 = new BalanceVo();
		vo50.setBalanceType("资产");
		vo50.setRowNum(50);
		vo50.setBalanceSubeType("固定资产");
		vo50.setBalanceName("固定资产合计");
		String firstAmountVo50 =  String.valueOf(Double.valueOf(firstAmountVo43)+Double.valueOf(firstAmountVo44)+Double.valueOf(firstAmountVo45)+Double.valueOf(firstAmountVo46));
		String restAmountVo50 =  String.valueOf(Double.valueOf(restAmountVo43)+Double.valueOf(restAmountVo44)+Double.valueOf(restAmountVo45)+Double.valueOf(restAmountVo46));
		vo50.setFirstAmount(firstAmountVo50);
		vo50.setRestAmount(restAmountVo50);
		vo50.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo50);
		
		/**无形资产**/
		BalanceVo vo51 = new BalanceVo();
		vo51.setBalanceType("资产");
		vo51.setRowNum(51);
		vo51.setBalanceSubeType("无形资产及其它资产");
		vo51.setBalanceName("无形资产");
		Double firstAmount_1801 = getBalaceAmount("1801", FIRST, subjectType);
		Double firstAmount_1805 = getBalaceAmount("1805", FIRST, subjectType);
		Double restAmount_1801 = getBalaceAmount("1801", REST, subjectType);
		Double restAmount_1805= getBalaceAmount("1805", REST, subjectType);
		String firstAmountVo51 = String.valueOf((firstAmount_1801-firstAmount_1805));
		String restAmountVo51 = String.valueOf((restAmount_1801-restAmount_1805));
		vo51.setFirstAmount(firstAmountVo51);
		vo51.setRestAmount(restAmountVo51);
		vo51.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo51);
		
		/**长期待摊费用**/
		BalanceVo vo52 = new BalanceVo();
		vo52.setBalanceType("资产");
		vo52.setRowNum(52);
		vo52.setBalanceSubeType("无形资产及其它资产");
		vo52.setBalanceName("长期待摊费用");
		Double firstAmount_1901 = getBalaceAmount("1901", FIRST, subjectType);
		Double restAmount_1901 = getBalaceAmount("1901", REST, subjectType);
		String firstAmountVo52 = String.valueOf((firstAmount_1901));
		String restAmountVo52 = String.valueOf((restAmount_1901));
		vo52.setFirstAmount(firstAmountVo51);
		vo52.setRestAmount(restAmountVo51);
		vo52.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo52);
		
		/**无形资产及其它资产合计**/
		BalanceVo vo60 = new BalanceVo();
		vo60.setBalanceType("资产");
		vo60.setRowNum(60);
		vo60.setBalanceSubeType("固定资产");
		vo60.setBalanceName("无形资产及其它资产合计");
		String firstAmountVo60 =  String.valueOf(Double.valueOf(firstAmountVo51)+Double.valueOf(firstAmountVo52));
		String restAmountVo60 =  String.valueOf(Double.valueOf(restAmountVo51)+Double.valueOf(restAmountVo52));
		vo60.setFirstAmount(firstAmountVo60);
		vo60.setRestAmount(restAmountVo60);
		vo60.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo60);
		
		/**递延税款借项**/
		BalanceVo vo61 = new BalanceVo();
		vo61.setBalanceType("资产");
		vo61.setRowNum(61);
		vo61.setBalanceSubeType("递延税款");
		vo61.setBalanceName("递延税款借项");
		Double firstAmount_2341 = getBalaceAmount("2341", FIRST, subjectType);
		Double restAmount_2341 = getBalaceAmount("2341", REST, subjectType);
		String firstAmountVo61 = String.valueOf((firstAmount_2341));
		String restAmountVo61 = String.valueOf((restAmount_2341));
		vo61.setFirstAmount(firstAmountVo61);
		vo61.setRestAmount(restAmountVo61);
		vo61.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo61);
		
		/**资产总计**/
		BalanceVo vo67 = new BalanceVo();
		vo67.setBalanceType("资产");
		vo67.setRowNum(67);
		vo67.setBalanceSubeType("资产总计");
		vo67.setBalanceName("资产总计");
		String firstAmountVo67 =  String.valueOf(Double.valueOf(firstAmountVo31)+Double.valueOf(firstAmountVo38)+Double.valueOf(firstAmountVo50)+Double.valueOf(firstAmountVo61));
		String restAmountVo67 =  String.valueOf(Double.valueOf(restAmountVo31)+Double.valueOf(restAmountVo38)+Double.valueOf(restAmountVo50)+Double.valueOf(restAmountVo61));
		vo67.setFirstAmount(firstAmountVo67);
		vo67.setRestAmount(restAmountVo67);
		vo67.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo67);
		
		/**短期借款**/
		BalanceVo vo68 = new BalanceVo();
		vo68.setBalanceType("负债及股东权益");
		vo68.setRowNum(68);
		vo68.setBalanceSubeType("流动负债");
		vo68.setBalanceName("短期借款");
		Double firstAmount_2101 = getBalaceAmount("2101", FIRST, subjectType);
		Double restAmount_2101 = getBalaceAmount("2101", REST, subjectType);
		String firstAmountVo68 = String.valueOf((firstAmount_2101));
		String restAmountVo68 = String.valueOf((restAmount_2101));
		vo68.setFirstAmount(firstAmountVo68);
		vo68.setRestAmount(restAmountVo68);
		vo68.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo68);
		

		/**应付票据**/
		BalanceVo vo69 = new BalanceVo();
		vo69.setBalanceType("负债及股东权益");
		vo69.setRowNum(69);
		vo69.setBalanceSubeType("流动负债");
		vo69.setBalanceName("应付票据");
		Double firstAmount_2111 = getBalaceAmount("2111", FIRST, subjectType);
		Double restAmount_2111 = getBalaceAmount("2111", REST, subjectType);
		String firstAmountVo69 = String.valueOf((firstAmount_2111));
		String restAmountVo69 = String.valueOf((restAmount_2111));
		vo69.setFirstAmount(firstAmountVo69);
		vo69.setRestAmount(restAmountVo69);
		vo69.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo69);
		
		/**应付账款**/
		BalanceVo vo70 = new BalanceVo();
		vo70.setBalanceType("负债及股东权益");
		vo70.setRowNum(70);
		vo70.setBalanceSubeType("流动负债");
		vo70.setBalanceName("应付账款");
		String firstAmountVo70 = String.valueOf((getBalaceAmount("2121", FIRST, subjectType)+first_yfzk+firstAmount_1151));
		String restAmountVo70 = String.valueOf((getBalaceAmount("2121", REST, subjectType)+rest_yfzk+restAmount_1151));
		vo70.setFirstAmount(firstAmountVo70);
		vo70.setRestAmount(restAmountVo70);
		vo70.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo70);
		
		/**预收账款**/
		BalanceVo vo71 = new BalanceVo();
		vo71.setBalanceType("负债及股东权益");
		vo71.setRowNum(71);
		vo71.setBalanceSubeType("流动负债");
		vo71.setBalanceName("预收账款");
		String firstAmountVo71 = String.valueOf((firstAmount_2131+first_yszk));
		String restAmountVo71 = String.valueOf((restAmount_2131+rest_yszk));
		vo71.setFirstAmount(firstAmountVo71);
		vo71.setRestAmount(restAmountVo71);
		vo71.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo71);
		
		/**应付工资**/
		BalanceVo vo72 = new BalanceVo();
		vo72.setBalanceType("负债及股东权益");
		vo72.setRowNum(72);
		vo72.setBalanceSubeType("流动负债");
		vo72.setBalanceName("应付工资");
		Double firstAmount_2151 = getBalaceAmount("2151", FIRST, subjectType);
		Double restAmount_2151 = getBalaceAmount("2151", REST, subjectType);
		String firstAmountVo72 = String.valueOf((firstAmount_2151));
		String restAmountVo72 = String.valueOf((restAmount_2151));
		vo72.setFirstAmount(firstAmountVo72);
		vo72.setRestAmount(restAmountVo72);
		vo72.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo72);
		
		/**应付福利费**/
		BalanceVo vo73 = new BalanceVo();
		vo73.setBalanceType("负债及股东权益");
		vo73.setRowNum(73);
		vo73.setBalanceSubeType("流动负债");
		vo73.setBalanceName("应付福利费");
		Double firstAmount_2153 = getBalaceAmount("2153", FIRST, subjectType);
		Double restAmount_2153 = getBalaceAmount("2153", REST, subjectType);
		String firstAmountVo73 = String.valueOf((firstAmount_2153));
		String restAmountVo73 = String.valueOf((restAmount_2153));
		vo73.setFirstAmount(firstAmountVo73);
		vo73.setRestAmount(restAmountVo73);
		vo73.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo73);
		
		/**应付股利**/
		BalanceVo vo74 = new BalanceVo();
		vo74.setBalanceType("负债及股东权益");
		vo74.setRowNum(74);
		vo74.setBalanceSubeType("流动负债");
		vo74.setBalanceName("应付股利");
		Double firstAmount_2161 = getBalaceAmount("2161", FIRST, subjectType);
		Double restAmount_2161 = getBalaceAmount("2161", REST, subjectType);
		String firstAmountVo74 = String.valueOf((firstAmount_2161));
		String restAmountVo74 = String.valueOf((restAmount_2161));
		vo74.setFirstAmount(firstAmountVo74);
		vo74.setRestAmount(restAmountVo74);
		vo74.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo74);
		
		/**应交税金**/
		BalanceVo vo75 = new BalanceVo();
		vo75.setBalanceType("负债及股东权益");
		vo75.setRowNum(75);
		vo75.setBalanceSubeType("流动负债");
		vo75.setBalanceName("应交税金");
		Double firstAmount_2171 = getBalaceAmount("2171", FIRST, subjectType);
		Double restAmount_2171 = getBalaceAmount("2171", REST, subjectType);
		String firstAmountVo75 = String.valueOf((firstAmount_2171));
		String restAmountVo75 = String.valueOf((restAmount_2171));
		vo75.setFirstAmount(firstAmountVo75);
		vo75.setRestAmount(restAmountVo75);
		vo75.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo75);
		
		/**其他应交款**/
		BalanceVo vo80 = new BalanceVo();
		vo80.setBalanceType("负债及股东权益");
		vo80.setRowNum(80);
		vo80.setBalanceSubeType("流动负债");
		vo80.setBalanceName("其他应交款");
		Double firstAmount_2176 = getBalaceAmount("2176", FIRST, subjectType);
		Double restAmount_2176 = getBalaceAmount("2176", REST, subjectType);
		String firstAmountvo80 = String.valueOf((firstAmount_2176));
		String restAmountvo80 = String.valueOf((restAmount_2176));
		vo80.setFirstAmount(firstAmountvo80);
		vo80.setRestAmount(restAmountvo80);
		vo80.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo80);
		
		/**其他应付款**/
		BalanceVo vo81 = new BalanceVo();
		vo81.setBalanceType("负债及股东权益");
		vo81.setRowNum(81);
		vo81.setBalanceSubeType("流动负债");
		vo81.setBalanceName("其他应付款");
		Double firstAmount_2181 = getBalaceAmount("2181", FIRST, subjectType);
		Double restAmount_2181 = getBalaceAmount("2181", REST, subjectType);
		String firstAmountvo81 = String.valueOf((firstAmount_2181));
		String restAmountvo81 = String.valueOf((restAmount_2181));
		vo81.setFirstAmount(firstAmountvo81);
		vo81.setRestAmount(restAmountvo81);
		vo81.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo81);
		
		/**预提费用**/
		BalanceVo vo82 = new BalanceVo();
		vo82.setBalanceType("负债及股东权益");
		vo82.setRowNum(82);
		vo82.setBalanceSubeType("流动负债");
		vo82.setBalanceName("预提费用");
		String firstAmountvo82 = String.valueOf((firstAmount_2191+firstAmount_1301));
		String restAmountvo82 = String.valueOf((restAmount_1301+restAmount_2191));
		vo82.setFirstAmount(firstAmountvo82);
		vo82.setRestAmount(restAmountvo82);
		vo82.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo82);
		
		/**预计负债**/
		BalanceVo vo83 = new BalanceVo();
		vo83.setBalanceType("负债及股东权益");
		vo83.setRowNum(83);
		vo83.setBalanceSubeType("流动负债");
		vo83.setBalanceName("预计负债");
		Double firstAmount_2211 = getBalaceAmount("2211", FIRST, subjectType);
		Double restAmount_2211 = getBalaceAmount("2211", REST, subjectType);
		String firstAmountvo83 = String.valueOf((firstAmount_2211));
		String restAmountvo83 = String.valueOf((restAmount_2211));
		vo83.setFirstAmount(firstAmountvo83);
		vo83.setRestAmount(restAmountvo83);
		vo83.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo83);
		
		/**一年内到期的长期负债**/
		BalanceVo vo86 = new BalanceVo();
		vo86.setBalanceType("负债及股东权益");
		vo86.setRowNum(86);
		vo86.setBalanceSubeType("流动负债");
		vo86.setBalanceName("一年内到期的长期负债");
		String firstAmountvo86 = String.valueOf("0.0");
		String restAmountvo86 = String.valueOf("0.0");
		vo86.setFirstAmount(firstAmountvo86);
		vo86.setRestAmount(restAmountvo86);
		vo86.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo86);
		
		/**其它流动负债**/
		BalanceVo vo90 = new BalanceVo();
		vo90.setBalanceType("负债及股东权益");
		vo90.setRowNum(86);
		vo90.setBalanceSubeType("流动负债");
		vo90.setBalanceName("其它流动负债");
		String firstAmountvo90 = String.valueOf("0.0");
		String restAmountvo90 = String.valueOf("0.0");
		vo90.setFirstAmount(firstAmountvo90);
		vo90.setRestAmount(restAmountvo90);
		vo90.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo90);
		
		
		/**流动负债合计**/
		BalanceVo vo100 = new BalanceVo();
		vo100.setBalanceType("负债及股东权益");
		vo100.setRowNum(100);
		vo100.setBalanceSubeType("流动负债");
		vo100.setBalanceName("流动负债合计");
		String firstAmountVo100 =  String.valueOf(Double.valueOf(firstAmountVo68)+Double.valueOf(firstAmountVo69)+Double.valueOf(firstAmountVo70)+Double.valueOf(firstAmountVo71)
		                          +Double.valueOf(firstAmountVo72)+Double.valueOf(firstAmountVo73)+Double.valueOf(firstAmountVo74)+Double.valueOf(firstAmountVo75)
		                          +Double.valueOf(firstAmountvo80)+Double.valueOf(firstAmountvo81)+Double.valueOf(firstAmountvo82)+Double.valueOf(firstAmountvo83)+Double.valueOf(firstAmountvo86));
		String restAmountVo100 = String.valueOf(Double.valueOf(restAmountVo68)+Double.valueOf(restAmountVo69)+Double.valueOf(restAmountVo70)+Double.valueOf(restAmountVo71)
						        +Double.valueOf(restAmountVo72)+Double.valueOf(restAmountVo73)+Double.valueOf(restAmountVo74)+Double.valueOf(restAmountVo75)
						        +Double.valueOf(restAmountvo80)+Double.valueOf(restAmountvo81)+Double.valueOf(restAmountvo82)+Double.valueOf(restAmountvo83)+Double.valueOf(restAmountvo86));
		vo100.setFirstAmount(firstAmountVo100);
		vo100.setRestAmount(restAmountVo100);
		vo100.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo100);
		
		
		/**长期借款**/
		BalanceVo vo101 = new BalanceVo();
		vo101.setBalanceType("负债及股东权益");
		vo101.setRowNum(101);
		vo101.setBalanceSubeType("长期负债");
		vo101.setBalanceName("长期借款");
		Double firstAmount_2301 = getBalaceAmount("2301", FIRST, subjectType);
		Double restAmount_2301 = getBalaceAmount("2301", REST, subjectType);
		String firstAmountvo101 = String.valueOf((firstAmount_2301));
		String restAmountvo101 = String.valueOf((restAmount_2301));
		vo101.setFirstAmount(firstAmountvo101);
		vo101.setRestAmount(restAmountvo101);
		vo101.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo101);
		
		/**应付债券**/
		BalanceVo vo102 = new BalanceVo();
		vo102.setBalanceType("负债及股东权益");
		vo102.setRowNum(102);
		vo102.setBalanceSubeType("长期负债");
		vo102.setBalanceName("应付债券");
		Double firstAmount_2311 = getBalaceAmount("2311", FIRST, subjectType);
		Double restAmount_2311 = getBalaceAmount("2311", REST, subjectType);
		String firstAmountvo102 = String.valueOf((firstAmount_2311));
		String restAmountvo102 = String.valueOf((restAmount_2311));
		vo102.setFirstAmount(firstAmountvo102);
		vo102.setRestAmount(restAmountvo102);
		vo102.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo102);
		
		/**长期应付款**/
		BalanceVo vo103 = new BalanceVo();
		vo103.setBalanceType("负债及股东权益");
		vo103.setRowNum(103);
		vo103.setBalanceSubeType("长期负债");
		vo103.setBalanceName("长期应付款");
		Double firstAmount_2321 = getBalaceAmount("2321", FIRST, subjectType);
		Double firstAmount_1815 = getBalaceAmount("1815", FIRST, subjectType);
		Double restAmount_2321 = getBalaceAmount("2321", REST, subjectType);
		Double restAmount_1815 = getBalaceAmount("1815", REST, subjectType);
		String firstAmountvo103 = String.valueOf((firstAmount_2321-firstAmount_1815));
		String restAmountvo103 = String.valueOf((restAmount_2321-restAmount_1815));
		vo103.setFirstAmount(firstAmountvo103);
		vo103.setRestAmount(restAmountvo103);
		vo103.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo103);
		
		/**专项应付款**/
		BalanceVo vo106 = new BalanceVo();
		vo106.setBalanceType("负债及股东权益");
		vo106.setRowNum(106);
		vo106.setBalanceSubeType("长期负债");
		vo106.setBalanceName("专项应付款");
		Double firstAmount_2331 = getBalaceAmount("2331", FIRST, subjectType);
		Double restAmount_2331 = getBalaceAmount("2331", REST, subjectType);
		String firstAmountvo106 = String.valueOf((firstAmount_2331));
		String restAmountvo106 = String.valueOf((restAmount_2331));
		vo106.setFirstAmount(firstAmountvo106);
		vo106.setRestAmount(restAmountvo106);
		vo106.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo106);
		
		/**其他长期负债**/
		BalanceVo vo108 = new BalanceVo();
		vo108.setBalanceType("负债及股东权益");
		vo108.setRowNum(108);
		vo108.setBalanceSubeType("长期负债");
		vo108.setBalanceName("其他长期负债");
		String firstAmountvo108 = String.valueOf("0.0");
		String restAmountvo108 = String.valueOf("0.0");
		vo108.setFirstAmount(firstAmountvo108);
		vo108.setRestAmount(restAmountvo108);
		vo108.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo108);
		

		/**长期负债合计**/
		BalanceVo vo110 = new BalanceVo();
		vo110.setBalanceType("负债及股东权益");
		vo110.setRowNum(110);
		vo110.setBalanceSubeType("流动负债");
		vo110.setBalanceName("长期负债合计");
		String firstAmountVo110 =  String.valueOf(Double.valueOf(firstAmountvo101)+Double.valueOf(firstAmountvo102)+Double.valueOf(firstAmountvo103)+Double.valueOf(firstAmountvo106)
		                          +Double.valueOf(firstAmountvo108));
		String restAmountVo110 = String.valueOf(Double.valueOf(restAmountvo101)+Double.valueOf(restAmountvo102)+Double.valueOf(restAmountvo103)+Double.valueOf(restAmountvo106)
						        +Double.valueOf(restAmountvo108));
		vo110.setFirstAmount(firstAmountVo110);
		vo110.setRestAmount(restAmountVo110);
		vo110.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo110);
		
		/**递处税款贷项**/
		BalanceVo vo111 = new BalanceVo();
		vo111.setBalanceType("负债及股东权益");
		vo111.setRowNum(111);
		vo111.setBalanceSubeType("递延税项");
		vo111.setBalanceName("递处税款贷项");
		String firstAmountvo111 = String.valueOf((firstAmount_2341));
		String restAmountvo111 = String.valueOf((restAmount_2341));
		vo111.setFirstAmount(firstAmountvo111);
		vo111.setRestAmount(restAmountvo111);
		vo111.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo111);
		
		/**负债合计**/
		BalanceVo vo114 = new BalanceVo();
		vo114.setBalanceType("负债及股东权益");
		vo114.setRowNum(114);
		vo114.setBalanceSubeType("递延税项");
		vo114.setBalanceName("负债合计");
		String firstAmountvo114 = String.valueOf(Double.valueOf(firstAmountVo100)+Double.valueOf(firstAmountVo110)+Double.valueOf(firstAmountvo111));
		String restAmountvo114 =String.valueOf(Double.valueOf(restAmountVo100)+Double.valueOf(restAmountVo110)+Double.valueOf(restAmountvo111));
		vo114.setFirstAmount(firstAmountvo114);
		vo114.setRestAmount(restAmountvo114);
		vo114.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo114);
		
		/**实收资本（或股本）**/
		BalanceVo vo115 = new BalanceVo();
		vo115.setBalanceType("负债及股东权益");
		vo115.setRowNum(115);
		vo115.setBalanceSubeType("所有者权益（或股东权益）");
		vo115.setBalanceName("实收资本（或股本）");
		Double firstAmount_3101 = getBalaceAmount("3101", FIRST, subjectType);
		Double restAmount_3101 = getBalaceAmount("3101", REST, subjectType);
		String firstAmountvo115 = String.valueOf((firstAmount_3101));
		String restAmountvo115 = String.valueOf((restAmount_3101));
		vo115.setFirstAmount(firstAmountvo115);
		vo115.setRestAmount(restAmountvo115);
		vo115.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo115);
		
		/**减：已归还投资**/
		BalanceVo vo116 = new BalanceVo();
		vo116.setBalanceType("负债及股东权益");
		vo116.setRowNum(116);
		vo116.setBalanceSubeType("所有者权益（或股东权益）");
		vo116.setBalanceName("减：已归还投资");
		Double firstAmount_3103 = getBalaceAmount("3103", FIRST, subjectType);
		Double restAmount_3103 = getBalaceAmount("3103", REST, subjectType);
		String firstAmountvo116 = String.valueOf((firstAmount_3103));
		String restAmountvo116 = String.valueOf((restAmount_3103));
		vo116.setFirstAmount(firstAmountvo116);
		vo116.setRestAmount(restAmountvo116);
		vo116.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo116);
		
		/**实收资本（或股本）净额**/
		BalanceVo vo117 = new BalanceVo();
		vo117.setBalanceType("负债及股东权益");
		vo117.setRowNum(117);
		vo117.setBalanceSubeType("所有者权益（或股东权益）");
		vo117.setBalanceName("实收资本（或股本）净额");
		String firstAmountvo117 = String.valueOf(Double.valueOf(firstAmountvo115)-Double.valueOf(firstAmountvo116));
		String restAmountvo117 = String.valueOf(Double.valueOf(restAmountvo115)-Double.valueOf(restAmountvo116));
		vo117.setFirstAmount(firstAmountvo117);
		vo117.setRestAmount(restAmountvo117);
		vo117.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo117);
		
		/**资本公积**/
		BalanceVo vo118 = new BalanceVo();
		vo118.setBalanceType("负债及股东权益");
		vo118.setRowNum(118);
		vo118.setBalanceSubeType("所有者权益（或股东权益）");
		vo118.setBalanceName("资本公积");
		Double firstAmount_3111 = getBalaceAmount("3111", FIRST, subjectType);
		Double restAmount_3111= getBalaceAmount("3111", REST, subjectType);
		String firstAmountvo118 = String.valueOf(firstAmount_3111);
		String restAmountvo118 = String.valueOf(restAmount_3111);
		vo118.setFirstAmount(firstAmountvo118);
		vo118.setRestAmount(restAmountvo118);
		vo118.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo118);
		
		/**盈余公积**/
		BalanceVo vo119 = new BalanceVo();
		vo119.setBalanceType("负债及股东权益");
		vo119.setRowNum(119);
		vo119.setBalanceSubeType("所有者权益（或股东权益）");
		vo119.setBalanceName("盈余公积");
		Double firstAmount_3121 = getBalaceAmount("3121", FIRST, subjectType);
		Double restAmount_3121= getBalaceAmount("3121", REST, subjectType);
		String firstAmountvo119 = String.valueOf(firstAmount_3121);
		String restAmountvo119 = String.valueOf(restAmount_3121);
		vo119.setFirstAmount(firstAmountvo119);
		vo119.setRestAmount(restAmountvo119);
		vo119.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo119);
		
		
		/**其中：法定公益金**/
		BalanceVo vo120 = new BalanceVo();
		vo120.setBalanceType("负债及股东权益");
		vo120.setRowNum(120);
		vo120.setBalanceSubeType("所有者权益（或股东权益）");
		vo120.setBalanceName("其中：法定公益金");
		Double firstAmount_3121_01 = getBalaceAmount("3121-01", FIRST, subjectType);
		Double restAmount_3121_01= getBalaceAmount("3121-01", REST, subjectType);
		String firstAmountvo120 = String.valueOf(firstAmount_3121_01);
		String restAmountvo120 = String.valueOf(restAmount_3121_01);
		vo120.setFirstAmount(firstAmountvo120);
		vo120.setRestAmount(restAmountvo120);
		vo120.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo120);
		
		/**未分配利润**/
		BalanceVo vo121 = new BalanceVo();
		vo121.setBalanceType("负债及股东权益");
		vo121.setRowNum(121);
		vo121.setBalanceSubeType("所有者权益（或股东权益）");
		vo121.setBalanceName("未分配利润");
		Double firstAmount_3131 = getBalaceAmount("3131", FIRST, subjectType);
		Double firstAmount_3141 = getBalaceAmount("3141", FIRST, subjectType);
		Double restAmount_3131= getBalaceAmount("3131", REST, subjectType);
		Double restAmount_3141= getBalaceAmount("3141", REST, subjectType);
		String firstAmountvo121 = String.valueOf(firstAmount_3131+firstAmount_3141);
		String restAmountvo121 = String.valueOf(restAmount_3131+restAmount_3141);
		vo121.setFirstAmount(firstAmountvo121);
		vo121.setRestAmount(restAmountvo121);
		vo121.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo121);
		
		/**所有者权益（或股东权益）合计**/
		BalanceVo vo122 = new BalanceVo();
		vo122.setBalanceType("负债及股东权益");
		vo122.setRowNum(122);
		vo122.setBalanceSubeType("所有者权益（或股东权益）");
		vo122.setBalanceName("所有者权益（或股东权益）合计");
		String firstAmountvo122 = String.valueOf(Double.valueOf(firstAmountvo117)+Double.valueOf(firstAmountvo118)+Double.valueOf(firstAmountvo119)+Double.valueOf(firstAmountvo121));
		String restAmountvo122 = String.valueOf(Double.valueOf(restAmountvo117)+Double.valueOf(restAmountvo118)+Double.valueOf(restAmountvo119)+Double.valueOf(restAmountvo121));
		vo122.setFirstAmount(firstAmountvo122);
		vo122.setRestAmount(restAmountvo122);
		vo122.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo122);
		
		/**负债和所有者权益（或股东权益）总计**/
		BalanceVo vo135 = new BalanceVo();
		vo135.setBalanceType("负债及股东权益");
		vo135.setRowNum(135);
		vo135.setBalanceSubeType("负债和所有者权益（或股东权益）总计");
		vo135.setBalanceName("负债和所有者权益（或股东权益）总计");
		String firstAmountvo135 = String.valueOf(Double.valueOf(firstAmountvo114)+Double.valueOf(firstAmountvo122));
		String restAmountvo135 = String.valueOf(Double.valueOf(restAmountvo114)+Double.valueOf(restAmountvo122));
		vo135.setFirstAmount(firstAmountvo135);
		vo135.setRestAmount(restAmountvo135);
		vo135.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo135);
		
		for(BalanceVo balanceVo:balanceVos) {
			BigDecimal firstAmount = new BigDecimal(Double.valueOf(balanceVo.getFirstAmount())).setScale(2, BigDecimal.ROUND_HALF_UP);
			BigDecimal restAmount = new BigDecimal(Double.valueOf(balanceVo.getRestAmount())).setScale(2, BigDecimal.ROUND_HALF_UP);
			balanceVo.setFirstAmount(firstAmount.toString());
			balanceVo.setRestAmount(restAmount.toString());
		}
		pages.setRecords(balanceVos);
		return pages;
	}
	
	@Override
	public Page<BalanceVo> selectProfit(SubjectRestVo vo) throws Exception {
		// 查询科目余额表数据
		Page<SubjectRestVo> page = selectSubjectRest(vo);//本年累计数
		//获取最后1月第1天
		Date endDate=vo.getEndDate();
		Calendar  cale=Calendar.getInstance();
		cale.setTime(endDate);
		cale.set(Calendar.DAY_OF_MONTH, 1);
		Date startDate=cale.getTime();
		vo.setBeginDate(startDate);
		Page<SubjectRestVo> monthPage = selectSubjectRest(vo); //本月数
		List<SubjectRestVo> list = page.getRecords();
		List<SubjectRestVo> monthList = monthPage.getRecords();
		Map<String, List<SubjectRestVo>> subjectType = list.stream()
				.collect(Collectors.groupingBy(SubjectRestVo::getSubject));

		Map<String, List<SubjectRestVo>> monthSubjectType = monthList.stream()
				.collect(Collectors.groupingBy(SubjectRestVo::getSubject));

		
		Page<BalanceVo> pages = new Page<>();
		pages.setCurrent(vo.getPage());
		pages.setSize(vo.getLimit());

		List<BalanceVo> balanceVos = new ArrayList();
		/** 一、主营业务收入 **/
		BalanceVo vo1 = new BalanceVo();
		vo1.setBalanceType("利润");
		vo1.setRowNum(1);
		vo1.setBalanceSubeType("一、主营业务收入");
		vo1.setBalanceName("一、主营业务收入");
		Double firstAmount_5101 = getBalaceAmount("5101", REST, monthSubjectType);
		Double restAmount_5101 = getBalaceAmount("5101", REST, subjectType);
		String firstAmountVo1 = String.valueOf((firstAmount_5101));
		String restAmountVo1 = String.valueOf((restAmount_5101));
		vo1.setFirstAmount(firstAmountVo1);
		vo1.setRestAmount(restAmountVo1);
		vo1.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo1);

		/** 减：主营业务成本 **/
		BalanceVo vo4 = new BalanceVo();
		vo4.setBalanceType("利润");
		vo4.setRowNum(4);
		vo4.setBalanceSubeType("减：主营业务成本");
		vo4.setBalanceName("减：主营业务成本");
		Double firstAmount_5401 = getBalaceAmount("5401", REST, monthSubjectType);
		Double restAmount_5401 = getBalaceAmount("5401", REST, subjectType);
		String firstAmountVo2 = String.valueOf((firstAmount_5401));
		String restAmountVo2 = String.valueOf((restAmount_5401));
		vo4.setFirstAmount(firstAmountVo2);
		vo4.setRestAmount(restAmountVo2);
		vo4.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo4);

		/** 主营业务税金及附加 **/
		BalanceVo vo5 = new BalanceVo();
		vo5.setBalanceType("利润");
		vo5.setRowNum(5);
		vo5.setBalanceSubeType("主营业务税金及附加");
		vo5.setBalanceName("主营业务税金及附加");
		Double firstAmount_5402 = getBalaceAmount("5402", REST, monthSubjectType);
		Double restAmount_5402 = getBalaceAmount("5402", REST, subjectType);
		String firstAmountVo3 = String.valueOf((firstAmount_5402));
		String restAmountVo3 = String.valueOf((restAmount_5402));
		vo5.setFirstAmount(firstAmountVo3);
		vo5.setRestAmount(restAmountVo3);
		vo5.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo5);

		/** 二、主营业务利润（亏损以“－”号填列） **/
		BalanceVo vo10 = new BalanceVo();
		vo10.setBalanceType("利润");
		vo10.setRowNum(10);
		vo10.setBalanceSubeType("二、主营业务利润（亏损以“－”号填列）");
		vo10.setBalanceName("二、主营业务利润（亏损以“－”号填列）");
		String firstAmountVo10 = String.valueOf(firstAmount_5101 - firstAmount_5401 - firstAmount_5402);
		String restAmountVo10 = String.valueOf(restAmount_5101 - restAmount_5401 - restAmount_5402);
		BigDecimal firstAmountVo10scale = new BigDecimal(Double.valueOf(firstAmountVo10)).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal restAmountVo10scale = new BigDecimal(Double.valueOf(restAmountVo10)).setScale(2, BigDecimal.ROUND_HALF_UP);
		vo10.setFirstAmount(firstAmountVo10scale.toString());
		vo10.setRestAmount(restAmountVo10scale.toString());
		vo10.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo10);

		/** 加：其他业务利润（亏损以“－”号填列） **/
		BalanceVo vo11 = new BalanceVo();
		vo11.setBalanceType("利润");
		vo11.setRowNum(11);
		vo11.setBalanceSubeType("加：其他业务利润（亏损以“－”号填列）");
		vo11.setBalanceName("加：其他业务利润（亏损以“－”号填列）");
		Double firstAmount_5102 = getBalaceAmount("5102", REST, monthSubjectType);
		Double firstAmount_5405 = getBalaceAmount("5405", REST, monthSubjectType);
		Double restAmount_5102 = getBalaceAmount("5102", REST, subjectType);
		Double restAmount_5405 = getBalaceAmount("5405", REST, subjectType);
		String firstAmountVo11 = String.valueOf(firstAmount_5102 - firstAmount_5405);
		String restAmountVo11 = String.valueOf(restAmount_5102 - restAmount_5405);
		BigDecimal firstAmountVo11scale = new BigDecimal(Double.valueOf(firstAmountVo11)).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal restAmountVo11scale = new BigDecimal(Double.valueOf(restAmountVo11)).setScale(2, BigDecimal.ROUND_HALF_UP);
		vo11.setFirstAmount(firstAmountVo11scale.toString());
		vo11.setRestAmount(restAmountVo11scale.toString());
		vo11.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo11);

		/** 减：营业费用 **/
		BalanceVo vo14 = new BalanceVo();
		vo14.setBalanceType("利润");
		vo14.setRowNum(14);
		vo14.setBalanceSubeType("减：营业费用");
		vo14.setBalanceName("减：营业费用");
		Double firstAmount_5501 = getBalaceAmount("5501", REST, monthSubjectType);
		Double restAmount_5501 = getBalaceAmount("5501", REST, subjectType);
		String firstAmountVo14 = String.valueOf(firstAmount_5501);
		String restAmountVo14 = String.valueOf(restAmount_5501);
		vo14.setFirstAmount(firstAmountVo14);
		vo14.setRestAmount(restAmountVo14);
		vo14.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo14);

		/** 管理费用 **/
		BalanceVo vo15 = new BalanceVo();
		vo15.setBalanceType("利润");
		vo15.setRowNum(15);
		vo15.setBalanceSubeType("管理费用");
		vo15.setBalanceName("管理费用");
		Double firstAmount_5502 = getBalaceAmount("5502", REST, monthSubjectType);
		Double restAmount_5502 = getBalaceAmount("5502", REST, subjectType);
		String firstAmountVo15 = String.valueOf(firstAmount_5502);
		String restAmountVo15 = String.valueOf(restAmount_5502);
		vo15.setFirstAmount(firstAmountVo15);
		vo15.setRestAmount(restAmountVo15);
		vo15.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo15);

		/** 财务费用 **/
		BalanceVo vo16 = new BalanceVo();
		vo16.setBalanceType("利润");
		vo16.setRowNum(16);
		vo16.setBalanceSubeType("财务费用");
		vo16.setBalanceName("财务费用");
		Double firstAmount_5503 = getBalaceAmount("5503", REST, monthSubjectType);
		Double restAmount_5503 = getBalaceAmount("5503", REST, subjectType);
		String firstAmountVo16 = String.valueOf(firstAmount_5503);
		String restAmountVo16 = String.valueOf(restAmount_5503);
		vo16.setFirstAmount(firstAmountVo16);
		vo16.setRestAmount(restAmountVo16);
		vo16.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo16);
		pages.setRecords(balanceVos);

		/** 三、营业利润（亏损以“－”号填列） **/
		BalanceVo vo18 = new BalanceVo();
		vo18.setBalanceType("利润");
		vo18.setRowNum(18);
		vo18.setBalanceSubeType("三、营业利润（亏损以“－”号填列）");
		vo18.setBalanceName("三、营业利润（亏损以“－”号填列）");
		String firstAmountVo18 = String.valueOf(Double.valueOf(firstAmountVo10) + Double.valueOf(firstAmountVo11)
				- Double.valueOf(firstAmountVo14) - Double.valueOf(firstAmountVo15) - Double.valueOf(firstAmountVo16));
		String restAmountVo18 = String.valueOf(Double.valueOf(restAmountVo10) + Double.valueOf(restAmountVo11)
				- Double.valueOf(restAmountVo14) - Double.valueOf(restAmountVo15) - Double.valueOf(restAmountVo16));
		
		BigDecimal firstAmountVo18scale = new BigDecimal(Double.valueOf(firstAmountVo18)).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal restAmountVo18scale = new BigDecimal(Double.valueOf(firstAmountVo18)).setScale(2, BigDecimal.ROUND_HALF_UP);
		vo18.setFirstAmount(firstAmountVo18scale.toString());
		vo18.setRestAmount(restAmountVo18scale.toString());
		vo18.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo18);
		pages.setRecords(balanceVos);

		/** 加：投资收益（损失以“－”号填列） **/
		BalanceVo vo19 = new BalanceVo();
		vo19.setBalanceType("利润");
		vo19.setRowNum(19);
		vo19.setBalanceSubeType("加：投资收益（损失以“－”号填列）");
		vo19.setBalanceName("加：投资收益（损失以“－”号填列）");
		Double firstAmount_5201 = getBalaceAmount("5201", REST, monthSubjectType);
		Double restAmount_5201 = getBalaceAmount("5201", REST, monthSubjectType);
		BigDecimal firstAmount_5201scale = new BigDecimal(Double.valueOf(firstAmount_5201)).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal restAmount_5201scale = new BigDecimal(Double.valueOf(restAmount_5201)).setScale(2, BigDecimal.ROUND_HALF_UP);
		String firstAmountVo19 = String.valueOf(firstAmount_5201scale);
		String restAmountVo19 = String.valueOf(restAmount_5201scale);
		vo19.setFirstAmount(firstAmountVo19);
		vo19.setRestAmount(restAmountVo19);
		vo19.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo19);
		pages.setRecords(balanceVos);

		/** 补贴收入 **/
		BalanceVo vo22 = new BalanceVo();
		vo22.setBalanceType("利润");
		vo22.setRowNum(22);
		vo22.setBalanceSubeType(" 补贴收入");
		vo22.setBalanceName(" 补贴收入");
		Double firstAmount_5203 = getBalaceAmount("5203", REST, monthSubjectType);
		Double restAmount_5203 = getBalaceAmount("5203", REST, subjectType);
		String firstAmountVo22 = String.valueOf(firstAmount_5203);
		String restAmountVo22 = String.valueOf(restAmount_5203);
		vo22.setFirstAmount(firstAmountVo22);
		vo22.setRestAmount(restAmountVo22);
		vo22.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo22);
		pages.setRecords(balanceVos);

		/** 营业外收入 **/
		BalanceVo vo23 = new BalanceVo();
		vo23.setBalanceType("利润");
		vo23.setRowNum(23);
		vo23.setBalanceSubeType(" 营业外收入");
		vo23.setBalanceName(" 营业外收入");
		Double firstAmount_5301 = getBalaceAmount("5301", REST, monthSubjectType);
		Double restAmount_5301 = getBalaceAmount("5301", REST, subjectType);
		String firstAmountVo23 = String.valueOf(firstAmount_5301);
		String restAmountVo23 = String.valueOf(restAmount_5301);
		vo23.setFirstAmount(firstAmountVo22);
		vo23.setRestAmount(restAmountVo22);
		vo23.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo23);
		pages.setRecords(balanceVos);

		/** 减：营业外支出 **/
		BalanceVo vo25 = new BalanceVo();
		vo25.setBalanceType("利润");
		vo25.setRowNum(25);
		vo25.setBalanceSubeType("减：营业外支出");
		vo25.setBalanceName("减：营业外支出");
		Double firstAmount_5601 = getBalaceAmount("5601", REST, monthSubjectType);
		Double restAmount_5601 = getBalaceAmount("5601", REST, subjectType);
		String firstAmountVo25 = String.valueOf(firstAmount_5601);
		String restAmountVo25 = String.valueOf(restAmount_5601);
		vo25.setFirstAmount(firstAmountVo25);
		vo25.setRestAmount(restAmountVo25);
		vo25.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo25);
		pages.setRecords(balanceVos);

		/** 四、利润总额（亏损总额以“－”号填列） **/
		BalanceVo vo27 = new BalanceVo();
		vo27.setBalanceType("利润");
		vo27.setRowNum(27);
		vo27.setBalanceSubeType("三、营业利润（亏损以“－”号填列）");
		vo27.setBalanceName("三、营业利润（亏损以“－”号填列）");
		String firstAmountVo27 = String.valueOf(Double.valueOf(firstAmountVo18) + Double.valueOf(firstAmountVo19)
				+ Double.valueOf(firstAmountVo22) + Double.valueOf(firstAmountVo23) - Double.valueOf(firstAmountVo25));
		String restAmountVo27 = String.valueOf(Double.valueOf(restAmountVo18) + Double.valueOf(restAmountVo19)
				+ Double.valueOf(restAmountVo22) + Double.valueOf(restAmountVo23) - Double.valueOf(restAmountVo25));
		
		BigDecimal firstAmountVo27scale = new BigDecimal(Double.valueOf(firstAmountVo27)).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal restAmountVo27scale = new BigDecimal(Double.valueOf(restAmountVo27)).setScale(2, BigDecimal.ROUND_HALF_UP);
		vo27.setFirstAmount(firstAmountVo27scale.toString());
		vo27.setRestAmount(restAmountVo27scale.toString());
		vo27.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo27);
		pages.setRecords(balanceVos);

		/** 减：所得税 **/
		BalanceVo vo28 = new BalanceVo();
		vo28.setBalanceType("利润");
		vo28.setRowNum(28);
		vo28.setBalanceSubeType("减：所得税");
		vo28.setBalanceName("减：所得税");
		Double firstAmount_5701 = getBalaceAmount("5701", REST, monthSubjectType);
		Double restAmount_5701 = getBalaceAmount("5701", REST, subjectType);
		String firstAmountVo28 = String.valueOf(firstAmount_5701);
		String restAmountVo28 = String.valueOf(restAmount_5701);
		vo28.setFirstAmount(firstAmountVo28);
		vo28.setRestAmount(restAmountVo28);
		vo28.setCompanyName(vo.getCompanyName());
		balanceVos.add(vo28);
		pages.setRecords(balanceVos);

		/** 五、净利润（净亏损以“－”号填列） **/
		BalanceVo v30 = new BalanceVo();
		v30.setBalanceType("利润");
		v30.setRowNum(30);
		v30.setBalanceSubeType("五、净利润（净亏损以“－”号填列）");
		v30.setBalanceName("五、净利润（净亏损以“－”号填列）");
		String firstAmountVo30 = String.valueOf(Double.valueOf(firstAmountVo27) - Double.valueOf(firstAmountVo28));
		String restAmountVo30 = String.valueOf(Double.valueOf(restAmountVo27) - Double.valueOf(restAmountVo28));
		BigDecimal firstAmountVo30scale = new BigDecimal(Double.valueOf(firstAmountVo30)).setScale(2, BigDecimal.ROUND_HALF_UP);
		BigDecimal restAmountVo30scale = new BigDecimal(Double.valueOf(restAmountVo30)).setScale(2, BigDecimal.ROUND_HALF_UP);
		v30.setFirstAmount(firstAmountVo30scale.toString());
		v30.setRestAmount(restAmountVo30scale.toString());
		v30.setCompanyName(vo.getCompanyName());
		balanceVos.add(v30);
		pages.setRecords(balanceVos);
		return pages;
	}
	
	public Double getBalaceAmount(String subject,String type,Map<String, List<SubjectRestVo>> map) {
		if("first".equals(type)) {
			List<SubjectRestVo> list=map.get(subject);
			if(null!=list&&list.size()>0) {
				return list.get(0).getFirstAmount()==null?0.0:Double.valueOf(list.get(0).getFirstAmount());
			}else {
				return 0.0;
			}
		}else {
			List<SubjectRestVo> list=map.get(subject);
			if(null!=list&&list.size()>0) {
				return list.get(0).getRestAmount()==null?0.0:Double.valueOf(list.get(0).getRestAmount());
			}else {
				return 0.0;
			}
		}
	}

	@Override
	public void downLoadFile(HttpServletResponse response,String tempFile,Map<String,Object> beans,String companyName) throws Exception {

		//加载excel模板文件
		InputStream tempStream=null;
		//tempStream	=Thread.currentThread().getContextClassLoader().getResourceAsStream("temp.xlsx");
		tempStream	=Thread.currentThread().getContextClassLoader().getResourceAsStream(tempFile);
		//配置下载路径
		createDir(new File(path));
		
		   // 表格使用的数据
//        Map map = new HashMap();
//        map.put("public.companyNo","709393");
//        map.put("public.companyName","软通有限公司");
//        map.put("public.beginYear","2020");
//        map.put("public.beginMonth","1");
//        map.put("public.beginDay","1");
//        map.put("public.endYear","2020");
//        map.put("public.endMonth","2");
//        map.put("public.endDay","1");
		File excelFile=createNewFile(beans, tempStream, path,companyName,tempFile);
		downLoad(response, excelFile);
		//删除服务器生成的文件
		deleteFile(excelFile);


	}
	
	  private File createNewFile(Map<String,Object> beans,InputStream tempStream,String path,String companyName,String fileType) throws ParsePropertyException, InvalidFormatException, IOException {
		
		   // 实例化 XLSTransformer 对象
	        XLSTransformer xlsTransformer = new XLSTransformer();
	        String title="企业会计制度";
	        if(fileType.equals(CamsConstant.COMPANY_TEMP)) {
	        	title="企业会计制度.xls";
	        }else {
	        	title="小企业会计准则.xls";
	        }
	        
	        String name=companyName+"__"+title;
	        File newFile=new File(path+name);
	        
	        
	        OutputStream out=new FileOutputStream(newFile);
	        Workbook workbook=xlsTransformer.transformXLS(tempStream, beans);
	        workbook.write(out);
	        out.flush();
	        return newFile;
		  
	  }
	  
	  private void downLoad(HttpServletResponse response,File excelFile) throws IOException {
		  //设置文件ContentType类型，这样设置，会自动判断下载文件类型
		  response.setContentType("mutipart/form-data");
		  //设置文件头:最后一个参数是设置下载文件名
		  response.setHeader("Content-Disposition",
					"attachment; filename=" + URLEncoder.encode(excelFile.getName(), "UTF-8"));// 导出中文名
		  InputStream  ins=new FileInputStream(excelFile);
		  OutputStream os=response.getOutputStream();
		  byte[] b=new byte[1024];
		  int len;
		  while((len=ins.read(b))>0){
			  os.write(b,0,len);
		  }
	  }
	  
	  private void createDir(File file) {
		  if(!file.exists()) {
			  file.mkdirs();
		  }
	  }
	  
	  private void deleteFile(File excelFile) {
		  excelFile.delete();
	  }


}
