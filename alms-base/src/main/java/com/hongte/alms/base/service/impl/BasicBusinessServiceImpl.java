package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.dto.UserPermissionBusinessDto;
import com.hongte.alms.base.vo.module.BusinessInfoForApplyDerateVo;
import com.hongte.alms.base.entity.BasicBusiness;
import com.hongte.alms.base.entity.SysParameter;
import com.hongte.alms.base.enums.SysParameterTypeEnums;
import com.hongte.alms.base.mapper.BasicBusinessMapper;
import com.hongte.alms.base.service.BasicBusinessService;
import com.hongte.alms.base.service.SysParameterService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

        //申请类型列表
        List<SysParameter> borrowRateUnitlist =  sysParameterService.selectList(new EntityWrapper<SysParameter>().eq("param_type", SysParameterTypeEnums.BORROW_RATE_UNIT.getKey()).orderBy("row_Index"));

        Map<String,SysParameter> sysParameterMap = sysParameterService.selectParameterMap(SysParameterTypeEnums.BORROW_RATE_UNIT);
        for(BusinessInfoForApplyDerateVo vo : List){

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




//
//    Public BusinessInfoForApplyDerateVo selectBusinessInfoForApplyDerateVo(String crpId){
//
//        return null;
//    }

//    Public List<BusinessInfoForApplyDerateVo> selectBusinessInfoForApplyDerateVo(String crpId){
//       return  basicBusinessMapper.selectBusinessInfoForApplyDerateVo(crpId);
//    }


}
