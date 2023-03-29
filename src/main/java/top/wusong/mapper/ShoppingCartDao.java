package top.wusong.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.wusong.domain.ShoppingCart;

@Mapper
public interface ShoppingCartDao extends BaseMapper<ShoppingCart> {
}
