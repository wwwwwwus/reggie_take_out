package top.wusong.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.wusong.domain.ShoppingCart;
import top.wusong.mapper.ShoppingCartDao;
import top.wusong.service.ShoppingCartService;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartDao,ShoppingCart>  implements ShoppingCartService {
}
