package com.example.simulation_pay.manageDuen.adminModule.entity;

    import java.time.LocalDate;
    import java.io.Serializable;
    import lombok.Data;
    import lombok.EqualsAndHashCode;
    import lombok.experimental.Accessors;

/**
* <p>
    * 管理员表
    * </p>
*
* @author 龚某鑫
* @since 2021-07-02
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class Admin implements Serializable {

    private static final long serialVersionUID = 1L;

            /**
            * 用户外键uuid
            */
    private String userUuid;

            /**
            * 管理员名称
            */
    private String adminName;

            /**
            * 管理员密码
            */
    private String adminPassword;

            /**
            * 管理员头像
            */
    private String adminImg;

            /**
            * 角色标识
            */
    private Integer adminRole;

            /**
            * 注册时间
            */
    private LocalDate createDate;
}
