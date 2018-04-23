package com.hongte.alms.base.service.impl;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.dto.UserPermissionBusinessDto;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.SysParameter;
import com.hongte.alms.base.enums.SysParameterTypeEnums;
import com.hongte.alms.base.mapper.BasicBusinessMapper;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.base.service.SysParameterService;
import com.hongte.alms.base.vo.module.BusinessInfoForApplyDerateVo;
import com.hongte.alms.common.service.impl.BaseServiceImpl;

/**
 * <p>
 * 基础业务信息表 服务实现类
 * </p>
 *
 * @author 曾坤
 * @since 2018-02-02
 */
@Service("BasicBusinessService")
public class BasicBusinessServiceImpl extends BaseServiceImpl<BasicBusinessMapper, BasicBusiness> implements BasicBusinessService {

    @Autowired
    BasicBusinessMapper basicBusinessMapper;

    @Autowired
    @Qualifier("SysParameterService")
    SysParameterService sysParameterService;




    public List<BusinessInfoForApplyDerateVo> selectBusinessInfoForApplyDerateVo(String crpId){
        List<BusinessInfoForApplyDerateVo> List  =  basicBusinessMapper.selectBusinessInfoForApplyDerateVo(crpId);
        
       
        //利息类型列表
        List<SysParameter> borrowRateUnitlist =  sysParameterService.selectList(new EntityWrapper<SysParameter>().eq("param_type", SysParameterTypeEnums.BORROW_RATE_UNIT.getKey()).orderBy("row_Index"));

        Map<String,SysParameter> sysParameterMap = sysParameterService.selectParameterMap(SysParameterTypeEnums.BORROW_RATE_UNIT);
        for(BusinessInfoForApplyDerateVo vo : List){
        	Double principal = basicBusinessMapper.queryPayedPrincipal(vo.getBusinessId()) ; 
        	vo.setPayedPrincipal(BigDecimal.valueOf(principal==null?0:principal));

            SysParameter  parameter =  sysParameterMap.get(vo.getRepaymentTypeId());
            if(parameter!=null){
                vo.setRepaymentTypeName(parameter.getParamValue());
            }else{
                vo.setRepaymentTypeName(vo.getRepaymentTypeId().toString());
            }

            if(vo.getBorrowRate()!=null){
                vo.setBorrowRateStr(String.format("%.2f",vo.getBorrowRate())+"%");
            }
            for(SysParameter rateUnit: borrowRateUnitlist){
                if(vo.getBorrowRateUnit().toString().equals(rateUnit.getParamValue())){
                    vo.setBorrowRateName(rateUnit.getParamName());
                }
            }
            //剩余本金
            if(vo.getPayedPrincipal()==null){
                if(vo.getGetMoney()!=null){
                    vo.setRemianderPrincipal(vo.getGetMoney());
                }else{
                    vo.setRemianderPrincipal(new BigDecimal(0.00));
                }
            }else{
                if(vo.getGetMoney()!=null){
                    vo.setRemianderPrincipal(vo.getGetMoney().subtract(vo.getPayedPrincipal()));
                }else{
                    vo.setRemianderPrincipal(new BigDecimal(0.00));
                }
            }

        }

        return List;

    }

    public BusinessInfoForApplyDerateVo selectBusinessInfoForApplyDerateVoOne(String crpId){
        List<BusinessInfoForApplyDerateVo> List  =selectBusinessInfoForApplyDerateVo(crpId);


        if(List.size()>0){
            return List.get(0);
        }
        return new BusinessInfoForApplyDerateVo();
    }

    @Override
    public List<UserPermissionBusinessDto> selectUserPermissionBusinessDtos(List<String> companyIds){
         return basicBusinessMapper.selectUserPermissionBusinessDtos(companyIds);
    }

    @Override
    public List<String> selectCompanysBusinessIds(List<String> companyIds){
        List<UserPermissionBusinessDto> bList = new LinkedList<>();
        if(companyIds.size()>0){
            bList = selectUserPermissionBusinessDtos(companyIds);
        }
        List<String> sList = new LinkedList<String>();

        for(UserPermissionBusinessDto dto: bList){
            sList.add(dto.getBusinessId());
        }
        return sList;
    }

	/**
	 * 	结清最终缴纳的金额 
	 * @param original_business_id
	 * @return
	 */
	@Override
	 public Double getSettleTotalFactSum(@Param("original_business_id") String original_business_id) {
		 
		 return basicBusinessMapper.getSettleTotalFactSum(original_business_id);
	 }

    
    /**
     * 一次性收取的分公司费用+期初收取的月收分公司服务费+平台费+担保费
     * @param original_business_id
     * @return
     */
	@Override
	public BigDecimal getPreChargeAndPreFees(String original_business_id) {
		BigDecimal preCharge=BigDecimal.valueOf(basicBusinessMapper.getPreCharge(original_business_id)==null?0:basicBusinessMapper.getPreCharge(original_business_id));
		BigDecimal preFees=BigDecimal.valueOf(basicBusinessMapper.getPreFees(original_business_id)==null?0:basicBusinessMapper.getPreFees(original_business_id));
		BigDecimal sum=preCharge.add(preFees);
		return sum;
	}
//
//    Public BusinessInfoForApplyDerateVo selectBusinessInfoForApplyDerateVo(String crpId){
//
//        return null;
//    }

	@Override
	public Double getMonthSumFactAmount(String original_business_id) {
		 return basicBusinessMapper.getMonthSumFactAmount(original_business_id);
	}

//    Public List<BusinessInfoForApplyDerateVo> selectBusinessInfoForApplyDerateVo(String crpId){
//       return  basicBusinessMapper.selectBusinessInfoForApplyDerateVo(crpId);
//    }


}
