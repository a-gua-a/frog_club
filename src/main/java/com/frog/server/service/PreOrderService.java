package com.frog.server.service;

import com.frog.pojo.dto.PreOrderDTO;
import com.frog.pojo.entity.PreOrder;
import java.util.List;

public interface PreOrderService {
     /**
      * 查看预订单
      */
     List<PreOrder> list();

     /**
      * 添加预订单
      */
     void add(PreOrderDTO preOrderDTO);

     /**
      * 清空预订单
      */
     void clean();

     /**
      * 删除预订单
      * @param preOrderDTO
      */
     void sub(PreOrderDTO preOrderDTO);
}
