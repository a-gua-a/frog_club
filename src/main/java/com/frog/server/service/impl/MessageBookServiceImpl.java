package com.frog.server.service.impl;

import com.frog.common.context.BaseContext;
import com.frog.server.mapper.MessageBookMapper;
import com.frog.server.service.MessageBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.frog.pojo.entity.MessageBook;

import java.util.List;

@Service
@Slf4j
public class MessageBookServiceImpl implements MessageBookService {
    @Autowired
    private MessageBookMapper messageBookMapper;

    /**
     * 条件查询
     *
     * @param messageBook
     * @return
     */
    @Override
    public List<MessageBook> list(MessageBook messageBook) {
        return messageBookMapper.list(messageBook);
    }

    /**
     * 新增信息
     *
     * @param messageBook
     */
    @Override
    public void save(MessageBook messageBook) {
        messageBook.setUserId(BaseContext.getCurrentId());
        messageBook.setIsDefault(0);
        messageBookMapper.insert(messageBook);
    }

    /**
     * 根据id查询
     *
     * @param id
     * @return
     */
    @Override
    public MessageBook getById(Long id) {
        MessageBook messageBook = messageBookMapper.getById(id);
        return messageBook;
    }

    /**
     * 根据id修改信息
     *
     * @param messageBook
     */
    @Override
    public void update(MessageBook messageBook) {
        messageBookMapper.update(messageBook);
    }

    /**
     * 设置默认信息
     *
     * @param messageBook
     */
    @Transactional
    @Override
    public void setDefault(MessageBook messageBook) {
        //1、将当前用户的所有信息修改为非默认地址 update message_book set is_default = ? where user_id = ?
        messageBook.setIsDefault(0);
        messageBook.setUserId(BaseContext.getCurrentId());
        messageBookMapper.updateIsDefaultByUserId(messageBook);

        //2、将当前信息改为默认地址 update message_book set is_default = ? where id = ?
        messageBook.setIsDefault(1);
        messageBookMapper.update(messageBook);
    }

    /**
     * 根据id删除地址
     *
     * @param id
     */
    public void deleteById(Long id) {
        messageBookMapper.deleteById(id);
    }

}
