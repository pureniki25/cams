package com.hongte.alms.core.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.hongte.alms.base.feignClient.UcAppRemote;
import com.hongte.alms.base.service.SysUserPermissionService;
import com.hongte.alms.base.vo.user.SelfBoaInUserInfo;
import com.ht.ussp.core.Result;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/userInfoAndPermissionSynch")
@Api(tags = "UserInfoAndPermissionSynch", description = "同步用户信息和权限")
public class UserInfoAndPermissionSynchController {

	@Autowired
	private UcAppRemote ucAppRemote;

    @Autowired
    @Qualifier("SysUserPermissionService")
    private SysUserPermissionService sysUserPermissionService;
	
    @ApiOperation(value="同步用户信息")
    @PostMapping("/getUserInfoForApp")
    @ResponseBody
    public Result<Integer> getUserInfoForApp(){
    	Result<List<SelfBoaInUserInfo>> usersInfoResult = ucAppRemote.getUserInfoForApp("ALMS");
    	if("0000".equals(usersInfoResult.getReturnCode())) {
    		List<SelfBoaInUserInfo> userInfoList = usersInfoResult.getData();
    		for(SelfBoaInUserInfo userInfo:userInfoList) {
    			sysUserPermissionService.updateUserPermision(userInfo);
    		}
    	}
    	return Result.buildSuccess(0);
    }
	
}
