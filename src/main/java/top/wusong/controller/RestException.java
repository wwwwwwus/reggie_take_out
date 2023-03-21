package top.wusong.controller;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.wusong.common.Result;
import top.wusong.exception.BusinessException;

@RestControllerAdvice
@Component
public class RestException {

    @ExceptionHandler(BusinessException.class)
    public Result<String> doBusinessException(BusinessException businessException){
        System.out.println("businessException:"+businessException.getMsg());
        return Result.error(businessException.getMsg());
    }
}
