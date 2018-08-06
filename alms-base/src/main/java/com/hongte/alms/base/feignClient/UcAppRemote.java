package com.hongte.alms.base.feignClient;


import java.util.List;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.hongte.alms.base.vo.user.BoaInRoleInfo;
import com.hongte.alms.base.vo.user.SelfBoaInUserInfo;
import com.ht.ussp.core.Result;


/**
 * @ClassName: UcAppRemote  
 * @Description: UC接口调用
 * @author liuzq  
 * @date 2018年7月12日    
 */
@FeignClient(value = "ussp-uc-app")
public interface UcAppRemote {

	@RequestMapping(value = "/datas/getUserInfoForApp", headers = { "app=ALMS",
			"content-type=application/json" }, method = RequestMethod.POST)
	Result<List<SelfBoaInUserInfo>> getUserInfoForApp(@RequestParam(value="appCode") String appCode);
	
	@RequestMapping(value = "/userrole/getUserRole", headers = { "app=ALMS",
	"content-type=application/json" }, method = RequestMethod.GET)
	List<BoaInRoleInfo> getUserRole(@RequestParam(value="userId") String userId);
	
}
