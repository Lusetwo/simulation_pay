package com.example.simulation_pay.common.interceptor;

import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.example.simulation_pay.config.JwtConfig;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * ToKen过滤器
 */
@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private JwtConfig jwtConfig;

    @Override
    public boolean preHandle(HttpServletRequest request,
                             HttpServletResponse response,
                             Object handler) throws IOException {
        /**
         * 地址过滤
         * */
        String uri = request.getRequestURI() ;
        System.out.println("访问路径:"+uri);
        if (uri.contains("/studentModule/student/login")
                || uri.contains("/api/adminModule/goods/QueryGoodsBy")
                || uri.contains("/api/adminModule/teacher-class/getTeacherList")
                || uri.contains("/api/adminModule/goods-sort/getGoodsSortList")
                || uri.contains("/api/adminModule/goods/saveGoods")
                || uri.contains("/api/adminModule/goods/getGoodsList")
                || uri.contains("/api/adminModule/goods/updateGoods")
                || uri.contains("/api/adminModule/goods-sort/deleteGoodsSort")
                || uri.contains("/api/adminModule/admin/adminLogin")
                || uri.contains("/api/studentModule/student/login")
                || uri.contains("/api/studentModule/student/saveUser")
                || uri.contains("/api/teacherModule/teacher/saveTeacherUser")
                || uri.contains("/api/teacherModule/teacher/teacherLogin")
                || uri.contains("/api/SystemModule/system-version/**")
                || uri.contains("/api/QRcodeMoudle/upload")
            ){
            return true;
        }

        /** Token 验证 */
        String token=null;
        try {
            token=request.getHeader(jwtConfig.getHeader());
        }catch (NullPointerException e){
            response.getWriter().write("Token is Null。");
            return false;
        }
        if(StringUtils.isEmpty(token)){
            token = request.getParameter(jwtConfig.getHeader());
        }
        if(StringUtils.isEmpty(token)){
            response.getWriter().write("token is NULL");
            return false;
        }

        Claims claims = null;
        try{
            claims = jwtConfig.getTokenClaim(token);
            if(claims == null || jwtConfig.isTokenExpired(claims.getExpiration())){
                response.getWriter().write("token is invalid , Please ReLogin.");
                return false;
            }
        }catch (Exception e){
            response.getWriter().write("token is Invalid , Please ReLogin");
            return false;
        }
        /**
         * 菜单访问
         * 学生(访问的菜单)
         * */

        /** 设置 identityId 用户身份ID */
        request.setAttribute("identityId", claims.getSubject());
        return true;
    }

}
