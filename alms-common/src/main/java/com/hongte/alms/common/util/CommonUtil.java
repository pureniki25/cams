package com.hongte.alms.common.util;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class CommonUtil {

	private CommonUtil() {}
	
	/**
	 * 获取客户端地址 
	 * @return clientIp 客户端地址
	 */
	public static String getClientIp() {
		String clientIp = "";
		ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
				.getRequestAttributes();
		HttpServletRequest request = requestAttributes.getRequest();

		if (request.getHeader("x-forwarded-for") == null) {
			clientIp = request.getRemoteAddr();
		} else {
			clientIp = request.getHeader("x-forwarded-for");
		}
		return clientIp;
	}
}
