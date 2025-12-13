package com.frog.pojo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class EmployeeDTO {
    //用户名
    private String username;

    //姓名
    private String name;

    private String phone;

    private String sex;

    private String idNumber;
}
