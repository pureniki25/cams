package com.hongte.alms.core.service.impl;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.hongte.alms.base.service.SysUserPermissionService;
import com.hongte.alms.base.vo.user.SelfBoaInUserInfo;

/**
 * @ClassName: UCMQListener  
 * @Description: UC用户操作监听
 * @author liuzq  
 * @date 2018年7月13日    
 */
@Component
public class UCMQListener {

	public final String UC_NODELAY = "uc.nodelay";//非延迟
	
	public final String UC_ADD_USER = "addAppUser";//添加用戶
	public final String UC_DEL_USER = "delAppUser";//刪除app用戶
	public final String UC_UPDATE_ORG = "updateUserOrg";//更新用户机构
	public final String UC_ADD_ROLE = "addUserRole";//添加用户角色
	public final String UC_DEL_ROLE = "delUserRole";//删除用户角色
	
    private static Logger logger = LoggerFactory.getLogger(UCMQListener.class);
    
    @Autowired
    @Qualifier("SysUserPermissionService")
    private SysUserPermissionService sysUserPermissionService;

    /**
     * @Title: processMsgService  
     * @Description: 即时消息处理 AmqpConfig.MSG_NODELAY队列，监控消息队列
     * @param     参数  mq返回的消息
     * @return void    返回类型  
     */
    @RabbitListener(queues = UC_NODELAY)
    @RabbitHandler
    public void userInfoAndPermissionSynch(Message message) {
		String text = "";
		try {
			text = new String(message.getBody(),"UTF-8");
		} catch (UnsupportedEncodingException e) {
			text = new String(message.getBody());
			logger.error("发送对象编码错误",e);
		}
		JSONObject jsonObject=JSONObject.parseObject(text);
		if(jsonObject.containsKey(UC_ADD_USER)) {
			addAppUser(jsonObject);
		}
		
		if(jsonObject.containsKey(UC_DEL_USER)) {
			delAppUser(jsonObject);
		}
		
		if(jsonObject.containsKey(UC_UPDATE_ORG)) {
			updateUserOrg(jsonObject);
		}
		
		if(jsonObject.containsKey(UC_ADD_ROLE)) {
			addUserRole(jsonObject);
		}
		
		if(jsonObject.containsKey(UC_DEL_ROLE)) {
			delUserRole(jsonObject);
		}
		
    }

	private void addAppUser(JSONObject jsonObject) {
		JSONObject addInfoObj = jsonObject.getJSONObject("addAppUser");
//		SelfBoaInUserInfo userInfo = new SelfBoaInUserInfo();
//		userInfo.setUserId(addInfoObj.getString("userId"));
		sysUserPermissionService.addAppUser(addInfoObj.getString("userId"));
	}
    
	private void delAppUser(JSONObject jsonObject) {
		JSONObject delInfoObj = jsonObject.getJSONObject("delAppUser");
		sysUserPermissionService.delAppUser(delInfoObj.getString("userId"));
	}
	
	private void updateUserOrg(JSONObject jsonObject) {
		JSONObject delInfoObj = jsonObject.getJSONObject("updateUserOrg");
		sysUserPermissionService.updateUserOrg(delInfoObj.getString("userId"));
	}

	private void addUserRole(JSONObject jsonObject) {
		JSONObject addInfoObj = jsonObject.getJSONObject("addUserRole");
		sysUserPermissionService.addUserRole(addInfoObj.getString("userId"));
	}
 
	private void delUserRole(JSONObject jsonObject) {
		JSONObject delInfoObj = jsonObject.getJSONObject("delUserRole");
		sysUserPermissionService.delUserRole(delInfoObj.getString("userId"));
	}


}
