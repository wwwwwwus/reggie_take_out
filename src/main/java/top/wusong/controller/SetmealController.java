package top.wusong.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.wusong.common.Result;
import top.wusong.domain.Setmeal;
import top.wusong.service.SetmealService;

@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @GetMapping("/page")
    public Result<Page<Setmeal>> getAllByPage(Integer page, Integer pageSize, String name) {
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (name != null) {
            setmealLambdaQueryWrapper.like(Setmeal::getName, name);
        }
        Page<Setmeal> pages = new Page<>(page, pageSize);
        setmealService.page(pages,setmealLambdaQueryWrapper);
        return Result.success(pages);
    }
}
