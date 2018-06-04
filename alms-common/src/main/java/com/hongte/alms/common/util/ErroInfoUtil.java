package com.hongte.alms.common.util;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

/**
 * @author zengkun
 * @since 2018/5/31
 */
public class ErroInfoUtil {

    public static String getErroeInfo(BindingResult bindingResult){
        StringBuilder retErrMsg = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            retErrMsg.append("   "+ fieldError.getDefaultMessage());
        }
        return retErrMsg.toString();
    }


}
