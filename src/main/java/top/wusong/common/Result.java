package top.wusong.common;

import lombok.Data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


@Data
public class Result<T> implements Serializable {

    //状态码
    private Integer code;
    //响应消息
    private String msg;
    //响应数据
    private T data;
    //动态消息
    private Map map = new HashMap();

    //返回一个成功状态的对象
    public static <T> Result<T> success(T object){
        Result<T> result = new Result<T>();
        result.code = 1;
        result.data = object;
        return  result;
    }

    //返回一个失败状态的对象
    public static <T> Result<T> error(String msg){
        Result<T> result = new Result<T>();
        result.code = 0;
        result.msg = msg;
        return  result;
    }
}
