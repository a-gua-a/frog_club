package com.frog.server.service;


import com.frog.pojo.vo.BusinessDataVO;
import com.frog.pojo.vo.OrderOverViewVO;
import com.frog.pojo.vo.PackageOverViewVO;
import com.frog.pojo.vo.ServicesOverViewVO;

import java.time.LocalDateTime;

public interface WorkspaceService {

    /**
     * 根据时间段统计营业数据
     * @param begin
     * @param end
     * @return
     */
    BusinessDataVO getBusinessData(LocalDateTime begin, LocalDateTime end);

    /**
     * 查询订单管理数据
     * @return
     */
    OrderOverViewVO getOrderOverView();

    /**
     * 查询服务总览
     * @return
     */
    ServicesOverViewVO getServicesOverView();

    /**
     * 查询套餐总览
     * @return
     */
    PackageOverViewVO getPackageOverView();

}
