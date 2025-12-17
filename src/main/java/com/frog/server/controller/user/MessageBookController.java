package com.frog.server.controller.user;

import com.frog.common.context.BaseContext;
import com.frog.common.result.Result;
import com.frog.pojo.entity.MessageBook;
import com.frog.server.service.MessageBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/messageBook")
public class MessageBookController {

    @Autowired
    private MessageBookService messageBookService;

    /**
     * 查询当前登录用户的所有信息
     *
     * @return
     */
    @GetMapping("/list")
    public Result<List<MessageBook>> list() {
        MessageBook messageBook = new MessageBook();
        messageBook.setUserId(BaseContext.getCurrentId());
        List<MessageBook> list = messageBookService.list(messageBook);
        return Result.success(list);
    }

    /**
     * 新增信息
     *
     * @param messageBook
     * @return
     */
    @PostMapping
    public Result save(@RequestBody MessageBook messageBook) {
        messageBookService.save(messageBook);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result<MessageBook> getById(@PathVariable Long id) {
        MessageBook messageBook = messageBookService.getById(id);
        return Result.success(messageBook);
    }

    /**
     * 根据id修改地址
     *
     * @param messageBook
     * @return
     */
    @PutMapping
    public Result update(@RequestBody MessageBook messageBook) {
        messageBookService.update(messageBook);
        return Result.success();
    }

    /**
     * 设置默认信息
     *
     * @param messageBook
     * @return
     */
    @PutMapping("/default")
    public Result setDefault(@RequestBody MessageBook messageBook) {
        messageBookService.setDefault(messageBook);
        return Result.success();
    }

    /**
     * 根据id删除信息
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable Long id) {
        messageBookService.deleteById(id);
        return Result.success();
    }

    /**
     * 查询默认信息
     */
    @GetMapping("default")
    public Result<MessageBook> getDefault() {
        //SQL:select * from message_book where user_id = ? and is_default = 1
        MessageBook messageBook = new MessageBook();
        messageBook.setIsDefault(1);
        messageBook.setUserId(BaseContext.getCurrentId());
        List<MessageBook> list = messageBookService.list(messageBook);

        if (list != null && list.size() == 1) {
            return Result.success(list.get(0));
        }

        return Result.error("没有查询到默认信息");
    }

}
