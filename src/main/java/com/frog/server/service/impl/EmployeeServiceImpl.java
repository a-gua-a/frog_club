package com.frog.server.service.impl;

import com.frog.common.constant.JwtClaimsConstant;
import com.frog.common.constant.MessageConstant;
import com.frog.common.constant.PasswordConstant;
import com.frog.common.constant.StatusConstant;
import com.frog.common.context.BaseContext;
import com.frog.common.exception.AccountLockedException;
import com.frog.common.exception.PasswordErrorException;
import com.frog.common.exception.AccountNotFoundException;
import com.frog.common.properties.JwtProperties;
import com.frog.common.result.PageResult;
import com.frog.common.utils.JwtUtil;
import com.frog.pojo.dto.EmployeeDTO;
import com.frog.pojo.dto.EmployeeLoginDTO;
import com.frog.pojo.dto.EmployeePageQueryDTO;
import com.frog.pojo.entity.Employee;
import com.frog.pojo.vo.AdminLoginVO;
import com.frog.pojo.vo.EmployeeLoginVO;
import com.frog.server.mapper.EmployeeMapper;
import com.frog.server.service.EmployeeService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private JwtProperties jwtProperties;

    /**
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
    public void startOrStop(Integer status, Long id){
        Employee employee = employeeMapper.selectById(id);
        if (employee == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        employee.setStatus(status);
        employeeMapper.update(employee);
    }

    /**
     * 员工登录
     */
    @Override
    public EmployeeLoginVO login(EmployeeLoginDTO employeeLoginDTO){
        String username = employeeLoginDTO.getUsername();
        String password = employeeLoginDTO.getPassword();

        //1、根据用户名查询数据库中的数据
        Employee employee = employeeMapper.getByUsername(username);

        //2、处理各种异常情况（用户名不存在、密码不对、账号被锁定）
        if (employee == null) {
            //账号不存在
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }

        //密码比对
        // 后期需要进行md5加密，然后再进行比对
        password=DigestUtils.md5DigestAsHex(password.getBytes());
        if (!password.equals(employee.getPassword())) {
            //密码错误
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        }

        if (employee.getStatus() == StatusConstant.DISABLE) {
            //账号被锁定
            throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);
        }

        //设置员工为在线状态
        employee.setOnline(StatusConstant.ONLINE);
        employeeMapper.update(employee);

        //登录成功后，生成jwt令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getEmployeeSecretKey(),
                jwtProperties.getEmployeeTtl(),
                claims);
        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .username(employee.getUsername())
                .name(employee.getName())
                .token(token)
                .build();
        //3、返回实体对象
        return employeeLoginVO;
    }

    @Override
    public void logout() {
        //1、获取当前员工id
        Long empId = BaseContext.getCurrentId();
        //2、根据员工id更新员工状态为离线
        Employee employee = employeeMapper.selectById(empId);
        if (employee == null) {
            throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        }
        employee.setOnline(StatusConstant.OFFLINE);
        employeeMapper.update(employee);
    }

    @Override
    public void deleteBatch(List<Long> ids) {
        employeeMapper.deleteBatch(ids);
    }

    @Override
    public void delete(Long id) {
        employeeMapper.delete(id);
    }
}
