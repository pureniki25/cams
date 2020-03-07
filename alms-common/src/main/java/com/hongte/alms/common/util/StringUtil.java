package com.hongte.alms.common.util;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class StringUtil {
	private StringUtil() {
	}
	
	/**
	 * 判断字符串是否为数字
	 * @param str
	 * @return
	 */
	public static boolean isInteger(String str) {
		if (notEmpty(str)) {
			Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
			return pattern.matcher(str).matches();
		}
		return false;
	}

	/**
	 * 判断一个字符串是否为 null 或 空串 或 空白
	 * 
	 * @param source
	 *            需要判断的字符串
	 * @return 当字符串为 null 或 为 空白、空串 时返回 true
	 */
	public static boolean isEmpty(String source) {
		return source == null || source.trim().isEmpty();
	}

	/**
	 * 判断一个字符串是否不是null且不是空串、不是空白
	 * 
	 * @param source
	 *            需要判断的字符串
	 * @return 当 字符串是不是null且不是空串也不是空白时返回 true
	 */
	public static boolean notEmpty(String source) {
		return source != null && source.trim().length() > 0;
	}

	/**
	 * 比较两个非空(不是null，不是空串、不是空白)字符串是否"相等"
	 * 
	 * @param one
	 *            第一个需要比较的字符串
	 * @param theOther
	 *            另一个参与比较的字符串
	 * @return 当 两个字符串 都不为空串 且 内容完全一致 (剔除首尾空白后、大小写也一致)时返回 true
	 */
	public static boolean equals(String one, String theOther) {
		return equals(one, theOther, true, false);
	}

	/**
	 * 比较两个字符串是否 "相等"
	 * 
	 * @param one
	 *            参与比较的第一个字符串
	 * @param theOther
	 *            参与比较的另一个字符串
	 * @param escapeSpace
	 *            是否需要剔除首尾空白 ( true 表示需要剔除首尾空白，false 表示不剔除 )
	 * @param ignoreCase
	 *            是否忽略大小写 ( true 表示忽略大小写 ，false 表示不忽略大小写 )
	 * @return
	 */
	public static boolean equals(String one, String theOther, boolean escapeSpace, boolean ignoreCase) {

		if (one == null || theOther == null) {
			return false;
		}

		if (escapeSpace) {
			one = one.trim();
			theOther = theOther.trim();
		}

		return ignoreCase ? one.equalsIgnoreCase(theOther) : one.equals(theOther);
	}

	/**
	 * 随机生成一个 32 位长度的 字符串( UUID )
	 * 
	 * @return
	 */
	public static String random() {
		UUID uuid = UUID.randomUUID();// 36位长度(包含了 四个 - )
		String uuidString = uuid.toString();
		uuidString = uuidString.replace("-", "");
		uuidString = uuidString.toUpperCase();
		return uuidString;
	}

	/**
	 * 检测邮箱合法性
	 * 
	 * @param email
	 * @return
	 */
	public static boolean isEmail(String email) {
		if ((email == null) || (email.trim().length() == 0)) {
			return false;
		}
		String regEx = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(email.trim().toLowerCase());

		return m.find();
	}

	/**
	 * 检测手机合法性
	 */
	public static boolean isTelephone(String phone)
	{
		if ((phone == null) || (phone.length() == 0)) {
			return false;
		}
		String regEx = "^1\\d{10}$";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(phone.toLowerCase());
		return m.find();
	}


	/**
	 * 检测身份证号合法性
	 */
	public static boolean isIdCard(String idCard)
	{
		if ((idCard == null) || (idCard.length() == 0)) {
			return false;
		}
		String regEx = "^[1-9]\\d{5}(18|19|([23]\\d))\\d{2}((0[1-9])|(10|11|12))(([0-2][1-9])|10|20|30|31)\\d{3}[0-9Xx]$";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(idCard.toLowerCase());
		return m.find();
	}

	/**
	 * Double进行四舍五入
	 * 
	 * @param v
	 * @param scale
	 * @return
	 */
	public static double getDouble(Double v, int scale) {

		if (scale < 0) {
			scale = 0;
		}
		BigDecimal b = new BigDecimal(v);
		BigDecimal one = new BigDecimal("1");
		return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	/**
	 * 根据Unicode编码完美的判断中文汉字和符号
	 * 
	 * @param c
	 * @return
	 */
	private static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		return (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION);
	}

	/**
	 * 判断是否包含中文汉字
	 * 
	 * @param strName
	 * @return
	 */
	public static boolean isChineseHave(String strName) {
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (isChinese(c)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 判断是否只有中文汉字
	 * 
	 * @param strName
	 * @return
	 */
	public static boolean isChineseAll(String strName) {
		char[] ch = strName.toCharArray();
		for (int i = 0; i < ch.length; i++) {
			char c = ch[i];
			if (!isChinese(c)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * 判断邮政编码
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isCard(String str) {
		Pattern p = Pattern.compile("[1-9]\\d{5}(?!\\d)");
		Matcher m = p.matcher(str);
		return m.matches();
	}
	
	public static String nullToStr(String str) {
		return str == null ? "" : str;
	}

	/**
	* 判断字符串是否可以转化为json对象
	* @param content
	* @return
	*/
	public static boolean isJsonObject(String content) {
	    // 此处应该注意，当content为"  "空格字符串时，JSONObject.parseObject可以解析成功，
	    if(!notEmpty(content)) {
	    	return false;
	    }
	    try {
	        JSONObject.parseObject(content);
	        return true;
	    } catch (Exception e) {
	        return false;
	    }
	}
	
	/**
	* 判断字符串是否可以转化为JSON数组
	* @param content
	* @return
	*/
	public static boolean isJsonArray(String content) {
		if(!notEmpty(content)) {
	    	return false;
	    }
	    try {
	        JSONArray.parseArray(content);
	        return true;
	    } catch (Exception e) {
	        return false;
	    }
	}
	
	/**
	 * 中文标点符号转英文字标点符号
	 * 
	 * @param str
	 *            原字符串
	 * @return str 新字符串
	 */
	public static final String cToe(String str) {
		String[] regs = { "！", "，", "。", "；", "~", "《", "》", "（", "）", "？",
				"”", "｛", "｝", "“", "：", "【", "】", "”", "‘", "’", "!", ",",
				".", ";", "`", "<", ">", "(", ")", "?", "'", "{", "}", "\"",
				":", "{", "}", "\"", "\'", "\'" };
		for (int i = 0; i < regs.length / 2; i++) {
			str = str.replaceAll(regs[i], regs[i + regs.length / 2]);
		}
		return str;
	}
 

 
	/**
	 * 全角转半角:
	 * 
	 * @param fullStr
	 * @return
	 */
	public static final String full2Half(String fullStr) {
		if (isEmpty(fullStr)) {
			return fullStr;
		}
		char[] c = fullStr.toCharArray();
		for (int i = 0; i < c.length; i++) {
			System.out.println((int) c[i]);
			if (c[i] >= 65281 && c[i] <= 65374) {
				c[i] = (char) (c[i] - 65248);
			} else if (c[i] == 12288) { // 空格
				c[i] = (char) 32;
			}
		}
		return new String(c);
	}
 
	/**
	 * 半角转全角
	 * 
	 * @param halfStr
	 * @return
	 */
	public static final String half2Full(String halfStr) {
		if (isEmpty(halfStr)) {
			return halfStr;
		}
		char[] c = halfStr.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == 32) {
				c[i] = (char) 12288;
			} else if (c[i] < 127) {
				c[i] = (char) (c[i] + 65248);
			}
		}
		return new String(c);
	}


}
