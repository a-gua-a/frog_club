package com.frog.server.controller.admin;


import com.frog.common.result.PageResult;
import com.frog.common.result.Result;
import com.frog.pojo.dto.PackageDTO;
import com.frog.pojo.dto.PackagePageQueryDTO;
import com.frog.server.service.PackageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/package")
public class PackageController {

    @Autowired
    private PackageService packageService;

    /**
     * 新增套餐
     */
    @PostMapping
    public Result insert(@RequestBody PackageDTO packageDTO) {
        packageService.insert(packageDTO);
        return Result.success();
    }

    /**
     * 批量删除套餐
     */
    @DeleteMapping
    public Result delete(@RequestBody List<Long> ids) {
        packageService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 套餐起售/停售
     */
    @PostMapping("/status/{status}")
    public Result updateStatus(@PathVariable Integer status, @RequestParam Long id) {
        packageService.updateStatus(status, id);
        return Result.success();
    }

    /**
     * 更新套餐
     */
    @PutMapping
    public Result update(@RequestBody PackageDTO packageDTO) {
        packageService.update(packageDTO);
        return Result.success();
    }

    /**
     * 分页查询套餐
     */
    @GetMapping("/page")
    public Result<PageResult> pageQuery(PackagePageQueryDTO packagePageQueryDTO) {
        PageResult pageResult = packageService.page(packagePageQueryDTO);
        return Result.success(pageResult);
    }
}
