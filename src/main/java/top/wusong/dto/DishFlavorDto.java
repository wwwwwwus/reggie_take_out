package top.wusong.dto;

import lombok.Data;
import top.wusong.domain.Dish;
import top.wusong.domain.DishFlavor;

import java.util.ArrayList;
import java.util.List;

@Data
public class DishFlavorDto extends Dish {
    //口味集合
    private List<DishFlavor> flavors = new ArrayList<>();

    //菜品名称
    private String categoryName;
}
