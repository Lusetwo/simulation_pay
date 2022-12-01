package com.example.simulation_pay.manageDuen.adminModule.entity;

    import java.io.Serializable;

    import com.example.simulation_pay.common.baseEntity.BaseEntity;
    import lombok.Data;
    import lombok.EqualsAndHashCode;
    import lombok.experimental.Accessors;

/**
* <p>
    * 
    * </p>
*
* @author 龚某鑫
* @since 2021-07-02
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class Classes extends BaseEntity {

    private static final long serialVersionUID = 1L;

            /**
            * 班级主键ID
            */
    private String classUuid;

            /**
            * 班级名称
            */
    private String className;


    private String classDirector;
            /**
            * 是否可用(0/1)
            */
    private Integer delFlag;


}
