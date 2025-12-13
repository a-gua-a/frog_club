package com.frog.server.mapper;

import com.frog.common.enumeration.OperationType;
import com.frog.pojo.dto.EmployeePageQueryDTO;
import com.frog.pojo.entity.Employee;
import com.frog.server.annotation.AutoFill;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {
        /**
         * 新增员工
         * @param employee
         */
        @AutoFill(OperationType.INSERT)
        @Insert("insert into employee (name, username, password, phone, sex,online, id_number, create_time, update_time, create_user, update_user) " +
                "values (#{name}, #{username}, #{password}, #{phone}, #{sex}, #{online}, #{idNumber}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
        void insert(Employee employee);

        /**
         * 根据id查询员工
         * @param id
         */
        @Select("select * from employee where id = #{id}")
        Employee selectById(Long id);

        /**
         * 分页查询员工
         * @param employeePageQueryDTO
         */
        Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

        /**
         * 更新员工信息
         * @param employee
         */
        @AutoFill(OperationType.UPDATE)
        void update(Employee employee);
}
