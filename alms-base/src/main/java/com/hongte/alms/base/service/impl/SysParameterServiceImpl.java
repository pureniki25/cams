package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.SysParameter;
import com.hongte.alms.base.enums.BusinessTypeEnum;
import com.hongte.alms.base.enums.SysParameterStautsEnums;
import com.hongte.alms.base.enums.SysParameterTypeEnums;
import com.hongte.alms.base.mapper.SysParameterMapper;
import com.hongte.alms.base.service.SysParameterService;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * [系统参数表] 服务实现类
 * </p>
 *
 * @author 黄咏康
 * @since 2018-02-01
 */
@Service("SysParameterService")
public class SysParameterServiceImpl extends BaseServiceImpl<SysParameterMapper, SysParameter> implements SysParameterService {

    @Override
    public List<SysParameter> selectByParamType(SysParameterTypeEnums enums){
        return selectList(new EntityWrapper<SysParameter>().eq("param_type", enums.getKey()).orderBy("row_Index"));
    }

    @Override
    public  List<SysParameter> selectActiveByParamType(SysParameterTypeEnums enums){
        return selectList(new EntityWrapper<SysParameter>().eq("param_type", enums.getKey()).eq("status", SysParameterStautsEnums.ACTIVE).orderBy("row_Index"));
    }

    @Override
    public List<SysParameter> selectByPtypeAndPname(SysParameterTypeEnums enums,String paramName){
        return selectList(new EntityWrapper<SysParameter>().eq("param_type", enums.getKey()).eq("status", SysParameterStautsEnums.ACTIVE).eq("param_name",paramName).orderBy("row_Index"));
    }

    @Override
    public SysParameter seleByParamTypeAndvalue(SysParameterTypeEnums enums,String paramVal){
        List<SysParameter> list =  selectList(new EntityWrapper<SysParameter>().eq("param_type", enums.getKey()).orderBy("row_Index").eq("param_value",paramVal));

        if(list.size()>0){
            return  list.get(0);
        }else{
            throw  new RuntimeException("指定类型的系统配置参数值不存在");
        }
    }

    @Override
    public Map<String,SysParameter> selectParameterMap(SysParameterTypeEnums enums){
        List<SysParameter> parameterList =  selectList(new EntityWrapper<SysParameter>().eq("param_type",enums));
        Map<String ,SysParameter> parameterMap = new HashMap<>();
        for(SysParameter parameter: parameterList){
            parameterMap.put(parameter.getParamValue(),parameter);
        }
        return parameterMap;
    }
    
    @Override
    public SysParameter queryRechargeAccountSysParams(String rechargeAccountType) {
    	return super.selectOne(new EntityWrapper<SysParameter>()
				.eq("param_type", BusinessTypeEnum.getRechargeAccountIdByRechargeAccountName(rechargeAccountType))
				.eq("param_type_name", rechargeAccountType).eq("param_name", rechargeAccountType));
    }

}
