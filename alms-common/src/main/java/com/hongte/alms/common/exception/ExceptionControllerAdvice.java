package com.hongte.alms.common.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * controller 增强器
 *
 * @author sam
 * @since 2017/7/17
 */
@ControllerAdvice
public class ExceptionControllerAdvice {
	private Logger logger = LoggerFactory.getLogger(ExceptionControllerAdvice.class);
    /**
     * 全局异常捕捉处理
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public Map<String,Object > errorHandler(Exception ex) {
    	Map<String,Object >  map = new HashMap<String,Object >();
        map.put("code", -500);
        map.put("msg", "系统异常");
        map.put("data", null);
        logger.error("=========打印日志开始============");
        logger.error(ex.getMessage());
        ex.printStackTrace();
        logger.error("=========打印日志结束============");
        return map;
    }
    
    /**
     * 拦截捕捉自定义异常 MyException.class
     * @param ex
     * @return
     */
    @ResponseBody
    @ExceptionHandler(value = MyException.class)
    public Map<String,Object > myErrorHandler(MyException ex) {
        Map<String,Object > map = new HashMap<String,Object >();
        map.put("code", ex.getCode());
        map.put("msg", ex.getMsg());
        map.put("data", null);
        return map;
    }

}