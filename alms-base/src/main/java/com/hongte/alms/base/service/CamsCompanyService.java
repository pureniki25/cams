package com.hongte.alms.base.service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hongte.alms.base.entity.CamsCompany;
import com.hongte.alms.base.entity.LoginUser;
import com.hongte.alms.base.enums.TokenTypeEnum;
import com.hongte.alms.common.result.Result;
import com.hongte.alms.common.service.BaseService;
import com.hongte.alms.common.util.TokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 公司表 服务类
 * </p>
 *
 * @author wjg
 * @since 2019-01-16
 */
public interface CamsCompanyService extends BaseService<CamsCompany> {
      Result<String> getCompany(HttpServletRequest request,TokenTypeEnum tokenTypeEnum);
}
