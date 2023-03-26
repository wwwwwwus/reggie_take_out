package top.wusong.domain;


import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.Version;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class Employee {

    private static final long serialVersionUID = 1L;

    //此注解为响应给前端时，把该类型转换为字符串格式作用就是防止long类型数据传输给前端，而前端会精度丢失
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long id;

    private String username;

    private String name;

    private String password;

    private String phone;

    private String sex;

    private String idNumber;

    private Integer status;


    //新增时自动更新
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    //更新和新增时自动更新
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

}
