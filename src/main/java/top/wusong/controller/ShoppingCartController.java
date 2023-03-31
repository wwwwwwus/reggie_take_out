package top.wusong.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.wusong.common.BaseContext;
import top.wusong.common.Result;
import top.wusong.domain.ShoppingCart;
import top.wusong.service.ShoppingCartService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {

    @Autowired
    private ShoppingCartService shoppingCartService;


    /**
     * 购物车功能，添加
     * @param shoppingCart
     * @return
     */
    @PostMapping("/add")
    public Result<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        log.info("购物车信息{}", shoppingCart);
        //先要确定是哪个用户的购物车
        Long userId = BaseContext.getEmployeeId();
        shoppingCart.setUserId(userId);
        //判断是菜品还是套餐
        Long dishId = shoppingCart.getDishId();
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (dishId != null) {
            //不为空则是菜品.构建查询语句
            lambdaQueryWrapper.eq(ShoppingCart::getUserId, shoppingCart.getUserId())
                    .eq(ShoppingCart::getDishId, shoppingCart.getDishId());
        } else {
            //为空则说明是套餐
            lambdaQueryWrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId())
                    .eq(ShoppingCart::getUserId, shoppingCart.getUserId());
        }
        //查询是否有对应的结果
        ShoppingCart serviceOne = shoppingCartService.getOne(lambdaQueryWrapper);
        //判断是否为空
        if (serviceOne == null){
            //说明是第一次添加
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            //然后保存
            shoppingCartService.save(shoppingCart);
            return Result.success(shoppingCart);
        }else {
            //说明不是第一次添加
            serviceOne.setNumber(serviceOne.getNumber()+1);
            //然后更新
            shoppingCartService.updateById(serviceOne);
            return Result.success(serviceOne);
        }
    }

    /**
     * 查询购物车
     * @return  Result<List<ShoppingCart>>当前用户的购物车信息
     */
    @GetMapping("/list")
    public Result<List<ShoppingCart>> listResult(){
        //获取购物车
        Long userId = BaseContext.getEmployeeId();
        //通过id查询用户的购物车
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId,userId);
        lambdaQueryWrapper.orderByDesc(ShoppingCart::getCreateTime);
        //查询
        List<ShoppingCart> list = shoppingCartService.list(lambdaQueryWrapper);
        return Result.success(list);
    }

    /**
     * 清空购物车
     * @return 清空结果
     */
    @DeleteMapping("/clean")
    public Result<String> delete(){
        //删除当前用户下的购物车
        Long userId = BaseContext.getEmployeeId();
        //根据条件删除当前用户的购物车
        LambdaUpdateWrapper<ShoppingCart> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(ShoppingCart::getUserId,userId);
        shoppingCartService.remove(lambdaUpdateWrapper);
        return Result.success("删除成功！");
    }

    /**
     * 购物车当中的菜品-1
     * @param shoppingCart 减去的菜品
     * @return Result<ShoppingCart> 剩余的菜品
     */
    @PostMapping("/sub")
    public Result<ShoppingCart> subShoppingCart(@RequestBody ShoppingCart shoppingCart){
        //获取用户的id
        Long userId = BaseContext.getEmployeeId();
        //根据当前用户id和菜品/套餐id查询套餐数量，看看还有多少
        LambdaQueryWrapper<ShoppingCart> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(ShoppingCart::getUserId,userId);
        Long dishId = shoppingCart.getDishId();
        if (dishId != null){
            lambdaQueryWrapper.eq(ShoppingCart::getDishId,dishId);
        }else {
            lambdaQueryWrapper.eq(ShoppingCart::getSetmealId,shoppingCart.getSetmealId());
        }
        //查询结果
        ShoppingCart shoppingCartServiceOne = shoppingCartService.getOne(lambdaQueryWrapper);
        Integer number = shoppingCartServiceOne.getNumber();
        //判断数量是否为1，若是为去则直接删除，如不是1，则数量减一
        if (number <= 1){
            shoppingCartService.removeById(shoppingCartServiceOne);
            //设置数量为0
            shoppingCartServiceOne.setNumber(0);
            return Result.success(shoppingCartServiceOne);
        }else {
            shoppingCartServiceOne.setNumber(number - 1);
            shoppingCartService.updateById(shoppingCartServiceOne);
            return Result.success(shoppingCartServiceOne);
        }

    }
}
