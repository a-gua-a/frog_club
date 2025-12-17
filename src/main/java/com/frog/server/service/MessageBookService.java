package com.frog.server.service;

import com.frog.pojo.entity.MessageBook;

import java.util.List;

public interface MessageBookService {

    List<MessageBook> list(MessageBook messageBook);

    void save(MessageBook messageBook);

    MessageBook getById(Long id);

    void update(MessageBook messageBook);

    void setDefault(MessageBook messageBook);

    void deleteById(Long id);

}
