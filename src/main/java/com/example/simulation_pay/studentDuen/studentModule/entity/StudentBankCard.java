package com.example.simulation_pay.studentDuen.studentModule.entity;

    import java.io.Serializable;
    import lombok.Data;
    import lombok.EqualsAndHashCode;
    import lombok.experimental.Accessors;

/**
* <p>
    * 银行卡表
    * </p>
*
* @author 龚某鑫
* @since 2021-07-02
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class StudentBankCard implements Serializable {

    private static final long serialVersionUID = 1L;

            /**
            * 用户外键UUID
            */
    private String userUuid;

            /**
            * 银行卡卡号
            */
    private String bankCard;

            /**
            * 银行卡是否解绑
            */
    private String isUntie;
}
