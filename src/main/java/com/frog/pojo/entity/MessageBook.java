package com.frog.pojo.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 地址簿
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MessageBook implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    //用户id
    private Long userId;

    //打手id
    private Long employeeId;

    //手机号
    private String phone;

    //性别 0 女 1 男
    private String sex;

    //标签
    private String label;

    //是否默认 0否 1是
    private Integer isDefault;
}
