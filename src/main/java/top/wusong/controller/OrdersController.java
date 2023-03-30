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

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrdersController {


    private static final String SUCCESS_MESSAGE = "操作成功！";

    @Autowired
    private OrdersService orderService;

    /**
     * 下单
     *
     * @param orders 订单对象
     * @return Result<String>
     */
    @PostMapping("/submit")
    public Result<String> submitOrder(@RequestBody Orders orders) {
        orderService.orders(orders);
        return Result.success(SUCCESS_MESSAGE);
    }

 /*   @PostMapping("/submit")
    public Result<String> order(@RequestBody Orders orders) {
        ordersService.orders(orders);
        return Result.success("下单成功！");
    }*/

    /**
     * 获取用户订单
     *
     * @param page     页码
     * @param pageSize 每页数量
     * @return Result<Page < Orders>>
     */
    @GetMapping("/userPage")
    public Result<Page<Orders>> getUserOrders(@RequestParam(defaultValue = "1") Integer page,
                                              @RequestParam(defaultValue = "10") Integer pageSize) {
        // 参数校验
        if (page < 1) {
            page = 1;
        }
        if (pageSize < 1 || pageSize > 100) {
            pageSize = 10;
        }

        // 查询分页数据
        Page<Orders> pages = new Page<>(page, pageSize);
        orderService.page(pages, new LambdaQueryWrapper<Orders>().eq(Orders::getUserId, BaseContext.getEmployeeId()));
        return Result.success(pages);
    }



   /* @GetMapping("/userPage")
    public Result<Page<Orders>> getAllOrders(Integer page, Integer pageSize) {
        //获取分页
        Page<Orders> pages = new Page<>(page,pageSize);

        ordersService.page(pages, new LambdaQueryWrapper<Orders>().eq(Orders::getUserId, BaseContext.getEmployeeId()));

        return Result.success(pages);
    }*/

    /**
     * 查询所有订单
     * @param page 当前页码
     * @param pageSize 每页显示的数量
     * @return Result< Page<Orders>> 分页结果
     */
    @GetMapping("/page")
    public Result< Page<Orders>> getOrdersPage(@RequestParam(defaultValue = "1")Integer page, @RequestParam(defaultValue = "10")Integer pageSize, Long number, String beginTime,String endTime){
        Page<Orders> pages = new Page<>(page,pageSize);
        LambdaQueryWrapper<Orders> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (number != null){
            lambdaQueryWrapper.eq(Orders::getNumber,number);
        }

        if (beginTime != null && endTime != null){
            lambdaQueryWrapper.between(Orders::getOrderTime, beginTime, endTime);
        }
        orderService.page(pages,lambdaQueryWrapper);

        return Result.success(pages);
    }

    @PutMapping
    public Result<String> updateStatus(@RequestBody Orders orders){
        //直接更新状态
        orderService.updateById(orders);
        return Result.success("已派送");
    }
}
