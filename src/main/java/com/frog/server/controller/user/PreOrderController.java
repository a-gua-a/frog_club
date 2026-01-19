package com.frog.server.controller.user;

import com.frog.common.result.Result;
import com.frog.pojo.dto.PreOrderDTO;
import com.frog.pojo.entity.PreOrder;
import com.frog.server.service.PreOrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user/preOrder")
@Slf4j
public class PreOrderController {

    @Autowired
    private PreOrderService preOrderService;

    /**
     * 查看购物车
     */
    @GetMapping("/list")
    public Result<List<PreOrder>> list() {
        List<PreOrder> list = preOrderService.list();
        return Result.success(list);
    }

    /**
     * 新增购物车
     */
    @PostMapping("/add")
    public Result add(@RequestBody PreOrderDTO preOrderDTO) {
        preOrderService.add(preOrderDTO);
        return Result.success();
    }

    /**
     * 清空购物车
     */
    @DeleteMapping("/clean")
    public Result clean() {
        preOrderService.clean();
        return Result.success();
    }

    /**
     * 减少购物车
     */
    @PostMapping("/sub")
    public Result sub(@RequestBody PreOrderDTO preOrderDTO) {
        preOrderService.sub(preOrderDTO);
        return Result.success();
    }
}
