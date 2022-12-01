package com.example.simulation_pay.config;

import com.example.simulation_pay.common.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Autowired
    private TokenInterceptor tokenInterceptor;

    //  将拦截器对象添加到拦截器注册器中
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/**")
                /**
                 * 拦截器白名单
                 * 以下地址被访问时不会被拦截
                 * */
                .excludePathPatterns("/upload/**")
                .excludePathPatterns("/error")
                .excludePathPatterns("/doc.html")
                .excludePathPatterns("/favicon.ico")
                .excludePathPatterns("/v2/api-docs")
                .excludePathPatterns("/api/adminModule/student/updateStudent2")
                .excludePathPatterns("/api/studentModule/student/updateStudentPassword")
                .excludePathPatterns("/api/teacherModule/teacher/updateTeacherPassword")
                .excludePathPatterns("/api/teacherModule/teacher/queryUserPoint")
                .excludePathPatterns("/api/SystemModule/sys-version/**")
                .excludePathPatterns("/api/adminModule/orderAdmin/OrderListExcel")
                .excludePathPatterns("/swagger-resources/**","/webjars/**","/v2/**","/swagger-ui.html/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        System.out.println("配置文件已经生效");
        //关于图片上传后需要重启服务器才能刷新图片
        //这是一种保护机制，为了防止绝对路径被看出来，目录结构暴露
        //解决方法:将虚拟路径/images/
        // 向绝对路径 (D:\\Java学习\\springboot小滴\\springboot项目\\upload\\src\\main\\resources\\static\\images\\)映射
//        String path="D:\\git\\simulated-payment-java\\simulation_pay\\upload\\";
//        registry.addResourceHandler("/upload/**").addResourceLocations("file:"+path);
        String property = System.getProperty("user.dir");
        registry.addResourceHandler("/upload/**").addResourceLocations("file:"+property+"/upload/");
    }
}

