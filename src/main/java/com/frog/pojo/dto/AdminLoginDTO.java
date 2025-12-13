package com.frog.pojo.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AdminLoginDTO {

    private String username;
    private String password;

}
