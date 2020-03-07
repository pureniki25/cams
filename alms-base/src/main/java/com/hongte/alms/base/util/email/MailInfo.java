package com.hongte.alms.base.util.email;

import java.io.File;
import java.util.List;
import java.util.Properties;

import lombok.Data;

/**
 * 发送邮件需要使用的基本信息
 * @author huweiqian
 *
 */
@Data
public class MailInfo {
	/**
	 * 发送邮件的服务器的地址
	 */
	private String mailServerHost;
	/**
	 * 发送邮件的服务器的端口
	 */
	private String mailServerPort;
	/**
	 * 邮件发送者的地址
	 */
	private String fromAddress;
	/**
	 * 邮件接收者的地址
	 */
	private String toAddress;
	/**
	 * 登陆邮件发送服务器的用户名
	 */
	private String userName;
	/**
	 * 登陆邮件发送服务器的密码
	 */
	private String password;
	/**
	 * 是否需要身份验证
	 */
	private boolean isAuth = true;
	/**
	 * 邮件主题
	 */
	private String subject;
	/**
	 * 邮件的文本内容格式(HTML格式：text/html，纯文本格式：text/plain)
	 */
	private String contentType = "text/html";
	/**
	 * 邮件的文本内容
	 */
	private String content;
	/**
	 * 邮件的附件
	 */
	private List<File> attachments;

	/**
	 * 获得邮件会话属性
	 */
	public Properties getProperties() {
		Properties properties = new Properties();
		
		properties.put("mail.smtp.host", this.mailServerHost);
		properties.put("mail.smtp.port", this.mailServerPort);
		properties.put("mail.smtp.auth", isAuth ? "true" : "false");
		
		// 开启debug调试
		properties.setProperty("mail.debug", "true");
		
		return properties;
	}
}
