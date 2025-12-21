package com.frog.server.controller.admin;

import com.frog.common.result.Result;
import com.frog.pojo.vo.BusinessDataVO;
import com.frog.pojo.vo.OrderOverViewVO;
import com.frog.pojo.vo.PackageOverViewVO;
import com.frog.pojo.vo.ServicesOverViewVO;
import com.frog.server.service.WorkspaceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * 工作台
 */
@RestController
@RequestMapping("/admin/workspace")
@Slf4j
public class WorkSpaceController {

    @Autowired
    private WorkspaceService workspaceService;

    /**
     * 工作台今日数据查询
     * @return
     */
    @GetMapping("/businessData")
    public Result<BusinessDataVO> businessData(){
        //获得当天的开始时间
        LocalDateTime begin = LocalDateTime.now().with(LocalTime.MIN);
        //获得当天的结束时间
        LocalDateTime end = LocalDateTime.now().with(LocalTime.MAX);

        BusinessDataVO businessDataVO = workspaceService.getBusinessData(begin, end);
        return Result.success(businessDataVO);
    }

    /**
     * 查询订单管理数据
     * @return
     */
    @GetMapping("/overviewOrders")
    public Result<OrderOverViewVO> orderOverView(){
        return Result.success(workspaceService.getOrderOverView());
    }

    /**
     * 查询服务总览
     * @return
     */
    @GetMapping("/overviewServices")
    public Result<ServicesOverViewVO> servicesOverView(){
        return Result.success(workspaceService.getServicesOverView());
    }

    /**
     * 查询套餐总览
     * @return
     */
    @GetMapping("/overviewPackages")
    public Result<PackageOverViewVO> packageOverView(){
        return Result.success(workspaceService.getPackageOverView());
    }
}
