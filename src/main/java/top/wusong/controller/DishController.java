package top.wusong.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.api.R;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.wusong.common.Result;
import top.wusong.domain.Category;
import top.wusong.domain.Dish;
import top.wusong.domain.Setmeal;
import top.wusong.dto.DishFlavorDto;
import top.wusong.service.CategoryService;
import top.wusong.service.DIshService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DIshService dIshService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 分页查询
     *
     * @param page     当前页
     * @param pageSize 每页展示的数量
     * @param name     模糊查询的名称
     * @return 分页结果
     */
    @GetMapping("/page")
    public Result<Page<DishFlavorDto>> getAllByPage(Integer page, Integer pageSize, String name) {
        //构建查询条件
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //判断是否条件成立
        if (name != null) {
            lambdaQueryWrapper.like(Dish::getName, name);
        }
        //根据更新时间来排序
        lambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
        //创建分页
        Page<Dish> pages = new Page<>(page, pageSize);
        dIshService.page(pages, lambdaQueryWrapper);
        //新建一个能放分类名称的分页对象
        Page<DishFlavorDto> dtoPage = new Page<>();
        //将上面查询的数据复制给下面的 排除records这个字段，需要单独处理，因为这个就是查询的结果集合
        BeanUtils.copyProperties(pages, dtoPage, "records");
        List<Dish> records = pages.getRecords();
        //重新构造一下，需要从中获取菜品id，查询出菜品后，然后更换一下泛型，使用一个能接收菜品名称的实体类
        List<DishFlavorDto> dishFlavorDtos = records.stream().map(dish -> {
            //新建一个实体类对象
            DishFlavorDto dto = new DishFlavorDto();
            //获取菜品的id
            Long categoryId = dish.getCategoryId();
            //查询菜品对象
            Category category = categoryService.getById(categoryId);
            //把剩余的dish和dto剩余的参数化复制一下
            BeanUtils.copyProperties(dish, dto);
            //设置参数
            if (category != null){
            dto.setCategoryName(category.getName());
            }
            //返回新的泛型
            return dto;
            //重新变成集合
        }).collect(Collectors.toList());
        //把修改后的集合重新设置给对象
        dtoPage.setRecords(dishFlavorDtos);
        return Result.success(dtoPage);
    }

    /**
     * 添加菜品
     *
     * @param dto 菜品和口味的对象
     * @return Result<String> 添加成功的消息
     */
    @PostMapping
    public Result<String> insertDish(@RequestBody DishFlavorDto dto) {
        System.out.println("dto:" + dto);
        //需要调用两个保存方法
        dIshService.insert(dto);
        return Result.success("添加成功！");
    }

}
