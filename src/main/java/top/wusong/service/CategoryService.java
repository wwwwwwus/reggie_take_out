package top.wusong.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.wusong.domain.Category;

public interface CategoryService extends IService<Category> {


    //删除菜品或套餐
    void delete(Long id);

}
