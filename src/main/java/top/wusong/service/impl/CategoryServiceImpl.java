package top.wusong.service.impl;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.wusong.domain.Category;
import top.wusong.domain.Dish;
import top.wusong.domain.Setmeal;
import top.wusong.exception.BusinessException;
import top.wusong.mapper.CategoryDao;
import top.wusong.service.CategoryService;
import top.wusong.service.DIshService;
import top.wusong.service.SetmealService;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, Category> implements CategoryService {

  /*  @Autowired
    private DIshService dIshService;

    @Autowired
    private SetmealService setmealService;*/

    private final DIshService dIshService;


    private final SetmealService setmealService;


    @Autowired
    public CategoryServiceImpl(DIshService dIshService, SetmealService setmealService) {
        this.dIshService = dIshService;
        this.setmealService = setmealService;
    }

    /**
     * 删除套餐及套餐对应的菜品（套餐菜品关系表中的）
     * @param id 需要删除套餐的id
     */
    @Override
    @Transactional
    public void delete(Long id) {
        //判断是否有菜品
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //按照
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int count1 = dIshService.count(dishLambdaQueryWrapper);
        if (count1 > 0){
            throw new BusinessException("该套餐分类或菜系分类下有对应的菜品");
        }

        //判断是否超套餐
        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(setmealLambdaQueryWrapper);
        if (count2 > 0){
            throw new BusinessException("该套餐分类或菜系分类下有对应的套餐");
        }

        //没有抛出异常，则进行删除
        super.removeById(id);

    }
}
