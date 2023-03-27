package top.wusong.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.wusong.domain.Dish;
import top.wusong.mapper.DishDao;
import top.wusong.service.DIshService;

@Service
public class DishServiceImpl extends ServiceImpl<DishDao, Dish> implements DIshService {
}
