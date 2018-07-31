package com.hongte.alms.common.exception;

import com.hongte.alms.common.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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
        logger.error(ex.getMessage(),ex);
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

    /**
     * 处理拦截Validation校验异常信息
     * @param e
     * @return
     */
    @ExceptionHandler(value = ConstraintViolationException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handle(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        for (ConstraintViolation<?> constraintViolation : violations) {
            return Result.error("899",constraintViolation.getMessageTemplate());
        }
        return Result.error("899","输入参数错误");
    }


    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handle(MethodArgumentNotValidException e) {
        final BindingResult result = e.getBindingResult();
        if (result.hasErrors()) {
            for (ObjectError error : result.getAllErrors()) {

                final String defaultMessage = error.getDefaultMessage();

                return Result.error("899",defaultMessage);

            }
        }
        return Result.error("899","参数错误");
    }



    @ExceptionHandler(value = HttpMessageConversionException.class)
    @ResponseBody
    public Result handle(HttpServletRequest req, HttpMessageConversionException e) {

//        logger.error("DaoException", e);
//        return ResultHelper.buildFail(ReturnCodeEnum.EXCEPTION, e.getMsg());
//
//        ResultBean resultBean = new ResultBean();
//        resultBean.setSuccess(false);
//        resultBean.setType(ResultType.MESSAGE_CONVERT_EXCEPTION.getType());
//        //resultBean.setMessageBeans(MessageBeanHelper.buildForOne(new MessageBean(e.getMessage())));
//        resultBean.setMessageBeans(MessageBeanHelper.buildForOne(new MessageBean(ResultType.MESSAGE_CONVERT_EXCEPTION.getDescript())));
//        return resultBean;

        return null;
    }

}