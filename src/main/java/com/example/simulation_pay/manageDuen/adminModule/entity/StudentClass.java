package com.example.simulation_pay.manageDuen.adminModule.entity;

    import java.time.LocalDate;
    import java.io.Serializable;
    import java.util.Date;

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
    public class StudentClass extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private String studentUuid;

    private String classUuid;
            /**
            * 是否(1/0)可用
            */
    private Integer delFlag;

    private Date createDate;


}
