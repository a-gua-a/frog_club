package com.frog.server.service;

import com.frog.common.result.PageResult;
import com.frog.pojo.dto.EmployeeDTO;
import com.frog.pojo.dto.EmployeeLoginDTO;
import com.frog.pojo.dto.EmployeePageQueryDTO;
import com.frog.pojo.entity.Employee;
import com.frog.pojo.vo.EmployeeLoginVO;

import javax.security.auth.login.AccountNotFoundException;

public interface EmployeeService {
    /**
     * 新增员工
     * @param employeeDTO
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * 根据id查询员工
     * @param id
     */
    Employee getById(Long id);

    /**
     * 员工分页查询
     * @param employeePageQueryDTO
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * 更新员工信息
     * @param employeeDTO
     */
    void update(EmployeeDTO employeeDTO);

     /**
      * 启用禁用员工账号
      * @param status
      * @param id
      */
    void startOrStop(Integer status, Long id);

    /*
     * 员工登录
     */
    EmployeeLoginVO login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * 员工退出登录
     */
    void logout();
}
