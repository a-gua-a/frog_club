package com.frog.server.controller.admin;

import com.frog.common.result.PageResult;
import com.frog.common.result.Result;
import com.frog.pojo.dto.ServicesDTO;
import com.frog.pojo.dto.ServicesPageQueryDTO;
import com.frog.pojo.entity.Services;
import com.frog.pojo.vo.ServicesVO;
import com.frog.server.mapper.ServicesMapper;
import com.frog.server.service.ServicesService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/services")
@Slf4j
public class ServicesController {

    @Autowired
    private ServicesService servicesService;
    @Autowired
    private ServicesMapper servicesMapper;

    /**
     * 根据id查询服务
     */
    @GetMapping("/{id}")
    public Result<ServicesVO> getById(@PathVariable Long id) {
        ServicesVO servicesVO = servicesService.getById(id);
        return Result.success(servicesVO);
    }

    /**
     * 新增服务
     * @param servicesDTO
     */
    @PostMapping("/save")
    public Result save(@RequestBody ServicesDTO servicesDTO) {
        log.info("新增服务:{}", servicesDTO);
        servicesService.save(servicesDTO);
        return Result.success();
    }

    /**
     * 更新服务
     *
     * @param servicesDTO
     */
    @PostMapping("/update")
    @CacheEvict(value = {"servicesCache"}, allEntries = true)
    public Result update(@RequestBody ServicesDTO servicesDTO) {
        log.info("更新服务:{}", servicesDTO);
        servicesService.update(servicesDTO);
        return Result.success();
    }

    /**
     * 删除服务
     * @param ids 服务id列表
     * @return
     */
    @DeleteMapping()
    public Result deleteBatch(@RequestParam List<Long> ids) {
        log.info("删除服务:{}", ids);
        servicesService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 分页查询服务
     * @param servicesPageQueryDTO
     * @return
     */
    @GetMapping("/page")
    public Result<PageResult> page(ServicesPageQueryDTO servicesPageQueryDTO) {
        log.info("分页查询服务:{}", servicesPageQueryDTO);
        PageResult pageResult = servicesService.page(servicesPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 根据分类id查询服务
     * @param categoryId 分类id
     * @return
     */
    @GetMapping("/list")
    public Result<List<Services>> getByCategoryId(@RequestParam Long categoryId) {
        log.info("根据分类id查询服务:{}", categoryId);
        List<Services> servicesList = servicesService.getByCategoryId(categoryId);
        return Result.success(servicesList);
    }

    /**
     * 更新服务状态
     * @param status 状态
     * @param id 服务id
     * @return
     */
    @PostMapping("/status/{status}")
    @CacheEvict(value = {"servicesCache"}, allEntries = true)
    public Result updateStatus(@PathVariable Integer status, @RequestBody Long id) {
        log.info("更新服务状态:{}", status);
        Services services = servicesMapper.selectById(id);
        services.setStatus(status);
        servicesMapper.update(services);
        return Result.success();
    }
}
