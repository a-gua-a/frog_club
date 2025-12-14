package com.frog.pojo.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class PreOrderDTO implements Serializable {
    private Long serverId;

    private Long packageId;

    private String serviceDetail;
}
