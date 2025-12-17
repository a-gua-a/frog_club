package com.frog.server.controller.user;

import com.frog.common.constant.StatusConstant;
import com.frog.common.result.Result;
import com.frog.pojo.entity.Pack;
import com.frog.pojo.vo.ServiceItemVO;
import com.frog.server.service.PackageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController("userPackageController")
@RequestMapping("/user/package")
@Slf4j
public class PackageController {
    @Autowired
    private PackageService packageService;

    /**
     * 条件查询
     *
     * @param categoryId
     * @return
     */
    @GetMapping("/list")
    public Result<List<Pack>> list(Long categoryId) {
        Pack pack = new Pack();
        pack.setCategoryId(categoryId);
        pack.setStatus(StatusConstant.ENABLE);

        List<Pack> list = packageService.list(pack);
        return Result.success(list);
    }

    /**
     * 根据套餐id查询包含的菜品列表
     *
     * @param id
     * @return
     */
    @GetMapping("/dish/{id}")
    public Result<List<ServiceItemVO>> serviceItemList(@PathVariable("id") Long id) {
        List<ServiceItemVO> list = packageService.getServiceItemById(id);
        return Result.success(list);
    }
}
