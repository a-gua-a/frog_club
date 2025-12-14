package com.frog.pojo.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class OrdersSubmitDTO implements Serializable {

    //付款方式
    private int payMethod;
    //备注
    private String remark;

    //总金额
    private BigDecimal amount;
}
