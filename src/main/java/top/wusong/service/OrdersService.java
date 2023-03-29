package top.wusong.service;

import com.baomidou.mybatisplus.extension.service.IService;
import top.wusong.domain.Orders;

public interface OrdersService extends IService<Orders> {
    //下单
    void orders(Orders orders);
}
