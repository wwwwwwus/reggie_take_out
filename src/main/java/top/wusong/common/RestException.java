package top.wusong.common;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.wusong.common.Result;
import top.wusong.exception.BusinessException;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
@Component
public class RestException {

    //自定义处理业务异常的方法
    @ExceptionHandler(BusinessException.class)
    public Result<String> doBusinessException(BusinessException businessException){
        System.out.println("businessException:"+businessException.getMsg());
        return Result.error(businessException.getMsg());
    }

    //处理重复添加的错误
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<String> doSQLIntegrityConstraintViolationException(SQLIntegrityConstraintViolationException ex){
       //获取错误信息
        String message = ex.getMessage();
        //判断是否该错误重复添加错误
        if (message.contains("Duplicate entry")){
            //截取字符数组
            String[] s = message.split(" ");
             message = s[2];
             return Result.error(message + "已存在！");
        }
        return Result.error("未知错误！");
    }
}
