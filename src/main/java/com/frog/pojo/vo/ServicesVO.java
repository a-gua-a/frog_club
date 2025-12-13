package com.frog.pojo.vo;

import com.frog.pojo.entity.PackageService;
import com.frog.pojo.entity.ServiceDetail;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ServicesVO {

    private int id;

    private String name;

    private Long categoryId;

    private BigDecimal price;

    private String image;

    private String description;
    //0 停售 1 起售
    private Integer status;
    //更新时间
    private LocalDateTime updateTime;
    //分类名称
    private String categoryName;
    //菜品关联的口味
    private List<ServiceDetail> serviceDetails = new ArrayList<>();
}
