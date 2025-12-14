package com.frog.server.service.impl;

import com.frog.common.result.PageResult;
import com.frog.pojo.dto.PackageDTO;
import com.frog.pojo.dto.PackagePageQueryDTO;
import com.frog.pojo.entity.PackageServices;
import com.frog.pojo.vo.ServiceItemVO;
import com.frog.server.mapper.PackageMapper;
import com.frog.server.mapper.PackageServiceMapper;
import com.frog.server.service.PackageService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.frog.pojo.entity.Pack;
import java.util.List;

@Service
public class PackageServiceImpl implements PackageService {

    @Autowired
    private PackageMapper packageMapper;
    @Autowired
    private PackageServiceMapper packageServiceMapper;

    /**
     * 新增套餐
     * @param packageDTO
     */
    @Override
    public void insert(PackageDTO packageDTO) {
        // 转换为实体类
        Pack pack = new Pack();
        BeanUtils.copyProperties(packageDTO, pack);
        // 新增套餐
        packageMapper.insert(pack);
        //更新关联的服务
        List<PackageServices> serviceList = packageDTO.getPackageServices();
        serviceList.forEach(service -> {
            service.setPackageId(pack.getId());
            packageServiceMapper.insert(service);
        });
    }

    /**
     * 批量删除套餐
     * @param ids
     */
    @Override
    public void deleteBatch(List<Long> ids) {
        // 删除套餐
        packageMapper.deleteBatch(ids);
        // 删除套餐服务
        ids.forEach(id -> {
            packageServiceMapper.deleteByPackageId(id);
        });
    }

    /**
     * 更新套餐状态
     * @param status
     * @param id
     */
    @Override
    public void updateStatus(Integer status, Long id) {
        // 更新套餐状态
        Pack pack = packageMapper.getById(id);
        pack.setStatus(status);
        packageMapper.update(pack);
    }

    /**
     * 更新套餐
     * @param packageDTO
     */
    @Override
    public void update(PackageDTO packageDTO) {
        // 转换为实体类
        Pack pack = new Pack();
        BeanUtils.copyProperties(packageDTO, pack);
        // 更新套餐
        packageMapper.update(pack);
        // 删除原有的关联服务
        packageServiceMapper.deleteByPackageId(packageDTO.getId());
        // 更新关联的服务
        List<PackageServices> serviceList = packageDTO.getPackageServices();
        serviceList.forEach(service -> {
            packageServiceMapper.insert(service);
        });
    }

    /**
     * 分页查询套餐
     */
    @Override
    public PageResult page(PackagePageQueryDTO packagePageQueryDTO) {
        PageHelper.startPage(packagePageQueryDTO.getPage(), packagePageQueryDTO.getPageSize());
        Pack pack = new Pack();
        BeanUtils.copyProperties(packagePageQueryDTO, pack);
        // 分页查询套餐
        Page<Pack> packList = packageMapper.pageQuery(pack);
        PageResult pageResult = new PageResult();
        pageResult.setTotal(packList.getTotal());
        pageResult.setRecords(packList.getResult());
        return pageResult;
    }

    /**
     * 条件查询
     * @param pack
     * @return
     */
    @Override
    public List<Pack> list(Pack pack) {
        List<Pack> list = packageMapper.list(pack);
        return list;
    }

    /**
     * 根据id查询服务项选项
     * @param id
     * @return
     */
    @Override
    public List<ServiceItemVO> getServiceItemById(Long id) {
        return packageMapper.getServiceItemByPackageId(id);
    }
}
