package com.example.simulation_pay.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * 1，Swagger2API接口文档生成的配置
 * 2，Knife4j用于对Swagger2进行接口文档服务
 */
@Configuration //这是一个配置类
@EnableSwagger2 //启用Swagger2
public class Swagger2Config {
//    Swagger2的配置
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.simulation_pay"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("SpringBoot2-项目骨架")
                .description("SpringBoot2项目骨架的API接口生成文档，用于后端接口调试")
                .version("1.0")
                .build();
    }

//    Knife4j的配置
    @Bean(value = "defaultApi2")
    public Docket defaultApi2() {
        Docket docket=new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(new ApiInfoBuilder()
                        .title("模拟支付app接口文档")
                        .description("SpringBoot2项目骨架的API接口生成文档，用于后端接口调试")
                        .version("1.0")
                        .build())
                //分组名称
                .groupName("2.X版本")
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.example.simulation_pay"))
                .paths(PathSelectors.any())
                .build();
        return docket;
    }
}
