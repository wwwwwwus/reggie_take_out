package top.wusong.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.wusong.domain.Employee;
import top.wusong.mapper.EmployeeDao;
import top.wusong.service.EmployeeService;

@Service                                //mybatis实现类对象，泛型是映射接口对象和 实体类对象
public class EmployeeServiceImpl extends ServiceImpl<EmployeeDao, Employee> implements EmployeeService {
}
