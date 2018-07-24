package com.hongte.alms.core.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.Date;

import javax.validation.constraints.NotNull;

import com.ht.ussp.bean.LoginUserInfoHelper;
import com.ht.ussp.client.dto.LoginInfoDto;

import org.apache.commons.beanutils.MethodUtils;
import org.springframework.beans.factory.annotation.Autowired;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 公用控制器父类 
 * Created by 张贵宏 on 2018/7/23 11:16
 */
@Slf4j
@Getter
public class BaseController{
    @Autowired
    private LoginUserInfoHelper loginUserInfoHelper;

    /**
     * 设置创建用户信息
     * setCreateUser
     * setCreateTime
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    @NotNull
    protected void setCreateUserInfo(Object bean) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException{
        if(bean == null ) return;
        //final LoginInfoDto loginInfo = loginUserInfoHelper.getLoginInfo();
        //MethodUtils.invokeMethod(bean, "setCreateUser", loginInfo.getUserId());
        MethodUtils.invokeMethod(bean, "setCreateUser", loginUserInfoHelper.getUserId());
        MethodUtils.invokeMethod(bean, "setCreateTime", new Date());
    }

    /**
     * 设置更新用户信息
     * setUpdateUser
     * setUpdateTime
     * @throws InvocationTargetException
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     */
    @NotNull
    protected void setUpdateUserInfo(Object bean) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException{
        if(bean == null ) return;
        // final LoginInfoDto loginInfo = loginUserInfoHelper.getLoginInfo();
        // MethodUtils.invokeMethod(bean, "setUpdateUser", loginInfo.getUserId());
        MethodUtils.invokeMethod(bean, "setUpdateUser", loginUserInfoHelper.getUserId());
        MethodUtils.invokeMethod(bean, "setUpdateTime", new Date());
    }
}