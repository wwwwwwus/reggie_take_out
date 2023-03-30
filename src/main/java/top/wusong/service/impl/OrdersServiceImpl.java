package top.wusong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.wusong.common.BaseContext;
import top.wusong.domain.*;
import top.wusong.exception.BusinessException;
import top.wusong.mapper.OrdersDao;
import top.wusong.service.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersDao, Orders> implements OrdersService {
   /* @Autowired
    private UserService userService;

    @Autowired
    private AddressBookService addressBookService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private ShoppingCartService shoppingCartService;*/


    private final UserService userService;


    private final AddressBookService addressBookService;


    private final OrderDetailService orderDetailService;

    private final ShoppingCartService shoppingCartService;

    @Autowired
    public OrdersServiceImpl(UserService userService, AddressBookService addressBookService, OrderDetailService orderDetailService, ShoppingCartService shoppingCartService) {
        this.userService = userService;
        this.addressBookService = addressBookService;
        this.orderDetailService = orderDetailService;
        this.shoppingCartService = shoppingCartService;
    }

    @Override
    @Transactional//开启事务
    public void orders(Orders orders) {
        //下单，先获取用户id查询用户
        Long employeeId = BaseContext.getEmployeeId();
        //查询用户
        User user = userService.getById(employeeId);
        System.out.println("user:"+user);
        System.out.println("user:"+user.getId());
        //获取用户的默认地址
        Long addressBookId = orders.getAddressBookId();
        AddressBook addressBook = addressBookService.getById(addressBookId);
        //判断地址是否存在
        if (addressBook == null){
            throw new BusinessException("地址不存在，无法下单！");
        }
        //生成订单号
        long orderId = IdWorker.getId();
        //生成订单金额
        AtomicInteger amount = new AtomicInteger(0);


        //查询购物车信息
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId,user.getId());
        List<ShoppingCart> shoppingCarts = shoppingCartService.list(lambdaQueryWrapper);

        //保存一个订单
        orders.setId(orderId);
        orders.setOrderTime(LocalDateTime.now());
        orders.setCheckoutTime(LocalDateTime.now());
        orders.setStatus(2);
        orders.setAmount(new BigDecimal(amount.get()));//总金额
        orders.setUserId(user.getId());
        orders.setNumber(String.valueOf(orderId));
        orders.setUserName(user.getName());
        orders.setConsignee(addressBook.getConsignee());
        orders.setPhone(addressBook.getPhone());
        orders.setAddress((addressBook.getProvinceName() == null ? "" : addressBook.getProvinceName())
                + (addressBook.getCityName() == null ? "" : addressBook.getCityName())
                + (addressBook.getDistrictName() == null ? "" : addressBook.getDistrictName())
                + (addressBook.getDetail() == null ? "" : addressBook.getDetail()));

        //插入订单明细订单
        this.save(orders);
        List<OrderDetail> orderDetails = shoppingCarts.stream().map(order ->{
            OrderDetail orderDetail = new OrderDetail();
            //设置参数
            orderDetail.setOrderId(orderId);
            orderDetail.setNumber(order.getNumber());
            orderDetail.setDishFlavor(order.getDishFlavor());
            orderDetail.setDishId(order.getDishId());
            orderDetail.setSetmealId(order.getSetmealId());
            orderDetail.setName(order.getName());
            orderDetail.setImage(order.getImage());
            orderDetail.setAmount(order.getAmount());
            amount.addAndGet(order.getAmount().multiply(new BigDecimal(order.getNumber())).intValue());
            return orderDetail;
        }).collect(Collectors.toList());
        orderDetailService.saveBatch(orderDetails);

        //清空购物车
        shoppingCartService.remove(lambdaQueryWrapper);
    }
}
