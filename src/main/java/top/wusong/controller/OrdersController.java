package top.wusong.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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

    @PostMapping("/submit")
    public Result<String> order(@RequestBody Orders orders) {
        ordersService.orders(orders);
        return Result.success("下单成功！");
    }

    @GetMapping("/userPage")
    public Result<Page<Orders>> getAllOrders(Integer page, Integer pageSize) {
        //获取分页
        Page<Orders> pages = new Page<>(page,pageSize);

        ordersService.page(pages, new LambdaQueryWrapper<Orders>().eq(Orders::getUserId, BaseContext.getEmployeeId()));

        return Result.success(pages);
    }

}
