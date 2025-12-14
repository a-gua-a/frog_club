package com.frog.pojo.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceItemVO implements Serializable {

    //服务名称
    private String name;

    //份数
    private Integer copies;

    //图片
    private String image;

    //描述
    private String description;
}
