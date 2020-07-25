package com.hongte.alms.base.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.CamsCompany;
import com.hongte.alms.base.entity.LoginUser;
import com.hongte.alms.base.enums.TokenTypeEnum;
import com.hongte.alms.base.mapper.CamsCompanyMapper;
import com.hongte.alms.base.service.CamsCompanyService;
import com.hongte.alms.base.service.LoginUserService;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.service.impl.BaseServiceImpl;
import com.hongte.alms.common.util.StringUtil;
import com.hongte.alms.common.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 公司表 服务实现类
 * </p>
 *
 * @author wjg
 * @since 2019-01-16
 */
@Service("CamsCompanyService")
public class CamsCompanyServiceImpl extends BaseServiceImpl<CamsCompanyMapper, CamsCompany> implements CamsCompanyService {
    @Autowired
    @Qualifier("LoginUserService")
    private LoginUserService loginUserService;

    public  Result<String> getCompany(HttpServletRequest request, TokenTypeEnum tokenTypeEnum) {
        String token= "";
        if(tokenTypeEnum==tokenTypeEnum.COOKIES){
            token=TokenUtil.getToeknByCookies(request);
        }else{
            token=TokenUtil.getToekn(request);
        }
        LoginUser loginUser = loginUserService
                .selectOne(new EntityWrapper<LoginUser>().eq("token", token));
        if (loginUser != null) {
            if(StringUtil.isEmpty(loginUser.getCompanyName())){
                return Result.error("请设置公司");
            }else{
                return Result.success(loginUser.getCompanyName());
            }
        } else {
            return Result.error("找不到该用户token");
        }
    }


}
