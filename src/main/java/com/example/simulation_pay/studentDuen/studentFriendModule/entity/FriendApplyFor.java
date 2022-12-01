package com.example.simulation_pay.studentDuen.studentFriendModule.entity;

    import java.time.LocalDate;
    import java.io.Serializable;
    import lombok.Data;
    import lombok.EqualsAndHashCode;
    import lombok.experimental.Accessors;

/**
* <p>
    * 好友申请表
    * </p>
*
* @author 龚某鑫
* @since 2021-07-02
*/
    @Data
        @EqualsAndHashCode(callSuper = false)
    @Accessors(chain = true)
    public class FriendApplyFor implements Serializable {

    private static final long serialVersionUID = 1L;

            /**
            * 被申请人ID
            */
    private String userUuid;

            /**
             * 申请记录唯一标识
             * */
    private String uuid;

            /**
            * 申请人ID
            */
    private String applyForId;

            /**
            * 申请状态 0为未申请 1为已申请
            */
    private String applyForStruts;

            /**
            * 删除标记
            */
    private Integer delFlag;

            /**
            * 申请时间
            */
    private LocalDate applyDate;


}
