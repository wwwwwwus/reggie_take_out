package top.wusong.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Insert;
import top.wusong.domain.Employee;

public interface EmployeeService extends IService<Employee> {

    //新增一个用
    boolean addEmployee(Employee employee);
}
