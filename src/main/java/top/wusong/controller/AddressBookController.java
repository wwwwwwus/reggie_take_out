package top.wusong.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.wusong.common.BaseContext;
import top.wusong.common.Result;
import top.wusong.domain.AddressBook;
import top.wusong.service.AddressBookService;

import java.util.List;

@RestController
@RequestMapping("/addressBook")
@Slf4j
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;


    /**
     * 展示当前用户的收货地址
     * @return Result<List<AddressBook>> 当前用户的所有地址
     */
    @GetMapping("/list")
    public Result<List<AddressBook>> list(){
        /*
        获取当前用户的所有地址
         */
        //构建条件
        LambdaQueryWrapper<AddressBook> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        //从用户登录线程中获取的id
        lambdaQueryWrapper.eq(AddressBook::getUserId, BaseContext.getEmployeeId());
        lambdaQueryWrapper.orderByDesc(AddressBook::getIsDefault).orderByDesc(AddressBook::getUpdateTime);
        //查询收货地址
       return Result.success(addressBookService.list(lambdaQueryWrapper));
    }

    /**
     * 给当前用户添加地址
     * @param addressBook 传递过来的地址信息
     * @return Result<AddressBook> 保存后的地址信息
     */
    @PostMapping
    public Result<AddressBook> saveAddressBook(@RequestBody AddressBook addressBook){
        log.info("AddressBook {}",addressBook);
        //设置为当前用户的地址
        addressBook.setUserId(BaseContext.getEmployeeId());
        //调用保存方法
        addressBookService.save(addressBook);
        return Result.success(addressBook);
    }


    /**
     * 修改地址为默认地址
     * @param addressBook 该用户的地址信息
     * @return Result<AddressBook> 更新后的地址
     */
    @PutMapping("/default")
    public Result<AddressBook> updateDefault( @RequestBody AddressBook addressBook ){
        //把当前用户下的所有地址都改为非默认的
        addressBook.setUserId(BaseContext.getEmployeeId());
        //构成条件
        LambdaUpdateWrapper<AddressBook> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        //                    当前用户的                                             值都为0
        lambdaUpdateWrapper.eq(AddressBook::getUserId,addressBook.getUserId()).set(AddressBook::getIsDefault,0);
        addressBookService.update(lambdaUpdateWrapper);
        //把传递过来的这个地址改为默认的
        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return Result.success(addressBook);
    }

    /**
     * 修改的第一步，根据id查询当前地址
     * @param id 需要修改的id
     * @return
     */
    @GetMapping("/{id}")
    public Result<AddressBook> updateAddressBookOne(@PathVariable("id") Long id){
        //查询单个地址
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null){
            return Result.success(addressBook);
        }
        return Result.error("没有该对象！");
    }

    /**
     * 更新地址
     * @param addressBook 需要更新的地址
     * @return Result<String>更新结果
     */
    @PutMapping
    public Result<String> updateAddressBookTwo(@RequestBody AddressBook addressBook){
        //直接更新
        addressBookService.updateById(addressBook);
        return Result.success("更新成功！");
    }

    /**
     * 删除地址
     * @param ids 地址集合
     * @return Result<String> 删除结果
     */
    @DeleteMapping
    public Result<String> deleteAddressBook(@RequestParam("ids") List<Long> ids){
        //删除
        ids.forEach((id) -> {
            addressBookService.removeById(id);
        });
        return Result.success("删除成功！");
    }
}
