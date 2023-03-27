package top.wusong.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.wusong.domain.Dish;
import top.wusong.domain.DishFlavor;
import top.wusong.dto.DishFlavorDto;
import top.wusong.mapper.DishDao;
import top.wusong.service.DIshService;
import top.wusong.service.DishFlavorService;

import java.util.List;

@Service
public class DishServiceImpl extends ServiceImpl<DishDao, Dish> implements DIshService {

    @Autowired
    private DishFlavorService dishFlavorService;
    @Autowired
    private DishDao dishDao;

    @Override
    @Transactional//一个方法同时对多张表进行操作时需要用事务进行管理
    public void insert(DishFlavorDto dto) {
        //先调用菜品保存方法
        super.save(dto);
        //调用口味保存
        List<DishFlavor> dishList = dto.getFlavors();
        if (dishList != null){
            for (DishFlavor dishFlavor : dishList) {
                //填写菜品的id，确保知道是哪个菜品的口味
                dishFlavor.setDishId(dto.getId());
                dishFlavorService.save(dishFlavor);
            }
        }

    }
}
