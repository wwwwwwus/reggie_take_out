package top.wusong.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.wusong.domain.Dish;
import top.wusong.dto.DishFlavorDto;

public interface DIshService extends IService<Dish> {
    //新增一个菜品
    void insert(DishFlavorDto dto);

    void updateTwo(DishFlavorDto dto);
}
