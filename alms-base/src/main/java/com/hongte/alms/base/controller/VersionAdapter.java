package com.hongte.alms.base.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hongte.alms.common.result.Result;

/**
 * 用于上线后排除版本问题,每次各自的服务修改代码后,请务必修改此文件<br>
 * 将对应服务的version改为开发者能识别新版本的字符
 * @author 王继光
 * 2018年8月27日 上午10:04:40
 */
public class VersionAdapter {
	/*Aservice="version"*/
	/*Bservice="abc"*/
	/*Cservice="2018-08-27 08:45:00"*/
	/*Dservice="1"*/
	final String ALMS_CORE_SERVICE = "(finance)1018 18:10" ;
	final String ALMS_FINANCE_SERVICE = "(finance)1019 14:42 实还记录操作人员" ;
	final String ALMS_OPEN_SERVICE = "0913" ;
	final String ALMS_PLATREPAY_SERVICE = "(finance)1018 18:10" ;
	final String ALMS_SCHEDULED_SERVICE = "(finance)1018 18:10" ;

	final String ALMS_WEBUI = "(finance)1018 18:10" ; 
	final String ALMS_WITHHOLD_SERVICE = "(finance)0926 22:16" ;
	
	@RequestMapping("/version")
	public Result version() {
		JSONObject v = new JSONObject() ;
		v.put("ALMS_CORE_SERVICE", ALMS_CORE_SERVICE);
		v.put("ALMS_FINANCE_SERVICE", ALMS_FINANCE_SERVICE);
		v.put("ALMS_OPEN_SERVICE", ALMS_OPEN_SERVICE);
		v.put("ALMS_PLATREPAY_SERVICE", ALMS_PLATREPAY_SERVICE);
		v.put("ALMS_SCHEDULED_SERVICE", ALMS_SCHEDULED_SERVICE);
		v.put("ALMS_WEBUI", ALMS_WEBUI);
		v.put("ALMS_WITHHOLD_SERVICE", ALMS_WITHHOLD_SERVICE);
		return Result.success(v);
	}
	
}
