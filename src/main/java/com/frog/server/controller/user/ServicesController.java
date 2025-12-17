package com.frog.server.controller.user;

import com.frog.common.constant.StatusConstant;
import com.frog.common.result.Result;
import com.frog.pojo.entity.Services;
import com.frog.pojo.vo.ServicesVO;
import com.frog.server.service.ServicesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userDishController")
@RequestMapping("/user/services")
@Slf4j
public class ServicesController {
    @Autowired
    private ServicesService servicesService;

    /**
     * 根据分类id查询菜品
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    @Cacheable(value = "dishCategoryCache", key = "#categoryId")
    public Result<List<ServicesVO>> list(Long categoryId) {
        Services services = new Services();
        services.setCategoryId(categoryId);
        services.setStatus(StatusConstant.ENABLE);//查询起售中的菜品

        List<ServicesVO> list = servicesService.listWithDetail(services);

        return Result.success(list);
    }

}
