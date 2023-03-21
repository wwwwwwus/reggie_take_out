package top.wusong;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import top.wusong.domain.Employee;
import top.wusong.mapper.EmployeeDao;

@SpringBootTest
class ReggieTakeOutApplicationTests {
    @Autowired
    private  EmployeeDao employeeDao;
    @Test
    void contextLoads() {
        Employee employee = new Employee();
        employee.setUsername("admin");
        employee.setName("admin");
        //1、先查询是否有对应的用户
        LambdaQueryWrapper<Employee> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        System.out.println("username:"+employee.getUsername());
        System.out.println("name:"+employee.getName());
        lambdaQueryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee employee1 = employeeDao.selectOne(lambdaQueryWrapper);
        System.out.println(employee1);
    }

    void testPage(){
        LambdaQueryWrapper<Employee> employeeLambdaQueryWrapper = new LambdaQueryWrapper<>();
        Page<Employee> page = new Page<>();
        employeeDao.selectPage(page,employeeLambdaQueryWrapper);
        System.out.println(page);
    }
}
