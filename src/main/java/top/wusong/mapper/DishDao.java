package top.wusong.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.wusong.domain.Dish;

@Mapper
public interface DishDao extends BaseMapper<Dish> {
}
