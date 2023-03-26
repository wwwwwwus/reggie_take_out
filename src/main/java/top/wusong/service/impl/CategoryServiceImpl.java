package top.wusong.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.wusong.domain.Category;
import top.wusong.mapper.CategoryDao;
import top.wusong.service.CategoryService;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryDao, Category> implements CategoryService {
}
