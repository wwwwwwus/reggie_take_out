package top.wusong.dto;


import lombok.Data;
import top.wusong.domain.Setmeal;
import top.wusong.domain.SetmealDish;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
