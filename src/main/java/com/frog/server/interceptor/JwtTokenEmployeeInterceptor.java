package com.frog.server.interceptor;

import com.frog.common.constant.JwtClaimsConstant;
import com.frog.common.context.BaseContext;
import com.frog.common.properties.JwtProperties;
import com.frog.common.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class JwtTokenEmployeeInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 校验jwt
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断当前拦截到的是Controller的方法还是其他资源
        if (!(handler instanceof HandlerMethod)) {
            //当前拦截到的不是动态方法，直接放行
            return true;
        }

        //1、从请求头中获取令牌
        String token = request.getHeader(jwtProperties.getEmployeeTokenName());

        //2、校验令牌
        try {
            log.info("jwt校验:{}", token);
            Claims claims = JwtUtil.parseJWT(jwtProperties.getEmployeeSecretKey(), token);
            Long employeeId = Long.valueOf(claims.get(JwtClaimsConstant.EMP_ID).toString());
            //5、将当前员工id存入ThreadLocal
            BaseContext.setCurrentId(employeeId);
            log.info("当前员工id：{}", employeeId);
            //3、通过，放行
            return true;
        } catch (Exception ex) {
            log.info("jwt校验失败:{}", ex.getMessage());
            //4、不通过，响应401状态码
            response.setStatus(401);
            return false;
        }
    }

}
