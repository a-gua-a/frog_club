package com.frog.server.mapper;

import com.frog.pojo.entity.MessageBook;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface MessageBookMapper {

    /**
     * 条件查询
     * @param messageBook
     * @return
     */
    List<MessageBook> list(MessageBook messageBook);

    /**
     * 新增
     * @param messageBook
     */
    @Insert("insert into message_book" +
            "(user_id, employee_id, phone, sex, label, is_default)" +
            "values (#{userId}, #{employeeId}, #{phone}, #{sex}, #{label}, #{isDefault})")
    void insert(MessageBook messageBook);

    /**
     * 根据id查询
     * @param id
     * @return
     */
    @Select("select * from message_book where id = #{id}")
    MessageBook getById(Long id);

    /**
     * 根据id修改
     * @param messageBook
     */
    void update(MessageBook messageBook);

    /**
     * 根据 用户id修改 是否默认地址
     * @param messageBook
     */
    @Update("update message_book set is_default = #{isDefault} where user_id = #{userId}")
    void updateIsDefaultByUserId(MessageBook messageBook);

    /**
     * 根据id删除地址
     * @param id
     */
    @Delete("delete from message_book where id = #{id}")
    void deleteById(Long id);

}
