package com.imooc.bilibli.handler;

import com.imooc.bilibli.domain.JsonResponse;
import com.imooc.bilibli.domain.exception.ConditionException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * 通用全局异常处理
 */
@ControllerAdvice
//提高bean的执行顺序的优先级，默认是最低优先级,值越小优先级越高
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CommonGlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public JsonResponse<String> commonExceptionHandler(HttpServletRequest request, Exception e) {
        String errorMsg = e.getMessage();
        if(e instanceof ConditionException) {
            String errorCode = ((ConditionException)e).getCode();
            return  new JsonResponse<>(errorCode, errorMsg);
        }
        return new JsonResponse<>("code", errorMsg);
    }
}
