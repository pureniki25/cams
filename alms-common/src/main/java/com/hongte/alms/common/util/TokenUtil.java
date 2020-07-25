package com.hongte.alms.common.util;

import com.hongte.alms.common.result.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.text.NumberFormat;
import java.util.Set;

/**
 * token工具类
 *
 * @author czs
 */
public class TokenUtil {

    public static String getToekn(HttpServletRequest request) {
        String author = request.getHeader("Authorization");
        String arry[] = author.split("Bearer ");
        if (arry.length == 2) {
            return arry[1];
        } else {
            return null;
        }
    }
    public static String getToeknByCookies(HttpServletRequest request) {
       Cookie[] cookies= request.getCookies();
        if (cookies.length >0 ) {
            String token=cookies[0].getValue();
            return token;
        } else {
            return null;
        }
    }
}
