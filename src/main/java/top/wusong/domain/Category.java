package top.wusong.domain;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;

@Data
public class Category {
    @TableId//代表该字段为主键
    @JsonFormat(shape = JsonFormat.Shape.STRING)//自动将值变成字符串
    private Long id;

    //类型   1 菜品分类 2 套餐分类
    private Integer Type;

    //分类名称
    private String name;

    //顺序
    private int sort;

    //创建时间
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime create_time;

    //更新时间
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime update_time;

    //创建人
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long create_user;

    //修改人
    @TableField(fill = FieldFill.UPDATE)
    private Long update_user;

}
