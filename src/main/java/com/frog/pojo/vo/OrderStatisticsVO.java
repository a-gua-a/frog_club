package com.frog.pojo.vo;

import lombok.Data;

import java.io.Serializable;

@Data
public class OrderStatisticsVO implements Serializable {
    //待接单数量
    private Integer toBeConfirmed;

    //进行中数量
    private Integer inProgress;

    //已完成数量
    private Integer completed;
}
