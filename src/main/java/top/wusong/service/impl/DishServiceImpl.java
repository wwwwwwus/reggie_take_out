package top.wusong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.wusong.domain.Dish;
import top.wusong.domain.DishFlavor;
import top.wusong.dto.DishFlavorDto;
import top.wusong.exception.BusinessException;
import top.wusong.mapper.DishDao;
import top.wusong.service.DIshService;
import top.wusong.service.DishFlavorService;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishServiceImpl extends ServiceImpl<DishDao, Dish> implements DIshService {

    /*  @Autowired
      private DishFlavorService dishFlavorService;
      @Autowired
      private DishDao dishDao;*/
    private final DishFlavorService dishFlavorService;

    private final DishDao dishDao;

    public DishServiceImpl(DishFlavorService dishFlavorService, DishDao dishDao) {
        this.dishFlavorService = dishFlavorService;
        this.dishDao = dishDao;
    }

    @Autowired

    @Override
    @Transactional//一个方法同时对多张表进行操作时需要用事务进行管理
    public void insert(DishFlavorDto dto) {
        //先调用菜品保存方法
        super.save(dto);
        //调用口味保存
        List<DishFlavor> dishList = dto.getFlavors();
        if (dishList != null) {
            for (DishFlavor dishFlavor : dishList) {
                //填写菜品的id，确保知道是哪个菜品的口味
                dishFlavor.setDishId(dto.getId());
                dishFlavorService.save(dishFlavor);
            }
        }
    }

    @Override
    @Transactional
    public void updateTwo(DishFlavorDto dto) {
        //保存菜品
        this.updateById(dto);
        //先把菜品对应的口味删除
        dishFlavorService.remove(new LambdaQueryWrapper<DishFlavor>().eq(DishFlavor::getDishId, dto.getId()));
        //重新插入
        List<DishFlavor> dishFlavors = dto.getFlavors().stream().map(dishFlavor -> {
            dishFlavor.setDishId(dto.getId());
            return dishFlavor;
        }).collect(Collectors.toList());
        //插入
        dishFlavorService.saveBatch(dishFlavors);
    }

    @Transactional
    @Override
    public void deleteById(List<Long> ids) {
        /*
        删除一个菜品，需要同时把菜品下对应的口味也删除
         */
        //删除前要判断是否在售
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //判断当前的菜品是否是在售当中
        lambdaQueryWrapper.in(Dish::getId, ids).eq(Dish::getStatus, 1);
        int count = this.count(lambdaQueryWrapper);
        if (count > 0) {
            throw new BusinessException("菜品在售中，无法删除!");
        }
        //删除菜品
        ids.forEach((this::removeById));
        //删除菜品对应的口味
        ids.forEach((id) -> {
            dishFlavorService.remove(new LambdaQueryWrapper<DishFlavor>().eq(DishFlavor::getDishId, id));
        });
    }
}
