package com.frog.pojo.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PackageService implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    //一条龙id
    private Long packageId;

    //服务id
    private Long serviceId;

    //服务名称 （冗余字段）
    private String name;

    //服务原价
    private BigDecimal price;

    //份数
    private Integer copies;

}
