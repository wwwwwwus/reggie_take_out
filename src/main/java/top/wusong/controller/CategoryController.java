package top.wusong.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.wusong.common.Result;
import top.wusong.domain.Category;
import top.wusong.dto.DishFlavorDto;
import top.wusong.service.CategoryService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }


    /**
     * 分页查询
     * @param page 当前是第几页
     * @param pageSize 每页展示多少条
     * @return Result<Page<Category>> 每页的数据
     */
   /* @GetMapping("/page")
    public Result<Page<Category>> getAllByPage(Long page,Long pageSize){
        //设置分页对象，当前是哪页，每页显示多少
        Page<Category> pages = new Page<>(page,pageSize);
        //根据sort排序
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByAsc(Category::getSort);
        //查询
        categoryService.page(pages,lambdaQueryWrapper);
        return Result.success(pages);
    }*/
    @GetMapping("/page")
    public Result<Page<Category>> getAllByPage(@RequestParam(defaultValue = "1") Long page,
                                               @RequestParam(defaultValue = "10") Long pageSize) {
        Page<Category> pages = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.orderByAsc(Category::getSort);
        categoryService.page(pages, lambdaQueryWrapper);
        return Result.success(pages);
    }

    /**
     * 添加菜品
     * @param category
     * @return
     */
    @PostMapping
    public Result<String> insertCategory(@RequestBody Category category){
        boolean save = categoryService.save(category);
        //判断是否添加成功
        /*if (save){
            return Result.success("添加成功！");
        }
        return Result.error("添加失败");*/
        return save ? Result.success("添加成功！") : Result.error("添加失败");
    }

    /**
     * 修改菜品
     * @param category 更新后参数
     * @return Result<Boolean> 更新结果
     */
    @PutMapping
    public Result<String> updateCategory(@RequestBody Category category){
        boolean updateById = categoryService.updateById(category);
        //判断是否修改成功
       /* if (updateById){
            return Result.success("修改成功！");
        }
        return Result.error("修改失败！");*/
        return updateById ? Result.success("修改成功！") : Result.error("修改失败！");
    }


    /**
     * 删除一个菜品,删除前要确定该菜品下是否绑定了菜品
     * @param id 配删除菜品的id
     * @return Result<Boolean> 删除结果
     */
   @DeleteMapping
    public Result<String> deleteCategory(@RequestParam("ids") Long id){
        //删除
        categoryService.delete(id);
        return Result.success("删除成功！");
    }

    /**
     * 查询菜品类型
     * @param type 菜品类型对应的数字
     * @return Result<List<Category>> 查询结果集合
     */
    @GetMapping("list")
    public Result<List<Category>> listByType(Integer type){
       //构建条件
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (type != null){
            lambdaQueryWrapper.eq(Category::getType,type);
        }
        //添加排序时间
        lambdaQueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(lambdaQueryWrapper);
        /*
         LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(type != null, Category::getType, type)
                .orderByAsc(Category::getSort)
                .orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(lambdaQueryWrapper);
         */
        return Result.success(list);
    }



}
