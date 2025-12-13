package com.frog.server.controller.admin;

import com.frog.common.result.Result;
import com.frog.pojo.vo.ServicesVO;
import com.frog.server.service.ServicesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin/services")
@Slf4j
public class ServicesController {

    @Autowired
    private ServicesService servicesService;

    /**
     * 根据id查询服务
     */
    @GetMapping("/{id}")
    public Result<ServicesVO> getById(@PathVariable Long id) {
        ServicesVO servicesVO = servicesService.getById(id);
        return Result.success(servicesVO);
    }
}
