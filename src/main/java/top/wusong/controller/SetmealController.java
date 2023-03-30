package top.wusong.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import top.wusong.common.Result;
import top.wusong.domain.Category;
import top.wusong.domain.Setmeal;
import top.wusong.domain.SetmealDish;
import top.wusong.dto.SetmealDto;
import top.wusong.service.CategoryService;
import top.wusong.service.SetmealDishService;
import top.wusong.service.SetmealService;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealDishService setmealDishService;


    /**
     * 分页查询套餐页面
     *
     * @param page     当前页
     * @param pageSize 没有展示多少条
     * @param name     查询套餐的名称
     * @return Result<Page < Setmeal>> 分页结果
     */
    @GetMapping("/page")
    public Result<Page<SetmealDto>> getAllByPage(Integer page, Integer pageSize, String name) {
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (name != null) {
            setmealLambdaQueryWrapper.like(Setmeal::getName, name);
        }
        Page<Setmeal> pages = new Page<>(page, pageSize);
        setmealService.page(pages, setmealLambdaQueryWrapper);
        //新建一个兼容性的实体类对象，用于存放菜品分类名称
        Page<SetmealDto> dtoPage = new Page<>();
        //把查询的结果查询复制给兼容性的实体类,排除要替换的类型
        BeanUtils.copyProperties(pages, dtoPage, "records");
        //获取查询到的套餐类集合
        List<Setmeal> records = pages.getRecords();
        List<SetmealDto> setmealDtos = records.stream().map((record) -> {
            //新建一个实体类对象
            SetmealDto setmealDto = new SetmealDto();
            //把前面查询的结果复制给新建的对象
            BeanUtils.copyProperties(record, setmealDto);
            //查询菜品分类
            Category category = categoryService.getById(record.getCategoryId());
            //设置菜品的名称
            setmealDto.setCategoryName(category.getName());
            //返回新的对象
            return setmealDto;
        }).collect(Collectors.toList());
        //替换新的结果
        dtoPage.setRecords(setmealDtos);
        return Result.success(dtoPage);
    }

    /**
     * 添加一个套餐
     *
     * @param setmealDto 套餐实体类对象，包含了套餐和套餐包含的套餐
     * @return Result<String> 添加结果
     */
    @PostMapping
    public Result<String> insertSetmeal(@RequestBody SetmealDto setmealDto) {
        log.info("新增的结果为 {}", setmealDto);
        setmealService.saveWithSeteamlDish(setmealDto);
        return Result.success("添加成功！");
    }

    /**
     * @param ids 需要删除的套餐集合
     * @return Result<String> 删除结果
     */
    @DeleteMapping
    public Result<String> deleteById(@RequestParam("ids") List<Long> ids) {
        setmealService.deleteByIds(ids);
        return Result.success("删除成功！");
    }

    /**
     * 修改套餐状态
     *
     * @param type 当前状态
     * @param ids  需要修改状态的集合
     * @return Result<String> 修改结果
     */
    @PostMapping("/status/{type}")
    public Result<String> updateStatus(@PathVariable("type") Integer type, @RequestParam("ids") List<Long> ids) {
        //判断是否在售
        ids.forEach((id) -> {
            if (type == 1) {
                setmealService.update(new LambdaUpdateWrapper<Setmeal>().eq(Setmeal::getId, id).set(Setmeal::getStatus, 1));
            } else {
                setmealService.update(new LambdaUpdateWrapper<Setmeal>().eq(Setmeal::getId, id).set(Setmeal::getStatus, 0));
            }
        });
        return Result.success("状态修改成功！");
    }
    /**
     * 修改套餐状态
     *
     * @param type 当前状态
     * @param ids  需要修改状态的集合
     * @return Result<String> 修改结果
     */
   // @PostMapping("/status/{type}")  -- gpt 优化
    public Result<String> updateStatus1(@PathVariable("type") Integer type, @RequestParam("ids") List<Long> ids) {
        //构建查询条件
        LambdaUpdateWrapper<Setmeal> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.in(Setmeal::getId, ids);

        //设置要更新的值
        if (type == 1) {
            lambdaUpdateWrapper.set(Setmeal::getStatus, 1);
        } else {
            lambdaUpdateWrapper.set(Setmeal::getStatus, 0);
        }

        //批量更新
        setmealService.update(lambdaUpdateWrapper);

        return Result.success("状态修改成功！");
    }





    /**
     * 修改套餐的第一步，回显数据
     *
     * @param id
     * @return Result<SetmealDto> 回显结果，包含套餐和套餐包含的菜品
     */
    @GetMapping("/{id}")
    public Result<SetmealDto> updateSetmealOne(@PathVariable Long id) {
        //查询套餐结果
        Setmeal setmeal = setmealService.getById(id);
        //查询套餐关联的菜品
        List<SetmealDish> setmealDishes = setmealDishService.list(new LambdaQueryWrapper<SetmealDish>().eq(SetmealDish::getSetmealId, id));
        //封装成一个新的结果
        SetmealDto setmealDto = new SetmealDto();
        //把结果复制过去
        BeanUtils.copyProperties(setmeal, setmealDto);
        //添加套餐的集合
        setmealDto.setSetmealDishes(setmealDishes);
        return Result.success(setmealDto);
    }





    /**
     * 修改操作第二步
     *
     * @param setmealDto 需要修改的套餐，包含了套餐和套餐包含的菜品
     * @return Result<String> 修改结果
     */
    @PutMapping
    public Result<String> updateSetmealTwo(@RequestBody SetmealDto setmealDto) {

        //调用修改方法
        setmealService.updateWithDish(setmealDto);

        return Result.success("修改成功！");
    }

    /**
     * 用户端显示套餐
     *
     * @param setmeal 需要查询的套餐
     * @return Result<List < Setmeal>> 套餐结果
     */
    @GetMapping("/list")
    public Result<List<Setmeal>> setmealList(Setmeal setmeal) {
        log.info("setmeal {}", setmeal);
        //构建条件
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId()).
                eq(setmeal.getStatus() != null, Setmeal::getStatus, setmeal.getStatus());
        //查询结果
        List<Setmeal> setmeals = setmealService.list(setmealLambdaQueryWrapper);
        return Result.success(setmeals);
    }

}
