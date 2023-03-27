package top.wusong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
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

    @Override
    @Transactional
    public void updateTwo(DishFlavorDto dto) {
        //保存菜品
        this.updateById(dto);
        //保存口味
        List<DishFlavor> flavors = dto.getFlavors();
        if (flavors != null){
            //说明还有口味，若口味名称相同则覆盖，如没有口味名称则是新增
            //没有的口味
            List<DishFlavor> list = dishFlavorService.list(new LambdaQueryWrapper<DishFlavor>().eq(DishFlavor::getDishId, dto.getId()));
            //遍历口味
            if (list != null){
                for (DishFlavor dishFlavor : list) {
                    for (DishFlavor flavor : flavors) {
                        //判断原来有的口味和现在有的口味相同的情况，则保存现在的
                        if (flavor.getName().equals(dishFlavor.getName())){
                            dishFlavorService.updateById(flavor);
                        }
                        //判断二者不相同的情况，说明原来有，但是跟现在不一样
                        if (!flavor.getName().equals(dishFlavor.getName())){
                            //设置id后保存
                            flavor.setDishId(dto.getId());
                            dishFlavorService.updateById(dishFlavor);
                            //然后删除原来的
                            dishFlavorService.removeById(dishFlavor);
                        }
                    }
                }
            }else {
                //原来没有口味，现在有了
                dishFlavorService.saveBatch(flavors);
                for (DishFlavor flavor : flavors) {
                    flavor.setDishId(dto.getId());
                    dishFlavorService.save(flavor);
                }
            }
        }else {
            //没有口味
            LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            //条件就是该菜品下的所有口味都去掉
            dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId,dto.getId());
            dishFlavorService.remove(dishFlavorLambdaQueryWrapper);

        }
    }
}
