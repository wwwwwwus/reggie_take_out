package top.wusong.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.wusong.domain.Dish;
import top.wusong.dto.DishFlavorDto;

import java.util.List;

public interface DIshService extends IService<Dish> {
    //新增一个菜品
    void insert(DishFlavorDto dto);

    //修改菜品
    void updateTwo(DishFlavorDto dto);

    //删除菜品
    void deleteById(List<Long> ids);
}
