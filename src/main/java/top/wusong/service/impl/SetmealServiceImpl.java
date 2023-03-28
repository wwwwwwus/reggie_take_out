package top.wusong.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.wusong.domain.Setmeal;
import top.wusong.domain.SetmealDish;
import top.wusong.dto.SetmealDto;
import top.wusong.mapper.SetmealDao;
import top.wusong.service.SetmealDishService;
import top.wusong.service.SetmealService;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
public class SetmealServiceImpl extends ServiceImpl<SetmealDao, Setmeal> implements SetmealService {
    @Autowired
    private SetmealDishService setmealDishService;



    @Override
    @Transactional
    public void saveWithSeteamlDish(SetmealDto setmealDto) {
        //先保存套餐
        this.save(setmealDto);
        log.info("setmealDto的值为： { }"+setmealDto);
        //获取菜品列表
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        if (setmealDishes.size() > 0) {
            setmealDishes.stream().map(setmealDish -> {
                //设置id
                setmealDish.setSetmealId(setmealDto.getId());
                return setmealDish;
            }).collect(Collectors.toList());
            setmealDishService.saveBatch(setmealDishes);
        }

    }

    @Override
    @Transactional
    public void deleteByIds(List<Long> ids) {
        if (ids.size()>0){
            //先删除套餐
            this.removeByIds(ids);
            //然后删除套餐包含的菜品
            ids.forEach((id) ->{
                setmealDishService.remove(new LambdaUpdateWrapper<SetmealDish>().eq(SetmealDish::getSetmealId,id));
            });
        }

    }

    @Override
    public void updateWithDish(SetmealDto setmealDto) {
        //修改套餐
        this.updateById(setmealDto);
        //删除套餐原来的菜品
        setmealDishService.remove(new LambdaUpdateWrapper<SetmealDish>().eq(SetmealDish::getSetmealId,setmealDto.getId()));
        //添加现有的菜品
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes().stream().map((dish) -> {
            dish.setSetmealId(setmealDto.getId());
            return dish;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(setmealDishes);
    }
}
