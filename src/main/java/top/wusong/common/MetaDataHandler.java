package top.wusong.common;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

@Component
public class MetaDataHandler implements MetaObjectHandler {
    //使用次方法进入注入获取到session从中获取id容易造成线程冲突
    @Autowired
    private HttpServletRequest httpServletRequest;

    /**
     * 新增时自动增加
     * @param metaObject
     */
    @Override
    public void insertFill(MetaObject metaObject) {
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("createTime", LocalDateTime.now());
        //metaObject.setValue("createUser", httpServletRequest.getSession().getAttribute("id"));
        //metaObject.setValue("updateUser", httpServletRequest.getSession().getAttribute("id"));
        metaObject.setValue("updateUser", BaseContext.getEmployeeId());
        metaObject.setValue("updateUser", BaseContext.getEmployeeId());
    }


    /**
     * 更新时自动增加
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        metaObject.setValue("updateTime", LocalDateTime.now());
        metaObject.setValue("updateUser", BaseContext.getEmployeeId());
    }
}
