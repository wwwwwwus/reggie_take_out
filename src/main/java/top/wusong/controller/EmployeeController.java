package top.wusong.controller;

import org.springframework.web.bind.annotation.*;
import top.wusong.domain.Employee;

@RestController
@RequestMapping("/employee")
public class EmployeeController {

    @PostMapping("/login")
    public void employeeLogin(@RequestBody Employee employee){


    }
}
