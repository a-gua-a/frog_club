package com.frog.server.service.impl;

import com.frog.common.constant.MessageConstant;
import com.frog.common.constant.PasswordConstant;
import com.frog.common.constant.StatusConstant;
import com.frog.common.exception.AccountNotFoundException;
import com.frog.common.result.PageResult;
import com.frog.pojo.dto.EmployeeDTO;
import com.frog.pojo.dto.EmployeePageQueryDTO;
import com.frog.pojo.entity.Employee;
import com.frog.server.mapper.EmployeeMapper;
import com.frog.server.service.EmployeeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /*
     * 新增员工
     * @param employeeDTO
     */
    @Override
    public void save(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        //设置默认密码123456
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        employee.setStatus(StatusConstant.ENABLE);
        employee.setOnline(StatusConstant.OFFLINE);
        log.info("新增员工：{}", employee);
        employeeMapper.insert(employee);
    }

    /**
     * 根据id查询员工
     * @param id
     */
    @Override
    public Employee getById(Long id) {
        Employee employee = employeeMapper.selectById(id);
        employee.setPassword("***");
        return employee;
    }

    /**
     * 分页查询员工
     * @param employeePageQueryDTO
     */
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        //1、根据条件查询数据库中的数据
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize());
        Page<Employee> employeeList = employeeMapper.pageQuery(employeePageQueryDTO);
        return new PageResult(employeeList.getTotal(), employeeList.getResult());
    }

    /**
     * 更新员工信息
     * @param employeeDTO
     */
    @Override
    public void update(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);
        employeeMapper.update(employee);
    }

    /**
     * 启用禁用员工账号
     * @param status
     * @param id
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Employee employee = employeeMapper.selectById(id);
        if (employee == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        employee.setStatus(status);
        employeeMapper.update(employee);
    }
}
