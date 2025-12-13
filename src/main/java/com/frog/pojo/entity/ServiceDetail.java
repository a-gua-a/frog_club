package com.frog.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ServiceDetail implements Serializable {
    private static final long serialVersionUID = 1L;

    private Long id;

    //服务id
    private Long serviceId;

    //服务详情描述list
    private String value;
}
