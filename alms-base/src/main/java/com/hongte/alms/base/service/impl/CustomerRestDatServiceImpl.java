package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.plugins.pagination.Pagination;
import com.hongte.alms.base.entity.CamsSubject;
import com.hongte.alms.base.entity.CustomerFirstDat;
import com.hongte.alms.base.entity.CustomerRestDat;
import com.hongte.alms.base.entity.SubjectFirstDat;
import com.hongte.alms.base.enums.CamsConstant;
import com.hongte.alms.base.mapper.CustomerRestDatMapper;
import com.hongte.alms.base.mapper.SubjectRestDatMapper;
import com.hongte.alms.base.service.CustomerFirstDatService;
import com.hongte.alms.base.service.CustomerRestDatService;
import com.hongte.alms.base.vo.cams.CustomerRestVo;
import com.hongte.alms.base.vo.cams.SubjectRestVo;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.DateUtil;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 单位余额表 服务实现类
 * </p>
 *
 * @author czs
 * @since 2020-02-10
 */
@Service("CustomerRestDatService")
public class CustomerRestDatServiceImpl extends BaseServiceImpl<CustomerRestDatMapper, CustomerRestDat> implements CustomerRestDatService {

	@Autowired
	private CustomerRestDatMapper customerRestDatMapper;
	
	@Autowired
	@Qualifier("CustomerRestDatService")
	private CustomerRestDatService customerRestDatService;
	
	@Autowired
	@Qualifier("CustomerFirstDatService")
	private CustomerFirstDatService customerFirstDatService;
	
	@Override
	public void syncCustomerRestData() {
		customerRestDatMapper.syncCustomerRestData();
		
	}

	@Override
	public void deleteCustomeRest() {
		customerRestDatMapper.deleteCustomerRest();
		
	}

	@Override
	public  Page<CustomerRestVo> selectCustomerRestList(CustomerRestVo vo) {
		Page<CustomerRestVo> pages = new Page<>();
		pages.setCurrent(vo.getPage());
		pages.setSize(vo.getLimit());
		List<CustomerRestVo> list=customerRestDatMapper.selectCustomerRestList(pages, vo);
		String beginDate=String.valueOf(DateUtil.getYear(vo.getBeginDate()));
		List<CustomerFirstDat> firstDats= customerFirstDatService.selectList(new EntityWrapper<CustomerFirstDat>().eq("company_name", vo.getCompanyName()).eq("period", beginDate));
		//初始化所有有期初余额的单位为flase
		HashMap<CustomerFirstDat,Boolean> resultMap=new HashMap<CustomerFirstDat,Boolean>();
		for(CustomerFirstDat firstDat:firstDats) {
			resultMap.put(firstDat, false);
		}
			
		//如果查询的单位有期初余额，该科目标记为true，剩下的是没有查询出来的记录，但是有期初余额的记录
		for(CustomerRestVo restVo:list) {
			for(CustomerFirstDat firstDat:firstDats) {
				if(restVo.getCustomerCode().equals(firstDat.getCustomerCode())) {
					resultMap.put(firstDat, true);
				}
			}
			
		}
		
		//把剩下的是没有查询记录，但是有期初余额的记录生成CustomerRestVo
		for(Map.Entry<CustomerFirstDat, Boolean> entry : resultMap.entrySet()){
			CustomerFirstDat firstDatKey = entry.getKey();
		    boolean mapValue = entry.getValue();
		    CustomerRestVo temp=null;
		    if(null!=firstDatKey.getFirstAmount()&&Double.valueOf(firstDatKey.getFirstAmount())==0) {
		    	continue;
		    }
	
		    if(mapValue==false) {
		    	 temp = new CustomerRestVo();
		    	 temp.setCompanyName(vo.getCompanyName());
		    	 temp.setCustomerCode(firstDatKey.getCustomerCode());
		    	 temp.setCustomerName(firstDatKey.getCustomerName());
		    	 temp.setFirstAmount(firstDatKey.getFirstAmount());
		    	 temp.setBorrowAmount("0");
		    	 temp.setAlmsAmount("0");
		    	 String direction="";
		    	 if(temp.getCustomerCode().contains("A")) {
		    		 direction=CamsConstant.DirectionEnum.JIE.getValue().toString();
		    	 }else {
		    		 direction=CamsConstant.DirectionEnum.DAI.getValue().toString();
		    	 }
		    	
		    	 String restAmountStr=getRestAmount(temp.getFirstAmount(), temp.getBorrowAmount(), temp.getAlmsAmount(), direction);
		    	 temp.setRestAmount(restAmountStr);
		    	 list.add(temp);
		    }
		}
		 list =list.stream().sorted(Comparator.comparing(CustomerRestVo::getCustomerCode)).collect(Collectors.toList());
		 for(CustomerRestVo customerRestVo:list) {
			 if(customerRestVo.getCustomerCode().contains("A")) {
				 customerRestVo.setSubject("1131"); 
				 customerRestVo.setSubjectName("应收账款");
			 }else {
				 customerRestVo.setSubject("2121"); 
				 customerRestVo.setSubjectName("应付账款");
			 }
		 }
		 List<CustomerRestVo> resultList=new ArrayList();
		 
			//按科目类型分组
			Map<String, List<CustomerRestVo>> subjectList=list.stream().collect(Collectors.groupingBy(CustomerRestVo::getSubject));
			for(Map.Entry<String, List<CustomerRestVo>> entry : subjectList.entrySet()) {
				List<CustomerRestVo> tempList=subjectList.get(entry.getKey());
				CustomerRestVo temp=new CustomerRestVo();
				 Double firstAmount=0.0;
				 Double borrowAmount=0.0;
				 Double almsAmount=0.0;
				 String subject="";
				 String direction="";
				for(CustomerRestVo tempVo:tempList) {
					   if(entry.getKey().equals("2121")) { //如果是资产类型，方向是贷，金额是负数
						   firstAmount=0-Double.valueOf(tempVo.getFirstAmount()==null?"0.0":tempVo.getFirstAmount())+firstAmount;
						   borrowAmount=0-Double.valueOf(tempVo.getBorrowAmount()==null?"0.0":tempVo.getBorrowAmount())+borrowAmount;
						   almsAmount=0-Double.valueOf(tempVo.getAlmsAmount()==null?"0.0":tempVo.getAlmsAmount())+almsAmount; 
					   }else {
						   firstAmount=Double.valueOf(tempVo.getFirstAmount()==null?"0.0":tempVo.getFirstAmount())+firstAmount;
						   borrowAmount=Double.valueOf(tempVo.getBorrowAmount()==null?"0.0":tempVo.getBorrowAmount())+borrowAmount;
						   almsAmount=Double.valueOf(tempVo.getAlmsAmount()==null?"0.0":tempVo.getAlmsAmount())+almsAmount; 
					   }
					   resultList.add(tempVo);
				}
				 if(entry.getKey().equals("2121")) {
					 subject="负债小计";
					 direction=CamsConstant.DirectionEnum.DAI.getValue().toString();
				}else {
					 subject="资产小计";
					 direction=CamsConstant.DirectionEnum.JIE.getValue().toString();
				}
				   temp.setSubject(subject);
				   temp.setCompanyName(vo.getCompanyName());
				   temp.setFirstAmount(firstAmount.toString());
		    	   temp.setBorrowAmount(borrowAmount.toString());
		    	   temp.setAlmsAmount(almsAmount.toString());
		    	   String restAmountStr=getRestAmount(temp.getFirstAmount(), temp.getBorrowAmount(), temp.getAlmsAmount(), direction);
			       temp.setRestAmount(restAmountStr);
			       resultList.add(temp);
			}
		pages.setRecords(resultList);
		return pages;
	}
	
	@SuppressWarnings("unused")
	private String getRestAmount(String firstAmount,String borrowAmount,String almsAmount,String direction) {
		if(null!=direction&&direction.equals(CamsConstant.DirectionEnum.DAI.getValue().toString())) {
		    Double restAmountStr=Double.valueOf(firstAmount)-Double.valueOf(borrowAmount)+Double.valueOf(almsAmount);
		    return restAmountStr.toString();
		}else {
			 Double restAmountStr=Double.valueOf(firstAmount)+Double.valueOf(borrowAmount)-Double.valueOf(almsAmount);
			    return restAmountStr.toString();
		}
		
	}

}
