package top.wusong.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.cassandra.CassandraProperties;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;
import top.wusong.common.Result;
import top.wusong.domain.Employee;
import top.wusong.service.EmployeeService;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    /**
     * 登录方法
     *
     * @param employee           用户登录信息
     * @param httpServletRequest 登录成功后把用户的id存储到session当中
     * @return Result<Employee> 结果对象
     */
    @PostMapping("/login")
    public Result<Employee> employeeLogin(@RequestBody Employee employee, HttpServletRequest httpServletRequest) {
        /*
        1、先把用户输入的密码通过MD5进行加密。
        2、查询用户名是否存在，不在直接返回用户名不存在
        3、查询存在，对比密码是否相等，不想等返回密码或账户名错误
        4、密码也正确，则返回判断状态码是否禁用。如果禁用则返回用户已被禁用
        5、用户状态正确，则返回正常查询结果，并记录用户的id
         */
        //1、获取用户的密码并进行加密
        String password = employee.getPassword();
        //进行MD5加密
        password = DigestUtils.md5DigestAsHex(password.getBytes());

        //2、查询当前用户名是否存在
        LambdaQueryWrapper<Employee> employeeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        //条件
        employeeLambdaQueryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee employee1 = employeeService.getOne(employeeLambdaQueryWrapper);
        //3、判断是否有个用户
        if(employee1 == null){
            //不存在直接返回一个失败的集合
           return Result.error("用户不存在！");
        }
        //4、判断密码是否正确
        if (!password.equals(employee1.getPassword())){
            //不正确
            return  Result.error("用户名或密码错误！");
        }
        //5、判断状态是否正常
        if (employee1.getStatus() == 0){
            //状态不正常
            return  Result.error("该用户已被封禁！");
        }
        //6、都正常,存id
        httpServletRequest.getSession().setAttribute("id",employee1.getId());
        return Result.success(employee1);

    }
}
