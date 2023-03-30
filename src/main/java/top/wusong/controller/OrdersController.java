package top.wusong.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.wusong.common.BaseContext;
import top.wusong.common.Result;
import top.wusong.domain.Orders;
import top.wusong.service.OrdersService;

import java.util.List;

@RestController
@RequestMapping("/order")
public class OrdersController {


    @Autowired
    private OrdersService ordersService;
    /**
     * 提交订单
     * @param orders 订单信息
     * @return 返回操作结果
     */
    @PostMapping("/submit")
    public Result<String> submitOrder(@RequestBody Orders orders) {
        ordersService.orders(orders);
        return Result.success("下单成功！");
    }



    /**
     * 获取用户的所有订单信息
     * @param page 当前页码
     * @param pageSize 每页显示的记录数
     * @return 返回订单列表
     */
    @GetMapping("/userPage")
    public Result<Page<Orders>> getAllOrders(Integer page, Integer pageSize) {
        //获取分页
        Page<Orders> pages = new Page<>(page,pageSize);

        ordersService.page(pages, new LambdaQueryWrapper<Orders>().eq(Orders::getUserId, BaseContext.getEmployeeId()));
        //ordersService.page(pages, Wrappers.<Orders>lambdaQuery().eq(Orders::getUserId, BaseContext.getEmployeeId()));

        return Result.success(pages);
    }

}
