package top.wusong.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.wusong.domain.Employee;
import top.wusong.mapper.EmployeeDao;
import top.wusong.service.EmployeeService;

@Service                                //mybatis实现类对象，泛型是映射接口对象和 实体类对象
public class EmployeeServiceImpl extends ServiceImpl<EmployeeDao, Employee> implements EmployeeService {
    @Autowired
    private EmployeeDao employeeDao;
    @Override
    public boolean addEmployee(Employee employee) {
        //1、先查询是否有对应的用户
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee employee1 = employeeDao.selectOne(lambdaQueryWrapper);
        //2、判断是否存在这个用户名称
        if (employee1 != null ){
            //不存在直接返回值false
            return false;
        }
        //3、不存在，进入添加操作
        int insert = employeeDao.insert(employee);
        if (insert == 1){
            //影响行数为1.说明添加成功！
            return true;
        }
        return false;
    }
}
