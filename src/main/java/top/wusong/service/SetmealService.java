package top.wusong.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.wusong.domain.Setmeal;
import top.wusong.dto.SetmealDto;

import java.util.List;

public interface SetmealService extends IService<Setmeal> {
    //添加一个套餐
    void saveWithSeteamlDish(SetmealDto setmealDto);

    //批量删除套餐
    void deleteByIds(List<Long> ids);

    //修改套餐
    void updateWithDish(SetmealDto setmealDto);
}
