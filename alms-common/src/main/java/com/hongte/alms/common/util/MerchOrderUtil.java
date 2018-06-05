package com.hongte.alms.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

/**
 * 
 * @author czs
 *  生成代扣订单商户号
 *
 */
public class MerchOrderUtil {
	public static String getMerchOrderId() {
		  // 取系统当前时间作为订单号变量前半部分，精确到毫秒  
        String nowLong = String.valueOf(Long.parseLong(new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date())));
        Random rnd = new Random();
        int num =  RandomUtil.generateRandomInt(1000, 9999);
        nowLong=nowLong+num;
		return nowLong;  
	} 

}
