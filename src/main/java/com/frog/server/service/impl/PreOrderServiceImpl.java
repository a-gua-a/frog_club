package com.frog.server.service.impl;

import com.frog.common.context.BaseContext;
import com.frog.pojo.dto.PreOrderDTO;
import com.frog.pojo.entity.Pack;
import com.frog.pojo.entity.PreOrder;
import com.frog.pojo.entity.Services;
import com.frog.server.mapper.PackageMapper;
import com.frog.server.mapper.PreOrderMapper;
import com.frog.server.mapper.ServicesMapper;
import com.frog.server.service.PreOrderService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PreOrderServiceImpl implements PreOrderService {

    @Autowired
    private PreOrderMapper preOrderMapper;
    @Autowired
    private ServicesMapper servicesMapper;
    @Autowired
    private PackageMapper packageMapper;



    /**
     * 查看购物车
     * @return
     */
    @Override
    public List<PreOrder> list() {
        Long id = BaseContext.getCurrentId();
        PreOrder preOrder = new PreOrder();
        preOrder.setUserId(id);
        List<PreOrder> list = preOrderMapper.list(preOrder);
        return list;
    }

    /**
     * 添加购物车
     * @param preOrderDTO
     */
    @Override
    public void add(PreOrderDTO preOrderDTO) {
        Long id = BaseContext.getCurrentId();
        PreOrder preOrder = new PreOrder();
        BeanUtils.copyProperties(preOrderDTO, preOrder);
        preOrder.setUserId(id);
        List<PreOrder> list = preOrderMapper.list(preOrder);
        if (list != null && list.size() > 0) {
            PreOrder cart = list.get(0);
            cart.setNumber(cart.getNumber() + 1);
            preOrderMapper.update(cart);
        } else {
            preOrder.setNumber(1);
            preOrder.setCreateTime(LocalDateTime.now());
            if(preOrder.getServiceId()!=null){
                Services service = servicesMapper.selectById(preOrder.getServiceId());
                preOrder.setImage(service.getImage());
                preOrder.setAmount(service.getPrice());
                preOrder.setName(service.getName());
            }else{
                Pack pack = packageMapper.getById(preOrder.getPackageId());
                preOrder.setImage(pack.getImage());
                preOrder.setAmount(pack.getPrice());
                preOrder.setName(pack.getName());
            }
            preOrderMapper.insert(preOrder);
        }
    }

    @Override
    public void clean() {
        Long id = BaseContext.getCurrentId();
        PreOrder preOrder = new PreOrder();
        preOrder.setUserId(id);
        preOrderMapper.delete(preOrder);
    }

    @Override
    public void sub(PreOrderDTO preOrderDTO) {
        Long id = BaseContext.getCurrentId();
        PreOrder preOrder = new PreOrder();
        BeanUtils.copyProperties(preOrderDTO, preOrder);
        preOrder.setUserId(id);
        List<PreOrder> list = preOrderMapper.list(preOrder);
        if (list != null && list.size() > 0) {
            PreOrder cart = list.get(0);
            cart.setNumber(cart.getNumber() - 1);
            if (cart.getNumber() <= 0) {
                preOrderMapper.delete(cart);
            } else {
                preOrderMapper.update(cart);
            }
        }
    }
}
