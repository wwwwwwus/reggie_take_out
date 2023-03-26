package top.wusong.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import top.wusong.domain.Category;

@Mapper
public interface CategoryDao extends BaseMapper<Category> {
}
