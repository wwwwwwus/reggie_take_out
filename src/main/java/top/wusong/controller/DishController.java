package top.wusong.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.wusong.common.Result;
import top.wusong.domain.Category;
import top.wusong.domain.Dish;
import top.wusong.domain.DishFlavor;
import top.wusong.dto.DishFlavorDto;
import top.wusong.service.CategoryService;
import top.wusong.service.DIshService;
import top.wusong.service.DishFlavorService;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {
    @Autowired
    private DIshService dIshService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishFlavorService dishFlavorService;

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
            if (category != null) {
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

    /**
     * 更新单个菜品以及更新菜品的口味
     *
     * @param id 需要更新的菜品
     * @return Result<DishFlavorDto> 更新结果
     */
    @GetMapping("/{id}")
    public Result<DishFlavorDto> updateById(@PathVariable Long id) {
        //查询菜品对象
        Dish byId = dIshService.getById(id);
        //查询菜品对应的口味
        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //构造条件
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, id);
        //调用查询语句
        List<DishFlavor> dishFlavors = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
        //新建结果对象
        DishFlavorDto dto = new DishFlavorDto();
        //把对应的结果复制给结果对象
        BeanUtils.copyProperties(byId, dto);
        //根据菜品名查询菜品分类
        Category category = categoryService.getById(byId.getCategoryId());
        //把口味的值附上
        dto.setFlavors(dishFlavors);
        //把菜品种类设置上
        dto.setCategoryName(category.getName());
        return Result.success(dto);
    }

    /**
     * 修改菜品，一同修改的还有菜品对应的口味
     *
     * @param dto 包含才菜品和口味的实体类对象
     * @return Result<String> 修改结果
     */
    @PutMapping
    public Result<String> updateByIdTwo(@RequestBody DishFlavorDto dto) {
        System.out.println("dto:" + dto);
        dIshService.updateTwo(dto);
        return Result.success("修改成功！");

    }

    /**
     * 根据id删除一个菜品。删除的同还要删除对应的口味
     *
     * @param ids 需要删除菜品的id集合
     * @return Result<String> 删除结果
     */
    @DeleteMapping
    public Result<String> deleteByID(@RequestParam("ids") List<Long> ids) {
        //删除方法
        dIshService.deleteById(ids);
        return Result.success("删除成功！");
    }

    /**
     * 更改菜品的在售状态
     *
     * @param type 是在售还是停用
     * @param ids  需要更改状态的集合
     * @return Result<String> 修改结果
     */
    @PostMapping("/status/{type}")
    public Result<String> updateStatus(@PathVariable Integer type, @RequestParam("ids") List<Long> ids) {
        //判断是启用还是停售
        if (type == 1) {
            ids.forEach((id) -> {
                dIshService.update(new LambdaUpdateWrapper<Dish>().eq(Dish::getId, id).set(Dish::getStatus, 1));
            });
        } else {
            //不等于1的则是停售
            ids.forEach((id) -> {
                dIshService.update(new LambdaUpdateWrapper<Dish>().eq(Dish::getId, id).set(Dish::getStatus, 0));
            });
        }
        return Result.success("设置成功！");
    }


   /* @GetMapping("/list")
    public Result<List<Dish>> getList(Dish dish){
        log.info("查询的dish为 { }"+dish);
        //构建条件
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //当前id的菜品
        lambdaQueryWrapper.eq(dish.getCategoryId() != null,Dish::getCategoryId,dish.getCategoryId()).
                //排序
                orderByAsc(Dish::getSort).
                orderByDesc(Dish::getUpdateTime).
                //只查询在售的
                eq(Dish::getStatus,1);
        List<Dish> list = dIshService.list(lambdaQueryWrapper);
        return Result.success(list);

    }*/

    /**
     * 给套餐查询一系列的套餐对应的菜品 ，需要再增强一下，用户端也需要该方法来展示每一个菜品的口味
     *
     * @param dish 菜品
     * @return Result<List < Dish>> 菜品集合
     */
    @GetMapping("/list")
    public Result<List<DishFlavorDto>> getList(Dish dish) {
        log.info("查询的dish为 { }" + dish);
        //构建条件
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //当前id的菜品
        lambdaQueryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId()).
                //排序
                        orderByAsc(Dish::getSort).
                orderByDesc(Dish::getUpdateTime).
                //只查询在售的
                        eq(Dish::getStatus, 1);
        List<Dish> list = dIshService.list(lambdaQueryWrapper);
        //新建一个菜品和菜品所对应的口味对象
        List<DishFlavorDto> dishFlavorDtos = new ArrayList<>();
        //把已查询的结果复制过去
        dishFlavorDtos = list.stream().map(dish1 -> {
            DishFlavorDto dishFlavorDto = new DishFlavorDto();
            //把菜品复制过去
            BeanUtils.copyProperties(dish1, dishFlavorDto);
            //通过菜品查询对应的口味
            List<DishFlavor> dishFlavors = dishFlavorService.list(new LambdaQueryWrapper<DishFlavor>().eq(DishFlavor::getDishId, dish1.getId()));
            //设置对应的口味
            dishFlavorDto.setFlavors(dishFlavors);
            //返回新的对象
            return dishFlavorDto;
        }).collect(Collectors.toList());

        return Result.success(dishFlavorDtos);

    }
}
