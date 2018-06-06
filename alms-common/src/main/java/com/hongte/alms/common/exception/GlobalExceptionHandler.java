package com.hongte.alms.common.exception;



import com.hongte.alms.common.result.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.Set;


/**
 * @author SIVEN
 * @ClassName: GlobalExceptionHandler
 * @Description: 全局的异常处理类，用于处理所有的异常信息
 * @date 2018年5月21日 下午2:46:39
 */
//@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    /**
     * 处理拦截Validation校验异常信息
     * @param e
     * @return
     */
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Result handle(Exception e) {

        logger.error("error:",e);
        return null;

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

//    @ExceptionHandler(value = MethodArgumentNotValidException.class)
//    @ResponseBody
//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    public Result handle(MethodArgumentNotValidException e) {
//        final BindingResult result = e.getBindingResult();
//        if (result.hasErrors()) {
//            for (ObjectError error : result.getAllErrors()) {
//
//                final String defaultMessage = error.getDefaultMessage();
//                return ResultHelper.buildFail(ReturnCodeEnum.ERROR_PARAM,
//                        defaultMessage
//                );
//
//            }
//        }
//        return ResultHelper.buildFail(ReturnCodeEnum.ERROR_PARAM, "参数错误");
//    }



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