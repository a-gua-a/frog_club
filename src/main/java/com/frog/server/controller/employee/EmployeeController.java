package com.frog.server.controller.employee;

import com.frog.common.result.Result;
import com.frog.pojo.dto.EmployeeLoginDTO;
import com.frog.pojo.vo.EmployeeLoginVO;
import com.frog.server.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("employeeEmployeeController")
@RequestMapping("/employee")
@Slf4j
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /**
     * 员工登录
     */
    @PostMapping("/login")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("员工登录：{}", employeeLoginDTO);
        EmployeeLoginVO employeeLoginVO = employeeService.login(employeeLoginDTO);
        return Result.success(employeeLoginVO);
    }

    /**
     * 员工退出登录
     */
    @PostMapping("/logout")
    public Result<Void> logout() {
        log.info("员工退出登录");
        employeeService.logout();
        return Result.success();
    }
}
