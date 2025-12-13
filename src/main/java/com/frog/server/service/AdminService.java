package com.frog.server.service;

import com.frog.pojo.dto.AdminLoginDTO;
import com.frog.pojo.vo.AdminLoginVO;


public interface AdminService {

    AdminLoginVO login(AdminLoginDTO adminLoginDTO);
}
